package es.mapfre.solvencia.dao;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

public class DaoBaseTest extends TestCase {
	private static final String CACHE_NAME ="propiedades";
	
	@Test
	public void testSetCache() {
		DaoBase dao = new DaoBase() {};
		dao.setCacheName(CACHE_NAME);
		Assert.assertEquals(CACHE_NAME, dao.getCacheName());
	}
	
	@Test
	public void testSizeAndEmpty() {
		DaoBase dao = new DaoBase() {};
		dao.setCacheName(CACHE_NAME);
		dao.clear();
		Assert.assertTrue(dao.isEmpty());
		dao.getCache().put("CLAVE", "VALOR");
		Assert.assertFalse(dao.isEmpty());
		Assert.assertEquals(1, dao.size());
	}
	
	@Test
	public void testContainsKey() {
		DaoBase dao = new DaoBase() {};
		dao.setCacheName(CACHE_NAME);
		dao.getCache().put("CLAVE", "VALOR");
		Assert.assertTrue(dao.containsKey("CLAVE"));
	}

	@Test
	public void testContainsValue() {
		DaoBase dao = new DaoBase() {};
		dao.setCacheName(CACHE_NAME);
		dao.getCache().put("CLAVE", "VALOR");
		Assert.assertTrue(dao.containsValue("VALOR"));
	}
}
