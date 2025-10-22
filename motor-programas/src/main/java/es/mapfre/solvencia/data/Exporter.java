package es.mapfre.solvencia.data;

import java.util.List;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.services.Servicio;

public interface Exporter extends Servicio {

	/**
	 * Devuelve una lista con los descriptores de fichero a exportar para una
	 * ruta base y una fecha de cierre.
	 * 
	 * @return los descriptores de fichero a exportar.
	 */
	List<FileDescriptor> getFileDescriptors(String rutaBase, String fecCierre, FichaProceso fichaProceso);
}
