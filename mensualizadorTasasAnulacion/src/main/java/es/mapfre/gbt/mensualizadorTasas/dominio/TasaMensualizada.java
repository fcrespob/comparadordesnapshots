package es.mapfre.gbt.mensualizadorTasas.dominio;

import java.io.Serializable;
import java.math.BigDecimal;

import es.mapfre.gbt.mensualizadorTasas.key.TasaMensualizadaKey;

public class TasaMensualizada implements EntidadBase<TasaMensualizadaKey>,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int IND_KTABLAANU = 0;
	public static final int IND_KFCIERRE = 1;
	public static final int IND_KANIOSDESDE = 2;
	public static final int IND_TASA_RED = 3;
	public static final int IND_POLIZA = 4;
	public static final int IND_USUARIOALTA = 5;
	public static final int IND_FECHAALTA = 6;
	
	private String ktablaanu;
	private String kfcierre;
	private BigDecimal kaniosdesde;
	private BigDecimal tasaRed;
	private BigDecimal poliza;
	private String usuarioAlta;
	private String fechaAlta;
	
	public String getKtablaanu() {
		return ktablaanu;
	}

	public BigDecimal getTasaRed() {
		return tasaRed;
	}

	public void setTasaRed(BigDecimal tasaRed) {
		this.tasaRed = tasaRed;
	}

	public BigDecimal getPoliza() {
		return poliza;
	}

	public void setPoliza(BigDecimal poliza) {
		this.poliza = poliza;
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
	
	public String getUsuarioAlta() {
		return usuarioAlta;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	public String getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(String fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public TasaMensualizada() {
		super();
	}

	public TasaMensualizada(String ktablaanu, String kfcierre,
			BigDecimal kaniosdesde, BigDecimal tasaRed, BigDecimal poliza,
			String usuarioAlta, String fechaAlta) {
		super();
		this.ktablaanu = ktablaanu;
		this.kfcierre = kfcierre;
		this.kaniosdesde = kaniosdesde;
		this.tasaRed = tasaRed;
		this.poliza = poliza;
		this.usuarioAlta = usuarioAlta;
		this.fechaAlta = fechaAlta;
	}

	@Override
	public TasaMensualizadaKey getKey() {
		return new TasaMensualizadaKey(ktablaanu, kfcierre, kaniosdesde);
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
		result = prime * result + ((poliza == null) ? 0 : poliza.hashCode());
		result = prime * result + ((tasaRed == null) ? 0 : tasaRed.hashCode());
		result = prime * result + ((usuarioAlta == null) ? 0 : usuarioAlta.hashCode());
		result = prime * result + ((fechaAlta == null) ? 0 : fechaAlta.hashCode());
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
		TasaMensualizada other = (TasaMensualizada) obj;
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
		if (poliza == null) {
			if (other.poliza != null)
				return false;
		} else if (!poliza.equals(other.poliza))
			return false;
		if (tasaRed == null) {
			if (other.tasaRed != null)
				return false;
		} else if (!tasaRed.equals(other.tasaRed))
			return false;
		
		if (usuarioAlta == null) {
			if (other.usuarioAlta != null)
				return false;
		} else if (!usuarioAlta.equals(other.usuarioAlta))
			return false;
		
		if (fechaAlta == null) {
			if (other.fechaAlta != null)
				return false;
		} else if (!fechaAlta.equals(other.fechaAlta))
			return false;
		
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TasaMensualizada [");
		if (ktablaanu != null) {
			builder.append("ktablaanu=");
			builder.append(ktablaanu);
			builder.append(", ");
		}
		if (kfcierre != null) {
			builder.append("kfcierre=");
			builder.append(kfcierre);
			builder.append(", ");
		}
		if (kaniosdesde != null) {
			builder.append("kaniosdesde=");
			builder.append(kaniosdesde);
			builder.append(", ");
		}
		if (tasaRed != null) {
			builder.append("tasaRed=");
			builder.append(tasaRed);
			builder.append(", ");
		}
		if (poliza != null) {
			builder.append("poliza=");
			builder.append(poliza);
		}
		
		if (usuarioAlta != null) {
			builder.append("usuarioAlta=");
			builder.append(usuarioAlta);
			builder.append(", ");
		}
		
		if (fechaAlta != null) {
			builder.append("fechaAlta=");
			builder.append(fechaAlta);
			builder.append(", ");
		}
		
		builder.append("]");
		return builder.toString();
	}

}
