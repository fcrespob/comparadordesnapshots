package es.mapfre.scr.tasasAnulacion.dominio.keys;

public class ValoresEstresKey {

	private String feccierre;
	private String bt;
	private String variable;
	
	public String getFeccierre() {
		return feccierre;
	}
	
	public void setFeccierre(String feccierre) {
		this.feccierre = feccierre;
	}
	
	public String getBt() {
		return bt;
	}
	
	public void setBt(String bt) {
		this.bt = bt;
	}
	
	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result
				+ ((bt == null) ? 0 : bt.hashCode());
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
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
		ValoresEstresKey other = (ValoresEstresKey) obj;
		if (feccierre == null) {
			if (other.feccierre != null) {
				return false;
			}
		} else if (!feccierre.equals(other.feccierre)) {
			return false;
		}
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (variable == null) {
			if (other.variable != null) {
				return false;
			}
		} else if (!variable.equals(other.variable)) {
			return false;
		}
		return true;
	}

	public ValoresEstresKey(String feccierre, String bt,
			String variable) {
		super();
		this.feccierre = feccierre;
		this.bt = bt;
		this.variable = variable;
	}

	public ValoresEstresKey() {
		super();
	}

	@Override
	public String toString() {
		return "ValoresEstresKey [feccierre=" + feccierre
				+ ", bt=" + bt + ", variable=" + variable
				+ "]";
	}
}
