package es.mapfre.solvencia.dao.impl.formulacion;

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
import es.mapfre.solvencia.dao.impl.formulacion.PlanPagosDao;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;

public class PlanPagosDaoTest extends TestCase {
	private static Logger log = LoggerFactory.getLogger(PlanPagosDaoTest.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/files/PlanPagos.txt";
	private static String zippedDataFileName = "datos.txt.gz";
	private static boolean COMPRIMIR = false;



	@Test
	public void testLoadCache() throws Exception{
//		PlanPagosDao dao = new PlanPagosDao();
//		
//		BeanIOReader in = new BeanIOReader(BEANIO_CONFIG_XML, COMPRIMIR ? zippedDataFileName : dataFileName, "curvasTipo656");
//		Map<PlanPagosKey,PlanPagos> mapValoresCurvasTipo = new HashMap<PlanPagosKey,PlanPagos>();
//		
//		try {
//			PlanPagos value = null;
//			
//			while ((value = (PlanPagos) in.read()) != null) {
//				log.debug("Leído: {}", value);
//				
//				mapValoresCurvasTipo.put(new PlanPagosKey(value.getCodCurvaTipos(),value.getFecEfecCurva()),value);
//			}
//		} catch (InvalidRecordException ire) {
//			log.error("Error parseando fichero: {}", ire.toString());
//		} 
//		if (in != null) {
//			in.close();
//		}
//		
//		dao.putAll(mapValoresCurvasTipo);
//		
//		dao.loadCache(null);
//		Assert.assertTrue(dao.getCache().size() > 0);
//		
//		dao.clear();
	}

	@Test
	public void testEntrySet()  throws Exception{
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		dao.put(createUmicKey(1), planPagos);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		UmicKey key = createUmicKey(1);
		
		dao.put(key, planPagos);
		
		Assert.assertEquals(planPagos, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		UmicKey key = createUmicKey(1);
		
		dao.put(key, planPagos);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		dao.put(createUmicKey(1), planPagos);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		PlanPagosDao dao = new PlanPagosDao();
		
		Map<UmicKey,List<PlanPagos>> map = new HashMap<UmicKey,List<PlanPagos>>();
		for ( int i = 0; i < 10; i++) {
			List<PlanPagos> planPagos = createPlanPagos(i);
			map.put(createUmicKey(i), planPagos);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		UmicKey key = createUmicKey(1);
		
		dao.put(key, planPagos);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		PlanPagosDao dao = new PlanPagosDao();
		List<PlanPagos> planPagos = createPlanPagos(1);
		
		dao.put(createUmicKey(1), planPagos);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	private UmicKey createUmicKey(Integer index){
		
		return new UmicKey("ctipoaport"+index, index, index, index, index, new Long(index),
			"kprestacion"+index, index, index, index);
	}
	
	private List<PlanPagos> createPlanPagos(Integer index){
		
		int init = index*10;
		long dia = 1000*60*60*24;
		List<PlanPagos> lista = new ArrayList<PlanPagos>();
		PlanPagos planPagos = null;
		for (int i = init; i < init+10; i++) {
			planPagos = new PlanPagos();
			planPagos.setImpPago(new java.math.BigDecimal(init+"0.05"));
			planPagos.setFecPago(new Timestamp(dia*i));
			lista.add(planPagos);
		}
		
		return lista;
	}
}	
