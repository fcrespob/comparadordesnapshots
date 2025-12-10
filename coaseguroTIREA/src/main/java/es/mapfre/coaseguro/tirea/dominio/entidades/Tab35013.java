package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35013Key;

public class Tab35013 implements EntidadBase<Tab35013Key>{

	public static final int IND_POLIZA = 0;
	public static final int IND_SUBPOLIZA = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_TIPOREVERSION = 3;
	public static final int IND_PERIODICIDADPRIMA = 4;
	public static final int IND_PERIODICIDADRENTA = 5;
	public static final int IND_CRITERIO = 6;
	public static final int IND_ORFANDAD = 7;
	public static final int IND_DIFERIMIENTO = 8;
	
	private Long poliza;
	private Integer subpoliza;
	private Integer kmodalidad;
	private String tipoReversion;
	private String periodicidadPrima;
	private String periodicidadRenta;
	private String criterio;
	private String orfandad;
	private String diferimiento;

	public Integer getkmodalidad() {
		return kmodalidad;
	}

	public void setkmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getTipoReversion() {
		return tipoReversion;
	}

	public void setTipoReversion(String tipoReversion) {
		this.tipoReversion = tipoReversion;
	}

	public String getPeriodicidadPrima() {
		return periodicidadPrima;
	}

	public void setPeriodicidadPrima(String periodicidadPrima) {
		this.periodicidadPrima = periodicidadPrima;
	}

	public String getPeriodicidadRenta() {
		return periodicidadRenta;
	}

	public void setPeriodicidadRenta(String periodicidadRenta) {
		this.periodicidadRenta = periodicidadRenta;
	}

	public String getCriterio() {
		return criterio;
	}

	public void setCriterio(String criterio) {
		this.criterio = criterio;
	}
	
	public Long getPoliza() {
		return poliza;
	}

	public void setPoliza(Long poliza) {
		this.poliza = poliza;
	}

	public Integer getSubpoliza() {
		return subpoliza;
	}

	public void setSubpoliza(Integer subpoliza) {
		this.subpoliza = subpoliza;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getOrfandad() {
		return orfandad;
	}

	public void setOrfandad(String orfandad) {
		this.orfandad = orfandad;
	}

	public String getDiferimiento() {
		return diferimiento;
	}

	public void setDiferimiento(String diferimiento) {
		this.diferimiento = diferimiento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((criterio == null) ? 0 : criterio.hashCode());
		result = prime * result + ((diferimiento == null) ? 0 : diferimiento.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((orfandad == null) ? 0 : orfandad.hashCode());
		result = prime * result + ((periodicidadPrima == null) ? 0 : periodicidadPrima.hashCode());
		result = prime * result + ((periodicidadRenta == null) ? 0 : periodicidadRenta.hashCode());
		result = prime * result + ((poliza == null) ? 0 : poliza.hashCode());
		result = prime * result + ((subpoliza == null) ? 0 : subpoliza.hashCode());
		result = prime * result + ((tipoReversion == null) ? 0 : tipoReversion.hashCode());
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
		Tab35013 other = (Tab35013) obj;
		if (criterio == null) {
			if (other.criterio != null)
				return false;
		} else if (!criterio.equals(other.criterio))
			return false;
		if (diferimiento == null) {
			if (other.diferimiento != null)
				return false;
		} else if (!diferimiento.equals(other.diferimiento))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (orfandad == null) {
			if (other.orfandad != null)
				return false;
		} else if (!orfandad.equals(other.orfandad))
			return false;
		if (periodicidadPrima == null) {
			if (other.periodicidadPrima != null)
				return false;
		} else if (!periodicidadPrima.equals(other.periodicidadPrima))
			return false;
		if (periodicidadRenta == null) {
			if (other.periodicidadRenta != null)
				return false;
		} else if (!periodicidadRenta.equals(other.periodicidadRenta))
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
		if (tipoReversion == null) {
			if (other.tipoReversion != null)
				return false;
		} else if (!tipoReversion.equals(other.tipoReversion))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tab35013 [poliza=" + poliza + ", subpoliza=" + subpoliza + ", kmodalidad=" + kmodalidad
				+ ", tipoReversion=" + tipoReversion + ", periodicidadPrima=" + periodicidadPrima
				+ ", periodicidadRenta=" + periodicidadRenta + ", criterio=" + criterio + ", orfandad=" + orfandad
				+ ", diferimiento=" + diferimiento + "]";
	}

	@Override
	public Tab35013Key getKey() {
		return new Tab35013Key(poliza, subpoliza, kmodalidad);
	}
}

