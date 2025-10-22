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

import es.mapfre.solvencia.dao.impl.maestro.ValoresLiquidativosDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.IPCGeneralFuturoDao;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Primas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo CSP84324, módulo de cálculo que devuelve el Valor del Fondo en cada momento de la 
 * PTRI (provisión técnica dónde tomador asume riego inversión) en cada momento. 
 * @author apedro
 *
 */

public class ModuloCSP84324Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP84324Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	public static final int cartera = 1101;
	public static final Integer modalidad = 858;
	public static final int garantia = 1;
	public static final String ramo = "146";
	public static final String negocio = "I";
	public static final Long poliza = 8063690L;
	
	@BeforeClass
	public static void init() {
		ModuloCSP84324Test.LOG.debug("Inicializando pruebas");
		
		try{
			
			IPCGeneralFuturoDao ipcGeneralFuturoDao = new IPCGeneralFuturoDao();
			IPCGeneralFuturo ipcgf = new IPCGeneralFuturo();
			
			Timestamp ffin = new Timestamp(new GregorianCalendar(2015, 03,30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			Timestamp finicio = new Timestamp(new GregorianCalendar(2010, 02,24, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
			
			ipcgf.setFfin(ffin);
			ipcgf.setPipcgas(BigDecimal.valueOf(0.3));
			ipcgf.setFinicio(finicio);
			
			ipcGeneralFuturoDao.put(ipcgf.getKey(), ipcgf);
			
			ValoresLiquidativosDao valoresLiquidativosDao = new ValoresLiquidativosDao();
			ValoresLiquidativos vl = new ValoresLiquidativos();
			
			vl.setKmodalidad(modalidad.toString());
			vl.setKpoliza(poliza);
			vl.setKramo(ramo);
			vl.setCnparticipacion(BigDecimal.valueOf(120.86023));
			vl.setCinptotfondo(BigDecimal.valueOf(11416.64));
			vl.setAnparticipacion(BigDecimal.ZERO);
			vl.setAinptotfondo(BigDecimal.ZERO);
			vl.setPkvalor(BigDecimal.valueOf(97.49748));
			vl.setKfondo("FLE2");
			
			valoresLiquidativosDao.put(vl.getKey(), vl);
			
		}catch (Exception e){
			ModuloCSP84324Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloCSP84324Test(){
		Modulo moduloCSP84324 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP84324);
				
		try{
			BigDecimal csp84324 = (BigDecimal) moduloCSP84324.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(csp84324);
			TestCase.assertEquals(BigDecimal.valueOf(11783.56786), csp84324.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP84324Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2015, 1, 26, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 1, 25, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(12380.02));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setKramo(ramo);
		datosGenerales.setKpoliza(poliza);
		bti.setPintertecnI1(BigDecimal.valueOf(3));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
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