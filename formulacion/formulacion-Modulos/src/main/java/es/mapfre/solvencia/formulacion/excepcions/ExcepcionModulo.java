package es.mapfre.solvencia.formulacion.excepcions;

/**
 * Clase encargada de gestionar las excepciones a nivel de modulo.
 * @author rschacon
 *
 */
public class ExcepcionModulo extends RuntimeException {

	/**.
	 * Serial version
	 */
	private static final long serialVersionUID = 6456874813671265890L;

	/**.
     * Codigo de excepcion
     */
    private String code = "";

    /**.
     * Detalle de la excepcion
     */
    private String detail = "";

    /**.
     * Constructor
     */
    public ExcepcionModulo() {
    	super();
    	//Constructor vacio
    }

    /**.
     * Constructor
     * @param code Codigo de error
     * @param message Mensaje de error
     */
    public ExcepcionModulo(final String code,
    		final String message) {
        super(message);
        this.code = code;
    }

    /**.
     * Constructor
     * @param code Codigo de error
     * @param cause Causa
     */
    public ExcepcionModulo(final String code,
    		final Throwable cause) {
        super(cause);
        this.code = code;
    }

    /**.
     * Constructor
     * @param code Codigo de error
     * @param message Mensaje
     * @param cause Causa
     */
    public ExcepcionModulo(final String code,
    		final String message,
    		final Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**.
     * Constructor
     * @param code Codigo de error
     * @param message Mensaje
     * @param detail Detalle de error
     */
    public ExcepcionModulo(final String code,
    		final String message,
    		final String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }

    /**.
     * Constructor
     * @param code Codigo de error
     * @param message Mensaje
     * @param detail Detalle de error
     * @param cause Causa
     */
    public ExcepcionModulo(final String code,
    		final String message,
    		final String detail,
    		final Throwable cause) {
        super(message, cause);
        this.code = code;
        this.detail = detail;
    }

    /**.
     * -
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**.
     * -
     * @return the detail
     */
    public String getDetail() {
        return this.detail;
    }

    /**.
     * -
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**.
     * -
     * @param detail the detail to set
     */
    public void setDetail(final String detail) {
        this.detail = detail;
    }
}

