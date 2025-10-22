package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.OpcionesGeneracionKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;

public class OpcionesGeneracionDaoTest extends TestCase {
		
	@Test
	public void testEntrySet()  throws Exception{
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		dao.put(opGen.getKey(), opGen);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		OpcionesGeneracionKey key = opGen.getKey();
		
		dao.put(key, opGen);
		
		Assert.assertEquals(opGen, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		OpcionesGeneracionKey key = opGen.getKey();
		
		dao.put(key, opGen);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		dao.put(opGen.getKey(), opGen);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		
		Map<OpcionesGeneracionKey,OpcionesGeneracion> map = new HashMap<OpcionesGeneracionKey,OpcionesGeneracion>();
		for ( int i = 0; i < 10; i++) {
			OpcionesGeneracion opGen = createOpcionesGeneracion(i);
			map.put(opGen.getKey(), opGen);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		OpcionesGeneracionKey key = opGen.getKey();
		dao.put(key, opGen);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		OpcionesGeneracionDao dao = new OpcionesGeneracionDao();
		OpcionesGeneracion opGen = createOpcionesGeneracion(1);
		
		dao.put(opGen.getKey(), opGen);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private OpcionesGeneracion createOpcionesGeneracion(Integer index){
		
		OpcionesGeneracion opGen = new OpcionesGeneracion();
		opGen.setModalidad(index);
		return opGen;
	}
}