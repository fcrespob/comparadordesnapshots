package es.mapfre.solvencia.cargaFicherosGBT;

import java.util.HashMap;
import java.util.Map;

import es.mapfre.solvencia.extensions.Extension;

public class ExtensionGBT implements Extension {

	private final static String NOMBRE_SERVICIO = "gbt";
	
	@Override
	public String getNombreServicio() {
		return NOMBRE_SERVICIO;
	}

	@Override
	public Map<String, Object> getExtensionConfig() {
		Map<String, Object> config = new HashMap<String, Object>();
		config.put(Extension.CACHE_CONFIG_FILE, "cache/gbt-cache-config.xml,cache/gbt-schemes-config.xml");
		return config;
	}

}
