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
 * Clase que implementa el modulo GZC013.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 		GZC(013,zc) = GZC003 + GZC012 
 *
 * @author ogperez
 *
 */
public class ModuloGZC013 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC013.class);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC013;
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
			if (ModuloGZC013.LOG.isTraceEnabled()) {
				ModuloGZC013.LOG.trace("Inicio de execute en clase ModuloGZC013");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC013
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC013
			resultado = moduloGZC013(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC013.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC013.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC013.LOG.isTraceEnabled()) {
			ModuloGZC013.LOG.trace("Fin de execute en clase ModuloGZC013");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 *    GZC(013,zc) = GZC003 + GZC012 
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando
	 */
	private BigDecimal  moduloGZC013(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzc013 = BigDecimal.ZERO;
		BigDecimal impFNGZC003;
		BigDecimal impFNGZC012; 
		Modulo moduloGZC003 = null;
		Modulo moduloGZC012 = null;
		//Fin variables locales
		
		if (ModuloGZC013.LOG.isTraceEnabled()) {
			ModuloGZC013.LOG.trace("Inicio función << moduloGZC013 >> de la clase ModuloGZC013, para la iteracion = {}", iteracion);
		}
		

		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc013;
		}

		/**
		 * Para todos los periodos los cálculos se realizan de la misma manera.	
		 * 
		 * 		GZC013 (j) = GZC003 (j) + GZC012(j)
		 * 	
		 **/
		
		
		//Se llama al módulo GZC012 
		moduloGZC012 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC012);
		impFNGZC012 = (BigDecimal) moduloGZC012.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

		//Se llama al módulo GZC003 
		moduloGZC003 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC003);
		impFNGZC003 = (BigDecimal) moduloGZC003.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		
		// Se calcula la cuantía nominal por Gastos de Administración
		gzc013 = impFNGZC012.add(impFNGZC003);
		
		if (ModuloGZC013.LOG.isTraceEnabled()) {
			ModuloGZC013.LOG.trace("Fin función << moduloGZC013 >> de la clase ModuloGZC013, para la iteracion = {} con resultado GZC013 = {}", iteracion, gzc013);
		}
		
		return gzc013;
	}

}
