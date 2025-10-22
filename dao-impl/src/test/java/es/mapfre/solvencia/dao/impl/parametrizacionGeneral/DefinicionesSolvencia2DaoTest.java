package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesSolvencia2Key;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesSolvencia2;

public class DefinicionesSolvencia2DaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(DefinicionesSolvencia2DaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1, true,false,true);
		
		dao.put(defSolv2.getKey(), defSolv2);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1,  true,false,true);
		
		DefinicionesSolvencia2Key key = defSolv2.getKey();
		
		dao.put(key, defSolv2);
		
		Assert.assertEquals(defSolv2, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1,  true,false,true);
		
		DefinicionesSolvencia2Key key = defSolv2.getKey();
		
		dao.put(key, defSolv2);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1,  true,false,true);
		
		dao.put(defSolv2.getKey(), defSolv2);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		
		Map<DefinicionesSolvencia2Key,DefinicionesSolvencia2> map = new HashMap<DefinicionesSolvencia2Key,DefinicionesSolvencia2>();
		for ( int i = 0; i < 10; i++) {
			DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(i,  true,false,true);
			map.put(defSolv2.getKey(), defSolv2);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1,  true,false,true);
		
		DefinicionesSolvencia2Key key = defSolv2.getKey();
		dao.put(key, defSolv2);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DefinicionesSolvencia2Dao dao = new DefinicionesSolvencia2Dao();
		DefinicionesSolvencia2 defSolv2 = createDefinicionesSolvencia2(1, true,false,true);
		
		dao.put(defSolv2.getKey(), defSolv2);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private DefinicionesSolvencia2 createDefinicionesSolvencia2(Integer index, boolean estado,boolean cindcol, boolean rescate){
		
		DefinicionesSolvencia2 defSolv2 = new DefinicionesSolvencia2();
		defSolv2.setKcarteorig(index);
		defSolv2.setKmodalidad(index);
		defSolv2.setKgarantia(index);
		defSolv2.setKestado(estado);
		defSolv2.setCindcol(cindcol);
		defSolv2.setSrescate(rescate);
		defSolv2.setCnegocio("C");
		defSolv2.setCinversion("cinversion"+index);
		defSolv2.setCriesgo("criesgo"+index);
		defSolv2.setCtipoprovi("ctipoprovi"+index);
		
		return defSolv2;
	}
}	
