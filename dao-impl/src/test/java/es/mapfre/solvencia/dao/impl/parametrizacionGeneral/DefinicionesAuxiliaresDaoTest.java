package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesAuxiliaresKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;

public class DefinicionesAuxiliaresDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DefinicionesAuxiliaresDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1,"C");
		
		dao.put(defAux.getKey(), defAux);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1, "C");
		
		DefinicionesAuxiliaresKey key = defAux.getKey();
		
		dao.put(key, defAux);
		
		Assert.assertEquals(defAux, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1, "C");
		
		DefinicionesAuxiliaresKey key = defAux.getKey();
		
		dao.put(key, defAux);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1, "C");
		
		dao.put(defAux.getKey(), defAux);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		
		Map<DefinicionesAuxiliaresKey,DefinicionesAuxiliares> map = new HashMap<DefinicionesAuxiliaresKey,DefinicionesAuxiliares>();
		for ( int i = 0; i < 10; i++) {
			DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(i, "C");
			map.put(defAux.getKey(), defAux);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1, "C");
		
		DefinicionesAuxiliaresKey key = defAux.getKey();
		dao.put(key, defAux);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		DefinicionesAuxiliares defAux = createDefinicionesAuxiliares(1, "C");
		
		dao.put(defAux.getKey(), defAux);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private DefinicionesAuxiliares createDefinicionesAuxiliares(Integer index, String negocio){
		
		DefinicionesAuxiliares defAux = new DefinicionesAuxiliares();
		defAux.setKcarteorig(index);
		defAux.setCnegocio(negocio);
		defAux.setKmodalidad(index);
		defAux.setKgarantia(index);
		defAux.setCidentivariab("Cidentivariab"+index);
		defAux.setGvariable("Gvariable"+index);
		defAux.setGvalor("Gvalor"+index);
		
		return defAux;
	}
}	
