package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesActualizacionFinanciera;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de una fcierta, 
 * y forzando el cálculo del Vzc2 con tabla generacional. La tabla generacional a utilizar será informada en la nueva tabla de Datos Sepi/Endesa.
 * Este módulo debe distinguir si el titular está en vigor o anulado por motivo distinto a 17
 * @author 
 *
 */
public class ModuloCSP333 implements Modulo {

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP333.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP333;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_MODBETA = ConstantsModulos.CTE_VA_MODBETA.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	
	private static final String CLAVE_VZC2 = ConstantsModulos.CTE_VZC2;
	private static final String CLAVE_VAR_VZC2 = CLAVE_VZC2.concat(CLAVE_MODULO);	
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VZC2H = ConstantsModulos.CTE_VZC2H;
	private static final String CLAVE_VAR_VZC2H = CLAVE_VZC2H.concat(CLAVE_MODULO);
	private static final String CLAVE_VZC2M = ConstantsModulos.CTE_VZC2M;
	private static final String CLAVE_VAR_VZC2M = CLAVE_VZC2H.concat(CLAVE_MODULO);


	
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP333.LOG.isTraceEnabled()) {
				ModuloCSP333.LOG.trace("Inicio de execute en clase ModuloCSP333");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP333
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Fin de la recuperación de los datos que se pasarán a la función moduloCSP333.
			
			//Invocamos a la función de calculo CSP333
			resultado = moduloCSP333(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP333.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP333.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP333.LOG.isTraceEnabled()) {
			ModuloCSP333.LOG.trace("Fin de execute en clase ModuloCSP333");
		}
		
		return resultado;

	}

	/** 
	 * Es una variación del módulo CSP363, pero partiendo desde la fcal en vez de una fcierta, 
	 * y forzando el cálculo del Vzc2 con tabla generacional. La tabla generacional a utilizar será informada en la nueva tabla de Datos Sepi/Endesa.
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloCSP333(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		String varCriterFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varM;
		Integer varW;
		Timestamp varFechaEfecto = null;
		BigDecimal csp333 = BigDecimal.ZERO;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		Integer varAnoNac;
		String varTabVZC2;
		Umic varUmic2 = null;
		DetalleBaseTecnica varBtcUmic2;
		String VZC2;
		Timestamp varfecJ;
		Timestamp varfecJ1 = null;
		String inicioKbencon;
		Modulo moduloCSP238 = null;
		Modulo moduloVZC = null;
		BigDecimal varCSP238 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyVzc2;
		List<DetalleCorriente> varProyCsp238;
		BigDecimal varTcyVida = BigDecimal.ZERO;
		BigDecimal varVVida = BigDecimal.ZERO;
		BigDecimal varVzc2 = BigDecimal.ZERO;
		BigDecimal varJ1 = BigDecimal.ZERO;
		BigDecimal varI1PorcentajeMasUno;
		BigDecimal varI2PorcentajeMasUno;
		BigDecimal aux = BigDecimal.ZERO;
		String tablaVZC2 = null;
		String varTabVZC2H = null;
		String varTabVZC2M = null;
		//Fin variables locales
		
		if (ModuloCSP333.LOG.isTraceEnabled()) {
			ModuloCSP333.LOG.trace("Inicio función << moduloCSP333 >> de la clase ModuloCSP333, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return csp333;
		}
		
		varI1 = btcUmic.getItcalc().get(0);
		if (btcUmic.getBt().equals("ROSSP") || btcUmic.getBt().equals("ROSSPCSM") || btcUmic.getBt().equals("ROSSPTI") || btcUmic.getBt().equals("ROSSPTE") || btcUmic.getBt().equals("ROSSPGA"))
			varI2 = umic.getBti().getPintertecnI2();
		else {
			varI2 = btcUmic.getItcalc().get(1);
		}
		varI1PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
		varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
		
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
	
		varCriterFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFecha);
			// Fin de la validación de las variables de apoyo.
		}
		
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varFechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFecha);

		varAnoNac = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1());
		
		varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac, umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		
		varProyVzc2 = proyUmic;
		varProyCsp238 = proyUmic;
		
		varfecJ = proyUmic.get(iteracion - 1).getFechaDesde();
		
		inicioKbencon = umic.getDatosGenerales().getKbencon().substring(0, 2);
		if(codSubproceso.equals(ConstantsModulos.CTE_PROY_VIDA)){
			if(inicioKbencon.equals(ConstantsModulos.CTE_30)){

				moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
				varCSP238 = (BigDecimal) moduloCSP238.execute(varProyCsp238, bloqueCorriente, iteracion, fcalc, umic, btcUmic,mapVariables,codSubproceso);
				csp333 = varCSP238;

			}else{

				return csp333;

			}
		}
		
		if(codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL)){

			if(inicioKbencon.substring(0, 1).equals(ConstantsModulos.CTE_3)){

				return csp333;
				
			}else{
				BloqueCorriente bloqCorriente = proyUmic.get(iteracion-1).getBloqueVida();

				varTcyVida = FuncionesAuxiliares.nAnnos(varFechaEfecto, proyUmic.get(iteracion-1).getFechaDesde(), varCriterFecha);

				int iteracionBucleModulos = iteracion;
				
				if (umic.getAsegurados().getFnacAseg2() == null || umic.getAsegurados().getCsexAseg2() == null || umic.getAsegurados().getEdadAseg2() == null) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);
				}else{
			
					if(umic.getAsegurados().getCsexAseg2().equals(ConstantsModulos.CTE_SX_H)){
					
						varTabVZC2 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_VZC2H, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_VZC2H);
					
						if(null == varTabVZC2){

							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_VZC2H});

						}
					
					}else if(umic.getAsegurados().getCsexAseg2().equals(ConstantsModulos.CTE_SX_M)){
					
						varTabVZC2 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_VZC2M, umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(), umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_VZC2M);
					
						if(null == varTabVZC2){

							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_VZC2M});

						}
					
					}
					else{
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G2);
					}
				}
			
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
				if(null == varUmic2){
					varUmic2 = new Umic();
					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varUmic2, umic);
					
					} catch (Exception e) {
						ModuloCSP333.LOG.error(e.getMessage());
					}
					Asegurados aseg = new Asegurados();
					aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
					aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
					aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
					varUmic2.setAsegurados(aseg);
			
					mapVariables.put(CLAVE_UMIC2, varUmic2);
				
					}
			
				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
				if(null == varBtcUmic2){
					varBtcUmic2 = new DetalleBaseTecnica();
				
					try {
						//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
					
					} catch (Exception e) {
						ModuloCSP333.LOG.error(e.getMessage());
					}
				
					varBtcUmic2.setTablacalc1aseg1(varTabVZC2);
				
					if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) 
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)) 
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM))   
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN))  
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))){
						if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
						}
					
						List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
						btcUmic.getTablasConversionAsegurado().get(1).get(0).setTablaInicio(varTabVZC2);
						btcUmic.getTablasConversionAsegurado().get(1).get(0).setTablaFin(varTabVZC2);
						tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

						varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(Integer.parseInt(varTabVZC2));
						varBtcUmic2.setTablaBaseExp(tablaBaseExp);
					}
				
					mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
			
				}

				for (int j = iteracion - 1 ; j < proyUmic.size(); j++ ) {
					if(!(null == proyUmic.get(j).getBloqueVida().getFechaDevengo())){
						moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
						varCSP238 = (BigDecimal) moduloCSP238.execute(varProyCsp238, proyUmic.get(j).getBloqueVida(), iteracionBucleModulos, fcalc, umic, btcUmic, mapVariables,codSubproceso);
						varCSP238 = varCSP238.divide(new BigDecimal(0.9985),ConstantsFunciones.MATH_CONTEXT);
						if(!varCSP238.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP))){

							varfecJ1 = proyUmic.get(j).getBloqueVida().getFechaDevengo();
							varJ1 = FuncionesAuxiliares.nAnnos(varFechaEfecto, varfecJ1, varCriterFecha);
							varVVida = FuncionesActualizacionFinanciera.vVida(varJ1, varI2, varI2, varM, varTcyVida, varI2PorcentajeMasUno, varI2PorcentajeMasUno);
							moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
							
							varVzc2 = (BigDecimal) moduloVZC.execute(varProyVzc2, proyUmic.get(j).getBloqueVida(), iteracionBucleModulos, proyUmic.get(iteracion-1).getFechaDesde(), varUmic2, varBtcUmic2,mapVariables,codSubproceso);
							
							aux = varCSP238.multiply(varVzc2).multiply(varVVida);

						}
					}
					
					csp333 = csp333.add(aux);
					iteracionBucleModulos++;
					
				}
				if(null == proyUmic.get(iteracion-1).getBloqueVida().getFechaDevengo()){
					csp333 = BigDecimal.ZERO;
				}

			}
		
		}
		// Cálculo y validación de las variables de apoyo.  
		
		
		if (ModuloCSP333.LOG.isTraceEnabled()) {
			ModuloCSP333.LOG.trace("Fin función << moduloCSP333 >> de la clase ModuloCSP333, para la iteracion = {}, con resultado csp333 = {}", iteracion, csp333);
		}
		
		return csp333;
	}
	
}
