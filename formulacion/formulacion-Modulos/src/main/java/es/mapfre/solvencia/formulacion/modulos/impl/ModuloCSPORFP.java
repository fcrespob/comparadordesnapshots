package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.InfoPtipoKey;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.InfoPtipo;
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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class ModuloCSPORFP implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPORFP.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPORFP;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSPORFP = ConstantsModulos.CTE_VAR_CSPORFP.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC_2.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC3 = ConstantsModulos.CTE_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC_2.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC3 = ConstantsModulos.CTE_BTC_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_EDAD_CAL1 = ConstantsModulos.CTE_EDAD_CAL1.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_W = ConstantsModulos.CTE_VAR_W.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TAB_ASEG_X = ConstantsModulos.VAR_TAB_ASEG_X.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_FEC_J_1 = ConstantsModulos.VAR_FEC_J_1.concat(CLAVE_MODULO);
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

		BigDecimal csporfp = BigDecimal.ZERO;

		try {
			if (ModuloCSPORFP.LOG.isTraceEnabled()) {
				ModuloCSPORFP.LOG.trace("Inicio de execute en clase ModuloCSPORFP");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSPORF
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			csporfp = moduloCSPORFP(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSPORFP.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPORFP.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPORFP.LOG.isTraceEnabled()) {
			ModuloCSPORFP.LOG.trace("Fin de execute en clase ModuloCSPORFP");
		}

		return csporfp;
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

	private BigDecimal moduloCSPORFP(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		String varTitular;
		String varMinusvalido = "N";
		Integer varNHijos = 0;
		Integer varIdHijo = 0;
		String tablaX = null;
		Timestamp varFechaEfecto = null, varfecvcto = null, varfecvitalicia = null;
		BigDecimal varI1 = BigDecimal.ZERO;
		BigDecimal varI2 = BigDecimal.ZERO;
		BigDecimal varI2PorcentajeMasUno;
		BigDecimal varM = BigDecimal.ZERO;
		BigDecimal varJ1 = BigDecimal.ZERO;
		BigDecimal varTcyVidaj = BigDecimal.ZERO;
		String varCriFec;
		List<DetalleCorriente> varProyVzc3Sup = proyUmic;
		List<DetalleCorriente> varProy238 = proyUmic;
		BigDecimal varcsporfp_jant = BigDecimal.ZERO;
		BigDecimal varcsporfp = BigDecimal.ZERO;
		Timestamp varfecJ, varfecJ1 = null;
		Timestamp varFecJInicial = null;
		BigDecimal varVzc3j = BigDecimal.ZERO;
		BigDecimal varVzc3Supj = BigDecimal.ZERO;
		BigDecimal varCsp238j = BigDecimal.ZERO;
		BigDecimal varVVidaj = BigDecimal.ZERO;
		Modulo moduloVZC3;
		Modulo moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
		Umic varUmic3 = new Umic();
		DetalleBaseTecnica varBtcUmic3 = new DetalleBaseTecnica();
		Integer varEdadCalc1;
		String varCriEdad;
		Integer varAnoNac3, varAnoNac4, varAnoNac5;
		Integer varW;
		BigDecimal sumatorio = BigDecimal.ZERO;
		Umic copiUmic;
		String varSexAseg1;
		String varEdadAseg1;
		String varFnacAseg1;
		String varTablaCalcAseg1;
		//Map<umicKey, InfoPtipo> copiUmic = UmicDao.getMap();
		
		FachadaServicios.getObtenerConfiguracion();

		if (ModuloCSPORFP.LOG.isTraceEnabled()) {
			ModuloCSPORFP.LOG.trace(
					"Inicio función << ModuloCSPORF >> de la clase ModuloCSPORFP, para la  iteracion = {}", iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Varibales de apoyo
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			varNHijos = 0;
			varcsporfp_jant = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_CSPORFP, varcsporfp_jant);
			//mapVariables.get(Umic)
			/*copiUmic = new Umic();
			try {
				PropertyUtils.copyProperties(copiUmic, umic);
			} catch (Exception e) {
				ModuloCSPORFP.LOG.error(e.getMessage());
			}*/
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
			
			
			//copiUmic.put(CLAVE_CAPIAUMIC, copiUmic);
		}
		

		if (bloqueCorriente.getFechaDevengo() == null) {
			return varcsporfp;
		}

		varTitular = umic.getDatosGenerales().getKbencon();
		if (null == varTitular) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		}
		if (varTitular.substring(0, 1).equals(ConstantsModulos.CTE_3)) {
			//varcsporfp = (BigDecimal) moduloCSP238.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
			//		mapVariables, codSubproceso);
			varcsporfp= BigDecimal.ZERO;
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
			UtilModulos.getNumPorcentajeMasUno(varI1, mapVariables, CLAVE_MODULO);
			varI2PorcentajeMasUno = UtilModulos.getNumPorcentajeMasUno(varI2, mapVariables, CLAVE_MODULO);
			varM = FuncionesAuxiliares.nAnnos(btcUmic.getFecfintramo().get(0), btcUmic.getFecInitramo().get(0),
					varCriFec);

			varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD,
					umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIEDAD);
			if (umic.getAsegurados().getFnacAseg3() != null) {

				if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto,
							umic.getAsegurados().getFnacAseg3(), varCriEdad, umic.getRentas().getFecIni(),
							umic.getDatosGenerales().getEdifer()).intValue();
					varAnoNac3 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg3());
					varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac3, umic, btcUmic,
							IObtenerConfiguracion.OrdenAsegurado.ASEG3);
					varfecvitalicia = UtilFechas.incrAnyo(varFechaEfecto, varW - varEdadCalc1);
					varfecvcto = varfecvitalicia;
					varIdHijo = 3;
					varMinusvalido = "S";
					varNHijos++;
					btcUmic.getTablacalc1aseg3();
				}
			}

			if (umic.getAsegurados().getFnacAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varMinusvalido = "S";
					varNHijos++;
					if (umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
						varIdHijo = 4;
						btcUmic.getTablacalc1aseg4();
						varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto,
								umic.getAsegurados().getFnacAseg4(), varCriEdad, umic.getRentas().getFecIni(),
								umic.getDatosGenerales().getEdifer()).intValue();
						varAnoNac4 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg4());
						varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac4, umic, btcUmic,
								IObtenerConfiguracion.OrdenAsegurado.ASEG4);
						varfecvitalicia = UtilFechas.incrAnyo(varFechaEfecto, varW - varEdadCalc1);
						varfecvcto = varfecvitalicia;
					}
				}
			}

			if (umic.getAsegurados().getFnacAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varMinusvalido = "S";
					varNHijos++;
					if ((umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg3()))
							&& (umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4()))) {
						varIdHijo = 5;
						btcUmic.getTablacalc1aseg5();
						varEdadCalc1 = UtilModulos.getVarEdadCalc(mapVariables, CLAVE_VAR_EDAD_CAL1, varFechaEfecto,
								umic.getAsegurados().getFnacAseg5(), varCriEdad, umic.getRentas().getFecIni(),
								umic.getDatosGenerales().getEdifer()).intValue();
						varAnoNac5 = UtilFechas.getAnio(umic.getAsegurados().getFnacAseg5());
						varW = UtilModulos.getVarEdadMaxima2(mapVariables, CLAVE_VAR_W, varAnoNac5, umic, btcUmic,
								IObtenerConfiguracion.OrdenAsegurado.ASEG5);
						varfecvitalicia = UtilFechas.incrAnyo(varFechaEfecto, varW - varEdadCalc1);
						varfecvcto = varfecvitalicia;
					}
				}
			}
			if (varNHijos == 0 || varMinusvalido.equals("N")) {
				return varcsporfp;
			}

			varfecJ = bloqueCorriente.getFechaDevengo();
			if (varfecJ == null || varNHijos == 0) {
			} else {
				moduloVZC3 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);

				varUmic3 = (Umic) mapVariables.get(CLAVE_UMIC3);
				if (null == varUmic3) {

					varUmic3 = new Umic();
					try {
						PropertyUtils.copyProperties(varUmic3, umic);
					} catch (Exception e) {
						ModuloCSPORFP.LOG.error(e.getMessage());
					}
					mapVariables.put(CLAVE_UMIC3, varUmic3);
				}

				varBtcUmic3 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC3);
				tablaX = (String) mapVariables.get(CLAVE_VAR_TAB_ASEG_X);
				if (null != tablaX) {
					varBtcUmic3.setTablacalc1aseg1(tablaX);
					varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
							.setTablaInicio(varBtcUmic3.getTablacalc1aseg1());
					varBtcUmic3.getTablasConversionAsegurado().get(0).get(0)
							.setTablaFin(varBtcUmic3.getTablacalc1aseg1());
				}
				if (null == varBtcUmic3) {
					varBtcUmic3 = new DetalleBaseTecnica();

					try {
						PropertyUtils.copyProperties(varBtcUmic3, btcUmic);

					} catch (Exception e) {
						ModuloCSPORFP.LOG.error(e.getMessage());
					}

					if (varIdHijo == 3) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc1aseg3(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg3(), umic.getAsegurados().getCsexAseg3(),
								umic.getAsegurados().getEdadAseg3());
					} else if (varIdHijo == 4) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc1aseg4(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg4(), umic.getAsegurados().getCsexAseg4(),
								umic.getAsegurados().getEdadAseg4());
					} else if (varIdHijo == 5) {
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
						tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(varIdHijo - 1));

						varBtcUmic3.setTablasConversionAsegurado(tablaConvAseg);

					} else {
						List<Integer> tablaBaseExp = new ArrayList<Integer>();
						tablaBaseExp.add(btcUmic.getTablaBaseExp().get(varIdHijo - 1));
						varBtcUmic3.setTablaBaseExp(tablaBaseExp);
					}
					tablaX = varBtcUmic3.getTablacalc1aseg1();
					mapVariables.put(CLAVE_VAR_TAB_ASEG_X, tablaX);
					mapVariables.put(CLAVE_BTC_UMIC3, varBtcUmic3);
				}

				varVzc3j = (BigDecimal) moduloVZC3.execute(varProyVzc3Sup, bloqueCorriente, iteracion, fcalc, varUmic3,
						varBtcUmic3, mapVariables, codSubproceso);

				int iteracionBucleModulos = iteracion;

				if (null != CLAVE_VAR_FEC_J_1) {
					varFecJInicial = proyUmic.get(iteracion - 1).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
					mapVariables.put(CLAVE_VAR_FEC_J_1, varFecJInicial);
				}

				for (int j = iteracion - 1; j < proyUmic.size() && proyUmic.get(j) != null
						&& proyUmic.get(j).getFechaHasta().before(varfecvcto); j++) {

					varfecJ1 = proyUmic.get(j).getBloqueBySubproceso(codSubproceso).getFechaDevengo();
					varJ1 = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varfecJ1, varCriFec);
					varTcyVidaj = FuncionesAuxiliares.nAnnos(umic.getFechas().getFecinisus(), varFecJInicial,
							varCriFec);
					varVVidaj = FuncionesActualizacionFinanciera.vVida(varJ1, varI2, varI2, varM, varTcyVidaj,
							varI2PorcentajeMasUno, varI2PorcentajeMasUno);

					if (varIdHijo == 3) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc2aseg3(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg3(), umic.getAsegurados().getCsexAseg3(),
								umic.getAsegurados().getEdadAseg3());
					} else if (varIdHijo == 4) {
						asignacionTablaSupervivencia(btcUmic.getTablacalc2aseg4(), varBtcUmic3, umic, varUmic3,
								umic.getAsegurados().getFnacAseg4(), umic.getAsegurados().getCsexAseg4(),
								umic.getAsegurados().getEdadAseg4());
					} else if (varIdHijo == 5) {
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
					varCsp238j = (BigDecimal) moduloCSP238.execute(varProy238,
							proyUmic.get(j).getBloqueBySubproceso(codSubproceso), iteracionBucleModulos, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					sumatorio = sumatorio.add(varVVidaj.multiply(varCsp238j).multiply(varVzc3Supj));
					iteracionBucleModulos++;
				}

				varcsporfp = new BigDecimal(varNHijos).multiply(varVzc3j).multiply(sumatorio);
			}
			
			if (iteracion == proyUmic.size()) {
				//umic 
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

			if (ModuloCSPORFP.LOG.isTraceEnabled()) {
				ModuloCSPORFP.LOG.trace(
						"Fin de la función << varcsporf >> de la clase varcsporf, para la iteración = {}", iteracion);
			}

		}
		
		return varcsporfp;
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
