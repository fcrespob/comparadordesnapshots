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

import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.CabeceraTablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dominio.maestro.Asegurados;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Primas;
import es.mapfre.solvencia.dominio.maestro.Rentas;
import es.mapfre.solvencia.dominio.maestro.Umic;
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
import junit.framework.TestCase;

public class ModuloPRI005Test {

	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloPRI005Test.class);

	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2017, Calendar.APRIL, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2012, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FPAGO = new Timestamp(new GregorianCalendar(2012, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2017, Calendar.MARCH, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 370;
	public static final int garantia = 273;
	public static final String negocio = "C";
	public static final int canal = 1;
	
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("986498.4"));
		TABLA_MORTALIDAD.add(new BigDecimal("985207.37"));
		TABLA_MORTALIDAD.add(new BigDecimal("984532.897"));
		TABLA_MORTALIDAD.add(new BigDecimal("983944.245"));
		TABLA_MORTALIDAD.add(new BigDecimal("983454.044"));
		TABLA_MORTALIDAD.add(new BigDecimal("982986.313"));
		TABLA_MORTALIDAD.add(new BigDecimal("982568.052"));
		TABLA_MORTALIDAD.add(new BigDecimal("982170.8"));
		TABLA_MORTALIDAD.add(new BigDecimal("981808.281"));
		TABLA_MORTALIDAD.add(new BigDecimal("981463.372"));
		TABLA_MORTALIDAD.add(new BigDecimal("981137.624"));
		TABLA_MORTALIDAD.add(new BigDecimal("980789.418"));
		TABLA_MORTALIDAD.add(new BigDecimal("980434.961"));
		TABLA_MORTALIDAD.add(new BigDecimal("980019.551"));
		TABLA_MORTALIDAD.add(new BigDecimal("979520.819"));
		TABLA_MORTALIDAD.add(new BigDecimal("978855.039"));
		TABLA_MORTALIDAD.add(new BigDecimal("977938.929"));
		TABLA_MORTALIDAD.add(new BigDecimal("976768.238"));
		TABLA_MORTALIDAD.add(new BigDecimal("975411.214"));
		TABLA_MORTALIDAD.add(new BigDecimal("973908.886"));
		TABLA_MORTALIDAD.add(new BigDecimal("972253.144"));
		TABLA_MORTALIDAD.add(new BigDecimal("970566.382"));
		TABLA_MORTALIDAD.add(new BigDecimal("968791.119"));
		TABLA_MORTALIDAD.add(new BigDecimal("966953.516"));
		TABLA_MORTALIDAD.add(new BigDecimal("965078.496"));
		TABLA_MORTALIDAD.add(new BigDecimal("963253.726"));
		TABLA_MORTALIDAD.add(new BigDecimal("961424.218"));
		TABLA_MORTALIDAD.add(new BigDecimal("959559.055"));
		TABLA_MORTALIDAD.add(new BigDecimal("957659.992"));
		TABLA_MORTALIDAD.add(new BigDecimal("955768.135"));
		TABLA_MORTALIDAD.add(new BigDecimal("953955.425"));
		TABLA_MORTALIDAD.add(new BigDecimal("952226.572"));
		TABLA_MORTALIDAD.add(new BigDecimal("950491.996"));
		TABLA_MORTALIDAD.add(new BigDecimal("948782.251"));
		TABLA_MORTALIDAD.add(new BigDecimal("947114.387"));
		TABLA_MORTALIDAD.add(new BigDecimal("945504.482"));
		TABLA_MORTALIDAD.add(new BigDecimal("943960.19"));
		TABLA_MORTALIDAD.add(new BigDecimal("942454.102"));
		TABLA_MORTALIDAD.add(new BigDecimal("940861.26"));
		TABLA_MORTALIDAD.add(new BigDecimal("939146.823"));
		TABLA_MORTALIDAD.add(new BigDecimal("937358.03"));
		TABLA_MORTALIDAD.add(new BigDecimal("935466.91"));
		TABLA_MORTALIDAD.add(new BigDecimal("933477.733"));
		TABLA_MORTALIDAD.add(new BigDecimal("931423.242"));
		TABLA_MORTALIDAD.add(new BigDecimal("929261.222"));
		TABLA_MORTALIDAD.add(new BigDecimal("926962.694"));
		TABLA_MORTALIDAD.add(new BigDecimal("924481.029"));
		TABLA_MORTALIDAD.add(new BigDecimal("921920.401"));
		TABLA_MORTALIDAD.add(new BigDecimal("919283.524"));
		TABLA_MORTALIDAD.add(new BigDecimal("916263.218"));
		TABLA_MORTALIDAD.add(new BigDecimal("912924.172"));
		TABLA_MORTALIDAD.add(new BigDecimal("909255.221"));
		TABLA_MORTALIDAD.add(new BigDecimal("905333.967"));
		TABLA_MORTALIDAD.add(new BigDecimal("900745.644"));
		TABLA_MORTALIDAD.add(new BigDecimal("895977.187"));
		TABLA_MORTALIDAD.add(new BigDecimal("890950.038"));
		TABLA_MORTALIDAD.add(new BigDecimal("885693.611"));
		TABLA_MORTALIDAD.add(new BigDecimal("880102.227"));
		TABLA_MORTALIDAD.add(new BigDecimal("874146.839"));
		TABLA_MORTALIDAD.add(new BigDecimal("867955.869"));
		TABLA_MORTALIDAD.add(new BigDecimal("861135.211"));
		TABLA_MORTALIDAD.add(new BigDecimal("853824.948"));
		TABLA_MORTALIDAD.add(new BigDecimal("846113.543"));
		TABLA_MORTALIDAD.add(new BigDecimal("837981.63"));
		TABLA_MORTALIDAD.add(new BigDecimal("829172.6"));
		TABLA_MORTALIDAD.add(new BigDecimal("819590.184"));
		TABLA_MORTALIDAD.add(new BigDecimal("809516.929"));
		TABLA_MORTALIDAD.add(new BigDecimal("798759.016"));
		TABLA_MORTALIDAD.add(new BigDecimal("787242.189"));
		TABLA_MORTALIDAD.add(new BigDecimal("775071.425"));
		TABLA_MORTALIDAD.add(new BigDecimal("762330.336"));
		TABLA_MORTALIDAD.add(new BigDecimal("748788.3"));
		TABLA_MORTALIDAD.add(new BigDecimal("734166.786"));
		TABLA_MORTALIDAD.add(new BigDecimal("718470.447"));
		TABLA_MORTALIDAD.add(new BigDecimal("701643.079"));
		TABLA_MORTALIDAD.add(new BigDecimal("683726.623"));
		TABLA_MORTALIDAD.add(new BigDecimal("664845.17"));
		TABLA_MORTALIDAD.add(new BigDecimal("645024.806"));
		TABLA_MORTALIDAD.add(new BigDecimal("624224.111"));
		TABLA_MORTALIDAD.add(new BigDecimal("602318.339"));
		TABLA_MORTALIDAD.add(new BigDecimal("579480.415"));
		TABLA_MORTALIDAD.add(new BigDecimal("555522.203"));
		TABLA_MORTALIDAD.add(new BigDecimal("530597.255"));
		TABLA_MORTALIDAD.add(new BigDecimal("505150.872"));
		TABLA_MORTALIDAD.add(new BigDecimal("479576.649"));
		TABLA_MORTALIDAD.add(new BigDecimal("453326.877"));
		TABLA_MORTALIDAD.add(new BigDecimal("426344.907"));
		TABLA_MORTALIDAD.add(new BigDecimal("399351.348"));
		TABLA_MORTALIDAD.add(new BigDecimal("372175.129"));
		TABLA_MORTALIDAD.add(new BigDecimal("344760.039"));
		TABLA_MORTALIDAD.add(new BigDecimal("317314.347"));
		TABLA_MORTALIDAD.add(new BigDecimal("290140.593"));
		TABLA_MORTALIDAD.add(new BigDecimal("261854.032"));
		TABLA_MORTALIDAD.add(new BigDecimal("232895.202"));
		TABLA_MORTALIDAD.add(new BigDecimal("203593.609"));
		TABLA_MORTALIDAD.add(new BigDecimal("174657.704"));
		TABLA_MORTALIDAD.add(new BigDecimal("146531.875"));
		TABLA_MORTALIDAD.add(new BigDecimal("119715.912"));
		TABLA_MORTALIDAD.add(new BigDecimal("94743.556"));
		TABLA_MORTALIDAD.add(new BigDecimal("72101.637"));
		TABLA_MORTALIDAD.add(new BigDecimal("52301.475"));
		TABLA_MORTALIDAD.add(new BigDecimal("35309.249"));
		TABLA_MORTALIDAD.add(new BigDecimal("23036.813"));
		TABLA_MORTALIDAD.add(new BigDecimal("14470.92"));
		TABLA_MORTALIDAD.add(new BigDecimal("8714.489"));
		TABLA_MORTALIDAD.add(new BigDecimal("5005.925"));
		TABLA_MORTALIDAD.add(new BigDecimal("2726.868"));
		TABLA_MORTALIDAD.add(new BigDecimal("1398.733"));
		TABLA_MORTALIDAD.add(new BigDecimal("684.259"));
		TABLA_MORTALIDAD.add(new BigDecimal("317.699"));
		TABLA_MORTALIDAD.add(new BigDecimal("139.209"));
		TABLA_MORTALIDAD.add(new BigDecimal("57.186"));
		TABLA_MORTALIDAD.add(new BigDecimal("21.849"));
		TABLA_MORTALIDAD.add(new BigDecimal("7.689"));
		TABLA_MORTALIDAD.add(new BigDecimal("2.463"));
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
		ModuloPRI005Test.LOG.debug("Inicializando pruebas");
		daos();
		/*try{
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
			ModuloPRI005Test.LOG.debug(e.getMessage(), e);
			fail();
		}*/
	}
	
	@Test
	public void ModuloPRI005Test(){
		Modulo moduloPRI005 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_PRI005);
				
		try{
			BigDecimal pri005 = (BigDecimal) moduloPRI005.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(pri005);

			TestCase.assertEquals(BigDecimal.valueOf(370615.49758018).setScale(8, RoundingMode.HALF_DOWN), pri005.setScale(8, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloPRI005Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial(); 
		Asegurados asegurados = new Asegurados();
		Rentas rentas = new Rentas();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2033, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		//Timestamp fecdesderenova = new Timestamp(new GregorianCalendar(2016, Calendar.JULY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2002, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecini = new Timestamp(new GregorianCalendar(2002, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecfinpagprim = new Timestamp(new GregorianCalendar(2009, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		//Timestamp fecefecred ;
		Timestamp fecinipagprim = new Timestamp(new GregorianCalendar(2002, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1943, Calendar.AUGUST, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecIni = new Timestamp(new GregorianCalendar(2002, Calendar.NOVEMBER, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		
		fechas.setFecefecfin(fecefecfin);
		//fechas.setFecdesderenova(fecdesderenova);
		fechas.setFecefecini(fecefecini);
		fechas.setFecinisus(fecinisus);
		fechas.setFecinipagprim(fecinipagprim);
		fechas.setFecfinpagprim(fecfinpagprim);
		//fechas.setFecefecred(fecefecred);
		
		primas.setIprimanetaini(BigDecimal.valueOf(58314.36));
		primas.setPrevprima(BigDecimal.valueOf(0)); //0.03
		primas.setCformpago("1");
		
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		datosGenerales.setEdifer(0);
		datosGenerales.setFecCierre(FCIERRE);
		
		bti.setPgastgesex1I(BigDecimal.valueOf(2));
		bti.setPsobremort(new BigDecimal(ConstantsFunciones.CTE_0));
		bti.setPriesgo(new BigDecimal(ConstantsFunciones.CTE_0));
		
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setCsexAseg1("H");
		
		rentas.setFecIni(fecIni);
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setAsegurados(asegurados);
		umic.setRentas(rentas);
		
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
		bloqueCorriente.setFechaPago(FPAGO);
		
		return bloqueCorriente;
	}
	
	
	private DetalleBaseTecnica getBtcUmic(){
		DetalleBaseTecnica btcUmic = new DetalleBaseTecnica();
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		List<Timestamp> fecfintramo = new ArrayList<Timestamp>(); 
 		String tablacalc1aseg1 = "740";
		
 		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("740");
		tc.setTablaFin("740");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		tca.add(tabCon);
 		
		itcalc.add(0,BigDecimal.valueOf(2.5));
		itcalc.add(1,BigDecimal.valueOf(0));
		fecfintramo.add(0, new Timestamp(new GregorianCalendar(2113, Calendar.JANUARY, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()));
		btcUmic.setFecfintramo(fecfintramo);
		btcUmic.setTablacalc1aseg1(tablacalc1aseg1);
		

		btcUmic.setIndTabExp('T');
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);
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
			da2.setCidentivariab("ID-CRITERIO");
			da2.setGvariable("ID-CRITERIO");
			da2.setGvalor("06");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			
			DetalleCorrienteDao detalleCorrienteDao = new DetalleCorrienteDao();
			DetalleCorriente dc = new DetalleCorriente();
			TotalFlujoProyeccion tfp = new TotalFlujoProyeccion();
			tfp.setProvbtiproy(BigDecimal.valueOf(8081.83));
			
			dc.setFcierre(FCIERRE);
			dc.setKmodalidad(modalidad);
			dc.setKpoliza(430486L);
			dc.setKsubpoliza(1);
			dc.setKcertificado(1);
			dc.setNsuscri(1);
			dc.setNorden(10);
			dc.setKgarantia(273);
			dc.setKprestacion("RVR11");
			dc.setKajuste(0);
			dc.setCtipoaport("P");
			dc.setBt("BTI");
			dc.setTotalFlujoProyeccion(tfp);
			
			detalleCorrienteDao.put(dc.getKey(), dc);
			
			TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1943");
			te.setKtabla(740);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("11");
			te.setGvalor(TABLA_MORTALIDAD);
			
			tablaExerienciaDao.put(te.getKey(), te);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(740);
			cte.setK2tipotabla("2");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
			
		}catch (Exception e){
			ModuloPRI005Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	
	}

}
