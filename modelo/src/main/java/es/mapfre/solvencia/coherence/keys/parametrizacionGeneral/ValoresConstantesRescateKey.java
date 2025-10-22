package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresConstantesRescate;

@Portable
public class ValoresConstantesRescateKey {
	
	@PortableProperty(ValoresConstantesRescate.IND_KK1) private String kk1;
	
	@PortableProperty(ValoresConstantesRescate.IND_KDURACION) private Integer kduracion;

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kduracion == null) ? 0 : kduracion.hashCode());
		result = prime * result + ((kk1 == null) ? 0 : kk1.hashCode());
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
		ValoresConstantesRescateKey other = (ValoresConstantesRescateKey) obj;
		if (kduracion == null) {
			if (other.kduracion != null) {
				return false;
			}
		} else if (!kduracion.equals(other.kduracion)) {
			return false;
		}
		if (kk1 == null) {
			if (other.kk1 != null) {
				return false;
			}
		} else if (!kk1.equals(other.kk1)) {
			return false;
		}
		return true;
	}
	
	public ValoresConstantesRescateKey() {
		super();
	}
	
	public ValoresConstantesRescateKey(String kk1, Integer kduracion) {
		
		super();
		this.kduracion = kduracion;
		this.kk1 = kk1;
	}
}
