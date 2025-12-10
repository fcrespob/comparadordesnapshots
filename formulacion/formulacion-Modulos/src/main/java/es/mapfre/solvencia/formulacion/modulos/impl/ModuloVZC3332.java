package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.formulacion.BaseTecnica;
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

public class ModuloVZC3332 implements Modulo {

	/**
	 * Cte para log.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVZC3332.class);

	// Inicio de las variables estáticas para agilizar operaciones.
	private static final String CLAVE_MODULO = ConstantsFactorias.MODULO_VZC3332;
	private static final String CLAVE_UMIC2 = ConstantsModulos.CTE_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_UMIC3 = ConstantsModulos.CTE_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC2 = ConstantsModulos.CTE_BTC_UMIC.concat(CLAVE_MODULO);
	private static final String CLAVE_BTC_UMIC3 = ConstantsModulos.CTE_BTC_UMIC_3.concat(CLAVE_MODULO);
	private static final String CLAVE_SEX_ASEG1 = ConstantsModulos.CTE_SEX_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_EDAD_ASEG1 = ConstantsModulos.CTE_EDAD_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_FNAC_ASEG1 = ConstantsModulos.CTE_FNAC_ASEG1.concat(CLAVE_MODULO);
	private static final String CLAVE_TABLACALC_ASEG1 = ConstantsModulos.CTE_TABLACALC_ASEG1.concat(CLAVE_MODULO);
	private static final String BA = "BA";
	private static final String BB = "BB";
	private static final String G5 = "G5";
	private static final String GM = "GM";
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
			if (ModuloVZC3332.LOG.isTraceEnabled()) {
				ModuloVZC3332.LOG.trace("Inicio de execute en clase ModuloVZC3332");
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
			resultado = moduloVZC3332(proyUmic, bloqueCorriente, iteracion, fcalc, umic, btcUmic, codSubproceso,
					mapVariables);

		} catch (Solvencia2Excepcion e) {
			ModuloVZC3332.LOG.error(e.getIncidencia().getTextoError(), e);
			throw e;
		} catch (Exception e) {
			ModuloVZC3332.LOG.error(e.getMessage(), e);
			throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_000, null, e);
		}

		if (ModuloVZC3332.LOG.isTraceEnabled()) {
			ModuloVZC3332.LOG.trace("Fin de execute en clase ModuloVZC3332");
		}

		return resultado;
	}

	private BigDecimal moduloVZC3332(List<DetalleCorriente> proyUmic, BloqueCorriente bloqueCorriente, int iteracion,
			Timestamp fcalc, Umic umic, DetalleBaseTecnica btcUmic, String codSubproceso,
			Map<String, Object> mapVariables) {

		// Declaracion de variables
		BigDecimal VZC3332 = BigDecimal.ZERO;
		Umic varUmic1, varUmic2, varUmic3;
		Modulo moduloFZC, moduloVZC, moduloVCVH, moduloVZC2, moduloVZC3;
		DetalleBaseTecnica varBtcUmic2, varBtcUmic3;
		BigDecimal varVzc2, varVzc3= null, varProyFzc1 = null, varProyVCVH = null, varProyVZC2;
		List<DetalleCorriente> varProyFzc = null;
		List<DetalleCorriente> varProyVzc2 = null;
		List<DetalleCorriente> umicProyVCVH = null;
		List<DetalleCorriente> umicProyVZC2 = null;
		List<DetalleCorriente> varProyVzc3 = null;
		Timestamp EdadAseg = null;
		IObtenerConfiguracion.OrdenAsegurado oAseg = null;
		int varHijoId = 0;
		// Fin declaracion

		if (ModuloVZC3332.LOG.isTraceEnabled()) {
			ModuloVZC3332.LOG.trace(
					"Inicio función << ModuloVZC3332 >> de la clase ModuloVZC3332, para la iteracion = {}", iteracion);
		}
		// Se realizará la validación de los parámetros de entrada marcados como
		// obligatorios.
		ValidacionesComunesModulos.validarParamEntrada(proyUmic, bloqueCorriente, fcalc, umic, btcUmic);

		moduloVZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC);
		moduloFZC = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_FZC);
		moduloVCVH = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VCVH);
		moduloVZC2 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC2);
		moduloVZC3 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VZC3);

		// Si estamos en el primer periodo
		if (ConstantsModulos.CTE_FIRST_ITER.equals(iteracion) || (Timestamp) mapVariables.get(CLAVE_FNAC_ASEG1) == null) {
//			varcsporfa_jant = BigDecimal.ZERO;
//			mapVariables.put(CLAVE_VAR_CSPORFA, varcsporfa_jant);
			mapVariables.put(CLAVE_SEX_ASEG1, umic.getAsegurados().getCsexAseg1());
			mapVariables.put(CLAVE_EDAD_ASEG1, umic.getAsegurados().getEdadAseg1());
			mapVariables.put(CLAVE_FNAC_ASEG1, umic.getAsegurados().getFnacAseg1());
			mapVariables.put(CLAVE_TABLACALC_ASEG1, btcUmic.getTablacalc1aseg1());
			mapVariables.put(CLAVE_TABLACONV_ASEG1, btcUmic.getTablasConversionAsegurado().get(0));
			mapVariables.put(CLAVE_TABLACONV_ASEG2, btcUmic.getTablasConversionAsegurado().get(1));
			mapVariables.put(CLAVE_TABLACONV_ASEG3, btcUmic.getTablasConversionAsegurado().get(2));
			mapVariables.put(CLAVE_TABLACONV_ASEG4, btcUmic.getTablasConversionAsegurado().get(3));
			mapVariables.put(CLAVE_TABLACONV_ASEG5, btcUmic.getTablasConversionAsegurado().get(4));
		}
		if (umic.getAsegurados().getFnacAseg2() == null && umic.getAsegurados().getCsexAseg2() == null
				&& umic.getAsegurados().getEdadAseg2() == null) {
			// Error conyuge no informdo, Fin proceso.
			ModuloVZC3332.LOG.trace("Error conyuge no informado, finalizacion del subproceso para la umic");
			throw Solvencia2ExcepcionHelper.crearExcepcion(BA,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}
		if (null == umic.getOtrosDatos().getCestadoAseg1()) {
			// Estado del asegurado no informado, fin proceso.
			ModuloVZC3332.LOG.trace("Estado del asegurado no informado, finalizando el subproceso para la umic");
			throw Solvencia2ExcepcionHelper.crearExcepcion(BB,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}

		if (umic.getDatosGenerales().getKbencon() == null) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(G5,
					new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
		}

		if (bloqueCorriente.getFechaDevengo() != null) {
			if ((umic.getDatosGenerales().getKbencon()).equals("BNC")) {
				varUmic1 = umic;
				varProyFzc = proyUmic;
				varProyVCVH = (BigDecimal) moduloVCVH.execute(varProyFzc, bloqueCorriente, iteracion, fcalc, varUmic1,
						btcUmic, mapVariables, codSubproceso);
				
				mapVariables.put("VZCASEG", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
				varProyFzc1 = (BigDecimal) moduloFZC.execute(varProyFzc, bloqueCorriente, iteracion, fcalc, varUmic1,
						btcUmic, mapVariables, codSubproceso);
				VZC3332 = varProyFzc1.multiply(varProyVCVH);

			} else if ((umic.getDatosGenerales().getKbencon()).equals("301")) {

				varUmic1 = umic;
				varProyFzc = proyUmic;
				varProyVCVH = (BigDecimal) moduloVCVH.execute(varProyFzc, bloqueCorriente, iteracion, fcalc, varUmic1,
						btcUmic, mapVariables, codSubproceso);
				VZC3332 = varProyVCVH;

			} else if ((umic.getDatosGenerales().getKbencon()).compareTo("301") == 1) {

				if (umic.getAsegurados().getFnacAseg3() != null && umic.getAsegurados().getEdadAseg3() != null
						&& umic.getAsegurados().getCsexAseg3() != null) {
					if (umic.getOtrosDatos().getCestadoAseg3().equalsIgnoreCase("M")) {
						varHijoId = 3;
						EdadAseg = umic.getAsegurados().getFnacAseg3();
					}

				}
				if (umic.getAsegurados().getFnacAseg4() != null && umic.getAsegurados().getEdadAseg4() != null
						&& umic.getAsegurados().getCsexAseg4() != null) {
					if (umic.getOtrosDatos().getCestadoAseg4().equalsIgnoreCase("M")) {
						if (EdadAseg == null || umic.getAsegurados().getFnacAseg4().before(EdadAseg)) { 
							varHijoId = 4;
						    EdadAseg = umic.getAsegurados().getFnacAseg4();
						}
					}

				}
				if (umic.getAsegurados().getFnacAseg5() != null && umic.getAsegurados().getEdadAseg5() != null
						&& umic.getAsegurados().getCsexAseg5() != null) {
					if (umic.getOtrosDatos().getCestadoAseg5().equalsIgnoreCase("M")) {
						if (EdadAseg == null || umic.getAsegurados().getFnacAseg5().before(EdadAseg)) {
							varHijoId = 5;
						    EdadAseg = umic.getAsegurados().getFnacAseg5();
						}
					}

				}

				if (varHijoId == 0) {
					throw Solvencia2ExcepcionHelper.crearExcepcion(GM,
							new String[] { ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC });
				} else {
					varUmic3 = (Umic) mapVariables.get(CLAVE_UMIC3);

					if (null == varUmic3) {
						varUmic3 = new Umic();
						try {

							PropertyUtils.copyProperties(varUmic3, umic);

						} catch (Exception e) {
							ModuloVZC3332.LOG.error(e.getMessage());
						}
						Asegurados aseg = new Asegurados();
						if (varHijoId == 3) {
							aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg3());
							aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg3());
							aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg3());
							varUmic3.setAsegurados(aseg);

						}
						if (varHijoId == 4) {
							aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg4());
							aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg4());
							aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg4());
							varUmic3.setAsegurados(aseg);

						}
						if (varHijoId == 5) {
							aseg.setFnacAseg1(umic.getAsegurados().getFnacAseg5());
							aseg.setCsexAseg1(umic.getAsegurados().getCsexAseg5());
							aseg.setEdadAseg1(umic.getAsegurados().getEdadAseg5());
							varUmic3.setAsegurados(aseg);

						}

						mapVariables.put(CLAVE_UMIC3, varUmic3);
					}

					varBtcUmic3 = (DetalleBaseTecnica) mapVariables.get(CLAVE_BTC_UMIC3);
					if (null == varBtcUmic3) {

						varBtcUmic3 = new DetalleBaseTecnica();

						try {

							PropertyUtils.copyProperties(varBtcUmic3, btcUmic);

						} catch (Exception e) {

							ModuloVZC3332.LOG.error(e.getMessage());
						}
						
						if (varHijoId == 3) {
							varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg3());

						}
						if (varHijoId == 4) {
							varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg4());

						}
						if (varHijoId == 5) {
							varBtcUmic3.setTablacalc1aseg1(btcUmic.getTablacalc1aseg5());

						}

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
								&& (!btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE))) {
							if (btcUmic.getTablasConversionAsegurado() == null
									|| btcUmic.getTablasConversionAsegurado().isEmpty()) {
								throw Solvencia2ExcepcionHelper.crearExcepcion(ConstantsFunciones.CTE_COD_ERROR_AC,
										new String[] { null, "tablasConversionAsegurado" });
							}

							List<List<TablaConversion>> tablaConvAseg = new ArrayList<List<TablaConversion>>();
							tablaConvAseg.add(btcUmic.getTablasConversionAsegurado().get(varHijoId - 1));

							varBtcUmic3.setTablasConversionAsegurado(tablaConvAseg);
						} else {
							List<Integer> tablaBaseExp = new ArrayList<Integer>();
							tablaBaseExp.add(btcUmic.getTablaBaseExp().get(varHijoId - 1));
							varBtcUmic3.setTablaBaseExp(tablaBaseExp);
							switch (varHijoId - 1) {
							case 0:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG1;
								break;
							case 1:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG2;
								break;
							case 2:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG3;
								break;
							case 3:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG4;
								break;
							case 4:
								oAseg = IObtenerConfiguracion.OrdenAsegurado.ASEG5;
								break;
							}
							mapVariables.put("VZCASEG", oAseg);
						}

						mapVariables.put(CLAVE_BTC_UMIC3, varBtcUmic3);

					}

					varProyVzc3 = proyUmic;
					varVzc3 = (BigDecimal) moduloVZC.execute(varProyVzc3, bloqueCorriente, iteracion, fcalc, varUmic3,
							varBtcUmic3, mapVariables, codSubproceso);
					VZC3332 = varVzc3;

				}

			} 

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
		if (ModuloVZC3332.LOG.isTraceEnabled()) {
			ModuloVZC3332.LOG.trace(
					"Fin función << ModuloVZC3332 >> de la clase ModuloVZC3332, para la iteracion = {}, con resultado VZC3332 = {}",
					iteracion, VZC3332);
		}
		
		return VZC3332;

	}
}
