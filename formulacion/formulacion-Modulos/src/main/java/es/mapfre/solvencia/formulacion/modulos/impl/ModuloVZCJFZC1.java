package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.servicios.impl.ObtenerDatos;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class ModuloVZCJFZC1 implements Modulo {
	
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZCJFZC1.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZCJFZC1;	
	
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	
	
	
	@Override
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Módulo que calcula una prestación temporal de orfandad, cuyo vencimiento dependerá de cada póliza, y estará definido en la tabla de datos de Sepi / Endesa.
	 */
	
	@Override
	public Object execute(Object... args) throws Solvencia2Excepcion {
		
		BigDecimal varvzcjfzc1= BigDecimal.ZERO;
		
		try {
			if (ModuloVZCJFZC1.LOG.isTraceEnabled()) {
				ModuloVZCJFZC1.LOG.trace("Inicio de execute en clase ModuloVZCJFZC1");
			}
			
			//Recuperamos los datos que le pasaremos a la función ModuloCSPVIUE01
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			
			varvzcjfzc1 = moduloVZCJFZC1(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso, mapVariables);
			
		} catch (Solvencia2Excepcion e) {
			ModuloVZCJFZC1.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZCJFZC1.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}
	
		if (ModuloVZCJFZC1.LOG.isTraceEnabled()) {
			ModuloVZCJFZC1.LOG.trace("Fin de execute en clase ModuloVZCJFZC1");
		}
		
		return varvzcjfzc1;
	}

	
	/**
	 * Módulo que calcula una prestación temporal de orfandad, cuyo vencimiento dependerá de cada póliza, y estará definido en la tabla de datos de Sepi / Endesa
	 * 
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
	
	private BigDecimal moduloVZCJFZC1(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {
		
		List<DetalleCorriente> varProyVzc;
		List<DetalleCorriente> varProyVzc2;
		final IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
		Umic varUmic2, umicAux;
		DetalleBaseTecnica  varBtcUmic2;
		Modulo moduloVZC;
		BigDecimal varVzc1j = BigDecimal.ZERO;
		BigDecimal varVzc2j = BigDecimal.ZERO;
		BigDecimal vzcjfzc1 = BigDecimal.ZERO;
		Integer id = 0;
		
		
		if (ModuloVZCJFZC1.LOG.isTraceEnabled()) {
			ModuloVZCJFZC1.LOG.trace("Inicio función << moduloVZCJFZC1 >> de la clase ModuloVZCJFZC1, para la  iteracion = {}", iteracion);
		}
		
		//validamos los parametros de entrada
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);	
		
		// Fin de la definición de las variables auxiliares.	
		
		if(null == bloqueCorriente.getFechaDevengo()){
			return vzcjfzc1;
		}
				
		//Varibales de apoyo
		
		varProyVzc = proyUmic;
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		varVzc1j = (BigDecimal) moduloVZC.execute(varProyVzc, bloqueCorriente, iteracion, fcalc, umic, btcUmic, mapVariables, codSubproceso);
		
		if (null == umic.getAsegurados().getFnacAseg2() || null == umic.getAsegurados().getCsexAseg2() ||
			null == umic.getAsegurados().getEdadAseg2()) {
			
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);	
			
		}else {
			
			Asegurados aseg = new Asegurados();
			varUmic2 = new Umic();
			try {
				PropertyUtils.copyProperties(varUmic2, umic);
				
			} catch (Exception e) {
				ModuloVZCJFZC1.LOG.error(e.getMessage());
			}
			
			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if(varBtcUmic2 == null) {
				//varBtcUmic2 = btcUmic;
				try {
					varBtcUmic2 = new DetalleBaseTecnica();
					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);
					
				} catch (Exception e) {
					ModuloVZCJFZC1.LOG.error(e.getMessage());
				}
			}
			
			//Recuperar todas las umics por norden distinto
		//HashMap<String, Object> mapAsegurados = new ;
			id = obtenerDatos.recuperarUmicRelacionadas(varUmic2.getKey(),umic.getDatosGenerales().getNorden());
			switch (id) {
			case 1:
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GH);
			case 2:
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
				varUmic2.setAsegurados(aseg);
				mapVariables.put(CLAVE_UMIC2, varUmic2);
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
				
				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
				break;
			case 3:
				if (null == umic.getAsegurados().getFnacAseg3() || null == umic.getAsegurados().getCsexAseg3() ||
					null == umic.getAsegurados().getEdadAseg3()) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GI);	
				}else {
					aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
					aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
					aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
					varUmic2.setAsegurados(aseg);
					mapVariables.put(CLAVE_UMIC2, varUmic2);
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
				break;
			case 4:
				if (null == umic.getAsegurados().getFnacAseg4() || null == umic.getAsegurados().getCsexAseg4() ||
					null == umic.getAsegurados().getEdadAseg4()) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GJ);	
				}else {
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg4());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg4());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg4());
				varUmic2.setAsegurados(aseg);
				mapVariables.put(CLAVE_UMIC2, varUmic2);
				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg4());
				
				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
					if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
					}
					
					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(3));

					varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(3));
					varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				}
				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
				}
			break;
			case 5:
				if (null == umic.getAsegurados().getFnacAseg5() || null == umic.getAsegurados().getCsexAseg5() ||
				null == umic.getAsegurados().getEdadAseg5()) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GK);	
			}else {
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg5());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg5());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg5());
				varUmic2.setAsegurados(aseg);
				mapVariables.put(CLAVE_UMIC2, varUmic2);
				varBtcUmic2.setTablacalc1aseg1(btcUmic.getTablacalc1aseg5());
				
				if ((!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_BT_BEL))  && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCOA)) && (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_BELCLR))){
					if (btcUmic.getTablasConversionAsegurado() == null || btcUmic.getTablasConversionAsegurado().isEmpty()){
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC, new String[]{null, "tablasConversionAsegurado"});
					}
					
					List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
					tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(4));

					varBtcUmic2.setTablasConversionAsegurado(tablaConvAseg);
				} else {
					List<Integer> tablaBaseExp = new ArrayList<Integer>();
					tablaBaseExp.add(btcUmic.getTablaBaseExp().get(4));
					varBtcUmic2.setTablaBaseExp(tablaBaseExp);
				}
				mapVariables.put(CLAVE_BTC_UMIC2, varBtcUmic2);
				}
			break;
			default:
				throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_GP);
			}
				varProyVzc2 = proyUmic;
				varVzc2j = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2, varBtcUmic2, mapVariables, codSubproceso);
				vzcjfzc1 = varVzc2j.multiply(BigDecimal.ONE.subtract(varVzc1j));
					
			if (ModuloVZCJFZC1.LOG.isTraceEnabled()) {
				ModuloVZCJFZC1.LOG.trace("Fin de la función << moduloVZCJFZC1 >> de la clase ModuloVZCJFZC1, para la iteración = {}", iteracion);
			}
		
			return vzcjfzc1;
		}
	}

}
