package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.RentaGAP;

@Portable
public class RentaGAPKey   {
	@PortableProperty(RentaGAP.IND_KGAP) private String kgap;
	@PortableProperty(RentaGAP.IND_FINI) private Timestamp fini;

	public Timestamp getFini() {
		return fini;
	}

	public void setFini(Timestamp fini) {
		this.fini = fini;
	}

	public String getKgap() {
		return kgap;
	}

	public void setKgap(String kgap) {
		this.kgap = kgap;
	}

	public RentaGAPKey() {
		super();
	}
	
	public RentaGAPKey(String kgap, Timestamp fini) {
		super();
		this.kgap = kgap;
		this.fini = fini;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kgap == null) ? 0 : kgap.hashCode());
		result = prime * result
				+ ((fini == null) ? 0 : fini.hashCode());
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
		RentaGAPKey other = (RentaGAPKey) obj;
		if (kgap == null) {
			if (other.kgap != null) {
				return false;
			}
		} else if (!kgap.equals(other.kgap)) {
			return false;
		}
		if (fini == null) {
			if (other.fini != null) {
				return false;
			}
		} else if (!fini.equals(other.fini)) {
			return false;
		}
		return true;
	}
}
