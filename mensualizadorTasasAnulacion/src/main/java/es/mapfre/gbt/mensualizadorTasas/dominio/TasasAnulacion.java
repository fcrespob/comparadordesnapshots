package es.mapfre.gbt.mensualizadorTasas.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

import es.mapfre.gbt.mensualizadorTasas.key.TasasAnulacionKey;

public class TasasAnulacion implements EntidadBase<TasasAnulacionKey>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int IND_KTABLAANU = 0;
	public static final int IND_KFCIERRE = 1;
	public static final int IND_KANIOSDESDE = 2;
	public static final int IND_NANIOSHASTA = 3;
	public static final int IND_PPROBANU = 4;
	public static final int IND_CUSUARALTA = 5;
	public static final int IND_FALTA = 6;
	public static final int IND_CUSUARMODIF = 7;
	public static final int IND_FMODIF = 8;
	
	private String ktablaanu;
	private String kfcierre;
	private BigDecimal kaniosdesde;
	private BigDecimal nanioshasta;
	private BigDecimal pprobanu;
	private String cusuaralta;
	private String falta;
	private String cusuarmodif;
	private String fmodif;
	
	public String getKtablaanu() {
		return ktablaanu;
	}

	public void setKtablaanu(String ktablaanu) {
		this.ktablaanu = ktablaanu;
	}

	public String getKfcierre() {
		return kfcierre;
	}

	public void setKfcierre(String kfcierre) {
		this.kfcierre = kfcierre;
	}

	public BigDecimal getKaniosdesde() {
		return kaniosdesde;
	}

	public void setKaniosdesde(BigDecimal kaniosdesde) {
		this.kaniosdesde = kaniosdesde;
	}

	public BigDecimal getNanioshasta() {
		return nanioshasta;
	}

	public void setNanioshasta(BigDecimal nanioshasta) {
		this.nanioshasta = nanioshasta;
	}

	public BigDecimal getPprobanu() {
		return pprobanu;
	}

	public void setPprobanu(BigDecimal pprobanu) {
		this.pprobanu = pprobanu;
	}

	public String getCusuaralta() {
		return cusuaralta;
	}

	public void setCusuaralta(String cusuaralta) {
		this.cusuaralta = cusuaralta;
	}

	public String getFalta() {
		return falta;
	}

	public void setFalta(String falta) {
		this.falta = falta;
	}

	public String getCusuarmodif() {
		return cusuarmodif;
	}

	public void setCusuarmodif(String cusuarmodif) {
		this.cusuarmodif = cusuarmodif;
	}

	public String getFmodif() {
		return fmodif;
	}

	public void setFmodif(String fmodif) {
		this.fmodif = fmodif;
	}
	
	public TasasAnulacion() {
		super();
	}

	public TasasAnulacion(String ktablaanu, String kfcierre,
			BigDecimal kaniosdesde, BigDecimal nanioshasta, BigDecimal pprobanu,
			String cusuaralta, String falta, String cusuarmodif, String fmodif) {
		super();
		this.ktablaanu = ktablaanu;
		this.kfcierre = kfcierre;
		this.kaniosdesde = kaniosdesde;
		this.nanioshasta = nanioshasta;
		this.pprobanu = pprobanu;
		this.cusuaralta = cusuaralta;
		this.falta = falta;
		this.cusuarmodif = cusuarmodif;
		this.fmodif = fmodif;
	}

	@Override
	public TasasAnulacionKey getKey() {
		return new TasasAnulacionKey(ktablaanu, kfcierre, kaniosdesde);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cusuaralta == null) ? 0 : cusuaralta.hashCode());
		result = prime * result
				+ ((cusuarmodif == null) ? 0 : cusuarmodif.hashCode());
		result = prime * result + ((falta == null) ? 0 : falta.hashCode());
		result = prime * result + ((fmodif == null) ? 0 : fmodif.hashCode());
		result = prime * result
				+ ((kfcierre == null) ? 0 : kfcierre.hashCode());
		result = prime * result
				+ ((kaniosdesde == null) ? 0 : kaniosdesde.hashCode());
		result = prime * result
				+ ((ktablaanu == null) ? 0 : ktablaanu.hashCode());
		result = prime * result
				+ ((nanioshasta == null) ? 0 : nanioshasta.hashCode());
		result = prime * result
				+ ((pprobanu == null) ? 0 : pprobanu.hashCode());
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
		TasasAnulacion other = (TasasAnulacion) obj;
		if (cusuaralta == null) {
			if (other.cusuaralta != null)
				return false;
		} else if (!cusuaralta.equals(other.cusuaralta))
			return false;
		if (cusuarmodif == null) {
			if (other.cusuarmodif != null)
				return false;
		} else if (!cusuarmodif.equals(other.cusuarmodif))
			return false;
		if (falta == null) {
			if (other.falta != null)
				return false;
		} else if (!falta.equals(other.falta))
			return false;
		if (fmodif == null) {
			if (other.fmodif != null)
				return false;
		} else if (!fmodif.equals(other.fmodif))
			return false;
		if (kfcierre == null) {
			if (other.kfcierre != null)
				return false;
		} else if (!kfcierre.equals(other.kfcierre))
			return false;
		if (kaniosdesde == null) {
			if (other.kaniosdesde != null)
				return false;
		} else if (!kaniosdesde.equals(other.kaniosdesde))
			return false;
		if (ktablaanu == null) {
			if (other.ktablaanu != null)
				return false;
		} else if (!ktablaanu.equals(other.ktablaanu))
			return false;
		if (nanioshasta == null) {
			if (other.nanioshasta != null)
				return false;
		} else if (!nanioshasta.equals(other.nanioshasta))
			return false;
		if (pprobanu == null) {
			if (other.pprobanu != null)
				return false;
		} else if (!pprobanu.equals(other.pprobanu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TasasAnulacion [ktablaanu=");
		builder.append(ktablaanu);
		builder.append(", kfcierre=");
		builder.append(kfcierre);
		builder.append(", kaniosdesde=");
		builder.append(kaniosdesde);
		builder.append(", nanioshasta=");
		builder.append(nanioshasta);
		builder.append(", pprobanu=");
		builder.append(pprobanu);
		builder.append(", cusuaralta=");
		builder.append(cusuaralta);
		builder.append(", falta=");
		builder.append(falta);
		builder.append(", cusuarmodif=");
		builder.append(cusuarmodif);
		builder.append(", fmodif=");
		builder.append(fmodif);
		builder.append("]");
		return builder.toString();
	}

}
