package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
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

public class ModuloCSP008 implements Modulo {
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP008.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP008;

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		//Variables locales
		BigDecimal csp008 = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP008.LOG.isTraceEnabled()) {
				ModuloCSP008.LOG.trace("Inicio de execute en clase ModuloCSP008");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP008
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//Invocamos a la función de calculo CSP008
			csp008 = moduloCSP008(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP008.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP008.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP008.LOG.isTraceEnabled()) {
			ModuloCSP008.LOG.trace("Fin de execute en clase ModuloCSP008");
		}
			
		return csp008;
	}
	
	
	/**
	 * Modulo de cálculo que devuelve el cálculo de la cuantía nominal en Seguros Mixtos Individuales.
	 * 
	 * @param proyUmic
	 * 			Estructura detalleCorrientes de la umic.
	 * @param iteracion
	 * 			Indica el periodo de proyección J que se está calculando de entre todos los periodos de proyección de la umic (proyUmic).
	 * @param fcalc
	 * 			Fecha de calculo.
	 * @param umic
	 * 			Contiene los datos de la Umic que se está procesando.
	 * @param btcUmic
	 * 			Contiene el detalle de la base técnica de cálculo para la umic.
	 * @param codSubproceso
	 * 			Código el subproceso que se está ejecutando.
	 * @param bloqueCorriente
	 * 			Bloque de Trabajo de la corriente.
	 * @param mapVariables 
	 * 			Mapa con las variables de memoria necesarias.
	 */
	
	private BigDecimal moduloCSP008(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final BloqueCorriente bloqueCorriente, final Map<String, Object> mapVariables) {
		
		//Variables locales
		BigDecimal csp008 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyNominal;
		List<DetalleCorriente> varProy116;
		BigDecimal varCSP050 = BigDecimal.ZERO;
		BigDecimal varCSP116 = BigDecimal.ZERO;
		
		//Fin variables locales
		if (ModuloCSP008.LOG.isTraceEnabled()) {
			ModuloCSP008.LOG.trace("Inicio función << moduloCSP008 >> para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		DatosGenerales datosGenerales = umic.getDatosGenerales();
		// Fin de la definición de las variables auxiliares.
		
		
		
		//Parte funcional
		if (ConstantsModulos.CTE_UMIC_PRINCIPAL.equals(datosGenerales.getSpcom())){
			//Se devuelve error funcional AJ – No se puede recuperar la clave de la garantía principal ya que la umic de 
			//entrada está marcada como garantía principal en el maestro & umic.claveUmic, finalizando el proceso para la UMIC.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AJ);
		}else{
			varProyNominal = proyUmic;
			varProy116 = proyUmic;
		}
		
		varCSP050 = calcularCSP050(varProyNominal, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		//Falta por implmentar el modulo CSP116
		varCSP116 = calcularCSP116(varProy116,bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		csp008 = varCSP116.subtract(varCSP050);
		//Fin parte funcional
		
		if (ModuloCSP008.LOG.isTraceEnabled()) {
			ModuloCSP008.LOG.trace("Fin función << moduloCSP008 >> de la clase ModuloCSP008, para la iteracion = {}, con resultado csp008 = {}", iteracion, csp008);
		}
		
		return csp008;
	}
	
	/**
	 * Funcion que devuelve el valor de ejecutar el ModuloCSP050
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return varCsp050Temp
	 */
	private BigDecimal calcularCSP050(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, Integer iteracion, Timestamp fcalc,
			Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables, String codSubproceso) {
		
		Modulo moduloCSP050;
		BigDecimal varCsp050Temp;	
		
		moduloCSP050 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP050);
		varCsp050Temp = (BigDecimal) moduloCSP050.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		return varCsp050Temp;
	}
	/** 
	 * Funcion que devuelve el valor al ejecutar el ModuloCSP116
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param mapVariables
	 * @param codSubproceso
	 * @return varCsp116Temp
	 */
	private BigDecimal calcularCSP116(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, Integer iteracion, Timestamp fcalc,
			Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables, String codSubproceso){
		
		Modulo moduloCSP116;
		BigDecimal varCsp116Temp;
		
		moduloCSP116 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP116);
		varCsp116Temp = (BigDecimal) moduloCSP116.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		return varCsp116Temp;
	}
	
	
	

}
