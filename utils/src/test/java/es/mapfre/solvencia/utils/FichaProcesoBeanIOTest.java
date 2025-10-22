package es.mapfre.solvencia.utils;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FiltroFichaProceso;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class FichaProcesoBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(FichaProcesoBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/201312/FICHAS/FICHASPEND/FICHASCALCULO.TXT";
	/**
	 * Patrón a emplear en la ejecución:
	 * 
	 * 1) Distribución de los datos, aplicación local 2) Distribución de las
	 * aplicaciones, datos locales
	 * 
	 */
	@Test
	public void testBeanIO() {
		
		BeanIOReader in = null;
		try {

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "R340T000");
			FichaProceso fichaProceso = null;
			
			try {
				FichaProceso aux = null;
				while ((aux = (FichaProceso) in.read()) != null) {
					fichaProceso = aux;
					log.debug("{}",fichaProceso);
					log.debug("Fefecto = {}",fichaProceso.getFefecto());
					log.debug("Ffinreal = {}",fichaProceso.getFfinreal());
					log.debug("Ghfinreal = {}",fichaProceso.getGhfinreal());
					log.debug("Fgrabacion = {}",fichaProceso.getFgrabacion());
					log.debug("Finiper = {}",fichaProceso.getFiniper());
					log.debug("Finireal = {}",fichaProceso.getFinireal());
					log.debug("Ghinireal = {}",fichaProceso.getGhinireal());
					log.debug("Fmodificacion = {}",fichaProceso.getFmodificacion());
					log.debug("Fprevejec = {}",fichaProceso.getFprevejec());
					
					for (FiltroFichaProceso filtro : fichaProceso.getFiltrosAmbito()) {
						log.debug("FiltroAmbito Fgrabacion = {}",filtro.getFgrabacion());
					}
					
					if (fichaProceso.getRegistroParametros()!=null) {
						log.debug("IndicadorTipoProceso = {}",fichaProceso.getRegistroParametros().getIndicadorTipoProceso());
					}
					
				}
				
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			}
			
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertNotNull(fichaProceso);
			
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
}