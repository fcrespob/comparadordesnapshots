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
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Szilard Toth
 *
 */

public class ModuloGZCTAR implements Modulo{
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCTAR.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_GZCTAR;
	private static final String CLAVE_NPP = ConstantsModulos.CTE_VA_NPP;
	private static final String CLAVE_VAR_NPP = CLAVE_NPP.concat(CLAVE_MODULO);
	private static final String CLAVE_ANT_RENOVA = ConstantsModulos.CTE_VAR_ANT_RENOVA;
	private static final String CLAVE_VAR_ANT_RENOVA = CLAVE_ANT_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_PROX_RENOVA = ConstantsModulos.CTE_VAR_PROX_RENOVA;
	private static final String CLAVE_VAR_PROX_RENOVA = CLAVE_PROX_RENOVA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_Z = ConstantsModulos.CTE_VAR_Z.concat(CLAVE_MODULO);
	private static final String CLAVE_GIPC = ConstantsModulos.CTE_VAR_GIPC_PRIMA.concat(CLAVE_MODULO);
	private static final String CLAVE_GIC = ConstantsModulos.CTE_VAR_GIC.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GE = ConstantsModulos.CTE_VAR_GE.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_GF = ConstantsModulos.CTE_VAR_GF.concat(CLAVE_MODULO); 
	private static final String CLAVE_VAR_REVALG = ConstantsModulos.CTE_VAR_REVALG.concat(CLAVE_MODULO); 
	private static final String CLAVE_VAR_PNEA = ConstantsModulos.CTE_VAR_PNEA.concat(CLAVE_MODULO);
	private static final String CLAVE_VAR_CSP = ConstantsModulos.CTE_VAR_CSP.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		try {
			if (ModuloGZCTAR.LOG.isTraceEnabled()) {
				ModuloGZCTAR.LOG.trace("Inicio de execute en clase ModuloGZCTAR");
			}
			//Recuperamos los datos que le pasaremos a la función ModuloGZCTAR
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			//Invocamos a la función moduloGZCTAR
			resultado = moduloGZCTAR(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
		
		} catch (Solvencia2Excepcion e) {
			ModuloGZCTAR.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloGZCTAR.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		if (ModuloGZCTAR.LOG.isTraceEnabled()) {
			ModuloGZCTAR.LOG.trace("Fin de execute en clase ModuloGZCTAR");
		}
		return resultado;
	}
	
	/**
	 * Usaremos este módulo cómo módulo general de gastos para Tares.
	 * Es un gasto general que agrupa toda tipología de gasto: sobre capital, sobre prima, y gasto fijo.
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
	 * @param codSubproceso
	 * 			Código del subproceso que se está ejecutando.
	 * @param mapVariables 
	 * 			mapa con las variables de memoria necesarias
	 */
	
	private BigDecimal moduloGZCTAR(final List<DetalleCorriente> proyUmic, final BloqueCorriente bloqueCorriente, final Integer iteracion,
			final Timestamp fcalc, final Umic umic, final DetalleBaseTecnica btcUmic, final String codSubproceso, final Map<String, Object> mapVariables) {
		//variables locales
		BigDecimal gzctar = BigDecimal.ZERO;
		BigDecimal varNpp = BigDecimal.ZERO;
		BigDecimal varGipc = BigDecimal.ZERO;
		BigDecimal varGic = BigDecimal.ZERO;
		BigDecimal varGE = BigDecimal.ZERO;
		BigDecimal varGF = BigDecimal.ZERO;
		BigDecimal varRevalg = BigDecimal.ZERO;
		BigDecimal varPNEA = BigDecimal.ZERO;
		BigDecimal varCSP = BigDecimal.ZERO;
		Timestamp varAntRenova = null;
		Timestamp varProxRenova = null;
		Integer varZ = 0;
		//Fin variables locales
		if (ModuloGZCTAR.LOG.isTraceEnabled()) {
			ModuloGZCTAR.LOG.trace("Inicio de la función << moduloGZCTAR >> de la clase ModuloGZCTAR, para la iteración = {}", iteracion);
		}
		
		//Validamos los parametros de entrada
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		
			//Calculo de la variable de apoyo varNpp en la primera iteración
			varNpp = UtilModulos.getVarNpp(mapVariables, CLAVE_VAR_NPP, umic.getDatosGenerales().getCcartera(), umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(), btcUmic.getBaseTec(), CLAVE_NPP);
			varGipc = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_GIPC, btcUmic.getGtorosspPrima());
			varGic = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_GIC, btcUmic.getGtorosspCap());
			varGE = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_GE,umic.getBti().getPgastgesex1I());
			varAntRenova = UtilModulos.getVarAntRenova(mapVariables, CLAVE_VAR_ANT_RENOVA, umic.getFechas().getFecdesderenova());
			varProxRenova = UtilModulos.getVarProxRenova(mapVariables, CLAVE_VAR_PROX_RENOVA, umic.getFechas().getFechastarenova());
			varZ = UtilModulos.getVarNR(mapVariables, CLAVE_VAR_Z, ConstantsFunciones.CTE_0);
			varPNEA = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_PNEA, umic.getPrimas().getIprimanetaact()); 
			varCSP = UtilModulos.getVarPN0(mapVariables, CLAVE_VAR_CSP, umic.getCapitales().getIcapact()); 
			varGF = UtilModulos.getVarGF(mapVariables, CLAVE_VAR_GF, umic.getBti().getPgastgesin3I());		
			varRevalg = UtilModulos.getVarGicDiv100(mapVariables, CLAVE_VAR_REVALG, umic.getBti().getPgastgesin4I());
			
			if(bloqueCorriente.getFechaDevengo() == null){
				gzctar = BigDecimal.ZERO;
			} else {
				if((bloqueCorriente.getFechaDevengo().after(varAntRenova)) && (bloqueCorriente.getFechaDevengo().after(varProxRenova))){
					
					varAntRenova = varProxRenova;
					mapVariables.put(CLAVE_VAR_ANT_RENOVA, varAntRenova);
					
					varProxRenova = UtilFechas.incrAnyo(varProxRenova, 1);
					mapVariables.put(CLAVE_VAR_PROX_RENOVA, varProxRenova);
					
					varPNEA = varPNEA.multiply(BigDecimal.ONE.add(varRevalg.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
					mapVariables.put(CLAVE_VAR_PNEA, varPNEA);
					
					varCSP = varCSP.multiply(BigDecimal.ONE.add(varRevalg.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01)));
					mapVariables.put(CLAVE_VAR_CSP, varCSP);
					
					varZ = varZ+1;
					mapVariables.put(CLAVE_VAR_Z, varZ);
				} 
				
				gzctar = varGic.multiply(varCSP).add(varGipc.multiply(varPNEA)).add(varGF.multiply(BigDecimal.ONE.add(varRevalg).pow(varZ))).add(varGE.multiply(varPNEA)).divide(varNpp, ConstantsFunciones.MATH_CONTEXT);
				
				
				if(ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) && (UtilFechas.getDia(proyUmic.get(iteracion-1).getFechaHasta()) !=  UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(proyUmic.get(iteracion-1).getFechaHasta())))){
					
					int vardiasmes = UtilFechas.getDia(UtilFechas.getUltimoDiaDelMes(proyUmic.get(iteracion-1).getFechaHasta()));
					int vardias = UtilFechas.getDia(proyUmic.get(iteracion-1).getFechaHasta()) - ConstantsFunciones.CTE_1;
					BigDecimal vardiasmes2 = new BigDecimal(vardiasmes);
					BigDecimal vardias2 = new BigDecimal(vardias);
					BigDecimal varFactor = vardias2.divide(vardiasmes2,ConstantsFunciones.MATH_CONTEXT);
					
					gzctar = gzctar.multiply(varFactor);
				}
				
				
			}
			
		
		if (ModuloGZCTAR.LOG.isTraceEnabled()) {
			ModuloGZCTAR.LOG.trace("Fin de la función << moduloGZCTAR >> de la clase ModuloGZCTAR, para la iteración = {}", iteracion);
		}
		
		return gzctar;
	}
}
