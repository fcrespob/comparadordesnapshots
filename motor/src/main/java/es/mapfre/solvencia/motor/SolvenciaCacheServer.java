package es.mapfre.solvencia.motor;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.DefaultCacheServer;

import es.mapfre.solvencia.extensions.Extension;
import es.mapfre.solvencia.extensions.services.FactoriaExtensions;

public class SolvenciaCacheServer extends DefaultCacheServer {

    private static Logger log = LoggerFactory.getLogger(SolvenciaCacheServer.class);
    
	public SolvenciaCacheServer(ConfigurableCacheFactory factory) {
		super(factory);
	}

	public static void main(String[] args) {
		log.info("Arrancando Motor de Cálculo...");
		
		Map<String, Extension> extensiones = FactoriaExtensions.getExtensions();
		String cacheConfigFiles = System.getProperty(Extension.CACHE_CONFIG_FILE, "");
		
		if (extensiones != null && extensiones.size() > 0) {
			for (Extension extension : extensiones.values()) {
				Map<String, Object> config = extension.getExtensionConfig();
				if (config != null) {
					String cacheConfigFile = (String) config.get(Extension.CACHE_CONFIG_FILE);
					if (cacheConfigFile != null) {
						if (cacheConfigFiles.length() > 0) {
							cacheConfigFiles = cacheConfigFiles.concat(",");
						}
						cacheConfigFiles = cacheConfigFiles.concat(cacheConfigFile);
						log.info("La extensión {} emplea el fichero de configuración de Coherence {}", extension.getNombreServicio(), cacheConfigFile);
					}
				}
			}
			System.setProperty(Extension.CACHE_CONFIG_FILE, cacheConfigFiles);
		}
		// Arrancamos el servidor de caché
		DefaultCacheServer.main(args);
	}
}
