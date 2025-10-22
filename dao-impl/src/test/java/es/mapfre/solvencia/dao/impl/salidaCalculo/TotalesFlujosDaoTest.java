package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;

public class TotalesFlujosDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(TotalesFlujosDaoTest.class);

//¯\_(ツ)_/¯
//    |
//  _/ \_

	@Test
	public void testEntrySet()  throws Exception{
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		dao.put(totalesF.getKey(), totalesF);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		TotalesFlujosKey key = totalesF.getKey();
		
		dao.put(key, totalesF);
		
		Assert.assertEquals(totalesF, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		TotalesFlujosKey key = totalesF.getKey();
		
		dao.put(key, totalesF);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		dao.put(totalesF.getKey(), totalesF);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		TotalesFlujosDao dao = new TotalesFlujosDao();
		
		Map<TotalesFlujosKey,TotalesFlujos> map = new HashMap<TotalesFlujosKey,TotalesFlujos>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,new Long(i),"aaaaaa",1111);
			map.put(totalesF.getKey(), totalesF);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		TotalesFlujosKey key = totalesF.getKey();
		dao.put(key, totalesF);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		TotalesFlujosDao dao = new TotalesFlujosDao();
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		dao.put(totalesF.getKey(), totalesF);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private TotalesFlujos createTotalesFlujos(Integer kajuste, Integer kcertificado, Integer kgarantia, 
			Integer kmodalidad, Long kpoliza, String kprestacion, Integer ksubpoliza){
		
		TotalesFlujos totalesF = new TotalesFlujos();
		totalesF.setKajuste(kajuste);
		totalesF.setKcertificado(kcertificado);
		totalesF.setKgarantia(kgarantia);
		totalesF.setKmodalidad(kmodalidad);
		totalesF.setKpoliza(kpoliza);
		totalesF.setKprestacion(kprestacion);
		totalesF.setKsubpoliza(ksubpoliza);
		
		return totalesF;
	}
}	
