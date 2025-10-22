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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;



/**
 * Este módulo es el módulo nominal de Capital Equivalente
 * de los TARes con prestación en forma de renta
 *  */


public class ModuloCSP012 implements Modulo {
	
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP012.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP012;
	
	private static final String CLAVE_NRTA = ConstantsModulos.CTE_NRTA;
	private static final String CLAVE_VAR_NRTA = CLAVE_NRTA.concat(CLAVE_MODULO);
	
	private static final String CLAVE_BETARENTAS = ConstantsModulos.CTE_BETARENTAS;
	private static final String CLAVE_VAR_BETARENTAS = CLAVE_BETARENTAS.concat(CLAVE_MODULO);
	
	private static final String CLAVE_ICAPACT = ConstantsModulos.CTE_ICAPACT;
	private static final String CLAVE_VAR_ICAPACT = CLAVE_ICAPACT.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VACF = ConstantsModulos.CTE_VACF;
	private static final String CLAVE_VAR_VACF = CLAVE_VACF.concat(CLAVE_MODULO);
	
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
		BigDecimal csp012 = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloCSP012.LOG.isTraceEnabled()) {
				ModuloCSP012.LOG.trace("Inicio de execute en clase ModuloCSP012");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP012
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función de calculo CSP012
			csp012 = moduloCSP012(proyUmic,iteracion,fcalc, umic, btcUmic, codSubproceso, bloqueCorriente,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP012.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP012.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP012.LOG.isTraceEnabled()) {
			ModuloCSP012.LOG.trace("Fin de execute en clase ModuloCSP012");
		}
		
		return csp012;
	}
	
	/**
	 * Modulo de cálculo que devuelve el Capital Equivalente de los TARes con prestación en forma de renta.
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
	
	private BigDecimal moduloCSP012(final List<DetalleCorriente> proyUmic, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final BloqueCorriente bloqueCorriente, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp012 = BigDecimal.ZERO;
		Integer NRTA = ConstantsFunciones.CTE_0;
		BigDecimal BETARENTAS = BigDecimal.ZERO;	
		BigDecimal varIcapAct = BigDecimal.ZERO;
		Integer varFPR = ConstantsFunciones.CTE_0;
		BigDecimal varPrr = BigDecimal.ZERO;
		BigDecimal varl1Bti = BigDecimal.ZERO;
		BigDecimal varVacf = BigDecimal.ZERO;
		BigDecimal varCSP012 = BigDecimal.ZERO;
		//Fin variables locales
		if (ModuloCSP012.LOG.isTraceEnabled()) {
			ModuloCSP012.LOG.trace("Inicio función << moduloCSP012 >> para la iteracion = {}", iteracion);
		}
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		// Fin de la definición de las variables auxiliares.
		//Calculo variables de apoyo
		NRTA = UtilModulos.getVarNRTA(mapVariables, CLAVE_VAR_NRTA, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_NRTA);
		BETARENTAS = UtilModulos.getVarBETARENTAS(mapVariables, CLAVE_VAR_BETARENTAS, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_BETARENTAS);
		//Fin calculo variables de apoyo
		//Validacion variables de apoyo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoNRTA(NRTA);
			ValidacionesComunesModulos.validarVariableDeApoyoBETARENTAS(BETARENTAS);
			// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		}
		//Fin validacion
		varIcapAct = umic.getCapitales().getIcapact(); 
		final Integer forpagrent = umic.getRentas().getForpagrent();
		final String cpagrenta = umic.getRentas().getCpagrenta();
		varl1Bti = umic.getBti().getPintertecnI1().divide(ConstantsFunciones.CTE_OPER_100, ConstantsFunciones.MATH_CONTEXT);
		//Parte funcional
		Map<Integer, Integer> fp = ConstantsFunciones.FORPAGRENT;
		if (forpagrent == null || forpagrent.equals(ConstantsFunciones.CTE_0)) {
			varFPR= fp.get(Integer.parseInt(cpagrenta));
		} else{
			varFPR = forpagrent;
		}
		varVacf = UtilModulos.getVarVacf(mapVariables,CLAVE_VAR_VACF,NRTA,varFPR,varl1Bti, varPrr);
		varCSP012 = UtilModulos.getVarCSP012(mapVariables, CLAVE_VAR_ICAPACT, varIcapAct,BETARENTAS,varVacf);
		
		if (bloqueCorriente.getFechaDevengo() != null) {
			csp012 = varCSP012;
		}
		//Fin parte funcional
		if (ModuloCSP012.LOG.isTraceEnabled()) {
			ModuloCSP012.LOG.trace("Fin función << moduloCSP012 >> de la clase ModuloCSP012, para la iteracion = {}, con resultado csp012 = {}", iteracion, csp012);
		}
		return csp012;
	}
}