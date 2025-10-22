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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloFPTOEXT implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOEXT.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOEXT;
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String BA = "BA";
	private static final String BB = "BB";
	private static final String G5 = "G5";
	private static final String CLAVE_FZC1_JANT = "FZC1_JANT_FPTOEXT";
	private static final String CLAVE_FZC2_JANT = "FZC2_JANT_FPTOEXT";
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
			if (ModuloFPTOEXT.LOG.isTraceEnabled()) {
				ModuloFPTOEXT.LOG.trace("Inicio de execute en clase ModuloFPTOEXT");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloVARB167
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función ModuloVARB167
			resultado = moduloFPTOEXT(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloFPTOEXT.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOEXT.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloFPTOEXT.LOG.isTraceEnabled()) {
			ModuloFPTOEXT.LOG.trace("Fin de execute en clase ModuloFPTOEXT");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
	 * el cálculo de la cuantía probable de la garantía. La expresión matemática
	 * para su determinación es la siguiente: FPTOEXT = Fptozc1zc2(fcal,jf) +
	 * Fzc1(fcal,jfant)*Fptozc2(fcal,jf) + Fzc2(fcal,jfant)*Fptozc1(fcal,jf)*Vcierta
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
	private BigDecimal moduloFPTOEXT(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		BigDecimal varfptoext = BigDecimal.ZERO;
		BigDecimal varProyVZC;
		BigDecimal varVzc;
		BigDecimal varProyfptozc;
		BigDecimal varVcierta;
		BigDecimal varFzc1;
		BigDecimal varFzc1J;
		BigDecimal varFzc2;
		BigDecimal varFzc2J;
		BigDecimal varFptozc1 = null;
		BigDecimal varFptozc2 = null;
		BigDecimal varFptozc1zc2;
		Modulo moduloFPTOZC1ZC2;
		Modulo moduloFPTOZC1;
		Modulo moduloFPTOZC2;
		Modulo moduloFZC1;
		Modulo moduloFZC2;
		Modulo moduloVZCIERTA;
		Umic varUmic1, varUmic2;
		DetalleBaseTecnica varBtcUmic2;
		List<DetalleCorriente> varProyFzc2 = null;

		if (ModuloFPTOEXT.LOG.isTraceEnabled()) {
			ModuloFPTOEXT.LOG.trace(
					"Inicio función << ModuloFPTOEXT >> de la clase ModuloFPTOEXT, para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (!(bloqueCorriente.getFechaDevengo() == null)) {

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

			if (codSubproceso.equals(ConstantsModulos.CTE_PROY_VIDA)) {
				varfptoext = BigDecimal.ZERO;
				return varfptoext;
			}

			if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_1_STRING)) {
				varfptoext = BigDecimal.ZERO;

			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1)
					.equals(ConstantsFunciones.CTE_2_STRING)) {
				varfptoext = BigDecimal.ZERO;

			} else {
				if ((umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_S))
						&& (umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V) || umic
								.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_S))) {

					moduloFPTOZC1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
					varFptozc1 = (BigDecimal) moduloFPTOZC1.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
					varVcierta = (BigDecimal) moduloVZCIERTA.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					moduloFZC1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
					
					varFzc1J = (BigDecimal) moduloFZC1.execute(proyUmic, bloqueCorriente, iteracion-1, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					
					if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
						varFzc1 = BigDecimal.ZERO;
					} else {
						varFzc1 = (BigDecimal) mapVariables.get(CLAVE_FZC1_JANT);
					}
					
					mapVariables.put(CLAVE_FZC1_JANT, varFzc1J);

					

					// Calculamos FZC2 para ello movemos los datos del asegurado 2 al 1 y llamamos a
					// FZC
					varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);

					if (null == varUmic2) {
						varUmic2 = new Umic();
						try {

							PropertyUtils.copyProperties(varUmic2, umic);

						} catch (Exception e) {
							ModuloFPTOEXT.LOG.error(e.getMessage());
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

							ModuloFPTOEXT.LOG.error(e.getMessage());
						}

						varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

						if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))) {
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
						}

						mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);

					}

					varProyFzc2 = proyUmic;
					moduloFZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
					
					varFzc2J = (BigDecimal) moduloFZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc, varUmic2,
							varBtcUmic2, mapVariables, codSubproceso);
					
					if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
						varFzc2 = BigDecimal.ZERO;
					} else {
						varFzc2 = (BigDecimal) mapVariables.get(CLAVE_FZC2_JANT);
					}
					
					mapVariables.put(CLAVE_FZC2_JANT, varFzc2J);
					
					moduloFPTOZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC2);
					varFptozc2 = (BigDecimal) moduloFPTOZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
							varUmic2, varBtcUmic2, mapVariables, codSubproceso);
					
					// varProyFZC1FZC2 = proyUmic;
					moduloFPTOZC1ZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC1ZC2);
					varFptozc1zc2 = (BigDecimal) moduloFPTOZC1ZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
							umic, btcUmic, mapVariables, codSubproceso);
					
					varfptoext = varFptozc1zc2
							.add((varFptozc2.multiply(varFzc1)).add(varFzc2.multiply(varFptozc1.multiply(varVcierta))));
				}

				if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& (umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V) || umic
								.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_S))) {
					varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);

					if (null == varUmic2) {
						varUmic2 = new Umic();
						try {

							PropertyUtils.copyProperties(varUmic2, umic);

						} catch (Exception e) {
							ModuloFPTOEXT.LOG.error(e.getMessage());
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

							ModuloFPTOEXT.LOG.error(e.getMessage());
						}

						varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

						if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))) {
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
						}

						mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);

					}

					moduloFPTOZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC2);
					varFptozc2 = (BigDecimal) moduloFPTOZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
							varUmic2, varBtcUmic2, mapVariables, codSubproceso);
					varfptoext = varFptozc2;
				}

				if ((umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
						|| umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_S))
						&& umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					moduloFPTOZC1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
					varFptozc1 = (BigDecimal) moduloFPTOZC1.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varfptoext = varFptozc1;
				}

				if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)
						&& umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
					varVcierta = (BigDecimal) moduloVZCIERTA.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					varfptoext = varVcierta;

				}

			}
		}

		if (ModuloFPTOEXT.LOG.isTraceEnabled()) {
			ModuloFPTOEXT.LOG.trace("Fin función << ModuloFPTOEXT >> de la clase ModuloFPTOEXT");
		}

		return varfptoext;
	}

}
