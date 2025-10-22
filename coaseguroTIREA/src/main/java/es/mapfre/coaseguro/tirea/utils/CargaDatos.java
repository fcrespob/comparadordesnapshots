package es.mapfre.coaseguro.tirea.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.coaseguro.tirea.dao.Dao;
import es.mapfre.coaseguro.tirea.dao.services.FactoriaDao;
import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencia;
import es.mapfre.coaseguro.tirea.excepcion.Solvencia2Excepcion;
import es.mapfre.coaseguro.tirea.gestores.GestorIncidenciasGen;
import es.mapfre.coaseguro.tirea.utils.beanio.BeanIOReader;

public class CargaDatos {
	
	private BtUtils btUtils = new BtUtils();
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-tirea.xml";

	Map<String, String> equivCanales = new HashMap<String, String>();
	
	public CargaDatos () {
		equivCanales.put("3", "1");
		equivCanales.put("42", "20");
		equivCanales.put("43", "60");
		equivCanales.put("44", "72");
		equivCanales.put("45", "75");
		equivCanales.put("46", "74");
		equivCanales.put("90", "80");
	}
	
	public void cargaFicheros (String fecCierre) 
			throws FileNotFoundException, IOException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		BeanIOReader reader = null, reader1 = null, reader2 = null, reader3 = null;
		String rutaFecCierre = fecCierre.substring(0, 6);
		String fichero = null, fichero2 = null, fichero3 = null, fichero4 = null;
		
		//CARGAR TODOS PARA CADA BT
		fichero = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TOTPMACOABTI);
		fichero2 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_DATOSCOA);
		fichero3 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TAB35012);
		fichero4 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMACOABTI);

		
		String filePath = "/mnt/solv2vida/" + "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero;
		String filePath2 = "/mnt/solv2vida/" + "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero2;
		String filePath3 = "/mnt/solv2vida/" + "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero3;
		String filePath4 = "/mnt/solv2vida/" + "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero4;
		
		String streamName = ConstantesSolvencia.STREAM_TOTPMACOA;
		String streamName1 = ConstantesSolvencia.STREAM_DATOSCOA;
		String streamName2 = ConstantesSolvencia.STREAM_TAB35012;
		String streamName3 = ConstantesSolvencia.STREAM_FLUJPMACOA;
		
		try {
			reader = new BeanIOReader(BEANIO_CONFIG_XML,filePath, streamName);
			reader1 = new BeanIOReader(BEANIO_CONFIG_XML,filePath2, streamName1);
			reader2 = new BeanIOReader(BEANIO_CONFIG_XML,filePath3, streamName2);
			reader3 = new BeanIOReader(BEANIO_CONFIG_XML,filePath4, streamName3);
			Dao dao = FactoriaDao.getDao(streamName);
			Dao dao1 = FactoriaDao.getDao(streamName1);
			Dao dao2 = FactoriaDao.getDao(streamName2);
			Dao dao3 = FactoriaDao.getDao(streamName3);
			dao.loadCache(reader);
			dao1.loadCache(reader1);
			dao2.loadCache(reader2);
			dao3.loadCache(reader3);
		} catch (IOException e) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error al cargar el fichero de pesos " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar el fichero de pesos " + filePath + " - " + e.getMessage());
			giGeneral.write(inci);
		} 
	}

	public void cargaFichaProceso () {

		String filePath = "/mnt/solv2vida/" + File.separator + "CIERRES" + File.separator + "FICHATIREA" + File.separator + ConstantesSolvencia.RUTA_FICHAS + btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FICHA);
		
		String streamName = ConstantesSolvencia.STREAM_FICHA;
		try {
			BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML,filePath, streamName);
			Dao dao = FactoriaDao.getDao(streamName);
			dao.loadCache(reader);

			reader.close();
		} catch (IOException e) {
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error al cargar la ficha de proceso " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar la ficha de proceso " + filePath + " - " + e.getMessage());
			Solvencia2Excepcion solv = new Solvencia2Excepcion(inci);
			throw solv;
		}
	}
	
	public void cargaDatosReales (String sistema, String fecCierre, String canal) {
		
		String catalogo = null, carpeta = null;
		switch (sistema) {
			case "GESINTRO":
				carpeta = ConstantesSolvencia.RUTA_GESINTRO;
				catalogo = ConstantesSolvencia.CATALOGO_GESINTRO;
				break;
			case "RTEVTOMAN":
				carpeta = ConstantesSolvencia.RUTA_RTE_VTO_MAN;
				catalogo = ConstantesSolvencia.CATALOGO_RTEVTOMAN;
				break;
			case "RTEIND":
				carpeta = ConstantesSolvencia.RUTA_RTE_IND;
				catalogo = ConstantesSolvencia.CATALOGO_RTEIND;
				break;
			case "VTOIND":
				carpeta = ConstantesSolvencia.RUTA_VTO_IND;
				catalogo = ConstantesSolvencia.CATALOGO_VTOIND;
				break;
			case "RTEVTOCOL":
				carpeta = ConstantesSolvencia.RUTA_RTE_VTO_COL;
				catalogo = ConstantesSolvencia.CATALOGO_RTEVTOCOL;
				break;
			case "ANTIND":
				carpeta = ConstantesSolvencia.RUTA_ANT_IND;
				catalogo = ConstantesSolvencia.CATALOGO_ANTIND;
				break;
			case "ASEVAL":
				carpeta = ConstantesSolvencia.RUTA_ASEVAL;
				catalogo = ConstantesSolvencia.CATALOGO_ASEVAL;
				break;
			case "AS400":
				carpeta = ConstantesSolvencia.RUTA_AS400;
				catalogo = ConstantesSolvencia.CATALOGO_AS400;
				break;
			case "RTANEO":
				carpeta = ConstantesSolvencia.RUTA_RTA_NEO;
				catalogo = ConstantesSolvencia.CATALOGO_RTANEO;
				break;
			case "MOVNEO":
				carpeta = ConstantesSolvencia.RUTA_MOV_NEO;
				catalogo = ConstantesSolvencia.CATALOGO_MOVNEO;
				break;
			case "PREONS":
				carpeta = ConstantesSolvencia.RUTA_PRE_ONS;
				catalogo = ConstantesSolvencia.CATALOGO_PREONS;
				break;
			case "RTAINDTRAD":
				carpeta = ConstantesSolvencia.RUTA_RTA_IND_TRAD;
				catalogo = ConstantesSolvencia.CATALOGO_RTAIND_TRAD;
				break;
			case "RTACOL":
				carpeta = ConstantesSolvencia.RUTA_RTA_COL;
				catalogo = ConstantesSolvencia.CATALOGO_RTACOL;
				break;
		}
			
		String rutaFecCierre = fecCierre.substring(0, 6);
		String filePath = ConstantesSolvencia.RUTA_BASE + rutaFecCierre + File.separator + carpeta + File.separator
				+ fecCierre + "_" + StringUtils.leftPad(canal, 2, "0") + "_" + btUtils.getCargaFicherosProperty(catalogo);

		try {
			BeanIOReader reader = new BeanIOReader(ConstantesSolvencia.BEANIO_CONFIG_XML, filePath, ConstantesSolvencia.STREAM_ENTRADA);
			Dao dao = FactoriaDao.getDao(ConstantesSolvencia.STREAM_ENTRADA);
			dao.loadCache(reader);

			reader.close();
		} catch (Exception e) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error al cargar el fichero de datos reales " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar el fichero de datos reales " + filePath + " - " + e.getMessage());
			giGeneral.write(inci);
		}
	}
}
