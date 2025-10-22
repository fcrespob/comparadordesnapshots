package es.mapfre.solvencia.dominio.salidaCalculo;

import java.beans.Transient;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciaKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class Incidencia implements EntidadConBaseTec, EntidadBase<IncidenciaKey>{
	
	public static final int IND_CNEGOCIO      = 0;
	public static final int IND_CCANAL	      = 1;
	public static final int IND_CCARTERA      = 2;
	public static final int IND_FECCIERRE     = 3;
	public static final int IND_BT            = 4;
	public static final int IND_CLAVEUMIC     = 5;
	public static final int IND_PROGRAMA      = 6;
	public static final int IND_CODIGORETORNO = 7;
	public static final int IND_TIPOERROR     = 8;
	public static final int IND_TEXTOERROR    = 9;
	public static final int IND_INFAMPLIADA   = 10;
	
	@PortableProperty(IND_CNEGOCIO)      private String cnegocio;
	@PortableProperty(IND_CCANAL)        private Integer ccanal;	
	@PortableProperty(IND_CCARTERA)      private Integer ccartera;
	@PortableProperty(IND_FECCIERRE)     private Timestamp fecCierre;
	@PortableProperty(IND_BT)            private String bt;
	@PortableProperty(IND_CLAVEUMIC)     private UmicKey claveUmic;
	@PortableProperty(IND_PROGRAMA)      private String generadorError;
	@PortableProperty(IND_CODIGORETORNO) private String codigoRetorno;
	@PortableProperty(IND_TIPOERROR)     private String tipoError;
	@PortableProperty(IND_TEXTOERROR)    private String textoError;
	@PortableProperty(IND_INFAMPLIADA)   private String infAmpliada;
	
	private Object[] args;
	
	public String getCnegocio() {
		return cnegocio;
	}
	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}
	public Integer getCcanal() {
		return ccanal;
	}
	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}
	public Integer getCcartera() {
		return ccartera;
	}
	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
	}
	public Timestamp getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}
	public String getBt() {
		return bt;
	}
	public void setBt(String bt) {
		this.bt = bt;
	}
	public UmicKey getClaveUmic() {
		return claveUmic;
	}
	public void setClaveUmic(UmicKey claveUmic) {
		this.claveUmic = claveUmic;
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
	
	@Transient
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result
				+ ((claveUmic == null) ? 0 : claveUmic.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
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
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (ccartera == null) {
			if (other.ccartera != null) {
				return false;
			}
		} else if (!ccartera.equals(other.ccartera)) {
			return false;
		}
		if (claveUmic == null) {
			if (other.claveUmic != null) {
				return false;
			}
		} else if (!claveUmic.equals(other.claveUmic)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
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
		return new IncidenciaKey(claveUmic, bt, generadorError, codigoRetorno);
	}
}