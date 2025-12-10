package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * 
 * @author Aleksandar Plamenov Nedyalkov
 *
 */

public class ModuloFZC1FZC2 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloFZC1FZC2.class);
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_FZC1FZC2;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);


	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_FZC1FZC2;
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
			if (ModuloFZC1FZC2.LOG.isTraceEnabled()) {
				ModuloFZC1FZC2.LOG.trace("Inicio de execute en clase ModuloFZC1FZC2");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloGZCRTA
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de los datos.
			
			//Invocamos a la función de calculo FZC1FZC2
			resultado = moduloFZC1FZC2(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
		} catch (Solvencia2Excepcion e) {
			ModuloFZC1FZC2.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloFZC1FZC2.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloFZC1FZC2.LOG.isTraceEnabled()) {
			ModuloFZC1FZC2.LOG.trace("Fin de execute en clase ModuloFZC1FZC2");
		}
		
		return resultado;
	}

	private BigDecimal moduloFZC1FZC2(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, Map<String, Object> mapVariables,
			String codSubproceso) {
		BigDecimal fzc1fzc2 = BigDecimal.ZERO;
		BigDecimal varFZC1;
		BigDecimal varFZC2;
		Modulo moduloFZC1, moduloVZC2;
		Umic varUmic2;
		DetalleBaseTecnica  varBtcUmic2;
		List<DetalleCorriente> varProyVzc2;
		
		if (ModuloFZC1FZC2.LOG.isTraceEnabled()) {
			ModuloFZC1FZC2.LOG.trace("Inicio función << moduloFZC1FZC2 >> de la clase ModuloFZC1FZC2, para la iteracion = {}", iteracion);
		}
		
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if (bloqueCorriente.getFechaDevengo() != null){
			moduloFZC1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
			//moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC2);
			varFZC1 = (BigDecimal) moduloFZC1.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			//varVZC2 = (BigDecimal) moduloVZC2.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
			
			if (null == umic.getAsegurados().getFnacAseg2() || null == umic.getAsegurados().getCsexAseg2() ||
					null == umic.getAsegurados().getEdadAseg2()) {
					
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);	
					
			}
				else {
					
					varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
					if(null == varUmic2){
						varUmic2 = new Umic();
						try {
							
							PropertyUtils.copyProperties(varUmic2, umic);
							
						} catch (Exception e) {
							ModuloFZC1FZC2.LOG.error(e.getMessage());
						}
						
						
						Asegurados aseg = new Asegurados();
						
						aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
						aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
						aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
						varUmic2.setAsegurados(aseg);
						
						mapVariables.put(CLAVE_UMIC2, varUmic2);
						
					}
					
					varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
					if(null == varBtcUmic2){
						varBtcUmic2 = new DetalleBaseTecnica();
						
						try {
							
							PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
							
						} catch (Exception e) {
							ModuloFZC1FZC2.LOG.error(e.getMessage());
						}
						
						varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());
						
						if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))           && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))   && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))   &&  
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NIIF17IF)) && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17))    && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR))  && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR))   && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN))  && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI)) &&
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))  && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))  && 
								(!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))){
							
							if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
							}
							
							List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
							tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

							varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
						} else {
							List<Integer> tablaBaseExp = new ArrayList<Integer>();
							tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
							varBtcUmic2.setTablaBaseExp(tablaBaseExp);
							mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
						}
						
						mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
					
						
					}
					varProyVzc2 = proyUmic;
					varFZC2 = (BigDecimal) moduloFZC1.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
							
				}
			
			
			fzc1fzc2 = varFZC1.multiply(varFZC2);
		}
		
		if (ModuloFZC1FZC2.LOG.isTraceEnabled()) {
			ModuloFZC1FZC2.LOG.trace("Fin función << moduloFZC1FZC2 >> de la clase ModuloFZC1FZC2, para la iteracion = {} con resultado fzc1fzc2 = {}", iteracion, fzc1fzc2);
		}
		return fzc1fzc2;
	}
	
}