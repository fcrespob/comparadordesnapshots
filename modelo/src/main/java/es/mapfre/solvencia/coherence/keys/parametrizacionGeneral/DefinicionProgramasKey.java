package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionProgramas;

@Portable
public class DefinicionProgramasKey {

	@PortableProperty(DefinicionProgramas.IND_KMODULO) private String kmodulo;
	@PortableProperty(DefinicionProgramas.IND_CIDENTIF) private String cidentif;
	
	
	
	public DefinicionProgramasKey(String kmodulo, String cidentif) {
		super();
		this.kmodulo = kmodulo;
		this.cidentif = cidentif;
	}
	
	public String getKmodulo() {
		return kmodulo;
	}
	public void setKmodulo(String kmodulo) {
		this.kmodulo = kmodulo;
	}
	public String getCidentif() {
		return cidentif;
	}
	public void setCidentif(String cidentif) {
		this.cidentif = cidentif;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cidentif == null) ? 0 : cidentif.hashCode());
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
		DefinicionProgramasKey other = (DefinicionProgramasKey) obj;
		if (cidentif == null) {
			if (other.cidentif != null){
				{ return false; }
			}	
		} else if (!cidentif.equals(other.cidentif))
			{ return false; }
		if (kmodulo == null) {
			if (other.kmodulo != null){
				{ return false; }
			}	
		} else if (!kmodulo.equals(other.kmodulo)){
			{ return false; }
		}
		
		{ return true; }
	}
	
	public DefinicionProgramasKey() {
		super();
	}
	
}
