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
 * Clase que testea el Módulo VBX165
 * 
 * @author ogperez
 *
 */
public class ModuloVBX165Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX165Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
//	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2015, 01, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
//	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 376;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 5029769L;
	
	/**
	 * Lista con valores de mortalidad para los valores:
	 * BASE_TECNICA: BTI
	 * TABLA: 707
	 * W-K2TIPOVALOR: 2
	 * FECHA_NACIMIENTO: 
	 * INTERES: 302
	 * SOBREMORT: 0
	 * PRIESGO:
	 * MODALIDAD: 
	 */
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("100000000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99842200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99684500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99527200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99370100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99213200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99056600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98900300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98744200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98588300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98432700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98277300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98122200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97967300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97812600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97658200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97504100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97348600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97192700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97037700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96884500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96734300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96588300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96446900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96309600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96175900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96045300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95917400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95791400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95666700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95542600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95418400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95293400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95166700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95037400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94904600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94767400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94624900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94475800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94319000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94153400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93977400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93789600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93588400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93372300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93139100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92886700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92612200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92312800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91985300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91626000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91231200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("90796900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("90318800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("89792600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("89213600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("88577600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("87880000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("87116500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("86282700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("85374700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("84388400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("83320300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("82167000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("80925600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("79593600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("78155300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("76589700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("74878600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("73006500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("70961500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("68735300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("66324000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("63728100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("60952800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("58008300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("54909700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("51676900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("48334300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("44910300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("41436600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("37947400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("34478500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("31066200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("27746000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("24551500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("21513600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("18658800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("16009300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("13581400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("11385800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("9427260000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("7704700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("6211720000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("4937200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("3866160000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("2980660000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("2260860000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("1685930000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("1235040000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("888051000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("626251000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("432736000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("292721000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("193646000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("125151000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("78931402537"));
		TABLA_MORTALIDAD.add(new BigDecimal("48521943097"));
		TABLA_MORTALIDAD.add(new BigDecimal("29037204756"));
		TABLA_MORTALIDAD.add(new BigDecimal("16893372421"));
		TABLA_MORTALIDAD.add(new BigDecimal("9541091245"));
		TABLA_MORTALIDAD.add(new BigDecimal("5223106295"));
		TABLA_MORTALIDAD.add(new BigDecimal("2766850200"));
		TABLA_MORTALIDAD.add(new BigDecimal("1415749658"));
		TABLA_MORTALIDAD.add(new BigDecimal("698365380"));
		TABLA_MORTALIDAD.add(new BigDecimal("331397908"));
		TABLA_MORTALIDAD.add(new BigDecimal("150930074"));
		TABLA_MORTALIDAD.add(new BigDecimal("65803339"));
		TABLA_MORTALIDAD.add(new BigDecimal("27386488"));
		TABLA_MORTALIDAD.add(new BigDecimal("10846101"));
		TABLA_MORTALIDAD.add(new BigDecimal("4073147"));
		TABLA_MORTALIDAD.add(new BigDecimal("1444713"));
		TABLA_MORTALIDAD.add(new BigDecimal("481804"));
		TABLA_MORTALIDAD.add(new BigDecimal("150297"));
		TABLA_MORTALIDAD.add(new BigDecimal("43594"));
		TABLA_MORTALIDAD.add(new BigDecimal("11675"));
		TABLA_MORTALIDAD.add(new BigDecimal("2863"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
	}
	
	
	private static final List<BigDecimal> TABLA_MORTALIDAD2 = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD2.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD2.add(new BigDecimal("998421.5"));
		TABLA_MORTALIDAD2.add(new BigDecimal("996845.4917"));
		TABLA_MORTALIDAD2.add(new BigDecimal("995271.9711"));
		TABLA_MORTALIDAD2.add(new BigDecimal("993700.9342"));
		TABLA_MORTALIDAD2.add(new BigDecimal("992132.3773"));
		TABLA_MORTALIDAD2.add(new BigDecimal("990566.2964"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989002.6875"));
		TABLA_MORTALIDAD2.add(new BigDecimal("987441.5467"));
		TABLA_MORTALIDAD2.add(new BigDecimal("985882.8702"));
		TABLA_MORTALIDAD2.add(new BigDecimal("984326.6541"));
		TABLA_MORTALIDAD2.add(new BigDecimal("982772.8945"));
		TABLA_MORTALIDAD2.add(new BigDecimal("981221.5875"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979672.7292"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978126.3158"));
		TABLA_MORTALIDAD2.add(new BigDecimal("976582.3434"));
		TABLA_MORTALIDAD2.add(new BigDecimal("975040.8082"));
		TABLA_MORTALIDAD2.add(new BigDecimal("973485.5206"));
		TABLA_MORTALIDAD2.add(new BigDecimal("971927.3597"));
		TABLA_MORTALIDAD2.add(new BigDecimal("970377.1355"));
		TABLA_MORTALIDAD2.add(new BigDecimal("968845.3952"));
		TABLA_MORTALIDAD2.add(new BigDecimal("967343.3942"));
		TABLA_MORTALIDAD2.add(new BigDecimal("965883.2861"));
		TABLA_MORTALIDAD2.add(new BigDecimal("964468.9432"));
		TABLA_MORTALIDAD2.add(new BigDecimal("963095.7323"));
		TABLA_MORTALIDAD2.add(new BigDecimal("961758.9554"));
		TABLA_MORTALIDAD2.add(new BigDecimal("960453.4638"));
		TABLA_MORTALIDAD2.add(new BigDecimal("959173.6596"));
		TABLA_MORTALIDAD2.add(new BigDecimal("957913.5932"));
		TABLA_MORTALIDAD2.add(new BigDecimal("956666.5812"));
		TABLA_MORTALIDAD2.add(new BigDecimal("955425.976"));
		TABLA_MORTALIDAD2.add(new BigDecimal("954184.4"));
		TABLA_MORTALIDAD2.add(new BigDecimal("952934.0367"));
		TABLA_MORTALIDAD2.add(new BigDecimal("951666.7298"));
		TABLA_MORTALIDAD2.add(new BigDecimal("950373.7953"));
		TABLA_MORTALIDAD2.add(new BigDecimal("949046.1231"));
		TABLA_MORTALIDAD2.add(new BigDecimal("947674.3719"));
		TABLA_MORTALIDAD2.add(new BigDecimal("946248.5958"));
		TABLA_MORTALIDAD2.add(new BigDecimal("944757.8757"));
		TABLA_MORTALIDAD2.add(new BigDecimal("943190.428"));
		TABLA_MORTALIDAD2.add(new BigDecimal("941533.6196"));
		TABLA_MORTALIDAD2.add(new BigDecimal("939773.5167"));
		TABLA_MORTALIDAD2.add(new BigDecimal("937895.5673"));
		TABLA_MORTALIDAD2.add(new BigDecimal("935884.2502"));
		TABLA_MORTALIDAD2.add(new BigDecimal("933722.732"));
		TABLA_MORTALIDAD2.add(new BigDecimal("931391.2263"));
		TABLA_MORTALIDAD2.add(new BigDecimal("928866.5041"));
		TABLA_MORTALIDAD2.add(new BigDecimal("926122.168"));
		TABLA_MORTALIDAD2.add(new BigDecimal("923128.4781"));
		TABLA_MORTALIDAD2.add(new BigDecimal("919853.0337"));
		TABLA_MORTALIDAD2.add(new BigDecimal("916260.3637"));
		TABLA_MORTALIDAD2.add(new BigDecimal("912312.4726"));
		TABLA_MORTALIDAD2.add(new BigDecimal("907969.3179"));
		TABLA_MORTALIDAD2.add(new BigDecimal("903188.4054"));
		TABLA_MORTALIDAD2.add(new BigDecimal("897925.6169"));
		TABLA_MORTALIDAD2.add(new BigDecimal("892136.3313"));
		TABLA_MORTALIDAD2.add(new BigDecimal("885775.9345"));
		TABLA_MORTALIDAD2.add(new BigDecimal("878799.9176"));
		TABLA_MORTALIDAD2.add(new BigDecimal("871164.5524"));
		TABLA_MORTALIDAD2.add(new BigDecimal("862827.1591"));
		TABLA_MORTALIDAD2.add(new BigDecimal("853746.6798"));
		TABLA_MORTALIDAD2.add(new BigDecimal("843884.1128"));
		TABLA_MORTALIDAD2.add(new BigDecimal("833202.9872"));
		TABLA_MORTALIDAD2.add(new BigDecimal("821670.0414"));
		TABLA_MORTALIDAD2.add(new BigDecimal("809256.0039"));
		TABLA_MORTALIDAD2.add(new BigDecimal("795935.812"));
		TABLA_MORTALIDAD2.add(new BigDecimal("781552.7743"));
		TABLA_MORTALIDAD2.add(new BigDecimal("765897.2562"));
		TABLA_MORTALIDAD2.add(new BigDecimal("748785.8861"));
		TABLA_MORTALIDAD2.add(new BigDecimal("730064.8911"));
		TABLA_MORTALIDAD2.add(new BigDecimal("709614.5324"));
		TABLA_MORTALIDAD2.add(new BigDecimal("687352.931"));
		TABLA_MORTALIDAD2.add(new BigDecimal("663240.0403"));
		TABLA_MORTALIDAD2.add(new BigDecimal("637280.8252"));
		TABLA_MORTALIDAD2.add(new BigDecimal("609527.8825"));
		TABLA_MORTALIDAD2.add(new BigDecimal("580082.9315"));
		TABLA_MORTALIDAD2.add(new BigDecimal("549097.0476"));
		TABLA_MORTALIDAD2.add(new BigDecimal("516769.2334"));
		TABLA_MORTALIDAD2.add(new BigDecimal("483343.2558"));
		TABLA_MORTALIDAD2.add(new BigDecimal("449103.0262"));
		TABLA_MORTALIDAD2.add(new BigDecimal("414365.8054"));
		TABLA_MORTALIDAD2.add(new BigDecimal("379474.0084"));
		TABLA_MORTALIDAD2.add(new BigDecimal("344785.3786"));
		TABLA_MORTALIDAD2.add(new BigDecimal("310662.211"));
		TABLA_MORTALIDAD2.add(new BigDecimal("277459.8765"));
		TABLA_MORTALIDAD2.add(new BigDecimal("245515.255"));
		TABLA_MORTALIDAD2.add(new BigDecimal("215135.5411"));
		TABLA_MORTALIDAD2.add(new BigDecimal("186588.2381"));
		TABLA_MORTALIDAD2.add(new BigDecimal("160092.6709"));
		TABLA_MORTALIDAD2.add(new BigDecimal("135813.7049"));
		TABLA_MORTALIDAD2.add(new BigDecimal("113857.9391"));
		TABLA_MORTALIDAD2.add(new BigDecimal("94272.55184"));
		TABLA_MORTALIDAD2.add(new BigDecimal("77047.00518"));
		TABLA_MORTALIDAD2.add(new BigDecimal("62117.19864"));
		TABLA_MORTALIDAD2.add(new BigDecimal("49372.01667"));
		TABLA_MORTALIDAD2.add(new BigDecimal("38661.56242"));
		TABLA_MORTALIDAD2.add(new BigDecimal("29806.61481"));
		TABLA_MORTALIDAD2.add(new BigDecimal("22608.58262"));
		TABLA_MORTALIDAD2.add(new BigDecimal("16859.3444"));
		TABLA_MORTALIDAD2.add(new BigDecimal("12350.35152"));
		TABLA_MORTALIDAD2.add(new BigDecimal("8880.51285"));
		TABLA_MORTALIDAD2.add(new BigDecimal("6262.509244"));
		TABLA_MORTALIDAD2.add(new BigDecimal("4327.358191"));
		TABLA_MORTALIDAD2.add(new BigDecimal("2927.207262"));
		TABLA_MORTALIDAD2.add(new BigDecimal("1936.464985"));
		TABLA_MORTALIDAD2.add(new BigDecimal("1251.514857"));
		TABLA_MORTALIDAD2.add(new BigDecimal("789.3140254"));
		TABLA_MORTALIDAD2.add(new BigDecimal("485.219431"));
		TABLA_MORTALIDAD2.add(new BigDecimal("290.3720476"));
		TABLA_MORTALIDAD2.add(new BigDecimal("168.9337242"));
		TABLA_MORTALIDAD2.add(new BigDecimal("95.41091245"));
		TABLA_MORTALIDAD2.add(new BigDecimal("52.23106295"));
		TABLA_MORTALIDAD2.add(new BigDecimal("27.668502"));
		TABLA_MORTALIDAD2.add(new BigDecimal("14.15749658"));
		TABLA_MORTALIDAD2.add(new BigDecimal("6.9836538"));
		TABLA_MORTALIDAD2.add(new BigDecimal("3.31397908"));
		TABLA_MORTALIDAD2.add(new BigDecimal("1.50930074"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.65803339"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.27386488"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.10846101"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.04073147"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.01444713"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.00481804"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.00150297"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.00043594"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.00011675"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0.00002863"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));

	}
	
	
	@BeforeClass
	public static void init() {
		ModuloVBX165Test.LOG.debug("Inicializando pruebas");
		
		daos();
	}
	
	@Test
	public void moduloVBX165Test(){
		Modulo moduloVBX165 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX165);
				
		try{
			TotalFlujoProyeccion proy = (TotalFlujoProyeccion) moduloVBX165.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			BigDecimal vbx165 = proy.getProvbtiproy();
			
			System.out.println(vbx165);
			
			TestCase.assertEquals(BigDecimal.valueOf(13265.6263), vbx165.setScale(4, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloVBX165Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Duraciones duraciones = new Duraciones();
		Asegurados asegurados = new Asegurados();
		Rentas rentas = new Rentas();
		Primas primas = new Primas();
		
		Timestamp fecefecfini = new Timestamp(new GregorianCalendar(2016, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1967, 5, 21, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg2 = new Timestamp(new GregorianCalendar(1968, 5, 21, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecFinTramo1 = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecIni = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()); 
		
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecini(fecefecfini);
		fechas.setFecefecred(fecefecred);
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
//		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setCnegocio(negocio);
//		datosGenerales.setCtipoaport("U");
		datosGenerales.setCsitupol("VI");
		datosGenerales.setEdifer(1);
		bti.setFecFinTramo1(fecFinTramo1);
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setFnacAseg2(fnacAseg2);
		rentas.setFecIni(fecIni);
		rentas.setPreversion(BigDecimal.valueOf(1));
		rentas.setPrevrenta(BigDecimal.valueOf(1));
		rentas.setTempVit("V");
		rentas.setCpagrenta("2");
		rentas.setRentini(BigDecimal.valueOf(1));
		rentas.setNpergaran(5);
		duraciones.setNdursegano(1);
		primas.setIprimanetaini(BigDecimal.valueOf(5));
		
		umic.setFechas(fechas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setDuraciones(duraciones);
		umic.setAsegurados(asegurados);
		umic.setRentas(rentas);
		umic.setPrimas(primas); 

		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		//detalleCorriente.setFechaHasta(FHASTA);
		//detalleCorriente.setFcierre(FCIERRE);
		proyUmic.add(detalleCorriente);
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		//bloqueCorriente.setFechaDevengo(FDEVENGO);
		
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(3.15));
		itcalc.add(BigDecimal.ZERO);
		
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("707");
		tc.setTablaFin("707");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		tca.add(tabCon);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);
		btcUmic.setGtorosspCap(BigDecimal.ZERO);
		btcUmic.setTablasConversionAsegurado(tca);
		
		return btcUmic;
	}
	
	private static void daos(){
		try{
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();

			DefinicionesAuxiliares da = new DefinicionesAuxiliares();
			da.setCnegocio(negocio);
			da.setKcarteorig(cartera);
			da.setKmodalidad(modalidad);
			da.setKgarantia(garantia);
			da.setGvariable("ID-TEMPORAL");
			da.setCidentivariab("ID-TEMPORAL");
			da.setGvalor("01");
			
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setCidentivariab("ID-CRITERIO");
			da2.setGvariable("ID-CRITERIO");
			da2.setGvalor("03");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			
			
			
			TablaExperienciaDao tablaExperienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1967");
			te.setKinteres(BigDecimal.ZERO);
			te.setKtabla(707);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			TablaExperiencia te2 = new TablaExperiencia();
			te2.setKanacimiento("1968");
			te2.setKinteres(BigDecimal.ZERO);
			te2.setKtabla(707);
			te2.setK2tipovalor("2");
			te2.setGvalor(TABLA_MORTALIDAD2);
			
			tablaExperienciaDao.put(te.getKey(), te);
			tablaExperienciaDao.put(te2.getKey(), te2);
			
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(707);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
			
		}catch (Exception e){
			ModuloVBX165Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}
}