package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
 * 				PRI(007,tc)= Pna(0)*(1+PRP/100)^tc*(1+RPF)/FP
 * 
 * @author agonzalezgar
 *
 */
public class ModuloPRI007 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI007.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI007;
	
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	
	private static final String CLAVE_FEC_EFEC = ConstantsModulos.CTE_FEC_EFEC; 
	private static final String CLAVE_CALCULO_PRI007 = "pri007";
	private static final String CLAVE_ITERACION_PREVIA = "iteracionPrevia";
	private static final String CLAVE_FACTOR_ANUAL = "factorAnual";
	
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
			if (ModuloPRI007.LOG.isTraceEnabled()) {
				ModuloPRI007.LOG.trace("Inicio de execute en clase ModuloPRI007");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI007
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI007
			resultado = moduloPRI007(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI007.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI007.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI007.LOG.isTraceEnabled()) {
			ModuloPRI007.LOG.trace("Fin de execute en clase ModuloPRI007");
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
	private BigDecimal moduloPRI007(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim007 = BigDecimal.ZERO;
		String varCriFec;
		int varNumAnualidades;
		BigDecimal varPNA0;
		BigDecimal varPRP;
		BigDecimal varRPF;
		BigDecimal varNP = BigDecimal.ZERO;
		//Fin variables locales
		
		if (ModuloPRI007.LOG.isTraceEnabled()) {
			ModuloPRI007.LOG.trace("Inicio función << moduloPRI007 >> de la clase ModuloPRI007, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| bloqueCorriente.getFechaPago().after(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return prim007;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim007;
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
		 * 			- PRI007 = 0
		 * 		Si proyUmic(j).varBloque.fecPago no es nulo:
		 * 			-	Si la póliza está reducida, es decir  umic.datosGenerales.csitupol= ‘RE’
		 * 				-	Varfecefec = umic.fechas.fecefecred
		 * 			-	Si la póliza debe estar en vigor, es decir  umic.datosGenerales.csitupol= ‘VI’
		 * 				-	Varfecefec = umic.fechas fecinisus
		 * 		
		 * 		Se calulará para j:
		 * 			-	varNumAnualidades = TC(Varfecefec, ProyUmic(j).varBloque.fecPago)
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
		
		varNumAnualidades = FuncionesAuxiliares.tc(varfecefec, bloqueCorriente.getFechaPago());
				
		/**
		 * Se calculará el importe de prima correspondiente al periodo j como:
		 * 		PRI007(j) = varPNA0*(1+varPRP/100)^varNumAnualidades*[((1+varRPF/100  ))/varNP]
		 */
		
		final String clave = CLAVE_CALCULO_PRI007.concat("_").concat(String.valueOf(varNumAnualidades));
		if(!mapVariables.containsKey(CLAVE_ITERACION_PREVIA)) {
			// Recuperación valores auxiliares.
			varPNA0 = umic.getPrimas().getIprimanetaini();
			varPRP = umic.getPrimas().getPrevprima();
			varRPF = umic.getPrimas().getPrecargfrac();
			// Fin de la recuperación de los valores auxiliares.
			mapVariables.put(CLAVE_ITERACION_PREVIA, clave);
			final BigDecimal factorAnual = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
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
			
			prim007 = varPNA0.multiply(Util.pow(factorAnual, varNumAnualidades)).multiply(BigDecimal.ONE.add(varRPF.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)).divide(varNP, ConstantsFunciones.MATH_CONTEXT));
			mapVariables.put(clave, prim007);
		} else {
			if(mapVariables.containsKey(clave)) {
				prim007 = (BigDecimal)mapVariables.get(clave);
			} else {
				final String clavePrevia = (String)mapVariables.put(CLAVE_ITERACION_PREVIA, clave);
				prim007 = (BigDecimal)mapVariables.get(clavePrevia);
				mapVariables.remove(clavePrevia);
				int varNumAnualidadesAnt = Integer.parseInt(StringUtils.substringAfter(clavePrevia, "_"));
				final BigDecimal factorAnual = (BigDecimal)mapVariables.get(CLAVE_FACTOR_ANUAL);
				for(int i = varNumAnualidadesAnt; i < varNumAnualidades; i++) {
					prim007 = prim007.multiply(factorAnual, ConstantsFunciones.MATH_CONTEXT);
				}
				mapVariables.put(clave, prim007);
			}
		}
		
		if (ModuloPRI007.LOG.isTraceEnabled()) {
			ModuloPRI007.LOG.trace("Fin función << moduloPRI007 >> de la clase ModuloPRI007, para la iteracion = {}, con resultado prim007 = {}", iteracion, prim007);
		}
			
		return prim007;
	}

}
