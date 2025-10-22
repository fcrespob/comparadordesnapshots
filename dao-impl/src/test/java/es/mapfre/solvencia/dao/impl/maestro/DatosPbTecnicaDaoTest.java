package es.mapfre.solvencia.dao.impl.maestro;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.DatosPbTecnicaKey;
import es.mapfre.solvencia.dominio.maestro.DatosPbTecnica;

public class DatosPbTecnicaDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DatosPbTecnicaDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		dao.put(DatosPbTecnica.getKey(), DatosPbTecnica);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		DatosPbTecnicaKey key = DatosPbTecnica.getKey();
		
		dao.put(key, DatosPbTecnica);
		
		Assert.assertEquals(DatosPbTecnica, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		DatosPbTecnicaKey key = DatosPbTecnica.getKey();
		
		dao.put(key, DatosPbTecnica);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		dao.put(DatosPbTecnica.getKey(), DatosPbTecnica);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		
		Map<DatosPbTecnicaKey,DatosPbTecnica> map = new HashMap<DatosPbTecnicaKey,DatosPbTecnica>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(i);
			map.put(DatosPbTecnica.getKey(), DatosPbTecnica);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		DatosPbTecnicaKey key = DatosPbTecnica.getKey();
		dao.put(key, DatosPbTecnica);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DatosPbTecnicaDao dao = new DatosPbTecnicaDao();
		DatosPbTecnica DatosPbTecnica = createDatosPbTecnica(1);
		
		dao.put(DatosPbTecnica.getKey(), DatosPbTecnica);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private DatosPbTecnica createDatosPbTecnica(Integer index){
		
		DatosPbTecnica valores = new DatosPbTecnica();
		valores.setKpoliza(new Long(index));
		valores.setKsubpol(index);
		return valores;
	}
}