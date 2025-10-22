package es.mapfre.proxy.prestaciones.dominio.keys;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class IncidenciaKey {
	
	private String ccanal;
	private String sistema;
	private String generadorError;
	private String codigoRetorno;
	private String fecCierre;
	
	public IncidenciaKey() {
		super();
	}

	public IncidenciaKey(String ccanal, String sistema, String generadorError, String codigoRetorno, String fecCierre) {
		super();
		this.ccanal = ccanal;
		this.sistema = sistema;
		this.fecCierre = fecCierre;
		this.generadorError = generadorError;
		this.codigoRetorno = codigoRetorno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((codigoRetorno == null) ? 0 : codigoRetorno.hashCode());
		result = prime * result + ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((generadorError == null) ? 0 : generadorError.hashCode());
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
		IncidenciaKey other = (IncidenciaKey) obj;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (codigoRetorno == null) {
			if (other.codigoRetorno != null)
				return false;
		} else if (!codigoRetorno.equals(other.codigoRetorno))
			return false;
		if (fecCierre == null) {
			if (other.fecCierre != null)
				return false;
		} else if (!fecCierre.equals(other.fecCierre))
			return false;
		if (generadorError == null) {
			if (other.generadorError != null)
				return false;
		} else if (!generadorError.equals(other.generadorError))
			return false;
		if (sistema == null) {
			if (other.sistema != null)
				return false;
		} else if (!sistema.equals(other.sistema))
			return false;
		return true;
	}

	public int compareTo(IncidenciaKey o) {
		if (o == null) {
			return -1;
		}

		CompareToBuilder compareToBuilder = new CompareToBuilder();

		compareToBuilder.append(this.fecCierre, o.fecCierre);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.sistema, o.sistema);
		compareToBuilder.append(this.generadorError, o.generadorError);
		compareToBuilder.append(this.codigoRetorno, o.codigoRetorno);
		
		return compareToBuilder.toComparison();
	}
}
