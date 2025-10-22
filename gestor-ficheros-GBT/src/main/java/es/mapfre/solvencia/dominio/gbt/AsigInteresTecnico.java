package es.mapfre.solvencia.dominio.gbt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.AsigInteresTecnicoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class AsigInteresTecnico implements
Serializable,EntidadBase<AsigInteresTecnicoKey> {

	public static final int IND_FCIERRE = 0;
	public static final int IND_KTIPOBT = 1;
	public static final int IND_KGAP = 2;
	public static final int IND_KAPROSSP = 3;
	public static final int IND_KCASADO = 4;
	public static final int IND_KCRITERIOIT = 5;
	public static final int IND_PITMEDIO = 6;
	public static final int IND_PITMAXCALC =7;
	public static final int IND_CCURVA = 8;
	
	@PortableProperty(IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(IND_KTIPOBT) private String ktipobt;
	@PortableProperty(IND_KGAP) private String kgap;
	@PortableProperty(IND_KAPROSSP) private String kaprossp;
	@PortableProperty(IND_KCASADO) private String kcasado;
	@PortableProperty(IND_KCRITERIOIT) private String kcriterioit;
	@PortableProperty(IND_PITMEDIO) private BigDecimal pitmedio;
	@PortableProperty(IND_PITMAXCALC) private BigDecimal pitmaxcalc;
	@PortableProperty(IND_CCURVA) private Integer ccurva;
	
	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public String getKtipobt() {
		return ktipobt;
	}

	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}

	public String getKgap() {
		return kgap;
	}

	public void setKgap(String kgap) {
		this.kgap = kgap;
	}

	public String getKaprossp() {
		return kaprossp;
	}

	public void setKaprossp(String kaprossp) {
		this.kaprossp = kaprossp;
	}

	public String getKcasado() {
		return kcasado;
	}

	public void setKcasado(String kcasado) {
		this.kcasado = kcasado;
	}

	public String getKcriterioit() {
		return kcriterioit;
	}

	public void setKcriterioit(String kcriterioit) {
		this.kcriterioit = kcriterioit;
	}

	public BigDecimal getPitmedio() {
		return pitmedio;
	}

	public void setPitmedio(BigDecimal pitmedio) {
		this.pitmedio = pitmedio;
	}

	public BigDecimal getPitmaxcalc() {
		return pitmaxcalc;
	}

	public void setPitmaxcalc(BigDecimal pitmaxcalc) {
		this.pitmaxcalc = pitmaxcalc;
	}

	public Integer getCcurva() {
		return ccurva;
	}

	public void setCcurva(Integer ccurva) {
		this.ccurva = ccurva;
	}

	public static int getIndFcierre() {
		return IND_FCIERRE;
	}

	public static int getIndKtipobt() {
		return IND_KTIPOBT;
	}

	public static int getIndKgap() {
		return IND_KGAP;
	}

	public static int getIndKaprossp() {
		return IND_KAPROSSP;
	}

	public static int getIndKcasado() {
		return IND_KCASADO;
	}

	public static int getIndKcriterioit() {
		return IND_KCRITERIOIT;
	}

	public static int getIndPitmedio() {
		return IND_PITMEDIO;
	}

	public static int getIndPitmaxcalc() {
		return IND_PITMAXCALC;
	}

	public static int getIndCcurva() {
		return IND_CCURVA;
	}

	@Override
	public AsigInteresTecnicoKey getKey() {
		return new AsigInteresTecnicoKey(fcierre,ktipobt,kgap,kaprossp,kcasado);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result
				+ ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result
				+ ((kgap == null) ? 0 : kgap.hashCode());
		result = prime * result
				+ ((kaprossp == null) ? 0 : kaprossp.hashCode());
		result = prime * result
				+ ((kcasado == null) ? 0 : kcasado.hashCode());
		result = prime * result
				+ ((kcriterioit == null) ? 0 : kcriterioit.hashCode());
		result = prime * result
				+ ((pitmedio == null) ? 0 : pitmedio.hashCode());
		result = prime * result
				+ ((pitmaxcalc == null) ? 0 : pitmaxcalc.hashCode());
		result = prime * result
				+ ((ccurva == null) ? 0 : ccurva.hashCode());
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
		AsigInteresTecnico other = (AsigInteresTecnico) obj;
		if (fcierre == null) {
			if (other.fcierre != null) {
				return false;
			}
		} else if (!fcierre.equals(other.fcierre)) {
			return false;
		}
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		if (kgap == null) {
			if (other.kgap != null) {
				return false;
			}
		} else if (!kgap.equals(other.kgap)) {
			return false;
		}
		if (kaprossp == null) {
			if (other.kaprossp != null) {
				return false;
			}
		} else if (!kaprossp.equals(other.kaprossp)) {
			return false;
		}
		if (kcasado == null) {
			if (other.kcasado != null) {
				return false;
			}
		} else if (!kcasado.equals(other.kcasado)) {
			return false;
		}
		if (kcriterioit == null) {
			if (other.kcriterioit != null) {
				return false;
			}
		} else if (!kcriterioit.equals(other.kcriterioit)) {
			return false;
		}
		if (pitmedio == null) {
			if (other.pitmedio != null) {
				return false;
			}
		} else if (!pitmedio.equals(other.pitmedio)) {
			return false;
		}
		if (pitmaxcalc == null) {
			if (other.pitmaxcalc != null) {
				return false;
			}
		} else if (!pitmaxcalc.equals(other.pitmaxcalc)) {
			return false;
		}
		if (ccurva == null) {
			if (other.ccurva != null) {
				return false;
			}
		} else if (!ccurva.equals(other.ccurva)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AsigInteresTecnico [fcierre=");
		builder.append(fcierre);
		builder.append(", ktipobt=");
		builder.append(ktipobt);
		builder.append(", kgap=");
		builder.append(kgap);
		builder.append(", kaprossp=");
		builder.append(kaprossp);
		builder.append(", kcasado=");
		builder.append(kcasado);
		builder.append(", kcriterioit=");
		builder.append(kcriterioit);
		builder.append(", pitmedio=");
		builder.append(pitmedio);
		builder.append(", pitmaxcalc=");
		builder.append(pitmaxcalc);
		builder.append(", ccurva=");
		builder.append(ccurva);
		builder.append("]");
		return builder.toString();
	}
	
}
