package es.mapfre.solvencia.dao.impl;

import java.io.IOException;
import java.util.Properties;


public class PropertiesDao extends DefaultDao<String, Object>{

	private static final String CACHE_NAME = "propiedades"; 
	
	public PropertiesDao() {
		super(CACHE_NAME);
		
	}
	
	public PropertiesDao(String cacheName) {
		super(cacheName);
	}
	
	public void addProperties(String fichero) throws IOException{
		
		Properties properties = new Properties();
		
		properties.load(ClassLoader.getSystemResourceAsStream(fichero));

		this.putAll(properties);
	}

	@Override
	public Object get(Object key) {
		
		if(!(key instanceof String)){
			return null;
		}
		
		return super.get(key);
	}
	
	
	

}
