package es.mapfre.solvencia.modulos;

import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.services.Servicio;

/**
 * Interfaz que implementan todos los módulos
 * @author rschacon
 *
 */
public interface Modulo extends Servicio {
	Object execute(Object...args) throws Solvencia2Excepcion;
}
