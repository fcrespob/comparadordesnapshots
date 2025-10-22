package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Months;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.FuncionesAuxiliares;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * 
 * Es la adaptación del módulo PRI003 para Colectivos, para tener en cuenta en la última renovación antes 
 * del vencimiento la prima devengada, que según el caso puede ser la prima completa anual o no.
 * 
 * @author ogperez
 *
 */
public class ModuloPRI307 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI307.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI307;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_FP = ConstantsModulos.CTE_VAR_FP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_OP_VAR_GE = ConstantsModulos.CTE_VAR_OP_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_OP_VAR_PRP = ConstantsModulos.CTE_VAR_OP_PRP.concat(CLAVE_MODULO);
	
	private static final String CLAVE_VAR_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA.concat(CLAVE_MODULO);
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
			if (ModuloPRI307.LOG.isTraceEnabled()) {
				ModuloPRI307.LOG.trace("Inicio de execute en clase PRI307");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI307
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI307
			resultado = moduloPRI307(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI307.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI307.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI307.LOG.isTraceEnabled()) {
			ModuloPRI307.LOG.trace("Fin de execute en clase PRI307");
		}

		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * Es la adaptación del módulo PRI003 para Colectivos, para tener en cuenta en la última renovación antes del vencimiento
	 * la prima devengada, que según el caso puede ser la prima completa anual o no.
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
	private BigDecimal moduloPRI307(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		String varCriterioFecha = ConstantsFunciones.CTE_CADENA_VACIA;
		Integer varNumAnualidades;
		BigDecimal pri307 = BigDecimal.ZERO;
		BigDecimal varPNA0;
		BigDecimal varPRP;
		Timestamp varfechaEfecto;
		BigDecimal varGE;
		Timestamp varAntRenova;
		Timestamp varProxRenova;
		Timestamp varfecvcto;
		String varTipFlxPrima;
		BigDecimal varFP;
		BigDecimal varNmeses;
		BigDecimal varPNA;
		BigDecimal opVarGE;
		BigDecimal opVarPRP;
		
		//Fin variables locales
		
		if (ModuloPRI307.LOG.isTraceEnabled()) {
			ModuloPRI307.LOG.trace("Inicio función << moduloPRI307 >> de la clase ModuloPRI307, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| !bloqueCorriente.getFechaPago().before(umic.getFechas().getFecfinpagprim()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return pri307;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return pri307;
		}
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo,
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso
		 * por los siguientes periodos a calcular. 
		 * 
		 * 		Variables de Apoyo
		 * 		-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 		Si la variable de apoyo  retornada es nula se devuelve error funcional A6 - No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando el proceso
		 * 		para la UMIC.
		 * 
		 * 		Variables Módulo
		 * 		-	varBloque= obtenerConfiguracion.getBloqueBySubproceso(proyUmic, codSubproceso)
		 *		-	varPNA0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
		 *		-	varPRP = umic.primas.prevprima --> dejo la variable en memoria disponible para el procesado de la umic.
		 *		
		 *		Si umic.datosGenerales.cnegocio = ‘I’
		 *		-	varfechaEfecto = umic.fechas.fecefecIni;
		 *		En caso contrario:
		 *		-	varfechaEfecto = umic.fechas. fecinisus;
		 *
		 *		-	varGE = umic.baseTecIni .pgastgesex1I --> dejo la variable en memoria
		 *		procesado de la umic.
		 *		-	varAntRenova = umic.fechas. fecdesderenova --> Se guarda la variable en memoria para el subproceso de la umic.
		 *		-	varProxRenova = umic.fechas. fechastarenova --> Se guardan la  variable en memoria para el subproceso de la umic.
		 *		-	varNR = umic.duraciones.nrenovaciones
		 *		-	varfecvcto = umic.fechas.fecefecfin
		 *		-	varTipFlxPrim = umic.datosGenerales.tipoflexprim -->  Se guarda la  variable en memoria para el subproceso de la umic.
		 *
		 *		Calculamos el nº de pagos por anualidad evaluando el campo umic.primas.cformpago de la siguiente manera:
		 *		Si umic.primas.cformpago = 4
		 *    	-	varFP = 12
		 *    	Si umic.primas.cformpago = 3
		 *      -	varFP =4
		 *      Si umic.primas.cformpago =1 o 2
		 *      -   varFP= umic.primas.cformpago
		 *      
		 *      -	varNmeses = 12/ varFP
		 *      -> dejo las variables en memoria, disponible para el subproceso de la umic.
		 *      
		 *      Para cualquier periodo j se calculará: 
		 *      -	Si mes(proyUmic(j). varBloque.fecPago) = Mes(varfechaEfecto) --> PRI307(j) 
		 * 		-	Si no --> devolver 0
		 */		
		
		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
		
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
			// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
			// Fin de la obtención y validación del criterio de fecha.
		}
		
		//Variables comunes a todas las iteraciones
		varPNA0 = umic.getPrimas().getIprimanetaini();
		varPRP = umic.getPrimas().getPrevprima();
		varGE = umic.getBti().getPgastgesex1I();
		varfecvcto = umic.getFechas().getFecefecfin();
		varTipFlxPrima = umic.getDatosGenerales().getTipoflexprim();
		//Modificación 14022020
		if (varTipFlxPrima.equals("N")) {
			varfecvcto = umic.getFechas().getFecfinpagprim();
		}
		//	
		varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
		varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
		
		if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(umic.getDatosGenerales().getCnegocio()) &&
				ConstantsFunciones.CTE_APOR_REDUCIDA.equals(umic.getDatosGenerales().getCsitupol())) {
			varfechaEfecto = umic.getFechas().getFecinisus();
		} else {
			varfechaEfecto = UtilModulos.getVarFecEfectoPrimas(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		}
		
		varFP = (BigDecimal) mapVariables.get(CLAVE_VAR_FP);
		if (varFP == null){
			if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_4)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_12);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_3)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_4);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_1) || umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_2)){
				varFP = new BigDecimal(umic.getPrimas().getCformpago());
			}			
			mapVariables.put(CLAVE_VAR_FP, varFP);
		}	
		varNmeses = BigDecimal.valueOf(ConstantsFunciones.CTE_12).divide(varFP);
		
		
		//Si mes(proyUmic(j). varBloque.fecPago) = Mes(fechaProxRenovacion) se calcula pri307
		Fecha fechaProxRenovacion = UtilFechas.getFecha(varProxRenova);
		Fecha fechaPago = UtilFechas.getFecha(bloqueCorriente.getFechaPago());
		
		if (fechaPago.getMes() == fechaProxRenovacion.getMes()) {
			
			// Calculo varPNA
			varNumAnualidades = FuncionesAuxiliares.tc(varfechaEfecto, bloqueCorriente.getFechaPago());
			if(UtilFechas.getMes(varfechaEfecto) != UtilFechas.getMes(varProxRenova)){
				varNumAnualidades++;
			}
			//Se calculan y almacenan los términos que no varían por iteración
			opVarGE = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_GE);
			opVarPRP = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_PRP);
			if (opVarGE == null){
				opVarGE = BigDecimal.ONE.subtract(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				opVarPRP = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				mapVariables.put(CLAVE_OP_VAR_GE, opVarGE);
				mapVariables.put(CLAVE_OP_VAR_PRP, opVarPRP);
			}
			varPNA = varPNA0.multiply(opVarGE.multiply(opVarPRP.pow(varNumAnualidades)));
			
			if (!proyUmic.get(iteracion-ConstantsFunciones.CTE_1).getFechaDesde().before(varProxRenova)) {
				varAntRenova = varProxRenova;
				varProxRenova = UtilFechas.incrAnyo(varProxRenova, ConstantsFunciones.CTE_1);
				mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
				mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
			}
			
			// Calculo PRI307
			pri307 = calcularPri307(varAntRenova,varProxRenova,varfecvcto,varTipFlxPrima,varPNA,varFP,varCriterioFecha,varNmeses);
		}
		
		
		if (ModuloPRI307.LOG.isTraceEnabled()) {
			ModuloPRI307.LOG.trace("Fin función << moduloPRI307 >> de la clase ModuloPRI307, para la iteracion = {}, con resultado prim307 = {}", iteracion, pri307);
		}
		
		return pri307;
	}
	
	/**
	 * Función encargada de realizar el calculo de PRI307
	 * @param varNmeses 
	 * @param varCriterioFecha 
	 * @param varFP 
	 * @param varPNA 
	 * @param varTipFlxPrima 
	 * @param varfecvcto 
	 * @param varProxRenova 
	 * @param varAntRenova 
	 * @return pri307
	 */
	private BigDecimal calcularPri307(final Timestamp varAntRenova, final Timestamp varProxRenova,
			final Timestamp varfecvcto,final String varTipFlxPrima, final BigDecimal varPNA, final BigDecimal varFP,
			final String varCriterioFecha, final BigDecimal varNmeses) {
		//Variables locales
		BigDecimal pri307 = BigDecimal.ZERO;
		BigDecimal varPrimRec;
		Integer varDifMeses;
		Integer varNumRec;
		Timestamp varfecUltRec;
		Timestamp FecAntRec; 
		BigDecimal varD1;
		BigDecimal varD2;
		//Fin variables locales
		
		if (ModuloPRI307.LOG.isTraceEnabled()) {
			ModuloPRI307.LOG.trace("Inicio de la función << calcularPrim307 >> de la clase ModuloPRI307");
		}		
		

		if ( !varAntRenova.after(varfecvcto) && !varfecvcto.after(varProxRenova) ) {
			if (varTipFlxPrima.equals("A") || varTipFlxPrima.equals("B")) {
				pri307 = varPNA;	
			}
			else if (varTipFlxPrima.equals("E")) {
				if (varFP.equals(BigDecimal.ONE)) {
					varD1 = new BigDecimal(FuncionesAuxiliares.nDias(varAntRenova, varfecvcto, varCriterioFecha));
					pri307 = (varD1.multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365)).multiply(varPNA);					
				}
				else {
					varPrimRec = varPNA.divide(varFP, ConstantsFunciones.MATH_CONTEXT);
					
					DateTime start = new DateTime(varAntRenova.getTime());
					DateTime end = new DateTime(varfecvcto.getTime());
					varDifMeses = Months.monthsBetween(start,end).getMonths();
				
					if (varDifMeses < ConstantsFunciones.CTE_0) {
						varDifMeses = Math.abs(varDifMeses);
					}
					
					varNumRec =  (varDifMeses / varNmeses.intValue());
					
					varfecUltRec = UtilFechas.incrMeses(varAntRenova, null, (varNmeses.intValue()*varNumRec), false);
					if (varfecUltRec.after(varfecvcto)) {
						varfecUltRec = UtilFechas.incrMeses(varfecUltRec, null, -varNmeses.intValue(), false);
					}
					FecAntRec = UtilFechas.incrMeses(varfecUltRec, null, -varNmeses.intValue(), false);
					varD1 = new BigDecimal(FuncionesAuxiliares.nDias(varfecUltRec, varfecvcto, varCriterioFecha));
					varD2 = new BigDecimal(FuncionesAuxiliares.nDias(FecAntRec, varfecUltRec, varCriterioFecha));
					
					pri307 = varPrimRec.multiply(new BigDecimal(varNumRec).add(varD1.divide(varD2, ConstantsFunciones.MATH_CONTEXT)));	
					
				}
			}
			else if (varTipFlxPrima.equals("N")) {
				if (varFP.equals(BigDecimal.ONE)) {
					pri307 = varPNA;
				}
				else {
					varPrimRec = varPNA.divide(varFP, ConstantsFunciones.MATH_CONTEXT);
					
					DateTime start = new DateTime(varAntRenova.getTime());
					DateTime end = new DateTime(varfecvcto.getTime());
					varDifMeses = Months.monthsBetween(start,end).getMonths();
					if(UtilFechas.getDia(varAntRenova) != UtilFechas.getDia(varfecvcto)) {
						varDifMeses++;
					}						
					
					if (varDifMeses < ConstantsFunciones.CTE_0) {
						varDifMeses = varDifMeses + ConstantsFunciones.CTE_12;
					}
					if(varDifMeses == ConstantsFunciones.CTE_0) {
						varDifMeses = varNmeses.intValue();
					}

					BigDecimal varNumRecibos =  BigDecimal.valueOf(varDifMeses).divide(varNmeses, RoundingMode.UP);
					
					pri307 = varPrimRec.multiply(varNumRecibos);					
				}
			}
		}
		else {
			pri307 = varPNA;
		}
					
					
		if (ModuloPRI307.LOG.isTraceEnabled()) {
			ModuloPRI307.LOG.trace("Fin de la función << calcularPrim307>> de la clase ModuloPRI307");
		}
			
		return pri307;
	}
}
