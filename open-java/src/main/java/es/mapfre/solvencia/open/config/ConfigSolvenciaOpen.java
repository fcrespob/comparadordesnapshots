/**
 * 
 */
package es.mapfre.solvencia.open.config;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.exception.SolvenciaRuntimeException;

/**
 * @author amdepedro
 *
 */
public final class ConfigSolvenciaOpen {
	
	private Properties properties = null;

	private static String CONFIG_FILE_NAME;
	
	private static Logger logger = LoggerFactory.getLogger(ConfigSolvenciaOpen.class);

	private ConfigSolvenciaOpen() {
		this.properties = new Properties();
		try {
			String entorno = System.getProperty("entorno.ejecucion", Constantes.ENTORNO_EJECUCION_DEFECTO);
			CONFIG_FILE_NAME = Constantes.SOLVENCIA_OPEN_PROPERTIES + "_" + entorno + "." + Constantes.EXT_PROPERTIES;
			InputStream fileStream = ConfigSolvenciaOpen.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
			if (fileStream != null) {
				properties.load(fileStream);
			} else {
				throw new FileNotFoundException(CONFIG_FILE_NAME);
			}
		} catch (IOException ex) {
			logger.error("Error cargando fichero de configuración " + CONFIG_FILE_NAME, ex);
			properties = null;
		}
	}

	/**
	 * Implementando Singleton
	 * 
	 * @return
	 */
	public static ConfigSolvenciaOpen getInstance() {
		return ConfigurationHolder.INSTANCE;
	}

	private static class ConfigurationHolder {

		private static final ConfigSolvenciaOpen INSTANCE = new ConfigSolvenciaOpen();
	}

	/**
	 * Retorna la propiedad de configuración solicitada
	 * 
	 * @param key
	 * @return
	 * @throws IOException 
	 */
	public String getProperty(String key) {
		if (this.properties == null) {
			throw new SolvenciaRuntimeException("Properties file not found");
		}
		return this.properties.getProperty(key);
	}
	
	/**
	 * Retorna la propiedad de configuración solicitada
	 * 
	 * @param key
	 * @param defaultKey
	 * @return
	 * @throws IOException 
	 */
	public String getProperty(String key, String defaultKey) {
		if (this.properties == null) {
			throw new SolvenciaRuntimeException("Properties file not found");
		}
		return this.properties.getProperty(key, defaultKey);
	}

}
