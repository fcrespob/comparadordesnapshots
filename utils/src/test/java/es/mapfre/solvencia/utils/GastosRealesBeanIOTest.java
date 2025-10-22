package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class GastosRealesBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(GastosRealesBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/GBT_TB340GRE0.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "GRE0");
			
			List<GastosReales> values = new ArrayList<GastosReales>();
			
			try {

				GastosReales value = null;
				
				while ((value = (GastosReales) in.read()) != null) {
					log.debug("GastoPorUmic: {}", value.getGastoPorUmic());
					log.debug("PctGastoProv: {}", value.getPctGastoProv());
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
