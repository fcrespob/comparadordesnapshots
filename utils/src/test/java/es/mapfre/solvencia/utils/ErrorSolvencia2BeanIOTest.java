package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.ErrorSolvencia2;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class ErrorSolvencia2BeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(ErrorSolvencia2BeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB340ERR0.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "ERR0");
			
			List<ErrorSolvencia2> errores = new ArrayList<ErrorSolvencia2>();
			
			try {

				ErrorSolvencia2 error = null;
				
				while ((error = (ErrorSolvencia2) in.read()) != null) {
					log.debug(error.getKretorno());
					errores.add(error);
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(errores.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
}