package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CabeceraTablaExperienciaKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ComisionesParticipadasCOMKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ComisionesParticipadasCOM implements EntidadBase<ComisionesParticipadasCOMKey>{
	public static final int IND_KCARTEORIG = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_KGARANTIA = 3;
	public static final int IND_INDICADOR = 4;
	public static final int IND_FALTADES = 5;
	public static final int IND_FALTAHAS = 6;
	public static final int IND_KFVIGDES = 7;
	public static final int IND_KFVIGHAS = 8;
	public static final int IND_BASECALCCOMI1 = 9;
	public static final int IND_BASECALCCOMI2 = 10;
	public static final int IND_BASECALCCOMI3 = 11;
	public static final int IND_MESPAGOPAGCOMI = 12;
	public static final int IND_NPERICOMI1 = 13;
	public static final int IND_PCOMISIONA1 = 14;
	public static final int IND_NPERICOMI2 = 15;
	public static final int IND_PCOMISIONA2 = 16;
	public static final int IND_NPERICOMI3 = 17;
	public static final int IND_PCOMISIONA3 = 18;

	@PortableProperty(IND_KCARTEORIG)
	private Integer kCarteOrig;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kModalidad;
	@PortableProperty(IND_KGARANTIA)
	private Integer kGarantia;
	@PortableProperty(IND_INDICADOR)
	private String indicador;
	@PortableProperty(IND_FALTADES)
	private Timestamp faltaDes;
	@PortableProperty(IND_FALTAHAS)
	private Timestamp faltaHas;
	@PortableProperty(IND_KFVIGDES)
	private Timestamp kFvigDes;
	@PortableProperty(IND_KFVIGHAS)
	private Timestamp kFvigHas;
	@PortableProperty(IND_BASECALCCOMI1)
	private String baseCalcComi1;
	@PortableProperty(IND_BASECALCCOMI2)
	private String baseCalcComi2;
	@PortableProperty(IND_BASECALCCOMI3)
	private String baseCalcComi3;
	@PortableProperty(IND_MESPAGOPAGCOMI)
	private String mesPagoPagComi;
	@PortableProperty(IND_NPERICOMI1)
	private Integer nPeriComi1;
	@PortableProperty(value = IND_PCOMISIONA1, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pComisiona1;
	@PortableProperty(IND_NPERICOMI2)
	private Integer nPeriComi2;
	@PortableProperty(value = IND_PCOMISIONA2, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pComisiona2;
	@PortableProperty(IND_NPERICOMI3)
	private Integer nPeriComi3;
	@PortableProperty(value = IND_PCOMISIONA3, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pComisiona3;

	public Integer getkCarteOrig() {
		return kCarteOrig;
	}

	public void setkCarteOrig(Integer kCarteOrig) {
		this.kCarteOrig = kCarteOrig;
	}

	public Integer getkModalidad() {
		return kModalidad;
	}

	public void setkModalidad(Integer kModalidad) {
		this.kModalidad = kModalidad;
	}

	public Integer getkGarantia() {
		return kGarantia;
	}

	public void setkGarantia(Integer kGarantia) {
		this.kGarantia = kGarantia;
	}

	public String getIndicador() {
		return indicador;
	}

	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}

	public Timestamp getFaltaDes() {
		return faltaDes;
	}

	public void setFaltaDes(Timestamp faltaDes) {
		this.faltaDes = faltaDes;
	}

	public Timestamp getFaltaHas() {
		return faltaHas;
	}

	public void setFaltaHas(Timestamp faltaHas) {
		this.faltaHas = faltaHas;
	}

	public Timestamp getkFvigDes() {
		return kFvigDes;
	}

	public void setkFvigDes(Timestamp kFvigDes) {
		this.kFvigDes = kFvigDes;
	}

	public Timestamp getkFvigHas() {
		return kFvigHas;
	}

	public void setkFvigHas(Timestamp kFvigHas) {
		this.kFvigHas = kFvigHas;
	}

	public String getBaseCalcComi1() {
		return baseCalcComi1;
	}

	public void setBaseCalcComi1(String baseCalcComi1) {
		this.baseCalcComi1 = baseCalcComi1;
	}

	public String getBaseCalcComi2() {
		return baseCalcComi2;
	}

	public void setBaseCalcComi2(String baseCalcComi2) {
		this.baseCalcComi2 = baseCalcComi2;
	}

	public String getBaseCalcComi3() {
		return baseCalcComi3;
	}

	public void setBaseCalcComi3(String baseCalcComi3) {
		this.baseCalcComi3 = baseCalcComi3;
	}

	public String getMesPagoPagComi() {
		return mesPagoPagComi;
	}

	public void setMesPagoPagComi(String mesPagoPagComi) {
		this.mesPagoPagComi = mesPagoPagComi;
	}

	public Integer getnPeriComi1() {
		return nPeriComi1;
	}

	public void setnPeriComi1(Integer nPeriComi1) {
		this.nPeriComi1 = nPeriComi1;
	}

	public java.math.BigDecimal getpComisiona1() {
		return pComisiona1;
	}

	public void setpComisiona1(java.math.BigDecimal pComisiona1) {
		this.pComisiona1 = pComisiona1;
	}

	public Integer getnPeriComi2() {
		return nPeriComi2;
	}

	public void setnPeriComi2(Integer nPeriComi2) {
		this.nPeriComi2 = nPeriComi2;
	}

	public java.math.BigDecimal getpComisiona2() {
		return pComisiona2;
	}

	public void setpComisiona2(java.math.BigDecimal pComisiona2) {
		this.pComisiona2 = pComisiona2;
	}

	public Integer getnPeriComi3() {
		return nPeriComi3;
	}

	public void setnPeriComi3(Integer nPeriComi3) {
		this.nPeriComi3 = nPeriComi3;
	}

	public java.math.BigDecimal getpComisiona3() {
		return pComisiona3;
	}

	public void setpComisiona3(java.math.BigDecimal pComisiona3) {
		this.pComisiona3 = pComisiona3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((baseCalcComi1 == null) ? 0 : baseCalcComi1.hashCode());
		result = prime * result + ((baseCalcComi2 == null) ? 0 : baseCalcComi2.hashCode());
		result = prime * result + ((baseCalcComi3 == null) ? 0 : baseCalcComi3.hashCode());
		result = prime * result + ((faltaDes == null) ? 0 : faltaDes.hashCode());
		result = prime * result + ((faltaHas == null) ? 0 : faltaHas.hashCode());
		result = prime * result + ((indicador == null) ? 0 : indicador.hashCode());
		result = prime * result + ((kCarteOrig == null) ? 0 : kCarteOrig.hashCode());
		result = prime * result + ((kFvigDes == null) ? 0 : kFvigDes.hashCode());
		result = prime * result + ((kFvigHas == null) ? 0 : kFvigHas.hashCode());
		result = prime * result + ((kGarantia == null) ? 0 : kGarantia.hashCode());
		result = prime * result + ((kModalidad == null) ? 0 : kModalidad.hashCode());
		result = prime * result + ((mesPagoPagComi == null) ? 0 : mesPagoPagComi.hashCode());
		result = prime * result + ((nPeriComi1 == null) ? 0 : nPeriComi1.hashCode());
		result = prime * result + ((nPeriComi2 == null) ? 0 : nPeriComi2.hashCode());
		result = prime * result + ((nPeriComi3 == null) ? 0 : nPeriComi3.hashCode());
		result = prime * result + ((pComisiona1 == null) ? 0 : pComisiona1.hashCode());
		result = prime * result + ((pComisiona2 == null) ? 0 : pComisiona2.hashCode());
		result = prime * result + ((pComisiona3 == null) ? 0 : pComisiona3.hashCode());
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
		ComisionesParticipadasCOM other = (ComisionesParticipadasCOM) obj;
		if (baseCalcComi1 == null) {
			if (other.baseCalcComi1 != null)
				return false;
		} else if (!baseCalcComi1.equals(other.baseCalcComi1))
			return false;
		if (baseCalcComi2 == null) {
			if (other.baseCalcComi2 != null)
				return false;
		} else if (!baseCalcComi2.equals(other.baseCalcComi2))
			return false;
		if (baseCalcComi3 == null) {
			if (other.baseCalcComi3 != null)
				return false;
		} else if (!baseCalcComi3.equals(other.baseCalcComi3))
			return false;
		if (faltaDes == null) {
			if (other.faltaDes != null)
				return false;
		} else if (!faltaDes.equals(other.faltaDes))
			return false;
		if (faltaHas == null) {
			if (other.faltaHas != null)
				return false;
		} else if (!faltaHas.equals(other.faltaHas))
			return false;
		if (indicador == null) {
			if (other.indicador != null)
				return false;
		} else if (!indicador.equals(other.indicador))
			return false;
		if (kCarteOrig == null) {
			if (other.kCarteOrig != null)
				return false;
		} else if (!kCarteOrig.equals(other.kCarteOrig))
			return false;
		if (kFvigDes == null) {
			if (other.kFvigDes != null)
				return false;
		} else if (!kFvigDes.equals(other.kFvigDes))
			return false;
		if (kFvigHas == null) {
			if (other.kFvigHas != null)
				return false;
		} else if (!kFvigHas.equals(other.kFvigHas))
			return false;
		if (kGarantia == null) {
			if (other.kGarantia != null)
				return false;
		} else if (!kGarantia.equals(other.kGarantia))
			return false;
		if (kModalidad == null) {
			if (other.kModalidad != null)
				return false;
		} else if (!kModalidad.equals(other.kModalidad))
			return false;
		if (mesPagoPagComi == null) {
			if (other.mesPagoPagComi != null)
				return false;
		} else if (!mesPagoPagComi.equals(other.mesPagoPagComi))
			return false;
		if (nPeriComi1 == null) {
			if (other.nPeriComi1 != null)
				return false;
		} else if (!nPeriComi1.equals(other.nPeriComi1))
			return false;
		if (nPeriComi2 == null) {
			if (other.nPeriComi2 != null)
				return false;
		} else if (!nPeriComi2.equals(other.nPeriComi2))
			return false;
		if (nPeriComi3 == null) {
			if (other.nPeriComi3 != null)
				return false;
		} else if (!nPeriComi3.equals(other.nPeriComi3))
			return false;
		if (pComisiona1 == null) {
			if (other.pComisiona1 != null)
				return false;
		} else if (!pComisiona1.equals(other.pComisiona1))
			return false;
		if (pComisiona2 == null) {
			if (other.pComisiona2 != null)
				return false;
		} else if (!pComisiona2.equals(other.pComisiona2))
			return false;
		if (pComisiona3 == null) {
			if (other.pComisiona3 != null)
				return false;
		} else if (!pComisiona3.equals(other.pComisiona3))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComisionesParticipadasCOM [kCarteOrig=" + kCarteOrig + ", kModalidad=" + kModalidad + ", kGarantia="
				+ kGarantia + ", indicador=" + indicador + ", faltaDes=" + faltaDes + ", faltaHas=" + faltaHas
				+ ", kFvigDes=" + kFvigDes + ", kFvigHas=" + kFvigHas + ", baseCalcComi1=" + baseCalcComi1
				+ ", baseCalcComi2=" + baseCalcComi2 + ", baseCalcComi3=" + baseCalcComi3 + ", mesPagoPagComi="
				+ mesPagoPagComi + ", nPeriComi1=" + nPeriComi1 + ", pComisiona1=" + pComisiona1 + ", nPeriComi2="
				+ nPeriComi2 + ", pComisiona2=" + pComisiona2 + ", nPeriComi3=" + nPeriComi3 + ", pComisiona3="
				+ pComisiona3 + "]";
	}

	@Override
	public ComisionesParticipadasCOMKey getKey() {
		// TODO Auto-generated method stub
		return new ComisionesParticipadasCOMKey(kCarteOrig, kModalidad,kGarantia,kFvigDes,kFvigHas,faltaDes,faltaHas);
	}

}
