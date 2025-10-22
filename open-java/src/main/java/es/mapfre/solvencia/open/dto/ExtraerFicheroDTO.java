/**
 * 
 */
package es.mapfre.solvencia.open.dto;

import java.io.Serializable;
import java.sql.Date;

import es.mapfre.solvencia.open.enums.TipoFichero;

/**
 * @author amdepedro
 *
 */
public class ExtraerFicheroDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String dir;
	
	private Integer canal;
	
	private String negocio;
	
	private String bt;
	
	private Date fechaCierre;
	
	private String nombreFichero;
	
	private String tipoEjec;
	
	//indica si la extraccion es de MAESBTC o FLUJOSTOT o INCIDENCIAS
	private TipoFichero tipoFichero;

	public String getDir() {
		return dir;
	}

	public void setDir(String dirParam) {
		this.dir = dirParam;
	}

	public Integer getCanal() {
		return canal;
	}

	public void setCanal(Integer canalParam) {
		this.canal = canalParam;
	}

	public String getNegocio() {
		return negocio;
	}

	public void setNegocio(String negocioParam) {
		this.negocio = negocioParam;
	}

	public String getNombreFichero() {
		return nombreFichero;
	}

	public void setNombreFichero(String nombreFicheroParam) {
		this.nombreFichero = nombreFicheroParam;
	}

	public String getTipoEjec() {
		return tipoEjec;
	}

	public void setTipoEjec(String tipoEjecParam) {
		this.tipoEjec = tipoEjecParam;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String btParam) {
		this.bt = btParam;
	}

	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierreParam) {
		this.fechaCierre = fechaCierreParam;
	}

	public TipoFichero getTipoFichero() {
		return tipoFichero;
	}

	public void setTipoFichero(TipoFichero tipoFicheroParam) {
		this.tipoFichero = tipoFicheroParam;
	}

	@Override
	public String toString() {
		return "ExtraccionFicheroDTO [dir=" + dir + ", canal=" + canal + ", negocio=" + negocio + ", bt=" + bt
				+ ", fechaCierre=" + fechaCierre + ", nombreFichero=" + nombreFichero + ", tipoEjec=" + tipoEjec + "]";
	}

}
