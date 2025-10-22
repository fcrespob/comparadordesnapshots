package es.mapfre.solvencia.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class TablasExperienciaBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(TablasExperienciaBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB005CTM0.TXT";
	
	/**
	 * Patrón a emplear en la ejecución:
	 * 
	 * 1) Distribución de los datos, aplicación local 2) Distribución de las
	 * aplicaciones, datos locales
	 * 
	 */
	@Test
	public void testBeanIO() {
		
		// Valores de mortalidad
		
		BeanIOReader in = null;
		BeanIOWriter out = null;
		try {

			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "CTM0");
			//out = new BeanIOWriter("beanio/beanio-config-out.xml", dataFileName+".csv", "CTM0-csv");
			
			List<TablaExperiencia> tablas = new ArrayList<TablaExperiencia>();
			
			try {

				TablaExperiencia tabla = null;
				
				while ((tabla = (TablaExperiencia) in.read()) != null) {
					log.debug("Campos gvalor: {}", tabla.getCusuario());
					log.debug(Integer.toString(tabla.getGvalor().size()));
					tablas.add(tabla);
					//out.write(tabla);
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
			// Asserts
			Assert.assertTrue(tablas.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					
				}
			}

		}

	}
	
}
