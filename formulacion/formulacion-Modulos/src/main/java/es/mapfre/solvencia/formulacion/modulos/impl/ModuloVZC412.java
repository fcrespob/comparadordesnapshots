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
 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en
 * el cálculo de la cuantía probable de la garantía
 * 
 * @author eugenio.torres
 *
 */
public class ModuloVZC412 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC412.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC412;
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
			if (ModuloVZC412.LOG.isTraceEnabled()) {
				ModuloVZC412.LOG.trace("Inicio de execute en clase VZC412");
			}

			// Recuperamos los datos que le pasaremos a la función moduloVZC412
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función moduloVZC412
			resultado = moduloVZC412(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVZC412.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC412.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZC412.LOG.isTraceEnabled()) {
			ModuloVZC412.LOG.trace("Fin de execute en clase VZC412");
		}

		return resultado;
	}

	/**
	 * Módulo que calculará la probabilidad de viudedad.
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
	 */
	private BigDecimal moduloVZC412(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		// Variables locales
		BigDecimal VZC412 = BigDecimal.ZERO;
		Modulo moduloVZC, moduloFPTOZC, moduloVZCIERTA;
		BigDecimal varFptozcj, varVzcj, varVzCiertaj = BigDecimal.ZERO;
		List<DetalleCorriente> varProyVzc = null;
		List<DetalleCorriente> varProyFptozc = null;
		List<DetalleCorriente> varProyVcierta = null;
		// Fin variables locales

		if (ModuloVZC412.LOG.isTraceEnabled()) {
			ModuloVZC412.LOG.trace("Inicio función << moduloVZC412 >> de la clase moduloVZC412, para la iteracion = {}",
					iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_A)) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GL);
		}

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTOZC);
		moduloVZCIERTA = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZCIERTA);

		if (null != bloqueCorriente.getFechaDevengo()) {
			if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals("1")) {
				if (!codSubproceso.equals(ConstantsModulos.CTE_PROY_FALL)) {
					varProyVzc = proyUmic;
					varVzcj = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic,
							btcUmic, mapVariables, codSubproceso);
					VZC412 = varVzcj;
				}
			} else if (umic.getDatosGenerales().getNorden().toString().substring(0, 1).equals("2")) {
				if (!codSubproceso.equals(ConstantsModulos.CTE_PROY_VIDA)) {
					if (umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)
							|| umic.getOtrosDatos().getCestadoAseg1().equals(ConstantsModulos.CTE_CESTADO_ASEG_S)) {
						varProyVcierta = proyUmic;
						varVzCiertaj = (BigDecimal) moduloVZCIERTA.execute(varProyVcierta, bloqueCorriente, iteracion,
								fcalc, umic, btcUmic, mapVariables, codSubproceso);
						varProyFptozc = proyUmic;
						varFptozcj = (BigDecimal) moduloFPTOZC.execute(varProyFptozc, bloqueCorriente, iteracion, fcalc,
								umic, btcUmic, mapVariables, codSubproceso);
						VZC412 = varVzCiertaj.multiply(varFptozcj);
					}
				}
			} else {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GQ);
			}
		}

		if (ModuloVZC412.LOG.isTraceEnabled()) {
			ModuloVZC412.LOG.trace(
					"Fin función << moduloVZC412 >> de la clase ModuloVZC412, para la iteracion = {}, con resultado VZC412 = {}",
					iteracion, VZC412);
		}

		return VZC412;
	}
}