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
 * Clase que implementa el modulo GZC012.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic
 * La expresión matemática para su determinación es la siguiente:
 * 				GZC(012, ZC) = 
 * @author apedro
 *
 */
public class ModuloGZC012 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC012.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC012;

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
			
			if (ModuloGZC012.LOG.isTraceEnabled()) {
				ModuloGZC012.LOG.trace("Inicio de execute en clase ModuloGZC012");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC012
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo moduloGZC012
			resultado = moduloGZC012(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC012.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC012.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC012.LOG.isTraceEnabled()) {
			ModuloGZC012.LOG.trace("Fin de execute en clase ModuloGZC012");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				GZC(012, ZC) = 
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
	 * @return BigDecimal
	 */
	private BigDecimal moduloGZC012(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal gzc012 = BigDecimal.ZERO;
		BigDecimal varGipc;
		//Fin variables locales
		
		if (ModuloGZC012.LOG.isTraceEnabled()) {
			ModuloGZC012.LOG.trace("Inicio función << moduloGZC012 >> de la clase ModuloGZC012, para la iteracion = {}", iteracion);
		}
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return gzc012;
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varGipc = btcUmic.getGtorosspPrima().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
		
		if(null == proyUmic.get(iteracion-1).getBloquePrim()){
			
			gzc012 = BigDecimal.ZERO;	
			
		}else{
			
			gzc012 = varGipc.multiply(proyUmic.get(iteracion-1).getBloquePrim().getImpFlujoNominal());
		}
		
		if (ModuloGZC012.LOG.isTraceEnabled()) {
			ModuloGZC012.LOG.trace("Fin función << moduloGZC012 >> de la clase ModuloGZC012, para la iteracion = {} con resultado gzc012 = {}", iteracion, gzc012);
		}
			
		return gzc012;
	}

}
