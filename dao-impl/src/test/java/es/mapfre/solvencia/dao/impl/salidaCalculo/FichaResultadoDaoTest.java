package es.mapfre.solvencia.dao.impl.salidaCalculo;

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

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;

public class FichaResultadoDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(FichaResultadoDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado fichaResultado = createFichaResultado(1);
		
		dao.put(fichaResultado);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado fichaResultado = createFichaResultado(1);
						
		dao.put(fichaResultado);
		
		boolean encontrado = false;
		
		List<FichaResultado> fichas = new ArrayList<FichaResultado>();
		
		fichas = dao.get(fichaResultado.getKey());
		
		for (FichaResultado ficha: fichas) {
			if (ficha.equals(fichaResultado)) {
				encontrado = true;
			}
		}
		
		Assert.assertTrue(encontrado);
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado fichaResultado = createFichaResultado(1);
		
		FichaResultadoKey key = fichaResultado.getKey();
		
		dao.put(fichaResultado);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado fichaResultado = createFichaResultado(1);
		
		dao.put(fichaResultado);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		FichaResultadoDao dao = new FichaResultadoDao();
		dao.clear();
		
		Map<FichaResultadoKey,FichaResultado> map = new HashMap<FichaResultadoKey,FichaResultado>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			FichaResultado FichaResultado = createFichaResultado(i);
			map.put(FichaResultado.getKey(), FichaResultado);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado FichaResultado = createFichaResultado(1);
		
		FichaResultadoKey key = FichaResultado.getKey();
		
		dao.put(FichaResultado);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		FichaResultadoDao dao = new FichaResultadoDao();
		FichaResultado FichaResultado = createFichaResultado(1);
		
		dao.put(FichaResultado);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	public FichaResultado createFichaResultado(Integer index) {
		
		FichaResultado registroResultado = new FichaResultado();
		registroResultado.setKejecucion(index);
		registroResultado.setKsistema(String.valueOf(index));
		registroResultado.setKprotecnico(String.valueOf(index));
		registroResultado.setKuejecucion(String.valueOf(index));
		registroResultado.setKsecuencia(index);
		registroResultado.setKtipores("HIST");
		registroResultado.setKsecres(000);
		registroResultado.setGc1Resul("");
		
		return registroResultado;
	}
}	
