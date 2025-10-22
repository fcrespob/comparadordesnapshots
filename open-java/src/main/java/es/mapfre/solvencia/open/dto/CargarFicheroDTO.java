/**
 * 
 */
package es.mapfre.solvencia.open.dto;

import java.io.Serializable;

import es.mapfre.solvencia.open.enums.TipoFichero;

/**
 * @author amdepedro
 *
 */
public class CargarFicheroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dir;
	
	private String fich;
	
	//indica si la carga es de MAESBTC o FLUJOSTOT o INCIDENCIAS
	private TipoFichero tipoFichero;

	public String getDir() {
		return dir;
	}

	public void setDir(String dirParam) {
		this.dir = dirParam;
	}

	public String getFich() {
		return fich;
	}

	public void setFich(String fichParam) {
		this.fich = fichParam;
	}

	public TipoFichero getTipoFichero() {
		return tipoFichero;
	}

	public void setTipoFichero(TipoFichero tipoFicheroParam) {
		this.tipoFichero = tipoFicheroParam;
	}

	@Override
	public String toString() {
		return "CargarFicheroDTO [dir=" + dir + ", fich=" + fich + ", tipoFichero=" + tipoFichero + "]";
	}

}
