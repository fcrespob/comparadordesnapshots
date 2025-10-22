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
 * Clase que testea el Módulo VRTA168, módulo de cálculo que devuelve los factores de probabilización a aplicar en  el 
 * cálculo de la cuantía probable de la garantía.
 * @author apedro
 *
 */

public class ModuloVRTAVITTest{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloVRTAVITTest.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2013, 12, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2014, 03, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2023, 05, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "";
	
	public static final int cartera = 1201;
	public static final int modalidad = 238;
	public static final int garantia = 265;
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
		TABLA_MORTALIDAD2.add(new BigDecimal("984700.4"));
		TABLA_MORTALIDAD2.add(new BigDecimal("983511.768"));
		TABLA_MORTALIDAD2.add(new BigDecimal("982664.964"));
		TABLA_MORTALIDAD2.add(new BigDecimal("982117.521"));
		TABLA_MORTALIDAD2.add(new BigDecimal("981607.704"));
		TABLA_MORTALIDAD2.add(new BigDecimal("981206.128"));
		TABLA_MORTALIDAD2.add(new BigDecimal("980853.483"));
		TABLA_MORTALIDAD2.add(new BigDecimal("980546.574"));
		TABLA_MORTALIDAD2.add(new BigDecimal("980299.182"));
		TABLA_MORTALIDAD2.add(new BigDecimal("980077.83"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979849.57"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979643.018"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979409.569"));
		TABLA_MORTALIDAD2.add(new BigDecimal("979172.454"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978932.067"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978679.405"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978395.294"));
		TABLA_MORTALIDAD2.add(new BigDecimal("978072.815"));
		TABLA_MORTALIDAD2.add(new BigDecimal("977731.37"));
		TABLA_MORTALIDAD2.add(new BigDecimal("977372.347"));
		TABLA_MORTALIDAD2.add(new BigDecimal("977023.034"));
		TABLA_MORTALIDAD2.add(new BigDecimal("976667.202"));
		TABLA_MORTALIDAD2.add(new BigDecimal("976336.405"));
		TABLA_MORTALIDAD2.add(new BigDecimal("975998.007"));
		TABLA_MORTALIDAD2.add(new BigDecimal("975654.163"));
		TABLA_MORTALIDAD2.add(new BigDecimal("975312.489"));
		TABLA_MORTALIDAD2.add(new BigDecimal("974950.941"));
		TABLA_MORTALIDAD2.add(new BigDecimal("974562.716"));
		TABLA_MORTALIDAD2.add(new BigDecimal("974171.819"));
		TABLA_MORTALIDAD2.add(new BigDecimal("973757.114"));
		TABLA_MORTALIDAD2.add(new BigDecimal("973353.589"));
		TABLA_MORTALIDAD2.add(new BigDecimal("972954.903"));
		TABLA_MORTALIDAD2.add(new BigDecimal("972584.694"));
		TABLA_MORTALIDAD2.add(new BigDecimal("972235.925"));
		TABLA_MORTALIDAD2.add(new BigDecimal("971867.642"));
		TABLA_MORTALIDAD2.add(new BigDecimal("971446.435"));
		TABLA_MORTALIDAD2.add(new BigDecimal("971019.776"));
		TABLA_MORTALIDAD2.add(new BigDecimal("970598.548"));
		TABLA_MORTALIDAD2.add(new BigDecimal("970167.796"));
		TABLA_MORTALIDAD2.add(new BigDecimal("969742.863"));
		TABLA_MORTALIDAD2.add(new BigDecimal("969254.888"));
		TABLA_MORTALIDAD2.add(new BigDecimal("968798.466"));
		TABLA_MORTALIDAD2.add(new BigDecimal("968276.962"));
		TABLA_MORTALIDAD2.add(new BigDecimal("967730.66"));
		TABLA_MORTALIDAD2.add(new BigDecimal("967157.086"));
		TABLA_MORTALIDAD2.add(new BigDecimal("966530.271"));
		TABLA_MORTALIDAD2.add(new BigDecimal("965905.506"));
		TABLA_MORTALIDAD2.add(new BigDecimal("965235.651"));
		TABLA_MORTALIDAD2.add(new BigDecimal("964556.608"));
		TABLA_MORTALIDAD2.add(new BigDecimal("963758.823"));
		TABLA_MORTALIDAD2.add(new BigDecimal("962865.708"));
		TABLA_MORTALIDAD2.add(new BigDecimal("961877.808"));
		TABLA_MORTALIDAD2.add(new BigDecimal("960870.818"));
		TABLA_MORTALIDAD2.add(new BigDecimal("959717.004"));
		TABLA_MORTALIDAD2.add(new BigDecimal("958523.116"));
		TABLA_MORTALIDAD2.add(new BigDecimal("957265.725"));
		TABLA_MORTALIDAD2.add(new BigDecimal("955990.264"));
		TABLA_MORTALIDAD2.add(new BigDecimal("954595.57"));
		TABLA_MORTALIDAD2.add(new BigDecimal("953098.955"));
		TABLA_MORTALIDAD2.add(new BigDecimal("951522.529"));
		TABLA_MORTALIDAD2.add(new BigDecimal("949797.894"));
		TABLA_MORTALIDAD2.add(new BigDecimal("947969.818"));
		TABLA_MORTALIDAD2.add(new BigDecimal("945996.714"));
		TABLA_MORTALIDAD2.add(new BigDecimal("943954.307"));
		TABLA_MORTALIDAD2.add(new BigDecimal("941770.563"));
		TABLA_MORTALIDAD2.add(new BigDecimal("939367.918"));
		TABLA_MORTALIDAD2.add(new BigDecimal("936772.257"));
		TABLA_MORTALIDAD2.add(new BigDecimal("933978.802"));
		TABLA_MORTALIDAD2.add(new BigDecimal("930964.385"));
		TABLA_MORTALIDAD2.add(new BigDecimal("927651.734"));
		TABLA_MORTALIDAD2.add(new BigDecimal("924074.616"));
		TABLA_MORTALIDAD2.add(new BigDecimal("920095.181"));
		TABLA_MORTALIDAD2.add(new BigDecimal("915749.663"));
		TABLA_MORTALIDAD2.add(new BigDecimal("910871.556"));
		TABLA_MORTALIDAD2.add(new BigDecimal("905456.151"));
		TABLA_MORTALIDAD2.add(new BigDecimal("899429.073"));
		TABLA_MORTALIDAD2.add(new BigDecimal("892694.418"));
		TABLA_MORTALIDAD2.add(new BigDecimal("885155.346"));
		TABLA_MORTALIDAD2.add(new BigDecimal("876813.819"));
		TABLA_MORTALIDAD2.add(new BigDecimal("867537.567"));
		TABLA_MORTALIDAD2.add(new BigDecimal("857066.475"));
		TABLA_MORTALIDAD2.add(new BigDecimal("844985.437"));
		TABLA_MORTALIDAD2.add(new BigDecimal("831423.505"));
		TABLA_MORTALIDAD2.add(new BigDecimal("815920.699"));
		TABLA_MORTALIDAD2.add(new BigDecimal("798001.449"));
		TABLA_MORTALIDAD2.add(new BigDecimal("777677.469"));
		TABLA_MORTALIDAD2.add(new BigDecimal("755085.316"));
		TABLA_MORTALIDAD2.add(new BigDecimal("729760.132"));
		TABLA_MORTALIDAD2.add(new BigDecimal("701831.555"));
		TABLA_MORTALIDAD2.add(new BigDecimal("670912.717"));
		TABLA_MORTALIDAD2.add(new BigDecimal("636780.368"));
		TABLA_MORTALIDAD2.add(new BigDecimal("601286.612"));
		TABLA_MORTALIDAD2.add(new BigDecimal("561685.455"));
		TABLA_MORTALIDAD2.add(new BigDecimal("518033.115"));
		TABLA_MORTALIDAD2.add(new BigDecimal("470121.734"));
		TABLA_MORTALIDAD2.add(new BigDecimal("418449.009"));
		TABLA_MORTALIDAD2.add(new BigDecimal("363854.009"));
		TABLA_MORTALIDAD2.add(new BigDecimal("307558.116"));
		TABLA_MORTALIDAD2.add(new BigDecimal("251170.78"));
		TABLA_MORTALIDAD2.add(new BigDecimal("196520.539"));
		TABLA_MORTALIDAD2.add(new BigDecimal("146297.73"));
		TABLA_MORTALIDAD2.add(new BigDecimal("101228.081"));
		TABLA_MORTALIDAD2.add(new BigDecimal("67906.226"));
		TABLA_MORTALIDAD2.add(new BigDecimal("44087.099"));
		TABLA_MORTALIDAD2.add(new BigDecimal("27650.591"));
		TABLA_MORTALIDAD2.add(new BigDecimal("16719.455"));
		TABLA_MORTALIDAD2.add(new BigDecimal("9725.623"));
		TABLA_MORTALIDAD2.add(new BigDecimal("5429.271"));
		TABLA_MORTALIDAD2.add(new BigDecimal("2883.046"));
		TABLA_MORTALIDAD2.add(new BigDecimal("1447.624"));
		TABLA_MORTALIDAD2.add(new BigDecimal("682.456"));
		TABLA_MORTALIDAD2.add(new BigDecimal("299.501"));
		TABLA_MORTALIDAD2.add(new BigDecimal("121.081"));
		TABLA_MORTALIDAD2.add(new BigDecimal("44.505"));
		TABLA_MORTALIDAD2.add(new BigDecimal("14.624"));
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
		ModuloVRTAVITTest.LOG.debug("Inicializando pruebas");
		
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
			da2.setGvalor("03");
			
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
			
			TablaExperiencia te2 = new TablaExperiencia();
			te2.setKanacimiento("1961");
			te2.setKtabla(741);
			te2.setKsobremort(BigDecimal.ZERO);
			te2.setKsobreries(BigDecimal.ZERO);
			te2.setK2tipovalor("2");
			te2.setGvalor(TABLA_MORTALIDAD2);
			
			tablaExerienciaDao.put(te.getKey(), te);
			tablaExerienciaDao.put(te2.getKey(), te2);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(740);
			cte.setK2tipotabla("3");
			
			CabeceraTablaExperiencia cte2 = new CabeceraTablaExperiencia();
			
			cte2.setKtabla(741);
			cte2.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
			cabeceraTablaExperienciaDao.put(cte2.getKey(), cte2);
		}catch (Exception e){
			ModuloVRTAVITTest.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloVRTAVITTest(){
		Modulo moduloVRTAVIT = FactoriaModulos.getModulo(ConstantsFactorias.MODULO_VRTAVIT);
				
		try{
			BigDecimal vrtaVIT = (BigDecimal) moduloVRTAVIT.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(vrtaVIT);

			TestCase.assertEquals(BigDecimal.valueOf(0.980040894), vrtaVIT.setScale(9, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloVRTAVITTest.LOG.debug(e.getMessage(), e);
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
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2025, 4, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2013, 7, 1, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinirent = new Timestamp(new GregorianCalendar(2023, 5, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(2023, 5, 30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecnacAseg1 = new Timestamp(new GregorianCalendar(1960, 5, 16, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecnacAseg2 = new Timestamp(new GregorianCalendar(1961, 4, 25, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		primas.setIprimanetaini(BigDecimal.valueOf(12380.02));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		datosGenerales.setCtipoaport("U");
		datosGenerales.setFecCierre(FCIERRE);
		bti.setPintertecnI1(BigDecimal.valueOf(3));
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		rentas.setNpergaran(10);
		rentas.setForpagrent(12);
		rentas.setFecIni(fecinirent);
		rentas.setPreversion(BigDecimal.valueOf(60));
		asegurados.setFnacAseg1(fecnacAseg1);
		asegurados.setCsexAseg1("H");
		asegurados.setEdadAseg1(53);
		asegurados.setFnacAseg2(fecnacAseg2);
		asegurados.setCsexAseg2("M");
		asegurados.setEdadAseg2(52);
		
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setRentas(rentas);
		umic.setAsegurados(asegurados);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		
		detalleCorriente.setFechaDesde(FDESDE);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
		proyUmic.add(detalleCorriente);
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
		List<BigDecimal> itcalc = new ArrayList<BigDecimal>();
		itcalc.add(BigDecimal.valueOf(3.15));
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("740");
		tc.setTablaFin("740");
		TablaConversion tc2 = new TablaConversion();
		tc2.setTablaInicio("741");
		tc2.setTablaFin("741");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<TablaConversion> tabCon2 = new ArrayList<TablaConversion>();
		tabCon2.add(tc2);
		tabCon2.add(tc2);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		tca.add(tabCon2);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setFecCierre(FCIERRE);
		btcUmic.setIndTabExp('T');
		btcUmic.setTablacalc1aseg1("740");
		btcUmic.setTablacalc1aseg2("741");
		btcUmic.setTablasConversionAsegurado(tca);
		
		return btcUmic;
	}
	
	
}