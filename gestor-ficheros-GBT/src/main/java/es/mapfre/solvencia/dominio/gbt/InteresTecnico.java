package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.InteresTecnicoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class InteresTecnico implements EntidadBase<InteresTecnicoKey> {

	public static final int IND_FINI = 0;
	public static final int IND_FFIN = 1;
	public static final int IND_PITREF = 2;
	
	@PortableProperty(IND_FINI) private Timestamp fini;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_PITREF) private BigDecimal pitref;
		
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

	public BigDecimal getPitref() {
		return pitref;
	}

	public void setPitref(BigDecimal pitref) {
		this.pitref = pitref;
	}

	public static int getIndFini() {
		return IND_FINI;
	}

	public static int getIndFfin() {
		return IND_FFIN;
	}

	public static int getIndPitref() {
		return IND_PITREF;
	}
	
	@Override
	public InteresTecnicoKey getKey() {
		return new InteresTecnicoKey(fini);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fini == null) ? 0 : fini.hashCode());
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result 
				+ ((pitref == null) ? 0 : pitref.hashCode());
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
		InteresTecnico other = (InteresTecnico) obj;
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
		if (pitref == null) {
			if (other.pitref != null) {
				return false;
			}
		} else if (!pitref.equals(other.pitref)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InteresTecnico [fini=");
		builder.append(fini);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", pitref=");
		builder.append(pitref);
		builder.append("]");
		return builder.toString();
	}

	
}
