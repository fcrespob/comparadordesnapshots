package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesSolvencia2;

@Portable
public class DefinicionesSolvencia2Key {

	@PortableProperty(DefinicionesSolvencia2.IND_KCARTEORIG) private Integer kcarteorig;
	@PortableProperty(DefinicionesSolvencia2.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(DefinicionesSolvencia2.IND_KGARANTIA) private Integer kgarantia;
	
	public DefinicionesSolvencia2Key(Integer kcarteorig, Integer kmodalidad,
			Integer kgarantia) {
		super();
		this.kcarteorig = kcarteorig;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kcarteorig == null) ? 0 : kcarteorig.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		DefinicionesSolvencia2Key other = (DefinicionesSolvencia2Key) obj;
		if (kcarteorig == null) {
			if (other.kcarteorig != null) {
				return false;
			}
		} else if (!kcarteorig.equals(other.kcarteorig)) {
			return false;
		}
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		return true;
	}

	public DefinicionesSolvencia2Key() {
		super();
	}
}
