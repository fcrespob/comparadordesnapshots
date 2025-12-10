package es.mapfre.coaseguro.tirea.dominio.keys;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class FlujPMaCoaKey implements Comparable<FlujPMaCoaKey> {
	
	private Timestamp fcierre;
	private String bt;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer nsuscri;
	private String fdesde;
	
	public FlujPMaCoaKey(Long kpoliza, Integer ksubpoliza, Integer nsuscri, String bt, Timestamp fcierre, String fdesde) {
		this.bt = bt;
		this.fcierre = fcierre;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
		this.fdesde = fdesde;
	}
	
	public FlujPMaCoaKey() {
		super();
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fdesde == null) ? 0 : fdesde.hashCode());
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
		FlujPMaCoaKey other = (FlujPMaCoaKey) obj;
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
		if (fdesde == null) {
			if (other.fdesde != null)
				return false;
		} else if (!fdesde.equals(other.fdesde))
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
	
	
	public int compareTo(FlujPMaCoaKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.fdesde.substring(2), o.fdesde.substring(2,6));
		compareToBuilder.append(this.fdesde.substring(0, 2), o.fdesde.substring(0, 2));
		
		return compareToBuilder.toComparison();
	}	
	
}