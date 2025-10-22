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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo de cuantía nominal para la viudedad de la póliza 421011
 * @author eugenio.torres
 *
 */
public class ModuloSEP011MC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloSEP011MC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_SEP011MC;
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Módulo de cuantía nominal para el huérfano minusválido.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloSEP011MC.LOG.isTraceEnabled()) {
				ModuloSEP011MC.LOG.trace("Inicio de execute en clase SEP011MC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloSEP011MC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloSEP011MC
			resultado = moduloSEP011MC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloSEP011MC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloSEP011MC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloSEP011MC.LOG.isTraceEnabled()) {
			ModuloSEP011MC.LOG.trace("Fin de execute en clase SEP011MC");
		}

		return resultado;
	}
	/**
	 * Módulo de cuantía nominal para el huérfano minusválido. 
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
	private BigDecimal moduloSEP011MC(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal SEP011MC = BigDecimal.ZERO;
		BigDecimal varPctRenta, varRenta, varRentaRev = BigDecimal.ZERO, impPago;
		//Fin variables locales
		
		if (ModuloSEP011MC.LOG.isTraceEnabled()) {
			ModuloSEP011MC.LOG.trace("Inicio función << moduloSEP011MC >> de la clase moduloSEP011MC, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() == null){
			return SEP011MC;
		}
		
		varPctRenta = ConstantsFunciones.CTE_OPER_50.divide(ConstantsFunciones.CTE_OPER_100);
		varRenta = umic.getRentas().getRentact();
		
		
		if(varRenta.setScale(34, RoundingMode.HALF_UP).equals(BigDecimal.ZERO.setScale(34, RoundingMode.HALF_UP)) || null == varRenta){
			
			impPago = proyUmic.get(iteracion-1).getImpPago();
			if ( null != impPago ) {
				varRenta = impPago;
			}
			
		}	
			
		varRentaRev = varPctRenta.multiply(varRenta);
		
		//Se calculará sep011mc (j)  = varRentaRev 
		SEP011MC = varRentaRev;
							
		if (ModuloSEP011MC.LOG.isTraceEnabled()) {
			ModuloSEP011MC.LOG.trace("Fin función << moduloSEP011MC >> de la clase ModuloSEP011MC, para la iteracion = {}, con resultado SEP011V = {}", iteracion, SEP011MC);
		}
		
		return SEP011MC;
	}
}
