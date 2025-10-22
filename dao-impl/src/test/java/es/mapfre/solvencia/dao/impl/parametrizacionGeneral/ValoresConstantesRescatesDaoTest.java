package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresConstantesRescateKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;

public class ValoresConstantesRescatesDaoTest extends TestCase {
		
	@Test
	public void testEntrySet()  throws Exception{
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		dao.put(defCons.getKey(), defCons);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		ValoresConstantesRescateKey key = defCons.getKey();
		
		dao.put(key, defCons);
		
		Assert.assertEquals(defCons, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		ValoresConstantesRescateKey key = defCons.getKey();
		
		dao.put(key, defCons);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		dao.put(defCons.getKey(), defCons);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		
		Map<ValoresConstantesRescateKey,ValoresConstantesRescate> map = new HashMap<ValoresConstantesRescateKey,ValoresConstantesRescate>();
		for ( int i = 0; i < 10; i++) {
			ValoresConstantesRescate defCons = createValoresConstantesRescate(i);
			map.put(defCons.getKey(), defCons);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		ValoresConstantesRescateKey key = defCons.getKey();
		dao.put(key, defCons);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ValoresConstantesRescatesDao dao = new ValoresConstantesRescatesDao();
		ValoresConstantesRescate defCons = createValoresConstantesRescate(1);
		
		dao.put(defCons.getKey(), defCons);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private ValoresConstantesRescate createValoresConstantesRescate(Integer index){
		
		ValoresConstantesRescate defCons = new ValoresConstantesRescate();
		defCons.setKduracion(index);
		defCons.setKk1(String.valueOf(index));
		defCons.setPorckonst(new BigDecimal(index));
		return defCons;
	}
}