package es.mapfre.scr.gastos.dominio.keys;

public class IPCGeneralFuturoKey {

	private String ffin;
		
	public IPCGeneralFuturoKey(String ffin) {
		super();
		this.ffin = ffin;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ffin == null) ? 0 : ffin.hashCode());
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
		IPCGeneralFuturoKey other = (IPCGeneralFuturoKey) obj;
		if (ffin == null) {
			if (other.ffin != null) {
				return false;
			}
		} else if (!ffin.equals(other.ffin)) {
			return false;
		}
		return true;
	}

	public IPCGeneralFuturoKey() {
		super();
	}
}