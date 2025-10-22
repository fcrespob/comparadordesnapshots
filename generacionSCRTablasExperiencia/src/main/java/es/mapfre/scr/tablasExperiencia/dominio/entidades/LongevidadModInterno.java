package es.mapfre.scr.tablasExperiencia.dominio.entidades;

import java.math.BigDecimal;
import java.util.List;

import es.mapfre.scr.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.scr.tablasExperiencia.dominio.keys.LongevidadModInternoKey;

public class LongevidadModInterno implements EntidadBase<LongevidadModInternoKey>{

	public static final int IND_FECCIERRE = 0;
	public static final int IND_KSEXO = 1;
	public static final int IND_KEDADFIJA = 2;
	public static final int IND_PESTRES = 3;
	
	private String feccierre;
	private String ksexo;
	private Integer kedadfija;
	private List<BigDecimal> pestres;
	
	public LongevidadModInterno() {
		super();
	}

	public String getFeccierre() {
		return feccierre;
	}

	public void setFeccierre(String feccierre) {
		this.feccierre = feccierre;
	}

	public String getKsexo() {
		return ksexo;
	}

	public void setKsexo(String ksexo) {
		this.ksexo = ksexo;
	}

	public Integer getKedadfija() {
		return kedadfija;
	}

	public void setKedadfija(Integer kedadfija) {
		this.kedadfija = kedadfija;
	}

	public List<BigDecimal> getPestres() {
		return pestres;
	}

	public void setPestres(List<BigDecimal> pestres) {
		this.pestres = pestres;
	}

	@Override
	public LongevidadModInternoKey getKey() {
		return new LongevidadModInternoKey(feccierre, ksexo, kedadfija);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result
				+ ((ksexo == null) ? 0 : ksexo.hashCode());
		result = prime * result
				+ ((kedadfija == null) ? 0 : kedadfija.hashCode());
		result = prime * result
				+ ((pestres == null) ? 0 : pestres.hashCode());
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
		LongevidadModInterno other = (LongevidadModInterno) obj;
		if (feccierre == null) {
			if (other.feccierre != null) {
				return false;
			}
		} else if (!feccierre.equals(other.feccierre)) {
			return false;
		}
		if (ksexo == null) {
			if (other.ksexo != null) {
				return false;
			}
		} else if (!ksexo.equals(other.ksexo)) {
			return false;
		}
		if (kedadfija == null) {
			if (other.kedadfija != null) {
				return false;
			}
		} else if (!kedadfija.equals(other.kedadfija)) {
			return false;
		}
		if (pestres == null) {
			if (other.pestres != null) {
				return false;
			}
		} else if (!pestres.equals(other.pestres)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValoresEstres [feccierre=" + feccierre
				+ ", ksexo=" + ksexo + ", kedadfija=" + kedadfija
				+ "]";
	}
	
	
}
