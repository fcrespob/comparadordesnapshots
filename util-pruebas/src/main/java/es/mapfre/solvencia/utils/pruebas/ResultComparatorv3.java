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

public class ResultComparatorv3 {
	private static final String D_WORKBOOK_XLSX = "C:/Users/sergio.becerro/CIERRES/201704/1/BRUTOSIND/workbook.xlsx";

	private static final Logger log = LoggerFactory.getLogger(ResultComparatorv3.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	private static final String ficheroNfq = "C:/Users/sergio.becerro/CIERRES/201704/1/BRUTOSIND/SOLV_2017040000_SO05_GP01_140_20170331_I_1_BTI_FLUJOSDET_.txt";
	private static final String ficheroMapfre = "C:/Users/sergio.becerro/CIERRES/201704/1/BRUTOSIND/resultado186.txt";
	
	private static XSSFCellStyle datoStyleParNfq;
	private static XSSFCellStyle datoStyleImparNfq;
	private static XSSFCellStyle datoStyleParMapfre;
	private static XSSFCellStyle datoStyleImparMapfre;	
	
	private static XSSFCellStyle distintosStyleParNfq;
	private static XSSFCellStyle distintosStyleParMapfre;
	private static XSSFCellStyle distintosStyleImparNfq;
	private static XSSFCellStyle distintosStyleImparMapfre;
	
	private static XSSFCellStyle cambioBloqueStyle;
	
	private static XSSFCellStyle datoStyleNfq;
	private static XSSFCellStyle distintosStyleNfq;
	private static XSSFCellStyle datoStyleMapfre;
	private static XSSFCellStyle distintosStyleMapfre;
	
	public static void main(String[] args) {


			BeanIOReader readerNfq, readerMapfre;
			DetalleCorriente detalle = null;
			try {
				readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, "detalle-corriente");
				readerMapfre = new BeanIOReader(BEANIO_CONFIG_XML, ficheroMapfre, "detalle-corriente");
				
				
				// create a new file
				FileOutputStream out = new FileOutputStream(D_WORKBOOK_XLSX);
				// create a new workbook
				XSSFWorkbook workBook = new XSSFWorkbook();
				XSSFSheet sheetNfq = workBook.createSheet("Nfoque");
				XSSFSheet sheetMapfre = workBook.createSheet("Mapfre");
				XSSFSheet sheetResul = workBook.createSheet("Resultado");
				
				XSSFRow rowNfqCab =  sheetNfq.createRow(0);
				XSSFRow rowMapfreCab =  sheetMapfre.createRow(0);
				XSSFRow rowResulCab =  sheetResul.createRow(0);
				
				XSSFCellStyle cabeceraStyle = workBook.createCellStyle();
				
				cabeceraStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
				cabeceraStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
				
				cabeceraStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
				cabeceraStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				cabeceraStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
				
			String[] textosCabeceras = { "CNEGOCIO", "CCANAL", "CCARTERA",
					"FCIERRE", "BT", "KRAMO","KMODALIDAD", "KPOLIZA", "KSUBPOLIZA",
					"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
					"KPRESTACION", "KAJUSTE", "CTIPOAPORT", "INTFECCALC",
					"FSUSCRI","KCARTERAINV","GAPACT" ,"FDESDE", "FHASTA",
					//VIDA
					"FDEVENGOVIDA", "FPAGOVIDA",  "IMPFLUJONOMINALVIDA",
					"FPPROBABLEVIDA","IMPFLUJOPROBABLEVIDA", "FBPACTVIDA",
					"IMPFLUJONOANUVIDA", "FBPACTFINVIDA",
					"IMPFLUJOACTVIDA", "IMPPROVIVIDA",
					//FALLECIMIENTO
					"FDEVENGOFALL", "FPAGOFALL",  "IMPFLUJONOMINALFALL",
					"FPPROBABLEFALL","IMPFLUJOPROBABLEFALL", "FBPACTFALL",
					"IMPFLUJONOANUFALL", "FBPACTFINFALL",
					"IMPFLUJOACTFALL", "IMPPROVIFALL",
					//COMPLEMENTARIO
					"FDEVENGOCOMP", "FPAGOCOMP",  "IMPFLUJONOMINALCOMP",
					"FPPROBABLECOMP","IMPFLUJOPROBABLECOMP", "FBPACTCOMP",
					"IMPFLUJONOANUCOMP", "FBPACTFINCOMP",
					"IMPFLUJOACTCOMP", "IMPPROVICOMP",
					//GASTO
					"FDEVENGOGTO", "FPAGOGTO",  "IMPFLUJONOMINALGTO",
					"FPPROBABLEGTO","IMPFLUJOPROBABLEGTO", "FBPACTGTO",
					"IMPFLUJONOANUGTO", "FBPACTFINGTO",
					"IMPFLUJOACTGTO", "IMPPROVIGTO",
					//COMISION
					"FDEVENGOCOMI", "FPAGOCOMI",  "IMPFLUJONOMINALCOMI",
					"FPPROBABLECOMI","IMPFLUJOPROBABLECOMI", "FBPACTCOMI",
					"IMPFLUJONOANUCOMI", "FBPACTFINCOMI",
					"IMPFLUJOACTCOMI", "IMPPROVICOMI",
					//RESCATE
					"FDEVENGORTE", "FPAGORTE", "IMPFLUJONOMINALRTE",
					"FPPROBABLERTE","IMPFLUJOPROBABLERTE", "FBPACTRTE",
					"IMPFLUJONOANURTE", "FBPACTFINRTE",
					"IMPFLUJOACTRTE", "IMPPROVIRTE",
					//PRIMA
					"FDEVENGOPRIMA", "FPAGOPRIMA", "IMPFLUJONOMINALPRIMA",
					"FPPROBABLEPRIMA","IMPFLUJOPROBABLEPRIMA", "FBPACTPRIMA",
					"IMPFLUJONOANUPRIMA", "FBPACTFINPRIMA",
					"IMPFLUJOACTPRIMA", "IMPPROVIPRIMA",
					//FLUJO PROYECCION
					"SUMFPROB", "SUMFPROBTANUL","SUMPROVISION", 
					"SUMCOLA", "PROVBTIPROY","TERMINAL ANTERIOR",  
					"TERMINAL POSTERIOR"  };
			
				sheetNfq.setDefaultColumnWidth(20);
				sheetMapfre.setDefaultColumnWidth(20);
				sheetResul.setDefaultColumnWidth(20);
				for (int i = 0; i < textosCabeceras.length; i++) {
					writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
					writeCell(rowMapfreCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
					writeCell(rowResulCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
				}
				
				
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
				
				distintosStyleParNfq = workBook.createCellStyle();
				distintosStyleParNfq.setBorderTop(CellStyle.BORDER_MEDIUM);
				distintosStyleParNfq.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleParNfq.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleParNfq.setAlignment(CellStyle.ALIGN_RIGHT);
				XSSFFont fail = workBook.createFont();
				fail.setColor(new XSSFColor(Color.red));
				distintosStyleParNfq.setFont(fail);
				
				distintosStyleImparNfq = workBook.createCellStyle();
				distintosStyleImparNfq.setBorderTop(CellStyle.BORDER_MEDIUM);
				distintosStyleImparNfq.setBorderLeft(CellStyle.BORDER_THIN);
				distintosStyleImparNfq.setBorderRight(CellStyle.BORDER_THIN);
				distintosStyleImparNfq.setAlignment(CellStyle.ALIGN_RIGHT);
				distintosStyleImparNfq.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
				distintosStyleImparNfq.setFillForegroundColor(new XSSFColor(Color.LIGHT_GRAY));
				distintosStyleImparNfq.setFont(fail);
				
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
				List<DetalleCorriente> listaNfq = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerNfq.read()) != null) {
					listaNfq.add(detalle);
				}
				
				List<DetalleCorriente> listaMapfre = new ArrayList<DetalleCorriente>();
				while ((detalle = (DetalleCorriente) readerMapfre.read()) != null) {
					listaMapfre.add(detalle);
				}
				
				
				
				if(listaMapfre.size() != listaNfq.size()){
					log.warn("NO COINCIDEN EL NUMERO DE PERIODOS, Nfq = {} , MAPFRE = {}",listaNfq.size(),listaMapfre.size());
				}
				
				
				for (int i =0; i<listaNfq.size(); i++) {
					
					if(listaMapfre.size() == i){
						break;
					}
					
					DetalleCorriente valor = listaNfq.get(i);
					DetalleCorriente valorMapfre = listaMapfre.get(i);
					
					
					
					if(i % 2 == 0){
						datoStyleNfq = datoStyleParNfq;	
						distintosStyleNfq = distintosStyleParNfq;
						datoStyleMapfre = datoStyleParMapfre;	
						distintosStyleMapfre = distintosStyleParMapfre;
					} else {
						datoStyleNfq = datoStyleImparNfq;	
						distintosStyleNfq = distintosStyleImparNfq;
						datoStyleMapfre = datoStyleImparMapfre;	
						distintosStyleMapfre = distintosStyleImparMapfre;
					}
					
					XSSFRow rowNfq = sheetNfq.createRow(cont);
					XSSFRow rowMapfre = sheetMapfre.createRow(cont);
					XSSFRow rowResul = sheetResul.createRow(cont);
					cont ++;
					
					int index = 0;
					
					writeCell(valor.getCnegocio(), valorMapfre.getCnegocio(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getCcanal(), valorMapfre.getCcanal(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getCcartera(), valorMapfre.getCcartera(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getFcierre(), valorMapfre.getFcierre(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getBt(), valorMapfre.getBt(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKramo(), valorMapfre.getKramo(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKmodalidad(), valorMapfre.getKmodalidad(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKpoliza().toString(), valorMapfre.getKpoliza().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKsubpoliza(), valorMapfre.getKsubpoliza(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKcertificado(), valorMapfre.getKcertificado(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getNsuscri(), valorMapfre.getNsuscri(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getNorden(), valorMapfre.getNorden(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKgarantia(), valorMapfre.getKgarantia(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKprestacion(), valorMapfre.getKprestacion(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKajuste(), valorMapfre.getKajuste(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getCtipoaport(), valorMapfre.getCtipoaport(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getIntfeccal().toString(), valorMapfre.getIntfeccal().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getFsuscri(), valorMapfre.getFsuscri(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getKcarterainv(), valorMapfre.getKcarterainv(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getGapAct(), valorMapfre.getGapAct(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getFechaDesde(), valorMapfre.getFechaDesde(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getFechaHasta(), valorMapfre.getFechaHasta(), rowNfq, rowMapfre,rowResul,index++);
					
					index = writeBloque(valor.getBloqueVida(), valorMapfre.getBloqueVida(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloqueFall(), valorMapfre.getBloqueFall(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloqueCompl(), valorMapfre.getBloqueCompl(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloqueGto(), valorMapfre.getBloqueGto(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloqueComi(), valorMapfre.getBloqueComi(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloqueRte(), valorMapfre.getBloqueRte(), rowNfq, rowMapfre,rowResul,index);
					index = writeBloque(valor.getBloquePrim(), valorMapfre.getBloquePrim(), rowNfq, rowMapfre,rowResul,index);
					
					writeCell(valor.getTotalFlujoProyeccion().getSumfprob().toString(), valorMapfre.getTotalFlujoProyeccion().getSumfprob().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getSumfprobtanul().toString(), valorMapfre.getTotalFlujoProyeccion().getSumfprobtanul().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getSumprovision().toString(), valorMapfre.getTotalFlujoProyeccion().getSumprovision().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getSumcola().toString(), valorMapfre.getTotalFlujoProyeccion().getSumcola().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getProvbtiproy().toString(), valorMapfre.getTotalFlujoProyeccion().getProvbtiproy().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getTerminalAnterior().toString(), valorMapfre.getTotalFlujoProyeccion().getTerminalAnterior().toString(), rowNfq, rowMapfre,rowResul,index++);
					writeCell(valor.getTotalFlujoProyeccion().getTerminalPosterior().toString(), valorMapfre.getTotalFlujoProyeccion().getTerminalPosterior().toString(), rowNfq, rowMapfre,rowResul,index++);
					
				}
				
				workBook.write(out);
				out.close();
				
			} catch (IOException e) {
				//e.printStackTrace();
				System.exit(-1);
			}


		log.info("Finalizado");
	}



	private static int writeBloque(BloqueCorriente valor, BloqueCorriente valorMapfre, XSSFRow rowNfq, XSSFRow rowMapfre, XSSFRow rowResul, int index) {
		writeCell(valor.getFechaDevengo(), valorMapfre.getFechaDevengo(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getFechaPago(), valorMapfre.getFechaPago(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getImpFlujoNominal().toString(), valorMapfre.getImpFlujoNominal().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getFpbProbable().toString(), valorMapfre.getFpbProbable().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getImpFlujoProbable().toString(), valorMapfre.getImpFlujoProbable().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getFpbAtc().toString(), valorMapfre.getFpbAtc().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getImpFlujoNoAnulado().toString(), valorMapfre.getImpFlujoNoAnulado().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getFpbAtcfin().toString(), valorMapfre.getFpbAtcfin().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getImpFlujoActualizado().toString(), valorMapfre.getImpFlujoActualizado().toString(), rowNfq, rowMapfre,rowResul,index++);
		writeCell(valor.getImpProvi().toString(), valorMapfre.getImpProvi().toString(), rowNfq, rowMapfre,rowResul,index++);
		
		return index;
	}

	
	
	private static void writeCell(String valor, String valorMapfre, XSSFRow rowNfq, XSSFRow rowMapfre, XSSFRow rowResul,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
			writeCell(rowResul.createCell(cellIndex),datoStyleMapfre,true);
		} else {
			writeCell(rowNfq.createCell(cellIndex),distintosStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);	
			writeCell(rowResul.createCell(cellIndex),distintosStyleMapfre,false);
		}
	}
	
	private static void writeCell(Timestamp valor, Timestamp valorMapfre, XSSFRow rowNfq, XSSFRow rowMapfre, XSSFRow rowResul,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
			writeCell(rowResul.createCell(cellIndex),datoStyleMapfre,true);
		} else {
			writeCell(rowNfq.createCell(cellIndex),distintosStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);	
			writeCell(rowResul.createCell(cellIndex),distintosStyleMapfre,false);
		}
	}
	
	private static void writeCell(Integer valor, Integer valorMapfre, XSSFRow rowNfq, XSSFRow rowMapfre, XSSFRow rowResul,int cellIndex) {
		if(valor==valorMapfre || (valor!= null && valor.equals(valorMapfre)) || (valorMapfre!= null && valorMapfre.equals(valor))){
			writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),datoStyleMapfre,valorMapfre);
			writeCell(rowResul.createCell(cellIndex),datoStyleMapfre,true);
		} else {
			writeCell(rowNfq.createCell(cellIndex),distintosStyleNfq,valor);
			writeCell(rowMapfre.createCell(cellIndex),distintosStyleMapfre,valorMapfre);
			writeCell(rowResul.createCell(cellIndex),distintosStyleMapfre,false);	
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
	
	private static void writeCell(XSSFCell cell,XSSFCellStyle cellStyle,Boolean cellValue){
		 cell.setCellStyle(cellStyle);
		 cell.setCellValue( cellValue);
	}

}
