package es.mapfre.solvencia.coherence.keys.salidaCalculo;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;

@Portable
public class DetalleBaseTecnicaKey implements Comparable<DetalleBaseTecnicaKey> {
	
	@PortableProperty(DetalleBaseTecnica.IND_UMICKEY) private UmicKey umicKey;
	
	@PortableProperty(DetalleBaseTecnica.IND_BASETEC) private String baseTec;
	
	public DetalleBaseTecnicaKey() {
		super();
	}
	
	public UmicKey getUmicKey() {
		return umicKey;
	}
	
	public String getBaseTec() {
		return baseTec;
	}
	
	public DetalleBaseTecnicaKey(UmicKey umicKey, String base_tec) {
		super();
		this.umicKey = umicKey;
		this.baseTec = base_tec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((baseTec == null) ? 0 : baseTec.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
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
		DetalleBaseTecnicaKey other = (DetalleBaseTecnicaKey) obj;
		if (baseTec == null) {
			if (other.baseTec != null) {
				return false;
			}
		} else if (!baseTec.equals(other.baseTec)) {
			return false;
		}
		if (umicKey == null) {
			if (other.umicKey != null) {
				return false;
			}
		} else if (!umicKey.equals(other.umicKey)) {
			return false;
		}
		return true;
	}

	@Override
	public int compareTo(DetalleBaseTecnicaKey o) {
		if (o == null) {
			return -1;
		}
		int comp = this.umicKey.compareTo(o.umicKey);
		if (comp == 0) {
			comp = this.baseTec.compareTo(o.baseTec);
		}
		return comp;
	}
}