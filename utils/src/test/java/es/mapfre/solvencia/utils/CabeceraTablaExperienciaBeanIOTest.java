package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class CabeceraTablaExperienciaBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(TablasExperienciaBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB005TXP0.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "TXP0");
			
			List<CabeceraTablaExperiencia> cabeceras = new ArrayList<CabeceraTablaExperiencia>();
			
			try {

				CabeceraTablaExperiencia cabecera = null;
				
				while ((cabecera = (CabeceraTablaExperiencia) in.read()) != null) {
					log.debug(cabecera.getK2tipotabla());
					log.debug("{} {} {} {}",cabecera.getcUsuario(), cabecera.getFecAlta(), cabecera.getFecAnula(), cabecera.getFecModif());
					cabeceras.add(cabecera);
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(cabeceras.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
}