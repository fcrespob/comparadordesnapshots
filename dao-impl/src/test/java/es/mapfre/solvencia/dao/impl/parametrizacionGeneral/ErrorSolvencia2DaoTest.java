package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import es.mapfre.solvencia.coherence.keys.maestro.ErrorSolvencia2Key;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;

public class ErrorSolvencia2DaoTest extends TestCase {
		
	@Test
	public void testEntrySet()  throws Exception{
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		dao.put(error.getKey(), error);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		ErrorSolvencia2Key key = error.getKey();
		
		dao.put(key, error);
		
		Assert.assertEquals(error, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		ErrorSolvencia2Key key = error.getKey();
		
		dao.put(key, error);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		dao.put(error.getKey(), error);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		
		Map<ErrorSolvencia2Key,ErrorSolvencia2> map = new HashMap<ErrorSolvencia2Key,ErrorSolvencia2>();
		for ( int i = 0; i < 10; i++) {
			ErrorSolvencia2 error = createErrorSolvencia2(i);
			map.put(error.getKey(), error);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		ErrorSolvencia2Key key = error.getKey();
		dao.put(key, error);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 error = createErrorSolvencia2(1);
		
		dao.put(error.getKey(), error);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private ErrorSolvencia2 createErrorSolvencia2(Integer index){
		
		ErrorSolvencia2 error = new ErrorSolvencia2();
		error.setKliteral(index);
		error.setKretorno(String.valueOf(index));
		return error;
	}
}