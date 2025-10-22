package es.mapfre.solvencia.dao.impl.maestro;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.CuadrosAmortizacionKey;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;

public class CuadrosAmortizacionDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(CuadrosAmortizacionDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		dao.put(CuadrosAmortizacion.getKey(), CuadrosAmortizacion);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		CuadrosAmortizacionKey key = CuadrosAmortizacion.getKey();
		
		dao.put(key, CuadrosAmortizacion);
		
		Assert.assertEquals(CuadrosAmortizacion, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		CuadrosAmortizacionKey key = CuadrosAmortizacion.getKey();
		
		dao.put(key, CuadrosAmortizacion);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		dao.put(CuadrosAmortizacion.getKey(), CuadrosAmortizacion);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		
		Map<CuadrosAmortizacionKey,CuadrosAmortizacion> map = new HashMap<CuadrosAmortizacionKey,CuadrosAmortizacion>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(i);
			map.put(CuadrosAmortizacion.getKey(), CuadrosAmortizacion);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		CuadrosAmortizacionKey key = CuadrosAmortizacion.getKey();
		dao.put(key, CuadrosAmortizacion);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		CuadrosAmortizacionDao dao = new CuadrosAmortizacionDao();
		CuadrosAmortizacion CuadrosAmortizacion = createCuadrosAmortizacion(1);
		
		dao.put(CuadrosAmortizacion.getKey(), CuadrosAmortizacion);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private CuadrosAmortizacion createCuadrosAmortizacion(Integer index){
		
		CuadrosAmortizacion valores = new CuadrosAmortizacion();
		valores.setKpoliza(new Long(index));
		valores.setCcanal(index);
		valores.setKprestacion(String.valueOf(index));
		return valores;
	}
}