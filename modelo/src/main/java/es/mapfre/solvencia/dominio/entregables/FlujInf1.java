/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 17/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/
package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujInf1Key;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujInf1 implements EntidadBase<FlujInf1Key>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_GAPACT = 5;
	public static final int IND_GESTIONIT = 6;
	public static final int IND_KRAMO = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_FECDESDE = 9;
	public static final int IND_TOTFLUJPROV = 10;
	public static final int IND_TOTPROVI = 11;
	public static final int IND_TOTFLUJOACT = 12;
	public static final int IND_TOTFLUJPROVPRES = 13;
	public static final int IND_TOTFLUJPROVPRIM = 14;
	public static final int IND_TOTFLUJPROVGAS = 15;
	public static final int IND_TOTFLUJACTPRES = 16;
	public static final int IND_TOTFLUJACTPRIM = 17;
	public static final int IND_TOTFLUJACTGAS = 18;
	public static final int IND_SPCOM =19; 
	public static final int IND_KMODEXT = 20;
	public static final int IND_KBENCON = 21; 
	public static final int IND_TOTFLUJPROVGASAD = 22;
	public static final int IND_TOTFLUJACTGASAD = 23;

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
	@PortableProperty(IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(value = IND_TOTFLUJPROV, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprov;
	@PortableProperty(value = IND_TOTPROVI, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totprovi;
	@PortableProperty(value = IND_TOTFLUJOACT, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujact;
	@PortableProperty(value = IND_TOTFLUJPROVPRES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprovpres;
	@PortableProperty(value = IND_TOTFLUJPROVGAS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprovgas;
	@PortableProperty(value = IND_TOTFLUJPROVPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprovprim;
	@PortableProperty(value = IND_TOTFLUJACTPRES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujactpres;
	@PortableProperty(value = IND_TOTFLUJACTGAS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujactgas;
	@PortableProperty(value = IND_TOTFLUJACTPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujactprim;
	@PortableProperty(IND_SPCOM)   private String spcom;
	@PortableProperty(IND_KMODEXT) private Integer kmodext;
	@PortableProperty(IND_KBENCON) private String kbencon;
	@PortableProperty(value = IND_TOTFLUJPROVGASAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujprovgasad;
	@PortableProperty(value = IND_TOTFLUJACTGASAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totflujactgasad;
	
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
	public java.math.BigDecimal getTotflujprovpres() {
		return totflujprovpres;
	}
	public void setTotflujprovpres(java.math.BigDecimal totflujprovpres) {
		this.totflujprovpres = totflujprovpres;
	}
	public java.math.BigDecimal getTotflujprovgas() {
		return totflujprovgas;
	}
	public void setTotflujprovgas(java.math.BigDecimal totflujprovgas) {
		this.totflujprovgas = totflujprovgas;
	}
	public java.math.BigDecimal getTotflujprovprim() {
		return totflujprovprim;
	}
	public void setTotflujprovprim(java.math.BigDecimal totflujprovprim) {
		this.totflujprovprim = totflujprovprim;
	}
	public java.math.BigDecimal getTotflujactpres() {
		return totflujactpres;
	}
	public void setTotflujactpres(java.math.BigDecimal totflujactpres) {
		this.totflujactpres = totflujactpres;
	}
	public java.math.BigDecimal getTotflujactgas() {
		return totflujactgas;
	}
	public void setTotflujactgas(java.math.BigDecimal totflujactgas) {
		this.totflujactgas = totflujactgas;
	}
	public java.math.BigDecimal getTotflujactprim() {
		return totflujactprim;
	}
	public void setTotflujactprim(java.math.BigDecimal totflujactprim) {
		this.totflujactprim = totflujactprim;
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
	
	public java.math.BigDecimal getTotflujprovgasad() {
		return totflujprovgasad;
	}
	public void setTotflujprovgasad(java.math.BigDecimal totflujprovgasad) {
		this.totflujprovgasad = totflujprovgasad;
	}
	public java.math.BigDecimal getTotflujactgasad() {
		return totflujactgasad;
	}
	public void setTotflujactgasad(java.math.BigDecimal totflujactgasad) {
		this.totflujactgasad = totflujactgasad;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((totflujact == null) ? 0 : totflujact.hashCode());
		result = prime * result + ((totflujactgas == null) ? 0 : totflujactgas.hashCode());
		result = prime * result + ((totflujactgasad == null) ? 0 : totflujactgasad.hashCode());
		result = prime * result + ((totflujactpres == null) ? 0 : totflujactpres.hashCode());
		result = prime * result + ((totflujactprim == null) ? 0 : totflujactprim.hashCode());
		result = prime * result + ((totflujprov == null) ? 0 : totflujprov.hashCode());
		result = prime * result + ((totflujprovgas == null) ? 0 : totflujprovgas.hashCode());
		result = prime * result + ((totflujprovgasad == null) ? 0 : totflujprovgasad.hashCode());
		result = prime * result + ((totflujprovpres == null) ? 0 : totflujprovpres.hashCode());
		result = prime * result + ((totflujprovprim == null) ? 0 : totflujprovprim.hashCode());
		result = prime * result + ((totprovi == null) ? 0 : totprovi.hashCode());
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
		FlujInf1 other = (FlujInf1) obj;
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
		if (kbencon == null) {
			if (other.kbencon != null)
				return false;
		} else if (!kbencon.equals(other.kbencon))
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
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (totflujact == null) {
			if (other.totflujact != null)
				return false;
		} else if (!totflujact.equals(other.totflujact))
			return false;
		if (totflujactgas == null) {
			if (other.totflujactgas != null)
				return false;
		} else if (!totflujactgas.equals(other.totflujactgas))
			return false;
		if (totflujactgasad == null) {
			if (other.totflujactgasad != null)
				return false;
		} else if (!totflujactgasad.equals(other.totflujactgasad))
			return false;
		if (totflujactpres == null) {
			if (other.totflujactpres != null)
				return false;
		} else if (!totflujactpres.equals(other.totflujactpres))
			return false;
		if (totflujactprim == null) {
			if (other.totflujactprim != null)
				return false;
		} else if (!totflujactprim.equals(other.totflujactprim))
			return false;
		if (totflujprov == null) {
			if (other.totflujprov != null)
				return false;
		} else if (!totflujprov.equals(other.totflujprov))
			return false;
		if (totflujprovgas == null) {
			if (other.totflujprovgas != null)
				return false;
		} else if (!totflujprovgas.equals(other.totflujprovgas))
			return false;
		if (totflujprovgasad == null) {
			if (other.totflujprovgasad != null)
				return false;
		} else if (!totflujprovgasad.equals(other.totflujprovgasad))
			return false;
		if (totflujprovpres == null) {
			if (other.totflujprovpres != null)
				return false;
		} else if (!totflujprovpres.equals(other.totflujprovpres))
			return false;
		if (totflujprovprim == null) {
			if (other.totflujprovprim != null)
				return false;
		} else if (!totflujprovprim.equals(other.totflujprovprim))
			return false;
		if (totprovi == null) {
			if (other.totprovi != null)
				return false;
		} else if (!totprovi.equals(other.totprovi))
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		return "FlujInf1 [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", gapact=" + gapact + ", gestionit=" + gestionit + ", kramo="
				+ kramo + ", kmodalidad=" + kmodalidad + ", fecdesde=" + fecdesde + ", totflujprov=" + totflujprov
				+ ", totprovi=" + totprovi + ", totflujact=" + totflujact + ", totflujprovpres=" + totflujprovpres
				+ ", totflujprovgas=" + totflujprovgas + ", totflujprovprim=" + totflujprovprim + ", totflujactpres="
				+ totflujactpres + ", totflujactgas=" + totflujactgas + ", totflujactprim=" + totflujactprim
				+ ", spcom=" + spcom + ", kmodext=" + kmodext + ", kbencon=" + kbencon + ", totflujprovgasad="
				+ totflujprovgasad + ", totflujactgasad=" + totflujactgasad + "]";
	}
	@Override
	public FlujInf1Key getKey() {
		return new FlujInf1Key(bt, feccierre, cnegocio, ccanal, kcarterainv, gapact, gestionit, kramo, kmodalidad, fecdesde, spcom, kmodext, kbencon );
	}

}
