package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35014Key;

public class Tab35014 implements EntidadBase<Tab35014Key>{


	public static final int IND_kgarantia = 0;
	public static final int IND_kprestacion = 1;
	public static final int IND_TIPOPRESTACION = 2;
	public static final int IND_PAGOPRESTACION = 3;
	
	
	private Integer kgarantia;
	private String kprestacion;
	private String tipoPrestacion;
	private String pagoPrestacion;
	
	public Integer getkgarantia() {
		return kgarantia;
	}

	public void setkgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getkprestacion() {
		return kprestacion;
	}

	public void setkprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public String getTipoPrestacion() {
		return tipoPrestacion;
	}

	public void setTipoPrestacion(String tipoPrestacion) {
		this.tipoPrestacion = tipoPrestacion;
	}

	public String getPagoPrestacion() {
		return pagoPrestacion;
	}

	public void setPagoPrestacion(String pagoPrestacion) {
		this.pagoPrestacion = pagoPrestacion;
	}

	public static int getIndkgarantia() {
		return IND_kgarantia;
	}

	public static int getIndkprestacion() {
		return IND_kprestacion;
	}

	public static int getIndTipoprestacion() {
		return IND_TIPOPRESTACION;
	}

	public static int getIndPagoprestacion() {
		return IND_PAGOPRESTACION;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((pagoPrestacion == null) ? 0 : pagoPrestacion.hashCode());
		result = prime * result + ((tipoPrestacion == null) ? 0 : tipoPrestacion.hashCode());
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
		Tab35014 other = (Tab35014) obj;
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
		if (pagoPrestacion == null) {
			if (other.pagoPrestacion != null)
				return false;
		} else if (!pagoPrestacion.equals(other.pagoPrestacion))
			return false;
		if (tipoPrestacion == null) {
			if (other.tipoPrestacion != null)
				return false;
		} else if (!tipoPrestacion.equals(other.tipoPrestacion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tab35014 [kgarantia=" + kgarantia + ", kprestacion=" + kprestacion + ", tipoPrestacion="
				+ tipoPrestacion + ", pagoPrestacion=" + pagoPrestacion + "]";
	}

	@Override
	public Tab35014Key getKey() {
		return new Tab35014Key(kgarantia, kprestacion);
	}
}

