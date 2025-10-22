package es.mapfre.solvencia.dao.impl;

import junit.framework.Assert;

import org.junit.Test;


public class PropertiesDaoTest{

	
	@Test
	public void testaddProperties() throws Exception {
		
		PropertiesDao dao = new PropertiesDao();
		dao.addProperties("test.properties");
		
		Assert.assertTrue(dao.size() > 0);
		dao.clear();
	}
	

}
