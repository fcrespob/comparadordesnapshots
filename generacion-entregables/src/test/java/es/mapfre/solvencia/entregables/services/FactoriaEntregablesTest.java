package es.mapfre.solvencia.entregables.services;

import junit.framework.TestCase;
import org.junit.Test;
import es.mapfre.solvencia.entregables.Entregable;

public class FactoriaEntregablesTest extends TestCase {
	
	@Test
	public void testFactoriaEntregables() {
		
		Entregable entregableTest = FactoriaEntregables.getEntregable("TEST");
		
		assertNotNull(entregableTest);
	
	}
}
