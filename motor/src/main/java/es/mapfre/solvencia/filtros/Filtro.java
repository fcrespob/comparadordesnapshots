package es.mapfre.solvencia.filtros;

//JBMARTA - PYAM0025 - INI
import java.util.Set;

//import com.tangosol.util.Filter;

import com.tangosol.net.partition.PartitionSet;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
//import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
// JBMARTA - PYAM0025 - FIN

import es.mapfre.solvencia.services.Servicio;

/**
 * Interfaz de los Filtros para Fichas de Proceso
 * @author Indra
 *
 */
public interface Filtro extends Servicio {
	
	/**
	 * @return constructor de filtros
	 */
	// JBMARTA - PYAM0025 - INI 
	//Filter crearFiltro(FichaProceso fichaProceso) throws Solvencia2Excepcion;

	Set<UmicKey> filtrar(final Set<UmicKey> umicSet, final PartitionSet partitionSet, final FichaProceso filtr);
	// JBMARTA - PYAM0025 - FIN
}