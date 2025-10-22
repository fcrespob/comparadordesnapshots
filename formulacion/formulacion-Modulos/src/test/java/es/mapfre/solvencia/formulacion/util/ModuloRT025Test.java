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
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ValoresConstantesRescatesDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.maestro.*;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo RT025, módulo de cálculo de la cuantía por Rescate de las garantías dentro de 
 * Solvencia II para las modalidades de MILLÓN VIDA.
 * @author apedro
 *
 */
public class ModuloRT025Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT025Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 376;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 5029769L;
	
	@BeforeClass
	public static void init() {
		ModuloRT025Test.LOG.debug("Inicializando pruebas");
		
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
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			
			DetalleCorrienteDao detalleCorrienteDao = new DetalleCorrienteDao();
			DetalleCorriente dc = new DetalleCorriente();
			TotalFlujoProyeccion tfp = new TotalFlujoProyeccion();
			tfp.setProvbtiproy(BigDecimal.valueOf(8081.83));
			
			dc.setFcierre(FCIERRE);
			dc.setKmodalidad(modalidad);
			dc.setKpoliza(poliza);
			dc.setKsubpoliza(0);
			dc.setKcertificado(0);
			dc.setNsuscri(1);
			dc.setNorden(10);
			dc.setKgarantia(1);
			dc.setKprestacion("0");
			dc.setKajuste(0);
			dc.setCtipoaport("U");
			dc.setBt("BTI");
			dc.setTotalFlujoProyeccion(tfp);
			
			detalleCorrienteDao.put(dc.getKey(), dc);
			
			ValoresConstantesRescatesDao valoresConstantesRescatesDao = new ValoresConstantesRescatesDao();
			ValoresConstantesRescate vcr = new ValoresConstantesRescate();
			ValoresConstantesRescate vcr2 = new ValoresConstantesRescate();
			vcr.setKduracion(9999);
			vcr.setKk1("KC001");
			vcr.setPorckonst(BigDecimal.ONE);
			
			vcr2.setKduracion(9999);
			vcr2.setKk1("KC002");
			vcr2.setPorckonst(BigDecimal.ONE);
			
			valoresConstantesRescatesDao.put(vcr.getKey(), vcr);
			valoresConstantesRescatesDao.put(vcr2.getKey(), vcr2);
			
		}catch (Exception e){
			ModuloRT025Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloRT025Test(){
		Modulo moduloRT025 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT025);
				
		try{
			BigDecimal rt025 = (BigDecimal) moduloRT025.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			TestCase.assertEquals(BigDecimal.valueOf(8425.315747), rt025.setScale(6, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloRT025Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Rescates rescates = new Rescates();
		Duraciones duraciones = new Duraciones();
		Capitales capitales = new Capitales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2016, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(12380.02));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setKsubpoliza(0);
		datosGenerales.setKcertificado(0);
		datosGenerales.setNsuscri(1);
		datosGenerales.setNorden(10);
		datosGenerales.setKprestacion("0");
		datosGenerales.setKajuste(0);
		datosGenerales.setCtipoaport("U");
		bti.setPintertecnI1(BigDecimal.valueOf(3));
		rescates.setKrescate1("KC001");
		rescates.setKrescate2("KC002");
		rescates.setTirIni(BigDecimal.valueOf(3.674177));
		rescates.setTirCie(BigDecimal.valueOf(1.376936));
		duraciones.setNdurprima(1);
		duraciones.setNdursegmes(0);
		capitales.setIcapact(BigDecimal.valueOf(13979.75));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setRescates(rescates);
		umic.setDuraciones(duraciones);
		umic.setCapitales(capitales);

		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
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
	
	
}