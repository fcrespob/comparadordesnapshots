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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada de determinar el cálculo de los factores de actualización financiera para la garantía  
 * en un periodo dado así como el Flujo Actualizado de dicho periodo.(Variación ACT001, usando solo interés técnico 2.)
 * La expresión matemática para su determinación es la siguiente:
 *  It(CT)=it2
 * 			Si fcalc <= ffincas entonces
 * 					Si j <= ffincas entonces
 * 							Actfin(fcal,j) = (1 + it(CT))^ -(nannos(fcal,j))
 * 					Sino
 * 							Actfin(fcal,j) = (1 + it(CT))^ -(nannos(fcal,ffincas)) * (1 + it2)^ -(nannos(ffincas, j))
 * 			Sino
 * 					Actfin(fcal,j) = (1 + it2)^ -(nannos(fcal,j))
 *  
 * @author agonzalezgar
 *
 */
public class ModuloACT002 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloACT002.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ACT002;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_FACTOR_I2 = "factorI2".concat(CLAVE_MODULO);
	private static final String CLAVE_TERMINO_ANOFFINCAS = "terminoAnoffincas".concat(CLAVE_MODULO);
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
			if (ModuloACT002.LOG.isTraceEnabled()) {
				ModuloACT002.LOG.trace("Inicio de execute en clase ModuloACT002");
			}
			
			// Recuperamos los datos que le pasaremos a la función moduloACT002
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función de calculo moduloACT002
			resultado = moduloACT002(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloACT002.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloACT002.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloACT002.LOG.isTraceEnabled()) {
			ModuloACT002.LOG.trace("FIN de execute en clase ModuloACT002");
		}
		
		return resultado;
	}
	/**
	 * Modulo encargado de determinar el cálculo de los factores de actualización financiera para la garantía  
	 * en un periodo dado así como el Flujo Actualizado de dicho periodo.(Variación ACT001, usando solo interés técnico 2.)
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
	private BigDecimal moduloACT002(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal act002 = BigDecimal.ZERO;
		String varCriFec;
		BigDecimal varI2;
		Timestamp varffincas;
		BigDecimal varAnnoJ;
		int diasEntreFechas;
		BigDecimal varAnoffincas;
		BigDecimal factorI2;
		BigDecimal terminoAnoffincas;
		//Fin variables locales

		if (ModuloACT002.LOG.isTraceEnabled()) {
			ModuloACT002.LOG.trace("Inicio función << moduloACT002 >> de la clase ModuloACT002, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaPago() == null) {
			return BigDecimal.ZERO;
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
				Variables de Apoyo
				-	VarCriterFec -->    obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
				-	Si la variable de apoyo  retornada es nula se devuelve error funcional 005 - 
				No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando el proceso para la UMIC.
				Variables Módulo
				-	varffincas = btcUmic.fecIniTramo2
				-	varI2= btcUmic.Itcalc2
				Para cualquier periodo j, se calculará la actualización financiera como: 
				
				-	Si fcalc <= varffincas entonces:
				
					o	Si proyUmic(j).fecPago <=  varffincas entonces:
						varAnoJ = nannos(fcalc, proyUmic(j).fecPago, VarCriterFec);
						actFinJ = (1 + varI2/100)^ -varAnnoJ
					o	 Si proyUmic(j).fecPago >  varffincas entonces:
							varAnoJ = nannos(varffincas, proyUmic(j).fecPago, VarCriterFec);
							varAnoffincas= nannos(varffincas, fcalc, VarCriterFec);
				             actFinj = (1 + varI2/100)^-varAnoffincas * (1 + (varI2/100))^-varAnnoJ
				-	Si fcalc > varffincas entonces:
						varAnoJ = nannos(fcalc, proyUmic(j).fecPago, VarCriterFec);
						actFinJ = (1 + (varI2/100))^-varAnnoJ
		 */
		
		// Cálculo y validación de las variables de apoyo.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);	
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}// Si llega a este punto las validaciones anteriores serán válidas para la iteración inicial, luego serán válidas para las siguientes.
		// Fin de la validación de las variables de apoyo.
		
		varffincas = btcUmic.getFecInitramo().get(1);
		if (varffincas == null){
			varffincas = btcUmic.getFecInitramo().get(0);
		}

		varI2 = btcUmic.getItcalc().get(1);
		varAnnoJ = FuncionesAuxiliares.nAnnos(fcalc, bloqueCorriente.getFechaPago(), varCriFec);
		varAnoffincas = FuncionesAuxiliares.nAnnos(varffincas, fcalc, varCriFec);
		diasEntreFechas = UtilFechas.diferenciasDeFechas(fcalc, varffincas);
		
		if ((factorI2=(BigDecimal) mapVariables.get(CLAVE_FACTOR_I2)) == null){
			factorI2 = BigDecimal.ONE.add(varI2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT));
			mapVariables.put(CLAVE_FACTOR_I2, factorI2);
		}
		if ((terminoAnoffincas= (BigDecimal) mapVariables.get(CLAVE_TERMINO_ANOFFINCAS)) == null){
			terminoAnoffincas = Util.pow(factorI2, varAnoffincas.negate());
			mapVariables.put(CLAVE_TERMINO_ANOFFINCAS,terminoAnoffincas);
		}
		
		if (diasEntreFechas >= 0) {
			
			diasEntreFechas = UtilFechas.diferenciasDeFechas(bloqueCorriente.getFechaPago(), varffincas);
			
			if (diasEntreFechas >= 0) {
				//actfinj = (1 + varI2/100)^-varAnnoJ
				act002 = terminoVarAnoJ(factorI2, varAnnoJ);
			} else {
				varAnnoJ = FuncionesAuxiliares.nAnnos(varffincas, bloqueCorriente.getFechaPago(), varCriFec);
				
				//actfinj = (1 + varI2/100)^-varAnoffincas  * (1 + (varI2 / 100))^-varAnnoJ
				act002 = terminoAnoffincas.multiply(terminoVarAnoJ(factorI2, varAnnoJ));
			}
		} else {
			//actfinj = (1 + (varI2 / 100))^-varAnnoJ
		    act002 = terminoVarAnoJ(factorI2, varAnnoJ);
		}
		
		if (ModuloACT002.LOG.isTraceEnabled()) {
			ModuloACT002.LOG.trace("Fin función << moduloACT002 >> de la clase ModuloACT002, para la iteracion = {}, con resultado act002 = {}", iteracion, act002);
		}
			
		return act002;
	}
	
	/**
	 * Resuelve esta operacion -- > resultado = (1 + (varI2 / 100))^-varAnnoJ
	 * @param varI2
	 * @param varAnnoJ
	 * @return
	 */
	private BigDecimal terminoVarAnoJ (final BigDecimal factorI2, final BigDecimal varAnnoJ){
		final BigDecimal resultado = Util.pow(factorI2, varAnnoJ.negate());
		return resultado;
	}
	
}
