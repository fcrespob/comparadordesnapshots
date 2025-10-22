package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProcesos;

public class DefinicionProcesosDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DefinicionProcesosDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		dao.put(defPro.getKey(), defPro);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		String key = defPro.getKey();
		
		dao.put(key, defPro);
		
		Assert.assertEquals(defPro, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		String key = defPro.getKey();
		
		dao.put(key, defPro);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		dao.put(defPro.getKey(), defPro);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		
		Map<String,DefinicionProcesos> map = new HashMap<String,DefinicionProcesos>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DefinicionProcesos defPro = createDefinicionProcesos(i, true);
			map.put(defPro.getKey(), defPro);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		String key = defPro.getKey();
		dao.put(key, defPro);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DefinicionProcesosDao dao = new DefinicionProcesosDao();
		DefinicionProcesos defPro = createDefinicionProcesos(1, true);
		
		dao.put(defPro.getKey(), defPro);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private DefinicionProcesos createDefinicionProcesos(Integer index, boolean kestado){
		
		DefinicionProcesos defPro = new DefinicionProcesos();
		defPro.setCdescripabrev("cdescripabrev"+index);
		defPro.setCproceso("cproceso"+index);
		defPro.setGdescrip("gdescrip"+index);
		defPro.setGrutadoc("grutadoc"+index);
		defPro.setKestado(kestado);
		
		return defPro;
	}
}	
