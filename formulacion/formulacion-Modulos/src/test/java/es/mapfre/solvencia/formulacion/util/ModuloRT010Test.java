package es.mapfre.solvencia.formulacion.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.maestro.*;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo RT010, módulo de cálculo de la cuantía por Rescate de las garantías dentro de 
 * Solvencia II para las modalidades de MILLÓN VIDA.
 * @author apedro
 *
 */
public class ModuloRT010Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT010Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 376;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 5029769L;
	
	@BeforeClass
	public static void init() {
		ModuloRT010Test.LOG.debug("Inicializando pruebas");
		
		try{
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
			DefinicionesAuxiliares da = new DefinicionesAuxiliares();
			da.setCnegocio(negocio);
			da.setKcarteorig(cartera);
			da.setKmodalidad(modalidad);
			da.setKgarantia(garantia);
			da.setGvariable("ID-TEMPORAL");
			da.setCidentivariab("ID-TEMPORAL");
			da.setGvalor("01");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			
			DetalleCorrienteDao detalleCorrienteDao = new DetalleCorrienteDao();
			DetalleCorriente dc = new DetalleCorriente();
			TotalFlujoProyeccion tfp = new TotalFlujoProyeccion();
			tfp.setProvbtiproy(BigDecimal.valueOf(8081.83));
			
			dc.setFcierre(FCIERRE);
			dc.setKmodalidad(modalidad);
			dc.setKpoliza(poliza);
			dc.setKsubpoliza(0);
			dc.setKcertificado(0);
			dc.setNsuscri(1);
			dc.setNorden(10);
			dc.setKgarantia(1);
			dc.setKprestacion("0");
			dc.setKajuste(0);
			dc.setCtipoaport("U");
			dc.setBt("BTI");
			dc.setTotalFlujoProyeccion(tfp);
			
			detalleCorrienteDao.put(dc.getKey(), dc);
			
		}catch (Exception e){
			ModuloRT010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloRT010Test(){
		Modulo moduloRT010 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT010);
				
		try{
			BigDecimal rt010 = (BigDecimal) moduloRT010.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			TestCase.assertEquals(BigDecimal.valueOf(8425.315747), rt010.setScale(6, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloRT010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		Rescates rescates = new Rescates();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2016, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setKsubpoliza(0);
		datosGenerales.setKcertificado(0);
		datosGenerales.setNsuscri(1);
		datosGenerales.setNorden(10);
		datosGenerales.setKprestacion("0");
		datosGenerales.setKajuste(0);
		datosGenerales.setCtipoaport("U");
		rescates.setTirIni(BigDecimal.valueOf(3.674177));
		rescates.setTirCie(BigDecimal.valueOf(1.376936));
		
		umic.setFechas(fechas);
		umic.setDatosGenerales(datosGenerales);
		umic.setRescates(rescates);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		proyUmic.add(detalleCorriente);
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaDevengo(FDEVENGO);
		
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setFecCierre(FCIERRE);
		
		return btcUmic;
	}
	
	
}