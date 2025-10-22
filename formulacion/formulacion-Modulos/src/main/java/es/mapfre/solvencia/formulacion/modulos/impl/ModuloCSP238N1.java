package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloCSP238N1 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP238N1.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP238N1;
	// private static final String CLAVE_CALCULO_CSP463 = CLAVE_MODULO;

	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINI = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO);

	private static final String CLAVE_CSP238 = ConstantsModulos.CTE_VAR_CSP238;
	private static final String CLAVE_VAR_CSP238 = CLAVE_CSP238.concat(CLAVE_MODULO);

	// Fin de las variables estáticas usadas para agilizar operaciones.

	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {

			if (ModuloCSP238N1.LOG.isTraceEnabled()) {
				ModuloCSP238N1.LOG.trace("Inicio de execute en clase ModuloCSP238N1");
			}

			// Recuperamos los datos que le pasaremos a la función moduloCSP463
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo ModuloCSP463
			resultado = moduloCSP238N1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP238N1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP238N1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP238N1.LOG.isTraceEnabled()) {
			ModuloCSP238N1.LOG.trace("Fin de execute en clase ModuloCSP238N1");
		}

		return resultado;
	}

	private BigDecimal moduloCSP238N1(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		final IObtenerDatos servicioDatos = FachadaServicios.getObtenerDatos();
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		BigDecimal csp238N1 = BigDecimal.ZERO;
		String varCriterioFecha;
		Timestamp varFechaEfecto;
		Integer varTCm;
		Umic umicTitular,umicSec;
		Integer varTCmini;
		BigDecimal varPU;
		BigDecimal varCsp238;
		BigDecimal varCsp238Temp = BigDecimal.ZERO;
		BigDecimal varCsp238N1;
		BigDecimal varFlujoNominal_Nodo1 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyC2S = proyUmic;
		BigDecimal varFlujoNominal_Nodo2 = BigDecimal.ZERO;
		// Fin variables locales

		if (ModuloCSP238N1.LOG.isTraceEnabled()) {
			ModuloCSP238N1.LOG.trace(
					"Inicio función << ModuloCSP238N1 >> de la clase ModuloCSP238N1, para la iteracion = {}",
					iteracion);
		}

		// Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (bloqueCorriente.getFechaDevengo() == null) {
			return BigDecimal.ZERO;
		}

		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC,
				umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(),
				umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la
			// forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
			// Fin de la obtención y validación del criterio de fecha.
		}

		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_1_STRING)) {
			
			if (!umic.getRentas().getCpagrenta().equals("4")) {
				varFlujoNominal_Nodo1 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubproceso);
			} else {
				if (iteracion == 1) {
					varFlujoNominal_Nodo1 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
							mapVariables, codSubproceso);
				} else {
					if (umic.getRentas().getCformaRevrenta().contentEquals("G")
							&& umic.getRentas().getTempVit().equals("L")) {
						varFlujoNominal_Nodo1 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
								mapVariables, codSubproceso);
					} else {
						varFlujoNominal_Nodo1 = calcularCSP238(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
								mapVariables, codSubproceso);
					}
					
				}
			}

			csp238N1 = varFlujoNominal_Nodo1;
		}

		else if(umic.getDatosGenerales().getNorden().toString().substring(0,1).equals(ConstantsFunciones.CTE_2_STRING)) {

			umicTitular = obtenerDatos.recuperarUmicTitular(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					ConstantsModulos.CTE_KBENCON_UMIC_PPAL, umic.getRentas().getFecIni(), umic.getRentas().getForpagrent(),
					umic.getRentas().getCpagrenta());
			if (umicTitular == null) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DL,
						new String[] { umic.getDatosGenerales().getKmodalidad().toString(),
								umic.getDatosGenerales().getKpoliza().toString(),
								umic.getDatosGenerales().getKsubpoliza().toString(),
								umic.getDatosGenerales().getKcertificado().toString(),
								umic.getDatosGenerales().getNsuscri().toString() });
			}
			varProyC2S = servicioDatos.recuperarProyeccion(btcUmic.getBt(), umic.getDatosGenerales().getFecCierre(),
					umicTitular.getKey());
			if (umicTitular.getRentas().getCformaRevrenta().contentEquals("G")) {
				if (varProyC2S.isEmpty()) {
					if (iteracion > proyUmic.size()) {
						varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, proyUmic.size(), fcalc, umic, btcUmic,
								mapVariables, codSubproceso);
					} else {
						varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
								mapVariables, codSubproceso);
					}
				}else {
					varFlujoNominal_Nodo2 = calcularCSP238(varProyC2S, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
							mapVariables, codSubproceso);
				}
			} else {

				if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_12)) {
					if (varProyC2S.isEmpty()) {
						if (iteracion > proyUmic.size()) {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, proyUmic.size(), fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						} else {
							if (iteracion == 1) {
								varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
										mapVariables, codSubproceso);
							} else {
								varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
										mapVariables, codSubproceso);
							}
							
						}
						
					}else {
						varFlujoNominal_Nodo2 = varProyC2S.get(iteracion - 1).getBloqueVida().getImpFlujoNominal();
					}
				} else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_1)) {
					if (varProyC2S.isEmpty()) {
						if (iteracion == 1) {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						} else {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						}
						
					}else {
						varFlujoNominal_Nodo2 = varProyC2S.get(iteracion - 1).getBloqueVida().getImpFlujoNominal();
					}
				} else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_2)) {
					if (varProyC2S.isEmpty()) {
						if (iteracion == 1) {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						} else {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						}
						
					}else {
						varFlujoNominal_Nodo2 = varProyC2S.get(iteracion - 1).getBloqueVida().getImpFlujoNominal();
					}
				} else if (umicTitular.getRentas().getForpagrent().equals(ConstantsFunciones.CTE_4)) {
					if (varProyC2S.isEmpty()) {
						if (iteracion == 1) {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						} else {
							varFlujoNominal_Nodo2 = calcularCSP238(proyUmic, bloqueCorriente, iteracion - 1, fcalc, umic, btcUmic,
									mapVariables, codSubproceso);
						}		
					}else {
						varFlujoNominal_Nodo2 = varProyC2S.get(iteracion - 1).getBloqueVida().getImpFlujoNominal();
					}
				}
			}

			csp238N1 = varFlujoNominal_Nodo2;
		}

		if(ModuloCSP238N1.LOG.isTraceEnabled()){
			ModuloCSP238N1.LOG.trace(
					"Fin función << ModuloCSP238N1 >> de la clase ModuloCSP238N1, para la iteracion = {} con resultado csp238N1 = {}",
					iteracion, csp238N1);
		}
		return csp238N1;
		}

		private BigDecimal calcularCSP238(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente,
				Integer iteracion, Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
				String codSubproceso) {

			Modulo moduloCSP238;
			BigDecimal varCsp238Temp = BigDecimal.ZERO;
			BigDecimal varFlujoNominal_Vida = BigDecimal.ZERO;
			
			varCsp238Temp = proyUmic.get(iteracion - 1).getImpPago();
			return varCsp238Temp;
		}
}
