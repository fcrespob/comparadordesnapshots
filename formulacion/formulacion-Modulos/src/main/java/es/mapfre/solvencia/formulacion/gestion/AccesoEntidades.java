package es.mapfre.solvencia.formulacion.gestion;

import es.mapfre.solvencia.formulacion.datos.acceso.CriteriosFiltrado;
import es.mapfre.solvencia.formulacion.datos.acceso.TipoEntidad;

/**
 * Interfaz para el acceso a las entidades
 * @author agonzalezgar
 *
 */
public interface AccesoEntidades {
	
	/**
	 * Interfaz para obtener la entidad segun unos filtros
	 * @param tipoEntidad
	 * @param filtros
	 * @return
	 */
	Object obtener(final TipoEntidad tipoEntidad, final CriteriosFiltrado filtros);
	
	/**
	 * Interfaz para guardar la entidad
	 * @param tipoEntidad
	 * @param clave
	 * @param valor
	 */
	void guardar(final TipoEntidad tipoEntidad, final String clave, final Object valor);
}
