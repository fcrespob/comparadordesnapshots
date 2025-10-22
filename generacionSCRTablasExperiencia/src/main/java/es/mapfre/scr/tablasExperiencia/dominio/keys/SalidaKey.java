package es.mapfre.scr.tablasExperiencia.dominio.keys;




public class SalidaKey {



	private String kfchcierre;

	private String kbasetec;

	private Integer kcompania;

	private String knegocio;

	private String kriesgo;

	private String ksexo;

	private String kcateg;

	private Integer kedadfija;

	private Integer kmodalidad;

	public SalidaKey(String kfchcierre, String kbasetec, Integer kcompania,
			String knegocio, String kriesgo, String ksexo, String kcateg,
			Integer kedadfija, Integer kmodalidad) {
		super();
		this.kfchcierre = kfchcierre;
		this.kbasetec = kbasetec;
		this.kcompania = kcompania;
		this.knegocio = knegocio;
		this.kriesgo = kriesgo;
		this.ksexo = ksexo;
		this.kcateg = kcateg;
		this.kedadfija = kedadfija;
		this.kmodalidad = kmodalidad;
	}

	public SalidaKey(){
		super();
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kedadfija == null) ? 0 : kedadfija.hashCode());
		result = prime * result
				+ ((kcateg == null) ? 0 : kcateg.hashCode());
		result = prime * result + ((ksexo == null) ? 0 : ksexo.hashCode());
		result = prime * result 
				+ ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result	+ ((knegocio == null) ? 0 : knegocio.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result + ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kfchcierre == null) ? 0 : kfchcierre.hashCode());
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
		SalidaKey other = (SalidaKey) obj;
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kedadfija == null) {
			if (other.kedadfija != null) {
				return false;
			}
		} else if (!kedadfija.equals(other.kedadfija)) {
			return false;
		}
		if (kcateg == null) {
			if (other.kcateg != null) {
				return false;
			}
		} else if (!kcateg.equals(other.kcateg)) {
			return false;
		}
		if (ksexo == null) {
			if (other.ksexo != null) {
				return false;
			}
		} else if (!ksexo.equals(other.ksexo)) {
			return false;
		}
		if (kriesgo == null) {
			if (other.kriesgo != null) {
				return false;
			}
		} else if (!kriesgo.equals(other.kriesgo)) {
			return false;
		}
		if (knegocio == null) {
			if (other.knegocio != null) {
				return false;
			}
		} else if (!knegocio.equals(other.knegocio)) {
			return false;
		}
		if (kcompania == null) {
			if (other.kcompania != null) {
				return false;
			}
		} else if (!kcompania.equals(other.kcompania)) {
			return false;
		}
		if (kbasetec == null) {
			if (other.kbasetec != null) {
				return false;
			}
		} else if (!kbasetec.equals(other.kbasetec)) {
			return false;
		}
		if (kfchcierre == null) {
			if (other.kfchcierre != null) {
				return false;
			}
		} else if (!kfchcierre.equals(other.kfchcierre)) {
			return false;
		}
		return true;
	}

	public String getKfchcierre() {
		return kfchcierre;
	}

	public void setKfchcierre(String kfchcierre) {
		this.kfchcierre = kfchcierre;
	}

	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}

	public Integer getKcompania() {
		return kcompania;
	}

	public void setKcompania(Integer kcompania) {
		this.kcompania = kcompania;
	}

	public String getKnegocio() {
		return knegocio;
	}

	public void setKnegocio(String knegocio) {
		this.knegocio = knegocio;
	}

	public String getKriesgo() {
		return kriesgo;
	}

	public void setKriesgo(String kriesgo) {
		this.kriesgo = kriesgo;
	}

	public String getKsexo() {
		return ksexo;
	}

	public void setKsexo(String ksexo) {
		this.ksexo = ksexo;
	}

	public String getKcateg() {
		return kcateg;
	}

	public void setKcateg(String kcateg) {
		this.kcateg = kcateg;
	}

	public Integer getKedadfija() {
		return kedadfija;
	}

	public void setKedadfija(Integer kedadfija) {
		this.kedadfija = kedadfija;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}




}

