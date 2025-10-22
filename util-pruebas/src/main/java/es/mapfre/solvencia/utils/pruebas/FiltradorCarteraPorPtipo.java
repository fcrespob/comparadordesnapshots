package es.mapfre.solvencia.utils.pruebas;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * Pasar como parámetros de JVM:
 * <br/>
 * -Dsolvencia.fecha.cierre=AAAAMM
 * -Dsolvencia.ruta.base=RUTA_BASE_DE_LA_NAS
 *
 */
public class FiltradorCarteraPorPtipo {

	private static BtUtils btUtils = new BtUtils();
	private String rutaCierre;
	
	public static final String RUTA_CIERRES = "CIERRES";
	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	
	private FiltradorCarteraPorPtipo() {
		
	}
	
	public static void main(String[] args) throws Exception {
		FiltradorCarteraPorPtipo fcpp = new FiltradorCarteraPorPtipo();

		fcpp.configura();
		
		List<UmicKey> ptipos = fcpp.cargaPtipo();
		
		fcpp.filtraCartera(ptipos);
	}
	
	private void configura() {
		rutaCierre = getRutaCierre(true);
	}

	private String getRutaCierre(boolean concatFechaCierre) {
		String rutaBase = getRutaBase();
		String fecCierre = getFechaCierre();
		StringBuffer rutaCierre = new StringBuffer();
		rutaCierre.append(rutaBase).append(File.separator).append("CIERRES");
		// concatFechaCierre = true en caso de querer la ruta de cierre con la
		// fecha de cierre
		if (concatFechaCierre) {
			rutaCierre.append(File.separator).append(fecCierre);
		}
		return rutaCierre.toString();
	}
	
	private String getFechaCierre() {
		return System.getProperty("solvencia.fecha.cierre");

	}

	private String getRutaBase() {
		String rutaBase = "";

		if (System.getProperty("solvencia.ruta.base") != null) {
			rutaBase = System.getProperty("solvencia.ruta.base");
		}

		return rutaBase;
	}

	private List<UmicKey> cargaPtipo() throws IOException {
		String ficheroPtipo = btUtils.getCargaFicherosProperty("PTIPO");

		String filePath = rutaCierre + ficheroPtipo;
		BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML, filePath, "PTIPO");
		
		PolizasTipo ptipo = null;
		List<UmicKey> ptipos = new ArrayList<UmicKey> ();
		
		while ((ptipo = (PolizasTipo) reader.read()) != null) {
			ptipos.add(ptipo.getClaveUmic());
		}
		reader.close();
		return ptipos;
	}
	
	private void filtraCartera(List<UmicKey> ptipos) throws IOException {
		String ficheroMaestro = btUtils.getCargaFicherosProperty("X880JI01");

		String filePath = rutaCierre + File.separator + ficheroMaestro;
		BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML, filePath, "X880JI01");
		BeanIOWriter writer = new BeanIOWriter(BEANIO_CONFIG_XML, filePath + ".PTIPEADO.txt.gz" , "X880JI01");
		
		Umic umic = null;
		while ((umic = (Umic) reader.read()) != null) {
			if (ptipos.contains(umic.getKey())) {
				writer.write(umic);
			}
		}
		writer.close();
		reader.close();
	}
}
