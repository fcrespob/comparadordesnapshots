package es.mapfre.solvencia.dominio.salidaCalculo;

import java.beans.Transient;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.IncidenciasMaestroKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class IncidenciasMaestro implements EntidadBase<IncidenciasMaestroKey> {
	
//	public static final int IND_CNEGOCIO      = 0;
//	public static final int IND_CCANAL	      = 1;
//	public static final int IND_CCARTERA      = 2;
//	public static final int IND_FECCIERRE     = 3;
//	public static final int IND_BT            = 4;
//	public static final int IND_CLAVEUMIC     = 5;
//	public static final int IND_PROGRAMA      = 6;
//	public static final int IND_CODIGORETORNO = 7;
//	public static final int IND_TIPOERROR     = 8;
//	public static final int IND_TEXTOERROR    = 9;
//	public static final int IND_INFAMPLIADA   = 10;
//	
//	@PortableProperty(IND_CNEGOCIO)      private String cnegocio;
//	@PortableProperty(IND_CCANAL)        private Integer ccanal;	
//	@PortableProperty(IND_CCARTERA)      private Integer ccartera;
//	@PortableProperty(IND_FECCIERRE)     private Timestamp fecCierre;
//	@PortableProperty(IND_BT)            private String bt;
//	@PortableProperty(IND_CLAVEUMIC)     private UmicKey claveUmic;
//	@PortableProperty(IND_PROGRAMA)      private String generadorError;
//	@PortableProperty(IND_CODIGORETORNO) private String codigoRetorno;
//	@PortableProperty(IND_TIPOERROR)     private String tipoError;
//	@PortableProperty(IND_TEXTOERROR)    private String textoError;
//	@PortableProperty(IND_INFAMPLIADA)   private String infAmpliada;
	
	public static final int IND_TEXTOERROR    = 0;
	@PortableProperty(IND_TEXTOERROR)    private String textoError;
	
	private Object[] args;
	
//	public String getCnegocio() {
//		return cnegocio;
//	}
//	public void setCnegocio(String cnegocio) {
//		this.cnegocio = cnegocio;
//	}
//	public Integer getCcanal() {
//		return ccanal;
//	}
//	public void setCcanal(Integer ccanal) {
//		this.ccanal = ccanal;
//	}
//	public Integer getCcartera() {
//		return ccartera;
//	}
//	public void setCcartera(Integer ccartera) {
//		this.ccartera = ccartera;
//	}
//	public Timestamp getFecCierre() {
//		return fecCierre;
//	}
//	public void setFecCierre(Timestamp fecCierre) {
//		this.fecCierre = fecCierre;
//	}
//	public String getBt() {
//		return bt;
//	}
//	public void setBt(String bt) {
//		this.bt = bt;
//	}
//	public UmicKey getClaveUmic() {
//		return claveUmic;
//	}
//	public void setClaveUmic(UmicKey claveUmic) {
//		this.claveUmic = claveUmic;
//	}
//	public String getGeneradorError() {
//		return generadorError;
//	}
//	public void setGeneradorError(String generadorError) {
//		this.generadorError = generadorError;
//	}
//	public String getCodigoRetorno() {
//		return codigoRetorno;
//	}
//	public void setCodigoRetorno(String codigoRetorno) {
//		this.codigoRetorno = codigoRetorno;
//	}
//	public String getTipoError() {
//		return tipoError;
//	}
//	public void setTipoError(String tipoError) {
//		this.tipoError = tipoError;
//	}
	public String getTextoError() {
		return textoError;
	}
	public void setTextoError(String textoError) {
		this.textoError = textoError;
	}
//	public String getInfAmpliada() {
//		return infAmpliada;
//	}
//	public void setInfAmpliada(String infAmpliada) {
//		this.infAmpliada = infAmpliada;
//	}
	
	@Transient
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((textoError == null) ? 0 : textoError.hashCode());
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
		IncidenciasMaestro other = (IncidenciasMaestro) obj;
		if (textoError == null) {
			if (other.textoError != null)
				return false;
		} else if (!textoError.equals(other.textoError))
			return false;
		return true;
	}
	
	public IncidenciasMaestroKey getKey() {
		return new IncidenciasMaestroKey(textoError);
	}
}