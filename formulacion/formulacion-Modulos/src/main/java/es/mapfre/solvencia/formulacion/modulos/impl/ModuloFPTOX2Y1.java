/* MODIFICACION:TAR00000000-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 14/12/2018
   AUTOR: INDRA
*/

package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo FPTOX2Y1 La expresión matemática para su
 * determinación es la siguiente: FPTOX2Y1 = FZC(2) * FPTOZC(1) + [0.5 * FPTOZC
 * (1) * FPTOZC(2)]
 * 
 *
 */
public class ModuloFPTOX2Y1 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOX2Y1.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOX2Y1;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String BA = "BA";
	private static final String BB = "BB";
	private static final String G5 = "G5";
	// Fin de las variables estáticas usadas para agilizar operaciones.

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
			if (ModuloFPTOX2Y1.LOG.isTraceEnabled()) {
				ModuloFPTOX2Y1.LOG.trace("Inicio de execute en clase ModuloFPTOX2Y1");
			}

			// Recuperamos los datos que le pasaremos a la función moduloFPTOX2Y1
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función ModuloFPTOX2Y1
			resultado = moduloFPTOX2Y1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloFPTOX2Y1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOX2Y1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloFPTOX2Y1.LOG.isTraceEnabled()) {
			ModuloFPTOX2Y1.LOG.trace("Fin de execute en clase ModuloFPTOX2Y1");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización La expresión
	 * matemática para su determinación es la siguiente: FPTOX2Y1 = FZC(2) *
	 * FPTOZC(1) + [0.5 * FPTOZC (1) * FPTOZC(2)]
	 * 
	 * @param proyUmic        Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente Bloque de Trabajo de la corriente
	 * @param iteracion       Indica el periodo de proyección J que se está
	 *                        calculando de entre todos los periodos de proyección
	 *                        de la umic (proyUmic)
	 * @param fcalc           Fecha de calculo
	 * @param umic            Contiene los datos de la Umic que se está procesando
	 * @param btcUmic         Contiene el detalle de la base técnica de cálculo para
	 *                        la umic
	 * @param mapVariables    mapa con las variables de memoria necesarias
	 * @param codSubproceso   Código del subproceso que se está ejecutando
	 */
	private BigDecimal moduloFPTOX2Y1(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal FPTOX2Y1 = BigDecimal.ZERO;
		BigDecimal varFzc = BigDecimal.ZERO ,varVzc2 = BigDecimal.ZERO;
		Modulo moduloFZC,moduloVZC,moduloFPTOZC,moduloFPTOZC2;
		Umic varUmic1, varUmic2;
		DetalleBaseTecnica  varBtcUmic2;
		List<DetalleCorriente> varProyFzc = null;
		List<DetalleCorriente> varProyVzc2 = null;
		// Fin variables locales

		if (ModuloFPTOX2Y1.LOG.isTraceEnabled()) {
			ModuloFPTOX2Y1.LOG.trace(
					"Inicio función << ModuloFPTOX2Y1 >> de la clase ModuloFPTOX2Y1, para la iteracion = {}",
					iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);

		// Si estamos en el primer periodo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
		}

		if (umic.getAsegurados().getFnacAseg2() == null && umic.getAsegurados().getCsexAseg2() == null
				&& umic.getAsegurados().getEdadAseg2() == null) {
			// Error conyuge no informdo, Fin proceso.
			throw Solvencia2ExcepcionHelper.crearExcepcion(BA,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}
		if (umic.getAsegurados().getFnacAseg1() == null && umic.getAsegurados().getCsexAseg1() == null
				&& umic.getAsegurados().getEdadAseg1() == null) {
			// Estado del asegurado no informado, fin proceso.
			throw Solvencia2ExcepcionHelper.crearExcepcion(BB,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}

		if (bloqueCorriente.getFechaDevengo() != null) {

			varUmic1 = umic;
			varProyFzc = proyUmic;
			varFzc  = (BigDecimal) moduloFPTOZC.execute(varProyFzc, bloqueCorriente, iteracion, fcalc, varUmic1,
					btcUmic, mapVariables, codSubproceso);

			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2); 

			if (null == varUmic2) {
				varUmic2 = new Umic();
				try {

					PropertyUtils.copyProperties(varUmic2, umic);

				} catch (Exception e) {
					ModuloFPTOX2Y1.LOG.error(e.getMessage());
				}
				Asegurados aseg = new Asegurados();

				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
				varUmic2.setAsegurados(aseg);

				mapVariables.put(CLAVE_UMIC2, varUmic2);
			}

			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if (null == varBtcUmic2) {

				varBtcUmic2 = new DetalleBaseTecnica();

				try {

					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

				} catch (Exception e) {

					ModuloFPTOX2Y1.LOG.error(e.getMessage());
				}
				
				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))){
					if (btcUmic.getTablasConversionAsegurado() == null
							|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
								new String[] { null, "tablasConversionAsegurado" });
					}

					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

					varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
					varBtcUmic2.setTablaBaseExp(tablaBaseExp);
					mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
				}

				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);

			}

			varProyVzc2 = proyUmic;
			if(null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equalsIgnoreCase("A")){
				varVzc2 = BigDecimal.ZERO;
			}else{
				varVzc2 = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2,
					varBtcUmic2, mapVariables, codSubproceso);
			}
			// VZC3332 (j) = varProyFzc1 (j) * varVzc2 (j)
			FPTOX2Y1 = BigDecimal.ONE.subtract(varVzc2);
			FPTOX2Y1 = FPTOX2Y1.multiply(varFzc);
		}
		
		if (ModuloFPTOX2Y1.LOG.isTraceEnabled()) {
			ModuloFPTOX2Y1.LOG.trace(
					"Fin función << ModuloFPTOX2Y1 >> de la clase ModuloFPTOX2Y1, para la iteracion = {} con resultado FPTOX2Y1 = {}",
					iteracion, FPTOX2Y1);
		}

		
		
		return FPTOX2Y1;

	}
}
