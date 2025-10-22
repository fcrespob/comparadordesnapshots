package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.DiferencialGastosKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DiferencialGastos implements EntidadBase<DiferencialGastosKey> {
	
	public static final int IND_KTIPOBT = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_KGARANTIA = 2;
	public static final int IND_FINI = 3;
	public static final int IND_FFIN = 4;
	public static final int IND_PDIFEGAP = 5;
	public static final int IND_PDIFEGAC = 6;
	public static final int IND_PDIFEGAR = 7;
	public static final int IND_PDIFEPRP = 8;
	public static final int IND_PDIFEPRC = 9;
	public static final int IND_PDIFEPRR = 10;
	public static final int IND_PDIFERSP = 11;
	public static final int IND_PDIFERSC = 12;
	public static final int IND_PDIFERSR = 13;
	
	@PortableProperty(IND_KTIPOBT) private String ktipobt;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_PDIFEGAP) private BigDecimal pdifegap;
	@PortableProperty(IND_PDIFEGAC) private BigDecimal pdifegac;
	@PortableProperty(IND_PDIFEGAR) private BigDecimal pdifegar;
	@PortableProperty(IND_PDIFEPRP) private BigDecimal pdifeprp;
	@PortableProperty(IND_PDIFEPRC) private BigDecimal pdifeprc;
	@PortableProperty(IND_PDIFEPRR) private BigDecimal pdifeprr;
	@PortableProperty(IND_PDIFERSP) private BigDecimal pdifersp;
	@PortableProperty(IND_PDIFERSC) private BigDecimal pdifersc;
	@PortableProperty(IND_PDIFERSR) private BigDecimal pdifersr;
	
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

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

	public BigDecimal getPdifegap() {
		return pdifegap;
	}

	public void setPdifegap(BigDecimal pdifegap) {
		this.pdifegap = pdifegap;
	}

	public BigDecimal getPdifegac() {
		return pdifegac;
	}

	public void setPdifegac(BigDecimal pdifegac) {
		this.pdifegac = pdifegac;
	}

	public BigDecimal getPdifegar() {
		return pdifegar;
	}

	public void setPdifegar(BigDecimal pdifegar) {
		this.pdifegar = pdifegar;
	}

	public BigDecimal getPdifeprp() {
		return pdifeprp;
	}

	public void setPdifeprp(BigDecimal pdifeprp) {
		this.pdifeprp = pdifeprp;
	}

	public BigDecimal getPdifeprc() {
		return pdifeprc;
	}

	public void setPdifeprc(BigDecimal pdifeprc) {
		this.pdifeprc = pdifeprc;
	}

	public BigDecimal getPdifeprr() {
		return pdifeprr;
	}

	public void setPdifeprr(BigDecimal pdifeprr) {
		this.pdifeprr = pdifeprr;
	}

	public BigDecimal getPdifersp() {
		return pdifersp;
	}

	public void setPdifersp(BigDecimal pdifersp) {
		this.pdifersp = pdifersp;
	}

	public BigDecimal getPdifersc() {
		return pdifersc;
	}

	public void setPdifersc(BigDecimal pdifersc) {
		this.pdifersc = pdifersc;
	}

	public BigDecimal getPdifersr() {
		return pdifersr;
	}

	public void setPdifersr(BigDecimal pdifersr) {
		this.pdifersr = pdifersr;
	}

	public static int getIndKtipobt() {
		return IND_KTIPOBT;
	}

	public static int getIndKmodalidad() {
		return IND_KMODALIDAD;
	}

	public static int getIndKgarantia() {
		return IND_KGARANTIA;
	}

	public static int getIndFini() {
		return IND_FINI;
	}

	public static int getIndFfin() {
		return IND_FFIN;
	}

	public static int getIndPdifegap() {
		return IND_PDIFEGAP;
	}

	public static int getIndPdifegac() {
		return IND_PDIFEGAC;
	}

	public static int getIndPdifegar() {
		return IND_PDIFEGAR;
	}

	public static int getIndPdifeprp() {
		return IND_PDIFEPRP;
	}

	public static int getIndPdifeprc() {
		return IND_PDIFEPRC;
	}

	public static int getIndPdifeprr() {
		return IND_PDIFEPRR;
	}

	public static int getIndPdifersp() {
		return IND_PDIFERSP;
	}

	public static int getIndPdifersc() {
		return IND_PDIFERSC;
	}

	public static int getIndPdifersr() {
		return IND_PDIFERSR;
	}

	@Override
	public DiferencialGastosKey getKey() {
		return new DiferencialGastosKey(ktipobt, kmodalidad, kgarantia, fini);
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
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result
				+ ((pdifegap == null) ? 0 : pdifegap.hashCode());
		result = prime * result
				+ ((pdifegac == null) ? 0 : pdifegac.hashCode());
		result = prime * result
				+ ((pdifegar == null) ? 0 : pdifegar.hashCode());
		result = prime * result
				+ ((pdifeprp == null) ? 0 : pdifeprp.hashCode());
		result = prime * result
				+ ((pdifeprc == null) ? 0 : pdifeprc.hashCode());
		result = prime * result
				+ ((pdifeprr == null) ? 0 : pdifeprr.hashCode());
		result = prime * result
				+ ((pdifersp == null) ? 0 : pdifersp.hashCode());
		result = prime * result
				+ ((pdifersc == null) ? 0 : pdifersc.hashCode());
		result = prime * result
				+ ((pdifersr == null) ? 0 : pdifersr.hashCode());
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
		DiferencialGastos other = (DiferencialGastos) obj;
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
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		if (pdifegap == null) {
			if (other.pdifegap != null) {
				return false;
			}
		} else if (!pdifegap.equals(other.pdifegap)) {
			return false;
		}
		if (pdifegac == null) {
			if (other.pdifegac != null) {
				return false;
			}
		} else if (!pdifegac.equals(other.pdifegac)) {
			return false;
		}
		if (pdifegar == null) {
			if (other.pdifegar != null) {
				return false;
			}
		} else if (!pdifegar.equals(other.pdifegar)) {
			return false;
		}
		if (pdifeprp == null) {
			if (other.pdifeprp != null) {
				return false;
			}
		} else if (!pdifeprp.equals(other.pdifeprp)) {
			return false;
		}
		if (pdifeprc == null) {
			if (other.pdifeprc != null) {
				return false;
			}
		} else if (!pdifeprc.equals(other.pdifeprc)) {
			return false;
		}
		if (pdifeprr == null) {
			if (other.pdifeprr != null) {
				return false;
			}
		} else if (!pdifeprr.equals(other.pdifeprr)) {
			return false;
		}
		if (pdifersp == null) {
			if (other.pdifersp != null) {
				return false;
			}
		} else if (!pdifersp.equals(other.pdifersp)) {
			return false;
		}
		if (pdifersc == null) {
			if (other.pdifersc != null) {
				return false;
			}
		} else if (!pdifersc.equals(other.pdifersc)) {
			return false;
		}
		if (pdifersr == null) {
			if (other.pdifersr != null) {
				return false;
			}
		} else if (!pdifersr.equals(other.pdifersr)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiferencialGastos [ktipobt=");
		builder.append(ktipobt);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kgarantia=");
		builder.append(kgarantia);
		builder.append(", fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", pdifegap=");
		builder.append(pdifegap);
		builder.append(", pdifegac=");
		builder.append(pdifegac);
		builder.append(", pdifegar=");
		builder.append(pdifegar);
		builder.append(", pdifeprp=");
		builder.append(pdifeprp);
		builder.append(", pdifeprc=");
		builder.append(pdifeprc);
		builder.append(", pdifeprr=");
		builder.append(pdifeprr);
		builder.append(", pdifersp=");
		builder.append(pdifersp);
		builder.append(", pdifersc=");
		builder.append(pdifersc);
		builder.append(", pdifersr=");
		builder.append(pdifersr);
		builder.append("]");
		return builder.toString();
	}
	
	
}
