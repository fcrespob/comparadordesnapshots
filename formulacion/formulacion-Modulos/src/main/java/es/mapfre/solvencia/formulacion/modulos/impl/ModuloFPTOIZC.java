/** MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
  * Para el correcto calculo del modulo FPTOX2Y1 
  * FECHA: 05/03/2019
  * AUTOR: INDRA
*/
package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
 * El módulo FPTOZC (fcal,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, 
 * fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
 * La expresión matemática para su determinación es la siguiente:
 * 				fptozc(fcal,j) = (lzc + nannos(fcal, jant) - lzc + nannos(fcal, j)) / lzc
 * 
 * @author agonzalezgar
 *
 */
public class ModuloFPTOIZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOIZC.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOIZC;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT;
	private static final String CLAVE_VAR_VAL_TAB_MORT = CLAVE_VAL_TAB_MORT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_LZC = ConstantsModulos.CTE_VAR_LZC.concat(CLAVE_MODULO);
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC;
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIZC = ConstantsModulos.CTE_VAR_LIZC.concat(CLAVE_MODULO);
	
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
			if (ModuloFPTOIZC.LOG.isTraceEnabled()) {
				ModuloFPTOIZC.LOG.trace("Inicio de execute en clase FPTOIZC");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloFPTOZC.
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Llamamos a la función moduloFPTOZC.
			resultado = moduloFPTOIZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFPTOIZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOIZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFPTOIZC.LOG.isTraceEnabled()) {
			ModuloFPTOIZC.LOG.trace("Fin de execute en clase FPTOIZC");
		}
		
		return resultado;
	}
	
	/** 
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de una garantía.  
	 * El módulo FPTOZC (fcal,j) (fallecimiento en un punto) determina la probabilidad de que una cabeza de edad zc en la fecha de cálculo, fcal, 
	 * fallezca en el periodo entre las fechas jant y j, fechas en las que se devengan las prestaciones C(i,jant) y C(i,j) respectivamente 
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
	 */
	private BigDecimal moduloFPTOIZC(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal fptoizc = BigDecimal.ZERO;
		// Variables locales
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varTabMort = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCritFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varZc = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varEdadCalc = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		BigDecimal nanos = BigDecimal.ZERO;
		BigDecimal nanos_jant = BigDecimal.ZERO;
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		Timestamp jant;
		BigDecimal fptoizc_t1 = BigDecimal.ZERO;
		BigDecimal fptoizc_t2 = BigDecimal.ZERO;
		BigDecimal sobreriesgo = umic.getBti().getPriesgo();
		List<BigDecimal>     varValoresLix;
		DetalleBaseTecnica  varBtcUmic2;
		List<BigDecimal>     varValoresTabInv;
		BigDecimal varLizc = BigDecimal.ZERO;
		BigDecimal varLzcnanos = BigDecimal.ZERO;
		BigDecimal varLizcnanos = BigDecimal.ZERO;
		BigDecimal varLzcnanosant = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloFPTOIZC.LOG.isTraceEnabled()) {
			ModuloFPTOIZC.LOG.trace("Inicio función << moduloFPTOIZC >> de la clase moduloFPTOIZC, para la iteracion = {}", iteracion);
		}
		
		if(bloqueCorriente.getFechaDevengo() == null){	
			return fptoizc;
		}
		
		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);
	
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		
		jant = (Timestamp) mapVariables.get("FECHA_DEVENGO_FPTOIZC");
		
		if (jant == null) {
			jant = fcalc;
		}
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		varCriterEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);
		varCritFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		Timestamp fecNacAseg = umic.getAsegurados().getFnacAseg1();
		if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_363)){
			varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + fecNacAseg, varFechaEfecto, fecNacAseg, ConstantsFunciones.CTE_CRI_FECHA_06, umic.getRentas().getFecIni(), varEdifer);
		}else{
			varEdadCalc = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL + fecNacAseg, varFechaEfecto, fecNacAseg, varCriterEdad, umic.getRentas().getFecIni(), varEdifer);
		}

		if (umic.getDatosGenerales().getKmodalidad().equals(ConstantsModulos.MOD_363)){
			if (fcalc.after(umic.getRentas().getFecIni())){
				varZc = UtilModulos.getVarZc(mapVariables,
						CLAVE_VAR_ZC + fecNacAseg + fcalc,
						varEdadCalc, umic.getRentas().getFecIni(), fcalc,
						varCritFec,varCriterEdad);
			}else{
				varZc = varEdadCalc;
			}
		}else{ 
			varZc = UtilModulos.getVarZc(mapVariables,
					CLAVE_VAR_ZC + fecNacAseg + fcalc,
					varEdadCalc, varFechaEfecto, fcalc,
					varCritFec,varCriterEdad);
		}
		
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_MORT, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);
	
		varLzc = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc, lstValoresTabMort);
		
		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();
			
			try {
				
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
				
			} catch (Exception e) {
				ModuloFPTOIZC.LOG.error(e.getMessage());
			}
			
			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc3aseg1());
			mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
		}

		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, varBtcUmic2,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_I);
		varValoresLix = FuncionesAuxiliares.obtenerLix(varValoresTabInv, sobreriesgo);
		varLizc = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LIZC + varZc + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc, varValoresLix);
	
		nanos = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCritFec);		
		nanos_jant = FuncionesAuxiliares.nAnnos(fcalc, jant, varCritFec);
		varLzcnanos = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + nanos + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc.add(nanos), lstValoresTabMort);
		varLzcnanosant = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + nanos_jant + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc.add(nanos_jant), lstValoresTabMort);
		varLizcnanos = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LIZC + varZc + nanos + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc.add(nanos), varValoresLix);
		
		fptoizc_t2 = varLizcnanos.divide(varLizc, ConstantsFunciones.MATH_CONTEXT);
		fptoizc_t1 = (varLzcnanosant.subtract(varLzcnanos)).divide(varLzc, ConstantsFunciones.MATH_CONTEXT);
		fptoizc = fptoizc_t2.multiply(fptoizc_t1);
		
		if (ModuloFPTOIZC.LOG.isTraceEnabled()) {
			ModuloFPTOIZC.LOG.trace("Fin función << ModuloFPTOIZC >> de la clase ModuloFPTOIZC, para la iteracion = {}, con resultado fptoizc = {}", iteracion, fptoizc);
		}
		mapVariables.put("FECHA_DEVENGO_FPTOIZC", bloqueCorriente.getFechaDevengo());
		return fptoizc;
	}

}