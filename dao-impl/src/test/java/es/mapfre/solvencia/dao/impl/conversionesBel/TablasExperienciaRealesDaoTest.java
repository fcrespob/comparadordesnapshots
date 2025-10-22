package es.mapfre.solvencia.dao.impl.conversionesBel;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.conversionesBel.TablasExperienciaRealesKey;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;

public class TablasExperienciaRealesDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(TablasExperienciaRealesDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales("1", new Timestamp(System.currentTimeMillis()));
		
		TablasExperienciaRealesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertEquals(vct, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		TablasExperienciaRealesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		
		Map<TablasExperienciaRealesKey,TablasExperienciaReales> map = new HashMap<TablasExperienciaRealesKey,TablasExperienciaReales>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(i), new Timestamp(addedDate+i));
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		TablasExperienciaRealesKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		TablasExperienciaRealesDao dao = new TablasExperienciaRealesDao();
		TablasExperienciaReales vct = createTablasExperienciaReales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private TablasExperienciaReales createTablasExperienciaReales(String index, Timestamp fecha){
		
		TablasExperienciaReales vct = new TablasExperienciaReales();
		vct.setFecCierre(fecha);
		vct.setKbasetec(index);
		
		return vct;
	}
}