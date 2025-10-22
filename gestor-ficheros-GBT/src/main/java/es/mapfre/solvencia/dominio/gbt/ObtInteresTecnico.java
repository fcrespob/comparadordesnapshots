package es.mapfre.solvencia.dominio.gbt;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.ObtInteresTecnicoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ObtInteresTecnico implements EntidadBase<ObtInteresTecnicoKey> {

	public static final int IND_KCRITERIOIT = 0;
	public static final int IND_GCRITERIOIT = 1;
	public static final int IND_KLITERAL = 2;
	public static final int IND_GLINK = 3;
	
	@PortableProperty(IND_KCRITERIOIT) private String kcriterioit;
	@PortableProperty(IND_GCRITERIOIT) private String gcriterioit;
	@PortableProperty(IND_KLITERAL) private Integer kliteral;
	@PortableProperty(IND_GLINK) private String glink;
	
	public String getKcriterioit() {
		return kcriterioit;
	}

	public void setKcriterioit(String kcriterioit) {
		this.kcriterioit = kcriterioit;
	}

	public String getGcriterioit() {
		return gcriterioit;
	}

	public void setGcriterioit(String gcriterioit) {
		this.gcriterioit = gcriterioit;
	}

	public Integer getKliteral() {
		return kliteral;
	}

	public void setKliteral(Integer kliteral) {
		this.kliteral = kliteral;
	}

	public String getGlink() {
		return glink;
	}

	public void setGlink(String glink) {
		this.glink = glink;
	}

	public static int getIndKcriterioit() {
		return IND_KCRITERIOIT;
	}

	public static int getIndGcriterioit() {
		return IND_GCRITERIOIT;
	}

	public static int getIndKliteral() {
		return IND_KLITERAL;
	}

	public static int getIndGlink() {
		return IND_GLINK;
	}

	@Override
	public ObtInteresTecnicoKey getKey() {
		return new ObtInteresTecnicoKey(kcriterioit);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kcriterioit == null) ? 0 : kcriterioit.hashCode());
		result = prime * result
				+ ((gcriterioit == null) ? 0 : gcriterioit.hashCode());
		result = prime * result
				+ ((kliteral == null) ? 0 : kliteral.hashCode());
		result = prime * result
				+ ((glink == null) ? 0 : glink.hashCode());
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
		ObtInteresTecnico other = (ObtInteresTecnico) obj;
		if (kcriterioit == null) {
			if (other.kcriterioit != null) {
				return false;
			}
		} else if (!kcriterioit.equals(other.kcriterioit)) {
			return false;
		}
		if (gcriterioit == null) {
			if (other.gcriterioit != null) {
				return false;
			}
		} else if (!gcriterioit.equals(other.gcriterioit)) {
			return false;
		}
		if (kliteral == null) {
			if (other.kliteral != null) {
				return false;
			}
		} else if (!kliteral.equals(other.kliteral)) {
			return false;
		}
		if (glink == null) {
			if (other.glink != null) {
				return false;
			}
		} else if (!glink.equals(other.glink)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ObtInteresTecnico [kcriterioit=");
		builder.append(kcriterioit);
		builder.append(", gcriterioit=");
		builder.append(gcriterioit);
		builder.append(", kliteral=");
		builder.append(kliteral);
		builder.append(", glink=");
		builder.append(glink);
		builder.append("]");
		return builder.toString();
	}
	
}
