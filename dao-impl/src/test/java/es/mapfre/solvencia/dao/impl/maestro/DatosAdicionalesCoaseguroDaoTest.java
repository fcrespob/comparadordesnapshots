package es.mapfre.solvencia.dao.impl.maestro;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;

public class DatosAdicionalesCoaseguroDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DatosAdicionalesCoaseguroDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		dao.put(DatosAdicionalesCoaseguro.getKey(), DatosAdicionalesCoaseguro);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		DatosAdicionalesCoaseguroKey key = DatosAdicionalesCoaseguro.getKey();
		
		dao.put(key, DatosAdicionalesCoaseguro);
		
		Assert.assertEquals(DatosAdicionalesCoaseguro, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		DatosAdicionalesCoaseguroKey key = DatosAdicionalesCoaseguro.getKey();
		
		dao.put(key, DatosAdicionalesCoaseguro);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		dao.put(DatosAdicionalesCoaseguro.getKey(), DatosAdicionalesCoaseguro);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		
		Map<DatosAdicionalesCoaseguroKey,DatosAdicionalesCoaseguro> map = new HashMap<DatosAdicionalesCoaseguroKey,DatosAdicionalesCoaseguro>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(i);
			map.put(DatosAdicionalesCoaseguro.getKey(), DatosAdicionalesCoaseguro);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		DatosAdicionalesCoaseguroKey key = DatosAdicionalesCoaseguro.getKey();
		dao.put(key, DatosAdicionalesCoaseguro);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DatosAdicionalesCoaseguroDao dao = new DatosAdicionalesCoaseguroDao();
		DatosAdicionalesCoaseguro DatosAdicionalesCoaseguro = createDatosAdicionalesCoaseguro(1);
		
		dao.put(DatosAdicionalesCoaseguro.getKey(), DatosAdicionalesCoaseguro);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private DatosAdicionalesCoaseguro createDatosAdicionalesCoaseguro(Integer index){
		
		DatosAdicionalesCoaseguro valores = new DatosAdicionalesCoaseguro();
		valores.setKpoliza(new Long(index));
		valores.setKsubpol(index);
		return valores;
	}
}