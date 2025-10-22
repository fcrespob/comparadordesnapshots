package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
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
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo de cálculo que recupera la provisión matemática de la garantía calculada anteriormente.
 * 
 * @author NFQ
 *
 */
public class ModuloCSPANETO implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory
			.getLogger(ModuloCSPANETO.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPANETO;
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC
			.concat(CLAVE_MODULO);
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
			if (ModuloCSPANETO.LOG.isTraceEnabled()) {
				ModuloCSPANETO.LOG.trace("Inicio de execute en clase ModuloCSPANETO");
			}

			// Recuperamos los datos que le pasaremos a la función moduloGZC010
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función de calculo GZC010
			resultado = moduloCSPANETO(proyUmic, bloqueCorriente, iteracion,
					fcalc, umic, btcUmic, mapVariables, codSubproceso);

		} catch (Solvencia2Excepcion e) {
			ModuloCSPANETO.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPANETO.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(
					ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloCSPANETO.LOG.isTraceEnabled()) {
			ModuloCSPANETO.LOG.trace("Fin de execute en clase ModuloCSPANETO");
		}

		return resultado;
	}

	/**
	 * Módulo de cálculo que recupera la provisión matemática de la garantía calculada anteriormente
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
	private BigDecimal moduloCSPANETO(final List<DetalleCorriente> proyUmic,
			final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic,
			final DetalleBaseTecnica btcUmic,
			final Map<String, Object> mapVariables, final String codSubproceso) {

		// Variables locales.
		BigDecimal cspaneto = BigDecimal.ZERO;
		
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		
		if (ModuloCSPANETO.LOG.isTraceEnabled()) {
			ModuloCSPANETO.LOG
					.trace("Inicio función << moduloCSPANETO >> de la clase ModuloCSPANETO, para la iteracion = {}",
							iteracion);
		}
		
		// Validamos los campos de entrada
		
		
		
		if (ConstantsModulos.CTE_UMIC_SECUNDARIA.equals(umic.getDatosGenerales().getSpcom())){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GO);
		}
		
		if (bloqueCorriente.getFechaDevengo()== null){
			return cspaneto;
		}
		
		if(iteracion != proyUmic.size()){
			return cspaneto;
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic,
				bloqueCorriente, fcalc, umic, btcUmic);
		
		List<DetalleCorriente> listaCorrienteUmic = null;
		
		if(btcUmic.getBt().equalsIgnoreCase(ConstantsModulos.CTE_BTI_PROY) 
				|| btcUmic.getBt().equalsIgnoreCase(ConstantsModulos.CTE_BT_NIIF17) 
				|| btcUmic.getBt().equalsIgnoreCase(ConstantsModulos.CTE_BT_NIF17LIR) 
				|| btcUmic.getBt().equalsIgnoreCase(ConstantsModulos.CTE_BT_N17LIRIN)
				|| btcUmic.getBt().equalsIgnoreCase(ConstantsModulos.CTE_BT_BEL)){
			listaCorrienteUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
					ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(),
					umic.getKey());
		}else{
			listaCorrienteUmic = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
					  ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(),
					  umic.getKey());
		}
		
		if(umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) && umic.getDatosGenerales().getTipoSubriesgo().contentEquals("UNIV")) { // subriesgo universales
			if(iteracion>listaCorrienteUmic.size()) {
 				 cspaneto = listaCorrienteUmic.get(listaCorrienteUmic.size()-1).getTotalFlujoProyeccion().getProvbtiproy();
 				 return cspaneto;
			
			}else {
				 cspaneto = listaCorrienteUmic.get(iteracion-1).getTotalFlujoProyeccion().getProvbtiproy();
				 return cspaneto;
			}
			
		}
		
		if(iteracion>listaCorrienteUmic.size())
			cspaneto = listaCorrienteUmic.get(listaCorrienteUmic.size()-1).getTotalFlujoProyeccion().getProvbtiproy();
		else
			cspaneto = listaCorrienteUmic.get(iteracion-1).getTotalFlujoProyeccion().getProvbtiproy();
		
		if (ModuloCSPANETO.LOG.isTraceEnabled()) {
			ModuloCSPANETO.LOG
					.trace("Fin función << moduloCSPANETO >> de la clase ModuloCSPANETO, para la iteracion = {} con resultado cspaneto = {}",
							iteracion, cspaneto);
		}

		return cspaneto;
	}

}
