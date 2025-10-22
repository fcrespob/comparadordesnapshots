package es.mapfre.solvencia.dominio.conversionesBel;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresAnulacionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresAnulacion implements EntidadBase<ValoresAnulacionKey>{
	
	public static final int IND_CODTABLA = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_ANIOSDESDE = 2;
	public static final int IND_PROBABANUL = 3;
	public static final int IND_POLIZAVIGENTES = 4;
	
	@PortableProperty(IND_CODTABLA) private String codTabla;
	@PortableProperty(IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(IND_ANIOSDESDE) private BigDecimal aniosDesde;
	@PortableProperty(IND_PROBABANUL) private BigDecimal probabAnul;
	@PortableProperty(IND_POLIZAVIGENTES) private BigDecimal polizaVigentes;
	
	public String getCodTabla() {
		return codTabla;
	}
	public void setCodTabla(String codTabla) {
		this.codTabla = codTabla;
	}
	public Timestamp getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}
	public BigDecimal getAniosDesde() {
		return aniosDesde;
	}
	public void setAniosDesde(BigDecimal aniosDesde) {
		this.aniosDesde = aniosDesde;
	}
	public BigDecimal getProbabAnul() {
		return probabAnul;
	}
	public void setProbabAnul(BigDecimal probabAnul) {
		this.probabAnul = probabAnul;
	}
	public BigDecimal getPolizaVigentes() {
		return polizaVigentes;
	}
	public void setPolizaVigentes(BigDecimal polizaVigentes) {
		this.polizaVigentes = polizaVigentes;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codTabla == null) ? 0 : codTabla.hashCode());
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result
				+ ((aniosDesde == null) ? 0 : aniosDesde.hashCode());
		result = prime * result
				+ ((polizaVigentes == null) ? 0 : polizaVigentes.hashCode());
		result = prime * result
				+ ((probabAnul == null) ? 0 : probabAnul.hashCode());
		return result;
	}
	
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValoresAnulacion other = (ValoresAnulacion) obj;
		if (codTabla == null) {
			if (other.codTabla != null) {
				return false;
			}
		} else if (!codTabla.equals(other.codTabla)) {
			return false;
		}
		if (fecCierre == null) {
			if (other.fecCierre != null) {
				return false;
			}
		} else if (!fecCierre.equals(other.fecCierre)) {
			return false;
		}
		if (aniosDesde == null) {
			if (other.aniosDesde != null) {
				return false;
			}
		} else if (!aniosDesde.equals(other.aniosDesde)) {
			return false;
		}
		if (polizaVigentes == null) {
			if (other.polizaVigentes != null) {
				return false;
			}
		} else if (!polizaVigentes.equals(other.polizaVigentes)) {
			return false;
		}
		if (probabAnul == null) {
			if (other.probabAnul != null) {
				return false;
			}
		} else if (!probabAnul.equals(other.probabAnul)) {
			return false;
		}
		return true;
	}
	@Override
	public ValoresAnulacionKey getKey() {
		return new ValoresAnulacionKey(codTabla, fecCierre, aniosDesde);
	}
	
	public ValoresAnulacion() {
		super();
	}
	
	
}