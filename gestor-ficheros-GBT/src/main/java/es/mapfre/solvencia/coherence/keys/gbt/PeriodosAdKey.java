package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.PeriodosAd;

@Portable
public class PeriodosAdKey   {
	@PortableProperty(PeriodosAd.IND_KMADAPTAC) private String kmadaptac;
	@PortableProperty(PeriodosAd.IND_FINI) private Timestamp fini;
	public String getKmadaptac() {
		return kmadaptac;
	}
	public void setKmadaptac(String kmadaptac) {
		this.kmadaptac = kmadaptac;
	}
	public Timestamp getFini() {
		return fini;
	}
	public void setFini(Timestamp fini) {
		this.fini = fini;
	}
	public PeriodosAdKey(String kmadaptac, Timestamp fini) {
		super();
		this.kmadaptac = kmadaptac;
		this.fini = fini;
	}
	public PeriodosAdKey() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kmadaptac == null) ? 0 : kmadaptac.hashCode());
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
		PeriodosAdKey other = (PeriodosAdKey) obj;
		if (kmadaptac == null) {
			if (other.kmadaptac != null) {
				return false;
			}
		} else if (!kmadaptac.equals(other.kmadaptac)) {
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
