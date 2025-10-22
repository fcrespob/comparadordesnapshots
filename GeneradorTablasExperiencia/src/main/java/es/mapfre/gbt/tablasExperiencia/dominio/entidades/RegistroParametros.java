package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

public class RegistroParametros {

	public static final int IND_INDMESES = 0;

	private String indicadormeses;

	public String getindicadormeses() {
		return indicadormeses;
	}

	public void setindicadormeses(String indicadormeses) {
		this.indicadormeses = indicadormeses;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((indicadormeses == null) ? 0 : indicadormeses.hashCode());
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
		RegistroParametros other = (RegistroParametros) obj;
		if (indicadormeses == null) {
			if (other.indicadormeses != null)
				return false;
		} else if (!indicadormeses.equals(other.indicadormeses))
			return false;
		return true;
	}
}
