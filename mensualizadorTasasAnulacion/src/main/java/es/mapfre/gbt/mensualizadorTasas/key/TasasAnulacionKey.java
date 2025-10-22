package es.mapfre.gbt.mensualizadorTasas.key;

import java.math.BigDecimal;

public class TasasAnulacionKey {
	 private String ktablaanu;
	 private String kfcierre;
	 private BigDecimal kaniosdesde;
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
	public TasasAnulacionKey() {
		super();
	}
	public TasasAnulacionKey(String ktablaanu, String kfcierre,
			BigDecimal kaniosdesde) {
		super();
		this.ktablaanu = ktablaanu;
		this.kfcierre = kfcierre;
		this.kaniosdesde = kaniosdesde;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kfcierre == null) ? 0 : kfcierre.hashCode());
		result = prime * result
				+ ((kaniosdesde == null) ? 0 : kaniosdesde.hashCode());
		result = prime * result
				+ ((ktablaanu == null) ? 0 : ktablaanu.hashCode());
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
		TasasAnulacionKey other = (TasasAnulacionKey) obj;
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
		return true;
	}
	
}
