package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.joda.time.LocalDateTime;
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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
 * el cálculo de la cuantía probable de la garantía. Es una adaptación del
 * módulo VRTA162 para adaptarlo a las carteras de árboles.
 * 
 * @author Belén P.A. Sáenz
 */
public class ModuloVARB162 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVARB162.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VARB162;
	private static final String CLAVE_VAR_FIN_RVC = ConstantsModulos.CTE_VA_FIN_RVC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_UMIC_COPIA = ConstantsModulos.CTE_VA_UMIC_COPIA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_BTC_UMIC_COPIA = ConstantsModulos.CTE_VA_BTCUMIC_COPIA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_IND_TABLA_ORIGEN = ConstantsModulos.CTE_VA_VARINDTABLA_ORIGEN
			.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_IND_TABLA_DESTINO = ConstantsModulos.CTE_VA_VARINDTABLA_DESTINO
			.concat(CLAVE_MODULO);
	private static final String VAR_BTCUMIC_COPIADA = "SW_BTCUMIC";
	private static final String CLAVE_VAR_BTCUMIC_COPIADA = VAR_BTCUMIC_COPIADA.concat(CLAVE_MODULO);

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
			if (ModuloVARB162.LOG.isTraceEnabled()) {
				ModuloVARB162.LOG.trace("Inicio de execute en clase ModuloVARB162");
			}

			// Recuperamos los datos que le pasaremos a la función moduloVARB162
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función ModuloVARB162
			resultado = moduloVARB162(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVARB162.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVARB162.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVARB162.LOG.isTraceEnabled()) {
			ModuloVARB162.LOG.trace("Fin de execute en clase ModuloVARB162");
		}

		return resultado;
	}

	/**
	 * Devuelve los factores de probabilización a aplicar en el cálculo de la
	 * cuantía probable de la garantía.
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
	 */
	private BigDecimal moduloVARB162(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal varb162 = BigDecimal.ZERO;
		BigDecimal varindTablaOrigen;
		BigDecimal varindTablaDestino;

		BigDecimal varVzc;

		List<DetalleCorriente> varProyVzc;
		List<DetalleCorriente> varProyVzcierta;

		Timestamp varFinRvc;
		BigDecimal varVcierta;

		Modulo moduloVZCIERTA;
		Modulo moduloVZC;

		Umic varUmicCopia;
		DetalleBaseTecnica varbtcUmicCopia;

		String sw_btcumic_copiada;
		String tablaOrigen;
		Integer varIndAsegOrigen;
		Timestamp fNacAsegOrigen;
		String sexAsegOrigen;
		Integer edadAsegOrigen;
		// Fin variables locales

		if (ModuloVARB162.LOG.isTraceEnabled()) {
			ModuloVARB162.LOG.trace(
					"Inicio función << ModuloVARB162 >> de la clase ModuloVARB162, para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (null == bloqueCorriente.getFechaDevengo()) {
			return varb162;
		}

		varUmicCopia = (Umic) mapVariables.get(CLAVE_VAR_UMIC_COPIA);
		if (null == varUmicCopia) {
			varUmicCopia = new Umic();
			try {
				PropertyUtils.copyProperties(varUmicCopia, umic);
			} catch (Exception e) {
				ModuloVARB162.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_UMIC_COPIA, varUmicCopia);
		}

		varbtcUmicCopia = (DetalleBaseTecnica) mapVariables.get(CLAVE_VAR_BTC_UMIC_COPIA);
		if (null == varbtcUmicCopia) {
			varbtcUmicCopia = new DetalleBaseTecnica();
			try {
				PropertyUtils.copyProperties(varbtcUmicCopia, btcUmic);
			} catch (Exception e) {
				ModuloVARB162.LOG.error(e.getMessage());
			}
			mapVariables.put(CLAVE_VAR_BTC_UMIC_COPIA, varbtcUmicCopia);
		}

		// varindTablaOrigen = BigDecimal.ZERO;
		// mapVariables.put(CLAVE_VAR_IND_TABLA_ORIGEN, varindTablaOrigen);
		// varindTablaDestino = BigDecimal.ZERO;
		// mapVariables.put(CLAVE_VAR_IND_TABLA_DESTINO, varindTablaDestino);

		if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsModulos.CTE_2)) {
			// Sobreescribimos el asegurado 1 con los datos del asegurado 2
			sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
			if (null == sw_btcumic_copiada) {
				varIndAsegOrigen = 2;
				tablaOrigen = btcUmic.getTablacalc1aseg2();
				fNacAsegOrigen = umic.getAsegurados().getFnacAseg2();
				sexAsegOrigen = umic.getAsegurados().getCsexAseg2();
				edadAsegOrigen = umic.getAsegurados().getEdadAseg2();
				UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
						fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
				mapVariables.put(CLAVE_VAR_BTC_UMIC_COPIA, varbtcUmicCopia);
				mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
			}
		} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsModulos.CTE_3)) {
			// Sobreescribimos el asegurado 1 con los datos del asegurado 3
			sw_btcumic_copiada = (String) mapVariables.get(CLAVE_VAR_BTCUMIC_COPIADA);
			if (null == sw_btcumic_copiada) {
				tablaOrigen = btcUmic.getTablacalc1aseg3();
				varIndAsegOrigen = 3;
				fNacAsegOrigen = umic.getAsegurados().getFnacAseg3();
				sexAsegOrigen = umic.getAsegurados().getCsexAseg3();
				edadAsegOrigen = umic.getAsegurados().getEdadAseg3();
				UtilModulos.fSobreescribirAsegurado(varUmicCopia, varbtcUmicCopia, tablaOrigen, varIndAsegOrigen,
						fNacAsegOrigen, sexAsegOrigen, edadAsegOrigen);
				mapVariables.put(CLAVE_VAR_BTC_UMIC_COPIA, varbtcUmicCopia);
				mapVariables.put(CLAVE_VAR_BTCUMIC_COPIADA, "S");
			}
		}

		varFinRvc = (Timestamp) mapVariables.get(CLAVE_VAR_FIN_RVC);
		if (null == varFinRvc) {
			if (!(null == umic.getFechas().getFecafinfinancia())) {
				varFinRvc = llevarFecha(umic.getFechas().getFecafinfinancia());
				mapVariables.put(CLAVE_VAR_FIN_RVC, varFinRvc);
			}
		}

		if (null == varFinRvc) {
			varProyVzc = proyUmic;
			moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
			varb162 = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, varUmicCopia,
					varbtcUmicCopia, mapVariables, codSubproceso);
		} else if (!bloqueCorriente.getFechaDevengo().after(varFinRvc)) {
			varProyVzcierta = proyUmic;
			moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
			varb162 = (BigDecimal) moduloVZCIERTA.execute(varProyVzcierta, bloqueCorriente, iteracion, fcalc,
					varUmicCopia, varbtcUmicCopia, mapVariables, codSubproceso);
		} else {
			varProyVzc = proyUmic;
			moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
			varb162 = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, varUmicCopia,
					varbtcUmicCopia, mapVariables, codSubproceso);
		}

		if (ModuloVARB162.LOG.isTraceEnabled()) {
			ModuloVARB162.LOG.trace("Fin de la función << ModuloVARB162 >> de la clase ModuloVARB162");
		}

		return varb162;
	}

	/**
	 * Si el mes de la fecha <> 02 Nos llevaremos la fecha varFinRvc a día 30
	 * del mes/año. En caso contrario Nos llevaremos la fecha varFinRvc a día 28
	 * del mes/año.
	 * 
	 * @param fecaFinFinancia
	 */
	private Timestamp llevarFecha(Timestamp fecha) {
		if (ModuloVARB162.LOG.isTraceEnabled()) {
			ModuloVARB162.LOG.trace("Inicio de la función << llevarFecha >> de la clase ModuloVARB162");
		}

		LocalDateTime dt = null;

		dt = new LocalDateTime(fecha.getTime());

		if (dt.dayOfMonth().getMaximumValue() == ConstantsFunciones.CTE_29
				|| dt.dayOfMonth().getMaximumValue() == ConstantsFunciones.CTE_28) {
			dt = dt.withDayOfMonth(ConstantsFunciones.CTE_28);
		} else {
			dt = dt.withDayOfMonth(ConstantsFunciones.CTE_30);
		}

		if (ModuloVARB162.LOG.isTraceEnabled()) {
			ModuloVARB162.LOG.trace(
					"Fin de la función << llevarFecha >> de la clase ModuloVARB162 , con resultado varM = {}",
					new Timestamp(dt.toDateTime().getMillis()));
		}

		return new Timestamp(dt.toDateTime().getMillis());
	}
}