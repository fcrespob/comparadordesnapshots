package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.Date;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.FichaProcesoKey;

public class FichaProceso implements EntidadBase<FichaProcesoKey>{

	public static final int IND_FECCIERRE = 0;

	private Date fecCierre;
	
	public Date getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Date fecCierre) {
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
		FichaProceso other = (FichaProceso) obj;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		return true;
	}
	@Override
	public FichaProcesoKey getKey() {
		return new FichaProcesoKey(fecCierre);
	}
}
