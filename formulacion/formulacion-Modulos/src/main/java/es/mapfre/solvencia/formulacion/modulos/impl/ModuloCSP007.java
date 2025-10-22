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

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloCSP007 implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP007.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSP007;
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
			if (ModuloCSP007.LOG.isTraceEnabled()) {
				ModuloCSP007.LOG.trace("Inicio de execute en clase ModuloCSP007");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloCSP007
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloCSP007
			resultado = moduloCSP007(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloCSP007.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSP007.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloCSP007.LOG.isTraceEnabled()) {
			ModuloCSP007.LOG.trace("Fin de execute en clase ModuloCSP007");
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
	private BigDecimal moduloCSP007(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal csp007 = BigDecimal.ZERO;
		List<DetalleCorriente> varProyNominal = null;
		List<DetalleCorriente> varProy204 = null;
		Modulo moduloNominal = null;
		Modulo moduloNominal1 = null;
		BigDecimal varCSP050 = null;
		BigDecimal varCSP204 = null;
		//Fin variables locales
		if (ModuloCSP007.LOG.isTraceEnabled()) {
			ModuloCSP007.LOG.trace("Inicio de la función << moduloCSP007 >> de la clase ModuloCSP007, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		/**
		 * Usaremos este módulo para el cálculo de la cuantía nominal de las garantías complementarias 
		 * del negocio de individuales
		 * 
		 * 
		 * Si  umic.spcom = ‘P’: o	Se devuelve error funcional XXX 
		 * – No se puede recuperar la clave de la garantía principal ya que 
		 * la umic de entrada está marcada como garantía principal en el maestro & umic.claveUmic, 
		 * finalizando el proceso para la UMIC.
		 * 
		 * En caso contrario: 
		 * 		varProyNominal = proyUmic
		 * 		varProy204 = proyUmic
		 * 
		 * Se guardarán las variables para el subproceso de la umic. 
		 * Para cada periodo j se  hará: 
		 * 
		 * 		varCSP050(j) = CSP050 (varProyNominal, periodoProyeccion , fcalc, umic,btcUmic,codSubproceso)
		 * 		varCSP204(j)  = CSP204 (varProy204, periodoProyeccion , fcalc, umic,btcUmic,codSubproceso)
		 * 
		 * Finalmente se obtendrá: CSP007(j) = varCSP204(j) - varCSP050(j)
		 */
		
		// Definición de variables auxiliares para agilizar las operaciones (se llaman varias veces).
		DatosGenerales datosGenerales = umic.getDatosGenerales();
		// Fin de la definición de las variables auxiliares.
		
		if (ConstantsModulos.CTE_UMIC_PRINCIPAL.equals(datosGenerales.getSpcom())){
			//Se devuelve error funcional AJ – No se puede recuperar la clave de la garantía principal ya que la umic de 
			//entrada está marcada como garantía principal en el maestro & umic.claveUmic, finalizando el proceso para la UMIC.
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AJ);
		}else{
			varProyNominal = proyUmic;
			varProy204 = proyUmic;
		}
		
		moduloNominal = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP050);
		moduloNominal1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP204);
		
		varCSP050 = (BigDecimal) moduloNominal.execute(varProyNominal, bloqueCorriente, iteracion , fcalc, umic, btcUmic,mapVariables,codSubproceso);
		varCSP204 = (BigDecimal) moduloNominal1.execute(varProy204, bloqueCorriente, iteracion , fcalc, umic, btcUmic,mapVariables,codSubproceso);
		
		csp007 = varCSP204.subtract(varCSP050);
		
		if (ModuloCSP007.LOG.isTraceEnabled()) {
			ModuloCSP007.LOG.trace("Fin de la función << moduloCSP007 >> de la clase ModuloCSP007, para la iteración = {}", iteracion);
		}
		return csp007;
	}
}
