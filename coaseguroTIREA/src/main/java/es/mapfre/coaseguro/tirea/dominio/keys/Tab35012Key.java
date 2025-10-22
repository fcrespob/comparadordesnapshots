package es.mapfre.coaseguro.tirea.dominio.keys;

import java.math.BigDecimal;
import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class Tab35012Key implements Comparable<Tab35012Key> {

	private String tablaMapfre;
	
	public Tab35012Key() {
		super();
	}
	
	public Tab35012Key(String tablaMapfre) {
		this.tablaMapfre = tablaMapfre;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tablaMapfre == null) ? 0 : tablaMapfre.hashCode());
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
		Tab35012Key other = (Tab35012Key) obj;
		if (tablaMapfre == null) {
			if (other.tablaMapfre != null)
				return false;
		} else if (!tablaMapfre.equals(other.tablaMapfre))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Tab35012Key [tablaMapfre=" + tablaMapfre + "]";
	}
	
	public int compareTo(Tab35012Key o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.tablaMapfre, o.tablaMapfre);
		return compareToBuilder.toComparison();
	}	
	
	
}
