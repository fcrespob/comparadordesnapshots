package es.mapfre.solvencia.utils.pruebas;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosReales;
import es.mapfre.scr.tablasExperiencia.dominio.entidades.LongevidadModInterno;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.entregables.Basetec;
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
import es.mapfre.solvencia.dominio.entregables.FlujCoaSeg;
import es.mapfre.solvencia.dominio.entregables.FlujInf1;
import es.mapfre.solvencia.dominio.entregables.FlujInf2;
import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.PesosBt;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.entregables.PrvBt;
import es.mapfre.solvencia.dominio.entregables.PrvCr;
import es.mapfre.solvencia.dominio.entregables.PrvFpb;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;
import es.mapfre.solvencia.dominio.entregables.PrvInf2;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;
import es.mapfre.solvencia.dominio.gbt.AdapTablaExp;
import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;
import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;
import es.mapfre.solvencia.dominio.gbt.AsigTasasAnul;
import es.mapfre.solvencia.dominio.gbt.ConvTablasExpReal;
import es.mapfre.solvencia.dominio.gbt.ConversionTablasExp;
import es.mapfre.solvencia.dominio.gbt.DiferencialGastos;
import es.mapfre.solvencia.dominio.gbt.InteresTecnico;
import es.mapfre.solvencia.dominio.gbt.MetodosAdaptacion;
import es.mapfre.solvencia.dominio.gbt.ObtInteresTecnico;
import es.mapfre.solvencia.dominio.gbt.PeriodosAd;
import es.mapfre.solvencia.dominio.gbt.RentaGAP;
import es.mapfre.solvencia.dominio.gbt.TasasAnulacion;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;
import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;
import es.mapfre.solvencia.dominio.maestro.DatosPbTecnica;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.maestro.PolizaInstrumental;
import es.mapfre.solvencia.dominio.maestro.Tabla2000;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ComisionesParticipadasCOM;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CurvasTipo656;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesConstantesRescates;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesSolvencia2;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.RegistroParametros;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class MapeoCatalogos {

	private static final Logger log = LoggerFactory.getLogger(MapeoCatalogos.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String GBT_BEANIO_CONFIG_XML = "beanio/gbt-beanio-config.xml";
	private static final String PROXY_BEANIO_CONFIG_XML = "beanio/beanio-config-proxy.xml";
	private static final String BEANIO_CONFIG_OUT_XML = "beanioPruebas/beanio-config-out_pruebas.xml";
	private static XSSFCellStyle datoStyleParNfq;
	private static XSSFCellStyle datoStyleImparNfq;
	private static XSSFSheet sheetNfq;
	
	private static XSSFCellStyle datoStyleNfq;

	private static BeanIOReader readerNfq;
	
	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void main(String[] args) {

		try {
			String rutabase = System.getProperty("rutabase");
			if (!rutabase.endsWith("/")){
				rutabase = rutabase + "/";
			}
			String rutabaseProxy = System.getProperty("rutabaseProxy");
			if (!rutabaseProxy.endsWith("/")){
				rutabaseProxy = rutabaseProxy + "/";
			}
			String fichero = System.getProperty("catalogo");
			String feccierre = System.getProperty("feccierre");
			String canal = System.getProperty("canal");
			String negocio = System.getProperty("negocio");
			if (negocio.equals("I")){
				negocio = "BRUTOSIND";
			} else {
				negocio = "BRUTOSCOL";
			}
			
			String rutaCatSalida = "";
			String ficheroNfq = "";
			if (fichero.contains("GENCARTE")){
				rutaCatSalida = rutabase + feccierre + "/CTEC/" + fichero + ".xlsx";
				ficheroNfq = rutabase + feccierre + "/CTEC/" + fichero + ".TXT";
			} else if (fichero.contains("FICHA")){
				rutaCatSalida = rutabase + feccierre + "/FICHAS/FICHASPEND/" + fichero + ".xlsx";
				ficheroNfq = rutabase + feccierre + "/FICHAS/FICHASPEND/" + fichero + ".TXT";
			} else if (fichero.contains("CTLG_")|| fichero.contains("GBT_")){
				rutaCatSalida = rutabase + "CATALOGOS/" + fichero + ".xlsx";
				ficheroNfq = rutabase + "CATALOGOS/" + fichero + ".TXT";
			} else if (fichero.contains("GESINTRO")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/SINIESTROS/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/SINIESTROS/" + fichero + ".TXT";
			} else if (fichero.contains("RTE_VTO_MANUAL")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTE_VTO_MANUAL/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTE_VTO_MANUAL/" + fichero + ".TXT";
			} else if (fichero.contains("RTE_IND")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTE_IND/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTE_IND/" + fichero + ".TXT";
			} else if (fichero.contains("VTO_IND")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/VTO_IND/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/VTO_IND/" + fichero + ".TXT";
			} else if (fichero.contains("RTE_VTO_COL")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTE_VTO_COL/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTE_VTO_COL/" + fichero + ".TXT";
			} else if (fichero.contains("ANT_IND")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/ANT_IND/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/ANT_IND/" + fichero + ".TXT";
			} else if (fichero.contains("ASEVAL")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/ASEVAL/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/ASEVAL/" + fichero + ".TXT";
			} else if (fichero.contains("AS400")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/AS400/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/AS400/" + fichero + ".TXT";
			} else if (fichero.contains("RTA_NEO")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTA_NEO/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTA_NEO/" + fichero + ".TXT";
			} else if (fichero.contains("MOV_NEO")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/MOV_NEO/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/MOV_NEO/" + fichero + ".TXT";
			} else if (fichero.contains("PRE_ONS")){
				rutaCatSalida = rutabaseProxy + feccierre + "/PRE_ONS/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/PRE_ONS/" + fichero + ".TXT";
			} else if (fichero.contains("RTA_COL_IDMS")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTA_COL/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTA_COL/" + fichero + ".TXT";
			} else if (fichero.contains("RTA_IND_IDMS")) {
				rutaCatSalida = rutabaseProxy + feccierre + "/RTA_IND_TRAD/" + fichero + ".xlsx";
				ficheroNfq = rutabaseProxy + feccierre + "/RTA_IND_TRAD/" + fichero + ".TXT";
			} else if (fichero.contains("PESOSBTPROXY")) {
				rutaCatSalida = rutabase + feccierre + "/" + canal + "/PROXY/" + fichero + ".xlsx";
				ficheroNfq = rutabase + feccierre + "/" + canal + "/PROXY/" +  fichero + ".TXT";
			} else if (fichero.contains(feccierre)){
				rutaCatSalida = rutabase + feccierre + "/" + canal + "/" + negocio + "/" + fichero + ".xlsx";
				ficheroNfq = rutabase + feccierre + "/" + canal + "/" + negocio + "/" + fichero + ".TXT";
			} else {
				System.out.print("El nombre del fichero no es válido");
				return;
			}
			
			System.out.println("Trabajando...");
			String beanio;
			
			switch (fichero){
				case "CTLG_RW06646":
					beanio = "RW06646";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB005CTM0":
					beanio = "CTM0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB005TXP0":
					beanio = "TXP0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB310CPR0":
					beanio = "CPR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB310VCR0":
					beanio = "VCR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB340ERR0":
					beanio = "ERR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880AUX0":
					beanio = "AUX0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880DIP0":
					beanio = "DIP0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880DPG0":
					beanio = "DPG0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880DPR0":
					beanio = "DPR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880FLP0":
					beanio = "FLP0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880IPC0":
				case "GBT_TB340SCRIPC0":
					beanio = "IPC0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880OPG0":
					beanio = "OPG0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880SOL0":
					beanio = "SOL0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_X880JI08":
					beanio = "X880JI08";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ACI0":
					beanio = "ACI0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340AIT0":
					beanio = "AIT0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ATA0":
					beanio = "ATA0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ATE0":
					beanio = "ATE0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ATR0":
					beanio = "ATR0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340CCI0":
					beanio = "CCI0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340CTA0":
					beanio = "CTA0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340CTE0":
					beanio = "CTE0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340CTU0":
					beanio = "CTU0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340DGI0":
					beanio = "DGI0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340SCR0":
					beanio = "SCR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340GAR0":
					beanio = "GAR0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340GRE0":
				case "GBT_TB340SCRGRE0":
					beanio = "GRE0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ITG0":
					beanio = "ITG0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340ITR0":
					beanio = "ITR0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340MAD0":
					beanio = "MAD0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340MAT0":
					beanio = "MAT0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340MCI0":
					beanio = "MCI0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340MVI0":
					beanio = "MVI0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340VCI0":
					beanio = "VCI0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340VMA0":
				case "GBT_TB340SCRAEP0":
				case "GBT_TB340SCRAEN0":
				case "GBT_TB340SCRAIP0":
				case "GBT_TB340SCRAIN0":
					beanio = "VMA0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340VTA0":
					beanio = "VTA0";
					readerNfq = new BeanIOReader(GBT_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB340VTR0":
				case "GBT_TB340SCRMFE0":
				case "GBT_TB340SCRMMI0":
				case "GBT_TB340SCRLFE0":
				case "GBT_TB340SCRLMI0":
				case "GBT_TB340SCRMCF0":
				case "GBT_TB340SCRMCI0":
					beanio = "VTR0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "CTLG_TB880LMI0":
					beanio = "LMI0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GBT_TB880COM0":
					beanio = "COM0";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_AMORTIZ":
					beanio = "X880J009";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_DATOSCOA":
					beanio = "X880JI03";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_ESPECIFIC":
					beanio = "X880J004";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_PAGOSPLAN":
					beanio = "X880J003";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_PBTECNI":
					beanio = "X880JI04";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_VLIQUID":
					beanio = "X880J005";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_PTIPO":
					beanio = "PTIPO";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_MAESTRO_UNIFICADO":
					beanio = "X880JI01";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_TAB2000": 
					beanio = "TABLA2000";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "GENCARTE_AUX_INSTRUMENTAL":
					beanio = "X880J007";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				case "FICHASCALCULO":
					beanio = "R340T000";
					readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, beanio);
					break;
				default:
					if (fichero.contains("PRVINF1")){
						beanio = "prvinf1";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PRVINF2")){
						beanio = "prvinf2";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PRVUMIC")){
						beanio = "prvumic";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PRVCR")){
						beanio = "prvcr";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PRVBT")){
						beanio = "prvbt";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJINF1")){
						beanio = "flujinf1";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJINF2")){
						beanio = "flujinf2";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PROVCOASEG")){
						beanio = "provcoaseg";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJCOASEG")){
						beanio = "flujcoaseg";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("BASETEC")){
						beanio = "basetec";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("PRVFPB")){
						beanio = "prvfpb";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJTCAS")){
						beanio = "flujtcas";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("CONTAB")){
						beanio = "contab";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJOSDET")){
						beanio = "detalle-corriente";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("MAESBTC")){
						beanio = "detalle-basetecnicas";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJOSTOT")){
						beanio = "totales-flujos";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJOSTOTP")){
						beanio = "flujostotp";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJINF3")){
						beanio = "flujinf3";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("FLUJOSTN17")){
						beanio = "flujostn17";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("SCRVM_SCRVM")){
						beanio = "scrvm";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else if (fichero.contains("INCIDENCIAS")){
						beanio = "incidencias";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					}  else if (fichero.contains("PESOSBTPROXY")){
						beanio = "PESIND0";
						readerNfq = new BeanIOReader(PROXY_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					}  else if (fichero.contains("PESOS")){
						beanio = "pesosbt";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					}  else if (fichero.contains("GESINTRO") ||
							fichero.contains("RTE_IND") ||
							fichero.contains("VTO_IND") ||
							fichero.contains("RTE_VTO") ||
							fichero.contains("ANT_IND") ||
							fichero.contains("ASEVAL") ||
							fichero.contains("AS400") ||
							fichero.contains("RTA_NEO") ||
							fichero.contains("MOV_NEO") ||
							fichero.contains("PRE_ONS") ||
							fichero.contains("RTA_IND_IDMS") ||
							fichero.contains("RTA_COL_IDMS")){
						beanio = "REA0";
						readerNfq = new BeanIOReader(PROXY_BEANIO_CONFIG_XML, ficheroNfq, beanio);
					}  if (fichero.contains("PTIPO_AUT")){
						beanio = "PTIPO";
						readerNfq = new BeanIOReader(BEANIO_CONFIG_OUT_XML, ficheroNfq, beanio);
					} else {
						beanio = "";
					}
					break;
			}
			
			if (null != beanio || !beanio.equals("")){
				
				// create a new file
				FileOutputStream out = new FileOutputStream(rutaCatSalida);
				// create a new workbook
				XSSFWorkbook workBook = new XSSFWorkbook();
				sheetNfq = workBook.createSheet("Nfoque_" + beanio);
				
				XSSFCellStyle cabeceraStyle = workBook.createCellStyle();
				
				cabeceraStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
				
				cabeceraStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				cabeceraStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				cabeceraStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
				
				datoStyleParNfq = workBook.createCellStyle();
				datoStyleParNfq.setBorderTop(CellStyle.BORDER_MEDIUM);
				datoStyleParNfq.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleParNfq.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleParNfq.setAlignment(CellStyle.ALIGN_RIGHT);
				
				datoStyleImparNfq = workBook.createCellStyle();
				datoStyleImparNfq.setBorderTop(CellStyle.BORDER_MEDIUM);
				datoStyleImparNfq.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleImparNfq.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleImparNfq.setAlignment(CellStyle.ALIGN_RIGHT);
				datoStyleImparNfq.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				datoStyleImparNfq.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				
				switch (fichero){
					case "CTLG_RW06646":
						transformarRW06646(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB005CTM0":
						transformarCTM0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB005COM0":
						transformarCOM0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB005CTU0":
						transformarCTU0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB005TXP0":
						transformarTXP0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB310CPR0":
						transformarCPR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB310VCR0":
						transformarVCR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB340ERR0":
						transformarERR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880AUX0":
						transformarAUX0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880DIP0":
						transformarDIP0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880DPR0":
						transformarDPR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880FLP0":
						transformarFLP0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880IPC0":
					case "GBT_TB340SCRIPC0":
						transformarIPC0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880OPG0":
						transformarOPG0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880SOL0":
						transformarSOL0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_X880JI08":
						transformarX880JI08(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ACI0":
						transformarACI0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340AIT0":
						transformarAIT0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ATA0":
						transformarATA0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ATE0":
						transformarATE0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ATR0":
						transformarATR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340CTE0":
						transformarCTE0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340CTU0":
						transformarCTU0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340DGI0":
						transformarDGI0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340GAR0":
						transformarGAR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340SCR0":
						transformarSCR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340GRE0":
					case "GBT_TB340SCRGRE0":
						transformarGRE0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ITG0":
						transformarITG0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340ITR0":
						transformarITR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340MAD0":
						transformarMAD0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340MVI0":
						transformarMVI0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340VCI0":
						transformarVCI0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340VMA0":
					case "GBT_TB340SCRAEP0":
					case "GBT_TB340SCRAEN0":
					case "GBT_TB340SCRAIP0":
					case "GBT_TB340SCRAIN0":
						transformarVMA0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340VTA0":
						transformarVTA0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GBT_TB340VTR0":
					case "GBT_TB340SCRMFE0":
					case "GBT_TB340SCRMMI0":
					case "GBT_TB340SCRLFE0":
					case "GBT_TB340SCRLMI0":
					case "GBT_TB340SCRMCF0":
					case "GBT_TB340SCRMCI0":
						transformarVTR0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "CTLG_TB880LMI0":
						transformarLMI0(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_AMORTIZ":
						transformarAuxAmortiz(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_DATOSCOA":
						transformarAuxDatoscoa(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_ESPECIFIC":
						transformarAuxEspecific(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_PAGOSPLAN":
						transformarAuxPagosPlan(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_PBTECNI":
						transformarAuxPBTecni(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_VLIQUID":
						transformarAuxVLiquid(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_PTIPO":
						transformarPTipo(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_MAESTRO_UNIFICADO":
						transformarMaestro(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						break;
					case "GENCARTE_AUX_TAB2000": 
						transformarTABLA2000(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq); 
						break;
					case "GENCARTE_AUX_INSTRUMENTAL": 
						transformarInstrumental(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq); 
						break;
					case "FICHASCALCULO":
						transformarFichasCalculo(workBook, datoStyleParNfq, datoStyleImparNfq);
						break;
					default:
						if (fichero.contains("PRVINF1")){
							transformarPRVINF1(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PRVINF2")){
							transformarPRVINF2(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PRVUMIC")){
							transformarPRVUMIC(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PRVCR")){
							transformarPRVCR(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PRVBT")){
							transformarPRVBT(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJINF1")){
							transformarFLUJINF1(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJINF2")){
							transformarFLUJINF2(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PROVCOASEG")){
							transformarPROVCOASEG(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJCOASEG")){
							transformarFLUJCOASEG(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("BASETEC")){
							transformarBASETEC(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PRVFPB")){
							transformarPRVFPB(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJTCAS")){
							transformarFLUJTCAS(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("CONTAB")){
							transformarCONTAB(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJOSDET")){
							transformarFLUJOSDET(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("MAESBTC")){
							transformarMAESBTC(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJOSTOT")){
							transformarFLUJOSTOT(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJOSTOTP")){
							transformarFLUJOSTOTP(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJINF3")){
							transformarFLUJINF3(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("FLUJOSTN17")){
							transformarFLUJOSTN17(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("SCRVM_SCRVM")){
							transformarSCRVM(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("INCIDENCIAS")){
							transformarINCIDENCIAS(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PESOSBTPROXY")){
							transformarPESOSBTPROXY(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						}  else if (fichero.contains("PESOS")){
							transformarPESOS(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						}  else if (fichero.contains("GESINTRO") ||
								fichero.contains("RTE_IND") ||
								fichero.contains("VTO_IND") ||
								fichero.contains("RTE_VTO") ||
								fichero.contains("ANT_IND") ||
								fichero.contains("ASEVAL") ||
								fichero.contains("AS400") ||
								fichero.contains("RTA_NEO") ||
								fichero.contains("MOV_NEO") ||
								fichero.contains("PRE_ONS") ||
								fichero.contains("RTA_IND_IDMS") ||
								fichero.contains("RTA_COL_IDMS")){
							transformarPresReales(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else if (fichero.contains("PTIPO_AUT")){
							transformarPTIPO(cabeceraStyle, datoStyleParNfq, datoStyleImparNfq);
						} else {
							beanio = "";
						}
						break;
				}
				
				workBook.write(out);
				out.close();
			}
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}

		log.info("Finalizado");
	}

	
	private static void transformarPESOSBTPROXY(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio","ccanal","ccartera","fcierre","kmodalidad","kpoliza",
				"ksubpoliza","kcertificado","cohorte","swcasado","totFactActT","pesosUoA"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt detalle = null;
		int cont=1;
		List<es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt> listaNfq = 
				new ArrayList<es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt>();
		while ((detalle = (es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			es.mapfre.proxy.prestaciones.dominio.entidades.PesosBt valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getCohorte(), rowNfq, index++);
			writeCell(valor.getSwcasado(), rowNfq, index++);
			writeCell(valor.getTotFactActT(), rowNfq, index++);
			writeCell(valor.getPesosUoA(), rowNfq, index++);
		}
	}


	private static void transformarInstrumental(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"KmodalidadInstr","KpolizaInstr","KsubpolizaInstr","KcertificadoInstr",
				"KmodalidadOrig","KpolizaOrig","KsubpolizaOrig","KcertificadoOrig"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PolizaInstrumental detalle = null;
		int cont=1;
		List<PolizaInstrumental> listaNfq = new ArrayList<PolizaInstrumental>();
		while ((detalle = (PolizaInstrumental) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PolizaInstrumental valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getKmodalidadInstr(), rowNfq, index++);
			writeCell(valor.getKpolizaInstr().toString(), rowNfq, index++);
			writeCell(valor.getKsubpolizaInstr(), rowNfq, index++);
			writeCell(valor.getKcertificadoInstr(), rowNfq, index++);
			writeCell(valor.getKmodalidadOrig(), rowNfq, index++);
			writeCell(valor.getKpolizaOrig().toString(), rowNfq, index++);
			writeCell(valor.getKsubpolizaOrig(), rowNfq, index++);
			writeCell(valor.getKcertificadoOrig(), rowNfq, index++);
		}
		
	}


	private static void transformarPresReales(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"codCia","numExp","anioMes","numMvto","fillerMvto","codCob","codCtoRva",
				"codProductor","claseProductor","claseMediador","tipDocum","codDocum","ofDirecta",
				"codTerceroAgt","fecMovExp","tipExp","codTipcGrpTipExp","fecOcurSini","fecApertExp",
				"fecReapertExp","fecTermExp","codMon","claseExp","numPoliza","numApli",
				"numRiesgo","modalidad","codRamo","codSector","tipCoa","tipRea",
				"pctCoa","tipoMov","tipoOper","valTotalReservaMes","pagoAnual","importeValoracion",
				"importePago","fecMovEco","codMonPago","cohorte","fecEfectoIni","fecEfectoSpto",
				"fecSuscripcion","codLobBis","pctDesglose","sistema","claveIncCorr","fechaEstado","mcaVigente"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujosReales detalle = null;
		int cont=1;
		List<FlujosReales> listaNfq = new ArrayList<FlujosReales>();
		while ((detalle = (FlujosReales) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujosReales valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getCodCia(), rowNfq, index++);
			writeCell(valor.getNumExp(), rowNfq, index++);
			writeCell(valor.getAnioMes(), rowNfq, index++);
			writeCell(valor.getNumMvto(), rowNfq, index++);
			writeCell(valor.getFillerMvto(), rowNfq, index++);
			writeCell(valor.getCodCob(), rowNfq, index++);
			writeCell(valor.getCodCtoRva(), rowNfq, index++);
			writeCell(valor.getCodProductor(), rowNfq, index++);
			writeCell(valor.getClaseProductor(), rowNfq, index++);
			writeCell(valor.getClaseMediador(), rowNfq, index++);
			writeCell(valor.getTipDocum(), rowNfq, index++);
			writeCell(valor.getCodDocum(), rowNfq, index++);
			writeCell(valor.getOfDirecta(), rowNfq, index++);
			writeCell(valor.getCodTerceroAgt(), rowNfq, index++);
			writeCell(valor.getFecMovExp(), rowNfq, index++);
			writeCell(valor.getTipExp(), rowNfq, index++);
			writeCell(valor.getCodTipcGrpTipExp(), rowNfq, index++);
			writeCell(valor.getFecOcurSini(), rowNfq, index++);
			writeCell(valor.getFecApertExp(), rowNfq, index++);
			writeCell(valor.getFecReapertExp(), rowNfq, index++);
			writeCell(valor.getFecTermExp(), rowNfq, index++);
			writeCell(valor.getCodMon(), rowNfq, index++);
			writeCell(valor.getClaseExp(), rowNfq, index++);
			writeCell(valor.getNumPoliza(), rowNfq, index++);
			writeCell(valor.getNumApli(), rowNfq, index++);
			writeCell(valor.getNumRiesgo(), rowNfq, index++);
			writeCell(valor.getModalidad(), rowNfq, index++);
			writeCell(valor.getCodRamo(), rowNfq, index++);
			writeCell(valor.getCodSector(), rowNfq, index++);
			writeCell(valor.getTipCoa(), rowNfq, index++);
			writeCell(valor.getTipRea(), rowNfq, index++);
			writeCell(valor.getPctCoa(), rowNfq, index++);
			writeCell(valor.getTipoMov(), rowNfq, index++);
			writeCell(valor.getTipoOper(), rowNfq, index++);
			writeCell(valor.getValTotalReservaMes(), rowNfq, index++);
			writeCell(valor.getPagoAnual(), rowNfq, index++);
			writeCell(valor.getImporteValoracion(), rowNfq, index++);
			writeCell(valor.getImportePago(), rowNfq, index++);
			writeCell(valor.getFecMovEco(), rowNfq, index++);
			writeCell(valor.getCodMonPago(), rowNfq, index++);
			writeCell(valor.getCohorte(), rowNfq, index++);
			writeCell(valor.getFecEfectoIni(), rowNfq, index++);
			writeCell(valor.getFecEfectoSpto(), rowNfq, index++);
			writeCell(valor.getFecSuscripcion(), rowNfq, index++);
			writeCell(valor.getCodLobBis(), rowNfq, index++);
			writeCell(valor.getPctDesglose(), rowNfq, index++);
			writeCell(valor.getSistema(), rowNfq, index++);
			writeCell(valor.getClaveIncCorr(), rowNfq, index++);
			writeCell(valor.getFechaEstado(), rowNfq, index++);
			writeCell(valor.getMcaVigente(), rowNfq, index++);
		}
		
	}


	private static void transformarPESOS(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","cnegocio","ccanal","ccartera","fcierre","kmodalidad","kpoliza",
				"ksubpoliza","kcertificado","kmodalidadOrig","kpolizaOrig",
				"ksubpolizaOrig","kcertificadoOrig","cohorte","swcasado","totFactActT","pesosUoA"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PesosBt detalle = null;
		int cont=1;
		List<PesosBt> listaNfq = new ArrayList<PesosBt>();
		while ((detalle = (PesosBt) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PesosBt valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getKmodalidadOrig(), rowNfq, index++);
			writeCell(valor.getKpolizaOrig().toString(), rowNfq, index++);
			writeCell(valor.getKsubpolizaOrig(), rowNfq, index++);
			writeCell(valor.getKcertificadoOrig(), rowNfq, index++);
			writeCell(valor.getCohorte(), rowNfq, index++);
			writeCell(valor.getSwcasado(), rowNfq, index++);
			writeCell(valor.getTotFactActT(), rowNfq, index++);
			writeCell(valor.getPesosUoA(), rowNfq, index++);
		}
		
	}


	private static void transformarFLUJOSTN17(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio","ccanal","ccartera","fcierre","bt","uoa","kcontrato","kramo","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion","kajuste",
				"ctipoaport","intfeccal","fsuscri","kcarterainv","gapAct","kmodext","spcom",
				"kbencon","totfpvida","totfpnavida","totfactvida","totcolavida","totfpfall","totfpnafall","totfactfall",
				"totcolafall","totfpcompl","totfpnacompl","totfactcompl","totcolacompl","totfpgto","totfpnagto","totfactgto",
				"totcolagto","totfpcom","totfpnacom","totfactcom","totcolacom","totfprte","totfpnarte","totfactrte","totcolarte",
				"totfpprim","totfpnaprim","totfactprim","totcolaprim","totfprob","totfprobtanul","totprovision","totcola",
				"provfcal","totra","totcsm","totcsmpatron"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujosTN17 detalle = null;
		int cont=1;
		List<FlujosTN17> listaNfq = new ArrayList<FlujosTN17>();
		while ((detalle = (FlujosTN17) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujosTN17 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getUoa(), rowNfq, index++);
			writeCell(valor.getKcontrato(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getNorden(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestacion(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getCtipoaport(), rowNfq, index++);
			writeCell(valor.getIntfeccal(), rowNfq, index++);
			writeCell(valor.getFsuscri(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getTotfpvida(), rowNfq, index++);
			writeCell(valor.getTotfpnavida(), rowNfq, index++);
			writeCell(valor.getTotfactvida(), rowNfq, index++);
			writeCell(valor.getTotcolavida(), rowNfq, index++);
			writeCell(valor.getTotfpfall(), rowNfq, index++);
			writeCell(valor.getTotfpnafall(), rowNfq, index++);
			writeCell(valor.getTotfactfall(), rowNfq, index++);
			writeCell(valor.getTotcolafall(), rowNfq, index++);
			writeCell(valor.getTotfpcompl(), rowNfq, index++);
			writeCell(valor.getTotfpnacompl(), rowNfq, index++);
			writeCell(valor.getTotfactcompl(), rowNfq, index++);
			writeCell(valor.getTotcolacompl(), rowNfq, index++);
			writeCell(valor.getTotfpgto(), rowNfq, index++);
			writeCell(valor.getTotfpnagto(), rowNfq, index++);
			writeCell(valor.getTotfactgto(), rowNfq, index++);
			writeCell(valor.getTotcolagto(), rowNfq, index++);
			writeCell(valor.getTotfpcom(), rowNfq, index++);
			writeCell(valor.getTotfpnacom(), rowNfq, index++);
			writeCell(valor.getTotfactcom(), rowNfq, index++);
			writeCell(valor.getTotcolacom(), rowNfq, index++);
			writeCell(valor.getTotfprte(), rowNfq, index++);
			writeCell(valor.getTotfpnarte(), rowNfq, index++);
			writeCell(valor.getTotfactrte(), rowNfq, index++);
			writeCell(valor.getTotcolarte(), rowNfq, index++);
			writeCell(valor.getTotfpprim(), rowNfq, index++);
			writeCell(valor.getTotcolaprim(), rowNfq, index++);
			writeCell(valor.getTotfprob(), rowNfq, index++);
			writeCell(valor.getTotfprobtanul(), rowNfq, index++);
			writeCell(valor.getTotprovision(), rowNfq, index++);
			writeCell(valor.getTotcola(), rowNfq, index++);
			writeCell(valor.getProvfcal(), rowNfq, index++);
			writeCell(valor.getTotra(), rowNfq, index++);
			writeCell(valor.getTotcsm(), rowNfq, index++);
			writeCell(valor.getTotcsmpatron(), rowNfq, index++);
		}
	}

	private static void transformarFLUJINF3(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","fcierre","cnegocio","ccanal","uoa","kcarteraContrato","kcarteraCohort","kcarteraOner",
				"fdesde","totFpVida","totFpFall","totFpGastos","totFpComisiones","totFpRescates","totFpCompl",
				"totFpPrimas"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujInf3 detalle = null;
		int cont=1;
		List<FlujInf3> listaNfq = new ArrayList<FlujInf3>();
		while ((detalle = (FlujInf3) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujInf3 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getNegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getUoa(), rowNfq, index++);
			writeCell(valor.getKcarteraContrato(), rowNfq, index++);
			writeCell(valor.getKcarteraCohort(), rowNfq, index++);
			writeCell(valor.getKcarteraOner(), rowNfq, index++);
			writeCell(valor.getFdesde(), rowNfq, index++);
			writeCell(valor.getTotFpVida(), rowNfq, index++);
			writeCell(valor.getTotFpFall(), rowNfq, index++);
			writeCell(valor.getTotFpGastos(), rowNfq, index++);
			writeCell(valor.getTotFpComisiones(), rowNfq, index++);
			writeCell(valor.getTotFpRescates(), rowNfq, index++);
			writeCell(valor.getTotFpCompl(), rowNfq, index++);
			writeCell(valor.getTotFpPrimas(), rowNfq, index++);
		}
	}


	private static void transformarFLUJOSTOTP(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio","ccanal","ccartera","fcierre","bt","kramo","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion","kajuste","csitupol",
				"ctipoaport","intfeccal","fsuscri","pcoaseg","pgastgesex1i","kcarterainv","gapAct","kmodext","spcom",
				"kbencon","totfpvida","totfpnavida","totfactvida","totcolavida","totfpfall","totfpnafall","totfactfall",
				"totcolafall","totfpcompl","totfpnacompl","totfactcompl","totcolacompl","totfpgto","totfpnagto","totfactgto",
				"totcolagto","totfpcom","totfpnacom","totfactcom","totcolacom","totfprte","totfpnarte","totfactrte","totcolarte",
				"totfpprim","totfpnaprim","totfactprim","totcolaprim","totfprob","totfprobtanul","totprovision","totcola",
				"provbtifcal","iprimanetaini","desviacion"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujosTotP detalle = null;
		int cont=1;
		List<FlujosTotP> listaNfq = new ArrayList<FlujosTotP>();
		while ((detalle = (FlujosTotP) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujosTotP valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getNorden(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestacion(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getCsitupol(), rowNfq, index++);
			writeCell(valor.getCtipoaport(), rowNfq, index++);
			writeCell(valor.getIntfeccal(), rowNfq, index++);
			writeCell(valor.getFsuscri(), rowNfq, index++);
			writeCell(valor.getPcoaseg(), rowNfq, index++);
			writeCell(valor.getPgastgesex1i(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getTotfpvida(), rowNfq, index++);
			writeCell(valor.getTotfpnavida(), rowNfq, index++);
			writeCell(valor.getTotfactvida(), rowNfq, index++);
			writeCell(valor.getTotcolavida(), rowNfq, index++);
			writeCell(valor.getTotfpfall(), rowNfq, index++);
			writeCell(valor.getTotfpnafall(), rowNfq, index++);
			writeCell(valor.getTotfactfall(), rowNfq, index++);
			writeCell(valor.getTotcolafall(), rowNfq, index++);
			writeCell(valor.getTotfpcompl(), rowNfq, index++);
			writeCell(valor.getTotfpnacompl(), rowNfq, index++);
			writeCell(valor.getTotfactcompl(), rowNfq, index++);
			writeCell(valor.getTotcolacompl(), rowNfq, index++);
			writeCell(valor.getTotfpgto(), rowNfq, index++);
			writeCell(valor.getTotfpnagto(), rowNfq, index++);
			writeCell(valor.getTotfactgto(), rowNfq, index++);
			writeCell(valor.getTotcolagto(), rowNfq, index++);
			writeCell(valor.getTotfpcom(), rowNfq, index++);
			writeCell(valor.getTotfpnacom(), rowNfq, index++);
			writeCell(valor.getTotfactcom(), rowNfq, index++);
			writeCell(valor.getTotcolacom(), rowNfq, index++);
			writeCell(valor.getTotfprte(), rowNfq, index++);
			writeCell(valor.getTotfpnarte(), rowNfq, index++);
			writeCell(valor.getTotfactrte(), rowNfq, index++);
			writeCell(valor.getTotcolarte(), rowNfq, index++);
			writeCell(valor.getTotfpprim(), rowNfq, index++);
			writeCell(valor.getTotcolaprim(), rowNfq, index++);
			writeCell(valor.getTotfprob(), rowNfq, index++);
			writeCell(valor.getTotfprobtanul(), rowNfq, index++);
			writeCell(valor.getTotprovision(), rowNfq, index++);
			writeCell(valor.getTotcola(), rowNfq, index++);
			writeCell(valor.getProvbtifcal(), rowNfq, index++);
			writeCell(valor.getIprimanetaini(), rowNfq, index++);
			writeCell(valor.getDesviacion(), rowNfq, index++);
		}
	}


	private static void transformarCTU0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kUOA","kFecCierreCurv","kCarInv17","kCurva","swActivo","usuarioAlta",
				"fechaAlta","usuarioMod","fechaMod"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		AsigCurvasTipoUOA detalle = null;
		int cont=1;
		List<AsigCurvasTipoUOA> listaNfq = new ArrayList<AsigCurvasTipoUOA>();
		while ((detalle = (AsigCurvasTipoUOA) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			AsigCurvasTipoUOA valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getkUOA(), rowNfq, index++);
			writeCell(valor.getkFecCierreCurv(), rowNfq, index++);
			writeCell(valor.getkCarInv17(), rowNfq, index++);
			writeCell(valor.getkCurva(), rowNfq, index++);
			writeCell(valor.getSwActivo(), rowNfq, index++);
			writeCell(valor.getUsuarioAlta(), rowNfq, index++);
			writeCell(valor.getFechaAlta(), rowNfq, index++);
			writeCell(valor.getUsuarioMod(), rowNfq, index++);
			writeCell(valor.getFechaMod(), rowNfq, index++);
		}
		
	}

	private static void transformarTABLA2000(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"codigo","valor"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		Tabla2000 detalle = null;
		int cont=1;
		List<Tabla2000> listaNfq = new ArrayList<Tabla2000>();
		while ((detalle = (Tabla2000) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			Tabla2000 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;
			} else {
				datoStyleNfq = datoStyleImparNfq;
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getCodigo(), rowNfq, index++);
			writeCell(valor.getValor(), rowNfq, index++);
		}
	}

	private static void transformarCOM0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kCarteOrig","kModalidad","kGarantia","indicador","faltaDes","faltaHas",
				"kFvigDes","kFvigHas","baseCalcComi1","baseCalcComi2","baseCalcComi3","mesPagoPagComi","nPeriComi1",
				"pComisiona1","nPeriComi2","pComisiona2","nPeriComi3","pComisiona3"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ComisionesParticipadasCOM detalle = null;
		int cont=1;
		List<ComisionesParticipadasCOM> listaNfq = new ArrayList<ComisionesParticipadasCOM>();
		while ((detalle = (ComisionesParticipadasCOM) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ComisionesParticipadasCOM valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getkCarteOrig(), rowNfq, index++);
			writeCell(valor.getkModalidad(), rowNfq, index++);
			writeCell(valor.getkGarantia(), rowNfq, index++);
			writeCell(valor.getIndicador(), rowNfq, index++);
			writeCell(valor.getFaltaDes(), rowNfq, index++);
			writeCell(valor.getFaltaHas(), rowNfq, index++);
			writeCell(valor.getkFvigDes(), rowNfq, index++);
			writeCell(valor.getkFvigHas(), rowNfq, index++);
			writeCell(valor.getBaseCalcComi1(), rowNfq, index++);
			writeCell(valor.getBaseCalcComi2(), rowNfq, index++);
			writeCell(valor.getBaseCalcComi3(), rowNfq, index++);
			writeCell(valor.getMesPagoPagComi(), rowNfq, index++);
			writeCell(valor.getnPeriComi1(), rowNfq, index++);
			writeCell(valor.getpComisiona1(), rowNfq, index++);
			writeCell(valor.getnPeriComi2(), rowNfq, index++);
			writeCell(valor.getpComisiona2(), rowNfq, index++);
			writeCell(valor.getnPeriComi3(), rowNfq, index++);
			writeCell(valor.getpComisiona3(), rowNfq, index++);
		}
	}

	private static void transformarSCR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"feccierre","bt","variable","valor"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ValoresEstres detalle = null;
		int cont=1;
		List<ValoresEstres> listaNfq = new ArrayList<ValoresEstres>();
		while ((detalle = (ValoresEstres) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ValoresEstres valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getVariable(), rowNfq, index++);
			writeCell(valor.getValor(), rowNfq, index++);
		}
	}

	private static void transformarSCRVM(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","ccanal","kmodalidad","c"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FactoresVolatilidad detalle = null;
		int cont=1;
		List<FactoresVolatilidad> listaNfq = new ArrayList<FactoresVolatilidad>();
		while ((detalle = (FactoresVolatilidad) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FactoresVolatilidad valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getC(), rowNfq, index++);
		}
	}

	private static void transformarLMI0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"feccierre","ksexo","kedadfija","pestres_1","pestres_2",
				"pestres_3","pestres_4","pestres_5","pestres_6","pestres_7","pestres_8",
				"pestres_9","pestres_10","pestres_11","pestres_12","pestres_13","pestres_14",
				"pestres_15","pestres_16","pestres_17","pestres_18","pestres_19","pestres_20",
				"pestres_21","pestres_22","pestres_23","pestres_24","pestres_25","pestres_26",
				"pestres_27","pestres_28","pestres_29","pestres_30","pestres_31","pestres_32",
				"pestres_33","pestres_34","pestres_35","pestres_36","pestres_37","pestres_38",
				"pestres_39","pestres_40","pestres_41","pestres_42","pestres_43","pestres_44",
				"pestres_45","pestres_46","pestres_47","pestres_48","pestres_49","pestres_50",
				"pestres_51","pestres_52","pestres_53","pestres_54","pestres_55","pestres_56",
				"pestres_57","pestres_58","pestres_59","pestres_60","pestres_61","pestres_62",
				"pestres_63","pestres_64","pestres_65","pestres_66","pestres_67","pestres_68",
				"pestres_69","pestres_70","pestres_71","pestres_72","pestres_73","pestres_74",
				"pestres_75","pestres_76","pestres_77","pestres_78","pestres_79","pestres_80",
				"pestres_81","pestres_82","pestres_83","pestres_84","pestres_85","pestres_86",
				"pestres_87","pestres_88","pestres_89","pestres_90","pestres_91","pestres_92",
				"pestres_93","pestres_94","pestres_95","pestres_96","pestres_97","pestres_98",
				"pestres_99"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		LongevidadModInterno detalle = null;
		int cont=1;
		List<LongevidadModInterno> listaNfq = new ArrayList<LongevidadModInterno>();
		while ((detalle = (LongevidadModInterno) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			LongevidadModInterno valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getKsexo(), rowNfq, index++);
			writeCell(valor.getKedadfija(), rowNfq, index++);
			for (int a=0;a<valor.getPestres().size();a++)
				writeCell(valor.getPestres().get(a), rowNfq, index++);
		}
	}

	private static void transformarPTIPO(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"origen","cnegocio","ccanal","ccartera","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion",
				"kajuste","ctipoaport"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PolizasTipo detalle = null;
		int cont=1;
		List<PolizasTipo> listaNfq = new ArrayList<PolizasTipo>();
		while ((detalle = (PolizasTipo) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PolizasTipo valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getOrigen(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKcertificado(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNsuscri(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNorden(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKgarantia(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKprestacion(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKajuste(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getCtipoaport(), rowNfq, index++);
		}
	}

	private static void transformarINCIDENCIAS(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "ccartera", "fecCierre","bt","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion",
				"kajuste","ctipoaport","generadorError","codigoRetorno","tipoError","textoError",
				"infAmpliada"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		Incidencia detalle = null;
		int cont=1;
		List<Incidencia> listaNfq = new ArrayList<Incidencia>();
		while ((detalle = (Incidencia) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			Incidencia valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFecCierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKcertificado(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNsuscri(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNorden(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKgarantia(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKprestacion(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKajuste(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getCtipoaport(), rowNfq, index++);
			writeCell(valor.getGeneradorError(), rowNfq, index++);
			writeCell(valor.getCodigoRetorno(), rowNfq, index++);
			writeCell(valor.getTipoError(), rowNfq, index++);
			writeCell(valor.getTextoError(), rowNfq, index++);
			writeCell(valor.getInfAmpliada(), rowNfq, index++);
		}
	}

	private static void transformarFLUJOSTOT(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "ccartera", "fecCierre","bt","kramo","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion",
				"kajuste","ctipoaport","intfeccal","fsuscri","kcarterainv","gapAct","kmodext","spcom","kbencon",
				"totfpvida","totfpnavida","totfactvida","totcolavida","totfpfall","totfpnafall",
				"totfactfall","totcolafall","totfpcompl","totfpnacompl","totfactcompl","totcolacompl",
				"totfpgto","totfpnagto","totfactgto","totcolagto","totfpcom","totfpnacom","totfactcom",
				"totcolacom","totfprte","totfpnarte","totfactrte","totcolarte","totfpprim","totfpnaprim",
				"totfactprim","totcolaprim","totfprob","totfprobtanul","totprovision","totcola","provbtifcal"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		TotalesFlujos detalle = null;
		int cont=1;
		List<TotalesFlujos> listaNfq = new ArrayList<TotalesFlujos>();
		while ((detalle = (TotalesFlujos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			TotalesFlujos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getNorden(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestacion(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getCtipoaport(), rowNfq, index++);
			writeCell(valor.getIntfeccal(), rowNfq, index++);
			writeCell(valor.getFsuscri(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getTotfpvida(), rowNfq, index++);
			writeCell(valor.getTotfpnavida(), rowNfq, index++);
			writeCell(valor.getTotfactvida(), rowNfq, index++);
			writeCell(valor.getTotcolavida(), rowNfq, index++);
			writeCell(valor.getTotfpfall(), rowNfq, index++);
			writeCell(valor.getTotfpnafall(), rowNfq, index++);
			writeCell(valor.getTotfactfall(), rowNfq, index++);
			writeCell(valor.getTotcolafall(), rowNfq, index++);
			writeCell(valor.getTotfpcompl(), rowNfq, index++);
			writeCell(valor.getTotfpnacompl(), rowNfq, index++);
			writeCell(valor.getTotfactcompl(), rowNfq, index++);
			writeCell(valor.getTotcolacompl(), rowNfq, index++);
			writeCell(valor.getTotfpgto(), rowNfq, index++);
			writeCell(valor.getTotfpnagto(), rowNfq, index++);
			writeCell(valor.getTotfactgto(), rowNfq, index++);
			writeCell(valor.getTotcolagto(), rowNfq, index++);
			writeCell(valor.getTotfpcom(), rowNfq, index++);
			writeCell(valor.getTotfpnacom(), rowNfq, index++);
			writeCell(valor.getTotfactcom(), rowNfq, index++);
			writeCell(valor.getTotcolacom(), rowNfq, index++);
			writeCell(valor.getTotfprte(), rowNfq, index++);
			writeCell(valor.getTotfpnarte(), rowNfq, index++);
			writeCell(valor.getTotfactrte(), rowNfq, index++);
			writeCell(valor.getTotcolarte(), rowNfq, index++);
			writeCell(valor.getTotfpprim(), rowNfq, index++);
			writeCell(valor.getTotfpnaprim(), rowNfq, index++);
			writeCell(valor.getTotfactprim(), rowNfq, index++);
			writeCell(valor.getTotcolaprim(), rowNfq, index++);
			writeCell(valor.getTotfprob(), rowNfq, index++);
			writeCell(valor.getTotfprobtanul(), rowNfq, index++);
			writeCell(valor.getTotprovision(), rowNfq, index++);
			writeCell(valor.getTotcola(), rowNfq, index++);
			writeCell(valor.getProvbtifcal(), rowNfq, index++);
		}
		
	}

	private static void transformarMAESBTC(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "ccartera", "fecCierre","basetec","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion",
				"kajuste","ctipoaport","edadcalc1","edadcalc2","edadcalc3","edadcalc4","edadcalc5",
				"tablacalc1aseg1","tablacalc2aseg1","tablacalc3aseg1",
				"tablacalc1aseg2","tablacalc2aseg2","tablacalc3aseg2","tablacalc1aseg3","tablacalc2aseg3",
				"tablacalc3aseg3","tablacalc1aseg4","tablacalc2aseg4","tablacalc3aseg4","tablacalc1aseg5",
				"tablacalc2aseg5","tablacalc3aseg5","factor1","factor2(1)","factor2(2)","factor2(3)",
				"factor2(4)","factor2(5)","factor2(6)","factor2(7)","factor2(8)","factor2(9)","factor2(10)",
				"fitrossp","itcalc(1)","itcalc(2)","itcalc(3)","itcalc(4)","itcalc(5)","fcurvaTi",
				"curvaTi","perTransRossp","metodoPtRossp","ftablaAn","tablaTanul","gtorosspPrima","gtorosspCap",
				"gtorosspProv","gtoUni","gtoprov","factorInterpolExperienciaRossp","metodoPtBel","perTransBel",
				"factorInterpolInteresesBel","tablaBaseExp(1)","tablaBaseExp(2)","tablaBaseExp(3)",
				"tablaBaseExp(4)","tablaBaseExp(5)","factor1_2","factor2_2(1)","factor2_2(2)","factor2_2(3)",
				"factor2_2(4)","factor2_2(5)","factor2_2(6)","factor2_2(7)","factor2_2(8)","factor2_2(9)",
				"factor2_2(10)","factor1_3","factor2_3(1)","factor2_3(2)","factor2_3(3)","factor2_3(4)",
				"factor2_3(5)","factor2_3(6)","factor2_3(7)","factor2_3(8)","factor2_3(9)","factor2_3(10)",
				"factor1_4","factor2_4(1)","factor2_4(2)","factor2_4(3)","factor2_4(4)","factor2_4(5)",
				"factor2_4(6)","factor2_4(7)","factor2_4(8)","factor2_4(9)","factor2_4(10)","factor1_5",
				"factor2_5(1)","factor2_5(2)","factor2_5(3)","factor2_5(4)","factor2_5(5)","factor2_5(6)",
				"factor2_5(7)","factor2_5(8)","factor2_5(9)","factor2_5(10)"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DetalleBaseTecnica detalle = null;
		int cont=1;
		List<DetalleBaseTecnica> listaNfq = new ArrayList<DetalleBaseTecnica>();
		while ((detalle = (DetalleBaseTecnica) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DetalleBaseTecnica valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFecCierre(), rowNfq, index++);
			writeCell(valor.getBaseTec(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKcertificado(), rowNfq, index++);
			writeCell(valor.getUmicKey().getNsuscri(), rowNfq, index++);
			writeCell(valor.getUmicKey().getNorden(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKgarantia(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKprestacion(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKajuste(), rowNfq, index++);
			writeCell(valor.getUmicKey().getCtipoaport(), rowNfq, index++);
			for (int a = 0; a<5;a++) {
				if (null == valor.getEdadcalc().get(a))
					writeCell("", rowNfq, index++);
				else
					writeCell(valor.getEdadcalc().get(a), rowNfq, index++);
			}
			writeCell(valor.getTablacalc1aseg1(), rowNfq, index++);
			writeCell(valor.getTablacalc2aseg1(), rowNfq, index++);
			writeCell(valor.getTablacalc3aseg1(), rowNfq, index++);
			writeCell(valor.getTablacalc1aseg2(), rowNfq, index++);
			writeCell(valor.getTablacalc2aseg2(), rowNfq, index++);
			writeCell(valor.getTablacalc3aseg2(), rowNfq, index++);
			writeCell(valor.getTablacalc1aseg3(), rowNfq, index++);
			writeCell(valor.getTablacalc2aseg3(), rowNfq, index++);
			writeCell(valor.getTablacalc3aseg3(), rowNfq, index++);
			writeCell(valor.getTablacalc1aseg4(), rowNfq, index++);
			writeCell(valor.getTablacalc2aseg4(), rowNfq, index++);
			writeCell(valor.getTablacalc3aseg4(), rowNfq, index++);
			writeCell(valor.getTablacalc1aseg5(), rowNfq, index++);
			writeCell(valor.getTablacalc2aseg5(), rowNfq, index++);
			writeCell(valor.getTablacalc3aseg5(), rowNfq, index++);
			writeCell(valor.getFactor1(), rowNfq, index++);
			for (int a = 0; a<10;a++) {
				if (null == valor.getFactor2().get(a))
					writeCell("", rowNfq, index++);
				else 
					writeCell(valor.getFactor2().get(a), rowNfq, index++);
			}	
			writeCell(valor.getFitrossp(), rowNfq, index++);
			for (int a = 0; a<5;a++) {
				if (null == valor.getItcalc().get(a))
					writeCell("", rowNfq, index++);
				else
					writeCell(valor.getItcalc().get(a), rowNfq, index++);
			}
			writeCell(valor.getFcurvaTi(), rowNfq, index++);
			writeCell(valor.getCurvaTi(), rowNfq, index++);
			if (valor.getPerTransRossp()) writeCell("S", rowNfq, index++); else writeCell("N", rowNfq, index++);
			writeCell(valor.getMetodoPtRossp(), rowNfq, index++);
			writeCell(valor.getFtablaAn(), rowNfq, index++);
			writeCell(valor.getTablaTanul(), rowNfq, index++);
			writeCell(valor.getGtorosspPrima(), rowNfq, index++);
			writeCell(valor.getGtorosspCap(), rowNfq, index++);
			writeCell(valor.getGtorosspProv(), rowNfq, index++);
			writeCell(valor.getGtoUni(), rowNfq, index++);
			writeCell(valor.getGtoprov(), rowNfq, index++);
			writeCell(valor.getFactorInterpolExperienciaRossp(), rowNfq, index++);
			writeCell(valor.getMetodoPtBel(), rowNfq, index++);
			if (valor.getPerTransBel()) writeCell("S", rowNfq, index++); else writeCell("N", rowNfq, index++);
			writeCell(valor.getFactorInterpolInteresesBel(), rowNfq, index++);
			for (int a = 0; a<5;a++){
				if (null == valor.getTablaBaseExp().get(a)){
					writeCell("", rowNfq, index++);
				} else {
					writeCell(valor.getTablaBaseExp().get(a), rowNfq, index++);
				}
			}
			writeCell(valor.getFactor1_2(), rowNfq, index++);
			for (int a = 0; a<10;a++) {
				if (null == valor.getFactor2_2().get(a)){
					writeCell("", rowNfq, index++);
				} else {
					writeCell(valor.getFactor2_2().get(a), rowNfq, index++);
				}
			}
			writeCell(valor.getFactor1_3(), rowNfq, index++);
			for (int a = 0; a<10;a++) {
				if (null == valor.getFactor2_3().get(a)){
					writeCell("", rowNfq, index++);
				} else {
					writeCell(valor.getFactor2_3().get(a), rowNfq, index++);
				}
			}
			writeCell(valor.getFactor1_4(), rowNfq, index++);
			for (int a = 0; a<10;a++) {
				if (null == valor.getFactor2_4().get(a)) {
					writeCell("", rowNfq, index++);
				} else {
					writeCell(valor.getFactor2_4().get(a), rowNfq, index++);
				}
			}
			writeCell(valor.getFactor1_5(), rowNfq, index++);
			for (int a = 0; a<10;a++) {
				if (null == valor.getFactor2_5().get(a)){
					writeCell("", rowNfq, index++);
				} else {
					writeCell(valor.getFactor2_5().get(a), rowNfq, index++);
				}
			}
		}
	}

	private static void transformarFLUJOSDET(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "ccartera", "fcierre","bt","kramo","kmodalidad",
				"kpoliza","ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion",
				"kajuste","ctipoaport","intfeccal","fsuscri","kcarterainv","gapAct","kmodext","spcom","kbencon","uoa","kcontrato",
				"fechaDesde","fechaHasta",
				"fechaDevengoVida","fechaPagoVida","impFlujoNominalVida","fpbProbableVida","impFlujoProbableVida",
				"fpbAtcVida","impFlujoNoAnuladoVida","fpbAtcfinVida","impFlujoActualizadoVida","impProviVida",
				"fechaDevengoFall","fechaPagoFall","impFlujoNominalFall","fpbProbableFall","impFlujoProbableFall",
				"fpbAtcFall","impFlujoNoAnuladoFall","fpbAtcfinFall","impFlujoActualizadoFall","impProviFall",
				"fechaDevengoCompl","fechaPagoCompl","impFlujoNominalCompl","fpbProbableCompl","impFlujoProbableCompl",
				"fpbAtcCompl","impFlujoNoAnuladoCompl","fpbAtcfinCompl","impFlujoActualizadoCompl","impProviCompl",
				"fechaDevengoGto","fechaPagoGto","impFlujoNominalGto","fpbProbableGto","impFlujoProbableGto","fpbAtcGto",
				"impFlujoNoAnuladoGto","fpbAtcfinGto","impFlujoActualizadoGto","impProviGto","fechaDevengoComi",
				"fechaPagoComi","impFlujoNominalComi","fpbProbableComi","impFlujoProbableComi","fpbAtcComi",
				"impFlujoNoAnuladoComi","fpbAtcfinComi","impFlujoActualizadoComi","impProviComi","fechaDevengoRte",
				"fechaPagoRte","impFlujoNominalRte","fpbProbableRte","impFlujoProbableRte","fpbAtcRte",
				"impFlujoNoAnuladoRte","fpbAtcfinRte","impFlujoActualizadoRte","impProviRte","fechaDevengoPrim",
				"fechaPagoPrim","impFlujoNominalPrim","fpbProbablePrim","impFlujoProbablePrim","fpbAtcPrim",
				"impFlujoNoAnuladoPrim","fpbAtcfinPrim","impFlujoActualizadoPrim","impProviPrim","sumfprob","sumfprobtanul",
				"sumprovision","sumcola","provbtiproy","terminalAnterior","terminalPosterior","raumic","csmumic",
				"patronCSM"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DetalleCorriente detalle = null;
		int cont=1;
		List<DetalleCorriente> listaNfq = new ArrayList<DetalleCorriente>();
		while ((detalle = (DetalleCorriente) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DetalleCorriente valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getNorden(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestacion(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getCtipoaport(), rowNfq, index++);
			writeCell(valor.getIntfeccal(), rowNfq, index++);
			writeCell(valor.getFsuscri(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getUoa(), rowNfq, index++);
			writeCell(valor.getKcontrato(), rowNfq, index++);
			writeCell(valor.getFechaDesde(), rowNfq, index++);
			writeCell(valor.getFechaHasta(), rowNfq, index++);
			BloqueCorriente bloque = valor.getBloqueVida();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloqueFall();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloqueCompl();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloqueGto();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloqueComi();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloqueRte();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			bloque = valor.getBloquePrim();
			writeCell(bloque.getFechaDevengo(), rowNfq, index++);
			writeCell(bloque.getFechaPago(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNominal(), rowNfq, index++);
			writeCell(bloque.getFpbProbable(), rowNfq, index++);
			writeCell(bloque.getImpFlujoProbable(), rowNfq, index++);
			writeCell(bloque.getFpbAtc(), rowNfq, index++);
			writeCell(bloque.getImpFlujoNoAnulado(), rowNfq, index++);
			writeCell(bloque.getFpbAtcfin(), rowNfq, index++);
			writeCell(bloque.getImpFlujoActualizado(), rowNfq, index++);
			writeCell(bloque.getImpProvi(), rowNfq, index++);
			TotalFlujoProyeccion total = valor.getTotalFlujoProyeccion();
			writeCell(total.getSumfprob(), rowNfq, index++);
			writeCell(total.getSumfprobtanul(), rowNfq, index++);
			writeCell(total.getSumprovision(), rowNfq, index++);
			writeCell(total.getSumcola(), rowNfq, index++);
			writeCell(total.getProvbtiproy(), rowNfq, index++);
			writeCell(total.getTerminalAnterior(), rowNfq, index++);
			writeCell(total.getTerminalPosterior(), rowNfq, index++);
			writeCell(valor.getRaumic(), rowNfq, index++);
			writeCell(valor.getCsmumic(), rowNfq, index++);
			writeCell(valor.getPatronCSM(), rowNfq, index++);
		}
		
	}

	private static void transformarCONTAB(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"ccanal","kpoliza","ksubpoliza","kramo","kmodalidad",
				"prv","fefecini","fefecfin","feccierre","cnegocio","koficont","ctipoprovi"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		Contabilidad detalle = null;
		int cont=1;
		List<Contabilidad> listaNfq = new ArrayList<Contabilidad>();
		while ((detalle = (Contabilidad) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			Contabilidad valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);
			writeCell(valor.getFefecini(), rowNfq, index++);
			writeCell(valor.getFefecfin(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getKoficont(), rowNfq, index++);
			writeCell(valor.getctipoprovi(), rowNfq, index++);
		}
	}

	private static void transformarFLUJTCAS(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","fecCierre","cnegocio", "ccanal", "kcarterainv","gapact",
				"gestionit","kramo","kmodalidad","kpoliza","ksubpoliza","nsuscri","fsuscri",
				"finit","it2","itdgs","fecdesde","impflujprob","impflujact"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujTcas detalle = null;
		int cont=1;
		List<FlujTcas> listaNfq = new ArrayList<FlujTcas>();
		while ((detalle = (FlujTcas) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujTcas valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getFsuscri(), rowNfq, index++);
			writeCell(valor.getFinit(), rowNfq, index++);
			writeCell(valor.getIt2(), rowNfq, index++);
			writeCell(valor.getItdgs(), rowNfq, index++);
			writeCell(valor.getFecdesde(), rowNfq, index++);
			writeCell(valor.getImpflujprob(), rowNfq, index++);
			writeCell(valor.getImpflujact(), rowNfq, index++);
		}
	}

	private static void transformarPRVFPB(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio", "ccanal", "kcartera","gap",
				"kmodalidad","intbti","prv","prvpb"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvFpb detalle = null;
		int cont=1;
		List<PrvFpb> listaNfq = new ArrayList<PrvFpb>();
		while ((detalle = (PrvFpb) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvFpb valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcartera(), rowNfq, index++);
			writeCell(valor.getGap(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getIntbti(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);
			writeCell(valor.getPrvpb(), rowNfq, index++);
		}
	}

	private static void transformarBASETEC(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","fcierre","cnegocio", "ccanal", "kramo","kmodalidad",
				"kgarantia","pintertecn1min","pintertecn1max","pintertecn1med","pintertecn2min",
				"pintertecn2max","pintertecn2med","tablarepre1","porcprovrepre1","tablarepre2",
				"porcprovrepre2","tablarepre3","porcprovrepre3","pgastgesin1","pgastgesin2",
				"pgastgesex","curvati1","porcprovcurvati1","curvati2","porcprovcurvati2",
				"curvati3","porcprovcurvati3","factor1","indfactor2","tablatanul1","porcprovtanul1",
				"tablatanul2","porcprovtanul2","tablatanul3","porcprovtanul3","gastrealunitario",
				"gastreal","ipc"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		Basetec detalle = null;
		int cont=1;
		List<Basetec> listaNfq = new ArrayList<Basetec>();
		while ((detalle = (Basetec) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			Basetec valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getPintertecn1min(), rowNfq, index++);
			writeCell(valor.getPintertecn1max(), rowNfq, index++);
			writeCell(valor.getPintertecn1med(), rowNfq, index++);
			writeCell(valor.getPintertecn2min(), rowNfq, index++);
			writeCell(valor.getPintertecn2max(), rowNfq, index++);
			writeCell(valor.getPintertecn2med(), rowNfq, index++);
			writeCell(valor.getTablarepre1(), rowNfq, index++);
			writeCell(valor.getPorcprovrepre1(), rowNfq, index++);
			writeCell(valor.getTablarepre2(), rowNfq, index++);
			writeCell(valor.getPorcprovrepre2(), rowNfq, index++);
			writeCell(valor.getTablarepre3(), rowNfq, index++);
			writeCell(valor.getPorcprovrepre3(), rowNfq, index++);
			writeCell(valor.getPgastgesin1(), rowNfq, index++);
			writeCell(valor.getPgastgesin2(), rowNfq, index++);
			writeCell(valor.getPgastgesex(), rowNfq, index++);
			writeCell(valor.getCurvati1(), rowNfq, index++);
			writeCell(valor.getPorcprovcurvati1(), rowNfq, index++);
			writeCell(valor.getCurvati2(), rowNfq, index++);
			writeCell(valor.getPorcprovcurvati2(), rowNfq, index++);
			writeCell(valor.getCurvati3(), rowNfq, index++);
			writeCell(valor.getPorcprovcurvati3(), rowNfq, index++);
			writeCell(valor.getFactor1(), rowNfq, index++);
			writeCell(valor.getIndfactor2(), rowNfq, index++);
			writeCell(valor.getTablatanul1(), rowNfq, index++);
			writeCell(valor.getPorcprovtanul1(), rowNfq, index++);
			writeCell(valor.getTablatanul2(), rowNfq, index++);
			writeCell(valor.getPorcprovtanul2(), rowNfq, index++);
			writeCell(valor.getTablatanul3(), rowNfq, index++);
			writeCell(valor.getPorcprovtanul3(), rowNfq, index++);
			writeCell(valor.getGastrealunitario(), rowNfq, index++);
			writeCell(valor.getGastreal(), rowNfq, index++);
			writeCell(valor.getIpc(), rowNfq, index++);
		}
	}

	private static void transformarFLUJCOASEG(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio", "ccanal", "kramo","kmodalidad",
				"segmento1","tiposubriesgo","kcarterainv","gapact","gestionit","kpoliza",
				"ksubpoliza","nsuscri","finisusc","fecfintramo1","pintertecn1","durtrcasado",
				"pintertecn2","tabla1","pgastgesin1","pgastgesin2","factor1","gastrealunitario",
				"gastreal","ipc","fecdesde","nveces","totflujoprobsingastos","totflujoprobdegastos",
				"totflujonomdegastos","kcuadro","kcoase1","kcoase2","kcoase3","kcoase4","kcoase5",
				"kcoase6","kcoase7","kcoase8","kcoase9","kcoase10","kcoase11","kcoase12","kcoase13",
				"kcoase14","kcoase15","kcoase16","kcoase17","kcoase18","kcoase19","kcoase20"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujCoaSeg detalle = null;
		int cont=1;
		List<FlujCoaSeg> listaNfq = new ArrayList<FlujCoaSeg>();
		while ((detalle = (FlujCoaSeg) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujCoaSeg valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getSegmento1(), rowNfq, index++);
			writeCell(valor.getTiposubriesgo(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getFinisusc(), rowNfq, index++);
			writeCell(valor.getFecfintramo1(), rowNfq, index++);
			writeCell(valor.getPintertecn1(), rowNfq, index++);
			writeCell(valor.getDurtrcasado(), rowNfq, index++);
			writeCell(valor.getPintertecn2(), rowNfq, index++);
			writeCell(valor.getTabla1(), rowNfq, index++);
			writeCell(valor.getPgastgesin1(), rowNfq, index++);
			writeCell(valor.getPgastgesin2(), rowNfq, index++);
			writeCell(valor.getFactor1(), rowNfq, index++);
			writeCell(valor.getGastrealunitario(), rowNfq, index++);
			writeCell(valor.getGastreal(), rowNfq, index++);
			writeCell(valor.getIpc(), rowNfq, index++);
			writeCell(valor.getFecdesde(), rowNfq, index++);
			writeCell(valor.getNveces(), rowNfq, index++);
			writeCell(valor.getTotflujoprobsingastos(), rowNfq, index++);
			writeCell(valor.getTotflujoprobdegastos(), rowNfq, index++);
			writeCell(valor.getTotflujonomdegastos(), rowNfq, index++);
			writeCell(valor.getKcuadro(), rowNfq, index++);
			writeCell(valor.getKcoase1(), rowNfq, index++);
			writeCell(valor.getKcoase2(), rowNfq, index++);
			writeCell(valor.getKcoase3(), rowNfq, index++);
			writeCell(valor.getKcoase4(), rowNfq, index++);
			writeCell(valor.getKcoase5(), rowNfq, index++);
			writeCell(valor.getKcoase6(), rowNfq, index++);
			writeCell(valor.getKcoase7(), rowNfq, index++);
			writeCell(valor.getKcoase8(), rowNfq, index++);
			writeCell(valor.getKcoase9(), rowNfq, index++);
			writeCell(valor.getKcoase10(), rowNfq, index++);
			writeCell(valor.getKcoase11(), rowNfq, index++);
			writeCell(valor.getKcoase12(), rowNfq, index++);
			writeCell(valor.getKcoase13(), rowNfq, index++);
			writeCell(valor.getKcoase14(), rowNfq, index++);
			writeCell(valor.getKcoase15(), rowNfq, index++);
			writeCell(valor.getKcoase16(), rowNfq, index++);
			writeCell(valor.getKcoase17(), rowNfq, index++);
			writeCell(valor.getKcoase18(), rowNfq, index++);
			writeCell(valor.getKcoase19(), rowNfq, index++);
			writeCell(valor.getKcoase20(), rowNfq, index++);
		}
	}
	

	private static void transformarPROVCOASEG(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio", "ccanal", "kramo","kmodalidad",
				"segmento1","tiposubriesgo","kpoliza","ksubpoliza","nsuscri","finisusc","fecfintramo1",
				"kcarterainv","gapact","gestionit","pcoase","distint","pintertecn1","durtrcasado",
				"pintertecn2","tabla1","pgastgesin1","pgastgesin2","factor1","gastrealunitario",
				"gastreal","ipc","curvati","totflujoactsingastos","totflujoactcongastos","kcuadro",
				"kcoase1","kcoase2","kcoase3","kcoase4","kcoase5","kcoase6","kcoase7","kcoase8",
				"kcoase9","kcoase10","kcoase11","kcoase12","kcoase13","kcoase14","kcoase15","kcoase16",
				"kcoase17","kcoase18","kcoase19","kcoase20"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ProvCoaSeg detalle = null;
		int cont=1;
		List<ProvCoaSeg> listaNfq = new ArrayList<ProvCoaSeg>();
		while ((detalle = (ProvCoaSeg) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ProvCoaSeg valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getSegmento1(), rowNfq, index++);
			writeCell(valor.getTiposubriesgo(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getFinisusc(), rowNfq, index++);
			writeCell(valor.getFecfintramo1(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getPcoase(), rowNfq, index++);
			writeCell(valor.getDistint(), rowNfq, index++);
			writeCell(valor.getPintertecn1(), rowNfq, index++);
			writeCell(valor.getDurtrcasado(), rowNfq, index++);
			writeCell(valor.getPintertecn2(), rowNfq, index++);
			writeCell(valor.getTabla1(), rowNfq, index++);
			writeCell(valor.getPgastgesin1(), rowNfq, index++);
			writeCell(valor.getPgastgesin2(), rowNfq, index++);
			writeCell(valor.getFactor1(), rowNfq, index++);
			writeCell(valor.getGastrealunitario(), rowNfq, index++);
			writeCell(valor.getGastreal(), rowNfq, index++);
			writeCell(valor.getIpc(), rowNfq, index++);
			writeCell(valor.getCurvati(), rowNfq, index++);
			writeCell(valor.getTotflujoactsingastos(), rowNfq, index++);
			writeCell(valor.getTotflujoactcongastos(), rowNfq, index++);
			writeCell(valor.getKcuadro(), rowNfq, index++);
			writeCell(valor.getKcoase1(), rowNfq, index++);
			writeCell(valor.getKcoase2(), rowNfq, index++);
			writeCell(valor.getKcoase3(), rowNfq, index++);
			writeCell(valor.getKcoase4(), rowNfq, index++);
			writeCell(valor.getKcoase5(), rowNfq, index++);
			writeCell(valor.getKcoase6(), rowNfq, index++);
			writeCell(valor.getKcoase7(), rowNfq, index++);
			writeCell(valor.getKcoase8(), rowNfq, index++);
			writeCell(valor.getKcoase9(), rowNfq, index++);
			writeCell(valor.getKcoase10(), rowNfq, index++);
			writeCell(valor.getKcoase11(), rowNfq, index++);
			writeCell(valor.getKcoase12(), rowNfq, index++);
			writeCell(valor.getKcoase13(), rowNfq, index++);
			writeCell(valor.getKcoase14(), rowNfq, index++);
			writeCell(valor.getKcoase15(), rowNfq, index++);
			writeCell(valor.getKcoase16(), rowNfq, index++);
			writeCell(valor.getKcoase17(), rowNfq, index++);
			writeCell(valor.getKcoase18(), rowNfq, index++);
			writeCell(valor.getKcoase19(), rowNfq, index++);
			writeCell(valor.getKcoase20(), rowNfq, index++);
		}
	}
	

	private static void transformarFLUJINF2(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kcarterainv","gapact","gestionit",
				"kramo","kmodalidad","kgarantia","kprestcal","kmodext","spcom","kbencon","nuevaproduc","segmento1","tiposubriesgo",
				"curvati","fecdesde","totflujprov","totprovi","totflujact"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujInf2 detalle = null;
		int cont=1;
		List<FlujInf2> listaNfq = new ArrayList<FlujInf2>();
		while ((detalle = (FlujInf2) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujInf2 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestcal(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getNuevaproduc(), rowNfq, index++);
			writeCell(valor.getSegmento1(), rowNfq, index++);
			writeCell(valor.getTiposubriesgo(), rowNfq, index++);
			writeCell(valor.getCurvati(), rowNfq, index++);
			writeCell(valor.getFecdesde(), rowNfq, index++);
			writeCell(valor.getTotflujprov(), rowNfq, index++);
			writeCell(valor.getTotprovi(), rowNfq, index++);
			writeCell(valor.getTotflujact(), rowNfq, index++);
		}
	}
	

	private static void transformarFLUJINF1(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kcarterainv","gapact","gestionit",
				"kramo","kmodalidad","kmodext","spcom","kbencon","fecdesde","totflujprovpres","totflujprovprim","totflujprovgas","totflujprov",
				"totprovi","totflujactpres","totflujactprim","totflujactgas","totflujact"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujInf1 detalle = null;
		int cont=1;
		List<FlujInf1> listaNfq = new ArrayList<FlujInf1>();
		while ((detalle = (FlujInf1) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujInf1 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getFecdesde(), rowNfq, index++);
			writeCell(valor.getTotflujprovpres(), rowNfq, index++);
			writeCell(valor.getTotflujprovprim(), rowNfq, index++);
			writeCell(valor.getTotflujprovgas(), rowNfq, index++);
			writeCell(valor.getTotflujprov(), rowNfq, index++);
			writeCell(valor.getTotprovi(), rowNfq, index++);
			writeCell(valor.getTotflujactpres(), rowNfq, index++);
			writeCell(valor.getTotflujactprim(), rowNfq, index++);
			writeCell(valor.getTotflujactgas(), rowNfq, index++);
			writeCell(valor.getTotflujact(), rowNfq, index++);
		}
	}
	

	private static void transformarPRVBT(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kramo","kmodalidad","kgarantia",
				"kpoliza","ksubpoliza","nsuscri","finisusc","kprestcal","kcarterainv","gapact","gestionit",
				"kmodext","spcom","kbencon","segmento1","tiposubriesgo","pcoase","distint","pintertecn1","durtrcasado","pintertecn2",
				"tabla1","pgastgesin1","pgastgesin2","pgastext","factor1","indfactor2","tablatanul",
				"gastrealunitario","gastreal","ipc","curvati","prv"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvBt detalle = null;
		int cont=1;
		List<PrvBt> listaNfq = new ArrayList<PrvBt>();
		while ((detalle = (PrvBt) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvBt valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getFinisusc(), rowNfq, index++);
			writeCell(valor.getKprestcal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapact(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getSegmento1(), rowNfq, index++);
			writeCell(valor.getTiposubriesgo(), rowNfq, index++);
			writeCell(valor.getPcoase(), rowNfq, index++);
			writeCell(valor.getDistint(), rowNfq, index++);
			writeCell(valor.getPintertecn1(), rowNfq, index++);
			writeCell(valor.getDurtrcasado(), rowNfq, index++);
			writeCell(valor.getPintertecn2(), rowNfq, index++);
			writeCell(valor.getTabla1(), rowNfq, index++);
			writeCell(valor.getPgastgesin1(), rowNfq, index++);
			writeCell(valor.getPgastgesin2(), rowNfq, index++);
			writeCell(valor.getPgastext(), rowNfq, index++);
			writeCell(valor.getFactor1(), rowNfq, index++);
			writeCell(valor.getIndfactor2(), rowNfq, index++);
			writeCell(valor.getTablatanul(), rowNfq, index++);
			writeCell(valor.getGastrealunitario(), rowNfq, index++);
			writeCell(valor.getGastreal(), rowNfq, index++);
			writeCell(valor.getIpc(), rowNfq, index++);
			writeCell(valor.getCurvati(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);
		}
	}
	

	private static void transformarPRVCR(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kramo","kmodalidad","spcom",
				"impnomfall","prv","capriesgo"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvCr detalle = null;
		int cont=1;
		List<PrvCr> listaNfq = new ArrayList<PrvCr>();
		while ((detalle = (PrvCr) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvCr valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getImpnomfall(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);
			writeCell(valor.getCapriesgo(), rowNfq, index++);
		}	
	}
	

	private static void transformarPRVUMIC(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kmodalidad","kpoliza",
				"ksubpoliza","kcertificado","nsuscri","norden","kgarantia","kprestacion","kajuste",
				"ctipoaport","fecinisus","kramo","kcarterainv","gapAct","intbti","intfeccalc","gestionit",
				"ctipoprovi","kmodext","spcom","kbencon","indicrescate","fnrte","fnfall","capriesgo","prv","prvpb"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvUmic detalle = null;
		int cont=1;
		List<PrvUmic> listaNfq = new ArrayList<PrvUmic>();
		while ((detalle = (PrvUmic) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvUmic valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKcertificado(), rowNfq, index++);
			writeCell(valor.getUmicKey().getNsuscri(), rowNfq, index++);
			writeCell(valor.getUmicKey().getNorden(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKgarantia(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKprestacion(), rowNfq, index++);
			writeCell(valor.getUmicKey().getKajuste(), rowNfq, index++);
			writeCell(valor.getUmicKey().getCtipoaport(), rowNfq, index++);
			writeCell(valor.getFecinisus(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getIntbti(), rowNfq, index++);
			writeCell(valor.getIntfeccalc(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getCtipoprovi(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKbencon(), rowNfq, index++);
			writeCell(valor.getIndicrescate(), rowNfq, index++);
			writeCell(valor.getFnrte(), rowNfq, index++);
			writeCell(valor.getFnfall(), rowNfq, index++);
			writeCell(valor.getCapriesgo(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);
			writeCell(valor.getPrvpb(), rowNfq, index++);
		}
	}
	

	private static void transformarPRVINF2(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kcarterainv","gapAct",
				"kramo","kmodalidad","kgarantia","ctipramo","segmento1","tiposubriesgo",
				"nuevaproduc","indicrescate","curvati","fnrte","prv"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvInf2 detalle = null;
		int cont=1;
		List<PrvInf2> listaNfq = new ArrayList<PrvInf2>();
		while ((detalle = (PrvInf2) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvInf2 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getCtipramo(), rowNfq, index++);
			writeCell(valor.getSegmento1(), rowNfq, index++);
			writeCell(valor.getTiposubriesgo(), rowNfq, index++);
			writeCell(valor.getNuevaproduc(), rowNfq, index++);
			writeCell(valor.getIndicrescate(), rowNfq, index++);
			writeCell(valor.getCurvati(), rowNfq, index++);
			writeCell(valor.getFnrte(), rowNfq, index++);
			writeCell(valor.getPrv(), rowNfq, index++);			
		}
	}

	
	private static void transformarPRVINF1(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"bt","feccierre","cnegocio","ccanal","kcarterainv","gapAct",
				"gestionit","kramo","kmodalidad","kmodext","ctipoprovi","spcom","koficont",
				"intfeccal","totProvi","pfpInv"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PrvInf1 detalle = null;
		int cont=1;
		List<PrvInf1> listaNfq = new ArrayList<PrvInf1>();
		while ((detalle = (PrvInf1) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PrvInf1 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;

			writeCell(valor.getBt(), rowNfq, index++);
			writeCell(valor.getFeccierre(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getGapAct(), rowNfq, index++);
			writeCell(valor.getGestionit(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKmodext(), rowNfq, index++);
			writeCell(valor.getCtipoprovi(), rowNfq, index++);
			writeCell(valor.getSpcom(), rowNfq, index++);
			writeCell(valor.getKoficont(), rowNfq, index++);
			writeCell(valor.getIntfeccal(), rowNfq, index++);
			writeCell(valor.getTotProvi(), rowNfq, index++);
			writeCell(valor.getPfpInv(), rowNfq, index++);
		}
	}

	
	private static void transformarFichasCalculo(XSSFWorkbook workBook,XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFCellStyle cabecera = workBook.createCellStyle();
		
		cabecera.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
		cabecera.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
		cabecera.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
		cabecera.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
		
		cabecera.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cabecera.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cabecera.setFillForegroundColor(new XSSFColor(Color.CYAN));
		
		String[] textosCabecerasFP = {"tipoRegistro", "kejecucion", "ksistema", "kprotecnico",
				"kuejecucion","ksecuencia","fefecto","fprevejec", "finiper",
				"cnegocio","ccanal","ctipobt","gparametros","csituacion","finireal",
				"ffinreal","sokauto","sokusua","gobservacion","ctipoejec",
				"smanual","fgrabacion","cusuario","fmodificacion","cusuariom","ktipoamb"};
		
		String[] textosCabecerasFA = {"tipoRegistro", "kejecucion", "ksistema", "kprotecnico",
				"kuejecucion","ksecuencia","ksecambito","cclaseamb", "coperadord",
				"gambitod","coperadorh","gambitoh","smanual","fgrabacion","cusuario",
				"ktipoamb"};
		
		String[] textosCabecerasFAd = {"tipoRegistro", "kejecucion", "ksistema", "kprotecnico",
				"kuejecucion","ksecuencia","ksecfil","cclaseamb", "ctipofiltro",
				"gclasefil","goperdesde","valordesde","goperhasta","valhasta","gopdesdegar",
				"valdesdegar","gophastagar","valhastagar","smanual","cpoliza","nsubpoliza",
				"scasados","csegmento","criesgoact","gtipinteres","fgrabacion","cusuariog",
				"fmodifica","cusuariom"};
		
		String[] textosCabecerasRP = {"tipoRegistro", "kejecucion", "ksistema", "kprotecnico",
				"kuejecucion","ksecuencia","indicadorTipoProceso"};
		
		sheetNfq.setDefaultColumnWidth(20);
		
		FichaProceso detalle = null;
		int cont=0;
		List<FichaProceso> listaNfq = new ArrayList<FichaProceso>();
		while ((detalle = (FichaProceso) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			for (int a = 0; a < textosCabecerasFP.length; a++) {
				writeCell(rowNfq.createCell(a),cabecera,textosCabecerasFP[a]);
			}
			
			FichaProceso valor = listaNfq.get(i);
			
			if(cont % 2 == 0){
				datoStyleNfq = datoStyleImparNfq;	
			} else {
				datoStyleNfq = datoStyleParNfq;	
			}
			
			rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell("01", rowNfq, index++);
			writeCell(valor.getKejecucion(), rowNfq, index++);
			writeCell(valor.getKsistema(), rowNfq, index++);
			writeCell(valor.getKprotecnico(), rowNfq, index++);
			writeCell(valor.getKuejecucion(), rowNfq, index++);
			writeCell(valor.getKsecuencia(), rowNfq, index++);
			writeCell(valor.getFefecto(), rowNfq, index++);
			writeCell(valor.getFprevejec(), rowNfq, index++);
			writeCell(valor.getFiniper(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCtipobt(), rowNfq, index++);
			writeCell(valor.getGparametros(), rowNfq, index++);
			writeCell(valor.getCsituacion(), rowNfq, index++);
			writeCell(valor.getFfinreal(), rowNfq, index++);
			writeCell(valor.getFfinreal(), rowNfq, index++);
			writeCell(valor.getSokauto(), rowNfq, index++);
			writeCell(valor.getSokusua(), rowNfq, index++);
			writeCell(valor.getGobservacion(), rowNfq, index++);
			writeCell(valor.getCtipoejec(), rowNfq, index++);
			writeCell(valor.getSmanual(), rowNfq, index++);
			writeCell(valor.getFgrabacion(), rowNfq, index++);
			writeCell(valor.getCusuario(), rowNfq, index++);
			writeCell(valor.getFmodificacion(), rowNfq, index++);
			writeCell(valor.getCusuariom(), rowNfq, index++);
			writeCell(valor.getKtipoamb(), rowNfq, index++);
			
			List<FiltroFichaProceso> listFA = valor.getFiltrosAmbito();
			
			if (null != listFA && listFA.size() > 0) {
				
				rowNfq = sheetNfq.createRow(cont);
				cont ++;
				
				for (int a = 0; a < textosCabecerasFA.length; a++) {
					writeCell(rowNfq.createCell(a),cabecera,textosCabecerasFA[a]);
				}
				
				for (int fa =0; fa<listFA.size(); fa++) {
					
					FiltroFichaProceso valorFA = listFA.get(fa);
					
					if(cont % 2 == 0){
						datoStyleNfq = datoStyleImparNfq;	
					} else {
						datoStyleNfq = datoStyleParNfq;
					}
					
					rowNfq = sheetNfq.createRow(cont);
					cont ++;
					
					index = 0;
					
					writeCell("02", rowNfq, index++);
					writeCell(valor.getKejecucion(), rowNfq, index++);
					writeCell(valor.getKsistema(), rowNfq, index++);
					writeCell(valor.getKprotecnico(), rowNfq, index++);
					writeCell(valor.getKuejecucion(), rowNfq, index++);
					writeCell(valor.getKsecuencia(), rowNfq, index++);
					writeCell(valorFA.getKsecambito(), rowNfq, index++);
					writeCell(valorFA.getCclaseamb(), rowNfq, index++);
					writeCell(valorFA.getCoperadord(), rowNfq, index++);
					writeCell(valorFA.getGambitod(), rowNfq, index++);
					writeCell(valorFA.getCoperadorh(), rowNfq, index++);
					writeCell(valorFA.getGambitoh(), rowNfq, index++);
					writeCell(valorFA.getSmanual(), rowNfq, index++);
					writeCell(valorFA.getFgrabacion(), rowNfq, index++);
					writeCell(valorFA.getCusuario(), rowNfq, index++);
					writeCell(valorFA.getKtipoamb(), rowNfq, index++);
				}
			}
			
			List<FiltroFichaProcesoAdicional> listFAd = valor.getFiltrosAdicionales();
			
			if (null != listFAd && listFAd.size() > 0) {
				
				rowNfq = sheetNfq.createRow(cont);
				cont ++;
				
				for (int a = 0; a < textosCabecerasFAd.length; a++) {
					writeCell(rowNfq.createCell(a),cabecera,textosCabecerasFAd[a]);
				}
				
				for (int fad =0; fad<listFAd.size(); fad++) {
					
					FiltroFichaProcesoAdicional valorFAd = listFAd.get(fad);
					
					if(cont % 2 == 0){
						datoStyleNfq = datoStyleImparNfq;	
					} else {
						datoStyleNfq = datoStyleParNfq;	
					}
					
					rowNfq = sheetNfq.createRow(cont);
					cont ++;
					
					index = 0;
					
					writeCell("03", rowNfq, index++);
					writeCell(valor.getKejecucion(), rowNfq, index++);
					writeCell(valor.getKsistema(), rowNfq, index++);
					writeCell(valor.getKprotecnico(), rowNfq, index++);
					writeCell(valor.getKuejecucion(), rowNfq, index++);
					writeCell(valor.getKsecuencia(), rowNfq, index++);
					writeCell(valorFAd.getKsecfil(), rowNfq, index++);
					writeCell(valorFAd.getCtipofiltro(), rowNfq, index++);
					writeCell(valorFAd.getGclasefil(), rowNfq, index++);
					writeCell(valorFAd.getGoperdesde(), rowNfq, index++);
					writeCell(valorFAd.getValordesde(), rowNfq, index++);
					writeCell(valorFAd.getGoperhasta(), rowNfq, index++);
					writeCell(valorFAd.getValhasta(), rowNfq, index++);
					writeCell(valorFAd.getGopdesdegar(), rowNfq, index++);
					writeCell(valorFAd.getValdesdegar(), rowNfq, index++);
					writeCell(valorFAd.getGophastagar(), rowNfq, index++);
					writeCell(valorFAd.getValhastagar(), rowNfq, index++);
					writeCell(valorFAd.getSmanual(), rowNfq, index++);
					writeCell(valorFAd.getCpoliza().toString(), rowNfq, index++);
					writeCell(valorFAd.getNsubpoliza(), rowNfq, index++);
					writeCell(valorFAd.getScasados(), rowNfq, index++);
					writeCell(valorFAd.getCsegmento(), rowNfq, index++);
					writeCell(valorFAd.getCriesgoact(), rowNfq, index++);
					writeCell(valorFAd.getGtipinteres(), rowNfq, index++);
					writeCell(valorFAd.getFgrabacion(), rowNfq, index++);
					writeCell(valorFAd.getCusuariog(), rowNfq, index++);
					writeCell(valorFAd.getFmodifica(), rowNfq, index++);
					writeCell(valorFAd.getCusuariom(), rowNfq, index++);
				}
			}
			
			RegistroParametros valorRP = valor.getRegistroParametros();
			
			if (null != valorRP) {
				
				rowNfq = sheetNfq.createRow(cont);
				cont ++;
				
				for (int a = 0; a < textosCabecerasRP.length; a++) {
					writeCell(rowNfq.createCell(a),cabecera,textosCabecerasRP[a]);
				}
				
				if(cont % 2 == 0){
					datoStyleNfq = datoStyleImparNfq;	
				} else {
					datoStyleNfq = datoStyleParNfq;	
				}
				
				rowNfq = sheetNfq.createRow(cont);
				cont ++;
				
				index = 0;
				
				writeCell("04", rowNfq, index++);
				writeCell(valor.getKejecucion(), rowNfq, index++);
				writeCell(valor.getKsistema(), rowNfq, index++);
				writeCell(valor.getKprotecnico(), rowNfq, index++);
				writeCell(valor.getKuejecucion(), rowNfq, index++);
				writeCell(valor.getKsecuencia(), rowNfq, index++);
				writeCell(valorRP.getIndicadorTipoProceso(), rowNfq, index++);
			}
		}	
	}

	
	private static void transformarMaestro(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "ccartera", "fecCierre",
				"kmodalidad","kpoliza","ksubpoliza","kcertificado", "nsuscri",
				"norden","kgarantia","kprestacion","kajuste","ctipoaport","tarifa",
				"kcategoria","swinnominada","nasegurados","nuevaProduc","csitupol",
				"ctipramo","kramo","kmodext","kcarterainv","koficont","ctipoprovi",
				"swcasado","gapAct","koficina","spcom","reglamento","kbencon","tipoflexseg",
				"tipoflexprim","edifer","pb","tipoPb","tipoSubriesgo","indicpension",
				"segmento1","kcoaseOri","ctipocoaseg","pcoaseg","distint","distext",
				"fecefecini","fecefecred","fecinisus","fecefecfin","fecafinfinancia",
				"fecinipagprim","fecfinpagprim","fecdesderenova","fechastarenova",
				"partano","tc","nrenovaciones","ndursegano","ndursegmes","ndursegdia",
				"ndurprima","porsexh","fnacAseg1","csexAseg1","edadAseg1","fnacAseg2",
				"csexAseg2","edadAseg2","fnacAseg3","csexAseg3","edadAseg3","fnacAseg4",
				"csexAseg4","edadAseg4","fnacAseg5","csexAseg5","edadAseg5","gedadMin","gedadMax",
				"iprimanetaini","iprimanetaact","iprimatarada","cformarevprim","prevprima","cformpago",
				"precargfrac","fdiadepago","pdtoemp","pdtoaseg","pfpinv","ppcap","ppc","ppr","icapini",
				"icapact","isaldo","icapfall","cformarevcap","porevalcap","porgamma","crmax","pcapriesgo",
				"tempVit","rentini","rentact","rentmini","fecIni","fecFin","prevrenta","preversion",
				"npergaran","nadifer","forpagrent","cpagrenta","ctipoRevrenta","cformaRevrenta","ndurrenta",
				"codTir","tirIni","tirCie","krescate1","krescate2","krescate3","indicrescate","riesgrescI",
				"tabla1Aseg1","tabla2Aseg1","tabla3Aseg1","tabla1Aseg2","tabla2Aseg2","tabla3Aseg2",
				"tabla1Aseg3","tabla2Aseg3","tabla3Aseg3","tabla1Aseg4","tabla2Aseg4","tabla3Aseg4",
				"tabla1Aseg5","tabla2Aseg5","tabla3Aseg5","pintertecnI1","gapI1","swcasadoI1","fecIniTramo1",
				"fecFinTramo1","pintertecnI2","gapI2","swcasadoI2","fecIniTramo2","fecFinTramo2",
				"pintertecnI3","gapI3","swcasadoI3","fecIniTramo3","fecFinTramo3","pintertecnI4","gapI4",
				"swcasadoI4","fecIniTramo4","fecFinTramo4","pintertecnI5","gapI5","swcasadoI5","fecIniTramo5",
				"fecFinTramo5","pgastgesex1I","pgastgesex2I","pgastgesin1I","pgastgesin2I","pgastgesin3I",
				"pgastgesin4I","pgastgesin5I","impgasmin","impgasmax","duracit","psobremort","priesgo",
				"ragravado","ggim","basecalcucomi1","basecalcucomi2","basecalcucomi3","mespagocomi","npericomi1",
				"pcomisiona1","npericomi2","pcomisiona2","npericomi3","pcomisiona3","prestCal","gestionit",
				"sigpitertecn1","sigpitertecn2","sigpitertecn3","sigpitertecn4","sigpitertecn5","indinval",
				"pregrupo","cestadoAseg1","cestadoAseg2","cestadoAseg3","cestadoAseg4","cestadoAseg5","swmodoner",
				"swpoloner","kcarcontacto","metmedicion","uoa","proxyra","proxycsm","kcarinv17",
				"kcurvalir","kcarinvlir","cohorte"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		Umic detalle = null;
		int cont=1;
		List<Umic> listaNfq = new ArrayList<Umic>();
		while ((detalle = (Umic) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			Umic valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			//Datos personales
			writeCell(valor.getDatosGenerales().getCnegocio(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getCcanal(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getCcartera(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getFecCierre(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKcertificado(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getNsuscri(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getNorden(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKgarantia(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKprestacion(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKajuste(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getCtipoaport(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getTarifa(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKcategoria(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getSwinnominada(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getNasegurados(), rowNfq, index++);
			if (valor.getDatosGenerales().getNuevaProduc()){writeCell("S", rowNfq, index++);} else {writeCell("N", rowNfq, index++);}
			writeCell(valor.getDatosGenerales().getCsitupol(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getCtipramo(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKramo(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKmodext(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKcarterainv(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKoficont(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getCtipoprovi(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getSwcasado(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getGapAct(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKoficina(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getSpcom(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getReglamento(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getKbencon(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getTipoflexseg(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getTipoflexprim(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getEdifer(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getPb(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getTipoPb(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getTipoSubriesgo(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getIndicpension(), rowNfq, index++);
			writeCell(valor.getDatosGenerales().getSegmento1(), rowNfq, index++);
			//Coaseguro
			writeCell(valor.getDatosCoaseguro().getKcoaseOri(), rowNfq, index++);
			writeCell(valor.getDatosCoaseguro().getCtipocoaseg(), rowNfq, index++);
			writeCell(valor.getDatosCoaseguro().getPcoaseg(), rowNfq, index++);
			writeCell(valor.getDatosCoaseguro().getDistint(), rowNfq, index++);
			writeCell(valor.getDatosCoaseguro().getDistext(), rowNfq, index++);
			//Fechas
			writeCell(valor.getFechas().getFecefecini(), rowNfq, index++);
			writeCell(valor.getFechas().getFecefecred(), rowNfq, index++);
			writeCell(valor.getFechas().getFecinisus(), rowNfq, index++);
			writeCell(valor.getFechas().getFecefecfin(), rowNfq, index++);
			writeCell(valor.getFechas().getFecafinfinancia(), rowNfq, index++);
			writeCell(valor.getFechas().getFecinipagprim(), rowNfq, index++);
			writeCell(valor.getFechas().getFecfinpagprim(), rowNfq, index++);
			writeCell(valor.getFechas().getFecdesderenova(), rowNfq, index++);
			writeCell(valor.getFechas().getFechastarenova(), rowNfq, index++);
			writeCell(valor.getFechas().getPartano(), rowNfq, index++);
			writeCell(valor.getFechas().getTc(), rowNfq, index++);
			//Duraciones
			writeCell(valor.getDuraciones().getNrenovaciones(), rowNfq, index++);
			writeCell(valor.getDuraciones().getNdursegano(), rowNfq, index++);
			writeCell(valor.getDuraciones().getNdursegmes(), rowNfq, index++);
			writeCell(valor.getDuraciones().getNdursegdia(), rowNfq, index++);
			writeCell(valor.getDuraciones().getNdurprima(), rowNfq, index++);
			//Asegurados
			writeCell(valor.getAsegurados().getPorsexh(), rowNfq, index++);
			writeCell(valor.getAsegurados().getFnacAseg1(), rowNfq, index++);
			writeCell(valor.getAsegurados().getCsexAseg1(), rowNfq, index++);
			writeCell(valor.getAsegurados().getEdadAseg1(), rowNfq, index++);
			writeCell(valor.getAsegurados().getFnacAseg2(), rowNfq, index++);
			writeCell(valor.getAsegurados().getCsexAseg2(), rowNfq, index++);
			writeCell(valor.getAsegurados().getEdadAseg2(), rowNfq, index++);
			writeCell(valor.getAsegurados().getFnacAseg3(), rowNfq, index++);
			writeCell(valor.getAsegurados().getCsexAseg3(), rowNfq, index++);
			writeCell(valor.getAsegurados().getEdadAseg3(), rowNfq, index++);
			writeCell(valor.getAsegurados().getFnacAseg4(), rowNfq, index++);
			writeCell(valor.getAsegurados().getCsexAseg4(), rowNfq, index++);
			writeCell(valor.getAsegurados().getEdadAseg4(), rowNfq, index++);
			writeCell(valor.getAsegurados().getFnacAseg5(), rowNfq, index++);
			writeCell(valor.getAsegurados().getCsexAseg5(), rowNfq, index++);
			writeCell(valor.getAsegurados().getEdadAseg5(), rowNfq, index++);
			writeCell(valor.getAsegurados().getGedadMin(), rowNfq, index++);
			writeCell(valor.getAsegurados().getGedadMax(), rowNfq, index++);
			//Primas
			writeCell(valor.getPrimas().getIprimanetaini(), rowNfq, index++);
			writeCell(valor.getPrimas().getIprimanetaact(), rowNfq, index++);
			writeCell(valor.getPrimas().getIprimatarada(), rowNfq, index++);
			writeCell(valor.getPrimas().getCformarevprim(), rowNfq, index++);
			writeCell(valor.getPrimas().getPrevprima(), rowNfq, index++);
			writeCell(valor.getPrimas().getCformpago(), rowNfq, index++);
			writeCell(valor.getPrimas().getPrecargfrac(), rowNfq, index++);
			writeCell(valor.getPrimas().getFdiadepago(), rowNfq, index++);
			writeCell(valor.getPrimas().getPdtoemp(), rowNfq, index++);
			writeCell(valor.getPrimas().getPdtoaseg(), rowNfq, index++);
			writeCell(valor.getPrimas().getPfpinv(), rowNfq, index++);
			writeCell(valor.getPrimas().getPpcap(), rowNfq, index++);
			writeCell(valor.getPrimas().getPpc(), rowNfq, index++);
			writeCell(valor.getPrimas().getPpr(), rowNfq, index++);
			//Capitales
			writeCell(valor.getCapitales().getIcapini(), rowNfq, index++);
			writeCell(valor.getCapitales().getIcapact(), rowNfq, index++);
			writeCell(valor.getCapitales().getIsaldo(), rowNfq, index++);
			writeCell(valor.getCapitales().getIcapfall(), rowNfq, index++);
			writeCell(valor.getCapitales().getCformarevcap(), rowNfq, index++);
			writeCell(valor.getCapitales().getPorevalcap(), rowNfq, index++);
			writeCell(valor.getCapitales().getPorgamma(), rowNfq, index++);
			writeCell(valor.getCapitales().getCrmax(), rowNfq, index++);
			writeCell(valor.getCapitales().getPcapriesgo(), rowNfq, index++);
			//Rentas
			writeCell(valor.getRentas().getTempVit(), rowNfq, index++);
			writeCell(valor.getRentas().getRentini(), rowNfq, index++);
			writeCell(valor.getRentas().getRentact(), rowNfq, index++);
			writeCell(valor.getRentas().getRentmini(), rowNfq, index++);
			writeCell(valor.getRentas().getFecIni(), rowNfq, index++);
			writeCell(valor.getRentas().getFecFin(), rowNfq, index++);
			writeCell(valor.getRentas().getPrevrenta(), rowNfq, index++);
			writeCell(valor.getRentas().getPreversion(), rowNfq, index++);
			writeCell(valor.getRentas().getNpergaran(), rowNfq, index++);
			writeCell(valor.getRentas().getNadifer(), rowNfq, index++);
			writeCell(valor.getRentas().getForpagrent(), rowNfq, index++);
			writeCell(valor.getRentas().getCpagrenta(), rowNfq, index++);
			writeCell(valor.getRentas().getCtipoRevrenta(), rowNfq, index++);
			writeCell(valor.getRentas().getCformaRevrenta(), rowNfq, index++);
			writeCell(valor.getRentas().getNdurrenta(), rowNfq, index++);
			//Rescates
			writeCell(valor.getRescates().getCodTir(), rowNfq, index++);
			writeCell(valor.getRescates().getTirIni(), rowNfq, index++);
			writeCell(valor.getRescates().getTirCie(), rowNfq, index++);
			writeCell(valor.getRescates().getKrescate1(), rowNfq, index++);
			writeCell(valor.getRescates().getKrescate2(), rowNfq, index++);
			writeCell(valor.getRescates().getKrescate3(), rowNfq, index++);
			writeCell(valor.getRescates().getIndicrescate(), rowNfq, index++);
			writeCell(valor.getRescates().getRiesgrescI(), rowNfq, index++);
			//Bti
			writeCell(valor.getBti().getTabla1Aseg1(), rowNfq, index++);
			writeCell(valor.getBti().getTabla2Aseg1(), rowNfq, index++);
			writeCell(valor.getBti().getTabla3Aseg1(), rowNfq, index++);
			writeCell(valor.getBti().getTabla1Aseg2(), rowNfq, index++);
			writeCell(valor.getBti().getTabla2Aseg2(), rowNfq, index++);
			writeCell(valor.getBti().getTabla3Aseg2(), rowNfq, index++);
			writeCell(valor.getBti().getTabla1Aseg3(), rowNfq, index++);
			writeCell(valor.getBti().getTabla2Aseg3(), rowNfq, index++);
			writeCell(valor.getBti().getTabla3Aseg3(), rowNfq, index++);
			writeCell(valor.getBti().getTabla1Aseg4(), rowNfq, index++);
			writeCell(valor.getBti().getTabla2Aseg4(), rowNfq, index++);
			writeCell(valor.getBti().getTabla3Aseg4(), rowNfq, index++);
			writeCell(valor.getBti().getTabla1Aseg5(), rowNfq, index++);
			writeCell(valor.getBti().getTabla2Aseg5(), rowNfq, index++);
			writeCell(valor.getBti().getTabla3Aseg5(), rowNfq, index++);
			writeCell(valor.getBti().getPintertecnI1(), rowNfq, index++);
			writeCell(valor.getBti().getGapI1(), rowNfq, index++);
			writeCell(valor.getBti().getSwcasadoI1(), rowNfq, index++);
			writeCell(valor.getBti().getFecIniTramo1(), rowNfq, index++);
			writeCell(valor.getBti().getFecFinTramo1(), rowNfq, index++);
			writeCell(valor.getBti().getPintertecnI2(), rowNfq, index++);
			writeCell(valor.getBti().getGapI2(), rowNfq, index++);
			writeCell(valor.getBti().getSwcasadoI2(), rowNfq, index++);
			writeCell(valor.getBti().getFecIniTramo2(), rowNfq, index++);
			writeCell(valor.getBti().getFecFinTramo2(), rowNfq, index++);
			writeCell(valor.getBti().getPintertecnI3(), rowNfq, index++);
			writeCell(valor.getBti().getGapI3(), rowNfq, index++);
			writeCell(valor.getBti().getSwcasadoI3(), rowNfq, index++);
			writeCell(valor.getBti().getFecIniTramo3(), rowNfq, index++);
			writeCell(valor.getBti().getFecFinTramo3(), rowNfq, index++);
			writeCell(valor.getBti().getPintertecnI4(), rowNfq, index++);
			writeCell(valor.getBti().getGapI4(), rowNfq, index++);
			writeCell(valor.getBti().getSwcasadoI4(), rowNfq, index++);
			writeCell(valor.getBti().getFecIniTramo4(), rowNfq, index++);
			writeCell(valor.getBti().getFecFinTramo4(), rowNfq, index++);
			writeCell(valor.getBti().getPintertecnI5(), rowNfq, index++);
			writeCell(valor.getBti().getGapI5(), rowNfq, index++);
			writeCell(valor.getBti().getSwcasadoI5(), rowNfq, index++);
			writeCell(valor.getBti().getFecIniTramo5(), rowNfq, index++);
			writeCell(valor.getBti().getFecFinTramo5(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesex1I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesex2I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesin1I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesin2I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesin3I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesin4I(), rowNfq, index++);
			writeCell(valor.getBti().getPgastgesin5I(), rowNfq, index++);
			writeCell(valor.getBti().getImpgasmin(), rowNfq, index++);
			writeCell(valor.getBti().getImpgasmax(), rowNfq, index++);
			writeCell(valor.getBti().getDuracit(), rowNfq, index++);
			writeCell(valor.getBti().getPsobremort(), rowNfq, index++);
			writeCell(valor.getBti().getPriesgo(), rowNfq, index++);
			writeCell(valor.getBti().getRagravado(), rowNfq, index++);
			writeCell(valor.getBti().getGgim(), rowNfq, index++);
			//Comisiones
			writeCell(valor.getComisiones().getBasecalcucomi1(), rowNfq, index++);
			writeCell(valor.getComisiones().getBasecalcucomi2(), rowNfq, index++);
			writeCell(valor.getComisiones().getBasecalcucomi3(), rowNfq, index++);
			writeCell(valor.getComisiones().getMespagocomi(), rowNfq, index++);
			writeCell(valor.getComisiones().getNpericomi1(), rowNfq, index++);
			writeCell(valor.getComisiones().getPcomisiona1(), rowNfq, index++);
			writeCell(valor.getComisiones().getNpericomi2(), rowNfq, index++);
			writeCell(valor.getComisiones().getPcomisiona2(), rowNfq, index++);
			writeCell(valor.getComisiones().getNpericomi3(), rowNfq, index++);
			writeCell(valor.getComisiones().getPcomisiona3(), rowNfq, index++);
			//Datos Adicionales
			writeCell(valor.getDatosAdicionales().getPrestCal(), rowNfq, index++);
			//Otros datos
			writeCell(valor.getOtrosDatos().getGestionit(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getSigpitertecn1(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getSigpitertecn2(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getSigpitertecn3(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getSigpitertecn4(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getSigpitertecn5(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getIndinval(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getPregrupo(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getCestadoAseg1(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getCestadoAseg2(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getCestadoAseg3(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getCestadoAseg4(), rowNfq, index++);
			writeCell(valor.getOtrosDatos().getCestadoAseg5(), rowNfq, index++);
			//Datos NIIF17
			writeCell(valor.getDatosNiif17().getswmodoner(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getswpoloner(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getkcarcontacto(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getmetmedicion(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getuoa(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getproxyra(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getproxycsm(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getkcarinv17(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getkcurvalir(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getkcarinvlir(), rowNfq, index++);
			writeCell(valor.getDatosNiif17().getcohorte(), rowNfq, index++);
			
		}
		
	}

	
	private static void transformarPTipo(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"origen", "cnegocio", "ccanal", "ccartera",
				"kmodalidad","kpoliza","ksubpoliza","kcertificado", "nsuscri",
				"norden","kgarantia","kprestacion","kajuste","ctipoaport"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PolizasTipo detalle = null;
		int cont=1;
		List<PolizasTipo> listaNfq = new ArrayList<PolizasTipo>();
		while ((detalle = (PolizasTipo) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PolizasTipo valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getOrigen(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCcartera(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKmodalidad(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKcertificado(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNsuscri(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getNorden(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKgarantia(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKprestacion(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getKajuste(), rowNfq, index++);
			writeCell(valor.getClaveUmic().getCtipoaport(), rowNfq, index++);
		}
	}

	
	private static void transformarAuxVLiquid(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kpoliza", "kfondo", "kramo", "kmodalidad",
				"pnparticipacion","pinptotfondo","cnparticipacion","cinptotfondo",
				"anparticipacion","ainptotfondo","pkvalor","pmoneda","pfvalor",
				"kcestapol"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ValoresLiquidativos detalle = null;
		int cont=1;
		List<ValoresLiquidativos> listaNfq = new ArrayList<ValoresLiquidativos>();
		while ((detalle = (ValoresLiquidativos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ValoresLiquidativos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKfondo(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getPnparticipacion(), rowNfq, index++);
			writeCell(valor.getPinptotfondo(), rowNfq, index++);
			writeCell(valor.getCnparticipacion(), rowNfq, index++);
			writeCell(valor.getCinptotfondo(), rowNfq, index++);
			writeCell(valor.getAnparticipacion(), rowNfq, index++);
			writeCell(valor.getAinptotfondo(), rowNfq, index++);
			writeCell(valor.getPkvalor(), rowNfq, index++);
			writeCell(valor.getPmoneda(), rowNfq, index++);
			writeCell(valor.getKcestapol(), rowNfq, index++);
		}
	}

	
	private static void transformarAuxPBTecni(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kpoliza", "ksubpol", "preajuste", "pgasto",
				"iarrastre"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DatosPbTecnica detalle = null;
		int cont=1;
		List<DatosPbTecnica> listaNfq = new ArrayList<DatosPbTecnica>();
		while ((detalle = (DatosPbTecnica) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DatosPbTecnica valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpol(), rowNfq, index++);
			writeCell(valor.getPreajuste(), rowNfq, index++);
			writeCell(valor.getPgasto(), rowNfq, index++);
			writeCell(valor.getIarrastre(), rowNfq, index++);
		}
	}

	
	private static void transformarAuxPagosPlan(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kpoliza", "ksubpol", "kcerti", "kgrsus","cprestaEntorno",
				"kpresta","kajuste","fplreaEfecto","eplreaBruto","pcoase","eplreaNeto","cgarantia",
				"cprestaFict","periodicidad","errorPeriodic"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PagosPlanificados detalle = null;
		int cont=1;
		List<PagosPlanificados> listaNfq = new ArrayList<PagosPlanificados>();
		while ((detalle = (PagosPlanificados) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PagosPlanificados valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpol(), rowNfq, index++);
			writeCell(valor.getKcerti(), rowNfq, index++);
			writeCell(valor.getKgrsus(), rowNfq, index++);
			writeCell(valor.getCprestaEntorno(), rowNfq, index++);
			writeCell(valor.getKpresta(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getFplreaEfecto(), rowNfq, index++);
			writeCell(valor.getEplreaBruto(), rowNfq, index++);
			writeCell(valor.getPcoase(), rowNfq, index++);
			writeCell(valor.getEplreaNeto(), rowNfq, index++);
			writeCell(valor.getCgarantia(), rowNfq, index++);
			writeCell(valor.getCprestaFict(), rowNfq, index++);
			writeCell(valor.getPeriodicidad(), rowNfq, index++);
			writeCell(valor.getErrorPeriodic(), rowNfq, index++);
		}
	}


	private static void transformarAuxEspecific(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kpoliza", "ksubpol", "kcerti", "nsuscri", "codigo","dato"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DatosEspecificos detalle = null;
		int cont=1;
		List<DatosEspecificos> listaNfq = new ArrayList<DatosEspecificos>();
		while ((detalle = (DatosEspecificos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DatosEspecificos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpol(), rowNfq, index++);
			writeCell(valor.getKcerti(), rowNfq, index++);
			writeCell(valor.getnsuscri(), rowNfq, index++);
			writeCell(valor.getCodigo().toString(), rowNfq, index++);
			writeCell(valor.getDato(), rowNfq, index++);
		}
	}

	
	private static void transformarAuxDatoscoa(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kpoliza", "ksubpol", "krazon", "kcuadro",
				"kcoase1", "kcoase2", "kcoase3", "kcoase4", "kcoase5","kcoase6",
				"kcoase7", "kcoase8", "kcoase9", "kcoase10", "kcoase11","kcoase12",
				"kcoase13","kcoase14","kcoase15","kcoase16","kcoase17","kcoase18",
				"kcoase19","kcoase20"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DatosAdicionalesCoaseguro detalle = null;
		int cont=1;
		List<DatosAdicionalesCoaseguro> listaNfq = new ArrayList<DatosAdicionalesCoaseguro>();
		while ((detalle = (DatosAdicionalesCoaseguro) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DatosAdicionalesCoaseguro valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpol(), rowNfq, index++);
			writeCell(valor.getKrazon(), rowNfq, index++);
			writeCell(valor.getKcuadro().toString(), rowNfq, index++);
			writeCell(valor.getKcoase1(), rowNfq, index++);
			writeCell(valor.getKcoase2(), rowNfq, index++);
			writeCell(valor.getKcoase3(), rowNfq, index++);
			writeCell(valor.getKcoase4(), rowNfq, index++);
			writeCell(valor.getKcoase5(), rowNfq, index++);
			writeCell(valor.getKcoase6(), rowNfq, index++);
			writeCell(valor.getKcoase7(), rowNfq, index++);
			writeCell(valor.getKcoase8(), rowNfq, index++);
			writeCell(valor.getKcoase9(), rowNfq, index++);
			writeCell(valor.getKcoase10(), rowNfq, index++);
			writeCell(valor.getKcoase11(), rowNfq, index++);
			writeCell(valor.getKcoase12(), rowNfq, index++);
			writeCell(valor.getKcoase13(), rowNfq, index++);
			writeCell(valor.getKcoase14(), rowNfq, index++);
			writeCell(valor.getKcoase15(), rowNfq, index++);
			writeCell(valor.getKcoase16(), rowNfq, index++);
			writeCell(valor.getKcoase17(), rowNfq, index++);
			writeCell(valor.getKcoase18(), rowNfq, index++);
			writeCell(valor.getKcoase19(), rowNfq, index++);
			writeCell(valor.getKcoase20(), rowNfq, index++);
		}
	}

	
	private static void transformarAuxAmortiz(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"cnegocio", "ccanal", "kmodalidad", "kpoliza",
				"ksubpoliza", "kcertificado", "nsuscri", "norden", "kgarantia",
				"kprestacion", "kajuste", "pintermor", "npercar", "cperamont",
				"csisamort","fdesembolso","capitalIni","capitalAct","poraseg",
				"cformcuota","cperaseg","porcrec"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		CuadrosAmortizacion detalle = null;
		int cont=1;
		List<CuadrosAmortizacion> listaNfq = new ArrayList<CuadrosAmortizacion>();
		while ((detalle = (CuadrosAmortizacion) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			CuadrosAmortizacion valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKpoliza().toString(), rowNfq, index++);
			writeCell(valor.getKsubpoliza(), rowNfq, index++);
			writeCell(valor.getKcertificado(), rowNfq, index++);
			writeCell(valor.getNsuscri(), rowNfq, index++);
			writeCell(valor.getNorden(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKprestacion(), rowNfq, index++);
			writeCell(valor.getKajuste(), rowNfq, index++);
			writeCell(valor.getPintermor(), rowNfq, index++);
			writeCell(valor.getNpercar(), rowNfq, index++);
			writeCell(valor.getCperamont(), rowNfq, index++);
			writeCell(valor.getCsisamort(), rowNfq, index++);
			writeCell(valor.getFdesembolso(), rowNfq, index++);
			writeCell(valor.getCapitalIni(), rowNfq, index++);
			writeCell(valor.getCapitalAct(), rowNfq, index++);
			writeCell(valor.getPoraseg(), rowNfq, index++);
			writeCell(valor.getCformcuota(), rowNfq, index++);
			writeCell(valor.getCperaseg(), rowNfq, index++);
			writeCell(valor.getPorcrec(), rowNfq, index++);
			
		}
	}

	
	private static void transformarMVI0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kmadaptac", "kfinicio", "ffin", "cperadaptac",
				"cperpend", "cperutil"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		MetodosAdaptacion detalle = null;
		int cont=1;
		List<MetodosAdaptacion> listaNfq = new ArrayList<MetodosAdaptacion>();
		while ((detalle = (MetodosAdaptacion) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			MetodosAdaptacion valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKmadaptac(), rowNfq, index++);
			writeCell(valor.getKfinicio(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getCperadaptac(), rowNfq, index++);
			writeCell(valor.getCperpend(), rowNfq, index++);
			writeCell(valor.getCperutil(), rowNfq, index++);
		}
	}

	
	private static void transformarMAD0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"kmadaptac", "fini", "ffin", "ndotacion",
				"npendiente", "ndivisor"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		PeriodosAd detalle = null;
		int cont=1;
		List<PeriodosAd> listaNfq = new ArrayList<PeriodosAd>();
		while ((detalle = (PeriodosAd) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			PeriodosAd valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKmadaptac(), rowNfq, index++);
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getNdotacion(), rowNfq, index++);
			writeCell(valor.getNpendiente(), rowNfq, index++);
			writeCell(valor.getNdivisor(), rowNfq, index++);
		}
	}

	
	private static void transformarITR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"fini", "ffin", "pitref"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		InteresTecnico detalle = null;
		int cont=1;
		List<InteresTecnico> listaNfq = new ArrayList<InteresTecnico>();
		while ((detalle = (InteresTecnico) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			InteresTecnico valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getPitref(), rowNfq, index++);
		}
		
	}

	
	private static void transformarITG0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = {"fcierre", "ktipobt", "kgap", "kaprossp", 
				"kcasado", "kcriterioit", "pitmedio", "pitmaxcalc", "ccurva"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		AsigInteresTecnico detalle = null;
		int cont=1;
		List<AsigInteresTecnico> listaNfq = new ArrayList<AsigInteresTecnico>();
		while ((detalle = (AsigInteresTecnico) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			AsigInteresTecnico valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFcierre(), rowNfq, index++);
			writeCell(valor.getKtipobt(), rowNfq, index++);
			writeCell(valor.getKgap(), rowNfq, index++);
			writeCell(valor.getKaprossp(), rowNfq, index++);
			writeCell(valor.getKcasado(), rowNfq, index++);
			writeCell(valor.getKcriterioit(), rowNfq, index++);
			writeCell(valor.getPitmedio(), rowNfq, index++);
			writeCell(valor.getPitmaxcalc(), rowNfq, index++);
			writeCell(valor.getCcurva(), rowNfq, index++);
		}
	}

	
	private static void transformarGRE0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktipobt", "ccanal", "cnegocio", "kramo", 
				"kmodalidad", "fecDesde", "fecHasta", "gastoPorUmic", "pctGastoProv"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		GastosReales detalle = null;
		int cont=1;
		List<GastosReales> listaNfq = new ArrayList<GastosReales>();
		while ((detalle = (GastosReales) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			GastosReales valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtipobt(), rowNfq, index++);
			writeCell(valor.getCcanal(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getFecDesde(), rowNfq, index++);
			writeCell(valor.getFecHasta(), rowNfq, index++);
			writeCell(valor.getGastoPorUmic(), rowNfq, index++);
			writeCell(valor.getPctGastoProv(), rowNfq, index++);
		}
	}

	
	private static void transformarGAR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kgap", "fini", "ffin", "prentabil", 
				"ffincasado"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		RentaGAP detalle = null;
		int cont=1;
		List<RentaGAP> listaNfq = new ArrayList<RentaGAP>();
		while ((detalle = (RentaGAP) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			RentaGAP valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKgap(), rowNfq, index++);
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getPrentabil(), rowNfq, index++);
			writeCell(valor.getFfincasado(), rowNfq, index++);
		}
	}

	
	private static void transformarDGI0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktipobt", "kmodalidad","kgarantia",
				"fini", "ffin", "pdifegap", "pdifegac", "pdifegar",
				"pdifeprp", "pdifeprc", "pdifeprr", "pdifersp", "pdifersc",
				"pdifersr"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DiferencialGastos detalle = null;
		int cont=1;
		List<DiferencialGastos> listaNfq = new ArrayList<DiferencialGastos>();
		while ((detalle = (DiferencialGastos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DiferencialGastos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtipobt(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getPdifegap(), rowNfq, index++);
			writeCell(valor.getPdifegac(), rowNfq, index++);
			writeCell(valor.getPdifegar(), rowNfq, index++);
			writeCell(valor.getPdifeprp(), rowNfq, index++);
			writeCell(valor.getPdifeprc(), rowNfq, index++);
			writeCell(valor.getPdifeprr(), rowNfq, index++);
			writeCell(valor.getPdifersp(), rowNfq, index++);
			writeCell(valor.getPdifersc(), rowNfq, index++);
			writeCell(valor.getPdifersr(), rowNfq, index++);
		}
		
	}

	
	private static void transformarCTE0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktipobt", "ktablaexp", "kmodalidad","kgarantia",
				"fini", "ffin", "ctablaini", "ctablafin"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ConversionTablasExp detalle = null;
		int cont=1;
		List<ConversionTablasExp> listaNfq = new ArrayList<ConversionTablasExp>();
		while ((detalle = (ConversionTablasExp) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ConversionTablasExp valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtipobt(), rowNfq, index++);
			writeCell(valor.getKtablaexp(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getCtablaini(), rowNfq, index++);
			writeCell(valor.getCtablafin(), rowNfq, index++);
		}
	}

	
	private static void transformarATR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kbasetec", "kcompania", "knegocio","kriesgo",
				"ksexo", "kcateg", "kedaddesde", "kmodalidad", "kfdesde", "edadhasta",
				"fhasta", "ctabbase", "pfactor1", "pfactor2a", "pfactor2b","pfactor2c",
				"pfactor2d","pfactor2e","pfactor2f","pfactor2g","pfactor2h","pfactor2i",
				"pfactor2j"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ConvTablasExpReal detalle = null;
		int cont=1;
		List<ConvTablasExpReal> listaNfq = new ArrayList<ConvTablasExpReal>();
		while ((detalle = (ConvTablasExpReal) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ConvTablasExpReal valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKbasetec(), rowNfq, index++);
			writeCell(valor.getKcompania(), rowNfq, index++);
			writeCell(valor.getKnegocio(), rowNfq, index++);
			writeCell(valor.getKriesgo(), rowNfq, index++);
			writeCell(valor.getKsexo(), rowNfq, index++);
			writeCell(valor.getKcateg(), rowNfq, index++);
			writeCell(valor.getKedaddesde(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKfdesde(), rowNfq, index++);
			writeCell(valor.getEdadhasta(), rowNfq, index++);
			writeCell(valor.getFhasta(), rowNfq, index++);
			writeCell(valor.getCtabbase(), rowNfq, index++);
			writeCell(valor.getPfactor1(), rowNfq, index++);
			writeCell(valor.getPfactor2a(), rowNfq, index++);
			writeCell(valor.getPfactor2b(), rowNfq, index++);
			writeCell(valor.getPfactor2c(), rowNfq, index++);
			writeCell(valor.getPfactor2d(), rowNfq, index++);
			writeCell(valor.getPfactor2e(), rowNfq, index++);
			writeCell(valor.getPfactor2f(), rowNfq, index++);
			writeCell(valor.getPfactor2g(), rowNfq, index++);
			writeCell(valor.getPfactor2h(), rowNfq, index++);
			writeCell(valor.getPfactor2i(), rowNfq, index++);
			writeCell(valor.getPfactor2j(), rowNfq, index++);
		}
	}

	
	private static void transformarATE0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktipobt", "kmodalidad", "fini","ffin",
				"kmadaptac"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		AdapTablaExp detalle = null;
		int cont=1;
		List<AdapTablaExp> listaNfq = new ArrayList<AdapTablaExp>();
		while ((detalle = (AdapTablaExp) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			AdapTablaExp valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtipobt(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getFini(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getKmadaptac(), rowNfq, index++);
		}
	}

	
	private static void transformarATA0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kbasetec", "kcompania", "knegocio","kramo",
				"kpinteresdesde", "kmodalidad", "kfiniconversion","pintereshasta",
				"ffinconversion", "ktablaanu"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		AsigTasasAnul detalle = null;
		int cont=1;
		List<AsigTasasAnul> listaNfq = new ArrayList<AsigTasasAnul>();
		while ((detalle = (AsigTasasAnul) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			AsigTasasAnul valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKbasetec(), rowNfq, index++);
			writeCell(valor.getKcompania(), rowNfq, index++);
			writeCell(valor.getKnegocio(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKpinteresdesde(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKfiniconversion(), rowNfq, index++);
			writeCell(valor.getPintereshasta(), rowNfq, index++);
			writeCell(valor.getFfinconversion(), rowNfq, index++);
			writeCell(valor.getKtablaanu(), rowNfq, index++);
		}
	}

	
	private static void transformarAIT0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kcriterioit", "gcriterioit", "kliteral","glink"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ObtInteresTecnico detalle = null;
		int cont=1;
		List<ObtInteresTecnico> listaNfq = new ArrayList<ObtInteresTecnico>();
		while ((detalle = (ObtInteresTecnico) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ObtInteresTecnico valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKcriterioit(), rowNfq, index++);
			writeCell(valor.getGcriterioit(), rowNfq, index++);
			writeCell(valor.getKliteral(), rowNfq, index++);
			writeCell(valor.getGlink(), rowNfq, index++);
		}
	}

	
	private static void transformarX880JI08(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "carterainv", "fecha", "diasplazo","pinteres", 
				"kplazo"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		CurvasTipo656 detalle = null;
		int cont=1;
		List<CurvasTipo656> listaNfq = new ArrayList<CurvasTipo656>();
		while ((detalle = (CurvasTipo656) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			CurvasTipo656 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCarterainv(), rowNfq, index++);
			writeCell(valor.getFecha(), rowNfq, index++);
			writeCell(valor.getDiasplazo(), rowNfq, index++);
			writeCell(valor.getPinteres(), rowNfq, index++);
			writeCell(valor.getKplazo(), rowNfq, index++);
		}
	}

	
	private static void transformarSOL0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kcarteorig", "kramo", "kmodalidad","kgarantia", 
				"kestado", "cindcol","srescate", "cnegocio","cinversion","criesgo",
				"ctipoprovi"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DefinicionesSolvencia2 detalle = null;
		int cont=1;
		List<DefinicionesSolvencia2> listaNfq = new ArrayList<DefinicionesSolvencia2>();
		while ((detalle = (DefinicionesSolvencia2) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DefinicionesSolvencia2 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKcarteorig(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			if (valor.getKestado()){
				writeCell("A", rowNfq, index++);
			} else {
				writeCell("I", rowNfq, index++);
			}
			if (valor.getCindcol()){
				writeCell("I", rowNfq, index++);
			} else {
				writeCell("C", rowNfq, index++);
			}
			if (valor.getSrescate()){
				writeCell("S", rowNfq, index++);
			} else {
				writeCell("N", rowNfq, index++);
			}
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getCinversion(), rowNfq, index++);
			writeCell(valor.getCriesgo(), rowNfq, index++);
			writeCell(valor.getCtipoprovi(), rowNfq, index++);
		}
		
	}

	
	private static void transformarOPG0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "modalidad", "garantia", "periodicidad","vencimiento", 
				"limite", "devengovida","pagovida", "devengofallecimient",
				"pagofallecimiento", "devengoinvalidez","pagoinvalidez", "devengoprimas",
				"pagoprimas", "devengogastos","pagogastos", "devengoanulaciones",
				"pagoanulaciones", "devengocomisiones","pagocomisiones"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		OpcionesGeneracion detalle = null;
		int cont=1;
		List<OpcionesGeneracion> listaNfq = new ArrayList<OpcionesGeneracion>();
		while ((detalle = (OpcionesGeneracion) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			OpcionesGeneracion valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getModalidad(), rowNfq, index++);
			writeCell(valor.getGarantia(), rowNfq, index++);
			writeCell(valor.getPeriodicidad(), rowNfq, index++);
			writeCell(valor.getVencimiento(), rowNfq, index++);
			writeCell(valor.getLimite(), rowNfq, index++);
			writeCell(valor.getDevengovida(), rowNfq, index++);
			writeCell(valor.getPagovida(), rowNfq, index++);
			writeCell(valor.getDevengofallecimient(), rowNfq, index++);
			writeCell(valor.getPagofallecimiento(), rowNfq, index++);
			writeCell(valor.getDevengoinvalidez(), rowNfq, index++);
			writeCell(valor.getPagoinvalidez(), rowNfq, index++);
			writeCell(valor.getDevengoprimas(), rowNfq, index++);
			writeCell(valor.getPagoprimas(), rowNfq, index++);
			writeCell(valor.getDevengogastos(), rowNfq, index++);
			writeCell(valor.getPagogastos(), rowNfq, index++);
			writeCell(valor.getDevengoanulaciones(), rowNfq, index++);
			writeCell(valor.getPagoanulaciones(), rowNfq, index++);
			writeCell(valor.getDevengocomisiones(), rowNfq, index++);
			writeCell(valor.getPagocomisiones(), rowNfq, index++);
		}
	}

	
	private static void transformarIPC0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "finicio", "ffin", "pipcleg","pipcgas"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		IPCGeneralFuturo detalle = null;
		int cont=1;
		List<IPCGeneralFuturo> listaNfq = new ArrayList<IPCGeneralFuturo>();
		while ((detalle = (IPCGeneralFuturo) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			IPCGeneralFuturo valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFinicio(), rowNfq, index++);
			writeCell(valor.getFfin(), rowNfq, index++);
			writeCell(valor.getPipcleg(), rowNfq, index++);
			writeCell(valor.getPipcgas(), rowNfq, index++);
		}
		
	}

	
	private static void transformarFLP0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "modalidad", "garantia", "prestacion","basetecnica",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"nominal","probable","noanulado","actualizado","provi",
				"provNominal","provTerminal"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		FlujosProbables detalle = null;
		int cont=1;
		List<FlujosProbables> listaNfq = new ArrayList<FlujosProbables>();
		while ((detalle = (FlujosProbables) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			FlujosProbables valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getModalidad(), rowNfq, index++);
			writeCell(valor.getGarantia(), rowNfq, index++);
			writeCell(valor.getPrestacion(), rowNfq, index++);
			writeCell(valor.getBasetecnica(), rowNfq, index++);
			writeCell(valor.getVida().getNominal(), rowNfq, index++);
			writeCell(valor.getVida().getProbable(), rowNfq, index++);
			writeCell(valor.getVida().getNoanulado(), rowNfq, index++);
			writeCell(valor.getVida().getActualizado(), rowNfq, index++);
			writeCell(valor.getVida().getProvi(), rowNfq, index++);
			writeCell(valor.getFall().getNominal(), rowNfq, index++);
			writeCell(valor.getFall().getProbable(), rowNfq, index++);
			writeCell(valor.getFall().getNoanulado(), rowNfq, index++);
			writeCell(valor.getFall().getActualizado(), rowNfq, index++);
			writeCell(valor.getFall().getProvi(), rowNfq, index++);
			writeCell(valor.getPrim().getNominal(), rowNfq, index++);
			writeCell(valor.getPrim().getProbable(), rowNfq, index++);
			writeCell(valor.getPrim().getNoanulado(), rowNfq, index++);
			writeCell(valor.getPrim().getActualizado(), rowNfq, index++);
			writeCell(valor.getPrim().getProvi(), rowNfq, index++);
			writeCell(valor.getInva().getNominal(), rowNfq, index++);
			writeCell(valor.getInva().getProbable(), rowNfq, index++);
			writeCell(valor.getInva().getNoanulado(), rowNfq, index++);
			writeCell(valor.getInva().getActualizado(), rowNfq, index++);
			writeCell(valor.getInva().getProvi(), rowNfq, index++);
			writeCell(valor.getGast().getNominal(), rowNfq, index++);
			writeCell(valor.getGast().getProbable(), rowNfq, index++);
			writeCell(valor.getGast().getNoanulado(), rowNfq, index++);
			writeCell(valor.getGast().getActualizado(), rowNfq, index++);
			writeCell(valor.getGast().getProvi(), rowNfq, index++);
			writeCell(valor.getComi().getNominal(), rowNfq, index++);
			writeCell(valor.getComi().getProbable(), rowNfq, index++);
			writeCell(valor.getComi().getNoanulado(), rowNfq, index++);
			writeCell(valor.getComi().getActualizado(), rowNfq, index++);
			writeCell(valor.getComi().getProvi(), rowNfq, index++);
			writeCell(valor.getAnul().getNominal(), rowNfq, index++);
			writeCell(valor.getAnul().getProbable(), rowNfq, index++);
			writeCell(valor.getAnul().getNoanulado(), rowNfq, index++);
			writeCell(valor.getAnul().getActualizado(), rowNfq, index++);
			writeCell(valor.getAnul().getProvi(), rowNfq, index++);
			writeCell(valor.getProvNominal(), rowNfq, index++);
			writeCell(valor.getProvTerminal(), rowNfq, index++);
		}
	}

	
	private static void transformarDPR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "cproceso", "gdescrip", "cdescripabrev","grutadoc",
				"kestado"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DefinicionProcesos detalle = null;
		int cont=1;
		List<DefinicionProcesos> listaNfq = new ArrayList<DefinicionProcesos>();
		while ((detalle = (DefinicionProcesos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DefinicionProcesos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCproceso(), rowNfq, index++);
			writeCell(valor.getGdescrip(), rowNfq, index++);
			writeCell(valor.getCdescripabrev(), rowNfq, index++);
			writeCell(valor.getGrutadoc(), rowNfq, index++);
			
			if (valor.getKestado()){
				writeCell("A", rowNfq, index++);
			} else {
				writeCell("", rowNfq, index++);
			}
		}	
	}

	
	private static void transformarDIP0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kcompania", "kramo", "kmodalidad","kgarantia",
				"kbasetec","kclaveadic","gproceso","cgestespeci","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","celement","ssubproc","celement","ssubproc",
				"celement","ssubproc","kestado"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DisenoProcesos detalle = null;
		int cont=1;
		List<DisenoProcesos> listaNfq = new ArrayList<DisenoProcesos>();
		while ((detalle = (DisenoProcesos) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DisenoProcesos valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKcompania(), rowNfq, index++);
			writeCell(valor.getKramo(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getKbasetec(), rowNfq, index++);
			writeCell(valor.getKclaveadic(), rowNfq, index++);
			writeCell(valor.getGproceso(), rowNfq, index++);
			writeCell(valor.getCgestespeci(), rowNfq, index++);
			
			List<ElementoSubproceso> elem = valor.getElementosSubprocesos();
			
			for (int a = 0;a < elem.size(); a++){
				writeCell(elem.get(a).getCelement(), rowNfq, index++);
				if (elem.get(a).getSsubproc()){
					writeCell("S", rowNfq, index++);
				} else {
					writeCell("N", rowNfq, index++);
				}
			}
			
			if (valor.getKestado()){
				writeCell("A", rowNfq, index++);
			} else {
				writeCell("", rowNfq, index++);
			}
		}	
		
	}

	
	private static void transformarAUX0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kcarteorig", "cnegocio", "kmodalidad","kgarantia",
				"cidentivariab","gvariable","gvalor"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DefinicionesAuxiliares detalle = null;
		int cont=1;
		List<DefinicionesAuxiliares> listaNfq = new ArrayList<DefinicionesAuxiliares>();
		while ((detalle = (DefinicionesAuxiliares) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DefinicionesAuxiliares valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKcarteorig(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getCidentivariab(), rowNfq, index++);
			writeCell(valor.getGvariable(), rowNfq, index++);
			writeCell(valor.getGvalor(), rowNfq, index++);
		}	
		
	}

	
	private static void transformarERR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kaplicacion", "kidprograma", "kretorno","cnivel",
				"gdesc","gdesccorta","kliteral"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ErrorSolvencia2 detalle = null;
		int cont=1;
		List<ErrorSolvencia2> listaNfq = new ArrayList<ErrorSolvencia2>();
		while ((detalle = (ErrorSolvencia2) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ErrorSolvencia2 valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKaplicacion(), rowNfq, index++);
			writeCell(valor.getKidprograma(), rowNfq, index++);
			writeCell(valor.getKretorno(), rowNfq, index++);
			writeCell(valor.getCnivel(), rowNfq, index++);
			writeCell(valor.getGdesc(), rowNfq, index++);
			writeCell(valor.getGdesccorta(), rowNfq, index++);
			writeCell(valor.getKliteral(), rowNfq, index++);
		}	
		
	}

	
	private static void transformarVCR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kk1", "kduracion", "porckonst","fechaMod",
				"usuarioMod","fechaAlta","usuarioAlta"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ValoresConstantesRescate detalle = null;
		int cont=1;
		List<ValoresConstantesRescate> listaNfq = new ArrayList<ValoresConstantesRescate>();
		while ((detalle = (ValoresConstantesRescate) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ValoresConstantesRescate valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKk1(), rowNfq, index++);
			writeCell(valor.getKduracion(), rowNfq, index++);
			writeCell(valor.getPorckonst(), rowNfq, index++);
		}	
	}

	
	private static void transformarCPR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kconstante", "descripcion", "fechaMod","usuarioMod",
				"fechaAlta","usuarioAlta"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		DefinicionesConstantesRescates detalle = null;
		int cont=1;
		List<DefinicionesConstantesRescates> listaNfq = new ArrayList<DefinicionesConstantesRescates>();
		while ((detalle = (DefinicionesConstantesRescates) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			DefinicionesConstantesRescates valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKconstante(), rowNfq, index++);
			writeCell(valor.getDescripcion(), rowNfq, index++);
		}	
		
	}

	
	private static void transformarTXP0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktabla", "k2tipotabla", "gcorta","gdeslarga",
				"fecAlta","fecAnula","cUsuario","fecModif"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		CabeceraTablaExperiencia detalle = null;
		int cont=1;
		List<CabeceraTablaExperiencia> listaNfq = new ArrayList<CabeceraTablaExperiencia>();
		while ((detalle = (CabeceraTablaExperiencia) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			CabeceraTablaExperiencia valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtabla(), rowNfq, index++);
			writeCell(valor.getK2tipotabla(), rowNfq, index++);
			writeCell(valor.getGcorta(), rowNfq, index++);
			writeCell(valor.getGdeslarga(), rowNfq, index++);
			writeCell(valor.getFecAlta(), rowNfq, index++);
			writeCell(valor.getFecAnula(), rowNfq, index++);
			writeCell(valor.getcUsuario(), rowNfq, index++);
			writeCell(valor.getFecModif(), rowNfq, index++);
		}	
	}

	
	private static void transformarVTA0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktablaanu", "kfcierre", "kaniosdesde","nanioshasta",
				"pprobanu"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		TasasAnulacion detalle = null;
		int cont=1;
		List<TasasAnulacion> listaNfq = new ArrayList<TasasAnulacion>();
		while ((detalle = (TasasAnulacion) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			TasasAnulacion valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtablaanu(), rowNfq, index++);
			writeCell(valor.getKfcierre(), rowNfq, index++);
			writeCell(valor.getKaniosdesde(), rowNfq, index++);
			writeCell(valor.getNanioshasta(), rowNfq, index++);
			writeCell(valor.getPprobanu(), rowNfq, index++);
		
		}	
		
	}

	
	private static void transformarVMA0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "codTabla", "fecCierre", "aniosDesde","probabAnul",
				"polizaVigentes"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ValoresAnulacion detalle = null;
		int cont=1;
		List<ValoresAnulacion> listaNfq = new ArrayList<ValoresAnulacion>();
		while ((detalle = (ValoresAnulacion) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ValoresAnulacion valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCodTabla(), rowNfq, index++);
			writeCell(valor.getFecCierre(), rowNfq, index++);
			writeCell(valor.getAniosDesde(), rowNfq, index++);
			writeCell(valor.getProbabAnul(), rowNfq, index++);
			writeCell(valor.getPolizaVigentes(), rowNfq, index++);
		}		
	}

	
	private static void transformarACI0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "kbasetec", "kriesgo", "kriesgorescate","kpagounico",
				"kcarterainv", "kpb","kapbel","kfdesdeconver","fhastaconver", "kcurva",
				"speriodotrans", "kmadaptac"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		AsigCurvaTipo detalle = null;
		int cont=1;
		List<AsigCurvaTipo> listaNfq = new ArrayList<AsigCurvaTipo>();
		while ((detalle = (AsigCurvaTipo) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			AsigCurvaTipo valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKbasetec(), rowNfq, index++);
			writeCell(valor.getKriesgo(), rowNfq, index++);
			writeCell(valor.getKriesgorescate(), rowNfq, index++);
			writeCell(valor.getKpagounico(), rowNfq, index++);
			writeCell(valor.getKcarterainv(), rowNfq, index++);
			writeCell(valor.getKpb(), rowNfq, index++);
			writeCell(valor.getKapbel(), rowNfq, index++);
			writeCell(valor.getKfdesdeconver(), rowNfq, index++);
			writeCell(valor.getFhastaconver(), rowNfq, index++);
			writeCell(valor.getKcurva(), rowNfq, index++);
			writeCell(valor.getSperiodotrans(), rowNfq, index++);
			writeCell(valor.getKmadaptac(), rowNfq, index++);
		
		}	
	}

	
	private static void transformarVCI0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "codCurvaTipos", "fecEfecCurva", "diasPlazo","porcentajeInteres"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		ValoresCurvaTipo detalle = null;
		int cont=1;
		List<ValoresCurvaTipo> listaNfq = new ArrayList<ValoresCurvaTipo>();
		while ((detalle = (ValoresCurvaTipo) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			ValoresCurvaTipo valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getCodCurvaTipos(), rowNfq, index++);
			writeCell(valor.getFecEfecCurva(), rowNfq, index++);
			writeCell(valor.getDiasPlazo(), rowNfq, index++);
			writeCell(valor.getPorcentajeInteres(), rowNfq, index++);
		
		}	
	}

	
	private static void transformarVTR0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2,
			XSSFCellStyle datoStyleImparNfq2) {

		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "fecCierre", "kbasetec", "compania","cnegocio",
				"riesgoActuarial", "sexo", "categoria","edadFija","kmodalidad",
				"tablaBase","generacion","q1","q2",
				"q3","q4","q5","q6","q7","q8","q9","q10","q11","q12","q13","q14",
				"q15","q16","q17","q18","q19","q20","q21","q22","q23","q24","q25",
				"q26","q27","q28","q29","q30","q31","q32","q33","q34","q35","q36",
				"q37","q38","q39","q40","q41","q42","q43","q44","q45","q46","q47",
				"q48","q49","q50","q51","q52","q53","q54","q55","q56","q57","q58",
				"q59","q60","q61","q62","q63","q64","q65","q66","q67","q68","q69",
				"q70","q71","q72","q73","q74","q75","q76","q77","q78","q79","q80",
				"q81","q82","q83","q84","q85","q86","q87","q88","q89","q90","q91",
				"q92","q93","q94","q95","q96","q97","q98","q99","q100","q101",
				"q102","q103","q104","q105","q106","q107","q108","q109","q110",
				"q111","q112","q113","q114","q115","q116","q117","q118","q119",
				"q120","q121","q122","q123","q124","q125","q126","q127","q128",
				"q129","q130","l1","l2","l3","l4","l5","l6","l7","l8","l9","l10",
				"l11","l12","l13","l14","l15","l16","l17","l18","l19","l20","l21",
				"l22","l23","l24","l25","l26","l27","l28","l29","l30","l31","l32",
				"l33","l34","l35","l36","l37","l38","l39","l40","l41","l42","l43",
				"l44","l45","l46","l47","l48","l49","l50","l51","l52","l53","l54",
				"l55","l56","l57","l58","l59","l60","l61","l62","l63","l64","l65",
				"l66","l67","l68","l69","l70","l71","l72","l73","l74","l75","l76",
				"l77","l78","l79","l80","l81","l82","l83","l84","l85","l86","l87",
				"l88","l89","l90","l91","l92","l93","l94","l95","l96","l97","l98",
				"l99","l100","l101","l102","l103","l104","l105","l106","l107","l108",
				"l109","l110","l111","l112","l113","l114","l115","l116","l117","l118",
				"l119","l120","l121","l122","l123","l124","l125","l126","l127","l128",
				"l129","l130"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		TablasExperienciaReales detalle = null;
		int cont=1;
		List<TablasExperienciaReales> listaNfq = new ArrayList<TablasExperienciaReales>();
		while ((detalle = (TablasExperienciaReales) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			TablasExperienciaReales valor = listaNfq.get(i);
			
			if (i%100 == 0){
				System.out.println(i + " registros tratados.");
			}
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getFecCierre(), rowNfq, index++);
			writeCell(valor.getKbasetec(), rowNfq, index++);
			writeCell(valor.getCompania(), rowNfq, index++);
			writeCell(valor.getCnegocio(), rowNfq, index++);
			writeCell(valor.getRiesgoActuarial(), rowNfq, index++);
			writeCell(valor.getSexo(), rowNfq, index++);
			writeCell(valor.getCategoria(), rowNfq, index++);
			writeCell(valor.getEdadFija(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getTablaBase(), rowNfq, index++);
			writeCell(valor.getGeneracion(), rowNfq, index++);
			for (int a = 0; a < valor.getQ().size(); a++){
				writeCell(valor.getQ().get(a), rowNfq, index++);
			}
			for (int a = 0; a < valor.getL().size(); a++){
				writeCell(valor.getL().get(a), rowNfq, index++);
			}
		}	
	}

	
	private static void transformarCTM0(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2, XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ktabla", "k2tipovalor", "kanacimiento",
				"kinteres", "ksobremort", "ksobreries","gvaloreslen", "gvalor1", 
				"gvalor2", "gvalor3", "gvalor4", "gvalor5", "gvalor6", "gvalor7", 
				"gvalor8", "gvalor9", "gvalor10", "gvalor11", "gvalor12", "gvalor13", 
				"gvalor14", "gvalor15", "gvalor16", "gvalor17", "gvalor18", "gvalor19", 
				"gvalor20", "gvalor21", "gvalor22", "gvalor23", "gvalor24", "gvalor25", 
				"gvalor26", "gvalor27", "gvalor28", "gvalor29", "gvalor30", "gvalor31", 
				"gvalor32", "gvalor33", "gvalor34", "gvalor35", "gvalor36", "gvalor37", 
				"gvalor38", "gvalor39", "gvalor40", "gvalor41", "gvalor42", "gvalor43", 
				"gvalor44", "gvalor45", "gvalor46", "gvalor47", "gvalor48", "gvalor49",
				"gvalor50", "gvalor51", "gvalor52", "gvalor53", "gvalor54", "gvalor55", 
				"gvalor56", "gvalor57", "gvalor58", "gvalor59", "gvalor60", "gvalor61", 
				"gvalor62", "gvalor63", "gvalor64", "gvalor65", "gvalor66", "gvalor67", 
				"gvalor68", "gvalor69", "gvalor70", "gvalor71", "gvalor72", "gvalor73", 
				"gvalor74", "gvalor75", "gvalor76", "gvalor77", "gvalor78", "gvalor79", 
				"gvalor80", "gvalor81", "gvalor82", "gvalor83", "gvalor84", "gvalor85", 
				"gvalor86", "gvalor87", "gvalor86", "gvalor89", "gvalor90", "gvalor91", 
				"gvalor92", "gvalor98", "gvalor92", "gvalor95", "gvalor96", "gvalor97", 
				"gvalor98", "gvalor99", "gvalor100", "gvalor101", "gvalor102", "gvalor103", 
				"gvalor104", "gvalor105", "gvalor106", "gvalor107", "gvalor108", "gvalor109", 
				"gvalor110", "gvalor111", "gvalor112", "gvalor113", "gvalor114", "gvalor115", 
				"gvalor116", "gvalor117", "gvalor118", "gvalor119", "gvalor8120", "gvalor121", 
				"gvalor122", "gvalor123", "gvalor124", "gvalor125", "gvalor126", "gvalor127", 
				"gvalor128", "gvalor129", "gvalor130",
				"fanulacion", "cusuario", "fmodificacion"};
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		TablaExperiencia detalle = null;
		int cont=1;
		List<TablaExperiencia> listaNfq = new ArrayList<TablaExperiencia>();
		while ((detalle = (TablaExperiencia) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			TablaExperiencia valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getKtabla(), rowNfq, index++);
			writeCell(valor.getK2tipovalor(), rowNfq, index++);
			writeCell(valor.getKanacimiento(), rowNfq, index++);
			writeCell(valor.getKinteres(), rowNfq, index++);
			writeCell(valor.getKsobremort(), rowNfq, index++);
			writeCell(valor.getKsobreries(), rowNfq, index++);
			writeCell(valor.getGvaloreslen(), rowNfq, index++);
			for (int a = 0; a < valor.getGvalor().size(); a++){
				writeCell(valor.getGvalor().get(a), rowNfq, index++);
			}
			writeCell(valor.getFanulacion(), rowNfq, index++);
			writeCell(valor.getCusuario(), rowNfq, index++);
			writeCell(valor.getFmodificacion(), rowNfq, index++);
		}	
	}

	
	private static void transformarRW06646(XSSFCellStyle cabeceraStyle, XSSFCellStyle datoStyleParNfq2, XSSFCellStyle datoStyleImparNfq2) {
		
		XSSFRow rowNfqCab =  sheetNfq.createRow(0);
		
		String[] textosCabeceras = { "ntabla", "kmodalidad", "kgarantia",
				"fefecfin", "kedad1", "kedad2","nmeshasta", "gspaces1", "nregistro",
				"porgamma", "ecaphasta", "ecaphastaAgra", "ecapdesdeNor",
				"ecapdesdeAgra", "cmoneda" };
		
		sheetNfq.setDefaultColumnWidth(20);
		for (int i = 0; i < textosCabeceras.length; i++) {
			writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
		}

		LimitesCapital detalle = null;
		int cont=1;
		List<LimitesCapital> listaNfq = new ArrayList<LimitesCapital>();
		while ((detalle = (LimitesCapital) readerNfq.read()) != null) {
			listaNfq.add(detalle);
		}
		
		for (int i =0; i<listaNfq.size(); i++) {
			
			LimitesCapital valor = listaNfq.get(i);
			
			if(i % 2 == 0){
				datoStyleNfq = datoStyleParNfq;	
			} else {
				datoStyleNfq = datoStyleImparNfq;	
			}
			
			XSSFRow rowNfq = sheetNfq.createRow(cont);
			cont ++;
			
			int index = 0;
			
			writeCell(valor.getNtabla(), rowNfq, index++);
			writeCell(valor.getKmodalidad(), rowNfq, index++);
			writeCell(valor.getKgarantia(), rowNfq, index++);
			writeCell(valor.getFefecfin(), rowNfq, index++);
			writeCell(valor.getKedad1(), rowNfq, index++);
			writeCell(valor.getKedad2(), rowNfq, index++);
			writeCell(valor.getNmeshasta(), rowNfq, index++);
			writeCell(valor.getGspaces1(), rowNfq, index++);
			writeCell(valor.getNregistro(), rowNfq, index++);
			writeCell(valor.getPorgamma(), rowNfq, index++);
			writeCell(valor.getEcaphasta(), rowNfq, index++);
			writeCell(valor.getEcaphastaAgra(), rowNfq, index++);
			writeCell(valor.getEcapdesdeNor(), rowNfq, index++);
			writeCell(valor.getEcapdesdeAgra(), rowNfq, index++);
			writeCell(valor.getCmoneda(), rowNfq, index++);
		}	
	}

	
	private static void writeCell(String valor, XSSFRow rowNfq, int cellIndex) {
		writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
	}
	
	private static void writeCell(Timestamp valor, XSSFRow rowNfq, int cellIndex) {
		writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
	}
	
	private static void writeCell(Integer valor, XSSFRow rowNfq, int cellIndex) {
		writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
	}
	
	private static void writeCell(BigDecimal valor, XSSFRow rowNfq, int cellIndex) {
		String value = valor.toString().replaceFirst ("^0*", "");
		value = value.replace(".", ",");
		if (value.equals("")){
			value = "0";
		}
		if (value.substring(0, 1).equals(",")){
			value = value.replace(",", "0,");
		}
		writeCellBD(rowNfq.createCell(cellIndex),datoStyleNfq,value);
	}
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,String cellValue){
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}

	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,Timestamp cellValue){
		String cellDate = null; 
		cell.setCellStyle(cellStyle);
		 if(cellValue != null){
			cellDate = format.format(cellValue);
			cell.setCellValue(cellDate);	 
		 }
	}
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,Integer cellValue){
		cell.setCellStyle(cellStyle);
		if (null == cellValue) {
			cell.setCellValue("");
		} else {
			cell.setCellValue(cellValue);
		}
	}
	
	private static void writeCellBD(XSSFCell cell,XSSFCellStyle cellStyle,String cellValue){
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}
	
}
