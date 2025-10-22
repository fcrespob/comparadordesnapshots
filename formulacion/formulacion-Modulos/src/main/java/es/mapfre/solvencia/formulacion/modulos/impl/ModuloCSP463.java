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
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo CSP463.
 * Clase encargada del cálculo que devuelve el importe nominal en cada momento según forma de pago de la Umic.  
 * La expresión matemática para su determinación es la siguiente:
 * 				CSP463 = Máximo (varCsp463, 0)
 * @author ogperez
 *
 */
public class ModuloCSP463 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP463.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP463;
	//private static final String CLAVE_CALCULO_CSP463 = CLAVE_MODULO;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCMINI = ConstantsModulos.CTE_VAR_TCMINI.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CSP071 = ConstantsModulos.CTE_VAR_CSP071;
	private static final String CLAVE_VAR_CSP071 = CLAVE_CSP071.concat(CLAVE_MODULO);
	
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
			
			if (ModuloCSP463.LOG.isTraceEnabled()) {
				ModuloCSP463.LOG.trace("Inicio de execute en clase ModuloCSP463");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP463
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo ModuloCSP463
			resultado = moduloCSP463(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP463.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP463.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP463.LOG.isTraceEnabled()) {
			ModuloCSP463.LOG.trace("Fin de execute en clase ModuloCSP463");
		}
		
		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve el importe nominal en cada momento según forma de pago de la Umic.
	 * La expresión matemática para su determinación es la siguiente:
	 * 				CSP463 = Máximo (varCsp463, 0)
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
	private BigDecimal  moduloCSP463(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables, final String codSubproceso) {
		//Variables locales
		BigDecimal csp463 = BigDecimal.ZERO;
		String varCriterioFecha;
		Timestamp varFechaEfecto;
		Integer varTCm;
		Integer varTCmini;
		BigDecimal varPU;
		BigDecimal varCsp071;
		BigDecimal varCsp071Temp;
		BigDecimal varCsp463;
		//Fin variables locales
		
		if (ModuloCSP463.LOG.isTraceEnabled()) {
			ModuloCSP463.LOG.trace("Inicio función << ModuloCSP463 >> de la clase ModuloCSP463, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);		
		
		if (bloqueCorriente.getFechaDevengo() == null) {
			return BigDecimal.ZERO;
		}				
		
		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
						
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
		// Fin de la obtención y validación del criterio de fecha.
		}
		
		if (umic.getDatosGenerales().getCnegocio().equals(ConstantsModulos.CTE_NEGOCIO_INDIVIDUAL)) {
			varFechaEfecto = umic.getFechas().getFecefecini();
		} else {
			varFechaEfecto = umic.getFechas().getFecinisus();
		}
		
		
//INI-816517: Se sustituye calculo de TCm por el TCmIni.
		varTCm = UtilModulos.getVarK(mapVariables, CLAVE_VAR_TCM, varFechaEfecto, fcalc);
 		varTCmini = UtilModulos.getVarKini(mapVariables, CLAVE_VAR_TCMINI, varFechaEfecto, fcalc);
//FIN-816517 
		varPU = umic.getPrimas().getIprimanetaini();
		
		//Inicializo variable varCsp071 a 0 (Se irá incrementando por cada iteración j)
		varCsp071 = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP071);
		if (varCsp071 == null) {
			varCsp071 = BigDecimal.ZERO;
			mapVariables.put(CLAVE_VAR_CSP071, varCsp071);			
		}
		
		// Si es la primera iteración calculo el ∑CSP071_j de los tcm primeros periodos 
		if (iteracion.equals(ConstantsModulos.CTE_FIRST_ITER)) {
//INI-816517: marcamos que estamos calculando el sumatorio de las rentas pagadas hasta la fecha de calculo, para que en el 
//			  modulo csp071, no se recalcule tcm, sino que calcule con tcm=0 y vaya variando la beta
			String sumator;
			sumator = (String) mapVariables.get("SUMATORIO");
			if (sumator == null) {
				sumator = ConstantsFunciones.CTE_CADENA_VACIA;
				mapVariables.put("SUMATORIO", sumator);	
			}
//FIN-816517
			for (int j=1; j<=varTCmini;j++) {
				varCsp071Temp = calcularCSP463(proyUmic, bloqueCorriente, j, fcalc, umic, btcUmic, mapVariables, codSubproceso);
	     	  	if ((!varCsp071Temp.equals(BigDecimal.ZERO)) && (j==1)) { 
 	  		        	varCsp071Temp = BigDecimal.ZERO;
  			 	}
				varCsp071 = varCsp071.add(varCsp071Temp);		
			}	
			
			sumator = "procesado";
			mapVariables.put("SUMATORIO", sumator);
		} 
		
//		// Para cualquier periodo j se calculará
//JAVIVIL
//  		varCsp071Temp = calcularCSP463(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
//  		varCsp071 = varCsp071.add(varCsp071Temp);
		if (!(varTCmini == 0)  || !(iteracion == 1)) {
			varCsp071Temp = calcularCSP463(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
  		    varCsp071 = varCsp071.add(varCsp071Temp);
		}
//JAVIVIL 		

		mapVariables.put(CLAVE_VAR_CSP071, varCsp071);
		
		varCsp463 = varPU.subtract(varCsp071); 
		// Se realiza el cálculo mediante la formula: CSP463 = Máximo (varCsp463, 0)
		csp463 = BigDecimal.ZERO.max(varCsp463);
			
		if (ModuloCSP463.LOG.isTraceEnabled()) {
			ModuloCSP463.LOG.trace("Fin función << ModuloCSP463 >> de la clase ModuloCSP463, para la iteracion = {} con resultado csp463 = {}", iteracion, csp463);
		}
			
		return csp463;
	}

	

	private BigDecimal calcularCSP463(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, Integer iteracion, Timestamp fcalc,
			Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables, String codSubproceso) {
		Modulo moduloCSP071;
		BigDecimal varCsp071Temp;	
		
		moduloCSP071 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP071);
		moduloCSP071 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP071B);
		varCsp071Temp = (BigDecimal) moduloCSP071.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		return varCsp071Temp;
	}

}
