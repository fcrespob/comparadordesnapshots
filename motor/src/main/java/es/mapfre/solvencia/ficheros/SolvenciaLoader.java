package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import es.mapfre.solvencia.data.Loader;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.utils.BtUtils;

public class SolvenciaLoader implements Loader {
	private static final String NOMBRE_LOADER = "SOLVLODR";

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	
	private BtUtils btUtils = new BtUtils();

	
	@Override
	public String getNombreServicio() {
		return NOMBRE_LOADER;
	}

	@Override
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre) {
		return getFileDescriptors(rutaBase, fecCierre, ConstantesSolvencia.AUXILIARES);
	}
	
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, String tipoFichero) {
		List<FileDescriptor> descriptores = new ArrayList<FileDescriptor>();
		
		String[] daos = btUtils.getCargaFicherosProperty(tipoFichero).split(",");

		for (int i = 0; i < daos.length; i++) {
			// bucle por ficheros

			String fileName = btUtils.getCargaFicherosProperty(daos[i]);
			int lastSeparatorIndex = 0;
			lastSeparatorIndex = fileName.lastIndexOf('/');
			if (lastSeparatorIndex < 0) {
				lastSeparatorIndex = fileName.lastIndexOf('\\');
			}

			String fileNamePatternPrev = fileName.substring(0, lastSeparatorIndex + 1);
			String fileNamePattern = fileName.substring(lastSeparatorIndex + 1);
			fileNamePattern = fileNamePattern.concat(ConstantesSolvencia.REGEX_FILES);

			final Pattern p = Pattern.compile(fileNamePattern);
			String[] ficheros = new File(rutaBase + File.separator + fecCierre + fileNamePatternPrev)
					.list(new FilenameFilter() {

						@Override
						public boolean accept(File dir, String name) {
							return (p.matcher(name).matches());
						}

					});

			if (ficheros == null || ficheros.length == 0) {
				ficheros = new String[] { fileName.substring(lastSeparatorIndex + 1) };
			}

			for (int k = 0; k < ficheros.length; k++) {
				descriptores.add(new FileDescriptor(fileNamePatternPrev + ficheros[k],
						daos[i], BEANIO_CONFIG_XML));
			}

		}

		
		return descriptores;
	}

}
