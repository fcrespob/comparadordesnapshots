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

public class ModuloFPTOZCPRES implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFPTOZCPRES.class);
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FPTOZCPRES;
	private static final String CLAVE_VAR_PROB_VIDA = ConstantsModulos.CTE_VAR_PROB_VIDA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_PROB_FALL = ConstantsModulos.CTE_VAR_PROB_FALL.concat(CLAVE_MODULO);

	// Fin de las variables estáticas usadas para agilizar operaciones.
	
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloFPTOZCPRES.LOG.isTraceEnabled()) {
				ModuloFPTOZCPRES.LOG.trace("Inicio de execute en clase ModuloFPTOZCPRES");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloFPTOZCPRES
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función ModuloFPTOZCPRES
			resultado = moduloFPTOZCPRES(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		} catch (Solvencia2Excepcion e) {
			ModuloFPTOZCPRES.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFPTOZCPRES.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloFPTOZCPRES.LOG.isTraceEnabled()) {
			ModuloFPTOZCPRES.LOG.trace("Fin de execute en clase ModuloFPTOZCPRES");
		}
		return resultado;
}
	/**
	 * Modulo de cálculo que devuelve los factores de probabilización a aplicar en  
	 * el cálculo de la cuantía probable de una garantía para la corriente de gastos. 
	 * El módulo FPTOZCPRES para la corriente de gastos no se calculará y 
	 * leaerá del probable de la corriente de fallecimiento.
	 * @param proyUmic
	 * @param bloqueCorriente
	 * @param iteracion
	 * @param fcalc
	 * @param umic
	 * @param btcUmic
	 * @param codSubproceso
	 * @param mapVariables
	 * @return
	 */
	private BigDecimal moduloFPTOZCPRES(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
			
			//Variables locales
			BigDecimal FPTOZCPRES = BigDecimal.ZERO;
			String varModProbVida, varModProbFall; 
			//Fin variables locales
		
		if (ModuloFPTOZCPRES.LOG.isTraceEnabled()) {
			ModuloFPTOZCPRES.LOG.trace("Inicio de la función << ModuloFPTOZCPRES >> de la clase ModuloFPTOZCPRES, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
				
		//varModProbVida
		varModProbVida = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_VIDA,umic, btcUmic, ConstantsModulos.CTE_PROY_VIDA, "02");

		//varModProbFall
		varModProbFall = UtilModulos.getVarModulo(mapVariables,CLAVE_VAR_PROB_FALL,umic, btcUmic, ConstantsModulos.CTE_PROY_FALL, "02");

		
		//Calculo modulo
		if(!(null == bloqueCorriente.getFechaDevengo())){
			
			if(!(null == varModProbVida) && !(null == varModProbVida)){
				if(null != proyUmic.get(iteracion-1) && null != proyUmic.get(iteracion-1).getBloqueVida() && null != proyUmic.get(iteracion-1).getBloqueVida().getFpbProbable() && !proyUmic.get(iteracion-1).getBloqueVida().getFpbProbable().equals(BigDecimal.ZERO)){
					FPTOZCPRES = proyUmic.get(iteracion-1).getBloqueVida().getFpbProbable();
				}else if(null != proyUmic.get(iteracion-1) && null != proyUmic.get(iteracion-1).getBloqueFall() && null != proyUmic.get(iteracion-1).getBloqueFall().getFpbProbable() && !proyUmic.get(iteracion-1).getBloqueFall().getFpbProbable().equals(BigDecimal.ZERO)){
					FPTOZCPRES = proyUmic.get(iteracion-1).getBloqueFall().getFpbProbable();
				}
			}
			
			if(!(null == varModProbVida) && null == varModProbFall){
				if(null != proyUmic.get(iteracion-1) && null != proyUmic.get(iteracion-1).getBloqueVida() && null != proyUmic.get(iteracion-1).getBloqueVida().getFpbProbable()){
					FPTOZCPRES = proyUmic.get(iteracion-1).getBloqueVida().getFpbProbable();
				}else{
					FPTOZCPRES = BigDecimal.ZERO;
				}
			}else if(!(null == varModProbFall) && (null == varModProbVida)){	
				if(null != proyUmic.get(iteracion-1) && null != proyUmic.get(iteracion-1).getBloqueFall() && null != proyUmic.get(iteracion-1).getBloqueFall().getFpbProbable()){
					FPTOZCPRES = proyUmic.get(iteracion-1).getBloqueFall().getFpbProbable();
				}else{
					FPTOZCPRES = BigDecimal.ZERO;
				}
			}
		}
		
		// Fin variables módulo
		if (ModuloFPTOZCPRES.LOG.isTraceEnabled()) {
			ModuloFPTOZCPRES.LOG.trace("Fin de la función << ModuloFPTOZCPRES >> de la clase ModuloFPTOZCPRES, para la iteración = {}", iteracion);
		}
		
		return FPTOZCPRES;
	}
	


}