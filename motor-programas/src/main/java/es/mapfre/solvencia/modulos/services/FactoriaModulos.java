package es.mapfre.solvencia.modulos.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaModulos extends FactoriaServicios<Modulo> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaModulos.class);
	
	private static Map<String, Modulo> modulos = null;

	private static FactoriaModulos instance;
	
	private FactoriaModulos() {
		super(Modulo.class);
	}

	static {
		instance = new FactoriaModulos();
	}
	
	/**
	 * Devuelve un Módulo en función del nombre de módulo solicitado
	 * 
	 * @param nombreModulo
	 * @return el modulo buscado
	 */
	public static Modulo getModulo(String nombreModulo) {
		return (Modulo) instance.getServicio(nombreModulo, Boolean.FALSE);
	}

	@Override
	protected Map<String, Modulo> getServicios() {
		return FactoriaModulos.modulos;
	}
	
	@Override
	protected void setServicios(Map<String, Modulo> modulos) {
		FactoriaModulos.modulos = modulos;
	}
}
