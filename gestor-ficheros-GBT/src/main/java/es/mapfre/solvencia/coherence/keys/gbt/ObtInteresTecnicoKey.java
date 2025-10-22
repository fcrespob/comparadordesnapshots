package es.mapfre.solvencia.coherence.keys.gbt;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.ObtInteresTecnico;

@Portable
public class ObtInteresTecnicoKey   {
	@PortableProperty(ObtInteresTecnico.IND_KCRITERIOIT) private String kcriterioit;

	public String getKcriterioit() {
		return kcriterioit;
	}

	public void setKcriterioit(String kcriterioit) {
		this.kcriterioit = kcriterioit;
	}

	public ObtInteresTecnicoKey() {
		super();
	}

	public ObtInteresTecnicoKey(String kcriterioit) {
		super();
		this.kcriterioit = kcriterioit;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kcriterioit == null) ? 0 : kcriterioit.hashCode());
		return result;
	}	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObtInteresTecnicoKey other = (ObtInteresTecnicoKey) obj;
		if (kcriterioit == null) {
			if (other.kcriterioit != null) {
				return false;
			}
		} else if (!kcriterioit.equals(other.kcriterioit)) {
			return false;
		}
		return true;
	}
}
