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

import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import junit.framework.TestCase;

/**
 * Clase que testea el Módulo GCZTAR 
 * 
 * @author 
 *
 */

public class ModuloGZCTARTest {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZCTARTest.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, 04, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2017, Calendar.DECEMBER, 02, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2018, Calendar.JANUARY, 02, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2017, 12, 02, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2017, 03,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 487;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 3477583;
	private static final int canal = 1;
	private static final String spcom = "P";
	private static final BigDecimal pgastgesex1i = BigDecimal.valueOf(1.05);
	private static final BigDecimal pgastgesin3i = BigDecimal.ZERO;
	private static final BigDecimal pgastgesin4i = BigDecimal.ZERO;

	@BeforeClass
	public static void init() {
		ModuloGZCTARTest.LOG.debug("Inicializando pruebas");
	
	}
	
	@Test
	public void moduloGZCTARTest(){
		Modulo moduloGZCTAR = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZCTAR);
	
		try{
			BigDecimal gzctar= (BigDecimal) moduloGZCTAR.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(gzctar);
			TestCase.assertEquals(BigDecimal.valueOf(5.363795829), gzctar.setScale(9, RoundingMode.HALF_DOWN));
	
		}catch (Exception e){
			ModuloGZCTARTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		Duraciones duraciones = new Duraciones();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		
		Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2016, Calendar.SEPTEMBER, 2, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fechastarenova = new Timestamp(new GregorianCalendar(2017, Calendar.SEPTEMBER, 2, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecdesderenova(fecdesderenova);
		fechas.setFechastarenova(fechastarenova);
		umic.setFechas(fechas);
		
		// Se pone un 2 aunque el campo nrenovaciones sea 0. Es debido a que se corresponde a varZ
		duraciones.setNrenovaciones(ConstantsFunciones.CTE_2);
		umic.setDuraciones(duraciones);
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);	
		datosGenerales.setSpcom(spcom);
		umic.setDatosGenerales(datosGenerales);
		
		bti.setPgastgesex1I(pgastgesex1i);
		bti.setPgastgesin3I(pgastgesin3i);
		bti.setPgastgesin4I(pgastgesin4i);
		
		umic.setBti(bti);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();		
		
		detalleCorriente.setFechaDesde(FDESDE);
		detalleCorriente.setFechaHasta(FHASTA);
		
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		//0
		bloqueCorriente.setImpFlujoNominal(BigDecimal.valueOf(394.7775));
		detalleCorriente.setBloquePrim(bloqueCorriente);
		//5055,22
		bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setImpFlujoNominal(BigDecimal.valueOf(5070.3857));
		detalleCorriente.setBloqueFall(bloqueCorriente);
		//0
		bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setImpFlujoNominal(BigDecimal.ZERO);
		detalleCorriente.setBloqueCompl(bloqueCorriente);
		
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
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(2.25));
		btcUmic.setGtorosspCap(BigDecimal.valueOf(0.75));
		return btcUmic;
	}
}

