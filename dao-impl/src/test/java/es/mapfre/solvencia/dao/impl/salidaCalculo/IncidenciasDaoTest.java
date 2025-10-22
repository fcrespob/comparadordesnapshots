package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciaKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;

public class IncidenciasDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(IncidenciasDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		dao.put(incidencia.getKey(), incidencia);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		IncidenciaKey key = incidencia.getKey();
		
		dao.put(incidencia.getKey(), incidencia);
		
		Assert.assertEquals(incidencia, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		IncidenciaKey key = incidencia.getKey();
		
		dao.put(incidencia.getKey(), incidencia);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		dao.put(incidencia.getKey(), incidencia);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		IncidenciaDao dao = new IncidenciaDao();
		dao.clear();
		
		Map<IncidenciaKey,Incidencia> map = new HashMap<IncidenciaKey,Incidencia>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			Incidencia incidencia = createIncidencia(i);
			map.put(incidencia.getKey(), incidencia);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		IncidenciaKey key = incidencia.getKey();
		
		dao.put(key, incidencia);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		IncidenciaDao dao = new IncidenciaDao();
		Incidencia incidencia = createIncidencia(1);
		
		dao.put(incidencia.getKey(), incidencia);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testIsUmicErronea() throws Exception {
		IncidenciaDao dao = new IncidenciaDao();
		
		Incidencia incidencia = createIncidencia(1);
		incidencia.setTipoError(ConstantesSolvencia.CTE_ERROR);
		Incidencia incidencia2 = createIncidencia(2);
		incidencia2.setTipoError(ConstantesSolvencia.CTE_AVISO);
		
		dao.put(incidencia.getKey(), incidencia);
		dao.put(incidencia2.getKey(), incidencia2);
		
		assertTrue(dao.isUmicErronea(incidencia.getClaveUmic(), incidencia.getFecCierre(), incidencia.getBt()));
		assertFalse(dao.isUmicErronea(incidencia.getClaveUmic(), incidencia.getFecCierre(), "OTRA_BT"));
		assertFalse(dao.isUmicErronea(incidencia2.getClaveUmic(), incidencia2.getFecCierre(), incidencia2.getBt()));
		dao.clear();
	}
	
	private Incidencia createIncidencia(Integer index){
		
		Incidencia incidencia = new Incidencia();
		incidencia.setBt("baseTecnica"+index);
		incidencia.setCcanal(index);
		incidencia.setCcartera(index);
		UmicKey key = new UmicKey();
		key.setKgarantia(index);
		incidencia.setClaveUmic(key);
		incidencia.setCodigoRetorno(String.valueOf(index));
		incidencia.setTextoError(String.valueOf(index));
		incidencia.setFecCierre(new Timestamp(0L));
		incidencia.setCnegocio("negocio"+index);
		incidencia.setGeneradorError("programa"+index);
		
		
		return incidencia;
	}
	
	@Test
	public void testObtenerSubErrores(){
		
		IncidenciaDao dao = new IncidenciaDao();
		dao.clear();
		
		Map<IncidenciaKey,Incidencia> map = new HashMap<IncidenciaKey,Incidencia>();
		for ( int i = 0; i < 5; i++) {
			Incidencia incidencia = createIncidencia(i);
			if (i>2) {
				incidencia.setCodigoRetorno("02");
			}
			map.put(incidencia.getKey(), incidencia);
		}
		
		dao.putAll(map);
		
		Map<String,Integer> mapaPrueba = dao.obtenerSubErrores();
		
		for (String texto : mapaPrueba.keySet()) {
			log.info(texto+ " :" + mapaPrueba.get(texto));
		}
		dao.clear();
		
	}
	
}	
