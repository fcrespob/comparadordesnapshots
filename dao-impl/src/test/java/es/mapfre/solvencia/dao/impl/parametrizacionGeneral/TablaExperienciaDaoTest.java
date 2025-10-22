package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TablaExperienciaKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;

public class TablaExperienciaDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(TablaExperienciaDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(tablaExperiencia.getKey(), tablaExperiencia);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		TablaExperienciaKey key = tablaExperiencia.getKey();
		
		dao.put(key, tablaExperiencia);
		
		Assert.assertEquals(tablaExperiencia, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		TablaExperienciaKey key = tablaExperiencia.getKey();
		
		dao.put(key, tablaExperiencia);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(tablaExperiencia.getKey(), tablaExperiencia);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		
		Map<TablaExperienciaKey,TablaExperiencia> map = new HashMap<TablaExperienciaKey,TablaExperiencia>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			TablaExperiencia tablaExperiencia = createTablaExperiencia(i, new Timestamp(addedDate+i));
			map.put(tablaExperiencia.getKey(), tablaExperiencia);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		TablaExperienciaKey key = tablaExperiencia.getKey();
		dao.put(key, tablaExperiencia);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(tablaExperiencia.getKey(), tablaExperiencia);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testGetValues() throws Exception{
		
		TablaExperienciaDao dao = new TablaExperienciaDao();
		TablaExperiencia tablaExperiencia = createTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(tablaExperiencia.getKey(), tablaExperiencia);
		Assert.assertNotNull(dao.getValue(tablaExperiencia));
		
		dao.clear();
		
	}
	
		
	private TablaExperiencia createTablaExperiencia(Integer index, Timestamp fecha){
		
		TablaExperiencia tablaExperiencia = new TablaExperiencia();
		
		tablaExperiencia.setCusuario("cusuario"+index);
		tablaExperiencia.setFanulacion(new Timestamp(Long.MAX_VALUE));
		tablaExperiencia.setFmodificacion(fecha);
		
		List<java.math.BigDecimal> gvalor = new ArrayList<java.math.BigDecimal>();
		for(int i=0;i < 100; i++){
			gvalor.add(new java.math.BigDecimal("0.01"+index));
		}
		
		tablaExperiencia.setGvalor(gvalor);
		tablaExperiencia.setGvaloreslen(index);
		tablaExperiencia.setK2tipovalor("k2tipovalor"+index);
		tablaExperiencia.setKanacimiento("kanacimiento"+index);
		tablaExperiencia.setKinteres(new java.math.BigDecimal(index+"0.07"));
		tablaExperiencia.setKsobremort(new java.math.BigDecimal(index+"0.07"));
		tablaExperiencia.setKsobreries(new java.math.BigDecimal(index+"0.07"));
		tablaExperiencia.setKtabla(index);
		
		
		return tablaExperiencia;
	}
}	
