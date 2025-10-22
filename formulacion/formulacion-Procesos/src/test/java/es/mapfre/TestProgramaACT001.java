package es.mapfre;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;

public class TestProgramaACT001 {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestProgramaACT001.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String DATA_FILE_NAME = "src/test/resources/Maestro_Host_Pruebas.txt";
	
	private static Umic[] arrUmics = null;
	private static FichaProceso fichaProceso = null;
	private static DetalleBaseTecnica detalleBT = null;
	private static List<DetalleCorriente>[] arrDetCorriente = null;
	
	@AfterClass
	public static void end() {
		TestProgramaACT001.LOG.debug("Fin pruebas del programa ACT001");
	}
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void init() {
		TestProgramaACT001.LOG.debug("Inicio pruebas del programa ACT001");
		try {
			// Carga de fichero con la umic
			final List<Umic> lstUmics = UtilTestProcesos.obtenerUMICFichero(DATA_FILE_NAME, BEANIO_CONFIG_XML);
			
			final Umic dejar1Umic = lstUmics.get(0);
			lstUmics.clear();
			lstUmics.add(dejar1Umic);
			
			// Carga de la ficha proceso
			fichaProceso = UtilTestProcesos.crearFichaProceso();
			// Carga del detalle base tecnica
			detalleBT = crearDetalleBaseTecnica();
			
			arrUmics = new Umic[lstUmics.size()];
			arrDetCorriente = new List[lstUmics.size()];
			for (int i = 0; i < lstUmics.size(); i++) {
				// Cargamos la umic en el array
				final Umic umic = lstUmics.get(i);
				arrUmics[i] = umic;
				UtilTestProcesos.setValoresUmic(umic);
				
				//Cargamos los DetalleCorriente en el array para la umic
				final List<DetalleCorriente> lstDetCorriente = UtilTestProcesos.obtenerPeriodos(umic, fichaProceso, detalleBT, ConstantsModulos.CTE_PROY_VIDA);
				arrDetCorriente[i] = lstDetCorriente;
				
				//establecemos la fecha de pago y devengo
				UtilTestProcesos.obtenerFechaPagoDevengo(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
			
		} catch (Exception e) {
			TestProgramaACT001.LOG.debug(e.getMessage(),e);
		} catch (Throwable e) {
			TestProgramaACT001.LOG.debug(e.getMessage(),e);
		}
	}
	
	//@Test
	public void test() {
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		
		TestProgramaACT001.LOG.debug("Inicio programa ACT001");
		try {
			
			for (int i = 0; i < arrUmics.length; i++) {
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				UtilTestProcesos.setValoresDePrueba(lstDetCorriente);
				
				UtilTestProcesos.obtenerFlujoNominal(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
				UtilTestProcesos.obtenerFlujoProbable(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
				UtilTestProcesos.obtenerFlujoNoAnulado(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
				UtilTestProcesos.obtenerFlujoActualizado(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
		} catch (Exception e) {
			TestProgramaACT001.LOG.debug(e.getMessage(),e);
			fail();
		}
		
		TestProgramaACT001.LOG.debug("Fin programa ACT001");
	}
	
	private static DetalleBaseTecnica crearDetalleBaseTecnica() {
		final DetalleBaseTecnica baseTecnica = new DetalleBaseTecnica();
		baseTecnica.setBaseTec(ConstantsModulos.CTE_BTI);
		baseTecnica.setTablacalc1aseg1("740");
		baseTecnica.setTablacalc1aseg2("740");
		baseTecnica.setGtorosspPrima(BigDecimal.ONE);
		baseTecnica.setGtorosspCap(BigDecimal.ONE);
		final List<BigDecimal> lstBigDecimal = new ArrayList<BigDecimal>();
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		lstBigDecimal.add(ConstantsFunciones.CTE_OPER_2);
		baseTecnica.setItcalc(lstBigDecimal);
		final List<String> scasadoX = new ArrayList<String>();
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		scasadoX.add(ConstantsModulos.CTE_S);
		baseTecnica.setSwcasado(scasadoX);
		final GregorianCalendar ini = new GregorianCalendar(1986, 9, 1);
		final GregorianCalendar fin = new GregorianCalendar();
		final GregorianCalendar finEspe = new GregorianCalendar(2014, 10, 30);
		final GregorianCalendar fecCierre = new GregorianCalendar(2020, 8, 1);
		baseTecnica.setFecCierre(new Timestamp(fecCierre.getTimeInMillis()));
		final List<Timestamp> lstIni = new ArrayList<Timestamp>();
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		lstIni.add(new Timestamp(ini.getTimeInMillis()));
		baseTecnica.setFecInitramo(lstIni);
		final List<Timestamp> lstFin = new ArrayList<Timestamp>();
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(finEspe.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		lstFin.add(UtilFechas.incrDias(new Timestamp(fin.getTimeInMillis()),1));
		baseTecnica.setFecfintramo(lstFin);
		return baseTecnica;
	}
}
