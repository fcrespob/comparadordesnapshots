package es.mapfre.solvencia.formulacion.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.CabeceraTablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Primas;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo BXul010, módulo que calcula el importe nominal para la proyección de Provisión Matemática 
 * por Fórmula Cerrada.
 * @author apedro
 *
 */

public class ModuloBXul010Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloBXul010Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "PROY_PRV";
	
	public static final int cartera = 20101;
	public static final int modalidad = 858;
	public static final int garantia = 10;
	public static final String negocio = "I";
	public static final String terminal = "VZC";
	
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("998421.5"));
		TABLA_MORTALIDAD.add(new BigDecimal("996845.4917"));
		TABLA_MORTALIDAD.add(new BigDecimal("995271.9711"));
		TABLA_MORTALIDAD.add(new BigDecimal("993700.9342"));
		TABLA_MORTALIDAD.add(new BigDecimal("992132.3773"));
		TABLA_MORTALIDAD.add(new BigDecimal("990566.2964"));
		TABLA_MORTALIDAD.add(new BigDecimal("989002.6875"));
		TABLA_MORTALIDAD.add(new BigDecimal("987441.5467"));
		TABLA_MORTALIDAD.add(new BigDecimal("985882.8702"));
		TABLA_MORTALIDAD.add(new BigDecimal("984326.6541"));
		TABLA_MORTALIDAD.add(new BigDecimal("982772.8945"));
		TABLA_MORTALIDAD.add(new BigDecimal("981221.5875"));
		TABLA_MORTALIDAD.add(new BigDecimal("979672.7292"));
		TABLA_MORTALIDAD.add(new BigDecimal("978126.3158"));
		TABLA_MORTALIDAD.add(new BigDecimal("976582.3434"));
		TABLA_MORTALIDAD.add(new BigDecimal("975040.8082"));
		TABLA_MORTALIDAD.add(new BigDecimal("973485.5206"));
		TABLA_MORTALIDAD.add(new BigDecimal("971927.3597"));
		TABLA_MORTALIDAD.add(new BigDecimal("970377.1355"));
		TABLA_MORTALIDAD.add(new BigDecimal("968845.3952"));
		TABLA_MORTALIDAD.add(new BigDecimal("967343.3942"));
		TABLA_MORTALIDAD.add(new BigDecimal("965883.2861"));
		TABLA_MORTALIDAD.add(new BigDecimal("964468.9432"));
		TABLA_MORTALIDAD.add(new BigDecimal("963095.7323"));
		TABLA_MORTALIDAD.add(new BigDecimal("961758.9554"));
		TABLA_MORTALIDAD.add(new BigDecimal("960453.4638"));
		TABLA_MORTALIDAD.add(new BigDecimal("959173.6596"));
		TABLA_MORTALIDAD.add(new BigDecimal("957913.5932"));
		TABLA_MORTALIDAD.add(new BigDecimal("956666.5812"));
		TABLA_MORTALIDAD.add(new BigDecimal("955425.976"));
		TABLA_MORTALIDAD.add(new BigDecimal("954184.4"));
		TABLA_MORTALIDAD.add(new BigDecimal("952934.0367"));
		TABLA_MORTALIDAD.add(new BigDecimal("951666.7298"));
		TABLA_MORTALIDAD.add(new BigDecimal("950373.7953"));
		TABLA_MORTALIDAD.add(new BigDecimal("949046.1231"));
		TABLA_MORTALIDAD.add(new BigDecimal("947674.3719"));
		TABLA_MORTALIDAD.add(new BigDecimal("946248.5958"));
		TABLA_MORTALIDAD.add(new BigDecimal("944757.8757"));
		TABLA_MORTALIDAD.add(new BigDecimal("943190.428"));
		TABLA_MORTALIDAD.add(new BigDecimal("941533.6196"));
		TABLA_MORTALIDAD.add(new BigDecimal("939773.5167"));
		TABLA_MORTALIDAD.add(new BigDecimal("937895.5673"));
		TABLA_MORTALIDAD.add(new BigDecimal("935884.2502"));
		TABLA_MORTALIDAD.add(new BigDecimal("933722.732"));
		TABLA_MORTALIDAD.add(new BigDecimal("931391.2263"));
		TABLA_MORTALIDAD.add(new BigDecimal("928866.5041"));
		TABLA_MORTALIDAD.add(new BigDecimal("926122.168"));
		TABLA_MORTALIDAD.add(new BigDecimal("923128.4781"));
		TABLA_MORTALIDAD.add(new BigDecimal("919853.0337"));
		TABLA_MORTALIDAD.add(new BigDecimal("916260.3637"));
		TABLA_MORTALIDAD.add(new BigDecimal("912312.4726"));
		TABLA_MORTALIDAD.add(new BigDecimal("907969.3179"));
		TABLA_MORTALIDAD.add(new BigDecimal("903188.4054"));
		TABLA_MORTALIDAD.add(new BigDecimal("897925.6169"));
		TABLA_MORTALIDAD.add(new BigDecimal("892136.3313"));
		TABLA_MORTALIDAD.add(new BigDecimal("885775.9345"));
		TABLA_MORTALIDAD.add(new BigDecimal("878799.9176"));
		TABLA_MORTALIDAD.add(new BigDecimal("871164.5524"));
		TABLA_MORTALIDAD.add(new BigDecimal("862827.1591"));
		TABLA_MORTALIDAD.add(new BigDecimal("853746.6798"));
		TABLA_MORTALIDAD.add(new BigDecimal("843884.1128"));
		TABLA_MORTALIDAD.add(new BigDecimal("833202.9872"));
		TABLA_MORTALIDAD.add(new BigDecimal("821670.0414"));
		TABLA_MORTALIDAD.add(new BigDecimal("809256.0039"));
		TABLA_MORTALIDAD.add(new BigDecimal("795935.812"));
		TABLA_MORTALIDAD.add(new BigDecimal("781552.7743"));
		TABLA_MORTALIDAD.add(new BigDecimal("765897.2562"));
		TABLA_MORTALIDAD.add(new BigDecimal("748785.8861"));
		TABLA_MORTALIDAD.add(new BigDecimal("730064.8911"));
		TABLA_MORTALIDAD.add(new BigDecimal("709614.5324"));
		TABLA_MORTALIDAD.add(new BigDecimal("687352.931"));
		TABLA_MORTALIDAD.add(new BigDecimal("663240.0403"));
		TABLA_MORTALIDAD.add(new BigDecimal("637280.8252"));
		TABLA_MORTALIDAD.add(new BigDecimal("609527.8825"));
		TABLA_MORTALIDAD.add(new BigDecimal("580082.9315"));
		TABLA_MORTALIDAD.add(new BigDecimal("549097.0476"));
		TABLA_MORTALIDAD.add(new BigDecimal("516769.2334"));
		TABLA_MORTALIDAD.add(new BigDecimal("483343.2558"));
		TABLA_MORTALIDAD.add(new BigDecimal("449103.0262"));
		TABLA_MORTALIDAD.add(new BigDecimal("414365.8054"));
		TABLA_MORTALIDAD.add(new BigDecimal("379474.0084"));
		TABLA_MORTALIDAD.add(new BigDecimal("344785.3786"));
		TABLA_MORTALIDAD.add(new BigDecimal("310662.211"));
		TABLA_MORTALIDAD.add(new BigDecimal("277459.8765"));
		TABLA_MORTALIDAD.add(new BigDecimal("245515.255"));
		TABLA_MORTALIDAD.add(new BigDecimal("215135.5411"));
		TABLA_MORTALIDAD.add(new BigDecimal("186588.2381"));
		TABLA_MORTALIDAD.add(new BigDecimal("160092.6709"));
		TABLA_MORTALIDAD.add(new BigDecimal("135813.7049"));
		TABLA_MORTALIDAD.add(new BigDecimal("113857.9391"));
		TABLA_MORTALIDAD.add(new BigDecimal("94272.55184"));
		TABLA_MORTALIDAD.add(new BigDecimal("77047.00518"));
		TABLA_MORTALIDAD.add(new BigDecimal("62117.19864"));
		TABLA_MORTALIDAD.add(new BigDecimal("49372.01667"));
		TABLA_MORTALIDAD.add(new BigDecimal("38661.56242"));
		TABLA_MORTALIDAD.add(new BigDecimal("29806.61481"));
		TABLA_MORTALIDAD.add(new BigDecimal("22608.58262"));
		TABLA_MORTALIDAD.add(new BigDecimal("16859.3444"));
		TABLA_MORTALIDAD.add(new BigDecimal("12350.35152"));
		TABLA_MORTALIDAD.add(new BigDecimal("8880.51285"));
		TABLA_MORTALIDAD.add(new BigDecimal("6262.509244"));
		TABLA_MORTALIDAD.add(new BigDecimal("4327.358191"));
		TABLA_MORTALIDAD.add(new BigDecimal("2927.207262"));
		TABLA_MORTALIDAD.add(new BigDecimal("1936.464985"));
		TABLA_MORTALIDAD.add(new BigDecimal("1251.514857"));
		TABLA_MORTALIDAD.add(new BigDecimal("789.3140254"));
		TABLA_MORTALIDAD.add(new BigDecimal("485.219431"));
		TABLA_MORTALIDAD.add(new BigDecimal("290.3720476"));
		TABLA_MORTALIDAD.add(new BigDecimal("168.9337242"));
		TABLA_MORTALIDAD.add(new BigDecimal("95.41091245"));
		TABLA_MORTALIDAD.add(new BigDecimal("52.23106295"));
		TABLA_MORTALIDAD.add(new BigDecimal("27.668502"));
		TABLA_MORTALIDAD.add(new BigDecimal("14.15749658"));
		TABLA_MORTALIDAD.add(new BigDecimal("6.9836538"));
		TABLA_MORTALIDAD.add(new BigDecimal("3.31397908"));
		TABLA_MORTALIDAD.add(new BigDecimal("1.50930074"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.65803339"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.27386488"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.10846101"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.04073147"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.01444713"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.00481804"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.00150297"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.00043594"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.00011675"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.00002863"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));

	}
	
	private static final List<BigDecimal> TABLA_VALORES_QX = new ArrayList<BigDecimal>();
	static {
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015951"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0016006"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001595"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015503"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015094"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0014643"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0014238"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001388"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013574"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013325"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013137"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013018"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0012968"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0012995"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013104"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013299"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0013586"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001397"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0014454"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015045"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0015754"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0016591"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0017566"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0018694"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0019983"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0021445"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0023096"));
		TABLA_VALORES_QX.add(new BigDecimal("0.002497"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0027107"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0029545"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0032325"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0035482"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0039057"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0043087"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0047606"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0052655"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0058269"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0064474"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0071294"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0078756"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0086884"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0095704"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0105241"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0115521"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0126571"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0138417"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0151083"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0164598"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0180706"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0200313"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0223416"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0250018"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0280117"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0313714"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0350808"));
		TABLA_VALORES_QX.add(new BigDecimal("0.03914"));
		TABLA_VALORES_QX.add(new BigDecimal("0.043549"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0483078"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0534163"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0588745"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0646826"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0708404"));
		TABLA_VALORES_QX.add(new BigDecimal("0.077348"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0842053"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0914124"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0989693"));
		TABLA_VALORES_QX.add(new BigDecimal("0.106876"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1151324"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1237386"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1326945"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1420002"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1516557"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1616609"));
		TABLA_VALORES_QX.add(new BigDecimal("0.172016"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1827207"));
		TABLA_VALORES_QX.add(new BigDecimal("0.1937753"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2051796"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2169337"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2290375"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2414911"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2542945"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2674477"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2809506"));
		TABLA_VALORES_QX.add(new BigDecimal("0.2948032"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3090057"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3235579"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3384599"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3537116"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3693131"));
		TABLA_VALORES_QX.add(new BigDecimal("0.3852644"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4015655"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4182163"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4352169"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4525672"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4702673"));
		TABLA_VALORES_QX.add(new BigDecimal("0.4883172"));
		TABLA_VALORES_QX.add(new BigDecimal("0.5067169"));
		TABLA_VALORES_QX.add(new BigDecimal("0.5254663"));
		TABLA_VALORES_QX.add(new BigDecimal("0.5445654"));
		TABLA_VALORES_QX.add(new BigDecimal("0.5640144"));
		TABLA_VALORES_QX.add(new BigDecimal("0.5838131"));
		TABLA_VALORES_QX.add(new BigDecimal("0.6039616"));
		TABLA_VALORES_QX.add(new BigDecimal("0.6244598"));
		TABLA_VALORES_QX.add(new BigDecimal("0.6453078"));
		TABLA_VALORES_QX.add(new BigDecimal("0.6665056"));
		TABLA_VALORES_QX.add(new BigDecimal("0.6880532"));
		TABLA_VALORES_QX.add(new BigDecimal("0.7099505"));
		TABLA_VALORES_QX.add(new BigDecimal("0.7321976"));
		TABLA_VALORES_QX.add(new BigDecimal("0.7547944"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
	
	}
	
	@BeforeClass
	public static void init() {
		ModuloBXul010Test.LOG.debug("Inicializando pruebas");
		
		try{
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
			DefinicionesAuxiliares da = new DefinicionesAuxiliares();
			da.setCnegocio(negocio);
			da.setKcarteorig(cartera);
			da.setKmodalidad(modalidad);
			da.setKgarantia(garantia);
			da.setCidentivariab("IFAL");
			da.setGvariable("IFAL");
			da.setGvalor("1");
			
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setGvariable("ID-TEMPORAL");
			da2.setCidentivariab("ID-TEMPORAL");
			da2.setGvalor("01");
			
			DefinicionesAuxiliares da3 = new DefinicionesAuxiliares();
			da3.setCnegocio(negocio);
			da3.setKcarteorig(cartera);
			da3.setKmodalidad(modalidad);
			da3.setKgarantia(garantia);
			da3.setCidentivariab("ID-CRITERIO");
			da3.setGvariable("ID-CRITERIO");
			da3.setGvalor("03");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			definicionesAuxiliaresDao.put(da3.getKey(), da3);
			
			TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1968");
			te.setKtabla(707);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			TablaExperiencia te2 = new TablaExperiencia();
			te2.setKanacimiento("1968");
			te2.setKtabla(707);
			te2.setKsobremort(BigDecimal.ZERO);
			te2.setKsobreries(BigDecimal.ZERO);
			te2.setK2tipovalor("1");
			te2.setGvalor(TABLA_VALORES_QX);
			
			tablaExerienciaDao.put(te.getKey(), te);
			tablaExerienciaDao.put(te2.getKey(), te2);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(707);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
		}catch (Exception e){
			ModuloBXul010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloBXul010Test(){
		Modulo moduloBXul010 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_BXul010);
				
		try{
			TotalFlujoProyeccion BXul010 = (TotalFlujoProyeccion) moduloBXul010.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso, terminal);
			System.out.println(BXul010.getProvbtiproy());
			
			TestCase.assertEquals(BigDecimal.valueOf(1.96467795), BXul010.getProvbtiproy().setScale(8, RoundingMode.HALF_DOWN));
			//TestCase.assertEquals(BigDecimal.ONE, BXul010.getTerminalAnterior());
			//TestCase.assertEquals(BigDecimal.valueOf(0.999765244), BXul010.getTerminalPosterior().setScale(9, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloBXul010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Asegurados asegurados = new Asegurados();
		Duraciones duraciones = new Duraciones();
		Rentas rentas = new Rentas();
		Capitales capitales = new Capitales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2015, 3, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 2, 24, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1968, 2, 7, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecIni = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(7.7));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		bti.setPintertecnI1(BigDecimal.valueOf(6));
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setCsexAseg1("H");
		duraciones.setNdursegano(5);
		duraciones.setNdursegmes(1);
		rentas.setFecIni(fecIni);
		capitales.setIcapact(BigDecimal.valueOf(600));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setAsegurados(asegurados);
		umic.setDuraciones(duraciones);
		umic.setRentas(rentas);
		umic.setCapitales(capitales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		
		bloqueCorriente.setImpFlujoNominal(BigDecimal.valueOf(11783.56786));
		bloqueCorriente.setFechaDevengo(FDEVENGO);
		detalleCorriente.setBloqueFall(bloqueCorriente);
		detalleCorriente.setFechaDesde(FDESDE);
		
		DetalleCorriente detalleCorriente2 = new DetalleCorriente();
		detalleCorriente2.setBloqueVida(bloqueCorriente);
		detalleCorriente2.setFechaDesde(new Timestamp(new GregorianCalendar(2014, 3, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()));
		
		
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente2);
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaDevengo(FDEVENGO);
		
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(2));
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("707");
		tc.setTablaFin("707");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setTablacalc1aseg1("707");
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(3));
		btcUmic.setIndTabExp('T');
		btcUmic.setFecCierre(FCIERRE);
		btcUmic.setTablasConversionAsegurado(tca);
		
		
		return btcUmic;
	}
	
	
}