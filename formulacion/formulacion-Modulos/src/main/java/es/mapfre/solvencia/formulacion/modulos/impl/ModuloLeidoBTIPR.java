package es.mapfre.solvencia.formulacion.modulos.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.modulos.Modulo;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el modulo LEIDOBTIPR.
 * @author NFQ
 *
 */
public class ModuloLeidoBTIPR implements Modulo {

	@Override
	public String getNombreServicio() {
		return ConstantsFactorias.MODULO_LEIDO_BTIPR;
	}
	
	/**
	 * Función encargada de obtener los parámetros necesarios y de realizar la llamada a la función que realiza los calculos del modulo.
	 */
	@SuppressWarnings("unchecked")
	public Object execute(final Object... args) throws Solvencia2Excepcion {		
		return null;
	}

}
