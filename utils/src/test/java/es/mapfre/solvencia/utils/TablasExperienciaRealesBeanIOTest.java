package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class TablasExperienciaRealesBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(TablasExperienciaRealesBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/GBT_TB340VTR0.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "VTR0");
			
			List<TablasExperienciaReales> values = new ArrayList<TablasExperienciaReales>();
			
			try {

				TablasExperienciaReales value = null;
				
				while ((value = (TablasExperienciaReales) in.read()) != null) {
					log.debug("Feccierre: {}", value.getFecCierre());
					log.debug("Kbasetec: {}", value.getKbasetec());
					log.debug("Compañia: {}", value.getCompania());
					log.debug("Negocio: {}", value.getCnegocio());
					log.debug("RiesgoActuarial: {}", value.getRiesgoActuarial());
					log.debug("Sexo: {}", value.getSexo());
					log.debug("Categoria: {}", value.getCategoria());
					log.debug("EdadFija: {}", value.getEdadFija());
					log.debug("Tabla base: {}", value.getTablaBase());
					log.debug("Generacion: {}", value.getGeneracion());				
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