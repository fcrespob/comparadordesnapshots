package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class DatosAdicionalesCoaseguroBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(DatosAdicionalesCoaseguroBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/201312/CTEC/GENCARTE_AUX_DATOSCOA.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "X880JI03");
			
			List<DatosAdicionalesCoaseguro> values = new ArrayList<DatosAdicionalesCoaseguro>();
			
			try {

				DatosAdicionalesCoaseguro value = null;
				
				while ((value = (DatosAdicionalesCoaseguro) in.read()) != null) {
					log.debug("Kpoliza: {}", value.getKpoliza());
					values.add(value);
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(values.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
	
}
