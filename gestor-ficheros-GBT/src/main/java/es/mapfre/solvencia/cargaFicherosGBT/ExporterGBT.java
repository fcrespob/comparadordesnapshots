package es.mapfre.solvencia.cargaFicherosGBT;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import es.mapfre.solvencia.data.Exporter;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.files.FileDescriptor;

public class ExporterGBT implements Exporter {

	private static final String NOMBRE_EXPORTER = "EXPORTER_GBT";

	private static final String BEANIO_CONFIG_XML = "beanio/gbt-beanio-config-out.xml";

	/*
	private static BeanIOWriter writerRes = null;
	DetalleBaseTecnicaDao daoRes = new DetalleBaseTecnicaDao();

	private static BeanIOWriter writerErr = null;
	ErrorOrquestadorDao daoErr = new ErrorOrquestadorDao();
	*/

	public String getNombreServicio() {
		return NOMBRE_EXPORTER;
	}

	public List<FileDescriptor> getFileDescriptors(String rutaBase,	String fecCierre) {
		
		/*
		File folder = new File(rutaBase + File.separator + fecCierre);
		
		if(!folder.exists()){
			folder.mkdirs();
		}
		
		File ficheroErr = new File(rutaBase + File.separator + fecCierre + "/ERRORES.xls");

		if(ficheroErr.exists()){
			ficheroErr.delete();
		}
		FileWriter fwErr;
		try {
			fwErr = new FileWriter(ficheroErr);

			PrintWriter pwErr = new PrintWriter(fwErr);
			pwErr.println(new ErrorOrquestador().camposTabString());
			fwErr.close();

			File fichero = new File(rutaBase + File.separator + fecCierre + "/RESULT.xls");
			if(fichero.exists()){
				if (fichero.delete()){
					FileWriter fw = new FileWriter(fichero);
					PrintWriter pw = new PrintWriter(fw);
					pw.println(new DetalleBaseTecnica().camposTabString());
					fw.close();
				}else{
					fwErr = new FileWriter(ficheroErr);
					pwErr = new PrintWriter(fwErr);
					pwErr.println(new ErrorOrquestador().camposTabString());
					fwErr.close();

					writerErr = new BeanIOWriter(BEANIO_CONFIG_XML, rutaBase + File.separator + fecCierre + "/ERRORES.XLS", "error-orquestador");

					ErrorOrquestador error = new ErrorOrquestador();
					error.setDescripcionErr("El fichero de resultados esta en uso");

					daoErr.put(error.getKey(), error);
					daoErr.exportCache(writerErr);
					
					return null;
				}
			}else{
				FileWriter fw = new FileWriter(fichero);
				PrintWriter pw = new PrintWriter(fw);
				pw.println(new DetalleBaseTecnica().camposTabString());
				fw.close();
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		*/
		
		List<FileDescriptor> descriptores = new ArrayList<FileDescriptor>();

		// No se devuelve el detalle base tecnica tratado porque ya se hace desde el motor
		//descriptores.add(new FileDescriptor(rutaBase + File.separator + fecCierre + "/RESULTADO_GBT.XLS", "detalle-base-tec", BEANIO_CONFIG_XML));
		
		/*
		try {
			writerRes = new BeanIOWriter(BEANIO_CONFIG_XML, rutaBase + File.separator + fecCierre + "/RESULT.XLS", "detalle-base-tec");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		descriptores.add(new FileDescriptor(rutaBase + File.separator + fecCierre + "/ERRORES_GBT.XLS", "error-orquestador", BEANIO_CONFIG_XML));
		
		/*
		try {
			writerErr = new BeanIOWriter(BEANIO_CONFIG_XML, rutaBase + File.separator + fecCierre + "/ERRORES.XLS", "error-orquestador");
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/

		return descriptores;
	}

	@Override
	public List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, FichaProceso fichaProceso) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * Se comenta todo lo relativo a la escritura de ficheros. al ser tratado desde el motor de cálculo
	public void putCacheRes(DetalleBaseTecnicaKey key, DetalleBaseTecnica dbt){
		daoRes.put(key, dbt);
	}

	public void exportCacheRes(){
		daoRes.exportCache(writerRes);
	}

	public void putCacheErr(ErrorOrquestadorKey key, ErrorOrquestador error){
		daoErr.put(key, error);
	}

	public void exportCacheErr(){
		daoErr.exportCache(writerErr);
	}

	public void cerrarDescriptorRes(){
		try {
			writerRes.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void cerrarDescriptorErr(){
		try {
			writerErr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

}
