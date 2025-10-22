package es.mapfre.solvencia.dao.impl.entregables;

import java.sql.Timestamp;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.entregables.PrvUmicKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;

public class PrvUmicDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(PrvUmicDaoTest.class);

	private String bt = "BTI";
	private Timestamp fcierre = new Timestamp(System.currentTimeMillis());
	private UmicKey umicKey = new UmicKey("P", 1, 1, 1, 1, Long.valueOf(1),"1", 1, 1, 1);
	
	@Test
	public void testEntrySet()  throws Exception{
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
		
		dao.put(PrvUmic.getKey(), PrvUmic);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}
	
	@Test
	public void testGet()  throws Exception{
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
		
		PrvUmicKey key = PrvUmic.getKey();
		
		dao.put(key, PrvUmic);
		
		Assert.assertEquals(PrvUmic, dao.get(key));
				
		dao.clear();		
	}
	
	@Test
	public void testKeySet() throws Exception {
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);

		PrvUmicKey key = PrvUmic.getKey();
		
		dao.put(key, PrvUmic);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();
		
	}
	
	
	@Test
	public void testPut() throws Exception {
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
		
		dao.put(PrvUmic.getKey(), PrvUmic);
		
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.clear();	
	}
	
	@Test
	public void Remove() throws Exception {
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
		
		dao.put(PrvUmic.getKey(), PrvUmic);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(PrvUmic.getKey());
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}
	
	
//	@Test
//	public void testPutAll() throws Exception {
//		PrvUmicDao dao = new PrvUmicDao();
//		
//		Map<PrvUmicKey,PrvUmic> map = new HashMap<PrvUmicKey,PrvUmic>();
//		for ( int i = 0; i < 10; i++) {
//			PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
//			map.put(PrvUmic.getKey(), PrvUmic);
//		}
//		
//		dao.putAll(map);
//		
//		Assert.assertTrue(dao.size() == 10);
//				
//		dao.clear();
//		
//	}
//	
	

	@Test
	public void Values() throws Exception {
		PrvUmicDao dao = new PrvUmicDao();
		PrvUmic PrvUmic = createPrvUmic(bt, fcierre, umicKey);
		
		dao.put(PrvUmic.getKey(), PrvUmic);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	private PrvUmic createPrvUmic(String bt, Timestamp fcierre, UmicKey umicKey){		
		PrvUmic PrvUmic = new PrvUmic();
		PrvUmic.setBt(bt);
		PrvUmic.setFeccierre(fcierre);
		PrvUmic.setUmicKey(umicKey);		
		
		return PrvUmic;
	}

	
}	
