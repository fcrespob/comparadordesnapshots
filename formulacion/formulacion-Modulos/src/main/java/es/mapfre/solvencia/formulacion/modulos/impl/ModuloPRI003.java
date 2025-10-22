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
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.  
 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente 
 * en 1 periodo del año que coincide con los aniversarios.
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(003,tc)=Pna(0)*(1+PRP/100)^tc*(1-GE/100)*1/FP*(1+RECARGOFRO)^-(naños(Frenoant,j))
 * @author agonzalezgar
 *
 */
public class ModuloPRI003 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI003;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
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
			if (ModuloPRI003.LOG.isTraceEnabled()) {
				ModuloPRI003.LOG.trace("Inicio de execute en clase PRI003");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI003
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI003
			resultado = moduloPRI003(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI003.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI003.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI003.LOG.isTraceEnabled()) {
			ModuloPRI003.LOG.trace("Fin de execute en clase PRI003");
		}

		return resultado;
	}
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
	 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.  
	 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente 
	 * en 1 periodo del año que coincide con los aniversarios.
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
	private BigDecimal moduloPRI003(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		String varCriterioFecha;
		int varNumAnualidades;
		Timestamp varfechaEfecto;
		BigDecimal prim003 = BigDecimal.ZERO;
		BigDecimal varPNA0;
		BigDecimal varPRP;
		BigDecimal varGE;
		Integer Vardurprimas;
		Timestamp Varefecfinprimas = null;
		//Fin variables locales
		
		if (ModuloPRI003.LOG.isTraceEnabled()) {
			ModuloPRI003.LOG.trace("Inicio función << moduloPRI003 >> de la clase ModuloPRI003, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		varfechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		if(!(null == umic.getDuraciones().getNdurprima())){
			
			Vardurprimas = umic.getDuraciones().getNdurprima();
			Varefecfinprimas = UtilFechas.incrAnyo(varfechaEfecto, Vardurprimas);
			
		}
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if (null == bloqueCorriente.getFechaPago() || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA)
				|| bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin()) || bloqueCorriente.getFechaPago().equals(umic.getFechas().getFecefecfin())
				|| bloqueCorriente.getFechaPago().after(Varefecfinprimas) || bloqueCorriente.getFechaPago().equals(Varefecfinprimas)
				/*!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())*/) {
			return prim003;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim003;
		}		
		
		/**
		 * Si estoy en el primer periodo (j=1) se recuperarán los datos de la umic necesarios para el cálculo que no varían por periodo, 
		 * así como las variables internas que tampoco varían por periodo, y que se dejarán accesibles para su uso en el subproceso 
		 * por los siguientes periodos a calcular.
		 * 		Variables de Apoyo
		 * 		-	VarCriterFec --> obtenerConfiguracion.recuperarVariableApoyo(ID-TEMPORAL)
		 * 			o	Si la variable de apoyo  retornada es nula se devuelve error funcional 005 - No se ha encontrado la Variable de Apoyo & ID-TEMPORAL, finalizando el proceso para la UMIC.
		 * 		
		 * 		Variables Módulo
		 * 			-	varPNA0 = umic.primas.iprimanetaini --> dejo la variable en memoria disponible para el procesado de la umic.
		 * 			-	varPRP = umic.primas.prevprima --> dejo la variable en memoria disponible para el procesado de la umic.
		 * 			-	varGE = umic.baseTecIni.pgastgesex1I --> dejo la variable en memoria disponible para el procesado de la umic.
		 * 		
		 * 		Para cualquier periodo j se calculará: 
		 * 		varNumAnualidades = TC(umic.fechas fecinisus, proyUmic(j).varBloque.fecPago)  
		 * 		
		 * 		-	Si mes(proyUmic(j). varBloque.fecPago) = Mes(varfechaEfecto) --> PRI003(j) = varPNA0 * (1 - (varGE/100)) * (1 - (varPRP/100)) * entero(varNumAnualidades)
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
		varPNA0 = umic.getPrimas().getIprimatarada();
		varPRP = umic.getPrimas().getPrevprima();
		varGE = umic.getBti().getPgastgesex1I();
		
			
		varNumAnualidades = FuncionesAuxiliares.tc(varfechaEfecto, bloqueCorriente.getFechaPago());
		
		/*
		 * Se calculará el importe de prima correspondiente al periodo j como: 
		 * Si mes(proyUmic(j). varBloque.fecPago) = Mes(varfechaEfecto) --> PRI003(j) = varPNA0 * (1 - (varGE/100)) * (1 + (varPRP/100)) ^ entero(varNumAnualidades)
		 * Si no--> devolver 0
		 */
		Fecha fechaEfecto = UtilFechas.getFecha(varfechaEfecto);
		Fecha fechaDevengo = UtilFechas.getFecha(bloqueCorriente.getFechaDevengo());
		Fecha fechaPago = UtilFechas.getFecha(bloqueCorriente.getFechaPago());
		/**
		 * INCIDENCIA B FASE VII (Incluida en ROSSEAR)
		 */
		if(fechaEfecto.getMes() == 2 && fechaEfecto.getDia() == 29){
			fechaEfecto.setDia(28);
		}
		if (fechaEfecto.getDia() == fechaPago.getDia() && fechaEfecto.getMes() == fechaPago.getMes()) {
			prim003 = calcularPrim003SiFechaPagoNoNulaYvarPPEs0(varPNA0, varPRP, varNumAnualidades, varGE);
		}
		
		if (ModuloPRI003.LOG.isTraceEnabled()) {
			ModuloPRI003.LOG.trace("Fin función << moduloPRI003 >> de la clase ModuloPRI003, para la iteracion = {}, con resultado prim003 = {}", iteracion, prim003);
		}
		
		return prim003;
	}
	
	/**
	 * Función encargada de realizar el siguiente calculo si la fecha de pago del periodo actual es distinto de nulo:
	 * 			PRI003(j) = varPNA0 * (1 - (varGE/100)) * (1 + (varPRP/100)) ^ entero(varNumAnualidades)
	 * 
	 * @param varPNA0
	 * @param varPRP
	 * @param varNumAnualidades
	 * @param varGE
	 * @return prim003
	 */
	private BigDecimal calcularPrim003SiFechaPagoNoNulaYvarPPEs0(final BigDecimal varPNA0, final BigDecimal varPRP, final Integer varNumAnualidades, final BigDecimal varGE) {
		//Variables locales
		BigDecimal prim003 = BigDecimal.ZERO;
		BigDecimal operando1 = BigDecimal.ZERO;
		BigDecimal operando2 = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloPRI003.LOG.isTraceEnabled()) {
			ModuloPRI003.LOG.trace("Inicio de la función << calcularPrim003SiFechaPagoNoNula >> de la clase ModuloPRI003");
		}
		
		//operando1 <-- varPNA0 * (1 - (varGE/100))
		operando1 = varPNA0.multiply(BigDecimal.ONE.subtract(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)));
		
		//operando2 <-- (1+varGE/100)
		operando2 = Util.pow(BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01, ConstantsFunciones.MATH_CONTEXT)), varNumAnualidades);
		
		//prim003 <-- operando1 * operando2
		prim003 = operando1.multiply(operando2);
		
		if (ModuloPRI003.LOG.isTraceEnabled()) {
			ModuloPRI003.LOG.trace("Fin de la función << calcularPrim003SiFechaPagoNoNula >> de la clase ModuloPRI003");
		}
			
		return prim003;
	}
}
