package es.mapfre.solvencia.dominio.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.PeriodosAdKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class PeriodosAd implements EntidadBase<PeriodosAdKey> {
	public static final int IND_KMADAPTAC = 0;
	public static final int IND_FINI = 1;
	public static final int IND_FFIN = 2;
	public static final int IND_NDOTACION = 3;
	public static final int IND_NPENDIENTE = 4;
	public static final int IND_NDIVISOR = 5;
	
	@PortableProperty(IND_KMADAPTAC) private String kmadaptac;
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_NDOTACION) private Integer ndotacion;
	@PortableProperty(IND_NPENDIENTE) private Integer npendiente;
	@PortableProperty(IND_NDIVISOR) private Integer ndivisor;
	
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

	public Integer getNdotacion() {
		return ndotacion;
	}

	public void setNdotacion(Integer ndotacion) {
		this.ndotacion = ndotacion;
	}

	public Integer getNpendiente() {
		return npendiente;
	}

	public void setNpendiente(Integer npendiente) {
		this.npendiente = npendiente;
	}

	public Integer getNdivisor() {
		return ndivisor;
	}

	public void setNdivisor(Integer ndivisor) {
		this.ndivisor = ndivisor;
	}

	public static int getIndKmadaptac() {
		return IND_KMADAPTAC;
	}

	public static int getIndFini() {
		return IND_FINI;
	}

	public static int getIndFfin() {
		return IND_FFIN;
	}

	public static int getIndNdotacion() {
		return IND_NDOTACION;
	}

	public static int getIndNpendiente() {
		return IND_NPENDIENTE;
	}

	public static int getIndNdivisor() {
		return IND_NDIVISOR;
	}
	
	@Override
	public PeriodosAdKey getKey() {
		return new PeriodosAdKey(kmadaptac, fini);
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
				+ ((ndotacion == null) ? 0 : ndotacion.hashCode());
		result = prime * result 
				+ ((npendiente == null) ? 0 : npendiente.hashCode());
		result = prime * result 
				+ ((ndivisor == null) ? 0 : ndivisor.hashCode());
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
		PeriodosAd other = (PeriodosAd) obj;
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
		if (ndotacion == null) {
			if (other.ndotacion != null) {
				return false;
			}
		} else if (!ndotacion.equals(other.ndotacion)) {
			return false;
		}
		if (npendiente == null) {
			if (other.npendiente != null) {
				return false;
			}
		} else if (!npendiente.equals(other.npendiente)) {
			return false;
		}
		if (ndivisor == null) {
			if (other.ndivisor != null) {
				return false;
			}
		} else if (!ndivisor.equals(other.ndivisor)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PeriodosAd [kmadaptac=");
		builder.append(kmadaptac);
		builder.append(", fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", ndotacion=");
		builder.append(ndotacion);
		builder.append(", npendiente=");
		builder.append(npendiente);
		builder.append(", ndivisor=");
		builder.append(ndivisor);
		builder.append("]");
		return builder.toString();
	}
}
