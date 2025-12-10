package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35015Key;

public class Tab35015 implements EntidadBase<Tab35015Key>{


	public static final int IND_KMODALIDAD = 0;
	public static final int IND_KGARANTIA = 1;
	public static final int IND_KPRESTACION = 2;
	public static final int IND_GARANTIATIREA = 3;
	public static final int IND_DESCRIPCIONGARANTIATIREA = 4;
	
	private Integer kmodalidad;
	private Integer kgarantia;
	private String kprestacion;
	private String garantiaTirea;
	private String descripcionGarantiaTirea;

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public String getGarantiaTirea() {
		return garantiaTirea;
	}

	public void setGarantiaTirea(String garantiaTirea) {
		this.garantiaTirea = garantiaTirea;
	}

	public String getDescripcionGarantiaTirea() {
		return descripcionGarantiaTirea;
	}

	public void setDescripcionGarantiaTirea(String descripcionGarantiaTirea) {
		this.descripcionGarantiaTirea = descripcionGarantiaTirea;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descripcionGarantiaTirea == null) ? 0 : descripcionGarantiaTirea.hashCode());
		result = prime * result + ((garantiaTirea == null) ? 0 : garantiaTirea.hashCode());
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
		Tab35015 other = (Tab35015) obj;
		if (descripcionGarantiaTirea == null) {
			if (other.descripcionGarantiaTirea != null)
				return false;
		} else if (!descripcionGarantiaTirea.equals(other.descripcionGarantiaTirea))
			return false;
		if (garantiaTirea == null) {
			if (other.garantiaTirea != null)
				return false;
		} else if (!garantiaTirea.equals(other.garantiaTirea))
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
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tab35015 [kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", kprestacion=" + kprestacion
				+ ", garantiaTirea=" + garantiaTirea + ", descripcionGarantiaTirea=" + descripcionGarantiaTirea + "]";
	}

	@Override
	public Tab35015Key getKey() {
		return new Tab35015Key(kmodalidad, kgarantia, kprestacion);
	}
}

