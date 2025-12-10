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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloSEP011 implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloSEP011.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_SEP011;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1 = ConstantsModulos.CTE_TABLACONV_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2 = ConstantsModulos.CTE_TABLACONV_ASEG2.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3 = ConstantsModulos.CTE_TABLACONV_ASEG3.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4 = ConstantsModulos.CTE_TABLACONV_ASEG4.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5 = ConstantsModulos.CTE_TABLACONV_ASEG5.concat(CLAVE_MODULO);

	private static final String BA = "BA";

	// Fin de las variables estáticas usadas para agilizar operaciones.
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
			if (ModuloSEP011.LOG.isTraceEnabled()) {
				ModuloSEP011.LOG.trace("Inicio de execute en clase ModuloSEP011");
			}

			// Recuperamos los datos que le pasaremos a la función moduloFZCHM1
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función ModuloFZCHM1
			resultado = moduloSEP011(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloSEP011.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloSEP011.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloSEP011.LOG.isTraceEnabled()) {
			ModuloSEP011.LOG.trace("Fin de execute en clase ModuloSEP011");
		}

		return resultado;
	}

	private BigDecimal moduloSEP011(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		// Declaración de Variables Locales y variables de apoyo.
		BigDecimal SEP011 = BigDecimal.ZERO;
		BigDecimal varPctRenta80 = BigDecimal.ZERO;
		BigDecimal varPctRenta50 = BigDecimal.ZERO;
		BigDecimal varRenta = umic.getRentas().getRentact();
//		BigDecimal varRenta = ConstantsFunciones.CTE_OPER_CTE_300_PUNTO_506052;
		BigDecimal varPctHijo = BigDecimal.ZERO;
		BigDecimal varRentaRev80 = BigDecimal.ZERO;
		BigDecimal varRentaRev50 = BigDecimal.ZERO; 
		int varIDHijo = 0;
		Boolean varHijoMinusvalido = false;
		Umic varUmic2, varUmic3;
		DetalleBaseTecnica varBtcUmic2, varBtcUmic3;
		BigDecimal varVzc2, varFzchm, varVzc3;
		List<DetalleCorriente> varProyVzc2 = null;
		List<DetalleCorriente> varProyFzchm = null;
		List<DetalleCorriente> varProyVzc3 = null;
		Modulo moduloVZC, moduloFZCHM;
		// Fin variables locales

		// Variables de Apoyo
		varRenta = proyUmic.get(iteracion - 1).getImpPago();
		if (null == varRenta ) {
			varRenta = BigDecimal.ZERO;
		}
		varPctRenta80 = ConstantsFunciones.CTE_OPER_80.divide(ConstantsFunciones.CTE_OPER_100);
		varPctRenta50 = ConstantsFunciones.CTE_OPER_50.divide(ConstantsFunciones.CTE_OPER_100);
		varPctHijo = ConstantsFunciones.CTE_OPER_CTE_300_PUNTO_506052;
		varRentaRev80 = varPctRenta80.multiply(varRenta);
		varRentaRev50 = varPctRenta50.multiply(varRenta);

		if (ModuloSEP011.LOG.isTraceEnabled()) {
			ModuloSEP011.LOG.trace("Inicio función << ModuloSEP011 >> de la clase ModuloSEP011, para la iteracion = {}",
					iteracion);
		}
		// Si se esta en el primer periodo se recuperan los datos de la umic necesarios.
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
			// Validar parametros de entrada obligatorios
			ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_TABLACONV_ASEG1, btcUmic.getTablasConversionAsegurado().get(0));
			mapVariables.put(CLAVE_TABLACONV_ASEG2, btcUmic.getTablasConversionAsegurado().get(1));
			mapVariables.put(CLAVE_TABLACONV_ASEG3, btcUmic.getTablasConversionAsegurado().get(2));
			mapVariables.put(CLAVE_TABLACONV_ASEG4, btcUmic.getTablasConversionAsegurado().get(3));
			mapVariables.put(CLAVE_TABLACONV_ASEG5, btcUmic.getTablasConversionAsegurado().get(4));
		}

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFZCHM = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZCHM);

		if (umic.getAsegurados().getFnacAseg2() == null && umic.getAsegurados().getCsexAseg2() == null
				&& umic.getAsegurados().getEdadAseg2() == null) {
			ModuloSEP011.LOG.trace("Conyuge no informado, finalizando el subProceso para la umic");
			throw Solvencia2ExcepcionHelper.crearExcepcion(BA,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}

		if (bloqueCorriente.getFechaDevengo() != null) {

			if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
					&& umic.getAsegurados().getEdadAseg3() != null) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varIDHijo = 3;
					varHijoMinusvalido = true;
				}
			}
			if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
					&& umic.getAsegurados().getEdadAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if (umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
						varIDHijo = 4;
						varHijoMinusvalido = true;
					}
				}
			}
			if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
					&& umic.getAsegurados().getEdadAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if (umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4())) {
						varIDHijo = 5;
						varHijoMinusvalido = true;
					}
				}
			}

			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
			if (varUmic2 == null) {
				varUmic2 = new Umic();
				try {

					PropertyUtils.copyProperties(varUmic2, umic);

				} catch (Exception e) {
					ModuloSEP011.LOG.error(e.getMessage());
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
					ModuloSEP011.LOG.error(e.getMessage());
				}
				varBtcUmic2 = btcUmic;
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
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))){
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
			if (umic.getDatosGenerales().getKbencon().substring(0, 1).equals("3") 
					&& (umic.getDatosGenerales().getKprestacion().equals("RS043")
							|| umic.getDatosGenerales().getKprestacion().equals("RS044")
							|| umic.getDatosGenerales().getKprestacion().equals("RS045"))) {
				SEP011 = BigDecimal.ZERO;
				
			}else {
				if (varIDHijo == 0) {
					SEP011 = varRentaRev80.multiply(varVzc2);
				} else {

					if (varHijoMinusvalido == false) {
						BigDecimal sumRenPctHijo = varRentaRev80.add(varPctHijo);
						SEP011 = sumRenPctHijo.multiply(varVzc2);
					} else {
						varProyFzchm = proyUmic;
						varFzchm = (BigDecimal) moduloFZCHM.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc,
								varUmic2, varBtcUmic2, mapVariables, codSubproceso);

						varUmic3 = umic;
						Asegurados aseg = new Asegurados();
						aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
						aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
						aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
						varUmic3.setAsegurados(aseg);

						varBtcUmic3 = btcUmic;
						varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());

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
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))){
							if (btcUmic.getTablasConversionAsegurado() == null
									|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
										new String[] { null, "tablasConversionAsegurado" });
							}

							List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
							tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(2));

							varBtcUmic3.setTablasConversionAsegurado(tablaConvAseg);
						} else {
							List<Integer> tablaBaseExp = new ArrayList<Integer>();
							tablaBaseExp.add(btcUmic.getTablaBaseExp().get(2));
							varBtcUmic3.setTablaBaseExp(tablaBaseExp);
						}
						
						varProyVzc3 = proyUmic;
						varVzc3 = (BigDecimal) moduloVZC.execute(varProyVzc3, bloqueCorriente, iteracion, fcalc, varUmic3,
								varBtcUmic3, mapVariables, codSubproceso);

						BigDecimal hijoFzchmVzc2 = varPctHijo.multiply(varFzchm).multiply(varVzc2);
						BigDecimal resRentaMulti = varRentaRev80.add(hijoFzchmVzc2);
						BigDecimal resta = BigDecimal.ONE.subtract(varVzc2);
						BigDecimal renta50Vzc3Res = varRentaRev50.multiply(varVzc3).multiply(resta);
						BigDecimal resultadoMiembro2 = renta50Vzc3Res.multiply(resta);

						SEP011 = resRentaMulti.add(resultadoMiembro2);
					}
				}

			}
		}
		if (iteracion == proyUmic.size()) {
			umic.getAsegurados().setCsexAseg1((String) mapVariables.get(CLAVE_SEX_ASEG1));
			umic.getAsegurados().setEdadAseg1((Integer) mapVariables.get(CLAVE_EDAD_ASEG1));
			umic.getAsegurados().setFnacAseg1((Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1));
			btcUmic.setTablacalc1aseg1((String) mapVariables.get(CLAVE_TABLACALC_ASEG1));
			List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
			

			if (mapVariables.get(CLAVE_TABLACONV_ASEG1) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG1)));
			}												
			if (mapVariables.get(CLAVE_TABLACONV_ASEG2) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG2)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG3) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG3)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG4) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG4)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG5) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG5)));
			}
			
			btcUmic.setTablasConversionAsegurado(tablaConvAseg);
			
		}

		if (ModuloSEP011.LOG.isTraceEnabled()) {
			ModuloSEP011.LOG.trace(
					"Fin función << ModuloSEP011>> de la clase ModuloSEP011, para la iteracion = {}, con resultado SEP011 = {}",
					iteracion, SEP011);
		}

		return SEP011;
	}

}
