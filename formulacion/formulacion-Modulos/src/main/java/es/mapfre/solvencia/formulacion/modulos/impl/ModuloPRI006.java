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
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.
 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales 
 * con cálculo exclusivamente en 1 periodo del año que coincide con los aniversarios.
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(006,tc)= Pnatc*(1+PRP/100)^tc*(1+RPF)/FP
 * 
 * @author apedro
 *
 */
public class ModuloPRI006 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI006.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI006;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_TC = ConstantsModulos.CTE_VAR_TC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
	
	private static final String CLAVE_CALCULO_PRI006 = "pri006";
	private static final String CLAVE_OPERADOR_RPF_NP = "operadorRPF_NP";
	private static final String CLAVE_FACTOR_ANUAL = "factorAnualPRI006";
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
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
			if (ModuloPRI006.LOG.isTraceEnabled()) {
				ModuloPRI006.LOG.trace("Inicio de execute en clase ModuloPRI006");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI006
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI006
			resultado = moduloPRI006(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI006.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI006.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI006.LOG.isTraceEnabled()) {
			ModuloPRI006.LOG.trace("Fin de execute en clase ModuloPRI006");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
	 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.
	 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales 
	 * con cálculo exclusivamente en 1 periodo del año que coincide con los aniversarios.
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
	private BigDecimal moduloPRI006(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim006 = BigDecimal.ZERO;
		String varCriFec;
		BigDecimal factorAnual;
		Integer varTCm;
		Integer varTC;
		Integer varBeta;
		BigDecimal varPNATC;
		BigDecimal varPRP;
		BigDecimal varRPF;
		BigDecimal varNP = BigDecimal.ZERO;
		BigDecimal operadorRpfNp;
		//Fin variables locales
		
		if (ModuloPRI006.LOG.isTraceEnabled()) {
			ModuloPRI006.LOG.trace("Inicio función << moduloPRI006 >> de la clase ModuloPRI006, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return prim006;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim006;
		}		
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular. 
		 * 		Variables Módulo
		 * 			-	varPNA0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
		 * 			-	varPRP = umic.primas.prevprima --> dejo la variable en memoria disponible para el procesado de la umic.
		 * 			-	varRPF=umic.primas.precargfrac --> dejo la variable en memoria disponible para el procesado de la umic. 
		 * 	
		 * 	Para cualquier periodo j se calculará:
		 * 		Si proyUmic(j).varBloque.fecPago = null:
		 * 			- PRI006 = 0
		 * 		Si proyUmic(j).varBloque.fecPago no es nulo:
		 * 			-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol= ‘RE’
		 * 				-	Varfecefec = umic.fechas.fecefecred
		 * 			-	Si la póliza debe estar en vigor, es decir  umic.datosGenerales.csitupol= ‘VI’
		 * 				-	Varfecefec = umic.fechas fecinisus
		 * 		
		 * 	
		 */
		
		// Obtención y validación del criterio de fecha.
		varCriFec = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			ValidacionesComunesModulos
				.validarVariableDeApoyoVarCriterioFecha(varCriFec);
		}
		// Fin de la obtención y validación del criterio de fecha.
		
		Timestamp varfecefec = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		
		varTC = UtilModulos.getVarTC(mapVariables, CLAVE_VAR_TC, varfecefec, fcalc);
		varTCm = UtilModulos.getVarTCmIni(mapVariables, CLAVE_VAR_TCM, ConstantsModulos.CTE_FIRST_ITER, varfecefec, fcalc);
		
		// β – Periodo mensual de proyección: Secuencial con inicio en valor 0, y final en el último periodo de proyección.
		varBeta = iteracion - 1;
		
		Integer potencia = (varTCm + varBeta)/12 - varTC;

		/**
		 * Se calculará el importe de prima correspondiente al periodo j como:
		 * 		PRI006(j) = varPNA0*(1+varPRP/100)^(entero(varTcm+varBeta/12)-varTC) * [((1+varRPF/100  ))/varNP]
		 */
		
		final String clave = CLAVE_CALCULO_PRI006.concat("_").concat(String.valueOf(potencia));
		if(!mapVariables.containsKey(clave)) {
			if (!mapVariables.containsKey(CLAVE_FACTOR_ANUAL)){
				// Recuperación valores auxiliares.
				
				varPRP = umic.getPrimas().getPrevprima();
				varRPF = umic.getPrimas().getPrecargfrac();
				// Fin de la recuperación de los valores auxiliares.
				
				//(1+varPRP/100)
				factorAnual = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				mapVariables.put(CLAVE_FACTOR_ANUAL, factorAnual);
				
				/**
				 * Calcular el nº de pagos por anualidad evaluando el campo umic.primas.cformpago de la siguiente manera:
					Si umic.primas.cformpago = 4
					    varNP = 12
					Si umic.primas.cformpago = 3
					    varNP =4
					Si umic.primas.cformpago =1 o 2
					    varNP = umic.primas.cformpago
					varNP --> dejo la variable en memoria, disponible para el subproceso de la umic.
	
				 */
				if (ConstantsFunciones.CTE_4_STRING.equals(umic.getPrimas().getCformpago())) {
					varNP = ConstantsFunciones.CTE_OPER_12;
				} else if (ConstantsFunciones.CTE_3_STRING.equals(umic.getPrimas().getCformpago())) {
					varNP = ConstantsFunciones.CTE_OPER_4;
				} else if (ConstantsFunciones.CTE_1_STRING.equals(umic.getPrimas().getCformpago()) || ConstantsFunciones.CTE_2_STRING.equals(umic.getPrimas().getCformpago())) {
					varNP = new BigDecimal(umic.getPrimas().getCformpago());
				}
				
				//[((1+varRPF/100  ))/varNP]
				operadorRpfNp = BigDecimal.ONE.add(varRPF.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).divide(varNP, ConstantsFunciones.MATH_CONTEXT);
				mapVariables.put(CLAVE_OPERADOR_RPF_NP, operadorRpfNp);
				
			} else {
				factorAnual = (BigDecimal) mapVariables.get(CLAVE_FACTOR_ANUAL);
				operadorRpfNp = (BigDecimal) mapVariables.get(CLAVE_OPERADOR_RPF_NP);
			}
			
			varPNATC = umic.getPrimas().getIprimanetaact();
			prim006 = varPNATC.multiply(Util.pow(factorAnual, potencia)).multiply(operadorRpfNp);
			mapVariables.put(clave, prim006);
			
		} else {
			prim006 = (BigDecimal)mapVariables.get(clave);
		}
		
		if (ModuloPRI006.LOG.isTraceEnabled()) {
			ModuloPRI006.LOG.trace("Fin función << moduloPRI006 >> de la clase ModuloPRI006, para la iteracion = {}, con resultado prim006 = {}", iteracion, prim006);
		}
			
		return prim006;
	}

}
