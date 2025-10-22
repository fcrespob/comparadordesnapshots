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

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionMensualesKey;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;

public class ValoresAnulacionMensualesDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(ValoresAnulacionMensualesDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales("1", new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionMensualesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertEquals(vct, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionMensualesKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		
		Map<ValoresAnulacionMensualesKey,ValoresAnulacionMensuales> map = new HashMap<ValoresAnulacionMensualesKey,ValoresAnulacionMensuales>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(i), new Timestamp(addedDate+i));
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresAnulacionMensualesKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testGetValuesEasy() throws Exception{
		
		ValoresAnulacionMensualesDao dao = new ValoresAnulacionMensualesDao();
		
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		ValoresAnulacionMensuales vct = createValoresAnulacionMensuales("KTAB1", fecha);
		
		dao.put(vct.getKey(), vct);
		
		List<ValoresAnulacionMensuales> valoresAnulacionMensuales = dao.getValues("KTAB1", fecha);
        Assert.assertNotNull(valoresAnulacionMensuales);
        
		
	}
	
		
	private ValoresAnulacionMensuales createValoresAnulacionMensuales(String index, Timestamp fecha){
		
		ValoresAnulacionMensuales vct = new ValoresAnulacionMensuales();
		vct.setFecCierre(fecha);
		vct.setCodTabla(index);
		vct.setPolizaVigentes(new java.math.BigDecimal("2"));
		vct.setMesesDesde(2);
		vct.setProbabAnul(new java.math.BigDecimal("0.2"));
		
		return vct;
	}
	
}
