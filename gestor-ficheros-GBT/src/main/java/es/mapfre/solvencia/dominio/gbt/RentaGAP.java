package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.RentaGAPKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class RentaGAP implements EntidadBase<RentaGAPKey> {
	public static final int IND_KGAP = 0;
	public static final int IND_FINI = 1;
	public static final int IND_FFIN = 2;
	public static final int IND_PRENTABIL = 3;
	public static final int IND_FFINCASADO = 4;
	
	@PortableProperty(IND_KGAP) private String kgap;
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_PRENTABIL) private BigDecimal prentabil;
	@PortableProperty(IND_FFINCASADO) private Timestamp ffincasado;
	
	public String getKgap() {
		return kgap;
	}

	public void setKgap(String kgap) {
		this.kgap = kgap;
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

	public BigDecimal getPrentabil() {
		return prentabil;
	}

	public void setPrentabil(BigDecimal prentabil) {
		this.prentabil = prentabil;
	}

	public Timestamp getFfincasado() {
		return ffincasado;
	}

	public void setFfincasado(Timestamp ffincasado) {
		this.ffincasado = ffincasado;
	}

	public static int getIndKgap() {
		return IND_KGAP;
	}

	public static int getIndFini() {
		return IND_FINI;
	}

	public static int getIndFfin() {
		return IND_FFIN;
	}

	public static int getIndPrentabil() {
		return IND_PRENTABIL;
	}

	public static int getIndFfincasado() {
		return IND_FFINCASADO;
	}

	@Override
	public RentaGAPKey getKey() {
		return new RentaGAPKey(kgap,fini);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kgap == null) ? 0 : kgap.hashCode());
		result = prime * result
				+ ((fini == null) ? 0 : fini.hashCode());
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result 
				+ ((prentabil == null) ? 0 : prentabil.hashCode());
		result = prime * result 
				+ ((ffincasado == null) ? 0 : ffincasado.hashCode());
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
		RentaGAP other = (RentaGAP) obj;
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
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		if (prentabil == null) {
			if (other.prentabil != null) {
				return false;
			}
		} else if (!prentabil.equals(other.prentabil)) {
			return false;
		}
		if (ffincasado == null) {
			if (other.ffincasado != null) {
				return false;
			}
		} else if (!ffincasado.equals(other.ffincasado)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RentaGAP [kgap=");
		builder.append(kgap);
		builder.append(", fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", prentabil=");
		builder.append(prentabil);
		builder.append(", ffincasado=");
		builder.append(ffincasado);
		builder.append("]");
		return builder.toString();
	}
	
}
