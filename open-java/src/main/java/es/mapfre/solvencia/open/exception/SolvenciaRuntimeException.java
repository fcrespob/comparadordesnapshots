/**
 * 
 */
package es.mapfre.solvencia.open.exception;

/**
 * @author amdepedro
 * 
 */
public class SolvenciaRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SolvenciaRuntimeException() {
		super();
	}

	public SolvenciaRuntimeException(String s) {
		super(s);
	}

	public SolvenciaRuntimeException(String s, Throwable throwable) {
		super(s, throwable);
	}

	public SolvenciaRuntimeException(Throwable throwable) {
		super(throwable);
	}

}
