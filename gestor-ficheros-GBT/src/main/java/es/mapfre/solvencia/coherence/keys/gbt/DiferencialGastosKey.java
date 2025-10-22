package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.DiferencialGastos;

@Portable
public class DiferencialGastosKey   {
	
	@PortableProperty(DiferencialGastos.IND_KTIPOBT) private String ktipobt;
	@PortableProperty(DiferencialGastos.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(DiferencialGastos.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(DiferencialGastos.IND_FINI) private Timestamp fini;
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
	public Integer getKgarantia() {
		return kgarantia;
	}
	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}
	public Timestamp getFini() {
		return fini;
	}
	public void setFini(Timestamp fini) {
		this.fini = fini;
	}
	public DiferencialGastosKey() {
		super();
	}
	public DiferencialGastosKey(String ktipobt, Integer kmodalidad,
			Integer kgarantia, Timestamp fini) {
		super();
		this.ktipobt = ktipobt;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
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
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
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
		DiferencialGastosKey other = (DiferencialGastosKey) obj;
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
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
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
