package es.mapfre.coaseguro.tirea.dominio.entidades;

import es.mapfre.coaseguro.tirea.dominio.entidades.Incidencias;

public class Incidencias {

	public static final int IND_FECCIERRE = 0;
	public static final int IND_CANAL = 1;
	public static final int IND_CODIGOERROR = 2;
	public static final int IND_DESCERRO = 3;

	private String feccierre;
	private String canal;
	private String codigoerror;
	private String descerror;

	public String getFeccierre() {
		return feccierre;
	}

	public void setCanal(String canal) {
		this.canal = canal;
	}

	public String getCanal() {
		return canal;
	}

	public void setFeccierre(String feccierre) {
		this.feccierre = feccierre;
	}
	
	public String getCodigoerror() {
		return codigoerror;
	}

	public void setCodigoerror(String codigoerror) {
		this.codigoerror = codigoerror;
	}

	public String getDescerror() {
		return descerror;
	}

	public void setDescerror(String descerror) {
		this.descerror = descerror;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoerror == null) ? 0 : codigoerror.hashCode());
		result = prime * result
				+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result
				+ ((descerror == null) ? 0 : descerror.hashCode());
		result = prime * result
				+ ((canal == null) ? 0 : canal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Incidencias other = (Incidencias) obj;
		if (codigoerror == null) {
			if (other.codigoerror != null)
				return false;
		} else if (!codigoerror.equals(other.codigoerror))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (descerror == null) {
			if (other.descerror != null)
				return false;
		} else if (!descerror.equals(other.descerror))
			return false;
		if (canal == null) {
			if (other.canal != null)
				return false;
		} else if (!canal.equals(other.canal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IncidenciasTExp [feccierre=");
		builder.append(feccierre);
		builder.append(", canal=");
		builder.append(canal);
		builder.append(", codigoerror=");
		builder.append(codigoerror);
		builder.append(", descerror=");
		builder.append(descerror);
		builder.append("]");
		return builder.toString();
	}
}
