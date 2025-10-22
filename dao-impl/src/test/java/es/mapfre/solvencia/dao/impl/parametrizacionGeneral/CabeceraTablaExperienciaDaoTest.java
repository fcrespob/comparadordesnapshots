package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CabeceraTablaExperienciaKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;

public class CabeceraTablaExperienciaDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(CabeceraTablaExperienciaDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(cabeceraTablaExp.getKey(), cabeceraTablaExp);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		CabeceraTablaExperienciaKey key = cabeceraTablaExp.getKey();
		
		dao.put(key, cabeceraTablaExp);
		
		Assert.assertEquals(cabeceraTablaExp, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		CabeceraTablaExperienciaKey key = cabeceraTablaExp.getKey();
		
		dao.put(key, cabeceraTablaExp);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(cabeceraTablaExp.getKey(), cabeceraTablaExp);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		
		Map<CabeceraTablaExperienciaKey,CabeceraTablaExperiencia> map = new HashMap<CabeceraTablaExperienciaKey,CabeceraTablaExperiencia>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(i, new Timestamp(addedDate+i));
			map.put(cabeceraTablaExp.getKey(), cabeceraTablaExp);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		CabeceraTablaExperienciaKey key = cabeceraTablaExp.getKey();
		dao.put(key, cabeceraTablaExp);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		CabeceraTablaExperienciaDao dao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cabeceraTablaExp = createCabeceraTablaExperiencia(1, new Timestamp(System.currentTimeMillis()));
		
		dao.put(cabeceraTablaExp.getKey(), cabeceraTablaExp);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	
	private CabeceraTablaExperiencia createCabeceraTablaExperiencia(Integer index, Timestamp fecha){
		
		CabeceraTablaExperiencia cabeceraTablaExp = new CabeceraTablaExperiencia();
		
		cabeceraTablaExp.setcUsuario("cUsuario"+index);
		cabeceraTablaExp.setFecAlta(new Timestamp(0));
		cabeceraTablaExp.setFecAnula(fecha);
		cabeceraTablaExp.setFecModif(fecha);
		cabeceraTablaExp.setGcorta("gcorta"+index);
		cabeceraTablaExp.setGdeslarga("gdeslarga"+index);
		cabeceraTablaExp.setK2tipotabla(index.toString());
		cabeceraTablaExp.setKtabla(index);
		
		return cabeceraTablaExp;
	}
}	
