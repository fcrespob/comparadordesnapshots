package es.mapfre.solvencia.dao.impl.conversionesBel;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresCurvaTipoKey;
import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;

public class ValoresCurvaTipoDaoTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(ValoresCurvaTipoDaoTest.class);
	
	@Test
	public void testEntrySet()  throws Exception{
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo("1", new Timestamp(System.currentTimeMillis()));
		
		ValoresCurvaTipoKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertEquals(vct, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresCurvaTipoKey key = vct.getKey();
		
		dao.put(key, vct);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		
		Map<ValoresCurvaTipoKey,ValoresCurvaTipo> map = new HashMap<ValoresCurvaTipoKey,ValoresCurvaTipo>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(i), new Timestamp(addedDate+i));
			map.put( vct.getKey(), vct);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		ValoresCurvaTipoKey key = vct.getKey();
		dao.put(key, vct);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(1), new Timestamp(System.currentTimeMillis()));
		
		dao.put(vct.getKey(), vct);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testGetByFecha() throws Exception{

		ValoresCurvaTipoDao dao = new ValoresCurvaTipoDao();
		
		Timestamp fecha = new Timestamp(System.currentTimeMillis() - 1000);
		Timestamp fecha1 = new Timestamp(fecha.getTime());
		
		log.debug("Empezamos a insertar {}", fecha);
		
		for (int i = 101; i < 1000; i++) {
			ValoresCurvaTipo vct = createValoresCurvaTipo(String.valueOf(i), fecha);
			dao.put(vct.getKey(), vct);
			fecha = new Timestamp(System.currentTimeMillis() + i);
		}
		
		log.debug("Terminamos de insertar {}", fecha);
		
		Long lfEfec = fecha1.getTime() + 1;
		String codCurvaTipo = "101";
		
		
		log.debug("Buscábamos para {} {}",new Timestamp(lfEfec), lfEfec);
        
        List<ValoresCurvaTipo> valoresCurvaTipo = dao.getByFecha(codCurvaTipo, fecha1);
        Assert.assertNotNull(valoresCurvaTipo);
        Timestamp encontrado = new Timestamp(0);
        for (ValoresCurvaTipo valorEncontrado : valoresCurvaTipo) {
        	encontrado = valorEncontrado.getFecEfecCurva().after(encontrado) ? valorEncontrado.getFecEfecCurva() : encontrado;
        }
        
        Assert.assertTrue(encontrado.getTime()==fecha1.getTime());
        
        log.debug("Hemos encontrado {} {}", encontrado, encontrado.getTime());

        dao.clear();
    }
	
	
	private ValoresCurvaTipo createValoresCurvaTipo(String index, Timestamp fecha){
		
		ValoresCurvaTipo vct = new ValoresCurvaTipo();
		vct.setFecEfecCurva(fecha);
		vct.setCodCurvaTipos(index);
		vct.setPorcentajeInteres(new java.math.BigDecimal("2"));
		
		return vct;
	}
	
}
