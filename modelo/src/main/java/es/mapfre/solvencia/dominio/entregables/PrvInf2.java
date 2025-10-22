package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvInf2Key;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvInf2 implements EntidadBase<PrvInf2Key>, EntidadConBaseTec {
	
	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_GAPACT = 5;
	public static final int IND_KRAMO = 6;
	public static final int IND_KMODALIDAD = 7;	
	public static final int IND_KGARANTIA = 8;
	public static final int IND_CTIPRAMO = 9;	
	public static final int IND_SEGMENTO1 = 10;
	public static final int IND_TIPOSUBRIESGO = 11;
	public static final int IND_NUEVAPRODUC = 12;
	public static final int IND_INDICRESCATE = 13;
	public static final int IND_CURVATI = 14;
	public static final int IND_PRV = 15;
	public static final int IND_FNRTE = 16;
	
	
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_CTIPRAMO) private String ctipramo;
	@PortableProperty(IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(IND_GAPACT) private String gapAct;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_SEGMENTO1) private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO) private String tiposubriesgo;
	@PortableProperty(IND_NUEVAPRODUC) private String nuevaproduc;
	@PortableProperty(IND_INDICRESCATE) private String indicrescate;
	@PortableProperty(IND_CURVATI) private String curvati;
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class) private BigDecimal prv;
	@PortableProperty(value = IND_FNRTE, codec = BigDecimalSolvenciaCodec.class) private BigDecimal fnrte;
	
	
	
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

	public String getCtipramo() {
		return ctipramo;
	}

	public void setCtipramo(String ctipramo) {
		this.ctipramo = ctipramo;
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

	public String getNuevaproduc() {
		return nuevaproduc;
	}

	public void setNuevaproduc(String nuevaproduc) {
		this.nuevaproduc = nuevaproduc;
	}

	public String getIndicrescate() {
		return indicrescate;
	}

	public void setIndicrescate(String indicrescate) {
		this.indicrescate = indicrescate;
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

	public BigDecimal getFnrte() {
		return fnrte;
	}

	public void setFnrte(BigDecimal fnrte) {
		this.fnrte = fnrte;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result	+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result	+ ((ctipramo == null) ? 0 : ctipramo.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result	+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fnrte == null) ? 0 : fnrte.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result	+ ((indicrescate == null) ? 0 : indicrescate.hashCode());
		result = prime * result	+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result	+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result	+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result	+ ((nuevaproduc == null) ? 0 : nuevaproduc.hashCode());
		result = prime * result + ((prv == null) ? 0 : prv.hashCode());
		result = prime * result	+ ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result	+ ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
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
		PrvInf2 other = (PrvInf2) obj;
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
		if (ctipramo == null) {
			if (other.ctipramo != null)
				return false;
		} else if (!ctipramo.equals(other.ctipramo))
			return false;
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
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
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (nuevaproduc == null) {
			if (other.nuevaproduc != null)
				return false;
		} else if (!nuevaproduc.equals(other.nuevaproduc))
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
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		return true;
	}

	
	@Override
	public String toString() {
		return "PrvInf2 [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio="
				+ cnegocio + ", ccanal=" + ccanal + ", ctipramo=" + ctipramo
				+ ", kcarterainv=" + kcarterainv + ", gapAct=" + gapAct
				+ ", kramo=" + kramo + ", kmodalidad=" + kmodalidad
				+ ", kgarantia=" + kgarantia + ", segmento1=" + segmento1
				+ ", tiposubriesgo=" + tiposubriesgo + ", nuevaproduc="
				+ nuevaproduc + ", indicrescate=" + indicrescate + ", curvati="
				+ curvati + ", prv=" + prv + ", fnrte=" + fnrte + "]";
	}

	@Override
	public PrvInf2Key getKey() {
		return new PrvInf2Key(bt,cnegocio,ccanal,ctipramo,kcarterainv,gapAct,kramo
				,kmodalidad,kgarantia,segmento1,tiposubriesgo,nuevaproduc,indicrescate,curvati);
	}
	
}
