package es.mapfre.coaseguro.tirea.dominio.keys;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class DatosCoaKey implements Comparable<DatosCoaKey> {

	private Long poliza;
	private Integer subpoliza;
	
	public DatosCoaKey() {
		super();
	}
	
	public DatosCoaKey(Long poliza, Integer subpoliza) {
		this.poliza = poliza;
		this.subpoliza = subpoliza;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		DatosCoaKey other = (DatosCoaKey) obj;
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
		return "DatosCoaKey [poliza=" + poliza +  ", subpoliza=" + subpoliza +  "]";
	}
	
	public int compareTo(DatosCoaKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.poliza, o.poliza);
		compareToBuilder.append(this.subpoliza, o.subpoliza);
		return compareToBuilder.toComparison();
	}	
	
	
}
