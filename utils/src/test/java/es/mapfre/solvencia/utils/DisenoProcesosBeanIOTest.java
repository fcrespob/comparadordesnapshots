package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class DisenoProcesosBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(DisenoProcesosBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB880DIP0.TXT";
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

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "DIP0");
			
			List<DisenoProcesos> procesos = new ArrayList<DisenoProcesos>();
			
			try {

				DisenoProcesos disenoProcesos = null;
				
				while ((disenoProcesos = (DisenoProcesos) in.read()) != null) {
					log.debug("Procesos y subprocesos de: {}", disenoProcesos.getGproceso());
					for (ElementoSubproceso elementosSubprocesos : disenoProcesos.getElementosSubprocesos()) {
						log.debug("nombre: {}", elementosSubprocesos.getCelement());
					}
					procesos.add(disenoProcesos);
					/*
					if (tabla.getGvalor() != null) {
						int i = 0;
						for (double valor : tabla.getGvalor()) {
							log.debug("valor {}: {}", i, valor);
							i++;
						}
					}*/
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(procesos.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
	
}
