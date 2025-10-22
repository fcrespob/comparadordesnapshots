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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Modulo de cálculo que devuelve el saldo de Cartera
 * 
 * @author NFQ
 */
public class ModuloCSPSALDO implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPSALDO.class);

	// incio de las variables estaticas
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPSALDO;

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	@SuppressWarnings("unchecked")
	public Object execute(Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloCSPSALDO.LOG.isTraceEnabled()) {
				ModuloCSPSALDO.LOG.trace("Inicio de execute en clase ModuloCSPSALDO");
			}

			// Recuperamos los datos que le pasaremos a la función moduloCSPSALDO
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];

			// Invocamos a la función moduloCSPSALDO
			resultado = moduloCSPSALDO(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);
		} catch (Solvencia2Excepcion e) {
			ModuloCSPSALDO.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPSALDO.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPSALDO.LOG.isTraceEnabled()) {
			ModuloCSPSALDO.LOG.trace("Fin de execute en clase ModuloCSPSALDO");
		}

		return resultado;
	}

	/**
	 * @param proyUmic      Estructura detalleCorrientes de la umic
	 * @param iteracion     Indica el periodo de proyección J que se está calculando
	 *                      de entre todos los periodos de proyección de la umic
	 *                      (proyUmic)
	 * @param Fcalc         Fecha de Cálculo
	 * @param umic          Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic       Contiene el detalle de la base técnica de cálculo para
	 *                      la umic.
	 * @param codSubproceso Código el subproceso que se está ejecutando.
	 */
	private BigDecimal moduloCSPSALDO(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente,
			final Integer iteracion, final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {
		// Variables locales
		BigDecimal cspsaldo = BigDecimal.ZERO;
		// Fin de variables locales

		if (ModuloCSPSALDO.LOG.isTraceEnabled()) {
			ModuloCSPSALDO.LOG.trace(
					"Inicio de la función << moduloCSPSALDO >> de la clase moduloCSPSALDO, para la iteración = {}",
					iteracion);
		}

		// Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if ( bloqueCorriente.getFechaDevengo() == null ) {
			return cspsaldo;
		}
		

		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			cspsaldo = umic.getCapitales().getIsaldo();
		}

		if (ModuloCSPSALDO.LOG.isTraceEnabled()) {
			ModuloCSPSALDO.LOG.trace(
					"Fin de la función << moduloCSPSALDO >> de la clase moduloCSPSALDO, para la iteración = {}",
					iteracion);
		}

		return cspsaldo;
	}
}