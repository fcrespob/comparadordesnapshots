package es.mapfre.solvencia.programas.services;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.services.FactoriaServicios;

public class FactoriaProgramas extends FactoriaServicios<Programa> {

	private static Logger log = LoggerFactory
			.getLogger(FactoriaProgramas.class);
	
	private static Map<String, Programa> programas = null;

	private static FactoriaProgramas instance;
	
	private FactoriaProgramas() {
		super(Programa.class);
	}

	static {
		instance = new FactoriaProgramas();
	}
	
	/**
	 * Devuelve un Programa en función del nombre de programa solicitado
	 * 
	 * @param nombrePrograma
	 * @return el programa buscado
	 */
	public static Programa getPrograma(String nombrePrograma) {
		return (Programa) instance.getServicio(nombrePrograma, Boolean.TRUE);
	}

	@Override
	protected Map<String, Programa> getServicios() {
		return FactoriaProgramas.programas;
	}
	
	@Override
	protected void setServicios(Map<String, Programa> programas) {
		FactoriaProgramas.programas = programas;
	}
}
