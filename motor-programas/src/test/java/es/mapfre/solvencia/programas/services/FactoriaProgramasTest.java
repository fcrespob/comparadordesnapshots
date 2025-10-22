package es.mapfre.solvencia.programas.services;

import junit.framework.TestCase;

import org.junit.Test;

import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.services.Servicio;

public class FactoriaProgramasTest extends TestCase {

	@Test
	public void testFactoriaProgramas() {
		
		Programa programaTest = FactoriaProgramas.getPrograma("TEST_TEST");
		
		assertNotNull(programaTest);
		
		Programa programaTest2 = FactoriaProgramas.getPrograma("TEST_TEST");
		
		assertNotNull(programaTest);

		Servicio servicioTest = FactoriaServiciosDummy.getFiltro("TAST");
		
		assertNotNull(servicioTest);
	}
}
