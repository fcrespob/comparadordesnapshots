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
 * Clase que implementa el modulo GZC011.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 		GZC(011,zc) = GZC008 + GZC003 
 *
 * @author apedro
 *
 */
public class ModuloGZC011 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC011.class);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC011;
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
			if (ModuloGZC011.LOG.isTraceEnabled()) {
				ModuloGZC011.LOG.trace("Inicio de execute en clase ModuloGZC011");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC011
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC011
			resultado = moduloGZC011(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC011.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC011.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC011.LOG.isTraceEnabled()) {
			ModuloGZC011.LOG.trace("Fin de execute en clase ModuloGZC011");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 *    GZC(011,zc) = GZC008 + GZC003
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
	private BigDecimal  moduloGZC011(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzc011 = BigDecimal.ZERO;
		BigDecimal impFNGZC003;
		BigDecimal impFNGZC008 = BigDecimal.ZERO; 
		Modulo moduloGZC003 = null;
		Modulo moduloGZC008 = null;
		//Fin variables locales
		
		if (ModuloGZC011.LOG.isTraceEnabled()) {
			ModuloGZC011.LOG.trace("Inicio función << moduloGZC011 >> de la clase ModuloGZC011, para la iteracion = {}", iteracion);
		}
		

		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc011;
		}

		/**
			 * Para todos los periodos los cálculos se realizan de la misma manera (en la primera iteración los auxiliares son calculados las demás los recupera).
					
					GZC011 (j) = GZC008 (j) + GZC003(j)
					
					Finalmente asignaremos la cuantía nominal del periodo como: 
					
					proyUmic(j).impFlujoNominal = GZC011 (j)

			 */
		
		
		//Se llama al módulo GZC008 para obtener el importe nomial del detalle de la corriente.
		moduloGZC008 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC008);
		impFNGZC008 = (BigDecimal) moduloGZC008.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

		
		moduloGZC003 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC003);
		impFNGZC003 = (BigDecimal) moduloGZC003.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		
		// Se calcula la cuantía nominal.
		gzc011 = impFNGZC008.add(impFNGZC003);
		
		if (ModuloGZC011.LOG.isTraceEnabled()) {
			ModuloGZC011.LOG.trace("Fin función << moduloGZC011 >> de la clase ModuloGZC011, para la iteracion = {} con resultado GZC011 = {}", iteracion, gzc011);
		}
		
		return gzc011;
	}

}
