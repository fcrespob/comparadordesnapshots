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
 * Clase que implementa el modulo GZC007.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 		GZC(007,zc) = GZC004 + GZC008 
 *
 * @author apedro
 *
 */
public class ModuloGZC007 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC007.class);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZC007;
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
			if (ModuloGZC007.LOG.isTraceEnabled()) {
				ModuloGZC007.LOG.trace("Inicio de execute en clase ModuloGZC007");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZC007
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZC007
			resultado = moduloGZC007(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZC007.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC007.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZC007.LOG.isTraceEnabled()) {
			ModuloGZC007.LOG.trace("Fin de execute en clase ModuloGZC007");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 *    GZC(007,zc) = GZC004 + GZC008
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
	private BigDecimal  moduloGZC007(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzc007 = BigDecimal.ZERO;
		BigDecimal impFNGZC004;
		BigDecimal impFNGZC008 = BigDecimal.ZERO; 
		Modulo moduloGZC004 = null;
		Modulo moduloGZC008 = null;
		//Fin variables locales
		
		if (ModuloGZC007.LOG.isTraceEnabled()) {
			ModuloGZC007.LOG.trace("Inicio función << moduloGZC007 >> de la clase ModuloGZC007, para la iteracion = {}", iteracion);
		}
		

		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzc007;
		}

		/**
			 * Para todos los periodos los cálculos se realizan de la misma manera (en la primera iteración los auxiliares son calculados las demás los recupera).
					
					GZC007 (j) = GZC004 (j) + GZC008(j)
					
					Finalmente asignaremos la cuantía nominal del periodo como: 
					
					proyUmic(j).impFlujoNominal = GZC007 (j)

			 */
		
		
		//Se llama al módulo GZC004 para obtener el importe nomial del detalle de la corriente.
		moduloGZC004 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC004);
		impFNGZC004 = (BigDecimal) moduloGZC004.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		

		moduloGZC008 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC008);
		impFNGZC008 = (BigDecimal) moduloGZC008.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);

		// Se calcula la cuantía nominal.
		gzc007 = impFNGZC004.add(impFNGZC008);
		
		if (ModuloGZC007.LOG.isTraceEnabled()) {
			ModuloGZC007.LOG.trace("Fin función << moduloGZC007 >> de la clase ModuloGZC007, para la iteracion = {} con resultado GZC007 = {}", iteracion, gzc007);
		}
		
		return gzc007;
	}

}
