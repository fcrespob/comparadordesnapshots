package es.mapfre.scr.tasasAnulacion.dominio.entidades;

import java.io.Serializable;
import java.math.BigDecimal;

import es.mapfre.scr.tasasAnulacion.dominio.EntidadBase;
import es.mapfre.scr.tasasAnulacion.dominio.keys.SalidaKey;

public class Salida implements EntidadBase<SalidaKey>,
		Serializable {

	private static final long serialVersionUID = 1L;
	public static final int IND_CODTABLA = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_KANIOSDESDE = 2;
	public static final int IND_PROBABANUL = 3;
	public static final int IND_POLIZAVIGENTES = 4;
	
	private String  codTabla;
	private String  fecCierre;
	private BigDecimal kaniosdesde;
	private String  probabAnul;
	private BigDecimal  polizaVigentes;
	
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

	public String getProbabAnul() {
		return probabAnul;
	}

	public void setProbabAnul(String probabAnul) {
		this.probabAnul = probabAnul;
	}

	public BigDecimal getPolizaVigentes() {
		return polizaVigentes;
	}

	public void setPolizaVigentes(BigDecimal polizaVigentes) {
		this.polizaVigentes = polizaVigentes;
	}

	public BigDecimal getKaniosdesde() {
		return kaniosdesde;
	}

	public void setKaniosdesde(BigDecimal kaniosdesde) {
		this.kaniosdesde = kaniosdesde;
	}

	public Salida() {
		super();
	}

	public Salida(String codTabla, String fecCierre,
			BigDecimal kaniosdesde, String probabAnul, BigDecimal polizaVigentes) {
		super();
		this.codTabla = codTabla;
		this.fecCierre = fecCierre;
		this.kaniosdesde = kaniosdesde;
		this.probabAnul = probabAnul;
		this.polizaVigentes = polizaVigentes;
	}

	@Override
	public SalidaKey getKey() {
		return new SalidaKey(codTabla, fecCierre, kaniosdesde);
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
		result = prime * result + ((polizaVigentes == null) ? 0 : polizaVigentes.hashCode());
		result = prime * result + ((probabAnul == null) ? 0 : probabAnul.hashCode());
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
		Salida other = (Salida) obj;
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
		if (polizaVigentes == null) {
			if (other.polizaVigentes != null)
				return false;
		} else if (!polizaVigentes.equals(other.polizaVigentes))
			return false;
		if (probabAnul == null) {
			if (other.probabAnul != null)
				return false;
		} else if (!probabAnul.equals(other.probabAnul))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TasaMensualizada [");
		if (codTabla != null) {
			builder.append("codTabla=");
			builder.append(codTabla);
			builder.append(", ");
		}
		if (fecCierre != null) {
			builder.append("fecCierre=");
			builder.append(fecCierre);
			builder.append(", ");
		}
		if (kaniosdesde != null) {
			builder.append("kaniosdesde=");
			builder.append(kaniosdesde);
			builder.append(", ");
		}
		if (probabAnul != null) {
			builder.append("probabAnul=");
			builder.append(probabAnul);
			builder.append(", ");
		}
		if (polizaVigentes != null) {
			builder.append("polizaVigentes=");
			builder.append(polizaVigentes);
		}
		
		builder.append("]");
		return builder.toString();
	}

}
