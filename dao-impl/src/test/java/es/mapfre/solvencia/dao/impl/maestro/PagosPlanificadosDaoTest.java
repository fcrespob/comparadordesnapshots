package es.mapfre.solvencia.dao.impl.maestro;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.PagosPlanificadosKey;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;

public class PagosPlanificadosDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(PagosPlanificadosDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(pagoPlan.getKey(), pagoPlan);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		PagosPlanificadosKey key = pagoPlan.getKey();
		
		dao.put(key, pagoPlan);
		
		Assert.assertEquals(pagoPlan, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		PagosPlanificadosKey key = pagoPlan.getKey();
		
		dao.put(key, pagoPlan);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(pagoPlan.getKey(), pagoPlan);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		
		Map<PagosPlanificadosKey,PagosPlanificados> map = new HashMap<PagosPlanificadosKey,PagosPlanificados>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			PagosPlanificados pagoPlan = createPagosPlanificados(i, new Timestamp(addedDate+i));
			map.put(pagoPlan.getKey(), pagoPlan);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		PagosPlanificadosKey key = pagoPlan.getKey();
		dao.put(key, pagoPlan);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados pagoPlan = createPagosPlanificados(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(pagoPlan.getKey(), pagoPlan);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	@Test
	public void testgetValues() throws Exception{
		//Insertamos 100 pagos planificados
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados var = null;
		
		long counter = 0;
		
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		
		for (int i = 0; i < 100; i++) {
			fecha = new Timestamp(fecha.getTime()+counter);
			log.debug("fecha = {}", fecha);
			var = createPagosPlanificados(1,fecha);
			dao.put(var.getKey(),var);
			counter = counter +10000;
		}
		
		Set pagos = dao.getValues(1,1L,1,1,1,1,"k","kpre",new Timestamp(0));
		
		log.debug("pagos size = {}", pagos.size());
		
		Assert.assertTrue(pagos.size() == 100);
		
		dao.clear();
		
	}
	
	private PagosPlanificados createPagosPlanificados(Integer index, Timestamp fecha){
		
		PagosPlanificados pagoPlan = new PagosPlanificados();
		pagoPlan.setCgarantia(index);
		pagoPlan.setCprestaEntorno("k");
		pagoPlan.setCprestaFict("cprestaFict"+index);
		pagoPlan.setEplreaBruto(new java.math.BigDecimal(index+0.5));
		pagoPlan.setEplreaNeto(new java.math.BigDecimal(index+0.5));
		pagoPlan.setErrorPeriodic("errorPeriodic"+index);
		pagoPlan.setFplreaEfecto(fecha);
		pagoPlan.setKajuste(index);
		pagoPlan.setKcerti(index);
		pagoPlan.setKgrsus(index);
		pagoPlan.setKpoliza(new Long(index));
		pagoPlan.setKpresta("kpre");
		pagoPlan.setKsubpol(index);
		pagoPlan.setPcoase(new java.math.BigDecimal(index+0.5));
		pagoPlan.setPeriodicidad("periodicidad"+index);
		
		return pagoPlan;
	}
}