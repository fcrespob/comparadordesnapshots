package es.mapfre.solvencia.formulacion.util;





/**
 * Clase de utilidades genericas
 * 
 */
public final class UtilBT {
	
		
	private UtilBT() { }
	

	
	/**
	 * Comprueba si el campo es distinto de null, vacio, ceros o espacios
	 * @param campo
	 * @return
	 */
	public static boolean campoDistintoCerosOEspacios(final String campo) {
		return campo != null && !campo.replaceAll(ConstantsBT.CTE_0_STRING, ConstantsBT.CTE_CADENA_VACIA).trim().isEmpty();
	}
	
}
