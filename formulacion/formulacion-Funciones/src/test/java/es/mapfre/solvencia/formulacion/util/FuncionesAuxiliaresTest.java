package es.mapfre.solvencia.formulacion.util;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.joda.time.LocalDateTime;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.UtilFechas.Fecha;
import es.mapfre.solvencia.formulacion.util.UtilFechas.FechaFr;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class FuncionesAuxiliaresTest {
	
	private static final Logger LOG = LoggerFactory.getLogger(FuncionesAuxiliaresTest.class);
	
	private static final boolean COMPRIMIR = false;
	private static final String DATA_FILE_NAME = "src/test/resources/Maestro_Host_Pruebas.txt";
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String ZIPPED_DATA = "datos.txt.gz";
		
	
	/**
	 * Lista con valores de experiencia para los valores:
	 * BASE_TECNICA: BTI y ROSSP
	 * TABLA: 740
	 * W-K2TIPOVALOR: 2
	 * FECHA_NACIMIENTO: 1950
	 * INTERES: 0
	 * SOBREMORT: 0
	 * PRIESGO:0
	 * MODALIDAD: 209
	 */
	private static final List<BigDecimal> TABLA_EXPERIENCIA = new ArrayList<BigDecimal>();
	static {
		TABLA_EXPERIENCIA.add(new BigDecimal("1000000"));
		TABLA_EXPERIENCIA.add(new BigDecimal("987844.2"));
		TABLA_EXPERIENCIA.add(new BigDecimal("986680.223"));
		TABLA_EXPERIENCIA.add(new BigDecimal("986072.132"));
		TABLA_EXPERIENCIA.add(new BigDecimal("985541.329"));
		TABLA_EXPERIENCIA.add(new BigDecimal("985099.215"));
		TABLA_EXPERIENCIA.add(new BigDecimal("984677.396"));
		TABLA_EXPERIENCIA.add(new BigDecimal("984300.166"));
		TABLA_EXPERIENCIA.add(new BigDecimal("983941.881"));
		TABLA_EXPERIENCIA.add(new BigDecimal("983614.917"));
		TABLA_EXPERIENCIA.add(new BigDecimal("983303.8"));
		TABLA_EXPERIENCIA.add(new BigDecimal("983009.989"));
		TABLA_EXPERIENCIA.add(new BigDecimal("982695.917"));
		TABLA_EXPERIENCIA.add(new BigDecimal("982376.148"));
		TABLA_EXPERIENCIA.add(new BigDecimal("982001.371"));
		TABLA_EXPERIENCIA.add(new BigDecimal("981551.418"));
		TABLA_EXPERIENCIA.add(new BigDecimal("980950.807"));
		TABLA_EXPERIENCIA.add(new BigDecimal("980124.258"));
		TABLA_EXPERIENCIA.add(new BigDecimal("979067.88"));
		TABLA_EXPERIENCIA.add(new BigDecimal("977843.262"));
		TABLA_EXPERIENCIA.add(new BigDecimal("976487.385"));
		TABLA_EXPERIENCIA.add(new BigDecimal("974992.676"));
		TABLA_EXPERIENCIA.add(new BigDecimal("973469.737"));
		TABLA_EXPERIENCIA.add(new BigDecimal("971866.627"));
		TABLA_EXPERIENCIA.add(new BigDecimal("970206.97"));
		TABLA_EXPERIENCIA.add(new BigDecimal("968513.183"));
		TABLA_EXPERIENCIA.add(new BigDecimal("966864.483"));
		TABLA_EXPERIENCIA.add(new BigDecimal("965211.145"));
		TABLA_EXPERIENCIA.add(new BigDecimal("963525.307"));
		TABLA_EXPERIENCIA.add(new BigDecimal("961808.498"));
		TABLA_EXPERIENCIA.add(new BigDecimal("960097.825"));
		TABLA_EXPERIENCIA.add(new BigDecimal("958458.362"));
		TABLA_EXPERIENCIA.add(new BigDecimal("956894.541"));
		TABLA_EXPERIENCIA.add(new BigDecimal("955325.138"));
		TABLA_EXPERIENCIA.add(new BigDecimal("953777.989"));
		TABLA_EXPERIENCIA.add(new BigDecimal("952268.445"));
		TABLA_EXPERIENCIA.add(new BigDecimal("950811.189"));
		TABLA_EXPERIENCIA.add(new BigDecimal("949413.021"));
		TABLA_EXPERIENCIA.add(new BigDecimal("948049.189"));
		TABLA_EXPERIENCIA.add(new BigDecimal("946606.543"));
		TABLA_EXPERIENCIA.add(new BigDecimal("945053.635"));
		TABLA_EXPERIENCIA.add(new BigDecimal("943432.963"));
		TABLA_EXPERIENCIA.add(new BigDecimal("941719.311"));
		TABLA_EXPERIENCIA.add(new BigDecimal("939916.389"));
		TABLA_EXPERIENCIA.add(new BigDecimal("938053.945"));
		TABLA_EXPERIENCIA.add(new BigDecimal("936093.506"));
		TABLA_EXPERIENCIA.add(new BigDecimal("934008.919"));
		TABLA_EXPERIENCIA.add(new BigDecimal("931757.584"));
		TABLA_EXPERIENCIA.add(new BigDecimal("929434.06"));
		TABLA_EXPERIENCIA.add(new BigDecimal("927040.674"));
		TABLA_EXPERIENCIA.add(new BigDecimal("924298.488"));
		TABLA_EXPERIENCIA.add(new BigDecimal("921265.865"));
		TABLA_EXPERIENCIA.add(new BigDecimal("917932.449"));
		TABLA_EXPERIENCIA.add(new BigDecimal("914368.301"));
		TABLA_EXPERIENCIA.add(new BigDecimal("910196.038"));
		TABLA_EXPERIENCIA.add(new BigDecimal("905857.771"));
		TABLA_EXPERIENCIA.add(new BigDecimal("901281.74"));
		TABLA_EXPERIENCIA.add(new BigDecimal("896494.312"));
		TABLA_EXPERIENCIA.add(new BigDecimal("891398.907"));
		TABLA_EXPERIENCIA.add(new BigDecimal("885968.237"));
		TABLA_EXPERIENCIA.add(new BigDecimal("880318.949"));
		TABLA_EXPERIENCIA.add(new BigDecimal("874090.692"));
		TABLA_EXPERIENCIA.add(new BigDecimal("867410.104"));
		TABLA_EXPERIENCIA.add(new BigDecimal("860356.845"));
		TABLA_EXPERIENCIA.add(new BigDecimal("852912.263"));
		TABLA_EXPERIENCIA.add(new BigDecimal("844839.96"));
		TABLA_EXPERIENCIA.add(new BigDecimal("836049.654"));
		TABLA_EXPERIENCIA.add(new BigDecimal("826798.347"));
		TABLA_EXPERIENCIA.add(new BigDecimal("816905.953"));
		TABLA_EXPERIENCIA.add(new BigDecimal("806301.452"));
		TABLA_EXPERIENCIA.add(new BigDecimal("795078.542"));
		TABLA_EXPERIENCIA.add(new BigDecimal("783311.3"));
		TABLA_EXPERIENCIA.add(new BigDecimal("770783.567"));
		TABLA_EXPERIENCIA.add(new BigDecimal("757232.729"));
		TABLA_EXPERIENCIA.add(new BigDecimal("742656.983"));
		TABLA_EXPERIENCIA.add(new BigDecimal("726996.872"));
		TABLA_EXPERIENCIA.add(new BigDecimal("710283.359"));
		TABLA_EXPERIENCIA.add(new BigDecimal("692623.655"));
		TABLA_EXPERIENCIA.add(new BigDecimal("674033.29"));
		TABLA_EXPERIENCIA.add(new BigDecimal("654463.677"));
		TABLA_EXPERIENCIA.add(new BigDecimal("633785.963"));
		TABLA_EXPERIENCIA.add(new BigDecimal("612150.221"));
		TABLA_EXPERIENCIA.add(new BigDecimal("589363.97"));
		TABLA_EXPERIENCIA.add(new BigDecimal("565556.377"));
		TABLA_EXPERIENCIA.add(new BigDecimal("541136.953"));
		TABLA_EXPERIENCIA.add(new BigDecimal("516471.606"));
		TABLA_EXPERIENCIA.add(new BigDecimal("491020.143"));
		TABLA_EXPERIENCIA.add(new BigDecimal("464707.748"));
		TABLA_EXPERIENCIA.add(new BigDecimal("438217.966"));
		TABLA_EXPERIENCIA.add(new BigDecimal("411369.271"));
		TABLA_EXPERIENCIA.add(new BigDecimal("384087.425"));
		TABLA_EXPERIENCIA.add(new BigDecimal("356558.65"));
		TABLA_EXPERIENCIA.add(new BigDecimal("329067.693"));
		TABLA_EXPERIENCIA.add(new BigDecimal("299878.928"));
		TABLA_EXPERIENCIA.add(new BigDecimal("269386.849"));
		TABLA_EXPERIENCIA.add(new BigDecimal("237895.85"));
		TABLA_EXPERIENCIA.add(new BigDecimal("206149.101"));
		TABLA_EXPERIENCIA.add(new BigDecimal("174649.972"));
		TABLA_EXPERIENCIA.add(new BigDecimal("144002.884"));
		TABLA_EXPERIENCIA.add(new BigDecimal("114895.811"));
		TABLA_EXPERIENCIA.add(new BigDecimal("88008.491"));
		TABLA_EXPERIENCIA.add(new BigDecimal("64092.509"));
		TABLA_EXPERIENCIA.add(new BigDecimal("43269.494"));
		TABLA_EXPERIENCIA.add(new BigDecimal("28230.316"));
		TABLA_EXPERIENCIA.add(new BigDecimal("17733.296"));
		TABLA_EXPERIENCIA.add(new BigDecimal("10679.115"));
		TABLA_EXPERIENCIA.add(new BigDecimal("6134.479"));
		TABLA_EXPERIENCIA.add(new BigDecimal("3341.622"));
		TABLA_EXPERIENCIA.add(new BigDecimal("1714.068"));
		TABLA_EXPERIENCIA.add(new BigDecimal("838.52"));
		TABLA_EXPERIENCIA.add(new BigDecimal("389.321"));
		TABLA_EXPERIENCIA.add(new BigDecimal("170.592"));
		TABLA_EXPERIENCIA.add(new BigDecimal("70.077"));
		TABLA_EXPERIENCIA.add(new BigDecimal("26.774"));
		TABLA_EXPERIENCIA.add(new BigDecimal("9.423"));
		TABLA_EXPERIENCIA.add(new BigDecimal("3.019"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
		TABLA_EXPERIENCIA.add(new BigDecimal("0"));
	}
	
	private static final LimitesCapital limCap = new LimitesCapital();

	private static final List<LimitesCapital> LISTA_LIMITES_CAPITAL = new ArrayList<LimitesCapital>();
	static{
		LISTA_LIMITES_CAPITAL.add(limCap);
	}
	
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
	
	private static final List<BigDecimal> TABLA_MORTALIDAD3 = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD3.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD3.add(new BigDecimal("999251.9"));
		TABLA_MORTALIDAD3.add(new BigDecimal("998504.3597"));
		TABLA_MORTALIDAD3.add(new BigDecimal("997757.3785"));
		TABLA_MORTALIDAD3.add(new BigDecimal("997010.9562"));
		TABLA_MORTALIDAD3.add(new BigDecimal("996265.0924"));
		TABLA_MORTALIDAD3.add(new BigDecimal("995519.7864"));
		TABLA_MORTALIDAD3.add(new BigDecimal("994775.0381"));
		TABLA_MORTALIDAD3.add(new BigDecimal("994030.8469"));
		TABLA_MORTALIDAD3.add(new BigDecimal("993287.2124"));
		TABLA_MORTALIDAD3.add(new BigDecimal("992544.1342"));
		TABLA_MORTALIDAD3.add(new BigDecimal("991801.612"));
		TABLA_MORTALIDAD3.add(new BigDecimal("991059.6452"));
		TABLA_MORTALIDAD3.add(new BigDecimal("990318.2335"));
		TABLA_MORTALIDAD3.add(new BigDecimal("989577.3764"));
		TABLA_MORTALIDAD3.add(new BigDecimal("988837.0736"));
		TABLA_MORTALIDAD3.add(new BigDecimal("988097.3245"));
		TABLA_MORTALIDAD3.add(new BigDecimal("987351.1134"));
		TABLA_MORTALIDAD3.add(new BigDecimal("986596.8759"));
		TABLA_MORTALIDAD3.add(new BigDecimal("985833.1513"));
		TABLA_MORTALIDAD3.add(new BigDecimal("985058.385"));
		TABLA_MORTALIDAD3.add(new BigDecimal("984271.0278"));
		TABLA_MORTALIDAD3.add(new BigDecimal("983469.5359"));
		TABLA_MORTALIDAD3.add(new BigDecimal("982652.3711"));
		TABLA_MORTALIDAD3.add(new BigDecimal("981818.0992"));
		TABLA_MORTALIDAD3.add(new BigDecimal("980965.1939"));
		TABLA_MORTALIDAD3.add(new BigDecimal("980092.1348"));
		TABLA_MORTALIDAD3.add(new BigDecimal("979197.5067"));
		TABLA_MORTALIDAD3.add(new BigDecimal("978279.8028"));
		TABLA_MORTALIDAD3.add(new BigDecimal("977337.4259"));
		TABLA_MORTALIDAD3.add(new BigDecimal("976369.08"));
		TABLA_MORTALIDAD3.add(new BigDecimal("975372.9882"));
		TABLA_MORTALIDAD3.add(new BigDecimal("974343.1894"));
		TABLA_MORTALIDAD3.add(new BigDecimal("973268.294"));
		TABLA_MORTALIDAD3.add(new BigDecimal("972135.9937"));
		TABLA_MORTALIDAD3.add(new BigDecimal("970933.4615"));
		TABLA_MORTALIDAD3.add(new BigDecimal("969646.8776"));
		TABLA_MORTALIDAD3.add(new BigDecimal("968261.9309"));
		TABLA_MORTALIDAD3.add(new BigDecimal("966763.3519"));
		TABLA_MORTALIDAD3.add(new BigDecimal("965135.3224"));
		TABLA_MORTALIDAD3.add(new BigDecimal("963361.4037"));
		TABLA_MORTALIDAD3.add(new BigDecimal("961424.4693"));
		TABLA_MORTALIDAD3.add(new BigDecimal("959306.7396"));
		TABLA_MORTALIDAD3.add(new BigDecimal("956990.0138"));
		TABLA_MORTALIDAD3.add(new BigDecimal("954455.3301"));
		TABLA_MORTALIDAD3.add(new BigDecimal("951683.4964"));
		TABLA_MORTALIDAD3.add(new BigDecimal("948654.8588"));
		TABLA_MORTALIDAD3.add(new BigDecimal("945349.3658"));
		TABLA_MORTALIDAD3.add(new BigDecimal("941746.7339"));
		TABLA_MORTALIDAD3.add(new BigDecimal("937826.4306"));
		TABLA_MORTALIDAD3.add(new BigDecimal("933567.8546"));
		TABLA_MORTALIDAD3.add(new BigDecimal("928950.428"));
		TABLA_MORTALIDAD3.add(new BigDecimal("923953.6036"));
		TABLA_MORTALIDAD3.add(new BigDecimal("918556.9754"));
		TABLA_MORTALIDAD3.add(new BigDecimal("912740.4889"));
		TABLA_MORTALIDAD3.add(new BigDecimal("906484.5656"));
		TABLA_MORTALIDAD3.add(new BigDecimal("899770.1438"));
		TABLA_MORTALIDAD3.add(new BigDecimal("892578.9109"));
		TABLA_MORTALIDAD3.add(new BigDecimal("884893.4494"));
		TABLA_MORTALIDAD3.add(new BigDecimal("876697.3008"));
		TABLA_MORTALIDAD3.add(new BigDecimal("867957.2424"));
		TABLA_MORTALIDAD3.add(new BigDecimal("858620.4528"));
		TABLA_MORTALIDAD3.add(new BigDecimal("848630.1462"));
		TABLA_MORTALIDAD3.add(new BigDecimal("837925.9499"));
		TABLA_MORTALIDAD3.add(new BigDecimal("826443.7668"));
		TABLA_MORTALIDAD3.add(new BigDecimal("814116.4489"));
		TABLA_MORTALIDAD3.add(new BigDecimal("800873.8679"));
		TABLA_MORTALIDAD3.add(new BigDecimal("786643.7809"));
		TABLA_MORTALIDAD3.add(new BigDecimal("771352.5271"));
		TABLA_MORTALIDAD3.add(new BigDecimal("754926.4979"));
		TABLA_MORTALIDAD3.add(new BigDecimal("737293.3777"));
		TABLA_MORTALIDAD3.add(new BigDecimal("718384.0144"));
		TABLA_MORTALIDAD3.add(new BigDecimal("698134.8524"));
		TABLA_MORTALIDAD3.add(new BigDecimal("676490.5775"));
		TABLA_MORTALIDAD3.add(new BigDecimal("653407.3661"));
		TABLA_MORTALIDAD3.add(new BigDecimal("628856.5644"));
		TABLA_MORTALIDAD3.add(new BigDecimal("602828.7572"));
		TABLA_MORTALIDAD3.add(new BigDecimal("575338.3793"));
		TABLA_MORTALIDAD3.add(new BigDecimal("546428.3737"));
		TABLA_MORTALIDAD3.add(new BigDecimal("516174.9297"));
		TABLA_MORTALIDAD3.add(new BigDecimal("484692.0786"));
		TABLA_MORTALIDAD3.add(new BigDecimal("452135.5056"));
		TABLA_MORTALIDAD3.add(new BigDecimal("418705.5106"));
		TABLA_MORTALIDAD3.add(new BigDecimal("384648.2556"));
		TABLA_MORTALIDAD3.add(new BigDecimal("350254.8549"));
		TABLA_MORTALIDAD3.add(new BigDecimal("315857.8317"));
		TABLA_MORTALIDAD3.add(new BigDecimal("281824.4662"));
		TABLA_MORTALIDAD3.add(new BigDecimal("248546.605"));
		TABLA_MORTALIDAD3.add(new BigDecimal("216427.0267"));
		TABLA_MORTALIDAD3.add(new BigDecimal("185862.6825"));
		TABLA_MORTALIDAD3.add(new BigDecimal("157225.6853"));
		TABLA_MORTALIDAD3.add(new BigDecimal("130843.2153"));
		TABLA_MORTALIDAD3.add(new BigDecimal("106978.3418"));
		TABLA_MORTALIDAD3.add(new BigDecimal("85813.46852"));
		TABLA_MORTALIDAD3.add(new BigDecimal("67438.72822"));
		TABLA_MORTALIDAD3.add(new BigDecimal("51846.93472"));
		TABLA_MORTALIDAD3.add(new BigDecimal("38936.22879"));
		TABLA_MORTALIDAD3.add(new BigDecimal("28520.49946"));
		TABLA_MORTALIDAD3.add(new BigDecimal("20346.69544"));
		TABLA_MORTALIDAD3.add(new BigDecimal("14116.88116"));
		TABLA_MORTALIDAD3.add(new BigDecimal("9512.363912"));
		TABLA_MORTALIDAD3.add(new BigDecimal("6216.856802"));
		TABLA_MORTALIDAD3.add(new BigDecimal("3935.990889"));
		TABLA_MORTALIDAD3.add(new BigDecimal("2411.32263"));
		TABLA_MORTALIDAD3.add(new BigDecimal("1428.069899"));
		TABLA_MORTALIDAD3.add(new BigDecimal("816.9151041"));
		TABLA_MORTALIDAD3.add(new BigDecimal("451.07683"));
		TABLA_MORTALIDAD3.add(new BigDecimal("240.304009"));
		TABLA_MORTALIDAD3.add(new BigDecimal("123.4756733"));
		TABLA_MORTALIDAD3.add(new BigDecimal("61.18844399"));
		TABLA_MORTALIDAD3.add(new BigDecimal("29.24613043"));
		TABLA_MORTALIDAD3.add(new BigDecimal("13.48672729"));
		TABLA_MORTALIDAD3.add(new BigDecimal("6.00335501"));
		TABLA_MORTALIDAD3.add(new BigDecimal("2.58118451"));
		TABLA_MORTALIDAD3.add(new BigDecimal("1.07285747"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0.4315081"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0.16812923"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0.06353856"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));
		TABLA_MORTALIDAD3.add(new BigDecimal("0"));


	}
	
	private static final List<BigDecimal> TABLA_MORTALIDAD4 = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD4.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD4.add(new BigDecimal("999050"));
		TABLA_MORTALIDAD4.add(new BigDecimal("998100.9025"));
		TABLA_MORTALIDAD4.add(new BigDecimal("997152.7066"));
		TABLA_MORTALIDAD4.add(new BigDecimal("996205.4116"));
		TABLA_MORTALIDAD4.add(new BigDecimal("995259.0164"));
		TABLA_MORTALIDAD4.add(new BigDecimal("994313.5204"));
		TABLA_MORTALIDAD4.add(new BigDecimal("993368.9225"));
		TABLA_MORTALIDAD4.add(new BigDecimal("992425.222"));
		TABLA_MORTALIDAD4.add(new BigDecimal("991482.4181"));
		TABLA_MORTALIDAD4.add(new BigDecimal("990540.5098"));
		TABLA_MORTALIDAD4.add(new BigDecimal("989599.4963"));
		TABLA_MORTALIDAD4.add(new BigDecimal("988659.3768"));
		TABLA_MORTALIDAD4.add(new BigDecimal("987720.1504"));
		TABLA_MORTALIDAD4.add(new BigDecimal("986781.8162"));
		TABLA_MORTALIDAD4.add(new BigDecimal("985844.3735"));
		TABLA_MORTALIDAD4.add(new BigDecimal("984907.8213"));
		TABLA_MORTALIDAD4.add(new BigDecimal("983968.2193"));
		TABLA_MORTALIDAD4.add(new BigDecimal("983024.5938"));
		TABLA_MORTALIDAD4.add(new BigDecimal("982075.975"));
		TABLA_MORTALIDAD4.add(new BigDecimal("981120.4151"));
		TABLA_MORTALIDAD4.add(new BigDecimal("980156.9549"));
		TABLA_MORTALIDAD4.add(new BigDecimal("979182.6788"));
		TABLA_MORTALIDAD4.add(new BigDecimal("978195.6627"));
		TABLA_MORTALIDAD4.add(new BigDecimal("977193.0122"));
		TABLA_MORTALIDAD4.add(new BigDecimal("976171.8455"));
		TABLA_MORTALIDAD4.add(new BigDecimal("975128.3178"));
		TABLA_MORTALIDAD4.add(new BigDecimal("974058.602"));
		TABLA_MORTALIDAD4.add(new BigDecimal("972958.8898"));
		TABLA_MORTALIDAD4.add(new BigDecimal("971823.4468"));
		TABLA_MORTALIDAD4.add(new BigDecimal("970646.5686"));
		TABLA_MORTALIDAD4.add(new BigDecimal("969422.5833"));
		TABLA_MORTALIDAD4.add(new BigDecimal("968144.8843"));
		TABLA_MORTALIDAD4.add(new BigDecimal("966805.9399"));
		TABLA_MORTALIDAD4.add(new BigDecimal("965397.3037"));
		TABLA_MORTALIDAD4.add(new BigDecimal("963910.5918"));
		TABLA_MORTALIDAD4.add(new BigDecimal("962336.5258"));
		TABLA_MORTALIDAD4.add(new BigDecimal("960663.985"));
		TABLA_MORTALIDAD4.add(new BigDecimal("958881.9533"));
		TABLA_MORTALIDAD4.add(new BigDecimal("956977.6137"));
		TABLA_MORTALIDAD4.add(new BigDecimal("954938.2944"));
		TABLA_MORTALIDAD4.add(new BigDecimal("952749.5758"));
		TABLA_MORTALIDAD4.add(new BigDecimal("950397.2371"));
		TABLA_MORTALIDAD4.add(new BigDecimal("947864.4285"));
		TABLA_MORTALIDAD4.add(new BigDecimal("945133.6311"));
		TABLA_MORTALIDAD4.add(new BigDecimal("942187.6496"));
		TABLA_MORTALIDAD4.add(new BigDecimal("939006.8241"));
		TABLA_MORTALIDAD4.add(new BigDecimal("935570.9981"));
		TABLA_MORTALIDAD4.add(new BigDecimal("931859.5879"));
		TABLA_MORTALIDAD4.add(new BigDecimal("927849.7961"));
		TABLA_MORTALIDAD4.add(new BigDecimal("923517.6654"));
		TABLA_MORTALIDAD4.add(new BigDecimal("918835.4309"));
		TABLA_MORTALIDAD4.add(new BigDecimal("913836.0473"));
		TABLA_MORTALIDAD4.add(new BigDecimal("908485.5372"));
		TABLA_MORTALIDAD4.add(new BigDecimal("902746.6341"));
		TABLA_MORTALIDAD4.add(new BigDecimal("896578.1663"));
		TABLA_MORTALIDAD4.add(new BigDecimal("889935.4187"));
		TABLA_MORTALIDAD4.add(new BigDecimal("882768.7688"));
		TABLA_MORTALIDAD4.add(new BigDecimal("875025.1211"));
		TABLA_MORTALIDAD4.add(new BigDecimal("866647.6306"));
		TABLA_MORTALIDAD4.add(new BigDecimal("857573.8299"));
		TABLA_MORTALIDAD4.add(new BigDecimal("847737.4581"));
		TABLA_MORTALIDAD4.add(new BigDecimal("837067.8345"));
		TABLA_MORTALIDAD4.add(new BigDecimal("825490.3492"));
		TABLA_MORTALIDAD4.add(new BigDecimal("812926.3861"));
		TABLA_MORTALIDAD4.add(new BigDecimal("799293.6106"));
		TABLA_MORTALIDAD4.add(new BigDecimal("784508.2774"));
		TABLA_MORTALIDAD4.add(new BigDecimal("768485.4804"));
		TABLA_MORTALIDAD4.add(new BigDecimal("751139.2261"));
		TABLA_MORTALIDAD4.add(new BigDecimal("732387.0353"));
		TABLA_MORTALIDAD4.add(new BigDecimal("712150.4492"));
		TABLA_MORTALIDAD4.add(new BigDecimal("690359.3576"));
		TABLA_MORTALIDAD4.add(new BigDecimal("666954.1043"));
		TABLA_MORTALIDAD4.add(new BigDecimal("641890.636"));
		TABLA_MORTALIDAD4.add(new BigDecimal("615144.9788"));
		TABLA_MORTALIDAD4.add(new BigDecimal("586719.1294"));
		TABLA_MORTALIDAD4.add(new BigDecimal("556646.8404"));
		TABLA_MORTALIDAD4.add(new BigDecimal("524998.1276"));
		TABLA_MORTALIDAD4.add(new BigDecimal("491888.0707"));
		TABLA_MORTALIDAD4.add(new BigDecimal("457480.5002"));
		TABLA_MORTALIDAD4.add(new BigDecimal("421994.1953"));
		TABLA_MORTALIDAD4.add(new BigDecimal("385704.8044"));
		TABLA_MORTALIDAD4.add(new BigDecimal("348946.3652"));
		TABLA_MORTALIDAD4.add(new BigDecimal("312108.0974"));
		TABLA_MORTALIDAD4.add(new BigDecimal("275628.5909"));
		TABLA_MORTALIDAD4.add(new BigDecimal("239983.7502"));
		TABLA_MORTALIDAD4.add(new BigDecimal("205670.1537"));
		TABLA_MORTALIDAD4.add(new BigDecimal("173183.7302"));
		TABLA_MORTALIDAD4.add(new BigDecimal("142993.1301"));
		TABLA_MORTALIDAD4.add(new BigDecimal("115510.7084"));
		TABLA_MORTALIDAD4.add(new BigDecimal("91063.67557"));
		TABLA_MORTALIDAD4.add(new BigDecimal("69868.0587"));
		TABLA_MORTALIDAD4.add(new BigDecimal("52009.08422"));
		TABLA_MORTALIDAD4.add(new BigDecimal("37432.44617"));
		TABLA_MORTALIDAD4.add(new BigDecimal("25948.50858"));
		TABLA_MORTALIDAD4.add(new BigDecimal("17250.41281"));
		TABLA_MORTALIDAD4.add(new BigDecimal("10944.85217"));
		TABLA_MORTALIDAD4.add(new BigDecimal("6591.405879"));
		TABLA_MORTALIDAD4.add(new BigDecimal("3744.808379"));
		TABLA_MORTALIDAD4.add(new BigDecimal("1993.058171"));
		TABLA_MORTALIDAD4.add(new BigDecimal("985.7306962"));
		TABLA_MORTALIDAD4.add(new BigDecimal("448.8475438"));
		TABLA_MORTALIDAD4.add(new BigDecimal("186.1191225"));
		TABLA_MORTALIDAD4.add(new BigDecimal("69.3684581"));
		TABLA_MORTALIDAD4.add(new BigDecimal("22.8710581"));
		TABLA_MORTALIDAD4.add(new BigDecimal("6.5383094"));
		TABLA_MORTALIDAD4.add(new BigDecimal("1.5788775"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0.3106978"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
		TABLA_MORTALIDAD4.add(new BigDecimal("0"));
	}
	
	private static final List<BigDecimal> TABLA_MORTALIDAD5 = new ArrayList<BigDecimal>();
	static{
		TABLA_MORTALIDAD5.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD5.add(new BigDecimal("994193"));
		TABLA_MORTALIDAD5.add(new BigDecimal("993777.4273"));
		TABLA_MORTALIDAD5.add(new BigDecimal("993430.599"));
		TABLA_MORTALIDAD5.add(new BigDecimal("993145.4844"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992911.1021"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992712.5199"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992536.8098"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992373.0412"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992215.2539"));
		TABLA_MORTALIDAD5.add(new BigDecimal("992057.4916"));
		TABLA_MORTALIDAD5.add(new BigDecimal("991889.8339"));
		TABLA_MORTALIDAD5.add(new BigDecimal("991700.383"));
		TABLA_MORTALIDAD5.add(new BigDecimal("991470.3085"));
		TABLA_MORTALIDAD5.add(new BigDecimal("991178.8162"));
		TABLA_MORTALIDAD5.add(new BigDecimal("990803.1594"));
		TABLA_MORTALIDAD5.add(new BigDecimal("990321.6291"));
		TABLA_MORTALIDAD5.add(new BigDecimal("989723.4748"));
		TABLA_MORTALIDAD5.add(new BigDecimal("989010.8739"));
		TABLA_MORTALIDAD5.add(new BigDecimal("988299.7751"));
		TABLA_MORTALIDAD5.add(new BigDecimal("987576.3397"));
		TABLA_MORTALIDAD5.add(new BigDecimal("986837.6326"));
		TABLA_MORTALIDAD5.add(new BigDecimal("986082.7018"));
		TABLA_MORTALIDAD5.add(new BigDecimal("985308.6269"));
		TABLA_MORTALIDAD5.add(new BigDecimal("984517.424"));
		TABLA_MORTALIDAD5.add(new BigDecimal("983711.1043"));
		TABLA_MORTALIDAD5.add(new BigDecimal("982894.6241"));
		TABLA_MORTALIDAD5.add(new BigDecimal("982077.8386"));
		TABLA_MORTALIDAD5.add(new BigDecimal("981269.5886"));
		TABLA_MORTALIDAD5.add(new BigDecimal("980477.704"));
		TABLA_MORTALIDAD5.add(new BigDecimal("979708.029"));
		TABLA_MORTALIDAD5.add(new BigDecimal("978956.5929"));
		TABLA_MORTALIDAD5.add(new BigDecimal("978217.4807"));
		TABLA_MORTALIDAD5.add(new BigDecimal("977478.9265"));
		TABLA_MORTALIDAD5.add(new BigDecimal("976722.3578"));
		TABLA_MORTALIDAD5.add(new BigDecimal("975923.3989"));
		TABLA_MORTALIDAD5.add(new BigDecimal("975056.779"));
		TABLA_MORTALIDAD5.add(new BigDecimal("974107.0737"));
		TABLA_MORTALIDAD5.add(new BigDecimal("973064.7791"));
		TABLA_MORTALIDAD5.add(new BigDecimal("971926.2933"));
		TABLA_MORTALIDAD5.add(new BigDecimal("970688.0592"));
		TABLA_MORTALIDAD5.add(new BigDecimal("969339.7735"));
		TABLA_MORTALIDAD5.add(new BigDecimal("967856.6836"));
		TABLA_MORTALIDAD5.add(new BigDecimal("966201.6487"));
		TABLA_MORTALIDAD5.add(new BigDecimal("964339.7781"));
		TABLA_MORTALIDAD5.add(new BigDecimal("962244.2678"));
		TABLA_MORTALIDAD5.add(new BigDecimal("959897.354"));
		TABLA_MORTALIDAD5.add(new BigDecimal("957279.7139"));
		TABLA_MORTALIDAD5.add(new BigDecimal("954361.9254"));
		TABLA_MORTALIDAD5.add(new BigDecimal("951123.7754"));
		TABLA_MORTALIDAD5.add(new BigDecimal("947534.2342"));
		TABLA_MORTALIDAD5.add(new BigDecimal("943566.9084"));
		TABLA_MORTALIDAD5.add(new BigDecimal("939194.4193"));
		TABLA_MORTALIDAD5.add(new BigDecimal("934412.0414"));
		TABLA_MORTALIDAD5.add(new BigDecimal("929205.4975"));
		TABLA_MORTALIDAD5.add(new BigDecimal("923542.9192"));
		TABLA_MORTALIDAD5.add(new BigDecimal("917377.3466"));
		TABLA_MORTALIDAD5.add(new BigDecimal("910680.492"));
		TABLA_MORTALIDAD5.add(new BigDecimal("903432.386"));
		TABLA_MORTALIDAD5.add(new BigDecimal("895661.0606"));
		TABLA_MORTALIDAD5.add(new BigDecimal("887409.3352"));
		TABLA_MORTALIDAD5.add(new BigDecimal("878718.9356"));
		TABLA_MORTALIDAD5.add(new BigDecimal("869624.1946"));
		TABLA_MORTALIDAD5.add(new BigDecimal("860152.2479"));
		TABLA_MORTALIDAD5.add(new BigDecimal("850302.6445"));
		TABLA_MORTALIDAD5.add(new BigDecimal("840055.6473"));
		TABLA_MORTALIDAD5.add(new BigDecimal("829384.4204"));
		TABLA_MORTALIDAD5.add(new BigDecimal("817724.1049"));
		TABLA_MORTALIDAD5.add(new BigDecimal("804915.2745"));
		TABLA_MORTALIDAD5.add(new BigDecimal("790779.3525"));
		TABLA_MORTALIDAD5.add(new BigDecimal("775116.3858"));
		TABLA_MORTALIDAD5.add(new BigDecimal("757707.2718"));
		TABLA_MORTALIDAD5.add(new BigDecimal("738306.1771"));
		TABLA_MORTALIDAD5.add(new BigDecimal("716633.9376"));
		TABLA_MORTALIDAD5.add(new BigDecimal("692388.0616"));
		TABLA_MORTALIDAD5.add(new BigDecimal("665245.0648"));
		TABLA_MORTALIDAD5.add(new BigDecimal("634885.2758"));
		TABLA_MORTALIDAD5.add(new BigDecimal("601017.3207"));
		TABLA_MORTALIDAD5.add(new BigDecimal("563420.6822"));
		TABLA_MORTALIDAD5.add(new BigDecimal("521991.2326"));
		TABLA_MORTALIDAD5.add(new BigDecimal("476814.4574"));
		TABLA_MORTALIDAD5.add(new BigDecimal("430652.1425"));
		TABLA_MORTALIDAD5.add(new BigDecimal("384064.6244"));
		TABLA_MORTALIDAD5.add(new BigDecimal("337712.633"));
		TABLA_MORTALIDAD5.add(new BigDecimal("292318.314"));
		TABLA_MORTALIDAD5.add(new BigDecimal("248621.4032"));
		TABLA_MORTALIDAD5.add(new BigDecimal("207338.0678"));
		TABLA_MORTALIDAD5.add(new BigDecimal("169147.4324"));
		TABLA_MORTALIDAD5.add(new BigDecimal("134654.0422"));
		TABLA_MORTALIDAD5.add(new BigDecimal("104336.6846"));
		TABLA_MORTALIDAD5.add(new BigDecimal("78492.0705"));
		TABLA_MORTALIDAD5.add(new BigDecimal("57192.61924"));
		TABLA_MORTALIDAD5.add(new BigDecimal("40273.72704"));
		TABLA_MORTALIDAD5.add(new BigDecimal("27351.74062"));
		TABLA_MORTALIDAD5.add(new BigDecimal("17881.41924"));
		TABLA_MORTALIDAD5.add(new BigDecimal("11232.48172"));
		TABLA_MORTALIDAD5.add(new BigDecimal("6767.008612"));
		TABLA_MORTALIDAD5.add(new BigDecimal("3902.290254"));
		TABLA_MORTALIDAD5.add(new BigDecimal("2149.494639"));
		TABLA_MORTALIDAD5.add(new BigDecimal("1128.409453"));
		TABLA_MORTALIDAD5.add(new BigDecimal("563.1688465"));
		TABLA_MORTALIDAD5.add(new BigDecimal("266.4869928"));
		TABLA_MORTALIDAD5.add(new BigDecimal("110.8913669"));
		TABLA_MORTALIDAD5.add(new BigDecimal("42.2777772"));
		TABLA_MORTALIDAD5.add(new BigDecimal("14.592217"));
		TABLA_MORTALIDAD5.add(new BigDecimal("4.491353"));
		TABLA_MORTALIDAD5.add(new BigDecimal("1.2089015"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.2771576"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.0521377"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.0075995"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.0007771"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.0000448"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0.0000005"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));
		TABLA_MORTALIDAD5.add(new BigDecimal("0"));

	}
	
	private static final List<BigDecimal> TABLA_MORTALIDAD6 = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD6.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD6.add(new BigDecimal("998921"));
		TABLA_MORTALIDAD6.add(new BigDecimal("997843.164"));
		TABLA_MORTALIDAD6.add(new BigDecimal("996766.491"));
		TABLA_MORTALIDAD6.add(new BigDecimal("995690.98"));
		TABLA_MORTALIDAD6.add(new BigDecimal("994616.629"));
		TABLA_MORTALIDAD6.add(new BigDecimal("993543.438"));
		TABLA_MORTALIDAD6.add(new BigDecimal("992471.405"));
		TABLA_MORTALIDAD6.add(new BigDecimal("991400.528"));
		TABLA_MORTALIDAD6.add(new BigDecimal("990330.807"));
		TABLA_MORTALIDAD6.add(new BigDecimal("989262.24"));
		TABLA_MORTALIDAD6.add(new BigDecimal("988194.826"));
		TABLA_MORTALIDAD6.add(new BigDecimal("987128.564"));
		TABLA_MORTALIDAD6.add(new BigDecimal("986063.452"));
		TABLA_MORTALIDAD6.add(new BigDecimal("984999.49"));
		TABLA_MORTALIDAD6.add(new BigDecimal("983936.676"));
		TABLA_MORTALIDAD6.add(new BigDecimal("982875.008"));
		TABLA_MORTALIDAD6.add(new BigDecimal("981802.495"));
		TABLA_MORTALIDAD6.add(new BigDecimal("980719.174"));
		TABLA_MORTALIDAD6.add(new BigDecimal("979625.084"));
		TABLA_MORTALIDAD6.add(new BigDecimal("978520.263"));
		TABLA_MORTALIDAD6.add(new BigDecimal("977404.75"));
		TABLA_MORTALIDAD6.add(new BigDecimal("976278.584"));
		TABLA_MORTALIDAD6.add(new BigDecimal("975141.805"));
		TABLA_MORTALIDAD6.add(new BigDecimal("973994.453"));
		TABLA_MORTALIDAD6.add(new BigDecimal("972836.568"));
		TABLA_MORTALIDAD6.add(new BigDecimal("971668.191"));
		TABLA_MORTALIDAD6.add(new BigDecimal("970489.363"));
		TABLA_MORTALIDAD6.add(new BigDecimal("969300.125"));
		TABLA_MORTALIDAD6.add(new BigDecimal("968100.519"));
		TABLA_MORTALIDAD6.add(new BigDecimal("966890.587"));
		TABLA_MORTALIDAD6.add(new BigDecimal("965670.371"));
		TABLA_MORTALIDAD6.add(new BigDecimal("964438.272"));
		TABLA_MORTALIDAD6.add(new BigDecimal("963185.177"));
		TABLA_MORTALIDAD6.add(new BigDecimal("961894.894"));
		TABLA_MORTALIDAD6.add(new BigDecimal("960549.876"));
		TABLA_MORTALIDAD6.add(new BigDecimal("959131.24"));
		TABLA_MORTALIDAD6.add(new BigDecimal("957618.69"));
		TABLA_MORTALIDAD6.add(new BigDecimal("955990.738"));
		TABLA_MORTALIDAD6.add(new BigDecimal("954224.641"));
		TABLA_MORTALIDAD6.add(new BigDecimal("952296.248"));
		TABLA_MORTALIDAD6.add(new BigDecimal("950180.436"));
		TABLA_MORTALIDAD6.add(new BigDecimal("947850.879"));
		TABLA_MORTALIDAD6.add(new BigDecimal("945280.213"));
		TABLA_MORTALIDAD6.add(new BigDecimal("942440.119"));
		TABLA_MORTALIDAD6.add(new BigDecimal("939301.322"));
		TABLA_MORTALIDAD6.add(new BigDecimal("935833.985"));
		TABLA_MORTALIDAD6.add(new BigDecimal("932007.453"));
		TABLA_MORTALIDAD6.add(new BigDecimal("927790.585"));
		TABLA_MORTALIDAD6.add(new BigDecimal("923152.003"));
		TABLA_MORTALIDAD6.add(new BigDecimal("918060.173"));
		TABLA_MORTALIDAD6.add(new BigDecimal("912465.698"));
		TABLA_MORTALIDAD6.add(new BigDecimal("906347.068"));
		TABLA_MORTALIDAD6.add(new BigDecimal("899656.233"));
		TABLA_MORTALIDAD6.add(new BigDecimal("892341.668"));
		TABLA_MORTALIDAD6.add(new BigDecimal("884348.428"));
		TABLA_MORTALIDAD6.add(new BigDecimal("875618.229"));
		TABLA_MORTALIDAD6.add(new BigDecimal("866089.576"));
		TABLA_MORTALIDAD6.add(new BigDecimal("855698.06"));
		TABLA_MORTALIDAD6.add(new BigDecimal("844376.576"));
		TABLA_MORTALIDAD6.add(new BigDecimal("832055.94"));
		TABLA_MORTALIDAD6.add(new BigDecimal("818665.83"));
		TABLA_MORTALIDAD6.add(new BigDecimal("804135.33"));
		TABLA_MORTALIDAD6.add(new BigDecimal("788394.381"));
		TABLA_MORTALIDAD6.add(new BigDecimal("771375.154"));
		TABLA_MORTALIDAD6.add(new BigDecimal("753013.957"));
		TABLA_MORTALIDAD6.add(new BigDecimal("733253.139"));
		TABLA_MORTALIDAD6.add(new BigDecimal("712043.865"));
		TABLA_MORTALIDAD6.add(new BigDecimal("689348.82"));
		TABLA_MORTALIDAD6.add(new BigDecimal("665145.369"));
		TABLA_MORTALIDAD6.add(new BigDecimal("639429.319"));
		TABLA_MORTALIDAD6.add(new BigDecimal("612218.404"));
		TABLA_MORTALIDAD6.add(new BigDecimal("583556.481"));
		TABLA_MORTALIDAD6.add(new BigDecimal("553517.211"));
		TABLA_MORTALIDAD6.add(new BigDecimal("522207.787"));
		TABLA_MORTALIDAD6.add(new BigDecimal("489771.999"));
		TABLA_MORTALIDAD6.add(new BigDecimal("456392.519"));
		TABLA_MORTALIDAD6.add(new BigDecimal("422291.92"));
		TABLA_MORTALIDAD6.add(new BigDecimal("387732.014"));
		TABLA_MORTALIDAD6.add(new BigDecimal("353011.349"));
		TABLA_MORTALIDAD6.add(new BigDecimal("318460.116"));
		TABLA_MORTALIDAD6.add(new BigDecimal("284432.716"));
		TABLA_MORTALIDAD6.add(new BigDecimal("251297.414"));
		TABLA_MORTALIDAD6.add(new BigDecimal("219423.755"));
		TABLA_MORTALIDAD6.add(new BigDecimal("189167.83"));
		TABLA_MORTALIDAD6.add(new BigDecimal("160856.386"));
		TABLA_MORTALIDAD6.add(new BigDecimal("134770.853"));
		TABLA_MORTALIDAD6.add(new BigDecimal("111132.49"));
		TABLA_MORTALIDAD6.add(new BigDecimal("90090.342"));
		TABLA_MORTALIDAD6.add(new BigDecimal("71713.192"));
		TABLA_MORTALIDAD6.add(new BigDecimal("55986.762"));
		TABLA_MORTALIDAD6.add(new BigDecimal("42816.716"));
		TABLA_MORTALIDAD6.add(new BigDecimal("32037.475"));
		TABLA_MORTALIDAD6.add(new BigDecimal("23426.087"));
		TABLA_MORTALIDAD6.add(new BigDecimal("16719.655"));
		TABLA_MORTALIDAD6.add(new BigDecimal("11634.471"));
		TABLA_MORTALIDAD6.add(new BigDecimal("7884.708"));
		TABLA_MORTALIDAD6.add(new BigDecimal("5198.777"));
		TABLA_MORTALIDAD6.add(new BigDecimal("3331.849"));
		TABLA_MORTALIDAD6.add(new BigDecimal("2073.823"));
		TABLA_MORTALIDAD6.add(new BigDecimal("1252.683"));
		TABLA_MORTALIDAD6.add(new BigDecimal("733.884"));
		TABLA_MORTALIDAD6.add(new BigDecimal("416.793"));
		TABLA_MORTALIDAD6.add(new BigDecimal("229.389"));
		TABLA_MORTALIDAD6.add(new BigDecimal("122.32"));
		TABLA_MORTALIDAD6.add(new BigDecimal("63.194"));
		TABLA_MORTALIDAD6.add(new BigDecimal("31.634"));
		TABLA_MORTALIDAD6.add(new BigDecimal("15.347"));
		TABLA_MORTALIDAD6.add(new BigDecimal("7.219"));
		TABLA_MORTALIDAD6.add(new BigDecimal("3.294"));
		TABLA_MORTALIDAD6.add(new BigDecimal("1.459"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.628"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.263"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.107"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.042"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.016"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.006"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0.002"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));
		TABLA_MORTALIDAD6.add(new BigDecimal("0"));

	}
	
	private static final List<BigDecimal> TABLA_QXVALORES = new ArrayList<BigDecimal>();
	static {
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015951"));
		TABLA_QXVALORES.add(new BigDecimal("0.0016006"));
		TABLA_QXVALORES.add(new BigDecimal("0.001595"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015785"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015503"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015094"));
		TABLA_QXVALORES.add(new BigDecimal("0.0014643"));
		TABLA_QXVALORES.add(new BigDecimal("0.0014238"));
		TABLA_QXVALORES.add(new BigDecimal("0.001388"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013574"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013325"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013137"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013018"));
		TABLA_QXVALORES.add(new BigDecimal("0.0012968"));
		TABLA_QXVALORES.add(new BigDecimal("0.0012995"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013104"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013299"));
		TABLA_QXVALORES.add(new BigDecimal("0.0013586"));
		TABLA_QXVALORES.add(new BigDecimal("0.001397"));
		TABLA_QXVALORES.add(new BigDecimal("0.0014454"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015045"));
		TABLA_QXVALORES.add(new BigDecimal("0.0015754"));
		TABLA_QXVALORES.add(new BigDecimal("0.0016591"));
		TABLA_QXVALORES.add(new BigDecimal("0.0017566"));
		TABLA_QXVALORES.add(new BigDecimal("0.0018694"));
		TABLA_QXVALORES.add(new BigDecimal("0.0019983"));
		TABLA_QXVALORES.add(new BigDecimal("0.0021445"));
		TABLA_QXVALORES.add(new BigDecimal("0.0023096"));
		TABLA_QXVALORES.add(new BigDecimal("0.002497"));
		TABLA_QXVALORES.add(new BigDecimal("0.0027107"));
		TABLA_QXVALORES.add(new BigDecimal("0.0029545"));
		TABLA_QXVALORES.add(new BigDecimal("0.0032325"));
		TABLA_QXVALORES.add(new BigDecimal("0.0035482"));
		TABLA_QXVALORES.add(new BigDecimal("0.0039057"));
		TABLA_QXVALORES.add(new BigDecimal("0.0043087"));
		TABLA_QXVALORES.add(new BigDecimal("0.0047606"));
		TABLA_QXVALORES.add(new BigDecimal("0.0052655"));
		TABLA_QXVALORES.add(new BigDecimal("0.0058269"));
		TABLA_QXVALORES.add(new BigDecimal("0.0064474"));
		TABLA_QXVALORES.add(new BigDecimal("0.0071294"));
		TABLA_QXVALORES.add(new BigDecimal("0.0078756"));
		TABLA_QXVALORES.add(new BigDecimal("0.0086884"));
		TABLA_QXVALORES.add(new BigDecimal("0.0095704"));
		TABLA_QXVALORES.add(new BigDecimal("0.0105241"));
		TABLA_QXVALORES.add(new BigDecimal("0.0115521"));
		TABLA_QXVALORES.add(new BigDecimal("0.0126571"));
		TABLA_QXVALORES.add(new BigDecimal("0.0138417"));
		TABLA_QXVALORES.add(new BigDecimal("0.0151083"));
		TABLA_QXVALORES.add(new BigDecimal("0.0164598"));
		TABLA_QXVALORES.add(new BigDecimal("0.0180706"));
		TABLA_QXVALORES.add(new BigDecimal("0.0200313"));
		TABLA_QXVALORES.add(new BigDecimal("0.0223416"));
		TABLA_QXVALORES.add(new BigDecimal("0.0250018"));
		TABLA_QXVALORES.add(new BigDecimal("0.0280117"));
		TABLA_QXVALORES.add(new BigDecimal("0.0313714"));
		TABLA_QXVALORES.add(new BigDecimal("0.0350808"));
		TABLA_QXVALORES.add(new BigDecimal("0.03914"));
		TABLA_QXVALORES.add(new BigDecimal("0.043549"));
		TABLA_QXVALORES.add(new BigDecimal("0.0483078"));
		TABLA_QXVALORES.add(new BigDecimal("0.0534163"));
		TABLA_QXVALORES.add(new BigDecimal("0.0588745"));
		TABLA_QXVALORES.add(new BigDecimal("0.0646826"));
		TABLA_QXVALORES.add(new BigDecimal("0.0708404"));
		TABLA_QXVALORES.add(new BigDecimal("0.077348"));
		TABLA_QXVALORES.add(new BigDecimal("0.0842053"));
		TABLA_QXVALORES.add(new BigDecimal("0.0914124"));
		TABLA_QXVALORES.add(new BigDecimal("0.0989693"));
		TABLA_QXVALORES.add(new BigDecimal("0.106876"));
		TABLA_QXVALORES.add(new BigDecimal("0.1151324"));
		TABLA_QXVALORES.add(new BigDecimal("0.1237386"));
		TABLA_QXVALORES.add(new BigDecimal("0.1326945"));
		TABLA_QXVALORES.add(new BigDecimal("0.1420002"));
		TABLA_QXVALORES.add(new BigDecimal("0.1516557"));
		TABLA_QXVALORES.add(new BigDecimal("0.1616609"));
		TABLA_QXVALORES.add(new BigDecimal("0.172016"));
		TABLA_QXVALORES.add(new BigDecimal("0.1827207"));
		TABLA_QXVALORES.add(new BigDecimal("0.1937753"));
		TABLA_QXVALORES.add(new BigDecimal("0.2051796"));
		TABLA_QXVALORES.add(new BigDecimal("0.2169337"));
		TABLA_QXVALORES.add(new BigDecimal("0.2290375"));
		TABLA_QXVALORES.add(new BigDecimal("0.2414911"));
		TABLA_QXVALORES.add(new BigDecimal("0.2542945"));
		TABLA_QXVALORES.add(new BigDecimal("0.2674477"));
		TABLA_QXVALORES.add(new BigDecimal("0.2809506"));
		TABLA_QXVALORES.add(new BigDecimal("0.2948032"));
		TABLA_QXVALORES.add(new BigDecimal("0.3090057"));
		TABLA_QXVALORES.add(new BigDecimal("0.3235579"));
		TABLA_QXVALORES.add(new BigDecimal("0.3384599"));
		TABLA_QXVALORES.add(new BigDecimal("0.3537116"));
		TABLA_QXVALORES.add(new BigDecimal("0.3693131"));
		TABLA_QXVALORES.add(new BigDecimal("0.3852644"));
		TABLA_QXVALORES.add(new BigDecimal("0.4015655"));
		TABLA_QXVALORES.add(new BigDecimal("0.4182163"));
		TABLA_QXVALORES.add(new BigDecimal("0.4352169"));
		TABLA_QXVALORES.add(new BigDecimal("0.4525672"));
		TABLA_QXVALORES.add(new BigDecimal("0.4702673"));
		TABLA_QXVALORES.add(new BigDecimal("0.4883172"));
		TABLA_QXVALORES.add(new BigDecimal("0.5067169"));
		TABLA_QXVALORES.add(new BigDecimal("0.5254663"));
		TABLA_QXVALORES.add(new BigDecimal("0.5445654"));
		TABLA_QXVALORES.add(new BigDecimal("0.5640144"));
		TABLA_QXVALORES.add(new BigDecimal("0.5838131"));
		TABLA_QXVALORES.add(new BigDecimal("0.6039616"));
		TABLA_QXVALORES.add(new BigDecimal("0.6244598"));
		TABLA_QXVALORES.add(new BigDecimal("0.6453078"));
		TABLA_QXVALORES.add(new BigDecimal("0.6665056"));
		TABLA_QXVALORES.add(new BigDecimal("0.6880532"));
		TABLA_QXVALORES.add(new BigDecimal("0.7099505"));
		TABLA_QXVALORES.add(new BigDecimal("0.7321976"));
		TABLA_QXVALORES.add(new BigDecimal("0.7547944"));
		TABLA_QXVALORES.add(new BigDecimal("1"));
		TABLA_QXVALORES.add(new BigDecimal("1"));
		TABLA_QXVALORES.add(new BigDecimal("1"));
		TABLA_QXVALORES.add(new BigDecimal("1"));

	}
	
	private static final List<BigDecimal> VALORES_LX = new ArrayList<BigDecimal>();
	static {
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.65"));
		VALORES_LX.add(new BigDecimal("0.75"));
		VALORES_LX.add(new BigDecimal("0.75"));
		VALORES_LX.add(new BigDecimal("0.75"));
		VALORES_LX.add(new BigDecimal("0.75"));
		VALORES_LX.add(new BigDecimal("0.75"));
		VALORES_LX.add(new BigDecimal("0.85"));
		VALORES_LX.add(new BigDecimal("0.85"));
		VALORES_LX.add(new BigDecimal("0.85"));
		VALORES_LX.add(new BigDecimal("0.85"));
		VALORES_LX.add(new BigDecimal("0.85"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));
		VALORES_LX.add(new BigDecimal("1"));

	}
	
	private static final String CRITERIO_INTERES = "01";
	private static final String CRITERIO_FECHA_01 = ConstantsFunciones.CTE_CRI_FECHA_01;
	private static final String CRITERIO_FECHA_02 = ConstantsFunciones.CTE_CRI_FECHA_02;
	private static final String CRITERIO_EDAD_02 = "02";
	private static final String CRITERIO_EDAD_03 = "03";
	private static final String CRITERIO_EDAD_06 = "06";
	private static final BigDecimal PORCEN_REVAL_REN = BigDecimal.ZERO;
	private static final String MOD_BETA = "CRP";
	private static final String TIPO_CALCULO = ConstantsFunciones.CTE_CAL_PRORRATA;
	private static final String CSITUPOL = ConstantsFunciones.CTE_POL_NO_RED;
	private static final String CTIPOAPORT = ConstantsFunciones.CTE_APOR_UNICA;
	private static final Integer VAR_DIFER = ConstantsFunciones.CTE_12;
	private static final Integer MESES_COMPLETOS = ConstantsFunciones.CTE_2;
	private static final Integer DUR_PRIMER_TRA = ConstantsFunciones.CTE_99;
	private static final Integer VAR_N = ConstantsFunciones.CTE_11;
	private static final BigDecimal VAR_I1 = new BigDecimal("26000.0");
	private static final BigDecimal VAR_I1_PORCENTAJE_MAS_UNO = BigDecimal.ONE.add(VAR_I1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
	private static final BigDecimal VAR_I2 = new BigDecimal("22500.0");
	private static final BigDecimal VAR_I2_PORCENTAJE_MAS_UNO = BigDecimal.ONE.add(VAR_I2.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
	private static final BigDecimal VAR_I1_BTI = new BigDecimal("26000.0");
	private static final BigDecimal VAR_I2_BTI = new BigDecimal("22500.0");
	private static final BigDecimal VAR_PU = new BigDecimal("107727.28");
	private static final BigDecimal VAR_PNA0 = new BigDecimal("107727.28");
	private static final Integer VAR_M = ConstantsFunciones.CTE_1;
	private static final BigDecimal VAR_M_BIG_DECIMAL = BigDecimal.ONE;
	private static final Integer VAR_K = ConstantsFunciones.CTE_2;
	private static final String CTE_PROY_VIDA = "PROY_VIDA";
	private static final String CTE_BTI = "BTI";
	private static final String CTE_S = "S";
	private static final Integer ITERACION = 10;
	private static final String NOMBRE_VZC = "VZC";
	private static final String NOMBRE_ACT001 = "ACT001";
	private static final boolean CTE_TRUE = true;
	
	// TODO nedad(varfechaEfecto, Varfecnac, VarCriterEdad)
	private static final Integer VAR_X = ConstantsFunciones.CTE_40;
	private static final Integer VAR_EDAD = ConstantsFunciones.CTE_40;
	
	// TODO umic.primas.iprimanetaini + nanos(fichaProceso.fcalc, proyUmic(j).fecDevengo, VarCriterFec)
	private static final Integer VAR_P = ConstantsFunciones.CTE_1;
	
	// TODO TC(umic.fechas fecinisus, fichaProceso.fcalc) +  nanos(fichaProceso.fcalc, varfecJ, CriterFec)
	private static final Integer VAR_J = ConstantsFunciones.CTE_1;
	
	// TODO obtenerConfiguracion.recuperarEdadMax (umic.datosgenerales.fecCierre, varTm, varAnoNac, varI1, umic.baseTecIni. psobremort, umic.baseTecIni. priesgo
	private static final BigDecimal VAR_W = new BigDecimal(ConstantsFunciones.CTE_99);
	
	// TODO nanos (umic.fechas.fecinisus, fichaProceso.fcalc, VarCriterFec
	private static final BigDecimal VAR_TCY = BigDecimal.ONE;
	
	// TODO (año(umic.fechas.fechastarenova)*365+ddenero(umic.fechas.fechastarenova, VarCriterFec)+dia(umic.fechas.fechastarenova))-(año(fichaProceso.fcalc)*365+ddenero(fichaProceso.fcalc, VarCriterFec)+dia(fichaProceso.fcalc))]/365
	private static final BigDecimal PART_ANO_NR = new BigDecimal("30.5");
	
	// TODO varRY =[(año(umic.fechas.fecdesderenova)*365+ddenero(umic.fechas. fecdesderenova, VarCriterFec)+dia(umic.fechas. fecdesderenova))-(año(fichaProceso.fcalc)*365+ddenero(fichaProceso.fcalc, VarCriterFec)+dia(fichaProceso.fcalc))]/365
	private static final BigDecimal VAR_RY = new BigDecimal("10.5");
	
	// TODO variables pendientes de obtener datos reales
	private static final Integer DUR_ANT = ConstantsFunciones.CTE_30;
	private static final BigDecimal INT_ANT = new BigDecimal("4.0");
	private static final Integer DUR_POST = ConstantsFunciones.CTE_40;
	private static final BigDecimal INT_POST = new BigDecimal("5.0");
	private static final Integer DUR_J = ConstantsFunciones.CTE_30;
	private static final BigDecimal CAPITAL_GAR = BigDecimal.ZERO;
	private static final Integer REN = ConstantsFunciones.CTE_1;
	private static final Integer X_REN = ConstantsFunciones.CTE_1;
	private static final Integer NIRP = ConstantsFunciones.CTE_1;
	private static final Integer NDAP = ConstantsFunciones.CTE_1;
	private static final BigDecimal NIRMV = BigDecimal.ONE;
	private static final BigDecimal VMORT = new BigDecimal("0.0000");
	private static final BigDecimal VVIDA = BigDecimal.ONE;
	private static final BigDecimal BETA1 = BigDecimal.ONE;
	private static final BigDecimal BETA2 = BigDecimal.ONE;
	private static final Integer DIFERCOL = ConstantsFunciones.CTE_1;
	private static final BigDecimal DIFERCOL_BIG_DECIMAL = BigDecimal.ONE;
	private static final BigDecimal VAR_CRM = BigDecimal.ONE; 
	private static final BigDecimal VAR_GAMMA = BigDecimal.ONE;
	private static final Integer VAR_MESCAREN = ConstantsFunciones.CTE_1;
	private static final Integer VAR_NR = ConstantsFunciones.CTE_8;
	private static final Integer VAR_NRM = ConstantsFunciones.CTE_8;
	private static final BigDecimal VAR_RENTA1 = BigDecimal.ONE;
	private static final BigDecimal VAR_RENTA2 = BigDecimal.ONE;
	private static final Integer VAR_D = ConstantsFunciones.CTE_1;
	private static final Integer VAR_L = ConstantsFunciones.CTE_1;
	private static final BigDecimal VAR_PPR = BigDecimal.ONE;
	private static final BigDecimal VAR_PRIMA = BigDecimal.ONE;
	private static final BigDecimal VAR_ZC = BigDecimal.ONE;
	private static final Integer VAR_TC = ConstantsFunciones.CTE_1;
	private static final BigDecimal VAR_Y = BigDecimal.ONE;
	private static final BigDecimal VAR_GIC = new BigDecimal(ConstantsFunciones.CTE_1000);
	private static final BigDecimal VAR_GEPC = BigDecimal.ZERO;
	private static final BigDecimal VAR_PRC = BigDecimal.ONE;
	private static final BigDecimal VAR_PRP = BigDecimal.ONE;
	private static final BigDecimal VAR_IFAL = BigDecimal.ONE;
	private static final BigDecimal VAR_FALLRED0 = BigDecimal.ZERO;
	private static final BigDecimal VAR_IANT = BigDecimal.ONE;
	private static final BigDecimal VAR_PAS = BigDecimal.ONE;
	private static final BigDecimal VAR_FUT = BigDecimal.ONE;
	
	// Fecha 2013-12-31
	private static final Timestamp FEC_1 = new Timestamp(new GregorianCalendar(2013, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2016-12-31
	private static final Timestamp FEC_2 = new Timestamp(new GregorianCalendar(2016, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2010-12-31
	private static final Timestamp FEC_EFECTO = new Timestamp(new GregorianCalendar(2010, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 1940-4-22
	private static final Timestamp FEC_NAC = new Timestamp(new GregorianCalendar(1940, 5, 22, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2000-12-31
	private static final Timestamp FEC_CAL = new Timestamp(new GregorianCalendar(2000, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2013-06-13
	private static final Timestamp FEC_INI_SUSC = new Timestamp(new GregorianCalendar(2013, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2013-06-13
	private static final Timestamp FEC_ANT_RENOVA = new Timestamp(new GregorianCalendar(2013, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2014-06-13
	private static final Timestamp FEC_PROX_RENOVA = new Timestamp(new GregorianCalendar(2014, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2013-11-30
	private static final Timestamp FEC_CIERRE = new Timestamp(new GregorianCalendar(2013, 10, 3, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2013-06-13
	private static final Timestamp FEC_INI = new Timestamp(new GregorianCalendar(2013, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2014-06-13
	private static final Timestamp FEC_VTO = new Timestamp(new GregorianCalendar(2014, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	// Fecha 2014-06-13
	private static final Timestamp FEC_J = new Timestamp(new GregorianCalendar(2014, 5, 13, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	//Fecha 2023-07-15
	private static final Timestamp FEC_VCTO = new Timestamp(new GregorianCalendar(2023, 6, 15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	//Fecha 2023-07-17
	private static final Timestamp FEC_VCTO2 = new Timestamp(new GregorianCalendar(2023, 6, 17, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	//Fecha 2014-12-17
	private static final Timestamp FEC_CALC = new Timestamp(new GregorianCalendar(2014, 11, 17, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	//Fecha 2014-12-01
	private static final Timestamp FEC_CALC2 = new Timestamp(new GregorianCalendar(2014, 11, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
	//Fecha 2009-12-17
	private static final Timestamp FEC_EFEC = new Timestamp(new GregorianCalendar(2009, 11, 17, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	private static Umic umic = null;
	
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	
	/**
	 * Inicialización para todas las pruebas. Para que se ejecute, la clase no debe extender de TestCase.
	 * A diferencia de @Before, que se ejecuta una vez justo antes de cada @Test,
	 * @BeforeClass se ejecuta una sola vez para todo el archivo de pruebas.
	 */
	@BeforeClass
	public static void init() {
		FuncionesAuxiliaresTest.LOG.debug("Inicializando pruebas");
		
		try {
			// Carga de fichero con la umic
			final List<Umic> lstUmics = obtenerUMICFichero(DATA_FILE_NAME, BEANIO_CONFIG_XML);
			FuncionesAuxiliaresTest.umic = lstUmics.get(0);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(),e);
		}
	}
	
	@AfterClass
	public static void end() {
		FuncionesAuxiliaresTest.LOG.debug("Finalizando pruebas");
	}
	
	@Test
	public void testDdEnero() {
		
		try {
			final Integer ddEnero01 = FuncionesVBX.ddEnero(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.CRITERIO_FECHA_01);
			final Integer ddEnero02 = FuncionesVBX.ddEnero(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.CRITERIO_FECHA_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, Integer.valueOf(334), ddEnero01);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, Integer.valueOf(330), ddEnero02);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testNAnnos() {
		
		try {
			final BigDecimal nAnnos01 = FuncionesAuxiliares.nAnnos(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_2, FuncionesAuxiliaresTest.CRITERIO_FECHA_01);
			final BigDecimal nAnnos02 = FuncionesAuxiliares.nAnnos(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_2, FuncionesAuxiliaresTest.CRITERIO_FECHA_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_OPER_3.compareTo(nAnnos01), 0);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_OPER_3.compareTo(nAnnos02), 0);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	

	@Test
	public void testNEdad() {
		
		try {
			final BigDecimal nAnnos01 = FuncionesAuxiliares.nEdad(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_NAC, FuncionesAuxiliaresTest.CRITERIO_EDAD_02, null, 0);
			final BigDecimal nAnnos02 = FuncionesAuxiliares.nEdad(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_NAC, FuncionesAuxiliaresTest.CRITERIO_EDAD_03, null, 0);
			final BigDecimal nAnnos03 = FuncionesAuxiliares.nEdad(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_NAC, FuncionesAuxiliaresTest.CRITERIO_EDAD_06, FEC_INI, 0);
			//System.out.println(nAnnos03);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("73.5"), nAnnos01.setScale(1, RoundingMode.HALF_UP));
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("74"), nAnnos02);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("73"), nAnnos03);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testTc() {
		
		try {
			final Integer varTc = FuncionesAuxiliares.tc(FuncionesAuxiliaresTest.FEC_EFECTO, FuncionesAuxiliaresTest.FEC_CAL);
			
			//TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, 173.3217784197198, varTc.intValue(), ConstantsFunciones.CTE_DELTA_TEST);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}

	@Test
	public void testTcm() {
		
		try {
			final Integer varTcm = FuncionesAuxiliares.tcm(FuncionesAuxiliaresTest.FEC_CAL, FuncionesAuxiliaresTest.FEC_EFECTO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_119, varTcm);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testY() {
		
		try {
			final BigDecimal varY = FuncionesSecundarias.y(FuncionesAuxiliaresTest.FEC_INI_SUSC, FuncionesAuxiliaresTest.FEC_CAL, FuncionesAuxiliaresTest.CRITERIO_EDAD_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, -12.457534246575342, varY.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}

	@Test
	public void testFfinvitalicia() {
		
		try {
			// Fecha 2030-06-22
			final Timestamp ffInvitCalculada = new Timestamp(new GregorianCalendar(2030, 05, 22, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			
			final Timestamp ffInvitalicia = FuncionesSecundarias.ffInvitalicia(FuncionesAuxiliaresTest.FEC_NAC, 90);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, ffInvitCalculada, ffInvitalicia);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testVrta() {
		
		try {
			final BigDecimal vrta = FuncionesRentas.vrta(FuncionesAuxiliaresTest.MESES_COMPLETOS, FuncionesAuxiliaresTest.VAR_DIFER, FuncionesAuxiliaresTest.VAR_N,
					FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.DUR_PRIMER_TRA, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_EDAD,
					FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_M, FuncionesAuxiliaresTest.PORCEN_REVAL_REN, false, 0);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO.doubleValue(), vrta);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}

//	@Test
	public void testVfal() {
		
		try {
			final BigDecimal vfal = FuncionesFallecimiento.vfal(FuncionesAuxiliaresTest.MESES_COMPLETOS, FuncionesAuxiliaresTest.VAR_I1,
					FuncionesAuxiliaresTest.DUR_PRIMER_TRA, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_EDAD,
					FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_PU, FuncionesAuxiliaresTest.LISTA_LIMITES_CAPITAL, FuncionesAuxiliaresTest.VAR_GAMMA,
					FuncionesAuxiliaresTest.VAR_MESCAREN, CTE_TRUE, BigDecimal.ZERO);
			
			//TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("-0.0017149692353962134"), vfal.setScale(128));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testNDias() {
		
		try {
			final Integer nDias = FuncionesAuxiliares.nDias(FuncionesAuxiliaresTest.FEC_1, FuncionesAuxiliaresTest.FEC_2, FuncionesAuxiliaresTest.CRITERIO_EDAD_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, Integer.valueOf(1080), nDias);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testCalcularTipoInteres() {
		
		try {
			final BigDecimal tipoInteres = FuncionesActualizacionFinanciera.calcularTipoInteres(FuncionesAuxiliaresTest.CRITERIO_INTERES, FuncionesAuxiliaresTest.DUR_ANT,
					FuncionesAuxiliaresTest.INT_ANT, FuncionesAuxiliaresTest.DUR_POST, FuncionesAuxiliaresTest.INT_POST, FuncionesAuxiliaresTest.DUR_J);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, ConstantsFunciones.CTE_OPER_4.setScale(ConstantsFunciones.CTE_1), tipoInteres);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testDif() {
		
		try {
			final BigDecimal dif = FuncionesActualizacionFinanciera.dif(FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.VAR_P, FuncionesAuxiliaresTest.VAR_N,
					FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.0"), dif.setScale(1, RoundingMode.HALF_DOWN));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testAx() {
		
		try {
			final BigDecimal varAx = FuncionesRentas.ax(FuncionesAuxiliaresTest.VAR_DIFER, FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.VAR_P,
					FuncionesAuxiliaresTest.VAR_M, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO, varAx);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVtx002() {
		
		try {
			final BigDecimal vtx002 = Terminales.vtx002(FuncionesAuxiliaresTest.CAPITAL_GAR, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA,
					FuncionesAuxiliaresTest.VAR_GIC, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_DIFER, FuncionesAuxiliaresTest.VAR_X,
					FuncionesAuxiliaresTest.VAR_P, FuncionesAuxiliaresTest.VAR_N);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO.doubleValue(), vtx002.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testPuccapDifer() {
		
		try {
			final BigDecimal puccapDifer = FuncionesVBX.puccapDifer(FuncionesAuxiliaresTest.VAR_PU, FuncionesAuxiliaresTest.VAR_I1_BTI,
					FuncionesAuxiliaresTest.VAR_I2_BTI, FuncionesAuxiliaresTest.VAR_NR, FuncionesAuxiliaresTest.VAR_NRM, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					BETA1, BETA2, FuncionesAuxiliaresTest.DIFERCOL_BIG_DECIMAL, FuncionesAuxiliaresTest.VAR_TCY);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, 2.2496371657156834E40, puccapDifer.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testRecargofro() {
		
		try {
			final BigDecimal recargofro = FuncionesPrimas.recargofro(new BigDecimal(FuncionesAuxiliaresTest.VAR_M),FuncionesAuxiliaresTest.VAR_I2);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO.doubleValue(), recargofro.doubleValue(), ConstantsFunciones.CTE_OPER_0_PUNTO_0001.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testASubXD() {
		
		try {
			final Map<String, Object> mapVariables = new HashMap<String, Object>();
			final List<DetalleCorriente> lstDetalleCorr = crearProyUmic(FuncionesAuxiliaresTest.ITERACION);
			setDatosUmic();
			
			final BigDecimal aSubXD = FuncionesActualizacionFinanciera.aSubXD(
					lstDetalleCorr, lstDetalleCorr.get(FuncionesAuxiliaresTest.ITERACION - 1).getBloqueVida(), FuncionesAuxiliaresTest.ITERACION,
					FuncionesAuxiliaresTest.FEC_CAL, umic, crearDetalleBaseTecnica(), mapVariables, FuncionesAuxiliaresTest.CTE_PROY_VIDA);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, 1.0E7, aSubXD.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testASubX() {
		
		try {
			final Map<String, Object> mapVariables = new HashMap<String, Object>();
			final List<DetalleCorriente> lstDetalleCorr = crearProyUmic(FuncionesAuxiliaresTest.ITERACION);
			setDatosUmic();
			
			final BigDecimal aSubX = FuncionesSecundarias.aSubX(
					lstDetalleCorr, lstDetalleCorr.get(FuncionesAuxiliaresTest.ITERACION - 1).getBloqueVida(),
					FuncionesAuxiliaresTest.FEC_CAL, umic, crearDetalleBaseTecnica(), mapVariables, FuncionesAuxiliaresTest.CTE_PROY_VIDA);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, 5.9E7, aSubX.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVtx005() {
		
		try {
			BigDecimal vtx005 = Terminales.vtx005(FuncionesAuxiliaresTest.CAPITAL_GAR, FuncionesAuxiliaresTest.VAR_PRC,
					FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_X,
					FuncionesAuxiliaresTest.VAR_P, FuncionesAuxiliaresTest.VAR_N, FuncionesAuxiliaresTest.VAR_M, FuncionesAuxiliaresTest.VAR_GIC,
					FuncionesAuxiliaresTest.VAR_GEPC, FuncionesAuxiliaresTest.VAR_PNA0, FuncionesAuxiliaresTest.VAR_PRP, FuncionesAuxiliaresTest.CSITUPOL,
					FuncionesAuxiliaresTest.CTIPOAPORT, FuncionesAuxiliaresTest.VAR_IFAL, FuncionesAuxiliaresTest.VAR_FALLRED0, FuncionesAuxiliaresTest.VAR_IANT,
					FuncionesAuxiliaresTest.VAR_PAS, FuncionesAuxiliaresTest.VAR_FUT);
			vtx005 = vtx005.setScale(9, RoundingMode.FLOOR);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("12.405989365"), vtx005);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}

	@Test
	public void testCapCriFallec() {
		
		try {
			BigDecimal capCriFallec = FuncionesFallecimiento.capCriFallec(FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.VAR_P, FuncionesAuxiliaresTest.VAR_N,
					FuncionesAuxiliaresTest.VAR_IFAL, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_IANT, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA);
			
			capCriFallec = capCriFallec.setScale(9,RoundingMode.FLOOR);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.000115161"), capCriFallec);

		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testGeoCriFallec() {
		
		try {
			final BigDecimal geoCriFallec = FuncionesFallecimiento.geoCriFallec(FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.VAR_P, FuncionesAuxiliaresTest.VAR_N,
					FuncionesAuxiliaresTest.VAR_IFAL, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_PRP, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA,
					FuncionesAuxiliaresTest.VAR_PAS, FuncionesAuxiliaresTest.VAR_FUT);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO, geoCriFallec);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testAxcg() {
		
		try {
			final BigDecimal axcg = FuncionesRentas.axcg(FuncionesAuxiliaresTest.VAR_DIFER, FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.VAR_P,
					FuncionesAuxiliaresTest.VAR_M, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_PRP, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO, axcg);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}

	//@Test
	public void testMg001Alfa() {
		
		try {
			final BigDecimal mg001Alfa = FuncionesVBX.mg001Alfa(FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.FEC_PROX_RENOVA,
					FuncionesAuxiliaresTest.FEC_CIERRE, FuncionesAuxiliaresTest.TIPO_CALCULO, FuncionesAuxiliaresTest.CRITERIO_FECHA_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.39166666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666667"), mg001Alfa);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testMg001Beta() {
		
		try {
			final BigDecimal mg001Beta = FuncionesSecundarias.mg001Beta(FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.FEC_PROX_RENOVA,
					FuncionesAuxiliaresTest.FEC_CIERRE, FuncionesAuxiliaresTest.CRITERIO_FECHA_02);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, 0.3888888888888889, mg001Beta.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVVida() {
		
		try {
			final BigDecimal vVida = FuncionesActualizacionFinanciera.vVida(new BigDecimal(FuncionesAuxiliaresTest.VAR_J), FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2,
					FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL, FuncionesAuxiliaresTest.VAR_TCY, VAR_I1_PORCENTAJE_MAS_UNO, VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ONE.setScale(ConstantsFunciones.CTE_1), vVida);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testGastgivitini() {
		
		try {
			final Map<String, Object> mapVariables = new HashMap<String, Object>();
			final List<DetalleCorriente> lstDetalleCorr = crearProyUmic(FuncionesAuxiliaresTest.ITERACION);
			setDatosUmic();
			
			final BigDecimal gastgivitini = FuncionesGastos.gastgivitini(
					lstDetalleCorr, lstDetalleCorr.get(FuncionesAuxiliaresTest.ITERACION - 1).getBloqueVida(), FuncionesAuxiliaresTest.ITERACION,
					FuncionesAuxiliaresTest.FEC_CAL, umic, crearDetalleBaseTecnica(), mapVariables, FuncionesAuxiliaresTest.CTE_PROY_VIDA,
					FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO, FuncionesAuxiliaresTest.VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO, gastgivitini);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testGastgivit() {
		
		try {
			final BigDecimal gastgivit = FuncionesGastos.gastgivit(FuncionesAuxiliaresTest.FEC_CAL, FuncionesAuxiliaresTest.FEC_INI_SUSC,
					FuncionesAuxiliaresTest.FEC_PROX_RENOVA, FEC_J, FuncionesAuxiliaresTest.PART_ANO_NR, FuncionesAuxiliaresTest.VAR_RY, BigDecimal.ONE,
					FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					FuncionesAuxiliaresTest.VAR_TCY, FuncionesAuxiliaresTest.CRITERIO_FECHA_02, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_W,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_X), FuncionesAuxiliaresTest.VAR_ZC, FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO, FuncionesAuxiliaresTest.VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("20.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000041032868222635044291000"), gastgivit);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVmort() {
		
		try {
			final BigDecimal vmort = FuncionesActualizacionFinanciera.vmort(FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_N), FuncionesAuxiliaresTest.VAR_TCY, FuncionesAuxiliaresTest.PART_ANO_NR,
					FuncionesAuxiliaresTest.VAR_RY, VAR_I1_PORCENTAJE_MAS_UNO, VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.0") ,vmort.setScale(1,RoundingMode.HALF_DOWN));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testRflex() {
		
		try {
			final BigDecimal rflex = FuncionesActualizacionFinanciera.rflex(FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_I1_BTI,
					FuncionesAuxiliaresTest.VAR_I2_BTI, FuncionesAuxiliaresTest.BETA1, FuncionesAuxiliaresTest.BETA2, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_N), new BigDecimal(FuncionesAuxiliaresTest.REN), new BigDecimal(FuncionesAuxiliaresTest.VAR_J),
							FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO, FuncionesAuxiliaresTest.VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("17.2").doubleValue(), rflex.setScale(1, RoundingMode.HALF_DOWN).doubleValue(), ConstantsFunciones.CTE_OPER_0_PUNTO_0001.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testActfall() {
		
		try {
			final BigDecimal actfall = FuncionesActualizacionFinanciera.actfall(FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_I1_BTI,
					FuncionesAuxiliaresTest.VAR_I2_BTI, FuncionesAuxiliaresTest.BETA1, FuncionesAuxiliaresTest.BETA2, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					new BigDecimal(FuncionesAuxiliaresTest.REN), new BigDecimal(FuncionesAuxiliaresTest.DIFERCOL), FuncionesAuxiliaresTest.NIRP, FuncionesAuxiliaresTest.NIRMV,
					FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("16.03"), actfall.setScale(2, RoundingMode.HALF_DOWN));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testCmorpend() {
		
		try {
			final BigDecimal cmorpend = FuncionesActualizacionFinanciera.cmorpend(FuncionesAuxiliaresTest.FEC_INI, FuncionesAuxiliaresTest.FEC_VTO, FuncionesAuxiliaresTest.FEC_ANT_RENOVA,
					FuncionesAuxiliaresTest.NDAP, FuncionesAuxiliaresTest.NIRP, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_ZC,
					new BigDecimal(FuncionesAuxiliaresTest.X_REN), FuncionesAuxiliaresTest.VMORT);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, FuncionesAuxiliaresTest.VMORT, cmorpend);
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testCfallec() {
		
		try {
			final BigDecimal cfallec = FuncionesFallecimiento.cfallec(FuncionesAuxiliaresTest.FEC_INI, FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_N), FuncionesAuxiliaresTest.NIRP, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_ZC,
					new BigDecimal(FuncionesAuxiliaresTest.REN),new BigDecimal(FuncionesAuxiliaresTest.X_REN), FuncionesAuxiliaresTest.VVIDA, FuncionesAuxiliaresTest.BETA1, FuncionesAuxiliaresTest.BETA2,
					FuncionesAuxiliaresTest.VAR_I1_BTI, FuncionesAuxiliaresTest.VAR_I2_BTI, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2,
					FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO, FuncionesAuxiliaresTest.VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO.doubleValue(), cfallec.doubleValue(), ConstantsFunciones.CTE_OPER_0_PUNTO_0001.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
//	@Test
	public void testCfallecvt() {
		
		try {
			final BigDecimal cfallecvt = FuncionesFallecimiento.cfallecvt(FuncionesAuxiliaresTest.FEC_INI, FuncionesAuxiliaresTest.FEC_INI, FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_N), FuncionesAuxiliaresTest.NIRP, FuncionesAuxiliaresTest.NIRMV, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA,
					new BigDecimal(FuncionesAuxiliaresTest.VAR_X), FuncionesAuxiliaresTest.VAR_ZC, new BigDecimal(FuncionesAuxiliaresTest.X_REN),
					new BigDecimal(FuncionesAuxiliaresTest.REN), FuncionesAuxiliaresTest.VMORT, FuncionesAuxiliaresTest.VVIDA,
					new BigDecimal(FuncionesAuxiliaresTest.DIFERCOL), FuncionesAuxiliaresTest.MOD_BETA, FuncionesAuxiliaresTest.BETA1, FuncionesAuxiliaresTest.BETA2,
					FuncionesAuxiliaresTest.VAR_I1_BTI, FuncionesAuxiliaresTest.VAR_I1_BTI, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2,
					FuncionesAuxiliaresTest.VAR_I1_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.7"), cfallecvt.setScale(1, RoundingMode.HALF_DOWN));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
//	@Test
	public void testObadorprim() {
		
		try {
			final BigDecimal obadorprim = FuncionesPrimas.obadorprim(FuncionesAuxiliaresTest.FEC_CAL, FuncionesAuxiliaresTest.FEC_INI_SUSC,
					FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.FEC_PROX_RENOVA, FuncionesAuxiliaresTest.FEC_INI, FuncionesAuxiliaresTest.FEC_VTO,
					FuncionesAuxiliaresTest.FEC_J, new BigDecimal(FuncionesAuxiliaresTest.VAR_J), FuncionesAuxiliaresTest.PART_ANO_NR, FuncionesAuxiliaresTest.VAR_RY,
					FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_M_BIG_DECIMAL, FuncionesAuxiliaresTest.VAR_TC,
					FuncionesAuxiliaresTest.CRITERIO_FECHA_02, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_W,
					BigDecimal.valueOf(FuncionesAuxiliaresTest.VAR_X), FuncionesAuxiliaresTest.VAR_ZC, FuncionesAuxiliaresTest.VAR_Y,
					BigDecimal.valueOf(FuncionesAuxiliaresTest.DIFERCOL), FuncionesAuxiliaresTest.MOD_BETA, FuncionesAuxiliaresTest.BETA1, FuncionesAuxiliaresTest.BETA2,
					BigDecimal.valueOf(FuncionesAuxiliaresTest.VAR_N), FuncionesAuxiliaresTest.VAR_I1_BTI, FuncionesAuxiliaresTest.VAR_I2_BTI, FuncionesAuxiliaresTest.VAR_RENTA1, 
					VAR_I1_PORCENTAJE_MAS_UNO, VAR_I2_PORCENTAJE_MAS_UNO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.0"), obadorprim.setScale(1, RoundingMode.HALF_DOWN));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testVtx009() {
		
		try {
			final BigDecimal vtx009 = Terminales.vtx009(FuncionesAuxiliaresTest.VAR_RENTA1, FuncionesAuxiliaresTest.VAR_RENTA2, FuncionesAuxiliaresTest.VAR_K,
					FuncionesAuxiliaresTest.VAR_D, FuncionesAuxiliaresTest.VAR_N, FuncionesAuxiliaresTest.VAR_I1, FuncionesAuxiliaresTest.VAR_L,
					FuncionesAuxiliaresTest.VAR_I2, FuncionesAuxiliaresTest.VAR_X, FuncionesAuxiliaresTest.TABLA_EXPERIENCIA, FuncionesAuxiliaresTest.VAR_M,
					FuncionesAuxiliaresTest.VAR_PPR, FuncionesAuxiliaresTest.VAR_PRIMA, FuncionesAuxiliaresTest.LISTA_LIMITES_CAPITAL,
					FuncionesAuxiliaresTest.VAR_GAMMA, FuncionesAuxiliaresTest.VAR_MESCAREN, FuncionesAuxiliaresTest.VAR_GIC, FuncionesAuxiliaresTest.VAR_GEPC, new HashMap<String, Object>(), FuncionesAuxiliaresTest.CTE_TRUE, 0, BigDecimal.ZERO);
			
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, BigDecimal.ZERO.doubleValue(), vtx009.doubleValue(), ConstantsFunciones.CTE_OPER_0_PUNTO_0001.doubleValue());
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	//@Test
	public void testMg00xAlfam() {
		
		try {
			final BigDecimal mg00xAlfam = FuncionesVBX.mg00xAlfam(FuncionesAuxiliaresTest.FEC_ANT_RENOVA, FuncionesAuxiliaresTest.FEC_PROX_RENOVA,
					FuncionesAuxiliaresTest.FEC_CIERRE, FuncionesAuxiliaresTest.TIPO_CALCULO, FuncionesAuxiliaresTest.CRITERIO_FECHA_02);
			
			//TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA,ConstantsFunciones.CTE_0_PUNTO_0, mg00xAlfam.doubleValue());
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("0.39166666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666666667"), mg00xAlfam.setScale(128));
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVtx003(){
		try{
			BigDecimal capitalGar = BigDecimal.valueOf(13979.75);
			BigDecimal I1 = BigDecimal.valueOf(3.15);
			Integer x = 43;
			Integer p = 4;
			Integer n = 6;
			Integer m = 1;
			BigDecimal gipc = BigDecimal.valueOf(1.6);
			BigDecimal gepc = BigDecimal.ZERO;
			BigDecimal Pna0 = BigDecimal.valueOf(12000);
			BigDecimal prp = BigDecimal.ZERO;
			BigDecimal Ifal = BigDecimal.valueOf(3.15);
			BigDecimal Iant = BigDecimal.ONE;
			
			final BigDecimal vtx003 = Terminales.vtx003(capitalGar, TABLA_MORTALIDAD, I1, x, p, n, m, gipc, gepc, Pna0, prp, Ifal, Iant);
			//System.out.println(vtx003);
			TestCase.assertEquals(ConstantsFunciones.CTE_CADENA_VACIA, new BigDecimal("13211.73814770"), vtx003.setScale(8, RoundingMode.HALF_DOWN));
		}catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFr(){
		try{
			Fecha fecha = new Fecha(new LocalDateTime());
			fecha.setMes(1);
			fecha.setDia(15);
			fecha.setAnio(2015);
			final FechaFr fr = FuncionesAuxiliares.fr(FEC_VCTO, FEC_CALC);
			TestCase.assertEquals(fecha.getDia(), fr.getDia());
			TestCase.assertEquals(fecha.getMes(), fr.getMes());
			TestCase.assertEquals(fecha.getAnio(), fr.getAnio());
		}catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testAlfa2T(){
		try{
			final int alfa2t = FuncionesAuxiliares.alfa2T(FEC_EFEC, FEC_VCTO2, FEC_CALC2, 2);
			//System.out.println(alfa2t);
			TestCase.assertEquals(16, alfa2t);
		}catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test 
	public void testBgmornlgip(){
		try{
			Integer x = 46;
			Integer N = 5;
			BigDecimal I1 = BigDecimal.valueOf(2);
			BigDecimal gipcPrima = BigDecimal.valueOf(0.231);
			Integer tcm = 48;
			Integer alfat = 37;
			Integer alfa2 = 59;
			BigDecimal vrta = BigDecimal.valueOf(56.60637567);
			BigDecimal factor = BigDecimal.ONE.add(I1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			BigDecimal alfat30 = BigDecimal.valueOf(alfat).divide(BigDecimal.valueOf(30), ConstantsFunciones.MATH_CONTEXT);
			BigDecimal expt = BigDecimal.valueOf(alfat).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
			BigDecimal factorAlfat = Util.pow(factor, expt.negate());
			
			BigDecimal alfa230 = BigDecimal.valueOf(alfa2).divide(BigDecimal.valueOf(30), ConstantsFunciones.MATH_CONTEXT);
			BigDecimal exp2 = BigDecimal.valueOf(alfa2).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
			BigDecimal factorAlfa2 = Util.pow(factor, exp2.negate());
			
			final BigDecimal bgmornlgip = FuncionesAuxiliares.bgmornlgip(x, TABLA_MORTALIDAD2, N, I1, gipcPrima, tcm, alfat, alfat30, factorAlfat, alfa2, alfa230, factorAlfa2, vrta, 0);
			//System.out.println(bgmornlgip);
			TestCase.assertEquals(BigDecimal.valueOf(0.05043755), bgmornlgip.setScale(8, RoundingMode.HALF_DOWN));
			
		}catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test 
	public void testBgmornl(){
		try{
			Integer x = 42;
			Integer N = 5;
			BigDecimal I1 = BigDecimal.valueOf(2);
			Integer tcm = 48;
			Integer alfa2 = 59;
			BigDecimal capital = BigDecimal.valueOf(600);
			Integer p = 12;
			BigDecimal factor = BigDecimal.ONE.add(I1.multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			BigDecimal alfa230 = BigDecimal.valueOf(alfa2).divide(BigDecimal.valueOf(30), ConstantsFunciones.MATH_CONTEXT);
			BigDecimal exp2 = BigDecimal.valueOf(alfa2).multiply(ConstantsFunciones.CTE_OPER_1_PARTIDO_365);
			BigDecimal factorAlfa2 = Util.pow(factor, exp2.negate());
			
			final BigDecimal bgmorn = FuncionesAuxiliares.bgmornl(x, TABLA_MORTALIDAD2, TABLA_QXVALORES, N, I1, tcm, capital, alfa2, alfa230, factorAlfa2, p, mapVariables);
			//System.out.println("bgmorn "+bgmorn);
			TestCase.assertEquals(BigDecimal.valueOf(2.0532801), bgmorn.setScale(7, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPuccapcol(){
		try {
			 BigDecimal pu = BigDecimal.valueOf(5212.24);  
			 BigDecimal n = BigDecimal.valueOf(29);  
			 BigDecimal i1bti = BigDecimal.valueOf(0.06);  
			 BigDecimal i2bti = BigDecimal.ZERO;  
			 Integer nr = 22;
			 Integer nrm = 29;  
			 BigDecimal m = BigDecimal.valueOf(29);  
			 BigDecimal beta1 = BigDecimal.ZERO;  
			 BigDecimal beta2 = BigDecimal.ZERO;  
			 Timestamp fcalc= new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());  
			 Timestamp fFinTramo1 = new Timestamp(new GregorianCalendar(2022, 1, 1).getTimeInMillis());
			 Map<String, Object> mapVariables = new HashMap<String, Object>();
			 
			 BigDecimal puccapcol = FuncionesVBX.puccapcol(pu, n, i1bti, i2bti, nr, nrm, m, beta1, beta2, fcalc, fFinTramo1, mapVariables, "Test");
			// System.out.println(puccapcol);
			 TestCase.assertEquals(BigDecimal.valueOf(18782.50186), puccapcol.setScale(5, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVpre(){
		try {
			BigDecimal vpre1 = FuncionesSecundarias.vpre(0, BigDecimal.valueOf(29), BigDecimal.valueOf(6), BigDecimal.ZERO);
			BigDecimal vpre2 = FuncionesSecundarias.vpre(1, BigDecimal.valueOf(29), BigDecimal.valueOf(6), BigDecimal.ZERO);
			//System.out.println(vpre1);
			//System.out.println(vpre2);
			TestCase.assertEquals(BigDecimal.valueOf(1.0), vpre1);
			TestCase.assertEquals(BigDecimal.valueOf(0.943396226), vpre2.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testCfallecpag(){
		try {
			BigDecimal m = BigDecimal.valueOf(29);
			BigDecimal n = BigDecimal.valueOf(29);
			Integer nDap = 6;
			BigDecimal zc = BigDecimal.valueOf(58); 
			BigDecimal xRen = BigDecimal.valueOf(59);
			BigDecimal ren = BigDecimal.valueOf(23);
			BigDecimal vVida = BigDecimal.valueOf(0.943396226);
			BigDecimal beta1 = BigDecimal.ZERO;
			BigDecimal beta2 = BigDecimal.ZERO;
			BigDecimal i1Bti = BigDecimal.valueOf(6); 
			BigDecimal i2Bti = BigDecimal.ZERO;
			BigDecimal i1 = BigDecimal.valueOf(0.06);
			BigDecimal i2 = BigDecimal.ZERO;
			BigDecimal vari1PorcentajeMasUno = BigDecimal.valueOf(1.06);
			BigDecimal vari2PorcentajeMasUno = BigDecimal.ONE;
			
			BigDecimal cfallecpag = FuncionesFallecimiento.cfallecpag(m, n, nDap, TABLA_MORTALIDAD4, zc, xRen, ren, vVida, beta1, beta2, i1Bti, i2Bti, i1, i2, vari1PorcentajeMasUno, vari2PorcentajeMasUno);
			//System.out.println(cfallecpag);
			TestCase.assertEquals(BigDecimal.valueOf(0.074763576), cfallecpag.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFallecpagvt(){
		try {
			BigDecimal m = BigDecimal.valueOf(29);
			BigDecimal n = BigDecimal.valueOf(29);
			Integer nDap = 5;
			BigDecimal nDmv = BigDecimal.ONE;
			Timestamp frpsgact = new Timestamp(new GregorianCalendar(2016, 1, 1).getTimeInMillis());
			BigDecimal x = BigDecimal.valueOf(36);
			BigDecimal zc = BigDecimal.valueOf(58.084932);
			BigDecimal xRen = BigDecimal.valueOf(59);
			BigDecimal vMort = BigDecimal.valueOf(0.97369222);
			BigDecimal vVida = BigDecimal.valueOf(0.948077);
			BigDecimal tcy = BigDecimal.valueOf(22.08493151);
			String modBeta="CRPI";
			BigDecimal beta1 = BigDecimal.ZERO;
			BigDecimal beta2 = BigDecimal.ZERO;
			BigDecimal i1Bti= BigDecimal.valueOf(6);
			BigDecimal i2Bti = BigDecimal.ZERO;
			BigDecimal i1 = BigDecimal.valueOf(0.06);
			BigDecimal i2 = BigDecimal.ZERO;
			Timestamp fecvcto = new Timestamp(new GregorianCalendar(2022, 1, 1).getTimeInMillis());
			BigDecimal vari1PorcentajeMasUno = BigDecimal.valueOf(1.06);
			BigDecimal vari2PorcentajeMasUno = BigDecimal.ZERO;
			
			BigDecimal fallecpagvt = FuncionesFallecimiento.fallecpagvt(m, n, nDap, nDmv, frpsgact, TABLA_MORTALIDAD4, x, zc, xRen, vMort, vVida, tcy, modBeta, beta1, beta2, i1Bti, i2Bti, i1, i2, fecvcto, vari1PorcentajeMasUno, vari2PorcentajeMasUno);
			//System.out.println(fallecpagvt);
			TestCase.assertEquals(BigDecimal.valueOf(0.01521996), fallecpagvt.setScale(8, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testObfutadorprim(){
		try {
			Timestamp fcalc = new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());
			Timestamp fecProxRenova = new Timestamp(new GregorianCalendar(2016, 1, 1).getTimeInMillis());
			Timestamp frpdgact = new Timestamp(new GregorianCalendar(2016, 1, 1).getTimeInMillis());
			Timestamp fecvto = new Timestamp(new GregorianCalendar(2022, 1, 1).getTimeInMillis());
			Timestamp fecJ = new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());
			BigDecimal partAnoNR = BigDecimal.ONE;
			BigDecimal ry = BigDecimal.ZERO;
			BigDecimal i1 = BigDecimal.valueOf(0.06);
			BigDecimal i2 = BigDecimal.ZERO;
			BigDecimal m = BigDecimal.valueOf(29);
			BigDecimal n = BigDecimal.valueOf(29);
			BigDecimal tcy = BigDecimal.valueOf(22);
			BigDecimal x = BigDecimal.valueOf(36);
			BigDecimal zc = BigDecimal.valueOf(58);
			Integer nDaP = 6;
			BigDecimal nDmv = BigDecimal.ZERO;
			String modBeta = "S";
			BigDecimal beta1 = BigDecimal.ZERO;
			BigDecimal beta2 = BigDecimal.ZERO;
			BigDecimal i1Bti= BigDecimal.valueOf(6);
			BigDecimal i2Bti = BigDecimal.ZERO;
			BigDecimal ren  = BigDecimal.valueOf(23);
			BigDecimal vari1PorcentajeMasUno = BigDecimal.valueOf(1.06);
			BigDecimal vari2PorcentajeMasUno = BigDecimal.ONE;
			
			BigDecimal obfutadorprim = FuncionesPrimas.obfutadorprim(fcalc, fecProxRenova, frpdgact, fecvto, fecJ, partAnoNR, ry, i1, i2, m, n, tcy, TABLA_MORTALIDAD4, x, zc, nDaP, nDmv, modBeta, beta1, beta2, i1Bti, i2Bti, ren, BigDecimal.ZERO, vari1PorcentajeMasUno, vari2PorcentajeMasUno);
			//System.out.println(obfutadorprim);
			TestCase.assertEquals(BigDecimal.valueOf(0.0300463643), obfutadorprim.setScale(10, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	
	
	@Test
	public void testObfutador(){
		try{
			Timestamp falc = new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());
			Timestamp fecIniSusc = new Timestamp(new GregorianCalendar(1993, 1, 1).getTimeInMillis());
			 BigDecimal i1 = BigDecimal.valueOf(6); 
			 BigDecimal i2 = BigDecimal.ZERO;
			 BigDecimal m = BigDecimal.valueOf(29);  
			 BigDecimal tcY = BigDecimal.valueOf(22);  
			 String criterFec = "01";   
			 BigDecimal w = BigDecimal.valueOf(107);  
			 BigDecimal x = BigDecimal.valueOf(36);  
			 BigDecimal n = BigDecimal.valueOf(29);  
			 BigDecimal gic = BigDecimal.ZERO;  
			 BigDecimal gastGi = BigDecimal.valueOf(16.762108); 
			 BigDecimal numi1PorcentajeMasUno = BigDecimal.valueOf(1.06);  
			 BigDecimal numi2PorcentajeMasUno = BigDecimal.ONE;
			 
			 BigDecimal obfutador = FuncionesVBX.obfutadorc(falc, fecIniSusc, i1, i2, m, tcY, criterFec, TABLA_MORTALIDAD4, w, x, n, gic, gastGi, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
			 //System.out.println(obfutador);
			 TestCase.assertEquals(BigDecimal.valueOf(0.607497875), obfutador.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVprer(){
		try {

			BigDecimal i1Porcentaje = BigDecimal.ONE.add(BigDecimal.valueOf(6).multiply(ConstantsFunciones.CTE_OPER_0_PUNTO_01));
			
			//BigDecimal vprer1 = FuncionesAuxiliares.vprer(BigDecimal.valueOf(22), BigDecimal.valueOf(29), i1Porcentaje, BigDecimal.ONE, BigDecimal.valueOf(29), 22);
			//BigDecimal vprer2 = FuncionesAuxiliares.vprer(BigDecimal.valueOf(22.08493151), BigDecimal.valueOf(29), i1Porcentaje, BigDecimal.ONE, BigDecimal.valueOf(28), 22);
			//System.out.println(vprer2);
			//TestCase.assertEquals(BigDecimal.valueOf(1.0), vprer1);
			//TestCase.assertEquals(BigDecimal.valueOf(0.943396226), vprer2.setScale(9, RoundingMode.HALF_DOWN));
			
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testGastgi(){
		try {
			Timestamp fcalc = new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());
			Timestamp fecIniSusc = new Timestamp(new GregorianCalendar(1993, 1, 1).getTimeInMillis());
			Timestamp fecJ = new Timestamp(new GregorianCalendar(2015, 1, 1).getTimeInMillis());
			BigDecimal partAnoNR = BigDecimal.ONE;
			BigDecimal ry = BigDecimal.ZERO;
			BigDecimal ren = BigDecimal.valueOf(23);
			BigDecimal i1 = BigDecimal.valueOf(6);
			BigDecimal i2 = BigDecimal.ZERO;
			BigDecimal m = BigDecimal.valueOf(29);
			BigDecimal tcy = BigDecimal.valueOf(22);
			String criterFec = "01";
			BigDecimal w = BigDecimal.valueOf(107);
			BigDecimal x = BigDecimal.valueOf(36);
			BigDecimal zc = BigDecimal.valueOf(36.39474886);
			Integer nDap = 6;
			BigDecimal nDmv = BigDecimal.ZERO;
			BigDecimal numi1PorcentajeMasUno = BigDecimal.valueOf(1.06);
			BigDecimal numi2PorcentajeMasUno = BigDecimal.ONE;
			
			BigDecimal gastgi = FuncionesGastos.gastgi(fcalc, fecIniSusc, fecJ, partAnoNR, ry, ren, i1, i2, m, tcy, criterFec, TABLA_MORTALIDAD4, w, x, zc, nDap, nDmv, numi1PorcentajeMasUno, numi2PorcentajeMasUno);
			//System.out.println(gastgi);
			TestCase.assertEquals(BigDecimal.valueOf(5.737667), gastgi.setScale(6, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testAxca(){
		try{
			Integer difer =0;
			Integer x = 34;
			Integer p =20;
			Integer m = 30;
			BigDecimal i1= BigDecimal.valueOf(6);
			BigDecimal prp = BigDecimal.valueOf(5);
			
			final BigDecimal axca = FuncionesRentas.axca(difer, x, p, m, i1, prp, TABLA_MORTALIDAD3);
			//final BigDecimal axca2 = FuncionesAuxiliares.axca(difer, x, p+1, m, i1, prp, TABLA_MORTALIDAD3);
			//System.out.println(axca);
			TestCase.assertEquals(BigDecimal.valueOf(16.576205), axca.setScale(6, RoundingMode.HALF_DOWN));
			//TestCase.assertEquals(BigDecimal.valueOf(180.03641), axca2.setScale(5, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
		
	}
	
	@Test
	public void testAriCriFallec(){
		try{
			Integer x = 34;
			Integer p =20;
			Integer n = 30;
			BigDecimal ifal = BigDecimal.valueOf(6);
			BigDecimal i1 = BigDecimal.valueOf(6);
			BigDecimal prp = BigDecimal.valueOf(5);
			BigDecimal pas = BigDecimal.ONE;
			BigDecimal fut = BigDecimal.ZERO;
			
			final BigDecimal ariCri = FuncionesPrimas.ariCriFallec(x, p, n, ifal, i1, prp, TABLA_MORTALIDAD3, pas, fut);
			//final BigDecimal ariCri2 = FuncionesAuxiliares.ariCriFallec(x, p+1, n, ifal, i1, prp, TABLA_MORTALIDAD3, pas, fut);
			//System.out.println(ariCri);
			TestCase.assertEquals(BigDecimal.valueOf(5.94037971), ariCri.setScale(8, RoundingMode.HALF_DOWN));
			//TestCase.assertEquals(BigDecimal.valueOf(40.37439038), ariCri2.setScale(8, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVtx004(){
		try{
			BigDecimal capitalGar = BigDecimal.valueOf(7038.53);
			BigDecimal prc = BigDecimal.valueOf(5);
			BigDecimal i1 = BigDecimal.valueOf(6);
			Integer x = 34;
			Integer p = 20;
			Integer n = 30;
			Integer m = 30;
			BigDecimal gic = BigDecimal.valueOf(0.175);
			BigDecimal gepc = BigDecimal.valueOf(7);
			BigDecimal pna0 = BigDecimal.valueOf(216.36);
			BigDecimal prp = BigDecimal.valueOf(5);
			String csitupol = "VI";
			String ctipoaport = "P";
			BigDecimal ifal = BigDecimal.valueOf(6);
			BigDecimal fallRed0 = BigDecimal.ZERO;
			BigDecimal iant =BigDecimal.ZERO;
			BigDecimal pas = BigDecimal.ONE;
			BigDecimal fut = BigDecimal.ZERO;
			
			final BigDecimal vtx004 = Terminales.vtx004(capitalGar, prc, TABLA_MORTALIDAD3, i1, x, p, n, m, gic, gepc, pna0, prp, csitupol, ctipoaport, ifal, fallRed0, iant, pas, fut);
			//System.out.println(vtx004);
			TestCase.assertEquals(BigDecimal.valueOf(6761.5757), vtx004.setScale(4, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVidapa(){
		try {
			Integer x = 37;
			Integer t = 18;
			BigDecimal alfa = BigDecimal.valueOf(0.249315068);
			Integer w = 118;
			Integer n = 25;
			BigDecimal i1 = BigDecimal.valueOf(5);
			BigDecimal gic = BigDecimal.valueOf(0.075);
			
			BigDecimal vidapa = FuncionesSecundarias.vidapa(x, t, alfa, TABLA_MORTALIDAD3, w, n, i1, gic);
			//System.out.println(vidapa);
			TestCase.assertEquals(BigDecimal.valueOf(0.679028167), vidapa.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testObfutadorcpp301(){
		try {
			Integer x = 37;
			Integer tc = 18;
			BigDecimal alfa = BigDecimal.valueOf(0.249315068);
			Integer w = 118;
			Integer n = 25;
			BigDecimal i1 = BigDecimal.valueOf(5);
			BigDecimal gic = BigDecimal.valueOf(0.075);
			
			BigDecimal obfutadorcpp = FuncionesVBX.obfutadorcpp301(x, tc, alfa, TABLA_MORTALIDAD3, w, n, i1, gic);
			
			TestCase.assertEquals(BigDecimal.valueOf(0.679028167), obfutadorcpp.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPasadaca(){
		try {
			Integer x = 37;
			Integer t = 18;
			BigDecimal ifal = BigDecimal.valueOf(5);
			Integer w = 118;
			Integer n = 25;
			BigDecimal i1 = BigDecimal.valueOf(5);
			
			BigDecimal pasadaca = FuncionesPrimas.pasadaca(x, t, ifal, TABLA_MORTALIDAD3, w, n, i1);
			//System.out.println(pasadaca);
			TestCase.assertEquals(BigDecimal.valueOf(0.062284714), pasadaca.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testMorpend(){
		try {
			Integer x = 36;
			Integer t = 18;
			BigDecimal beta = BigDecimal.valueOf(0.035616438);
			Integer w=118;
			Integer n = 30;
			BigDecimal i1 = BigDecimal.valueOf(5);
			
			BigDecimal morpend = FuncionesFallecimiento.morpend(x, t, beta, TABLA_MORTALIDAD3, w, n, i1);
			//System.out.println(morpend);
			TestCase.assertEquals(BigDecimal.valueOf(0.006457771), morpend.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testSumaf(){
		try {
			Integer t = 18;
			Integer j = 18;
			Integer n = 25;
			BigDecimal prp = BigDecimal.valueOf(5);
			
			BigDecimal sumaf = FuncionesPrimas.sumaf(t, j, n, prp);
			//System.out.println(sumaf);
			TestCase.assertEquals(BigDecimal.valueOf(7.142008453), sumaf.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testVidapu(){
		try {
			Integer x = 36;
			Integer t = 18;
			BigDecimal beta = BigDecimal.valueOf(0.035616438);
			Integer w = 118;
			Integer n = 30;
			BigDecimal i1 = BigDecimal.valueOf(5);
			BigDecimal gic = BigDecimal.valueOf(0.075);
			
			BigDecimal vidapu = FuncionesVBX.vidapu(x, t, beta, TABLA_MORTALIDAD3, w, n, i1, gic);
			//System.out.println(vidapu);
			TestCase.assertEquals(BigDecimal.valueOf(0.496228715), vidapu.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFobligado(){
		try {
			Integer x = 37;
			Integer t = 18;
			BigDecimal alfa = BigDecimal.valueOf(0.249315068);
			Integer w = 118;
			Integer n = 25;
			BigDecimal i1 = BigDecimal.valueOf(5); 
			BigDecimal gepc = BigDecimal.valueOf(8);
			BigDecimal prp = BigDecimal.valueOf(5);
			
			BigDecimal fobligado = FuncionesPrimas.fobligado(x, t, alfa, TABLA_MORTALIDAD3, w, n, i1, gepc, prp);
			//System.out.println(fobligado);
			TestCase.assertEquals(BigDecimal.valueOf(5.434873812), fobligado.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPascap(){
		try {
			Integer x = 36;
			Integer t = 18;
			BigDecimal ifal = BigDecimal.valueOf(5);
			Integer w=118;
			Integer n = 30;
			BigDecimal i1 = BigDecimal.valueOf(5);
			
			BigDecimal pascap = FuncionesFallecimiento.pascap(x, t, ifal, TABLA_MORTALIDAD3, w, n, i1);
			//System.out.println(pascap);
			TestCase.assertEquals(BigDecimal.valueOf(0.119607539), pascap.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testSumapc(){
		try {
			Integer j = 18;
			Integer t = 18;
			BigDecimal prp = BigDecimal.valueOf(5);
			BigDecimal ifal = BigDecimal.valueOf(5);
			
			BigDecimal sumapc = FuncionesPrimas.sumapc(j, t, prp, ifal);
			BigDecimal sumapc2 = FuncionesPrimas.sumapc(19, t, prp, ifal);

			TestCase.assertEquals(BigDecimal.valueOf(1), sumapc);
			TestCase.assertEquals(BigDecimal.valueOf(2.1), sumapc2.setScale(1, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFallecpu(){
		try {
			Integer x = 36;
			Integer t = 18;
			BigDecimal beta = BigDecimal.valueOf(0.035616438);
			Integer n = 30;
			BigDecimal i1 = BigDecimal.valueOf(5);
			BigDecimal ifal = BigDecimal.valueOf(5);
			Integer w = 118;
			
			BigDecimal fallecpu = FuncionesFallecimiento.fallecpu(x, t, beta, n, i1, ifal, TABLA_MORTALIDAD3, w);
			//System.out.println(fallecpu);
			TestCase.assertEquals(BigDecimal.valueOf(0.125481176), fallecpu.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFuturas(){
		try {
			Integer x = 37;
			Integer t = 19;
			Integer n = 25;
			BigDecimal fut = BigDecimal.ONE;
			BigDecimal prp = BigDecimal.valueOf(5);
			BigDecimal ifal = BigDecimal.valueOf(5);
			BigDecimal i1 = BigDecimal.valueOf(5);
			Integer w = 118;
			
			BigDecimal futuras = FuncionesPrimas.futuras(x, t, n, fut, prp, ifal, i1, TABLA_MORTALIDAD3, w);
			//System.out.println(futuras);
			TestCase.assertEquals(BigDecimal.valueOf(0.348146902), futuras.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testObfutadorpap301(){
		try {
			 Integer x = 37;
			 Integer t = 18;
			 Integer n = 25;
			 BigDecimal fut = BigDecimal.ONE;
			 BigDecimal prp = BigDecimal.valueOf(5);
			 BigDecimal ifal = BigDecimal.valueOf(5);
			 BigDecimal i1 = BigDecimal.valueOf(5);
			 Integer w = 118;
			 BigDecimal alfa = BigDecimal.valueOf(0.249315068);
			 
			 BigDecimal obfutadorpap = FuncionesVBX.obfutadorpap301(x, t, n, fut, prp, ifal, i1, TABLA_MORTALIDAD3, w, alfa);
			 //System.out.println(obfutadorpap);
			 TestCase.assertEquals(BigDecimal.valueOf(0.389508429), obfutadorpap.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPpr(){
		try {
			BigDecimal ppr = FuncionesPrimas.ppr(18, 18, BigDecimal.valueOf(294.87));
			BigDecimal ppr2 = FuncionesPrimas.ppr(18, 19, BigDecimal.valueOf(294.87));

			TestCase.assertEquals(BigDecimal.valueOf(294.87), ppr);
			TestCase.assertEquals(BigDecimal.ZERO, ppr2);
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPendpa(){
		try{
			String cformapago ="4";
			Integer tc = 2;
			Integer t = 2;
			BigDecimal pprUmic = BigDecimal.valueOf(356.48);
			BigDecimal primaTotal = BigDecimal.valueOf(534.76);
			BigDecimal primaIni = BigDecimal.valueOf(490.14);
			
			final BigDecimal pendpa = FuncionesPrimas.pendpa(cformapago, tc, t, pprUmic, primaTotal, primaIni);
			//System.out.println(pendpa);
			
			TestCase.assertEquals(BigDecimal.valueOf(0.3333832), pendpa.setScale(7, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPaeint(){
		try {
			Integer x = 37;
			Integer t = 18;
			BigDecimal bbeta = BigDecimal.valueOf(0.753424658);
			Integer w =118;
			BigDecimal gepc = BigDecimal.valueOf(8);
			String cformapago = "4";
			Integer tc0 = 18;
			BigDecimal pnatc = BigDecimal.valueOf(1179.59);
			BigDecimal ifal = BigDecimal.valueOf(5);
			BigDecimal pprUmic = BigDecimal.valueOf(294.87);
			BigDecimal primaIni = BigDecimal.valueOf(490.14);
			
			BigDecimal paeint = FuncionesPrimas.paeint(x, t, bbeta, TABLA_MORTALIDAD3, w, gepc, cformapago, tc0, pnatc, ifal, pprUmic, primaIni);
			//System.out.println(paeint);
			TestCase.assertEquals(BigDecimal.valueOf(849.158311), paeint.setScale(6, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testObfutadorpa301(){
		try {
			Integer x = 37;
			Integer t = 18;
			Integer n = 25;
			BigDecimal fut = BigDecimal.ONE;
			BigDecimal ifal = BigDecimal.valueOf(5);
			BigDecimal i1 = BigDecimal.valueOf(5);
			Integer w = 118;
			BigDecimal alfa = BigDecimal.valueOf(0.249315068);
			Integer tc0 =18;
			BigDecimal pNAtc = BigDecimal.valueOf(1179.59);
			BigDecimal pprUmic = BigDecimal.valueOf(294.87);
			String cformapago = "4";
			Integer tcm = 218;
			Integer ttm = 300;
			BigDecimal ppcapUmic = BigDecimal.valueOf(21232.55);
			BigDecimal pas = BigDecimal.ONE;
			BigDecimal prp = BigDecimal.valueOf(5);
			Integer beta = 0;
			BigDecimal primaIni = BigDecimal.valueOf(490.14);
			
			BigDecimal obfutadorpa = FuncionesVBX.obfutadorpa301(x, t, n, fut, ifal, i1, TABLA_MORTALIDAD3, w, alfa, tc0, pNAtc, pprUmic, cformapago, tcm, ttm, ppcapUmic, pas, prp, beta, primaIni);
			//System.out.println(obfutadorpa);
			TestCase.assertEquals(BigDecimal.valueOf(1397.094479), obfutadorpa.setScale(6, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPuccap(){
		try {
			Integer tc = 18;
			Integer tcm = 216;
			Integer beta = 0;
			Integer tc0 = 18;
			BigDecimal ifal = BigDecimal.valueOf(5);
			BigDecimal ppcap = BigDecimal.valueOf(3897.09);
			BigDecimal ppr = BigDecimal.ZERO;
			
			BigDecimal puccap = FuncionesVBX.puccap(tc, tcm, beta, tc0, ifal, ppcap, ppr);
			BigDecimal puccap2 = FuncionesVBX.puccap(19, tcm, 12, tc0, ifal, ppcap, ppr);

			TestCase.assertEquals(BigDecimal.valueOf(3897.09), puccap.setScale(2, RoundingMode.HALF_DOWN));
			TestCase.assertEquals(BigDecimal.valueOf(4091.9445), puccap2.setScale(4, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testFutflex(){
		try {
			Integer tc = 18;
			Integer tcm = 218;
			Integer ttm = 300;
			Integer beta = 0;
			BigDecimal pNAtc = BigDecimal.valueOf(1179.59);
			BigDecimal pas = BigDecimal.ONE;
			BigDecimal fut = BigDecimal.ONE;
			BigDecimal prp = BigDecimal.valueOf(5);
			BigDecimal tit1 = BigDecimal.valueOf(5);
			
			BigDecimal futflex = FuncionesPrimas.fut_flex(tc, tcm, ttm, beta, pNAtc, pas, fut, prp, tit1);
			//System.out.println(futflex);
			TestCase.assertEquals(BigDecimal.valueOf(8023.468334), futflex.setScale(6, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
		
	}
	
	@Test
	public void testFutpg(){
		try {
			Integer tc = 18;
			Integer tcm = 218;
			Integer ttm = 300;
			Integer beta = 0;
			BigDecimal prp = BigDecimal.valueOf(5);
			
			BigDecimal futpg = FuncionesSecundarias.futpg(tc, tcm, ttm, beta, prp);
			//System.out.println(futpg);
			TestCase.assertEquals(BigDecimal.valueOf(6.80191281), futpg.setScale(8, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testPpcap(){
		try {
			Integer tc =18;
			Integer tcm = 218;
			Integer ttm = 300;
			Integer beta = 0;
			Integer tc0 =18;
			BigDecimal ifal = BigDecimal.valueOf(5);
			BigDecimal ppcapUmic = BigDecimal.valueOf(21232.55);
			Integer n = 25;
			BigDecimal pprUmic = BigDecimal.valueOf(294.87);
			BigDecimal pNAtc = BigDecimal.valueOf(1179.59);
			String cformapago = "4";
			BigDecimal prp = BigDecimal.valueOf(5);
			BigDecimal pas = BigDecimal.ONE;
			BigDecimal fut = BigDecimal.ONE;
			BigDecimal tit1 = BigDecimal.valueOf(5);
			BigDecimal primaIni = BigDecimal.valueOf(490.14);
			
			BigDecimal ppcap = FuncionesPrimas.ppcap(tc, tcm, ttm, beta, tc0, ifal, ppcapUmic, n, pprUmic, pNAtc, cformapago, prp, pas, fut, tit1, primaIni);
			//System.out.println(ppcap);
			TestCase.assertEquals(BigDecimal.valueOf(21232.55), ppcap);
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testBVida(){
		try {
			Integer x = 37;
			Integer n = 5;
			BigDecimal i1 = BigDecimal.valueOf(1.95);
			Integer tcm = 20;
			Integer alfat = 20;
			Integer alfa2 = 22;
			BigDecimal icapini = BigDecimal.valueOf(5055.22);
			
			BigDecimal bvida = FuncionesAuxiliares.bvida(x, TABLA_MORTALIDAD5, n, i1, tcm, alfat, alfa2, icapini);
			//System.out.println(bvida);
			TestCase.assertEquals(BigDecimal.valueOf(4712.75903053), bvida.setScale(8, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testBgmornlFac(){
		try {
			Integer x = 58;
			Integer n = 5;
			BigDecimal i1 = BigDecimal.valueOf(2);
			Integer tcm = 0;
			BigDecimal capital = BigDecimal.valueOf(2500);
			Integer alfa2 = 29;
			BigDecimal alfa230 = BigDecimal.valueOf(0.966666667);
			BigDecimal factorAlfa2 = BigDecimal.valueOf(0.998427878);
			Integer p = 12;
			
			BigDecimal bgmornlFac = FuncionesAuxiliares.bgmornlFac(x, VALORES_LX, n, i1, tcm, capital, alfa2, alfa230, factorAlfa2, p, VALORES_LX);
			//System.out.println(bgmornlFac);
			TestCase.assertEquals(BigDecimal.valueOf(7.856897039), bgmornlFac.setScale(9, RoundingMode.HALF_DOWN));
		} catch(Exception e){
			FuncionesAuxiliaresTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void testViveDifer(){
		BigDecimal x = BigDecimal.valueOf(34);
		Integer t = 1;
		BigDecimal i = BigDecimal.valueOf(1.06); 
		BigDecimal edadCob = BigDecimal.valueOf(65);
		
		BigDecimal viveDifer = FuncionesVBX.viveDifer(x, t, i, edadCob, TABLA_MORTALIDAD6);
		//System.out.println(viveDifer);
		TestCase.assertEquals(BigDecimal.valueOf(0.12876602296), viveDifer.setScale(11, RoundingMode.HALF_DOWN));
	}
	
	@Test
	public void testActvbx(){
		BigDecimal var1 = BigDecimal.ONE;
		BigDecimal var2 = BigDecimal.valueOf(1.5);
		BigDecimal m = BigDecimal.valueOf(99);
		BigDecimal unoMasI1Entre100 = BigDecimal.valueOf(1.06); 
		BigDecimal unoMasI2Entre100 = BigDecimal.ONE;
		
		BigDecimal actVbx = FuncionesActualizacionFinanciera.actVbx(var1, var2, m, unoMasI1Entre100, unoMasI2Entre100, mapVariables);
		//System.out.println(actVbx);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), actVbx.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testGast(){
		Integer t = 0;
		Integer ttm = 0;
		Integer diaVto = 0;
		BigDecimal ggim = BigDecimal.valueOf(0);
		Integer diaprima = 0;
		BigDecimal bxAnterior = BigDecimal.valueOf(0);
		
		BigDecimal gast = FuncionesGastos.gast(t, ttm, diaVto, ggim, diaprima, bxAnterior);
		//System.out.println(gast);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), gast.setScale(9, RoundingMode.HALF_DOWN));
		
	}
	
	//@Test
	public void testCfall(){
		Integer t = 0;
		Integer ttm = 0;
		Integer x = 0;
		BigDecimal ifal = BigDecimal.valueOf(0);
		BigDecimal gtosF = BigDecimal.valueOf(0);
		BigDecimal ggim = BigDecimal.valueOf(0);
		BigDecimal k = BigDecimal.valueOf(0);
		Integer diaVto = 0;
		Integer diaPrima = 0;
		BigDecimal bxAnterior = BigDecimal.valueOf(0);
		Integer w = 0;
		BigDecimal CRMax = BigDecimal.valueOf(0);
		
		BigDecimal cfall = FuncionesFallecimiento.cfall(t, ttm, x, ifal, gtosF, ggim, k, diaVto, diaPrima, bxAnterior, w, CRMax, TABLA_MORTALIDAD6);
		//System.out.println(cfall);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), cfall.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testPrima(){
		Integer t = 0;
		Integer ttm = 0;
		BigDecimal pna = BigDecimal.valueOf(0);
		Integer np = 0;
		BigDecimal prp = BigDecimal.valueOf(0);
		Integer mesefect = 0;
		Integer diaVto = 0;
		Integer diaPrima = 0;
		Integer tcm = 0;
		
		BigDecimal prima = FuncionesPrimas.prima(t, ttm, pna, np, prp, mesefect, diaVto, diaPrima, tcm);
		//System.out.println(prima);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), prima.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testGastp(){
		Integer t = 0;
		Integer ttm = 0;
		Integer diaVto = 0;
		BigDecimal ggim = BigDecimal.valueOf(0);
		Integer diaprima = 0;
		BigDecimal prima = BigDecimal.valueOf(0);
		
		BigDecimal gastp = FuncionesGastos.gastp(t, ttm, diaVto, ggim, diaprima, prima);
		//System.out.println(gastp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), gastp.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testIntp(){
		Integer t = 0;
		Integer ttm = 0;
		BigDecimal prima = BigDecimal.valueOf(0);
		Integer diaVto = 0;
		Integer diaPrima = 0;
		BigDecimal i1 = BigDecimal.valueOf(0);
		Timestamp fecEfect = new Timestamp(new GregorianCalendar(2013, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		BigDecimal intp = FuncionesPrimas.intp(t, ttm, prima, diaVto, diaPrima, i1, fecEfect);
		//System.out.println(intp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), intp.setScale(9, RoundingMode.HALF_DOWN));		
	}
	
	//@Test
	public void testIntg(){
		Integer t = 0;
		Integer ttm = 0;
		Integer diaVto = 0;
		BigDecimal gast = BigDecimal.valueOf(0);
		BigDecimal cfall = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal CC06 = BigDecimal.valueOf(0);
		BigDecimal bxAnterior = BigDecimal.valueOf(0);
		
		BigDecimal intg = FuncionesPrimas.intg(t, ttm, diaVto, gast, cfall, i1, CC06, bxAnterior);
		//System.out.println(intg);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), intg.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testVpret(){
		Integer j = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer t = 0;
		
		BigDecimal vpet = FuncionesVBX.vpret(j, m, i1, i2, t);
		//System.out.println(vpet);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), vpet.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testArxMasT(){
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		
		BigDecimal arxMasT = FuncionesVBX.aarxt(difer, x, w, ppr, t, TABLA_MORTALIDAD6, m, i1, i2);
		//System.out.println(arxMasT);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), arxMasT.setScale(9, RoundingMode.HALF_DOWN));			
	}
	
	//@Test
	public void testAfrxMasT(){
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer f = 0;
		
		BigDecimal afrxMasT = FuncionesVBX.afrxt(difer, x, w, ppr, t, TABLA_MORTALIDAD6, m, i1, i2, f);
		//System.out.println(afrxMasT);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), afrxMasT.setScale(9, RoundingMode.HALF_DOWN));		
	}
	
	//@Test
	public void testVpost(){
		Integer j = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer t = 0;
			
		BigDecimal vpost = FuncionesVBX.vpost(j, m, i1, i2, t);
		//System.out.println(vpost);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), vpost.setScale(9, RoundingMode.HALF_DOWN));
	}
		
	//@Test
	public void testArxt () {
			
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);		
			
		BigDecimal arxt = FuncionesVBX.arxt(difer, x, w, ppr, t, FuncionesAuxiliaresTest.TABLA_MORTALIDAD, m, i1, i2);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), arxt.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testAgtr () {
			
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);	
		Integer f = 0;
		Integer agp = 0;
				
		BigDecimal agtr = FuncionesVBX.agtr(difer, x, w, ppr, t, FuncionesAuxiliaresTest.TABLA_MORTALIDAD, m, i1, i2, f, agp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), agtr.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testArxDiferAGP () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.ZERO;
		Integer ag = 0;
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal i1 = BigDecimal.ZERO;
		BigDecimal i2 = BigDecimal.ZERO;
		Integer agp = 0;
		Integer varLimSuperior = 0;
		
		BigDecimal arxDiferAGP = FuncionesVBX.arxDiferAGP(difer, x, w, ppr, ag, FuncionesAuxiliaresTest.TABLA_MORTALIDAD, m, i1, i2, agp, varLimSuperior);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), arxDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testAarxDiferAGP () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.ZERO;
		Integer ag = 0;
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal i1 = BigDecimal.ZERO;
		BigDecimal i2 = BigDecimal.ZERO;
		Integer agp = 0;
		Integer varLimSuperior = 0;
		
		BigDecimal aarxDiferAGP = FuncionesVBX.aarxDiferAGP(difer, x, w, ppr, ag, FuncionesAuxiliaresTest.TABLA_MORTALIDAD,
				m, i1, i2, agp, varLimSuperior);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), aarxDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
		
	//@Test
	public void testAfrxDiferAGP () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer w = 0;
		BigDecimal ppr = BigDecimal.ZERO;
		Integer t = 0;
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal i1 = BigDecimal.ZERO;
		BigDecimal i2 = BigDecimal.ZERO;
		Integer f = 0;
		Integer ag = 0;
		Integer agp = 0;
		Integer varLimSuperior = 0;
		
		BigDecimal afrxDiferAGP = FuncionesVBX.afrxDiferAGP(difer, x, w, ppr, t, FuncionesAuxiliaresTest.TABLA_MORTALIDAD,
				m, i1, i2, f, ag, agp, varLimSuperior);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), afrxDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testArxyt () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
			
		BigDecimal arxyt = FuncionesVBX.arxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), arxyt.setScale(9, RoundingMode.HALF_DOWN));
		}
		
	//@Test 
	public void testAarxyt () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.ZERO;
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.ZERO;
		BigDecimal i1 = BigDecimal.ZERO;
		BigDecimal i2 = BigDecimal.ZERO;
		
		BigDecimal aarxyt = FuncionesVBX.aarxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), aarxyt.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testAfxyt () {
		
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer f = 0;
		
		BigDecimal afrxyt = FuncionesVBX.afrxyt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY,
				m, i1, i2, f);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), afrxyt.setScale(9, RoundingMode.HALF_DOWN));
		
	}
	
	//@Test
	public void testArxyDiferAGP (){
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer agp = 0;
		
		BigDecimal arxyDiferAGP = FuncionesVBX.arxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		//System.out.println(arxyDiferAGP);
		TestCase.assertEquals(BigDecimal.valueOf(0), arxyDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testAarxyDiferAGP (){
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer agp = 0;
		
		BigDecimal aarxyDiferAGP = FuncionesVBX.aarxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		//System.out.println(aarxyDiferAGP);
		TestCase.assertEquals(BigDecimal.valueOf(0), aarxyDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testAfrxyDiferAGP (){
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer agp = 0;
		Integer f = 0;
		
		BigDecimal afrxyDiferAGP = FuncionesVBX.afrxyDiferAGP(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp, f);
		//System.out.println(afrxyDiferAGP);
		TestCase.assertEquals(BigDecimal.valueOf(0), afrxyDiferAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	
	//@Test
	public void testarxytAGPt() {
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer agp = 0;
		
		BigDecimal arxytAGP = FuncionesVBX.arxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), arxytAGP.setScale(9, RoundingMode.HALF_DOWN));
	}
		
	//@Test
	public void testaarxytAGPt() {
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer agp = 0;
			
		BigDecimal aarxytAGPt = FuncionesVBX.arxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, agp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), aarxytAGPt.setScale(9, RoundingMode.HALF_DOWN));
		}
		
	//@Test
	public void testAfrxytAGPt() {
		Integer difer = 0;
		Integer x = 0;
		Integer wx = 0;
		Integer y = 0;
		Integer wy = 0;
		BigDecimal ppr = BigDecimal.valueOf(0);
		Integer t = 0;
		List<BigDecimal> valoresTabMortX = FuncionesAuxiliaresTest.TABLA_MORTALIDAD;
		List<BigDecimal> valoresTabMortY = FuncionesAuxiliaresTest.TABLA_MORTALIDAD2;
		BigDecimal m = BigDecimal.valueOf(0);
		BigDecimal i1 = BigDecimal.valueOf(0);
		BigDecimal i2 = BigDecimal.valueOf(0);
		Integer f = 0;
		Integer agp = 0;
			
		BigDecimal afrxytAGPt = FuncionesVBX.afrxytAGPt(difer, x, wx, y, wy, ppr, t, valoresTabMortX, valoresTabMortY, m, i1, i2, f, agp);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), afrxytAGPt.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testVida() {
		BigDecimal xc = BigDecimal.valueOf(2);
		BigDecimal xj = BigDecimal.valueOf(2);
		Integer w = 1;
		List<BigDecimal> valoresTabMortX = TABLA_MORTALIDAD;
		
		BigDecimal vida = FuncionesVBX.vida(xc, xj, w, valoresTabMortX);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), vida.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testVt() {
		BigDecimal durcierre = BigDecimal.valueOf(2);;
		BigDecimal actj = BigDecimal.valueOf(2);;
		Integer durit1 = 2;
		BigDecimal i1 = BigDecimal.valueOf(2);
		BigDecimal i2 = BigDecimal.valueOf(2);
		
		BigDecimal vt = FuncionesVBX.vt(durcierre, actj, durit1, i1, i2);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), vt.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testVtr() {
		Integer j = 1;
		BigDecimal durcierre = BigDecimal.valueOf(2);
		BigDecimal actj = BigDecimal.valueOf(2);
		BigDecimal actjant = BigDecimal.valueOf(1);
		Integer durit1 = 2;
		BigDecimal i1 = BigDecimal.valueOf(2);
		BigDecimal i2 = BigDecimal.valueOf(2);
		
		BigDecimal vtr = FuncionesVBX.vtr(j, durcierre, actj, actjant, durit1, i1, i2);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), vtr.setScale(9, RoundingMode.HALF_DOWN));		
	}
	
	//@Test
	public void TestCf() {
		Integer j = 1;
		
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		BigDecimal impPago = BigDecimal.valueOf(2);
		Integer iteraciones = 5;
	    for (int i=1; i<=iteraciones;i++) {
	    	DetalleCorriente detalleCorriente = new DetalleCorriente();
	    	detalleCorriente.setImpPago(impPago);
	    	proyUmic.add(detalleCorriente);
	    }			
		
		BigDecimal cfant = BigDecimal.valueOf(2);
		BigDecimal pu = BigDecimal.valueOf(100);
		Integer varTcm = 2;
		
		BigDecimal cf = FuncionesFallecimiento.cf(j, proyUmic, cfant, pu, varTcm);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), cf.setScale(9, RoundingMode.HALF_DOWN));
	}
	
	//@Test
	public void testFallec() {
		Integer j = 1;
		BigDecimal xc = BigDecimal.valueOf(2);
		BigDecimal xj = BigDecimal.valueOf(2);
		BigDecimal xjant = BigDecimal.valueOf(1);
		List<BigDecimal> valoresTabMortX = TABLA_MORTALIDAD;
		
		BigDecimal fallec = FuncionesFallecimiento.fallec(j, xc, xj, xjant, valoresTabMortX);
		TestCase.assertEquals(BigDecimal.valueOf(0.971285862), fallec.setScale(9, RoundingMode.HALF_DOWN));				
	}
	
	
	
	
	private List<DetalleCorriente> crearProyUmic(final Integer iteracion) {
		
		final List<DetalleCorriente> lstDetCorriente = new ArrayList<DetalleCorriente>();
		
		for (int i = 0; i < iteracion; i++) {
			
			final DetalleCorriente detalleCorriente = new DetalleCorriente();
			
			detalleCorriente.getTotalFlujoProyeccion().setSumfprob(BigDecimal.ONE);
			
			detalleCorriente.setBloqueVida(new BloqueCorriente());
			detalleCorriente.getBloqueVida().setImpFlujoActualizado(BigDecimal.ONE);
			detalleCorriente.getBloqueVida().setImpFlujoNoAnulado(BigDecimal.ONE);
			detalleCorriente.getBloqueVida().setImpFlujoNominal(BigDecimal.ONE);
			detalleCorriente.getBloqueVida().setImpFlujoProbable(BigDecimal.ONE);
			detalleCorriente.getBloqueVida().setImpProvi(BigDecimal.ONE);
			
			detalleCorriente.getTotalFlujoProyeccion().setSumcola(BigDecimal.ONE);
			detalleCorriente.getTotalFlujoProyeccion().setSumfprob(BigDecimal.ONE);
			detalleCorriente.getTotalFlujoProyeccion().setSumfprobtanul(BigDecimal.ONE);
			detalleCorriente.getTotalFlujoProyeccion().setSumprovision(BigDecimal.ONE);
			
			lstDetCorriente.add(detalleCorriente);
		}
		
		return lstDetCorriente;
	}
	
	private static DetalleBaseTecnica crearDetalleBaseTecnica() {
		final DetalleBaseTecnica baseTecnica = new DetalleBaseTecnica();
		baseTecnica.setBaseTec(FuncionesAuxiliaresTest.CTE_BTI);
		baseTecnica.setTablacalc1aseg1("740");
		baseTecnica.setTablacalc1aseg2("740");
		baseTecnica.setGtorosspPrima(BigDecimal.ONE);
		baseTecnica.setGtorosspCap(BigDecimal.ONE);
		final List<BigDecimal> lstBigDecimal = new ArrayList<BigDecimal>();
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		baseTecnica.setItcalc(lstBigDecimal);
		final List<String> scasadoX = new ArrayList<String>();
		scasadoX.add(FuncionesAuxiliaresTest.CTE_S);
		scasadoX.add(FuncionesAuxiliaresTest.CTE_S);
		scasadoX.add(FuncionesAuxiliaresTest.CTE_S);
		scasadoX.add(FuncionesAuxiliaresTest.CTE_S);
		scasadoX.add(FuncionesAuxiliaresTest.CTE_S);
		baseTecnica.setSwcasado(scasadoX);
		final GregorianCalendar ini = new GregorianCalendar(1986, 9, 1);
		final GregorianCalendar fin = new GregorianCalendar();
		final GregorianCalendar finEspe = new GregorianCalendar(2014, 10, 30);
		final GregorianCalendar fecCierre = new GregorianCalendar(2020, 8, 1);
		baseTecnica.setFecCierre(new Timestamp(fecCierre.getTimeInMillis()));
		final List<Timestamp> lstIni = new ArrayList<Timestamp>();
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		baseTecnica.setFecInitramo(lstIni);
		final List<Timestamp> lstFin = new ArrayList<Timestamp>();
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(finEspe.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		baseTecnica.setFecfintramo(lstFin);
		
		return baseTecnica;
	}
	
	private void setDatosUmic() {
		
		// ATC
		umic.getFechas().setFecinisus(new Timestamp(new GregorianCalendar(2012, 9, 6).getTimeInMillis()));
		
		// ACT001
		umic.getBti().setFecIniTramo2(new Timestamp(new GregorianCalendar(2014, 11, 1, 0, 0, 0).getTimeInMillis()));
		
		// testModGZCRTA, gzc008, 
		umic.getAsegurados().setFnacAseg1(new Timestamp(new GregorianCalendar(1950, 6, 1, 0, 0, 0).getTimeInMillis()));
		
		// jub
		umic.getFechas().setFecinisus(new Timestamp(new GregorianCalendar(2014, 9, 6, 0, 0, 0).getTimeInMillis()));
		
		// VZCIERTA
		umic.getBti().setTabla1Aseg1("740");
		umic.getRentas().setForpagrent(1);
		umic.getRentas().setCformaRevrenta("C");
		umic.getRentas().setCpagrenta("1");
		umic.getRentas().setCtipoRevrenta("1");
		umic.getFechas().setFecefecred(new Timestamp(new GregorianCalendar(2014, 9, 6, 0, 0, 0).getTimeInMillis()));
		umic.getPrimas().setCformpago("4");
		umic.getComisiones().setNpericomi1(1);
		umic.getComisiones().setNpericomi2(1);
		umic.getComisiones().setNpericomi3(99);
		umic.getFechas().setFecefecfin(new Timestamp(new GregorianCalendar(2040, 1, 15, 0, 0, 0).getTimeInMillis()));
		
		// En todos
		umic.getDuraciones().setNdursegano(30);
		umic.getRentas().setFecIni(new Timestamp(new GregorianCalendar(2014, 4, 30, 0, 0, 0).getTimeInMillis()));
		umic.getRentas().setFecFin(new Timestamp(new GregorianCalendar(2014, 11, 30, 0, 0, 0).getTimeInMillis()));
	}
	
	public static List<Umic> obtenerUMICFichero(final String rutaFichero, final String beanConfig) throws IOException {
		
		final List<Umic> lstUmics = new ArrayList<Umic>();
		
		BeanIOReader inBeanIOReader = null;
		try {
			inBeanIOReader = new BeanIOReader(beanConfig, COMPRIMIR ? ZIPPED_DATA : rutaFichero, "X880JI01");
						
			try {
				Umic umic = null;	
				while ((umic = (Umic) inBeanIOReader.read()) != null) {
					lstUmics.add(umic);
				}
			} catch (InvalidRecordException ire) {
				FuncionesAuxiliaresTest.LOG.error("Error parseando fichero: {}", ire.toString());
			}
		} catch (Exception e) {
			FuncionesAuxiliaresTest.LOG.error("Error ", e);
		} finally {
			if (inBeanIOReader != null) {
				inBeanIOReader.close();
			}
		}
		
		return lstUmics;
	}
	
}
