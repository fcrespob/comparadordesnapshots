package es.mapfre.solvencia.servicios.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dao.impl.PropertiesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.CabeceraTablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.DefinicionesAuxiliaresDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FlujosProbablesDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.IPCGeneralFuturoDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.TablaExperienciaDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.ValoresConstantesRescatesDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
//import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;
import es.mapfre.solvencia.dominio.maestro.BaseTecnicaInicial;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.BloqueFlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.TablaConversion;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;



public class ObtenerConfiguracionTest {

	private static Logger log = LoggerFactory.getLogger(ObtenerConfiguracion.class);
	
//	private static final String BASETEC = "basetec";
//	private static final String CAR = "car";
//	
//	private ValoresCurvaTipo createValoresCurvaTipo(String codCurvaTipos, Timestamp fecha){
//		
//		ValoresCurvaTipo vct = new ValoresCurvaTipo();
//		
//		Timestamp fechaEfec = new Timestamp(fecha.getTime()-(1000*60*60*12));
//		
//		vct.setFecEfecCurva(fechaEfec);
//		vct.setCodCurvaTipos(codCurvaTipos);
//		vct.setPorcentajeInteres(new java.math.BigDecimal(new Double(0.05 * System.currentTimeMillis()).toString()));
//		
//		return vct;
//	}
	
	private DefinicionesAuxiliares createVariableApoyo(Integer index, Boolean identidadEstatica){
		DefinicionesAuxiliares defAux = new DefinicionesAuxiliares();
		defAux.setKcarteorig(index);
		defAux.setCnegocio("C");
		defAux.setKmodalidad(index);
		defAux.setKgarantia(index);
		defAux.setCidentivariab("Cidentivariab"+(identidadEstatica ? "" : index));
		defAux.setGvariable("Gvariable"+index);
		defAux.setGvalor("Gvalor"+index);
		return defAux;
	}
	
	@Test
	public void recuperarDefinicionAuxiliarTest(){
		
		DefinicionesAuxiliaresDao dao = new DefinicionesAuxiliaresDao();
		
		DefinicionesAuxiliares var = createVariableApoyo(1, Boolean.TRUE);
		dao.put(var.getKey(), var);
		
		DefinicionesAuxiliares varGenerica = createVariableApoyo(1, Boolean.TRUE);
		varGenerica.setKcarteorig(Integer.valueOf(0));
		varGenerica.setKmodalidad(Integer.valueOf(0));
		varGenerica.setKgarantia(Integer.valueOf(0));
		dao.put(varGenerica.getKey(), varGenerica);
		
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		Object varRecuperada = servicio.recuperarDefinicionAuxiliar(var.getKcarteorig(), var.getKmodalidad(), var.getKgarantia(), var.getKbasetec(), var.getCidentivariab());

		log.debug(var.getGvalor());
		log.debug(varRecuperada.toString());
		
		TestCase.assertEquals(var.getGvalor(), varRecuperada);
		
		// Sólo hemos cargado una variable de apoyo, el resto tirará de ella. 
		DefinicionesAuxiliares var2 = createVariableApoyo(2, Boolean.TRUE);
		Object varRecuperada2 = servicio.recuperarDefinicionAuxiliar(var2.getKcarteorig(), var2.getKmodalidad(), var2.getKgarantia(), var2.getKbasetec(), var2.getCidentivariab());

		TestCase.assertEquals(var.getGvalor(), varRecuperada2);

		log.debug(varRecuperada2.toString());

		DefinicionesAuxiliares var3 = createVariableApoyo(2, Boolean.TRUE);
		Object varRecuperada3 = servicio.recuperarDefinicionAuxiliar(var3.getKcarteorig(), var3.getKmodalidad(), var3.getKgarantia(), var3.getKbasetec(), var3.getCidentivariab());

		TestCase.assertEquals(var.getGvalor(), varRecuperada3);

		log.debug(varRecuperada3.toString());

		dao.clear();
	}
	
	@Test
	public void validarCodBaseTecnicaTest() throws IOException{
		
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		PropertiesDao propertiesDao = new PropertiesDao();
		propertiesDao.addProperties("test.properties");
		
		TestCase.assertTrue(servicio.validarCodBaseTecnica("BTI"));
		TestCase.assertTrue(servicio.validarCodBaseTecnica("BEL"));
		TestCase.assertTrue(servicio.validarCodBaseTecnica("ROSSP"));
		TestCase.assertFalse(servicio.validarCodBaseTecnica("ESTANO"));
		
	}
	
	
	private IPCGeneralFuturo createIPCGeneralFuturo(Integer index, Timestamp date){
		
		IPCGeneralFuturo ipcGeneral = new IPCGeneralFuturo();
		
		long after = date.getTime()+(index*24*60*60*1000); 
		long before = date.getTime() - (index*24*60*60*1000); 
		
		ipcGeneral.setFfin(new Timestamp(after));
		ipcGeneral.setFinicio(new Timestamp(before));
		ipcGeneral.setPipcgas(new java.math.BigDecimal(new Double(index*0.5).toString()));
		ipcGeneral.setPipcleg(new java.math.BigDecimal(new Double(index*-0.5).toString()));
		
		return ipcGeneral;
	}
	
	
	@Test
	public void recuperarIpcFuturoTest(){
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		
		IPCGeneralFuturoDao dao = new IPCGeneralFuturoDao();
		
		IPCGeneralFuturo ipcGeneral = createIPCGeneralFuturo(1,new Timestamp(System.currentTimeMillis()));
		dao.put(ipcGeneral.getKey(), ipcGeneral);
		
		IPCGeneralFuturo ipcGeneral2 = createIPCGeneralFuturo(2,new Timestamp(0));
		dao.put(ipcGeneral.getKey(), ipcGeneral2);
		
		java.math.BigDecimal resultado = servicio.recuperarIpcFuturo(new Timestamp(0),"");
		
		TestCase.assertNotNull(resultado);
		TestCase.assertEquals(ipcGeneral2.getPipcgas(), resultado);
		
		dao.clear();
		
	}
	
	
	private CabeceraTablaExperiencia createCabeceraTablaExperiencia(Integer ktabla, Timestamp fecha){
		
		long dia = 1000*60*60*24;
		
		CabeceraTablaExperiencia cabeceraTablaExp = new CabeceraTablaExperiencia();
		
		cabeceraTablaExp.setcUsuario("cUsuario"+ktabla);
		cabeceraTablaExp.setFecAlta(new Timestamp(fecha.getTime()-(ktabla*dia)));
		cabeceraTablaExp.setFecAnula( new Timestamp(fecha.getTime()+(ktabla*dia)) );
		cabeceraTablaExp.setFecModif(fecha);
		cabeceraTablaExp.setGcorta("gcorta"+ktabla);
		cabeceraTablaExp.setGdeslarga("gdeslarga"+ktabla);
		cabeceraTablaExp.setK2tipotabla(ktabla.toString());
		cabeceraTablaExp.setKtabla(ktabla);
		
		return cabeceraTablaExp;
	}
	
	private TablaExperiencia createTablaExperiencia(Integer ktabla,String k2tipovalor, Timestamp fecha){
		
		TablaExperiencia tablaExperiencia = new TablaExperiencia();
		
		tablaExperiencia.setCusuario("cusuario"+ktabla);
		tablaExperiencia.setFanulacion(new Timestamp(Long.MAX_VALUE));
		tablaExperiencia.setFmodificacion(fecha);
		
		List<java.math.BigDecimal> gvalor = new ArrayList<java.math.BigDecimal>();
		for(int i=0;i < 131; i++){
			gvalor.add(new java.math.BigDecimal(new Double ((130.0) - i).toString()) );
		}
		
		tablaExperiencia.setGvalor(gvalor);
		tablaExperiencia.setGvaloreslen(ktabla);
		tablaExperiencia.setKanacimiento("kanacimiento"+ktabla);
		tablaExperiencia.setKinteres(new java.math.BigDecimal(ktabla+"0.07"));
		tablaExperiencia.setKsobremort(new java.math.BigDecimal(ktabla+"0.07"));
		tablaExperiencia.setKsobreries(new java.math.BigDecimal(ktabla+"0.07"));
		tablaExperiencia.setKtabla(ktabla);
		
		if (ktabla.toString().equals(ConstantesSolvencia.TIPO_TABLA_TRADICIONAL)) {
			tablaExperiencia.setKinteres(BigDecimal.ZERO);
			tablaExperiencia.setK2tipovalor("2");

		} else if (ktabla.toString().equals(ConstantesSolvencia.TIPO_TABLA_GENERACIONAL)) {
			tablaExperiencia.setK2tipovalor("2");

		} else if (ktabla.toString().equals(ConstantesSolvencia.TIPO_TABLA_COMPLEMENTARIA)) {
			tablaExperiencia.setK2tipovalor("11");
		}
		
		
		return tablaExperiencia;
	}
	
	@Test
	public void recuperarLxTest() throws Exception{
		
		CabeceraTablaExperienciaDao cabecerasDao = new CabeceraTablaExperienciaDao();
		cabecerasDao.clear();
		TablaExperienciaDao tablaDao = new TablaExperienciaDao();
		tablaDao.clear();
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		
		//TRADICIONAL
		CabeceraTablaExperiencia cabeceraTradicional = createCabeceraTablaExperiencia(1,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraTradicional.getKey(), cabeceraTradicional);
		
		TablaExperiencia tablaTradicional = createTablaExperiencia(1,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaTradicional.getKey(), tablaTradicional);
		
		//GENERACIONAL
		CabeceraTablaExperiencia cabeceraGeneracional = createCabeceraTablaExperiencia(2,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraGeneracional.getKey(), cabeceraGeneracional);
		
		TablaExperiencia tablaGeneracional = createTablaExperiencia(3,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaGeneracional.getKey(), tablaGeneracional);
		
		//COMPLEMENTARIAS
		CabeceraTablaExperiencia cabeceraComplementarias = createCabeceraTablaExperiencia(3,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraComplementarias.getKey(), cabeceraComplementarias);
		
		TablaExperiencia tablaComplementarias = createTablaExperiencia(2,"11",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaComplementarias.getKey(), tablaComplementarias);
		
		int kedad = 5;
		
		java.math.BigDecimal var1 = tablaTradicional.getGvalor().get(kedad);
		java.math.BigDecimal var2 = servicio.recuperarLx(new Timestamp(System.currentTimeMillis()), tablaTradicional.getKtabla(), tablaTradicional.getKanacimiento()
													, tablaTradicional.getKinteres(), tablaTradicional.getKsobremort(), tablaTradicional.getKsobreries()
													, kedad);
		
		TestCase.assertTrue(var1.equals(var2));
		
		var1 = tablaGeneracional.getGvalor().get(kedad);
	    var2 =servicio.recuperarLx(new Timestamp(System.currentTimeMillis()), tablaGeneracional.getKtabla(), tablaGeneracional.getKanacimiento(), null, null, null, kedad);
	    TestCase.assertTrue(var1.equals(var2));
		
		
		var1 = tablaComplementarias.getGvalor().get(kedad);
		var2 = servicio.recuperarLx(new Timestamp(System.currentTimeMillis()), tablaComplementarias.getKtabla(), null, null, null, null, kedad);
		
		TestCase.assertTrue(var1.equals(var2));
		
		tablaDao.clear();
		cabecerasDao.clear();
	}
	
	public void iniciar(){
		
	}
	
	//@Test
	public void recuperarEdadMaxTest() throws Exception{
		
		CabeceraTablaExperienciaDao cabecerasDao = new CabeceraTablaExperienciaDao();
		TablaExperienciaDao tablaDao = new TablaExperienciaDao();
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		
		//TRADICIONAL
		CabeceraTablaExperiencia cabeceraTradicional = createCabeceraTablaExperiencia(1,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraTradicional.getKey(), cabeceraTradicional);
		
		TablaExperiencia tablaTradicional = createTablaExperiencia(1,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaTradicional.getKey(), tablaTradicional);
		
		//GENERACIONAL
		CabeceraTablaExperiencia cabeceraGeneracional = createCabeceraTablaExperiencia(2,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraGeneracional.getKey(), cabeceraGeneracional);
		
		TablaExperiencia tablaGeneracional = createTablaExperiencia(3,"11",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaGeneracional.getKey(), tablaGeneracional);
		
		//COMPLEMENTARIAS
		CabeceraTablaExperiencia cabeceraComplementarias = createCabeceraTablaExperiencia(3,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraComplementarias.getKey(), cabeceraComplementarias);
		
		TablaExperiencia tablaComplementarias = createTablaExperiencia(2,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaComplementarias.getKey(), tablaComplementarias);
		
		Umic umic = new Umic();
		umic.setDatosGenerales(new DatosGenerales());
		umic.getDatosGenerales().setFecCierre(new Timestamp(System.currentTimeMillis()));
		umic.setFechas(new Fechas());
		umic.setBti(new BaseTecnicaInicial());
		umic.getBti().setPriesgo(tablaTradicional.getKsobreries());
		umic.getBti().setPsobremort(tablaTradicional.getKsobremort());
		
		DetalleBaseTecnica detalleBaseTecnica = new DetalleBaseTecnica();
		detalleBaseTecnica.setBaseTec(ConstantesSolvencia.BASE_BTI);
		List<List<TablaConversion>> tabConv = new ArrayList<List<TablaConversion>>();
		
		detalleBaseTecnica.setTablasConversionAsegurado(tabConv);
		
		Integer var2 = servicio.recuperarEdadMax(umic, detalleBaseTecnica,  tablaTradicional.getKanacimiento(),"H",IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		TestCase.assertTrue(tablaTradicional.getGvalor().get(var2).compareTo(BigDecimal.ZERO)==0);
//		
//		
//	    var2 = servicio.recuperarEdadMax(new Timestamp(System.currentTimeMillis()), tablaGeneracional.getKtabla(), tablaGeneracional.getKanacimiento(), null, null, null);
//	    
//	    TestCase.assertTrue(tablaGeneracional.getGvalor().get(var2).compareTo(new java.math.BigDecimal("0"))==0);
//		
//		var2 = servicio.recuperarEdadMax(new Timestamp(System.currentTimeMillis()), tablaComplementarias.getKtabla(), null, null, null, null);
//		TestCase.assertTrue(tablaComplementarias.getGvalor().get(var2).compareTo(new java.math.BigDecimal("0"))==0);
		
		tablaDao.clear();
		cabecerasDao.clear();
	}
	
	//@Test
	public void recuperarValoresExperienciaTest() throws Exception{

		CabeceraTablaExperienciaDao cabecerasDao = new CabeceraTablaExperienciaDao();
		cabecerasDao.clear();
		
		TablaExperienciaDao tablaDao = new TablaExperienciaDao();
		tablaDao.clear();
		
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		
		//TRADICIONAL
		CabeceraTablaExperiencia cabeceraTradicional = createCabeceraTablaExperiencia(1,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraTradicional.getKey(), cabeceraTradicional);
		
		TablaExperiencia tablaTradicional = createTablaExperiencia(1,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaTradicional.getKey(), tablaTradicional);
		
		//GENERACIONAL
		CabeceraTablaExperiencia cabeceraGeneracional = createCabeceraTablaExperiencia(2,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraGeneracional.getKey(), cabeceraGeneracional);
		
		TablaExperiencia tablaGeneracional = createTablaExperiencia(2,"2",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaGeneracional.getKey(), tablaGeneracional);
		
		//COMPLEMENTARIAS
		CabeceraTablaExperiencia cabeceraComplementarias = createCabeceraTablaExperiencia(3,new Timestamp(System.currentTimeMillis()));
		cabecerasDao.put(cabeceraComplementarias.getKey(), cabeceraComplementarias);
		
		TablaExperiencia tablaComplementarias = createTablaExperiencia(3,"11",new Timestamp(System.currentTimeMillis()));
		tablaDao.put(tablaComplementarias.getKey(), tablaComplementarias);
		
		Umic umic1 = new Umic();
		umic1.setDatosGenerales(new DatosGenerales());
		umic1.getDatosGenerales().setFecCierre(new Timestamp(System.currentTimeMillis()));
		umic1.setFechas(new Fechas());
		umic1.setBti(new BaseTecnicaInicial());
		umic1.getBti().setPriesgo(tablaTradicional.getKsobreries());
		umic1.getBti().setPsobremort(tablaTradicional.getKsobremort());
		DetalleBaseTecnica detalleBaseTecnica = new DetalleBaseTecnica();
		detalleBaseTecnica.setBaseTec(ConstantesSolvencia.BASE_BTI);
		List<java.math.BigDecimal> var2 = servicio.recuperarValoresExperiencia(umic1, detalleBaseTecnica,  tablaTradicional.getKanacimiento(),"H", 23, "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		TestCase.assertTrue(tablaTradicional.getGvalor().equals(var2));
		
		Umic umic2 = new Umic();
		umic2.setDatosGenerales(new DatosGenerales());
		umic2.getDatosGenerales().setFecCierre(new Timestamp(System.currentTimeMillis()));
		umic2.setFechas(new Fechas());
		umic2.setBti(new BaseTecnicaInicial());		
		var2 = servicio.recuperarValoresExperiencia(umic2, detalleBaseTecnica, tablaGeneracional.getKanacimiento(),"H", 23, "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		TestCase.assertTrue(tablaGeneracional.getGvalor().equals(var2));
		
		Umic umic3 = new Umic();
		umic3.setDatosGenerales(new DatosGenerales());
		umic3.getDatosGenerales().setFecCierre(new Timestamp(System.currentTimeMillis()));
		umic3.setFechas(new Fechas());
		umic3.setBti(new BaseTecnicaInicial());
		var2 = servicio.recuperarValoresExperiencia(umic3, detalleBaseTecnica, null, null, 23, "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		
		TestCase.assertTrue(tablaComplementarias.getGvalor().equals(var2));
		
		tablaDao.clear();
		cabecerasDao.clear();
	}

	@Test
	public void recuperarConfProvTest() throws Exception {
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		
		FlujosProbables flujosProbables = new FlujosProbables();
		flujosProbables.setModalidad(1);
		flujosProbables.setGarantia(1);
		flujosProbables.setPrestacion("prestacion");
		flujosProbables.setBasetecnica("BTI");
				
		flujosProbablesDao.put(flujosProbables.getKey(), flujosProbables);
		
		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		
		FlujosProbables valor = obtenerConfiguracion.recuperarConfProv(1, 1, "prestacion", "BTI");
		
		TestCase.assertNotNull(valor);
	}
	
	@Test
	public void recuperarModuloTest() {
		FlujosProbablesDao flujosProbablesDao = new FlujosProbablesDao();
		
		FlujosProbables flujosProbables = new FlujosProbables();
		flujosProbables.setModalidad(1);
		flujosProbables.setGarantia(1);
		flujosProbables.setPrestacion("prestacion");
		flujosProbables.setBasetecnica("BTI");
		
		
		BloqueFlujosProbables bloque = new BloqueFlujosProbables();
		bloque.setNominal("NOMINAL");
		
		flujosProbables.setVida(bloque );
				
		flujosProbablesDao.put(flujosProbables.getKey(), flujosProbables);

		IObtenerConfiguracion obtenerConfiguracion = FachadaServicios.getObtenerConfiguracion();
		
		String modulo = obtenerConfiguracion.recuperarModulo(1, 1,"prestacion", "BTI", ConstantesSolvencia.CTE_PROY_VIDA, ConstantesSolvencia.CTE_ELEMENTO_NOMINAL);
		
		TestCase.assertEquals("NOMINAL", modulo);
	}
	
	@Test
	public void recuperarConstantesRescateTest() {
		ValoresConstantesRescatesDao valoresConstantesRescatesDao = new ValoresConstantesRescatesDao();
		
		// Probamos los KT
		ValoresConstantesRescate valorConstante1 = new ValoresConstantesRescate();
		valorConstante1.setKduracion(1);
		valorConstante1.setKk1("KT1");
		valorConstante1.setPorckonst(new BigDecimal("1.0"));
		
		valoresConstantesRescatesDao.put(valorConstante1.getKey(), valorConstante1);
		
		ValoresConstantesRescate valorConstante2 = new ValoresConstantesRescate();
		valorConstante2.setKduracion(2);
		valorConstante2.setKk1("KT1");
		valorConstante2.setPorckonst(new BigDecimal("2.0"));
		
		valoresConstantesRescatesDao.put(valorConstante2.getKey(), valorConstante2);
		
		ValoresConstantesRescate valorConstante3 = new ValoresConstantesRescate();
		valorConstante3.setKduracion(3);
		valorConstante3.setKk1("KT1");
		valorConstante3.setPorckonst(new BigDecimal("3.0"));
		
		valoresConstantesRescatesDao.put(valorConstante3.getKey(), valorConstante3);
		
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		BigDecimal resultadoKT = servicio.recuperarCteRescate("KT1", 2);

		TestCase.assertEquals(new BigDecimal("2.0"), resultadoKT);
		
		// Probamos los KC
		ValoresConstantesRescate valorConstante6 = new ValoresConstantesRescate();
		valorConstante6.setKduracion(6);
		valorConstante6.setKk1("KC1");
		valorConstante6.setPorckonst(new BigDecimal("6.0"));
		
		valoresConstantesRescatesDao.put(valorConstante6.getKey(), valorConstante6);
		
		ValoresConstantesRescate valorConstante7 = new ValoresConstantesRescate();
		valorConstante7.setKduracion(7);
		valorConstante7.setKk1("KC1");
		valorConstante7.setPorckonst(new BigDecimal("7.0"));
		
		valoresConstantesRescatesDao.put(valorConstante7.getKey(), valorConstante7);
		
		BigDecimal resultadoKC = servicio.recuperarCteRescate("KC1", 7);

		TestCase.assertEquals(new BigDecimal("7.0"), resultadoKC);
		
	}
	
	@Test
	public void recuperarValoresExperiencia2Test() throws Exception{
		createTablaExp();
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		List<java.math.BigDecimal> var2 = servicio.recuperarValoresExperiencia(createUmic(), createBtcUmic(),  "1960","H", 54, "L", IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		BigDecimal valor = var2.get(0);
		BigDecimal valor2 = var2.get(115);
		TestCase.assertEquals(BigDecimal.valueOf(1000000), valor);
		TestCase.assertEquals(BigDecimal.valueOf(3.913), valor2);
	}
	
	@Test
	public void recuperarEdadMax2Test() throws Exception{
		createTablaExp();
		IObtenerConfiguracion servicio = FachadaServicios.getObtenerConfiguracion();
		Integer var2 = servicio.recuperarEdadMax(createUmic(), createBtcUmic(),  "1960","H",IObtenerConfiguracion.OrdenAsegurado.ASEG1);
		TestCase.assertEquals(Integer.valueOf(116), var2);
	}
	

	public void createTablaExp(){
		final List<BigDecimal> TABLA_MORTALIDAD = new ArrayList<BigDecimal>();
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
		
		TablaExperienciaDao tablaExerienciaDao = new TablaExperienciaDao();
		TablaExperiencia te = new TablaExperiencia();
		te.setKanacimiento("1960");
		te.setKtabla(740);
		te.setKsobremort(BigDecimal.ZERO);
		te.setKsobreries(BigDecimal.ZERO);
		te.setK2tipovalor("2");
		te.setGvalor(TABLA_MORTALIDAD);
		
		tablaExerienciaDao.put(te.getKey(), te);
		
		CabeceraTablaExperienciaDao cabeceraTablaExperienciaDao = new CabeceraTablaExperienciaDao();
		CabeceraTablaExperiencia cte = new CabeceraTablaExperiencia();
		
		cte.setKtabla(740);
		cte.setK2tipotabla("3");
		
		cabeceraTablaExperienciaDao.put(cte.getKey(), cte);
		
	}
	
	private Umic createUmic(){
		Umic umic = new Umic();
		Fechas fechas = new Fechas();
		DatosGenerales datosGenerales = new DatosGenerales();
		BaseTecnicaInicial bti = new BaseTecnicaInicial();

		Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, 0, 0, 0).getTimeInMillis());

		datosGenerales.setKmodalidad(238);
		datosGenerales.setCcanal(1);
		datosGenerales.setCnegocio("C");
		datosGenerales.setFecCierre(FCIERRE);
		bti.setPriesgo(BigDecimal.ZERO);
		bti.setPsobremort(BigDecimal.ZERO);
		
		
		umic.setFechas(fechas);
		umic.setDatosGenerales(datosGenerales);
		umic.setBti(bti);
		
		return umic;
	}
	

	private DetalleBaseTecnica createBtcUmic(){
		Timestamp FCIERRE = new Timestamp(new GregorianCalendar(2014, 02,31, 0, 0, 0).getTimeInMillis());
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
