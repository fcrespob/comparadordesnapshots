package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class ModuloCSPVIU implements Modulo {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPVIU.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPVIU;

	private static final String CLAVE_C2S = ConstantsModulos.CTE_C2S;
	private static final String CLAVE_VAR_CSPVIU = ConstantsModulos.CTE_VAR_CSPVIU.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo de cuantía nominal para el huérfano minusválido.
	 */

	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {

		BigDecimal cspviu = BigDecimal.ZERO;

		try {
			if (ModuloCSPVIU.LOG.isTraceEnabled()) {
				ModuloCSPVIU.LOG.trace("Inicio de execute en clase ModuloCSPVIU");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSPVIU
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			cspviu = moduloCSPVIU(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloCSPVIU.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPVIU.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPVIU.LOG.isTraceEnabled()) {
			ModuloCSPVIU.LOG.trace("Fin de execute en clase ModuloCSPVIU");
		}

		return cspviu;
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

	private BigDecimal moduloCSPVIU(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		BigDecimal varcspviu = BigDecimal.ZERO;
		List<DetalleCorriente> varProyFptozc = proyUmic;
		List<DetalleCorriente> varProyC2S = proyUmic;
		BigDecimal varFptozc = BigDecimal.ZERO;
		Modulo moduloFPTOVIU, moduloCSP238;
		BigDecimal varModC2S = BigDecimal.ZERO;
		BigDecimal varModC2SM = BigDecimal.ZERO;
		String varModuloC2S = null;
		BigDecimal varcspviuant = null;
		String varTitular;
		Umic umicMensual;
		Modulo ModuloC2S = null;
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		List<DetalleCorriente> calculoProyUmicTitular;
		Modulo moduloNominalUmicTitular = null;
		List<BigDecimal> calcUmicTitular = new ArrayList<BigDecimal>();
		BigDecimal auxCalcUmicTitular = BigDecimal.ZERO;
		Timestamp varAuxFecDevengo = null;
		Timestamp varAuxFecPago = null;

		if (ModuloCSPVIU.LOG.isTraceEnabled()) {
			ModuloCSPVIU.LOG.trace(
					"Inicio función << moduloCSPVIU >> de la clase ModuloCSPVIU, para la  iteracion = {}", iteracion);
		}

		// Validamos los campos de entrada 
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
				&& umic.getDatosGenerales().getKprestacion().equals("RS217")) {
			if (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) != 6) {
				bloqueCorriente.setFechaDevengo(null);
			}
		}
		
		if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
				&& umic.getDatosGenerales().getKprestacion().equals("RS218")) {
			if (UtilFechas.getMes(bloqueCorriente.getFechaDevengo()) != 11) {
				bloqueCorriente.setFechaDevengo(null);
			}
		}

		if (bloqueCorriente.getFechaDevengo() == null) {
			return varcspviu;
		}

		// Varibales de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			varProyFptozc = proyUmic;
			varProyC2S = proyUmic;
		}

		varTitular = umic.getDatosGenerales().getKbencon();
		if (null == varTitular) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		} else if ((umic.getDatosGenerales().getKprestacion().equals("REV20")
				|| umic.getDatosGenerales().getKprestacion().equals("REV21")
				|| umic.getDatosGenerales().getKprestacion().equals("REV22")
				|| umic.getDatosGenerales().getKprestacion().equals("REV25")
				|| umic.getDatosGenerales().getKprestacion().equals("REV26")
				|| umic.getDatosGenerales().getKprestacion().equals("REV27")
				|| umic.getDatosGenerales().getKprestacion().equals("RS216")
				|| umic.getDatosGenerales().getKprestacion().equals("RS217")
				|| umic.getDatosGenerales().getKprestacion().equals("RS218")
				|| umic.getDatosGenerales().getKprestacion().equals("REV42")
				|| umic.getDatosGenerales().getKprestacion().equals("REV45")
				|| umic.getDatosGenerales().getKprestacion().equals("REV23")
				|| umic.getDatosGenerales().getKprestacion().equals("REV24"))) {

			if (null != umic.getOtrosDatos().getCestadoAseg2()) {
				if (umic.getOtrosDatos().getCestadoAseg2().equals("A")) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G8);
				}
			}

		} else if ((umic.getDatosGenerales().getKprestacion().equals("REO00")
				|| umic.getDatosGenerales().getKprestacion().equals("REO01")
				|| umic.getDatosGenerales().getKprestacion().equals("REO02")
				|| umic.getDatosGenerales().getKprestacion().equals("REO05")
				|| umic.getDatosGenerales().getKprestacion().equals("REO06")
				|| umic.getDatosGenerales().getKprestacion().equals("REO07")
				|| umic.getDatosGenerales().getKprestacion().equals("REO13")
				|| umic.getDatosGenerales().getKprestacion().equals("REO14")
				|| umic.getDatosGenerales().getKprestacion().equals("REO15"))) {

			if (null != umic.getOtrosDatos().getCestadoAseg3()) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals("A")) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G9);
				}
			}
		}

		if (varTitular.substring(0, 1).equals("3")) {
			// Si kbencon=301 ejecuta solo el csp 238 sin realizar ninguna acumulacion, ni
			// utilizando la cuantia del fptozc
			moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
			varcspviu = (BigDecimal) moduloCSP238.execute(varProyFptozc, bloqueCorriente, iteracion, fcalc, umic,
					btcUmic, mapVariables, codSubproceso);
		} else {

			switch (umic.getDatosGenerales().getKprestacion()) {
			case "REV20":
			case "REV21":
			case "REV22":
			case "REV25":
			case "REV26":
			case "REV27":
				varModuloC2S = ConstantsFactorias.MODULO_VIUE01;
				break;
			case "RS216":
			case "RS217":
			case "RS218":
				varModuloC2S = ConstantsFactorias.MODULO_VIUS02;
				break;
			case "REV42":
			case "REV45":
				varModuloC2S = ConstantsFactorias.MODULO_VIUE102;
				break;
			case "REV23":
				varModuloC2S = ConstantsFactorias.MODULO_VIUE02;
				break;
			case "REV24":
				varModuloC2S = ConstantsFactorias.MODULO_VIUE03;
				break;
			case "REO00":
			case "REO01":
			case "REO02":
			case "REO05":
			case "REO06":
			case "REO07":
			case "REO13":
			case "REO14":
			case "REO15":
				varModuloC2S = ConstantsFactorias.MODULO_ORFE01;
				break;
			}

			if (null == varModuloC2S)
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DJ,
						new Object[] { CLAVE_C2S });
			else {

				ModuloC2S = FactoriaModulos.getModulo(varModuloC2S);
				varModC2S = (BigDecimal) ModuloC2S.execute(varProyFptozc, bloqueCorriente, iteracion, fcalc, umic,
						btcUmic, mapVariables, codSubproceso);
				if (!umic.getRentas().getCpagrenta().equals("4")) {

					if (umic.getDatosGenerales().getKbencon().equals("BNC") && umic.getDatosGenerales().getKmodalidad().equals(333)
							&& (umic.getDatosGenerales().getKprestacion().equals("RS217") || umic.getDatosGenerales().getKprestacion().equals("RS218"))) {
					
						umicMensual = obtenerDatos.recuperarUmicMensualBNC(umic.getDatosGenerales().getKmodalidad(),
								umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
								umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
								umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKajuste(),
								umic.getDatosGenerales().getCtipoaport(), umic.getDatosGenerales().getKbencon(),
								"RS216");
						umicMensual.getRentas().setCpagrenta("4");
						umicMensual.getRentas().setForpagrent(12);
					}else {
						umicMensual = obtenerDatos.recuperarUmicMensual(umic.getDatosGenerales().getKmodalidad(),
							umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
							umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
							umic.getDatosGenerales().getKgarantia(), umic.getDatosGenerales().getKajuste(),
							umic.getDatosGenerales().getCtipoaport(), umic.getDatosGenerales().getKbencon(),
							ConstantsFunciones.CTE_4_STRING);
					}

					varProyC2S = servicioDatos.recuperarProyeccionCualquierNodo(btcUmic.getBt(),
							umic.getDatosGenerales().getFecCierre(), umicMensual.getKey());
					if (varProyC2S.size() == ConstantsFunciones.CTE_0) {
						
						FlujosProbablesKey key;
						if(btcUmic.getBt().equals("ROSSPGA") || btcUmic.getBt().equals("ROSSPTI")  || btcUmic.getBt().equals("ROSSPTE")){
							key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
									umicMensual.getDatosGenerales().getKgarantia(),
									umicMensual.getDatosGenerales().getKprestacion(), "ROSSP");
						}else{
							key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
									umicMensual.getDatosGenerales().getKgarantia(),
									umicMensual.getDatosGenerales().getKprestacion(), btcUmic.getBt());
						}
						
						FlujosProbables fp = flujosProbablesDao.get(key);
						if (null == fp) {
							
							if(btcUmic.getBt().equals("ROSSPGA") || btcUmic.getBt().equals("ROSSPTI")  || btcUmic.getBt().equals("ROSSPTE")){
								key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
										umicMensual.getDatosGenerales().getKgarantia(),
										umicMensual.getDatosAdicionales().getPrestCal(), "ROSSP");
							}else{
								key = new FlujosProbablesKey(umicMensual.getDatosGenerales().getKmodalidad(),
										umicMensual.getDatosGenerales().getKgarantia(),
										umicMensual.getDatosAdicionales().getPrestCal(), btcUmic.getBt());
							}
							
							fp = flujosProbablesDao.get(key);
						}

						calculoProyUmicTitular = proyUmic;
						moduloNominalUmicTitular = FactoriaModulos.getModulo(fp.getVida().getNominal());
						if (null != moduloNominalUmicTitular) {
							if ((List<BigDecimal>) mapVariables.get("CSPViuTitular") == null){
					 			varAuxFecDevengo = bloqueCorriente.getFechaDevengo();
								varAuxFecPago = bloqueCorriente.getFechaPago();
								for (int i=1; i <= calculoProyUmicTitular.size(); i++) {
									if (i == 1) {
										bloqueCorriente.setFechaDevengo(UtilFechas.plusMeses(bloqueCorriente.getFechaDevengo(), -iteracion));
										bloqueCorriente.setFechaPago(UtilFechas.plusMeses(bloqueCorriente.getFechaPago(), -iteracion));
									}else {
										bloqueCorriente.setFechaDevengo(UtilFechas.plusMeses(bloqueCorriente.getFechaDevengo(), 1));
										bloqueCorriente.setFechaPago(UtilFechas.plusMeses(bloqueCorriente.getFechaPago(), 1));
									}
									calcUmicTitular.add((BigDecimal) moduloNominalUmicTitular.execute(calculoProyUmicTitular,
											bloqueCorriente, i, fcalc, umicMensual, btcUmic, mapVariables,
											codSubproceso));
								}
								mapVariables.put("CSPViuTitular", calcUmicTitular);
								bloqueCorriente.setFechaDevengo(varAuxFecDevengo);
								bloqueCorriente.setFechaPago(varAuxFecPago);
							}
								
							calcUmicTitular =  (List<BigDecimal>) mapVariables.get("CSPViuTitular");

							varModC2SM = calcUmicTitular.get(iteracion - 1);
							varcspviu = varModC2SM;
						}
						if (null == varModC2SM) {

							varModC2SM = BigDecimal.ZERO;
							varcspviu = varModC2SM;

						}

					} else {
						varModC2SM = varProyC2S.get(iteracion - 1).getBloqueBySubproceso(codSubproceso)
								.getImpFlujoNominal();
						varcspviu = varModC2SM;
					}
				} else {
					varModC2S = (BigDecimal) ModuloC2S.execute(varProyC2S, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					// Parte funcional
					moduloFPTOVIU = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOVIU);
					if (umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_301)) {
						varFptozc = BigDecimal.ONE;
					} else {
						varFptozc = (BigDecimal) moduloFPTOVIU.execute(varProyFptozc, bloqueCorriente, iteracion, fcalc,
								umic, btcUmic, mapVariables, codSubproceso);
					}
					varcspviuant = (BigDecimal) mapVariables.get(CLAVE_VAR_CSPVIU);
					if (varcspviuant == null) {
						varcspviu = varModC2S.multiply(varFptozc);
					} else {
						varcspviu = varcspviuant.add(varModC2S.multiply(varFptozc));
					}

				}
			}

			mapVariables.put(CLAVE_VAR_CSPVIU, varcspviu);
		}

		if (ModuloCSPVIU.LOG.isTraceEnabled()) {
			ModuloCSPVIU.LOG.trace(
					"Fin de la función << moduloCSPVIU >> de la clase ModuloCSPVIU, para la iteración = {}", iteracion);
		}

		return varcspviu;
	}

}
