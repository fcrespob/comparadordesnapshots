package es.mapfre.solvencia.formulacion.exception;

/**
 * Excepcion generica con codigo asociado.
 * 
 * @author rschacon
 *
 */
public class BaseCodedException extends RuntimeException {
    /**.
     * serialVersionUID
     */
    private static final long serialVersionUID = -3870041823028691694L;
    /**.
     * Codigo de Excepcion
     */
    private String code = "";
    /**.
     * Detalle de la Excepcion
     */
    private String detail = "";
    /**.
     * Constructor
     */
    public BaseCodedException() {
    super();
    }
    /**.
     * Constructor
     * @param code codigo de error
     * @param message Mensaje de error
     */
    public BaseCodedException(final String code, final String message) {
        super(message);
        this.code = code;
    }
    /**.
     * Constructor
     * @param code codigo de error
     * @param cause Causa
     */
    public BaseCodedException(final String code, final Throwable cause) {
        super(cause);
        this.code = code;
    }
    /**.
     * Constructor
     * @param code codigo de error
     * @param message Mensaje
     * @param cause Causa
     */
    public BaseCodedException(final String code, final String message,
    final Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    /**.
     * Constructor
     * @param code codigo de error
     * @param message Mensaje
     * @param detail Detalle de error
     */
    public BaseCodedException(final String code, final String message,
    final String detail) {
        super(message);
        this.code = code;
        this.detail = detail;
    }
    /**.
     * Constructor
     * @param code codigo de error
     * @param message Mensaje
     * @param detail Detalle de error
     * @param cause Causa
     */
    public BaseCodedException(final String code, final String message,
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
