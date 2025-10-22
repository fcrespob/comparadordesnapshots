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
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Primas;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo GZC017, módulo de cálculo que devuelve ela prima en el periodo que le corresponda pagar al 
 * asegurado.
 * @author apedro
 *
 */

public class ModuloGZC017Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloGZC017Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2014, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2013, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
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
		ModuloGZC017Test.LOG.debug("Inicializando pruebas");
		
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
			ModuloGZC017Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloGZC017Test(){
		Modulo moduloGZC017 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_GZC017);
				
		try{
			BigDecimal GZC017 = (BigDecimal) moduloGZC017.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(GZC017);

			TestCase.assertEquals(BigDecimal.valueOf(412.49034), GZC017.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloGZC017Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		//Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Rentas rentas = new Rentas();
		//Duraciones duraciones = new Duraciones();
		Asegurados aseg = new Asegurados();
		//
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2023, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecFnacAseg1 = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecderenova = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fechastarenova = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecfinpagprim = new Timestamp(new GregorianCalendar(1993, 11, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		//
		fechas.setFecefecred(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecdesderenova(fecderenova);
		fechas.setFechastarenova(fechastarenova);
		fechas.setFecefecred(fecefecred);
		fechas.setFecfinpagprim(fecfinpagprim);
        //
		//primas.setIprimanetaini(BigDecimal.valueOf(216.36));
		//primas.setPrevprima(BigDecimal.valueOf(5));
		//primas.setCformarevprim("V");
		//primas.setCformpago("1");
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setSpcom("");
		datosGenerales.setEdifer(0);
		datosGenerales.setCsitupol("");
		datosGenerales.setCnegocio("");
		//datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		//datosGenerales.setCtipoaport("U");
		datosGenerales.setFecCierre(FCIERRE);
        //
		bti.setPgastgesex1I(BigDecimal.valueOf(7));
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPgastgesin3I(BigDecimal.ZERO);
		bti.setPgastgesin4I(BigDecimal.ZERO);
		//duraciones.setNrenovaciones(21); 
        //
		aseg.setFnacAseg1(fecFnacAseg1);
		aseg.setCsexAseg1("");
		aseg.setFnacAseg2(fecFnacAseg1);
		aseg.setCsexAseg2("");
		aseg.setFnacAseg3(fecFnacAseg1);
		aseg.setCsexAseg3("");
		aseg.setFnacAseg4(fecFnacAseg1);
		aseg.setCsexAseg4("");
		aseg.setFnacAseg5(fecFnacAseg1);
		aseg.setCsexAseg5("");
		//
		rentas.setFecFin(fecFnacAseg1);
		//
		//umic.setFechas(fechas);
		//umic.setPrimas(primas);
		//umic.setDatosGenerales(datosGenerales);
		//umic.setBti(bti);
		//umic.setDuraciones(duraciones);
		
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
			BloqueCorriente bloqueVida = new BloqueCorriente();
			BloqueCorriente bloqueFall = new BloqueCorriente();
			bloqueVida = getBloqueCorriente();
			bloqueFall = getBloqueCorriente();
			bloqueVida.setImpFlujoNominal(BigDecimal.ZERO);
			bloqueFall.setImpFlujoNominal(BigDecimal.ZERO);
			detalleCorriente.setBloqueVida(bloqueVida);
			detalleCorriente.setBloqueFall(bloqueFall);
			proyUmic.add(detalleCorriente);
			
			fecDesde = UtilFechas.incrMeses(fecDesde, FCALC, 1, false);
			fecDevengo = UtilFechas.incrMeses(fecDevengo, FDEVENGO, 1, false);
		}
		
		return proyUmic;
	}
	
	private BloqueCorriente getBloqueCorriente(){
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		bloqueCorriente.setFechaPago(FPAGO);		
		return bloqueCorriente;
	}
	
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(0.06));
		itcalc.add(BigDecimal.valueOf(0));
		
		btcUmic.setFecCierre(FCIERRE);
		
		
		
		btcUmic.setGtorosspPrima(BigDecimal.ZERO);
		btcUmic.setGtorosspCap(BigDecimal.ZERO);
		btcUmic.setItcalc(itcalc);
		btcUmic.setTablacalc1aseg1("");
			
		
		return btcUmic;
	}
	
	
}