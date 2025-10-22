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
 * Clase que implementa el modulo VVALzc.
 * Clase encargada del cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de la garantía.  
 * La expresión matemática para su determinación es la siguiente:
 * 				VVALzc (j) = varVzc(j)  * (1 – varIzcj)
 * @author ogperez
 *
 */
public class ModuloVVALzc implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVVALzc.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VVALZC;
	//private static final String CLAVE_CALCULO_VVALZC = CLAVE_MODULO;
	
	private static final String CLAVE_REVER = ConstantsModulos.CTE_VAR_REVER;
	private static final String CLAVE_VAR_REVER = CLAVE_REVER.concat(CLAVE_MODULO);
	
	private static final String CLAVE_IZCJ = ConstantsModulos.CTE_VAR_IZCJ;
	private static final String CLAVE_VAR_IZCJ = CLAVE_IZCJ.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_VVALZC;
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
			
			if (ModuloVVALzc.LOG.isTraceEnabled()) {
				ModuloVVALzc.LOG.trace("Inicio de execute en clase ModuloVVALzc");
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
			
			//Invocamos a la función de calculo ModuloVVALzc
			resultado = moduloVVALzc(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVVALzc.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVVALzc.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVVALzc.LOG.isTraceEnabled()) {
			ModuloVVALzc.LOG.trace("Fin de execute en clase ModuloVVALzc");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  el cálculo de la cuantía probable de la garantía.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				VVALzc (j) = varVzc(j)  * (1 – varIzcj)
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
	private BigDecimal  moduloVVALzc(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal vvalzc = BigDecimal.ZERO;
		BigDecimal varRever;
		BigDecimal varIzcj; 
		BigDecimal varVzc;
		BigDecimal varCompl;
		Modulo moduloVZC;
		Modulo moduloCOMPLPTOZC;
		//Fin variables locales
		
		if (ModuloVVALzc.LOG.isTraceEnabled()) {
			ModuloVVALzc.LOG.trace("Inicio función << ModuloVVALzc >> de la clase ModuloVVALzc, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);		
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return vvalzc;
		}		
		
		
		// Se calculan y almacenan las variables del módulo que no cambian entre periodos
		varRever = (BigDecimal) mapVariables.get(CLAVE_VAR_REVER);
		if (varRever == null){
			varRever = umic.getRentas().getPreversion().multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01);
			mapVariables.put(CLAVE_VAR_REVER, varRever);
		}
		
		// Inicializo la variable varIzcj 
		varIzcj = (BigDecimal) mapVariables.get(CLAVE_VAR_IZCJ);
		if (varIzcj == null){
			varIzcj = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_IZCJ, varIzcj);
		}
		
		// LLamada al modulo VZC
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		varVzc = (BigDecimal) moduloVZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		// LLamada al modulo COMPLPTOZC
		moduloCOMPLPTOZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_COMPLPTOZC); 
		varCompl = (BigDecimal) moduloCOMPLPTOZC.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		varIzcj = varIzcj.add(varCompl);
		mapVariables.put(CLAVE_VAR_IZCJ, varIzcj);
		
		// Se realiza el cálculo de la formula: VVALzc (j) = varVzc(j)  * (1 – varIzcj)
		vvalzc = varVzc.multiply(BigDecimal.ONE.subtract(varIzcj));
	
			
		if (ModuloVVALzc.LOG.isTraceEnabled()) {
			ModuloVVALzc.LOG.trace("Fin función << ModuloVVALzc >> de la clase ModuloVVALzc, para la iteracion = {} con resultado vvalzc = {}", iteracion, vvalzc);
		}
			
		return vvalzc;
	}

}
