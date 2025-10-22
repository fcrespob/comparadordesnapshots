package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.entregables.PatronCsm;

@Portable
public class PatronCsmKey implements Comparable<PatronCsmKey>{
	@PortableProperty(PatronCsm.IND_BT)
	private String bt;
	@PortableProperty(PatronCsm.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(PatronCsm.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(PatronCsm.IND_UOA)
	private String uoa;
	@PortableProperty(PatronCsm.IND_FECDESDE)
	private Timestamp fecdesde;
	
	public PatronCsmKey() {
		super();
	}

	public PatronCsmKey(String bt, Timestamp fcierre, Integer ccanal, String uoa, Timestamp fecdesde) {
		super();
		this.bt = bt;
		this.fcierre = fcierre;
		this.ccanal = ccanal;
		this.uoa = uoa;
		this.fecdesde = fecdesde;
	}

	@Override
	public String toString() {
		return "PatronCsmKey [bt=" + bt + ", fcierre=" + fcierre + ", ccanal=" + ccanal
				+ ", uoa=" + uoa + ", fecdesde=" + fecdesde + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PatronCsmKey other = (PatronCsmKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		if (fecdesde == null) {
			if (other.fecdesde != null)
				return false;
		} else if (!fecdesde.equals(other.fecdesde))
			return false;
		return true;
	}

	public int compareTo(PatronCsmKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.uoa, o.uoa);
		compareToBuilder.append(this.fecdesde, o.fecdesde);
		
		return compareToBuilder.toComparison();
	}

}
