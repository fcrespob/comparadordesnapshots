package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.IPCGeneralFuturoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class IPCGeneralFuturo implements EntidadBase<IPCGeneralFuturoKey>{
	
	public static final int IND_FINICIO = 0;
	public static final int IND_FFIN = 1;
	public static final int IND_PIPCLEG = 2;
	public static final int IND_PIPCGAS = 3;
	
	@PortableProperty(IND_FINICIO) private Timestamp finicio;
	@PortableProperty(IND_FFIN) private Timestamp ffin;
	@PortableProperty(IND_PIPCLEG) private java.math.BigDecimal pipcleg;
	@PortableProperty(IND_PIPCGAS) private java.math.BigDecimal pipcgas;
	
	public Timestamp getFinicio() {
		return finicio;
	}
	public void setFinicio(Timestamp finicio) {
		this.finicio = finicio;
	}
	public Timestamp getFfin() {
		return ffin;
	}
	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}
	public java.math.BigDecimal getPipcleg() {
		return pipcleg;
	}
	public void setPipcleg(java.math.BigDecimal pipcleg) {
		this.pipcleg = pipcleg;
	}
	public java.math.BigDecimal getPipcgas() {
		return pipcgas;
	}
	public void setPipcgas(java.math.BigDecimal pipcgas) {
		this.pipcgas = pipcgas;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result + ((finicio == null) ? 0 : finicio.hashCode());
		result = prime * result + ((pipcgas == null) ? 0 : pipcgas.hashCode());
		result = prime * result + ((pipcleg == null) ? 0 : pipcleg.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			{ return true; }
		if (obj == null)
			{ return false; }
		if (getClass() != obj.getClass())
			{ return false; }
		IPCGeneralFuturo other = (IPCGeneralFuturo) obj;
		if (ffin == null) {
			if (other.ffin != null)
				{ return false; }
		} else if (!ffin.equals(other.ffin))
			{ return false; }
		if (finicio == null) {
			if (other.finicio != null)
				{ return false; }
		} else if (!finicio.equals(other.finicio))
			{ return false; }
		if (pipcgas == null) {
			if (other.pipcgas != null)
				{ return false; }
		} else if (!pipcgas.equals(other.pipcgas))
			{ return false; }
		if (pipcleg == null) {
			if (other.pipcleg != null)
				{ return false; }
		} else if (!pipcleg.equals(other.pipcleg))
			{ return false; }
		{ return true; }
	}
	@Override
	public IPCGeneralFuturoKey getKey() {
		return new IPCGeneralFuturoKey(ffin);
	}
	
	
	@Override
	public String toString() {
		return "IPCGeneralFuturo [finicio=" + finicio + ", ffin=" + ffin
				+ ", pipcleg=" + pipcleg + ", pipcgas=" + pipcgas + "]";
	}
	
	
}