package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.PatronCsmKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class PatronCsm implements EntidadBase<PatronCsmKey>, EntidadConBaseTec {
	
	public static final int IND_BT = 0;
	public static final int IND_FCIERRE = 1;
	public static final int IND_CCANAL = 2;
	public static final int IND_UOA = 3;
	public static final int IND_FECDESDE = 4;
	public static final int IND_PROVBTIPROY = 5;
	public static final int IND_PROVBELPROY = 6;
	public static final int IND_RAUMIC = 7;
	public static final int IND_ROSSP_CSM = 8;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_FECDESDE)
	private Timestamp fecdesde;
	@PortableProperty(value=IND_PROVBTIPROY, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provbtiproy;
	@PortableProperty(value=IND_PROVBELPROY, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provbelproy;
	@PortableProperty(value = IND_RAUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal raumic;
	@PortableProperty(value = IND_ROSSP_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal rosspCSM;			
	
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

	public String getUoa() {
		return uoa;
	}

	public void setUoa(String uoa) {
		this.uoa = uoa;
	}

	public Timestamp getFecdesde() {
		return fecdesde;
	}

	public void setFecdesde(Timestamp fecdesde) {
		this.fecdesde = fecdesde;
	}

	public BigDecimal getProvbtiproy() {
		return provbtiproy;
	}

	public void setProvbtiproy(BigDecimal provbtiproy) {
		this.provbtiproy = provbtiproy;
	}

	public BigDecimal getProvbelproy() {
		return provbelproy;
	}

	public void setProvbelproy(BigDecimal provbelproy) {
		this.provbelproy = provbelproy;
	}

	public java.math.BigDecimal getRaumic() {
		return raumic;
	}

	public void setRaumic(java.math.BigDecimal raumic) {
		this.raumic = raumic;
	}

	public java.math.BigDecimal getRosspCSM() {
		return rosspCSM;
	}

	public void setRosspCSM(java.math.BigDecimal rosspCSM) {
		this.rosspCSM = rosspCSM;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
		result = prime * result + ((fecdesde == null) ? 0 : fecdesde.hashCode());
		result = prime * result + ((provbtiproy == null) ? 0 : provbtiproy.hashCode());
		result = prime * result + ((provbelproy == null) ? 0 : provbelproy.hashCode());
		result = prime * result + ((raumic == null) ? 0 : raumic.hashCode());
		result = prime * result + ((rosspCSM == null) ? 0 : rosspCSM.hashCode());
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
		PatronCsm other = (PatronCsm) obj;
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
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		if (fecdesde == null) {
			if (other.fecdesde != null)
				return false;
		} else if (!fecdesde.equals(other.fecdesde))
			return false;
		if (provbtiproy == null) {
			if (other.provbtiproy != null)
				return false;
		} else if (!provbtiproy.equals(other.provbtiproy))
			return false;
		if (provbelproy == null) {
			if (other.provbelproy != null)
				return false;
		} else if (!provbelproy.equals(other.provbelproy))
			return false;
		if (raumic == null) {
			if (other.raumic != null)
				return false;
		} else if (!raumic.equals(other.raumic))
			return false;
		if (rosspCSM == null) {
			if (other.rosspCSM != null)
				return false;
		} else if (!rosspCSM.equals(other.rosspCSM))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "PatronCsm [bt=" + bt + ", fcierre=" + fcierre + ", ccanal=" + ccanal + ", uoa="
				+ uoa + ", fecdesde=" + fecdesde + ", provbtiproy=" + provbtiproy
				+ ", provbelproy=" + provbelproy + ", raumic=" + raumic + ", rosspCSM="
				+ rosspCSM + "]";
	}

	@Override
	public PatronCsmKey getKey() {
		return new PatronCsmKey(bt, fcierre, ccanal, uoa, fecdesde);
	}
}

