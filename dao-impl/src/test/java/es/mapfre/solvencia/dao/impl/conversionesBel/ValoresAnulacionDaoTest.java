package es.mapfre.solvencia.dao.impl.conversionesBel;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionKey;
import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionMensualesKey;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;

public class ValoresAnulacionDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(ValoresAnulacionDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion("1", new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertEquals(vct, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		
		Map<ValoresAnulacionKey,ValoresAnulacion> map = new HashMap<ValoresAnulacionKey,ValoresAnulacion>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			ValoresAnulacion vct = createValoresAnulacion(String.valueOf(i), new Timestamp(addedDate+i));
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		ValoresAnulacion vct = createValoresAnulacion(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testGetValuesEasy() throws Exception{
		
		ValoresAnulacionDao dao = new ValoresAnulacionDao();
		
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		ValoresAnulacion vct = createValoresAnulacion("KTAB1", fecha);
		
		dao.put(vct.getKey(), vct);
		
		List<ValoresAnulacion> valoresAnulacion = dao.getValues("KTAB1", fecha);
        Assert.assertNotNull(valoresAnulacion);
        
		
	}
	
		
	private ValoresAnulacion createValoresAnulacion(String index, Timestamp fecha){
		
		ValoresAnulacion vct = new ValoresAnulacion();
		vct.setFecCierre(fecha);
		vct.setCodTabla(index);
		vct.setPolizaVigentes(new java.math.BigDecimal("2"));
		vct.setAniosDesde(new BigDecimal("123, 1234"));
		vct.setProbabAnul(new java.math.BigDecimal("0.2"));
		
		return vct;
	}
	
}
