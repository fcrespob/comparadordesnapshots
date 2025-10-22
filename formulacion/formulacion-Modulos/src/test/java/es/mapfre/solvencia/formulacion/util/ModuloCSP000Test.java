package es.mapfre.solvencia.formulacion.util;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import junit.framework.TestCase;

public class ModuloCSP000Test {

private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP000Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	public static final int cartera = 1201;
	public static final int modalidad = 308;
	public static final int garantia = 308;
	public static final String negocio = "C";
	
	@BeforeClass
	public static void init() {
		ModuloCSP000Test.LOG.debug("Inicializando pruebas");
		
	}
	
	@Test
	public void moduloCSP000Test(){
		Modulo moduloCSP000 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP000);
				
		try{
			BigDecimal csp000 = (BigDecimal) moduloCSP000.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(csp000);
			TestCase.assertEquals(BigDecimal.valueOf(0), csp000);
		}catch (Exception e){
			ModuloCSP000Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		
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
		
		return bloqueCorriente;
	}
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		
		return btcUmic;
	}
}
