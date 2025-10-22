package es.mapfre;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;

public class TestProgramaNAN001 {
	
	private static final Logger LOG = LoggerFactory.getLogger(TestProgramaNAN001.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String DATA_FILE_NAME = "src/test/resources/Maestro_Host_Pruebas.txt";
	
	private static Umic[] arrUmics = null;
	private static FichaProceso fichaProceso = null;
	private static DetalleBaseTecnica detalleBT = null;
	private static List<DetalleCorriente>[] arrDetCorriente = null;
	
	@AfterClass
	public static void end() {
		TestProgramaNAN001.LOG.debug("Fin pruebas del programa NAN001");
	}
	
	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void init() {
		TestProgramaNAN001.LOG.debug("Inicio pruebas del programa NAN001");
		try {
			// Carga de fichero con la umic
			final List<Umic> lstUmics = UtilTestProcesos.obtenerUMICFichero(DATA_FILE_NAME, BEANIO_CONFIG_XML);
			
			final Umic dejar1Umic = lstUmics.get(0);
			lstUmics.clear();
			lstUmics.add(dejar1Umic);
			
			// Carga de la ficha proceso
			fichaProceso = UtilTestProcesos.crearFichaProceso();
			// Carga del detalle base tecnica
			detalleBT = UtilTestProcesos.crearDetalleBaseTecnica();
			
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
			TestProgramaNAN001.LOG.debug(e.getMessage(),e);
		} catch (Throwable e) {
			TestProgramaNAN001.LOG.debug(e.getMessage(),e);
		}
	}
	
	//@Test
	public void test() {
		Umic umic = null;
		List<DetalleCorriente> lstDetCorriente = null;
		
		TestProgramaNAN001.LOG.debug("Inicio programa NAN001");
		try {
			
			for (int i = 0; i < arrUmics.length; i++) {
				umic = arrUmics[i];
				lstDetCorriente = arrDetCorriente[i];
				UtilTestProcesos.setValoresDePrueba(lstDetCorriente);
				
				UtilTestProcesos.obtenerFlujoNominal(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
				UtilTestProcesos.obtenerFlujoProbable(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
				UtilTestProcesos.obtenerFlujoNoAnulado(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
		} catch (Exception e) {
			TestProgramaNAN001.LOG.debug(e.getMessage(),e);
			fail();
		}
		
		TestProgramaNAN001.LOG.debug("Fin programa NAN001");
	}
}
