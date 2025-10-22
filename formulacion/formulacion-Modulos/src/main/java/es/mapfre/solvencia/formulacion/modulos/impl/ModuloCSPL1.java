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


public class ModuloCSPL1 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPL1.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_CSPL1;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);

	private static final String CLAVE_CRIEDAD = ConstantsModulos.CTE_VA_CRIT_EDA;
	private static final String CLAVE_VAR_CRIEDAD = CLAVE_CRIEDAD.concat(CLAVE_MODULO);

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
		BigDecimal resultado = null;
		//Fin variables locales
		
		try {
			
			if (ModuloCSPL1.LOG.isTraceEnabled()) {
				ModuloCSPL1.LOG.trace("Inicio de execute en clase ModuloCSPL1");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVBX363
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente =  (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			//final String terminal = (String) args[ConstantsModulos.PARAM_P_TERMINAL];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo CSPL1
			resultado = moduloCSPL1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloCSPL1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloCSPL1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloCSPL1.LOG.isTraceEnabled()) {
			ModuloCSPL1.LOG.trace("Fin de execute en clase ModuloCSPL1");
		}
		
		return resultado;
	}
	
	/**
	 * Módulo que calcula el importe nominal para la proyección de Provisión Matemática por Fórmula Cerrada
	 *  La expresión matemática para su determinación es la siguiente:
	 * 				VBX363 = RENTA + FALL + GASTO
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
	 * @param terminal
	 * 			Terminal de cálculo
	 */
	private BigDecimal moduloCSPL1(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables,
			final String codSubproceso) {		
		//Variables locales
		BigDecimal cspl1 = BigDecimal.ZERO;
		String varCriFec;
		String varCriEdad;
		BigDecimal varPu;
		//Fin variables locales
		
		
		if (ModuloCSPL1.LOG.isTraceEnabled()) {
			ModuloCSPL1.LOG.trace("Inicio función << moduloCSPL1 >> de la clase ModuloCSPL1, para la iteracion = {}", iteracion);
		}
		
		//Validamos los campos de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, fcalc, umic, btcUmic);
		
		/**
		 * Si estoy en el primer periodo, se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varíen por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular.
		 * 
				Variables de Apoyo
				-	VarCriterFec--> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	VarBeta --> obtenerConfiguracion.recuperarVariableApoyo(MODBETA)
				-	VarCriterEdad --> obtenerConfiguracion.recuperarVariableApoyo(ID-CRITERIO)
				
				Si alguna de las variable de apoyo  retornada es nula se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo & NombreVariableApoyo, finalizando el proceso para la UMIC.
		 */

		
		//INICIO DE VALORES QUE NO CAMBIAN POR ITERACION
		// Se definen estas variables auziliares dado que se llaman varias veces.
		final Integer ccartera = umic.getDatosGenerales().getCcartera();
		final Integer kmodalidad = umic.getDatosGenerales().getKmodalidad();
		final Integer kgarantia = umic.getDatosGenerales().getKgarantia();
		final String kprestacion = umic.getDatosGenerales().getKprestacion();
		
		// Cálculo y validación de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIFEC);
		varCriEdad = UtilModulos.getVarCriEdad(mapVariables, CLAVE_VAR_CRIEDAD, ccartera, kmodalidad, kgarantia, btcUmic.getBaseTec(), CLAVE_CRIEDAD);

		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriEdad(varCriEdad);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		
		//Variables módulo
		if (bloqueCorriente.getFechaDevengo() == null) {
			return cspl1;
		}
		varPu = umic.getPrimas().getIprimanetaini();
		cspl1 = varPu.add(BigDecimal.valueOf(600,00));
		
		if (ModuloCSPL1.LOG.isTraceEnabled()) {
			ModuloCSPL1.LOG.trace("Fin función << moduloCSPL1 >> de la clase ModuloCSPL1, para la iteracion = {}, con resultado cspl1 = {}", iteracion, cspl1);
		}
		
		return cspl1;
	}

}
