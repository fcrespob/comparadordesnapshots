package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.io.File;
import java.math.BigDecimal;
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
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleBaseTecnicaKey;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class DetalleBaseTecnicaDaoTest extends TestCase {

	private static Logger log = LoggerFactory.getLogger(DetalleBaseTecnicaDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		dao.put(dbt.getKey(), dbt);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		DetalleBaseTecnicaKey key = dbt.getKey();
		
		dao.put(key, dbt);
		
		Assert.assertEquals(dbt, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		DetalleBaseTecnicaKey key = dbt.getKey();
		
		dao.put(key, dbt);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		dao.put(dbt.getKey(), dbt);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		
		Map<DetalleBaseTecnicaKey,DetalleBaseTecnica> map = new HashMap<DetalleBaseTecnicaKey,DetalleBaseTecnica>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			DetalleBaseTecnica dbt = createDetalleBaseTecnica(i);
			map.put(dbt.getKey(), dbt);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		DetalleBaseTecnicaKey key = dbt.getKey();
		dao.put(key, dbt);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		DetalleBaseTecnica dbt = createDetalleBaseTecnica(1);
		
		dao.put(dbt.getKey(), dbt);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testWriteAndRead() throws Exception{
		DetalleBaseTecnicaDao dao = new DetalleBaseTecnicaDao();
		
		for ( int i = 0; i < 10; i++) {
			DetalleBaseTecnica dbt = createDetalleBaseTecnica(i);
			dao.put(dbt.getKey(), dbt);
		}
		
		String filepath = "TestDetallesBaseTecnica.txt";
		
		BeanIOWriter out = new BeanIOWriter("beanio/beanio-config-out.xml", filepath, "detalle-basetecnicas");
		dao.exportCache(out);
		out.close();
		
		dao.clear();
	}
	
	
	private DetalleBaseTecnica createDetalleBaseTecnica(Integer index){
		
		List <BigDecimal> numeros10 = new ArrayList<BigDecimal>();
		for (int i = 0; i < 10; i++) {
			numeros10.add(new BigDecimal("1"+index));
		}
		
		List <BigDecimal> numeros5 = new ArrayList<BigDecimal>();
		for (int i = 0; i < 5; i++) {
			numeros5.add(new BigDecimal("1"+index));
		}
		
		DetalleBaseTecnica dbt = new DetalleBaseTecnica();
		dbt.setBaseTec("baseTec"+index);
		dbt.setCcanal(index);
		dbt.setCcartera(index);
		dbt.setCnegocio("cnegocio"+index);
		dbt.setCurvaTi("curvaTi"+index);
		dbt.setEdadcalc(numeros5);
		dbt.setFactor1(numeros5.get(0));
		dbt.setFactor2(numeros10);
		dbt.setFtablaAn(new Timestamp(0L));
		dbt.setFcurvaTi(new Timestamp(0L));
		dbt.setFecCierre(new Timestamp(0L));
		dbt.setPerTransRossp(true);
		dbt.setMetodoPtRossp("metodoPt"+index);
		dbt.setFtablaAn(new Timestamp(0L));
		dbt.setTablaTanul("tablaTanul"+index);
		dbt.setGtorosspPrima(numeros5.get(3));
		dbt.setGtorosspCap(numeros5.get(2));
		dbt.setGtorosspProv(numeros5.get(4));
		dbt.setGtoUni(numeros5.get(1));
		dbt.setGtoprov(numeros5.get(0));
		dbt.setUmicKey( new UmicKey("ctipoaport", index,index,index,index,new Long(index),"kprestacion"+index,index,index,index));
		
		return dbt;
	}
}	
