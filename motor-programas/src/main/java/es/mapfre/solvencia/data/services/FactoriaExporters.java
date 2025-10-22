package es.mapfre.solvencia.data.services;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.data.Exporter;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaExporters extends FactoriaServicios<Exporter> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaExporters.class);
	
	private static Map<String, Exporter> exporters = null;

	private static FactoriaExporters instance;
	
	private FactoriaExporters() {
		super(Exporter.class);
	}

	static {
		instance = new FactoriaExporters();
	}
	
	/**
	 * Devuelve un Exporter en función del nombre de Exporter solicitado
	 * 
	 * @param nombreExporter
	 * @return el Exporter buscado
	 */
	public static Exporter getExporter(String nombreExporter) {
		return (Exporter) instance.getServicio(nombreExporter, Boolean.FALSE);
	}

	/**
	 * Devuelve todos los Exporter menos los Exporter indicados
	 * 
	 * @param nombreExporters
	 * @return los Exporter buscados
	 */
	public static List<Exporter> getExportersFiltrado(List<String> nombreExporters) {
		return instance.getServiciosFiltrado(nombreExporters);
	}
	
	@Override
	protected Map<String, Exporter> getServicios() {
		return FactoriaExporters.exporters;
	}
	
	@Override
	protected void setServicios(Map<String, Exporter> exporters) {
		FactoriaExporters.exporters = exporters;
	}
}
