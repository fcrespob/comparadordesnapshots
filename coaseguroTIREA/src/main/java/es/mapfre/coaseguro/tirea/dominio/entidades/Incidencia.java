package es.mapfre.coaseguro.tirea.dominio.entidades;

import es.mapfre.coaseguro.tirea.dominio.keys.IncidenciaKey;

public class Incidencia {

	public static final int IND_CCANAL         = 1;
	public static final int IND_FECCIERRE      = 2;
	public static final int IND_SISTEMA        = 3;
	public static final int IND_GENERADORERROR = 4;
	public static final int IND_CODIGORETORNO  = 5;
	public static final int IND_TIPOERROR      = 6;
	public static final int IND_TEXTOERROR     = 7;
	public static final int IND_INFAMPLIADA    = 8;

	private String ccanal;
	private String fecCierre;
	private String sistema;
	private String generadorError;
	private String codigoRetorno;
	private String tipoError;
	private String textoError;
	private String infAmpliada;

	public String getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(String fecCierre) {
		this.fecCierre = fecCierre;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	public String getCcanal() {
		return ccanal;
	}
	public void setCcanal(String ccanal) {
		this.ccanal = ccanal;
	}
	public String getGeneradorError() {
		return generadorError;
	}
	public void setGeneradorError(String generadorError) {
		this.generadorError = generadorError;
	}
	public String getCodigoRetorno() {
		return codigoRetorno;
	}
	public void setCodigoRetorno(String codigoRetorno) {
		this.codigoRetorno = codigoRetorno;
	}
	public String getTipoError() {
		return tipoError;
	}
	public void setTipoError(String tipoError) {
		this.tipoError = tipoError;
	}
	public String getTextoError() {
		return textoError;
	}
	public void setTextoError(String textoError) {
		this.textoError = textoError;
	}
	public String getInfAmpliada() {
		return infAmpliada;
	}
	public void setInfAmpliada(String infAmpliada) {
		this.infAmpliada = infAmpliada;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((sistema == null) ? 0 : sistema.hashCode());
		result = prime * result
				+ ((codigoRetorno == null) ? 0 : codigoRetorno.hashCode());
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result
				+ ((generadorError == null) ? 0 : generadorError.hashCode());
		result = prime * result
				+ ((infAmpliada == null) ? 0 : infAmpliada.hashCode());
		result = prime * result
				+ ((textoError == null) ? 0 : textoError.hashCode());
		result = prime * result
				+ ((tipoError == null) ? 0 : tipoError.hashCode());
		return result;
	}
	
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Incidencia other = (Incidencia) obj;
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (sistema == null) {
			if (other.sistema != null) {
				return false;
			}
		} else if (!sistema.equals(other.sistema)) {
			return false;
		}
		if (codigoRetorno == null) {
			if (other.codigoRetorno != null) {
				return false;
			}
		} else if (!codigoRetorno.equals(other.codigoRetorno)) {
			return false;
		}
		if (fecCierre == null) {
			if (other.fecCierre != null) {
				return false;
			}
		} else if (!fecCierre.equals(other.fecCierre)) {
			return false;
		}
		if (generadorError == null) {
			if (other.generadorError != null) {
				return false;
			}
		} else if (!generadorError.equals(other.generadorError)) {
			return false;
		}
		if (infAmpliada == null) {
			if (other.infAmpliada != null) {
				return false;
			}
		} else if (!infAmpliada.equals(other.infAmpliada)) {
			return false;
		}
		if (textoError == null) {
			if (other.textoError != null) {
				return false;
			}
		} else if (!textoError.equals(other.textoError)) {
			return false;
		}
		if (tipoError == null) {
			if (other.tipoError != null) {
				return false;
			}
		} else if (!tipoError.equals(other.tipoError)) {
			return false;
		}
		return true;
	}

	public IncidenciaKey getKey() {
		return new IncidenciaKey(ccanal, sistema, generadorError, codigoRetorno, fecCierre);
	}
}
