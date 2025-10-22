package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionProgramasKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DefinicionProgramas implements EntidadBase<DefinicionProgramasKey>{

	public static final int IND_KMODULO = 0;
	public static final int IND_GDESCRIP = 1;
	public static final int IND_CIDENTIF = 2;
	public static final int IND_GRUTADOC = 3;
	public static final int IND_KESTADO = 4;

	
	@PortableProperty(IND_KMODULO) private String kmodulo;
	@PortableProperty(IND_GDESCRIP) private String gdescrip;
	@PortableProperty(IND_CIDENTIF) private String cidentif;
	@PortableProperty(IND_GRUTADOC) private String grutadoc;
	@PortableProperty(IND_KESTADO) private Boolean kestado;

	
	public String getKmodulo() {
		return kmodulo;
	}
	public void setKmodulo(String kmodulo) {
		this.kmodulo = kmodulo;
	}
	public String getGdescrip() {
		return gdescrip;
	}
	public void setGdescrip(String gdescrip) {
		this.gdescrip = gdescrip;
	}
	public String getCidentif() {
		return cidentif;
	}
	public void setCidentif(String cidentif) {
		this.cidentif = cidentif;
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
	public DefinicionProgramasKey getKey() {
		
		return new DefinicionProgramasKey(kmodulo, cidentif);
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cidentif == null) ? 0 : cidentif.hashCode());
		result = prime * result
				+ ((gdescrip == null) ? 0 : gdescrip.hashCode());
		result = prime * result
				+ ((grutadoc == null) ? 0 : grutadoc.hashCode());
		result = prime * result + ((kestado == null) ? 0 : kestado.hashCode());
		result = prime * result + ((kmodulo == null) ? 0 : kmodulo.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj){
			{ return true; }
		}
		if (obj == null){
			{ return false; }
		}	
		if (getClass() != obj.getClass()){
			{ return false; }
		}	
		DefinicionProgramas other = (DefinicionProgramas) obj;
		if (cidentif == null) {
			if (other.cidentif != null){
				{ return false; }
			}	
		} else if (!cidentif.equals(other.cidentif)){
			{ return false; }
		}	
		if (gdescrip == null) {
			if (other.gdescrip != null){
				{ return false; }
			}	
		} else if (!gdescrip.equals(other.gdescrip)){
			{ return false; }
		}	
		if (grutadoc == null) {
			if (other.grutadoc != null){
				{ return false; }
			}	
		} else if (!grutadoc.equals(other.grutadoc)){
			{ return false; }
		}	
		if (kestado == null) {
			if (other.kestado != null){
				{ return false; }
			}	
		} else if (!kestado.equals(other.kestado)){
			{ return false; }
		}	
		if (kmodulo == null) {
			if (other.kmodulo != null){
				{ return false; }
			}	
		} else if (!kmodulo.equals(other.kmodulo)){
			{ return false; }
		}
		
		{ return true; }
	}

	
}
