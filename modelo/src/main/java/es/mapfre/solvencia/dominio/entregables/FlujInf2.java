/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/
package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInf2Key;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujInf2 implements EntidadBase<FlujInf2Key>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_GAPACT = 5;
	public static final int IND_GESTIONIT = 6;
	public static final int IND_KRAMO = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_KGARANTIA = 9;
	public static final int IND_KPRESTCAL = 10;
	public static final int IND_NUEVAPRODUC = 11;
	public static final int IND_SEGMENTO1 = 12;
	public static final int IND_TIPOSUBRIESGO = 13;
	public static final int IND_CURVATI = 14;
	public static final int IND_FECDESDE = 15;
	public static final int IND_TOTFLUJPROV = 16;
	public static final int IND_TOTPROVI = 17;
	public static final int IND_TOTFLUJOACT = 18;
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
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KPRESTCAL)
	private String kprestcal;
	@PortableProperty(IND_NUEVAPRODUC)
	private String nuevaproduc;
	@PortableProperty(IND_SEGMENTO1)
	private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(IND_CURVATI)
	private String curvati;
	@PortableProperty(IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(value = IND_TOTFLUJPROV, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprov;
	@PortableProperty(value = IND_TOTPROVI, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totprovi;
	@PortableProperty(value = IND_TOTFLUJOACT, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujact;
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

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public String getGapact() {
		return gapact;
	}

	public void setGapact(String gapact) {
		this.gapact = gapact;
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

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestcal() {
		return kprestcal;
	}

	public void setKprestcal(String kprestcal) {
		this.kprestcal = kprestcal;
	}

	public String getNuevaproduc() {
		return nuevaproduc;
	}

	public void setNuevaproduc(String nuevaproduc) {
		this.nuevaproduc = nuevaproduc;
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

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
	}

	public Timestamp getFecdesde() {
		return fecdesde;
	}

	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}

	public java.math.BigDecimal getTotflujprov() {
		return totflujprov;
	}

	public void setTotflujprov(java.math.BigDecimal totflujprov) {
		this.totflujprov = totflujprov;
	}

	public java.math.BigDecimal getTotprovi() {
		return totprovi;
	}

	public void setTotprovi(java.math.BigDecimal totprovi) {
		this.totprovi = totprovi;
	}

	public java.math.BigDecimal getTotflujact() {
		return totflujact;
	}

	public void setTotflujact(java.math.BigDecimal totflujact) {
		this.totflujact = totflujact;
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
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kprestcal == null) ? 0 : kprestcal.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((nuevaproduc == null) ? 0 : nuevaproduc.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((totflujact == null) ? 0 : totflujact.hashCode());
		result = prime * result + ((totflujprov == null) ? 0 : totflujprov.hashCode());
		result = prime * result + ((totprovi == null) ? 0 : totprovi.hashCode());
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
		FlujInf2 other = (FlujInf2) obj;
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
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (fecdesde == null) {
			if (other.fecdesde != null)
				return false;
		} else if (!fecdesde.equals(other.fecdesde))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
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
		if (nuevaproduc == null) {
			if (other.nuevaproduc != null)
				return false;
		} else if (!nuevaproduc.equals(other.nuevaproduc))
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
		if (totflujact == null) {
			if (other.totflujact != null)
				return false;
		} else if (!totflujact.equals(other.totflujact))
			return false;
		if (totflujprov == null) {
			if (other.totflujprov != null)
				return false;
		} else if (!totflujprov.equals(other.totflujprov))
			return false;
		if (totprovi == null) {
			if (other.totprovi != null)
				return false;
		} else if (!totprovi.equals(other.totprovi))
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
		return "FlujInf2 [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapact=" + gapact + ", gestionit=" + gestionit + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", kprestcal=" + kprestcal
				+ ", nuevaproduc=" + nuevaproduc + ", segmento1=" + segmento1 + ", tiposubriesgo=" + tiposubriesgo
				+ ", curvati=" + curvati + ", fecdesde=" + fecdesde + ", totflujprov="
				+ totflujprov + ", totprovi=" + totprovi + ", totflujact=" + totflujact + ", spcom=" + spcom + ", kmodext=" + kmodext + ", kbencon=" + kbencon + "]";
	}

	@Override
	public FlujInf2Key getKey() {
		return new FlujInf2Key(bt, feccierre, cnegocio, ccanal, kcarterainv, gapact, gestionit, kramo, kmodalidad, kgarantia, kprestcal, nuevaproduc, segmento1, tiposubriesgo, curvati, fecdesde, spcom, kmodext, kbencon);
	}

}
