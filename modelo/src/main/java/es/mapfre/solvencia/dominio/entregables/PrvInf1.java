package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf1Key;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvInf1 implements EntidadBase<PrvInf1Key>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_GAPACT = 5;
	public static final int IND_GESTIONIT = 6;
	public static final int IND_KRAMO = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_CTIPOPROVI = 9;
	public static final int IND_SPCOM = 10;
	public static final int IND_KOFICONT = 11;
	public static final int IND_INTFECCALC = 12;
	public static final int IND_PFPINV = 13;
	public static final int IND_TOTPROVI = 14;
	public static final int IND_KMODEXT = 15;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapAct;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(value = IND_INTFECCALC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal intfeccal;
	@PortableProperty(IND_CTIPOPROVI)
	private String ctipoprovi;
	@PortableProperty(IND_SPCOM)
	private String spcom;
	@PortableProperty(IND_KOFICONT)
	private String koficont;
	@PortableProperty(IND_PFPINV)
	private BigDecimal pfpInv;
	@PortableProperty(IND_TOTPROVI)
	private BigDecimal totProvi;

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public Timestamp getFeccierre() {
		return feccierre;
	}

	public void setFeccierre(Timestamp feccierre) {
		this.feccierre = feccierre;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public String getGapAct() {
		return gapAct;
	}

	public void setGapAct(String gapAct) {
		this.gapAct = gapAct;
	}

	public String getGestionit() {
		return gestionit;
	}

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}

	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public java.math.BigDecimal getIntfeccal() {
		return intfeccal;
	}

	public void setIntfeccal(java.math.BigDecimal intfeccal) {
		this.intfeccal = intfeccal;
	}

	public String getCtipoprovi() {
		return ctipoprovi;
	}

	public void setCtipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}

	public String getKoficont() {
		return koficont;
	}

	public void setKoficont(String koficont) {
		this.koficont = koficont;
	}

	public BigDecimal getPfpInv() {
		return pfpInv;
	}

	public void setPfpInv(BigDecimal pfpInv) {
		this.pfpInv = pfpInv;
	}

	public BigDecimal getTotProvi() {
		return totProvi;
	}

	public void setTotProvi(BigDecimal totProvi) {
		this.totProvi = totProvi;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((koficont == null) ? 0 : koficont.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((pfpInv == null) ? 0 : pfpInv.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((totProvi == null) ? 0 : totProvi.hashCode());
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
		PrvInf1 other = (PrvInf1) obj;
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
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null)
				return false;
		} else if (!ctipoprovi.equals(other.ctipoprovi))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		if (intfeccal == null) {
			if (other.intfeccal != null)
				return false;
		} else if (!intfeccal.equals(other.intfeccal))
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
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;
		if (koficont == null) {
			if (other.koficont != null)
				return false;
		} else if (!koficont.equals(other.koficont))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (pfpInv == null) {
			if (other.pfpInv != null)
				return false;
		} else if (!pfpInv.equals(other.pfpInv))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (totProvi == null) {
			if (other.totProvi != null)
				return false;
		} else if (!totProvi.equals(other.totProvi))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrvInf1 [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapAct=" + gapAct + ", gestionit=" + gestionit + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", kmodext=" + kmodext + ", intfeccal=" + intfeccal
				+ ", ctipoprovi=" + ctipoprovi + ", spcom=" + spcom + ", koficont=" + koficont + ", pfpInv=" + pfpInv
				+ ", totProvi=" + totProvi + "]";
	}

	@Override
	public PrvInf1Key getKey() {
		return new PrvInf1Key(bt, cnegocio, ccanal, kcarterainv, gapAct, kramo, kmodalidad, intfeccal, ctipoprovi,
				spcom, koficont, kmodext, gestionit);
	}

}
