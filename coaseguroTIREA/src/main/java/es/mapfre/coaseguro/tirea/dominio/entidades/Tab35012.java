package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.Tab35012Key;

public class Tab35012 implements EntidadBase<Tab35012Key>{


	public static final int IND_TABLAMAPFRE = 0;
	public static final int IND_TABLATIREA = 1;
	public static final int IND_PORCENTAJETABLA = 2;
	public static final int IND_FACTORCORRECTOR = 3;
	public static final int IND_SOBREMORTALIDAD = 4;
	public static final int IND_SOBRERIESGO = 5;
	public static final int IND_DESCRIPCIONTABLA = 6;
	
	
	private String tablaMapfre;
	private String tablaTirea;
	private BigDecimal porcentajeTabla;
	private BigDecimal factorCorrector;
	private BigDecimal sobreMortalidad;
	private BigDecimal sobreRiesgo;
	private String descripcionTabla;

	public String getTablaMapfre() {
		return tablaMapfre;
	}

	public void setTablaMapfre(String tablaMapfre) {
		this.tablaMapfre = tablaMapfre;
	}

	public String getTablaTirea() {
		return tablaTirea;
	}

	public void setTablaTirea(String tablaTirea) {
		this.tablaTirea = tablaTirea;
	}

	public BigDecimal getPorcentajeTabla() {
		return porcentajeTabla;
	}

	public void setPorcentajeTabla(BigDecimal porcentajeTabla) {
		this.porcentajeTabla = porcentajeTabla;
	}

	public BigDecimal getFactorCorrector() {
		return factorCorrector;
	}

	public void setFactorCorrector(BigDecimal factorCorrector) {
		this.factorCorrector = factorCorrector;
	}

	public BigDecimal getSobreMortalidad() {
		return sobreMortalidad;
	}

	public void setSobreMortalidad(BigDecimal sobreMortalidad) {
		this.sobreMortalidad = sobreMortalidad;
	}

	public BigDecimal getSobreRiesgo() {
		return sobreRiesgo;
	}

	public void setSobreRiesgo(BigDecimal sobreRiesgo) {
		this.sobreRiesgo = sobreRiesgo;
	}

	public String getdescripcionTabla() {
		return descripcionTabla;
	}

	public void setdescripcionTabla(String descripcionTabla) {
		this.descripcionTabla = descripcionTabla;
	}

	public static int getIndTablamapfre() {
		return IND_TABLAMAPFRE;
	}

	public static int getIndTablatirea() {
		return IND_TABLATIREA;
	}

	public static int getIndPorcentajetabla() {
		return IND_PORCENTAJETABLA;
	}

	public static int getIndFactorcorrector() {
		return IND_FACTORCORRECTOR;
	}

	public static int getIndSobremortalidad() {
		return IND_SOBREMORTALIDAD;
	}

	public static int getIndSobreriesgo() {
		return IND_SOBRERIESGO;
	}

	public static int getIndDescripciontabla() {
		return IND_DESCRIPCIONTABLA;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tablaMapfre == null) ? 0 : tablaMapfre.hashCode());
		result = prime * result + ((tablaTirea == null) ? 0 : tablaTirea.hashCode());
		result = prime * result + ((porcentajeTabla == null) ? 0 : porcentajeTabla.hashCode());
		result = prime * result + ((factorCorrector == null) ? 0 : factorCorrector.hashCode());
		result = prime * result + ((sobreMortalidad == null) ? 0 : sobreMortalidad.hashCode());
		result = prime * result + ((sobreRiesgo == null) ? 0 : sobreRiesgo.hashCode());
		result = prime * result + ((descripcionTabla == null) ? 0 : descripcionTabla.hashCode());
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
		Tab35012 other = (Tab35012) obj;
		if (tablaMapfre == null) {
			if (other.tablaMapfre != null)
				return false;
		} else if (!tablaMapfre.equals(other.tablaMapfre))
			return false;
		if (tablaTirea == null) {
			if (other.tablaTirea != null)
				return false;
		} else if (!porcentajeTabla.equals(other.tablaTirea))
			return false;
		if (porcentajeTabla == null) {
			if (other.porcentajeTabla != null)
				return false;
		} else if (!porcentajeTabla.equals(other.porcentajeTabla))
			return false;
		if (factorCorrector == null) {
			if (other.factorCorrector != null)
				return false;
		} else if (!factorCorrector.equals(other.factorCorrector))
			return false;
		if (sobreMortalidad == null) {
			if (other.sobreMortalidad != null)
				return false;
		} else if (!sobreMortalidad.equals(other.sobreMortalidad))
			return false;
		if (sobreRiesgo == null) {
			if (other.sobreRiesgo != null)
				return false;
		} else if (!sobreRiesgo.equals(other.sobreRiesgo))
			return false;
		if (descripcionTabla == null) {
			if (other.descripcionTabla != null)
				return false;
		} else if (!descripcionTabla.equals(other.descripcionTabla))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		return "Tab35012 [tablaMapfre=" + tablaMapfre + ", tablaTirea=" + tablaTirea + ", porcentajeTabla=" + porcentajeTabla + ", factorCorrector=" + factorCorrector
				+ ", sobreMortalidad=" + sobreMortalidad + ", sobreRiesgo=" + sobreRiesgo + ", descripcionTabla=" + descripcionTabla
				+ "]";
	}

	@Override
	public Tab35012Key getKey() {
		return new Tab35012Key(tablaMapfre);
	}
}

