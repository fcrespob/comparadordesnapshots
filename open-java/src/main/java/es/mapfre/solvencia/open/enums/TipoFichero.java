/**
 * 
 */
package es.mapfre.solvencia.open.enums;

/**
 * @author amdepedro
 *
 */
public enum TipoFichero {
	
	MAESBTC("MAESBTC"),
	FLUJOSTOT("FLUJOSTOT"),
	INCIDENCIAS("INCIDENCIAS");
	
	private String tipo;
	
	private TipoFichero(String tipoParam) {
		this.tipo = tipoParam;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipoParam) {
		this.tipo = tipoParam;
	}

	public static TipoFichero getTipoFichero(String tipo) {
		TipoFichero[] tiposFicheros = TipoFichero.values();
		for (TipoFichero tipoFichero : tiposFicheros) {
			if (tipoFichero.getTipo().equals(tipo)) {
				return tipoFichero;
			}
		}
		return null;
	}
	
}
