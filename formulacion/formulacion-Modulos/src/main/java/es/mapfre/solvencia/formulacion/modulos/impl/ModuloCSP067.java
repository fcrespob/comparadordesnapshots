package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.formulacion.util.UtilUmicPrincipal;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del calculo que devuelve el capital en riesgo de la garantía principal para la garantía complementaria.
 * La expresión matemática para su determinación es la siguiente:
 * 		CSP(067,tc)= CSP(FallecPpal,tc) - CSP(050,tc)garantía principal
 * 
 * @author apedro
 *
 */
public class ModuloCSP067 implements Modulo {

	/**
	 * Log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP067.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP067;
	private static final String CLAVELISTACORRUMIC = ConstantsModulos.CTE_LST_CORR_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_UMICPRINCIPAL = ConstantsModulos.CLAVE_VAR_CLAVEPRINCIPAL;
	
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
			if (ModuloCSP067.LOG.isTraceEnabled()) {
				ModuloCSP067.LOG.trace("Inicio de execute en clase ModuloCSP051");
			}
			
			//Inicio recuperación de datos que le pasaremos a la función moduloCSP067
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];

			//Invocamos a la función moduloCSP067
			resultado = moduloCSP067(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSP067.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP067.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSP067.LOG.isTraceEnabled()) {
			ModuloCSP067.LOG.trace("Fin de execute en clase ModuloCSP067");
		}
		
		return resultado;
	}
	/**
	 * Módulo de cálculo que devuelve el capital en riesgo de la garantía principal para la garantía complementaria.
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
	private BigDecimal moduloCSP067(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal csp067 = BigDecimal.ZERO;
		UmicKey varClavePrincipal;
		List<DetalleCorriente> varProyUmicPrincipal;
		BigDecimal varCSP050 = BigDecimal.ZERO;
		BigDecimal varCSPFallec;
		//Fin variables locales

		if (ModuloCSP067.LOG.isTraceEnabled()) {
			ModuloCSP067.LOG.trace("Inicio función << moduloCSP050 >> de la clase ModuloCSP050, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		DatosGenerales datosGenerales = umic.getDatosGenerales();
		if (ConstantsModulos.CTE_UMIC_PRINCIPAL.equals(datosGenerales.getSpcom())){
			//Se devuelve error funcional AJ – No se puede recuperar la clave de la garantía principal ya que la umic de 
			//entrada está marcada como garantía principal en el maestro & umic.claveUmic, finalizando el proceso para la UMIC.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AJ);
		}
			
		//Se recupera la clave de la UMIC principal
		varClavePrincipal = UtilUmicPrincipal.getUmicPrincipal(mapVariables, CLAVE_UMICPRINCIPAL, datosGenerales);
		
		//Se recupera el detalle corriente de la UMIC principal
		varProyUmicPrincipal = UtilModulos.getListaCorrienteUmic(mapVariables, CLAVELISTACORRUMIC,
						ConstantsModulos.CTE_BTI, btcUmic.getFecCierre(),
						varClavePrincipal);
		
		if(null == varProyUmicPrincipal || varProyUmicPrincipal.size() == 0){
			mapVariables.remove(CLAVELISTACORRUMIC);
			varProyUmicPrincipal = UtilModulos.getVarLstProyeccion(mapVariables, CLAVELISTACORRUMIC, ConstantsModulos.CTE_BTI_PROY, btcUmic.getFecCierre(), umic.getKey());
		}
		
		if (varProyUmicPrincipal == null || varProyUmicPrincipal.isEmpty() || varProyUmicPrincipal.get(iteracion-1).getBloqueFall() == null){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_DE);
		}
		
		//Se recupera la provisión matemática de la UMIC principal (modulo CSP050)
		varCSP050 = UtilUmicPrincipal.getCSP050(varProyUmicPrincipal.get(iteracion-1));
		
		if(null == varCSP050){
			varCSP050 = BigDecimal.ZERO;
		}
		//Se recupera el nominal de fallecimiento de la UMIC principal
		varCSPFallec = varProyUmicPrincipal.get(iteracion-1).getBloqueFall().getImpFlujoNominal();
		
		if(null == varCSPFallec){
			varCSPFallec = BigDecimal.ZERO;
		}
		
		//Se calcula CSP067 = varCSPFallec(j) - varCSP050(j)
		csp067 = varCSPFallec.subtract(varCSP050);
		
		if(csp067.compareTo(BigDecimal.ZERO) < 0){
			csp067 = BigDecimal.ZERO;
		}
		
		if (ModuloCSP067.LOG.isTraceEnabled()) {
			ModuloCSP067.LOG.trace("Fin función << moduloCSP067 >> de la clase ModuloCSP067, para la iteracion = {}, con resultado csp050 = {}", iteracion, csp067);
		}
		
		return csp067;
	}

}
