package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;

public class FlujosProbablesDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(FlujosProbablesDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		dao.put(flu.getKey(), flu);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		FlujosProbablesKey key = flu.getKey();
		
		dao.put(key, flu);
		
		Assert.assertEquals(flu, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		FlujosProbablesKey key = flu.getKey();
		
		dao.put(key, flu);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		dao.put(flu.getKey(), flu);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		FlujosProbablesDao dao = new FlujosProbablesDao();
		
		Map<FlujosProbablesKey,FlujosProbables> map = new HashMap<FlujosProbablesKey,FlujosProbables>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			FlujosProbables flu = createFlujosProbables(i);
			map.put(flu.getKey(), flu);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		FlujosProbablesKey key = flu.getKey();
		dao.put(key, flu);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		FlujosProbablesDao dao = new FlujosProbablesDao();
		FlujosProbables flu = createFlujosProbables(1);
		
		dao.put(flu.getKey(), flu);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private FlujosProbables createFlujosProbables(Integer index){
		
		FlujosProbables flu = new FlujosProbables();
		flu.setModalidad(index);
		flu.setGarantia(index);
		flu.setPrestacion("prestacion"+index);
		flu.setBasetecnica("basetecnica"+index);
		
		return flu;
	}
}	