package es.mapfre.solvencia.dao.impl.entregables;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.dominio.entregables.PrvInf1;

public class PrvInf1DaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(PrvInf1DaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
		
		dao.put(prvInf1.getKey(), prvInf1);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}
	
	@Test
	public void testGet()  throws Exception{
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
		
		PrvInf1Key key = prvInf1.getKey();
		
		dao.put(key, prvInf1);
		
		Assert.assertEquals(prvInf1, dao.get(key));
				
		dao.clear();		
	}
	
	@Test
	public void testKeySet() throws Exception {
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));

		PrvInf1Key key = prvInf1.getKey();
		
		dao.put(key, prvInf1);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();
		
	}
	
	@Test
	public void testPut() throws Exception {
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
		
		dao.put(prvInf1.getKey(), prvInf1);
		
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.clear();	
	}
	
	@Test
	public void testPutAll() throws Exception {
		PrvInf1Dao dao = new PrvInf1Dao();
		
		Map<PrvInf1Key,PrvInf1> map = new HashMap<PrvInf1Key,PrvInf1>();
		for ( int i = 0; i < 10; i++) {
			PrvInf1 prvInf1 = createPrvInf1("BTI",new Integer(i),"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
			map.put(prvInf1.getKey(), prvInf1);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();
		
	}
	
	
	@Test
	public void Remove() throws Exception {
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
		
		dao.put(prvInf1.getKey(), prvInf1);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(prvInf1.getKey());
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}
	
	
	@Test
	public void Values() throws Exception {
		PrvInf1Dao dao = new PrvInf1Dao();
		PrvInf1 prvInf1 = createPrvInf1("BTI",1,"I",70,BigDecimal.valueOf(20),BigDecimal.valueOf(30));
		
		dao.put(prvInf1.getKey(), prvInf1);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	private PrvInf1 createPrvInf1(String bt, Integer ccanal, String cnegocio, Integer kmodalidad,
			BigDecimal pfpInv, BigDecimal totProvi){
		
		PrvInf1 prvInf1 = new PrvInf1();
		prvInf1.setBt(bt);
		prvInf1.setCcanal(ccanal);
		//prvInf1.setCcartera(ccartera);
		prvInf1.setCnegocio(cnegocio);
		prvInf1.setKmodalidad(kmodalidad);
		prvInf1.setPfpInv(pfpInv);
		prvInf1.setTotProvi(totProvi);
		
		return prvInf1;
	}

	
}	
