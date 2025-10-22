package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.FuncionesVBX;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo VBX160.  
 * 				
 * @author apedro
 *
 */
public class ModuloVBX160 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX160.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VBX160;
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;	
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_M = ConstantsModulos.CTE_VAR_M.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_X = ConstantsModulos.CTE_VAR_X.concat(CLAVE_MODULO);	
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_J = ConstantsFunciones.CTE_VAR_J.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_AFRXT = ConstantsModulos.CTE_VAR_AFRXT.concat(CLAVE_MODULO);
	
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
		TotalFlujoProyeccion resultado = null;
		//Fin variables locales
		
		try {
			
			if (ModuloVBX160.LOG.isTraceEnabled()) {
				ModuloVBX160.LOG.trace("Inicio de execute en clase ModuloVBX160");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX160
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente =  (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo VBX160
			resultado = moduloVBX160(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVBX160.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVBX160.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVBX160.LOG.isTraceEnabled()) {
			ModuloVBX160.LOG.trace("Fin de execute en clase ModuloVBX160");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo de cálculo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada.
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
	 * 			Código del subproceso que se está ejecutando
	 * 
	 */
	private TotalFlujoProyeccion moduloVBX160(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal vbx160 = BigDecimal.ZERO;
		final TotalFlujoProyeccion salida = new TotalFlujoProyeccion();
		Timestamp varfechaEfecto = null;
		String varCriterioEdad;
		String varCriterFec;
		BigDecimal varM;
		BigDecimal varppr;
		Integer varDifer;
		Integer varX;
		BigDecimal varI1;
		BigDecimal varI2;
		List<BigDecimal> varValoresTabMort;
		Integer varW = 0;
		BigDecimal varFactorGic;
		Integer varF = 0;
		Timestamp varfecJ;
		Integer varTc;
		Integer varJ;
		BigDecimal varRentaj;
		BigDecimal varAfrxt;
		//Fin variables locales
		
		if (ModuloVBX160.LOG.isTraceEnabled()) {
			ModuloVBX160.LOG.trace("Inicio función << moduloVBX160 >> de la clase ModuloVBX160, para la iteracion = {}", iteracion);
		}
		
		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
	
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		// Cálculo y validación de las variables de apoyo.
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
			// Fin de la validación de las variables de apoyo.
		}
		

		// Variables Módulo
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_VAR_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		varM = UtilModulos.getVarM(mapVariables, CLAVE_VAR_M, varfechaEfecto, umic.getBti().getFecFinTramo1(), varCriterFec);
		
		varppr = umic.getRentas().getPrevrenta();
		varDifer = umic.getRentas().getNadifer();
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varX = UtilModulos.getVarX(mapVariables, CLAVE_VAR_X, varfechaEfecto, umic.getAsegurados().getFnacAseg1(), 
				varCriterioEdad, umic.getRentas().getFecIni(), varEdifer).intValue();
		varI1 = btcUmic.getItcalc().get(0);
		varI2 = btcUmic.getItcalc().get(1);
		varValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		varFactorGic = BigDecimal.ONE.add(btcUmic.getGtorosspCap().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
		//varFactorGic = BigDecimal.ONE.add(btcUmic.getGtorosspCap());
		
		
		if (ConstantsModulos.CTE_VAL_TP_VIT.equals(umic.getRentas().getTempVit())){
			varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, UtilFechas.getAnio(umic.getAsegurados().getFnacAseg1()),
					umic, btcUmic, IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		} else if (ConstantsModulos.CTE_VAL_TP_TEM.equals(umic.getRentas().getTempVit())){
			varW = varX + umic.getDuraciones().getNdursegano();
		}
		
		
		if (ConstantsFunciones.CTE_FORMPAGO_1.equals(umic.getRentas().getCpagrenta()) || 
				ConstantsFunciones.CTE_FORMPAGO_2.equals(umic.getRentas().getCpagrenta())){
			varF = Integer.valueOf(umic.getRentas().getCpagrenta());
		} else if (ConstantsFunciones.CTE_FORMPAGO_3.equals(umic.getRentas().getCpagrenta())){
			varF = ConstantsFunciones.CTE_4;
		} else if (ConstantsFunciones.CTE_FORMPAGO_4.equals(umic.getRentas().getCpagrenta())){
			varF = ConstantsFunciones.CTE_12;
		} else {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AQ, new String[]{umic.getRentas().getCpagrenta()});
		}
		
		
		varRentaj = umic.getRentas().getRentini();
		varfecJ = proyUmic.get(iteracion-1).getFechaDesde();
		varTc= FuncionesAuxiliares.tc(varfechaEfecto, UtilFechas.decreDias(varfecJ, 1));
		
		if (iteracion == ConstantsModulos.CTE_FIRST_ITER) {
			varJ = varTc;
			varAfrxt = FuncionesVBX.afrxt(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF);
			mapVariables.put(CLAVE_VAR_J, varJ);
			mapVariables.put(CLAVE_VAR_AFRXT, varAfrxt);
			
			
		} else {
			varJ = (Integer) mapVariables.get(CLAVE_VAR_J);
			if (varJ != varTc){
				varJ = varTc;
				varAfrxt = FuncionesVBX.afrxt(varDifer, varX, varW, varppr, varTc, varValoresTabMort, varM, varI1, varI2, varF);
				mapVariables.put(CLAVE_VAR_J, varJ);
				mapVariables.put(CLAVE_VAR_AFRXT, varAfrxt);
			} else {
				varAfrxt = (BigDecimal) mapVariables.get(CLAVE_VAR_AFRXT);
			}
		}
		

		//Se calcula VBX160(j) = varRentaj * (varAfrxt )/2   * varFactorGic
		vbx160 = varRentaj.multiply(varAfrxt.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_5)).multiply(varFactorGic);
		
		
		/* Se retornará:
		 * 	- VBX160 = vbx160  
		 * 	- Terminal anterior = 0
		 *  - Terminal posterior = 0
		 */		
		salida.setProvbtiproy(vbx160);
		salida.setTerminalAnterior(BigDecimal.ZERO);
		salida.setTerminalPosterior(BigDecimal.ZERO);		
		
		
		if (ModuloVBX160.LOG.isTraceEnabled()) {
			ModuloVBX160.LOG.trace("Fin función << moduloVBX700 >> de la clase ModuloVBX160, para la iteracion = {}, con resultado vbx160 = {}", iteracion, vbx160);
		}
		
		return salida;
	}
	
	
}
