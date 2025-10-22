package es.mapfre.solvencia.programas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.services.Servicio;

public class ServicioTest implements Servicio {

	private static Logger log = LoggerFactory.getLogger(ServicioTest.class);
	
	private static final String PROGRAM_NAME = "TAST";
	
	@Override
	public String getNombreServicio() {
		return PROGRAM_NAME;
	}
}
