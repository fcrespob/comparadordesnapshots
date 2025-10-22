package es.mapfre.proxy.prestaciones.dominio.keys;

import java.util.Date;

public class FichaProcesoKey {

	private Integer ccanal;
	private Date    fecCierre;
	private Date    fecPesos;
	private String  sistema;
	
	public FichaProcesoKey() {
		super();
	}

	public FichaProcesoKey(Integer ccanal, Date fecCierre, Date fecPesos, String sistema) {
		super();
		this.ccanal = ccanal;
		this.fecCierre = fecCierre;
		this.fecPesos = fecPesos;
		this.sistema = sistema;
	}
	
	@Override
	public String toString() {
		return "FichaProceso [ccanal=" + ccanal + ", fecCierre=" + fecCierre + ", fecPesos=" + fecPesos + ", sistema=" + sistema + "]";
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
		FichaProcesoKey other = (FichaProcesoKey) obj;
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
}
