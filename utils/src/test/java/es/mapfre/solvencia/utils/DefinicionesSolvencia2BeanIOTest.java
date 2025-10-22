package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesSolvencia2;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class DefinicionesSolvencia2BeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(DefinicionesSolvencia2BeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB880SOL0.TXT";

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
			
			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "SOL0");
				
			List<DefinicionesSolvencia2> definiciones = new ArrayList<DefinicionesSolvencia2>();
			
			try {

				DefinicionesSolvencia2 definicion = null;
				
				while ((definicion = (DefinicionesSolvencia2) in.read()) != null) {
					log.debug("Cenegocio: " + String.valueOf(definicion.getCnegocio()));
					definiciones.add(definicion);
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(definiciones.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
	
}
