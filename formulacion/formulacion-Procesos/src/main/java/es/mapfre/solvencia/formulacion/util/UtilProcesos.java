package es.mapfre.solvencia.formulacion.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;


/**
 * Clase de utilidades para los subprocesos.
 * Contiene los metodos para el calculo de las distintas fechas e importes de las proyecciones.
 * Son los encargados de realizar la llamada a los distintos modulos de cada proyeccion. 
 * 
 */
public final class UtilProcesos {
	
	private UtilProcesos() { }
	
	/**
	 * Método que decide si el módulo tiene todos los datos necesarios,
	 * para almacenar la incidencia y continuar con la ejecución del programa,
	 * o si debe dejar que sea el programa el que la capture y almacene.
	 * 
	 * @param btc
	 * @param umic
	 * @param nombrePrograma
	 * @param exception
	 */
	public static void exceptionControladaPrograma(final DetalleBaseTecnica btc, final Umic umic, final String nombrePrograma, final Solvencia2Excepcion exception) {
		
		if (exception.getIncidencia().getBt() == null || exception.getIncidencia().getGeneradorError() == null) {
			exception.getIncidencia().setBt(btc.getBt());
			exception.getIncidencia().setGeneradorError(nombrePrograma);
		}
		throw exception;	
	}
}
