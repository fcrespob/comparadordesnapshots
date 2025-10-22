package es.mapfre.gbt.tablasExperiencia.dominio.keys;


public class CabeceraTablaExperienciaKey{
	
	
	private Integer ktabla;
	
	private String fecAnula;

	
	private String fecAlta;

	public CabeceraTablaExperienciaKey(Integer ktabla, String fecAnula,
			String fecAlta) {
		super();
		this.ktabla = ktabla;
		this.fecAnula = fecAnula;
		this.fecAlta = fecAlta;
	}

	public CabeceraTablaExperienciaKey(){
		super();
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecAlta == null) ? 0 : fecAlta.hashCode());
		result = prime * result
				+ ((fecAnula == null) ? 0 : fecAnula.hashCode());
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
		CabeceraTablaExperienciaKey other = (CabeceraTablaExperienciaKey) obj;
		if (fecAlta == null) {
			if (other.fecAlta != null) {
				return false;
			}
		} else if (!fecAlta.equals(other.fecAlta)) {
			return false;
		}
		if (fecAnula == null) {
			if (other.fecAnula != null) {
				return false;
			}
		} else if (!fecAnula.equals(other.fecAnula)) {
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

	public Integer getKtabla() {
		return ktabla;
	}

	public void setKtabla(Integer ktabla) {
		this.ktabla = ktabla;
	}

	public String getFecAnula() {
		return fecAnula;
	}

	public void setFecAnula(String fecAnula) {
		this.fecAnula = fecAnula;
	}

	public String getFecAlta() {
		return fecAlta;
	}

	public void setFecAlta(String fecAlta) {
		this.fecAlta = fecAlta;
	}
	
	
	
}

