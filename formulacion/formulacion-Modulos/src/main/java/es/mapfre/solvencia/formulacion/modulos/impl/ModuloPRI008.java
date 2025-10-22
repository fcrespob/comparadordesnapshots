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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.  
 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente en 1 periodo del año que coincide con los aniversarios.
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(008,tc)= Pna(0)*(1+TC*PRP/100) * (1+RPF)/FP
 * 
 * @author apedro
 *
 */
public class ModuloPRI008 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI008.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI008;
	private static final String CLAVE_VAR_FP = ConstantsModulos.CTE_VAR_FP.concat(CLAVE_MODULO);
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
			if (ModuloPRI008.LOG.isTraceEnabled()) {
				ModuloPRI008.LOG.trace("Inicio de execute en clase ModuloPRI008");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI008
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI008
			resultado = moduloPRI008(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI008.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI008.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI008.LOG.isTraceEnabled()) {
			ModuloPRI008.LOG.trace("Fin de execute en clase ModuloPRI008");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
	 * En base a la fecha de efecto de la garantía y la forma de pago establecida, nos dará los puntos en los que corresponde calcular prima.  
	 * Podremos encontrar desde pagos mensuales, con cálculo en cada periodo, hasta periodos anuales con cálculo exclusivamente en 1 periodo del año que coincide con los aniversarios.
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
	private BigDecimal moduloPRI008(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim008 = BigDecimal.ZERO;
		BigDecimal varPNA0;
		BigDecimal varPRP;
		BigDecimal varRPF;
		Timestamp varfecefec=null;
		BigDecimal varFP;
		Integer varTC;
		
		//Fin variables locales
		
		if (ModuloPRI008.LOG.isTraceEnabled()) {
			ModuloPRI008.LOG.trace("Inicio función << moduloPRI008 >> de la clase ModuloPRI008, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| !bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return prim008;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim008;
		}		
		
	
		//Variables Módulo
		varPNA0 = umic.getPrimas().getIprimanetaini();
		varPRP = umic.getPrimas().getPrevprima();
		varRPF = umic.getPrimas().getPrecargfrac();
		
		varfecefec = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		varFP = (BigDecimal) mapVariables.get(CLAVE_VAR_FP);
		if (varFP == null){
			if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_4)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_12);
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_3)){
				varFP = BigDecimal.valueOf(ConstantsFunciones.CTE_4);;
			} else if (umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_1) || umic.getPrimas().getCformpago().equals(ConstantsFunciones.CTE_FORMPAGO_2)){
				varFP = new BigDecimal(umic.getPrimas().getCformpago());
			}
			
			mapVariables.put(CLAVE_VAR_FP, varFP);
		}
		
		varTC = FuncionesAuxiliares.tc(varfecefec, bloqueCorriente.getFechaPago());
		

		// Se calcula PRI008 = varPNA0 * (1 + varTc * (varPRP/100)) * ((1+ varRPF)/varFP)
		BigDecimal opVarPRP = BigDecimal.ONE.add(BigDecimal.valueOf(varTC).multiply(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))); //(1 + varTC * (varPRP/100))
		BigDecimal opVarFP = (BigDecimal.ONE.add(varRPF.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))).divide(varFP, ConstantsFunciones.MATH_CONTEXT); //((1+ varRPF/100)/varFP)
		
		prim008 = varPNA0.multiply(opVarPRP).multiply(opVarFP);
			

		if (ModuloPRI008.LOG.isTraceEnabled()) {
			ModuloPRI008.LOG.trace("Fin función << moduloPRI008 >> de la clase ModuloPRI008, para la iteracion = {}, con resultado prim008 = {}", iteracion, prim008);
		}
			
		return prim008;
	}

}
