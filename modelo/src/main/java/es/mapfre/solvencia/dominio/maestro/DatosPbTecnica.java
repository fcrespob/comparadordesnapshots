package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.DatosPbTecnicaKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosPbTecnica implements EntidadBase<DatosPbTecnicaKey> {

	public static final int IND_IARRASTRE = 0;
	public static final int IND_KPOLIZA = 1;
	public static final int IND_KSUBPOL = 2;
	public static final int IND_PGASTO = 3;
	public static final int IND_PREAJUSTE = 4;

	@PortableProperty(IND_IARRASTRE)
	private java.math.BigDecimal iarrastre;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOL)
	private Integer ksubpol;
	@PortableProperty(IND_PGASTO)
	private java.math.BigDecimal pgasto;
	@PortableProperty(IND_PREAJUSTE)
	private java.math.BigDecimal preajuste;

	public java.math.BigDecimal getIarrastre() {
		return iarrastre;
	}

	public void setIarrastre(java.math.BigDecimal iarrastre) {
		this.iarrastre = iarrastre;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpol() {
		return ksubpol;
	}

	public void setKsubpol(Integer ksubpol) {
		this.ksubpol = ksubpol;
	}

	public java.math.BigDecimal getPgasto() {
		return pgasto;
	}

	public void setPgasto(java.math.BigDecimal pgasto) {
		this.pgasto = pgasto;
	}

	public java.math.BigDecimal getPreajuste() {
		return preajuste;
	}

	public void setPreajuste(java.math.BigDecimal preajuste) {
		this.preajuste = preajuste;
	}

	@Override
	public DatosPbTecnicaKey getKey() {
		return new DatosPbTecnicaKey(kpoliza, ksubpol);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iarrastre == null) ? 0 : iarrastre.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
		result = prime * result + ((pgasto == null) ? 0 : pgasto.hashCode());
		result = prime * result
				+ ((preajuste == null) ? 0 : preajuste.hashCode());
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
		DatosPbTecnica other = (DatosPbTecnica) obj;
		if (iarrastre == null) {
			if (other.iarrastre != null) {
				return false;
			}
		} else if (!iarrastre.equals(other.iarrastre)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (ksubpol == null) {
			if (other.ksubpol != null) {
				return false;
			}
		} else if (!ksubpol.equals(other.ksubpol)) {
			return false;
		}
		if (pgasto == null) {
			if (other.pgasto != null) {
				return false;
			}
		} else if (!pgasto.equals(other.pgasto)) {
			return false;
		}
		if (preajuste == null) {
			if (other.preajuste != null) {
				return false;
			}
		} else if (!preajuste.equals(other.preajuste)) {
			return false;
		}
		return true;
	}
}