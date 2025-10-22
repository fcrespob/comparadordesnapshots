/**
 * 
 */
package es.mapfre.solvencia.open.config;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.open.comun.Constantes;

/**
 * @author amdepedro
 *
 */
public final class ConfigInstancias {
	
	private Properties properties = null;

	private static final  String CONFIG_FILE_NAME = Constantes.INSTANCIAS_PROPERTIES;
	
	private static Logger logger = LoggerFactory.getLogger(ConfigInstancias.class);

	private ConfigInstancias() {
		this.properties = new Properties();
		try {
			properties.load(ConfigInstancias.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
		} catch (IOException ex) {
			logger.error("Error cargando fichero de configuración " + CONFIG_FILE_NAME, ex);
		}
	}

	/**
	 * Implementando Singleton
	 * 
	 * @return
	 */
	public static ConfigInstancias getInstance() {
		return ConfigurationHolder.INSTANCE;
	}

	private static class ConfigurationHolder {

		private static final ConfigInstancias INSTANCE = new ConfigInstancias();
	}

	/**
	 * Retorna la propiedad de configuración solicitada
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return this.properties.getProperty(key);
	}
	
	/**
	 * Retorna la propiedad de configuración solicitada
	 * 
	 * @param key
	 * @param defaultKey
	 * @return
	 */
	public String getProperty(String key, String defaultKey) {
		return this.properties.getProperty(key, defaultKey);
	}

}
