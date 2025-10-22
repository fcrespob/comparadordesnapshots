package es.mapfre.solvencia.dominio.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.AdapTablaExpKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class AdapTablaExp implements EntidadBase<AdapTablaExpKey> {
	public static final int IND_KTIPOBT = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_FINI = 2;
	public static final int IND_FFIN = 3;
	public static final int IND_KMADAPTAC = 4;
	
	@PortableProperty(IND_KMADAPTAC) private String kmadaptac;
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_KTIPOBT) private String ktipobt;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	
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

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

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

	public static int getIndKtipobt() {
		return IND_KTIPOBT;
	}

	public static int getIndKmodalidad() {
		return IND_KMODALIDAD;
	}

	public static int getIndFini() {
		return IND_FINI;
	}

	public static int getIndFfin() {
		return IND_FFIN;
	}

	public static int getIndKmadaptac() {
		return IND_KMADAPTAC;
	}

	@Override
	public AdapTablaExpKey getKey() {
		return new AdapTablaExpKey(ktipobt, kmodalidad, fini);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kmadaptac == null) ? 0 : kmadaptac.hashCode());
		result = prime * result
				+ ((fini == null) ? 0 : fini.hashCode());
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result
				+ ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result 
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		AdapTablaExp other = (AdapTablaExp) obj;
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
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
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
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdapTablaExp [kmadaptac=");
		builder.append(kmadaptac);
		builder.append(", fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", ktipobt=");
		builder.append(ktipobt);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append("]");
		return builder.toString();
	}

	
}
