package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DefinicionProcesos implements EntidadBase<String>{

	public static final int IND_CPROCESO = 0;
	public static final int IND_GDESCRIP = 1;
	public static final int IND_CDESCRIPABREV = 2;
	public static final int IND_GRUTADOC = 3;
	public static final int IND_KESTADO = 4;

	@PortableProperty(IND_CPROCESO) private String cproceso;
	@PortableProperty(IND_GDESCRIP) private String gdescrip;
	@PortableProperty(IND_CDESCRIPABREV) private String cdescripabrev;
	@PortableProperty(IND_GRUTADOC) private String grutadoc;
	@PortableProperty(IND_KESTADO) private Boolean kestado;

	
	public String getCproceso() {
		return cproceso;
	}
	public void setCproceso(String cproceso) {
		this.cproceso = cproceso;
	}
	public String getGdescrip() {
		return gdescrip;
	}
	public void setGdescrip(String gdescrip) {
		this.gdescrip = gdescrip;
	}
	public String getCdescripabrev() {
		return cdescripabrev;
	}
	public void setCdescripabrev(String cdescripabrev) {
		this.cdescripabrev = cdescripabrev;
	}
	public String getGrutadoc() {
		return grutadoc;
	}
	public void setGrutadoc(String grutadoc) {
		this.grutadoc = grutadoc;
	}
	public Boolean getKestado() {
		return kestado;
	}
	public void setKestado(Boolean kestado) {
		this.kestado = kestado;
	}
	@Override
	public String getKey() {
		return getCproceso();
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cdescripabrev == null) ? 0 : cdescripabrev.hashCode());
		result = prime * result
				+ ((cproceso == null) ? 0 : cproceso.hashCode());
		result = prime * result
				+ ((gdescrip == null) ? 0 : gdescrip.hashCode());
		result = prime * result
				+ ((grutadoc == null) ? 0 : grutadoc.hashCode());
		result = prime * result + ((kestado == null) ? 0 : kestado.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			{ return true; }
		if (obj == null)
			{ return false; }
		if (getClass() != obj.getClass())
			{ return false; }
		DefinicionProcesos other = (DefinicionProcesos) obj;
		if (cdescripabrev == null) {
			if (other.cdescripabrev != null)
				{ return false; }
		} else if (!cdescripabrev.equals(other.cdescripabrev))
			{ return false; }
		if (cproceso == null) {
			if (other.cproceso != null)
				{ return false; }
		} else if (!cproceso.equals(other.cproceso))
			{ return false; }
		if (gdescrip == null) {
			if (other.gdescrip != null)
				{ return false; }
		} else if (!gdescrip.equals(other.gdescrip))
			{ return false; }
		if (grutadoc == null) {
			if (other.grutadoc != null)
				{ return false; }
		} else if (!grutadoc.equals(other.grutadoc))
			{ return false; }
		if (kestado == null) {
			if (other.kestado != null)
				{ return false; }
		} else if (!kestado.equals(other.kestado))
			{ return false; }
		{ return true; }
	}

	
		
}
