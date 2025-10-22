package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calculará el fallecimiento de ambos progenitores, con independencia del orden, 
 * condicionado a la supervivencia del hijo
 * La expresión matemática para su determinación es la siguiente:
 * 			ORFABSzc (j) = varVzc3(j) * varFzc1(j) * varFzc2(j)
 * @author eugenio.torres
 *
 */
public class ModuloORFABSZC implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloORFABSZC.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_ORFABSZC;
	private static final String CLAVE_UMIC_3 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC_3 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC_2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO + "2");
	private static final String CLAVE_BTC_UMIC_2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO + "2");
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
			if (ModuloORFABSZC.LOG.isTraceEnabled()) {
				ModuloORFABSZC.LOG.trace("Inicio de execute en clase ORFABSZC");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloORFABSZC
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloORFABSZC
			resultado = moduloORFABSZC(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloORFABSZC.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloORFABSZC.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloORFABSZC.LOG.isTraceEnabled()) {
			ModuloORFABSZC.LOG.trace("Fin de execute en clase ORFABSZC");
		}

		return resultado;
	}
	/**
	 * Módulo que calculará el fallecimiento de ambos progenitores, con independencia del orden, 
	 * condicionado a la supervivencia del hijo
	 * La expresión matemática para su determinación es la siguiente:
	 * 			ORFABSzc (j) = varVzc3(j) * varFzc1(j) * varFzc2(j)
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
	private BigDecimal moduloORFABSZC(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal ORFABSZC = BigDecimal.ZERO;
		Modulo moduloVZC, moduloFZC;
		List<DetalleCorriente> varProyVzc3;
		List<DetalleCorriente> varProyFzc1;
		List<DetalleCorriente> varProyFzc2;
		BigDecimal varVzc3j;
		Umic varUmic3, varUmic2, varUmic1;
		DetalleBaseTecnica varBtcUmic3, varBtcUmic2;
		BigDecimal varProyFzc1j, varProyFzc2j;
		//Fin variables locales
		
		if (ModuloORFABSZC.LOG.isTraceEnabled()) {
			ModuloORFABSZC.LOG.trace("Inicio función << moduloORFABSZC >> de la clase moduloORFABSZC, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		if(!umic.getOtrosDatos().getCestadoAseg3().equals("M")){	
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G7);	
		}
		
		if(null == umic.getAsegurados().getFnacAseg3()){		
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G2);	
		}
		
		
		if(bloqueCorriente.getFechaDevengo() == null){
			return ORFABSZC;
		}
		
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
		
		varUmic3 = (Umic) mapVariables.get(CLAVE_UMIC_3);
		
		if (varUmic3 == null) {
			//UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC_3, CLAVE_BTC_UMIC_3, umic, btcUmic);
			
			varUmic3 = new Umic();
			try {
				
				PropertyUtils.copyProperties(varUmic3, umic); //Probar con la asginacion sencilla.
				
			} catch (Exception e) {
				ModuloORFABSZC.LOG.error(e.getMessage());
			}
			
			Asegurados aseg = new Asegurados();
			aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
			aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
			aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
			varUmic3.setAsegurados(aseg);
		
			mapVariables.put(CLAVE_UMIC_3, varUmic3);
			
		}
		
		varBtcUmic3 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC_3);
		
		if(null == varBtcUmic3){
			varBtcUmic3 = new DetalleBaseTecnica();
			
			try {
				
				PropertyUtils.copyProperties(varBtcUmic3, btcUmic);
				
			} catch (Exception e) {
				ModuloORFABSZC.LOG.error(e.getMessage());
			}
			
			varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());
			
			if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
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
			
			mapVariables.put(CLAVE_BTC_UMIC_3, varBtcUmic3);
		
			
		}
		
		varProyVzc3 = proyUmic;
		varVzc3j = (BigDecimal) moduloVZC.execute(varProyVzc3, bloqueCorriente, iteracion, fcalc, varUmic3, varBtcUmic3, mapVariables, codSubproceso);			
		
		varUmic1 = umic;
		varProyFzc1 = proyUmic;
		if(null == umic.getAsegurados().getFnacAseg1()){
			varProyFzc1j = BigDecimal.ONE;
		}
		else if(umic.getOtrosDatos().getCestadoAseg1().equals("F")){
			varProyFzc1j = BigDecimal.ONE;
		}else{
			varProyFzc1j = (BigDecimal) moduloFZC.execute(varProyFzc1, bloqueCorriente, iteracion, fcalc, varUmic1, btcUmic,mapVariables, codSubproceso);
			
		}
		//Si existe un segundo asegurado:
		/*if (null != umic.getAsegurados().getFnacAseg2()) {
			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC_2);
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC_2);
			if (varUmic2 == null) {
				//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
				UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC_2, CLAVE_BTC_UMIC_2, umic, btcUmic);
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC_2);
				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC_2);
			}
			varProyFzc2 = proyUmic;
			varProyFzc2j = (BigDecimal) moduloFZC.execute(varProyFzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
			}
		else {
			varProyFzc2j = BigDecimal.ONE;
		}*/
		
		if(null == umic.getAsegurados().getFnacAseg2()){
			
			varProyFzc2j = BigDecimal.ONE;
			
		}
		else if(umic.getOtrosDatos().getCestadoAseg2().equals("F")){
		
			varProyFzc2j = BigDecimal.ONE;
		
		}else{
		
		varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC_2);
		if(null == varUmic2){
			varUmic2 = new Umic();
			try {
				
				PropertyUtils.copyProperties(varUmic2, umic);
				
			} catch (Exception e) {
				ModuloORFABSZC.LOG.error(e.getMessage());
			}
			
			
			Asegurados aseg = new Asegurados();
			
			aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
			aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
			aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
			varUmic2.setAsegurados(aseg);
			
			mapVariables.put(CLAVE_UMIC_2, varUmic2);
			
		}
		
		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC_2);
		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();
			
			try {
				
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
				
			} catch (Exception e) {
				ModuloORFABSZC.LOG.error(e.getMessage());
			}
			
			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());
			
			if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
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
			}
			
			mapVariables.put(CLAVE_BTC_UMIC_2, varBtcUmic2);
		
			
		}
		varProyFzc2 = proyUmic;
		varProyFzc2j = (BigDecimal) moduloFZC.execute(varProyFzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
				
	}
		//Se realiza el cálculo de ORFABSzc (j) = varVzc3(j) * varFzc1(j) * varFzc2(j)
		ORFABSZC = varVzc3j.multiply(varProyFzc1j).multiply(varProyFzc2j);
								
		if (ModuloORFABSZC.LOG.isTraceEnabled()) {
			ModuloORFABSZC.LOG.trace("Fin función << moduloORFABSZC >> de la clase ModuloORFABSZC, para la iteracion = {}, con resultado ORFABSZC = {}", iteracion, ORFABSZC);
		}
		
		return ORFABSZC;
	}
}
