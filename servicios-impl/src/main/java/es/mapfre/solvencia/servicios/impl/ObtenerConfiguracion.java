//MODIFICACION: TAR00433819
//FECHA: 24/09/2018
//DESCRIP: SE QUITA LA VALIDACION DE QUE PUEDA HABER ENCONTRADO MAS DE UN DATO.

package es.mapfre.solvencia.servicios.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.entregables.FlujCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInfSCRKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujSuscriKey;
import es.mapfre.solvencia.coherence.keys.entregables.FlujTcasKey;
import es.mapfre.solvencia.coherence.keys.entregables.ProvCoaSegKey;
import es.mapfre.solvencia.coherence.keys.entregables.TotPMaCoaKey;
import es.mapfre.solvencia.coherence.keys.maestro.CuadrosAmortizacionKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.OpcionesGeneracionKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dao.impl.PropertiesDao;
import es.mapfre.solvencia.dao.impl.conversionesBel.GastosRealesDao;
import es.mapfre.solvencia.dao.impl.conversionesBel.TablasExperienciaRealesDao;
import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresAnulacionDao;
import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresAnulacionMensualesDao;
import es.mapfre.solvencia.dao.impl.conversionesBel.ValoresCurvaTipoDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujCoaSegDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujInfSCRDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMaCoaDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujPMdCoaDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujSuscriDao;
import es.mapfre.solvencia.dao.impl.entregables.FlujTcasDao;
import es.mapfre.solvencia.dao.impl.entregables.ProvCoaSegDao;
import es.mapfre.solvencia.dao.impl.entregables.TotPMaCoaDao;
import es.mapfre.solvencia.dao.impl.maestro.CuadrosAmortizacionDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosEspecificosDao;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dao.impl.maestro.Tabla2000Dao;
import es.mapfre.solvencia.dao.impl.maestro.ValoresLiquidativosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.CabeceraTablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.IPCGeneralFuturoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.LimitesCapitalDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.OpcionesGeneracionDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.Tab35050Dao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.Tab923Dao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TabOGADao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaHibridaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ValoresConstantesRescatesDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TotalesFlujosDao;
import es.mapfre.solvencia.dao.impl.scr.GastosRealesNF17GTODao;
import es.mapfre.solvencia.dao.impl.scr.GastosRealesSCRGTODao;
import es.mapfre.solvencia.dao.impl.scr.IPCGeneralFuturoSCRGTODao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesNF17MFEDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRLFEDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRLMIDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRMCFDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRMCIDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRMFEDao;
import es.mapfre.solvencia.dao.impl.scr.TablasExperienciaRealesSCRMMIDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresAnulacionNF17AENDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresAnulacionSCRAENDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresAnulacionSCRAEPDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresAnulacionSCRAINDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresAnulacionSCRAIPDao;
import es.mapfre.solvencia.dao.impl.scr.ValoresEstresDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujInfSCR;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.formulacion.CriterioFechas;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Tabla2000;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TabOGA;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaHibrida;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class ObtenerConfiguracion implements IObtenerConfiguracion {

	private static final String E01 = "01";
	private static final String AH = "AH";
	private static final String AD = "AD";
	private static final String AC = "AC";
	private static final String AN = "AN";
	private static final String AP = "AP";
	private static final String D7 = "D7";
	private static final String DC = "DC";
	private static final String DG = "DG";
	private static final String DH = "DH";
	private static final String DI = "DI";								   

	private static Logger log = LoggerFactory
			.getLogger(ObtenerConfiguracion.class);

	private CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
	private DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
	private FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
	private IPCGeneralFuturoDao ipcGeneralFuturoDao = new IPCGeneralFuturoDao();
	private GastosRealesDao gastosRealesDao = new GastosRealesDao();
	private LimitesCapitalDao limitesCapitalDao = new LimitesCapitalDao();
	private PropertiesDao propertiesDao = new PropertiesDao();
	private TablaExperienciaDao tablaExperienciaDao = new TablaExperienciaDao();
	private ValoresAnulacionMensualesDao valoresAnulacionMensualesDao = new ValoresAnulacionMensualesDao();
	private ValoresAnulacionDao valoresAnulacionDao = new ValoresAnulacionDao();
	private ValoresCurvaTipoDao valoresCurvaTipoDao = new ValoresCurvaTipoDao();
	private ValoresConstantesRescatesDao valoresCteRescateDao = new ValoresConstantesRescatesDao();
	private OpcionesGeneracionDao opcionesGenDao = new OpcionesGeneracionDao();
	private TablasExperienciaRealesDao tablasExperienciaRealesDao = new TablasExperienciaRealesDao();
	private ValoresLiquidativosDao valoresLiquidativosDao = new ValoresLiquidativosDao();
	private DatosEspecificosDao datosEspecificosDao = new DatosEspecificosDao();
	private CuadrosAmortizacionDao cuadrosAmortizacionDao = new CuadrosAmortizacionDao();
	private TotalesFlujosDao totalesFlujosdao = new TotalesFlujosDao();
	private Tabla2000Dao tabla2000Dao = new Tabla2000Dao();
	private ValoresEstresDao valoresEstresDao = new ValoresEstresDao();
	private IPCGeneralFuturoSCRGTODao ipcGeneralFuturoSCRGTODao = new IPCGeneralFuturoSCRGTODao();
	private GastosRealesSCRGTODao gastosRealesSCRGTODao = new GastosRealesSCRGTODao();
	private TablasExperienciaRealesSCRLFEDao tablasExperienciaRealesSCRLFEDao = new TablasExperienciaRealesSCRLFEDao();
	private TablasExperienciaRealesSCRLMIDao tablasExperienciaRealesSCRLMIDao = new TablasExperienciaRealesSCRLMIDao();
	private TablasExperienciaRealesSCRMFEDao tablasExperienciaRealesSCRMFEDao = new TablasExperienciaRealesSCRMFEDao();
	private TablasExperienciaRealesSCRMMIDao tablasExperienciaRealesSCRMMIDao = new TablasExperienciaRealesSCRMMIDao();
	private TablasExperienciaRealesSCRMCFDao tablasExperienciaRealesSCRMCFDao = new TablasExperienciaRealesSCRMCFDao();
	private TablasExperienciaRealesSCRMCIDao tablasExperienciaRealesSCRMCIDao = new TablasExperienciaRealesSCRMCIDao();
	private ValoresAnulacionSCRAEPDao valoresAnulacionSCRAEPDao = new ValoresAnulacionSCRAEPDao();
	private ValoresAnulacionSCRAENDao valoresAnulacionSCRAENDao = new ValoresAnulacionSCRAENDao();
	private ValoresAnulacionSCRAIPDao valoresAnulacionSCRAIPDao = new ValoresAnulacionSCRAIPDao();
	private ValoresAnulacionSCRAINDao valoresAnulacionSCRAINDao = new ValoresAnulacionSCRAINDao();
	private Tab923Dao tab923Dao = new Tab923Dao();
	private TablaHibridaDao tablaHibridaDao = new TablaHibridaDao();
	private FlujCoaSegDao flujCoaSegDao = new FlujCoaSegDao();
	private FlujTcasDao flujTcasDao = new FlujTcasDao();
	private ProvCoaSegDao provCoaSegDao = new ProvCoaSegDao();
	private TotPMaCoaDao totPMaCoaDao = new TotPMaCoaDao();
	private FlujPMaCoaDao flujPMaCoaDao = new FlujPMaCoaDao();
	private FlujPMdCoaDao flujPMdCoaDao = new FlujPMdCoaDao();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	private Tab35050Dao tab35050Dao = new Tab35050Dao();
	private FlujInfSCRDao flujInfSCRDao = new FlujInfSCRDao();
	private FlujSuscriDao flujSuscriDao = new FlujSuscriDao();
	private TabOGADao tabOGADao = new TabOGADao();
	private ValoresAnulacionNF17AENDao valoresAnulacionNF17AENDao = new ValoresAnulacionNF17AENDao();
	private TablasExperienciaRealesNF17MFEDao tablasExperienciaRealesNF17MFEDao = new TablasExperienciaRealesNF17MFEDao();
	private GastosRealesNF17GTODao gastosRealesNF17GTODao = new GastosRealesNF17GTODao();
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarEdadMax(java.sql.Timestamp, java.lang.Integer, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal)
	 */
	
	public Integer recuperarEdadMaxOld(Timestamp feccierre, Integer ktabla,
			String wkanacimiento, BigDecimal bigDecimal,
			BigDecimal bigDecimal2, BigDecimal bigDecimal3) {

		TablaExperiencia tablaExperiencia = recuperarValorDeMortalidad(feccierre, ktabla, wkanacimiento, bigDecimal2, bigDecimal3);

		if (tablaExperiencia != null && tablaExperiencia.getGvalor() != null) {
			int i;
			for(i = tablaExperiencia.getGvalor().size(); i > 0 && (tablaExperiencia.getGvalor().get(i-1).signum() == 0); i--);
			return i;

		} else {
			return null;
		}
	}
	
	@Override
	public Integer recuperarEdadMax(Umic umic, DetalleBaseTecnica btc, String wkanacimiento, 
			String sexAseg, OrdenAsegurado ordenAsegurado){
		
		List<BigDecimal> tablaExperiencia = recuperarValoresExperiencia(umic, btc, wkanacimiento, sexAseg, null, ConstantesSolvencia.CTE_TABMORT_L, ordenAsegurado);
		if (tablaExperiencia != null){
			int i;
			for(i = tablaExperiencia.size(); i > 0 && (tablaExperiencia.get(i-1).signum() == 0); i--);
			return i;
		} else {
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarGastosReales(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.sql.Timestamp)
	 */
	@Override
	public GastosReales recuperarGastosReales(String ktipobt, Integer ccanal,
			String cnegocio, String kramo, Integer kmodalidad,
			Timestamp fecCierre, String matching) {

		List<GastosReales> gastos;
		
		if (ktipobt.equals(ConstantesSolvencia.BASE_SCRGTO)){
			gastos = gastosRealesSCRGTODao.obtenerGastosReales(ccanal,
					cnegocio, fecCierre, kmodalidad, kramo, ktipobt, matching);
		}  else if (ktipobt.equals(ConstantesSolvencia.BASE_NF17GTO)){
			gastos = gastosRealesNF17GTODao.obtenerGastosReales(ccanal,
					cnegocio, fecCierre, kmodalidad, kramo, ktipobt, matching);
		} else if (ktipobt.equals(ConstantesSolvencia.BASE_SCRTIU) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRTID) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRINC) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRMFE) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRMMI) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRMCF) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRMCI) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRLFE) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRLMI) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRVM)  ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRAEN) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRAEP) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRAIN) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRAIP) ||
				ktipobt.equals(ConstantesSolvencia.BASE_SCRANM)){
			gastos = gastosRealesDao.obtenerGastosReales(ccanal,
					cnegocio, fecCierre, kmodalidad, kramo, ConstantesSolvencia.BASE_BEL, matching);
		}else if(ktipobt.equals(ConstantesSolvencia.BASE_NIIF17) ||
				ktipobt.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				ktipobt.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				ktipobt.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				ktipobt.equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				ktipobt.equals(ConstantesSolvencia.BASE_N17CLIR) ||
				ktipobt.equals(ConstantesSolvencia.BASE_NF17AEN) ||
				ktipobt.equals(ConstantesSolvencia.BASE_NF17MFE)){
			gastos = gastosRealesDao.obtenerGastosReales(ccanal,
					cnegocio, fecCierre, kmodalidad, kramo, ConstantesSolvencia.BASE_BEL, matching);		 
		}else {
			gastos = gastosRealesDao.obtenerGastosReales(ccanal,
					cnegocio, fecCierre, kmodalidad, kramo, ktipobt, matching);
		}
		
		GastosReales gasto = null;

		if (gastos.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.GASTOS_REALES });
		} else if (!gastos.isEmpty()) {
			gasto = gastos.get(0);
		}

		return gasto;

	}

	// ipc0Finicio <= fecCierre <= ipc0Ffin
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarIpcFuturo(java.sql.Timestamp)
	 */
	@Override
	public BigDecimal recuperarIpcFuturo(Timestamp fecCierre, String bt) {
		
		List<IPCGeneralFuturo> ipcs = null;
		
		if (bt.equals(ConstantesSolvencia.BASE_SCRGTO)){
			ipcs = ipcGeneralFuturoSCRGTODao.getValues(fecCierre);
		} else {
			ipcs = ipcGeneralFuturoDao.getValues(fecCierre);
		}
		
		IPCGeneralFuturo ipc = null;

		if (ipcs.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.IPC_GENERAL_FUTURO });
		} else if (!ipcs.isEmpty()) {
			ipc = ipcs.get(0);
		}

		return ipc.getPipcgas();
	}
	
	@Override
	public IPCGeneralFuturo recuperarFondosIpcFuturo(Timestamp fecCierre, String bt) {

		List<IPCGeneralFuturo> ipcs = null;
		
		if (bt.equals(ConstantesSolvencia.BASE_SCRGTO)){
			ipcs = ipcGeneralFuturoSCRGTODao.getValues(fecCierre);
		} else {
			ipcs = ipcGeneralFuturoDao.getValues(fecCierre);
		}
		
		IPCGeneralFuturo ipc = null;

		if (ipcs.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.IPC_GENERAL_FUTURO });
		} else if (!ipcs.isEmpty()) {
			ipc = ipcs.get(0);
		}

		return ipc;
	}
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarLx(java.sql.Timestamp, java.lang.Integer, java.lang.String, java.math.BigDecimal, java.math.BigDecimal, java.math.BigDecimal, java.lang.Integer)
	 */
	@Override
	public java.math.BigDecimal recuperarLx(Timestamp feccierre,
			Integer ktabla, String wkanacimiento,
			java.math.BigDecimal wkinteres, java.math.BigDecimal wksobremort,
			java.math.BigDecimal wksobreries, Integer kedad) {

		TablaExperiencia tablaExperiencia = recuperarValorDeMortalidad(
				feccierre, ktabla, wkanacimiento, wksobremort, wksobreries);

		if (tablaExperiencia != null && tablaExperiencia.getGvalor() != null) {
			return tablaExperiencia.getGvalor().get(kedad);
		} else {
			return null;
		}

	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarValoresExperiencia(es.mapfre.solvencia.dominio.maestro.Umic, es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica, java.lang.Integer, java.lang.String, java.lang.String, java.math.BigDecimal, java.lang.Integer)
	 */
	//@Override
	public List<java.math.BigDecimal> recuperarValoresExperienciaOld(Umic umic, DetalleBaseTecnica btc, Integer ktabla, String wkanacimiento, String sexAseg, java.math.BigDecimal wkinteres, Integer edadAseg, String tipoValores,
			final IObtenerConfiguracion.OrdenAsegurado ordenAsegurado) {

		if(btc.getIndTabExp() == ConstantesSolvencia.CTE_TABLA_REALISTA){
			return recuperarValoresExperienciaReal(umic, ktabla, Integer.valueOf(wkanacimiento), sexAseg, edadAseg, btc.getBaseTec(), tipoValores);
		}else{
			TablaExperiencia tablaExperiencia= null;
			if (tipoValores.equals("L")){
				tablaExperiencia = recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), ktabla, wkanacimiento,  umic.getBti().getPriesgo(), umic.getBti().getPsobremort());
			}else if (tipoValores.equals("Q")){
				tablaExperiencia = recuperarValoresQx(umic.getDatosGenerales().getFecCierre(), ktabla, wkanacimiento,  umic.getBti().getPriesgo(), umic.getBti().getPsobremort());
			}

			if (tablaExperiencia != null) {
				return tablaExperiencia.getGvalor();
			} else {
				return null;
			}
		}
	}

	@Override
	public List<java.math.BigDecimal> recuperarValoresExperiencia(Umic umic, DetalleBaseTecnica btc, String wkanacimiento, String sexAseg, Integer edadAseg, String tipoValores,
			final IObtenerConfiguracion.OrdenAsegurado ordenAsegurado) {	
		//Se obtiene el numero del asegurado para el que se debe recuperar la tabla de experiencia
		int asegurado=0;
		switch (ordenAsegurado){
		case ASEG1:
			asegurado = 0;
			break;
		case ASEG2:
			asegurado = 1;
			break;
		case ASEG3:
			asegurado = 2;
			break;
		case ASEG4:
			asegurado = 3;
			break;
		case ASEG5:
			asegurado = 4;
			break;
		}
		
		if(btc.getIndTabExp() == ConstantesSolvencia.CTE_TABLA_REALISTA && btc.getTablaBaseExpList() != null/*&& !tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_I)*/){
			Integer tabla = Integer.valueOf(btc.getTablaBaseExpList().get(asegurado).get(0));
			if (tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_I)){
				tabla = Integer.valueOf(btc.getTablaBaseExpList().get(asegurado).get(2));
			}
			List<BigDecimal> tablaExperienciaReal = recuperarValoresExperienciaReal(umic, tabla, Integer.valueOf(wkanacimiento), sexAseg, edadAseg, btc.getBaseTec(), tipoValores);
			if (tablaExperienciaReal != null){
				return tablaExperienciaReal;
			}else{
				tablaExperienciaReal = recuperarValoresExperienciaReal(umic, tabla, Integer.valueOf(0), sexAseg, edadAseg, btc.getBaseTec(), tipoValores);
				if (tablaExperienciaReal != null){
					return tablaExperienciaReal;
				}
				throw Solvencia2ExcepcionHelper.crearExcepcion(DC, new Object[]{tabla, wkanacimiento, sexAseg, edadAseg});
			}
		} else {
			TablaExperiencia tablaExperiencia= new TablaExperiencia();
			TablaExperiencia tablaExperienciaI= null;
			TablaExperiencia tablaExperienciaF= null;
			
			Integer tablaIni=0;
			Integer tablaFin=0;
			
			if (btc.getTablasConversionAsegurado() == null || btc.getTablasConversionAsegurado().get(asegurado) == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(AC, new String[]{null, "tablasConversionAsegurado de "+ordenAsegurado});
			}

			if (ConstantesSolvencia.CTE_TABMORT_L.equals(tipoValores)) {
				tablaIni = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(0).getTablaInicio());
				tablaFin = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(0).getTablaFin());
			} else if (ConstantesSolvencia.CTE_TABMORT_Q.equals(tipoValores)) {
				tablaIni = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(0).getTablaInicio());
				tablaFin = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(0).getTablaFin());
			} else if (ConstantesSolvencia.CTE_TABMORT_I.equals(tipoValores)){
				if (btc.getTablasConversionAsegurado().get(asegurado).get(2) == null || btc.getTablasConversionAsegurado().get(asegurado).get(2).getTablaInicio() == null
						|| btc.getTablasConversionAsegurado().get(asegurado).get(2).getTablaFin() == null){
					throw Solvencia2ExcepcionHelper.crearExcepcion(AC, new String[]{null, "tablasInvalidez de "+ordenAsegurado});
				}
				tablaIni = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(2).getTablaInicio());
				tablaFin = Integer.valueOf(btc.getTablasConversionAsegurado().get(asegurado).get(2).getTablaFin());
			}

			if ((tablaIni == 0 && tablaFin!=0) || tablaIni == tablaFin || btc.getBaseTec().equals(ConstantesSolvencia.BASE_BTI)|| btc.getBaseTec().equals(ConstantesSolvencia.BASE_BTIPROY)){
				if (ConstantesSolvencia.CTE_TABMORT_L.equals(tipoValores) || ConstantesSolvencia.CTE_TABMORT_I.equals(tipoValores)){
					tablaExperiencia = recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), tablaFin, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
				}else if (ConstantesSolvencia.CTE_TABMORT_Q.equals(tipoValores)) {
					tablaExperiencia = recuperarValoresQx(umic.getDatosGenerales().getFecCierre(), tablaFin, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
				}
			} else if(tablaIni != tablaFin){
				if (tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_L) || tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_I)) {
					tablaExperienciaI = recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), tablaIni, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
					tablaExperienciaF = recuperarValorDeMortalidad(umic.getDatosGenerales().getFecCierre(), tablaFin, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
				} else if (tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_Q)) {
					tablaExperienciaI = recuperarValoresQx(umic.getDatosGenerales().getFecCierre(), tablaIni, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
					tablaExperienciaF = recuperarValoresQx(umic.getDatosGenerales().getFecCierre(), tablaFin, wkanacimiento, umic.getBti().getPsobremort(),  umic.getBti().getPriesgo());
				}
				List<BigDecimal> lstTabExp = new ArrayList<BigDecimal>();
				BigDecimal valorIni;
				BigDecimal valorFin;
				BigDecimal valor = BigDecimal.ZERO;
				BigDecimal factor = BigDecimal.ZERO;
				for(int i=0; i<tablaExperienciaI.getGvalor().size();i++){
					valorIni = tablaExperienciaI.getGvalor().get(i);
					valorFin = tablaExperienciaF.getGvalor().get(i);
					if (valorIni.signum()!=0 && valorFin.signum()!=0){
						
						if(null != btc.getFactorInterpolExperienciaRossp()){ 
							factor = btc.getFactorInterpolExperienciaRossp();
						}else{
							factor = BigDecimal.ZERO;
						}	
							valor = valorIni.add((valorFin.subtract(valorIni)).multiply(BigDecimal.ONE.subtract(factor)));
						
					}else{
						valor = BigDecimal.ZERO;
					}
					
					lstTabExp.add(valor);
				}
				//tablaExperiencia.setGvalor(lstTabExp);
				return lstTabExp;
			} else if (tablaFin == 0 || tablaFin == null){
				throw Solvencia2ExcepcionHelper.crearExcepcion(AD, new Object[]{tablaFin, ordenAsegurado});
			}


			if (tablaExperiencia != null) {
				return tablaExperiencia.getGvalor();
			} else {
				/**
				 * INCIDENCIA A FASE VII
				 */
				throw Solvencia2ExcepcionHelper.crearExcepcion(D7, new Object[]{tablaIni, wkanacimiento, sexAseg, tablaFin, edadAseg});
			}
		}
	}

	/**
	 * @param umic
	 * @param ktabla
	 * @param nanoAsegurado
	 * @param sexAseg
	 * @param edadAseg
	 * @param bt
	 * @return
	 */
	private List<java.math.BigDecimal> recuperarValoresExperienciaReal(Umic umic, Integer ktabla, Integer nanoAsegurado,String sexAseg, Integer edadAseg, String bt, String tipoValores) {

		List<CabeceraTablaExperiencia> cabeceras = cabeceraTablaExperienciaDao
				.getValue(ktabla, umic.getDatosGenerales().getFecCierre());
		if (cabeceras.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.CABECERA_TABLA_EXPERIENCIA });
		} else if (cabeceras.isEmpty()) {
			// No tenemos resultado para la cabecera así que no realizamos la segunda consulta
			return null;
		}

		Integer generacion = 0;
		if (cabeceras.get(0).getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_GENERACIONAL)) {
			generacion = nanoAsegurado;
		}
		
		return getValoresTablasExperienciaReales(umic, ktabla, sexAseg,	edadAseg, bt, generacion,umic.getDatosGenerales().getKmodalidad(), tipoValores);
			
	}

	/**
	 * @param umic
	 * @param ktabla
	 * @param sexAseg
	 * @param edadAseg
	 * @param bt
	 * @param generacion
	 * @param modalidad
	 * @return
	 */
	private List<java.math.BigDecimal> getValoresTablasExperienciaReales(
			Umic umic, Integer ktabla, String sexAseg, Integer edadAseg,
			String bt, Integer generacion, Integer modalidad, String tipoValores) {
			
		List<TablasExperienciaReales> tablasExperiencia;
		
		switch (bt) {
		case ConstantesSolvencia.BASE_SCRMFE:
			tablasExperiencia = tablasExperienciaRealesSCRMFEDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_SCRMMI:
			tablasExperiencia = tablasExperienciaRealesSCRMMIDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_SCRLFE:
			tablasExperiencia = tablasExperienciaRealesSCRLFEDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_SCRLMI:
			tablasExperiencia = tablasExperienciaRealesSCRLMIDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_SCRMCF:
			tablasExperiencia = tablasExperienciaRealesSCRMCFDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_SCRMCI:
			tablasExperiencia = tablasExperienciaRealesSCRMCIDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		case ConstantesSolvencia.BASE_NF17MFE:
			tablasExperiencia = tablasExperienciaRealesNF17MFEDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		default:
			tablasExperiencia = tablasExperienciaRealesDao.getValues(umic.getDatosGenerales().getFecCierre(), 
					bt, umic.getDatosGenerales().getCcanal(), umic.getDatosGenerales().getCnegocio(), 
					umic.getDatosGenerales().getTipoSubriesgo(), sexAseg, umic.getDatosGenerales().getKcategoria(), 
					edadAseg, modalidad, ktabla, generacion, umic.getFechas().getTc(), umic.getBti().getPriesgo(), 
					umic.getBti().getPsobremort());	
			break;
		}
		
//INI-TAR00433819
//		if (tablasExperiencia.size() > 1) {
//			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,	new String[] { ConstantesSolvencia.TABLA_EXPERIENCIA });
//		}
//FIN-TAR00433819		
		if (!tablasExperiencia.isEmpty() && tablasExperiencia.get(0) != null) {
			//return tablasExperiencia.get(0).getQ();
			if (tipoValores.equals(ConstantesSolvencia.CTE_TABMORT_I)) {
				return tablasExperiencia.get(0).getQ();
			}
			return tablasExperiencia.get(0).getL();
		} else if(!modalidad.equals(0)){
			return getValoresTablasExperienciaReales(umic, ktabla, sexAseg,	edadAseg, bt, generacion,0, tipoValores);
		}else{
			return null;
		}
	}

	/**
	 * @param feccierre
	 * @param ktabla
	 * @param wkanacimiento
	 * @param wksobremort
	 * @param wksobreries
	 * @return
	 */
	@Override
	public TablaExperiencia recuperarValorDeMortalidad(Timestamp feccierre,
			Integer ktabla, String wkanacimiento, java.math.BigDecimal wksobremort,
			java.math.BigDecimal wksobreries) {
		// Buscar la cabecera por CabeceraTablaExperiencia.ktabla = ktabla
		// CabeceraTablaExperiencia.fecanula > feccierre.

		List<CabeceraTablaExperiencia> cabeceras = cabeceraTablaExperienciaDao
				.getValue(ktabla, feccierre);
		CabeceraTablaExperiencia cabecera = null;
		if (cabeceras.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.CABECERA_TABLA_EXPERIENCIA });
		} else if (!cabeceras.isEmpty()) {
			cabecera = cabeceras.get(0);
		}else{
			throw Solvencia2ExcepcionHelper.crearExcepcion(AH, 
					new Object[] { ktabla});
		}

		TablaExperiencia configValoresTabla = new TablaExperiencia();

		if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_TRADICIONAL)) {
			configValoresTabla.setKtabla(ktabla);
			configValoresTabla.setKinteres(BigDecimal.ZERO);
			configValoresTabla.setKsobremort(wksobremort);
			configValoresTabla.setKsobreries(wksobreries);
			configValoresTabla.setK2tipovalor("2");

		} else if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_GENERACIONAL)) {
			configValoresTabla.setKtabla(ktabla);
			configValoresTabla.setKanacimiento(wkanacimiento);
			configValoresTabla.setK2tipovalor("2");

		} else if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_COMPLEMENTARIA)) {
			configValoresTabla.setKtabla(ktabla);
			configValoresTabla.setK2tipovalor("11");
		}

		List<TablaExperiencia> tablas = tablaExperienciaDao
				.getValue(configValoresTabla);
		

		if (tablas == null) {
			return null;
		}

		TablaExperiencia tabla = null;

		if (tablas.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.TABLA_EXPERIENCIA });
		} else if (!tablas.isEmpty()) {
			tabla = tablas.get(0);
		}else{
			
			if(cabeceras.get(0).getK2tipotabla().equals("1")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DG, new Object[]{ktabla, wksobremort, wksobreries});
			}else if(cabeceras.get(0).getK2tipotabla().equals("2")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DH, new Object[]{ktabla, wkanacimiento});
			}else if(cabeceras.get(0).getK2tipotabla().equals("3")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DI, new Object[]{ktabla});
			}
		}

		return tabla;
	}
	
	/**
	 * 
	 * @param fecCierre
	 * @param ktabla
	 * @param wkanacimiento
	 * @param wksobremort
	 * @param wksobreries
	 * @return
	 */
	public TablaExperiencia recuperarValoresQx(Timestamp fecCierre, Integer ktabla, String wkanacimiento, 
			java.math.BigDecimal wksobremort, java.math.BigDecimal wksobreries){
		List<CabeceraTablaExperiencia> cabeceras = cabeceraTablaExperienciaDao
				.getValue(ktabla, fecCierre);
		CabeceraTablaExperiencia cabecera = null;
		if (cabeceras.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.CABECERA_TABLA_EXPERIENCIA });
		} else if (!cabeceras.isEmpty()) {
			cabecera = cabeceras.get(0);
		}

		TablaExperiencia configValoresTabla = new TablaExperiencia();

		if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_TRADICIONAL)) {
			configValoresTabla.setKtabla(ktabla);
			configValoresTabla.setKinteres(BigDecimal.ZERO);
			configValoresTabla.setKsobremort(wksobremort);
			configValoresTabla.setKsobreries(wksobreries);
			configValoresTabla.setK2tipovalor("1");

		} else if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_GENERACIONAL)) {
			configValoresTabla.setKtabla(ktabla);
			configValoresTabla.setKanacimiento(wkanacimiento);
			configValoresTabla.setK2tipovalor("1");

		} else if (cabecera.getK2tipotabla().equals(ConstantesSolvencia.TIPO_TABLA_COMPLEMENTARIA)) {
			return null;
		}

		List<TablaExperiencia> tablas = tablaExperienciaDao
				.getValue(configValoresTabla);
		

		if (tablas == null) {
			return null;
		}

		TablaExperiencia tabla = null;

		if (tablas.size() > 1) {
			throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
					new String[] { ConstantesSolvencia.TABLA_EXPERIENCIA });
		} else if (!tablas.isEmpty()) {
			tabla = tablas.get(0);
		}
		/** 
		 * INCIDENCIA A FASE VII
		 */
		else{

			if(cabeceras.get(0).getK2tipotabla().equals("1")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DG, new Object[]{ktabla, wksobremort, wksobreries});
			}else if(cabeceras.get(0).getK2tipotabla().equals("2")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DH, new Object[]{ktabla, wkanacimiento});
			}else if(cabeceras.get(0).getK2tipotabla().equals("3")){
				throw Solvencia2ExcepcionHelper.crearExcepcion(DI, new Object[]{ktabla});
			}
			
		}

		return tabla;
	
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarValoresTipos(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public List<ValoresCurvaTipo> recuperarValoresTipos(String codCurvaTipos, Timestamp fcalc) {
		
		List<ValoresCurvaTipo> returnValue = null;
		
		returnValue = valoresCurvaTipoDao.getByFecha(codCurvaTipos, fcalc);
	
		return returnValue;
	}
	
	@Override
	public List<ValoresLiquidativos> recuperarValoresLiquidativos(Long poliza, String modalidad, String ramo, Integer certificado) {
		List<ValoresLiquidativos> returnValue = valoresLiquidativosDao.getValues(poliza, modalidad, ramo, certificado);
	
		return returnValue;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarDefinicionAuxiliar(java.lang.Integer, java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public Object recuperarDefinicionAuxiliar(Integer carteraOrigen,
			Integer modalidad, Integer garantia, String basetec, String nombreVariable) {

		String valor = null;
		
		if (basetec.equals(ConstantesSolvencia.BASE_NIF17LIR) || basetec.equals(ConstantesSolvencia.BASE_N17LIRIN) 
				|| basetec.equals(ConstantesSolvencia.BASE_NIFF17OCI) || basetec.equals(ConstantesSolvencia.BASE_NIIF17IF)
				|| basetec.equals(ConstantesSolvencia.BASE_N17CLIR) || basetec.equals(ConstantesSolvencia.BASE_NF17MFE)
				|| basetec.equals(ConstantesSolvencia.BASE_NF17AEN) || basetec.equals(ConstantesSolvencia.BASE_NF17GTO)) {
			basetec = ConstantesSolvencia.BASE_NIIF17;
		}
		
		if (basetec.equals(ConstantesSolvencia.BASE_BTIPROY)){
			basetec = ConstantesSolvencia.BASE_BTI;
		}
		
		if (basetec.equals(ConstantesSolvencia.BASE_SCRTIU) || basetec.equals(ConstantesSolvencia.BASE_SCRTID) 
				|| basetec.equals(ConstantesSolvencia.BASE_SCRGTO) || basetec.equals(ConstantesSolvencia.BASE_SCRMFE)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRMMI) || basetec.equals(ConstantesSolvencia.BASE_SCRMCF)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRMCI) || basetec.equals(ConstantesSolvencia.BASE_SCRLFE)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRLMI) || basetec.equals(ConstantesSolvencia.BASE_SCRINC)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRVM) || basetec.equals(ConstantesSolvencia.BASE_SCRAEN)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRAEP) || basetec.equals(ConstantesSolvencia.BASE_SCRAIN)
				|| basetec.equals(ConstantesSolvencia.BASE_SCRAIP) || basetec.equals(ConstantesSolvencia.BASE_SCRANM)
				|| basetec.equals(ConstantesSolvencia.BASE_SCR)) {
			basetec = ConstantesSolvencia.BASE_BEL;
		}
		if (basetec.equals(ConstantesSolvencia.BASE_ROSSPCSM) || basetec.equals(ConstantesSolvencia.BASE_ROSSPTI) || basetec.equals(ConstantesSolvencia.BASE_ROSSPTE) || basetec.equals(ConstantesSolvencia.BASE_ROSSPGA)){
			basetec = ConstantesSolvencia.BASE_ROSSP;
		}
		
		
		Boolean primerIntento = Boolean.TRUE;
		// 1º Intento
		List<DefinicionesAuxiliares> value = definicionesAuxiliaresDao.getValue(carteraOrigen, modalidad, garantia, basetec, nombreVariable);
		
		if (value == null || value.isEmpty()) {
			// 2º Intento sin base técnica
			primerIntento = Boolean.FALSE;
			value = definicionesAuxiliaresDao.getValue(carteraOrigen, modalidad, garantia, null, nombreVariable);
			// 3º Intento con base técnica, sin garantia ni modalidad
			if (value == null || value.isEmpty()) {
				value = definicionesAuxiliaresDao.getValue(carteraOrigen, Integer.valueOf(0), Integer.valueOf(0), basetec, nombreVariable);
				// 4º Intento sin base técnica, sin garantia ni modalidad
				if (value == null || value.isEmpty()) {
					primerIntento = Boolean.FALSE;
					value = definicionesAuxiliaresDao.getValue(carteraOrigen, Integer.valueOf(0), Integer.valueOf(0), null, nombreVariable);
					// 5º Intento con base técnica sin garantia ni modalidad ni categoria
					if (value == null || value.isEmpty()) {
						value = definicionesAuxiliaresDao.getValue(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), basetec, nombreVariable);
						// 6º Intento sin base técnica sin garantia ni modalidad ni categoria
						if (value == null || value.isEmpty()) {
							value = definicionesAuxiliaresDao.getValue(Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), null, nombreVariable);
						}
					}
				}
			}
		}

		
		if (value != null && !value.isEmpty()) {
			DefinicionesAuxiliares definicionAuxiliar = value.get(0);
			if (!primerIntento) {
				// Guardamos el resultado para el futuro
				definicionAuxiliar.setKcarteorig(carteraOrigen);
				definicionAuxiliar.setKmodalidad(modalidad);
				definicionAuxiliar.setKgarantia(garantia);
				definicionAuxiliar.setKbasetec(basetec);
				definicionesAuxiliaresDao.put(definicionAuxiliar.getKey(), definicionAuxiliar);
			}
			valor = definicionAuxiliar.getGvalor();
		}
		
		return valor;

	}
	//FIN - SBM2
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarDefinicionAuxiliar(java.lang.String)
	 */
	public Object recuperarDefinicionAuxiliar(String nombreVariable) {
		
		DefinicionesAuxiliares value = definicionesAuxiliaresDao.getValue(nombreVariable);
		
		if (value != null){
			return value.getGvalor();
		} else {
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#validarCodBaseTecnica(java.lang.String)
	 */
	@Override
	public Boolean validarCodBaseTecnica(String ktipobt) {

		String[] btContempladas = propertiesDao
				.get("bases.tecnicas.contempladas").toString().split(",");

		if (log.isDebugEnabled()) {
			log.debug("ktipobt = {}", ktipobt);
		}

		for (int i = 0; i < btContempladas.length; i++) {
			if (btContempladas[i].equals(ktipobt)) {
				return true;
			}
		}

		return false;

	}
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarCteRescate(java.lang.String, java.lang.Integer)
	 */
	@Override
	public java.math.BigDecimal recuperarCteRescate(String codk1, Integer duracion){
		BigDecimal constanteRescate = null;
		if (codk1!=null){
			constanteRescate = valoresCteRescateDao.recuperarCteRescate(codk1, duracion);
		}
		return constanteRescate;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarCtesRescate(java.lang.String, java.lang.Integer)
	 */
	@Override
	public List<ValoresConstantesRescate> recuperarCtesRescate(String kk1, Integer duracion) {
		
		List<ValoresConstantesRescate> constantesRescate = valoresCteRescateDao.recuperarCtesRescate(kk1, duracion);
		
		if (constantesRescate == null || constantesRescate.isEmpty()){
			throw Solvencia2ExcepcionHelper.crearExcepcion(AN, new Object[] { kk1, duracion });
		}
		
		return constantesRescate;
		
	}
	

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarLimitesCapital(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public LimitesCapital recuperarLimitesCapital(String kmodalidad,
			String kgarantia, Timestamp fecEfecto, Integer edadAsegurado,
			Integer numMeses) {

		List<LimitesCapital> returnValue = null;

		returnValue = limitesCapitalDao.getValue(ConstantesSolvencia.NTABLA_LIMITES_CAPITAL, kmodalidad,
				kgarantia, fecEfecto, edadAsegurado, numMeses);

		if (returnValue != null) {
			if (returnValue.isEmpty()) {
				throw Solvencia2ExcepcionHelper.crearExcepcion(E01,
						new String[] { ConstantesSolvencia.LIMITES_CAPITAL });
			} else {
				return returnValue.get(0);
			}
		} else {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarListaLimitesCapital(java.lang.String, java.lang.String, java.sql.Timestamp, java.lang.Integer)
	 */
	@Override
	public List<LimitesCapital> recuperarListaLimitesCapital(String kmodalidad,
			String kgarantia, Timestamp fecEfecto, Integer edadAsegurado) {

		List<LimitesCapital> returnValue = null;

		returnValue = limitesCapitalDao.getValue(ConstantesSolvencia.NTABLA_LIMITES_CAPITAL, kmodalidad,
				kgarantia, fecEfecto, edadAsegurado, null);

		return returnValue;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarConfProv(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	@Override
	public FlujosProbables recuperarConfProv(Integer modalidad,
			Integer garantia, String prestacion, String baseTecnica) {
		FlujosProbables returnValue = null;
		FlujosProbablesKey key;
		
		// Para las bases técnicas de SCR, se recuperan los flujos probables de BEL
		if (baseTecnica.equals(ConstantesSolvencia.BASE_SCRMFE) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRMMI) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRVM)  ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRMCF) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRMCI) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRLFE) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRLMI) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRINC) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRTIU) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRTID) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRGTO) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRAEN) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRAEP) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRAIN) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRAIP) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_SCRANM)){

			key = new FlujosProbablesKey(modalidad, garantia,
					prestacion, ConstantesSolvencia.BASE_BEL);
		}else if(baseTecnica.equals(ConstantesSolvencia.BASE_NIIF17) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NIF17LIR) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_N17LIRIN) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NIFF17OCI) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NIIF17IF) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_N17CLIR) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NF17GTO) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NF17MFE) ||
				baseTecnica.equals(ConstantesSolvencia.BASE_NF17AEN)){
			key = new FlujosProbablesKey(modalidad, garantia,
					prestacion, ConstantesSolvencia.BASE_NIIF17);		 
		}else {
			if (baseTecnica.equals(ConstantesSolvencia.BASE_ROSSPTE)
					|| baseTecnica.equals(ConstantesSolvencia.BASE_ROSSPTI)
					|| baseTecnica.equals(ConstantesSolvencia.BASE_ROSSPGA)) {
				key = new FlujosProbablesKey(modalidad, garantia,
						prestacion, ConstantesSolvencia.BASE_ROSSP);
			} else {
				key = new FlujosProbablesKey(modalidad, garantia,
						prestacion, baseTecnica);
			}
		}
		
		returnValue = flujosProbablesDao.get(key);

		return returnValue;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarOpcionesGeneracion(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public OpcionesGeneracion recuperarOpcionesGeneracion(Integer kmodalidad,
			Integer kgarantia, String kprestacion) {
		OpcionesGeneracion opg = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad,
				kgarantia, kprestacion));
		if (null == opg) {
			opg = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad,
					kgarantia, null));
		}
		return opg;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarCriterioFechas(java.lang.Integer, java.lang.Integer, java.lang.String)
	 */
	@Override
	public CriterioFechas recuperarCriterioFechas(Integer kmodalidad,
			Integer kgarantia, String kprestacion, String kprestcal, String codSubproceso) {

		OpcionesGeneracion value = opcionesGenDao
				.get(new OpcionesGeneracionKey(kmodalidad, kgarantia, kprestcal));
		
		if (value == null) {
			value = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad, kgarantia, kprestacion));
		}
		
		if (value == null) {
			value = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad, kgarantia, null));
		}

		CriterioFechas criterio = new CriterioFechas();
		
		if (value != null) {
			switch (codSubproceso) {
				case ConstantesSolvencia.CTE_PROY_VIDA:
					criterio.setFecDevengo(value.getDevengovida());
					criterio.setFecPago(value.getPagovida());
					break;
				case ConstantesSolvencia.CTE_PROY_FALL:
					criterio.setFecDevengo(value.getDevengofallecimient());
					criterio.setFecPago(value.getPagofallecimiento());
					break;
				case ConstantesSolvencia.CTE_PROY_COMP:
					criterio.setFecDevengo(value.getDevengoinvalidez());
					criterio.setFecPago(value.getPagoinvalidez());
					break;
				case ConstantesSolvencia.CTE_PROY_PRIMA:
					criterio.setFecDevengo(value.getDevengoprimas());
					criterio.setFecPago(value.getPagoprimas());
					break;
				case ConstantesSolvencia.CTE_PROY_GTOS:
					criterio.setFecDevengo(value.getDevengogastos());
					criterio.setFecPago(value.getPagogastos());
					break;
				case ConstantesSolvencia.CTE_PROY_COMI:
					criterio.setFecDevengo(value.getDevengocomisiones());
					criterio.setFecPago(value.getPagocomisiones());
					break;
				case ConstantesSolvencia.CTE_PROY_RESC:
					criterio.setFecDevengo(value.getDevengoanulaciones());
					criterio.setFecPago(value.getPagoanulaciones());
					break;
				case ConstantesSolvencia.CTE_PROY_PRV:	
					criterio.setFecDevengo(ConstantesSolvencia.CRITERIO_FECHA_PROY_PRV);
					criterio.setFecPago(ConstantesSolvencia.CRITERIO_FECHA_PROY_PRV);
					break;
				case ConstantesSolvencia.CTE_PROY_GTOAD:
					criterio.setFecDevengo(value.getDevengogtoad());
					criterio.setFecPago(value.getPagogtoad());
					break;	
				}
		} else {
			return null;
		}
		return criterio;
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarModulo(java.lang.Integer, java.lang.Integer, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public String recuperarModulo(Integer modalidad, Integer garantia,
			String prestacion, String baseTecnica, String codSubproceso,
			String tipoElemento) {
		FlujosProbables flujoProbable = this.recuperarConfProv(modalidad,
				garantia, prestacion, baseTecnica);
		if (baseTecnica.equals("ROSSPTE")
				|| baseTecnica.equals("ROSSPTI")
				|| baseTecnica.equals("ROSSPGA")) {
			flujoProbable = this.recuperarConfProv(modalidad,
					garantia, prestacion, "ROSSP");
		}
		
		return recuperarModuloFlujoProbable(flujoProbable,codSubproceso, tipoElemento);

	}
	
	@Override
	public String recuperarModuloFlujoProbable(FlujosProbables flujoProbable, String codSubproceso, String tipoElemento){
		String modulo = null;
		
		if (flujoProbable != null && tipoElemento != null) {
			
			// Sólo hay terminal en el Prov
			if (!(tipoElemento.equalsIgnoreCase(ConstantesSolvencia.CTE_ELEMENTO_TERMINAL) && !codSubproceso.equalsIgnoreCase(ConstantesSolvencia.CTE_PROY_PRV)
					&& !codSubproceso.equalsIgnoreCase(ConstantesSolvencia.CTE_PROY_PMRR))) {
				BloqueFlujosProbables bloque = null;
				
				// El método getter a ejecutar depende de la implementación de
				// FlujosProbables
				switch (codSubproceso) {
				case ConstantesSolvencia.CTE_PROY_VIDA:
					bloque = flujoProbable.getVida();
					break;
				case ConstantesSolvencia.CTE_PROY_FALL:
					bloque = flujoProbable.getFall();
					break;
				case ConstantesSolvencia.CTE_PROY_INVA:
					bloque = flujoProbable.getInva();
					break;
				case ConstantesSolvencia.CTE_PROY_COMP:
					bloque = flujoProbable.getInva();
					break;
				case ConstantesSolvencia.CTE_PROY_PRIMA:
					bloque = flujoProbable.getPrim();
					break;
				case ConstantesSolvencia.CTE_PROY_GTOS:
					bloque = flujoProbable.getGast();
					break;
				case ConstantesSolvencia.CTE_PROY_COMI:
					bloque = flujoProbable.getComi();
					break;
				case ConstantesSolvencia.CTE_PROY_RESC:
					bloque = flujoProbable.getAnul();
					break;
				case ConstantesSolvencia.CTE_PROY_PRV:
					// El Prov no tiene más que Nominal y Terminal
					if (tipoElemento.equalsIgnoreCase(ConstantesSolvencia.CTE_ELEMENTO_NOMINAL)) {
						modulo = flujoProbable.getProvNominal();
						
					} else if (tipoElemento.equalsIgnoreCase(ConstantesSolvencia.CTE_ELEMENTO_TERMINAL)) {
						modulo = flujoProbable.getProvNominal();
					}
					break;
				case ConstantesSolvencia.CTE_PROY_PMRR:
					modulo = flujoProbable.getProvNominal();
					break;
				case ConstantesSolvencia.CTE_PROY_GTOAD:
					bloque = flujoProbable.getGtoad();
					break;
				default:
					bloque =  null;
				}
	
				if(bloque != null){
					modulo = this.recuperaModuloDeBloque(bloque, tipoElemento);
				}	
			}
		}

		return modulo;
	}
	
	/**
	 * Recupera el elemento correspondiente al tipo pasado como parámetro del bloque de flujos probables.
	 * 
	 * @param bloque
	 * @param tipoElemento
	 * @return
	 */
	private String recuperaModuloDeBloque(BloqueFlujosProbables bloque, String tipoElemento) {
		switch (tipoElemento) {
		case ConstantesSolvencia.CTE_ELEMENTO_NOMINAL:
			return bloque.getNominal();
		case ConstantesSolvencia.CTE_ELEMENTO_PROBABLE:
			return bloque.getProbable();
		case ConstantesSolvencia.CTE_ELEMENTO_NOANULADO:
			return bloque.getNoanulado();
		case ConstantesSolvencia.CTE_ELEMENTO_ACTUALIZADO:
			return bloque.getActualizado();
		case ConstantesSolvencia.CTE_ELEMENTO_PROVI:
			return bloque.getProvi();
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see es.mapfre.solvencia.servicios.IObtenerConfiguracion#recuperarTasasAnulMensuales(java.lang.String, java.sql.Timestamp)
	 */
	@Override
	public List<ValoresAnulacionMensuales> recuperarTasasAnulMensuales(String ktabla, Timestamp fecCierre) {
		return valoresAnulacionMensualesDao.getValues(ktabla, fecCierre);
	}
	
	@Override
	public List<ValoresAnulacion> recuperarTasasAnul(String ktabla, Timestamp fecCierre, String bt) {
		if (bt.equals(ConstantesSolvencia.BASE_SCRAEP)){
			return valoresAnulacionSCRAEPDao.getValues(ktabla, fecCierre);
		} else if (bt.equals(ConstantesSolvencia.BASE_SCRAEN)){
			return valoresAnulacionSCRAENDao.getValues(ktabla, fecCierre);
		} else if (bt.equals(ConstantesSolvencia.BASE_SCRAIP)){
			return valoresAnulacionSCRAIPDao.getValues(ktabla, fecCierre);
		} else if (bt.equals(ConstantesSolvencia.BASE_SCRAIN)){
			return valoresAnulacionSCRAINDao.getValues(ktabla, fecCierre);
		} else if (bt.equals(ConstantesSolvencia.BASE_NF17AEN)){
			return valoresAnulacionNF17AENDao.getValues(ktabla, fecCierre);
		} else {
			return valoresAnulacionDao.getValues(ktabla, fecCierre);
		}
	}
	
	@Override
	public String recuperarDatosEspecificosUmic(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion, String codigo){
		String datoEspecifico = null;
		DatosEspecificos datos = datosEspecificosDao.getValues(poliza, subpoliza, certificado, nsuscripcion, codigo);
		
		if (datos != null){
			datoEspecifico = datos.getDato();
		}
		
		return datoEspecifico;
	}
	
	@Override
	public Timestamp recuperarVarFdiferimiento(Long poliza, Integer subpoliza,Integer certificado, Integer nsuscripcion){
		Timestamp valor = null;
		//String varFdiferimiento = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "C1FDI");
		
		//1er nivel con todos los datos informados
		String varFdiferimiento = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "C1FDI");
		
		if(null == varFdiferimiento){
			//2º nivel suscripcion = 0
			varFdiferimiento = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, Integer.valueOf(0), "C1FDI");
			
		}
		
		if(null == varFdiferimiento){
			//3º nivel certificado = 0
			varFdiferimiento = recuperarDatosEspecificosUmic(poliza, subpoliza, Integer.valueOf(0), nsuscripcion, "C1FDI");
		}
		
		if (varFdiferimiento != null){
			varFdiferimiento = varFdiferimiento.substring(5, 13);
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyyMMdd");
			try {
				Date fechaParseada = formatoFecha.parse(varFdiferimiento);
				valor = new Timestamp(fechaParseada.getTime());

			} catch (ParseException e) {
				//Si el formato no es el esperado se lanza excepción.
				throw Solvencia2ExcepcionHelper.crearExcepcion(AP, new String[]{varFdiferimiento, "yyyyMMdd"});
			}
		}
		
		return valor;
	}
	@Override
	public String recuperarVarC2S(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion){
		
		String varC2S = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "C2S");

		return varC2S;
	}	
	@Override
	public BigDecimal recuperarVarE1PSJ(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion){
		
		BigDecimal valor = BigDecimal.ZERO;
		String varE1PSJ = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "VARE1PSJ");

		if (varE1PSJ != null){
			valor = new BigDecimal(varE1PSJ);		
		}
		
		return valor;
	}								 
	
	public String recuperarDatoEspecifico(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion, String codigo) {

		String datoEspecifico;
		
		// 1º Nivel
		datoEspecifico = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, codigo);

		// 2º Nivel
		if(null == datoEspecifico){
			datoEspecifico = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, Integer.valueOf(0), codigo);
			
		}
		return datoEspecifico;

	}
	@Override
	public BigDecimal recuperarVarVZC2(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion) {
		BigDecimal valor = BigDecimal.ZERO;
		String varVZC2 = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "VZC2");

		/*if (varVZC2 != null){
			valor = new BigDecimal(varVZC2);		
		}*/
		
		return valor;
	}
	@Override
	public BigDecimal recuperarVarTabla2000(String codigo) {
		BigDecimal valor = BigDecimal.ZERO;
		String valorFormat;
		Tabla2000 tabla2000 = tabla2000Dao.getValues(codigo);
		if (tabla2000 != null){
			valorFormat = tabla2000.getValor().trim().replace(",", ".");			
			valor = new BigDecimal(valorFormat);		
		}
		
		return valor;
	}
	
	@Override
	public BigDecimal recuperarVarE3SAL(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion) {
		BigDecimal valor = BigDecimal.ZERO;
		String varE3SAL = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "E3SAL");

		if (varE3SAL != null){
			valor = new BigDecimal(varE3SAL);		
		}
		
		return valor;
	}
	
	@Override
	public BigDecimal recuperarVarAnoEsp(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion){
		BigDecimal valor = BigDecimal.ZERO;
		String varAnoEsp = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "ANOESP");

		if (varAnoEsp != null){
			valor = new BigDecimal(varAnoEsp);		
		}
		
		return valor;
	}
	@Override
	public BigDecimal recuperarVarPorvius(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion){
		BigDecimal valor = BigDecimal.ZERO;
		String varPorviuss = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "PORVIUSS");

		if (varPorviuss != null){
			valor = new BigDecimal(varPorviuss);		
		}
		
		return valor;
	}
	@Override
	public BigDecimal recuperarVarPrpss(Long poliza, Integer subpoliza, Integer certificado, Integer nsuscripcion){
		BigDecimal valor = BigDecimal.ZERO;
		String varPrpss = recuperarDatosEspecificosUmic(poliza, subpoliza, certificado, nsuscripcion, "PRPSS");

		if (varPrpss != null){
			valor = new BigDecimal(varPrpss);		
		}
		
		return valor;
	}
	@Override
	public CuadrosAmortizacion recuperarCuadrosAmortizacion(Umic umic) {
		
		DatosGenerales datosgenerales = umic.getDatosGenerales();
		
		return cuadrosAmortizacionDao.get(new CuadrosAmortizacionKey(datosgenerales.getKsubpoliza(),datosgenerales.getKajuste(),datosgenerales.getKgarantia(),datosgenerales.getKmodalidad(),datosgenerales.getNorden(),datosgenerales.getNsuscri(),datosgenerales.getKpoliza(),datosgenerales.getCcanal(),datosgenerales.getCnegocio(),datosgenerales.getKcertificado(),datosgenerales.getKprestacion()));
	
	}

	@Override
	public TotalesFlujos recuperarTotalesFlujos(Umic umic, DetalleBaseTecnica detalleBT) {
		
		DatosGenerales datosgenerales = umic.getDatosGenerales();
		
		return totalesFlujosdao.get(new TotalesFlujosKey(datosgenerales.getKajuste(), datosgenerales.getKcertificado(), datosgenerales.getKgarantia(), datosgenerales.getKmodalidad(), datosgenerales.getKpoliza(), datosgenerales.getKprestacion(), datosgenerales.getKsubpoliza(), detalleBT.getBaseTec(), datosgenerales.getNsuscri(), datosgenerales.getNorden(), datosgenerales.getCtipoaport()));
	
	}
	
	/**
	 * Recupera los valores de estrés
	 * 
	 * @param feccierre
	 * @param bt
	 * @return List<ValoresEstres>
	 */
	public List<ValoresEstres> recuperarValoresEstres(Timestamp feccierre, String bt) {
		
		return valoresEstresDao.obtenerValoresEstres(feccierre, bt);
	}
	
	@Override
    public OpcionesGeneracion recuperarOpcionesGeneracionPrestCal(Integer kmodalidad,
            Integer kgarantia, String kprestacion, String kprestcal) {
       
        OpcionesGeneracion value = opcionesGenDao
                .get(new OpcionesGeneracionKey(kmodalidad, kgarantia, kprestcal));
       
        if(null == value){
            value = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad,
                kgarantia, kprestacion));
        }
       
        if (null == value) {
            value = opcionesGenDao.get(new OpcionesGeneracionKey(kmodalidad,
                    kgarantia, null));
        }
       
        return value;
   
    }
	
	/**
	 * @param umic
	 * @param ktabla
	 * @param nanoAsegurado
	 * @param sexAseg
	 * @param edadAseg
	 * @param bt
	 * @return
	 */
	public TablaHibrida recuperarValoresTablaHibrida(Umic umic, Timestamp feccierre, Integer ktabla, String sexAseg, String riesgo, String bt) {

		List<TablaHibrida> tablaHibrida;
		
		tablaHibrida = tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
				umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
				umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
				riesgo, sexAseg, ktabla);
		
//		TablaHibrida value = tablaHibridaDao
//				.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
//						riesgo, sexAseg, ktabla));
		
		if (bt.equals("ROSSP")) {
			
			if(null == tablaHibrida || tablaHibrida.isEmpty()){
				tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
						null, sexAseg, ktabla);
				
			}
			
			if(null == tablaHibrida || tablaHibrida.isEmpty()){
				tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
						umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
						null, null, ktabla);
				
			}
			
//			if(null == value){
//				value = tablaHibridaDao
//						.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//								umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//								umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
//								null, sexAseg, ktabla);
//			}
//			if(null == value){
//				value = tablaHibridaDao
//						.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//								umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//								umic.getDatosGenerales().getKmodalidad(), umic.getDatosGenerales().getKgarantia(),
//								null, null, ktabla));
//			}
			
		}else if(bt.equals("NIIF17")) {
		
			if(null == tablaHibrida || tablaHibrida.isEmpty()){
				tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
						umic.getDatosGenerales().getKmodalidad(), null,
						riesgo, sexAseg, ktabla);
				
			}
			
			if(null == tablaHibrida || tablaHibrida.isEmpty()){
				tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
						null, null, riesgo, sexAseg, ktabla);
				
			}
			
			if(null == tablaHibrida || tablaHibrida.isEmpty()){
				tablaHibridaDao.getValues(feccierre, umic.getDatosGenerales().getCcanal(),
						umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
						null, null, riesgo, sexAseg, null);
				
			}
			
			
//			if(null == value){
//				value = tablaHibridaDao
//						.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//								umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//								umic.getDatosGenerales().getKmodalidad(), null,
//								riesgo, sexAseg, ktabla));
//			}
//		
//			if(null == value){
//				value = tablaHibridaDao
//						.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//								umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//								null, null, riesgo, sexAseg, ktabla));
//			}
//        
//			if(null == value){
//				value = tablaHibridaDao
//						.get(new TablaHibridaKey(feccierre, umic.getDatosGenerales().getCcanal(),
//								umic.getDatosGenerales().getCnegocio(), bt, umic.getDatosGenerales().getKramo(),
//								null, null, riesgo, sexAseg, null));
//			}
		}
		
		if (!tablaHibrida.isEmpty() && tablaHibrida.get(0) != null) {
			//return tablasExperiencia.get(0).getQ();
			return tablaHibrida.get(0);
		}else{
			return null;
		}
		
//        return value;
			
	}
	
	@Override
	public List<Tab923> recuperarDatosTab923(Integer modalidad, Timestamp finip, Timestamp ffinp){
		//String datoTab923 = null;
		//Tab923 datos = Tab923Dao.getValue(new Tab923Key(modalidad, finip, ffinp));
		List<Tab923> datos = tab923Dao.getValue(modalidad, finip, ffinp);
		
		return datos;
	}
	
	@Override
	public List<Tab35050> recuperarDatosTab35050(String ramo, Integer modalidad){
		List<Tab35050> datos = tab35050Dao.getValue(ramo, modalidad);
		
		return datos;
	}
	
	@Override
	public FlujCoaSeg recuperarFlujCoaSeg(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, Timestamp fecdesde, String gestionit, String tabla1aseg1) {
		
		return flujCoaSegDao.get(new FlujCoaSegKey(bt, fcierre, negocio, canal, gapact, ramo, modalidad, segmento1, tiposubriesgo,
				carterainv, poliza, subpoliza, nsuscri, fecdesde, gestionit, tabla1aseg1));
	}
	
	@Override
	public FlujTcas recuperarFlujTcas(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, Long poliza, Integer subpoliza, Integer nsuscri, String carterainv, String gapact, Timestamp fecdesde) {
		
		return flujTcasDao.get(new FlujTcasKey(bt, fcierre, negocio, canal, carterainv, gapact, ramo, modalidad,
				poliza, subpoliza, nsuscri, fecdesde));
	}
	
	@Override
	public ProvCoaSeg recuperarProvCoaSeg(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, String gestionit, String tabla1aseg1) {
		
		return provCoaSegDao.get(new ProvCoaSegKey(bt, fcierre, negocio, canal, ramo, modalidad, segmento1, tiposubriesgo,
				poliza, subpoliza, nsuscri, carterainv, gapact, gestionit, tabla1aseg1));
	}
	
	@Override
	public TotPMaCoa recuperarTotPMaCoa(Long poliza, Integer subpoliza, Integer nsuscri, String bt, Timestamp fcierre) {
		
		return totPMaCoaDao.get(new TotPMaCoaKey(bt, fcierre, poliza, subpoliza, nsuscri));
	}
	
	@Override
	public FlujPMaCoa recuperarFlujPMaCoa(Long poliza, Integer subpoliza, Integer nsuscri, String bt, Timestamp fcierre, Timestamp fecdesde) {
		
		return flujPMaCoaDao.get(new FlujPMaCoaKey(poliza, subpoliza, nsuscri, bt, fcierre, fecdesde));
	}
	
	@Override
	public FlujPMdCoa recuperarFlujPMdCoa(Integer kmodalidad, Integer kgarantia, String kprestacion, Long poliza, 
			Integer subpoliza, Integer kcertificado, Integer nsuscri, String bt, Timestamp fcierre) {
		
		return flujPMdCoaDao.get(new FlujPMdCoaKey(kmodalidad, kgarantia, kprestacion, poliza, subpoliza, kcertificado,
				nsuscri, bt, fcierre));
	}
	
	@Override
	public Collection<DatosGenerales> recuperarMaestro() {
		
		return datosGeneralesDao.values();
	}
	
	@Override
	public FlujSuscri recuperarFlujSuscri(String bt, Timestamp fcierre, String negocio, Integer canal, String ramo, 
			Integer modalidad, String segmento1, String tiposubriesgo, Long poliza, Integer subpoliza, Integer nsuscri,
			 String carterainv, String gapact, Timestamp fecdesde, Integer kgarantia) {
		
		return flujSuscriDao.get(new FlujSuscriKey(bt, fcierre, negocio, canal, gapact, ramo, modalidad, segmento1, tiposubriesgo,
				carterainv, poliza, subpoliza, nsuscri, fecdesde, kgarantia));
	}
	
	@Override
	public TabOGA recuperarTabOGA(String cartera, Timestamp fcierre) {
		
		return tabOGADao.getValue(cartera, fcierre);
	}
	
	@Override
	public FlujInfSCR recuperarFlujInfSCR(String bt, Timestamp fcierre, Integer canal, String negocio, String uoa, 
			String carteracontrato, String carteracohort, String carteraoner, Timestamp fproyflujest,
			 String carterainv, Integer modalidad) {
		
		return flujInfSCRDao.get(new FlujInfSCRKey(bt, fcierre, canal, negocio, uoa, carteracontrato, carteracohort, carteraoner, fproyflujest, carterainv, modalidad));
	}
}