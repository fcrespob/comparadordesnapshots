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
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */
public class ModuloCSP013 implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP013.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP013;
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PRC = ConstantsModulos.CTE_VAR_PRC.concat(CLAVE_MODULO);
	private static final String CLAVE_LST_PROY = ConstantsModulos.CTE_LST_PROY.concat(CLAVE_MODULO);
	
	private static final String CLAVE_X = ConstantsModulos.CTE_VAR_X;
	private static final String CLAVE_VAR_X = CLAVE_X.concat(CLAVE_MODULO);
	
	// CTE_VAR_FACTOR_REV
	private static final String CLAVE_VAR_FACTOR_REV = ConstantsModulos.CTE_VAR_FACTOR_REV.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSP = ConstantsModulos.CTE_VAR_CSP.concat(CLAVE_MODULO);
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
			if (ModuloCSP013.LOG.isTraceEnabled()) {
				ModuloCSP013.LOG.trace("Inicio de execute en clase ModuloCSP013");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP013
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP013
			resultado = moduloCSP013(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP013.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP013.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP013.LOG.isTraceEnabled()) {
			ModuloCSP013.LOG.trace("Fin de execute en clase ModuloCSP013");
		}
		return resultado;
	}
	/**
	 * Usaremos este módulo cómo módulo general para el capital en renovaciones
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
	private BigDecimal moduloCSP013(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		
		//variables locales
		BigDecimal csp013 = BigDecimal.ZERO;
		BigDecimal varFactorRev = BigDecimal.ZERO;
		List<DetalleCorriente> varProyNominal = null;
		Timestamp varfecvcto = null;
		int varX;
		Modulo moduloNominal = null;
		BigDecimal varCSPj = BigDecimal.ZERO;;
		//Fin variables locales
		
		if (ModuloCSP013.LOG.isTraceEnabled()) {
			ModuloCSP013.LOG.trace("Inicio de la función << moduloCSP013 >> de la clase ModuloCSP013, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo
		 * que no varían por periodo, así como las variables internas que tampoco varíen por periodo, 
		 * y que se dejarán accesibles para su uso en el subproceso por los siguientes periodos a calcular.
		 * 
		 * 		varPrc= umic.capitales.porevalcap/100 --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * 		varFactorRev = 1 + varPrc --> dejo la variable en memoria, disponible para el subproceso de la umic.
		 * 		varProyNominal = proyUmic
		 * 		varfecvcto = umic.fechas.fecefecfin
		 * 
		 * Para cualquier periodo j (j >= 1), se calculará el importe del capital correspondiente al periodo j como: 
		 * 
		 * 		Se invocará al módulo CSP001. --> varCSP001j  = CSP001 (varProyNominal, periodoProyeccion , fcalc, umic,btcUmic,codSubproceso)
		 * 			Si proyUmic(j).fecDevengo <= varfecvcto ó proyUmic(j).fecDevengo es nula --> CSP013 (j)= varCSP001j  
		 * 			En caso contrario: varX = TC (varfecvcto, proyUmic(j).fecDevengo)  + 1 --> CSP013 (j)= varCSP001j  * 〖(varFactorRev 〗^x) 
		 */
			
		
			varFactorRev = UtilModulos.getVarUnoMasPrp(mapVariables, CLAVE_VAR_FACTOR_REV, umic.getCapitales().getPorevalcap());
			
			varProyNominal = proyUmic;
			varfecvcto = umic.getFechas().getFecefecfin();
			 
			List<DetalleCorriente> listaCorrienteUmic;
			//Recuperará la proyección previamente calculAda en BTI para la umic
			listaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(), umic.getKey());
			
			if(null == listaCorrienteUmic || listaCorrienteUmic.size() == 0){
				mapVariables.remove(CLAVE_LST_PROY);
				listaCorrienteUmic = UtilModulos.getVarLstProyeccion(mapVariables, CLAVE_LST_PROY, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
			}
			
			if(iteracion <= listaCorrienteUmic.size()){
				varCSPj = listaCorrienteUmic.get(iteracion-1).getBloqueBySubproceso(codSubproceso).getImpFlujoNominal();
				mapVariables.put(CLAVE_VAR_CSP, varCSPj);
			}
			else{
				varCSPj = (BigDecimal) mapVariables.get(CLAVE_VAR_CSP);
			}
			if((bloqueCorriente.getFechaDevengo() == null) || (bloqueCorriente.getFechaDevengo().before(varfecvcto) || bloqueCorriente.getFechaDevengo().equals(varfecvcto))){
				csp013 = varCSPj;
			} else {
				
				varX = FuncionesAuxiliares.tc(varfecvcto, bloqueCorriente.getFechaDevengo()) + 1;
				csp013 = varCSPj.multiply(varFactorRev.pow(varX)); 
			
			}
		
		if (ModuloCSP013.LOG.isTraceEnabled()) {
			ModuloCSP013.LOG.trace("Fin de la función << moduloCSP013 >> de la clase ModuloCSP013, para la iteración = {}", iteracion);
		}
		return csp013;
	}
}
