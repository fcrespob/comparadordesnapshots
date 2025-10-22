package es.mapfre.solvencia.cargaFicherosGBT;

import java.util.ArrayList;
import java.util.List;

import es.mapfre.solvencia.data.Loader;
import es.mapfre.solvencia.files.FileDescriptor;

public class LoaderGBT implements Loader {

	private static final String NOMBRE_LOADER = "LOADER_GBT";

	private static final String BEANIO_CONFIG_XML = "beanio/gbt-beanio-config.xml";

	public String getNombreServicio() {
		return NOMBRE_LOADER;
	}

	public List<FileDescriptor> getFileDescriptors(String rutaBase,
			String fecCierre) {
		List<FileDescriptor> descriptores = new ArrayList<FileDescriptor>();
		BtUtilsGBT btUtils = new BtUtilsGBT();

		String[] daos = btUtils.getCargaFicherosProperty("catalogos").split(",");

		for (int i = 0; i < daos.length; i++) {

			String streamName = daos[i];
			String filePath = btUtils.getCargaFicherosProperty(streamName);
			descriptores.add(new FileDescriptor(filePath, streamName, BEANIO_CONFIG_XML));
		}

		//this.cargarDatos(descriptores);

		return descriptores;
	}

	/*
	 * Método creado por si no se cargasen los ficheros desde el motor, si no que se tuviese que hacer uso de un método de esta clase
	 *
	private void cargarDatos(List<FileDescriptor> descriptores){
		for(FileDescriptor descriptor:descriptores){
			try {
				BeanIOReader reader = new BeanIOReader(descriptor.getBeanioConfigXml(),descriptor.getFilePath(), descriptor.getStreamName());
				Dao dao = FactoriaDao.getDao(descriptor.getStreamName());
				dao.loadCache(reader);

				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	/**/
	

}
