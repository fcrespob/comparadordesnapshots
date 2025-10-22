package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class RegistroParametros {
	
	public static final int IND_INDPROCESO = 0;

	@PortableProperty(IND_INDPROCESO) private String indicadorTipoProceso;

	public String getIndicadorTipoProceso() {
		return indicadorTipoProceso;
	}

	public void setIndicadorTipoProceso(String indicadorTipoProceso) {
		this.indicadorTipoProceso = indicadorTipoProceso;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((indicadorTipoProceso == null) ? 0 : indicadorTipoProceso
						.hashCode());
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
		RegistroParametros other = (RegistroParametros) obj;
		if (indicadorTipoProceso == null) {
			if (other.indicadorTipoProceso != null) {
				return false;
			}
		} else if (!indicadorTipoProceso.equals(other.indicadorTipoProceso)) {
			return false;
		}
		return true;
	}
}