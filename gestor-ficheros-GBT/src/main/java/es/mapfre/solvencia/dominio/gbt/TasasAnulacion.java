package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.TasasAnulacionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class TasasAnulacion implements EntidadBase<TasasAnulacionKey> {

	public static final int IND_KTABLAANU = 0;
	public static final int IND_KFCIERRE = 1;
	public static final int IND_KANIOSDESDE = 2;
	public static final int IND_NANIOSHASTA = 3;
	public static final int IND_PPROBANU = 4;
	
	@PortableProperty(IND_KTABLAANU) private String ktablaanu;
	@PortableProperty(IND_KFCIERRE) private Timestamp kfcierre;
	@PortableProperty(IND_KANIOSDESDE) private BigDecimal kaniosdesde;
	@PortableProperty(IND_NANIOSHASTA) private BigDecimal nanioshasta;
	@PortableProperty(IND_PPROBANU) private BigDecimal pprobanu;
	
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

	public BigDecimal getKaniosdesde() {
		return kaniosdesde;
	}

	public void setKaniosdesde(BigDecimal kaniosdesde) {
		this.kaniosdesde = kaniosdesde;
	}

	public BigDecimal getNanioshasta() {
		return nanioshasta;
	}

	public void setNanioshasta(BigDecimal nanioshasta) {
		this.nanioshasta = nanioshasta;
	}

	public BigDecimal getPprobanu() {
		return pprobanu;
	}

	public void setPprobanu(BigDecimal pprobanu) {
		this.pprobanu = pprobanu;
	}
	
	public TasasAnulacion() {
		super();
	}

	public TasasAnulacion(String ktablaanu, Timestamp kfcierre,
			BigDecimal kaniosdesde, BigDecimal nanioshasta, BigDecimal pprobanu,
			String cusuaralta, Timestamp falta, String cusuarmodif, Timestamp fmodif) {
		super();
		this.ktablaanu = ktablaanu;
		this.kfcierre = kfcierre;
		this.kaniosdesde = kaniosdesde;
		this.nanioshasta = nanioshasta;
		this.pprobanu = pprobanu;
	}

	@Override
	public TasasAnulacionKey getKey() {
		return new TasasAnulacionKey(ktablaanu, kfcierre, kaniosdesde);
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
		result = prime * result
				+ ((nanioshasta == null) ? 0 : nanioshasta.hashCode());
		result = prime * result
				+ ((pprobanu == null) ? 0 : pprobanu.hashCode());
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
		TasasAnulacion other = (TasasAnulacion) obj;
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
		if (nanioshasta == null) {
			if (other.nanioshasta != null)
				return false;
		} else if (!nanioshasta.equals(other.nanioshasta))
			return false;
		if (pprobanu == null) {
			if (other.pprobanu != null)
				return false;
		} else if (!pprobanu.equals(other.pprobanu))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TasasAnulacion [ktablaanu=");
		builder.append(ktablaanu);
		builder.append(", kfcierre=");
		builder.append(kfcierre);
		builder.append(", kaniosdesde=");
		builder.append(kaniosdesde);
		builder.append(", nanioshasta=");
		builder.append(nanioshasta);
		builder.append(", pprobanu=");
		builder.append(pprobanu);
		builder.append("]");
		return builder.toString();
	}

}
