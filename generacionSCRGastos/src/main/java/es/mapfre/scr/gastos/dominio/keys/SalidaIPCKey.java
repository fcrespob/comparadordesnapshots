package es.mapfre.scr.gastos.dominio.keys;

public class SalidaIPCKey {

	private String ffin;

	public String getFfin() {
		return ffin;
	}
	
	public void setFfin(String ffin) {
		this.ffin = ffin;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ffin == null) ? 0 : ffin.hashCode());
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
		SalidaIPCKey other = (SalidaIPCKey) obj;
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		return true;
	}


	public SalidaIPCKey(String ffin) {
		super();
		this.ffin = ffin;
	}


	public SalidaIPCKey() {
		super();
	}


	@Override
	public String toString() {
		return "IPCGeneralFuturoKey [ffin=" + ffin + "]";
	}
}

