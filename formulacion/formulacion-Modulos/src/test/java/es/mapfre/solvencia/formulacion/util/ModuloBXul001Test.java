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
import es.mapfre.solvencia.dominio.maestro.Capitales;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Duraciones;
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

/**
 * Clase que testea el Módulo BXul001, módulo que calcula el importe nominal para la proyección de Provisión Matemática 
 * por Fórmula Cerrada.
 * @author apedro
 *
 */

public class ModuloBXul001Test{
	
	private static final Logger LOG = LoggerFactory.getLogger(ModuloBXul001Test.class);
	
	private static final Timestamp FCALC = new Timestamp(new GregorianCalendar(2015, 04, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDESDE = new Timestamp(new GregorianCalendar(2015, 04, 01, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FDEVENGO = new Timestamp(new GregorianCalendar(2015, 04,15, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2015, 03,30, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
	private static final int iteracion = 1;
	private static final  Map<String, Object> mapVariables = new HashMap<String, Object>();
	private static final String codigoSubproceso = "PROY_PRV";
	
	public static final int cartera = 1101;
	public static final int modalidad = 487;
	public static final int garantia = 1;
	public static final String negocio = "I";

	
	private static final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
	static {
		TABLA_MORTALIDAD.add(new BigDecimal("1000000"));
		TABLA_MORTALIDAD.add(new BigDecimal("994193"));
		TABLA_MORTALIDAD.add(new BigDecimal("993777.4273"));
		TABLA_MORTALIDAD.add(new BigDecimal("993430.599"));
		TABLA_MORTALIDAD.add(new BigDecimal("993145.4844"));
		TABLA_MORTALIDAD.add(new BigDecimal("992911.1021"));
		TABLA_MORTALIDAD.add(new BigDecimal("992712.5199"));
		TABLA_MORTALIDAD.add(new BigDecimal("992536.8098"));
		TABLA_MORTALIDAD.add(new BigDecimal("992373.0412"));
		TABLA_MORTALIDAD.add(new BigDecimal("992215.2539"));
		TABLA_MORTALIDAD.add(new BigDecimal("992057.4916"));
		TABLA_MORTALIDAD.add(new BigDecimal("991889.8339"));
		TABLA_MORTALIDAD.add(new BigDecimal("991700.383"));
		TABLA_MORTALIDAD.add(new BigDecimal("991470.3085"));
		TABLA_MORTALIDAD.add(new BigDecimal("991178.8162"));
		TABLA_MORTALIDAD.add(new BigDecimal("990803.1594"));
		TABLA_MORTALIDAD.add(new BigDecimal("990321.6291"));
		TABLA_MORTALIDAD.add(new BigDecimal("989723.4748"));
		TABLA_MORTALIDAD.add(new BigDecimal("989010.8739"));
		TABLA_MORTALIDAD.add(new BigDecimal("988299.7751"));
		TABLA_MORTALIDAD.add(new BigDecimal("987576.3397"));
		TABLA_MORTALIDAD.add(new BigDecimal("986837.6326"));
		TABLA_MORTALIDAD.add(new BigDecimal("986082.7018"));
		TABLA_MORTALIDAD.add(new BigDecimal("985308.6269"));
		TABLA_MORTALIDAD.add(new BigDecimal("984517.424"));
		TABLA_MORTALIDAD.add(new BigDecimal("983711.1043"));
		TABLA_MORTALIDAD.add(new BigDecimal("982894.6241"));
		TABLA_MORTALIDAD.add(new BigDecimal("982077.8386"));
		TABLA_MORTALIDAD.add(new BigDecimal("981269.5886"));
		TABLA_MORTALIDAD.add(new BigDecimal("980477.704"));
		TABLA_MORTALIDAD.add(new BigDecimal("979708.029"));
		TABLA_MORTALIDAD.add(new BigDecimal("978956.5929"));
		TABLA_MORTALIDAD.add(new BigDecimal("978217.4807"));
		TABLA_MORTALIDAD.add(new BigDecimal("977478.9265"));
		TABLA_MORTALIDAD.add(new BigDecimal("976722.3578"));
		TABLA_MORTALIDAD.add(new BigDecimal("975923.3989"));
		TABLA_MORTALIDAD.add(new BigDecimal("975056.779"));
		TABLA_MORTALIDAD.add(new BigDecimal("974107.0737"));
		TABLA_MORTALIDAD.add(new BigDecimal("973064.7791"));
		TABLA_MORTALIDAD.add(new BigDecimal("971926.2933"));
		TABLA_MORTALIDAD.add(new BigDecimal("970688.0592"));
		TABLA_MORTALIDAD.add(new BigDecimal("969339.7735"));
		TABLA_MORTALIDAD.add(new BigDecimal("967856.6836"));
		TABLA_MORTALIDAD.add(new BigDecimal("966201.6487"));
		TABLA_MORTALIDAD.add(new BigDecimal("964339.7781"));
		TABLA_MORTALIDAD.add(new BigDecimal("962244.2678"));
		TABLA_MORTALIDAD.add(new BigDecimal("959897.354"));
		TABLA_MORTALIDAD.add(new BigDecimal("957279.7139"));
		TABLA_MORTALIDAD.add(new BigDecimal("954361.9254"));
		TABLA_MORTALIDAD.add(new BigDecimal("951123.7754"));
		TABLA_MORTALIDAD.add(new BigDecimal("947534.2342"));
		TABLA_MORTALIDAD.add(new BigDecimal("943566.9084"));
		TABLA_MORTALIDAD.add(new BigDecimal("939194.4193"));
		TABLA_MORTALIDAD.add(new BigDecimal("934412.0414"));
		TABLA_MORTALIDAD.add(new BigDecimal("929205.4975"));
		TABLA_MORTALIDAD.add(new BigDecimal("923542.9192"));
		TABLA_MORTALIDAD.add(new BigDecimal("917377.3466"));
		TABLA_MORTALIDAD.add(new BigDecimal("910680.492"));
		TABLA_MORTALIDAD.add(new BigDecimal("903432.386"));
		TABLA_MORTALIDAD.add(new BigDecimal("895661.0606"));
		TABLA_MORTALIDAD.add(new BigDecimal("887409.3352"));
		TABLA_MORTALIDAD.add(new BigDecimal("878718.9356"));
		TABLA_MORTALIDAD.add(new BigDecimal("869624.1946"));
		TABLA_MORTALIDAD.add(new BigDecimal("860152.2479"));
		TABLA_MORTALIDAD.add(new BigDecimal("850302.6445"));
		TABLA_MORTALIDAD.add(new BigDecimal("840055.6473"));
		TABLA_MORTALIDAD.add(new BigDecimal("829384.4204"));
		TABLA_MORTALIDAD.add(new BigDecimal("817724.1049"));
		TABLA_MORTALIDAD.add(new BigDecimal("804915.2745"));
		TABLA_MORTALIDAD.add(new BigDecimal("790779.3525"));
		TABLA_MORTALIDAD.add(new BigDecimal("775116.3858"));
		TABLA_MORTALIDAD.add(new BigDecimal("757707.2718"));
		TABLA_MORTALIDAD.add(new BigDecimal("738306.1771"));
		TABLA_MORTALIDAD.add(new BigDecimal("716633.9376"));
		TABLA_MORTALIDAD.add(new BigDecimal("692388.0616"));
		TABLA_MORTALIDAD.add(new BigDecimal("665245.0648"));
		TABLA_MORTALIDAD.add(new BigDecimal("634885.2758"));
		TABLA_MORTALIDAD.add(new BigDecimal("601017.3207"));
		TABLA_MORTALIDAD.add(new BigDecimal("563420.6822"));
		TABLA_MORTALIDAD.add(new BigDecimal("521991.2326"));
		TABLA_MORTALIDAD.add(new BigDecimal("476814.4574"));
		TABLA_MORTALIDAD.add(new BigDecimal("430652.1425"));
		TABLA_MORTALIDAD.add(new BigDecimal("384064.6244"));
		TABLA_MORTALIDAD.add(new BigDecimal("337712.633"));
		TABLA_MORTALIDAD.add(new BigDecimal("292318.314"));
		TABLA_MORTALIDAD.add(new BigDecimal("248621.4032"));
		TABLA_MORTALIDAD.add(new BigDecimal("207338.0678"));
		TABLA_MORTALIDAD.add(new BigDecimal("169147.4324"));
		TABLA_MORTALIDAD.add(new BigDecimal("134654.0422"));
		TABLA_MORTALIDAD.add(new BigDecimal("104336.6846"));
		TABLA_MORTALIDAD.add(new BigDecimal("78492.0705"));
		TABLA_MORTALIDAD.add(new BigDecimal("57192.61924"));
		TABLA_MORTALIDAD.add(new BigDecimal("40273.72704"));
		TABLA_MORTALIDAD.add(new BigDecimal("27351.74062"));
		TABLA_MORTALIDAD.add(new BigDecimal("17881.41924"));
		TABLA_MORTALIDAD.add(new BigDecimal("11232.48172"));
		TABLA_MORTALIDAD.add(new BigDecimal("6767.008612"));
		TABLA_MORTALIDAD.add(new BigDecimal("3902.290254"));
		TABLA_MORTALIDAD.add(new BigDecimal("2149.494639"));
		TABLA_MORTALIDAD.add(new BigDecimal("1128.409453"));
		TABLA_MORTALIDAD.add(new BigDecimal("563.1688465"));
		TABLA_MORTALIDAD.add(new BigDecimal("266.4869928"));
		TABLA_MORTALIDAD.add(new BigDecimal("110.8913669"));
		TABLA_MORTALIDAD.add(new BigDecimal("42.2777772"));
		TABLA_MORTALIDAD.add(new BigDecimal("14.592217"));
		TABLA_MORTALIDAD.add(new BigDecimal("4.491353"));
		TABLA_MORTALIDAD.add(new BigDecimal("1.2089015"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.2771576"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.0521377"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.0075995"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.0007771"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.0000448"));
		TABLA_MORTALIDAD.add(new BigDecimal("0.0000005"));
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
	
	private static final List<BigDecimal> TABLA_VALORES_QX = new ArrayList<BigDecimal>();
	static {
		TABLA_VALORES_QX.add(new BigDecimal("0.005807"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000418"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000349"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000287"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000236"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0002"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000177"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000165"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000159"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000159"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000169"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000191"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000232"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000294"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000379"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000486"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000604"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00072"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000719"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000732"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000748"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000765"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000803"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000819"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00083"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000831"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000823"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000807"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000785"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000767"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000755"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000755"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000774"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000818"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000888"));
		TABLA_VALORES_QX.add(new BigDecimal("0.000974"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00107"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00117"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001274"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001389"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00153"));
		TABLA_VALORES_QX.add(new BigDecimal("0.00171"));
		TABLA_VALORES_QX.add(new BigDecimal("0.001927"));
		TABLA_VALORES_QX.add(new BigDecimal("0.002173"));
		TABLA_VALORES_QX.add(new BigDecimal("0.002439"));
		TABLA_VALORES_QX.add(new BigDecimal("0.002727"));
		TABLA_VALORES_QX.add(new BigDecimal("0.003048"));
		TABLA_VALORES_QX.add(new BigDecimal("0.003393"));
		TABLA_VALORES_QX.add(new BigDecimal("0.003774"));
		TABLA_VALORES_QX.add(new BigDecimal("0.004187"));
		TABLA_VALORES_QX.add(new BigDecimal("0.004634"));
		TABLA_VALORES_QX.add(new BigDecimal("0.005092"));
		TABLA_VALORES_QX.add(new BigDecimal("0.005572"));
		TABLA_VALORES_QX.add(new BigDecimal("0.006094"));
		TABLA_VALORES_QX.add(new BigDecimal("0.006676"));
		TABLA_VALORES_QX.add(new BigDecimal("0.0073"));
		TABLA_VALORES_QX.add(new BigDecimal("0.007959"));
		TABLA_VALORES_QX.add(new BigDecimal("0.008602"));
		TABLA_VALORES_QX.add(new BigDecimal("0.009213"));
		TABLA_VALORES_QX.add(new BigDecimal("0.009793"));
		TABLA_VALORES_QX.add(new BigDecimal("0.01035"));
		TABLA_VALORES_QX.add(new BigDecimal("0.010892"));
		TABLA_VALORES_QX.add(new BigDecimal("0.011451"));
		TABLA_VALORES_QX.add(new BigDecimal("0.012051"));
		TABLA_VALORES_QX.add(new BigDecimal("0.012703"));
		TABLA_VALORES_QX.add(new BigDecimal("0.014059"));
		TABLA_VALORES_QX.add(new BigDecimal("0.015664"));
		TABLA_VALORES_QX.add(new BigDecimal("0.017562"));
		TABLA_VALORES_QX.add(new BigDecimal("0.019807"));
		TABLA_VALORES_QX.add(new BigDecimal("0.02246"));
		TABLA_VALORES_QX.add(new BigDecimal("0.025605"));
		TABLA_VALORES_QX.add(new BigDecimal("0.029354"));
		TABLA_VALORES_QX.add(new BigDecimal("0.033833"));
		TABLA_VALORES_QX.add(new BigDecimal("0.039202"));
		TABLA_VALORES_QX.add(new BigDecimal("0.045637"));
		TABLA_VALORES_QX.add(new BigDecimal("0.053345"));
		TABLA_VALORES_QX.add(new BigDecimal("0.062555"));
		TABLA_VALORES_QX.add(new BigDecimal("0.073532"));
		TABLA_VALORES_QX.add(new BigDecimal("0.086547"));
		TABLA_VALORES_QX.add(new BigDecimal("0.096814"));
		TABLA_VALORES_QX.add(new BigDecimal("0.108179"));
		TABLA_VALORES_QX.add(new BigDecimal("0.120688"));
		TABLA_VALORES_QX.add(new BigDecimal("0.134417"));
		TABLA_VALORES_QX.add(new BigDecimal("0.149484"));
		TABLA_VALORES_QX.add(new BigDecimal("0.166049"));
		TABLA_VALORES_QX.add(new BigDecimal("0.184195"));
		TABLA_VALORES_QX.add(new BigDecimal("0.203925"));
		TABLA_VALORES_QX.add(new BigDecimal("0.22515"));
		TABLA_VALORES_QX.add(new BigDecimal("0.247704"));
		TABLA_VALORES_QX.add(new BigDecimal("0.271358"));
		TABLA_VALORES_QX.add(new BigDecimal("0.295823"));
		TABLA_VALORES_QX.add(new BigDecimal("0.320854"));
		TABLA_VALORES_QX.add(new BigDecimal("0.346242"));
		TABLA_VALORES_QX.add(new BigDecimal("0.371835"));
		TABLA_VALORES_QX.add(new BigDecimal("0.39755"));
		TABLA_VALORES_QX.add(new BigDecimal("0.423336"));
		TABLA_VALORES_QX.add(new BigDecimal("0.449171"));
		TABLA_VALORES_QX.add(new BigDecimal("0.475035"));
		TABLA_VALORES_QX.add(new BigDecimal("0.500918"));
		TABLA_VALORES_QX.add(new BigDecimal("0.526808"));
		TABLA_VALORES_QX.add(new BigDecimal("0.583877"));
		TABLA_VALORES_QX.add(new BigDecimal("0.618746"));
		TABLA_VALORES_QX.add(new BigDecimal("0.654849"));
		TABLA_VALORES_QX.add(new BigDecimal("0.692209"));
		TABLA_VALORES_QX.add(new BigDecimal("0.730838"));
		TABLA_VALORES_QX.add(new BigDecimal("0.770736"));
		TABLA_VALORES_QX.add(new BigDecimal("0.811884"));
		TABLA_VALORES_QX.add(new BigDecimal("0.854241"));
		TABLA_VALORES_QX.add(new BigDecimal("0.897733"));
		TABLA_VALORES_QX.add(new BigDecimal("0.942245"));
		TABLA_VALORES_QX.add(new BigDecimal("0.987609"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));
		TABLA_VALORES_QX.add(new BigDecimal("1"));

	
	}
	
	@BeforeClass
	public static void init() {
		ModuloBXul001Test.LOG.debug("Inicializando pruebas");
		
		try{
			DefinicionesAuxiliaresDao definicionesAuxiliaresDao = new DefinicionesAuxiliaresDao();
			DefinicionesAuxiliares da = new DefinicionesAuxiliares();
			da.setCnegocio(negocio);
			da.setKcarteorig(cartera);
			da.setKmodalidad(modalidad);
			da.setKgarantia(garantia);
			da.setCidentivariab("IFAL");
			da.setGvariable("IFAL");
			da.setGvalor("1");
			
			DefinicionesAuxiliares da2 = new DefinicionesAuxiliares();
			da2.setCnegocio(negocio);
			da2.setKcarteorig(cartera);
			da2.setKmodalidad(modalidad);
			da2.setKgarantia(garantia);
			da2.setGvariable("ID-TEMPORAL");
			da2.setCidentivariab("ID-TEMPORAL");
			da2.setGvalor("01");
			
			DefinicionesAuxiliares da3 = new DefinicionesAuxiliares();
			da3.setCnegocio(negocio);
			da3.setKcarteorig(cartera);
			da3.setKmodalidad(modalidad);
			da3.setKgarantia(garantia);
			da3.setCidentivariab("ID-CRITERIO");
			da3.setGvariable("ID-CRITERIO");
			da3.setGvalor("03");
			
			definicionesAuxiliaresDao.put(da.getKey(), da);
			definicionesAuxiliaresDao.put(da2.getKey(), da2);
			definicionesAuxiliaresDao.put(da3.getKey(), da3);
			
			TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
			TablaExperiencia te = new TablaExperiencia();
			te.setKanacimiento("1976");
			te.setKtabla(370);
			te.setKsobremort(BigDecimal.ZERO);
			te.setKsobreries(BigDecimal.ZERO);
			te.setK2tipovalor("2");
			te.setGvalor(TABLA_MORTALIDAD);
			
			TablaExperiencia te2 = new TablaExperiencia();
			te2.setKanacimiento("1976");
			te2.setKtabla(370);
			te2.setKsobremort(BigDecimal.ZERO);
			te2.setKsobreries(BigDecimal.ZERO);
			te2.setK2tipovalor("1");
			te2.setGvalor(TABLA_VALORES_QX);
			
			tablaExerienciaDao.put(te.getKey(), te);
			tablaExerienciaDao.put(te2.getKey(), te2);
			
			CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
			CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
			
			cte.setKtabla(370);
			cte.setK2tipotabla("3");
			
			cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
		}catch (Exception e){
			ModuloBXul001Test.LOG.debug(e.getMessage(), e);
			fail();
		}
	}
	
	@Test
	public void moduloBXul001Test(){
		Modulo moduloBXul001= FactoriaModulos.getModulo(ConstantsFactorias.MODULO_BXul001);
				
		try{
			TotalFlujoProyeccion BXul001 = (TotalFlujoProyeccion) moduloBXul001.execute(getProyUmic(), getBloqueCorriente(), iteracion, FCALC, getUmic(), getBtcUmic(), mapVariables, codigoSubproceso);
			System.out.println(BXul001.getProvbtiproy());
			
			TestCase.assertEquals(BigDecimal.valueOf(4801.43735904), BXul001.getProvbtiproy().setScale(8, RoundingMode.HALF_DOWN));
		}catch (Exception e){
			ModuloBXul001Test.LOG.debug(e.getMessage(), e);
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
		Duraciones duraciones = new Duraciones();
		Rentas rentas = new Rentas();
		Capitales capitales = new Capitales();
		
		Timestamp fecefecfin = new Timestamp(new GregorianCalendar(2018, 9, 22, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecinisus = new Timestamp(new GregorianCalendar(2013, 8, 2, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fnacAseg1 = new Timestamp(new GregorianCalendar(1976, 10, 17, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecIni = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		Timestamp fecefecred = new Timestamp(new GregorianCalendar(0, 0, 0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis());
		
		fechas.setFecefecfin(fecefecfin);
		fechas.setFecinisus(fecinisus);
		fechas.setFecefecred(fecefecred);
		fechas.setFecefecini(fecinisus);
		primas.setIprimanetaini(BigDecimal.valueOf(4737.33));
		datosGenerales.setCcartera(cartera);
		datosGenerales.setKmodalidad(modalidad);
		datosGenerales.setKgarantia(garantia);
		datosGenerales.setCnegocio(negocio);
		datosGenerales.setCsitupol("VI");
		bti.setPintertecnI1(BigDecimal.valueOf(1.95));
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		asegurados.setFnacAseg1(fnacAseg1);
		asegurados.setCsexAseg1("H");
		duraciones.setNdursegano(5);
		duraciones.setNdursegmes(1);
		duraciones.setNdursegdia(20);
		rentas.setFecIni(fecIni);
		capitales.setIcapact(BigDecimal.valueOf(5055.22));
		capitales.setIcapini(BigDecimal.valueOf(5055.22));
		
		umic.setFechas(fechas);
		umic.setPrimas(primas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		umic.setAsegurados(asegurados);
		umic.setDuraciones(duraciones);
		umic.setRentas(rentas);
		umic.setCapitales(capitales);
		
		return umic;
	}
	
	private List<DetalleCorriente> getProyUmic(){
		DetalleCorriente detalleCorriente = new DetalleCorriente();
		List<DetalleCorriente> proyUmic = new ArrayList<DetalleCorriente>();
		BloqueCorriente bloqueCorriente = new BloqueCorriente();
		
		bloqueCorriente.setImpFlujoNominal(BigDecimal.valueOf(11783.56786));
		bloqueCorriente.setFechaDevengo(FDEVENGO);
		detalleCorriente.setBloqueFall(bloqueCorriente);
		detalleCorriente.setFechaDesde(FDESDE);
		
		DetalleCorriente detalleCorriente2 = new DetalleCorriente();
		detalleCorriente2.setBloqueVida(bloqueCorriente);
		detalleCorriente2.setFechaDesde(new Timestamp(new GregorianCalendar(2015, 4, 22, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0, ConstantsFunciones.CTE_0).getTimeInMillis()));
		
		
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
		itcalc.add(BigDecimal.valueOf(1.95));
		TablaConversion tc = new TablaConversion();
		tc.setTablaInicio("370");
		tc.setTablaFin("370");
		List<TablaConversion> tabCon = new ArrayList<TablaConversion>();
		tabCon.add(tc);
		tabCon.add(tc);
		List<List<TablaConversion>> tca = new ArrayList<List<TablaConversion>>();
		tca.add(tabCon);
		
		btcUmic.setBaseTec("BTI");
		btcUmic.setItcalc(itcalc);
		btcUmic.setTablacalc1aseg1("370");
		btcUmic.setGtorosspPrima(BigDecimal.valueOf(2.25));
		btcUmic.setIndTabExp('T');
		btcUmic.setFecCierre(FCIERRE);
		btcUmic.setTablasConversionAsegurado(tca);
		
		
		return btcUmic;
	}
	
	
}