package es.mapfre.solvencia.coherence.keys.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.TasasAnulacion;

@Portable
public class TasasAnulacionKey   {
	@PortableProperty(TasasAnulacion.IND_KTABLAANU) private String ktablaanu;
	@PortableProperty(TasasAnulacion.IND_KFCIERRE) private Timestamp kfcierre;
	@PortableProperty(TasasAnulacion.IND_KANIOSDESDE) private BigDecimal kaniosdesde;
	public String getKtablaanu() {
		return ktablaanu;
	}
	public void setKtablaanu(String ktablaanu) {
		this.ktablaanu = ktablaanu;
	}
	public Timestamp getKfcierre() {
		return kfcierre;
	}
	public void setKfcierre(Timestamp kfcierre) {
		this.kfcierre = kfcierre;
	}
	public BigDecimal getkaniosdesde() {
		return kaniosdesde;
	}
	public void setkaniosdesde(BigDecimal kaniosdesde) {
		this.kaniosdesde = kaniosdesde;
	}
	public TasasAnulacionKey() {
		super();
	}
	public TasasAnulacionKey(String ktablaanu, Timestamp kfcierre,
			BigDecimal kaniosdesde) {
		super();
		this.ktablaanu = ktablaanu;
		this.kfcierre = kfcierre;
		this.kaniosdesde = kaniosdesde;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kfcierre == null) ? 0 : kfcierre.hashCode());
		result = prime * result
				+ ((kaniosdesde == null) ? 0 : kaniosdesde.hashCode());
		result = prime * result
				+ ((ktablaanu == null) ? 0 : ktablaanu.hashCode());
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
		TasasAnulacionKey other = (TasasAnulacionKey) obj;
		if (kfcierre == null) {
			if (other.kfcierre != null)
				return false;
		} else if (!kfcierre.equals(other.kfcierre))
			return false;
		if (kaniosdesde == null) {
			if (other.kaniosdesde != null)
				return false;
		} else if (!kaniosdesde.equals(other.kaniosdesde))
			return false;
		if (ktablaanu == null) {
			if (other.ktablaanu != null)
				return false;
		} else if (!ktablaanu.equals(other.ktablaanu))
			return false;
		return true;
	}
	
}
