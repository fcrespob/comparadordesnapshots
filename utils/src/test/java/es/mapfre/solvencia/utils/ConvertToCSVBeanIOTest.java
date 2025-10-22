package es.mapfre.solvencia.utils;

import java.util.Properties;

import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class ConvertToCSVBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(TablasExperienciaBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String BEANIO_CONFIG_CSV_XML = "beanio/beanio-config-csv.xml";

	private static String RUTA_BASE = "src/test/resources/NAS/CIERRES";

	private static Properties nombresFicheros = new Properties();
	private static final String CARGA_FICHEROS_PROPERTIES = "beanio/cargaFicheros.properties";
	
	//@Test
	public void testBeanIO() throws Exception {
		
		nombresFicheros.load(ClassLoader.getSystemResourceAsStream(CARGA_FICHEROS_PROPERTIES));
		
		String[] streams = nombresFicheros.getProperty("streams").split(",");

		BeanIOReader in = null;
		BeanIOWriter out = null;
		for (String stream : streams) {
			try {
				String dataFileName = RUTA_BASE
						+ nombresFicheros.getProperty(stream);
				String dataFileNameCsv = dataFileName + ".csv";
	
				in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, stream);
				out = new BeanIOWriter(BEANIO_CONFIG_CSV_XML, dataFileNameCsv, stream);
				
				try {
					Object objeto = null;
					
					while ((objeto = in.read()) != null) {
						out.write(objeto);
					}
				} catch (InvalidRecordException ire) {
					log.error("Error parseando fichero: {}", ire.toString());
				}
				
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (Exception e) {
				log.error("Error ", e);
			}
		}

	}
}