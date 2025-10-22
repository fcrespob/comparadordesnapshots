package es.mapfre.solvencia.dao.impl.formulacion;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.formulacion.PeriodoKey;
import es.mapfre.solvencia.dominio.formulacion.Periodo;

public class PeriodoDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(PeriodoDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		PeriodoKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Periodo test = dao.get(key);
		
		Assert.assertEquals(vct, test);
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		PeriodoKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		
		Map<PeriodoKey,Periodo> map = new HashMap<PeriodoKey,Periodo>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, new Long(i), 1, 1, 1, 1, 1, "1", 1, "1");
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		PeriodoKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		PeriodoDao dao = new PeriodoDao();
		Periodo vct = createPeriodo(new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()), "1", 1, 1, new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
		
	public Periodo createPeriodo(Timestamp fechaInicio, Timestamp fechaFin, String cnegocio, Integer ccanal, Integer ccartera, Timestamp fcierre, Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri, Integer norden, Integer kgarantia, String kprestacion, Integer kajuste, String ctipoaport) {
		
		Periodo periodo = new Periodo();
		
		periodo.setFechaInicio(fechaInicio);
		periodo.setFechaFin(fechaFin);
		periodo.setCnegocio(cnegocio);
		periodo.setCcanal(ccanal);
		periodo.setCcartera(ccartera);
		periodo.setFcierre(fcierre);
		periodo.setKmodalidad(kmodalidad);
		periodo.setKpoliza(kpoliza);
		periodo.setKsubpoliza(ksubpoliza);
		periodo.setKcertificado(kcertificado);
		periodo.setNsuscri(nsuscri);
		periodo.setNorden(norden);
		periodo.setKgarantia(kgarantia);
		periodo.setKprestacion(kprestacion);
		periodo.setKajuste(kajuste);
		periodo.setCtipoaport(ctipoaport);
		
		return periodo;
	}
	
}
