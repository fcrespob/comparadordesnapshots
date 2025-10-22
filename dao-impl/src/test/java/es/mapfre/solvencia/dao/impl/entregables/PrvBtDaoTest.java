package es.mapfre.solvencia.dao.impl.entregables;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.entregables.PrvBtKey;
import es.mapfre.solvencia.dominio.entregables.PrvBt;

public class PrvBtDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(PrvBtDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		PrvBtDao dao = new PrvBtDao();
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);
		
		dao.put(prvBt.getKey(), prvBt);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}
	
	
	@Test
	public void testGet()  throws Exception{
		PrvBtDao dao = new PrvBtDao();
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);
		
		PrvBtKey key = prvBt.getKey();
		
		dao.put(key, prvBt);
		
		Assert.assertEquals(prvBt, dao.get(key));
				
		dao.clear();		
	}
	
	
	@Test
	public void testKeySet() throws Exception {
		PrvBtDao dao = new PrvBtDao();
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);

		PrvBtKey key = prvBt.getKey();
		
		dao.put(key, prvBt);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();
	}
	
	@Test
	public void testPut() throws Exception {
		PrvBtDao dao = new PrvBtDao();
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);
		
		dao.put(prvBt.getKey(), prvBt);
		
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.clear();	
	}
	
	
	@Test
	public void testPutAll() throws Exception {
		PrvBtDao dao = new PrvBtDao();
		
		Map<PrvBtKey,PrvBt> map = new HashMap<PrvBtKey,PrvBt>();
		for ( int i = 0; i < 10; i++) {
			PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),i,1,Long.valueOf(123456),2,1);
			map.put(prvBt.getKey(), prvBt);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();
		
	}
	
	@Test
	public void Remove() throws Exception {
		PrvBtDao dao = new PrvBtDao();
		
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);
		
		dao.put(prvBt.getKey(), prvBt);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(prvBt.getKey());
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}
	
	@Test
	public void Values() throws Exception {
		PrvBtDao dao = new PrvBtDao();
		
		PrvBt prvBt = createPrvBt("BTI",new Timestamp(0L),320,1,Long.valueOf(123456),2,1);
		
		dao.put(prvBt.getKey(), prvBt);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	private PrvBt createPrvBt(String bt, Timestamp feccierre, Integer kmodalidad, Integer kgarantia, Long kpoliza,
			Integer ksubpoliza, Integer nsuscri){
		
		PrvBt prvBt = new PrvBt();
		prvBt.setBt(bt);
		prvBt.setFeccierre(feccierre);
		prvBt.setKmodalidad(kmodalidad);
		prvBt.setKgarantia(kgarantia);
		prvBt.setKpoliza(kpoliza);
		prvBt.setKsubpoliza(ksubpoliza);
		prvBt.setNsuscri(nsuscri);		
	
		return prvBt;
	}
	
}	
