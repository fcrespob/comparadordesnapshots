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
 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en el cálculo de la cuantía probable 
 * de una garantía.  
 * El módulo FZC(fcal,j) determina la probabilidad de que una cabeza de edad zc en la fecha de 
 * cálculo, fcal, fallezca a la fecha j en la que se devenga la prestación
 *
 * @author eugenio.torres
 *
 */
public class ModuloFZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFZC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FZC;
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
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
			if (ModuloFZC.LOG.isTraceEnabled()) {
				ModuloFZC.LOG.trace("Inicio de execute en clase FZC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloFZC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloFZC
			resultado = moduloFZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFZC.LOG.isTraceEnabled()) {
			ModuloFZC.LOG.trace("Fin de execute en clase FZC");
		}

		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en el cálculo de la cuantía probable 
	 * de una garantía.  
	 * El módulo FZC(fcal,j) determina la probabilidad de que una cabeza de edad zc en la fecha de 
	 * cálculo, fcal, fallezca a la fecha j en la que se devenga la prestación. 
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
	private BigDecimal moduloFZC(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal FZC = BigDecimal.ZERO;
		Modulo moduloVZC;
		List<DetalleCorriente> varProyVzc;
		BigDecimal varVzc;
		//Fin variables locales
		
		if (ModuloFZC.LOG.isTraceEnabled()) {
			ModuloFZC.LOG.trace("Inicio función << moduloFZC >> de la clase moduloFZC, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		varProyVzc = proyUmic;
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		
		varVzc = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		FZC = BigDecimal.ONE.subtract(varVzc);
		
		if (ModuloFZC.LOG.isTraceEnabled()) {
			ModuloFZC.LOG.trace("Fin función << moduloFZC >> de la clase ModuloFZC, para la iteracion = {}, con resultado FZC = {}", iteracion, FZC);
		}
		
		return FZC;
	}
}
