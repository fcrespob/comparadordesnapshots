package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FichaProcesoKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;

// JBMARTA - PYAM0025 - INI
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProcesoAdicional;
// JBMARTA - PYAM0025 - FIN

public class FichaProcesoDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(FichaProcesoDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		dao.put(ficha.getKey(), ficha);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		FichaProcesoKey key = ficha.getKey();
		
		dao.put(key, ficha);
		
		Assert.assertEquals(ficha, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		FichaProcesoKey key = ficha.getKey();
		
		dao.put(key, ficha);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
	}

	@Test
	public void testPut() throws Exception {
		
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		dao.put(ficha.getKey(), ficha);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		FichaProcesoDao dao = new FichaProcesoDao();
		
		Map<FichaProcesoKey,FichaProceso> map = new HashMap<FichaProcesoKey,FichaProceso>();
		for ( int i = 0; i < 10; i++) {
			FichaProceso ficha = createFichaProceso(i);
			map.put(ficha.getKey(), ficha);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		FichaProcesoKey key = ficha.getKey();
		dao.put(key, ficha);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
	}

	@Test
	public void testValues() throws Exception{
		
		FichaProcesoDao dao = new FichaProcesoDao();
		FichaProceso ficha = createFichaProceso(1);
		
		dao.put(ficha.getKey(), ficha);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private FichaProceso createFichaProceso(Integer index){
		
		FichaProceso ficha = new FichaProceso();
		ficha.setKejecucion(index);
		ficha.setKsistema("ksistema"+index);
		ficha.setKprotecnico("kprotecnico"+index);
		ficha.setKuejecucion("kuejecucion"+index);
		ficha.setKsecuencia(index);
		
		ficha.setFiltrosAmbito(new ArrayList<FiltroFichaProceso>());
        // NUEVO
        ficha.setFiltrosAdicionales(new ArrayList<FiltroFichaProcesoAdicional>());
        // FIN NUEVO
		
		return ficha;
	}
}