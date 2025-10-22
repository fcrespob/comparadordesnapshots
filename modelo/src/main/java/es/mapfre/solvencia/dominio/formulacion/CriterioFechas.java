package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class CriterioFechas implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private String fecDevengo;
	
	@PortableProperty(1) private String fecPago;
	
	public String getFecDevengo() {
		return fecDevengo;
	}
	public void setFecDevengo(String fecDevengo) {
		this.fecDevengo = fecDevengo;
	}
	public String getFecPago() {
		return fecPago;
	}
	public void setFecPago(String fecPago) {
		this.fecPago = fecPago;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fecDevengo == null) ? 0 : fecDevengo.hashCode());
		result = prime * result + ((fecPago == null) ? 0 : fecPago.hashCode());
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
		CriterioFechas other = (CriterioFechas) obj;
		if (fecDevengo == null) {
			if (other.fecDevengo != null) {
				return false;
			}
		} else if (!fecDevengo.equals(other.fecDevengo)) {
			return false;
		}
		if (fecPago == null) {
			if (other.fecPago != null) {
				return false;
			}
		} else if (!fecPago.equals(other.fecPago)) {
			return false;
		}
		return true;
	}
}