package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloVARB167 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVARB167.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VARB167;
	private static final String CLAVE_VAR_REVER = ConstantsModulos.CTE_VAR_REVER.concat(CLAVE_MODULO);
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
			if (ModuloVARB167.LOG.isTraceEnabled()) {
				ModuloVARB167.LOG.trace("Inicio de execute en clase ModuloVRTA167");
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
			resultado = moduloVARB167(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloVARB167.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVARB167.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVARB167.LOG.isTraceEnabled()) {
			ModuloVARB167.LOG.trace("Fin de execute en clase ModuloVARB167");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
	 * el cálculo de la cuantía probable de la garantía. La expresión matemática
	 * para su determinación es la siguiente: VARB167 = Vzc (fcal, j) + [REVER * Fzc
	 * (fcal, j)]
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
	private BigDecimal moduloVARB167(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal varb167 = BigDecimal.ZERO;
		BigDecimal varProyVZC;
		BigDecimal varVzc;
		BigDecimal varProyfptozc;
		BigDecimal varVcierta;
		Modulo moduloVZC;
		Modulo moduloFZC;
		Modulo moduloVZCIERTA;

		if (ModuloVARB167.LOG.isTraceEnabled()) {
			ModuloVARB167.LOG.trace(
					"Inicio función << ModuloVARB167 >> de la clase ModuloVARB167, para la iteracion = {}", iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (!(bloqueCorriente.getFechaDevengo() == null)) {

			if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_1_STRING)) {
				moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
				varVzc = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic,
						mapVariables, codSubproceso);
				varb167 = varVzc;
			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals(ConstantsFunciones.CTE_2_STRING)) {
				if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
					moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
					varVcierta = (BigDecimal) moduloVZCIERTA.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					varb167 = varVcierta;
				} else {
					moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);
					varVcierta = (BigDecimal) moduloVZCIERTA.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);

					moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
					varProyfptozc = (BigDecimal) moduloFZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc,
							umic, btcUmic, mapVariables, codSubproceso);

					varb167 = varVcierta.multiply(varProyfptozc);
				}
			} else {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GQ);
			}
		}
		
		if (ModuloVARB167.LOG.isTraceEnabled()) {
			ModuloVARB167.LOG.trace("Fin función << ModuloVARB167 >> de la clase ModuloVARB167");
		}

		return varb167;
	}

}
