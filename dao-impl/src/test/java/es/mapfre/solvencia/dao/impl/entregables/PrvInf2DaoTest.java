package es.mapfre.solvencia.dao.impl.entregables;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf2Key;
import es.mapfre.solvencia.dominio.entregables.PrvInf2;

public class PrvInf2DaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(PrvInf2DaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);
		
		dao.put(prvInf2.getKey(), prvInf2);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}
	
	@Test
	public void testGet()  throws Exception{
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);
		
		PrvInf2Key key = prvInf2.getKey();
		
		dao.put(key, prvInf2);
		
		Assert.assertEquals(prvInf2, dao.get(key));
				
		dao.clear();		
	}
	
	@Test
	public void testKeySet() throws Exception {
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);

		PrvInf2Key key = prvInf2.getKey();
		
		dao.put(key, prvInf2);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();
		
	}
	
	@Test
	public void testPut() throws Exception {
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);
		
		dao.put(prvInf2.getKey(), prvInf2);
		
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.clear();	
	}
	
	@Test
	public void testPutAll() throws Exception {
		PrvInf2Dao dao = new PrvInf2Dao();
		
		Map<PrvInf2Key,PrvInf2> map = new HashMap<PrvInf2Key,PrvInf2>();
		for ( int i = 0; i < 10; i++) {
			PrvInf2 prvInf2 = createPrvInf2("BTI",new Integer(i),"I",70);
			map.put(prvInf2.getKey(), prvInf2);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();
		
	}
	
	
	@Test
	public void Remove() throws Exception {
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);
		
		dao.put(prvInf2.getKey(), prvInf2);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(prvInf2.getKey());
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}
	
	
	@Test
	public void Values() throws Exception {
		PrvInf2Dao dao = new PrvInf2Dao();
		PrvInf2 prvInf2 = createPrvInf2("BTI",1,"I",70);
		
		dao.put(prvInf2.getKey(), prvInf2);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	private PrvInf2 createPrvInf2(String bt, Integer ccanal, String cnegocio, Integer kmodalidad){		
		PrvInf2 prvInf2 = new PrvInf2();
		prvInf2.setBt(bt);
		prvInf2.setCcanal(ccanal);
		//prvInf1.setCcartera(ccartera);
		prvInf2.setCnegocio(cnegocio);
		prvInf2.setKmodalidad(kmodalidad);
		
		return prvInf2;
	}

	
}	
