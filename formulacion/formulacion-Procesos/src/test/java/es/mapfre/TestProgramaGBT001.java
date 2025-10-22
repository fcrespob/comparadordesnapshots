package es.mapfre;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.programas.Programa;
import es.mapfre.solvencia.programas.services.FactoriaProgramas;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class TestProgramaGBT001 {

	private static final Logger LOG = LoggerFactory.getLogger(TestProgramaGBT001.class);
	
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String DATA_FILE_NAME = "src/test/resources/Maestro_Host_Pruebas.txt";
	
	private static Umic[] arrUmics = null;
	private static FichaProceso fichaProceso = null;
	private static DetalleBaseTecnica detalleBT = null;
	
	@BeforeClass
	public static void init() {
		TestProgramaGBT001.LOG.debug("Inicializando pruebas");
		try {
			// Carga de fichero con la umic
			final List<Umic> lstUmics = UtilTestProcesos.obtenerUMICFichero(DATA_FILE_NAME, BEANIO_CONFIG_XML);
			// Carga de la ficha proceso
			fichaProceso = UtilTestProcesos.crearFichaProceso();
			// Carga del detalle base tecnica
			detalleBT = UtilTestProcesos.crearDetalleBaseTecnica();
			
			arrUmics = new Umic[lstUmics.size()];
			for (int i = 0; i < lstUmics.size(); i++) {
				// Cargamos la umic en el array
				final Umic umic = lstUmics.get(i);
				arrUmics[i] = umic;
				UtilTestProcesos.setValoresUmic(umic);
				
				// Para el programa BTC
				umic.getDatosGenerales().setKmodalidad(362);
				
			}
		} catch (Exception e) {
			TestProgramaGBT001.LOG.debug(e.getMessage(),e);
		} catch (Throwable e) {
			TestProgramaGBT001.LOG.debug(e.getMessage(),e);
		}
	}
	
	//@Test
	public void test() {
		Umic umic = null;
		final List<DetalleCorriente> lstDetCorriente = new ArrayList<DetalleCorriente>();
		
		TestProgramaGBT001.LOG.debug("Inicio programa GBT001");
		
		try {
			for (int i = 0; i < arrUmics.length; i++) {
				umic = arrUmics[i];
				
				final Programa programa = FactoriaProgramas.getPrograma(ConstantsFactorias.PROGRAMA_GBT001);
				programa.execute(umic, fichaProceso, detalleBT, lstDetCorriente, ConstantsModulos.CTE_PROY_VIDA);
			}
		} catch (Exception e) {
			TestProgramaGBT001.LOG.debug(e.getMessage(),e);
			fail();
		}
		
		TestProgramaGBT001.LOG.debug("Fin programa GBT001");
	}
}
