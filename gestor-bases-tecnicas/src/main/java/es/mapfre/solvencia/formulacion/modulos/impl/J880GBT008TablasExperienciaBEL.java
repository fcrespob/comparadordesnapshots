package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.ConvTablasExpRealDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.ConvTablasExpReal;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.excepcionGBT.GestorBasesTecnicasException;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.ConstantsProcesos;
import es.mapfre.solvencia.gbt.util.ConstantesErrores;
import es.mapfre.solvencia.gbt.util.ConstantesGBT;
import es.mapfre.solvencia.gbt.util.ConstantesModulosGBT;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class J880GBT008TablasExperienciaBEL implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT008TablasExperienciaBEL INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT008TablasExperienciaBEL getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT008TablasExperienciaBEL();
		}
		return INSTANCE;
	}

	@Override
	public Object execute(Object... args) {

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		try {
			// Calculo de base tecnica
			// final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica)
			// args[ConstantesGBT.PARAM_BTC];
			// final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
			final Boolean simulacion = (Boolean) args[ConstantesGBT.PARAM_SIMUL];

			// Base tecnica inicial de la umic y Datos generales
			final DatosGenerales datosGenerales = umic.getDatosGenerales();
			final Asegurados asegurados = umic.getAsegurados();

			exc.setGeneradorError(getNombreServicio());
			exc.setTipoError(ConstantesErrores.CTE_ERROR);

			if (btcUmic.getBaseTec() == null || btcUmic.getBaseTec().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BT);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BT);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT);
				throw exc;
			}
			if (datosGenerales.getCcanal() == null || datosGenerales.getCcanal() == 0) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CANAL);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CANAL);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CANAL);
				throw exc;
			}
			if (datosGenerales.getCnegocio() == null || datosGenerales.getCnegocio().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_NEG);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_NEG);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_NEG);
				throw exc;
			}
			if (datosGenerales.getTipoSubriesgo() == null || datosGenerales.getTipoSubriesgo().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SUBR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SUBR);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SUBR);
				throw exc;
			}
			if (asegurados.getCsexAseg1() == null || asegurados.getCsexAseg1().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_SEXO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_SEXO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_SEXO);
				throw exc;
			}
			if (datosGenerales.getKcategoria() == null || datosGenerales.getKcategoria().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CAT);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_CAT);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CAT);
				throw exc;
			}
			if (asegurados.getEdadAseg1() == null || asegurados.getEdadAseg1() == 0) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_EDAD);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_EDAD);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_EDAD);
				throw exc;
			}
			if (datosGenerales.getKmodalidad() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MOD);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_MOD);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MOD);
				throw exc;
			}
			if (umic.getDatosGenerales().getFecCierre() == null) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FCIERRE);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_FCIERRE);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FCIERRE);
				throw exc;
			}

			List<String> sexo = new ArrayList<String>();
			if (umic.getAsegurados().getCsexAseg1() != null && !umic.getAsegurados().getCsexAseg1().equals(""))
				sexo.add(umic.getAsegurados().getCsexAseg1());
			if (umic.getAsegurados().getCsexAseg2() != null && !umic.getAsegurados().getCsexAseg2().equals(""))
				sexo.add(umic.getAsegurados().getCsexAseg2());
			if (umic.getAsegurados().getCsexAseg3() != null && !umic.getAsegurados().getCsexAseg3().equals(""))
				sexo.add(umic.getAsegurados().getCsexAseg3());
			if (umic.getAsegurados().getCsexAseg4() != null && !umic.getAsegurados().getCsexAseg4().equals(""))
				sexo.add(umic.getAsegurados().getCsexAseg4());
			if (umic.getAsegurados().getCsexAseg5() != null && !umic.getAsegurados().getCsexAseg5().equals(""))
				sexo.add(umic.getAsegurados().getCsexAseg5());

			List<Integer> edad = new ArrayList<Integer>();
			if ((umic.getAsegurados().getEdadAseg1() != null || !umic.getAsegurados().getEdadAseg1().equals(0)) && (umic.getAsegurados().getCsexAseg1() != null) && (!umic.getAsegurados().getCsexAseg1().equals("")))
				edad.add(umic.getAsegurados().getEdadAseg1());
			if ((umic.getAsegurados().getEdadAseg2() != null || !umic.getAsegurados().getEdadAseg2().equals(0)) && (umic.getAsegurados().getCsexAseg2() != null) && (!umic.getAsegurados().getCsexAseg2().equals("")))
				edad.add(umic.getAsegurados().getEdadAseg2());
			if ((umic.getAsegurados().getEdadAseg3() != null || umic.getAsegurados().getEdadAseg3().equals(0)) && (umic.getAsegurados().getCsexAseg3() != null) && (!umic.getAsegurados().getCsexAseg3().equals("")))
				edad.add(umic.getAsegurados().getEdadAseg3());
			if ((umic.getAsegurados().getEdadAseg4() != null || !umic.getAsegurados().getEdadAseg4().equals(0)) && (umic.getAsegurados().getCsexAseg4() != null) && (!umic.getAsegurados().getCsexAseg4().equals("")))
				edad.add(umic.getAsegurados().getEdadAseg4());
			if ((umic.getAsegurados().getEdadAseg5() != null || !umic.getAsegurados().getEdadAseg5().equals(0)) &&  (umic.getAsegurados().getCsexAseg5() != null) && (!umic.getAsegurados().getCsexAseg5().equals("")))
				edad.add(umic.getAsegurados().getEdadAseg5());

			if (sexo.size() != edad.size()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_EDAD_SEXO);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_EDAD_SEXO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_EDAD_SEXO);
				throw exc;
			}

			List<ConvTablasExpReal> cte = null;
			List<Integer> tablaExp = new ArrayList<Integer>();
			List<BigDecimal> factor1 = new ArrayList<BigDecimal>();
			List<List<BigDecimal>> factor2 = new ArrayList<List<BigDecimal>>();
			
			List<TablaConversion> tablasConv = new ArrayList<TablaConversion>();
			List<List<TablaConversion>> tablaConvTotal = new ArrayList<List<TablaConversion>>();
			List<List<Integer>> tablaExpList = new ArrayList<List<Integer>>();
			List<Integer> tablaAseg1 = new ArrayList<Integer>();
			List<Integer> tablaAseg2 = new ArrayList<Integer>();
			List<Integer> tablaAseg3 = new ArrayList<Integer>();
			List<Integer> tablaAseg4 = new ArrayList<Integer>();
			List<Integer> tablaAseg5 = new ArrayList<Integer>();
			String kriesgo = "";
			Integer tab11 = null;
			Integer tab21 = null;
			Integer tab31 = null;
			Integer tab12 = null;
			Integer tab22 = null;
			Integer tab32 = null;
			Integer tab13 = null;
			Integer tab23 = null;
			Integer tab33 = null;
			Integer tab14 = null;
			Integer tab24 = null;
			Integer tab34 = null;
			Integer tab15 = null;
			Integer tab25 = null;
			Integer tab35 = null;

			if (umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_INCA)) {
				kriesgo = "FALL";
				for (int i = 0; i < sexo.size(); i++) {
					cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i),
							umic.getDatosGenerales().getKmodalidad(), kriesgo);
					if (cte.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
						throw exc;
					}
					if (cte.isEmpty()) {
						cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
						if (cte.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
							throw exc;
						}
						if (cte.isEmpty()) {
							if (simulacion.booleanValue()) {
								cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i),
										umic.getDatosGenerales().getKmodalidad(), kriesgo);
								if (cte.isEmpty()) {
									cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
									if (cte.isEmpty()) {
										exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
										exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
										exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
										throw exc;
									}
								}
							} else {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
								throw exc;
							}
						}
					}
					if (i == 0) {
						tab11 = cte.get(0).getCtabbase();
						tab21 = cte.get(0).getCtabbase();
					}
					if (i == 1) {
						tab12 = cte.get(0).getCtabbase();
						tab22 = cte.get(0).getCtabbase();
					}
					if (i == 2) {
						tab13 = cte.get(0).getCtabbase();
						tab23 = cte.get(0).getCtabbase();
					}
					if (i == 3) {
						tab14 = cte.get(0).getCtabbase();
						tab24 = cte.get(0).getCtabbase();
					}
					if (i == 4) {
						tab15 = cte.get(0).getCtabbase();
						tab25 = cte.get(0).getCtabbase();
					}
					
					tablaExp.add(cte.get(0).getCtabbase());
					factor1.add(cte.get(0).getPfactor1());

					List<BigDecimal> factores = new ArrayList<BigDecimal>();
					factores.add(cte.get(0).getPfactor2a());
					factores.add(cte.get(0).getPfactor2b());
					factores.add(cte.get(0).getPfactor2c());
					factores.add(cte.get(0).getPfactor2d());
					factores.add(cte.get(0).getPfactor2e());
					factores.add(cte.get(0).getPfactor2f());
					factores.add(cte.get(0).getPfactor2g());
					factores.add(cte.get(0).getPfactor2h());
					factores.add(cte.get(0).getPfactor2i());
					factores.add(cte.get(0).getPfactor2j());

					factor2.add(factores);
					//}

					// Se rellenan los factores1 no informados
					for (int a = factor1.size(); a < ConstantesGBT.NUM_ASEGURADOS; a++) {
						factor1.add(null);
						factor2.add(null);
					}
				}
				
				kriesgo = "INCA";
				
				for (int i = 0; i < sexo.size(); i++) {
					cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i),
							umic.getDatosGenerales().getKmodalidad(), kriesgo);
					if (cte.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
						throw exc;
					}
					if (cte.isEmpty()) {
						cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
						if (cte.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
							throw exc;
						}
						if (cte.isEmpty()) {
							if (simulacion.booleanValue()) {
								cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i),
										umic.getDatosGenerales().getKmodalidad(), kriesgo);
								if (cte.isEmpty()) {
									cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
									if (cte.isEmpty()) {
										exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
										exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
										exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
										throw exc;
									}
								}
							} else {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
								throw exc;
							}
						}
					}
					if (i == 0) {
						tab31 = cte.get(0).getCtabbase();
					}
					if (i == 1) {
						tab32 = cte.get(0).getCtabbase();
					}
					if (i == 2) {
						tab33 = cte.get(0).getCtabbase();
					}
					if (i == 3) {
						tab34 = cte.get(0).getCtabbase();
					}
					if (i == 4) {
						tab35 = cte.get(0).getCtabbase();
					}
				}
			}
			
			if (umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_FACC)) {
				kriesgo = "FALL";
				for (int i = 0; i < sexo.size(); i++) {
					cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i),
							umic.getDatosGenerales().getKmodalidad(), kriesgo);
					if (cte.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
						throw exc;
					}
					if (cte.isEmpty()) {
						cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
						if (cte.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
							throw exc;
						}
						if (cte.isEmpty()) {
							if (simulacion.booleanValue()) {
								cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i),
										umic.getDatosGenerales().getKmodalidad(), kriesgo);
								if (cte.isEmpty()) {
									cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
									if (cte.isEmpty()) {
										exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
										exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
										exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
										throw exc;
									}
								}
							} else {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
								throw exc;
							}
						}
					}
					if (i == 0) {
						tab11 = cte.get(0).getCtabbase();
						tab21 = cte.get(0).getCtabbase();
					}
					if (i == 1) {
						tab12 = cte.get(0).getCtabbase();
						tab22 = cte.get(0).getCtabbase();
					}
					if (i == 2) {
						tab13 = cte.get(0).getCtabbase();
						tab23 = cte.get(0).getCtabbase();
					}
					if (i == 3) {
						tab14 = cte.get(0).getCtabbase();
						tab24 = cte.get(0).getCtabbase();
					}
					if (i == 4) {
						tab15 = cte.get(0).getCtabbase();
						tab15 = cte.get(0).getCtabbase();
					}
					
					tablaExp.add(cte.get(0).getCtabbase());
					factor1.add(cte.get(0).getPfactor1());

					List<BigDecimal> factores = new ArrayList<BigDecimal>();
					factores.add(cte.get(0).getPfactor2a());
					factores.add(cte.get(0).getPfactor2b());
					factores.add(cte.get(0).getPfactor2c());
					factores.add(cte.get(0).getPfactor2d());
					factores.add(cte.get(0).getPfactor2e());
					factores.add(cte.get(0).getPfactor2f());
					factores.add(cte.get(0).getPfactor2g());
					factores.add(cte.get(0).getPfactor2h());
					factores.add(cte.get(0).getPfactor2i());
					factores.add(cte.get(0).getPfactor2j());

					factor2.add(factores);
					//}

					// Se rellenan los factores1 no informados
					for (int a = factor1.size(); a < ConstantesGBT.NUM_ASEGURADOS; a++) {
						factor1.add(null);
						factor2.add(null);
					}
				}
				
				kriesgo = "FACC";
				
				for (int i = 0; i < sexo.size(); i++) {
					cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i),
							umic.getDatosGenerales().getKmodalidad(), kriesgo);
					if (cte.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
						throw exc;
					}
					if (cte.isEmpty()) {
						cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
						if (cte.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
							throw exc;
						}
						if (cte.isEmpty()) {
							if (simulacion.booleanValue()) {
								cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i),
										umic.getDatosGenerales().getKmodalidad(), kriesgo);
								if (cte.isEmpty()) {
									cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
									if (cte.isEmpty()) {
										exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
										exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
										exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
										throw exc;
									}
								}
							} else {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
								throw exc;
							}
						}
					}
					if (i == 0) {
						tab31 = cte.get(0).getCtabbase();
					}
					if (i == 1) {
						tab32 = cte.get(0).getCtabbase();
					}
					if (i == 2) {
						tab33 = cte.get(0).getCtabbase();
					}
					if (i == 3) {
						tab34 = cte.get(0).getCtabbase();
					}
					if (i == 4) {
						tab35 = cte.get(0).getCtabbase();
					}
				}
			}
			
			if (umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_OTRO)) {
				kriesgo = "";
				final BaseTecnicaInicial btinicial = umic.getBti();
				btcUmic.setTablacalc1aseg1(btinicial.getTabla1Aseg1());
				btcUmic.setTablacalc2aseg1(btinicial.getTabla2Aseg1());
				btcUmic.setTablacalc3aseg1(btinicial.getTabla3Aseg1());
				btcUmic.setTablacalc1aseg2(btinicial.getTabla1Aseg2());
				btcUmic.setTablacalc2aseg2(btinicial.getTabla2Aseg2());
				btcUmic.setTablacalc3aseg2(btinicial.getTabla3Aseg2());
				btcUmic.setTablacalc1aseg3(btinicial.getTabla1Aseg3());
				btcUmic.setTablacalc2aseg3(btinicial.getTabla2Aseg3());
				btcUmic.setTablacalc3aseg3(btinicial.getTabla3Aseg3());
				btcUmic.setTablacalc1aseg4(btinicial.getTabla1Aseg4());
				btcUmic.setTablacalc2aseg4(btinicial.getTabla2Aseg4());
				btcUmic.setTablacalc3aseg4(btinicial.getTabla3Aseg4());
				btcUmic.setTablacalc1aseg5(btinicial.getTabla1Aseg5());
				btcUmic.setTablacalc2aseg5(btinicial.getTabla2Aseg5());
				btcUmic.setTablacalc3aseg5(btinicial.getTabla3Aseg5());
				
				tablasConv.add(crearTablaConversion(btinicial.getTabla1Aseg2(), btinicial.getTabla1Aseg2()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla2Aseg1(), btinicial.getTabla2Aseg1()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla3Aseg1(), btinicial.getTabla3Aseg1()));
				tablaConvTotal.add(tablasConv);
				
				tab11 = new Integer(btinicial.getTabla1Aseg1());
				tab21 = new Integer(btinicial.getTabla2Aseg1());
				tab31 = new Integer(btinicial.getTabla3Aseg1());
				tab12 = new Integer(btinicial.getTabla1Aseg2());
				tab22 = new Integer(btinicial.getTabla2Aseg2());
				tab32 = new Integer(btinicial.getTabla3Aseg2());
				tab13 = new Integer(btinicial.getTabla1Aseg3());
				tab23 = new Integer(btinicial.getTabla2Aseg3());
				tab33 = new Integer(btinicial.getTabla3Aseg3());
				tab14 = new Integer(btinicial.getTabla1Aseg4());
				tab24 = new Integer(btinicial.getTabla2Aseg4());
				tab34 = new Integer(btinicial.getTabla3Aseg4());
				tab15 = new Integer(btinicial.getTabla1Aseg5());
				tab25 = new Integer(btinicial.getTabla2Aseg5());
				tab35 = new Integer(btinicial.getTabla3Aseg5());
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(btinicial.getTabla1Aseg2(), btinicial.getTabla1Aseg2()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla2Aseg2(), btinicial.getTabla2Aseg2()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla3Aseg2(), btinicial.getTabla3Aseg2()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(btinicial.getTabla1Aseg3(), btinicial.getTabla1Aseg3()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla2Aseg3(), btinicial.getTabla2Aseg3()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla3Aseg3(), btinicial.getTabla3Aseg3()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(btinicial.getTabla1Aseg4(), btinicial.getTabla1Aseg4()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla2Aseg4(), btinicial.getTabla2Aseg4()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla3Aseg4(), btinicial.getTabla3Aseg4()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(btinicial.getTabla1Aseg5(), btinicial.getTabla1Aseg5()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla2Aseg5(), btinicial.getTabla2Aseg5()));
				tablasConv.add(crearTablaConversion(btinicial.getTabla3Aseg5(), btinicial.getTabla3Aseg5()));
				
				tablaConvTotal.add(tablasConv);

				List<BigDecimal> factores = new ArrayList<BigDecimal>();

				factor2.add(factores);
				//}

				// Se rellenan los factores1 no informados
				for (int a = factor1.size(); a < ConstantesGBT.NUM_ASEGURADOS; a++) {
					factor1.add(null);
					factor2.add(null);
				}
			}
			
			if (!umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_INCA)
					&& !umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_FACC)
					&& !umic.getDatosGenerales().getTipoSubriesgo().equals(ConstantsModulos.CTE_RIES_OTRO)) {
				kriesgo = umic.getDatosGenerales().getTipoSubriesgo();
				for (int i = 0; i < sexo.size(); i++) {
					cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i),
							umic.getDatosGenerales().getKmodalidad(), kriesgo);
					if (cte.size() > 1) {
						exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
						exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
						exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
						throw exc;
					}
					if (cte.isEmpty()) {
						cte = getTablaExpReal(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
						if (cte.size() > 1) {
							exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_MAS);
							exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_MAS);
							exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_MAS);
							throw exc;
						}
						if (cte.isEmpty()) {
							if (simulacion.booleanValue()) {
								cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i),
										umic.getDatosGenerales().getKmodalidad(), kriesgo);
								if (cte.isEmpty()) {
									cte = getTablaExpRealAnt(btcUmic, umic, sexo.get(i), edad.get(i), 0, kriesgo);
									if (cte.isEmpty()) {
										exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
										exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
										exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
										throw exc;
									}
								}
							} else {
								exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATR_VACIO);
								exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_ATR_VACIO);
								exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATR_VACIO);
								throw exc;
							}
						}
					}
					if (i == 0) {
						tab11 = cte.get(0).getCtabbase();
						tab21 = cte.get(0).getCtabbase();
						tab31 = cte.get(0).getCtabbase();
					}
					if (i == 1) {
						tab12 = cte.get(0).getCtabbase();
						tab22 = cte.get(0).getCtabbase();
						tab32 = cte.get(0).getCtabbase();
					}
					if (i == 2) {
						tab13 = cte.get(0).getCtabbase();
						tab23 = cte.get(0).getCtabbase();
						tab33 = cte.get(0).getCtabbase();
					}
					if (i == 3) {
						tab14 = cte.get(0).getCtabbase();
						tab24 = cte.get(0).getCtabbase();
						tab34 = cte.get(0).getCtabbase();
					}
					if (i == 4) {
						tab15 = cte.get(0).getCtabbase();
						tab25 = cte.get(0).getCtabbase();
						tab35 = cte.get(0).getCtabbase();
					}
					
					tablaExp.add(cte.get(0).getCtabbase());
					factor1.add(cte.get(0).getPfactor1());

					List<BigDecimal> factores = new ArrayList<BigDecimal>();
					factores.add(cte.get(0).getPfactor2a());
					factores.add(cte.get(0).getPfactor2b());
					factores.add(cte.get(0).getPfactor2c());
					factores.add(cte.get(0).getPfactor2d());
					factores.add(cte.get(0).getPfactor2e());
					factores.add(cte.get(0).getPfactor2f());
					factores.add(cte.get(0).getPfactor2g());
					factores.add(cte.get(0).getPfactor2h());
					factores.add(cte.get(0).getPfactor2i());
					factores.add(cte.get(0).getPfactor2j());

					factor2.add(factores);
					//}

					// Se rellenan los factores1 no informados
					for (int a = factor1.size(); a < ConstantesGBT.NUM_ASEGURADOS; a++) {
						factor1.add(null);
						factor2.add(null);
					}
				}
				
			}

			tablaAseg1.add(tab11);
			tablaAseg1.add(tab21);
			tablaAseg1.add(tab31);
			tablaAseg2.add(tab12);
			tablaAseg2.add(tab22);
			tablaAseg2.add(tab32);
			tablaAseg3.add(tab13);
			tablaAseg3.add(tab23);
			tablaAseg3.add(tab33);
			tablaAseg4.add(tab14);
			tablaAseg4.add(tab24);
			tablaAseg4.add(tab34);
			tablaAseg5.add(tab15);
			tablaAseg5.add(tab25);
			tablaAseg5.add(tab35);
			
			
			tablaExpList.add(tablaAseg1);
			tablaExpList.add(tablaAseg2);
			tablaExpList.add(tablaAseg3);
			tablaExpList.add(tablaAseg4);
			tablaExpList.add(tablaAseg5);

			btcUmic.setTablaBaseExp(tablaExp);
			btcUmic.setTablaBaseExpList(tablaExpList);
			informarFactores(btcUmic, factor1, factor2);

		} catch (Exception e) {

			btcUmic.setTablaBaseExp(null);

			final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
			Incidencia aviso = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
					umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
					btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
					ConstantsFunciones.CTE_COD_ERROR_I006, ArrayUtils.EMPTY_OBJECT_ARRAY);
			String vacio = "";
			if (null != exc.getInfAmpliada() && !vacio.equals(exc.getInfAmpliada())) {
				String error = exc.getInfAmpliada();
				Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(btcUmic.getBaseTec(),
						umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCcartera(), umic.getKey(),
						btcUmic.getFecCierre(), umic.getDatosGenerales().getCnegocio(), this.getNombreServicio(),
						ConstantsFunciones.CTE_COD_ERROR_I007, new Object[] { error });
				servicio.almacenarIncidencias(incidencia);
			}
			servicio.almacenarIncidencias(aviso);
			flujosProbablesBTI(umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
					umic.getDatosGenerales().getKprestacion(), umic.getDatosAdicionales().getPrestCal(),
					btcUmic.getBt());
		}

		return null;
	}

	private void flujosProbablesBTI(Integer kmodalidad, Integer kgarantia, String kprestacion, String kprestacionGen,
			String bt) {
		FlujosProbablesKey key;
		
		if(bt.equals(ConstantesSolvencia.BASE_ROSSPTE) || bt.equals(ConstantesSolvencia.BASE_ROSSPTI) || bt.equals(ConstantesSolvencia.BASE_ROSSPGA)){	
			bt = "ROSSP";
		}

		// Para las bases t�cnicas de SCR, se recuperan los flujos probables de BEL
		if (bt.equals(ConstantesSolvencia.BASE_SCRMFE) || bt.equals(ConstantesSolvencia.BASE_SCRMMI)
				|| bt.equals(ConstantesSolvencia.BASE_SCRVM) || bt.equals(ConstantesSolvencia.BASE_SCRMCF)
				|| bt.equals(ConstantesSolvencia.BASE_SCRMCI) || bt.equals(ConstantesSolvencia.BASE_SCRLFE)
				|| bt.equals(ConstantesSolvencia.BASE_SCRLMI) || bt.equals(ConstantesSolvencia.BASE_SCRINC)
				|| bt.equals(ConstantesSolvencia.BASE_SCRTIU) || bt.equals(ConstantesSolvencia.BASE_SCRTID)
				|| bt.equals(ConstantesSolvencia.BASE_SCRGTO) || bt.equals(ConstantesSolvencia.BASE_SCRAEN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAEP) || bt.equals(ConstantesSolvencia.BASE_SCRAIN)
				|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
				|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)) {

			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, ConstantesSolvencia.BASE_BEL);
		} else {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
		}

		FlujosProbables fp = flujosProbablesDao.get(key);
		if (null == fp) {
			// Para las bases t�cnicas de SCR, se recuperan los flujos probables de BEL
			if (bt.equals(ConstantesSolvencia.BASE_SCRMFE) || bt.equals(ConstantesSolvencia.BASE_SCRMMI)
					|| bt.equals(ConstantesSolvencia.BASE_SCRVM) || bt.equals(ConstantesSolvencia.BASE_SCRMCF)
					|| bt.equals(ConstantesSolvencia.BASE_SCRMCI) || bt.equals(ConstantesSolvencia.BASE_SCRLFE)
					|| bt.equals(ConstantesSolvencia.BASE_SCRLMI) || bt.equals(ConstantesSolvencia.BASE_SCRINC)
					|| bt.equals(ConstantesSolvencia.BASE_SCRTIU) || bt.equals(ConstantesSolvencia.BASE_SCRTID)
					|| bt.equals(ConstantesSolvencia.BASE_SCRGTO) || bt.equals(ConstantesSolvencia.BASE_SCRAEN)
					|| bt.equals(ConstantesSolvencia.BASE_SCRAEP) || bt.equals(ConstantesSolvencia.BASE_SCRAIN)
					|| bt.equals(ConstantesSolvencia.BASE_SCRAIP) || bt.equals(ConstantesSolvencia.BASE_SCRANM)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)) {

				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantesSolvencia.BASE_BEL);
			} else {
				key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, bt);
			}
			fp = flujosProbablesDao.get(key);
		}
		try {
			setFpClone(fp.clonar());
			INSTANCE = this;
		} catch (Exception e) {
			e.printStackTrace();
		}
		// BTI para provi
		FlujosProbablesKey keyBti = new FlujosProbablesKey();
		if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
				|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
				|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
				|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
			keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion,
					ConstantsModulos.CTE_VAL_BTI_PROY);
		}else{
			keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion,
					ConstantsModulos.CTE_VAL_BTI);
		}
		
		FlujosProbables fpBti = flujosProbablesDao.get(keyBti);
		if (null == fpBti) {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI_PROY);
			}else{
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI);
			} 
			fpBti = flujosProbablesDao.get(keyBti);
		} // Fallecimiento
		BloqueFlujosProbables fall = fp.getFall();
		if (null == fpBti.getFall().getNominal()) {
			// fall.setActualizado(fpBti.getFall().getActualizado());
			// fall.setNoanulado(fpBti.getFall().getNoanulado());
			// fall.setNominal(fpBti.getFall().getNominal());
			fall.setProbable(fpBti.getFall().getProbable());
		} else {
			// fall.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// fall.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// fall.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			fall.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		fall.setProvi(fpBti.getFall().getProvi());
		// Vida
		BloqueFlujosProbables vida = fp.getVida();
		if (null == fpBti.getVida().getNominal()) {
			// vida.setActualizado(fpBti.getVida().getActualizado());
			// vida.setNoanulado(fpBti.getVida().getNoanulado());
			// vida.setNominal(fpBti.getVida().getNominal());
			vida.setProbable(fpBti.getVida().getProbable());
		} else {
			// vida.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// vida.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// vida.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			vida.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		vida.setProvi(fpBti.getVida().getProvi());
		// Anulacion
		BloqueFlujosProbables anul = fp.getAnul();
		if (null == fpBti.getAnul().getNominal()) {
			// anul.setActualizado(fpBti.getAnul().getActualizado());
			// anul.setNoanulado(fpBti.getAnul().getNoanulado());
			// anul.setNominal(fpBti.getAnul().getNominal());
			anul.setProbable(fpBti.getAnul().getProbable());
		} else {
			// anul.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// anul.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// anul.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			anul.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		anul.setProvi(fpBti.getAnul().getProvi());
		// Comisiones
		BloqueFlujosProbables comi = fp.getComi();
		if (null == fpBti.getComi().getNominal()) {
			// comi.setActualizado(fpBti.getComi().getActualizado());
			// comi.setNoanulado(fpBti.getComi().getNoanulado());
			// comi.setNominal(fpBti.getComi().getNominal());
			comi.setProbable(fpBti.getComi().getProbable());
		} else {
			// comi.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// comi.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// comi.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			comi.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		comi.setProvi(fpBti.getComi().getProvi());
		// Gastos
		BloqueFlujosProbables gast = fp.getGast();
		if (null == fpBti.getGast().getNominal()) {
			// gast.setActualizado(fpBti.getGast().getActualizado());
			// gast.setNoanulado(fpBti.getGast().getNoanulado());
			// gast.setNominal(fpBti.getGast().getNominal());
			gast.setProbable(fpBti.getGast().getProbable());
		} else {
			// gast.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// gast.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// gast.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			gast.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		gast.setProvi(fpBti.getGast().getProvi());
		// Invalidez
		BloqueFlujosProbables inv = fp.getInva();
		if (null == fpBti.getInva().getNominal()) {
			// inv.setActualizado(fpBti.getInva().getActualizado());
			// inv.setNoanulado(fpBti.getInva().getNoanulado());
			// inv.setNominal(fpBti.getInva().getNominal());
			inv.setProbable(fpBti.getInva().getProbable());
		} else {
			// inv.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// inv.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// inv.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			inv.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		inv.setProvi(fpBti.getInva().getProvi());
		// Prima
		BloqueFlujosProbables prim = fp.getPrim();
		if (null == fpBti.getPrim().getNominal()) {
			// prim.setActualizado(fpBti.getPrim().getActualizado());
			// prim.setNoanulado(fpBti.getPrim().getNoanulado());
			// prim.setNominal(fpBti.getPrim().getNominal());
			prim.setProbable(fpBti.getPrim().getProbable());
		} else {
			// prim.setActualizado(ConstantsProcesos.CTE_LEIDO_BTI);
			// prim.setNoanulado(ConstantsProcesos.CTE_LEIDO_BTI);
			// prim.setNominal(ConstantsProcesos.CTE_LEIDO_BTI);
			prim.setProbable(ConstantsProcesos.CTE_ERROR_BT_PRB);
		}
		prim.setProvi(fpBti.getPrim().getProvi());
		// Nominal y terminal
		if (null == fpBti.getProvNominal()) {
			fp.setProvNominal(fpBti.getProvNominal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
				fp.setProvNominal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvNominal(ConstantsProcesos.CTE_LEIDO_BTI);

			}
		}
		if (null == fpBti.getProvTerminal()) {
			fp.setProvTerminal(fpBti.getProvTerminal());
		} else {
			if (bt.equals(ConstantesSolvencia.BASE_NIIF17) || bt.equals(ConstantesSolvencia.BASE_NIF17LIR)
					|| bt.equals(ConstantesSolvencia.BASE_N17LIRIN) || bt.equals(ConstantesSolvencia.BASE_NIFF17OCI)
					|| bt.equals(ConstantesSolvencia.BASE_NIIF17IF) || bt.equals(ConstantesSolvencia.BASE_N17CLIR)
					|| bt.equals(ConstantesSolvencia.BASE_BEL) || bt.equals(ConstantesSolvencia.BASE_BELCOA)
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals("BTCOA")
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO) || bt.equals(ConstantesSolvencia.BASE_NF17AEN)
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	private void informarFactores(DetalleBaseTecnica detalleBT, List<BigDecimal> factor1,
			List<List<BigDecimal>> factor2) {
		detalleBT.setFactor1(factor1.get(0));
		detalleBT.setFactor1_2(factor1.get(1));
		detalleBT.setFactor1_3(factor1.get(2));
		detalleBT.setFactor1_4(factor1.get(3));
		detalleBT.setFactor1_5(factor1.get(4));

		detalleBT.setFactor2(factor2.get(0));
		detalleBT.setFactor2_2(factor2.get(1));
		detalleBT.setFactor2_3(factor2.get(2));
		detalleBT.setFactor2_4(factor2.get(3));
		detalleBT.setFactor2_5(factor2.get(4));
	}

	public List<ConvTablasExpReal> getTablaExpReal(DetalleBaseTecnica btcUmic, Umic umic, String sexo, Integer edad,
			Integer modalidad, String kriesgo) {
		List<ConvTablasExpReal> result = null;
		ConvTablasExpRealDao dao = new ConvTablasExpRealDao();
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)) {

			result = dao.obtenerTablasExp(ConstantsModulos.CTE_BT_BEL, umic.getDatosGenerales().getCcanal(),
					umic.getDatosGenerales().getCnegocio(), kriesgo, sexo,
					umic.getDatosGenerales().getKcategoria(), edad, modalidad, umic.getDatosGenerales().getFecCierre());
		} else {
			result = dao.obtenerTablasExp(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(),
					umic.getDatosGenerales().getCnegocio(), kriesgo, sexo,
					umic.getDatosGenerales().getKcategoria(), edad, modalidad, umic.getDatosGenerales().getFecCierre());
		}
		return result;
	}

	public List<ConvTablasExpReal> getTablaExpRealAnt(DetalleBaseTecnica btcUmic, Umic umic, String sexo, Integer edad,
			Integer modalidad, String kriesgo) {
		List<ConvTablasExpReal> result = null;
		ConvTablasExpRealDao dao = new ConvTablasExpRealDao();
		if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMFE)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMMI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLFE)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRLMI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRVM)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCF)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRMCI)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRINC)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTIU)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRTID)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRGTO)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAEP)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRAIP)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_SCRANM)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIF17LIR)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17LIRIN)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIFF17OCI)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| btcUmic.getBaseTec().equals(ConstantesSolvencia.BASE_N17CLIR)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17AEN)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17GTO)
				|| btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_NF17MFE)) {

			result = dao.obtenerTablasExpAnt(ConstantsModulos.CTE_BT_BEL, umic.getDatosGenerales().getCcanal(),
					umic.getDatosGenerales().getCnegocio(), kriesgo, sexo,
					umic.getDatosGenerales().getKcategoria(), edad, modalidad, umic.getDatosGenerales().getFecCierre());
		} else {
			result = dao.obtenerTablasExpAnt(btcUmic.getBaseTec(), umic.getDatosGenerales().getCcanal(),
					umic.getDatosGenerales().getCnegocio(), kriesgo, sexo,
					umic.getDatosGenerales().getKcategoria(), edad, modalidad, umic.getDatosGenerales().getFecCierre());
		}

		return result;
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.BEL_TE;
	}
	public FlujosProbables getFpClone() {
		return fpClone;
	}

	public void setFpClone(FlujosProbables fpClone) {
		this.fpClone = fpClone;
	}
	public static void setNullInstance() {
		INSTANCE = null;
	}
	
	private TablaConversion crearTablaConversion(String tablaIni, String tablaFin) {
		TablaConversion tablaConversion = new TablaConversion();
		tablaConversion.setTablaInicio(tablaIni);
		tablaConversion.setTablaFin(tablaFin);

		return tablaConversion;
	}
}
