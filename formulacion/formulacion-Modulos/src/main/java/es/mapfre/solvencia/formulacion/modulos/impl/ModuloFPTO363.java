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
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el módulo FPTO363.
 * La expresión matemática para su determinación es la siguiente:
 * 				FPTO363(fcierta,j) = (lzc + nannos(fcierta, jant) - lzc + nannos(fcierta, j)) / lzc
 * 
 * @author apedro
 *
 */
public class ModuloFPTO363 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTO363.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTO363;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC = ConstantsModulos.CTE_VAR_LZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LJ_ANT = ConstantsModulos.CTE_VAR_LJ1.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
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
			if (ModuloFPTO363.LOG.isTraceEnabled()) {
				ModuloFPTO363.LOG.trace("Inicio de execute en clase FPTO363");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloFPTO363.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubProceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Llamamos a la función moduloFPTO363.
			resultado = moduloFPTO363(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubProceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFPTO363.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTO363.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFPTO363.LOG.isTraceEnabled()) {
			ModuloFPTO363.LOG.trace("Fin de execute en clase FPTO363");
		}
		
		return resultado;
	}
	
	/** 
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * El módulo FPTO363 (fcierta,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cierta, fcierta, 
	 * (mayor entre fecha de cálculo y fecha inicio de renta) fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
	 * El procedimiento es similar al FPTOZC, ya implantado en fase anterior, pero sustituyendo la fcal por la fcierta.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				FPTO363(fcierta,j) = (lzc + nannos(fcierta, jant) - lzc + nannos(fcierta, j)) / lzc
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
	 */
	private BigDecimal moduloFPTO363(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubProceso) {
		//Variables locales
		String varCriterioEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCriterFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varEdadCalc;
		BigDecimal varEdadNJ = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varFracc1 = BigDecimal.ZERO;
		BigDecimal varZc = BigDecimal.ZERO;
		BigDecimal varLzcEntero = BigDecimal.ZERO;
		BigDecimal varLzcEntero1 = BigDecimal.ZERO;
		BigDecimal varLj = BigDecimal.ZERO;
		BigDecimal varLjEntero = BigDecimal.ZERO;
		BigDecimal varLjEntero1 = BigDecimal.ZERO;
		BigDecimal fpto363 = BigDecimal.ZERO;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varLJant = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		Timestamp varFcierta;
		//Fin variables locales
		
		if (ModuloFPTO363.LOG.isTraceEnabled()) {
			ModuloFPTO363.LOG.trace("Inicio función << moduloFPTO363 >> de la clase moduloFPTO363, para la iteracion = {}", iteracion);
		}
		

		//Validación de los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * Variables de Apoyo
		 * - VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
		 * - VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * - Si alguna de las variables de apoyo  retornadas es nulo se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC.
		 * 
		 */
		
		// Variables auxiliares
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		
		// Cálculo y validación de las variables de apoyo.  
		varCriterioEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCriterFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriterioEdad);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		// Introducido por nuevos criterios de generación de fechas de pago y devengo (corte de fechas). Si no existe fecha devengo no se realiza el calculo
		if (null == bloqueCorriente.getFechaDevengo() ) {
			return fpto363;
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		if(fcalc.after(umic.getRentas().getFecIni())){
			varFcierta = fcalc;
		} else {
			varFcierta = umic.getRentas().getFecIni();
		}

		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL, varFechaEfecto, umic.getAsegurados().getFnacAseg1(), ConstantsFunciones.CTE_CRI_FECHA_06, umic.getRentas().getFecIni(), varEdifer);
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterioEdad, ConstantesSolvencia.CTE_TABMORT_L);
		
		//Se calcula varZc en función de varFcierta y se deja en memoria para el subproceso de la umic
		if (varFcierta.after(umic.getRentas().getFecIni())){
			varZc = UtilModulos.getVarZc(mapVariables, CLAVE_VAR_ZC, varEdadCalc, umic.getRentas().getFecIni(), varFcierta, varCriterFec,varCriterioEdad);
		} else {
			varZc = varEdadCalc;
		}
	
		//Se calcula varLzc la primera vez y se deja en memoria para el subproceso de la umic
		varLzc = (BigDecimal) mapVariables.get(CLAVE_VAR_LZC);
		if (varLzc == null){
			varLzcEntero = lstValoresTabMort.get(varZc.intValue());
			varLzcEntero1 = lstValoresTabMort.get(varZc.intValue() + 1);
			varLzc = Util.interpolaPorEdad(varLzcEntero, varLzcEntero1, varZc);
			varLzc = UtilModulos.setVarLzcFPTOZC(mapVariables, CLAVE_VAR_LZC, varLzc);
		}
		
		if (codSubProceso != null && codSubProceso.equals("PROY_PMRR")) {
			mapVariables.remove(CLAVE_VAR_LZC);
		}
		
		//Se calcula varLj para cualquier periodo j
		varFracc1 = FuncionesAuxiliares.nAnnos(varFcierta, bloqueCorriente.getFechaDevengo(), varCriterFec);
		varEdadNJ = varZc.add(varFracc1);
		
		if (varEdadNJ.compareTo(BigDecimal.valueOf(lstValoresTabMort.size()-2)) == 1){
			varLj = BigDecimal.ZERO;
		} else {
			varLjEntero = lstValoresTabMort.get(varEdadNJ.intValue());
			varLjEntero1 = lstValoresTabMort.get(varEdadNJ.intValue() + 1);
			varLj = Util.interpolaPorEdad(varLjEntero, varLjEntero1, varEdadNJ);
		}
		
		varLJant = (BigDecimal) mapVariables.get(CLAVE_VAR_LJ_ANT);
		if (varLJant == null) {//Si es la primera vez que se calcula fpto363:
			//Se calculará la probabilidad en el periodo j como
			//fpto363 = (varLzc - varLj)/ varLzc
			if (varLzc.intValue() == 0) {
				fpto363 = BigDecimal.ONE;
			} else {
				fpto363 = (varLzc.subtract(varLj)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
			}
			
			mapVariables.put(CLAVE_VAR_LJ_ANT, varLj);
			
		} else {
			//Se calculará la probabilidad en el periodo j como
			//fpto363 = (varLjant - varLj)/ varLzc
			fpto363 = (varLJant.subtract(varLj)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
			
			mapVariables.put(CLAVE_VAR_LJ_ANT, varLj);
		}
		
		if (codSubProceso != null && codSubProceso.equals("PROY_PMRR")) {
			mapVariables.remove(CLAVE_VAR_LJ_ANT);
		}

		
		if (ModuloFPTO363.LOG.isTraceEnabled()) {
			ModuloFPTO363.LOG.trace("Fin función << moduloFPTO363 >> de la clase moduloFPTO363, para la iteracion = {}, con resultado fpto363 = {}", iteracion, fpto363);
		}
		
		
		return fpto363;
	}

}