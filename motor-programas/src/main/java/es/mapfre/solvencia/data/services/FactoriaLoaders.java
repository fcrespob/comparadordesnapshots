package es.mapfre.solvencia.data.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.data.Loader;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaLoaders extends FactoriaServicios<Loader> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaLoaders.class);
	
	private static Map<String, Loader> loaders = null;

	private static FactoriaLoaders instance;
	
	private FactoriaLoaders() {
		super(Loader.class);
	}

	static {
		instance = new FactoriaLoaders();
	}
	
	/**
	 * Devuelve un Loader en función del nombre de Loader solicitado
	 * 
	 * @param nombreLoader
	 * @return el Loader buscado
	 */
	public static Loader getLoader(String nombreLoader) {
		return (Loader) instance.getServicio(nombreLoader, Boolean.FALSE);
	}
	
	/**
	 * Devuelve todos los Loader menos los Loader indicados
	 * 
	 * @param nombreLoaders
	 * @return los Loader buscados
	 */
	public static List<Loader> getLoadersFiltrado(List<String> nombreLoaders) {
		return instance.getServiciosFiltrado(nombreLoaders);
	}

	@Override
	protected Map<String, Loader> getServicios() {
		return FactoriaLoaders.loaders;
	}
	
	@Override
	protected void setServicios(Map<String, Loader> loaders) {
		FactoriaLoaders.loaders = loaders;
	}
}
