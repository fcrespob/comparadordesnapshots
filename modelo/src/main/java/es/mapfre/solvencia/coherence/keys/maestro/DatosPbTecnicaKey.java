package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.maestro.DatosPbTecnica;

@Portable
public class DatosPbTecnicaKey {
	
	@PortableProperty(DatosPbTecnica.IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(DatosPbTecnica.IND_KSUBPOL) private Integer ksubpol;

	public DatosPbTecnicaKey(Long kpoliza, Integer ksubpoliza) {
		this.kpoliza = kpoliza;
		this.ksubpol = ksubpoliza;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		DatosPbTecnicaKey other = (DatosPbTecnicaKey) obj;
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (ksubpol == null) {
			if (other.ksubpol != null) {
				return false;
			}
		} else if (!ksubpol.equals(other.ksubpol)) {
			return false;
		}
		return true;
	}
}