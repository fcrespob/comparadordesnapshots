package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.SwCobroComisionesKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class SwCobroComisiones implements EntidadBase<SwCobroComisionesKey> {
	
	public static final int IND_KPOLIZA = 1;
	public static final int IND_KSUBPOLIZA = 2;
	public static final int IND_SIGNO = 3;
	public static final int IND_COMISION= 4;
	public static final int IND_KFECANO = 5;

	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_SIGNO)
	private String signo;
	@PortableProperty(IND_COMISION)
	private java.math.BigDecimal comision;
	@PortableProperty(IND_KFECANO)
	private Integer fecano;
	
	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public String getSigno() {
		return signo;
	}

	public void setSigno(String signo) {
		this.signo = signo;
	}

	public java.math.BigDecimal getComision() {
		return comision;
	}

	public void setComision(java.math.BigDecimal comision) {
		this.comision = comision;
	}

	public Integer getFecano() {
		return fecano;
	}

	public void setFecano(Integer fecano) {
		this.fecano = fecano;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comision == null) ? 0 : comision.hashCode());
		result = prime * result + ((fecano == null) ? 0 : fecano.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((signo == null) ? 0 : signo.hashCode());
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
		SwCobroComisiones other = (SwCobroComisiones) obj;
		if (comision == null) {
			if (other.comision != null)
				return false;
		} else if (!comision.equals(other.comision))
			return false;
		if (fecano == null) {
			if (other.fecano != null)
				return false;
		} else if (!fecano.equals(other.fecano))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (signo == null) {
			if (other.signo != null)
				return false;
		} else if (!signo.equals(other.signo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SwCobroComisiones [kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", signo=" + signo
				+ ", comision=" + comision + ", fecano=" + fecano + "]";
	}

	@Override
	public SwCobroComisionesKey getKey() {
		// TODO Auto-generated method stub
		return new SwCobroComisionesKey(kpoliza, ksubpoliza);
	}
	
}