package es.mapfre.coaseguro.tirea.dominio.keys;

import java.util.Date;

public class FichaProcesoKey {

	private Date    fecCierre;
	
	public FichaProcesoKey() {
		super();
	}

	public FichaProcesoKey(Date fecCierre) {
		super();
		this.fecCierre = fecCierre;
	}
	
	@Override
	public String toString() {
		return "FichaProceso [fecCierre=" + fecCierre + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecCierre == null) ? 0 : fecCierre.hashCode());
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
		FichaProcesoKey other = (FichaProcesoKey) obj;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		return true;
	}
}
