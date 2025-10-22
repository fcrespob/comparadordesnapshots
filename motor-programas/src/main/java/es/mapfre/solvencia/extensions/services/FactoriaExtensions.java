package es.mapfre.solvencia.extensions.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.extensions.Extension;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaExtensions extends FactoriaServicios<Extension> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaExtensions.class);
	
	private static Map<String, Extension> extensions = null;

	private static FactoriaExtensions instance;
	
	private FactoriaExtensions() {
		super(Extension.class);
	}

	static {
		instance = new FactoriaExtensions();
	}
	
	/**
	 * Devuelve un Extension en función del nombre de Extension solicitado
	 * 
	 * @param nombreExtension
	 * @return el Extension buscado
	 */
	public static Extension getExtension(String nombreExtension) {
		return (Extension) instance.getServicio(nombreExtension, Boolean.FALSE);
	}
	
	public static Map<String, Extension> getExtensions() {
		return instance.getServicios();
	}

	@Override
	protected Map<String, Extension> getServicios() {
		return FactoriaExtensions.extensions;
	}
	
	@Override
	protected void setServicios(Map<String, Extension> extensions) {
		FactoriaExtensions.extensions = extensions;
	}
}
