package es.mapfre.scr.tasasAnulacion.dominio.keys;

import java.math.BigDecimal;

public class SalidaKey {
	private String codTabla;
	private String fecCierre;
	private BigDecimal kaniosdesde;
	 
	public String getCodTabla() {
		return codTabla;
	}
	public void setCodTabla(String codTabla) {
		this.codTabla = codTabla;
	}
	public String getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(String fecCierre) {
		this.fecCierre = fecCierre;
	}
	public BigDecimal getKaniosdesde() {
		return kaniosdesde;
	}
	public void setKaniosdesde(BigDecimal kaniosdesde) {
		this.kaniosdesde = kaniosdesde;
	}
	public SalidaKey() {
		super();
	}
	public SalidaKey(String codTabla, String fecCierre,
			BigDecimal kaniosdesde) {
		super();
		this.codTabla = codTabla;
		this.fecCierre = fecCierre;
		this.kaniosdesde = kaniosdesde;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result
				+ ((kaniosdesde == null) ? 0 : kaniosdesde.hashCode());
		result = prime * result
				+ ((codTabla == null) ? 0 : codTabla.hashCode());
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
		SalidaKey other = (SalidaKey) obj;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		if (kaniosdesde == null) {
			if (other.kaniosdesde != null)
				return false;
		} else if (!kaniosdesde.equals(other.kaniosdesde))
			return false;
		if (codTabla == null) {
			if (other.codTabla != null)
				return false;
		} else if (!codTabla.equals(other.codTabla))
			return false;
		return true;
	}
	
}
