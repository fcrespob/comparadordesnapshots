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
 * Clase que testea el Módulo CSP308, módulo de cálculo que devuelve el capital de fallecimiento para productos Flexibles a Prima única.
 * @author apedro
 *
 */

public class ModuloCSP308Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP308Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2015, 01, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 308;
	public static final int garantia = 308;
	public static final String negocio = "C";
	public static final int canal = 1;
	
	
	
	@BeforeClass
	public static void init() {
		ModuloCSP308Test.LOG.debug("Inicializando pruebas");
		
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
			
			DefinicionesAuxiliares da3 = new DefinicionesAuxiliares();
			da3.setCnegocio(negocio);
			da3.setKcarteorig(cartera);
			da3.setKmodalidad(modalidad);
			da3.setKgarantia(garantia);
			da3.setCidentivariab("MODBETA");
			da3.setGvariable("MODBETA");
			da3.setGvalor("CRPI");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			definicionesAuxiliaresDao.put(da3.getKey(), da3);

		}catch (Exception e){
			ModuloCSP308Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloCSP308Test(){
		Modulo moduloCSP308 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP308);
				
		try{
			BigDecimal csp308 = (BigDecimal) moduloCSP308.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(csp308);

			TestCase.assertEquals(BigDecimal.valueOf(18782.50186), csp308.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP308Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Duraciones duraciones = new Duraciones();
		//Rentas rentas = new Rentas();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2021, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(1992, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		//Timestamp fecinirent = new Timestamp(new GregorianCalendar(2023, 6, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		//Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecFinTramo1 = new Timestamp(new GregorianCalendar(2021, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2013, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fechastarenova = new Timestamp(new GregorianCalendar(2014, 12, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecini(fecinisus);
		fechas.setFecdesderenova(fecdesderenova);
		fechas.setFechastarenova(fechastarenova);
		//fechas.setFecefecred(fecefecred);
		primas.setIprimanetaini(BigDecimal.valueOf(5212.24)); 
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		datosGenerales.setCtipoaport("U");
		datosGenerales.setFecCierre(FCIERRE);

		bti.setFecFinTramo1(fecFinTramo1);
		duraciones.setNrenovaciones(21); 

		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setDuraciones(duraciones);
		
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
		
		return bloqueCorriente;
	}
	
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(6));
		itcalc.add(BigDecimal.valueOf(0));
		
//		TablaConversion tc = new TablaConversion();
//		tc.setTablaInicio("740");
//		tc.setTablaFin("740");
//		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
//		tabCon.add(tc);
//		tabCon.add(tc);
//		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
//		tca.add(tabCon);
//		tca.add(tabCon);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);
		
//		btcUmic.setIndTabExp('T');
//		btcUmic.setTablacalc1aseg1("740");
//		btcUmic.setTablacalc1aseg2("740");
//		btcUmic.setTablasConversionAsegurado(tca);
		
		
		return btcUmic;
	}
	
	
}