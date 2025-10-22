package es.mapfre.scr.gastos.dominio.keys;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class IncidenciaKey {
	
	private String bt;
	private String generadorError;
	private String codigoRetorno;
	private String fecCierre;
	
	public IncidenciaKey() {
		super();
	}

	public IncidenciaKey(String bt, String generadorError, String codigoRetorno, String fecCierre) {
		super();
		this.bt = bt;
		this.fecCierre = fecCierre;
		this.generadorError = generadorError;
		this.codigoRetorno = codigoRetorno;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result + ((codigoRetorno == null) ? 0 : codigoRetorno.hashCode());
		result = prime * result + ((generadorError == null) ? 0 : generadorError.hashCode());
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
		IncidenciaKey other = (IncidenciaKey) obj;
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (fecCierre == null) {
			if (other.fecCierre != null) {
				return false;
			}
		} else if (!fecCierre.equals(other.fecCierre)) {
			return false;
		}
		if (codigoRetorno == null) {
			if (other.codigoRetorno != null) {
				return false;
			}
		} else if (!codigoRetorno.equals(other.codigoRetorno)) {
			return false;
		}
		if (generadorError == null) {
			if (other.generadorError != null) {
				return false;
			}
		} else if (!generadorError.equals(other.generadorError)) {
			return false;
		}
		return true;
	}
	
	public int compareTo(IncidenciaKey o) {
		if (o == null) {
			return -1;
		}

		CompareToBuilder compareToBuilder = new CompareToBuilder();

		compareToBuilder.append(this.fecCierre, o.fecCierre);
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.generadorError, o.generadorError);
		compareToBuilder.append(this.codigoRetorno, o.codigoRetorno);
		
		return compareToBuilder.toComparison();
	}
}
