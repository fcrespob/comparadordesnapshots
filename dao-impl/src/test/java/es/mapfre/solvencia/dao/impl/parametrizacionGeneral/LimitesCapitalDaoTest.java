package es.mapfre.solvencia.dao.impl.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.LimitesCapitalKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;

public class LimitesCapitalDaoTest {
	
	@Test
	public void testEntrySet()  throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		dao.put(lC.getKey(), lC);		
		
		Assert.assertTrue(dao.entrySet().size() > 0); 
		
		dao.clear();
	}

	@Test
	public void testGet()  throws Exception{
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		LimitesCapitalKey key = lC.getKey();
		
		dao.put(key, lC);
		
		Assert.assertEquals(lC, dao.get(key));
				
		dao.clear();		
	}

	@Test
	public void testKeySet()  throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		LimitesCapitalKey key = lC.getKey();
		
		dao.put(key, lC);
		
		Assert.assertTrue(dao.keySet().contains(key));
		
		dao.clear();	
		
	}

	@Test
	public void testPut() throws Exception {
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		dao.put(lC.getKey(), lC);
		
		Assert.assertTrue(dao.getCache().size() == 1);
				
		dao.clear();		
	}

	@Test
	public void testPutAll()  throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		
		Map<LimitesCapitalKey,LimitesCapital> map = new HashMap<LimitesCapitalKey,LimitesCapital>();
		long addedDate = System.currentTimeMillis();
		for ( int i = 0; i < 10; i++) {
			LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(addedDate+i),10+i,20+i,3,"2","4");
			map.put(lC.getKey(), lC);
		}
		
		dao.putAll(map);
		
		Assert.assertTrue(dao.size() == 10);
				
		dao.clear();		
	}

	@Test
	public void testRemove() throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		LimitesCapitalKey key = lC.getKey();
		dao.put(key, lC);
		Assert.assertTrue(dao.getCache().size() == 1);
		
		dao.remove(key);
		Assert.assertTrue(dao.getCache().isEmpty());
		
		dao.clear();		
		
	}

	@Test
	public void testValues() throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		LimitesCapital lC = createLimitesCapital("mod","gar",new Timestamp(System.currentTimeMillis()),10,20,3,"2","4");
		
		dao.put(lC.getKey(), lC);
		
		Assert.assertTrue(dao.values().size() == 1);
		
		dao.clear();
	}
	
	@Test
	public void testgetValues() throws Exception{
		
		LimitesCapitalDao dao = new LimitesCapitalDao();
		
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		
		LimitesCapital lC = createLimitesCapital("mo1","ga1",fecha,10,20,3,"2","4");
		LimitesCapital lC2 = createLimitesCapital("mo2","ga2",fecha,20,30,3,"2","4");
		LimitesCapital lC3 = createLimitesCapital("mo3","ga3",fecha,10,20,3,"2","4");
				
		dao.put(lC.getKey(), lC);
		dao.put(lC2.getKey(), lC2);
		dao.put(lC3.getKey(), lC3);
		
		List<LimitesCapital> lCtest = dao.getValue("646", "mo1","ga1", fecha, 15, 3);
		
		Assert.assertEquals(lC, lCtest.get(0));

		dao.clear();
	}
	
	@Test
	public void testOrdenados(){
		LimitesCapitalDao dao = new LimitesCapitalDao();
		
		Timestamp fecha = new Timestamp(2013, 10, 22, 0, 0, 0, 0);
		
		LimitesCapital l1 = createLimitesCapital("475","1",fecha,0,999,20,"2","4");
		LimitesCapital l2 = createLimitesCapital("475","1",fecha,0,999,55,"2","4");
		LimitesCapital l3 = createLimitesCapital("475","1",fecha,0,999,5,"2","4");
		
		dao.put(l1.getKey(), l1);
		dao.put(l2.getKey(), l2);
		dao.put(l3.getKey(), l3);
		
		List<LimitesCapital> limites = dao.getValue("646", "475", "1", fecha, 60, 2);
		
		Assert.assertTrue(limites.size() == 3);
		Assert.assertTrue(limites.get(0).getNmeshasta()<limites.get(1).getNmeshasta());
		Assert.assertTrue(limites.get(1).getNmeshasta()<limites.get(2).getNmeshasta());
		
		dao.clear();
	}
	
	private LimitesCapital createLimitesCapital(String kmodalidad, String kgarantia, Timestamp fefecfin, 
			Integer kedad1, Integer kedad2, Integer nmeshasta, String gspaces1, String nregistro){
		
		LimitesCapital lC = new LimitesCapital();
		lC.setNtabla("646");
		lC.setKmodalidad(kmodalidad);
		lC.setKgarantia(kgarantia);
		lC.setFefecfin(fefecfin);
		lC.setKedad1(kedad1);
		lC.setKedad2(kedad2);
		lC.setNmeshasta(nmeshasta);
		lC.setGspaces1(gspaces1);
		lC.setNregistro(nregistro);
		
		return lC;
	}
}