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
 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos 
 * a utilizar en estas modalidades. Es una variación del módulo ya 
 * creado GZC008, pero para considerar gasto anual prepagable en vez 
 * de mensualizado
 * 
 * @author Szilard Toth
 *
 */

public class ModuloGZC008M implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC008M.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZC008M;
	private static final String CLAVE_VAR_ASUBX = ConstantsModulos.CTE_VAR_ASUBX.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloGZC008M.LOG.isTraceEnabled()) {
				ModuloGZC008M.LOG.trace("Inicio de execute en clase ModuloGZC008M");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloGZC008M
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloGZC008M
			resultado = moduloGZC008M(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloGZC008M.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZC008M.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloGZC008M.LOG.isTraceEnabled()) {
			ModuloGZC008M.LOG.trace("Fin de execute en clase ModuloGZC008M");
		}
		return resultado;
	}
	/**
	 * Usaremos este módulo para el cálculo de la cuantía nominal de gastos a utilizar en estas modalidades. 
	 * Es una variación del módulo ya creado GZC008, pero para considerar gasto 
	 * anual prepagable en vez de mensualizado
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	private static BigDecimal moduloGZC008M(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal gzc008M = BigDecimal.ZERO;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varPU = BigDecimal.ZERO;
		BigDecimal varax;
		Integer varTCm = 0;
		Integer varBeta = 0;
		Integer varPP = 0;
		BigDecimal varp;
		Timestamp varFechaEfecto;
		List<DetalleCorriente> varProyAx = null;
		//Fin variables locales
		if (ModuloGZC008M.LOG.isTraceEnabled()) {
			ModuloGZC008M.LOG.trace("Inicio de la función << moduloGZC008M >> de la clase ModuloGZC008M, para la iteración = {}", iteracion);
		}
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * 	Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el 
		 * cálculo que no varían por periodo, así como las varibales internas que tampoco varían por periodo, 
		 * y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular. 
		 * 
		 * 	Variables Módulo
		 * 		varGipc = btcUmic.gtoRosspPrima;
		 * 		varPU = umic.primas.iprimanetaini
		 * 		varProyAx = proyUmic
		 * 		varäx= äx (varProyAx, j, ,fcalc, umic, btcUmic, codSubproceso);
		 * 	
		 * 	Si estoy en el primer periodo (j=1), se establecerán las siguientes variables internas: 
		 * 		varTCm = TCm(varfechaEfecto, fcalc);
		 * 		varβ = 0
		 * 	Para cualquier periodo J  se calculará: 
		 * 		Si j > 1 --> varβ = varβ  +1
		 * 		Si proyUmic(j).varBloque.fecDevengo es nula: 
		 * 			GZC008M = 0 // BigDecimal.ZERO;
		 * 		Si proyUmic(j).varBloque.fecDevengo es no nula: 
		 * 			varPP = MOD(varTCm + varβ,12) 
		 * 			Si varPP =0
		 * 				varp = 1;
		 * 			En cualquier otro caso:
		 * 				varp = 0;
		 * 
		 * 			GZC008M = (((varGipc.multiply(varPU)).divide(varäx)).multiply(varp));
		 *  
		 */
		varGipc = btcUmic.getGtorosspPrima();
		varPU = umic.getPrimas().getIprimanetaini();
		varProyAx = proyUmic;
		varax = UtilModulos.getVarASubX(mapVariables, CLAVE_VAR_ASUBX, varProyAx, bloqueCorriente, fcalc, umic, btcUmic, codSubproceso);
		varFechaEfecto = umic.getFechas().getFecefecini();
		varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varFechaEfecto, fcalc);
		varBeta = iteracion - 1;

		if (!bloqueCorriente.getFechaDevengo().equals(null)){
			varPP = (varTCm + varBeta) % (ConstantsFunciones.CTE_12);
			if(varPP == 0){
				varp = BigDecimal.ONE;
			} else {
				varp = BigDecimal.ZERO;
			}
			gzc008M = (((varGipc.multiply(varPU)).divide(varax,ConstantsFunciones.MATH_CONTEXT)).multiply(varp));
		}
			
		if (ModuloGZC008M.LOG.isTraceEnabled()) {
			ModuloGZC008M.LOG.trace("Fin de la función << moduloGZC008M >> de la clase ModuloGZC008M, para la iteración = {}", iteracion);
		}
		return gzc008M;
	}
}
