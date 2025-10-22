/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvBtKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvBt implements EntidadBase<PrvBtKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KGARANTIA = 6;
	public static final int IND_KPOLIZA = 7;
	public static final int IND_KSUBPOLIZA = 8;
	public static final int IND_NSUSCRI = 9;
	public static final int IND_FINISUSC = 10;
	public static final int IND_KPRESTCAL = 11;
	public static final int IND_KCARTERAINV = 12;
	//public static final int IND_FNFALL = 13;
	public static final int IND_GAPACT = 13;
	public static final int IND_GESTIONIT = 14;
	public static final int IND_SEGMENTO1 = 15;
	public static final int IND_TIPOSUBRIESGO = 16;
	public static final int IND_PCOASE = 17;
	public static final int IND_DISTINT = 18;
	public static final int IND_PINTERTECN1 = 19;
	public static final int IND_DURTRCASADO = 20;
	public static final int IND_PINTERTECN2 = 21;
	public static final int IND_TABLA1 = 22;
	public static final int IND_PGASTGESIN1 = 23;
	public static final int IND_PGASTGESIN2 = 24;
	public static final int IND_PGASTEXT = 25;
	public static final int IND_FACTOR1 = 26;
	public static final int IND_INDFACTOR2 = 27;
	public static final int IND_TABLATANUL = 28;
	public static final int IND_GASTREALUNITARIO = 29;
	public static final int IND_GASTREAL = 30;
	public static final int IND_IPC = 31;
	public static final int IND_CURVATI = 32;
	public static final int IND_PRV = 33;
	public static final int IND_UMICKEY = 34;
	public static final int IND_SPCOM =35; 
	public static final int IND_KMODEXT = 36;
	public static final int IND_KBENCON = 37; 
	
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI) private Integer nsuscri;
	@PortableProperty(IND_FINISUSC) private Timestamp finisusc;
	@PortableProperty(IND_KPRESTCAL) private String kprestcal;
	@PortableProperty(IND_KCARTERAINV) private String kcarterainv;
	//@PortableProperty(value = IND_FNFALL, codec = BigDecimalSolvenciaCodec.class) private BigDecimal fnfall;
	@PortableProperty(IND_GAPACT) private String gapact;
	@PortableProperty(IND_GESTIONIT) private String gestionit;
	@PortableProperty(IND_SEGMENTO1) private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO) private String tiposubriesgo;
	@PortableProperty(value = IND_PCOASE, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pcoase;
	@PortableProperty(value = IND_DISTINT, codec = BigDecimalSolvenciaCodec.class) private BigDecimal distint;
	@PortableProperty(value = IND_PINTERTECN1, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn1;
	@PortableProperty(value = IND_DURTRCASADO, codec = BigDecimalSolvenciaCodec.class) private BigDecimal durtrcasado;
	@PortableProperty(value = IND_PINTERTECN2, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pintertecn2;
	@PortableProperty(IND_TABLA1) private String tabla1;
	@PortableProperty(value = IND_PGASTGESIN1, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pgastgesin1;
	@PortableProperty(value = IND_PGASTGESIN2, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pgastgesin2;
	@PortableProperty(value = IND_PGASTEXT, codec = BigDecimalSolvenciaCodec.class) private BigDecimal pgastext;
	@PortableProperty(value = IND_FACTOR1, codec = BigDecimalSolvenciaCodec.class) private BigDecimal factor1;
	@PortableProperty(IND_INDFACTOR2) private String indfactor2;
	@PortableProperty(IND_TABLATANUL) private String tablatanul;
	@PortableProperty(value = IND_GASTREALUNITARIO, codec = BigDecimalSolvenciaCodec.class) private BigDecimal gastrealunitario;
	@PortableProperty(value = IND_GASTREAL, codec = BigDecimalSolvenciaCodec.class) private BigDecimal gastreal;
	@PortableProperty(value = IND_IPC, codec = BigDecimalSolvenciaCodec.class) private BigDecimal ipc;
	@PortableProperty(IND_CURVATI) private String curvati;
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class) private BigDecimal prv;
	@PortableProperty(IND_UMICKEY) private UmicKey umickey;
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

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

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

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Timestamp getFinisusc() {
		return finisusc;
	}

	public void setFinisusc(Timestamp finisusc) {
		this.finisusc = finisusc;
	}

	public String getKprestcal() {
		return kprestcal;
	}


    public void setKprestcal(String kprestcal) {
		this.kprestcal = kprestcal;
	}

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

//	public BigDecimal getFnfall() {
//		return fnfall;
//	}
//
//	public void setFnfall(BigDecimal fnfall) {
//		this.fnfall = fnfall;
//	}
	
	public String getGestionit() {
		return gestionit;
	}

	public String getGapact() {
		return gapact;
	}

	public void setGapact(String gapact) {
		this.gapact = gapact;
	}

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}

	public String getSegmento1() {
		return segmento1;
	}

	public void setSegmento1(String segmento1) {
		this.segmento1 = segmento1;
	}

	public String getTiposubriesgo() {
		return tiposubriesgo;
	}

	public void setTiposubriesgo(String tiposubriesgo) {
		this.tiposubriesgo = tiposubriesgo;
	}

	public BigDecimal getPcoase() {
		return pcoase;
	}

	public void setPcoase(BigDecimal pcoase) {
		this.pcoase = pcoase;
	}

	public BigDecimal getDistint() {
		return distint;
	}

	public void setDistint(BigDecimal distint) {
		this.distint = distint;
	}

	public BigDecimal getPintertecn1() {
		return pintertecn1;
	}

	public void setPintertecn1(BigDecimal pintertecn1) {
		this.pintertecn1 = pintertecn1;
	}

	public BigDecimal getDurtrcasado() {
		return durtrcasado;
	}

	public void setDurtrcasado(BigDecimal durtrcasado) {
		this.durtrcasado = durtrcasado;
	}

	public BigDecimal getPintertecn2() {
		return pintertecn2;
	}

	public void setPintertecn2(BigDecimal pintertecn2) {
		this.pintertecn2 = pintertecn2;
	}

	public String getTabla1() {
		return tabla1;
	}

	public void setTabla1(String tabla1) {
		this.tabla1 = tabla1;
	}

	public BigDecimal getPgastgesin1() {
		return pgastgesin1;
	}

	public void setPgastgesin1(BigDecimal pgastgesin1) {
		this.pgastgesin1 = pgastgesin1;
	}

	public BigDecimal getPgastgesin2() {
		return pgastgesin2;
	}

	public void setPgastgesin2(BigDecimal pgastgesin2) {
		this.pgastgesin2 = pgastgesin2;
	}

	public BigDecimal getPgastext() {
		return pgastext;
	}

	public void setPgastext(BigDecimal pgastext) {
		this.pgastext = pgastext;
	}

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public String getIndfactor2() {
		return indfactor2;
	}

	public void setIndfactor2(String indfactor2) {
		this.indfactor2 = indfactor2;
	}

	public String getTablatanul() {
		return tablatanul;
	}

	public void setTablatanul(String tablatanul) {
		this.tablatanul = tablatanul;
	}

	public BigDecimal getGastrealunitario() {
		return gastrealunitario;
	}

	public void setGastrealunitario(BigDecimal gastrealunitario) {
		this.gastrealunitario = gastrealunitario;
	}

	public BigDecimal getGastreal() {
		return gastreal;
	}

	public void setGastreal(BigDecimal gastreal) {
		this.gastreal = gastreal;
	}

	public BigDecimal getIpc() {
		return ipc;
	}

	public void setIpc(BigDecimal ipc) {
		this.ipc = ipc;
	}

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
	}

	public BigDecimal getPrv() {
		return prv;
	}

	public void setPrv(BigDecimal prv) {
		this.prv = prv;
	}

	public UmicKey getUmickey() {
		return umickey;
	}

	public void setUmickey(UmicKey umickey) {
		this.umickey = umickey;
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
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((distint == null) ? 0 : distint.hashCode());
		result = prime * result	+ ((durtrcasado == null) ? 0 : durtrcasado.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result	+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result	+ ((finisusc == null) ? 0 : finisusc.hashCode());
//		result = prime * result + ((fnfall == null) ? 0 : fnfall.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result	+ ((gastreal == null) ? 0 : gastreal.hashCode());
		result = prime * result	+ ((gastrealunitario == null) ? 0 : gastrealunitario.hashCode());
		result = prime * result	+ ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result	+ ((indfactor2 == null) ? 0 : indfactor2.hashCode());
		result = prime * result + ((ipc == null) ? 0 : ipc.hashCode());
		result = prime * result	+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result	+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result	+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result	+ ((kprestcal == null) ? 0 : kprestcal.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result	+ ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pcoase == null) ? 0 : pcoase.hashCode());
		result = prime * result	+ ((pgastext == null) ? 0 : pgastext.hashCode());
		result = prime * result	+ ((pgastgesin1 == null) ? 0 : pgastgesin1.hashCode());
		result = prime * result	+ ((pgastgesin2 == null) ? 0 : pgastgesin2.hashCode());
		result = prime * result	+ ((pintertecn1 == null) ? 0 : pintertecn1.hashCode());
		result = prime * result	+ ((pintertecn2 == null) ? 0 : pintertecn2.hashCode());
		result = prime * result + ((prv == null) ? 0 : prv.hashCode());
		result = prime * result	+ ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((tabla1 == null) ? 0 : tabla1.hashCode());
		result = prime * result	+ ((tablatanul == null) ? 0 : tablatanul.hashCode());
		result = prime * result	+ ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((umickey == null) ? 0 : umickey.hashCode());
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
		PrvBt other = (PrvBt) obj;
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
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (distint == null) {
			if (other.distint != null)
				return false;
		} else if (!distint.equals(other.distint))
			return false;
		if (durtrcasado == null) {
			if (other.durtrcasado != null)
				return false;
		} else if (!durtrcasado.equals(other.durtrcasado))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (finisusc == null) {
			if (other.finisusc != null)
				return false;
		} else if (!finisusc.equals(other.finisusc))
			return false;
//		if (fnfall == null) {
//			if (other.fnfall != null)
//				return false;
//		} else if (!fnfall.equals(other.fnfall))
//			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (gastreal == null) {
			if (other.gastreal != null)
				return false;
		} else if (!gastreal.equals(other.gastreal))
			return false;
		if (gastrealunitario == null) {
			if (other.gastrealunitario != null)
				return false;
		} else if (!gastrealunitario.equals(other.gastrealunitario))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		if (indfactor2 == null) {
			if (other.indfactor2 != null)
				return false;
		} else if (!indfactor2.equals(other.indfactor2))
			return false;
		if (ipc == null) {
			if (other.ipc != null)
				return false;
		} else if (!ipc.equals(other.ipc))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kprestcal == null) {
			if (other.kprestcal != null)
				return false;
		} else if (!kprestcal.equals(other.kprestcal))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
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
		if (pgastext == null) {
			if (other.pgastext != null)
				return false;
		} else if (!pgastext.equals(other.pgastext))
			return false;
		if (pgastgesin1 == null) {
			if (other.pgastgesin1 != null)
				return false;
		} else if (!pgastgesin1.equals(other.pgastgesin1))
			return false;
		if (pgastgesin2 == null) {
			if (other.pgastgesin2 != null)
				return false;
		} else if (!pgastgesin2.equals(other.pgastgesin2))
			return false;
		if (pintertecn1 == null) {
			if (other.pintertecn1 != null)
				return false;
		} else if (!pintertecn1.equals(other.pintertecn1))
			return false;
		if (pintertecn2 == null) {
			if (other.pintertecn2 != null)
				return false;
		} else if (!pintertecn2.equals(other.pintertecn2))
			return false;
		if (prv == null) {
			if (other.prv != null)
				return false;
		} else if (!prv.equals(other.prv))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (tabla1 == null) {
			if (other.tabla1 != null)
				return false;
		} else if (!tabla1.equals(other.tabla1))
			return false;
		if (tablatanul == null) {
			if (other.tablatanul != null)
				return false;
		} else if (!tablatanul.equals(other.tablatanul))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		if (umickey == null) {
			if (other.umickey != null)
				return false;
		} else if (!umickey.equals(other.umickey))
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
		return "PrvBt [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio="
				+ cnegocio + ", ccanal=" + ccanal + ", kramo=" + kramo
				+ ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia
				+ ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", nsuscri=" + nsuscri + ", finisusc=" + finisusc
				+ ", kprestcal=" + kprestcal + ", kcarterainv=" + kcarterainv
				+ ", gapact=" + gapact + ", gestionit=" + gestionit
				+ ", segmento1=" + segmento1 + ", tiposubriesgo="
				+ tiposubriesgo + ", pcoase=" + pcoase + ", distint=" + distint
				+ ", pintertecn1=" + pintertecn1 + ", durtrcasado="
				+ durtrcasado + ", pintertecn2=" + pintertecn2 + ", tabla1="
				+ tabla1 + ", pgastgesin1=" + pgastgesin1 + ", pgastgesin2="
				+ pgastgesin2 + ", pgastext=" + pgastext + ", factor1="
				+ factor1 + ", indfactor2=" + indfactor2 + ", tablatanul="
				+ tablatanul + ", gastrealunitario=" + gastrealunitario
				+ ", gastreal=" + gastreal + ", ipc=" + ipc + ", curvati="
				+ curvati + ", prv=" + prv + ", umickey=" + umickey + ", spcom=" + spcom + ", kmodext=" + kmodext + ", kbencon=" + kbencon + "]";
	}

	
	
	@Override
	public PrvBtKey getKey() {
		return new PrvBtKey(bt, feccierre, umickey);
	}

}
