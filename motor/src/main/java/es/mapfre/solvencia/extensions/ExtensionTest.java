package es.mapfre.solvencia.extensions;

import java.util.HashMap;
import java.util.Map;

public class ExtensionTest implements Extension {
	private final static String NOMBRE_SERVICIO = "EXTENSION_TEST";
	
	@Override
	public String getNombreServicio() {
		return NOMBRE_SERVICIO;
	}

	@Override
	public Map<String, Object> getExtensionConfig() {
		Map<String, Object> config = new HashMap<String, Object>();
		//config.put(Extension.CACHE_CONFIG_FILE, "cache/solvencia-salidaCalculo-cache-config.xml");
		return config;
	}

}
