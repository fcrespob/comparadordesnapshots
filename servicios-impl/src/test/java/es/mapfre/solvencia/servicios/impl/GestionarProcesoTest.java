package es.mapfre.solvencia.servicios.impl;

import junit.framework.Assert;

import org.junit.Test;

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionProcesosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DisenoProcesosDao;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class GestionarProcesoTest {
	
	private static final String PROCESO = "calculo";
	
	@Test
	public void obtenerDisenoProcesoTest(){
		
		DefinicionProcesosDao defDao = new DefinicionProcesosDao();
		DisenoProcesosDao disDao = new DisenoProcesosDao();
		IGestionarProceso servicio = FachadaServicios.getGestionarProceso();
		
		DefinicionProcesos def1 = createDefinicionProcesos(1,false,PROCESO);
		defDao.put(def1.getKey(),def1);
		
		DisenoProcesos dis1 = createDisenoProcesos(1,def1.getCproceso(),null);
		disDao.put(dis1.getKey(),dis1);
		
		DisenoProcesos value = null;
				
		try {
			value = servicio.obtenerDisenoProceso(def1.getCproceso(), dis1.getKcompania(), dis1.getKramo(), dis1.getKmodalidad(), dis1.getKgarantia(), dis1.getKbasetec(), dis1.getKclaveadic());
			Assert.fail();
		} catch (Exception e) {
			Assert.assertNull(value);
		}
		
		def1.setKestado(true);
		defDao.put(def1.getKey(),def1);
		
		try {
			value = servicio.obtenerDisenoProceso(def1.getCproceso(), dis1.getKcompania(), dis1.getKramo(), dis1.getKmodalidad(), dis1.getKgarantia(), dis1.getKbasetec(), dis1.getKclaveadic());
			Assert.assertEquals(value, dis1);
		} catch (Exception e) {
			Assert.fail();
		}
		
		defDao.clear();
		disDao.clear();
	}
	
	
	
	private DefinicionProcesos createDefinicionProcesos(Integer index, boolean kestado,String cproceso){
		
		DefinicionProcesos defPro = new DefinicionProcesos();
		defPro.setCdescripabrev("cdescripabrev"+index);
		defPro.setCproceso(cproceso+index);
		defPro.setGdescrip("gdescrip"+index);
		defPro.setGrutadoc("grutadoc"+index);
		defPro.setKestado(kestado);
		
		return defPro;
	}
	
	
	private DisenoProcesos createDisenoProcesos(Integer index,String gproceso,String cgestespeci){
		
		DisenoProcesos disPro = new DisenoProcesos();
		disPro.setKcompania(index); 
		disPro.setKramo("kramo"+index);
		disPro.setKmodalidad(index);
		disPro.setKgarantia(index);
		disPro.setKbasetec("kbasetec"+index);
		disPro.setKclaveadic("kclaveadic"+index);
		disPro.setGproceso(gproceso);
		disPro.setCgestespeci(cgestespeci);
		return disPro;
	}

}
