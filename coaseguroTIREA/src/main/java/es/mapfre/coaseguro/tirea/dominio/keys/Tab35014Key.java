package es.mapfre.coaseguro.tirea.dominio.keys;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Tab35014Key implements Comparable<Tab35014Key> {

	private Integer kgarantia;
	private String kprestacion;
	
	public Tab35014Key() {
		super();
	}
	
	public Tab35014Key(Integer kgarantia, String kprestacion) {
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
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
		Tab35014Key other = (Tab35014Key) obj;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Tab35014Key [kgarantia=" + kgarantia + ", kprestacion=" + kprestacion + "]";
	}
	
	public int compareTo(Tab35014Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		return compareToBuilder.toComparison();
	}	
	
	
}
