package es.mapfre.solvencia.extensions;

import java.util.Map;

import es.mapfre.solvencia.services.Servicio;

public interface Extension extends Servicio {

	static final String CACHE_CONFIG_FILE = "solvencia.cache.config.files";
			
	/**
	 * Devuelve los parámetros de configuración necesarios para la extensión.
	 * 
	 * @return Mapa con clave-valor de la configuración de la extensión
	 */
	Map<String, Object> getExtensionConfig();
}
