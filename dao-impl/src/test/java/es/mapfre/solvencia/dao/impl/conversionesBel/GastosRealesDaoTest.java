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

import es.mapfre.solvencia.coherence.keys.conversionesBel.GastosRealesKey;
import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;

public class GastosRealesDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(GastosRealesDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		GastosRealesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertEquals(vct, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		GastosRealesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		GastosRealesDao dao = new GastosRealesDao();
		
		Map<GastosRealesKey,GastosReales> map = new HashMap<GastosRealesKey,GastosReales>();
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		for ( int i = 0; i < 10; i++) {
			GastosReales vct = createGastosReales(i,"1", fecha, fecha, 1, "1", "1");
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		GastosRealesKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		GastosRealesDao dao = new GastosRealesDao();
		GastosReales vct = createGastosReales(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testObtenerGastosReales() throws Exception{
		
		GastosRealesDao dao = new GastosRealesDao();
		
		Timestamp fecha1 = new Timestamp(System.currentTimeMillis());
		
		Timestamp fechaCierre = new Timestamp(System.currentTimeMillis()+100);
		
		Timestamp fecha2 = new Timestamp(System.currentTimeMillis()+500);
		
		GastosReales vct = createGastosReales(1,"1", fecha1, fecha2, 1, "1", "1");
		
		GastosRealesKey key = vct.getKey();
		
		dao.put(vct.getKey(), vct);
		
		GastosReales vct2 = createGastosReales(2,"1", fecha1, fecha2, 2, "1", "1");
		
		GastosRealesKey key2 = vct2.getKey();
		
		dao.put(vct2.getKey(), vct2);
		
		GastosReales vct3 = createGastosReales(2,"1", fecha1, fecha2, 4, "1", "1");
		
		GastosRealesKey key3 = vct3.getKey();
		
		dao.put(vct3.getKey(), vct3);
		
		Assert.assertTrue(dao.size()==3);
		
		List<GastosReales> lista = dao.obtenerGastosReales(1, "1", fechaCierre, 1, "1", "1", "N");
		
		Assert.assertTrue(lista.size()==1);
		
		lista = dao.obtenerGastosReales(2, "1", fechaCierre, 3, "1", "1", "N");
		
		Assert.assertTrue(lista.size()==2);
		
		dao.clear();
		
	}
	
	
	private GastosReales createGastosReales(Integer ccanal, Timestamp fecCierre) {
		
		GastosReales vct = new GastosReales();
		vct.setCcanal(ccanal);
		vct.setFecDesde(fecCierre);
		return vct;
	}
	
	
	private GastosReales createGastosReales(Integer ccanal, String cnegocio,
			Timestamp fecDesde, Timestamp fecHasta, Integer kmodalidad,
			String kramo, String ktipobt) {
		
		GastosReales vct = new GastosReales();
		vct.setCcanal(ccanal);
		vct.setCnegocio(cnegocio);
		vct.setKramo(kramo);
		vct.setKtipobt(ktipobt);
		vct.setKmodalidad(kmodalidad);
		vct.setFecDesde(fecDesde);
		vct.setFecHasta(fecHasta);	
		vct.setNumUmicRef(ccanal*10);
		return vct;
	}
}