package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacion;

@Portable
public class ValoresAnulacionKey {
	
	@PortableProperty(ValoresAnulacion.IND_CODTABLA) private String ktabla;
	@PortableProperty(ValoresAnulacion.IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(ValoresAnulacion.IND_ANIOSDESDE) private BigDecimal aniosDesde;
	
	public ValoresAnulacionKey(String ktabla, Timestamp fecCierre, BigDecimal aniosDesde) {
		super();
		this.ktabla = ktabla;
		this.fecCierre = fecCierre;
		this.aniosDesde = aniosDesde;
	}
	
	public ValoresAnulacionKey() {
		super();
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((ktabla == null) ? 0 : ktabla.hashCode());
		result = prime * result
				+ ((aniosDesde == null) ? 0 : aniosDesde.hashCode());
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
		ValoresAnulacionKey other = (ValoresAnulacionKey) obj;
		if (fecCierre == null) {
			if (other.fecCierre != null) {
				return false;
			}
		} else if (!fecCierre.equals(other.fecCierre)) {
			return false;
		}
		if (ktabla == null) {
			if (other.ktabla != null) {
				return false;
			}
		} else if (!ktabla.equals(other.ktabla)) {
			return false;
		}
		if (aniosDesde == null) {
			if (other.aniosDesde != null) {
				return false;
			}
		} else if (!aniosDesde.equals(other.aniosDesde)) {
			return false;
		}
		return true;
	}
}