package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesConstantesRescatesKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesConstantesRescates;

public class DefinicionesConstantesRescatesDaoTest extends TestCase {
		
	@Test
	public void testEntrySet()  throws Exception{
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		dao.put(defCons.getKey(), defCons);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		DefinicionesConstantesRescatesKey key = defCons.getKey();
		
		dao.put(key, defCons);
		
		Assert.assertEquals(defCons, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		DefinicionesConstantesRescatesKey key = defCons.getKey();
		
		dao.put(key, defCons);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		dao.put(defCons.getKey(), defCons);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		
		Map<DefinicionesConstantesRescatesKey,DefinicionesConstantesRescates> map = new HashMap<DefinicionesConstantesRescatesKey,DefinicionesConstantesRescates>();
		for ( int i = 0; i < 10; i++) {
			DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(i);
			map.put(defCons.getKey(), defCons);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		DefinicionesConstantesRescatesKey key = defCons.getKey();
		dao.put(key, defCons);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DefinicionesConstantesRescatesDao dao = new DefinicionesConstantesRescatesDao();
		DefinicionesConstantesRescates defCons = createDefinicionesConstantesRescates(1);
		
		dao.put(defCons.getKey(), defCons);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private DefinicionesConstantesRescates createDefinicionesConstantesRescates(Integer index){
		
		DefinicionesConstantesRescates defCons = new DefinicionesConstantesRescates();
		defCons.setDescripcion(String.valueOf(index));
		defCons.setKconstante(String.valueOf(index));
		return defCons;
	}
}