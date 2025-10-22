package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CurvasTipo656Key;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CurvasTipo656;

public class CurvasTipo656DaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(CurvasTipo656DaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(ct656.getKey(), ct656);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		CurvasTipo656Key key = ct656.getKey();
		
		dao.put(key, ct656);
		
		Assert.assertEquals(ct656, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		CurvasTipo656Key key = ct656.getKey();
		
		dao.put(key, ct656);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(ct656.getKey(), ct656);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		
		Map<CurvasTipo656Key,CurvasTipo656> map = new HashMap<CurvasTipo656Key,CurvasTipo656>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			CurvasTipo656 ct656 = createCurvasTipo656(i, new Timestamp(addedDate+i));
			map.put(ct656.getKey(), ct656);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		CurvasTipo656Key key = ct656.getKey();
		dao.put(key, ct656);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		CurvasTipo656Dao dao = new CurvasTipo656Dao();
		CurvasTipo656 ct656 = createCurvasTipo656(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(ct656.getKey(), ct656);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private CurvasTipo656 createCurvasTipo656(Integer index, Timestamp fecha){
		
		CurvasTipo656 ct656 = new CurvasTipo656();
		ct656.setCarterainv("carterainv"+index);
		ct656.setDiasplazo(index);
		ct656.setFecha(fecha);
		ct656.setKplazo("plazo"+index);
		ct656.setPinteres(new java.math.BigDecimal(new Double(0.05 * index).toString()));
		
		return ct656;
	}
}