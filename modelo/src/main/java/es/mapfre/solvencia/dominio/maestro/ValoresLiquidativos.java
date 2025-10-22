package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.ValoresLiquidativosKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresLiquidativos implements EntidadBase<ValoresLiquidativosKey> {

	public static final int IND_AINPTOTFONDO = 0;
	public static final int IND_ANPARTICIPACION = 1;
	public static final int IND_CINPTOTFONDO = 2;
	public static final int IND_CNPARTICIPACION = 3;
	public static final int IND_KCESTAPOL = 4;
	public static final int IND_KFONDO = 5;
	public static final int IND_KMODALIDAD = 6;
	public static final int IND_KPOLIZA = 7;
	public static final int IND_KRAMO = 8;
	public static final int IND_PFVALOR = 9;

	public static final int IND_PKVALOR = 10;
	public static final int IND_PINPTOTFONDO = 11;
	public static final int IND_PMONEDA = 12;
	public static final int IND_PNPARTICIPACION = 13;
	public static final int IND_KCERTIFICADO = 14;

	@PortableProperty(IND_AINPTOTFONDO)
	private java.math.BigDecimal ainptotfondo;
	@PortableProperty(IND_ANPARTICIPACION)
	private java.math.BigDecimal anparticipacion;
	@PortableProperty(IND_CINPTOTFONDO)
	private java.math.BigDecimal cinptotfondo;
	@PortableProperty(IND_CNPARTICIPACION)
	private java.math.BigDecimal cnparticipacion;
	@PortableProperty(IND_KCESTAPOL)
	private String kcestapol;
	@PortableProperty(IND_KFONDO)
	private String kfondo;
	@PortableProperty(IND_KMODALIDAD)
	private String kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_PFVALOR)
	private Timestamp pfvalor;

	@PortableProperty(IND_PKVALOR)
	private java.math.BigDecimal pkvalor;
	@PortableProperty(IND_PINPTOTFONDO)
	private java.math.BigDecimal pinptotfondo;
	@PortableProperty(IND_PMONEDA)
	private String pmoneda;
	@PortableProperty(IND_PNPARTICIPACION)
	private java.math.BigDecimal pnparticipacion;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;

	public java.math.BigDecimal getAinptotfondo() {
		return ainptotfondo;
	}

	public void setAinptotfondo(java.math.BigDecimal ainptotfondo) {
		this.ainptotfondo = ainptotfondo;
	}

	public java.math.BigDecimal getAnparticipacion() {
		return anparticipacion;
	}

	public void setAnparticipacion(java.math.BigDecimal anparticipacion) {
		this.anparticipacion = anparticipacion;
	}

	public java.math.BigDecimal getCinptotfondo() {
		return cinptotfondo;
	}

	public void setCinptotfondo(java.math.BigDecimal cinptotfondo) {
		this.cinptotfondo = cinptotfondo;
	}

	public java.math.BigDecimal getCnparticipacion() {
		return cnparticipacion;
	}

	public void setCnparticipacion(java.math.BigDecimal cnparticipacion) {
		this.cnparticipacion = cnparticipacion;
	}

	public String getKcestapol() {
		return kcestapol;
	}

	public void setKcestapol(String kcestapol) {
		this.kcestapol = kcestapol;
	}

	public String getKfondo() {
		return kfondo;
	}

	public void setKfondo(String kfondo) {
		this.kfondo = kfondo;
	}

	public String getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(String kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Timestamp getPfvalor() {
		return pfvalor;
	}

	public void setPfvalor(Timestamp pfvalor) {
		this.pfvalor = pfvalor;
	}

	public java.math.BigDecimal getPkvalor() {
		return pkvalor;
	}

	public void setPkvalor(java.math.BigDecimal pkvalor) {
		this.pkvalor = pkvalor;
	}

	public java.math.BigDecimal getPinptotfondo() {
		return pinptotfondo;
	}

	public void setPinptotfondo(java.math.BigDecimal pinptotfondo) {
		this.pinptotfondo = pinptotfondo;
	}

	public String getPmoneda() {
		return pmoneda;
	}

	public void setPmoneda(String pmoneda) {
		this.pmoneda = pmoneda;
	}

	public java.math.BigDecimal getPnparticipacion() {
		return pnparticipacion;
	}

	public void setPnparticipacion(java.math.BigDecimal pnparticipacion) {
		this.pnparticipacion = pnparticipacion;
	}
	
	public Integer getkcertificado() {
		return kcertificado;
	}

	public void setkcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	@Override
	public ValoresLiquidativosKey getKey() {
		return new ValoresLiquidativosKey(kpoliza);	
	}

	@Override // NOSONAR
	public int hashCode() { // NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ainptotfondo == null) ? 0 : ainptotfondo.hashCode());
		result = prime * result
				+ ((anparticipacion == null) ? 0 : anparticipacion.hashCode());
		result = prime * result
				+ ((cinptotfondo == null) ? 0 : cinptotfondo.hashCode());
		result = prime * result
				+ ((cnparticipacion == null) ? 0 : cnparticipacion.hashCode());
		result = prime * result
				+ ((kcestapol == null) ? 0 : kcestapol.hashCode());
		result = prime * result + ((kfondo == null) ? 0 : kfondo.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((pfvalor == null) ? 0 : pfvalor.hashCode());
		result = prime * result
				+ ((pinptotfondo == null) ? 0 : pinptotfondo.hashCode());
		result = prime * result + ((pkvalor == null) ? 0 : pkvalor.hashCode());
		result = prime * result + ((pmoneda == null) ? 0 : pmoneda.hashCode());
		result = prime * result
				+ ((pnparticipacion == null) ? 0 : pnparticipacion.hashCode());
		return result;
	}

	@Override // NOSONAR
	public boolean equals(Object obj) { // NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValoresLiquidativos other = (ValoresLiquidativos) obj;
		if (ainptotfondo == null) {
			if (other.ainptotfondo != null) {
				return false;
			}
		} else if (!ainptotfondo.equals(other.ainptotfondo)) {
			return false;
		}
		if (anparticipacion == null) {
			if (other.anparticipacion != null) {
				return false;
			}
		} else if (!anparticipacion.equals(other.anparticipacion)) {
			return false;
		}
		if (cinptotfondo == null) {
			if (other.cinptotfondo != null) {
				return false;
			}
		} else if (!cinptotfondo.equals(other.cinptotfondo)) {
			return false;
		}
		if (cnparticipacion == null) {
			if (other.cnparticipacion != null) {
				return false;
			}
		} else if (!cnparticipacion.equals(other.cnparticipacion)) {
			return false;
		}
		if (kcestapol == null) {
			if (other.kcestapol != null) {
				return false;
			}
		} else if (!kcestapol.equals(other.kcestapol)) {
			return false;
		}
		if (kfondo == null) {
			if (other.kfondo != null) {
				return false;
			}
		} else if (!kfondo.equals(other.kfondo)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (kramo == null) {
			if (other.kramo != null) {
				return false;
			}
		} else if (!kramo.equals(other.kramo)) {
			return false;
		}
		if (pfvalor == null) {
			if (other.pfvalor != null) {
				return false;
			}
		} else if (!pfvalor.equals(other.pfvalor)) {
			return false;
		}
		if (pinptotfondo == null) {
			if (other.pinptotfondo != null) {
				return false;
			}
		} else if (!pinptotfondo.equals(other.pinptotfondo)) {
			return false;
		}
		if (pkvalor == null) {
			if (other.pkvalor != null) {
				return false;
			}
		} else if (!pkvalor.equals(other.pkvalor)) {
			return false;
		}
		if (pmoneda == null) {
			if (other.pmoneda != null) {
				return false;
			}
		} else if (!pmoneda.equals(other.pmoneda)) {
			return false;
		}
		if (pnparticipacion == null) {
			if (other.pnparticipacion != null) {
				return false;
			}
		} else if (!pnparticipacion.equals(other.pnparticipacion)) {
			return false;
		}
		return true;
	}
	
	
}