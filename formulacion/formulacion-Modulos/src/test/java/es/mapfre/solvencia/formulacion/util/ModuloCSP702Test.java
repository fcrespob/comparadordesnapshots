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

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TerminosPMCUmicDao;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo CSP702 
 * 
 * @author ogperez
 *
 */
public class ModuloCSP702Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP702Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2015, 01, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 2;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 376;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 5029769L;
	private static final String ctipoaport = "U";
	
	@BeforeClass
	public static void init() {
		ModuloCSP702Test.LOG.debug("Inicializando pruebas");
		
		daos();
	}
	
	@Test
	public void moduloCSP702Test(){
		Modulo moduloCSP702 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP702);
				
		try{
			BigDecimal csp702 = (BigDecimal) moduloCSP702.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			
			System.out.println(csp702);
			
			TestCase.assertEquals(BigDecimal.valueOf(13265.6263), csp702.setScale(4, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloCSP702Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Capitales capitales = new Capitales();
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCtipoaport(ctipoaport);
		datosGenerales.setCsitupol("VI");
		capitales.setPcapriesgo(BigDecimal.valueOf(10));
		capitales.setCrmax(BigDecimal.valueOf(20));
		
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setCapitales(capitales);
		
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
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(3.15));
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);		
		
		return btcUmic;
	}
	
	private static void daos(){
		try{						
			TerminosPMCUmicDao terminosPMCUmicDao = new TerminosPMCUmicDao();
			TerminosPMCUmic dcu = new TerminosPMCUmic();
			
			UmicKey claveUmic = new UmicKey();
			claveUmic.setCtipoaport(ctipoaport);
			claveUmic.setKgarantia(garantia);
			claveUmic.setKmodalidad(modalidad);
			claveUmic.setKpoliza(poliza);
			
			dcu.setClaveUmic(claveUmic);
			dcu.setBt("BTI");
			dcu.setIteracion(iteracion-1);
			dcu.setGast(BigDecimal.valueOf(3));
			
			terminosPMCUmicDao.put(dcu.getKey(), dcu);
			
		}catch (Exception e){
			ModuloCSP702Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}
}