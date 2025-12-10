package es.mapfre.coaseguro.tirea.dominio.keys;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Tab35013Key implements Comparable<Tab35013Key> {

	private Long poliza;
	private Integer subpoliza;
	private Integer kmodalidad;
	
	public Tab35013Key() {
		super();
	}
	
	public Tab35013Key(Long poliza, Integer subpoliza, Integer kmodalidad) {
		this.poliza = poliza;
		this.subpoliza = subpoliza;
		this.kmodalidad = kmodalidad;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((poliza == null) ? 0 : poliza.hashCode());
		result = prime * result + ((subpoliza == null) ? 0 : subpoliza.hashCode());
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
		Tab35013Key other = (Tab35013Key) obj;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (poliza == null) {
			if (other.poliza != null)
				return false;
		} else if (!poliza.equals(other.poliza))
			return false;
		if (subpoliza == null) {
			if (other.subpoliza != null)
				return false;
		} else if (!subpoliza.equals(other.subpoliza))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Tab35013Key [poliza=" + poliza + ", subpoliza=" + subpoliza + ", kmodalidad=" + kmodalidad + "]";
	}
	
	public int compareTo(Tab35013Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.poliza, o.poliza);
		compareToBuilder.append(this.subpoliza, o.subpoliza);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		return compareToBuilder.toComparison();
	}	
	
	
}
