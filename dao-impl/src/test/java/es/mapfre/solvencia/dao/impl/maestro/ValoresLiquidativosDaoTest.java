package es.mapfre.solvencia.dao.impl.maestro;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.ValoresLiquidativosKey;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;

public class ValoresLiquidativosDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(ValoresLiquidativosDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		dao.put(ValoresLiquidativos.getKey(), ValoresLiquidativos);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		ValoresLiquidativosKey key = ValoresLiquidativos.getKey();
		
		dao.put(key, ValoresLiquidativos);
		
		Assert.assertEquals(ValoresLiquidativos, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		ValoresLiquidativosKey key = ValoresLiquidativos.getKey();
		
		dao.put(key, ValoresLiquidativos);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		dao.put(ValoresLiquidativos.getKey(), ValoresLiquidativos);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		
		Map<ValoresLiquidativosKey,ValoresLiquidativos> map = new HashMap<ValoresLiquidativosKey,ValoresLiquidativos>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(i);
			map.put(ValoresLiquidativos.getKey(), ValoresLiquidativos);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		ValoresLiquidativosKey key = ValoresLiquidativos.getKey();
		dao.put(key, ValoresLiquidativos);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ValoresLiquidativosDao dao = new ValoresLiquidativosDao();
		ValoresLiquidativos ValoresLiquidativos = createValoresLiquidativos(1);
		
		dao.put(ValoresLiquidativos.getKey(), ValoresLiquidativos);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private ValoresLiquidativos createValoresLiquidativos(Integer index){
		
		ValoresLiquidativos valores = new ValoresLiquidativos();
		valores.setKpoliza(new Long(index));
		valores.setKmodalidad(String.valueOf(index));
		return valores;
	}
}