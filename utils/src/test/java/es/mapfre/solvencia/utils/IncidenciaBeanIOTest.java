package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class IncidenciaBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(IncidenciaBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	private static String dataFileName = "src/test/resources/files/incidencias.txt";


	//@Test
	public void testBeanIO(){
		
		BeanIOReader in = null;

			try {
				in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "incidencias");
				
				List<Incidencia> incidencias = new ArrayList<Incidencia>();
				
				try {

					Incidencia incidencia = null;
					
					while ((incidencia = (Incidencia) in.read()) != null) {
						log.debug("Codigo: {}", incidencia.getCodigoRetorno());
						log.debug("InfAmpliada: {}", incidencia.getInfAmpliada());
						incidencias.add(incidencia);
					}
				} catch (InvalidRecordException ire) {
					log.error("Error parseando fichero: {}", ire.toString());
				} 
				if (in != null) {
					in.close();
				}
				// Asserts
				Assert.assertTrue(incidencias.size()!=0);
			} catch (Exception e) {
				log.error("Error ", e);
			}

	}
	
}
