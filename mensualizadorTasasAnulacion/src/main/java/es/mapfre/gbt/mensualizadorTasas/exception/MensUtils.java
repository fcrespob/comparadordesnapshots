package es.mapfre.gbt.mensualizadorTasas.exception;

import java.io.IOException;
import java.util.Properties;


public class MensUtils {

	public Properties getProperties(String fichero) throws IOException{
		
		Properties properties = null;
		
		try {
			properties = new Properties();
			properties.load(ClassLoader.getSystemResourceAsStream(fichero));
		} catch (IOException e) {
			throw e;
		}
		
		return properties;
	}
	
}
