package es.mapfre.solvencia.coherence.keys.scr.entregables;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;

@Portable
public class FactoresVolatilidadKey implements Comparable<FactoresVolatilidadKey> {

	@PortableProperty(FactoresVolatilidad.IND_BT) private String bt;
	@PortableProperty(FactoresVolatilidad.IND_CCANAL) private Integer ccanal;
	@PortableProperty(FactoresVolatilidad.IND_KMODALIDAD) private Integer kmodalidad;
	
	public FactoresVolatilidadKey(){
		super();
	}
	
	public FactoresVolatilidadKey(String bt, Integer ccanal, Integer kmodalidad) {
		super();
		this.ccanal = ccanal;
		this.bt = bt;
		this.kmodalidad = kmodalidad;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		return result;
	}

	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FactoresVolatilidadKey other = (FactoresVolatilidadKey) obj;
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
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "FactoresVolatilidadKey [bt=" + bt + ", ccanal=" + ccanal + ", kmodalidad=" + kmodalidad + "]";
	}
	
	@Override
	public int compareTo(FactoresVolatilidadKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.ccanal, o.ccanal);	
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);	

		return compareToBuilder.toComparison();
	}

}
