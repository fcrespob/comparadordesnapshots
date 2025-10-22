package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.maestro.ValoresLiquidativos;

@Portable
public class ValoresLiquidativosKey {
	
	@PortableProperty(ValoresLiquidativos.IND_KPOLIZA)
	private Long kpoliza;

	public ValoresLiquidativosKey(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
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
		ValoresLiquidativosKey other = (ValoresLiquidativosKey) obj;
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		return true;
	}
}