package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.IPCGeneralFuturoKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;

public class IPCGeneralFuturoDaoTest extends TestCase {
	
	private static Logger log = LoggerFactory.getLogger(IPCGeneralFuturoDaoTest.class);

	@Test
	public void testEntrySet()  throws Exception{
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		dao.put(ipcGeneral.getKey(), ipcGeneral);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		IPCGeneralFuturoKey key = ipcGeneral.getKey();
		
		dao.put(key, ipcGeneral);
		
		Assert.assertEquals(ipcGeneral, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		IPCGeneralFuturoKey key = ipcGeneral.getKey();
		
		dao.put(key, ipcGeneral);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		dao.put(ipcGeneral.getKey(), ipcGeneral);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		
		Map<IPCGeneralFuturoKey,IPCGeneralFuturo> map = new HashMap<IPCGeneralFuturoKey,IPCGeneralFuturo>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(i,new Timestamp(System.currentTimeMillis()));
			map.put(ipcGeneral.getKey(), ipcGeneral);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		IPCGeneralFuturoKey key = ipcGeneral.getKey();
		dao.put(key, ipcGeneral);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		
		dao.put(ipcGeneral.getKey(), ipcGeneral);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testgetValues() throws Exception{
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		dao.put(ipcGeneral.getKey(), ipcGeneral);
		
		IPCGeneralFuturo ipcGeneral2 = createIPCGeneralFuturo(2,new Timestamp(0));
		dao.put(ipcGeneral2.getKey(), ipcGeneral2);
		
		List<IPCGeneralFuturo> ipcGeneralResultado = dao.getValues(new Timestamp(0));
		
		Iterator iter = dao.values().iterator();
		
		Assert.assertTrue(!ipcGeneralResultado.isEmpty());
		Assert.assertEquals(ipcGeneral2, ipcGeneralResultado.get(0));
		
		dao.clear();
	}
	
	private IPCGeneralFuturo createIPCGeneralFuturo(Integer index, Timestamp date){
		
		IPCGeneralFuturo ipcGeneral = new IPCGeneralFuturo();
		
		long after = date.getTime() + (index*24*60*60*1000); 
		long before = date.getTime() - (index*24*60*60*1000); 
		
		ipcGeneral.setFfin(new Timestamp(after));
		ipcGeneral.setFinicio(new Timestamp(before));
		ipcGeneral.setPipcgas(new java.math.BigDecimal(index*0.5));
		ipcGeneral.setPipcleg(new java.math.BigDecimal(index*-0.5));
		
		return ipcGeneral;
	}
}	