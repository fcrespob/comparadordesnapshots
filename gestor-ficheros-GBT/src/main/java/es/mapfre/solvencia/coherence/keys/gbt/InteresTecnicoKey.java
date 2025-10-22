package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.InteresTecnico;

@Portable
public class InteresTecnicoKey   {
	@PortableProperty(InteresTecnico.IND_FINI) private Timestamp fini;

	public Timestamp getFini() {
		return fini;
	}

	public void setFini(Timestamp fini) {
		this.fini = fini;
	}

	public InteresTecnicoKey(Timestamp fini) {
		super();
		this.fini = fini;
	}

	public InteresTecnicoKey() {
		super();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fini == null) ? 0 : fini.hashCode());
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
		InteresTecnicoKey other = (InteresTecnicoKey) obj;
		if (fini == null) {
			if (other.fini != null) {
				return false;
			}
		} else if (!fini.equals(other.fini)) {
			return false;
		}
		return true;
	}
}
