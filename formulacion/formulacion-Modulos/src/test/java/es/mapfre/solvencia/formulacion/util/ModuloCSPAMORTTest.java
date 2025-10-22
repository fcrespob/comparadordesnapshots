package es.mapfre.solvencia.formulacion.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.maestro.CuadrosAmortizacionDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import junit.framework.TestCase;

/**
 * Clase que testea el Módulo CSPAMORT 
 */
public class ModuloCSPAMORTTest {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSPAMORTTest.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2017, Calendar.MAY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 04, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2017, Calendar.MARCH, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	private static final int cartera = 1101;
	private static final int modalidad = 256;
	private static final int garantia = 10;
	private static final String negocio = "I";
	private static final long poliza = 2365372;
	private static final int ccanal = 1;
	private static final String ctipoaport = "P";
	private static final String kprestacion = "0";
	
	@BeforeClass
	public static void init() {
		ModuloCSPAMORTTest.LOG.debug("Inicializando pruebas");
		
		daos();
	}
	
	@Test
	public void moduloCSPAMORTTest(){
		Modulo moduloCSPAMORT = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSPAMORT);
				
		try{
			BigDecimal cspAMORT = (BigDecimal) moduloCSPAMORT.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(cspAMORT);
			
			TestCase.assertEquals(BigDecimal.valueOf(43164.49722).setScale(5, RoundingMode.HALF_DOWN), cspAMORT.setScale(5, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloCSPAMORTTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
	
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setKsubpoliza(0);
		datosGenerales.setKajuste(0);
		datosGenerales.setNorden(10);
		datosGenerales.setNsuscri(1);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setCcanal(ccanal);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCtipoaport(ctipoaport);
		datosGenerales.setKcertificado(0);
		datosGenerales.setKprestacion(kprestacion);
		datosGenerales.setCsitupol("VI");
		
		Timestamp fecefecini = new Timestamp(new GregorianCalendar(2004, Calendar.JUNE, 04, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		Fechas fechas = new Fechas();
		fechas.setFecefecini(fecefecini);
		
		Duraciones duraciones = new Duraciones();
		duraciones.setNdursegano(25);
		
		umic.setDatosGenerales(datosGenerales);
		umic.setDuraciones(duraciones);
		umic.setFechas(fechas);
		umic.setBti(bti);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		detalleCorriente.setFechaHasta(FHASTA);
		detalleCorriente.setFcierre(FCIERRE);
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
	
	private static void daos(){
		try{
			CuadrosAmortizacionDao cuadroAmortizacionDao = new CuadrosAmortizacionDao();
			CuadrosAmortizacion    ca = new CuadrosAmortizacion();
			ca.setPintermor(BigDecimal.valueOf(3.5));
			ca.setNpercar(0);
			ca.setCapitalIni(BigDecimal.valueOf(71998.00));
			ca.setPorcrec(BigDecimal.valueOf(0));
			ca.setCperamont("4");
			ca.setCperaseg(0);
			ca.setKsubpoliza(0);
			ca.setKajuste(0);
			ca.setKgarantia(garantia);
			ca.setKmodalidad(modalidad);
			ca.setNorden(10);
			ca.setNsuscri(1);
			ca.setKpoliza(poliza);
			ca.setCcanal(ccanal);
			ca.setCnegocio(negocio);
			ca.setKcertificado(0);
			ca.setKprestacion(kprestacion);
			
			cuadroAmortizacionDao.put(ca.getKey(), ca);
			
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setGvariable("ID-TEMPORAL");
			da2.setCidentivariab("ID-TEMPORAL");
			da2.setGvalor("01");
			
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			
			
			
		}catch (Exception e){
			ModuloCSPAMORTTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}
}
