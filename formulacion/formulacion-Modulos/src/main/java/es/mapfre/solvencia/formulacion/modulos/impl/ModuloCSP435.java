package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.impl.ObtenerDatos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Modulo de cálculo que devuelve el nominal de la modalidad 435 de indexados.
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */
public class ModuloCSP435 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP435.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP435;
	private static final String CLAVE_SPCOMP = ConstantsModulos.CTE_SPCOMP;
	private static final String CLAVE_VAR_SPCOMP = CLAVE_SPCOMP.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * es.mapfre.solvencia.formulacion.modulos.Modulo#execute(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloCSP435.LOG.isTraceEnabled()) {
				ModuloCSP435.LOG.trace("Inicio de execute en clase ModuloCSP435");
			}

			// Recuperamos los datos que le pasaremos a la función ModuloCSP435
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.

			// Invocamos a la función de calculo CSP435
			resultado = moduloCSP435(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,
					codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSP435.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP435.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSP435.LOG.isTraceEnabled()) {
			ModuloCSP435.LOG.trace("Fin de execute en clase ModuloCSP435");
		}

		return resultado;
	}

	/**
	 * Modulo de cálculo que devuelve el nominal de la modalidad 435 de indexados.
	 * 
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return
	 */
	private BigDecimal moduloCSP435(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {

		BigDecimal csp435 = BigDecimal.ZERO;
		BigDecimal varPrimaTaradaPrincipal;
		BigDecimal varPrimaTarada;
		BigDecimal varCgar;
		BigDecimal varCotizacion;
		ObtenerDatos servicio;

		if (ModuloCSP435.LOG.isTraceEnabled()) {
			ModuloCSP435.LOG.trace("Inicio función << moduloCSP435 >> de la clase ModuloCSP435, para la iteracion = {}",
					iteracion);
		}

		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (null != bloqueCorriente.getFechaDevengo()) {

		
		varCotizacion = umic.getRescates().getTirCie().divide(new BigDecimal(100));
		varPrimaTarada = umic.getPrimas().getIprimatarada();
		
		varPrimaTaradaPrincipal = (BigDecimal) mapVariables.get(CLAVE_VAR_SPCOMP);
		
		if(varPrimaTaradaPrincipal == null) {
			servicio = new ObtenerDatos();

			UmicKey k = servicio.recuperarUmicPrincipal(umic.getDatosGenerales().getKmodalidad(),
					umic.getDatosGenerales().getKpoliza(), umic.getDatosGenerales().getKsubpoliza(),
					umic.getDatosGenerales().getKcertificado(), umic.getDatosGenerales().getNsuscri(),
					umic.getDatosGenerales().getCtipoaport());
			
			Umic u = servicio.recuperarUmic(k);
			
			varPrimaTaradaPrincipal = u.getPrimas().getIprimatarada();
			
			mapVariables.put(CLAVE_VAR_SPCOMP, varPrimaTaradaPrincipal);
		}
		
		varCgar = varPrimaTarada.add(varPrimaTaradaPrincipal);

			csp435 = varCgar.multiply(varCotizacion);
		}

		if (ModuloCSP435.LOG.isTraceEnabled()) {
			ModuloCSP435.LOG.trace(
					"Fin función << moduloCSP435 >> de la clase ModuloCSP435, para la iteracion = {}, con resultado csp435 = {}",
					iteracion, csp435);
		}

		return csp435;
	}
}
