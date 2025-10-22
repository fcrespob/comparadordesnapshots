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

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.CabeceraTablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ValoresConstantesRescatesDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.maestro.*;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.BloqueCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalFlujoProyeccion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo VBX001, Módulo que calcula el importe nominal para la proyección de Provisión Matemática 
 * por Fórmula Cerrada.
 * @author apedro
 *
 */
public class ModuloRT009Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloRT009Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2014, 03, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2014, 03,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "01";
	
	private static final int cartera = 1101;
	private static final int modalidad = 141;
	private static final int garantia = 1;
	private static final String negocio = "I";
	private static final long poliza = 326641L;
	private static final String prestacion = "0";
	private static final String terminal = "VTX003";
	
	/**
	 * Lista con valores de mortalidad para los valores:
	 * BASE_TECNICA: BTI
	 * TABLA: 37
	 * W-K2TIPOVALOR: 2
	 * FECHA_NACIMIENTO: 
	 * INTERES: 0
	 * SOBREMORT: 0
	 * PRIESGO:
	 * MODALIDAD: 
	 */
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("999050"));
		TABLA_MORTALIDAD.add(new BigDecimal("998100.9025"));
		TABLA_MORTALIDAD.add(new BigDecimal("997152.7066"));
		TABLA_MORTALIDAD.add(new BigDecimal("996205.4116"));
		TABLA_MORTALIDAD.add(new BigDecimal("995259.0164"));
		TABLA_MORTALIDAD.add(new BigDecimal("994313.5204"));
		TABLA_MORTALIDAD.add(new BigDecimal("993368.9225"));
		TABLA_MORTALIDAD.add(new BigDecimal("992425.222"));
		TABLA_MORTALIDAD.add(new BigDecimal("991482.4181"));
		TABLA_MORTALIDAD.add(new BigDecimal("990540.5098"));
		TABLA_MORTALIDAD.add(new BigDecimal("989599.4963"));
		TABLA_MORTALIDAD.add(new BigDecimal("988659.3768"));
		TABLA_MORTALIDAD.add(new BigDecimal("987720.1504"));
		TABLA_MORTALIDAD.add(new BigDecimal("986781.8162"));
		TABLA_MORTALIDAD.add(new BigDecimal("985844.3735"));
		TABLA_MORTALIDAD.add(new BigDecimal("984907.8213"));
		TABLA_MORTALIDAD.add(new BigDecimal("983968.2193"));
		TABLA_MORTALIDAD.add(new BigDecimal("983024.5938"));
		TABLA_MORTALIDAD.add(new BigDecimal("982075.975"));
		TABLA_MORTALIDAD.add(new BigDecimal("981120.4151"));
		TABLA_MORTALIDAD.add(new BigDecimal("980156.9549"));
		TABLA_MORTALIDAD.add(new BigDecimal("979182.6788"));
		TABLA_MORTALIDAD.add(new BigDecimal("978195.6627"));
		TABLA_MORTALIDAD.add(new BigDecimal("977193.0122"));
		TABLA_MORTALIDAD.add(new BigDecimal("976171.8455"));
		TABLA_MORTALIDAD.add(new BigDecimal("975128.3178"));
		TABLA_MORTALIDAD.add(new BigDecimal("974058.602"));
		TABLA_MORTALIDAD.add(new BigDecimal("972958.8898"));
		TABLA_MORTALIDAD.add(new BigDecimal("971823.4468"));
		TABLA_MORTALIDAD.add(new BigDecimal("970646.5686"));
		TABLA_MORTALIDAD.add(new BigDecimal("969422.5833"));
		TABLA_MORTALIDAD.add(new BigDecimal("968144.8843"));
		TABLA_MORTALIDAD.add(new BigDecimal("966805.9399"));
		TABLA_MORTALIDAD.add(new BigDecimal("965397.3037"));
		TABLA_MORTALIDAD.add(new BigDecimal("963910.5918"));
		TABLA_MORTALIDAD.add(new BigDecimal("962336.5258"));
		TABLA_MORTALIDAD.add(new BigDecimal("960663.985"));
		TABLA_MORTALIDAD.add(new BigDecimal("958881.9533"));
		TABLA_MORTALIDAD.add(new BigDecimal("956977.6137"));
		TABLA_MORTALIDAD.add(new BigDecimal("954938.2944"));
		TABLA_MORTALIDAD.add(new BigDecimal("952749.5758"));
		TABLA_MORTALIDAD.add(new BigDecimal("950397.2371"));
		TABLA_MORTALIDAD.add(new BigDecimal("947864.4285"));
		TABLA_MORTALIDAD.add(new BigDecimal("945133.6311"));
		TABLA_MORTALIDAD.add(new BigDecimal("942187.6496"));
		TABLA_MORTALIDAD.add(new BigDecimal("939006.8241"));
		TABLA_MORTALIDAD.add(new BigDecimal("935570.9981"));
		TABLA_MORTALIDAD.add(new BigDecimal("931859.5879"));
		TABLA_MORTALIDAD.add(new BigDecimal("927849.7961"));
		TABLA_MORTALIDAD.add(new BigDecimal("923517.6654"));
		TABLA_MORTALIDAD.add(new BigDecimal("918835.4309"));
		TABLA_MORTALIDAD.add(new BigDecimal("913836.0473"));
		TABLA_MORTALIDAD.add(new BigDecimal("908485.5372"));
		TABLA_MORTALIDAD.add(new BigDecimal("902746.6341"));
		TABLA_MORTALIDAD.add(new BigDecimal("896578.1663"));
		TABLA_MORTALIDAD.add(new BigDecimal("889935.4187"));
		TABLA_MORTALIDAD.add(new BigDecimal("882768.7688"));
		TABLA_MORTALIDAD.add(new BigDecimal("875025.1211"));
		TABLA_MORTALIDAD.add(new BigDecimal("866647.6306"));
		TABLA_MORTALIDAD.add(new BigDecimal("857573.8299"));
		TABLA_MORTALIDAD.add(new BigDecimal("847737.4581"));
		TABLA_MORTALIDAD.add(new BigDecimal("837067.8345"));
		TABLA_MORTALIDAD.add(new BigDecimal("825490.3492"));
		TABLA_MORTALIDAD.add(new BigDecimal("812926.3861"));
		TABLA_MORTALIDAD.add(new BigDecimal("799293.6106"));
		TABLA_MORTALIDAD.add(new BigDecimal("784508.2774"));
		TABLA_MORTALIDAD.add(new BigDecimal("768485.4804"));
		TABLA_MORTALIDAD.add(new BigDecimal("751139.2261"));
		TABLA_MORTALIDAD.add(new BigDecimal("732387.0353"));
		TABLA_MORTALIDAD.add(new BigDecimal("712150.4492"));
		TABLA_MORTALIDAD.add(new BigDecimal("690359.3576"));
		TABLA_MORTALIDAD.add(new BigDecimal("666954.1043"));
		TABLA_MORTALIDAD.add(new BigDecimal("641890.636"));
		TABLA_MORTALIDAD.add(new BigDecimal("615144.9788"));
		TABLA_MORTALIDAD.add(new BigDecimal("586719.1294"));
		TABLA_MORTALIDAD.add(new BigDecimal("556646.8404"));
		TABLA_MORTALIDAD.add(new BigDecimal("524998.1276"));
		TABLA_MORTALIDAD.add(new BigDecimal("491888.0707"));
		TABLA_MORTALIDAD.add(new BigDecimal("457480.5002"));
		TABLA_MORTALIDAD.add(new BigDecimal("421994.1953"));
		TABLA_MORTALIDAD.add(new BigDecimal("385704.8044"));
		TABLA_MORTALIDAD.add(new BigDecimal("348946.3652"));
		TABLA_MORTALIDAD.add(new BigDecimal("312108.0974"));
		TABLA_MORTALIDAD.add(new BigDecimal("275628.5909"));
		TABLA_MORTALIDAD.add(new BigDecimal("239983.7502"));
		TABLA_MORTALIDAD.add(new BigDecimal("205670.1537"));
		TABLA_MORTALIDAD.add(new BigDecimal("173183.7302"));
		TABLA_MORTALIDAD.add(new BigDecimal("142993.1301"));
		TABLA_MORTALIDAD.add(new BigDecimal("115510.7084"));
		TABLA_MORTALIDAD.add(new BigDecimal("91063.67557"));
		TABLA_MORTALIDAD.add(new BigDecimal("69868.0587"));
		TABLA_MORTALIDAD.add(new BigDecimal("52009.08422"));
		TABLA_MORTALIDAD.add(new BigDecimal("37432.44617"));
		TABLA_MORTALIDAD.add(new BigDecimal("25948.50858"));
		TABLA_MORTALIDAD.add(new BigDecimal("17250.41281"));
		TABLA_MORTALIDAD.add(new BigDecimal("10944.85217"));
		TABLA_MORTALIDAD.add(new BigDecimal("6591.405879"));
		TABLA_MORTALIDAD.add(new BigDecimal("3744.808379"));
		TABLA_MORTALIDAD.add(new BigDecimal("1993.058171"));
		TABLA_MORTALIDAD.add(new BigDecimal("985.7306962"));
		TABLA_MORTALIDAD.add(new BigDecimal("448.8475439"));
		TABLA_MORTALIDAD.add(new BigDecimal("186.1191226"));
		TABLA_MORTALIDAD.add(new BigDecimal("69.36845817"));
		TABLA_MORTALIDAD.add(new BigDecimal("22.87105813"));
		TABLA_MORTALIDAD.add(new BigDecimal("6.53830949"));
		TABLA_MORTALIDAD.add(new BigDecimal("1.57887751"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.31069783"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
	}
	
	@BeforeClass
	public static void init() {
		ModuloRT009Test.LOG.debug("Inicializando pruebas");
		
		daos();
	}
	
	@Test
	public void moduloRT009Test(){
		Modulo moduloRT009 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_RT009);
				
		try{
			BigDecimal rt009= (BigDecimal) moduloRT009.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(rt009);
			TestCase.assertEquals(BigDecimal.valueOf(3502.501705), rt009.setScale(6, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloRT009Test.LOG.debug(e.getMessage(), e);
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
		Asegurados asegurados = new Asegurados();
		Rentas rentas = new Rentas();
		DatosAdicionales datosAdicionales = new DatosAdicionales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2025, 9, 28, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(1987, 9, 28, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1960, 4, 18, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(721.2));
		primas.setPrevprima(BigDecimal.ZERO);
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setFecCierre(FCIERRE);
		datosGenerales.setKpoliza(poliza);
		datosGenerales.setKsubpoliza(0);
		datosGenerales.setKcertificado(0);
		datosGenerales.setNsuscri(1);
		datosGenerales.setNorden(10);
		datosGenerales.setKprestacion(prestacion);
		datosGenerales.setKajuste(0);
		datosGenerales.setCtipoaport("U");
		datosGenerales.setCsitupol("VI");
		bti.setPintertecnI1(BigDecimal.valueOf(6));
		bti.setPgastgesex1I(BigDecimal.ZERO);
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		rescates.setKrescate1("KT000");
		rescates.setKrescate2("KC000");
		rescates.setTirIni(BigDecimal.valueOf(3.674177));
		rescates.setTirCie(BigDecimal.valueOf(1.376936));
		duraciones.setNdurprima(1);
		duraciones.setNdursegmes(0);
		duraciones.setNdursegano(38);
		capitales.setIcapact(BigDecimal.valueOf(6080.4));
		capitales.setIcapini(BigDecimal.valueOf(6080.4));
		capitales.setPorevalcap(BigDecimal.ZERO);
		capitales.setIcapfall(BigDecimal.ZERO);
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setCsexAseg1("H");
		rentas.setNadifer(0);
		datosAdicionales.setPrestCal(prestacion);
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setRescates(rescates);
		umic.setDuraciones(duraciones);
		umic.setCapitales(capitales);
		umic.setAsegurados(asegurados);
		umic.setRentas(rentas);
		umic.setDatosAdicionales(datosAdicionales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		DetalleCorriente detalleCorriente2 = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		detalleCorriente.setFechaHasta(FHASTA);
		
		Timestamp fecDesde2= new Timestamp(new GregorianCalendar(2025, 8, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		detalleCorriente2.setFechaDesde(fecDesde2);
		
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente2);
		
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
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("37");
		tc.setTablaFin("37");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		tca.add(tabCon);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);
		btcUmic.setIndTabExp('T');
		btcUmic.setTablacalc1aseg1("37");
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(2));
		btcUmic.setGtorosspCap(BigDecimal.ZERO);
		btcUmic.setTablasConversionAsegurado(tca);
		
		return btcUmic;
	}
	
	private static void daos(){
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
			da2.setCidentivariab("IFAL");
			da2.setGvariable("IFAL");
			da2.setGvalor("1");
			
			DefinicionesAuxiliares da3 = new DefinicionesAuxiliares();
			da3.setCnegocio(negocio);
			da3.setKcarteorig(cartera);
			da3.setKmodalidad(modalidad);
			da3.setKgarantia(garantia);
			da3.setCidentivariab("ID-CRITERIO");
			da3.setGvariable("ID-CRITERIO");
			da3.setGvalor("03");
			
			DefinicionesAuxiliares da4 = new DefinicionesAuxiliares();
			da4.setCnegocio(negocio);
			da4.setKcarteorig(cartera);
			da4.setKmodalidad(modalidad);
			da4.setKgarantia(garantia);
			da4.setCidentivariab("PAS");
			da4.setGvariable("PAS");
			da4.setGvalor("1");
			
			DefinicionesAuxiliares da5 = new DefinicionesAuxiliares();
			da5.setCnegocio(negocio);
			da5.setKcarteorig(cartera);
			da5.setKmodalidad(modalidad);
			da5.setKgarantia(garantia);
			da5.setCidentivariab("FUT");
			da5.setGvariable("FUT");
			da5.setGvalor("0");
			
			DefinicionesAuxiliares da6 = new DefinicionesAuxiliares();
			da6.setCnegocio(negocio);
			da6.setKcarteorig(cartera);
			da6.setKmodalidad(modalidad);
			da6.setKgarantia(garantia);
			da6.setCidentivariab("IANT");
			da6.setGvariable("IANT");
			da6.setGvalor("1");
			
			DefinicionesAuxiliares da7 = new DefinicionesAuxiliares();
			da7.setCnegocio(negocio);
			da7.setKcarteorig(cartera);
			da7.setKmodalidad(modalidad);
			da7.setKgarantia(garantia);
			da7.setCidentivariab("TIPO_ALFA");
			da7.setGvariable("TIPO_ALFA");
			da7.setGvalor("PRORRATA");
						
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			definicionesAuxiliaresDao.put(da3.getKey(), da3);
			definicionesAuxiliaresDao.put(da4.getKey(), da4);
			definicionesAuxiliaresDao.put(da5.getKey(), da5);
			definicionesAuxiliaresDao.put(da6.getKey(), da6);
			definicionesAuxiliaresDao.put(da7.getKey(), da7);
			
			
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
			
			TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1960");
			te.setKtabla(37);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			tablaExerienciaDao.put(te.getKey(), te);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(37);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
			
			ValoresConstantesRescatesDao valoresConstantesRescatesDao = new ValoresConstantesRescatesDao();
			ValoresConstantesRescate vcr = new ValoresConstantesRescate();
			ValoresConstantesRescate vcr2 = new ValoresConstantesRescate();
			ValoresConstantesRescate vcr3 = new ValoresConstantesRescate();
			vcr.setKduracion(9999);
			vcr.setKk1("KT000");
			vcr.setPorckonst(BigDecimal.ONE);
			
			vcr2.setKduracion(9999);
			vcr2.setKk1("KC000");
			vcr2.setPorckonst(BigDecimal.ZERO);
			
			vcr3.setKduracion(11);
			vcr3.setKk1("KT000");
			vcr3.setPorckonst(BigDecimal.ZERO);
			
			valoresConstantesRescatesDao.put(vcr.getKey(), vcr);
			valoresConstantesRescatesDao.put(vcr2.getKey(), vcr2);
			valoresConstantesRescatesDao.put(vcr3.getKey(), vcr3);
			
			FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
			FlujosProbables fp = new FlujosProbables();
			
			fp.setBasetecnica("BTI");
			fp.setModalidad(modalidad);
			fp.setGarantia(garantia);
			fp.setPrestacion(prestacion);
			fp.setProvTerminal(terminal);
			
			flujosProbablesDao.put(fp.getKey(), fp);
			
		}catch (Exception e){
			ModuloRT009Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}
}