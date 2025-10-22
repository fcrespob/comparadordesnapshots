package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ElementoSubproceso {

	
	private static final int IND_CELEMENT = 0;

	private static final int IND_SSUBPROC = 1;
	
	@PortableProperty(IND_CELEMENT) private String celement;
	@PortableProperty(IND_SSUBPROC) private Boolean ssubproc;
	
	public String getCelement() {
		return celement;
	}
	public void setCelement(String celement) {
		this.celement = celement;
	}
	public Boolean getSsubproc() {
		return ssubproc;
	}
	public void setSsubproc(Boolean ssubproc) {
		this.ssubproc = ssubproc;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((celement == null) ? 0 : celement.hashCode());
		result = prime * result
				+ ((ssubproc == null) ? 0 : ssubproc.hashCode());
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
		ElementoSubproceso other = (ElementoSubproceso) obj;
		if (celement == null) {
			if (other.celement != null) {
				return false;
			}
		} else if (!celement.equals(other.celement)) {
			return false;
		}
		if (ssubproc == null) {
			if (other.ssubproc != null) {
				return false;
			}
		} else if (!ssubproc.equals(other.ssubproc)) {
			return false;
		}
		return true;
	}	
	
	
	
}
