package es.mapfre.gbt.tablasExperiencia.dominio.entidades;


public class Incidencias {

	public static final int IND_KBASETEC = 0;
	public static final int IND_KCOMPANIA = 1;
	public static final int IND_KNEGOCIO = 2;
	public static final int IND_KRIESGO = 3;
	public static final int IND_KSEXO = 4;
	public static final int IND_KCATEG = 5;
	public static final int IND_KEDADDESDE = 6;
	public static final int IND_EDADHASTA = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_KFDESDE = 9;
	public static final int IND_FHASTA = 10;
	public static final int IND_CTABBASE = 11;
	public static final int IND_CODIGOERROR = 12;
	public static final int IND_DESCERRO = 13;

	//

	private String kbasetec;
	private Integer kcompania;
	private String knegocio;
	private String kriesgo;
	private String ksexo;
	private String kcateg;
	private Integer kedaddesde;
	private Integer edadhasta;
	private Integer kmodalidad;
	private String kfdesde;
	private String fhasta;
	private Integer ctabbase;
	private String codigoerror;
	private String descerror;


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

	public Integer getEdadhasta() {
		return edadhasta;
	}

	public void setEdadhasta(Integer edadhasta) {
		this.edadhasta = edadhasta;
	}

	public String getFhasta() {
		return fhasta;
	}

	public void setFhasta(String fhasta) {
		this.fhasta = fhasta;
	}

	public Integer getCtabbase() {
		return ctabbase;
	}

	public void setCtabbase(Integer ctabbase) {
		this.ctabbase = ctabbase;
	}

	public String getCodigoerror() {
		return codigoerror;
	}

	public void setCodigoerror(String codigoerror) {
		this.codigoerror = codigoerror;
	}

	public String getDescerror() {
		return descerror;
	}

	public void setDescerror(String descerror) {
		this.descerror = descerror;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codigoerror == null) ? 0 : codigoerror.hashCode());
		result = prime * result
				+ ((ctabbase == null) ? 0 : ctabbase.hashCode());
		result = prime * result
				+ ((descerror == null) ? 0 : descerror.hashCode());
		result = prime * result
				+ ((edadhasta == null) ? 0 : edadhasta.hashCode());
		result = prime * result + ((fhasta == null) ? 0 : fhasta.hashCode());
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
		Incidencias other = (Incidencias) obj;
		if (codigoerror == null) {
			if (other.codigoerror != null)
				return false;
		} else if (!codigoerror.equals(other.codigoerror))
			return false;
		if (ctabbase == null) {
			if (other.ctabbase != null)
				return false;
		} else if (!ctabbase.equals(other.ctabbase))
			return false;
		if (descerror == null) {
			if (other.descerror != null)
				return false;
		} else if (!descerror.equals(other.descerror))
			return false;
		if (edadhasta == null) {
			if (other.edadhasta != null)
				return false;
		} else if (!edadhasta.equals(other.edadhasta))
			return false;
		if (fhasta == null) {
			if (other.fhasta != null)
				return false;
		} else if (!fhasta.equals(other.fhasta))
			return false;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("IncidenciasTExp [kbasetec=");
		builder.append(kbasetec);
		builder.append(", kcompania=");
		builder.append(kcompania);
		builder.append(", knegocio=");
		builder.append(knegocio);
		builder.append(", kriesgo=");
		builder.append(kriesgo);
		builder.append(", ksexo=");
		builder.append(ksexo);
		builder.append(", kcateg=");
		builder.append(kcateg);
		builder.append(", kedaddesde=");
		builder.append(kedaddesde);
		builder.append(", edadhasta=");
		builder.append(edadhasta);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kfdesde=");
		builder.append(kfdesde);
		builder.append(", fhasta=");
		builder.append(fhasta);
		builder.append(", ctabbase=");
		builder.append(ctabbase);
		builder.append(", codigoerror=");
		builder.append(codigoerror);
		builder.append(", descerror=");
		builder.append(descerror);
		builder.append("]");
		return builder.toString();
	}
}
