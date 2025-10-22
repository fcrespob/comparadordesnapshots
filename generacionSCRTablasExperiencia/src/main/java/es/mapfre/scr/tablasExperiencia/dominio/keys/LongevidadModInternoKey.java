package es.mapfre.scr.tablasExperiencia.dominio.keys;

public class LongevidadModInternoKey {

	private String feccierre;
	private String ksexo;
	private Integer kedadfija;
	
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
		LongevidadModInternoKey other = (LongevidadModInternoKey) obj;
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
		return true;
	}

	public LongevidadModInternoKey(String feccierre, String ksexo,
			Integer kedadfija) {
		super();
		this.feccierre = feccierre;
		this.ksexo = ksexo;
		this.kedadfija = kedadfija;
	}

	public LongevidadModInternoKey() {
		super();
	}

	@Override
	public String toString() {
		return "ValoresEstresKey [feccierre=" + feccierre
				+ ", ksexo=" + ksexo + ", kedadfija=" + kedadfija
				+ "]";
	}
}
