package es.mapfre.solvencia.dominio.entregables;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.SwCobroComCsvKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class SwCobroComCsv implements EntidadBase<SwCobroComCsvKey>{ 

	public static final int IND_KPOL = 1;
	public static final int IND_KSUBPOL = 2;
	public static final int IND_COM = 3;
	public static final int IND_COMCALC = 4;
	public static final int IND_PCORREC = 5;
	public static final int IND_TXT_CABECERA = 6;

	
	@PortableProperty(SwCobroComCsv.IND_KPOL)
	private Long kpoliza;
	@PortableProperty(SwCobroComCsv.IND_KSUBPOL)
	private Integer ksubpoliza;
	@PortableProperty(SwCobroComCsv.IND_COM)
	private java.math.BigDecimal comision;
	@PortableProperty(SwCobroComCsv.IND_COMCALC)
	private java.math.BigDecimal comisioncalc;
	@PortableProperty(SwCobroComCsv.IND_PCORREC)
	private java.math.BigDecimal pcorrector;
	
	@PortableProperty(FPSL.IND_TXT_CABECERA)
	private String txtCabecera;
	
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

	public java.math.BigDecimal getComision() {
		return comision;
	}

	public void setComision(java.math.BigDecimal comision) {
		this.comision = comision;
	}

	public java.math.BigDecimal getComisioncalc() {
		return comisioncalc;
	}

	public void setComisioncalc(java.math.BigDecimal comisioncalc) {
		this.comisioncalc = comisioncalc;
	}

	public java.math.BigDecimal getPcorrector() {
		return pcorrector;
	}

	public void setPcorrector(java.math.BigDecimal pcorrector) {
		this.pcorrector = pcorrector;
	}

	public String getTxtCabecera() {
		return txtCabecera;
	}

	public void setTxtCabecera(String txtCabecera) {
		this.txtCabecera = txtCabecera;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((comision == null) ? 0 : comision.hashCode());
		result = prime * result + ((comisioncalc == null) ? 0 : comisioncalc.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((pcorrector == null) ? 0 : pcorrector.hashCode());
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
		SwCobroComCsv other = (SwCobroComCsv) obj;
		if (comision == null) {
			if (other.comision != null)
				return false;
		} else if (!comision.equals(other.comision))
			return false;
		if (comisioncalc == null) {
			if (other.comisioncalc != null)
				return false;
		} else if (!comisioncalc.equals(other.comisioncalc))
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
		if (pcorrector == null) {
			if (other.pcorrector != null)
				return false;
		} else if (!pcorrector.equals(other.pcorrector))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SWCOBROCOMCSV [kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", comision=" + comision
				+ ", comisioncalc=" + comisioncalc + ", pcorrector=" + pcorrector + "]";
	}

	@Override
	public SwCobroComCsvKey getKey() {
		return new SwCobroComCsvKey(kpoliza, ksubpoliza);
	}

}
