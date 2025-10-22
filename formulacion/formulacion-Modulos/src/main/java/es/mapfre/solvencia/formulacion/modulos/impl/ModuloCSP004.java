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
 * Clase encargada del cálculo que devuelve el capital.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP(004,tc) = Capital(0)*(1+mod_beta/100)
 * 
 * @author apedro
 *
 */
public class ModuloCSP004 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP004.class);
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP004;
	private static final String CLAVE_VAR_MOD_BETA = ConstantsModulos.CTE_VA_MOD_BETA.concat(CLAVE_MODULO);

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_CSP004;
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
			if (ModuloCSP004.LOG.isTraceEnabled()) {
				ModuloCSP004.LOG.trace("Inicio de execute en clase ModuloCSP004");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloCSP004
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			
			//Invocamos a la función de calculo CSP004
			resultado = moduloCSP004(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP004.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP004.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP004.LOG.isTraceEnabled()) {
			ModuloCSP004.LOG.trace("Fin de execute en clase ModuloCSP004");
		}
		
		return resultado;
		
	}
	
	/**
	 * Modulo de cálculo que devuelve el capital.
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
	private BigDecimal moduloCSP004(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp004 = BigDecimal.ZERO;
		BigDecimal varModBeta;
		BigDecimal varCapital;
		BigDecimal varBeta;
		//Fin variables locales

		if (ModuloCSP004.LOG.isTraceEnabled()) {
			ModuloCSP004.LOG.trace("Inicio función << moduloCSP004 >> para la iteracion = {}", iteracion);
		}

		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (null == bloqueCorriente.getFechaDevengo()) {
			return csp004;
		}
		
		csp004 = (BigDecimal) mapVariables.get(CLAVE_MODULO);
		
		if (csp004 == null){
		
			//Se recupera y valida la definición auxiliar MOD_BETA
			varModBeta = UtilModulos.getsetBigDecimalRecuperarDefinicionAuxiliar(mapVariables, CLAVE_VAR_MOD_BETA, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), ConstantsModulos.CTE_VA_MOD_BETA);
			ValidacionesComunesModulos.validarVariableDeApoyoVarMod_Beta(varModBeta);
			
			varBeta = BigDecimal.ONE.add(varModBeta.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			varCapital = umic.getCapitales().getIcapini();
			
			//Se calcula csp004 = varCapital * VarBeta
			csp004 = varCapital.multiply(varBeta);
			
			mapVariables.put(CLAVE_MODULO, csp004);

		}

		if (ModuloCSP004.LOG.isTraceEnabled()) {
			ModuloCSP004.LOG.trace("Fin función << moduloCSP004 >> de la clase ModuloCSP004, para la iteracion = {}, con resultado capitalCartera = {}", iteracion, csp004);
		}
		
		return csp004;
	}

}
