package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresAnulacionMensuales;

@Portable
public class ValoresAnulacionMensualesKey {
	
	@PortableProperty(ValoresAnulacionMensuales.IND_CODTABLA) private String ktabla;
	@PortableProperty(ValoresAnulacionMensuales.IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(ValoresAnulacionMensuales.IND_MESESDESDE) private Integer mesesDesde;
	
	public ValoresAnulacionMensualesKey(String ktabla, Timestamp fecCierre, Integer mesesDesde) {
		super();
		this.ktabla = ktabla;
		this.fecCierre = fecCierre;
		this.mesesDesde = mesesDesde;
	}
	
	public ValoresAnulacionMensualesKey() {
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
				+ ((mesesDesde == null) ? 0 : mesesDesde.hashCode());
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
		ValoresAnulacionMensualesKey other = (ValoresAnulacionMensualesKey) obj;
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
		if (mesesDesde == null) {
			if (other.mesesDesde != null) {
				return false;
			}
		} else if (!mesesDesde.equals(other.mesesDesde)) {
			return false;
		}
		return true;
	}
}