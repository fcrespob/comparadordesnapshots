package es.mapfre.solvencia.servicios.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.formulacion.PeriodoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TotalesFlujosDao;
import es.mapfre.solvencia.dominio.formulacion.Periodo;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class AlmacenarDatosTest {
	
	private static final String TIPO_CORRIENTE = "tipoCorriente";

	private static final String BTI = "BTI";
	private static final String ROSSP = "ROSSP";
	private static final String BEL = "BEL";

	private static Logger log = LoggerFactory.getLogger(AlmacenarDatosTest.class);
	
	private IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private IObtenerDatos servicioObtener = FachadaServicios.getObtenerDatos();
	
	
	@Test
	public void almacenarPlanPagosTest() {
		
		UmicKey key = createUmicKey(1);
		List<PlanPagos> listaPlanPagos = createPlanPagos(1);
		
		servicioAlmacenar.almacenarPlanPagos(key, listaPlanPagos);
		
		Assert.assertNotNull(servicioObtener.recuperarPlanPagos(key));

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

	@Test
	public void almacenarPeriodosTest() {
		PeriodoDao periodoDao = new PeriodoDao();
		
		Periodo periodo = createPeriodo(new Timestamp(System.currentTimeMillis()), 
				new Timestamp(System.currentTimeMillis()), "1", 1, 1, 
				new Timestamp(System.currentTimeMillis()), 1, 1L, 1, 1, 1, 1, 1, "1", 1, "1");
		servicioAlmacenar.almacenarPeriodos(periodo);
		
		Assert.assertNotNull(periodoDao.get(periodo.getKey()));
		periodoDao.clear();
	}
	
	public Periodo createPeriodo(Timestamp fechaInicio, Timestamp fechaFin, String cnegocio, Integer ccanal, Integer ccartera, Timestamp fcierre, Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri, Integer norden, Integer kgarantia, String kprestacion, Integer kajuste, String ctipoaport) {
		
		Periodo periodo = new Periodo();
		
		periodo.setFechaInicio(fechaInicio);
		periodo.setFechaFin(fechaFin);
		periodo.setCnegocio(cnegocio);
		periodo.setCcanal(ccanal);
		periodo.setCcartera(ccartera);
		periodo.setFcierre(fcierre);
		periodo.setKmodalidad(kmodalidad);
		periodo.setKpoliza(kpoliza);
		periodo.setKsubpoliza(ksubpoliza);
		periodo.setKcertificado(kcertificado);
		periodo.setNsuscri(nsuscri);
		periodo.setNorden(norden);
		periodo.setKgarantia(kgarantia);
		periodo.setKprestacion(kprestacion);
		periodo.setKajuste(kajuste);
		periodo.setCtipoaport(ctipoaport);
		
		return periodo;
	}

	@Test
	public void almacenarProyeccionTest() {
		DetalleCorrienteDao dCdao = new DetalleCorrienteDao();
		
		DetalleCorriente dtCorriente = createDetalleCorriente(1, new Timestamp(System.currentTimeMillis()),BEL,43); 
		
		servicioAlmacenar.almacenarProyeccion(dtCorriente);
		
		Assert.assertNotNull(dCdao.get(dtCorriente.getKey()));
		
	}
	
	private DetalleCorriente createDetalleCorriente(Integer index, Timestamp fecha, String bt, Integer total){
		
		DetalleCorriente dtCorriente = new DetalleCorriente();

		dtCorriente.setFcierre(fecha);
		dtCorriente.setBt(bt);

		dtCorriente.setCnegocio("cnegocio"+index);
		dtCorriente.setCcanal(index);
		dtCorriente.setCcartera(index);
		
		dtCorriente.setKpoliza(new Long(index));
		dtCorriente.setKsubpoliza(total);
		dtCorriente.setKprestacion("kprestacion");
		dtCorriente.setNorden(total);
		dtCorriente.setNsuscri(total);
		dtCorriente.setKmodalidad(total);
		dtCorriente.setKgarantia(total);
		dtCorriente.setKcertificado(total);
		dtCorriente.setKajuste(total);
		dtCorriente.setCtipoaport("ctipoaport");
		
		return dtCorriente;
	}

	@Test
	public void almacenarTotalFlujosTest() {
		TotalesFlujosDao tFdao = new TotalesFlujosDao();
		
		TotalesFlujos totalesF = createTotalesFlujos(1111111,1111111,11111,11111,1111111111L,"aaaaaa",1111);
		
		servicioAlmacenar.almacenarTotalFlujos(totalesF);
		
		Assert.assertNotNull(tFdao.get(totalesF.getKey()));
		
		tFdao.clear();
	}
	
	private TotalesFlujos createTotalesFlujos(Integer kajuste, Integer kcertificado, Integer kgarantia, 
			Integer kmodalidad, Long kpoliza, String kprestacion, Integer ksubpoliza){
		
		TotalesFlujos totalesF = new TotalesFlujos();
		totalesF.setKajuste(kajuste);
		totalesF.setKcertificado(kcertificado);
		totalesF.setKgarantia(kgarantia);
		totalesF.setKmodalidad(kmodalidad);
		totalesF.setKpoliza(kpoliza);
		totalesF.setKprestacion(kprestacion);
		totalesF.setKsubpoliza(ksubpoliza);		
		
		return totalesF;
	}
	
}
