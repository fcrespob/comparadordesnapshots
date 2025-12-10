package es.mapfre.coaseguro.tirea.dominio.keys;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Tab35015Key implements Comparable<Tab35015Key> {

	private Integer kmodalidad;
	private Integer kgarantia;
	private String kprestacion;
	
	public Tab35015Key() {
		super();
	}
	
	public Tab35015Key(Integer kmodalidad, Integer kgarantia, String kprestacion) {
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		Tab35015Key other = (Tab35015Key) obj;
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
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Tab35015Key [kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", kprestacion=" + kprestacion
				+ "]";
	}
	
	public int compareTo(Tab35015Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		return compareToBuilder.toComparison();
	}	
	
	
}
