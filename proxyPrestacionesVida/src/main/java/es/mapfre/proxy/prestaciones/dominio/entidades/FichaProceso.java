package es.mapfre.proxy.prestaciones.dominio.entidades;

import java.util.Date;

import es.mapfre.proxy.prestaciones.dominio.EntidadBase;
import es.mapfre.proxy.prestaciones.dominio.keys.FichaProcesoKey;

public class FichaProceso implements EntidadBase<FichaProcesoKey>{

	public static final int IND_CANAL = 0;
	public static final int IND_FECCIERRE = 1;
	public static final int IND_FECPESOS = 2;
	public static final int IND_SISTEMA = 3;

	private Integer ccanal;
	private Date    fecCierre;
	private Date    fecPesos;
	private String  sistema;
	
	public Integer getCcanal() {
		return ccanal;
	}
	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}
	public Date getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Date fecCierre) {
		this.fecCierre = fecCierre;
	}
	public Date getFecPesos() {
		return fecPesos;
	}
	public void setFecPesos(Date fecPesos) {
		this.fecPesos = fecPesos;
	}
	public String getSistema() {
		return sistema;
	}
	public void setSistema(String sistema) {
		this.sistema = sistema;
	}
	
	@Override
	public String toString() {
		return "FichaProceso [ccanal=" + ccanal + ", fecCierre=" + fecCierre + ", sistema=" + sistema + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((fecPesos == null) ? 0 : fecPesos.hashCode());
		result = prime * result + ((sistema == null) ? 0 : sistema.hashCode());
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
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		if (fecPesos == null) {
			if (other.fecPesos != null)
				return false;
		} else if (!fecPesos.equals(other.fecPesos))
			return false;
		if (sistema == null) {
			if (other.sistema != null)
				return false;
		} else if (!sistema.equals(other.sistema))
			return false;
		return true;
	}
	@Override
	public FichaProcesoKey getKey() {
		return new FichaProcesoKey(ccanal, fecCierre, fecPesos, sistema);
	}
}
