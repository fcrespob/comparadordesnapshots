package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DisenoProcesosKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;

public class DisenoProcesosDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(DisenoProcesosDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		dao.put(disPro.getKey(), disPro);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		DisenoProcesosKey key = disPro.getKey();
		
		dao.put(key, disPro);
		
		Assert.assertEquals(disPro, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		DisenoProcesosKey key = disPro.getKey();
		
		dao.put(key, disPro);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		dao.put(disPro.getKey(), disPro);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		
		Map<DisenoProcesosKey,DisenoProcesos> map = new HashMap<DisenoProcesosKey,DisenoProcesos>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DisenoProcesos disPro = createDisenoProcesos(i);
			map.put(disPro.getKey(), disPro);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		DisenoProcesosKey key = disPro.getKey();
		dao.put(key, disPro);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		DisenoProcesos disPro = createDisenoProcesos(1);
		
		dao.put(disPro.getKey(), disPro);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	@Test
	public void testObtenerDisenoProcesos6() throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		
		DisenoProcesos disPro1 = createDisenoProcesos(1);
		dao.put(disPro1.getKey(), disPro1);
		List<DisenoProcesos> value = dao.obtenerDisenoProcesos(disPro1.getGproceso(),disPro1.getKcompania(), disPro1.getKramo(), disPro1.getKmodalidad()
														,disPro1.getKgarantia(), disPro1.getKbasetec(), disPro1.getKclaveadic());
		Assert.assertEquals(disPro1, value.get(0)); 
		
		DisenoProcesos disPro2 = createDisenoProcesos(2);
		value = dao.obtenerDisenoProcesos(disPro2.getGproceso(),disPro2.getKcompania(), disPro2.getKramo(), disPro2.getKmodalidad()
										  ,disPro2.getKgarantia(), disPro2.getKbasetec(), disPro2.getKclaveadic());
		
		Assert.assertTrue(value.isEmpty());
		dao.clear();
	}
	
	
	@Test
	public void testObtenerDisenoProcesos2() throws Exception{
		
		DisenoProcesosDao dao = new DisenoProcesosDao();
		
		DisenoProcesos disPro1 = createDisenoProcesos(1);
		dao.put(disPro1.getKey(), disPro1);
		Assert.assertEquals(disPro1, dao.obtenerDisenoProcesos(disPro1.getGproceso(),disPro1.getKcompania(), disPro1.getKbasetec()).get(0)); 
		
		DisenoProcesos disPro2 = createDisenoProcesos(2);
		Assert.assertTrue(dao.obtenerDisenoProcesos(disPro2.getGproceso(),disPro2.getKcompania(), disPro2.getKbasetec()).isEmpty());
		
		dao.clear();
	}
	
	
	private DisenoProcesos createDisenoProcesos(Integer index){
		
		DisenoProcesos disPro = new DisenoProcesos();
		disPro.setKcompania(index); 
		disPro.setKramo("kramo"+index);
		disPro.setKmodalidad(index);
		disPro.setKgarantia(index);
		disPro.setKbasetec("kbasetec"+index);
		disPro.setKclaveadic("kclaveadic"+index);
		disPro.setGproceso("gproceso"+index);
		return disPro;
	}
}	
