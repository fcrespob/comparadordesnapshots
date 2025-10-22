package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSPORFA implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPORFA.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPORFA;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSPORFA = ConstantsModulos.CTE_VAR_CSPORFA.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC_2.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC3 = ConstantsModulos.CTE_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC_2.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC3 = ConstantsModulos.CTE_BTC_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2_FALL = ConstantsModulos.CTE_UMIC2_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2_FALL = ConstantsModulos.CTE_BTC_UMIC_2_FALL.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FEC_J_1 = ConstantsModulos.VAR_FEC_J_1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TAB_ASEG_X = ConstantsModulos.VAR_TAB_ASEG_X.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1 = ConstantsModulos.CTE_TABLACONV_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2 = ConstantsModulos.CTE_TABLACONV_ASEG2.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3 = ConstantsModulos.CTE_TABLACONV_ASEG3.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4 = ConstantsModulos.CTE_TABLACONV_ASEG4.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5 = ConstantsModulos.CTE_TABLACONV_ASEG5.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1_POS0_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG1_POS0_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1_POS1_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG1_POS1_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1_POS2_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG1_POS2_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2_POS0_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG2_POS0_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2_POS1_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG2_POS1_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2_POS2_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG2_POS2_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3_POS0_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG3_POS0_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3_POS1_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG3_POS1_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3_POS2_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG3_POS2_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4_POS0_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG4_POS0_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4_POS1_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG4_POS1_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4_POS2_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG4_POS2_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5_POS0_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG5_POS0_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5_POS1_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG5_POS1_TABLAINI.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5_POS2_TABLAINI = ConstantsModulos.CTE_TABLACONV_ASEG5_POS2_TABLAINI.concat(CLAVE_MODULO);

	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para el huérfano minusválido.
	 */

	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal csporfa = BigDecimal.ZERO;

		try {
			if (ModuloCSPORFA.LOG.isTraceEnabled()) {
				ModuloCSPORFA.LOG.trace("Inicio de execute en clase ModuloCSPORFA");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSPORFA
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			csporfa = moduloCSPORFA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSPORFA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPORFA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPORFA.LOG.isTraceEnabled()) {
			ModuloCSPORFA.LOG.trace("Fin de execute en clase ModuloCSPORFA");
		}

		return csporfa;
	}

	/**
	 * Módulo de cuantía nominal para el huérfano minusválido
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param mapVariables
	 * @return
	 */

	private BigDecimal moduloCSPORFA(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		String varTitular;
		Integer varIdHijos = 0;
		Timestamp varFecNac = null;
		String varMinusvalido = "N";
		Timestamp varFechaEfecto = null;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varI2PorcentajeMasUno;
		BigDecimal varM = BigDecimal.ZERO;
		BigDecimal varJ1 = BigDecimal.ZERO;
		BigDecimal varTcyVidaj = BigDecimal.ZERO;
		String varCriFec;
		List<DetalleCorriente> varProyVzc2Fall = proyUmic;
		List<DetalleCorriente> varProyVzc2Sup = proyUmic;
		List<DetalleCorriente> varProyVzc3Sup = proyUmic;
		List<DetalleCorriente> varProy238 = proyUmic;
		BigDecimal varcsporfa_jant = BigDecimal.ZERO;
		BigDecimal varcsporfaj = BigDecimal.ZERO;
		BigDecimal varcsporfa = BigDecimal.ZERO;
		Timestamp varfecJ1 = null, varfecvcto = null;
		BigDecimal varVzc2Fallj = BigDecimal.ZERO;
		BigDecimal varVzc2Supj = BigDecimal.ZERO;
		BigDecimal varVzc3Supj = BigDecimal.ZERO;
		BigDecimal varVzc3Fallj = BigDecimal.ZERO;
		BigDecimal varCsp238j = BigDecimal.ZERO;
		BigDecimal varVVidaj = BigDecimal.ZERO;
		Modulo moduloVZC3, moduloVZC2, moduloVZC;
		Modulo moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
		Umic varUmic2, varUmic3, varUmic2Fall;
		DetalleBaseTecnica varBtcUmic2, varBtcUmic3, varBtcUmic2Fall;
		Timestamp varFecJInicial = null;
		BigDecimal sumatorio = BigDecimal.ZERO;
		String tablaX = null;

		if (ModuloCSPORFA.LOG.isTraceEnabled()) {
			ModuloCSPORFA.LOG.trace(
					"Inicio función << ModuloCSPORFA >> de la clase ModuloCSPORFA, para la  iteracion = {}", iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Varibales de apoyo
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			varcsporfa_jant = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_CSPORFA, varcsporfa_jant);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			if (btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG1_POS0_TABLAINI, btcUmic.getTablasConversionAsegurado().get(0).get(0).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(0).get(1).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG1_POS1_TABLAINI, btcUmic.getTablasConversionAsegurado().get(0).get(1).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(0).get(2).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG1_POS2_TABLAINI, btcUmic.getTablasConversionAsegurado().get(0).get(2).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(1).get(0).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG2_POS0_TABLAINI, btcUmic.getTablasConversionAsegurado().get(1).get(0).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(1).get(1).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG2_POS1_TABLAINI, btcUmic.getTablasConversionAsegurado().get(1).get(1).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(1).get(2).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG2_POS2_TABLAINI, btcUmic.getTablasConversionAsegurado().get(1).get(2).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(2).get(0).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG3_POS0_TABLAINI, btcUmic.getTablasConversionAsegurado().get(2).get(0).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(2).get(1).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG3_POS1_TABLAINI, btcUmic.getTablasConversionAsegurado().get(2).get(1).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(2).get(2).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG3_POS2_TABLAINI, btcUmic.getTablasConversionAsegurado().get(2).get(2).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(3).get(0).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG4_POS0_TABLAINI, btcUmic.getTablasConversionAsegurado().get(3).get(0).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(3).get(1).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG4_POS1_TABLAINI, btcUmic.getTablasConversionAsegurado().get(3).get(1).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(3).get(2).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG4_POS2_TABLAINI, btcUmic.getTablasConversionAsegurado().get(3).get(2).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(4).get(0).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG5_POS0_TABLAINI, btcUmic.getTablasConversionAsegurado().get(4).get(0).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(4).get(1).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG5_POS1_TABLAINI, btcUmic.getTablasConversionAsegurado().get(4).get(1).getTablaInicio());
			}
			if (btcUmic.getTablasConversionAsegurado().get(4).get(2).getTablaInicio() != null) {
				mapVariables.put(CLAVE_TABLACONV_ASEG5_POS2_TABLAINI, btcUmic.getTablasConversionAsegurado().get(4).get(2).getTablaInicio());
			}
			mapVariables.put(CLAVE_TABLACONV_ASEG1, btcUmic.getTablasConversionAsegurado().get(0));
			mapVariables.put(CLAVE_TABLACONV_ASEG2, btcUmic.getTablasConversionAsegurado().get(1));
			mapVariables.put(CLAVE_TABLACONV_ASEG3, btcUmic.getTablasConversionAsegurado().get(2));
			mapVariables.put(CLAVE_TABLACONV_ASEG4, btcUmic.getTablasConversionAsegurado().get(3));
			mapVariables.put(CLAVE_TABLACONV_ASEG5, btcUmic.getTablasConversionAsegurado().get(4));
		}

		if (bloqueCorriente.getFechaDevengo() == null) {
			return varcsporfa;
		}

		varTitular = umic.getDatosGenerales().getKbencon();
		if (null == varTitular) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		}
		if (varTitular.substring(0, 1).equals(ConstantsModulos.CTE_3)) {
			//varcsporfa = (BigDecimal) moduloCSP238.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
			//		mapVariables, codSubproceso);
			varcsporfa = BigDecimal.ZERO;
		} else {
			if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSI_NO_RED)) {
				if (umic.getDatosGenerales().getCnegocio().contentEquals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)) {
					varFechaEfecto = umic.getFechas().getFecefecini();
				} else {
					varFechaEfecto = umic.getFechas().getFecinisus();
				}
			} else if (umic.getDatosGenerales().getCsitupol().equals(ConstantsModulos.CTE_DG_CSITU_RED)) {
				varFechaEfecto = umic.getFechas().getFecefecred();
			}

			varI1 = btcUmic.getItcalc().get(0);
			if (btcUmic.getBt().equals("ROSSP") || btcUmic.getBt().equals("ROSSPCSM") || btcUmic.getBt().equals("ROSSPTI") || btcUmic.getBt().equals("ROSSPTE") || btcUmic.getBt().equals("ROSSPGA"))
				varI2 = umic.getBti().getPintertecnI2();
			else {
				varI2 = btcUmic.getItcalc().get(1);
			}
			varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
			varM = FuncionesAuxiliares.nAnnos(btcUmic.getFecfintramo().get(0), btcUmic.getFecInitramo().get(0),
					varCriFec);
			if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
					&& umic.getAsegurados().getEdadAseg3() != null) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals("M")) {
					varIdHijos = 3;
					varFecNac = umic.getAsegurados().getFnacAseg3();
					varMinusvalido = "S";
				}
			}
			if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
					&& umic.getAsegurados().getEdadAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals("M")) {
					if (umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
						varIdHijos = 4;
						varFecNac = umic.getAsegurados().getFnacAseg4();
						varMinusvalido = "S";
					}
				}
			}
			if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
					&& umic.getAsegurados().getEdadAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals("M")) {
						if ((umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg3()))
								&& (umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4()))) {
							varIdHijos = 5;
							varFecNac = umic.getAsegurados().getFnacAseg5();
							varMinusvalido = "S";
						}
				}
			}
			if (varIdHijos == 0 || varMinusvalido.equals("N")) {
				return varcsporfa;
			} else {

				// Calculo vzc3 fallecimiento
				varUmic3 = (Umic) mapVariables.get(CLAVE_UMIC3);
				if (null == varUmic3) {
					varUmic3 = new Umic();
					try {
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varUmic3, umic);
					} catch (Exception e) {
						ModuloCSPORFA.LOG.error(e.getMessage());
					}
					mapVariables.put(CLAVE_UMIC3, varUmic3);
				}

				varBtcUmic3 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC3);
				tablaX = (String) mapVariables.get(CLAVE_VAR_TAB_ASEG_X);
				
				if (null != tablaX) {
					varBtcUmic3.setTablacalc1aseg1(tablaX);
					varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
							.setTablaInicio(tablaX);
					varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
							.setTablaFin(tablaX);
				}
				
				if (null == varBtcUmic3) {
					varBtcUmic3 = new DetalleBaseTecnica();

					try {
						// y btcUmic (con este método no se clonan las listas)
						PropertyUtils.copyProperties(varBtcUmic3, btcUmic);

					} catch (Exception e) {
						ModuloCSPORFA.LOG.error(e.getMessage());
					}

					if (varIdHijos == 3) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc1aseg3(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg3(), umic.getAsegurados().getCsexAseg3(),
								umic.getAsegurados().getEdadAseg3());
					} else if (varIdHijos == 4) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc1aseg4(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg4(), umic.getAsegurados().getCsexAseg4(),
								umic.getAsegurados().getEdadAseg4());
					} else if (varIdHijos == 5) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc1aseg5(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg5(), umic.getAsegurados().getCsexAseg5(),
								umic.getAsegurados().getEdadAseg5());
					}

					if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
							&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))) {
						if (btcUmic.getTablasConversionAsegurado() == null
								|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
							throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
									new String[] { null, "tablasConversionAsegurado" });
						}

						List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
						tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(varIdHijos - 1));

						varBtcUmic3.setTablasConversionAsegurado(tablaConvAseg);
					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(btcUmic.getTablaBaseExp().get(varIdHijos - 1));
						varBtcUmic3.setTablaBaseExp(tablaBaseExp);
					}
					
					tablaX = varBtcUmic3.getTablacalc1aseg1();
					mapVariables.put(CLAVE_VAR_TAB_ASEG_X, tablaX);
					mapVariables.put(CLAVE_BTC_UMIC3, varBtcUmic3);
				}

				moduloVZC3 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc3Fallj = (BigDecimal) moduloVZC3.execute(varProyVzc3Sup, bloqueCorriente, iteracion, fcalc,
						varUmic3, varBtcUmic3, mapVariables, codSubproceso);

				// Calculo VZC2 fallecimiento
				varUmic2Fall = (Umic) mapVariables.get(CLAVE_UMIC2_FALL);
				if (null == varUmic2Fall) {
					varUmic2Fall = new Umic();
					try {

						PropertyUtils.copyProperties(varUmic2Fall, umic);

					} catch (Exception e) {
						ModuloCSPORFA.LOG.error(e.getMessage());
					}

					Asegurados aseg = new Asegurados();

					aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
					aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
					aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
					varUmic2Fall.setAsegurados(aseg);

					mapVariables.put(CLAVE_UMIC2_FALL, varUmic2Fall);

				}

				varBtcUmic2Fall = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2_FALL);
				if (null == varBtcUmic2Fall) {
					varBtcUmic2Fall = new DetalleBaseTecnica();

					try {

						PropertyUtils.copyProperties(varBtcUmic2Fall, btcUmic);

					} catch (Exception e) {
						ModuloCSPORFA.LOG.error(e.getMessage());
					}

					varBtcUmic2Fall.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());

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

						varBtcUmic2Fall.setTablasConversionAsegurado(tablaConvAseg);
					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
						varBtcUmic2Fall.setTablaBaseExp(tablaBaseExp);
					}

					mapVariables.put(CLAVE_BTC_UMIC2_FALL, varBtcUmic2Fall);

				}

				moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc2Fallj = (BigDecimal) moduloVZC2.execute(varProyVzc2Fall, bloqueCorriente, iteracion, fcalc,
						varUmic2Fall, varBtcUmic2Fall, mapVariables, codSubproceso);

				if (null != CLAVE_VAR_FEC_J_1) {
					varFecJInicial = proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
					mapVariables.put(CLAVE_VAR_FEC_J_1, varFecJInicial);
				}

				int iteracionBucleModulos = iteracion;

				for (int j = iteracion - 1; j < proyUmic.size(); j++) {
					
					varCsp238j = (BigDecimal) moduloCSP238.execute(varProy238,
							proyUmic.get(j).getBloqueBySubproceso(codSubproceso), iteracionBucleModulos, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					if (varCsp238j.equals(BigDecimal.ZERO)) {
						varVVidaj = BigDecimal.ZERO;
					} else {
						varfecJ1 = proyUmic.get(j).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
						varJ1 = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varfecJ1, varCriFec);
						varTcyVidaj = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varFecJInicial,
								varCriFec);
						varVVidaj = FuncionesActualizacionFinanciera.vVida(varJ1, varI2, varI2, varM, varTcyVidaj,
								varI2PorcentajeMasUno, varI2PorcentajeMasUno);

						if (varIdHijos == 3) {
							asignacionTablaSupervivencia(btcUmic.getTablacalc2aseg3(), varBtcUmic3, umic, varUmic3,
									umic.getAsegurados().getFnacAseg3(), umic.getAsegurados().getCsexAseg3(),
									umic.getAsegurados().getEdadAseg3());
						} else if (varIdHijos == 4) {
							asignacionTablaSupervivencia(btcUmic.getTablacalc2aseg4(), varBtcUmic3, umic, varUmic3,
									umic.getAsegurados().getFnacAseg4(), umic.getAsegurados().getCsexAseg4(),
									umic.getAsegurados().getEdadAseg4());
						} else if (varIdHijos == 5) {
							asignacionTablaSupervivencia(btcUmic.getTablacalc2aseg5(), varBtcUmic3, umic, varUmic3,
									umic.getAsegurados().getFnacAseg5(), umic.getAsegurados().getCsexAseg5(),
									umic.getAsegurados().getEdadAseg5());

						}

						varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
								.setTablaInicio(varBtcUmic3.getTablacalc1aseg1());
						varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
								.setTablaFin(varBtcUmic3.getTablacalc1aseg1());

						varVzc3Supj = (BigDecimal) moduloVZC3.execute(varProyVzc3Sup,
								proyUmic.get(j).getBloqueBySubproceso(codSubproceso), iteracionBucleModulos, proyUmic.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getFechaDevengo(),
								varUmic3, varBtcUmic3, mapVariables, codSubproceso);

						varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
						if (null == varUmic2) {
							// Se clona la Umic y la btcUmic para asignar los
							// datos del
							// asegurado2 al asegurado1 de la nueva Umic y
							// btcUmic
							// UtilModulos.clonarUmicBtcumic(mapVariables,
							// CLAVE_UMIC,
							// CLAVE_BTC_UMIC, varUmic2, varBtcUmic2);

							varUmic2 = new Umic();
							try {
								// Se clona la Umic y la btcUmic para asignar
								// los datos
								// del asegurado2 al asegurado1 de la nueva Umic
								// y btcUmic (con este método no se clonan las
								// listas)
								PropertyUtils.copyProperties(varUmic2, umic);

							} catch (Exception e) {
								ModuloCSPORFA.LOG.error(e.getMessage());
							}
							mapVariables.put(CLAVE_UMIC2, varUmic2);
						}
						Asegurados aseg = new Asegurados();

						aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
						aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
						aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
						varUmic2.setAsegurados(aseg);

						varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
						if (null == varBtcUmic2) {
							varBtcUmic2 = new DetalleBaseTecnica();

							try {
								// Se clona la Umic y la btcUmic para asignar
								// los datos
								// del asegurado2 al asegurado1 de la nueva Umic
								// y btcUmic (con este método no se clonan las
								// listas)
								PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

							} catch (Exception e) {
								ModuloCSPORFA.LOG.error(e.getMessage());
							}
							
							mapVariables.put(CLAVE_UMIC2, varUmic2);

							if (btcUmic.getTablacalc2aseg2() == null || btcUmic.getTablacalc2aseg2().equals("00000")) {
								if (umic.getAsegurados().getCsexAseg2().equals("M")) {
									varBtcUmic2.setTablacalc1aseg1("741");
								} else {
									varBtcUmic2.setTablacalc1aseg1("740");
								}
							} else {
								varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc2aseg2());
							}

							varBtcUmic2.getTablasConversionAsegurado().get(0).get(0)
							.setTablaInicio(varBtcUmic2.getTablacalc1aseg1());
							varBtcUmic2.getTablasConversionAsegurado().get(0).get(0)
							.setTablaFin(varBtcUmic2.getTablacalc1aseg1());

							mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);

						}

						moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
						varVzc2Supj = (BigDecimal) moduloVZC.execute(varProyVzc2Sup, proyUmic.get(j).getBloqueBySubproceso(codSubproceso), iteracion, fcalc,
								varUmic2, varBtcUmic2, mapVariables, codSubproceso);
						varVzc3Supj = (BigDecimal) moduloVZC3.execute(varProyVzc3Sup,
								proyUmic.get(j).getBloqueBySubproceso(codSubproceso), iteracionBucleModulos, fcalc,
								varUmic3, varBtcUmic3, mapVariables, codSubproceso);
						
						
						sumatorio = sumatorio.add(varCsp238j.multiply(varVzc3Supj)
								.multiply(BigDecimal.ONE.subtract((varVzc2Fallj).multiply(varVzc2Supj)))
								.multiply(varVVidaj));
						iteracionBucleModulos++;
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
				
				if (mapVariables.get(CLAVE_TABLACONV_ASEG1_POS0_TABLAINI) != null) {
					tablaConvAseg.get(0).get(0).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS0_TABLAINI));
					tablaConvAseg.get(0).get(0).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS0_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG1_POS1_TABLAINI) != null) {
					tablaConvAseg.get(0).get(1).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS1_TABLAINI));
					tablaConvAseg.get(0).get(1).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS1_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG1_POS2_TABLAINI) != null) {
					tablaConvAseg.get(0).get(2).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS2_TABLAINI));
					tablaConvAseg.get(0).get(2).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG1_POS2_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG2_POS0_TABLAINI) != null) {
					tablaConvAseg.get(1).get(0).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS0_TABLAINI));
					tablaConvAseg.get(1).get(0).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS0_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG2_POS1_TABLAINI) != null) {
					tablaConvAseg.get(1).get(1).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS1_TABLAINI));
					tablaConvAseg.get(1).get(1).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS1_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG2_POS2_TABLAINI) != null) {
					tablaConvAseg.get(1).get(2).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS2_TABLAINI));
					tablaConvAseg.get(1).get(2).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG2_POS2_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG3_POS0_TABLAINI) != null) {
					tablaConvAseg.get(2).get(0).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS0_TABLAINI));
					tablaConvAseg.get(2).get(0).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS0_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG3_POS1_TABLAINI) != null) {
					tablaConvAseg.get(2).get(1).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS1_TABLAINI));
					tablaConvAseg.get(2).get(1).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS1_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG3_POS2_TABLAINI) != null) {
					tablaConvAseg.get(2).get(2).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS2_TABLAINI));
					tablaConvAseg.get(2).get(2).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG3_POS2_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG4_POS0_TABLAINI) != null) {
					tablaConvAseg.get(3).get(0).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS0_TABLAINI));
					tablaConvAseg.get(3).get(0).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS0_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG4_POS1_TABLAINI) != null) {
					tablaConvAseg.get(3).get(1).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS1_TABLAINI));
					tablaConvAseg.get(3).get(1).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS1_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG4_POS2_TABLAINI) != null) {
					tablaConvAseg.get(3).get(2).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS2_TABLAINI));
					tablaConvAseg.get(3).get(2).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG4_POS2_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG5_POS0_TABLAINI) != null) {
					tablaConvAseg.get(4).get(0).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS0_TABLAINI));
					tablaConvAseg.get(4).get(0).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS0_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG5_POS1_TABLAINI) != null) {
					tablaConvAseg.get(4).get(1).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS1_TABLAINI));
					tablaConvAseg.get(4).get(1).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS1_TABLAINI));
				}
				if (mapVariables.get(CLAVE_TABLACONV_ASEG5_POS2_TABLAINI) != null) {
					tablaConvAseg.get(4).get(2).setTablaInicio((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS2_TABLAINI));
					tablaConvAseg.get(4).get(2).setTablaFin((String) mapVariables.get(CLAVE_TABLACONV_ASEG5_POS2_TABLAINI));
				}
				
				btcUmic.setTablasConversionAsegurado(tablaConvAseg);
			}
			
			if (ModuloCSPORFA.LOG.isTraceEnabled()) {
				ModuloCSPORFA.LOG.trace(
						"Fin de la función << varcsporfa >> de la clase varcsporfa, para la iteración = {}", iteracion);
			}
			
			varcsporfa = varVzc3Fallj.multiply(sumatorio);
		}
		
		return varcsporfa;
	}

	private void asignacionTablaSupervivencia(String tablaAseg, DetalleBaseTecnica varBtcUmic3, Umic umic,
			Umic varUmic3, Timestamp fechaNac, String sexAseg, Integer edadAseg) {
		if (tablaAseg == null || tablaAseg.equals("00000")) {
			if (umic.getAsegurados().getCsexAseg3().equals("M")) {
				varBtcUmic3.setTablacalc1aseg1("741");
			} else {
				varBtcUmic3.setTablacalc1aseg1("740");
			}
		} else {
			varBtcUmic3.setTablacalc1aseg1(tablaAseg);
		}

		varUmic3.getAsegurados().setCsexAseg1(sexAseg);
		varUmic3.getAsegurados().setFnacAseg1(fechaNac);
		varUmic3.getAsegurados().setEdadAseg1(edadAseg);

	}

}
