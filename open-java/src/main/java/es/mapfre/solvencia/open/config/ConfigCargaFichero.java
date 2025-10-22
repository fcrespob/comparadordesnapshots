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
public final class ConfigCargaFichero {
	
	private Properties properties = null;

	private static final  String CONFIG_FILE_NAME = Constantes.CARGA_FICHEROS_PROPERTIES;
	
	private static Logger logger = LoggerFactory.getLogger(ConfigCargaFichero.class);

	private ConfigCargaFichero() {
		this.properties = new Properties();
		try {
			properties.load(ConfigCargaFichero.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME));
		} catch (IOException ex) {
			logger.error("Error cargando fichero de configuración " + CONFIG_FILE_NAME, ex);
		}
	}

	/**
	 * Implementando Singleton
	 * 
	 * @return
	 */
	public static ConfigCargaFichero getInstance() {
		return ConfigurationHolder.INSTANCE;
	}

	private static class ConfigurationHolder {

		private static final ConfigCargaFichero INSTANCE = new ConfigCargaFichero();
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
