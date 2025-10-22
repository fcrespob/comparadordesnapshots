package es.mapfre.scr.gastos.dominio.entidades;

import es.mapfre.scr.gastos.dominio.EntidadBase;
import es.mapfre.scr.gastos.dominio.keys.SalidaIPCKey;

public class SalidaIPC implements EntidadBase<SalidaIPCKey> {

	public static final int IND_FINICIO = 0;
	public static final int IND_FFIN = 1;
	public static final int IND_PIPCLEG = 2;
	public static final int IND_PIPCGAS = 3;

	private String finicio;
	private String ffin;
	private String pipcleg;
	private String pipcgas;
	
	public SalidaIPC() {
		super();
	}

	public String getFinicio() {
		return finicio;
	}

	public void setFinicio(String finicio) {
		this.finicio = finicio;
	}

	public String getFfin() {
		return ffin;
	}

	public void setFfin(String ffin) {
		this.ffin = ffin;
	}

	public String getPipcleg() {
		return pipcleg;
	}

	public void setPipcleg(String pipcleg) {
		this.pipcleg = pipcleg;
	}

	public String getPipcgas() {
		return pipcgas;
	}

	public void setPipcgas(String pipcgas) {
		this.pipcgas = pipcgas;
	}

	@Override
	public SalidaIPCKey getKey() {
		return new SalidaIPCKey(ffin);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finicio == null) ? 0 : finicio.hashCode());
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result
				+ ((pipcleg == null) ? 0 : pipcleg.hashCode());
		result = prime * result
				+ ((pipcgas == null) ? 0 : pipcgas.hashCode());
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
		SalidaIPC other = (SalidaIPC) obj;
		if (finicio == null) {
			if (other.finicio != null) {
				return false;
			}
		} else if (!finicio.equals(other.finicio)) {
			return false;
		}
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		if (pipcleg == null) {
			if (other.pipcleg != null) {
				return false;
			}
		} else if (!pipcleg.equals(other.pipcleg)) {
			return false;
		}
		if (pipcgas == null) {
			if (other.pipcgas != null) {
				return false;
			}
		} else if (!pipcgas.equals(other.pipcgas)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "IPCGeneralFuturo [finicio=" + finicio
				+ ", ffin=" + ffin + ", pipcleg=" + pipcleg
				+ ", pipcgas=" + pipcgas + "]";
	}

}
