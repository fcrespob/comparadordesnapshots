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

import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo GCZ012 
 * 
 * @author ogperez
 *
 */
public class ModuloGZC012Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC012Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2015, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2016, 05, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2016, 06, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2015, 11,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1201;
	private static final int modalidad = 331;
	private static final int garantia = 200;
	private static final String negocio = "C";
	private static final long poliza = 421043L;
	
	@BeforeClass
	public static void init() {
		ModuloGZC012Test.LOG.debug("Inicializando pruebas");
		
	}
	
	@Test
	public void moduloGZC012Test(){
		Modulo moduloGZC012 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC012);
				
		try{
			BigDecimal gzc012= (BigDecimal) moduloGZC012.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(gzc012);
			TestCase.assertEquals(BigDecimal.valueOf(0.04), gzc012.setScale(2, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloGZC012Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		DatosGenerales datosGenerales = new DatosGenerales();
				
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);	
		umic.setDatosGenerales(datosGenerales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		DetalleCorriente detalleCorriente2 = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();		
		
		detalleCorriente.setFechaDesde(FDESDE);
		detalleCorriente.setFechaHasta(FHASTA);
		detalleCorriente.setBloquePrim(getBloqueCorriente());
		
		Timestamp fecDesde2= new Timestamp(new GregorianCalendar(2025, 8, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		detalleCorriente2.setFechaDesde(fecDesde2);
		
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente2);
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaDevengo(FDEVENGO);	
		bloqueCorriente.setImpFlujoNominal(BigDecimal.valueOf(1.32));
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();	
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(3));
		return btcUmic;
	}
}