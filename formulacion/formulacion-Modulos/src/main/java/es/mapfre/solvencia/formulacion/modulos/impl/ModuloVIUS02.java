package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.impl.ObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloVIUS02 implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloVIUS02.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VIUS02;

	private static final String CLAVE_ANOESP = ConstantsModulos.CTE_ANOESP;
	private static final String CLAVE_VAR_ANOESP = CLAVE_ANOESP.concat(CLAVE_MODULO);
	private static final String CLAVE_P1JUB = ConstantsModulos.CTE_VAR_P1JUB;
	private static final String CLAVE_VAR_P1JUB = CLAVE_P1JUB.concat(CLAVE_MODULO);
	private static final String CLAVE_P2VIV = ConstantsModulos.CTE_VAR_P2VIV;
	private static final String CLAVE_VAR_P2VIV = CLAVE_P2VIV.concat(CLAVE_MODULO);
	private static final String CLAVE_PRPSS_2001 = ConstantsModulos.CTE_PRPSS_2001;
	private static final String CLAVE_PRPSS_2002 = ConstantsModulos.CTE_PRPSS_2002;
	private static final String CLAVE_PRPSS_REST = ConstantsModulos.CTE_PRPSS_REST;
	private static final String CLAVE_VAR_PRPSS_2001 = CLAVE_PRPSS_2001.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_2002 = CLAVE_PRPSS_2002.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRPSS_REST = CLAVE_PRPSS_REST.concat(CLAVE_MODULO);
	private static final String CLAVE_P3TOP = ConstantsModulos.CTE_VAR_P3TOP;
	private static final String CLAVE_MUS = ConstantsModulos.CTE_VAR_MUS;
	private static final String CLAVE_VAR_P3TOP = CLAVE_P3TOP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_MUS = CLAVE_MUS.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para viudedado.
	 */

	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal cspvius02 = BigDecimal.ZERO;

		try {
			if (ModuloVIUS02.LOG.isTraceEnabled()) {
				ModuloVIUS02.LOG.trace("Inicio de execute en clase ModuloVIUS02");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSPVIUE01
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			cspvius02 = moduloVIUS02(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVIUS02.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVIUS02.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVIUS02.LOG.isTraceEnabled()) {
			ModuloVIUS02.LOG.trace("Fin de execute en clase ModuloVIUS02");
		}

		return cspvius02;
	}

	/**
	 * Módulo de cuantía nominal para viudedad
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

	private BigDecimal moduloVIUS02(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		BigDecimal varvius02 = BigDecimal.ZERO;
		BigDecimal varPct = BigDecimal.ZERO;
		String varAnoEspStr, varP1JUBStr, varP2VIVStr, varMUSStr, varP3TOPStr;
		BigDecimal varAnoEsp = BigDecimal.ZERO, varP1JUB = BigDecimal.ZERO, varP2VIV = BigDecimal.ZERO,
				varP3TOP = BigDecimal.ZERO, varMUS = BigDecimal.ZERO;

		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();

		Fecha varFecJ;
		Integer varAnoj;
		BigDecimal varPSJj = BigDecimal.ZERO, varPSVj = BigDecimal.ZERO, varC1sant = BigDecimal.ZERO;
		BigDecimal varNumPrestaciones;
		BigDecimal varNs = BigDecimal.ZERO;
		String varPrpssStr2001, varPrpssStr2002, varPrpssStrREST;
		BigDecimal varPrpss2001 = BigDecimal.ZERO, varPrpss2002 = BigDecimal.ZERO, varPrpssREST = BigDecimal.ZERO;
		BigDecimal auxPRPSS2001 = BigDecimal.ZERO;
		BigDecimal auxPRPSS2002 = BigDecimal.ZERO;
		BigDecimal auxPRPSSREST = BigDecimal.ZERO;
		List<Umic> umics;
		BigDecimal opeMusDiveNS = BigDecimal.ZERO;

		if (ModuloVIUS02.LOG.isTraceEnabled()) {
			ModuloVIUS02.LOG.trace(
					"Inicio función << ModuloVIUS02 >> de la clase ModuloVIUS01, para la  iteracion = {}", iteracion);
		}
		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Fin de la definición de las variables auxiliares.

		// Varibales de apoyo
		varPct = ConstantsFunciones.CTE_OPER_75.divide(ConstantsFunciones.CTE_OPER_100);
		varAnoEspStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_ANOESP,
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_ANOESP);
		varP1JUBStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P1JUB,
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P1JUB);
		varP2VIVStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P2VIV,
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P2VIV);
		varP3TOPStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_P3TOP,
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_P3TOP);
		varMUSStr = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_MUS,
				umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
				umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(), CLAVE_MUS);

		if (null == varAnoEspStr) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
					new Object[] { CLAVE_ANOESP });
		} else {
			varAnoEsp = new BigDecimal(varAnoEspStr);
		}

		if (null == varP1JUBStr) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
					new Object[] { CLAVE_P1JUB });

		} else {
			varP1JUB = new BigDecimal(varP1JUBStr).setScale(2, RoundingMode.HALF_UP);
			varP1JUB = varP1JUB.divide(new BigDecimal("100"));
		}
		if (null == varP2VIVStr) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
					new Object[] { CLAVE_P2VIV });
		} else {
			varP2VIV = new BigDecimal(varP2VIVStr).setScale(2, RoundingMode.HALF_UP);
			varP2VIV = varP2VIV.divide(new BigDecimal("100"));
		}
		if (null == varP3TOPStr) {

			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
					new Object[] { CLAVE_P3TOP });

		} else {
			varP3TOP = new BigDecimal(varP3TOPStr).setScale(2, RoundingMode.HALF_UP);
			varP3TOP = varP3TOP.divide(new BigDecimal("100"));
		}

		varC1sant = varP3TOP.divide(ConstantsFunciones.CTE_OPER_14, ConstantsFunciones.MATH_CONTEXT);

		if (null == varMUSStr) {

			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
					new Object[] { CLAVE_MUS });
		}else {
			varMUS = new BigDecimal(varMUSStr).setScale(2, RoundingMode.HALF_UP);
			varMUS = varMUS.divide(new BigDecimal("100"));
		}
		
		varNumPrestaciones = BigDecimal.ONE;

		if (umic.getAsegurados().getFnacAseg2() != null) {
			varNumPrestaciones = varNumPrestaciones.add(BigDecimal.ONE);
		}

		if (umic.getAsegurados().getFnacAseg3() != null) {
			varNumPrestaciones = varNumPrestaciones.add(BigDecimal.ONE);
		}

		if (umic.getAsegurados().getFnacAseg4() != null) {
			varNumPrestaciones = varNumPrestaciones.add(BigDecimal.ONE);
		}

		if (umic.getAsegurados().getFnacAseg5() != null) {
			varNumPrestaciones = varNumPrestaciones.add(BigDecimal.ONE);
		}

		if (umic.getRentas().getForpagrent() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DK);
		}
		
		varNs = varNumPrestaciones.multiply(new BigDecimal((umic.getRentas().getForpagrent())));
		
		if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
				&& umic.getDatosGenerales().getKprestacion().equals("RS216")) {
			varNs = new BigDecimal(14);
			umic.getRentas().setCpagrenta("4");
			umic.getRentas().setForpagrent(12);
		}
		
		if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
				&& umic.getDatosGenerales().getKprestacion().equals("RS217")) {
			varNs = new BigDecimal(14);
			umic.getRentas().setCpagrenta("1");
			umic.getRentas().setForpagrent(1);
		}
		
		if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
				&& umic.getDatosGenerales().getKprestacion().equals("RS218")) {
			varNs = new BigDecimal(14);
			umic.getRentas().setCpagrenta("1");
			umic.getRentas().setForpagrent(1);
		}

		if (varAnoEsp.equals(new BigDecimal(2001))) {
			varPrpssStr2001 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2001,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_2001);
			if (null == varPrpssStr2001) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_2001 });

			} else {

				varPrpss2001 = UtilModulos.StringToBigDecimal(varPrpssStr2001);

			}
			varPrpssStr2002 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2002,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_2002);
			if (null == varPrpssStr2002) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_2002 });

			} else {

				varPrpss2002 = UtilModulos.StringToBigDecimal(varPrpssStr2002);

			}
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_REST);
			if (null == varPrpssStrREST) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_REST });

			} else {

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		} else if (varAnoEsp.equals(new BigDecimal(2002))) {
			varPrpssStr2002 = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_2002,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_2002);
			if (null == varPrpssStr2002) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_2002 });

			} else {

				varPrpss2002 = UtilModulos.StringToBigDecimal(varPrpssStr2002);

			}
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_REST);
			if (null == varPrpssStrREST) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_REST });

			} else {

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		} else if (varAnoEsp.compareTo(new BigDecimal(2002)) == 1) {
			varPrpssStrREST = UtilModulos.getDatosEspecificosUmic(mapVariables, CLAVE_VAR_PRPSS_REST,
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					CLAVE_PRPSS_REST);
			if (null == varPrpssStrREST) {

				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_PRPSS_REST });

			} else {

				varPrpssREST = UtilModulos.StringToBigDecimal(varPrpssStrREST);

			}
		}
		if (bloqueCorriente.getFechaDevengo() != null) {

			varFecJ = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
			varAnoj = UtilFechas.getAnio(varFecJ.toTimestamp());

			if (varNs == BigDecimal.ZERO) {
				varPSJj = BigDecimal.ZERO;
				varPSVj = BigDecimal.ZERO;
				
			}else {
				if (varAnoEsp.equals(new BigDecimal(2001))) {
					auxPRPSS2001 = BigDecimal.ONE.add(varPrpss2001);
					auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);

					varPSJj = (varP1JUB.multiply(auxPRPSS2001).multiply(auxPRPSS2002)
						.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue() - 2))).divide(varNs,
								ConstantsFunciones.MATH_CONTEXT);
					varPSVj = (varP2VIV.multiply(auxPRPSS2001).multiply(auxPRPSS2002)
						.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue() - 2))).divide(varNs,
								ConstantsFunciones.MATH_CONTEXT);

				} else if (varAnoEsp.equals(new BigDecimal(2002))) {
					auxPRPSS2002 = BigDecimal.ONE.add(varPrpss2002);
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);
				
					varPSJj = (varP1JUB.multiply(auxPRPSS2002)
						.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue() - 1))).divide(varNs,
								ConstantsFunciones.MATH_CONTEXT);
					varPSVj = (varP2VIV.multiply(auxPRPSS2002)
						.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue() - 1))).divide(varNs,
								ConstantsFunciones.MATH_CONTEXT);

				} else if (varAnoEsp.compareTo(new BigDecimal(2002)) == 1) {
					auxPRPSSREST = BigDecimal.ONE.add(varPrpssREST);

					varPSJj = (varP1JUB.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue()))).divide(varNs,
						ConstantsFunciones.MATH_CONTEXT);
					varPSVj = (varP2VIV.multiply(auxPRPSSREST.pow(varAnoj - varAnoEsp.intValue()))).divide(varNs,
						ConstantsFunciones.MATH_CONTEXT);

				}
			}
			if (varP3TOP == BigDecimal.ZERO || varP3TOPStr == null) {
				varC1sant = varPSJj;
			}
			if (varP3TOP.compareTo(BigDecimal.ZERO) > 0) {
				varC1sant = varP3TOP.divide(ConstantsFunciones.CTE_OPER_14, ConstantsFunciones.MATH_CONTEXT);
				
			}
			if (varNs != BigDecimal.ZERO) {
				opeMusDiveNS = varMUS.divide(varNs, ConstantsFunciones.MATH_CONTEXT);
			}
			
			varvius02 = varPct.multiply(opeMusDiveNS.add(varC1sant)).subtract(varPSVj);

			if (varvius02.add(varPSVj).compareTo(varPct.multiply(varPSJj)) == -1) {
				varvius02 = varPct.multiply(varPSJj).subtract(varPSVj);
			}
		}
		if (ModuloVIUS02.LOG.isTraceEnabled()) {
			ModuloVIUS02.LOG.trace(
					"Fin de la función << moduloVIUS02 >> de la clase ModuloVIUS02, para la iteración = {}", iteracion);
		}
		
		return varvius02;
	}

}
