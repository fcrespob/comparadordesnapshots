package es.mapfre.solvencia.data;

import java.util.List;

import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.services.Servicio;

public interface Loader extends Servicio {
	/**
	 * Devuelve una lista con los descriptores de fichero a cargar en caché para
	 * una ruta base y una fecha de cierre.
	 * 
	 * @return los descriptores de fichero a cargar.
	 */
	List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre);
}
