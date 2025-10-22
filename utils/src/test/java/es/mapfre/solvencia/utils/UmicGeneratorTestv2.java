package es.mapfre.solvencia.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.beanio.InvalidRecordException;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class UmicGeneratorTestv2 extends TestCase {
	private static Logger log = LoggerFactory.getLogger(UmicGeneratorTestv2.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String dataFileName = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/CTEC/GENCARTE_MAESTRO_POLIZA_362.TXT";
	private String outFileName = "D:/Mapfre/ficherosPruebas01/NAS/CIERRES/201312/CTEC/GENCARTE_MAESTRO_POLIZA_362";
	private static final String outFileType =".TXT";
	private static final Integer numUmics =1000000;
	
	/**
	 * Patrón a emplear en la ejecución:
	 * 
	 * 1) Distribución de los datos, aplicación local 2) Distribución de las
	 * aplicaciones, datos locales
	 * @throws IOException 
	 * @throws InterruptedException 
	 * 
	 */
	
	@Test
	public void testBeanIO() throws IOException, InterruptedException {
		
		BeanIOReader in = null;
		BeanIOWriter out = null;
			
			in = new BeanIOReader(BEANIO_CONFIG_XML, dataFileName, "X880JI01");

			List<Umic> umics = new ArrayList<Umic>();
			Umic umic = null;
			
			while ((umic = (Umic) in.read()) != null) {
				umics.add(umic);
			}
			
			if (in != null) {
				in.close();
			}

			outFileName = outFileName.concat("_").concat(numUmics.toString()).concat(outFileType);
			umic = umics.get(0);
			out = new BeanIOWriter(BEANIO_CONFIG_XML, outFileName, "X880JI01");
			for (Integer i = 1; i <= numUmics; i++) {
				umic.getDatosGenerales().setKpoliza(new Long(i));
				out.write(umic);
				out.flush();
			}
			
	}
}
