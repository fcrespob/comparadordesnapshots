package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TerminosPMCUmicKey;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;

public class TerminosPMCUmicDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(TerminosPMCUmicDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		dao.put(terminosPMC.getKey(), terminosPMC);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		TerminosPMCUmicKey key = terminosPMC.getKey();
		
		dao.put(terminosPMC.getKey(), terminosPMC);
		
		Assert.assertEquals(terminosPMC, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		TerminosPMCUmicKey key = terminosPMC.getKey();
		
		dao.put(terminosPMC.getKey(), terminosPMC);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		dao.put(terminosPMC.getKey(), terminosPMC);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		TerminosPMCUmicKey key = terminosPMC.getKey();
		
		dao.put(key, terminosPMC);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}
	
	@Test
	public void testValues() throws Exception{
		
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		dao.put(terminosPMC.getKey(), terminosPMC);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testGetValues() throws Exception{
		
		TerminosPMCUmicDao dao = new TerminosPMCUmicDao();
		TerminosPMCUmic terminosPMC = createDatos(1);
		
		dao.put(terminosPMC.getKey(), terminosPMC);
		
		UmicKey key = new UmicKey();
		key.setKgarantia(1);
		
		
		Assert.assertTrue(dao.getValues(key, "BTI", 1) == terminosPMC);
		
		dao.clear();
	}
	
	
	
	
	private TerminosPMCUmic createDatos(Integer i){		
		TerminosPMCUmic terminosPMC = new TerminosPMCUmic();
		
		UmicKey key = new UmicKey();
		key.setKgarantia(i);		
		
		terminosPMC.setClaveUmic(key);
		terminosPMC.setBt("BTI");
		terminosPMC.setIteracion(1);
		terminosPMC.setPrima(BigDecimal.ONE);	
		
		return terminosPMC;
	}
	

	
}	
