package es.mapfre.solvencia.utils.pruebas;

import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class ResultComparatorv2 {
	private static final String D_WORKBOOK_XLSX = "d:/workbook.xlsx";

	private static final Logger log = LoggerFactory.getLogger(ResultComparatorv2.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	private static final String ficheroIndra = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/1/BRUTOSIND/2013120000_SOLV_SO03_GP01_110_20131231_I_1_BTI_FLUJOSDET_.txt";
	private static final String ficheroMapfre = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/CTEC/MOD186/resultado186.txt";
	
//	private static final String ficheroIndra = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/20/BRUTOSIND/2013120000_SOLV_SO03_GP01_110_20131231_I_20_BTI_FLUJOSDET_.txt";
//	private static final String ficheroMapfre = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/resultado852.txt";

	private static XSSFCellStyle datoStyleParIndra;
	private static XSSFCellStyle datoStyleImparIndra;
	private static XSSFCellStyle datoStyleParMapfre;
	private static XSSFCellStyle datoStyleImparMapfre;	
	
	private static XSSFCellStyle distintosStyleParIndra;
	private static XSSFCellStyle distintosStyleParMapfre;
	private static XSSFCellStyle distintosStyleImparIndra;
	private static XSSFCellStyle distintosStyleImparMapfre;
	
	private static XSSFCellStyle cambioBloqueStyle;
	
	private static XSSFCellStyle datoStyleIndra;
	private static XSSFCellStyle distintosStyleIndra;
	private static XSSFCellStyle datoStyleMapfre;
	private static XSSFCellStyle distintosStyleMapfre;
	
	public static void main(String[] args) {


			BeanIOReader readerIndra, readerMapfre;
			DetalleCorriente detalle = null;
			try {
				readerIndra = new BeanIOReader(BEANIO_CONFIG_XML, ficheroIndra, "detalle-corriente");
				readerMapfre = new BeanIOReader(BEANIO_CONFIG_XML, ficheroMapfre, "detalle-corriente");
				
				
				// create a new file
				FileOutputStream out = new FileOutputStream(D_WORKBOOK_XLSX);
				// create a new workbook
				XSSFWorkbook workBook = new XSSFWorkbook();
				XSSFSheet sheet = workBook.createSheet();
				
				XSSFRow row =  sheet.createRow(0);
				
				XSSFCellStyle cabeceraStyle = workBook.createCellStyle();
				
				cabeceraStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
				
				cabeceraStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				cabeceraStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				cabeceraStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
				
//			String[] textosCabeceras = { "CNEGOCIO", "CCANAL", "CCARTERA",
//					"FCIERRE", "BT", "KMODALIDAD", "KPOLIZA", "KSUBPOLIZA",
//					"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
//					"KPRESTACION", "KAJUSTE", "CTIPOAPORT", "FDESDE", "FHASTA",
//					"FDEVENGOVIDA", "FPAGOVIDA", "SIG_CUANTIAVIDA",
//					"CUANTIAVIDA", "FPB_VIDA", "SIG_FPVIDA", "FPVIDA", "ATC",
//					"SIG_FPNAVIDA", "FPNAVIDA", "ACTFIN", "SIG_FACTVIDA",
//					"FACTVIDA", "SIG_COLAVIDA", "COLAVIDA", "FDEVENGOFALL",
//					"FPAGOFALL", "SIG_CUANTIAFALL", "CUANTIAFALL", "FPB_FALL",
//					"SIG_FPFALL", "FPFALL", "ATC", "SIG_FPNAFALL", "FPNAFALL",
//					"ACTFIN", "SIG_FACTFALL", "FACTFALL", "SIG_COLAFALL",
//					"COLAFALL", "FDEVENGOCOMPL", "FPAGOCOMPL",
//					"SIG_CUANTIACOMPL", "CUANTIACOMPL", "FPB_COMPL",
//					"SIG_FPCOMPL", "FPCOMPL", "ATC", "SIG_FPNACOMPL",
//					"FPNACOMPL", "ACTFIN", "SIG_FACTCOMPL", "FACTCOMPL",
//					"SIG_COLACOMPL", "COLACOMPL", "FDEVENGOGTO", "FPAGOGTO",
//					"SIG_CUANTIAGTO", "CUANTIAGTO", "FPB_GTO", "SIG_FPGTO",
//					"FPGTO", "ATC", "SIG_FPNAGTO", "FPNAGTO", "ACTFIN",
//					"SIG_FACTGTO", "FACTGTO", "SIG_COLAGTO", "COLAGTO",
//					"FDEVENGOCOMI", "FPAGOCOMI", "SIG_CUANTIACOMI",
//					"CUANTIACOMI", "FPB_COMI", "SIG_FPCOMI", "FPCOMI", "ATC",
//					"SIG_FPNACOMI", "FPNACOMI", "ACTFIN", "SIG_FACTCOMI",
//					"FACTCOMI", "SIG_COLACOMI", "COLACOMI", "FDEVENGORTE",
//					"FPAGORTE", "SIG_CUANTIARTE", "CUANTIARTE", "FPB_RTE",
//					"SIG_FPRTE", "FPRTE", "APTOTC", "SIG_FPANRTE", "FPANRTE",
//					"ACTFIN", "SIG_FACTRTE", "FACTRTE", "SIG_COLARTE",
//					"COLARTE", "FDEVENGOPRIM", "FPAGOPRIM", "SIG_CUANTIAPRIM",
//					"CUANTIAPRIM", "FPB_PRIM", "SIG_FPPRIM", "FPPRIM", "ATC",
//					"SIG_FPNAPRIM", "FPNAPRIM", "ACTFIN", "SIG_FACTPRIM",
//					"FACTPRIM", "SIG_COLAPRIM", "COLAPRIM", "SIG_SUMFPROB",
//					"SUMFPROB", "SIG_SUMFPROBTANUL", "SUMFPROBTANUL",
//					"SIG_SUMPROVISION", "SUMPROVISION", "SIG_SUMCOLA",
//					"SUMCOLA", "SIG_PROVBTIPROY", "PROVBTIPROY",
//					"SIG_TERMINAL_ ANTERIOR", "TERMINAL ANTERIOR",
//					"SIG_TERMINAL_POSTERIOR", "TERMINAL POSTERIOR" };
				
				
			String[] textosCabeceras = { "CNEGOCIO", "CCANAL", "CCARTERA",
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
			
				sheet.setDefaultColumnWidth(20);
				for (int i = 0; i < textosCabeceras.length; i++) {
					writeCell(row.createCell(i),cabeceraStyle,textosCabeceras[i]);
					
				}
				
				
				datoStyleParIndra = workBook.createCellStyle();
				datoStyleParIndra.setBorderTop(CellStyle.BORDER_MEDIUM);
				datoStyleParIndra.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleParIndra.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleParIndra.setAlignment(CellStyle.ALIGN_RIGHT);
				
				datoStyleImparIndra = workBook.createCellStyle();
				datoStyleImparIndra.setBorderTop(CellStyle.BORDER_MEDIUM);
				datoStyleImparIndra.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleImparIndra.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleImparIndra.setAlignment(CellStyle.ALIGN_RIGHT);
				datoStyleImparIndra.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				datoStyleImparIndra.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				
				datoStyleParMapfre = workBook.createCellStyle();
				datoStyleParMapfre.setBorderBottom(CellStyle.BORDER_MEDIUM);
				datoStyleParMapfre.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleParMapfre.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleParMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
				
				datoStyleImparMapfre = workBook.createCellStyle();
				datoStyleImparMapfre.setBorderBottom(CellStyle.BORDER_MEDIUM);
				datoStyleImparMapfre.setBorderLeft(CellStyle.BORDER_THIN);
				datoStyleImparMapfre.setBorderRight(CellStyle.BORDER_THIN);
				datoStyleImparMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
				datoStyleImparMapfre.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				datoStyleImparMapfre.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				
				distintosStyleParIndra = workBook.createCellStyle();
				distintosStyleParIndra.setBorderTop(CellStyle.BORDER_MEDIUM);
				distintosStyleParIndra.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleParIndra.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleParIndra.setAlignment(CellStyle.ALIGN_RIGHT);
				XSSFFont fail = workBook.createFont();
				fail.setColor(new XSSFColor(Color.red));
				distintosStyleParIndra.setFont(fail);
				
				distintosStyleImparIndra = workBook.createCellStyle();
				distintosStyleImparIndra.setBorderTop(CellStyle.BORDER_MEDIUM);
				distintosStyleImparIndra.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleImparIndra.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleImparIndra.setAlignment(CellStyle.ALIGN_RIGHT);
				distintosStyleImparIndra.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				distintosStyleImparIndra.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				distintosStyleImparIndra.setFont(fail);
				
				distintosStyleParMapfre = workBook.createCellStyle();
				distintosStyleParMapfre.setBorderBottom(CellStyle.BORDER_MEDIUM);
				distintosStyleParMapfre.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleParMapfre.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleParMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
				distintosStyleParMapfre.setFont(fail);
				
				distintosStyleImparMapfre = workBook.createCellStyle();
				distintosStyleImparMapfre.setBorderBottom(CellStyle.BORDER_MEDIUM);
				distintosStyleImparMapfre.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleImparMapfre.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleImparMapfre.setAlignment(CellStyle.ALIGN_RIGHT);
				distintosStyleImparMapfre.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				distintosStyleImparMapfre.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				distintosStyleImparMapfre.setFont(fail);
				
				
				cambioBloqueStyle = workBook.createCellStyle();
				
				cambioBloqueStyle.setBorderBottom(CellStyle.BORDER_THIN);
				cambioBloqueStyle.setBorderTop(CellStyle.BORDER_THIN);
				cambioBloqueStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
				cambioBloqueStyle.setBorderRight(CellStyle.BORDER_THIN);
				cambioBloqueStyle.setAlignment(CellStyle.ALIGN_RIGHT);
				
				int cont=1;
				List<DetalleCorriente> listaIndra = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerIndra.read()) != null) {
					listaIndra.add(detalle);
				}
				
				List<DetalleCorriente> listaMapfre = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerMapfre.read()) != null) {
					listaMapfre.add(detalle);
				}
				
				
				
				if(listaMapfre.size() != listaIndra.size()){
					log.warn("NO COINCIDEN EL NUMERO DE PERIODOS, INDRA = {} , MAPFRE = {}",listaIndra.size(),listaMapfre.size());
				}
				
				
				for (int i =0; i<listaIndra.size(); i++) {
					
					if(listaMapfre.size() == i){
						break;
					}
					
					DetalleCorriente valor = listaIndra.get(i);
					DetalleCorriente valorMapfre = listaMapfre.get(i);
					
					
					
					if(i % 2 == 0){
						datoStyleIndra = datoStyleParIndra;	
						distintosStyleIndra = distintosStyleParIndra;
						datoStyleMapfre = datoStyleParMapfre;	
						distintosStyleMapfre = distintosStyleParMapfre;
					} else {
						datoStyleIndra = datoStyleImparIndra;	
						distintosStyleIndra = distintosStyleImparIndra;
						datoStyleMapfre = datoStyleImparMapfre;	
						distintosStyleMapfre = distintosStyleImparMapfre;
					}
					
					if(!valor.equals(valorMapfre)){
						XSSFRow rowIndra = sheet.createRow(cont);
						cont ++;
						XSSFRow rowMapfre = sheet.createRow(cont);
						cont ++;
						
						int index = 0;
						
						writeCell(valor.getCnegocio(), valorMapfre.getCnegocio(), rowIndra, rowMapfre,index++);
						writeCell(valor.getCcanal(), valorMapfre.getCcanal(), rowIndra, rowMapfre,index++);
						writeCell(valor.getCcartera(), valorMapfre.getCcartera(), rowIndra, rowMapfre,index++);
						writeCell(valor.getFcierre(), valorMapfre.getFcierre(), rowIndra, rowMapfre,index++);
						writeCell(valor.getBt(), valorMapfre.getBt(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKmodalidad(), valorMapfre.getKmodalidad(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKpoliza().toString(), valorMapfre.getKpoliza().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKsubpoliza(), valorMapfre.getKsubpoliza(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKcertificado(), valorMapfre.getKcertificado(), rowIndra, rowMapfre,index++);
						writeCell(valor.getNsuscri(), valorMapfre.getNsuscri(), rowIndra, rowMapfre,index++);
						writeCell(valor.getNorden(), valorMapfre.getNorden(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKgarantia(), valorMapfre.getKgarantia(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKprestacion(), valorMapfre.getKprestacion(), rowIndra, rowMapfre,index++);
						writeCell(valor.getKajuste(), valorMapfre.getKajuste(), rowIndra, rowMapfre,index++);
						writeCell(valor.getCtipoaport(), valorMapfre.getCtipoaport(), rowIndra, rowMapfre,index++);
						writeCell(valor.getFechaDesde(), valorMapfre.getFechaDesde(), rowIndra, rowMapfre,index++);
						writeCell(valor.getFechaHasta(), valorMapfre.getFechaHasta(), rowIndra, rowMapfre,index++);
						
						index = writeBloque(valor.getBloqueVida(), valorMapfre.getBloqueVida(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloqueFall(), valorMapfre.getBloqueFall(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloqueCompl(), valorMapfre.getBloqueCompl(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloqueGto(), valorMapfre.getBloqueGto(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloqueComi(), valorMapfre.getBloqueComi(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloqueRte(), valorMapfre.getBloqueRte(), rowIndra, rowMapfre,index);
						index = writeBloque(valor.getBloquePrim(), valorMapfre.getBloquePrim(), rowIndra, rowMapfre,index);
						
						writeCell(valor.getTotalFlujoProyeccion().getSumfprob().toString(), valorMapfre.getTotalFlujoProyeccion().getSumfprob().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getSumfprobtanul().toString(), valorMapfre.getTotalFlujoProyeccion().getSumfprobtanul().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getSumprovision().toString(), valorMapfre.getTotalFlujoProyeccion().getSumprovision().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getSumcola().toString(), valorMapfre.getTotalFlujoProyeccion().getSumcola().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getProvbtiproy().toString(), valorMapfre.getTotalFlujoProyeccion().getProvbtiproy().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getTerminalAnterior().toString(), valorMapfre.getTotalFlujoProyeccion().getTerminalAnterior().toString(), rowIndra, rowMapfre,index++);
						writeCell(valor.getTotalFlujoProyeccion().getTerminalPosterior().toString(), valorMapfre.getTotalFlujoProyeccion().getTerminalPosterior().toString(), rowIndra, rowMapfre,index++);
						
					}
					
					
					
				}
				
				workBook.write(out);
				out.close();
				
			} catch (IOException e) {
				//e.printStackTrace();
				System.exit(-1);
			}


		log.info("Finalizado");
	}



	private static int writeBloque(BloqueCorriente valor, BloqueCorriente valorMapfre, XSSFRow rowIndra, XSSFRow rowMapfre, int index) {
		writeCell(valor.getFechaDevengo(), valorMapfre.getFechaDevengo(), rowIndra, rowMapfre,index++);
		writeCell(valor.getFechaPago(), valorMapfre.getFechaPago(), rowIndra, rowMapfre,index++);
		writeCell(valor.getImpFlujoNominal().toString(), valorMapfre.getImpFlujoNominal().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getFpbProbable().toString(), valorMapfre.getFpbProbable().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getImpFlujoProbable().toString(), valorMapfre.getImpFlujoProbable().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getFpbAtc().toString(), valorMapfre.getFpbAtc().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getImpFlujoNoAnulado().toString(), valorMapfre.getImpFlujoNoAnulado().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getFpbAtcfin().toString(), valorMapfre.getFpbAtcfin().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getImpFlujoActualizado().toString(), valorMapfre.getImpFlujoActualizado().toString(), rowIndra, rowMapfre,index++);
		writeCell(valor.getImpProvi().toString(), valorMapfre.getImpProvi().toString(), rowIndra, rowMapfre,index++);
		
		return index;
	}

	
	
	private static void writeCell(String valor, String valorMapfre, XSSFRow rowIndra, XSSFRow rowMapfre,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowIndra.createCell(cellIndex),datoStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
		} else {
			writeCell(rowIndra.createCell(cellIndex),distintosStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);	
		}
	}
	
	private static void writeCell(Timestamp valor, Timestamp valorMapfre, XSSFRow rowIndra, XSSFRow rowMapfre,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowIndra.createCell(cellIndex),datoStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
		} else {
			writeCell(rowIndra.createCell(cellIndex),distintosStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);	
		}
	}
	
	private static void writeCell(Integer valor, Integer valorMapfre, XSSFRow rowIndra, XSSFRow rowMapfre,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowIndra.createCell(cellIndex),datoStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
		} else {
			writeCell(rowIndra.createCell(cellIndex),distintosStyleIndra,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);	
		}
	}
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,String cellValue){
		 cell.setCellStyle(cellStyle);
		 cell.setCellValue( cellValue);
	}

	
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,Timestamp cellValue){
		 cell.setCellStyle(cellStyle);
		 if(cellValue != null){
			 cell.setCellValue(cellValue.toString());	 
		 }
		 
	}
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,Integer cellValue){
		 cell.setCellStyle(cellStyle);
		 cell.setCellValue( cellValue);
	}

}
