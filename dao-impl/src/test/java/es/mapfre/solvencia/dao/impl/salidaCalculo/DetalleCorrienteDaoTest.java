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
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleCorrienteKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

public class DetalleCorrienteDaoTest extends TestCase {
	private static final String TIPO_CORRIENTE = "tipoCorriente";

	private static Logger log = LoggerFactory.getLogger(DetalleCorrienteDaoTest.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/files/DetalleCorriente.txt";
	private static String zippedDataFileName = "datos.txt.gz";
	private static boolean COMPRIMIR = false;
	
	@Test
	public void testLoadCache() throws Exception{
	}

	@Test
	public void testEntrySet()  throws Exception{
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BTI);
		
		dao.put(dtCorriente.getKey(), dtCorriente);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BTI);
		
		DetalleCorrienteKey key = dtCorriente.getKey();
		
		dao.put(key, dtCorriente);
		
		Assert.assertEquals(dtCorriente, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BTI);
		
		DetalleCorrienteKey key = dtCorriente.getKey();
		
		dao.put(key, dtCorriente);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BTI);
		
		dao.put(dtCorriente.getKey(), dtCorriente);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		
		Map<DetalleCorrienteKey,DetalleCorriente> map = new HashMap<DetalleCorrienteKey,DetalleCorriente>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DetalleCorriente dtCorriente = createDetalleCorriente(i, new Timestamp(addedDate+i),ConstantesSolvencia.BASE_BTI);
			map.put(dtCorriente.getKey(), dtCorriente);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BEL);
		
		DetalleCorrienteKey key = dtCorriente.getKey();
		dao.put(key, dtCorriente);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),ConstantesSolvencia.BASE_BTI);
		
		dao.put(dtCorriente.getKey(), dtCorriente);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	
	@Test
	public void testgetValues() throws Exception{
		
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		DetalleCorriente dtCorriente = null;
		UmicKey umicKey = null;
		
		String[] bts = new String[]{ConstantesSolvencia.BASE_BTI,ConstantesSolvencia.BASE_BTI,ConstantesSolvencia.BASE_BTI,ConstantesSolvencia.BASE_BTI,ConstantesSolvencia.BASE_BTI,ConstantesSolvencia.BASE_BEL,ConstantesSolvencia.BASE_BEL,ConstantesSolvencia.BASE_BEL,ConstantesSolvencia.BASE_ROSSP,ConstantesSolvencia.BASE_ROSSP,ConstantesSolvencia.BASE_ROSSP};
		int total = 10;
		for (int i = 0; i <= total; i++) {
			
			dtCorriente = createDetalleCorriente(i, fecha,bts[i],total);
			dao.put(dtCorriente.getKey(), dtCorriente);
		}

		umicKey = new UmicKey(dtCorriente.getCtipoaport(), dtCorriente.getKajuste(), dtCorriente.getKcertificado(), dtCorriente.getKgarantia(),dtCorriente.getKmodalidad(), dtCorriente.getKpoliza(), dtCorriente.getKprestacion(), dtCorriente.getKsubpoliza(), dtCorriente.getNorden(), dtCorriente.getNsuscri());
		
		List<DetalleCorriente> lista = dao.getValues(ConstantesSolvencia.BASE_BTI, fecha, umicKey );
		Assert.assertTrue(lista!=null && lista.size() == 5);
		
		lista = dao.getValues(ConstantesSolvencia.BASE_BEL, fecha, umicKey );
		Assert.assertTrue(lista!=null && lista.size() == 3);
		
		lista = dao.getValues(ConstantesSolvencia.BASE_ROSSP, fecha, umicKey );
		Assert.assertTrue(lista!=null && lista.size() == 3);
		
		dao.clear();
	}
	
	private DetalleCorriente createDetalleCorriente(Integer index, Timestamp fecha, String bt){
		return createDetalleCorriente(index,fecha,bt,index);
	}
	
	private DetalleCorriente createDetalleCorriente(Integer index, Timestamp fecha, String bt, Integer total){
		
		DetalleCorriente dtCorriente = new DetalleCorriente();

		dtCorriente.setFcierre(fecha);
		dtCorriente.setBt(bt);

		dtCorriente.setCnegocio("cnegocio"+index);
		dtCorriente.setCcanal(index+1);
		dtCorriente.setCcartera(index+2);
		
		dtCorriente.setKpoliza(1L);
		dtCorriente.setKsubpoliza(total+1);
		dtCorriente.setKprestacion("kprestacion");
		dtCorriente.setNorden(total+2);
		dtCorriente.setNsuscri(total+3);
		dtCorriente.setKmodalidad(total+4);
		dtCorriente.setKgarantia(total+5);
		dtCorriente.setKcertificado(total+6);
		dtCorriente.setKajuste(total+7);
		dtCorriente.setCtipoaport("ctipoaport");
		
		return dtCorriente;
	}
}	
