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
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.
 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente en 1 periodo 
 * del año que coincide con los aniversarios.
 * 
 * Se calculará prima únicamente hasta la FECHA FIN PAGO DE PRIMAS (excluida).
 * 
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(009,tc)= Pna0 * (1 - GE/100) * (1+ PRP/100)^NA
 * 
 * @author ogperez
 *
 */
public class ModuloPRI009 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI009.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI009;
	private static final String CLAVE_CRIFEC = ConstantsModulos.CTE_VA_CRIT_FEC;
	private static final String CLAVE_VAR_CRIFEC = CLAVE_CRIFEC.concat(CLAVE_MODULO);
	private static final String CLAVE_OP_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_OP_VAR_PRP = ConstantsModulos.CTE_VAR_OP_PRP.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_TCM = ConstantsModulos.CTE_VAR_TCM.concat(CLAVE_MODULO);
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
			if (ModuloPRI009.LOG.isTraceEnabled()) {
				ModuloPRI009.LOG.trace("Inicio de execute en clase ModuloPRI009");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI009
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI009
			resultado = moduloPRI009(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI009.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI009.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI009.LOG.isTraceEnabled()) {
			ModuloPRI009.LOG.trace("Fin de execute en clase ModuloPRI009");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
	 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.  
	 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente en 1 periodo del año que coincide con los aniversarios.
	 * Se calculará prima únicamente hasta la FECHA FIN PAGO DE PRIMAS (excluida).
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
	private BigDecimal moduloPRI009(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim009 = BigDecimal.ZERO;
		String varCriterioFecha; 	
		BigDecimal varPNA0;
		BigDecimal varPRP;
		BigDecimal varGE;
		Timestamp varfecefec=null;
		Integer varTCm = ConstantsFunciones.CTE_0;
		Integer varTC = ConstantsFunciones.CTE_0;
		Integer varNumAnualidades;
		BigDecimal opVarGE;
		BigDecimal opVarPRP;
		//Fin variables locales
		
		if (ModuloPRI009.LOG.isTraceEnabled()) {
			ModuloPRI009.LOG.trace("Inicio función << moduloPRI009 >> de la clase ModuloPRI009, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| !bloqueCorriente.getFechaPago().before(umic.getFechas().getFecfinpagprim()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return prim009;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim009;
		}		
		
		// Obtención y validación del criterio de fecha.
		varCriterioFecha = UtilModulos.getVarCriFec(mapVariables, CLAVE_VAR_CRIFEC, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_CRIFEC);
				
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion)) {
		// Si es válido para la iteración 1 ha de ser válido para las siguientes (por la forma en que se gestiona el paso de umic).
			ValidacionesComunesModulos.validarVariableDeApoyoVarCriterioFecha(varCriterioFecha);
		// Fin de la obtención y validación del criterio de fecha.
		}
		
		//Variables Módulo
		varPNA0 = umic.getPrimas().getIprimanetaini();
		varPRP = umic.getPrimas().getPrevprima();
		varGE = umic.getBti().getPgastgesex1I();
		
		varfecefec = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		// Si mes(proyUmic(j). varBloque.fecPago) = Mes(varfechaEfecto) -> PRI009 = varPNA0*(varP )* (1-varGE/100)*(1+varPRP/100)^(entero(varNumAnualidades))
		// otro caso -> PRI009 = 0
		int fpago = UtilFechas.getMes(bloqueCorriente.getFechaPago());
		int fecefec = UtilFechas.getMes(varfecefec);
		if (fpago == fecefec) {			
			varTCm = UtilModulos.getVarTCm(mapVariables, CLAVE_VAR_TCM, iteracion, varfecefec, proyUmic.get(0).getFcierre());
			varTC = FuncionesAuxiliares.tc(varfecefec, proyUmic.get(0).getFcierre());
			varNumAnualidades = FuncionesAuxiliares.tc(varfecefec, bloqueCorriente.getFechaPago());
			//Fin Variables Modulo
			
			//Se calculan y almacenan los términos que no varían por iteración
			opVarGE = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_GE);
			opVarPRP = (BigDecimal) mapVariables.get(CLAVE_OP_VAR_PRP);
			if (opVarGE == null){
				opVarGE = (BigDecimal.ONE.subtract(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
				opVarPRP = BigDecimal.ONE.add(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
				mapVariables.put(CLAVE_OP_VAR_GE, opVarGE);
				mapVariables.put(CLAVE_OP_VAR_PRP, opVarPRP);
			}
	
			// PRI009 = varPNA0 * ( 1 - varGE/100) * ( 1 + varPRP/100)^(entero(varNumAnualidades))
			opVarPRP = Util.pow(opVarPRP, varNumAnualidades);
			prim009 = varPNA0.multiply(opVarPRP).multiply(opVarGE);
			
		}
			
		if (ModuloPRI009.LOG.isTraceEnabled()) {
			ModuloPRI009.LOG.trace("Fin función << moduloPRI009 >> de la clase ModuloPRI009, para la iteracion = {}, con resultado prim009 = {}", iteracion, prim009);
		}
			
		return prim009;
	}

}
