package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionProgramasKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProgramas;

public class DefinicionProgramasDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DefinicionProgramasDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		dao.put(defPro.getKey(), defPro);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		DefinicionProgramasKey key = defPro.getKey();
		
		dao.put(key, defPro);
		
		Assert.assertEquals(defPro, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		DefinicionProgramasKey key = defPro.getKey();
		
		dao.put(key, defPro);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		dao.put(defPro.getKey(), defPro);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		
		Map<DefinicionProgramasKey,DefinicionProgramas> map = new HashMap<DefinicionProgramasKey,DefinicionProgramas>();
		for ( int i = 0; i < 10; i++) {
			DefinicionProgramas defPro = createDefinicionProgramas(i, true);
			map.put(defPro.getKey(), defPro);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		DefinicionProgramasKey key = defPro.getKey();
		dao.put(key, defPro);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DefinicionProgramasDao dao = new DefinicionProgramasDao();
		DefinicionProgramas defPro = createDefinicionProgramas(1, true);
		
		dao.put(defPro.getKey(), defPro);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private DefinicionProgramas createDefinicionProgramas(Integer index, boolean kestado){
		
		DefinicionProgramas defPro = new DefinicionProgramas();
		defPro.setCidentif("cidentif"+index);
		defPro.setGdescrip("gdescrip"+index);
		defPro.setGrutadoc("grutadoc"+index);
		defPro.setKestado(kestado);
		defPro.setKmodulo("kmodulo"+index);
		return defPro;
	}
}	
