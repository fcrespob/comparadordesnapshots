package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.BasetecKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;

@Portable
public class PolizasTipoKey implements Comparable<PolizasTipoKey> {
	
	@PortableProperty(PolizasTipo.IND_CLAVEUMIC) private UmicKey claveUmic;
	
	public PolizasTipoKey() {
		super();
	}

	public PolizasTipoKey(UmicKey claveUmic) {
		super();
		this.claveUmic = claveUmic;
	}
	
	public UmicKey getClaveUmic() {
		return claveUmic;
	}

	public void setClaveUmic(UmicKey claveUmic) {
		this.claveUmic = claveUmic;
	}

	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((claveUmic == null) ? 0 : claveUmic.hashCode());
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
		PolizasTipoKey other = (PolizasTipoKey) obj;
		if (claveUmic == null) {
			if (other.claveUmic != null) {
				return false;
			}
		} else if (!claveUmic.equals(other.claveUmic)) {
			return false;
		}
		return true;
	}
	
	@Override
	public int compareTo(PolizasTipoKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.claveUmic, o.claveUmic);


		return compareToBuilder.toComparison();
	}
}