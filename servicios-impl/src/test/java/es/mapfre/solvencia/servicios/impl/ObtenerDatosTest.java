package es.mapfre.solvencia.servicios.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.formulacion.PlanPagosDao;
import es.mapfre.solvencia.dao.impl.maestro.PagosPlanificadosDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.formulacion.PlanPagos;
import es.mapfre.solvencia.dominio.maestro.PagosPlanificados;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;

public class ObtenerDatosTest {

	private static Logger log = LoggerFactory.getLogger(ObtenerDatosTest.class);

	private static final String TIPO_CORRIENTE = "tipoCorriente";

	private IObtenerDatos servicio = FachadaServicios.getObtenerDatos();

	private PagosPlanificados createPagosPlanificados(Integer index,
			Timestamp fecha) {

		PagosPlanificados pagoPlan = new PagosPlanificados();
		pagoPlan.setCgarantia(index);
		pagoPlan.setCprestaEntorno("k");
		pagoPlan.setCprestaFict("cprestaFict" + index);
		pagoPlan.setEplreaBruto(new java.math.BigDecimal(index + 0.5));
		pagoPlan.setEplreaNeto(new java.math.BigDecimal(index + 0.5));
		pagoPlan.setErrorPeriodic("errorPeriodic" + index);
		pagoPlan.setFplreaEfecto(fecha);
		pagoPlan.setKajuste(index);
		pagoPlan.setKcerti(index);
		pagoPlan.setKgrsus(index);
		pagoPlan.setKpoliza(new Long(index));
		pagoPlan.setKpresta("kpre");
		pagoPlan.setKsubpol(index);
		pagoPlan.setPcoase(new java.math.BigDecimal(index + 0.5));
		pagoPlan.setPeriodicidad("periodicidad" + index);

		return pagoPlan;
	}

	@Test
	public void recuperarPagosPlanificadosTest() {

		// Insertamos 100 pagos planificados
		PagosPlanificadosDao dao = new PagosPlanificadosDao();
		PagosPlanificados var = null;

		long counter = 0;

		Timestamp fecha = new Timestamp(System.currentTimeMillis());

		for (int i = 0; i < 100; i++) {
			fecha = new Timestamp(fecha.getTime() + counter);
			log.debug("fecha = {}", fecha);
			var = createPagosPlanificados(1, fecha);
			dao.put(var.getKey(), var);
			counter = counter + 10000;
		}

		Set pagos = dao.getValues(1, 1L, 1, 1, 1, 1, "k", "kpre", new Timestamp(
				0));

		log.debug("pagos size = {}", pagos.size());

		Assert.assertTrue(pagos.size() == 100);

		dao.clear();
	}

	private UmicKey createUmicKey(Integer index) {

		return new UmicKey("ctipoaport" + index, index, index, index, index,
				new Long(index), "kprestacion" + index, index, index, index);
	}

	private List<PlanPagos> createPlanPagos(Integer index) {

		int init = index * 10;
		long dia = 1000 * 60 * 60 * 24;
		List<PlanPagos> lista = new ArrayList<PlanPagos>();
		PlanPagos planPagos = null;
		for (int i = init; i < init + 10; i++) {
			planPagos = new PlanPagos();
			planPagos.setImpPago(new java.math.BigDecimal(init + "0.05"));
			planPagos.setFecPago(new Timestamp(dia * i));
			lista.add(planPagos);
		}

		return lista;
	}

	@Test
	public void recuperarPlanPagosTest() {

		PlanPagosDao dao = new PlanPagosDao();

		Map<UmicKey, List<PlanPagos>> map = new HashMap<UmicKey, List<PlanPagos>>();
		for (int i = 0; i < 10; i++) {
			List<PlanPagos> planPagos = createPlanPagos(i);
			map.put(createUmicKey(i), planPagos);
		}

		dao.putAll(map);

		UmicKey trueKey = createUmicKey(1);
		Assert.assertEquals(servicio.recuperarPlanPagos(trueKey),
				map.get(trueKey));

		dao.clear();
	}

	private DetalleCorriente createDetalleCorriente(Integer index,
			Timestamp fecha, String bt, Integer total) {

		DetalleCorriente dtCorriente = new DetalleCorriente();

		dtCorriente.setFcierre(fecha);
		dtCorriente.setBt(bt);

		dtCorriente.setCnegocio("cnegocio" + index);
		dtCorriente.setCcanal(index);
		dtCorriente.setCcartera(index);

		dtCorriente.setKpoliza(1L);
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
	public void recuperarProyBTI() {
		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		DetalleCorriente dtCorriente = null;
		UmicKey umicKey = null;

		String[] bts = new String[] { ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BEL, ConstantesSolvencia.BASE_BEL, ConstantesSolvencia.BASE_BEL,
				ConstantesSolvencia.BASE_ROSSP, ConstantesSolvencia.BASE_ROSSP, ConstantesSolvencia.BASE_ROSSP };
		int total = 10;
		for (int i = 0; i <= total; i++) {

			dtCorriente = createDetalleCorriente(i, fecha, bts[i], total);
			dao.put(dtCorriente.getKey(), dtCorriente);
		}

		umicKey = new UmicKey(dtCorriente.getCtipoaport(),
				dtCorriente.getKajuste(), dtCorriente.getKcertificado(),
				dtCorriente.getKgarantia(), dtCorriente.getKmodalidad(),
				dtCorriente.getKpoliza(), dtCorriente.getKprestacion(),
				dtCorriente.getKsubpoliza(), dtCorriente.getNorden(),
				dtCorriente.getNsuscri());

		List<DetalleCorriente> lista = servicio
				.recuperarProyBTI(fecha, umicKey);
		Assert.assertTrue(lista != null && lista.size() == 5);
		dao.clear();
	}

	@Test
	public void recuperarProyeccion() {

		DetalleCorrienteDao dao = new DetalleCorrienteDao();
		Timestamp fecha = new Timestamp(System.currentTimeMillis());
		DetalleCorriente dtCorriente = null;
		UmicKey umicKey = null;

		String[] bts = new String[] { ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BTI, ConstantesSolvencia.BASE_BEL, ConstantesSolvencia.BASE_BEL, ConstantesSolvencia.BASE_BEL,
				ConstantesSolvencia.BASE_ROSSP, ConstantesSolvencia.BASE_ROSSP, ConstantesSolvencia.BASE_ROSSP };
		int total = 10;
		for (int i = 0; i <= total; i++) {

			dtCorriente = createDetalleCorriente(i, fecha, bts[i], total);
			dao.put(dtCorriente.getKey(), dtCorriente);
		}

		umicKey = new UmicKey(dtCorriente.getCtipoaport(),
				dtCorriente.getKajuste(), dtCorriente.getKcertificado(),
				dtCorriente.getKgarantia(), dtCorriente.getKmodalidad(),
				dtCorriente.getKpoliza(), dtCorriente.getKprestacion(),
				dtCorriente.getKsubpoliza(), dtCorriente.getNorden(),
				dtCorriente.getNsuscri());

		List<DetalleCorriente> lista = servicio.recuperarProyeccion(ConstantesSolvencia.BASE_BEL, fecha,
				umicKey);
		Assert.assertTrue(lista != null && lista.size() == 3);

		lista = servicio.recuperarProyeccion(ConstantesSolvencia.BASE_ROSSP, fecha, umicKey);
		Assert.assertTrue(lista != null && lista.size() == 3);

		dao.clear();
	}

}
