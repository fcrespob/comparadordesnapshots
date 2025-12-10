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

public class ModuloVCVH implements Modulo {
	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVCVH.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VCVH;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG1 = ConstantsModulos.CTE_TABLACONV_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG2 = ConstantsModulos.CTE_TABLACONV_ASEG2.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG3 = ConstantsModulos.CTE_TABLACONV_ASEG3.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG4 = ConstantsModulos.CTE_TABLACONV_ASEG4.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACONV_ASEG5 = ConstantsModulos.CTE_TABLACONV_ASEG5.concat(CLAVE_MODULO);


	// Fin de las variables estáticas usadas para agilizar operaciones.
	public String getNombreServicio() {
		return CLAVE_MODULO;
	}

	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la
	 * llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {
		// Variables locales
		BigDecimal resultado = BigDecimal.ZERO;
		// Fin variables locales

		try {
			if (ModuloVCVH.LOG.isTraceEnabled()) {
				ModuloVCVH.LOG.trace("Inicio de execute en clase ModuloVCVH");
			}

			// Recuperamos los datos que le pasaremos a la función moduloFZCHM1
			final List<DetalleCorriente> proyUmic = (List<DetalleCorriente>) args[ConstantsModulos.PARAM_P_PROYUMIC];
			final BloqueCorriente bloqueCorriente = (BloqueCorriente) args[ConstantsModulos.PARAM_P_PROY_BLK];
			final int iteracion = (Integer) args[ConstantsModulos.PARAM_P_ITERACI];
			final Timestamp fcalc = (Timestamp) args[ConstantsModulos.PARAM_P_FCALC];
			final Umic umic = (Umic) args[ConstantsModulos.PARAM_P_UMIC];
			final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantsModulos.PARAM_P_BTC_UMIC];
			final Map<String, Object> mapVariables = (Map<String, Object>) args[ConstantsModulos.PARAM_P_MAP_VARIA];
			final String codSubproceso = (String) args[ConstantsModulos.PARAM_P_SUBPROC];
			// Fin de la recuperación de datos.

			// Invocamos a la función ModuloFZCHM1
			resultado = moduloVCVH(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVCVH.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVCVH.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVCVH.LOG.isTraceEnabled()) {
			ModuloVCVH.LOG.trace("Fin de execute en clase ModuloVCVH");
		}

		return resultado;
	}

	private BigDecimal moduloVCVH(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		// Variables locales
		BigDecimal VCVH = BigDecimal.ZERO;
		Modulo moduloFZCHM1, moduloVZC, moduloFZC;
		DetalleBaseTecnica varBtcUmic, varBtcUmic2;
		BigDecimal varFzchm1, varVzc2, varFzc2;
		List<DetalleCorriente> varProyFzchm1 = null;
		List<DetalleCorriente> varProyVzc2 = null;
		List<DetalleCorriente> varProyFzc2 = null;
		Umic varUmic2;
		BigDecimal varcsporfa_jant = BigDecimal.ZERO;
		boolean varHijoMinusvalido = false;
		int varIdHijo = 0;
		// Fin variables locales

		if (ModuloVCVH.LOG.isTraceEnabled()) {
			ModuloVCVH.LOG.trace("Inicio función << ModuloVCVH >> de la clase moduloVCHM, para la iteracion = {}",
					iteracion);
		}
		// Se realizará la validación de los parámetros de entrada marcados como
		// obligatorios.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		moduloFZCHM1 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZCHM1);
		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);

//		if (umic.getAsegurados().getFnacAseg3() == null || umic.getAsegurados().getCsexAseg3() == null
//				|| umic.getAsegurados().getEdadAseg3() == null) {
//			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_G3);
//		}
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
//			varcsporfa_jant = BigDecimal.ZERO;
//			mapVariables.put(CLAVE_VAR_CSPORFA, varcsporfa_jant);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_TABLACONV_ASEG1, btcUmic.getTablasConversionAsegurado().get(0));
			mapVariables.put(CLAVE_TABLACONV_ASEG2, btcUmic.getTablasConversionAsegurado().get(1));
			mapVariables.put(CLAVE_TABLACONV_ASEG3, btcUmic.getTablasConversionAsegurado().get(2));
			mapVariables.put(CLAVE_TABLACONV_ASEG4, btcUmic.getTablasConversionAsegurado().get(3));
			mapVariables.put(CLAVE_TABLACONV_ASEG5, btcUmic.getTablasConversionAsegurado().get(4));
		}

		if (null != bloqueCorriente.getFechaDevengo()) {
			
			if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getCsexAseg3() != null
					&& umic.getAsegurados().getEdadAseg3() != null) {
				if (umic.getOtrosDatos().getCestadoAseg3().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					varHijoMinusvalido = true;
				}
			}
			if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getCsexAseg4() != null
					&& umic.getAsegurados().getEdadAseg4() != null) {
				if (umic.getOtrosDatos().getCestadoAseg4().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if ( umic.getAsegurados().getFnacAseg4().after(umic.getAsegurados().getFnacAseg3())) {
						varHijoMinusvalido = true;
					}
				}
			}
			if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getCsexAseg5() != null
					&& umic.getAsegurados().getEdadAseg5() != null) {
				if (umic.getOtrosDatos().getCestadoAseg5().equals(ConstantsModulos.CTE_CESTADO_ASEG_M)) {
					if ( umic.getAsegurados().getFnacAseg5().after(umic.getAsegurados().getFnacAseg4())) {
						varHijoMinusvalido = true;
					}
				}
			}
			
			varProyFzchm1 = proyUmic;
			varFzchm1 = (BigDecimal) moduloFZCHM1.execute(varProyFzchm1, bloqueCorriente, iteracion, fcalc, umic,
					btcUmic, mapVariables, codSubproceso);

			varUmic2 = (Umic) mapVariables.get(CLAVE_UMIC2);

			if (varUmic2 == null) {
				varUmic2 = new Umic();
				try {

					PropertyUtils.copyProperties(varUmic2, umic);

				} catch (Exception e) {
					ModuloVCVH.LOG.error(e.getMessage());
				}
				Asegurados aseg = new Asegurados();
				aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg2());
				aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg2());
				aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg2());
				varUmic2.setAsegurados(aseg);

				mapVariables.put(CLAVE_UMIC2, varUmic2);
			}

			varBtcUmic2 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC2);
			if (null == varBtcUmic2) {
				varBtcUmic2 = new DetalleBaseTecnica();

				try {

					PropertyUtils.copyProperties(varBtcUmic2, btcUmic);

				} catch (Exception e) {
					ModuloVCVH.LOG.error(e.getMessage());
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
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))
						&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO))) {
					if (btcUmic.getTablasConversionAsegurado() == null
							|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
						throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
								new String[] { null, "tablasConversionAsegurado" });
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
			if (umic.getOtrosDatos().getCestadoAseg2() == null || (varHijoMinusvalido==true && umic.getOtrosDatos().getCestadoAseg2().equals("A"))) {
				varFzc2 = BigDecimal.ONE;
				varVzc2 = BigDecimal.ZERO;
			}else {
				varProyVzc2 = proyUmic;
				varVzc2 = (BigDecimal) moduloVZC.execute(varProyVzc2, bloqueCorriente, iteracion, fcalc, varUmic2,
						varBtcUmic2, mapVariables, codSubproceso);
				varProyFzc2 = proyUmic;
				varFzc2 = (BigDecimal) moduloFZC.execute(varProyFzc2, bloqueCorriente, iteracion, fcalc, varUmic2,
						varBtcUmic2, mapVariables, codSubproceso);
			}
			if(varHijoMinusvalido==true) {
				varProyFzchm1 = proyUmic;
				varFzchm1 = (BigDecimal) moduloFZCHM1.execute(varProyFzchm1, bloqueCorriente, iteracion, fcalc, umic,
						btcUmic, mapVariables, codSubproceso);

			}else {
				varFzchm1 = BigDecimal.ONE;
			}
			
			BigDecimal VZVH_REST = BigDecimal.ONE.subtract(varFzchm1);
			BigDecimal VCVH_MULTIPLY = VZVH_REST.multiply(varFzc2);
			VCVH = varVzc2.add(VCVH_MULTIPLY);
		}

		if (iteracion == proyUmic.size()) {
			umic.getAsegurados().setCsexAseg1((String) mapVariables.get(CLAVE_SEX_ASEG1));
			umic.getAsegurados().setEdadAseg1((Integer) mapVariables.get(CLAVE_EDAD_ASEG1));
			umic.getAsegurados().setFnacAseg1((Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1));
			btcUmic.setTablacalc1aseg1((String) mapVariables.get(CLAVE_TABLACALC_ASEG1));
			List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
			

			if (mapVariables.get(CLAVE_TABLACONV_ASEG1) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG1)));
			}												
			if (mapVariables.get(CLAVE_TABLACONV_ASEG2) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG2)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG3) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG3)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG4) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG4)));
			}
			if (mapVariables.get(CLAVE_TABLACONV_ASEG5) != null) {
				tablaConvAseg.add((List<TablaConversion>) (mapVariables.get(CLAVE_TABLACONV_ASEG5)));
			}
			
			btcUmic.setTablasConversionAsegurado(tablaConvAseg);
		}

		if (ModuloVCVH.LOG.isTraceEnabled()) {
			ModuloVCVH.LOG.trace(
					"Fin función << ModuloVCVH>> de la clase ModuloVCVH, para la iteracion = {}, con resultado VZVH = {}",
					iteracion, VCVH);
		}

		return VCVH;
	}

}
