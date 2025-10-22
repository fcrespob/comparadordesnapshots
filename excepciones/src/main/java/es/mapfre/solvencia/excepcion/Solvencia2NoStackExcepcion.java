package es.mapfre.solvencia.excepcion;

import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;

/**
 * Excepción a usar cuando no se quiere guardar la pila para ahorrar tiempo de
 * ejecución.
 */
public class Solvencia2NoStackExcepcion extends Solvencia2Excepcion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3213995784689386536L;

	public Solvencia2NoStackExcepcion(Incidencia incidencia) {
		super(incidencia);
	}

	public Solvencia2NoStackExcepcion(String mensaje, Incidencia incidencia) {
		super(mensaje, incidencia);
	}

	public Solvencia2NoStackExcepcion(Throwable cause, Incidencia incidencia) {
		super(cause, incidencia);
	}

	public Solvencia2NoStackExcepcion(String mensaje, Incidencia incidencia, Throwable cause) {
		super(mensaje, incidencia, cause);
	}

	/**
	 * No rellenamos el stacktrace para mejorar el tiempo de creación de la
	 * excepción
	 **/
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}
	
	private static final StackTraceElement[] UNASSIGNED_STACK_TRACE = new StackTraceElement[0];

	@Override
	public StackTraceElement[] getStackTrace() {
		return UNASSIGNED_STACK_TRACE;
	}
}
