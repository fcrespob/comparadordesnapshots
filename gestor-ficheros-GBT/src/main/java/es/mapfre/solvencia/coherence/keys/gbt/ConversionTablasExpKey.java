package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.ConversionTablasExp;

@Portable
public class ConversionTablasExpKey   {
	@PortableProperty(ConversionTablasExp.IND_KTIPOBT) private String ktipobt;
	@PortableProperty(ConversionTablasExp.IND_KTABLAEXP) private Integer ktablaexp;
	@PortableProperty(ConversionTablasExp.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(ConversionTablasExp.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(ConversionTablasExp.IND_FINI) private Timestamp fini;
	
	public String getKtipobt() {
		return ktipobt;
	}
	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}
	public Integer getKtablaexp() {
		return ktablaexp;
	}
	public void setKtablaexp(Integer ktablaexp) {
		this.ktablaexp = ktablaexp;
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
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result
				+ ((ktablaexp == null) ? 0 : ktablaexp.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((fini == null) ? 0 : fini.hashCode());
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
		ConversionTablasExpKey other = (ConversionTablasExpKey) obj;
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		if (ktablaexp == null) {
			if (other.ktablaexp != null) {
				return false;
			}
		} else if (!ktablaexp.equals(other.ktablaexp)) {
			return false;
		}
		if (fini == null) {
			if (other.fini != null) {
				return false;
			}
		} else if (!fini.equals(other.fini)) {
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
	public ConversionTablasExpKey(String ktipobt, Integer ktablaexp,
			Integer kmodalidad, Integer kgarantia, Timestamp fini) {
		super();
		this.ktipobt = ktipobt;
		this.ktablaexp = ktablaexp;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.fini = fini;
	}
	public ConversionTablasExpKey() {
		super();
	}
}
