package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.PrvBt;

@Portable	
public class PrvBtKey implements Comparable<PrvBtKey> {
	
	@PortableProperty(PrvBt.IND_BT) private String bt;
	@PortableProperty(PrvBt.IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(PrvBt.IND_UMICKEY) private UmicKey umickey;
	
	public PrvBtKey() {
		super();
	}
	
	
	public PrvBtKey(String bt, Timestamp feccierre, UmicKey umickey) {
		super();
		this.bt = bt;
		this.feccierre = feccierre;
		this.umickey = umickey;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result	+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((umickey == null) ? 0 : umickey.hashCode());
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
		PrvBtKey other = (PrvBtKey) obj;
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
		if (umickey == null) {
			if (other.umickey != null)
				return false;
		} else if (!umickey.equals(other.umickey))
			return false;
		return true;
	}


	public int compareTo(PrvBtKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.feccierre, o.feccierre);
		compareToBuilder.append(this.umickey.getKpoliza(), o.umickey.getKpoliza());
		compareToBuilder.append(this.umickey.getKsubpoliza(), o.umickey.getKsubpoliza());
		compareToBuilder.append(this.umickey.getNsuscri(), o.umickey.getNsuscri());
		compareToBuilder.append(this.umickey.getKgarantia(), o.umickey.getKgarantia());
		compareToBuilder.append(this.umickey, o.umickey);
		
		return compareToBuilder.toComparison();
	}	


}
