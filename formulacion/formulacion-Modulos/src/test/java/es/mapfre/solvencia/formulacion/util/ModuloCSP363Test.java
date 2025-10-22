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
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que testea el Módulo VRTA163, módulo de cálculo que devuelve los factores de probabilización a aplicar en  el 
 * cálculo de la cuantía probable de la garantía principal de Modalidades Rentas de Jubilación con Reembolso de Reservas (Mod. 363)
 * @author apedro
 *
 */

public class ModuloCSP363Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloCSP363Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2023, 6, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2023, 6, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2023, 6, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 363;
	public static final int garantia = 274;
	public static final String negocio = "C";
	public static final int canal = 1;
	
	/**
	 * Lista con valores de mortalidad para los valores:
	 * BASE_TECNICA: BTI
	 * TABLA: 740
	 * W-K2TIPOVALOR: 2
	 * FECHA_NACIMIENTO: 1960
	 * INTERES: 0
	 * SOBREMORT: 0
	 * PRIESGO:
	 * MODALIDAD: 
	 */
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("989537.4"));
		TABLA_MORTALIDAD.add(new BigDecimal("988533.811"));
		TABLA_MORTALIDAD.add(new BigDecimal("988009.394"));
		TABLA_MORTALIDAD.add(new BigDecimal("987551.55"));
		TABLA_MORTALIDAD.add(new BigDecimal("987170.256"));
		TABLA_MORTALIDAD.add(new BigDecimal("986806.484"));
		TABLA_MORTALIDAD.add(new BigDecimal("986481.134"));
		TABLA_MORTALIDAD.add(new BigDecimal("986172.069"));
		TABLA_MORTALIDAD.add(new BigDecimal("985890.024"));
		TABLA_MORTALIDAD.add(new BigDecimal("985621.665"));
		TABLA_MORTALIDAD.add(new BigDecimal("985368.163"));
		TABLA_MORTALIDAD.add(new BigDecimal("985097.187"));
		TABLA_MORTALIDAD.add(new BigDecimal("984821.36"));
		TABLA_MORTALIDAD.add(new BigDecimal("984498.043"));
		TABLA_MORTALIDAD.add(new BigDecimal("984109.757"));
		TABLA_MORTALIDAD.add(new BigDecimal("983591.426"));
		TABLA_MORTALIDAD.add(new BigDecimal("982878.027"));
		TABLA_MORTALIDAD.add(new BigDecimal("981966.211"));
		TABLA_MORTALIDAD.add(new BigDecimal("980909.026"));
		TABLA_MORTALIDAD.add(new BigDecimal("979738.311"));
		TABLA_MORTALIDAD.add(new BigDecimal("978447.506"));
		TABLA_MORTALIDAD.add(new BigDecimal("977132.081"));
		TABLA_MORTALIDAD.add(new BigDecimal("975747.094"));
		TABLA_MORTALIDAD.add(new BigDecimal("974312.941"));
		TABLA_MORTALIDAD.add(new BigDecimal("972848.938"));
		TABLA_MORTALIDAD.add(new BigDecimal("971423.52"));
		TABLA_MORTALIDAD.add(new BigDecimal("969993.779"));
		TABLA_MORTALIDAD.add(new BigDecimal("968535.587"));
		TABLA_MORTALIDAD.add(new BigDecimal("967050.241"));
		TABLA_MORTALIDAD.add(new BigDecimal("965569.784"));
		TABLA_MORTALIDAD.add(new BigDecimal("964150.686"));
		TABLA_MORTALIDAD.add(new BigDecimal("962796.729"));
		TABLA_MORTALIDAD.add(new BigDecimal("961437.645"));
		TABLA_MORTALIDAD.add(new BigDecimal("960097.497"));
		TABLA_MORTALIDAD.add(new BigDecimal("958789.652"));
		TABLA_MORTALIDAD.add(new BigDecimal("957526.734"));
		TABLA_MORTALIDAD.add(new BigDecimal("956314.792"));
		TABLA_MORTALIDAD.add(new BigDecimal("955132.404"));
		TABLA_MORTALIDAD.add(new BigDecimal("953881.467"));
		TABLA_MORTALIDAD.add(new BigDecimal("952534.586"));
		TABLA_MORTALIDAD.add(new BigDecimal("951128.645"));
		TABLA_MORTALIDAD.add(new BigDecimal("949641.65"));
		TABLA_MORTALIDAD.add(new BigDecimal("948076.83"));
		TABLA_MORTALIDAD.add(new BigDecimal("946459.885"));
		TABLA_MORTALIDAD.add(new BigDecimal("944757.393"));
		TABLA_MORTALIDAD.add(new BigDecimal("942946.577"));
		TABLA_MORTALIDAD.add(new BigDecimal("940990.34"));
		TABLA_MORTALIDAD.add(new BigDecimal("938970.598"));
		TABLA_MORTALIDAD.add(new BigDecimal("936889.464"));
		TABLA_MORTALIDAD.add(new BigDecimal("934504.143"));
		TABLA_MORTALIDAD.add(new BigDecimal("931865.103"));
		TABLA_MORTALIDAD.add(new BigDecimal("928962.996"));
		TABLA_MORTALIDAD.add(new BigDecimal("925858.495"));
		TABLA_MORTALIDAD.add(new BigDecimal("922222.278"));
		TABLA_MORTALIDAD.add(new BigDecimal("918438.953"));
		TABLA_MORTALIDAD.add(new BigDecimal("914445.672"));
		TABLA_MORTALIDAD.add(new BigDecimal("910264.918"));
		TABLA_MORTALIDAD.add(new BigDecimal("905811.902"));
		TABLA_MORTALIDAD.add(new BigDecimal("901062.096"));
		TABLA_MORTALIDAD.add(new BigDecimal("896116.887"));
		TABLA_MORTALIDAD.add(new BigDecimal("890659.983"));
		TABLA_MORTALIDAD.add(new BigDecimal("884800.954"));
		TABLA_MORTALIDAD.add(new BigDecimal("878608.498"));
		TABLA_MORTALIDAD.add(new BigDecimal("872064.973"));
		TABLA_MORTALIDAD.add(new BigDecimal("864961.045"));
		TABLA_MORTALIDAD.add(new BigDecimal("857214.973"));
		TABLA_MORTALIDAD.add(new BigDecimal("849050.686"));
		TABLA_MORTALIDAD.add(new BigDecimal("840307.077"));
		TABLA_MORTALIDAD.add(new BigDecimal("830918.242"));
		TABLA_MORTALIDAD.add(new BigDecimal("820963.675"));
		TABLA_MORTALIDAD.add(new BigDecimal("810505.829"));
		TABLA_MORTALIDAD.add(new BigDecimal("799348.73"));
		TABLA_MORTALIDAD.add(new BigDecimal("787253.225"));
		TABLA_MORTALIDAD.add(new BigDecimal("774210.407"));
		TABLA_MORTALIDAD.add(new BigDecimal("760158.953"));
		TABLA_MORTALIDAD.add(new BigDecimal("745117.308"));
		TABLA_MORTALIDAD.add(new BigDecimal("729172.021"));
		TABLA_MORTALIDAD.add(new BigDecimal("712326.835"));
		TABLA_MORTALIDAD.add(new BigDecimal("694526.215"));
		TABLA_MORTALIDAD.add(new BigDecimal("675639.269"));
		TABLA_MORTALIDAD.add(new BigDecimal("655787.433"));
		TABLA_MORTALIDAD.add(new BigDecimal("634777.053"));
		TABLA_MORTALIDAD.add(new BigDecimal("612706.68"));
		TABLA_MORTALIDAD.add(new BigDecimal("589936.417"));
		TABLA_MORTALIDAD.add(new BigDecimal("566792.267"));
		TABLA_MORTALIDAD.add(new BigDecimal("542751.603"));
		TABLA_MORTALIDAD.add(new BigDecimal("517718.325"));
		TABLA_MORTALIDAD.add(new BigDecimal("492317.511"));
		TABLA_MORTALIDAD.add(new BigDecimal("466355.738"));
		TABLA_MORTALIDAD.add(new BigDecimal("439735.313"));
		TABLA_MORTALIDAD.add(new BigDecimal("412608.173"));
		TABLA_MORTALIDAD.add(new BigDecimal("385226.958"));
		TABLA_MORTALIDAD.add(new BigDecimal("355371.907"));
		TABLA_MORTALIDAD.add(new BigDecimal("323323.331"));
		TABLA_MORTALIDAD.add(new BigDecimal("289294.585"));
		TABLA_MORTALIDAD.add(new BigDecimal("254011.552"));
		TABLA_MORTALIDAD.add(new BigDecimal("218003.586"));
		TABLA_MORTALIDAD.add(new BigDecimal("181976.706"));
		TABLA_MORTALIDAD.add(new BigDecimal("146812.583"));
		TABLA_MORTALIDAD.add(new BigDecimal("113471.651"));
		TABLA_MORTALIDAD.add(new BigDecimal("83095.233"));
		TABLA_MORTALIDAD.add(new BigDecimal("56098.423"));
		TABLA_MORTALIDAD.add(new BigDecimal("36600.294"));
		TABLA_MORTALIDAD.add(new BigDecimal("22991.024"));
		TABLA_MORTALIDAD.add(new BigDecimal("13845.356"));
		TABLA_MORTALIDAD.add(new BigDecimal("7953.285"));
		TABLA_MORTALIDAD.add(new BigDecimal("4332.377"));
		TABLA_MORTALIDAD.add(new BigDecimal("2222.271"));
		TABLA_MORTALIDAD.add(new BigDecimal("1087.133"));
		TABLA_MORTALIDAD.add(new BigDecimal("504.752"));
		TABLA_MORTALIDAD.add(new BigDecimal("221.172"));
		TABLA_MORTALIDAD.add(new BigDecimal("90.855"));
		TABLA_MORTALIDAD.add(new BigDecimal("34.712"));
		TABLA_MORTALIDAD.add(new BigDecimal("12.216"));
		TABLA_MORTALIDAD.add(new BigDecimal("3.913"));
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
	
	private static final List<BigDecimal> TABLA_MORTALIDAD2 = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD2.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD2.add(new BigDecimal("992590.2"));
		TABLA_MORTALIDAD2.add(new BigDecimal("991877.222"));
		TABLA_MORTALIDAD2.add(new BigDecimal("991504.574"));
		TABLA_MORTALIDAD2.add(new BigDecimal("991179.162"));
		TABLA_MORTALIDAD2.add(new BigDecimal("990908.174"));
		TABLA_MORTALIDAD2.add(new BigDecimal("990649.547"));
		TABLA_MORTALIDAD2.add(new BigDecimal("990418.23"));
		TABLA_MORTALIDAD2.add(new BigDecimal("990198.456"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989997.842"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989806.97"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989626.627"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989433.848"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989237.643"));
		TABLA_MORTALIDAD2.add(new BigDecimal("989007.645"));
		TABLA_MORTALIDAD2.add(new BigDecimal("988731.415"));
		TABLA_MORTALIDAD2.add(new BigDecimal("988362.618"));
		TABLA_MORTALIDAD2.add(new BigDecimal("987854.995"));
		TABLA_MORTALIDAD2.add(new BigDecimal("987205.974"));
		TABLA_MORTALIDAD2.add(new BigDecimal("986453.229"));
		TABLA_MORTALIDAD2.add(new BigDecimal("985619.38"));
		TABLA_MORTALIDAD2.add(new BigDecimal("984699.699"));
		TABLA_MORTALIDAD2.add(new BigDecimal("983762.166"));
		TABLA_MORTALIDAD2.add(new BigDecimal("982774.666"));
		TABLA_MORTALIDAD2.add(new BigDecimal("981751.598"));
		TABLA_MORTALIDAD2.add(new BigDecimal("980706.818"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979689.139"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978668.009"));
		TABLA_MORTALIDAD2.add(new BigDecimal("977626.021"));
		TABLA_MORTALIDAD2.add(new BigDecimal("976564.124"));
		TABLA_MORTALIDAD2.add(new BigDecimal("975505.333"));
		TABLA_MORTALIDAD2.add(new BigDecimal("974489.929"));
		TABLA_MORTALIDAD2.add(new BigDecimal("973520.701"));
		TABLA_MORTALIDAD2.add(new BigDecimal("972547.472"));
		TABLA_MORTALIDAD2.add(new BigDecimal("971587.373"));
		TABLA_MORTALIDAD2.add(new BigDecimal("970649.986"));
		TABLA_MORTALIDAD2.add(new BigDecimal("969744.564"));
		TABLA_MORTALIDAD2.add(new BigDecimal("968875.285"));
		TABLA_MORTALIDAD2.add(new BigDecimal("968026.938"));
		TABLA_MORTALIDAD2.add(new BigDecimal("967128.996"));
		TABLA_MORTALIDAD2.add(new BigDecimal("966161.867"));
		TABLA_MORTALIDAD2.add(new BigDecimal("965151.938"));
		TABLA_MORTALIDAD2.add(new BigDecimal("964083.322"));
		TABLA_MORTALIDAD2.add(new BigDecimal("962958.237"));
		TABLA_MORTALIDAD2.add(new BigDecimal("961795.08"));
		TABLA_MORTALIDAD2.add(new BigDecimal("960569.849"));
		TABLA_MORTALIDAD2.add(new BigDecimal("959265.875"));
		TABLA_MORTALIDAD2.add(new BigDecimal("957856.426"));
		TABLA_MORTALIDAD2.add(new BigDecimal("956400.388"));
		TABLA_MORTALIDAD2.add(new BigDecimal("954899.126"));
		TABLA_MORTALIDAD2.add(new BigDecimal("953177.347"));
		TABLA_MORTALIDAD2.add(new BigDecimal("951270.992"));
		TABLA_MORTALIDAD2.add(new BigDecimal("949172.869"));
		TABLA_MORTALIDAD2.add(new BigDecimal("946926.367"));
		TABLA_MORTALIDAD2.add(new BigDecimal("944292.491"));
		TABLA_MORTALIDAD2.add(new BigDecimal("941548.944"));
		TABLA_MORTALIDAD2.add(new BigDecimal("938649.632"));
		TABLA_MORTALIDAD2.add(new BigDecimal("935610.378"));
		TABLA_MORTALIDAD2.add(new BigDecimal("932368.862"));
		TABLA_MORTALIDAD2.add(new BigDecimal("928906.324"));
		TABLA_MORTALIDAD2.add(new BigDecimal("925295.758"));
		TABLA_MORTALIDAD2.add(new BigDecimal("921305.235"));
		TABLA_MORTALIDAD2.add(new BigDecimal("917012.966"));
		TABLA_MORTALIDAD2.add(new BigDecimal("912467.7"));
		TABLA_MORTALIDAD2.add(new BigDecimal("907654.798"));
		TABLA_MORTALIDAD2.add(new BigDecimal("902418.356"));
		TABLA_MORTALIDAD2.add(new BigDecimal("896694.858"));
		TABLA_MORTALIDAD2.add(new BigDecimal("890646.472"));
		TABLA_MORTALIDAD2.add(new BigDecimal("884150.72"));
		TABLA_MORTALIDAD2.add(new BigDecimal("877154.435"));
		TABLA_MORTALIDAD2.add(new BigDecimal("869712.13"));
		TABLA_MORTALIDAD2.add(new BigDecimal("861865.848"));
		TABLA_MORTALIDAD2.add(new BigDecimal("853463.432"));
		TABLA_MORTALIDAD2.add(new BigDecimal("844317.206"));
		TABLA_MORTALIDAD2.add(new BigDecimal("834410.41"));
		TABLA_MORTALIDAD2.add(new BigDecimal("823685.065"));
		TABLA_MORTALIDAD2.add(new BigDecimal("812142.025"));
		TABLA_MORTALIDAD2.add(new BigDecimal("799833.444"));
		TABLA_MORTALIDAD2.add(new BigDecimal("786747.209"));
		TABLA_MORTALIDAD2.add(new BigDecimal("772823.357"));
		TABLA_MORTALIDAD2.add(new BigDecimal("757939.243"));
		TABLA_MORTALIDAD2.add(new BigDecimal("742167.209"));
		TABLA_MORTALIDAD2.add(new BigDecimal("725327.287"));
		TABLA_MORTALIDAD2.add(new BigDecimal("707466.9"));
		TABLA_MORTALIDAD2.add(new BigDecimal("688846.442"));
		TABLA_MORTALIDAD2.add(new BigDecimal("669707.119"));
		TABLA_MORTALIDAD2.add(new BigDecimal("649589.519"));
		TABLA_MORTALIDAD2.add(new BigDecimal("628370.547"));
		TABLA_MORTALIDAD2.add(new BigDecimal("606536.304"));
		TABLA_MORTALIDAD2.add(new BigDecimal("583883.932"));
		TABLA_MORTALIDAD2.add(new BigDecimal("560279.548"));
		TABLA_MORTALIDAD2.add(new BigDecimal("535800.991"));
		TABLA_MORTALIDAD2.add(new BigDecimal("510619.202"));
		TABLA_MORTALIDAD2.add(new BigDecimal("481609.087"));
		TABLA_MORTALIDAD2.add(new BigDecimal("448651.518"));
		TABLA_MORTALIDAD2.add(new BigDecimal("411563.292"));
		TABLA_MORTALIDAD2.add(new BigDecimal("370753.581"));
		TABLA_MORTALIDAD2.add(new BigDecimal("326523.828"));
		TABLA_MORTALIDAD2.add(new BigDecimal("279518.699"));
		TABLA_MORTALIDAD2.add(new BigDecimal("230816.842"));
		TABLA_MORTALIDAD2.add(new BigDecimal("181893.55"));
		TABLA_MORTALIDAD2.add(new BigDecimal("134851.822"));
		TABLA_MORTALIDAD2.add(new BigDecimal("91039.814"));
		TABLA_MORTALIDAD2.add(new BigDecimal("59397.106"));
		TABLA_MORTALIDAD2.add(new BigDecimal("37311.183"));
		TABLA_MORTALIDAD2.add(new BigDecimal("22469.056"));
		TABLA_MORTALIDAD2.add(new BigDecimal("12907.057"));
		TABLA_MORTALIDAD2.add(new BigDecimal("7030.835"));
		TABLA_MORTALIDAD2.add(new BigDecimal("3606.432"));
		TABLA_MORTALIDAD2.add(new BigDecimal("1764.263"));
		TABLA_MORTALIDAD2.add(new BigDecimal("819.14"));
		TABLA_MORTALIDAD2.add(new BigDecimal("358.93"));
		TABLA_MORTALIDAD2.add(new BigDecimal("147.444"));
		TABLA_MORTALIDAD2.add(new BigDecimal("56.333"));
		TABLA_MORTALIDAD2.add(new BigDecimal("19.826"));
		TABLA_MORTALIDAD2.add(new BigDecimal("6.351"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));
		TABLA_MORTALIDAD2.add(new BigDecimal("0"));

	}
	
	@BeforeClass
	public static void init() {
		ModuloCSP363Test.LOG.debug("Inicializando pruebas");
		
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
			
			TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1960");
			te.setKtabla(740);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			tablaExerienciaDao.put(te.getKey(), te);
			
			TablaExperiencia te2 = new TablaExperiencia();
			te2.setKanacimiento("1983");
			te2.setKtabla(740);
			te2.setKsobremort(BigDecimal.ZERO);
			te2.setKsobreries(BigDecimal.ZERO);
			te2.setK2tipovalor("2");
			te2.setGvalor(TABLA_MORTALIDAD2);
			
			tablaExerienciaDao.put(te2.getKey(), te2);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(740);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
		}catch (Exception e){
			ModuloCSP363Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloCSP363Test(){
		Modulo moduloCSP363 = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_CSP363);
				
		try{
			BigDecimal csp363 = (BigDecimal) moduloCSP363.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			
			System.out.println(csp363);

			TestCase.assertEquals(BigDecimal.valueOf(84591.08696), csp363.setScale(5, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloCSP363Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	private Umic getUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		Primas primas = new Primas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();
		Rentas rentas = new Rentas();
		Asegurados asegurados = new Asegurados();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(9999, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2012, 8, 19, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinirent = new Timestamp(new GregorianCalendar(2023, 6, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecnacAseg1 = new Timestamp(new GregorianCalendar(1960, 6, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecnacAseg2 = new Timestamp(new GregorianCalendar(1983, 7, 29, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecFinTramo1 = new Timestamp(new GregorianCalendar(2042, 8, 19, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecFinTramo2 = new Timestamp(new GregorianCalendar(9999, 11, 31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(58832.32));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		datosGenerales.setCtipoaport("U");
		datosGenerales.setFecCierre(FCIERRE);
		bti.setPintertecnI1(BigDecimal.valueOf(5.29));
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		bti.setFecFinTramo1(fecFinTramo1);
		bti.setFecFinTramo2(fecFinTramo2);
		rentas.setNpergaran(0);
		rentas.setForpagrent(12);
		rentas.setFecIni(fecinirent);
		rentas.setPreversion(BigDecimal.ZERO);
		asegurados.setFnacAseg1(fecnacAseg1);
		asegurados.setCsexAseg1("H");
		asegurados.setEdadAseg1(52);
		asegurados.setFnacAseg2(fecnacAseg2);
		asegurados.setCsexAseg2("H");
		asegurados.setEdadAseg2(29);
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setRentas(rentas);
		umic.setAsegurados(asegurados);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente;
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		Timestamp fecDesde = FDESDE;
		Timestamp fecDevengo = FDEVENGO;
		
		for (int i=0;i<914;i++){
			detalleCorriente = new DetalleCorriente();
			detalleCorriente.setFechaDesde(fecDesde);
			BloqueCorriente bloque = new BloqueCorriente();
			bloque = getBloqueCorriente();
			bloque.setFechaDevengo(fecDevengo);
			detalleCorriente.setBloqueVida(bloque);
			detalleCorriente.setImpPago(BigDecimal.valueOf(512));
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
		itcalc.add(BigDecimal.valueOf(5.29));
		itcalc.add(BigDecimal.valueOf(2.25));
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("740");
		tc.setTablaFin("740");
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
		btcUmic.setTablacalc1aseg1("740");
		btcUmic.setTablacalc1aseg2("740");
		btcUmic.setTablasConversionAsegurado(tca);
		
		
		return btcUmic;
	}
	
	
}