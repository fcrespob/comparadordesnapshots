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
import es.mapfre.solvencia.dominio.maestro.Duraciones;
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
 * Clase que testea el Módulo PRI007R 
 * 
 * @author 
 *
 */

public class ModuloPRI007RTest {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI007RTest.class);

	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2017, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL,01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2017, Calendar.MARCH,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1201;
	private static final int modalidad = 338;
	private static final int garantia = 266;
	private static final String negocio = "C";
	private static final long poliza = 419850;
	public static final int canal = 1;
	public static final String csitupol = "VI";
	public static final String cformpago = "1";
	public static final BigDecimal iprimanetaini = BigDecimal.valueOf(19282);
	public static final BigDecimal prevprima = BigDecimal.valueOf(2.5);
	public static final BigDecimal precargfrac = BigDecimal.valueOf(0);
	
	@BeforeClass
	public static void init() {
		ModuloPRI007RTest.LOG.debug("Inicializando pruebas");
		
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

		}catch (Exception e){
			ModuloPRI007RTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void ModuloPRI007RTest(){
		Modulo ModuloPRI007R = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI007R);
	
		try{
			BigDecimal pri007R= (BigDecimal) ModuloPRI007R.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(pri007R);
			TestCase.assertEquals(BigDecimal.valueOf(20258.15125).setScale(9, RoundingMode.HALF_DOWN), pri007R.setScale(9, RoundingMode.HALF_DOWN));
	
		}catch (Exception e){
			ModuloPRI007RTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		
		Umic umic = new Umic();
		Primas primas = new Primas();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		
		primas.setCformpago(cformpago);
		primas.setIprimanetaini(iprimanetaini);
		primas.setPrevprima(prevprima);
		primas.setPrecargfrac(precargfrac);
		umic.setPrimas(primas);
		
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2015, Calendar.JULY, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2017, Calendar.JULY, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2018, Calendar.JULY, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecfinpagprim = new Timestamp(new GregorianCalendar(2033, Calendar.JULY, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecinisus(fecinisus);
		fechas.setFecdesderenova(fecdesderenova);
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecfinpagprim(fecfinpagprim);
		umic.setFechas(fechas);
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol(csitupol);
		umic.setDatosGenerales(datosGenerales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();		
		
		proyUmic.add(detalleCorriente);
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaPago(FPAGO);	
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();	
		
		return btcUmic;
	}
}
