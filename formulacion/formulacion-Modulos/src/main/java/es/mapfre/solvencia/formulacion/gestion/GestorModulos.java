package es.mapfre.solvencia.formulacion.gestion;

import es.mapfre.solvencia.modulos.Modulo;

/** Interfaz para el obtener los modulos implementados.
 * @author rschacon
 *
 */
public interface GestorModulos {
	Modulo obtenerModulo(String idModulo);
}
