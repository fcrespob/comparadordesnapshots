package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;

@Portable	
public class PrvUmicKey implements Comparable<PrvUmicKey> {

	@PortableProperty(PrvUmic.IND_BT) private String bt;
	@PortableProperty(PrvUmic.IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(PrvUmic.IND_UMICKEY) private UmicKey umicKey;
	
	public PrvUmicKey() {
		super();
	}
	
	
	public PrvUmicKey(String bt, Timestamp feccierre, UmicKey umicKey) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.umicKey = umicKey;
	}
	

	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result	+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
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
		PrvUmicKey other = (PrvUmicKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (umicKey == null) {
			if (other.umicKey != null)
				return false;
		} else if (!umicKey.equals(other.umicKey))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "PrvUmicKey [bt=" + bt + ", feccierre=" + feccierre
				+ ", umicKey=" + umicKey + "]";
	}


	public int compareTo(PrvUmicKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.umicKey, o.umicKey);
		
		return compareToBuilder.toComparison();
	}	
	
}
