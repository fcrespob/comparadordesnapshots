package es.mapfre.solvencia.dao;

import java.util.Map;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public interface DaoSalidaCalculo {

	/**
	 * @return guarda los datos de la cache en varios ficheros definidos por la BaseTecnica
	 */
	void exportCachePorBT(Map<String,BeanIOWriter> outs, FichaProceso ficha);

	/**
	 * Indica si ha de borrarse el contenido de la caché después de ser exportada a fichero.
	 * @return true si se debe borrar, false si no
	 */
	boolean clearAfterExport();
	
}
