package es.mapfre.coaseguro.tirea.dominio.keys;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class TotPmaCoaBtcoatfKey implements Comparable<TotPmaCoaBtcoatfKey> {
	
	private String bt;
	private Timestamp fcierre;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer nsuscri;
	
	public TotPmaCoaBtcoatfKey() {
		super();
	}
	
	public TotPmaCoaBtcoatfKey(String bt, Timestamp fcierre, Long kpoliza, Integer ksubpoliza, Integer nsuscri) {
		this.bt = bt;
		this.fcierre = fcierre;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
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
		TotPmaCoaBtcoatfKey other = (TotPmaCoaBtcoatfKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "TotPmaCoaKey [bt=" + bt + ", feccierre=" + fcierre + ",  kpoliza=" + kpoliza
				+ ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri + "]";
	}
	
	public int compareTo(TotPmaCoaBtcoatfKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		
		return compareToBuilder.toComparison();
	}	
	
}