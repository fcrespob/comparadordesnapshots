package es.mapfre.solvencia.programas;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

public class ProgramaTestTest implements Programa{

	private static Logger log = LoggerFactory.getLogger(ProgramaTestTest.class);
	
	private static final String PROGRAM_NAME = "TEST_TEST";
	
	@Override
	public void execute(Umic umic, FichaProceso fp, DetalleBaseTecnica detalleBaseTecnica, List<DetalleCorriente> detallesCorriente, String subProcesoActual)  {
		log.info("Se ejecuta el subProcesoActual {} con la base técnica {}",subProcesoActual, detalleBaseTecnica.getBaseTec());
	}

	@Override
	public String getNombrePrograma() {
		return PROGRAM_NAME;
	}

	@Override
	public String getNombreServicio() {
		return getNombrePrograma();
	}
}
