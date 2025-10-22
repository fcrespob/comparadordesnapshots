package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * Usaremos este módulo para aquellas modalidades cuyo Capital 
 * revaloriza geométricamente al IPC, y siempre que tengamos que 
 * provocar la renovación tácita como consecuencia de la 
 * aplicación del CRITERIO TAR
 * 
 * @author Szilard Toth
 *
 */

public class ModuloCSP011 implements Modulo{

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP011.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP011;

	private static final String CLAVE_IPC_FUTURO = ConstantsModulos.CTE_IPC_FUTURO.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSP011 = ConstantsModulos.CTE_VAR_CSP011.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_NR = ConstantsModulos.CTE_VAR_NR.concat(CLAVE_MODULO);
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
			
			if (ModuloCSP011.LOG.isTraceEnabled()) {
				ModuloCSP011.LOG.trace("Inicio de execute en clase ModuloCSP011");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSP011
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			//Invocamos a la función moduloCSP011
			resultado = moduloCSP011(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP011.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP011.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP011.LOG.isTraceEnabled()) {
			ModuloCSP011.LOG.trace("Fin de execute en clase ModuloCSP011");
		}
		
		return resultado;
	}
	
	/**
	 * Usaremos este módulo para aquellas modalidades cuyo Capital revaloriza geométricamente al IPC, y siempre que tengamos que provocar la renovación tácita como consecuencia de la aplicación del CRITERIO TAR.
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

	private BigDecimal moduloCSP011(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso,final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal csp011 = BigDecimal.ZERO;
		IPCGeneralFuturo varIpcFuturo = null;
		BigDecimal varCsp011 = BigDecimal.ZERO;
		BigDecimal varIpcJ = null;
		BigDecimal varIpcLegal;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova = null;
		
		Integer varNR = 0;
		//Fin variables locales
		
		if (ModuloCSP011.LOG.isTraceEnabled()) {
			ModuloCSP011.LOG.trace("Inicio de la función << moduloCSP011 >> de la clase ModuloCSP011, para la iteración = {}", iteracion);
		}
		
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo
		 * que no varían por periodo, así como las variables internas que tampoco varíen por periodo, 
		 * y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.
		 * 
		 * varIpc = obtenerConfiguracion.recuperarIpcFuturo(proyUmic (j).fecCierre)
		 * varCSP011 = umic.capitales.icapact --> Se guarda la variable en memoria para el subproceso de la umic.
		 * varAntRenova = umic.fechas. fecdesderenova --> Se guarda la variable en memoria para el subproceso de la umic.
		 * varProxRenova = umic.fechas. fechastarenova --> Se guarda la  variable en memoria para el subproceso de la umic.
		 * varNR = umic.duraciones.nrenovaciones --> Se guarda la  variable en memoria para el subproceso de la umic.
		 * 
		 * Para cualquier periodo j (j >= 1), se calculará el importe del capital correspondiente al periodo j como: 
		 * 
		 * Si proyUmic(j).fecDevengo es nula: CSP011 = 0;
		 * 
		 * Si proyUmic(j).fecDevengo es no nula: 
		 * 		Si proyUmic(j).fechaDesde  >  varAntRenova  y  proyUmic(j).fechaDesde  >  varProxRenova  (periodo en el que reniueva la póliza):
		 * 			varAntRenova = varProxRenova --> Se guardan las variables en memoria para el subproceso de la umic.
		 * 			varProxRenova = varProxRenova + 1 año. --> Se guardan las variables en memoria para el subproceso de la umic.
		 * 			varNR = varNR +1 --> Se guarda la variable en memoria para el subproceso de la umic.
		 * 			varIpcJ  --> Se buscará dentro de la estructura de intereses varIpc el registro que cumpla con las condiciones: varIpc.ipc0Finicio  <=  proyUmic(j).varBloque.fecDevengo  <= varIpc.ipc0Ffin. Para el registro así encontrado:
		 * 			varIpcLegal = varIpcJ  = varIpc.ipc0Pipcleg (varCSP011 = varCSP011 * (1 + (varIpcLegal )/100))
		 * 
		 * 		Si no se cumple la condición se mantendrá el valor de las variables varAntRenova, varProxRenova, varNR, varIcapRenoAnt, varIpcLegal y varCSP011
		 * 
		 * Para cualquier periodo j (j >= 1): CSP011 (j)= varCSP011
		 * 
		 * 
		 */
		
		varIpcFuturo = UtilModulos.getVarIpc(mapVariables, CLAVE_IPC_FUTURO, proyUmic.get(iteracion-1).getFcierre(), btcUmic.getBt());
		varCsp011 = UtilModulos.getVarCSP011(mapVariables, CLAVE_VAR_CSP011, umic.getCapitales().getIcapact());
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		varNR = UtilModulos.getVarNR(mapVariables, CLAVE_VAR_NR, umic.getDuraciones().getNrenovaciones());
		
		if(null != bloqueCorriente.getFechaDevengo()) { 
		   
			if (proyUmic.get(iteracion-1).getFechaDesde().after(varAntRenova) && 
		        proyUmic.get(iteracion-1).getFechaDesde().after(varProxRenova)){
					varAntRenova = varProxRenova;
					mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
					varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
					mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
					varNR = varNR + 1;
					mapVariables.put(CLAVE_VAR_NR, varNR);
					varIpcJ = varIpcFuturo.getPipcleg();
					varIpcLegal = varIpcJ;
					varCsp011 = varCsp011.multiply(UtilModulos.getNumPorcentajeMasUno(varIpcLegal, mapVariables, CLAVE_MODULO));
					mapVariables.put(CLAVE_VAR_CSP011, varCsp011);
					csp011 = varCsp011;
			} else {
				csp011 = varCsp011;
			}
		}
		
		if(ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) && (UtilFechas.getDia(proyUmic.get(iteracion-1).getFechaHasta()) !=  UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(proyUmic.get(iteracion-1).getFechaHasta())))){
			
			int vardiasmes = UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(proyUmic.get(iteracion-1).getFechaHasta()));
			int vardias = UtilFechas.getDia(proyUmic.get(iteracion-1).getFechaHasta()) - ConstantsFunciones.CTE_1;
			BigDecimal vardiasmes2 = new BigDecimal(vardiasmes);
			BigDecimal vardias2 = new BigDecimal(vardias);
			BigDecimal varFactor = vardias2.divide(vardiasmes2,ConstantsFunciones.MATH_CONTEXT);
			
			if(vardias2.equals(BigDecimal.ZERO)){
				varFactor = BigDecimal.ONE;
			}
			
			csp011 = csp011.multiply(varFactor);
		}
		
		if (ModuloCSP011.LOG.isTraceEnabled()) {
			ModuloCSP011.LOG.trace("Fin de la función << moduloCSP011 >> de la clase ModuloCSP011, para la iteración = {}", iteracion);
		}
		return csp011;
	}
}
