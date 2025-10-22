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

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.IPCGeneralFuturoDao;
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.modulos.impl.ModuloCSP011;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;
import junit.framework.TestCase;

/**
 * Clase Test del ModuloCSP011. Módulo para aquellas modalidades cuyo Capital 
 * revaloriza geométricamente al IPC, y siempre que tengamos que 
 * provocar la renovación tácita como consecuencia de la 
 * aplicación del CRITERIO TAR
 *
 */
public class ModuloCSP011Test {

	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP011Test.class);
	//Fecha de cierre + 1 día
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, 04, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2018, 03, 02, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2018, 03, 17, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2017, 03, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	public static final int cartera = 1101;
	public static final int modalidad = 487;
	public static final int garantia = 1;
	public static final String negocio = "I";
	public static final int canal = 1;
	
	@BeforeClass
	public static void init() {
		ModuloCSP011Test.LOG.debug("Inicializando pruebas");
		
		Timestamp finicio = new Timestamp(new GregorianCalendar(2014, 01, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp ffin = new Timestamp(new GregorianCalendar(2019, 01, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		IPCGeneralFuturo ipc = new IPCGeneralFuturo();
		ipc.setFfin(ffin);
		ipc.setFinicio(finicio);
		ipc.setPipcgas(BigDecimal.valueOf(0.3));
		ipc.setPipcleg(BigDecimal.valueOf(0.3));
		
		dao.put(ipc.getKey(), ipc);
				
				
	}
	
	@Test
	public void moduloCSP011Test(){
		Modulo moduloCSP011 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP011);

		try{
			BigDecimal csp011 = (BigDecimal) moduloCSP011.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(csp011);
			TestCase.assertEquals(BigDecimal.valueOf(5070.3857).setScale(4, RoundingMode.HALF_DOWN), csp011.setScale(4, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP011Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		Duraciones duraciones = new Duraciones();
		Capitales capitales = new Capitales();
		
		Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2016, 9, 2, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fechastarenova = new Timestamp(new GregorianCalendar(2017, 9, 2, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecdesderenova(fecdesderenova);
		fechas.setFechastarenova(fechastarenova);
		umic.setFechas(fechas);
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		umic.setDatosGenerales(datosGenerales);
		
		duraciones.setNrenovaciones(ConstantsFunciones.CTE_0);
		umic.setDuraciones(duraciones);
		
		capitales.setIcapact(BigDecimal.valueOf(5055.22));
		umic.setCapitales(capitales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
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
		btcUmic.setBaseTec("BTI");
		
		return btcUmic;
	}
}
