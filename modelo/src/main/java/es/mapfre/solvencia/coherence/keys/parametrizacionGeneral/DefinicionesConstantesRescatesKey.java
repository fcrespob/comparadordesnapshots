package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesConstantesRescates;

@Portable
public class DefinicionesConstantesRescatesKey {
	
	@PortableProperty(DefinicionesConstantesRescates.IND_KCONSTANTE) private String kconstante;
	
	public DefinicionesConstantesRescatesKey (String kconstante) {
		super();
		this.kconstante = kconstante;
	}
	
	public DefinicionesConstantesRescatesKey(){
		super();
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kconstante == null) ? 0 : kconstante.hashCode());
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
		DefinicionesConstantesRescatesKey other = (DefinicionesConstantesRescatesKey) obj;
		if (kconstante == null) {
			if (other.kconstante != null) {
				return false;
			}
		} else if (!kconstante.equals(other.kconstante)) {
			return false;
		}
		return true;
	}
}