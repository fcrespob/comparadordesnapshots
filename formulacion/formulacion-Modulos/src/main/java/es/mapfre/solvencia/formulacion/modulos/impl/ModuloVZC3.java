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
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calculará VZC de viudedad tomando el hijo
 * @author 
 *
 */
public class ModuloVZC3 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC3.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC3;
	private static final String CLAVE_UMIC3 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC3 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);

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
			if (ModuloVZC3.LOG.isTraceEnabled()) {
				ModuloVZC3.LOG.trace("Inicio de execute en clase VZC3");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloVZC3
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloVZC3
			resultado = moduloVZC3(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZC3.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC3.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloVZC3.LOG.isTraceEnabled()) {
			ModuloVZC3.LOG.trace("Fin de execute en clase VZC3");
		}

		return resultado;
	}
	/**
	 * Módulo que calculara VZC tomando el hijo.
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
	private BigDecimal moduloVZC3(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		//Variables locales
		BigDecimal VZC3 = BigDecimal.ZERO;
		Modulo moduloVZC;
		BigDecimal varVzc3j;
		Umic varUmic3;
		DetalleBaseTecnica  varBtcUmic3;
		List<DetalleCorriente> varProyVzc3;
		//Fin variables locales
		
		if (ModuloVZC3.LOG.isTraceEnabled()) {
			ModuloVZC3.LOG.trace("Inicio función << moduloVZC3 >> de la clase moduloVZC3, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		if (null == umic.getAsegurados().getFnacAseg3() || null == umic.getAsegurados().getCsexAseg3() ||
			null == umic.getAsegurados().getEdadAseg3()) {
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);	
			
		}
		else {
			
			varUmic3 = (Umic) mapVariables.get(CLAVE_UMIC3);
			if(null == varUmic3){
				varUmic3 = new Umic();
				try {
					
					PropertyUtils.copyProperties(varUmic3, umic);
					
				} catch (Exception e) {
					ModuloVZC3.LOG.error(e.getMessage());
				}
				
				
				Asegurados aseg = new Asegurados();
				
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
				varUmic3.setAsegurados(aseg);
				
				mapVariables.put(CLAVE_UMIC3, varUmic3);
				
			}
			
			varBtcUmic3 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC3);
			if(null == varBtcUmic3){
				varBtcUmic3 = new DetalleBaseTecnica();
				
				try {
					
					PropertyUtils.copyProperties(varBtcUmic3, btcUmic);
					
				} catch (Exception e) {
					ModuloVZC3.LOG.error(e.getMessage());
				}
				
				varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());
				
				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) 
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)) 
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)) 
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM))   
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN))  
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM))){
					if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
					}
					
					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(2));

					varBtcUmic3.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(2));
					varBtcUmic3.setTablaBaseExp(tablaBaseExp);
				}
				
				mapVariables.put(CLAVE_BTC_UMIC3, varBtcUmic3);
			
				
			}
			varProyVzc3 = proyUmic;
			varVzc3j = (BigDecimal) moduloVZC.execute(varProyVzc3, bloqueCorriente, iteracion, fcalc, varUmic3, varBtcUmic3, mapVariables, codSubproceso);
					
		}	

		VZC3 = varVzc3j;
				
		if (ModuloVZC3.LOG.isTraceEnabled()) {
			ModuloVZC3.LOG.trace("Fin función << moduloVZC3 >> de la clase ModuloVZC3, para la iteracion = {}, con resultado VZC3 = {}", iteracion, VZC3);
		}
		
		return VZC3;
	}
}
