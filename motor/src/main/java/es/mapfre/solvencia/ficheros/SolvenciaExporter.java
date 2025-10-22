package es.mapfre.solvencia.ficheros;

import java.util.ArrayList;
import java.util.List;

import es.mapfre.solvencia.data.Exporter;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.utils.BtUtils;

public class SolvenciaExporter implements Exporter {
	private static final String NOMBRE_EXPORTER = "SOLVXPRT";

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";
	
	private BtUtils btUtils = new BtUtils();

	
	@Override
	public String getNombreServicio() {
		return NOMBRE_EXPORTER;
	}

	@Override
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, FichaProceso fichaProceso) {
		return getFileDescriptors(rutaBase, fecCierre, ConstantesSolvencia.FICHEROS_SALIDA);
	}
	
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, String tipoFichero) {
		List<FileDescriptor> descriptores = new ArrayList<FileDescriptor>();
		
		String[] daos = btUtils.getCargaFicherosProperty(tipoFichero).split(",");

		for (int i = 0; i < daos.length; i++) {
			String nombref = btUtils.getCargaFicherosProperty(daos[i]);
			FileDescriptor descriptor = new FileDescriptor(nombref, daos[i], BEANIO_CONFIG_XML,ConstantsFunciones.CTE_1);
			descriptores.add(descriptor);
		}
		
		return descriptores;
	}
	
}
