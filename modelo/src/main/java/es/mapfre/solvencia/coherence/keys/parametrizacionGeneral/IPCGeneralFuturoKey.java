package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.IPCGeneralFuturo;

@Portable
public class IPCGeneralFuturoKey {

	@PortableProperty(IPCGeneralFuturo.IND_FFIN) private Timestamp ffin;
		
	public IPCGeneralFuturoKey(Timestamp ffin) {
		super();
		this.ffin = ffin;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ffin == null) ? 0 : ffin.hashCode());
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
		IPCGeneralFuturoKey other = (IPCGeneralFuturoKey) obj;
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		return true;
	}

	public IPCGeneralFuturoKey() {
		super();
	}
}