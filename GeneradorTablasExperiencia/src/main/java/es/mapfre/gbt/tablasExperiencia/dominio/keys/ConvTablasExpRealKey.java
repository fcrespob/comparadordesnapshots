package es.mapfre.gbt.tablasExperiencia.dominio.keys;


public class ConvTablasExpRealKey{
	private String kbasetec;
	private Integer kcompania;
	private String knegocio;
	private String kriesgo;
	private String ksexo;
	private String kcateg;
	private Integer kedaddesde;
	private Integer kmodalidad;
	private String kfdesde;
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
	public Integer getKedaddesde() {
		return kedaddesde;
	}
	public void setKedaddesde(Integer kedaddesde) {
		this.kedaddesde = kedaddesde;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public String getKfdesde() {
		return kfdesde;
	}
	public void setKfdesde(String kfdesde) {
		this.kfdesde = kfdesde;
	}
	public ConvTablasExpRealKey() {
		super();
	}
	public ConvTablasExpRealKey(String kbasetec, Integer kcompania,
			String knegocio, String kriesgo, String ksexo, String kcateg,
			Integer kedaddesde, Integer kmodalidad, String kfdesde) {
		super();
		this.kbasetec = kbasetec;
		this.kcompania = kcompania;
		this.knegocio = knegocio;
		this.kriesgo = kriesgo;
		this.ksexo = ksexo;
		this.kcateg = kcateg;
		this.kedaddesde = kedaddesde;
		this.kmodalidad = kmodalidad;
		this.kfdesde = kfdesde;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcateg == null) ? 0 : kcateg.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result
				+ ((kedaddesde == null) ? 0 : kedaddesde.hashCode());
		result = prime * result + ((kfdesde == null) ? 0 : kfdesde.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((knegocio == null) ? 0 : knegocio.hashCode());
		result = prime * result + ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result + ((ksexo == null) ? 0 : ksexo.hashCode());
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
		ConvTablasExpRealKey other = (ConvTablasExpRealKey) obj;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcateg == null) {
			if (other.kcateg != null)
				return false;
		} else if (!kcateg.equals(other.kcateg))
			return false;
		if (kcompania == null) {
			if (other.kcompania != null)
				return false;
		} else if (!kcompania.equals(other.kcompania))
			return false;
		if (kedaddesde == null) {
			if (other.kedaddesde != null)
				return false;
		} else if (!kedaddesde.equals(other.kedaddesde))
			return false;
		if (kfdesde == null) {
			if (other.kfdesde != null)
				return false;
		} else if (!kfdesde.equals(other.kfdesde))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (knegocio == null) {
			if (other.knegocio != null)
				return false;
		} else if (!knegocio.equals(other.knegocio))
			return false;
		if (kriesgo == null) {
			if (other.kriesgo != null)
				return false;
		} else if (!kriesgo.equals(other.kriesgo))
			return false;
		if (ksexo == null) {
			if (other.ksexo != null)
				return false;
		} else if (!ksexo.equals(other.ksexo))
			return false;
		return true;
	}

}
