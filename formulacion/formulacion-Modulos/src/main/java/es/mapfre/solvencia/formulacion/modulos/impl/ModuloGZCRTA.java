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
 * Clase que implementa el modulo GZCRTA.
 * Clase encargada del cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
 * La expresión matemática para su determinación es la siguiente:
 * 		GZCRTA =
 * 		SI VITALICIA   GZC(007,zc) = GZC004 + GZC008
 *		SI TEMPORAL   GZC(006,zc) = GZC004 + GZC001
 *
 * @author agonzalezgar
 *
 */
public class ModuloGZCRTA implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCRTA.class);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_GZCRTA;
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
			if (ModuloGZCRTA.LOG.isTraceEnabled()) {
				ModuloGZCRTA.LOG.trace("Inicio de execute en clase ModuloGZCRTA");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZCRTA
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo GZCRTA
			resultado = moduloGZCRTA(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloGZCRTA.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZCRTA.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloGZCRTA.LOG.isTraceEnabled()) {
			ModuloGZCRTA.LOG.trace("Fin de execute en clase ModuloGZCRTA");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la cuantía nominal por Gastos de Administración en cada periodo de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * GZCRTA =
	 * 		SI VITALICIA   GZC(007,zc) = GZC004 + GZC008
	 *		SI TEMPORAL   GZC(006,zc) = GZC004 + GZC001
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
	private BigDecimal  moduloGZCRTA(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal gzcrta = BigDecimal.ZERO;
		String varTipoPres;
		Modulo modulo = null;
		//Fin variables locales
		
		if (ModuloGZCRTA.LOG.isTraceEnabled()) {
			ModuloGZCRTA.LOG.trace("Inicio función << moduloGZCRTA >> de la clase ModuloGZCRTA, para la iteracion = {}", iteracion);
		}
		

		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(bloqueCorriente.getFechaDevengo() == null) {
			return gzcrta;
		}

		/**
			 * Para todos los periodos los cálculos se realizan de la misma manera (en la primera iteración los auxiliares son calculados las demás los recupera).
					Variables Módulo
					-	varTipoPrestacion = umic.rentas. tempVit

					Para cualquier periodo j se calculará GZCRTA(j) como: 
					Si varTipoPrestacion  =’V’
					   GZCRTA (j) = GZC007 (j)					 
					Si varTipoPrestacion  =’T’: 
					 GZCRTA (j) = GZC006 (j)
					
					Finalmente asignaremos la cuantía nominal del periodo como: 
					
					proyUmic(j).impFlujoNominal = GZCRTA (j)

			 */
		varTipoPres = umic.getRentas().getTempVit();
			
		
		// En función del tipo de prestación el módulo que se usará para calcular gzcrta será GZC007 o GZC006.
		if (ConstantsModulos.CTE_TIPO_PRES_VIT.equals(varTipoPres)) {
			modulo = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC007);
		} else if (ConstantsModulos.CTE_TIPO_PRES_TEM.equals(varTipoPres)) {
			modulo = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC006);
		}
		
		// Cálculo de la cuantía nominal.
		if ( null != modulo ){ // Se hace por seguridad esta comprobación, si es nulo es porque hay errores en los datos de entrada.
			gzcrta = (BigDecimal) modulo.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		}
		
		
		if (ModuloGZCRTA.LOG.isTraceEnabled()) {
			ModuloGZCRTA.LOG.trace("Fin función << moduloGZCRTA >> de la clase ModuloGZCRTA, para la iteracion = {} con resultado GZCRTA = {}", iteracion, gzcrta);
		}
		
		return gzcrta;
	}

}
