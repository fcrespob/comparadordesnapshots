package es.mapfre.solvencia.dominio.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.ConversionTablasExpKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ConversionTablasExp implements EntidadBase<ConversionTablasExpKey> {

	public static final int IND_KTIPOBT = 0;
	public static final int IND_KTABLAEXP = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_KGARANTIA = 3;
	public static final int IND_FINI = 4;
	public static final int IND_FFIN = 5;
	public static final int IND_CTABLAINI = 6;
	public static final int IND_CTABLAFIN = 7;
	
	@PortableProperty(IND_KTIPOBT) private String ktipobt;
	@PortableProperty(IND_KTABLAEXP) private Integer ktablaexp;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_CTABLAINI) private Integer ctablaini;
	@PortableProperty(IND_CTABLAFIN) private Integer ctablafin;
		
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

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

	public Integer getCtablaini() {
		return ctablaini;
	}

	public void setCtablaini(Integer ctablaini) {
		this.ctablaini = ctablaini;
	}

	public Integer getCtablafin() {
		return ctablafin;
	}

	public void setCtablafin(Integer ctablafin) {
		this.ctablafin = ctablafin;
	}

	public static int getIndKtipobt() {
		return IND_KTIPOBT;
	}

	public static int getIndKtablaexp() {
		return IND_KTABLAEXP;
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

	public static int getIndCtablaini() {
		return IND_CTABLAINI;
	}

	public static int getIndCtablafin() {
		return IND_CTABLAFIN;
	}
	
	@Override
	public ConversionTablasExpKey getKey() {
		return new ConversionTablasExpKey(ktipobt, ktablaexp, kmodalidad, kgarantia, fini);
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
		result = prime * result 
				+ ((fini == null) ? 0 : fini.hashCode());
		result = prime * result 
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result 
				+ ((ctablaini == null) ? 0 : ctablaini.hashCode());
		result = prime * result 
				+ ((ctablafin == null) ? 0 : ctablafin.hashCode());
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
		ConversionTablasExp other = (ConversionTablasExp) obj;
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
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		if (ctablaini == null) {
			if (other.ctablaini != null) {
				return false;
			}
		} else if (!ctablaini.equals(other.ctablaini)) {
			return false;
		}
		if (ctablafin == null) {
			if (other.ctablafin != null) {
				return false;
			}
		} else if (!ctablafin.equals(other.ctablafin)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ConversionTablasExp [ktipobt=");
		builder.append(ktipobt);
		builder.append(", ktablaexp=");
		builder.append(ktablaexp);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kgarantia=");
		builder.append(kgarantia);
		builder.append(", fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", ctablaini=");
		builder.append(ctablaini);
		builder.append(", ctablafin=");
		builder.append(ctablafin);
		builder.append("]");
		return builder.toString();
	}
	
}
