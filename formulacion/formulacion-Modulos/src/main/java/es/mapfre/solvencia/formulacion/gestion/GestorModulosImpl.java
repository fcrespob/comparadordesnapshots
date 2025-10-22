package es.mapfre.solvencia.formulacion.gestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.modulos.services.FactoriaModulos;

/**
 * Implementación para obtener los modulos implementados.
 * 
 * @author rschacon
 *
 */
public class GestorModulosImpl implements GestorModulos {
	
	
	/**
	 * Metodo que carga el fichero de propiedades
	 */
	public GestorModulosImpl() {
	}
	
	/**
	 * Metodo que obtiene las implementaciones de los modulos que se van a ejecutar segun el fichero
	 * de propiedades que se ha cargado anteriormente
	 */
	public Modulo obtenerModulo(final String idModulo) {
		
		return FactoriaModulos.getModulo(idModulo);
	}

}
