/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/
package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvUmicKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvUmic implements EntidadBase<PrvUmicKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_UMICKEY = 4;
	public static final int IND_FECINISUS = 5;
	public static final int IND_KRAMO = 6;
	public static final int IND_KCARTERAINV = 7;
	public static final int IND_GAPACT = 8;
	public static final int IND_GESTIONIT = 9;
	public static final int IND_CTIPOPROVI = 10;
	public static final int IND_INDICRESCATE = 11;
	public static final int IND_FNRTE = 12;
	public static final int IND_FNFALL = 13;
	public static final int IND_CAPRIESGO = 14;
	public static final int IND_PRV = 15;
	public static final int IND_INTFECCALC = 16;
	public static final int IND_PRVPB = 17;
	public static final int IND_INTBTI = 18;
	public static final int IND_SPCOM =19; 
	public static final int IND_KMODEXT = 20;
	public static final int IND_KBENCON = 21; 

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FECCIERRE)
	private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_UMICKEY)
	private UmicKey umicKey;
	@PortableProperty(IND_FECINISUS)
	private Timestamp fecinisus;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapAct;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(IND_CTIPOPROVI)
	private String ctipoprovi;
	@PortableProperty(IND_INDICRESCATE)
	private String indicrescate;
	@PortableProperty(value = IND_FNRTE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal fnrte;
	@PortableProperty(value = IND_FNFALL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal fnfall;
	@PortableProperty(value = IND_CAPRIESGO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal capriesgo;
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal prv;
	@PortableProperty(value = IND_INTFECCALC, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal intfeccalc;
	@PortableProperty(value = IND_PRVPB, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal prvpb;
	@PortableProperty(value = IND_INTBTI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal intbti;
	@PortableProperty(IND_SPCOM)   private String spcom;
	@PortableProperty(IND_KMODEXT) private Integer kmodext;
	@PortableProperty(IND_KBENCON) private String kbencon;

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

	public UmicKey getUmicKey() {
		return umicKey;
	}

	public void setUmicKey(UmicKey umicKey) {
		this.umicKey = umicKey;
	}

	public Timestamp getFecinisus() {
		return fecinisus;
	}

	public void setFecinisus(Timestamp fecinisus) {
		this.fecinisus = fecinisus;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
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

	public String getCtipoprovi() {
		return ctipoprovi;
	}

	public void setCtipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}

	public String getIndicrescate() {
		return indicrescate;
	}

	public void setIndicrescate(String indicrescate) {
		this.indicrescate = indicrescate;
	}

	public BigDecimal getFnrte() {
		return fnrte;
	}

	public void setFnrte(BigDecimal fnrte) {
		this.fnrte = fnrte;
	}

	public BigDecimal getFnfall() {
		return fnfall;
	}

	public void setFnfall(BigDecimal fnfall) {
		this.fnfall = fnfall;
	}

	public BigDecimal getCapriesgo() {
		return capriesgo;
	}

	public void setCapriesgo(BigDecimal capriesgo) {
		this.capriesgo = capriesgo;
	}

	public BigDecimal getPrv() {
		return prv;
	}

	public void setPrv(BigDecimal prv) {
		this.prv = prv;
	}

	public BigDecimal getIntfeccalc() {
		return intfeccalc;
	}

	public void setIntfeccalc(BigDecimal intfeccalc) {
		this.intfeccalc = intfeccalc;
	}

	public BigDecimal getIntbti() {
		return intbti;
	}

	public void setIntbti(BigDecimal intbti) {
		this.intbti = intbti;
	}

	public BigDecimal getPrvpb() {
		return prvpb;
	}

	public void setPrvpb(BigDecimal prvpb) {
		this.prvpb = prvpb;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}
	
	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}
	
	public String getKbencon() {
		return kbencon;
	}
	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((capriesgo == null) ? 0 : capriesgo.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecinisus == null) ? 0 : fecinisus.hashCode());
		result = prime * result + ((fnfall == null) ? 0 : fnfall.hashCode());
		result = prime * result + ((fnrte == null) ? 0 : fnrte.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((indicrescate == null) ? 0 : indicrescate.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((prv == null) ? 0 : prv.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
		result = prime * result + ((intfeccalc == null) ? 0 : intfeccalc.hashCode());
		result = prime * result + ((intbti == null) ? 0 : intbti.hashCode());
		result = prime * result + ((prvpb == null) ? 0 : prvpb.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result	+ ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result	+ ((kbencon == null) ? 0 : kbencon.hashCode());
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
		PrvUmic other = (PrvUmic) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (capriesgo == null) {
			if (other.capriesgo != null)
				return false;
		} else if (!capriesgo.equals(other.capriesgo))
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
		if (fecinisus == null) {
			if (other.fecinisus != null)
				return false;
		} else if (!fecinisus.equals(other.fecinisus))
			return false;
		if (fnfall == null) {
			if (other.fnfall != null)
				return false;
		} else if (!fnfall.equals(other.fnfall))
			return false;
		if (fnrte == null) {
			if (other.fnrte != null)
				return false;
		} else if (!fnrte.equals(other.fnrte))
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
		if (indicrescate == null) {
			if (other.indicrescate != null)
				return false;
		} else if (!indicrescate.equals(other.indicrescate))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (prv == null) {
			if (other.prv != null)
				return false;
		} else if (!prv.equals(other.prv))
			return false;
		if (umicKey == null) {
			if (other.umicKey != null)
				return false;
		} else if (!umicKey.equals(other.umicKey))
			return false;
		if (intfeccalc == null) {
			if (other.intfeccalc != null)
				return false;
		} else if (!intfeccalc.equals(other.intfeccalc))
			return false;
		if (intbti == null) {
			if (other.intbti != null)
				return false;
		} else if (!intbti.equals(other.intbti))
			return false;
		if (prvpb == null) {
			if (other.prvpb != null)
				return false;
		} else if (!prvpb.equals(other.prvpb))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
			return false;
		if (kbencon == null) {
			if (other.kbencon != null)
				return false;
		} else if (!kbencon.equals(other.kbencon))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PrvUmic [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", umicKey=" + umicKey + ", fecinisus=" + fecinisus + ", kramo=" + kramo + ", kcarterainv="
				+ kcarterainv + ", gapAct=" + gapAct + ", gestionit=" + gestionit + ", ctipoprovi=" + ctipoprovi
				+ ", indicrescate=" + indicrescate + ", fnrte=" + fnrte + ", fnfall=" + fnfall + ", capriesgo="
				+ capriesgo + ", prv=" + prv + ", intfeccalc=" + intfeccalc + ", intbti=" + intbti + ", prvpb=" + prvpb
				+ ", spcom=" + spcom + ", kmodext=" + kmodext + ", kbencon=" + kbencon + "]";
	}

	@Override
	public PrvUmicKey getKey() {
		return new PrvUmicKey(bt, feccierre, umicKey);
	}

}
