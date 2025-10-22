package es.mapfre.scr.gastos.dominio.keys;

public class SalidaGREKey {

	private Integer ccanal;
	private String cnegocio;
	private String fecDesde;
	private String fecHasta;
	private Integer kmodalidad;
	private String kramo;
	private String ktipobt;

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getFecDesde() {
		return fecDesde;
	}

	public void setFecDesde(String fecDesde) {
		this.fecDesde = fecDesde;
	}

	public String getFecHasta() {
		return fecHasta;
	}

	public void setFecHasta(String fecHasta) {
		this.fecHasta = fecHasta;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public String getKtipobt() {
		return ktipobt;
	}

	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((fecDesde == null) ? 0 : fecDesde.hashCode());
		result = prime * result
				+ ((fecHasta == null) ? 0 : fecHasta.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ktipobt == null) ? 0 : ktipobt.hashCode());
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
		SalidaGREKey other = (SalidaGREKey) obj;
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (fecDesde == null) {
			if (other.fecDesde != null) {
				return false;
			}
		} else if (!fecDesde.equals(other.fecDesde)) {
			return false;
		}
		if (fecHasta == null) {
			if (other.fecHasta != null) {
				return false;
			}
		} else if (!fecHasta.equals(other.fecHasta)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kramo == null) {
			if (other.kramo != null) {
				return false;
			}
		} else if (!kramo.equals(other.kramo)) {
			return false;
		}
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		return true;
	}


	public SalidaGREKey(Integer ccanal, String cnegocio, String fecDesde, String fecHasta,
			Integer kmodalidad, String kramo, String ktipobt) {
		super();
		this.ccanal = ccanal;
		this.cnegocio = cnegocio;
		this.fecDesde = fecDesde;
		this.fecHasta = fecHasta;
		this.kmodalidad = kmodalidad;
		this.kramo = kramo;
		this.ktipobt = ktipobt;
	}

	public SalidaGREKey() {
		super();
	}
}

