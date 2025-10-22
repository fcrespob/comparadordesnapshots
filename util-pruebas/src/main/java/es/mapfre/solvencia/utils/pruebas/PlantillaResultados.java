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

import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class PlantillaResultados {
	private static final String D_WORKBOOK_XLSX = "C:/Users/eugenio.torres/CIERRES/201704/1/BRUTOSIND/SOLV_2017040000_SO05_GP01_140_20170331_I_1_BTI_FLUJOSDET_.xlsx";

	private static final Logger log = LoggerFactory.getLogger(PlantillaResultados.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	private static final String ficheroNfq = "C:/Users/eugenio.torres/CIERRES/201704/1/BRUTOSIND/SOLV_2017040000_SO05_GP01_140_20170331_I_1_BTI_FLUJOSDET_.txt";
	
	private static XSSFCellStyle datoStyleParNfq;
	private static XSSFCellStyle datoStyleImparNfq;
	
	private static XSSFCellStyle cambioBloqueStyle;
	
	private static XSSFCellStyle datoStyleNfq;

	private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	
	public static void main(String[] args) {

		BeanIOReader readerNfq;
		DetalleCorriente detalle = null;
		
		try {
			
			readerNfq = new BeanIOReader(BEANIO_CONFIG_XML, ficheroNfq, "detalle-corriente");
			
			// create a new file
			FileOutputStream out = new FileOutputStream(D_WORKBOOK_XLSX);
			// create a new workbook
			XSSFWorkbook workBook = new XSSFWorkbook();
			XSSFSheet sheetNfq = workBook.createSheet("Nfoque");
			
			XSSFRow rowNfqCab =  sheetNfq.createRow(0);
			
			XSSFCellStyle cabeceraStyle = workBook.createCellStyle();
			
			cabeceraStyle.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			cabeceraStyle.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			cabeceraStyle.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
			cabeceraStyle.setBorderRight(XSSFCellStyle.BORDER_MEDIUM);
			
			cabeceraStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cabeceraStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
			cabeceraStyle.setFillForegroundColor(new XSSFColor(Color.YELLOW));
			
		String[] textosCabeceras = { "CNEGOCIO", "CCANAL", "CCARTERA",
				"FCIERRE", "BT", "KMODALIDAD","KRAMO", "KPOLIZA", "KSUBPOLIZA",
				"KCERTIFICADO", "NSUSCRI", "NORDEN", "KGARANTIA",
				"KPRESTACION", "KAJUSTE", "CTIPOAPORT", "SIG-INTFECCALC","INTFECCALC",
				"FSUSCRI","KCARTERAINV","GAPACT" ,"FDESDE", "FHASTA",
				//VIDA
				"FDEVENGOVIDA", "FPAGOVIDA", "SIG-IMPFLUJONOMINALVIDA", "IMPFLUJONOMINALVIDA",
				"FPPROBABLEVIDA", "SIG-IMPFLUJOPROBABLEVIDA","IMPFLUJOPROBABLEVIDA", "FBPACTVIDA",
				"SIG-IMPFLUJONOANUVIDA","IMPFLUJONOANUVIDA", "FBPACTFINVIDA",
				"SIG-IMPFLUJOACTVIDA","IMPFLUJOACTVIDA","SIG-IMPPROVIVIDA", "IMPPROVIVIDA",
				//FALLECIMIENTO
				"FDEVENGOFALL", "FPAGOFALL", "SIG-IMPFLUJONOMINALFALL", "IMPFLUJONOMINALFALL",
				"FPPROBABLEFALL", "SIG-IMPFLUJOPROBABLEFALL","IMPFLUJOPROBABLEFALL", "FBPACTFALL",
				"SIG-IMPFLUJONOANUFALL","IMPFLUJONOANUFALL", "FBPACTFINFALL",
				"SIG-IMPFLUJOACTFALL","IMPFLUJOACTFALL","SIG-IMPPROVIFALL", "IMPPROVIFALL",
				//COMPLEMENTARIO
				"FDEVENGOCOMP", "FPAGOCOMP", "SIG-IMPFLUJONOMINALCOMP", "IMPFLUJONOMINALCOMP",
				"FPPROBABLECOMP", "SIG-IMPFLUJOPROBABLECOMP","IMPFLUJOPROBABLECOMP", "FBPACTCOMP",
				"SIG-IMPFLUJONOANUCOMP","IMPFLUJONOANUCOMP", "FBPACTFINCOMP",
				"SIG-IMPFLUJOACTCOMP","IMPFLUJOACTCOMP","SIG-IMPPROVICOMP", "IMPPROVICOMP",
				//GASTO
				"FDEVENGOGTO", "FPAGOGTO", "SIG-IMPFLUJONOMINALGTO", "IMPFLUJONOMINALGTO",
				"FPPROBABLEGTO", "SIG-IMPFLUJOPROBABLEGTO","IMPFLUJOPROBABLEGTO", "FBPACTGTO",
				"SIG-IMPFLUJONOANUGTO","IMPFLUJONOANUGTO", "FBPACTFINGTO",
				"SIG-IMPFLUJOACTGTO","IMPFLUJOACTGTO","SIG-IMPPROVIGTO", "IMPPROVIGTO",
				//COMISION
				"FDEVENGOCOMI", "FPAGOCOMI", "SIG-IMPFLUJONOMINALCOMI", "IMPFLUJONOMINALCOMI",
				"FPPROBABLECOMI", "SIG-IMPFLUJOPROBABLECOMI","IMPFLUJOPROBABLECOMI", "FBPACTCOMI",
				"SIG-IMPFLUJONOANUCOMI","IMPFLUJONOANUCOMI", "FBPACTFINCOMI",
				"SIG-IMPFLUJOACTCOMI","IMPFLUJOACTCOMI","SIG-IMPPROVICOMI", "IMPPROVICOMI",
				//RESCATE
				"FDEVENGORTE", "FPAGORTE", "SIG-IMPFLUJONOMINALRTE", "IMPFLUJONOMINALRTE",
				"FPPROBABLERTE", "SIG-IMPFLUJOPROBABLERTE","IMPFLUJOPROBABLERTE", "FBPACTRTE",
				"SIG-IMPFLUJONOANURTE","IMPFLUJONOANURTE", "FBPACTFINRTE",
				"SIG-IMPFLUJOACTRTE","IMPFLUJOACTRTE","SIG-IMPPROVIRTE", "IMPPROVIRTE",
				//PRIMA
				"FDEVENGOPRIMA", "FPAGOPRIMA", "SIG-IMPFLUJONOMINALPRIMA", "IMPFLUJONOMINALPRIMA",
				"FPPROBABLEPRIMA", "SIG-IMPFLUJOPROBABLEPRIMA","IMPFLUJOPROBABLEPRIMA", "FBPACTPRIMA",
				"SIG-IMPFLUJONOANUPRIMA","IMPFLUJONOANUPRIMA", "FBPACTFINPRIMA",
				"SIG-IMPFLUJOACTPRIMA","IMPFLUJOACTPRIMA","SIG-IMPPROVIPRIMA", "IMPPROVIPRIMA",
				//FLUJO PROYECCION
				"SIG-SUMFPROB","SUMFPROB", "SIG-SUMFPROBTANUL","SUMFPROBTANUL",
				"SIG-SUMPROVISION", "SUMPROVISION", "SIG-SUMCOLA", "SUMCOLA", 
				"SIG-PROVBTIPROY","PROVBTIPROY","SIG-TERMINAL ANTERIOR", "TERMINAL ANTERIOR",  
				"SIG-TERMINAL POSTERIOR","TERMINAL POSTERIOR"  };
		
			sheetNfq.setDefaultColumnWidth(20);
			for (int i = 0; i < textosCabeceras.length; i++) {
				writeCell(rowNfqCab.createCell(i),cabeceraStyle,textosCabeceras[i]);
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
				writeCell(valor.getKmodalidad(), rowNfq, index++);
				writeCell(valor.getKramo(), rowNfq, index++);
				writeCell(valor.getKpoliza().toString(), rowNfq, index++);
				writeCell(valor.getKsubpoliza(), rowNfq, index++);
				writeCell(valor.getKcertificado(), rowNfq, index++);
				writeCell(valor.getNsuscri(), rowNfq, index++);
				writeCell(valor.getNorden(), rowNfq, index++);
				writeCell(valor.getKgarantia(), rowNfq, index++);
				writeCell(valor.getKprestacion(), rowNfq, index++);
				writeCell(valor.getKajuste(), rowNfq, index++);
				writeCell(valor.getCtipoaport(), rowNfq, index++);
				writeCellSig(valor.getIntfeccal().signum(), rowNfq, index++);
				writeCell(valor.getIntfeccal(), rowNfq, index++);
				writeCell(valor.getFsuscri(), rowNfq, index++);
				writeCell(valor.getKcarterainv(), rowNfq, index++);
				writeCell(valor.getGapAct(), rowNfq, index++);
				writeCell(valor.getFechaDesde(), rowNfq, index++);
				writeCell(valor.getFechaHasta(), rowNfq, index++);
				
				index = writeBloque(valor.getBloqueVida(), rowNfq, index);
				index = writeBloque(valor.getBloqueFall(), rowNfq, index);
				index = writeBloque(valor.getBloqueCompl(), rowNfq, index);
				index = writeBloque(valor.getBloqueGto(), rowNfq, index);
				index = writeBloque(valor.getBloqueComi(), rowNfq, index);
				index = writeBloque(valor.getBloqueRte(), rowNfq, index);
				index = writeBloque(valor.getBloquePrim(), rowNfq, index);
				
				writeCellSig(valor.getTotalFlujoProyeccion().getSumfprob().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getSumfprob(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getSumfprobtanul().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getSumfprobtanul(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getSumprovision().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getSumprovision(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getSumcola().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getSumcola(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getProvbtiproy().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getProvbtiproy(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getTerminalAnterior().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getTerminalAnterior(), rowNfq, index++);
				writeCellSig(valor.getTotalFlujoProyeccion().getTerminalPosterior().signum(), rowNfq, index++);
				writeCell(valor.getTotalFlujoProyeccion().getTerminalPosterior(), rowNfq, index++);
			}
			
			workBook.write(out);
			out.close();
			
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}

		log.info("Finalizado");
	}

	private static int writeBloque(BloqueCorriente valor, XSSFRow rowNfq, int index) {
		writeCell(valor.getFechaDevengo(), rowNfq, index++);
		writeCell(valor.getFechaPago(), rowNfq, index++);
		writeCellSig(valor.getImpFlujoNominal().signum(), rowNfq, index++);
		writeCell(valor.getImpFlujoNominal(), rowNfq, index++);
		writeCell(valor.getFpbProbable(), rowNfq, index++);
		writeCellSig(valor.getImpFlujoProbable().signum(), rowNfq, index++);
		writeCell(valor.getImpFlujoProbable(), rowNfq, index++);
		writeCell(valor.getFpbAtc().toString(), rowNfq, index++);
		writeCellSig(valor.getImpFlujoNoAnulado().signum(), rowNfq, index++);
		writeCell(valor.getImpFlujoNoAnulado(), rowNfq, index++);
		writeCell(valor.getFpbAtcfin(), rowNfq, index++);
		writeCellSig(valor.getImpFlujoActualizado().signum(), rowNfq, index++);
		writeCell(valor.getImpFlujoActualizado(), rowNfq, index++);
		writeCellSig(valor.getImpProvi().signum(), rowNfq, index++);
		writeCell(valor.getImpProvi(), rowNfq, index++);
		
		return index;
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
		if (value.equals("")){
			value = "0";
		}
		writeCellBD(rowNfq.createCell(cellIndex),datoStyleNfq,value);
	}
	
	private static void writeCellSig(Integer valor, XSSFRow rowNfq, int cellIndex) {
		if (valor.equals(-1)){
			writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,"-");
		} else {
			writeCell(rowNfq.createCell(cellIndex),datoStyleNfq,"+");
		}
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
		cell.setCellValue(cellValue);
	}
	
	private static void writeCellBD(XSSFCell cell,XSSFCellStyle cellStyle,String cellValue){
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}

}
