package es.mapfre.solvencia.coherence.keys.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.formulacion.PeriodosFall;

@Portable
public class PeriodosFallKey {
	
	@PortableProperty(PeriodosFall.FEC_DESDE)
	private Timestamp fecDesde;

	public PeriodosFallKey(Timestamp fecDesde) {
		this.fecDesde = fecDesde;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fecDesde == null) ? 0 : fecDesde.hashCode());
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
		PeriodosFallKey other = (PeriodosFallKey) obj;
		if (fecDesde == null) {
			if (other.fecDesde != null) {
				return false;
			}
		} else if (!fecDesde.equals(other.fecDesde)) {
			return false;
		}
		return true;
	}

	
}