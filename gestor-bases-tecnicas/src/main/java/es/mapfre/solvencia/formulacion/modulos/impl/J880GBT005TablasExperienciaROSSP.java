package es.mapfre.solvencia.formulacion.modulos.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dao.impl.gbt.AdapTablaExpDao;
import es.mapfre.solvencia.dao.impl.gbt.ConversionTablasExpDao;
import es.mapfre.solvencia.dao.impl.gbt.PeriodosAdDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.gbt.AdapTablaExp;
import es.mapfre.solvencia.dominio.gbt.ConversionTablasExp;
import es.mapfre.solvencia.dominio.gbt.PeriodosAd;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaHibrida;
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
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaHibridaDao;

/**
 * Se obtendr�n las tablas resultantes que se usar�n en el proceso de c�lculo de
 * flujos en el caso de una ejecuci�n bajo la base t�cnica ROSSP. Se debe tener
 * en cuenta que los resultados obtenidos en el c�lculo en caso de no encontrar
 * una conversi�n para la base t�cnica tratada pueden desvirtuar el proceso, por
 * lo cual, en caso de error, se rechazar� la UMIC tratada para la base t�cnica
 * tratada y se cancelar� el c�lculo de los valores siguientes.
 * 
 * @author Everis
 *
 */
public class J880GBT005TablasExperienciaROSSP implements Modulo {

	GestorBasesTecnicasException exc = new GestorBasesTecnicasException();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	public static J880GBT005TablasExperienciaROSSP INSTANCE = null;
	private FlujosProbables fpClone;

	public static J880GBT005TablasExperienciaROSSP getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new J880GBT005TablasExperienciaROSSP();
		}
		return INSTANCE;
	}

	@Override
	public Object execute(Object... args) {

		final DetalleBaseTecnica btcUmic = (DetalleBaseTecnica) args[ConstantesGBT.PARAM_BTC];
		final Umic umic = (Umic) args[ConstantesGBT.PARAM_UMI_BASE_TEC];
		setFpClone(null);
		// Base tecnica inicial de la umic y Datos generales
		final BaseTecnicaInicial bti = umic.getBti();
		final DatosGenerales datosGenerales = umic.getDatosGenerales();
		try {
			// Calculo de base tecnica
			
			exc.setGeneradorError(getNombreServicio());
			exc.setTipoError(ConstantesErrores.CTE_ERROR);

			/**
			 * INCIDENCIA H FASE VII
			 */
			if ((bti.getTabla1Aseg1() == null || bti.getTabla1Aseg1().equals("")
					|| bti.getTabla1Aseg1().equals("00000"))
					&& (((!(ConstantesGBT.KGARANTIA == umic.getDatosGenerales().getKgarantia())
							&& umic.getDatosGenerales().getKramo().equals(ConstantesGBT.KRAMO)))
							|| ((ConstantesGBT.KGARANTIA == umic.getDatosGenerales().getKgarantia())
									&& !umic.getDatosGenerales().getKramo().equals(ConstantesGBT.KRAMO))
							|| (!(ConstantesGBT.KGARANTIA == umic.getDatosGenerales().getKgarantia())
									&& !umic.getDatosGenerales().getKramo().equals(ConstantesGBT.KRAMO)))) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_TABASEG);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_TABASEG);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_TABASEG);
				throw exc;
			}
			if (btcUmic.getBaseTec() == null || btcUmic.getBaseTec().equals("")) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_BT);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_BT);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_BT);
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
			if (datosGenerales.getKgarantia() == null || datosGenerales.getKgarantia() == 0) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_GAR);
				exc.setTextoError(ConstantesErrores.CTE_DESC_ERROR_GAR);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_GAR);
				throw exc;
			}

			List<TablaConversion> tablasConv = new ArrayList<TablaConversion>();
			List<List<TablaConversion>> tablaConvTotal = new ArrayList<List<TablaConversion>>();
			
			if (btcUmic.getBaseTec().equals(ConstantsModulos.CTE_VAL_ROSSPTE)) {
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
				
				tablasConv.add(crearTablaConversion(bti.getTabla1Aseg1(), bti.getTabla1Aseg1()));
				tablasConv.add(crearTablaConversion(bti.getTabla2Aseg1(), bti.getTabla2Aseg1()));
				tablasConv.add(crearTablaConversion(bti.getTabla3Aseg1(), bti.getTabla3Aseg1()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(bti.getTabla1Aseg2(), bti.getTabla1Aseg2()));
				tablasConv.add(crearTablaConversion(bti.getTabla2Aseg2(), bti.getTabla2Aseg2()));
				tablasConv.add(crearTablaConversion(bti.getTabla3Aseg2(), bti.getTabla3Aseg2()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(bti.getTabla1Aseg3(), bti.getTabla1Aseg3()));
				tablasConv.add(crearTablaConversion(bti.getTabla2Aseg3(), bti.getTabla2Aseg3()));
				tablasConv.add(crearTablaConversion(bti.getTabla3Aseg3(), bti.getTabla3Aseg3()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(bti.getTabla1Aseg4(), bti.getTabla1Aseg4()));
				tablasConv.add(crearTablaConversion(bti.getTabla2Aseg4(), bti.getTabla2Aseg4()));
				tablasConv.add(crearTablaConversion(bti.getTabla3Aseg4(), bti.getTabla3Aseg4()));
				tablaConvTotal.add(tablasConv);
				
				tablasConv = new ArrayList<TablaConversion>();
				tablasConv.add(crearTablaConversion(bti.getTabla1Aseg5(), bti.getTabla1Aseg5()));
				tablasConv.add(crearTablaConversion(bti.getTabla2Aseg5(), bti.getTabla2Aseg5()));
				tablasConv.add(crearTablaConversion(bti.getTabla3Aseg5(), bti.getTabla3Aseg5()));
				
				tablaConvTotal.add(tablasConv);

				// Mover las tablas a la estructura de salida de cálculo de flujos y GBT
				incluirTablasConversion(btcUmic, tablaConvTotal);
				btcUmic.setTablasConversionAsegurado(tablaConvTotal);
				
				String metodo = obtenerMetodo(btcUmic, umic);

				if (metodo != null && !metodo.equals("")) {
					btcUmic.setMetodoPtRossp(metodo);
					btcUmic.setPerTransRossp(true);
					BigDecimal factor = getFactorCalculado(metodo, umic);
					factor = factor.setScale(8, BigDecimal.ROUND_HALF_UP);
					btcUmic.setFactorInterpolExperienciaRossp(factor);
				} else {
					// GBT v1 Inicio - Cambios pedidos
					btcUmic.setMetodoPtRossp(null);
					// GBT v1 Final - Cambios pedidos
					btcUmic.setPerTransRossp(false);
					btcUmic.setFactorInterpolExperienciaRossp(new BigDecimal(0));
				}
			
			}else{

				/**
				 * INCIDENCIA H FASE VII
				 */
				// Se valida que las tablas sean distinto de vacio o cualquier numero de ceros
				int tablaInt;
				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla1Aseg1().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				// Asegurado 1
				if (tablaInt != 0) {
					List<String> tablas11 = getTablasConversion(btcUmic, umic.getBti().getTabla1Aseg1(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(),umic.getAsegurados().getCsexAseg1(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas11.get(0), tablas11.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla2Aseg1().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}
				// if(umic.getBti().getTabla2Aseg1()!=null &&
				// !umic.getBti().getTabla2Aseg1().equals("") &&
				// !umic.getBti().getTabla2Aseg1().equals("00000")){
				if (tablaInt != 0) {
					List<String> tablas21 = getTablasConversion(btcUmic, umic.getBti().getTabla2Aseg1(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg1(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas21.get(0), tablas21.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla3Aseg1().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas31 = getTablasConversion(btcUmic, umic.getBti().getTabla3Aseg1(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg1(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas31.get(0), tablas31.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				tablaConvTotal.add(tablasConv);

				tablasConv = new ArrayList<TablaConversion>();

				// Asegurado 2
				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla1Aseg2().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas12 = getTablasConversion(btcUmic, umic.getBti().getTabla1Aseg2(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg2(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas12.get(0), tablas12.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla2Aseg2().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas22 = getTablasConversion(btcUmic, umic.getBti().getTabla2Aseg2(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg2(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas22.get(0), tablas22.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla3Aseg2().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas32 = getTablasConversion(btcUmic, umic.getBti().getTabla3Aseg2(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg2(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas32.get(0), tablas32.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				tablaConvTotal.add(tablasConv);
				tablasConv = new ArrayList<TablaConversion>();

				// Asegurado 3
				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla1Aseg3().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas13 = getTablasConversion(btcUmic, umic.getBti().getTabla1Aseg3(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg3(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas13.get(0), tablas13.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla2Aseg3().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas23 = getTablasConversion(btcUmic, umic.getBti().getTabla2Aseg3(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg3(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas23.get(0), tablas23.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla3Aseg3().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas33 = getTablasConversion(btcUmic, umic.getBti().getTabla3Aseg3(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg3(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas33.get(0), tablas33.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				tablaConvTotal.add(tablasConv);
				tablasConv = new ArrayList<TablaConversion>();

				// Asegurado 4
				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla1Aseg4().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas14 = getTablasConversion(btcUmic, umic.getBti().getTabla1Aseg4(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg4(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas14.get(0), tablas14.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla2Aseg4().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas24 = getTablasConversion(btcUmic, umic.getBti().getTabla2Aseg4(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg4(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas24.get(0), tablas24.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla3Aseg4().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas34 = getTablasConversion(btcUmic, umic.getBti().getTabla3Aseg4(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg4(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas34.get(0), tablas34.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				tablaConvTotal.add(tablasConv);
				tablasConv = new ArrayList<TablaConversion>();

				// Asegurado 5
				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla1Aseg5().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas15 = getTablasConversion(btcUmic, umic.getBti().getTabla1Aseg5(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg5(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas15.get(0), tablas15.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla2Aseg5().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas25 = getTablasConversion(btcUmic, umic.getBti().getTabla2Aseg5(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg5(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas25.get(0), tablas25.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				try {
					tablaInt = Integer.parseInt(umic.getBti().getTabla3Aseg5().trim());
				} catch (Exception e) {
					tablaInt = 0;
				}

				if (tablaInt != 0) {
					List<String> tablas35 = getTablasConversion(btcUmic, umic.getBti().getTabla3Aseg5(),
						datosGenerales.getKmodalidad(), datosGenerales.getKgarantia(),
						umic.getDatosGenerales().getFecCierre(), datosGenerales.getCcanal(), datosGenerales.getCnegocio(),
						datosGenerales.getKramo(), umic.getAsegurados().getCsexAseg5(), datosGenerales.getTipoSubriesgo());

					tablasConv.add(crearTablaConversion(tablas35.get(0), tablas35.get(1)));
				} else {
					tablasConv.add(crearTablaConversion(null, null));
				}

				tablaConvTotal.add(tablasConv);

				// Mover las tablas a la estructura de salida de c�lculo de flujos y GBT
				incluirTablasConversion(btcUmic, tablaConvTotal);
				btcUmic.setTablasConversionAsegurado(tablaConvTotal);

				String metodo = obtenerMetodo(btcUmic, umic);

				if (metodo != null && !metodo.equals("")) {
					btcUmic.setMetodoPtRossp(metodo);
					btcUmic.setPerTransRossp(true);
					BigDecimal factor = getFactorCalculado(metodo, umic);
					factor = factor.setScale(8, BigDecimal.ROUND_HALF_UP);
					btcUmic.setFactorInterpolExperienciaRossp(factor);
				} else {
					// GBT v1 Inicio - Cambios pedidos
					btcUmic.setMetodoPtRossp(null);
					// GBT v1 Final - Cambios pedidos
					btcUmic.setPerTransRossp(false);
					btcUmic.setFactorInterpolExperienciaRossp(new BigDecimal(0));
				}
			}

		} catch (Exception e) {
			//btcUmic.setTablasConversionAsegurado(null);
			//btcUmic.setMetodoPtRossp(null);
			//btcUmic.setPerTransRossp(null);
			//btcUmic.setFactorInterpolExperienciaRossp(null);
			
			//CONFIGURACION BTI
			List<TablaConversion> tablasConv = new ArrayList<TablaConversion>();
			List<List<TablaConversion>> tablaConvTotal = new ArrayList<List<TablaConversion>>();
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
			
			tablasConv.add(crearTablaConversion(bti.getTabla1Aseg1(), bti.getTabla1Aseg1()));
			tablasConv.add(crearTablaConversion(bti.getTabla2Aseg1(), bti.getTabla2Aseg1()));
			tablasConv.add(crearTablaConversion(bti.getTabla3Aseg1(), bti.getTabla3Aseg1()));
			tablaConvTotal.add(tablasConv);
			
			tablasConv = new ArrayList<TablaConversion>();
			tablasConv.add(crearTablaConversion(bti.getTabla1Aseg2(), bti.getTabla1Aseg2()));
			tablasConv.add(crearTablaConversion(bti.getTabla2Aseg2(), bti.getTabla2Aseg2()));
			tablasConv.add(crearTablaConversion(bti.getTabla3Aseg2(), bti.getTabla3Aseg2()));
			tablaConvTotal.add(tablasConv);
			
			tablasConv = new ArrayList<TablaConversion>();
			tablasConv.add(crearTablaConversion(bti.getTabla1Aseg3(), bti.getTabla1Aseg3()));
			tablasConv.add(crearTablaConversion(bti.getTabla2Aseg3(), bti.getTabla2Aseg3()));
			tablasConv.add(crearTablaConversion(bti.getTabla3Aseg3(), bti.getTabla3Aseg3()));
			tablaConvTotal.add(tablasConv);
			
			tablasConv = new ArrayList<TablaConversion>();
			tablasConv.add(crearTablaConversion(bti.getTabla1Aseg4(), bti.getTabla1Aseg4()));
			tablasConv.add(crearTablaConversion(bti.getTabla2Aseg4(), bti.getTabla2Aseg4()));
			tablasConv.add(crearTablaConversion(bti.getTabla3Aseg4(), bti.getTabla3Aseg4()));
			tablaConvTotal.add(tablasConv);
			
			tablasConv = new ArrayList<TablaConversion>();
			tablasConv.add(crearTablaConversion(bti.getTabla1Aseg5(), bti.getTabla1Aseg5()));
			tablasConv.add(crearTablaConversion(bti.getTabla2Aseg5(), bti.getTabla2Aseg5()));
			tablasConv.add(crearTablaConversion(bti.getTabla3Aseg5(), bti.getTabla3Aseg5()));
			
			tablaConvTotal.add(tablasConv);

			// Mover las tablas a la estructura de salida de cálculo de flujos y GBT
			//incluirTablasConversion(btcUmic, tablaConvTotal);
			btcUmic.setTablasConversionAsegurado(tablaConvTotal);
			
			String metodo = obtenerMetodo(btcUmic, umic);

			if (metodo != null && !metodo.equals("")) {
				btcUmic.setMetodoPtRossp(metodo);
				btcUmic.setPerTransRossp(true);
				BigDecimal factor = getFactorCalculado(metodo, umic);
				factor = factor.setScale(8, BigDecimal.ROUND_HALF_UP);
				btcUmic.setFactorInterpolExperienciaRossp(factor);
			} else {
				// GBT v1 Inicio - Cambios pedidos
				btcUmic.setMetodoPtRossp(null);
				// GBT v1 Final - Cambios pedidos
				btcUmic.setPerTransRossp(false);
				btcUmic.setFactorInterpolExperienciaRossp(new BigDecimal(0));
			}
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
		
		if(bt.equals(ConstantesSolvencia.BASE_ROSSPTE) || bt.equals(ConstantesSolvencia.BASE_ROSSPTI) || bt.equals(ConstantesSolvencia.BASE_ROSSPGA)){	
			bt = "ROSSP";
		}
		
		FlujosProbablesKey key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacion, bt);
		FlujosProbables fp = flujosProbablesDao.get(key);
		if (null == fp) {
			key = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, bt);
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
				|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
				|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_NF17AEN) || bt.equals(ConstantesSolvencia.BASE_NF17MFE)
					|| bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI_PROY);

			}else{
				keyBti = new FlujosProbablesKey(kmodalidad, kgarantia, kprestacionGen, ConstantsModulos.CTE_VAL_BTI);
			}
			fpBti = flujosProbablesDao.get(keyBti);
		}
		// Fallecimiento
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
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
					|| bt.equals(ConstantesSolvencia.BASE_ROSSPCSM) || bt.equals(ConstantesSolvencia.BASE_NF17AEN) 
					|| bt.equals(ConstantesSolvencia.BASE_NF17MFE)  || bt.equals(ConstantesSolvencia.BASE_NF17GTO)){
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTIPR);
			}else{
				fp.setProvTerminal(ConstantsProcesos.CTE_LEIDO_BTI);
			}
		}
	}

	public List<String> getTablasConversion(DetalleBaseTecnica btcUmic, String tabla, Integer mod, Integer gar,
			Timestamp fc, Integer compania, String negocio, String ramo, String sexo, String riesgoactuarial) {	
		List<String> result = new ArrayList<String>();
		String bt = btcUmic.getBaseTec();
		
		TablaHibridaDao daoTH = new TablaHibridaDao();
		List<TablaHibrida> htr = daoTH.getValues(fc, compania, negocio, bt, ramo, mod, gar, riesgoactuarial, sexo, Integer.parseInt(tabla));
		if (htr.isEmpty()) {
			if (bt.equals("ROSSP") || bt.equals("BTCOA") || bt.equals("ROSSPCSM") || bt.equals("ROSSPTI") || bt.equals("ROSSPGA") || bt.equals("ROSSEAR") || bt.equals("BTCOATF")) {
				if (bt.equals(ConstantsModulos.CTE_VAL_ROSSPTI) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPGA)) {
					bt = ConstantsModulos.CTE_VAL_ROSSP;
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValuesGarantia(fc,negocio, bt, ramo, mod, gar, sexo, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValuesSinGarantia(fc, negocio, bt, ramo, mod, sexo, Integer.parseInt(tabla));
				}
				
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, negocio, bt, ramo, mod, gar, null, sexo, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, negocio, bt, ramo, mod, null, null, sexo, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, negocio, bt, ramo, null, null, null, null, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, null, bt, null, mod, null, null, null, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, null, bt, ramo, null, null, null, null, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, compania, null, bt, null, null, null, null, null, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, negocio, bt, null, null, null, null, null, Integer.parseInt(tabla));
				}
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, null, null, bt, null, null, null, null, null, Integer.parseInt(tabla));
				}
				
			}else if(bt.equals("NIIF17")) {
			
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, compania, negocio, bt, ramo, mod, null, riesgoactuarial, sexo, Integer.parseInt(tabla));
				}
				
				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, compania, negocio, bt, ramo, null, null, riesgoactuarial, sexo, Integer.parseInt(tabla));
				}

				if(null == htr || htr.isEmpty()){
					htr = daoTH.getValues(fc, compania, negocio, bt, ramo, null, null, riesgoactuarial, sexo, null);
				}
				
			}
		}
		
//		if (htr.isEmpty()) {
//			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTE_VACIO);
//			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_CTE_VACIO);
//			exc.setInfAmpliada(
//					ConstantesErrores.CTE_DESL_ERROR_CTE_VACIO + " Tabla: " + Integer.parseInt(tabla));
//			throw exc;
//		}
		
		if (!htr.isEmpty() && htr.get(0).getExcepcion().equals("N")) {
			result.add(htr.get(0).getTmbtcalc().toString());
			result.add(htr.get(0).getTmbtcalc().toString());
			return result;
		}
		
		if (bt.equals(ConstantsModulos.CTE_VAL_ROSSPCSM) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPTI) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPGA)) {
			bt = ConstantsModulos.CTE_VAL_ROSSP;
		}
		
		ConversionTablasExpDao dao = new ConversionTablasExpDao();
		List<ConversionTablasExp> cte = dao.obtenerConversionTablasExp(bt, Integer.parseInt(tabla), mod, gar, fc);
		if (cte.isEmpty()) {
			cte = dao.obtenerConversionTablasExp(bt, Integer.parseInt(tabla), mod, 0, fc);
			if (cte.isEmpty()) {
				cte = dao.obtenerConversionTablasExp(bt, Integer.parseInt(tabla), 0, 0, fc);
				if (cte.isEmpty()) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTE_VACIO);
					exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_CTE_VACIO);
					exc.setInfAmpliada(
							ConstantesErrores.CTE_DESL_ERROR_CTE_VACIO + " Tabla: " + Integer.parseInt(tabla));
					throw exc;
				} else if (cte.size() > 1) {
					exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTE_MAS);
					exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_CTE_MAS);
					exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CTE_MAS + " Tabla: " + Integer.parseInt(tabla));
					throw exc;
				}
			} else if (cte.size() > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTE_MAS);
				exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_CTE_MAS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CTE_MAS + " Tabla: " + Integer.parseInt(tabla));
				throw exc;
			}
		} else if (cte.size() > 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_CTE_MAS);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_CTE_MAS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_CTE_MAS + " Tabla: " + Integer.parseInt(tabla));
			throw exc;
		}

		Integer tablaIni = cte.get(0).getCtablaini();
		if (cte.get(0).getCtablafin() == null || cte.get(0).getCtablafin() == 0
				|| cte.get(0).getCtablafin().equals("")) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_TABFIN);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_TABFIN);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_TABFIN);
			throw exc;
		}
		Integer tablaFin = cte.get(0).getCtablafin();
		result.add(tablaIni.toString());
		result.add(tablaFin.toString());
		return result;
	}

	public String obtenerMetodo(DetalleBaseTecnica btcUmic, Umic umic) {
		String result = null;
		String bt = btcUmic.getBaseTec();
		if (bt.equals(ConstantsModulos.CTE_VAL_ROSSPCSM) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPGA) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPTI) || bt.equals(ConstantsModulos.CTE_VAL_ROSSPTE) ) {
			bt = ConstantsModulos.CTE_VAL_ROSSP;
		}
		if (bt.equals(ConstantsModulos.CTE_VAL_BTCOATF)) {
			bt = ConstantsModulos.CTE_VAL_BTCOA;
		}
		
		Integer mod = umic.getDatosGenerales().getKmodalidad();
		Timestamp fc = umic.getDatosGenerales().getFecCierre();
		AdapTablaExpDao dao = new AdapTablaExpDao();
		List<AdapTablaExp> ate = dao.obtenerAdapTablasExp(bt, mod, fc);
		if (ate.size() > 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATE_MAS);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_ATE_MAS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATE_MAS);
			throw exc;
		} else if (!ate.isEmpty()) {
			result = ate.get(0).getKmadaptac();
		} else {
			ate = dao.obtenerAdapTablasExp(bt, 0, fc);
			if (!ate.isEmpty()) {
				result = ate.get(0).getKmadaptac();
			} else if (ate.size() > 1) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_ATE_MAS);
				exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_ATE_MAS);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_ATE_MAS);
				throw exc;
			}
		}
		return result;
	}

	public BigDecimal getFactorCalculado(String metodo, Umic umic) {
		BigDecimal result = null;
		Timestamp fc = umic.getDatosGenerales().getFecCierre();
		PeriodosAdDao dao = new PeriodosAdDao();
		List<PeriodosAd> mad = dao.obtenerPeriodosAd(metodo, fc);

		// Si no se han encontrado periodos para la fecha de cierre, se obtienen los
		// periodos de metodos anteriores, si existe algun periodo anterior al
		// consultado, se devuelve un 0
		if (mad.isEmpty()) {
			mad = dao.obtenerPeriodosAd(metodo);
			if (mad.isEmpty()) {
				exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MAD_VACIO);
				exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_MAD_VACIO);
				exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MAD_VACIO + " Metodo: " + metodo);
				throw exc;
			} else {
				return new BigDecimal("0");
			}
		}

		if (mad.isEmpty()) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MAD_VACIO);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_MAD_VACIO);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MAD_VACIO + " Metodo: " + metodo);
			throw exc;
		} else if (mad.size() > 1) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_MAD_MAS);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_MAD_MAS);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_MAD_MAS + " Metodo: " + metodo);
			throw exc;
		}
		double pen = new Double(mad.get(0).getNpendiente());
		double div = new Double(mad.get(0).getNdivisor());
		if (div == 0) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_PERTOT);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_PERTOT);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_PERTOT);
			throw exc;
		}
		if (pen > div) {
			exc.setCodigoRetorno(ConstantesErrores.CTE_COD_ERROR_FACT_INT);
			exc.setTipoError(ConstantesErrores.CTE_DESC_ERROR_FACT_INT);
			exc.setInfAmpliada(ConstantesErrores.CTE_DESL_ERROR_FACT_INT);
			throw exc;
		}
		result = new BigDecimal(pen / div);
		return result;
	}

	@Override
	public String getNombreServicio() {
		return ConstantesModulosGBT.ROSSP_TE;
	}

	private TablaConversion crearTablaConversion(String tablaIni, String tablaFin) {
		TablaConversion tablaConversion = new TablaConversion();
		tablaConversion.setTablaInicio(tablaIni);
		tablaConversion.setTablaFin(tablaFin);

		return tablaConversion;
	}

	private void incluirTablasConversion(DetalleBaseTecnica detalleBT, List<List<TablaConversion>> tablaConversion) {
		detalleBT.setTablacalc1aseg1(tablaConversion.get(0).get(0).getTablaFin());
		detalleBT.setTablacalc2aseg1(tablaConversion.get(0).get(1).getTablaFin());
		detalleBT.setTablacalc3aseg1(tablaConversion.get(0).get(2).getTablaFin());
		detalleBT.setTablacalc1aseg2(tablaConversion.get(1).get(0).getTablaFin());
		detalleBT.setTablacalc2aseg2(tablaConversion.get(1).get(1).getTablaFin());
		detalleBT.setTablacalc3aseg2(tablaConversion.get(1).get(2).getTablaFin());
		detalleBT.setTablacalc1aseg3(tablaConversion.get(2).get(0).getTablaFin());
		detalleBT.setTablacalc2aseg3(tablaConversion.get(2).get(1).getTablaFin());
		detalleBT.setTablacalc3aseg3(tablaConversion.get(2).get(2).getTablaFin());
		detalleBT.setTablacalc1aseg4(tablaConversion.get(3).get(0).getTablaFin());
		detalleBT.setTablacalc2aseg4(tablaConversion.get(3).get(1).getTablaFin());
		detalleBT.setTablacalc3aseg4(tablaConversion.get(3).get(2).getTablaFin());
		detalleBT.setTablacalc1aseg5(tablaConversion.get(4).get(0).getTablaFin());
		detalleBT.setTablacalc2aseg5(tablaConversion.get(4).get(1).getTablaFin());
		detalleBT.setTablacalc3aseg5(tablaConversion.get(4).get(2).getTablaFin());
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
}
