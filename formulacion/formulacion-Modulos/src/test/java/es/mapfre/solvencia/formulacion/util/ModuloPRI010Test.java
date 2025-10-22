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

/**
 * Clase que testea el Módulo PRI010, módulo de cálculo que devuelve ela prima en el periodo que le corresponda pagar al 
 * asegurado.
 * @author apedro
 *
 */

public class ModuloPRI010Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI010Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2015, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2015, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2015, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2015, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2015, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 248;
	public static final int garantia = 200;
	public static final String negocio = "C";
	public static final int canal = 1;
	
	
	
	@BeforeClass
	public static void init() {
		ModuloPRI010Test.LOG.debug("Inicializando pruebas");
		
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
			ModuloPRI010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloPRI010Test(){
		Modulo moduloPRI010 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI010);
				
		try{
			BigDecimal pri010 = (BigDecimal) moduloPRI010.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			//System.out.println(pri010);

			TestCase.assertEquals(BigDecimal.valueOf(6896.51), pri010.setScale(2, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloPRI010Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(9999, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2005, 11, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecfinpagprim = new Timestamp(new GregorianCalendar(2020, 11, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecfinpagprim(fecfinpagprim);
		//fechas.setFecefecred(fecefecred);

		primas.setIprimanetaini(BigDecimal.valueOf(6896.51));
		primas.setPrevprima(BigDecimal.ZERO);
		primas.setCformarevprim("V");
		primas.setCformpago("1");
		primas.setPrecargfrac(BigDecimal.ZERO);
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		datosGenerales.setCtipoaport("P");
		datosGenerales.setFecCierre(FCIERRE);

		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente;
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		Timestamp fecDesde = FDESDE;
		Timestamp fecDevengo = FDEVENGO;
		
		for (int i=0;i<85;i++){
			detalleCorriente = new DetalleCorriente();
			detalleCorriente.setFechaDesde(fecDesde);
			detalleCorriente.setFcierre(FCIERRE);
			BloqueCorriente bloque = new BloqueCorriente();
			bloque = getBloqueCorriente();
			bloque.setFechaDevengo(fecDevengo);
			detalleCorriente.setBloqueVida(bloque);
			proyUmic.add(detalleCorriente);
			
			fecDesde = UtilFechas.incrMeses(fecDesde, FCALC, 1, false);
			fecDevengo = UtilFechas.incrMeses(fecDevengo, FDEVENGO, 1, false);
		}
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaDevengo(FDEVENGO);
		bloqueCorriente.setFechaPago(FPAGO);
		
		return bloqueCorriente;
	}
	
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();

		btcUmic.setBaseTec("BTI");
		btcUmic.setFecCierre(FCIERRE);
		
		
		return btcUmic;
	}
	
	
}