package es.mapfre.coaseguro.tirea.dominio.entidades;



import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.PagosPlanKey;


public class PagosPlan implements EntidadBase<PagosPlanKey> {

	public static final int IND_CGARANTIA = 0;
	public static final int IND_CPRESTAENTORNO = 1;
	public static final int IND_CPRESTAFICT = 2;
	public static final int IND_EPLREABRUTO = 3;
	public static final int IND_EPLREANETO = 4;
	public static final int IND_ERRORPERIODIC = 5;
	public static final int IND_FPLREAEFECTO = 6;
	public static final int IND_KAJUSTE = 7;
	public static final int IND_KCERTI = 8;
	public static final int IND_NSUSCRI = 9;

	public static final int IND_KPOLIZA = 10;
	public static final int IND_KPRESTA = 11;
	public static final int IND_KSUBPOL = 12;
	public static final int IND_PCOASE = 13;
	public static final int IND_PERIODICIDAD = 14;
	public static final int IND_FILLER = 15;

	private Integer cgarantia;
	private String cprestaEntorno;
	private String cprestaFict;
	private java.math.BigDecimal eplreaBruto;
	private java.math.BigDecimal eplreaNeto;
	private String errorPeriodic;
	private Timestamp fplreaEfecto;
	private Integer kajuste;
	private Integer kcerti;
	private Integer nsuscri;

	private Long kpoliza;
	private String kpresta;
	private Integer ksubpol;
	private java.math.BigDecimal pcoase;
	private String periodicidad;
	private String filler;
	
	public Integer getCgarantia() {
		return cgarantia;
	}

	public void setCgarantia(Integer cgarantia) {
		this.cgarantia = cgarantia;
	}

	public String getCprestaEntorno() {
		return cprestaEntorno;
	}

	public void setCprestaEntorno(String cprestaEntorno) {
		this.cprestaEntorno = cprestaEntorno;
	}

	public String getCprestaFict() {
		return cprestaFict;
	}

	public void setCprestaFict(String cprestaFict) {
		this.cprestaFict = cprestaFict;
	}

	public java.math.BigDecimal getEplreaBruto() {
		return eplreaBruto;
	}

	public void setEplreaBruto(java.math.BigDecimal eplreaBruto) {
		this.eplreaBruto = eplreaBruto;
	}

	public java.math.BigDecimal getEplreaNeto() {
		return eplreaNeto;
	}

	public void setEplreaNeto(java.math.BigDecimal eplreaNeto) {
		this.eplreaNeto = eplreaNeto;
	}

	public String getErrorPeriodic() {
		return errorPeriodic;
	}

	public void setErrorPeriodic(String errorPeriodic) {
		this.errorPeriodic = errorPeriodic;
	}

	public Timestamp getFplreaEfecto() {
		return fplreaEfecto;
	}

	public void setFplreaEfecto(Timestamp fplreaEfecto) {
		this.fplreaEfecto = fplreaEfecto;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public Integer getKcerti() {
		return kcerti;
	}

	public void setKcerti(Integer kcerti) {
		this.kcerti = kcerti;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKpresta() {
		return kpresta;
	}

	public void setKpresta(String kpresta) {
		this.kpresta = kpresta;
	}

	public Integer getKsubpol() {
		return ksubpol;
	}

	public void setKsubpol(Integer ksubpol) {
		this.ksubpol = ksubpol;
	}

	public java.math.BigDecimal getPcoase() {
		return pcoase;
	}

	public void setPcoase(java.math.BigDecimal pcoase) {
		this.pcoase = pcoase;
	}

	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cgarantia == null) ? 0 : cgarantia.hashCode());
		result = prime * result + ((cprestaEntorno == null) ? 0 : cprestaEntorno.hashCode());
		result = prime * result + ((cprestaFict == null) ? 0 : cprestaFict.hashCode());
		result = prime * result + ((eplreaBruto == null) ? 0 : eplreaBruto.hashCode());
		result = prime * result + ((eplreaNeto == null) ? 0 : eplreaNeto.hashCode());
		result = prime * result + ((errorPeriodic == null) ? 0 : errorPeriodic.hashCode());
		result = prime * result + ((filler == null) ? 0 : filler.hashCode());
		result = prime * result + ((fplreaEfecto == null) ? 0 : fplreaEfecto.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcerti == null) ? 0 : kcerti.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpresta == null) ? 0 : kpresta.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pcoase == null) ? 0 : pcoase.hashCode());
		result = prime * result + ((periodicidad == null) ? 0 : periodicidad.hashCode());
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
		PagosPlan other = (PagosPlan) obj;
		if (cgarantia == null) {
			if (other.cgarantia != null)
				return false;
		} else if (!cgarantia.equals(other.cgarantia))
			return false;
		if (cprestaEntorno == null) {
			if (other.cprestaEntorno != null)
				return false;
		} else if (!cprestaEntorno.equals(other.cprestaEntorno))
			return false;
		if (cprestaFict == null) {
			if (other.cprestaFict != null)
				return false;
		} else if (!cprestaFict.equals(other.cprestaFict))
			return false;
		if (eplreaBruto == null) {
			if (other.eplreaBruto != null)
				return false;
		} else if (!eplreaBruto.equals(other.eplreaBruto))
			return false;
		if (eplreaNeto == null) {
			if (other.eplreaNeto != null)
				return false;
		} else if (!eplreaNeto.equals(other.eplreaNeto))
			return false;
		if (errorPeriodic == null) {
			if (other.errorPeriodic != null)
				return false;
		} else if (!errorPeriodic.equals(other.errorPeriodic))
			return false;
		if (filler == null) {
			if (other.filler != null)
				return false;
		} else if (!filler.equals(other.filler))
			return false;
		if (fplreaEfecto == null) {
			if (other.fplreaEfecto != null)
				return false;
		} else if (!fplreaEfecto.equals(other.fplreaEfecto))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kcerti == null) {
			if (other.kcerti != null)
				return false;
		} else if (!kcerti.equals(other.kcerti))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kpresta == null) {
			if (other.kpresta != null)
				return false;
		} else if (!kpresta.equals(other.kpresta))
			return false;
		if (ksubpol == null) {
			if (other.ksubpol != null)
				return false;
		} else if (!ksubpol.equals(other.ksubpol))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (pcoase == null) {
			if (other.pcoase != null)
				return false;
		} else if (!pcoase.equals(other.pcoase))
			return false;
		if (periodicidad == null) {
			if (other.periodicidad != null)
				return false;
		} else if (!periodicidad.equals(other.periodicidad))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PagosPlan [cgarantia=" + cgarantia + ", cprestaEntorno=" + cprestaEntorno + ", cprestaFict="
				+ cprestaFict + ", eplreaBruto=" + eplreaBruto + ", eplreaNeto=" + eplreaNeto + ", errorPeriodic="
				+ errorPeriodic + ", fplreaEfecto=" + fplreaEfecto + ", kajuste=" + kajuste + ", kcerti=" + kcerti
				+ ", nsuscri=" + nsuscri + ", kpoliza=" + kpoliza + ", kpresta=" + kpresta + ", ksubpol=" + ksubpol
				+ ", pcoase=" + pcoase + ", periodicidad=" + periodicidad + ", filler=" + filler + "]";
	}

	@Override
	public PagosPlanKey getKey() {
		return new PagosPlanKey(kajuste, kcerti, nsuscri, ksubpol,
				kpoliza, kpresta, cgarantia, cprestaEntorno, cprestaFict,fplreaEfecto, Integer.parseInt(filler));
	}
	
}