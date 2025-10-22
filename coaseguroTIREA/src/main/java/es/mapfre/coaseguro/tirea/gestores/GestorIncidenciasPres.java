package es.mapfre.coaseguro.tirea.gestores;

import java.io.File;
import java.io.FileWriter;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencias;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.utils.BtUtils;
import es.mapfre.coaseguro.tirea.utils.ConstantesSolvencia;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOWriter;

public class GestorIncidenciasPres {
	
	private static GestorIncidenciasPres INSTANCE = null;
	private static int NUM_INCI;
	
	private BtUtils btUtils = new BtUtils();
	// Constantes
	private String NOMBRE_FICH_INCIDENCIAS;
	
	//Atributos
	private BeanIOWriter writer = null;
	
	private GestorIncidenciasPres(String sistema, String fecCierre, String canal){
		try {
			String catalogo = null, carpeta = null;
			switch (sistema) {
				case "GESINTRO":
					carpeta = ConstantesSolvencia.RUTA_GESINTRO;
					catalogo = ConstantesSolvencia.CATALOGO_INC_GESINTRO;
					break;
				case "RTEVTOMAN":
					carpeta = ConstantesSolvencia.RUTA_RTE_VTO_MAN;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTEVTOMAN;
					break;
				case "RTEIND":
					carpeta = ConstantesSolvencia.RUTA_RTE_IND;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTEIND;
					break;
				case "VTOIND":
					carpeta = ConstantesSolvencia.RUTA_VTO_IND;
					catalogo = ConstantesSolvencia.CATALOGO_INC_VTOIND;
					break;
				case "RTEVTOCOL":
					carpeta = ConstantesSolvencia.RUTA_RTE_VTO_COL;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTEVTOCOL;
					break;
				case "ANTIND":
					carpeta = ConstantesSolvencia.RUTA_ANT_IND;
					catalogo = ConstantesSolvencia.CATALOGO_INC_ANTIND;
					break;
				case "ASEVAL":
					carpeta = ConstantesSolvencia.RUTA_ASEVAL;
					catalogo = ConstantesSolvencia.CATALOGO_INC_ASEVAL;
					break;
				case "AS400":
					carpeta = ConstantesSolvencia.RUTA_AS400;
					catalogo = ConstantesSolvencia.CATALOGO_INC_AS400;
					break;
				case "RTANEO":
					carpeta = ConstantesSolvencia.RUTA_RTA_NEO;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTANEO;
					break;
				case "MOVNEO":
					carpeta = ConstantesSolvencia.RUTA_MOV_NEO;
					catalogo = ConstantesSolvencia.CATALOGO_INC_MOVNEO;
					break;
				case "PREONS":
					carpeta = ConstantesSolvencia.RUTA_PRE_ONS;
					catalogo = ConstantesSolvencia.CATALOGO_INC_PREONS;
					break;
				case "RTAINDTRAD":
					carpeta = ConstantesSolvencia.RUTA_RTA_IND_TRAD;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTAIND_TRAD;
					break;
				case "RTACOL":
					carpeta = ConstantesSolvencia.RUTA_RTA_COL;
					catalogo = ConstantesSolvencia.CATALOGO_INC_RTACOL;
					break;
			}

			String rutaFecCierre = fecCierre.substring(0, 6);
			NOMBRE_FICH_INCIDENCIAS = ConstantesSolvencia.RUTA_BASE + rutaFecCierre + File.separator + carpeta + File.separator + fecCierre + "_" + StringUtils.leftPad(canal, 2, "0") + "_" + btUtils.getCargaFicherosProperty(catalogo);

			FileWriter fwErr = new FileWriter(NOMBRE_FICH_INCIDENCIAS);
			fwErr.close();
			
			writer = new BeanIOWriter(ConstantesSolvencia.BEANIO_CONFIG_XML, NOMBRE_FICH_INCIDENCIAS, ConstantesSolvencia.STREAM_INCIDENCIAS);
			NUM_INCI = 0;
		} catch (Exception e) {
			Incidencia incidencia = new Incidencia();
			incidencia.setCodigoRetorno("10");
			incidencia.setInfAmpliada("Se ha producido un error al cargar el gestor de incidencias [sistema: " + sistema + "; fecCierre: " + fecCierre + "; canal:" + canal + "]. - Error: " + e.getMessage());
			Solvencia2Excepcion solv = new Solvencia2Excepcion(incidencia);
			throw solv;
		}
	}
	
	private synchronized static void createInstance(String sistema, String fecCierre, String canal){
		INSTANCE = new GestorIncidenciasPres(sistema, fecCierre, canal);
	}
	
	public static GestorIncidenciasPres getInstance(String sistema, String fecCierre, String canal){
		createInstance(sistema, fecCierre, canal);
		return INSTANCE;
	}

	public void write(Solvencia2Excepcion e) {
		NUM_INCI++;
		Incidencias incidencia = new Incidencias();
		incidencia.setCodigoerror(e.getIncidencia().getCodigoRetorno());
		incidencia.setDescerror(e.getIncidencia().getInfAmpliada());
		writer.write(incidencia);
		writer.flush();
	}
	
	public void write(Incidencia inci) {
		NUM_INCI++;
		Incidencias incidencia = new Incidencias();
		incidencia.setCanal(inci.getCcanal());
		if (null == inci.getFecCierre()){
			incidencia.setFeccierre("00000000");
		} else {
			incidencia.setFeccierre(inci.getFecCierre());
		}
		incidencia.setCodigoerror(inci.getCodigoRetorno());
		incidencia.setDescerror(inci.getInfAmpliada());
		writer.write(incidencia);
		writer.flush();
	}
	
	public void write(Incidencias inci) {
		NUM_INCI++;
		writer.write(inci);
		writer.flush();
	}
	
	public int getNumInci(){
		return NUM_INCI;
	}

	public void cerrarConector() {
		try {
			writer.close();
		} catch (Exception e) {}
	}
	
}
