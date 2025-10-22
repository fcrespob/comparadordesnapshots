package es.mapfre.solvencia.filtros;

import java.util.Map;

import es.mapfre.solvencia.services.FactoriaServicios;

public final class FactoriaFiltros extends FactoriaServicios<Filtro> {
		
	private static Map<String, Filtro> filtros = null;

	private static FactoriaFiltros instance;
	
	private FactoriaFiltros() {
		super(Filtro.class);
	}

	static {
		instance = new FactoriaFiltros();
	}
	
	/**
	 * Devuelve un Filtro en función del identificador del filtro solicitado
	 * 
	 * @param idFiltro
	 * @return el filtro buscado
	 */
	public static Filtro getFiltro(String idFiltro) {
		return (Filtro) instance.getServicio(idFiltro);
	}

	@Override
	protected Map<String, Filtro> getServicios() {
		return FactoriaFiltros.filtros;
	}
	
	@Override
	protected void setServicios(Map<String, Filtro> filtros) {
		FactoriaFiltros.filtros = filtros;
	}
}