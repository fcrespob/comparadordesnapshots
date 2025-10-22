package es.mapfre.solvencia.utils;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class DefinicionesAuxiliaresBeanIOTest extends TestCase{
	
	private static Logger log = LoggerFactory.getLogger(DefinicionesAuxiliaresBeanIOTest.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static String dataFileName = "src/test/resources/NAS/CIERRES/CATALOGOS/CTLG_TB880AUX0.TXT";
	
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
						
			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "AUX0");
			in.setFailOnError(true);
			
			String variable = null;
				
			List<DefinicionesAuxiliares> definicionesAuxiliares = new ArrayList<DefinicionesAuxiliares>();
		
			try {
				DefinicionesAuxiliares variableApoyo = null;
								
				while ((variableApoyo = (DefinicionesAuxiliares) in.read()) != null) {
			
					log.debug(" {}",variableApoyo.getKcarteorig());
					log.debug(" {}",variableApoyo.getCnegocio());
					log.debug(" {}",variableApoyo.getKmodalidad());
					log.debug(" {}",variableApoyo.getKgarantia());
					log.debug(" {}",variableApoyo.getCidentivariab());
					log.debug(" {}",variableApoyo.getGvariable());
					log.debug(" {}",variableApoyo.getGvalor());
					
					definicionesAuxiliares.add(variableApoyo);
				}
			} catch (InvalidRecordException ire) {
				log.error("Error parseando fichero: {}", ire.toString());
			} 
			if (in != null) {
				in.close();
			}
			// Asserts
			Assert.assertTrue(definicionesAuxiliares.size()!=0);
		} catch (Exception e) {
			log.error("Error ", e);
		}

	}
	
}
