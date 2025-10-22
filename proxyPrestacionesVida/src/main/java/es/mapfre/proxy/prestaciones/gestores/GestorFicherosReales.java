package es.mapfre.proxy.prestaciones.gestores;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.proxy.prestaciones.dominio.entidades.FlujosRealesSalida;
import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.utils.BtUtils;
import es.mapfre.proxy.prestaciones.utils.ConstantesSolvencia;
import es.mapfre.proxy.prestaciones.utils.LoggerManager;
import es.mapfre.proxy.prestaciones.utils.beanio.BeanIOWriter;

public class GestorFicherosReales {
	private static GestorFicherosReales INSTANCE = null;
	
	private BtUtils btUtils = new BtUtils();
	private String NOMBRE_FICH_BT, RUTA_FICH_BT; 
	private BeanIOWriter writer = null;
	
	private GestorFicherosReales(String sistema, String fecCierre, String canal){
		crearConectores(sistema, fecCierre, canal);
	}
	
	private void crearConectores(String sistema, String fecCierre, String canal){
		try {
			String catalogo_bt = null, carpeta = null;
			switch (sistema) {
				case "GESINTRO":
					carpeta = ConstantesSolvencia.RUTA_GESINTRO;
					catalogo_bt = ConstantesSolvencia.CATALOGO_GESINTRO_BT;
					break;
				case "RTEVTOMAN":
					carpeta = ConstantesSolvencia.RUTA_RTE_VTO_MAN;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTEVTOMAN_BT;
					break;
				case "RTEIND":
					carpeta = ConstantesSolvencia.RUTA_RTE_IND;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTEIND_BT;
					break;
				case "VTOIND":
					carpeta = ConstantesSolvencia.RUTA_VTO_IND;
					catalogo_bt = ConstantesSolvencia.CATALOGO_VTOIND_BT;
					break;
				case "RTEVTOCOL":
					carpeta = ConstantesSolvencia.RUTA_RTE_VTO_COL;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTEVTOCOL_BT;
					break;
				case "ANTIND":
					carpeta = ConstantesSolvencia.RUTA_ANT_IND;
					catalogo_bt = ConstantesSolvencia.CATALOGO_ANTIND_BT;
					break;
				case "ASEVAL":
					carpeta = ConstantesSolvencia.RUTA_ASEVAL;
					catalogo_bt = ConstantesSolvencia.CATALOGO_ASEVAL_BT;
					break;
				case "AS400":
					carpeta = ConstantesSolvencia.RUTA_AS400;
					catalogo_bt = ConstantesSolvencia.CATALOGO_AS400_BT;
					break;
				case "RTANEO":
					carpeta = ConstantesSolvencia.RUTA_RTA_NEO;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTANEO_BT;
					break;
				case "MOVNEO":
					carpeta = ConstantesSolvencia.RUTA_MOV_NEO;
					catalogo_bt = ConstantesSolvencia.CATALOGO_MOVNEO_BT;
					break;
				case "PREONS":
					carpeta = ConstantesSolvencia.RUTA_PRE_ONS;
					catalogo_bt = ConstantesSolvencia.CATALOGO_PREONS_BT;
					break;
				case "RTAINDTRAD":
					carpeta = ConstantesSolvencia.RUTA_RTA_IND_TRAD;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTAIND_TRAD_BT;
					break;
				case "RTACOL":
					carpeta = ConstantesSolvencia.RUTA_RTA_COL;
					catalogo_bt = ConstantesSolvencia.CATALOGO_RTACOL_BT;
					break;
			}
				
			String rutaFecCierre = fecCierre.substring(0, 6);
			if (sistema.contentEquals("PREONS")) {
				NOMBRE_FICH_BT = btUtils.getCargaFicherosProperty(catalogo_bt) + "_" + StringUtils.leftPad(canal, 2, "0") + ConstantesSolvencia.STR_MOV_MES + "NIIF17" + "_" + rutaFecCierre + ".TXT";
			} else {
				NOMBRE_FICH_BT = btUtils.getCargaFicherosProperty(catalogo_bt) + "_" + StringUtils.leftPad(canal, 2, "0") + ConstantesSolvencia.STR_MOV_MES + rutaFecCierre + ".TXT";
			}
			RUTA_FICH_BT = ConstantesSolvencia.RUTA_BASE + File.separator + rutaFecCierre + File.separator + carpeta + File.separator + NOMBRE_FICH_BT;

			FileWriter fwErr = new FileWriter(RUTA_FICH_BT);
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, RUTA_FICH_BT, ConstantesSolvencia.STREAM_SALIDA);
			
		} catch (Exception e) {}
	}
	
	private synchronized static void createInstance(String sistema, String fecCierre, String canal){
		INSTANCE = new GestorFicherosReales(sistema, fecCierre, canal);
	}
	
	public static GestorFicherosReales getInstance(String sistema, String fecCierre, String canal){
		createInstance(sistema, fecCierre, canal);
		return INSTANCE;
	}

	public void cerrarConectores() {
		try {
			writer.close();
		} catch (Exception e) {}
	}
	
	public void write(FlujosRealesSalida registroSalida){
		writer.write(registroSalida);
		writer.flush();
	}
	
	public Incidencia copyToSSAA (String sistema, String fecCierre) {
		Incidencia inci = null;
		LoggerManager logGeneral = LoggerManager.getInstance();
		String carpetaSSAA = null;
		InputStream in = null;
		OutputStream out = null;
		switch (sistema) {
			case "GESINTRO":
				carpetaSSAA = "VIDA_STROS_GESINTRO";
				break;
			case "RTEVTOMAN":
				carpetaSSAA = "VIDA_RESC_VCTOS_MAN";
				break;
			case "RTEIND":
				carpetaSSAA = "VIDA_RESC_INDV";
				break;
			case "VTOIND":
				carpetaSSAA = "VIDA_VCTOS_INDV";
				break;
			case "RTEVTOCOL":
				carpetaSSAA = "VIDA_RESC_VCTOS_COL_IDMS";
				break;
			case "ANTIND":
				carpetaSSAA = "VIDA_ANTCP_IDMS";
				break;
			case "ASEVAL":
				carpetaSSAA = "VIDA_ASEVAL";
				break;
			case "AS400":
				carpetaSSAA = "VIDA_AS400";
				break;
			case "RTANEO":
				carpetaSSAA = "RENTASDB2";
				break;
			case "MOVNEO":
				carpetaSSAA = "VIDA_MGM";
				break;
			case "PREONS":
				carpetaSSAA = "ONESAIT VIDA";
				break;
			case "RTAINDTRAD":
				carpetaSSAA = "VIDA_RENTAS_TRAD";
				break;
			case "RTACOL":
				carpetaSSAA = "VIDA_RENTAS_COL_IDMS";
				break;
		}
		File origin = new File(RUTA_FICH_BT);
		File destination = new File(btUtils.getRutaFicherosProperty(ConstantesSolvencia.RUTA_RAIZ_SSAA) + carpetaSSAA + File.separator + "CIERRE_" + fecCierre.substring(0, 6) + File.separator + NOMBRE_FICH_BT);
		logGeneral.writeLog(sistema, "Se copia el fichero " + RUTA_FICH_BT + " al fichero " + destination, ConstantesSolvencia.LOG_INFO, true);
		if (origin.exists()) {
            try {
                in = new FileInputStream(origin);
                try {
                    out = new FileOutputStream(destination);
                    try {
                        byte[] buf = new byte[1024];
                        int len;
        				while ((len = in.read(buf)) > 0) {
        				    out.write(buf, 0, len);
        				}
                        in.close();
                        out.close();
        			} catch (IOException e) {
                    	inci = new Incidencia();
                    	inci.setCodigoRetorno("12");
                    	inci.setInfAmpliada("Error al copiar el fichero " + origin + " al fichero " + destination + " - " + e.getMessage());
                    	logGeneral.writeLog(sistema, "Error al copiar el fichero " + origin + " al fichero " + destination + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
        			}
                } catch (FileNotFoundException e) {
                	inci = new Incidencia();
                	inci.setCodigoRetorno("12");
                	inci.setInfAmpliada("Error al crear el fichero " + destination + " - " + e.getMessage());
                	logGeneral.writeLog(sistema, "Error al crear el fichero " + destination + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
                }
            } catch (FileNotFoundException e) {
            	inci = new Incidencia();
            	inci.setCodigoRetorno("12");
            	inci.setInfAmpliada("Error al cargar el fichero " + origin + " - " + e.getMessage());
            	logGeneral.writeLog(sistema, "Error al cargar el fichero " + origin + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
            }
        } else {
        	inci = new Incidencia();
        	inci.setCodigoRetorno("12");
        	inci.setInfAmpliada("No se encuentra el fichero " + origin);
        	logGeneral.writeLog(sistema, "No se encuentra el fichero " + origin, ConstantesSolvencia.LOG_ERROR, true);
        }
        return inci;
	}
}
