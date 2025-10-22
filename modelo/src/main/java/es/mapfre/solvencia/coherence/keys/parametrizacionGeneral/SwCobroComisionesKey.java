package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.SwCobroComisiones;

@Portable
public class SwCobroComisionesKey {
	
	@PortableProperty(SwCobroComisiones.IND_KPOLIZA) private Long kpoliza;
	
	@PortableProperty(SwCobroComisiones.IND_KSUBPOLIZA) private Integer ksubpoliza;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
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
		SwCobroComisionesKey other = (SwCobroComisionesKey) obj;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		return true;
	}
	
	public SwCobroComisionesKey() {
		super();
	}
	
	public SwCobroComisionesKey(Long kpoliza, Integer ksubpoliza) {
		
		super();
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
	}
}
