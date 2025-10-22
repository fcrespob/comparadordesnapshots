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
import es.mapfre.solvencia.dominio.maestro.Capitales;
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

/**
 * Clase que testea el Módulo CSP002, módulo de cálculo para la cuantía del suceso de VIDA de la garantía principal. 
 * Devuelve un capital revalorizable aritméticamente.
 * @author apedro
 *
 */

public class ModuloCSP002Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP002Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 12,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2021, 12,01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	public static final int cartera = 1201;
	public static final int modalidad = 308;
	public static final int garantia = 308;
	public static final String negocio = "C";
	
	@BeforeClass
	public static void init() {
		ModuloCSP002Test.LOG.debug("Inicializando pruebas");
		
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
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
		}catch (Exception e){
			ModuloCSP002Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloCSP002Test(){
		Modulo moduloCSP002 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP002);
				
		try{
			BigDecimal csp002 = (BigDecimal) moduloCSP002.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(csp002);
			TestCase.assertEquals(BigDecimal.valueOf(54680.9274), csp002.setScale(4, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP002Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		Duraciones duraciones = new Duraciones();
		Capitales capitales = new Capitales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2021, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		
		fechas.setFecefecfin(fecefecfin);
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		duraciones.setNdursegano(29);
		capitales.setIcapini(BigDecimal.valueOf(27616.63));
		capitales.setPorevalcap(BigDecimal.valueOf(3.5));
		
		umic.setFechas(fechas);
		umic.setDatosGenerales(datosGenerales);
		umic.setDuraciones(duraciones);
		umic.setCapitales(capitales);
		
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
		bloqueCorriente.setFechaPago(FPAGO);
		
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		btcUmic.setBaseTec("BTI");
		
		return btcUmic;
	}
	
	
}