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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase encargada del cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
 * Sólo debería haber prima en los meses de proyección que coincidan con los aniversarios de la póliza.
 * La expresión matemática para su determinación es la siguiente:
 * 				PRI(004,tc)= Pna(0)* (1-Ge/100) * P * (1+tc*prp/100)
 * 
 * @author apedro
 *
 */
public class ModuloPRI004 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI004.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_PRI004;
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
			if (ModuloPRI004.LOG.isTraceEnabled()) {
				ModuloPRI004.LOG.trace("Inicio de execute en clase ModuloPRI004");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloPRI004
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloPRI004
			resultado = moduloPRI004(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloPRI004.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloPRI004.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloPRI004.LOG.isTraceEnabled()) {
			ModuloPRI004.LOG.trace("Fin de execute en clase ModuloPRI004");
		}
		
		return resultado;
	}
	
	/**
	 * Modulo de cálculo que devuelve la prima en el periodo que le corresponda pagar al asegurado.
	 * El importe nominal solo será calculado en aquellos periodos en los que le corresponda a asegurado realizar un pago de primas.
	 * Sólo debería haber prima en los meses de proyección que coincidan con los aniversarios de la póliza.
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
	private BigDecimal moduloPRI004(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal prim004 = BigDecimal.ZERO;
		BigDecimal varPNA0;
		BigDecimal varPRP;
		BigDecimal varGE;
		Integer varTC;
		Timestamp varFechaEfecto = null;
		//Fin variables locales
		
		if (ModuloPRI004.LOG.isTraceEnabled()) {
			ModuloPRI004.LOG.trace("Inicio función << moduloPRI004 >> de la clase ModuloPRI004, para la iteracion = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		// Si no tiene fecha de pago, o la forma de pago es 9 (unica), no se calcula
		if(bloqueCorriente.getFechaPago() == null || umic.getPrimas().getCformpago().equals(ConstantsModulos.CTE_CFORMPAG_UNICA) 
				|| !bloqueCorriente.getFechaPago().before(umic.getFechas().getFecefecfin()) || 
				!proyUmic.get(iteracion-1).getFechaDesde().before(umic.getFechas().getFecefecfin())) {
			return prim004;
		}
		
		//Si la poliza es reducida y la fecha de reduccion es anterior a la fecha desde, no se calcula
		if (!UtilModulos.getCalcularSiReducidaPRI(umic.getDatosGenerales().getCsitupol(), umic.getDatosGenerales().getCnegocio(), umic.getFechas(), proyUmic.get(iteracion-1).getFechaDesde())){
			return prim004;
		}		
		
		//Variables Módulo
		varPNA0 = umic.getPrimas().getIprimatarada();
		varPRP = umic.getPrimas().getPrevprima();
		varGE = umic.getBti().getPgastgesex1I();
		
		varFechaEfecto = UtilModulos.getVarFecEfecto(mapVariables, CLAVE_FEC_EFEC, umic.getDatosGenerales(), umic.getFechas(), umic.getCapitales().getIsaldo());
		
		//Si mes(proyUmic(j). varBloque.fecDevengo) = Mes(varfechaEfecto): varP = 1 --> Se calcula PRI004
		if (UtilFechas.getMes(varFechaEfecto) == UtilFechas.getMes(bloqueCorriente.getFechaPago())){

			varTC = FuncionesAuxiliares.tc(varFechaEfecto, bloqueCorriente.getFechaPago());
	
			/**
			 * Se calcula PRI004 como:
			 * PRI004 = varPNA0*(varP )* (1-varGE/100) * (1+varTC*  varPRP/100)
			 */
			BigDecimal opVarGE = BigDecimal.ONE.subtract(varGE.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)); // ( 1 - varGE/100)
			BigDecimal opVarPRP = BigDecimal.ONE.add(BigDecimal.valueOf(varTC).multiply(varPRP.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01))); //(1 + varTC * (varPRP/100))
			
			prim004 = varPNA0.multiply(opVarGE).multiply(opVarPRP);
			
		} //Si no, varP = 0 --> PRI004 = 0
		
		if (ModuloPRI004.LOG.isTraceEnabled()) {
			ModuloPRI004.LOG.trace("Fin función << moduloPRI004 >> de la clase ModuloPRI004, para la iteracion = {}, con resultado prim004 = {}", iteracion, prim004);
		}
			
		return prim004;
	}

}
