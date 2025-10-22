package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calculará VZC de viudedad tomando el conyuge o el hijo.
 * @author 
 *
 */
public class ModuloVZCREV implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZCREV.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZCREV;
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
			if (ModuloVZCREV.LOG.isTraceEnabled()) {
				ModuloVZCREV.LOG.trace("Inicio de execute en clase VZCREV");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVZC2
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloVZCREV
			resultado = moduloVZCREV(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZCREV.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZCREV.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZCREV.LOG.isTraceEnabled()) {
			ModuloVZCREV.LOG.trace("Fin de execute en clase VZCREV");
		}

		return resultado;
	}
	/**
	 * Módulo que calculará VZC de viudedad tomando el conyuge o el hijo.
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
	private BigDecimal moduloVZCREV(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		//Variables locales
		BigDecimal VZCREV = BigDecimal.ZERO;
		Modulo moduloVZC2 = null;
		Modulo moduloVZC3 = null;
		//Fin variables locales
		
		if (ModuloVZCREV.LOG.isTraceEnabled()) {
			ModuloVZCREV.LOG.trace("Inicio función << moduloVZCREV >> de la clase moduloVZCREV, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		if(null == umic.getOtrosDatos().getCestadoAseg2() && null == umic.getOtrosDatos().getCestadoAseg3()){
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G6);	
		
		}else if(null != umic.getOtrosDatos().getCestadoAseg2() && umic.getOtrosDatos().getCestadoAseg2().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)){
		
			moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC2);
			VZCREV = (BigDecimal) moduloVZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
		
		}else{
		
			if(null != umic.getOtrosDatos().getCestadoAseg3() && umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_V)){
				moduloVZC3 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC3);
				VZCREV = (BigDecimal) moduloVZC3.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables,codSubproceso);
			}
		
		}
		if (ModuloVZCREV.LOG.isTraceEnabled()) {
			ModuloVZCREV.LOG.trace("Fin función << moduloVZCREV >> de la clase ModuloVZCREV, para la iteracion = {}, con resultado VZCREV = {}", iteracion, VZCREV);
		}
		
		return VZCREV;
	}
}