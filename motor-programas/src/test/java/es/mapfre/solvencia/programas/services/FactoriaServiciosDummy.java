package es.mapfre.solvencia.programas.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.services.FactoriaServicios;
import es.mapfre.solvencia.services.Servicio;

public class FactoriaServiciosDummy extends FactoriaServicios<Servicio> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaServiciosDummy.class);
	
	private static Map<String, Servicio> servicios = null;
	
	private static FactoriaServiciosDummy instance;
	
	public FactoriaServiciosDummy() {
		super(Servicio.class);
	}

	static {
		instance = new FactoriaServiciosDummy();
	}
	
	/**
	 * Devuelve un Programa en función del nombre de programa solicitado
	 * 
	 * @param nombrePrograma
	 * @return el programa buscado
	 */
	public static Servicio getFiltro(String nombreFiltro) {
		return (Servicio) instance.getServicio(nombreFiltro);
	}

	@Override
	protected Map<String, Servicio> getServicios() {
		return FactoriaServiciosDummy.servicios;
	}
	
	@Override
	protected void setServicios(Map<String, Servicio> servicios) {
		FactoriaServiciosDummy.servicios = servicios;
	}
}
