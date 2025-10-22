package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;

public class PolizasTipoDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(PolizasTipoDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		dao.put(polizaTipo.getKey(), polizaTipo);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		PolizasTipoKey key = polizaTipo.getKey();
		
		dao.put(key, polizaTipo);
		
		Assert.assertEquals(polizaTipo, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		PolizasTipoKey key = polizaTipo.getKey();
		
		dao.put(key, polizaTipo);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
	}

	@Test
	public void testPut() throws Exception {
		
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		dao.put(polizaTipo.getKey(), polizaTipo);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		PolizasTipoDao dao = new PolizasTipoDao();
		
		Map<PolizasTipoKey,PolizasTipo> map = new HashMap<PolizasTipoKey,PolizasTipo>();
		for ( int i = 0; i < 10; i++) {
			PolizasTipo polizaTipo = createPolizasTipo(i);
			map.put(polizaTipo.getKey(), polizaTipo);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		PolizasTipoKey key = polizaTipo.getKey();
		dao.put(key, polizaTipo);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}

	@Test
	public void testValues() throws Exception{
		
		PolizasTipoDao dao = new PolizasTipoDao();
		PolizasTipo polizaTipo = createPolizasTipo(1);
		
		dao.put(polizaTipo.getKey(), polizaTipo);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private PolizasTipo createPolizasTipo(Integer index){
		
		PolizasTipo polizaTipo = new PolizasTipo();
		polizaTipo.setCcanal(index);
		UmicKey cU = new UmicKey();
		cU.setKpoliza(new Long(index));
		cU.setKajuste(index);
		polizaTipo.setClaveUmic(cU);
		return polizaTipo;
	}
}