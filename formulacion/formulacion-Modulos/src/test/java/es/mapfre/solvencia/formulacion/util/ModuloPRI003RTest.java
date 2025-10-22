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

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Primas;
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
 * Clase que testea el Módulo PRI003R, módulo de cálculo de la 
 * cuantía nominal de primas.
 */
public class ModuloPRI003RTest {

private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI003RTest.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 338;
	public static final int garantia = 266;
	public static final String negocio = "C";
	public static final int canal = 1;
	
	@BeforeClass
	public static void init() {
		ModuloPRI003RTest.LOG.debug("Inicializando pruebas");
		
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
				
			
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setCidentivariab("ID-CRITERIO");
			da2.setGvariable("ID-CRITERIO");
			da2.setGvalor("06");
			

			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);

		}catch (Exception e){
			ModuloPRI003RTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloPRI003RTest(){
		Modulo moduloPRI003R = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI003R);
				
		try{
			BigDecimal pri003R = (BigDecimal) moduloPRI003R.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(pri003R);

			TestCase.assertEquals(BigDecimal.valueOf(0), pri003R.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloPRI003RTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2033, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2016, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2015, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecdesderenova(fecdesderenova);
		fechas.setFecinisus(fecinisus);
		
		primas.setIprimanetaini(BigDecimal.valueOf(19282));
		primas.setPrevprima(BigDecimal.valueOf(2.5));
		primas.setCformpago("1");
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");

		bti.setPgastgesex1I(BigDecimal.valueOf(2));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente;
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		Timestamp fecDesde = FDESDE;
		
		for (int i=0;i<85;i++){
			detalleCorriente = new DetalleCorriente();
			detalleCorriente.setFechaDesde(fecDesde);
			proyUmic.add(detalleCorriente);
			
			fecDesde = UtilFechas.incrMeses(fecDesde, FCALC, 1, false);
		}
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaPago(FPAGO);
		
		return bloqueCorriente;
	}
	
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(0.06));
		itcalc.add(BigDecimal.valueOf(0));
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		
		return btcUmic;
	}
}
