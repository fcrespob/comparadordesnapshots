package es.mapfre.solvencia.dao.impl.maestro;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.DatosEspecificosKey;
import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;

public class DatosEspecificosDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DatosEspecificosDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		dao.put(DatosEspecificos.getKey(), DatosEspecificos);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		DatosEspecificosKey key = DatosEspecificos.getKey();
		
		dao.put(key, DatosEspecificos);
		
		Assert.assertEquals(DatosEspecificos, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		DatosEspecificosKey key = DatosEspecificos.getKey();
		
		dao.put(key, DatosEspecificos);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		dao.put(DatosEspecificos.getKey(), DatosEspecificos);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DatosEspecificosDao dao = new DatosEspecificosDao();
		
		Map<DatosEspecificosKey,DatosEspecificos> map = new HashMap<DatosEspecificosKey,DatosEspecificos>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DatosEspecificos DatosEspecificos = createDatosEspecificos(i);
			map.put(DatosEspecificos.getKey(), DatosEspecificos);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		DatosEspecificosKey key = DatosEspecificos.getKey();
		dao.put(key, DatosEspecificos);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DatosEspecificosDao dao = new DatosEspecificosDao();
		DatosEspecificos DatosEspecificos = createDatosEspecificos(1);
		
		dao.put(DatosEspecificos.getKey(), DatosEspecificos);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private DatosEspecificos createDatosEspecificos(Integer index){
		
		DatosEspecificos valores = new DatosEspecificos();
		valores.setKpoliza(new Long(index));
		valores.setKsubpol(index);
		return valores;
	}
}