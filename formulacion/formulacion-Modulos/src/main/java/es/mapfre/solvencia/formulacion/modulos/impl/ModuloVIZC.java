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
 * Clase encargada del cálculo que devuelve los factores de probabilización a
 * aplicar en el cálculo de la cuantía probable de una garantía. El módulo
 * Vzc(fcal,j) determina la probabilidad de que una cabeza de edad zc en la
 * fecha de cálculo, fcal, alcance con vida la fecha j en la que se devenga la
 * prestación. La expresión matemática para su determinación es la siguiente:
 * VZC(fcal, j) = (lzc + nannos(fcal,j)) /lzc
 * 
 * @author agonzalezgar
 *
 */
public class ModuloVIZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVIZC.class);

	// Estas variables se ponen como estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VIZC;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_VAR_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CAL = ConstantsModulos.CTE_EDAD_CAL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ZC = ConstantsModulos.CTE_VAR_ZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LZC = ConstantsModulos.CTE_VAR_LZC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_MORT = ConstantsModulos.CTE_VAR_VAL_TAB_MORT.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_VAL_TAB_INV = ConstantsModulos.CTE_VAR_VAL_TAB_INV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_LIZC = ConstantsModulos.CTE_VAR_LIZC.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloVIZC.LOG.isTraceEnabled()) {
				ModuloVIZC.LOG.trace("Inicio de execute en clase ModuloVIZC");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función de calculo VZC
			resultado = moduloVIZC(proyUmic, bloqueCorriente, iteracion, fcalc,
					umic, btcUmic, mapVariables, codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVIZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVIZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(
					ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVIZC.LOG.isTraceEnabled()) {
			ModuloVIZC.LOG.trace("Fin de execute en clase ModuloVIZC");
		}

		return resultado;

	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar
	 * en el cálculo de la cuantía probable de una garantía. El módulo
	 * Vzc(fcal,j) determina la probabilidad de que una cabeza de edad zc en la
	 * fecha de cálculo, fcal, alcance con vida la fecha j en la que se devenga
	 * la prestación.
	 *
	 * @param proyUmic
	 *            Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 *            Bloque de Trabajo de la corriente
	 * @param iteracion
	 *            Indica el periodo de proyección J que se está calculando de
	 *            entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 *            Fecha de calculo
	 * @param umic
	 *            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 *            Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables
	 *            mapa con las variables de memoria necesarias
	 * @param codSubproceso
	 *            Código del subproceso que se está ejecutando
	 * @return vzcj
	 */
	private BigDecimal moduloVIZC(final List<DetalleCorriente> proyUmic,
			final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		String varCriterEdad = ConstantsFunciones.CTE_CADENA_VACIA;
		String varTabMort = ConstantsFunciones.CTE_CADENA_VACIA;
		String varCritFec = ConstantsFunciones.CTE_CADENA_VACIA;
		BigDecimal varLzc = BigDecimal.ZERO;
		BigDecimal varLizc = BigDecimal.ZERO;
		BigDecimal vizc = BigDecimal.ZERO;
		BigDecimal varZc = BigDecimal.ZERO;
		List<BigDecimal> lstValoresTabMort = null;
		BigDecimal varEdadCalc = BigDecimal.ZERO;
		Timestamp varFechaEfecto = null;
		BigDecimal nanos = BigDecimal.ZERO;
		Integer varEdifer = umic.getDatosGenerales().getEdifer();
		BigDecimal sobreriesgo = umic.getBti().getPriesgo();
		List<BigDecimal>     varValoresLix;
		DetalleBaseTecnica  varBtcUmic2;
		List<BigDecimal>     varValoresTabInv;
		BigDecimal varLzcnanos = BigDecimal.ZERO;
		BigDecimal varLizcnanos = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloVIZC.LOG.isTraceEnabled()) {
			ModuloVIZC.LOG
				.trace("Inicio función << moduloVIZC >> de la clase ModuloVIZC, para la entrada iteracion = {}",
						iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);
		
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();

		if (null == bloqueCorriente.getFechaDevengo()) {
			return vizc;
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
		varTabMort = btcUmic.getTablacalc1aseg1();
		lstValoresTabMort = UtilModulos.getVarValoresTabMort(mapVariables,
				CLAVE_VAR_VAL_TAB_MORT + varTabMort + fecNacAseg, umic, btcUmic,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1,
				varCriterEdad, ConstantesSolvencia.CTE_TABMORT_L);	
		
		varLzc = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc, lstValoresTabMort);
		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();
			
			try {
				
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
				
			} catch (Exception e) {
				ModuloVIZC.LOG.error(e.getMessage());
			}
			
			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc3aseg1());
			mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
		}

		varValoresTabInv = UtilModulos.getVarValoresTabMort(mapVariables, CLAVE_VAR_VAL_TAB_INV, umic, varBtcUmic2,
				IObtenerConfiguracion.OrdenAsegurado.ASEG1, varCriterEdad, ConstantesSolvencia.CTE_TABMORT_I);
		varValoresLix = FuncionesAuxiliares.obtenerLix(varValoresTabInv, sobreriesgo);
		varLizc = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LIZC + varZc + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc, varValoresLix);
		
		nanos = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaDevengo(), varCritFec);		
		varLzcnanos = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LZC + varZc + nanos + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc.add(nanos), lstValoresTabMort);
		varLizcnanos = UtilModulos.getVarLzcVZC(mapVariables, CLAVE_VAR_LIZC + varZc + nanos + varTabMort + umic.getAsegurados().getFnacAseg1(), varZc.add(nanos), varValoresLix);
		
		vizc = (varLzcnanos.divide(varLzc, ConstantsFunciones.MATH_CONTEXT)).multiply(varLizcnanos.divide(varLizc, ConstantsFunciones.MATH_CONTEXT));
		if (ModuloVIZC.LOG.isTraceEnabled()) {
			ModuloVIZC.LOG.trace("Fin función << ModuloVIZC >> de la clase ModuloVIZC, para la iteracion = {}, con resultado vizc = {}", iteracion, vizc);
		}
		return vizc;
	}

}
