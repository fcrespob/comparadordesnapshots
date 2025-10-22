package es.mapfre.solvencia.entregables.services;

import java.util.Map;

import es.mapfre.solvencia.entregables.Entregable;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaEntregables extends FactoriaServicios<Entregable> {

	private static Map<String, Entregable> entregable = null;
	
	private static FactoriaEntregables instance;
	
	protected FactoriaEntregables() {
		super(Entregable.class);
	}

	static {
		instance = new FactoriaEntregables();
	}
	
	/**
	 * Devuelve un entregable en función del nombre del entregable solicitado
	 * @param nombreEntregable
	 * @return
	 */
	public static Entregable getEntregable(String nombreEntregable) {
		return (Entregable) instance.getServicio(nombreEntregable, Boolean.TRUE);
	}
	
	
	@Override
	protected Map<String, Entregable> getServicios() {
		return FactoriaEntregables.entregable;
	}

	@Override
	protected void setServicios(Map<String, Entregable> entregable) {
		FactoriaEntregables.entregable = entregable;
		
	}

}
