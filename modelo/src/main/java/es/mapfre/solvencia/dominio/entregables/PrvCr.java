package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvCrKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvCr implements EntidadBase<PrvCrKey>, EntidadConBaseTec {
	
	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_SPCOM = 6;
	public static final int IND_IMPNOMFALL = 7;
	public static final int IND_PRV = 8;
	public static final int IND_CAPRIESGO = 9;

	
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_SPCOM) private String spcom;
	@PortableProperty(value = IND_IMPNOMFALL, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impnomfall;
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal prv;
	@PortableProperty(value = IND_CAPRIESGO, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal capriesgo;
	
	
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
	public String getSpcom() {
		return spcom;
	}
	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}
	public java.math.BigDecimal getImpnomfall() {
		return impnomfall;
	}
	public void setImpnomfall(java.math.BigDecimal impnomfall) {
		this.impnomfall = impnomfall;
	}
	public java.math.BigDecimal getPrv() {
		return prv;
	}
	public void setPrv(java.math.BigDecimal prv) {
		this.prv = prv;
	}
	public java.math.BigDecimal getCapriesgo() {
		return capriesgo;
	}
	public void setCapriesgo(java.math.BigDecimal capriesgo) {
		this.capriesgo = capriesgo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((capriesgo == null) ? 0 : capriesgo.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((impnomfall == null) ? 0 : impnomfall.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((prv == null) ? 0 : prv.hashCode());
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
		PrvCr other = (PrvCr) obj;
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
		if (feccierre == null) {
			if (other.feccierre != null)
				return false;
		} else if (!feccierre.equals(other.feccierre))
			return false;
		if (impnomfall == null) {
			if (other.impnomfall != null)
				return false;
		} else if (!impnomfall.equals(other.impnomfall))
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
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (prv == null) {
			if (other.prv != null)
				return false;
		} else if (!prv.equals(other.prv))
			return false;
		return true;
	};
	
	

	@Override
	public String toString() {
		return "PrvCr [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", spcom=" + spcom + ", impnomfall=" + impnomfall
				+ ", prv=" + prv + ", capriesgo=" + capriesgo + "]";
	}
	@Override
	public PrvCrKey getKey() {
		return new PrvCrKey(bt,cnegocio,ccanal,kramo
				,kmodalidad,spcom,feccierre);
	}
	
}
