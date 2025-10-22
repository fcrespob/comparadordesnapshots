package es.mapfre.scr.tablasExperiencia.dominio.keys;

import java.math.BigDecimal;

public class TablaExperienciaKey{


	private Integer ktabla;

	private String k2tipovalor;

	private String knacimiento;

	private java.math.BigDecimal kinteres;

	private java.math.BigDecimal ksobremort;

	private java.math.BigDecimal ksobreries;

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((k2tipovalor == null) ? 0 : k2tipovalor.hashCode());
		result = prime * result
				+ ((knacimiento == null) ? 0 : knacimiento.hashCode());
		result = prime * result
				+ ((kinteres == null) ? 0 : kinteres.hashCode());
		result = prime * result
				+ ((ksobremort == null) ? 0 : ksobremort.hashCode());
		result = prime * result
				+ ((ksobreries == null) ? 0 : ksobreries.hashCode());
		result = prime * result + ((ktabla == null) ? 0 : ktabla.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TablaExperienciaKey other = (TablaExperienciaKey) obj;
		if (k2tipovalor == null) {
			if (other.k2tipovalor != null) {
				return false;
			}
		} else if (!k2tipovalor.equals(other.k2tipovalor)) {
			return false;
		}
		if (knacimiento == null) {
			if (other.knacimiento != null) {
				return false;
			}
		} else if (!knacimiento.equals(other.knacimiento)) {
			return false;
		}
		if (kinteres == null) {
			if (other.kinteres != null) {
				return false;
			}
		} else if (!kinteres.equals(other.kinteres)) {
			return false;
		}
		if (ksobremort == null) {
			if (other.ksobremort != null) {
				return false;
			}
		} else if (!ksobremort.equals(other.ksobremort)) {
			return false;
		}
		if (ksobreries == null) {
			if (other.ksobreries != null) {
				return false;
			}
		} else if (!ksobreries.equals(other.ksobreries)) {
			return false;
		}
		if (ktabla == null) {
			if (other.ktabla != null) {
				return false;
			}
		} else if (!ktabla.equals(other.ktabla)) {
			return false;
		}
		return true;
	}

	public TablaExperienciaKey(Integer ktabla, String k2tipovalor,
			String knacimiento, BigDecimal kinteres, BigDecimal ksobremort,
			BigDecimal ksobreries) {
		super();
		this.ktabla = ktabla;
		this.k2tipovalor = k2tipovalor;
		this.knacimiento = knacimiento;
		this.kinteres = kinteres;
		this.ksobremort = ksobremort;
		this.ksobreries = ksobreries;
	}

	public TablaExperienciaKey() {
		super();
	}




}
