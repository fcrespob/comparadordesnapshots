package es.mapfre.coaseguro.tirea.dominio.keys;

public class FlujosRealesSalidaKey {

	private String codCia;
	private String  numExp;
	private String anioMes;
	private String numMvto;
	private String codCob;
	private String codCtoRva;
	
	public FlujosRealesSalidaKey(String codCia, String numExp, String anioMes, String numMvto, String codCob,
			String codCtoRva) {
		super();
		this.codCia = codCia;
		this.numExp = numExp;
		this.anioMes = anioMes;
		this.numMvto = numMvto;
		this.codCob = codCob;
		this.codCtoRva = codCtoRva;
	}

	public FlujosRealesSalidaKey() {
		super();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anioMes == null) ? 0 : anioMes.hashCode());
		result = prime * result + ((codCia == null) ? 0 : codCia.hashCode());
		result = prime * result + ((codCob == null) ? 0 : codCob.hashCode());
		result = prime * result + ((codCtoRva == null) ? 0 : codCtoRva.hashCode());
		result = prime * result + ((numExp == null) ? 0 : numExp.hashCode());
		result = prime * result + ((numMvto == null) ? 0 : numMvto.hashCode());
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
		FlujosRealesSalidaKey other = (FlujosRealesSalidaKey) obj;
		if (anioMes == null) {
			if (other.anioMes != null)
				return false;
		} else if (!anioMes.equals(other.anioMes))
			return false;
		if (codCia == null) {
			if (other.codCia != null)
				return false;
		} else if (!codCia.equals(other.codCia))
			return false;
		if (codCob == null) {
			if (other.codCob != null)
				return false;
		} else if (!codCob.equals(other.codCob))
			return false;
		if (codCtoRva == null) {
			if (other.codCtoRva != null)
				return false;
		} else if (!codCtoRva.equals(other.codCtoRva))
			return false;
		if (numExp == null) {
			if (other.numExp != null)
				return false;
		} else if (!numExp.equals(other.numExp))
			return false;
		if (numMvto == null) {
			if (other.numMvto != null)
				return false;
		} else if (!numMvto.equals(other.numMvto))
			return false;
		return true;
	}

	public String getCodCia() {
		return codCia;
	}

	public void setCodCia(String codCia) {
		this.codCia = codCia;
	}

	public String getNumExp() {
		return numExp;
	}

	public void setNumExp(String numExp) {
		this.numExp = numExp;
	}

	public String getAnioMes() {
		return anioMes;
	}

	public void setAnioMes(String anioMes) {
		this.anioMes = anioMes;
	}

	public String getNumMvto() {
		return numMvto;
	}

	public void setNumMvto(String numMvto) {
		this.numMvto = numMvto;
	}

	public String getCodCob() {
		return codCob;
	}

	public void setCodCob(String codCob) {
		this.codCob = codCob;
	}

	public String getCodCtoRva() {
		return codCtoRva;
	}

	public void setCodCtoRva(String codCtoRva) {
		this.codCtoRva = codCtoRva;
	}
}
