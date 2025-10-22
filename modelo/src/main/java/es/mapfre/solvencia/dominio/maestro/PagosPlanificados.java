package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.PagosPlanificadosKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class PagosPlanificados implements EntidadBase<PagosPlanificadosKey> {

	public static final int IND_CGARANTIA = 0;
	public static final int IND_CPRESTAENTORNO = 1;
	public static final int IND_CPRESTAFICT = 2;
	public static final int IND_EPLREABRUTO = 3;
	public static final int IND_EPLREANETO = 4;
	public static final int IND_ERRORPERIODIC = 5;
	public static final int IND_FPLREAEFECTO = 6;
	public static final int IND_KAJUSTE = 7;
	public static final int IND_KCERTI = 8;
	public static final int IND_KGRSUS = 9;

	public static final int IND_KPOLIZA = 10;
	public static final int IND_KPRESTA = 11;
	public static final int IND_KSUBPOL = 12;
	public static final int IND_PCOASE = 13;
	public static final int IND_PERIODICIDAD = 14;
	public static final int IND_FILLER = 15;

	@PortableProperty(IND_CGARANTIA)
	private Integer cgarantia;
	@PortableProperty(IND_CPRESTAENTORNO)
	private String cprestaEntorno;
	@PortableProperty(IND_CPRESTAFICT)
	private String cprestaFict;
	@PortableProperty(IND_EPLREABRUTO)
	private java.math.BigDecimal eplreaBruto;
	@PortableProperty(IND_EPLREANETO)
	private java.math.BigDecimal eplreaNeto;
	@PortableProperty(IND_ERRORPERIODIC)
	private String errorPeriodic;
	@PortableProperty(IND_FPLREAEFECTO)
	private Timestamp fplreaEfecto;
	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_KCERTI)
	private Integer kcerti;
	@PortableProperty(IND_KGRSUS)
	private Integer kgrsus;

	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KPRESTA)
	private String kpresta;
	@PortableProperty(IND_KSUBPOL)
	private Integer ksubpol;
	@PortableProperty(IND_PCOASE)
	private java.math.BigDecimal pcoase;
	@PortableProperty(IND_PERIODICIDAD)
	private String periodicidad;
	@PortableProperty(IND_FILLER)
	private String filler;

	public java.math.BigDecimal getEplreaBruto() {
		return eplreaBruto;
	}

	public void setEplreaBruto(java.math.BigDecimal eplreaBruto) {
		this.eplreaBruto = eplreaBruto;
	}

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

	public java.math.BigDecimal getEplreaNeto() {
		return eplreaNeto;
	}

	public void setEplreaNeto(java.math.BigDecimal eplreaNeto) {
		this.eplreaNeto = eplreaNeto;
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

	public Integer getKgrsus() {
		return kgrsus;
	}

	public void setKgrsus(Integer kgrsus) {
		this.kgrsus = kgrsus;
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

	public String getErrorPeriodic() {
		return errorPeriodic;
	}

	public void setErrorPeriodic(String errorPeriodic) {
		this.errorPeriodic = errorPeriodic;
	}
	
	public String getFiller() {
		return filler;
	}

	public void setFiller(String filler) {
		this.filler = filler;
	}

	@Override
	public PagosPlanificadosKey getKey() {
		return new PagosPlanificadosKey(kajuste, kcerti, kgrsus, ksubpol,
				kpoliza, kpresta, cgarantia, cprestaEntorno, cprestaFict,fplreaEfecto, Integer.parseInt(filler));
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
		result = prime * result + ((kgrsus == null) ? 0 : kgrsus.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpresta == null) ? 0 : kpresta.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		PagosPlanificados other = (PagosPlanificados) obj;
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
		if (kgrsus == null) {
			if (other.kgrsus != null)
				return false;
		} else if (!kgrsus.equals(other.kgrsus))
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
	
}