package es.mapfre.solvencia.dao.impl.maestro;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.ErrorSolvencia2Key;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ErrorSolvencia2Dao;
import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;

public class ErrorSolvencia2DaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(ErrorSolvencia2DaoTest.class);
	
	
	@Test
	public void testEntrySet()  throws Exception{
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(errorSolvencia2.getKey(), errorSolvencia2);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		ErrorSolvencia2Key key = errorSolvencia2.getKey();
		
		dao.put(key, errorSolvencia2);
		
		Assert.assertEquals(errorSolvencia2, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		ErrorSolvencia2Key key = errorSolvencia2.getKey();
		
		dao.put(key, errorSolvencia2);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(errorSolvencia2.getKey(), errorSolvencia2);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		
		Map<ErrorSolvencia2Key,ErrorSolvencia2> map = new HashMap<ErrorSolvencia2Key,ErrorSolvencia2>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(i, new Timestamp(addedDate+i));
			map.put(errorSolvencia2.getKey(), errorSolvencia2);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		ErrorSolvencia2Key key = errorSolvencia2.getKey();
		dao.put(key, errorSolvencia2);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ErrorSolvencia2Dao dao = new ErrorSolvencia2Dao();
		ErrorSolvencia2 errorSolvencia2 = createErrorSolvencia2(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(errorSolvencia2.getKey(), errorSolvencia2);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private ErrorSolvencia2 createErrorSolvencia2(Integer index, Timestamp fecha){
		
		ErrorSolvencia2 errorSolvencia2 = new ErrorSolvencia2();
		errorSolvencia2.setCnivel("01");
		errorSolvencia2.setGdesc("desc"+index);
		errorSolvencia2.setGdesccorta("gdesccorta"+index);
		errorSolvencia2.setKaplicacion("kaplicacion");
		errorSolvencia2.setKidprograma("kidprograma");
		errorSolvencia2.setKliteral(index);
		errorSolvencia2.setKretorno("0"+index);
		
		return errorSolvencia2;
	}
}
