package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.AdapTablaExp;

@Portable
public class AdapTablaExpKey {
	@PortableProperty(AdapTablaExp.IND_KMADAPTAC) private String ktipobt;
	@PortableProperty(AdapTablaExp.IND_KMADAPTAC) private Integer kmodalidad;
	@PortableProperty(AdapTablaExp.IND_FINI) private Timestamp fini;
	public String getKtipobt() {
		return ktipobt;
	}
	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public Timestamp getFini() {
		return fini;
	}
	public void setFini(Timestamp fini) {
		this.fini = fini;
	}
	public AdapTablaExpKey() {
		super();
	}
	public AdapTablaExpKey(String ktipobt, Integer kmodalidad, Timestamp fini) {
		super();
		this.ktipobt = ktipobt;
		this.kmodalidad = kmodalidad;
		this.fini = fini;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		AdapTablaExpKey other = (AdapTablaExpKey) obj;
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
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
