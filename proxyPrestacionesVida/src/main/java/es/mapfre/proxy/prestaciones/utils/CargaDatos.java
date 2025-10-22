package es.mapfre.proxy.prestaciones.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import es.mapfre.proxy.prestaciones.dao.Dao;
import es.mapfre.proxy.prestaciones.dao.services.FactoriaDao;
import es.mapfre.proxy.prestaciones.dominio.entidades.Incidencia;
import es.mapfre.proxy.prestaciones.excepcion.Solvencia2Excepcion;
import es.mapfre.proxy.prestaciones.gestores.GestorIncidenciasGen;
import es.mapfre.proxy.prestaciones.utils.beanio.BeanIOReader;

public class CargaDatos {
	
	private BtUtils btUtils = new BtUtils();
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-proxy.xml";

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
	
	public void cargaPesosBt (String fecCierre, String canal, String negocio) 
			throws FileNotFoundException, IOException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		BeanIOReader reader = null;
		String rutaFecCierre = fecCierre.substring(0, 6);
		String fichero = null;
		
		fichero = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_PESOS);
		
		String filePath = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.PROXY + File.separator 
				+ fecCierre + "_" + StringUtils.leftPad(equivCanales.get(canal), 5, "0") + "_" + negocio + "_" + fichero;
		
		String streamName;
		if (negocio.equals(ConstantesSolvencia.COLECTIVOS)) {
			streamName = ConstantesSolvencia.STREAM_PESOS_COL;
		} else {
			streamName = ConstantesSolvencia.STREAM_PESOS_IND;
		}
		try {
			reader = new BeanIOReader(BEANIO_CONFIG_XML,filePath, streamName);
			Dao dao = FactoriaDao.getDao(streamName);
			dao.loadCache(reader);
			reader.close();
		} catch (IOException e) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("", "Error al cargar el fichero de pesos " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar el fichero de pesos " + filePath + " - " + e.getMessage());
			giGeneral.write(inci);
		} 
	}

	public void cargaFichaProceso () {

		String filePath = "PROXYVIDA" + File.separator + "CIERRES" + File.separator + ConstantesSolvencia.RUTA_FICHAS + btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FICHA);
		
		String streamName = ConstantesSolvencia.STREAM_FICHA;
		try {
			BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML,filePath, streamName);
			Dao dao = FactoriaDao.getDao(streamName);
			dao.loadCache(reader);

			reader.close();
		} catch (IOException e) {
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("", "Error al cargar la ficha de proceso " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
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
			logGeneral.writeLog("", "Error al cargar el fichero de datos reales " + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar el fichero de datos reales " + filePath + " - " + e.getMessage());
			giGeneral.write(inci);
		}
	}
}
