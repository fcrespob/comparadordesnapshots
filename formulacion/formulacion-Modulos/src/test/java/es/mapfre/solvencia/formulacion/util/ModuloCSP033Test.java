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

/**
 * Clase que testea el Módulo CSP033, módulo de cálculo de la cuantía de fallecimiento de la garantía principal de la 
 * póliza con estado en vigor.
 * @author apedro
 *
 */

public class ModuloCSP033Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP033Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2015, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2015, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2015, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	public static final int cartera = 1101;
	public static final int modalidad = 196;
	public static final int garantia = 1;
	public static final String negocio = "I";
	
	@BeforeClass
	public static void init() {
		ModuloCSP033Test.LOG.debug("Inicializando pruebas");
		
		try{
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
			DefinicionesAuxiliares da = new DefinicionesAuxiliares();
			da.setCnegocio(negocio);
			da.setKcarteorig(cartera);
			da.setKmodalidad(modalidad);
			da.setKgarantia(garantia);
			da.setCidentivariab("IFAL");
			da.setGvariable("IFAL");
			da.setGvalor("1");
			
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setCidentivariab("PAS");
			da2.setGvariable("PAS");
			da2.setGvalor("1");
			
			DefinicionesAuxiliares da3 = new DefinicionesAuxiliares();
			da3.setCnegocio(negocio);
			da3.setKcarteorig(cartera);
			da3.setKmodalidad(modalidad);
			da3.setKgarantia(garantia);
			da3.setCidentivariab("FUT");
			da3.setGvariable("FUT");
			da3.setGvalor("0");
			
			DefinicionesAuxiliares da4 = new DefinicionesAuxiliares();
			da4.setCnegocio(negocio);
			da4.setKcarteorig(cartera);
			da4.setKmodalidad(modalidad);
			da4.setKgarantia(garantia);
			da4.setGvariable("ID-TEMPORAL");
			da4.setCidentivariab("ID-TEMPORAL");
			da4.setGvalor("01");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			definicionesAuxiliaresDao.put(da3.getKey(), da3);
			definicionesAuxiliaresDao.put(da4.getKey(), da4);
			
		}catch (Exception e){
			ModuloCSP033Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloCSP033Test(){
		Modulo moduloCSP033 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP033);
				
		try{
			BigDecimal csp033 = (BigDecimal) moduloCSP033.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(csp033);
			TestCase.assertEquals(BigDecimal.valueOf(41258.77762), csp033.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP033Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		Duraciones duraciones = new Duraciones();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2023, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(1990, 01, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(360.6));
		primas.setPrevprima(BigDecimal.valueOf(10));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		duraciones.setNdursegano(30);
		bti.setPintertecnI1(BigDecimal.valueOf(6));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setDuraciones(duraciones);
		umic.setBti(bti);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		proyUmic.add(detalleCorriente);
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
		
		return btcUmic;
	}
	
	
}