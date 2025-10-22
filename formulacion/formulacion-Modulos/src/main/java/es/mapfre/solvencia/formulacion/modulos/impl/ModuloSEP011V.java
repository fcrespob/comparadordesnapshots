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
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo de cuantía nominal para la viudedad de la póliza 421011
 * @author eugenio.torres
 *
 */
public class ModuloSEP011V implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloSEP011V.class);
	
	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_SEP011V;
	private static final String CLAVE_UMIC = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	// Fin de las variables estáticas usadas para agilizar operaciones.

	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}
	
	/**
	 * Módulo de cuantía nominal para la viudedad de la póliza 421011.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		//Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		//Fin variables locales
		
		try {
			if (ModuloSEP011V.LOG.isTraceEnabled()) {
				ModuloSEP011V.LOG.trace("Inicio de execute en clase SEP011V");
			}
			
			//Recuperamos los datos que le pasaremos a la función moduloSEP011V
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.
			
			//Invocamos a la función moduloSEP011V
			resultado = moduloSEP011V(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloSEP011V.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloSEP011V.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
		
		if (ModuloSEP011V.LOG.isTraceEnabled()) {
			ModuloSEP011V.LOG.trace("Fin de execute en clase SEP011V");
		}

		return resultado;
	}
	/**
	 * Módulo que calculará la probabilidad de viudedad. 
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
	private BigDecimal moduloSEP011V(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal SEP011V = BigDecimal.ZERO;
 		Modulo moduloFZC;
		BigDecimal varFzc1j, varPctRenta, varRenta, varPctHijo, varRentaRev;
		Umic varUmic2;
		DetalleBaseTecnica  varBtcUmic2;
		List<DetalleCorriente> varProyFzc1;
		Modulo moduloCSP238;
		//Fin variables locales
		
		if (ModuloSEP011V.LOG.isTraceEnabled()) {
			ModuloSEP011V.LOG.trace("Inicio función << moduloSEP011V >> de la clase moduloSEP011V, para la iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		if(null == bloqueCorriente.getFechaDevengo()){
			
			return SEP011V; 
			
		}
		
		varPctRenta = ConstantsModulos.CTE_OPER_80.divide(ConstantsFunciones.CTE_OPER_100);
		/*varRenta = umic.getRentas().getRentact();
		
		if(null == varRenta || varRenta.equals(BigDecimal.ZERO)){
			
			varRenta = umic.getRentas().getRentini();
			
		}*/
		
		
		if(umic.getDatosGenerales().getKbencon().equals(ConstantsModulos.CTE_KBENCON_301)){
			moduloCSP238 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP238);
			SEP011V = (BigDecimal) moduloCSP238.execute(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);	
		}else{
		
		varRenta = proyUmic.get(iteracion - 1).getImpPago();
		//Revisar tratamiento en el caso de no venir informado
		if (null == varRenta ) {
			varRenta = BigDecimal.ZERO;
		}
		
		varPctHijo = new BigDecimal(ConstantsModulos.CTE_300_PUNTO_51);
		
		varRentaRev = varPctRenta.multiply(varRenta);
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
		/*if (umic.getAsegurados().getFnacAseg3() == null || umic.getAsegurados().getCsexAseg3() == null ||
			umic.getAsegurados().getEdadAseg3() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G2);
		}else if(umic.getOtrosDatos().getCestadoAseg3() == null || !umic.getOtrosDatos().getCestadoAseg3().equals("M")){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G6);
		}else{*/
			/*varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);
			if (varUmic2 == null) {
				//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic y btcUmic 
				UtilModulos.clonarUmicBtcumic(mapVariables, CLAVE_UMIC, CLAVE_BTC_UMIC, umic, btcUmic);
				varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);
				varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);
			}*/
		if(null == umic.getAsegurados().getFnacAseg3()){
			
			varFzc1j = BigDecimal.ZERO;
			
		}else if(!umic.getOtrosDatos().getCestadoAseg3().equals("M")){
			
			varFzc1j = BigDecimal.ONE;
			
		}else{
			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);
			if(null == varUmic2){
				varUmic2 = new Umic();
				try {
					//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
					// y btcUmic (con este método no se clonan las listas)
					PropertyUtils.copyProperties(varUmic2, umic);
					
				} catch (Exception e) {
					ModuloSEP011V.LOG.error(e.getMessage());
				}
				
				Asegurados aseg = new Asegurados();
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
				varUmic2.setAsegurados(aseg);
			
				mapVariables.put(CLAVE_UMIC2, varUmic2);
				
			}
			
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if(null == varBtcUmic2){
				varBtcUmic2 = new DetalleBaseTecnica();
				
				try {
					//Se clona la Umic y la btcUmic para asignar los datos del asegurado2 al asegurado1 de la nueva Umic 
					// y btcUmic (con este método no se clonan las listas)
					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
					
				} catch (Exception e) {
					ModuloSEP011V.LOG.error(e.getMessage());
				}
				
				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());
				
				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
					if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
					}
					
					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(2));

					varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(2));
					varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				}
				
				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
			
			}
			varProyFzc1 = proyUmic;
			varFzc1j = (BigDecimal) moduloFZC.execute(varProyFzc1, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
		}
			
		//Se calculará sep011v (j)  = varRentaRev + (varPctHijo * varFptozc1(j))
		SEP011V = varRentaRev.add(varPctHijo.multiply(varFzc1j));
		}			
		
		if (ModuloSEP011V.LOG.isTraceEnabled()) {
			ModuloSEP011V.LOG.trace("Fin función << moduloSEP011V >> de la clase ModuloSEP011V, para la iteracion = {}, con resultado SEP011V = {}", iteracion, SEP011V);
		}
		
		return SEP011V;
	}
}
