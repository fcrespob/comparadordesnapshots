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
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Módulo que calculará la probabilidad de viudedad
 * @author eugenio.torres
 *
 */
public class ModuloVZC333 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC333.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC333;
	private static final String CLAVE_UMIC = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
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
			if (ModuloVZC333.LOG.isTraceEnabled()) {
				ModuloVZC333.LOG.trace("Inicio de execute en clase VZC333");
			}

			//Recuperamos los datos que le pasaremos a la función moduloVZC333
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			//Invocamos a la función moduloVZC333
			resultado = moduloVZC333(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso ,mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVZC333.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC333.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZC333.LOG.isTraceEnabled()) {
			ModuloVZC333.LOG.trace("Fin de execute en clase VZC333");
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
	private BigDecimal moduloVZC333(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		//Variables locales
		BigDecimal VZC333 = BigDecimal.ZERO;
		Modulo moduloVZC, moduloFPTODIFER;
		BigDecimal varFptodifer1j, varVzc2j = BigDecimal.ZERO;
		Umic varUmic2, varUmic1;
		DetalleBaseTecnica  varBtcUmic2;
		List<DetalleCorriente> varProyVzc2 = null;
		List<DetalleCorriente> varProyFptozc1 = null;
		//Fin variables locales

		if (ModuloVZC333.LOG.isTraceEnabled()) {
			ModuloVZC333.LOG.trace("Inicio función << moduloVZC333 >> de la clase moduloVZC333, para la iteracion = {}", iteracion);
		}

		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);
		
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFPTODIFER = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FPTODIFER);

		if (null == umic.getAsegurados().getFnacAseg2()) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);
		}else if(null == umic.getOtrosDatos().getCestadoAseg1()){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G4);
		}else if(null == umic.getDatosGenerales().getKbencon()){
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G5);
		}

		if(umic.getDatosGenerales().getKbencon().substring(0,2).equals(ConstantsModulos.CTE_30)){

			varFptodifer1j = BigDecimal.ONE;

		}
		else{
			varUmic1 = umic;
			varProyFptozc1 = proyUmic;
		
			varFptodifer1j = (BigDecimal) moduloFPTODIFER.execute(proyUmic, bloqueCorriente, iteracion, fcalc, varUmic1, btcUmic, mapVariables, codSubproceso);

			
		}


		varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC);

		if (null == varUmic2) {

			varUmic2 = new Umic();
			try {

				PropertyUtils.copyProperties(varUmic2, umic);

			} catch (Exception e) {
				ModuloVZC333.LOG.error(e.getMessage());
			}

			Asegurados aseg = new Asegurados();
			aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
			aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
			aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
			varUmic2.setAsegurados(aseg);

			mapVariables.put(CLAVE_UMIC,varUmic2);

		}

		varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC);

		if(null == varBtcUmic2){
			varBtcUmic2 = new DetalleBaseTecnica();

			try {
				PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

			} catch (Exception e) {
				ModuloVZC333.LOG.error(e.getMessage());
			}

			varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg2());
			
			if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIF17LIR))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIFF17OCI))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17LIRIN))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_NIIF17IF))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_N17CLIR))
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
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))
					&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))){
				if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
				}
				
				List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
				btcUmic.getTablasConversionAsegurado().get(1).get(0).setTablaInicio(btcUmic.getTablacalc1aseg2());
				btcUmic.getTablasConversionAsegurado().get(1).get(0).setTablaFin(btcUmic.getTablacalc1aseg2());
				tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(1));

				varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
			} else {
//				List<Integer> tablaBaseExp = new ArrayList<Integer>();
//				tablaBaseExp.add(Integer.parseInt(btcUmic.getTablacalc1aseg2()));
//				varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				List<Integer> tablaBaseExp = new ArrayList<Integer>();
				tablaBaseExp.add(btcUmic.getTablaBaseExp().get(1));
				varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG2);
			}

			mapVariables.put(CLAVE_BTC_UMIC,varBtcUmic2);


		}

		varProyVzc2 = proyUmic;
		varVzc2j = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);

		//Se realiza el cálculo de VZC333 (j) = varFptozc1 (j) * varVzc2 (j)
		VZC333 = varFptodifer1j.multiply(varVzc2j);

		if (ModuloVZC333.LOG.isTraceEnabled()) {
			ModuloVZC333.LOG.trace("Fin función << moduloVZC333 >> de la clase ModuloVZC333, para la iteracion = {}, con resultado VZC333 = {}", iteracion, VZC333);
		}

		return VZC333;
	}
}
