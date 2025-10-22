package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PrvFpbKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PrvFpb implements EntidadBase<PrvFpbKey>, EntidadConBaseTec {
	
	public static final int IND_BT = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_CCANAL = 3;
	public static final int IND_KCARTERA = 4;
	public static final int IND_KGAP = 5;
	public static final int IND_KMODALIDAD = 6;
	public static final int IND_INTBTI = 7;
	public static final int IND_PRV = 8;
	public static final int IND_PRVPB = 9;

	
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_KCARTERA) private String kcartera;
	@PortableProperty(IND_KGAP) private String gap;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(value = IND_INTBTI, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal intbti;
	@PortableProperty(value = IND_PRV, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal prv;
	@PortableProperty(value = IND_PRVPB, codec = BigDecimalSolvenciaCodec.class) private java.math.BigDecimal prvpb;
	
	



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



	public String getKcartera() {
		return kcartera;
	}



	public void setKcartera(String kcartera) {
		this.kcartera = kcartera;
	}



	public String getGap() {
		return gap;
	}



	public void setGap(String gap) {
		this.gap = gap;
	}



	public Integer getKmodalidad() {
		return kmodalidad;
	}



	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}



	public java.math.BigDecimal getIntbti() {
		return intbti;
	}



	public void setIntbti(java.math.BigDecimal intbti) {
		this.intbti = intbti;
	}



	public java.math.BigDecimal getPrv() {
		return prv;
	}



	public void setPrv(java.math.BigDecimal prv) {
		this.prv = prv;
	}



	public java.math.BigDecimal getPrvpb() {
		return prvpb;
	}



	public void setPrvpb(java.math.BigDecimal prvpb) {
		this.prvpb = prvpb;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result + ((gap == null) ? 0 : gap.hashCode());
		result = prime * result + ((intbti == null) ? 0 : intbti.hashCode());
		result = prime * result + ((kcartera == null) ? 0 : kcartera.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((prv == null) ? 0 : prv.hashCode());
		result = prime * result + ((prvpb == null) ? 0 : prvpb.hashCode());
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
		PrvFpb other = (PrvFpb) obj;
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
		if (gap == null) {
			if (other.gap != null)
				return false;
		} else if (!gap.equals(other.gap))
			return false;
		if (intbti == null) {
			if (other.intbti != null)
				return false;
		} else if (!intbti.equals(other.intbti))
			return false;
		if (kcartera == null) {
			if (other.kcartera != null)
				return false;
		} else if (!kcartera.equals(other.kcartera))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (prv == null) {
			if (other.prv != null)
				return false;
		} else if (!prv.equals(other.prv))
			return false;
		if (prvpb == null) {
			if (other.prvpb != null)
				return false;
		} else if (!prvpb.equals(other.prvpb))
			return false;
		return true;
	}





	@Override
	public String toString() {
		return "PrvFpb [bt=" + bt + ", feccierre=" + feccierre + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcartera=" + kcartera + ", gap=" + gap + ", kmodalidad=" + kmodalidad + ", intbti=" + intbti
				+ ", prv=" + prv + ", prvpb=" + prvpb + "]";
	}



	@Override
	public PrvFpbKey getKey() {
		return new PrvFpbKey(bt,feccierre,cnegocio,ccanal,kcartera
				,gap,kmodalidad,intbti);
	}
	
}
