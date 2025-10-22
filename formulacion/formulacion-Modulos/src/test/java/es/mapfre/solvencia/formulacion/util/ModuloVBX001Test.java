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
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.maestro.*;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
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
public class ModuloVBX001Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVBX001Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FHASTA = new Timestamp(new GregorianCalendar(2015, 01, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
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
	private static final String terminal = "VTX003";
	
	/**
	 * Lista con valores de mortalidad para los valores:
	 * BASE_TECNICA: BTI
	 * TABLA: 707
	 * W-K2TIPOVALOR: 2
	 * FECHA_NACIMIENTO: 
	 * INTERES: 302
	 * SOBREMORT: 0
	 * PRIESGO:
	 * MODALIDAD: 
	 */
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("100000000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99842200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99684500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99527200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99370100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99213200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("99056600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98900300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98744200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98588300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98432700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98277300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("98122200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97967300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97812600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97658200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97504100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97348600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97192700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("97037700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96884500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96734300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96588300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96446900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96309600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96175900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("96045300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95917400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95791400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95666700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95542600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95418400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95293400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95166700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("95037400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94904600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94767400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94624900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94475800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94319000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("94153400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93977400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93789600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93588400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93372300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("93139100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92886700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92612200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("92312800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91985300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91626000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("91231200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("90796900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("90318800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("89792600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("89213600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("88577600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("87880000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("87116500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("86282700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("85374700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("84388400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("83320300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("82167000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("80925600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("79593600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("78155300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("76589700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("74878600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("73006500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("70961500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("68735300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("66324000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("63728100000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("60952800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("58008300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("54909700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("51676900000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("48334300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("44910300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("41436600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("37947400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("34478500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("31066200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("27746000000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("24551500000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("21513600000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("18658800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("16009300000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("13581400000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("11385800000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("9427260000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("7704700000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("6211720000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("4937200000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("3866160000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("2980660000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("2260860000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("1685930000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("1235040000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("888051000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("626251000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("432736000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("292721000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("193646000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("125151000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("78931402537"));
		TABLA_MORTALIDAD.add(new BigDecimal("48521943097"));
		TABLA_MORTALIDAD.add(new BigDecimal("29037204756"));
		TABLA_MORTALIDAD.add(new BigDecimal("16893372421"));
		TABLA_MORTALIDAD.add(new BigDecimal("9541091245"));
		TABLA_MORTALIDAD.add(new BigDecimal("5223106295"));
		TABLA_MORTALIDAD.add(new BigDecimal("2766850200"));
		TABLA_MORTALIDAD.add(new BigDecimal("1415749658"));
		TABLA_MORTALIDAD.add(new BigDecimal("698365380"));
		TABLA_MORTALIDAD.add(new BigDecimal("331397908"));
		TABLA_MORTALIDAD.add(new BigDecimal("150930074"));
		TABLA_MORTALIDAD.add(new BigDecimal("65803339"));
		TABLA_MORTALIDAD.add(new BigDecimal("27386488"));
		TABLA_MORTALIDAD.add(new BigDecimal("10846101"));
		TABLA_MORTALIDAD.add(new BigDecimal("4073147"));
		TABLA_MORTALIDAD.add(new BigDecimal("1444713"));
		TABLA_MORTALIDAD.add(new BigDecimal("481804"));
		TABLA_MORTALIDAD.add(new BigDecimal("150297"));
		TABLA_MORTALIDAD.add(new BigDecimal("43594"));
		TABLA_MORTALIDAD.add(new BigDecimal("11675"));
		TABLA_MORTALIDAD.add(new BigDecimal("2863"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
		TABLA_MORTALIDAD.add(new BigDecimal("0"));
	}
	
	@BeforeClass
	public static void init() {
		ModuloVBX001Test.LOG.debug("Inicializando pruebas");
		
		daos();
	}
	
	@Test
	public void moduloVBX001Test(){
		Modulo moduloVBX001 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VBX001);
				
		try{
			TotalFlujoProyeccion proy = (TotalFlujoProyeccion) moduloVBX001.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso, terminal);
			BigDecimal vbx001 = proy.getProvbtiproy();
			
			TestCase.assertEquals(BigDecimal.valueOf(13265.6263), vbx001.setScale(4, RoundingMode.HALF_DOWN));
			
		}catch (Exception e){
			ModuloVBX001Test.LOG.debug(e.getMessage(), e);
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
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2016, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2010, 1, 8, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1967, 5, 21, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(12000));
		primas.setPrevprima(BigDecimal.ZERO);
		primas.setCformarevprim("V");
		primas.setPpr(BigDecimal.ZERO);
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
		datosGenerales.setCsitupol("VI");
		bti.setPintertecnI1(BigDecimal.valueOf(3.15));
		bti.setPgastgesex1I(BigDecimal.ZERO);
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		rescates.setKrescate1("KC001");
		rescates.setKrescate2("KC002");
		rescates.setTirIni(BigDecimal.valueOf(3.674177));
		rescates.setTirCie(BigDecimal.valueOf(1.376936));
		duraciones.setNdurprima(1);
		duraciones.setNdursegmes(0);
		duraciones.setNdursegano(6);
		capitales.setIcapact(BigDecimal.valueOf(13979.75));
		capitales.setIcapini(BigDecimal.valueOf(13979.75));
		capitales.setPorevalcap(BigDecimal.ZERO);
		capitales.setIcapfall(BigDecimal.ZERO);
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setCsexAseg1("H");
		rentas.setNadifer(0);
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setRescates(rescates);
		umic.setDuraciones(duraciones);
		umic.setCapitales(capitales);
		umic.setAsegurados(asegurados);
		umic.setRentas(rentas);

		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		detalleCorriente.setFechaHasta(FHASTA);
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
		itcalc.add(BigDecimal.ZERO);
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("707");
		tc.setTablaFin("707");
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
		btcUmic.setTablacalc1aseg1("707");
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(1.6));
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
			te.setKanacimiento("1967");
			te.setKtabla(707);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			tablaExerienciaDao.put(te.getKey(), te);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(707);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
			
		}catch (Exception e){
			ModuloVBX001Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}
}