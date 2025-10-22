package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */
public class ModuloCSP016 implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP016.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP016;
	
	private static final String CLAVE_P3TOP = ConstantsModulos.CTE_VAR_P3TOP;
	private static final String CLAVE_VAR_P3TOP = CLAVE_P3TOP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_P1JUB= ConstantsModulos.CTE_VAR_P1JUB;
	private static final String CLAVE_VAR_P1JUB = CLAVE_P1JUB.concat(CLAVE_MODULO);
	
	private static final String CLAVE_P2VIV = ConstantsModulos.CTE_VAR_P2VIV;
	private static final String CLAVE_VAR_P2VIV = CLAVE_P2VIV.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FEC_EFECTO = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_VAR_PORREVAL = ConstantsModulos.CTE_VAR_PORREVAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PORCENTAJE = ConstantsModulos.CTE_VAR_PORCENTAJE.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_CSP016 = ConstantsModulos.CTE_VAR_CSP016.concat(CLAVE_MODULO);
	
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloCSP016.LOG.isTraceEnabled()) {
				ModuloCSP016.LOG.trace("Inicio de execute en clase ModuloCSP016");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP016
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP016
			resultado = moduloCSP016(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP016.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP016.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP016.LOG.isTraceEnabled()) {
			ModuloCSP016.LOG.trace("Fin de execute en clase ModuloCSP016");
		}
		return resultado;
	}
	/**
	 * Usaremos este módulo cómo módulo general para el capital en renovaciones
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloCSP016(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		
		//variables locales
		BigDecimal csp016 = BigDecimal.ZERO;
		String VarCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String VarCriterFec =  ConstantsFunciones.CTE_CADENA_VACIA;
		String varP3TOPStr = ConstantsFunciones.CTE_CADENA_VACIA;
		String varP1JUBStr = ConstantsFunciones.CTE_CADENA_VACIA;
		String varP2VIVStr = ConstantsFunciones.CTE_CADENA_VACIA;
		Timestamp varFechaEfecto = null;
		Integer varAnoEfecto; 
		BigDecimal varPorreval;
		BigDecimal varPorcentaje;
		List<DetalleCorriente> varProyFptozc;
		List<DetalleCorriente> varProy238;
		BigDecimal varCSP016 = BigDecimal.ZERO;
		BigDecimal varCSP016ant;
		BigDecimal varFptozc;
		BigDecimal varCSP238L;
		Timestamp varfechaS;
		BigDecimal varP3TOP;
		BigDecimal varP1JUB;
		BigDecimal varP2VIV;
		BigDecimal op1; 
		BigDecimal op2; 
		BigDecimal op3;
		BigDecimal varC2;
		int varNumPagos;
		String varTitular;
		BigDecimal varC2aux;
		
		//Fin variables locales
		
		if (ModuloCSP016.LOG.isTraceEnabled()) {
			ModuloCSP016.LOG.trace("Inicio de la función << moduloCSP016 >> de la clase ModuloCSP016, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		

		if(bloqueCorriente.getFechaDevengo() == null){
			
			return varCSP016;
			
		}
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * 
		 * Variables de Apoyo
		 * 	VarCriterEdad  obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * 	VarCriterFec     obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 
		 * 	Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 
		 *  varP3TOP = obtenerConfiguracion.recuperarDatosEspecificosUmic(poliza,subPoliza,certificado, ‘P3TOP’)
		 * 	varP3TOP =  TO_NUMBER (varP3TOP,15,2)   Se convertirá ell string retornado a un importe, con dos decimales.
		 * 	varP1JUB = obtenerConfiguracion.recuperarDatosEspecificosUmic(poliza,subPoliza,certificado, ‘P1JUB’)
		 * 	varP1JUB =  TO_NUMBER (varP1JUB,15,2)   Se convertirá ell string retornado a un importe, con dos decimales.
		 * 	varP2VIV = obtenerConfiguracion.recuperarDatosEspecificosUmic(poliza,subPoliza,certificado, ‘P2VIV’)
		 * 	varP2VIV =  TO_NUMBER (varP2VIV,15,2)   Se convertirá ell string retornado a un importe, con dos decimales.
		 * 
		 * Si estoy en el primer periodo (j=1), se establecerán las siguientes variables:  
		 * 		Si umic.datosGenerales.cnegocio = ‘I’ ->varfechaEfecto = umic.fechas.fecefecIni
		 * 		En caso contrario -> varfechaEfecto = umic.fechas fecinisus
		 *		varAñoEfecto = Año(varfechaEfecto)
		 * 		varPorreval = 1  + (3/100)   
		 * 		varPorcentaje = 75/100 
		 * 		varProyFptozc = proyUmic
		 * 		varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
		 *  	dejo las variablse en memoria disponibles para el subproceso de la umic.
		 * 
		 * Para cualquier periodo j (j >= 1), se establecerán las siguientes variables:
		 * 
		 * varfecJ = proyUmic(j).varBloque.fecDevengo
		 * varCSP016(j) = ∑_(s=fcalc)^varfecJ▒〖varC2(s) *varFptozc(fcalc,s)〗
		 * varCSP016_jant = varCSP016(j)
		 * Donde cada término del sumatorio se obtendrá como: 
		 * 	varFptozc(s)= FPTOZC (varProyFptozc, periodoProyeccion , fcalc, umic,btcUmic,codSubproceso)
		 * 	varfechaS = varProyFptozc.varBloque.fecDevengo
		 *  varC2(s) = varPorcentaje*[varP3TOP+(varP1JUB * 〖varPorreval 〗^(año(varfechaS)-varAñoEfecto ) )-(varP2VIV*〖varPorreval 〗^(año(varfechaS)-varAñoEfecto )) ]  
		 */
		
		// Definición de variables auxiliares para agilizar las operaciones.
			final Integer cCartera = umic.getDatosGenerales().getCcartera();
			final Integer kModalidad = umic.getDatosGenerales().getKmodalidad();
			final Integer kGarantia = umic.getDatosGenerales().getKgarantia();
		//Variables de Apoyo	
			VarCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, cCartera, kModalidad, kGarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
			VarCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, cCartera, kModalidad, kGarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		
			// Validación de las variables de apoyo.
			if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(VarCriterFec);
				ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(VarCriterEdad);
			}
			
			varP3TOPStr = UtilModulos.getDatosEspecificosUmic(mapVariables, 
														   CLAVE_VAR_P3TOP,
														   umic.getDatosGenerales().getKpoliza()	 , 
														   umic.getDatosGenerales().getKsubpoliza()  ,
														   umic.getDatosGenerales().getKcertificado(),
														   umic.getDatosGenerales().getNsuscri(),
														   CLAVE_P3TOP);
			
			if(null == varP3TOPStr){
				
				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P3TOP});
				varP3TOP = BigDecimal.ZERO;
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso3 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DQ, new Object[]{CLAVE_P3TOP});
				servicio.almacenarIncidencias(aviso3);
				
			}else{
				varP3TOP = new BigDecimal(varP3TOPStr).setScale(2, RoundingMode.HALF_UP);
				varP3TOP = varP3TOP.divide(new BigDecimal("100"));
			}
			
			varP1JUBStr = UtilModulos.getDatosEspecificosUmic(mapVariables, 
														   CLAVE_VAR_P1JUB,
														   umic.getDatosGenerales().getKpoliza()	 , 
														   umic.getDatosGenerales().getKsubpoliza()  ,
														   umic.getDatosGenerales().getKcertificado(),
														   umic.getDatosGenerales().getNsuscri(),
														   CLAVE_P1JUB);	
			if(null == varP1JUBStr){
				
				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P1JUB});
				varP1JUB = BigDecimal.ZERO;
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso1 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DN, new Object[]{CLAVE_P1JUB});
				servicio.almacenarIncidencias(aviso1);
				
			}else{
				varP1JUB = new BigDecimal(varP1JUBStr).setScale(2, RoundingMode.HALF_UP);
				varP1JUB = varP1JUB.divide(new BigDecimal("100"));
			}
			
			varP2VIVStr = UtilModulos.getDatosEspecificosUmic(mapVariables,
														   CLAVE_VAR_P2VIV,
														   umic.getDatosGenerales().getKpoliza()	 , 
														   umic.getDatosGenerales().getKsubpoliza()  ,
														   umic.getDatosGenerales().getKcertificado(),
														   umic.getDatosGenerales().getNsuscri(),
														   CLAVE_P2VIV);
			
			if(null == varP2VIVStr){
				
				//throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ, new Object[]{CLAVE_P2VIV});
				varP2VIV = BigDecimal.ZERO;
				final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
				Incidencia aviso2 = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(), btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(), ConstantsFunciones.CTE_COD_ERROR_DP, new Object[]{CLAVE_P2VIV});
				servicio.almacenarIncidencias(aviso2);
				
			}else{
				varP2VIV = new BigDecimal(varP2VIVStr).setScale(2, RoundingMode.HALF_UP);
				varP2VIV = varP2VIV.divide(new BigDecimal("100"));
			}
			
			varNumPagos = 14;
			varTitular = umic.getDatosGenerales().getKbencon();
			
			varFechaEfecto = UtilModulos.getVarFechaEfecto(mapVariables, CLAVE_VAR_FEC_EFECTO, umic); 
			varAnoEfecto = UtilFechas.getAnio(varFechaEfecto);
			varPorreval = UtilModulos.getNumPorcentajeMasUno(ConstantsFunciones.CTE_OPER_3,mapVariables, CLAVE_VAR_PORREVAL);
			varPorcentaje = UtilModulos.getPorcentaje(mapVariables, CLAVE_VAR_PORCENTAJE, BigDecimal.valueOf(75));
			varProyFptozc = proyUmic;
			varProy238 = proyUmic;
			
		if(varTitular.equals("101")){
			Modulo moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
			varFptozc = (BigDecimal) moduloFPTOZC.execute(varProyFptozc,bloqueCorriente, iteracion, fcalc, umic, btcUmic,mapVariables, codSubproceso);
			varfechaS = bloqueCorriente.getFechaDevengo();
			
			op1 = varP1JUB.multiply(varPorreval.pow(UtilFechas.getAnio(varfechaS)-varAnoEfecto));
			op2 = varP2VIV.multiply(varPorreval.pow(UtilFechas.getAnio(varfechaS)-varAnoEfecto));
			op3 = varP3TOP.add(op1);
			varC2aux = varPorcentaje.multiply(op3).subtract(op2);
			varC2 = varC2aux.divide(new BigDecimal(varNumPagos),ConstantsFunciones.MATH_CONTEXT);
			varCSP016ant = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP016);
			
			if (null == varCSP016ant){
				varCSP016=varC2.multiply(varFptozc);
			} else {
				varCSP016=varCSP016ant.add(varC2.multiply(varFptozc));
			}
			
			mapVariables.put(CLAVE_VAR_CSP016, varCSP016);
			
		}else{
			Modulo moduloCSP238L = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238L);
			varCSP238L = (BigDecimal) moduloCSP238L.execute(varProy238,bloqueCorriente, iteracion, fcalc, umic, btcUmic,mapVariables, codSubproceso);
			varCSP016 = varCSP238L;
			
		}	
		
		if (ModuloCSP016.LOG.isTraceEnabled()) {
			ModuloCSP016.LOG.trace("Fin de la función << moduloCSP016 >> de la clase ModuloCSP016, para la iteración = {}", iteracion);
		}
		return varCSP016;
	}
}
