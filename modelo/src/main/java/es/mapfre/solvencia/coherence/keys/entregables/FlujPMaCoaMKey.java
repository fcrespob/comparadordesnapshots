package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;

@Portable
public class FlujPMaCoaMKey implements Comparable<FlujPMaCoaMKey> {
	
	@PortableProperty(FlujPMaCoa.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(FlujPMaCoa.IND_BT)
	private String bt;
	@PortableProperty(FlujPMaCoa.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(FlujPMaCoa.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(FlujPMaCoa.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(FlujPMaCoa.IND_FDESDE)
	private Timestamp fdesde;
	
	public FlujPMaCoaMKey(Long kpoliza, Integer ksubpoliza, Integer nsuscri, String bt, Timestamp fcierre, Timestamp fdesde) {
		this.bt = bt;
		this.fcierre = fcierre;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.nsuscri = nsuscri;
		this.fdesde = fdesde;
	}
	
	public FlujPMaCoaMKey() {
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
		FlujPMaCoaMKey other = (FlujPMaCoaMKey) obj;
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
	
	
	public int compareTo(FlujPMaCoaMKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.fdesde, o.fdesde);
		
		return compareToBuilder.toComparison();
	}	
	
}