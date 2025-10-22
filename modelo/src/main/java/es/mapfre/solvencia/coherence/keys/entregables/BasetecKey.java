package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.Basetec;

@Portable	
public class BasetecKey implements Comparable<BasetecKey> {

	@PortableProperty(Basetec.IND_BT) private String bt;
	@PortableProperty(Basetec.IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(Basetec.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(Basetec.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(Basetec.IND_KRAMO) private String kramo;
	
	public BasetecKey() {
		super();
	}
	


	public BasetecKey(String bt, Timestamp fcierre, Integer kmodalidad, Integer kgarantia, String kramo) {
		super();
		this.bt = bt;
		this.fcierre = fcierre;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kramo = kramo;
	}


	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
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
		BasetecKey other = (BasetecKey) obj;
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
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		return true;
	}



	@Override
	public int compareTo(BasetecKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.fcierre, fcierre);
		compareToBuilder.append(this.kramo, o.kramo);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);

		return compareToBuilder.toComparison();
	}
	
	
	
}
