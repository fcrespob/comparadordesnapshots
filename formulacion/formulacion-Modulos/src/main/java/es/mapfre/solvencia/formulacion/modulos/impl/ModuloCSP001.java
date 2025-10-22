package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

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
 * Clase encargada del cálculo que devuelve el capital de Cartera.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP(001,tc) = Capital(TC)
 * 
 * @author agonzalezgar
 *
 */
public class ModuloCSP001 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP001.class);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP001;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP001.LOG.isTraceEnabled()) {
				ModuloCSP001.LOG.trace("Inicio de execute en clase ModuloCSP001");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			//final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función de calculo CSP001
			resultado = moduloCSP001(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP001.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP001.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP001.LOG.isTraceEnabled()) {
			ModuloCSP001.LOG.trace("Fin de execute en clase ModuloCSP001");
		}
		
		return resultado;
		
	}
	
	/**
	 * Modulo de cálculo que devuelve el capital de Cartera
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic)
	 * @param fcalc
	 * 			Fecha de calculo
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private BigDecimal moduloCSP001(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic) {
		//Variables locales
		BigDecimal csp001 = BigDecimal.ZERO;
		//Fin variables locales

		if (ModuloCSP001.LOG.isTraceEnabled()) {
			ModuloCSP001.LOG.trace("Inicio función << moduloCSP001 >> para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		if (null != bloqueCorriente.getFechaDevengo()) {
			csp001 = umic.getCapitales().getIcapact();
		}

		if (ModuloCSP001.LOG.isTraceEnabled()) {
			ModuloCSP001.LOG.trace("Fin función << moduloCSP001 >> de la clase ModuloCSP001, para la iteracion = {}, con resultado capitalCartera = {}", iteracion, csp001);
		}
		
		return csp001;
	}

}
