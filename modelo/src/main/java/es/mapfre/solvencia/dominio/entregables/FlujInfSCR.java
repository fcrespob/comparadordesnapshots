package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.coherence.keys.entregables.FlujInfSCRKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujInfSCR implements EntidadBase<FlujInfSCRKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FCIERRE = 1;
	public static final int IND_CCANAL = 2;
	public static final int IND_NEGOCIO = 3;
	public static final int IND_UOA = 4;
	public static final int IND_KCARTERA_CONTRATO = 5;
	public static final int IND_KCARTERA_COHORT = 6;
	public static final int IND_KCARTERA_ONER = 7;
	public static final int IND_FPROYFLUJEST = 8;
	public static final int IND_TOT_FP_VIDA = 9;
	public static final int IND_TOT_FP_FALL = 10;
	public static final int IND_TOT_FP_GASTOS = 11;
	public static final int IND_TOT_FP_COMISIONES = 12;
	public static final int IND_TOT_FP_RESCATES = 13;
	public static final int IND_TOT_FP_COMPL = 14;
	public static final int IND_TOT_FP_PRIMAS = 15;
	public static final int IND_KCARTERAINV = 16;
	public static final int IND_TOT_FP_GTOAD = 17;
	public static final int IND_KMODALIDAD = 18;
	public static final int IND_TOT_FP = 19;
	
	public static final int IND_TOT_FP_VIDA_SCR = 20;
	public static final int IND_TOT_FP_FALL_SCR = 21;
	public static final int IND_TOT_FP_GASTOS_SCR = 22;
	public static final int IND_TOT_FP_COMISIONES_SCR = 23;
	public static final int IND_TOT_FP_RESCATES_SCR = 24;
	public static final int IND_TOT_FP_COMPL_SCR = 25;
	public static final int IND_TOT_FP_PRIMAS_SCR = 26;
	public static final int IND_KCARTERAINV_SCR = 27;
	public static final int IND_TOT_FP_GTOAD_SCR = 28;
	public static final int IND_KMODALIDAD_SCR = 29;
	public static final int IND_TOT_FP_SCR = 30;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_NEGOCIO)
	private String negocio;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_KCARTERA_CONTRATO)
	private String kcarteraContrato;
	@PortableProperty(IND_KCARTERA_COHORT)
	private String kcarteraCohort;
	@PortableProperty(IND_KCARTERA_ONER)
	private String kcarteraOner;
	@PortableProperty(IND_FPROYFLUJEST)
	private Timestamp fproyflujest;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv; 
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;	
	
	
	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public Timestamp getFproyflujest() {
		return fproyflujest;
	}

	public void setFproyflujest(Timestamp fproyflujest) {
		this.fproyflujest = fproyflujest;
	}

	@PortableProperty(value = IND_TOT_FP_VIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpVida;
	@PortableProperty(value = IND_TOT_FP_FALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpFall;
	@PortableProperty(value = IND_TOT_FP_GASTOS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGastos;
	@PortableProperty(value = IND_TOT_FP_COMISIONES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpComisiones;
	@PortableProperty(value = IND_TOT_FP_RESCATES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpRescates;
	@PortableProperty(value = IND_TOT_FP_COMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpCompl;
	@PortableProperty(value = IND_TOT_FP_PRIMAS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpPrimas;
	@PortableProperty(value = IND_TOT_FP_GTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGtoAd;
	@PortableProperty(value = IND_TOT_FP, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFp;
	
	@PortableProperty(value = IND_TOT_FP_VIDA_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpVidaSCR;
	@PortableProperty(value = IND_TOT_FP_FALL_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpFallSCR;
	@PortableProperty(value = IND_TOT_FP_GASTOS_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGastosSCR;
	@PortableProperty(value = IND_TOT_FP_COMISIONES_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpComisionesSCR;
	@PortableProperty(value = IND_TOT_FP_RESCATES_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpRescatesSCR;
	@PortableProperty(value = IND_TOT_FP_COMPL_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpComplSCR;
	@PortableProperty(value = IND_TOT_FP_PRIMAS_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpPrimasSCR;
	@PortableProperty(value = IND_TOT_FP_GTOAD_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGtoAdSCR;
	@PortableProperty(value = IND_TOT_FP_SCR, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpSCR;

	@Override
	public String getBt() {
		return bt;
	}

	@Override
	public void setBt(String bt) {
		this.bt = bt;
	}

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getNegocio() {
		return negocio;
	}

	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}

	public String getUoa() {
		return uoa;
	}

	public void setUoa(String uoa) {
		this.uoa = uoa;
	}

	public String getKcarteraContrato() {
		return kcarteraContrato;
	}

	public void setKcarteraContrato(String kcarteraContrato) {
		this.kcarteraContrato = kcarteraContrato;
	}

	public String getKcarteraCohort() {
		return kcarteraCohort;
	}

	public void setKcarteraCohort(String kcarteraCohort) {
		this.kcarteraCohort = kcarteraCohort;
	}

	public String getKcarteraOner() {
		return kcarteraOner;
	}

	public void setKcarteraOner(String kcarteraOner) {
		this.kcarteraOner = kcarteraOner;
	}

	public java.math.BigDecimal getTotFpVida() {
		return totFpVida;
	}

	public void setTotFpVida(java.math.BigDecimal totFpVida) {
		this.totFpVida = totFpVida;
	}

	public java.math.BigDecimal getTotFpFall() {
		return totFpFall;
	}

	public void setTotFpFall(java.math.BigDecimal totFpFall) {
		this.totFpFall = totFpFall;
	}

	public java.math.BigDecimal getTotFpGastos() {
		return totFpGastos;
	}

	public void setTotFpGastos(java.math.BigDecimal totFpGastos) {
		this.totFpGastos = totFpGastos;
	}

	public java.math.BigDecimal getTotFpComisiones() {
		return totFpComisiones;
	}

	public void setTotFpComisiones(java.math.BigDecimal totFpComisiones) {
		this.totFpComisiones = totFpComisiones;
	}

	public java.math.BigDecimal getTotFpRescates() {
		return totFpRescates;
	}

	public void setTotFpRescates(java.math.BigDecimal totFpRescates) {
		this.totFpRescates = totFpRescates;
	}

	public java.math.BigDecimal getTotFpCompl() {
		return totFpCompl;
	}

	public void setTotFpCompl(java.math.BigDecimal totFpCompl) {
		this.totFpCompl = totFpCompl;
	}

	public java.math.BigDecimal getTotFpPrimas() {
		return totFpPrimas;
	}

	public void setTotFpPrimas(java.math.BigDecimal totFpPrimas) {
		this.totFpPrimas = totFpPrimas;
	}

	public java.math.BigDecimal getTotFpGtoAd() {
		return totFpGtoAd;
	}

	public void setTotFpGtoAd(java.math.BigDecimal totFpGtoAd) {
		this.totFpGtoAd = totFpGtoAd;
	}
	
	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public java.math.BigDecimal getTotFp() {
		return totFp;
	}

	public void setTotFp(java.math.BigDecimal totFp) {
		this.totFp = totFp;
	}

	public java.math.BigDecimal getTotFpVidaSCR() {
		return totFpVidaSCR;
	}

	public void setTotFpVidaSCR(java.math.BigDecimal totFpVidaSCR) {
		this.totFpVidaSCR = totFpVidaSCR;
	}

	public java.math.BigDecimal getTotFpFallSCR() {
		return totFpFallSCR;
	}

	public void setTotFpFallSCR(java.math.BigDecimal totFpFallSCR) {
		this.totFpFallSCR = totFpFallSCR;
	}

	public java.math.BigDecimal getTotFpGastosSCR() {
		return totFpGastosSCR;
	}

	public void setTotFpGastosSCR(java.math.BigDecimal totFpGastosSCR) {
		this.totFpGastosSCR = totFpGastosSCR;
	}

	public java.math.BigDecimal getTotFpComisionesSCR() {
		return totFpComisionesSCR;
	}

	public void setTotFpComisionesSCR(java.math.BigDecimal totFpComisionesSCR) {
		this.totFpComisionesSCR = totFpComisionesSCR;
	}

	public java.math.BigDecimal getTotFpRescatesSCR() {
		return totFpRescatesSCR;
	}

	public void setTotFpRescatesSCR(java.math.BigDecimal totFpRescatesSCR) {
		this.totFpRescatesSCR = totFpRescatesSCR;
	}

	public java.math.BigDecimal getTotFpComplSCR() {
		return totFpComplSCR;
	}

	public void setTotFpComplSCR(java.math.BigDecimal totFpComplSCR) {
		this.totFpComplSCR = totFpComplSCR;
	}

	public java.math.BigDecimal getTotFpPrimasSCR() {
		return totFpPrimasSCR;
	}

	public void setTotFpPrimasSCR(java.math.BigDecimal totFpPrimasSCR) {
		this.totFpPrimasSCR = totFpPrimasSCR;
	}

	public java.math.BigDecimal getTotFpGtoAdSCR() {
		return totFpGtoAdSCR;
	}

	public void setTotFpGtoAdSCR(java.math.BigDecimal totFpGtoAdSCR) {
		this.totFpGtoAdSCR = totFpGtoAdSCR;
	}

	public java.math.BigDecimal getTotFpSCR() {
		return totFpSCR;
	}

	public void setTotFpSCR(java.math.BigDecimal totFpSCR) {
		this.totFpSCR = totFpSCR;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((kcarteraCohort == null) ? 0 : kcarteraCohort.hashCode());
		result = prime * result + ((kcarteraContrato == null) ? 0 : kcarteraContrato.hashCode());
		result = prime * result + ((kcarteraOner == null) ? 0 : kcarteraOner.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((negocio == null) ? 0 : negocio.hashCode());
		result = prime * result + ((totFp == null) ? 0 : totFp.hashCode());
		result = prime * result + ((totFpComisiones == null) ? 0 : totFpComisiones.hashCode());
		result = prime * result + ((totFpComisionesSCR == null) ? 0 : totFpComisionesSCR.hashCode());
		result = prime * result + ((totFpCompl == null) ? 0 : totFpCompl.hashCode());
		result = prime * result + ((totFpComplSCR == null) ? 0 : totFpComplSCR.hashCode());
		result = prime * result + ((totFpFall == null) ? 0 : totFpFall.hashCode());
		result = prime * result + ((totFpFallSCR == null) ? 0 : totFpFallSCR.hashCode());
		result = prime * result + ((totFpGastos == null) ? 0 : totFpGastos.hashCode());
		result = prime * result + ((totFpGastosSCR == null) ? 0 : totFpGastosSCR.hashCode());
		result = prime * result + ((totFpGtoAd == null) ? 0 : totFpGtoAd.hashCode());
		result = prime * result + ((totFpGtoAdSCR == null) ? 0 : totFpGtoAdSCR.hashCode());
		result = prime * result + ((totFpPrimas == null) ? 0 : totFpPrimas.hashCode());
		result = prime * result + ((totFpPrimasSCR == null) ? 0 : totFpPrimasSCR.hashCode());
		result = prime * result + ((totFpRescates == null) ? 0 : totFpRescates.hashCode());
		result = prime * result + ((totFpRescatesSCR == null) ? 0 : totFpRescatesSCR.hashCode());
		result = prime * result + ((totFpSCR == null) ? 0 : totFpSCR.hashCode());
		result = prime * result + ((totFpVida == null) ? 0 : totFpVida.hashCode());
		result = prime * result + ((totFpVidaSCR == null) ? 0 : totFpVidaSCR.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
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
		FlujInfSCR other = (FlujInfSCR) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fproyflujest == null) {
			if (other.fproyflujest != null)
				return false;
		} else if (!fproyflujest.equals(other.fproyflujest))
			return false;
		if (kcarteraCohort == null) {
			if (other.kcarteraCohort != null)
				return false;
		} else if (!kcarteraCohort.equals(other.kcarteraCohort))
			return false;
		if (kcarteraContrato == null) {
			if (other.kcarteraContrato != null)
				return false;
		} else if (!kcarteraContrato.equals(other.kcarteraContrato))
			return false;
		if (kcarteraOner == null) {
			if (other.kcarteraOner != null)
				return false;
		} else if (!kcarteraOner.equals(other.kcarteraOner))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (negocio == null) {
			if (other.negocio != null)
				return false;
		} else if (!negocio.equals(other.negocio))
			return false;
		if (totFp == null) {
			if (other.totFp != null)
				return false;
		} else if (!totFp.equals(other.totFp))
			return false;
		if (totFpComisiones == null) {
			if (other.totFpComisiones != null)
				return false;
		} else if (!totFpComisiones.equals(other.totFpComisiones))
			return false;
		if (totFpComisionesSCR == null) {
			if (other.totFpComisionesSCR != null)
				return false;
		} else if (!totFpComisionesSCR.equals(other.totFpComisionesSCR))
			return false;
		if (totFpCompl == null) {
			if (other.totFpCompl != null)
				return false;
		} else if (!totFpCompl.equals(other.totFpCompl))
			return false;
		if (totFpComplSCR == null) {
			if (other.totFpComplSCR != null)
				return false;
		} else if (!totFpComplSCR.equals(other.totFpComplSCR))
			return false;
		if (totFpFall == null) {
			if (other.totFpFall != null)
				return false;
		} else if (!totFpFall.equals(other.totFpFall))
			return false;
		if (totFpFallSCR == null) {
			if (other.totFpFallSCR != null)
				return false;
		} else if (!totFpFallSCR.equals(other.totFpFallSCR))
			return false;
		if (totFpGastos == null) {
			if (other.totFpGastos != null)
				return false;
		} else if (!totFpGastos.equals(other.totFpGastos))
			return false;
		if (totFpGastosSCR == null) {
			if (other.totFpGastosSCR != null)
				return false;
		} else if (!totFpGastosSCR.equals(other.totFpGastosSCR))
			return false;
		if (totFpGtoAd == null) {
			if (other.totFpGtoAd != null)
				return false;
		} else if (!totFpGtoAd.equals(other.totFpGtoAd))
			return false;
		if (totFpGtoAdSCR == null) {
			if (other.totFpGtoAdSCR != null)
				return false;
		} else if (!totFpGtoAdSCR.equals(other.totFpGtoAdSCR))
			return false;
		if (totFpPrimas == null) {
			if (other.totFpPrimas != null)
				return false;
		} else if (!totFpPrimas.equals(other.totFpPrimas))
			return false;
		if (totFpPrimasSCR == null) {
			if (other.totFpPrimasSCR != null)
				return false;
		} else if (!totFpPrimasSCR.equals(other.totFpPrimasSCR))
			return false;
		if (totFpRescates == null) {
			if (other.totFpRescates != null)
				return false;
		} else if (!totFpRescates.equals(other.totFpRescates))
			return false;
		if (totFpRescatesSCR == null) {
			if (other.totFpRescatesSCR != null)
				return false;
		} else if (!totFpRescatesSCR.equals(other.totFpRescatesSCR))
			return false;
		if (totFpSCR == null) {
			if (other.totFpSCR != null)
				return false;
		} else if (!totFpSCR.equals(other.totFpSCR))
			return false;
		if (totFpVida == null) {
			if (other.totFpVida != null)
				return false;
		} else if (!totFpVida.equals(other.totFpVida))
			return false;
		if (totFpVidaSCR == null) {
			if (other.totFpVidaSCR != null)
				return false;
		} else if (!totFpVidaSCR.equals(other.totFpVidaSCR))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "FlujInfSCR [bt=" + bt + ", fcierre=" + fcierre + ", ccanal=" + ccanal + ", negocio=" + negocio
				+ ", uoa=" + uoa + ", kcarteraContrato=" + kcarteraContrato + ", kcarteraCohort=" + kcarteraCohort
				+ ", kcarteraOner=" + kcarteraOner + ", fproyflujest=" + fproyflujest + ", kcarterainv=" + kcarterainv
				+ ", kmodalidad=" + kmodalidad + ", totFpVida=" + totFpVida + ", totFpFall=" + totFpFall
				+ ", totFpGastos=" + totFpGastos + ", totFpComisiones=" + totFpComisiones + ", totFpRescates="
				+ totFpRescates + ", totFpCompl=" + totFpCompl + ", totFpPrimas=" + totFpPrimas + ", totFpGtoAd="
				+ totFpGtoAd + ", totFp=" + totFp + ", totFpVidaSCR=" + totFpVidaSCR + ", totFpFallSCR=" + totFpFallSCR
				+ ", totFpGastosSCR=" + totFpGastosSCR + ", totFpComisionesSCR=" + totFpComisionesSCR
				+ ", totFpRescatesSCR=" + totFpRescatesSCR + ", totFpComplSCR=" + totFpComplSCR + ", totFpPrimasSCR="
				+ totFpPrimasSCR + ", totFpGtoAdSCR=" + totFpGtoAdSCR + ", totFpSCR=" + totFpSCR + "]";
	}

	@Override
	public FlujInfSCRKey getKey() {
		return new FlujInfSCRKey(bt, fcierre, ccanal, negocio, uoa, kcarteraContrato, kcarteraCohort, kcarteraOner,
				fproyflujest,kcarterainv, kmodalidad);
	}
}
