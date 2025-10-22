package es.mapfre.solvencia.utils.cacheloader;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import es.mapfre.solvencia.utils.beanio.BeanIOReader;

// FIXME: Refactorizar para emplear el nuevo FactoriaDao
public class BeanIOCacheLoader {
	private String beanioConfigXml = "config/beanio-config.xml";
	private Map<String, String> ficherosDaos = new HashMap<String, String>();
	private Map<String, String> ficherosPaths = new HashMap<String, String>();
	
	public BeanIOCacheLoader(String beanioConfigXml) {
		this.beanioConfigXml = beanioConfigXml;
	}

	public void setInputConfig(String streamName, String filePath, String daoClass) {
		ficherosDaos.put(streamName, daoClass);
		ficherosPaths.put(streamName, filePath);
	}

	public void loadCaches() throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		BeanIOReader reader = new BeanIOReader(beanioConfigXml, null, null);
		for (Map.Entry<String, String> entry : ficherosDaos.entrySet()) {
			Class entidadDaoClass = Class.forName(entry.getValue());
			Constructor constructorDao = entidadDaoClass.getConstructor(new Class[] {});
			Object dao = constructorDao.newInstance();
			
			reader.createReader(ficherosPaths.get(entry.getKey()), entry.getKey());
			
			Method loadCacheMethod = entidadDaoClass.getMethod("loadCache", new Class[] {BeanIOReader.class});
			
			loadCacheMethod.invoke(dao, reader);
		}
		ficherosDaos.clear();
		reader.close();
	}
}
