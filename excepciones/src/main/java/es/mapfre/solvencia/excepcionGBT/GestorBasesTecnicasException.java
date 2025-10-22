package es.mapfre.solvencia.excepcionGBT;

import com.tangosol.io.pof.annotation.PortableProperty;

public class GestorBasesTecnicasException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public static final int IND_PROGRAMA      = 0;
	public static final int IND_CODIGORETORNO = 1;
	public static final int IND_TIPOERROR     = 2;
	public static final int IND_TEXTOERROR    = 3;
	public static final int IND_INFAMPLIADA   = 4;

	@PortableProperty(IND_PROGRAMA)      private String generadorError;
	@PortableProperty(IND_CODIGORETORNO) private String codigoRetorno;
	@PortableProperty(IND_TIPOERROR)     private String tipoError;
	@PortableProperty(IND_TEXTOERROR)    private String textoError;
	@PortableProperty(IND_INFAMPLIADA)   private String infAmpliada;
	
	public GestorBasesTecnicasException(String arg0) {
		super(arg0);
	}
	
	public GestorBasesTecnicasException() {
		super();
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoRetorno == null) ? 0 : codigoRetorno.hashCode());
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
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GestorBasesTecnicasException other = (GestorBasesTecnicasException) obj;
		if (codigoRetorno == null) {
			if (other.codigoRetorno != null)
				return false;
		} else if (!codigoRetorno.equals(other.codigoRetorno))
			return false;
		if (generadorError == null) {
			if (other.generadorError != null)
				return false;
		} else if (!generadorError.equals(other.generadorError))
			return false;
		if (infAmpliada == null) {
			if (other.infAmpliada != null)
				return false;
		} else if (!infAmpliada.equals(other.infAmpliada))
			return false;
		if (textoError == null) {
			if (other.textoError != null)
				return false;
		} else if (!textoError.equals(other.textoError))
			return false;
		if (tipoError == null) {
			if (other.tipoError != null)
				return false;
		} else if (!tipoError.equals(other.tipoError))
			return false;
		return true;
	}
	
}
