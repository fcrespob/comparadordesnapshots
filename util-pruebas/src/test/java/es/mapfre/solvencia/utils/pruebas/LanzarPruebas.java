package es.mapfre.solvencia.utils.pruebas;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class LanzarPruebas {
	
	private static final Logger LOG = LoggerFactory.getLogger(LanzarPruebas.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	
	//private static final String STREAM_NAME_BT = "detalle-basetecnicas";
	private static final String STREAM_NAME_DETALLE = "detalle-corriente";
	private static final String STREAM_NAME_TOTALES = "totales-flujos";
	
	private static final  String RUTA_COMPARAR = "COMPARAR/";
	
//	String[] TEXTOS_CABECERAS = { "CNEGOCIO", "CCANAL", "CCARTERA",
//			"FCIERRE", "BT", "KMODALIDAD", "KPOLIZA", "KSUBPOLIZA",
//			"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
//			"KPRESTACION", "KAJUSTE", "CTIPOAPORT", "FDESDE", "FHASTA",
//			"FDEVENGOVIDA", "FPAGOVIDA", "SIG_CUANTIAVIDA",
//			"CUANTIAVIDA", "FPB_VIDA", "SIG_FPVIDA", "FPVIDA", "ATC",
//			"SIG_FPNAVIDA", "FPNAVIDA", "ACTFIN", "SIG_FACTVIDA",
//			"FACTVIDA", "SIG_COLAVIDA", "COLAVIDA", "FDEVENGOFALL",
//			"FPAGOFALL", "SIG_CUANTIAFALL", "CUANTIAFALL", "FPB_FALL",
//			"SIG_FPFALL", "FPFALL", "ATC", "SIG_FPNAFALL", "FPNAFALL",
//			"ACTFIN", "SIG_FACTFALL", "FACTFALL", "SIG_COLAFALL",
//			"COLAFALL", "FDEVENGOCOMPL", "FPAGOCOMPL",
//			"SIG_CUANTIACOMPL", "CUANTIACOMPL", "FPB_COMPL",
//			"SIG_FPCOMPL", "FPCOMPL", "ATC", "SIG_FPNACOMPL",
//			"FPNACOMPL", "ACTFIN", "SIG_FACTCOMPL", "FACTCOMPL",
//			"SIG_COLACOMPL", "COLACOMPL", "FDEVENGOGTO", "FPAGOGTO",
//			"SIG_CUANTIAGTO", "CUANTIAGTO", "FPB_GTO", "SIG_FPGTO",
//			"FPGTO", "ATC", "SIG_FPNAGTO", "FPNAGTO", "ACTFIN",
//			"SIG_FACTGTO", "FACTGTO", "SIG_COLAGTO", "COLAGTO",
//			"FDEVENGOCOMI", "FPAGOCOMI", "SIG_CUANTIACOMI",
//			"CUANTIACOMI", "FPB_COMI", "SIG_FPCOMI", "FPCOMI", "ATC",
//			"SIG_FPNACOMI", "FPNACOMI", "ACTFIN", "SIG_FACTCOMI",
//			"FACTCOMI", "SIG_COLACOMI", "COLACOMI", "FDEVENGORTE",
//			"FPAGORTE", "SIG_CUANTIARTE", "CUANTIARTE", "FPB_RTE",
//			"SIG_FPRTE", "FPRTE", "APTOTC", "SIG_FPANRTE", "FPANRTE",
//			"ACTFIN", "SIG_FACTRTE", "FACTRTE", "SIG_COLARTE",
//			"COLARTE", "FDEVENGOPRIM", "FPAGOPRIM", "SIG_CUANTIAPRIM",
//			"CUANTIAPRIM", "FPB_PRIM", "SIG_FPPRIM", "FPPRIM", "ATC",
//			"SIG_FPNAPRIM", "FPNAPRIM", "ACTFIN", "SIG_FACTPRIM",
//			"FACTPRIM", "SIG_COLAPRIM", "COLAPRIM", "SIG_SUMFPROB",
//			"SUMFPROB", "SIG_SUMFPROBTANUL", "SUMFPROBTANUL",
//			"SIG_SUMPROVISION", "SUMPROVISION", "SIG_SUMCOLA",
//			"SUMCOLA", "SIG_PROVBTIPROY", "PROVBTIPROY",
//			"SIG_TERMINAL_ ANTERIOR", "TERMINAL ANTERIOR",
//			"SIG_TERMINAL_POSTERIOR", "TERMINAL POSTERIOR" };
		
	private static final String[] TEXTOS_CABECERAS = { "CNEGOCIO", "CCANAL", "CCARTERA",
			"FCIERRE", "BT", "KMODALIDAD", "KPOLIZA", "KSUBPOLIZA",
			"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
			"KPRESTACION", "KAJUSTE", "CTIPOAPORT", "FDESDE", "FHASTA",
			"FDEVENGOVIDA", "FPAGOVIDA", "CUANTIAVIDA", "FPB_VIDA",
			"FPVIDA", "ATC", "FPNAVIDA", "ACTFIN", "FACTVIDA",
			"COLAVIDA", "FDEVENGOFALL", "FPAGOFALL", "CUANTIAFALL",
			"FPB_FALL", "FPFALL", "ATC", "FPNAFALL", "ACTFIN",
			"FACTFALL", "COLAFALL", "FDEVENGOCOMPL", "FPAGOCOMPL",
			"CUANTIACOMPL", "FPB_COMPL", "FPCOMPL", "ATC", "FPNACOMPL",
			"ACTFIN", "FACTCOMPL", "COLACOMPL", "FDEVENGOGTO",
			"FPAGOGTO", "CUANTIAGTO", "FPB_GTO", "FPGTO", "ATC",
			"FPNAGTO", "ACTFIN", "FACTGTO", "COLAGTO", "FDEVENGOCOMI",
			"FPAGOCOMI", "CUANTIACOMI", "FPB_COMI", "FPCOMI", "ATC",
			"FPNACOMI", "ACTFIN", "FACTCOMI", "COLACOMI",
			"FDEVENGORTE", "FPAGORTE", "CUANTIARTE", "FPB_RTE",
			"FPRTE", "APTOTC", "FPANRTE", "ACTFIN", "FACTRTE",
			"COLARTE", "FDEVENGOPRIM", "FPAGOPRIM", "CUANTIAPRIM",
			"FPB_PRIM", "FPPRIM", "ATC", "FPNAPRIM", "ACTFIN",
			"FACTPRIM", "COLAPRIM", "SUMFPROB", "SUMFPROBTANUL",
			"SUMPROVISION", "SUMCOLA", "PROVBTIPROY",
			"TERMINAL ANTERIOR", "TERMINAL POSTERIOR" };
	
	private static final String[] TEXTOS_CABECERAS_TOTALES = { "CNEGOCIO", "CCANAL", "CCARTERA",
		"FCIERRE", "BT", "KMODALIDAD", "KPOLIZA", "KSUBPOLIZA",
		"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
		"KPRESTACION", "KAJUSTE", "CTIPOAPORT",
		"TOTFPVIDA", "TOTFPNAVIDA", "TOTFACTVIDA", "TOTCOLAVIDA",
		"TOTFPFALL", "TOTFPNAFALL", "TOTFACTFALL", "TOTCOLAFALL",
		"TOTFPCOMPL", "TOTFPNACOMPL", "TOTFACTCOMPL", "TOTCOLACOMPL",
		"TOTFPGTO", "TOTFPNAGTO", "TOTFACTGTO", "TOTCOLAGTO",
		"TOTFPCOM", "TOTFPNACOM", "TOTFACTCOM", "TOTCOLACOM",
		"TOTFPRTE", "TOTFPNARTE", "TOTFACTRTE", "TOTCOLARTE",
		"TOTFPPRIM", "TOTFPNAPRIM", "TOTFACTPRIM", "TOTCOLAPRIM",
		"TOTFPROB", "TOTFPROBTANUL", "TOTPROVISION", "TOTCOLA", "PROVBTIFCAL" };
	
	/**
	 * 362	273	RPB9
	 * Vida_Nominal: Vitalicia
	 * Vida_Probable: 1 Cabeza
	 * Vida_Actualizado: No casado - 1 tramo
	 * Fall_Nominal: MODBETA = CRP
	 * Gast_Nominal: Vitalicia
	 * Poliza: 426782
	 * 
	 * BTI:		20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt
	 * ROSSP:	20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt
	 * BEL:		20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt, 20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt
	 */
	private static final String MOD_362_BTI_CASO1 = "362_BTI_Caso1/";
	private static final String RUTA_362_BTI_MAPFRE_DET_1 = "20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_DET_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_362_BTI_MAPFRE_TOT_1 = "20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_TOT_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSTOT_.txt";
	private static final String MOD_362_ROSSP_CASO1 = "362_ROSSP_Caso1/";
	private static final String RUTA_362_ROSSP_MAPFRE_DET_1 = "20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_DET_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_362_ROSSP_MAPFRE_TOT_1 = "20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_TOT_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSTOT_.txt";
	private static final String MOD_362_BEL_CASO1 = "362_BEL_Caso1/";
	private static final String RUTA_362_BEL_MAPFRE_DET_1 = "20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_DET_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_362_BEL_MAPFRE_TOT_1 = "20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_TOT_1 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSTOT_.txt";
	
	/**
	 * 362	273	RTC00
	 * Vida_Nominal: Temporal
	 * Vida_Probable: 1 Cabeza
	 * Vida_Actualizado: Casado - 2 tramos
	 * Fall_Nominal: MODBETA = CRPI
	 * Gast_Nominal: Temporal
	 * Poliza: 437745
	 * 
	 * BTI:		20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt
	 * ROSSP:	20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt
	 * BEL:		20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt, 20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt
	 */
	private static final String MOD_362_BTI_CASO2 = "362_BTI_Caso2/";
	private static final String RUTA_362_BTI_MAPFRE_DET_2 = "20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_DET_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_362_BTI_MAPFRE_TOT_2 = "20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_TOT_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSTOT_.txt";
	private static final String MOD_362_ROSSP_CASO2 = "362_ROSSP_Caso2/";
	private static final String RUTA_362_ROSSP_MAPFRE_DET_2 = "20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_DET_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_362_ROSSP_MAPFRE_TOT_2 = "20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_TOT_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSTOT_.txt";
	private static final String MOD_362_BEL_CASO2 = "362_BEL_Caso2/";
	private static final String RUTA_362_BEL_MAPFRE_DET_2 = "20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_DET_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_362_BEL_MAPFRE_TOT_2 = "20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_TOT_2 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSTOT_.txt";
	
	/**
	 * 362	273	RVR09
	 * Vida_Nominal: Vitalicia
	 * Vida_Probable: 2 Cabeza
	 * Vida_Actualizado: Casado - 2 tramos
	 * Fall_Nominal: MODBETA = CRPI
	 * Poliza: 441078
	 * 
	 * BTI:		20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt
	 * ROSSP:	20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt, 20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt
	 * BEL:		20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt, 20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt
	 */
	private static final String MOD_362_BTI_CASO3 = "362_BTI_Caso3/";
	private static final String RUTA_362_BTI_MAPFRE_DET_3 = "20131231_MAVI_10201_BTI_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_DET_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_362_BTI_MAPFRE_TOT_3 = "20131231_MAVI_10201_BTI_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_BTI_INDRA_TOT_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BTI_FLUJOSTOT_.txt";
	private static final String MOD_362_ROSSP_CASO3 = "362_ROSSP_Caso3/";
	private static final String RUTA_362_ROSSP_MAPFRE_DET_3 = "20131231_MAVI_10201_ROSSP_FLUDESA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_DET_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_362_ROSSP_MAPFRE_TOT_3 = "20131231_MAVI_10201_ROSSP_FLUTOTA_RTASJUB_REGEN02.txt";
	private static final String RUTA_362_ROSSP_INDRA_TOT_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_ROSSP_FLUJOSTOT_.txt";
	private static final String MOD_362_BEL_CASO3 = "362_BEL_Caso3/";
	private static final String RUTA_362_BEL_MAPFRE_DET_3 = "20131231_MAVI_10201_BEL_FLUDESA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_DET_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_362_BEL_MAPFRE_TOT_3 = "20131231_MAVI_10201_BEL_FLUTOTA_RTASJUB_REGEN.txt";
	private static final String RUTA_362_BEL_INDRA_TOT_3 = "2013120000_SOLV_SO03_GP01_110_20131231_C_1_BEL_FLUJOSTOT_.txt";
	
	/**
	 * 209	1	0
	 * Vida_Nominal: Poliza en vigor
	 * Vida_Actualizado: No casado - 1 tramo
	 * Fall_Nominal: Poliza en vigor
	 * BTI:		20131231_MAVI_10101_BTI_FLUDESA_MOD209.txt, 20131231_MAVI_10101_BTI_FLUTOTA_MOD209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_BTI_CASO1 = "209_BTI_Caso1/";
	private static final String RUTA_209_BTI_MAPFRE_DET_1 = "20131231_MAVI_10101_BTI_FLUDESA_MOD209.txt";
	private static final String RUTA_209_BTI_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_209_BTI_MAPFRE_TOT_1 = "20131231_MAVI_10101_BTI_FLUTOTA_MOD209.txt";
	private static final String RUTA_209_BTI_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSTOT_.txt";
	
	/**
	 * 209	19	0
	 * Vida_Actualizado: No casado - 1 tramo
	 * BTI:		20131231_MAVI_10101_BTI_FLUDESA_BONO209.TXT, 20131231_MAVI_10101_BTI_FLUTOTA_BONO209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_BTI_CASO2 = "209_BTI_Caso2/";
	private static final String RUTA_209_BTI_MAPFRE_DET_2 = "20131231_MAVI_10101_BTI_FLUDESA_BONO209.TXT";
	private static final String RUTA_209_BTI_INDRA_DET_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_209_BTI_MAPFRE_TOT_2 = "20131231_MAVI_10101_BTI_FLUTOTA_BONO209.txt";
	private static final String RUTA_209_BTI_INDRA_TOT_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSTOT_.txt";
	
	/**
	 * 209	1	0
	 * Vida_Nominal: Poliza reducida
	 * Fall_Nominal: Poliza reducida
	 * BTI:		20131231_MAVI_10101_BTI_FLUDESA_MOD209.txt, 20131231_MAVI_10101_BTI_FLUTOTA_MOD209.txt
	 * Poliza: 1429000
	 */
	private static final String MOD_209_BTI_CASO4 = "209_BTI_Caso3/";
	private static final String RUTA_209_BTI_MAPFRE_DET_3 = "20131231_MAVI_10101_BTI_FLUDESA_MOD209.txt";
	private static final String RUTA_209_BTI_INDRA_DET_3 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSDET_.txt";
	private static final String RUTA_209_BTI_MAPFRE_TOT_3 = "20131231_MAVI_10101_BTI_FLUTOTA_MOD209.txt";
	private static final String RUTA_209_BTI_INDRA_TOT_3 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BTI_FLUJOSTOT_.txt";
	
	/**
	 * 209	1	0
	 * ROSSP:	20131231_MAVI_10101_ROSSP_FLUDESA_MOD209.txt, 20131231_MAVI_10101_ROSSP_FLUTOTA_MOD209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_ROSSP_CASO1 = "209_ROSSP_Caso1/";
	private static final String RUTA_209_ROSSP_MAPFRE_DET_1 = "20131231_MAVI_10101_ROSSP_FLUDESA_MOD209.txt";
	private static final String RUTA_209_ROSSP_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_209_ROSSP_MAPFRE_TOT_1 = "20131231_MAVI_10101_ROSSP_FLUTOTA_MOD209.txt";
	private static final String RUTA_209_ROSSP_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_ROSSP_FLUJOSTOT_.txt";
	
	/**
	 * 209	19	0
	 * ROSSP:	20131231_MAVI_10101_ROSSP_FLUDESA_BONO209.TXT, 20131231_MAVI_10101_ROSSP_FLUTOTA_BONO209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_ROSSP_CASO2 = "209_ROSSP_Caso2/";
	private static final String RUTA_209_ROSSP_MAPFRE_DET_2 = "20131231_MAVI_10101_ROSSP_FLUDESA_BONO209.TXT";
	private static final String RUTA_209_ROSSP_INDRA_DET_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_209_ROSSP_MAPFRE_TOT_2 = "20131231_MAVI_10101_ROSSP_FLUTOTA_BONO209.txt";
	private static final String RUTA_209_ROSSP_INDRA_TOT_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_ROSSP_FLUJOSTOT_.txt";
	
	/**
	 * 209	1	0
	 * BEL:		20131231_MAVI_10101_BEL_FLUDESA_MOD209.txt, 20131231_MAVI_10101_BEL_FLUTOTA_MOD209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_BEL_CASO1 = "209_BEL_Caso1/";
	private static final String RUTA_209_BEL_MAPFRE_DET_1 = "20131231_MAVI_10101_BEL_FLUDESA_MOD209.txt";
	private static final String RUTA_209_BEL_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_209_BEL_MAPFRE_TOT_1 = "20131231_MAVI_10101_BEL_FLUTOTA_MOD209.txt";
	private static final String RUTA_209_BEL_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSTOT_.txt";
	
	/**
	 * 209	19	0
	 * Anul_Nominal: Poliza en vigor
	 * BEL:		20131231_MAVI_10101_BEL_FLUDESA_BONO209.txt, 20131231_MAVI_10101_BEL_FLUTOTA_BONO209.txt
	 * Poliza: 1387000
	 */
	private static final String MOD_209_BEL_CASO2 = "209_BEL_Caso2/";
	private static final String RUTA_209_BEL_MAPFRE_DET_2 = "20131231_MAVI_10101_BEL_FLUDESA_BONO209.txt";
	private static final String RUTA_209_BEL_INDRA_DET_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_209_BEL_MAPFRE_TOT_2 = "20131231_MAVI_10101_BEL_FLUTOTA_BONO209.txt";
	private static final String RUTA_209_BEL_INDRA_TOT_2 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSTOT_.txt";

	/**
	 * 209	1	0
	 * Anul_Nominal: Poliza reducida
	 * BEL:		20131231_MAVI_10101_BEL_FLUDESA_MOD209.txt, 20131231_MAVI_10101_BEL_FLUDESA_MOD209.txt
	 * Poliza: 1429000
	 */
	private static final String MOD_209_BEL_CASO3 = "209_BEL_Caso3/";
	private static final String RUTA_209_BEL_MAPFRE_DET_3 = "20131231_MAVI_10101_BEL_FLUDESA_MOD209.txt";
	private static final String RUTA_209_BEL_INDRA_DET_3 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSDET_.txt";
	private static final String RUTA_209_BEL_MAPFRE_TOT_3 = "20131231_MAVI_10101_BEL_FLUTOTA_MOD209.txt";
	private static final String RUTA_209_BEL_INDRA_TOT_3 = "2013120000_SOLV_SO02_GP01_100_20131231_I_1_BEL_FLUJOSTOT_.txt";
	
	/**
	 * 852	1	0
	 * Vida_Actualizado: No casado - 2 tramos
	 * Fall_Nominal: Riesgo no agravado
	 * Prov_Nominal: Riesgo no agravado
	 * BTI: 	20131231_MCMV_20101_BTI_FLUDESA_MOD852.txt, 20131231_MCMV_20101_BTI_FLUTOTA_MOD852.txt
	 * Poliza: 8040000
	 */
	private static final String MOD_852_BTI_CASO1 = "852_BTI_Caso1/";
	private static final String RUTA_852_BTI_MAPFRE_DET_1 = "20131231_MCMV_20101_BTI_FLUDESA_MOD852.txt";
	private static final String RUTA_852_BTI_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_BTI_FLUJOSDET_.txt";
	private static final String RUTA_852_BTI_MAPFRE_TOT_1 = "20131231_MCMV_20101_BTI_FLUTOTA_MOD852.txt";
	private static final String RUTA_852_BTI_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_BTI_FLUJOSTOT_.txt";
	
	/**
	 * 852	1	0
	 * ROSSP:	20131231_MCMV_20101_ROSSP_FLUDESA_MOD852.txt, 20131231_MCMV_20101_ROSSP_FLUTOTA_MOD852.txt
	 * Poliza: 8040000
	 */
	private static final String MOD_852_ROSSP_CASO1 = "852_ROSSP_Caso1/";
	private static final String RUTA_852_ROSSP_MAPFRE_DET_1 = "20131231_MCMV_20101_ROSSP_FLUDESA_MOD852.txt";
	private static final String RUTA_852_ROSSP_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_ROSSP_FLUJOSDET_.txt";
	private static final String RUTA_852_ROSSP_MAPFRE_TOT_1 = "20131231_MCMV_20101_ROSSP_FLUTOTA_MOD852.txt";
	private static final String RUTA_852_ROSSP_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_ROSSP_FLUJOSTOT_.txt";
	
	/**
	 * 852	1	0
	 * BEL:		20131231_MCMV_20101_BEL_FLUDESA_MOD852.txt, 20131231_MCMV_20101_BEL_FLUTOTA_MOD852.txt
	 * Poliza: 8040000
	 */
	private static final String MOD_852_BEL_CASO1 = "852_BEL_Caso1/";
	private static final String RUTA_852_BEL_MAPFRE_DET_1 = "20131231_MCMV_20101_BEL_FLUDESA_MOD852.txt";
	private static final String RUTA_852_BEL_INDRA_DET_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_BEL_FLUJOSDET_.txt";
	private static final String RUTA_852_BEL_MAPFRE_TOT_1 = "20131231_MCMV_20101_BEL_FLUTOTA_MOD852.txt";
	private static final String RUTA_852_BEL_INDRA_TOT_1 = "2013120000_SOLV_SO02_GP01_100_20131231_I_20_BEL_FLUJOSTOT_.txt";
	
	private static XSSFCellStyle datoStyleParIndraComparador;
	private static XSSFCellStyle datoStyleImparIndraComparador;
	private static XSSFCellStyle datoStyleParMapfreComparador;
	private static XSSFCellStyle datoStyleImparMapfreComparador;
	private static XSSFCellStyle datoStyleParIndra;
	private static XSSFCellStyle datoStyleImparIndra;
	private static XSSFCellStyle datoStyleParMapfre;
	private static XSSFCellStyle datoStyleImparMapfre;
	
	private static XSSFCellStyle distintosStyleParIndraComparador;
	private static XSSFCellStyle distintosStyleParMapfreComparador;
	private static XSSFCellStyle distintosStyleImparIndraComparador;
	private static XSSFCellStyle distintosStyleImparMapfreComparador;
	private static XSSFCellStyle distintosStyleParIndra;
	private static XSSFCellStyle distintosStyleParMapfre;
	private static XSSFCellStyle distintosStyleImparIndra;
	private static XSSFCellStyle distintosStyleImparMapfre;
	
	private static XSSFCellStyle datoStyleIndraComparador;
	private static XSSFCellStyle distintosStyleIndraComparador;
	private static XSSFCellStyle datoStyleMapfreComparador;
	private static XSSFCellStyle distintosStyleMapfreComparador;
	private static XSSFCellStyle datoStyleIndra;
	private static XSSFCellStyle distintosStyleIndra;
	private static XSSFCellStyle datoStyleMapfre;
	private static XSSFCellStyle distintosStyleMapfre;
	
	private static BigDecimal diferenciaPermitida = new BigDecimal("0.0001");
	private static XSSFColor textoFail = new XSSFColor(Color.RED);
	private static XSSFColor fondoImpar = new XSSFColor(new byte[] {(byte)240, (byte)240, (byte)240});
	
	public static void main(String[] args) {
	
		try {
			
			LanzarPruebas.LOG.debug("Inicio test resultados 362");
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BTI_MAPFRE_DET_1, RUTA_362_BTI_INDRA_DET_1, MOD_362_BTI_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BTI_MAPFRE_TOT_1, RUTA_362_BTI_INDRA_TOT_1, MOD_362_BTI_CASO1);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_ROSSP_MAPFRE_DET_1, RUTA_362_ROSSP_INDRA_DET_1, MOD_362_ROSSP_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_ROSSP_MAPFRE_TOT_1, RUTA_362_ROSSP_INDRA_TOT_1, MOD_362_ROSSP_CASO1);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BEL_MAPFRE_DET_1, RUTA_362_BEL_INDRA_DET_1, MOD_362_BEL_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BEL_MAPFRE_TOT_1, RUTA_362_BEL_INDRA_TOT_1, MOD_362_BEL_CASO1);
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BTI_MAPFRE_DET_2, RUTA_362_BTI_INDRA_DET_2, MOD_362_BTI_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BTI_MAPFRE_TOT_2, RUTA_362_BTI_INDRA_TOT_2, MOD_362_BTI_CASO2);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_ROSSP_MAPFRE_DET_2, RUTA_362_ROSSP_INDRA_DET_2, MOD_362_ROSSP_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_ROSSP_MAPFRE_TOT_2, RUTA_362_ROSSP_INDRA_TOT_2, MOD_362_ROSSP_CASO2);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BEL_MAPFRE_DET_2, RUTA_362_BEL_INDRA_DET_2, MOD_362_BEL_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BEL_MAPFRE_TOT_2, RUTA_362_BEL_INDRA_TOT_2, MOD_362_BEL_CASO2);
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BTI_MAPFRE_DET_3, RUTA_362_BTI_INDRA_DET_3, MOD_362_BTI_CASO3);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BTI_MAPFRE_TOT_3, RUTA_362_BTI_INDRA_TOT_3, MOD_362_BTI_CASO3);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_ROSSP_MAPFRE_DET_3, RUTA_362_ROSSP_INDRA_DET_3, MOD_362_ROSSP_CASO3);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_ROSSP_MAPFRE_TOT_3, RUTA_362_ROSSP_INDRA_TOT_3, MOD_362_ROSSP_CASO3);
			comparar(false, STREAM_NAME_DETALLE, RUTA_362_BEL_MAPFRE_DET_3, RUTA_362_BEL_INDRA_DET_3, MOD_362_BEL_CASO3);
			comparar(false, STREAM_NAME_TOTALES, RUTA_362_BEL_MAPFRE_TOT_3, RUTA_362_BEL_INDRA_TOT_3, MOD_362_BEL_CASO3);
			
			LanzarPruebas.LOG.debug("Fin test resultados 362");
			
			LanzarPruebas.LOG.debug("Inicio test resultados 209");
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BTI_MAPFRE_DET_1, RUTA_209_BTI_INDRA_DET_1, MOD_209_BTI_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BTI_MAPFRE_TOT_1, RUTA_209_BTI_INDRA_TOT_1, MOD_209_BTI_CASO1);
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BTI_MAPFRE_DET_2, RUTA_209_BTI_INDRA_DET_2, MOD_209_BTI_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BTI_MAPFRE_TOT_2, RUTA_209_BTI_INDRA_TOT_2, MOD_209_BTI_CASO2);
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BTI_MAPFRE_DET_3, RUTA_209_BTI_INDRA_DET_3, MOD_209_BTI_CASO4);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BTI_MAPFRE_TOT_3, RUTA_209_BTI_INDRA_TOT_3, MOD_209_BTI_CASO4);
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_ROSSP_MAPFRE_DET_1, RUTA_209_ROSSP_INDRA_DET_1, MOD_209_ROSSP_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_ROSSP_MAPFRE_TOT_1, RUTA_209_ROSSP_INDRA_TOT_1, MOD_209_ROSSP_CASO1);
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_ROSSP_MAPFRE_DET_2, RUTA_209_ROSSP_INDRA_DET_2, MOD_209_ROSSP_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_ROSSP_MAPFRE_TOT_2, RUTA_209_ROSSP_INDRA_TOT_2, MOD_209_ROSSP_CASO2);
			
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BEL_MAPFRE_DET_1, RUTA_209_BEL_INDRA_DET_1, MOD_209_BEL_CASO1);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BEL_MAPFRE_TOT_1, RUTA_209_BEL_INDRA_TOT_1, MOD_209_BEL_CASO1);
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BEL_MAPFRE_DET_2, RUTA_209_BEL_INDRA_DET_2, MOD_209_BEL_CASO2);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BEL_MAPFRE_TOT_2, RUTA_209_BEL_INDRA_TOT_2, MOD_209_BEL_CASO2);
			comparar(false, STREAM_NAME_DETALLE, RUTA_209_BEL_MAPFRE_DET_3, RUTA_209_BEL_INDRA_DET_3,MOD_209_BEL_CASO3);
			comparar(false, STREAM_NAME_TOTALES, RUTA_209_BEL_MAPFRE_TOT_3, RUTA_209_BEL_INDRA_TOT_3,MOD_209_BEL_CASO3);
			
			LanzarPruebas.LOG.debug("Fin test resultados 209");
			
			LanzarPruebas.LOG.debug("Inicio test resultados 852");
			
			comparar(true, STREAM_NAME_DETALLE, RUTA_852_BTI_MAPFRE_DET_1, RUTA_852_BTI_INDRA_DET_1, MOD_852_BTI_CASO1);
			comparar(true, STREAM_NAME_TOTALES, RUTA_852_BTI_MAPFRE_TOT_1, RUTA_852_BTI_INDRA_TOT_1, MOD_852_BTI_CASO1);
			comparar(true, STREAM_NAME_DETALLE, RUTA_852_ROSSP_MAPFRE_DET_1, RUTA_852_ROSSP_INDRA_DET_1, MOD_852_ROSSP_CASO1);
			comparar(true, STREAM_NAME_TOTALES, RUTA_852_ROSSP_MAPFRE_TOT_1, RUTA_852_ROSSP_INDRA_TOT_1, MOD_852_ROSSP_CASO1);
			comparar(true, STREAM_NAME_DETALLE, RUTA_852_BEL_MAPFRE_DET_1, RUTA_852_BEL_INDRA_DET_1, MOD_852_BEL_CASO1);
			comparar(true, STREAM_NAME_TOTALES, RUTA_852_BEL_MAPFRE_TOT_1, RUTA_852_BEL_INDRA_TOT_1, MOD_852_BEL_CASO1);
			
			LanzarPruebas.LOG.debug("Fin test resultados 852");
			
		} catch (Solvencia2Excepcion e) {
			LanzarPruebas.LOG.error(e.getMessage(), e);
			fail();
		} catch (Throwable e) {
			LanzarPruebas.LOG.error(e.getMessage(), e);
		}

		LOG.info("Finalizado");
	}
	
	private static void comparar(boolean ejecutar, String tipoFichero, String ficheroMapfre, String ficheroIndra, String ruta) {
		if (ejecutar) {
			comparar(tipoFichero, ficheroMapfre, ficheroIndra, ruta);
		}
	}
	
	private static void comparar(String tipoFichero, String ficheroMapfre, String ficheroIndra, String ruta) {
		
		BeanIOReader readerIndra;
		BeanIOReader readerMapfre;
		DetalleCorriente detalle = null;
		TotalesFlujos totales = null;
		
		String ficheroSalida = null;
		try {
			readerIndra = new BeanIOReader(BEANIO_CONFIG_XML, RUTA_COMPARAR.concat(ruta).concat(ficheroIndra), tipoFichero);
			readerMapfre = new BeanIOReader(BEANIO_CONFIG_XML, RUTA_COMPARAR.concat(ruta).concat(ficheroMapfre), tipoFichero);
			
			// create a new file
			ficheroSalida = RUTA_COMPARAR.concat(ruta).concat(ficheroIndra).concat(".xlsx");
			FileOutputStream out = new FileOutputStream(ficheroSalida);
			// create a new workbook
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheetComparacion = workBook.createSheet("Comparación");
			XSSFSheet sheetIndra = workBook.createSheet("Indra");
			XSSFSheet sheetMapfre = workBook.createSheet("Mapfre");
			
			XSSFRow rowComparacion = sheetComparacion.createRow(0);
			XSSFRow rowIndra = sheetIndra.createRow(0);
			XSSFRow rowMapfre = sheetMapfre.createRow(0);
			
			escribirCabecera(workBook, sheetComparacion, tipoFichero, rowComparacion);
			escribirCabecera(workBook, sheetIndra, tipoFichero, rowIndra);
			escribirCabecera(workBook, sheetMapfre, tipoFichero, rowMapfre);
			
			XSSFFont fail = workBook.createFont();
			fail.setColor(textoFail);
			
			datoStyleParIndraComparador = workBook.createCellStyle();
			datoStyleParIndraComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleParIndra = workBook.createCellStyle();
			datoStyleParIndra.setAlignment(CellStyle.ALIGN_RIGHT);
			
			datoStyleImparIndraComparador = workBook.createCellStyle();
			datoStyleImparIndraComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleImparIndraComparador.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			datoStyleImparIndraComparador.setFillForegroundColor(fondoImpar);
			datoStyleImparIndra = workBook.createCellStyle();
			datoStyleImparIndra.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleImparIndra.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			datoStyleImparIndra.setFillForegroundColor(fondoImpar);
			
			datoStyleParMapfreComparador = workBook.createCellStyle();
			datoStyleParMapfreComparador.setBorderBottom(CellStyle.BORDER_THIN);
			datoStyleParMapfreComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleParMapfre = workBook.createCellStyle();
			datoStyleParMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
			
			datoStyleImparMapfreComparador = workBook.createCellStyle();
			datoStyleImparMapfreComparador.setBorderBottom(CellStyle.BORDER_THIN);
			datoStyleImparMapfreComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleImparMapfreComparador.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			datoStyleImparMapfreComparador.setFillForegroundColor(fondoImpar);
			datoStyleImparMapfre = workBook.createCellStyle();
			datoStyleImparMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
			datoStyleImparMapfre.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			datoStyleImparMapfre.setFillForegroundColor(fondoImpar);
			
			distintosStyleParIndraComparador = workBook.createCellStyle();
			distintosStyleParIndraComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleParIndraComparador.setFont(fail);
			distintosStyleParIndra = workBook.createCellStyle();
			distintosStyleParIndra.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleParIndra.setFont(fail);
			
			distintosStyleImparIndraComparador = workBook.createCellStyle();
			distintosStyleImparIndraComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleImparIndraComparador.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			distintosStyleImparIndraComparador.setFillForegroundColor(fondoImpar);
			distintosStyleImparIndraComparador.setFont(fail);
			distintosStyleImparIndra = workBook.createCellStyle();
			distintosStyleImparIndra.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleImparIndra.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			distintosStyleImparIndra.setFillForegroundColor(fondoImpar);
			distintosStyleImparIndra.setFont(fail);
			
			distintosStyleParMapfreComparador = workBook.createCellStyle();
			distintosStyleParMapfreComparador.setBorderBottom(CellStyle.BORDER_THIN);
			distintosStyleParMapfreComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleParMapfreComparador.setFont(fail);
			distintosStyleParMapfre = workBook.createCellStyle();
			distintosStyleParMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleParMapfre.setFont(fail);
			
			distintosStyleImparMapfreComparador = workBook.createCellStyle();
			distintosStyleImparMapfreComparador.setBorderBottom(CellStyle.BORDER_THIN);
			distintosStyleImparMapfreComparador.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleImparMapfreComparador.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			distintosStyleImparMapfreComparador.setFillForegroundColor(fondoImpar);
			distintosStyleImparMapfreComparador.setFont(fail);
			distintosStyleImparMapfre = workBook.createCellStyle();
			distintosStyleImparMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
			distintosStyleImparMapfre.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			distintosStyleImparMapfre.setFillForegroundColor(fondoImpar);
			distintosStyleImparMapfre.setFont(fail);
			
			int contComparacion = 1;
			int contIndra = 1;
			int contMapfre = 1;
			
			if (tipoFichero.equals(STREAM_NAME_DETALLE)) {
				List<DetalleCorriente> listaIndra = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerIndra.read()) != null) {
					listaIndra.add(detalle);
				}
				
				List<DetalleCorriente> listaMapfre = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerMapfre.read()) != null) {
					listaMapfre.add(detalle);
				}
				
				if (listaMapfre.size() != listaIndra.size()) {
					LanzarPruebas.LOG.warn("NO COINCIDEN EL NUMERO DE PERIODOS, INDRA = {} , MAPFRE = {}", listaIndra.size(), listaMapfre.size());
				}
				
				int tamanio = listaIndra.size();
				if (listaMapfre.size() > tamanio) {
					tamanio = listaMapfre.size();
				}
				
				for (int i = 0; i < tamanio; i++) {
					
					DetalleCorriente valor;
					DetalleCorriente valorMapfre;
					if (i < listaIndra.size()) {
						valor = listaIndra.get(i);
					} else {
						valor = new DetalleCorriente();
						valor.setTotalFlujoProyeccion(new TotalFlujoProyeccion());
					}
					if (i < listaMapfre.size()) {
						valorMapfre = listaMapfre.get(i);
					} else {
						valorMapfre = new DetalleCorriente();
						valorMapfre.setTotalFlujoProyeccion(new TotalFlujoProyeccion());
					}
					
					if (i % 2 == 0) {
						datoStyleIndraComparador = datoStyleParIndraComparador;
						distintosStyleIndraComparador = distintosStyleParIndraComparador;
						datoStyleMapfreComparador = datoStyleParMapfreComparador;
						distintosStyleMapfreComparador = distintosStyleParMapfreComparador;
						datoStyleIndra = datoStyleParIndra;
						distintosStyleIndra = distintosStyleParIndra;
						datoStyleMapfre = datoStyleParMapfre;
						distintosStyleMapfre = distintosStyleParMapfre;
					} else {
						datoStyleIndraComparador = datoStyleImparIndraComparador;
						distintosStyleIndraComparador = distintosStyleImparIndraComparador;
						datoStyleMapfreComparador = datoStyleImparMapfreComparador;
						distintosStyleMapfreComparador = distintosStyleImparMapfreComparador;
						datoStyleIndra = datoStyleImparIndra;
						distintosStyleIndra = distintosStyleImparIndra;
						datoStyleMapfre = datoStyleImparMapfre;
						distintosStyleMapfre = distintosStyleImparMapfre;
					}
					
					// Si solo nos interesa generar los datos de Indra con la cabecera hay que mostrar todas las lineas, no solo las que coincidan
					if (!valor.equals(valorMapfre)) {
						
						XSSFRow rowComparacionIndra = null;
						rowComparacionIndra = sheetComparacion.createRow(contComparacion);
						contComparacion++;
						XSSFRow rowComparacionMapfre = null;
						rowComparacionMapfre = sheetComparacion.createRow(contComparacion);
						contComparacion++;
						
						rowIndra = sheetIndra.createRow(contIndra);
						contIndra++;
						rowMapfre = sheetMapfre.createRow(contMapfre);
						contMapfre++;
						
						int index = 0;
						
						writeCell(valor.getCnegocio(), valorMapfre.getCnegocio(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCcanal(), valorMapfre.getCcanal(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCcartera(), valorMapfre.getCcartera(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getFcierre(), valorMapfre.getFcierre(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getBt(), valorMapfre.getBt(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKmodalidad(), valorMapfre.getKmodalidad(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKpoliza().toString(), valorMapfre.getKpoliza().toString(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKsubpoliza(), valorMapfre.getKsubpoliza(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKcertificado(), valorMapfre.getKcertificado(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getNsuscri(), valorMapfre.getNsuscri(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getNorden(), valorMapfre.getNorden(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKgarantia(), valorMapfre.getKgarantia(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKprestacion(), valorMapfre.getKprestacion(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKajuste(), valorMapfre.getKajuste(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCtipoaport(), valorMapfre.getCtipoaport(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getFechaDesde(), valorMapfre.getFechaDesde(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getFechaHasta(), valorMapfre.getFechaHasta(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						
						index = writeBloque(valor.getBloqueVida(), valorMapfre.getBloqueVida(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloqueFall(), valorMapfre.getBloqueFall(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloqueCompl(), valorMapfre.getBloqueCompl(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloqueGto(), valorMapfre.getBloqueGto(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloqueComi(), valorMapfre.getBloqueComi(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloqueRte(), valorMapfre.getBloqueRte(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						index = writeBloque(valor.getBloquePrim(), valorMapfre.getBloquePrim(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index);
						
						final TotalFlujoProyeccion indraTotalFlujoProyeccion = valor.getTotalFlujoProyeccion();
						final TotalFlujoProyeccion mapfreTotalFlujoProyeccion = valorMapfre.getTotalFlujoProyeccion();
						writeCell(indraTotalFlujoProyeccion.getSumfprob(), mapfreTotalFlujoProyeccion.getSumfprob(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getSumfprobtanul(), mapfreTotalFlujoProyeccion.getSumfprobtanul(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getSumprovision(), mapfreTotalFlujoProyeccion.getSumprovision(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getSumcola(), mapfreTotalFlujoProyeccion.getSumcola(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getProvbtiproy(), mapfreTotalFlujoProyeccion.getProvbtiproy(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getTerminalAnterior(), mapfreTotalFlujoProyeccion.getTerminalAnterior(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(indraTotalFlujoProyeccion.getTerminalPosterior(), mapfreTotalFlujoProyeccion.getTerminalPosterior(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
					}
				}
				
				workBook.write(out);
				out.close();
			} else {
				List<TotalesFlujos> listaIndra = new ArrayList<TotalesFlujos>();
				while ((totales = (TotalesFlujos) readerIndra.read()) != null) {
					listaIndra.add(totales);
				}
				
				List<TotalesFlujos> listaMapfre = new ArrayList<TotalesFlujos>();
				while ((totales = (TotalesFlujos) readerMapfre.read()) != null) {
					listaMapfre.add(totales);
				}
				
				if (listaMapfre.size() != listaIndra.size()) {
					LanzarPruebas.LOG.warn("NO COINCIDEN EL NUMERO DE PERIODOS, INDRA = {} , MAPFRE = {}", listaIndra.size(),listaMapfre.size());
				}

				int tamanio = listaIndra.size();
				if (listaMapfre.size() > tamanio) {
					tamanio = listaMapfre.size();
				}
				
				for (int i = 0; i < tamanio; i++) {
					
					TotalesFlujos valor;
					TotalesFlujos valorMapfre;
					if (i < listaIndra.size()) {
						valor = listaIndra.get(i);
					} else {
						valor = new TotalesFlujos();
					}
					
					if (i < listaMapfre.size()) {
						valorMapfre = listaMapfre.get(i);
					} else {
						valorMapfre = new TotalesFlujos();
					}
					
					if (i % 2 == 0) {
						datoStyleIndraComparador = datoStyleParIndraComparador;
						distintosStyleIndraComparador = distintosStyleParIndraComparador;
						datoStyleMapfreComparador = datoStyleParMapfreComparador;
						distintosStyleMapfreComparador = distintosStyleParMapfreComparador;
						datoStyleIndra = datoStyleParIndra;
						distintosStyleIndra = distintosStyleParIndra;
						datoStyleMapfre = datoStyleParMapfre;
						distintosStyleMapfre = distintosStyleParMapfre;
					} else {
						datoStyleIndraComparador = datoStyleImparIndraComparador;
						distintosStyleIndraComparador = distintosStyleImparIndraComparador;
						datoStyleMapfreComparador = datoStyleImparMapfreComparador;
						distintosStyleMapfreComparador = distintosStyleImparMapfreComparador;
						datoStyleIndra = datoStyleImparIndra;
						distintosStyleIndra = distintosStyleImparIndra;
						datoStyleMapfre = datoStyleImparMapfre;
						distintosStyleMapfre = distintosStyleImparMapfre;
					}
					
					// Si solo nos interesa generar los datos de Indra con la cabecera hay que mostrar todas las lineas, no solo las que coincidan
					if (!valor.equals(valorMapfre)) {

						XSSFRow rowComparacionIndra = null;
						rowComparacionIndra = sheetComparacion.createRow(contComparacion);
						contComparacion++;
						XSSFRow rowComparacionMapfre = null;
						rowComparacionMapfre = sheetComparacion.createRow(contComparacion);
						contComparacion++;
						
						rowIndra = sheetIndra.createRow(contIndra);
						contIndra++;
						rowMapfre = sheetMapfre.createRow(contMapfre);
						contMapfre++;
						
						int index = 0;
						
						writeCell(valor.getCnegocio(), valorMapfre.getCnegocio(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCcanal(), valorMapfre.getCcanal(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCcartera(), valorMapfre.getCcartera(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getFcierre(), valorMapfre.getFcierre(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getBt(), valorMapfre.getBt(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKmodalidad(), valorMapfre.getKmodalidad(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKpoliza().toString(), valorMapfre.getKpoliza().toString(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKsubpoliza(), valorMapfre.getKsubpoliza(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKcertificado(), valorMapfre.getKcertificado(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getNsuscri(), valorMapfre.getNsuscri(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getNorden(), valorMapfre.getNorden(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKgarantia(), valorMapfre.getKgarantia(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKprestacion(), valorMapfre.getKprestacion(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getKajuste(), valorMapfre.getKajuste(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getCtipoaport(), valorMapfre.getCtipoaport(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						
						writeCell(valor.getTotfpvida(), valorMapfre.getTotfpvida(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnavida(), valorMapfre.getTotfpnavida(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactvida(), valorMapfre.getTotfactvida(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolavida(), valorMapfre.getTotcolavida(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpfall(), valorMapfre.getTotfpfall(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnafall(), valorMapfre.getTotfpnafall(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactfall(), valorMapfre.getTotfactfall(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolafall(), valorMapfre.getTotcolafall(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpcompl(), valorMapfre.getTotfpcompl(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnacompl(), valorMapfre.getTotfpnacompl(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactcompl(), valorMapfre.getTotfactcompl(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolacompl(), valorMapfre.getTotcolacompl(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpgto(), valorMapfre.getTotfpgto(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnagto(), valorMapfre.getTotfpnagto(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactgto(), valorMapfre.getTotfactgto(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolagto(), valorMapfre.getTotcolagto(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpcom(), valorMapfre.getTotfpcom(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnacom(), valorMapfre.getTotfpnacom(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactcom(), valorMapfre.getTotfactcom(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolacom(), valorMapfre.getTotcolacom(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfprte(), valorMapfre.getTotfprte(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnarte(), valorMapfre.getTotfpnarte(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactrte(), valorMapfre.getTotfactrte(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++); 
						writeCell(valor.getTotcolarte(), valorMapfre.getTotcolarte(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpprim(), valorMapfre.getTotfpprim(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfpnaprim(), valorMapfre.getTotfpnaprim(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfactprim(), valorMapfre.getTotfactprim(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcolaprim(), valorMapfre.getTotcolaprim(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfprob(), valorMapfre.getTotfprob(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotfprobtanul(), valorMapfre.getTotfprobtanul(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotprovision(), valorMapfre.getTotprovision(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getTotcola(), valorMapfre.getTotcola(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
						writeCell(valor.getProvbtifcal(), valorMapfre.getProvbtifcal(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
					}
				}
				
				workBook.write(out);
				out.close();
			}
		} catch (IOException e) {
			LanzarPruebas.LOG.error("Error: ".concat(e.getMessage()), e);
			System.exit(-1);
		}
		
		LanzarPruebas.LOG.info("Finalizado ".concat(ficheroSalida));
	}
	
	private static void escribirCabecera(XSSFWorkbook workBook, XSSFSheet sheet, String tipoFichero, XSSFRow row) {
		
		XSSFCellStyle cabeceraStyle = workBook.createCellStyle();
		
		cabeceraStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cabeceraStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		cabeceraStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cabeceraStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cabeceraStyle.setBorderColor(BorderSide.BOTTOM, new XSSFColor(Color.red));
		cabeceraStyle.setBorderColor(BorderSide.TOP, new XSSFColor(Color.red));
		cabeceraStyle.setBorderColor(BorderSide.LEFT, new XSSFColor(Color.red));
		cabeceraStyle.setBorderColor(BorderSide.RIGHT, new XSSFColor(Color.red));
		
		cabeceraStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cabeceraStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cabeceraStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
		
		sheet.setDefaultColumnWidth(20);
		if (tipoFichero.equals(STREAM_NAME_DETALLE)) {
			for (int i = 0; i < TEXTOS_CABECERAS.length; i++) {
				writeCell(row.createCell(i), cabeceraStyle, TEXTOS_CABECERAS[i]);
				
			}
		} else {
			for (int i = 0; i < TEXTOS_CABECERAS_TOTALES.length; i++) {
				writeCell(row.createCell(i), cabeceraStyle, TEXTOS_CABECERAS_TOTALES[i]);
				
			}
		}
	}
	
	private static int writeBloque(BloqueCorriente bloqueIndra, BloqueCorriente bloqueMapfre, XSSFRow rowComparacionIndra, XSSFRow rowComparacionMapfre, XSSFRow rowIndra, XSSFRow rowMapfre, int index) {
		
		BloqueCorriente valor;
		BloqueCorriente valorMapfre;
		if (bloqueIndra == null) {
			valor = new BloqueCorriente();
		} else {
			valor = bloqueIndra;
		}
		if (bloqueMapfre == null) {
			valorMapfre = new BloqueCorriente();
		} else {
			valorMapfre = bloqueMapfre;
		}
		
		writeCell(valor.getFechaDevengo(), valorMapfre.getFechaDevengo(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getFechaPago(), valorMapfre.getFechaPago(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getImpFlujoNominal(), valorMapfre.getImpFlujoNominal(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getFpbProbable(), valorMapfre.getFpbProbable(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getImpFlujoProbable(), valorMapfre.getImpFlujoProbable(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getFpbAtc(), valorMapfre.getFpbAtc(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getImpFlujoNoAnulado(), valorMapfre.getImpFlujoNoAnulado(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getFpbAtcfin(), valorMapfre.getFpbAtcfin(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getImpFlujoActualizado(), valorMapfre.getImpFlujoActualizado(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);
		writeCell(valor.getImpProvi(), valorMapfre.getImpProvi(), rowComparacionIndra, rowComparacionMapfre, rowIndra, rowMapfre, index++);

		return index;
	}
	
	private static void writeCell(String valor, String valorMapfre, XSSFRow rowComparacionIndra, XSSFRow rowComparacionMapfre, XSSFRow rowIndra, XSSFRow rowMapfre, int cellIndex) {
		if (valor == valorMapfre || (valor != null && valor.equals(valorMapfre)) || (valorMapfre != null && valorMapfre.equals(valor))) {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), datoStyleIndraComparador, valor);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), datoStyleMapfreComparador, valorMapfre);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), datoStyleIndra, valor);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), datoStyleMapfre, valorMapfre);
			}
		} else {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), distintosStyleIndraComparador, valor);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), distintosStyleMapfreComparador, valorMapfre);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), distintosStyleIndra, valor);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), distintosStyleMapfre, valorMapfre);
			}
		}
	}
	
	private static void writeCell(BigDecimal valor, BigDecimal valorMapfre, XSSFRow rowComparacionIndra, XSSFRow rowComparacionMapfre, XSSFRow rowIndra, XSSFRow rowMapfre,int cellIndex) {
		
		if (comparadorPrecision(valor, valorMapfre)) {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), datoStyleIndraComparador, valor, false);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), datoStyleMapfreComparador, valorMapfre, false);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), datoStyleIndra, valor, true);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), datoStyleMapfre, valorMapfre, true);
			}
		} else {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), distintosStyleIndraComparador, valor, false);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), distintosStyleMapfreComparador, valorMapfre, false);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), distintosStyleIndra, valor, true);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), distintosStyleMapfre, valorMapfre, true);
			}
		}
	}
	
	private static void writeCell(Timestamp valor, Timestamp valorMapfre, XSSFRow rowComparacionIndra, XSSFRow rowComparacionMapfre, XSSFRow rowIndra, XSSFRow rowMapfre, int cellIndex) {
		if (valor == valorMapfre || (valor != null && valor.equals(valorMapfre)) || (valorMapfre != null && valorMapfre.equals(valor))) {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), datoStyleIndraComparador, valor, false);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), datoStyleMapfreComparador, valorMapfre, false);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), datoStyleIndra, valor, true);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), datoStyleMapfre, valorMapfre, true);
			}
		} else {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), distintosStyleIndraComparador, valor, false);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), distintosStyleMapfreComparador, valorMapfre, false);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), distintosStyleIndra, valor, true);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), distintosStyleMapfre, valorMapfre, true);
			}
		}
	}
	
	private static void writeCell(Integer valor, Integer valorMapfre, XSSFRow rowComparacionIndra, XSSFRow rowComparacionMapfre, XSSFRow rowIndra, XSSFRow rowMapfre, int cellIndex) {
		if (valor == valorMapfre || (valor != null && valor.equals(valorMapfre)) || (valorMapfre != null && valorMapfre.equals(valor))) {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), datoStyleIndraComparador, valor);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), datoStyleMapfreComparador, valorMapfre);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), datoStyleIndra, valor);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), datoStyleMapfre, valorMapfre);
			}
		} else {
			if (rowComparacionIndra != null) {
				writeCell(rowComparacionIndra.createCell(cellIndex), distintosStyleIndraComparador, valor);
			}
			if (rowComparacionMapfre != null) {
				writeCell(rowComparacionMapfre.createCell(cellIndex), distintosStyleMapfreComparador, valorMapfre);
			}
			if (rowIndra != null) {
				writeCell(rowIndra.createCell(cellIndex), distintosStyleIndra, valor);
			}
			if (rowMapfre != null) {
				writeCell(rowMapfre.createCell(cellIndex), distintosStyleMapfre, valorMapfre);
			}
		}
	}
	
	private static boolean comparadorPrecision(BigDecimal valorIndra, BigDecimal valorMapfre) {
		return valorIndra == valorMapfre || (valorIndra != null && valorIndra.equals(valorMapfre)) || (valorMapfre != null && valorMapfre.equals(valorIndra)) ||
				(valorIndra != null && valorMapfre != null && diferenciaPermitida.compareTo(valorIndra.subtract(valorMapfre).abs()) == 1);
	}
	
	private static void writeCell(XSSFCell cell, XSSFCellStyle cellStyle, String cellValue) {
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}
	
	private static void writeCell(XSSFCell cell, XSSFCellStyle cellStyle, BigDecimal cellValue, boolean conFormato) {
		cell.setCellStyle(cellStyle);
		if (cellValue != null) {
			if (conFormato) {
				cell.setCellValue(cellValue.doubleValue());
			} else {
				cell.setCellValue(cellValue.toString());
			}
		}
	}
	
	private static void writeCell(XSSFCell cell, XSSFCellStyle cellStyle, Timestamp cellValue, boolean conFormato) {
		cell.setCellStyle(cellStyle);
		if (cellValue != null) {
			if (conFormato) {
				cell.setCellValue(UtilFechas.obtenerStringFechaConFormato(cellValue));
			} else {
				cell.setCellValue(cellValue.toString());
			}
		}
	}
	
	private static void writeCell(XSSFCell cell, XSSFCellStyle cellStyle, Integer cellValue) {
		cell.setCellStyle(cellStyle);
		if (cellValue != null) {
			cell.setCellValue(cellValue);
		}
	}
}
