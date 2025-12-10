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

		BeanIOReader reader = null, reader1 = null, reader2 = null, reader3 = null, reader4 = null, reader5 = null, reader6 = null, reader7 = null, reader8 = null, reader9 = null, reader10 = null, reader11 = null, reader12 = null, reader13 = null, reader14 = null, reader15 = null;
		String rutaFecCierre = fecCierre.substring(0, 6);
		String fichero = null, fichero2 = null, fichero3 = null, fichero4 = null, fichero5 = null, fichero6 = null, fichero7 = null, fichero8 = null, fichero9 = null, fichero10 = null, fichero11 = null, fichero12 = null, fichero13 = null, fichero14 = null, fichero15 = null, fichero16 = null;
		
		//CARGAR TODOS PARA CADA BT
		fichero = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TOTPMACOABTI);
		fichero2 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_DATOSCOA);
		fichero3 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TAB35012);
		fichero4 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMACOABTI);
		fichero5 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TOTPMACOABTCOA);
		fichero6 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMACOABTCOA);
		fichero7 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TOTPMACOABTCOATF);
		fichero8 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMACOABTCOATF);
		fichero9 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMDCOABTI);
		fichero10 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMDCOABTCOA);
		fichero11 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_FLUJPMDCOABTCOATF);
		fichero12 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TAB35013);
		fichero13 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TAB35014);
		fichero14 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_TAB35015);
		fichero15 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_DATOSESPECIFIC);
		fichero16 = btUtils.getCargaFicherosProperty(ConstantesSolvencia.CATALOGO_PAGOSPLAN);

		
		String filePath = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero;
		String filePath2 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero2;
		String filePath3 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero3;
		String filePath4 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero4;
		String filePath5 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero5;
		String filePath6 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero6;
		String filePath7 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero7;
		String filePath8 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero8;
		String filePath9 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero9;
		String filePath10 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero10;
		String filePath11 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero11;
		String filePath12 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero12;
		String filePath13 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero13;
		String filePath14 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero14;
		String filePath15 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero15;
		String filePath16 = "CIERRES" + File.separator + rutaFecCierre + File.separator + ConstantesSolvencia.TIREA + File.separator + fichero16;
		
		String streamName = ConstantesSolvencia.STREAM_TOTPMACOA;
		String streamName1 = ConstantesSolvencia.STREAM_DATOSCOA;
		String streamName2 = ConstantesSolvencia.STREAM_TAB35012;
		String streamName3 = ConstantesSolvencia.STREAM_FLUJPMACOA;
		String streamName4 = ConstantesSolvencia.STREAM_TOTPMACOABTCOA;
		String streamName5 = ConstantesSolvencia.STREAM_FLUJPMACOABTCOA;
		String streamName6 = ConstantesSolvencia.STREAM_TOTPMACOABTCOATF;
		String streamName7 = ConstantesSolvencia.STREAM_FLUJPMACOABTCOATF;
		String streamName8 = ConstantesSolvencia.STREAM_FLUJPMDCOA;
		String streamName9 = ConstantesSolvencia.STREAM_FLUJPMDCOABTCOA;
		String streamName10 = ConstantesSolvencia.STREAM_FLUJPMDCOABTCOATF;
		String streamName11 = ConstantesSolvencia.STREAM_TAB35013;
		String streamName12 = ConstantesSolvencia.STREAM_TAB35014;
		String streamName13 = ConstantesSolvencia.STREAM_TAB35015;
		String streamName14 = ConstantesSolvencia.STREAM_DATOSESPECIFIC;
		String streamName15 = ConstantesSolvencia.STREAM_PAGOSPLAN;
		try {
			reader = new BeanIOReader(BEANIO_CONFIG_XML,filePath, streamName);
			reader1 = new BeanIOReader(BEANIO_CONFIG_XML,filePath2, streamName1);
			reader2 = new BeanIOReader(BEANIO_CONFIG_XML,filePath3, streamName2);
			reader3 = new BeanIOReader(BEANIO_CONFIG_XML,filePath4, streamName3);
			reader4 = new BeanIOReader(BEANIO_CONFIG_XML,filePath5, streamName4);
			reader5 = new BeanIOReader(BEANIO_CONFIG_XML,filePath6, streamName5);
			reader6 = new BeanIOReader(BEANIO_CONFIG_XML,filePath7, streamName6);
			reader7 = new BeanIOReader(BEANIO_CONFIG_XML,filePath8, streamName7);
			reader8 = new BeanIOReader(BEANIO_CONFIG_XML,filePath9, streamName8);
			reader9 = new BeanIOReader(BEANIO_CONFIG_XML,filePath10, streamName9);
			reader10 = new BeanIOReader(BEANIO_CONFIG_XML,filePath11, streamName10);
			reader11 = new BeanIOReader(BEANIO_CONFIG_XML,filePath12, streamName11);
			reader12 = new BeanIOReader(BEANIO_CONFIG_XML,filePath13, streamName12);
			reader13 = new BeanIOReader(BEANIO_CONFIG_XML,filePath14, streamName13);
			reader14 = new BeanIOReader(BEANIO_CONFIG_XML,filePath15, streamName14);
			reader15 = new BeanIOReader(BEANIO_CONFIG_XML,filePath16, streamName15);
			
			Dao dao = FactoriaDao.getDao(streamName);
			Dao dao1 = FactoriaDao.getDao(streamName1);
			Dao dao2 = FactoriaDao.getDao(streamName2);
			Dao dao3 = FactoriaDao.getDao(streamName3);
			Dao dao4 = FactoriaDao.getDao(streamName4);
			Dao dao5 = FactoriaDao.getDao(streamName5);
			Dao dao6 = FactoriaDao.getDao(streamName6);
			Dao dao7 = FactoriaDao.getDao(streamName7);
			Dao dao8 = FactoriaDao.getDao(streamName8);
			Dao dao9 = FactoriaDao.getDao(streamName9);
			Dao dao10 = FactoriaDao.getDao(streamName10);
			Dao dao11 = FactoriaDao.getDao(streamName11);
			Dao dao12 = FactoriaDao.getDao(streamName12);
			Dao dao13 = FactoriaDao.getDao(streamName13);
			Dao dao14 = FactoriaDao.getDao(streamName14);
			Dao dao15 = FactoriaDao.getDao(streamName15);

			
			dao.loadCache(reader);
			dao1.loadCache(reader1);
			dao2.loadCache(reader2);
			dao3.loadCache(reader3);
			dao4.loadCache(reader4);
			dao5.loadCache(reader5);
			dao6.loadCache(reader6);
			dao7.loadCache(reader7);
			dao8.loadCache(reader8);
			dao9.loadCache(reader9);
			dao10.loadCache(reader10);
			dao11.loadCache(reader11);
			dao12.loadCache(reader12);
			dao13.loadCache(reader13);
			dao14.loadCache(reader14);
			dao15.loadCache(reader15);
			
		} catch (IOException e) {
			GestorIncidenciasGen giGeneral = GestorIncidenciasGen.getInstance();
			LoggerManager logGeneral = LoggerManager.getInstance();
			logGeneral.writeLog("Error al cargar el fichero" + filePath + " - " + e.getMessage(), ConstantesSolvencia.LOG_ERROR, true);
			Incidencia inci = new Incidencia();
			inci.setCodigoRetorno("11");
			inci.setInfAmpliada("Error al cargar el fichero" + filePath + " - " + e.getMessage());
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
}
