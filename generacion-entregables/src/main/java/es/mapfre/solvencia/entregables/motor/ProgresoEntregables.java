package es.mapfre.solvencia.entregables.motor;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ProgresoEntregables {
	@PortableProperty(1) private Integer terminadas;
	@PortableProperty(2) private Integer totales;
	@PortableProperty(3) private Long timestamp;
	
	public ProgresoEntregables() {
		super();
	}

	public Integer getTerminadas() {
		return terminadas;
	}
	public void setTerminadas(Integer terminadas) {
		this.terminadas = terminadas;
	}
	public Integer getTotales() {
		return totales;
	}
	public void setTotales(Integer totales) {
		this.totales = totales;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((terminadas == null) ? 0 : terminadas.hashCode());
		result = prime * result + ((timestamp == null) ? 0 : timestamp.hashCode());
		result = prime * result + ((totales == null) ? 0 : totales.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ProgresoEntregables other = (ProgresoEntregables) obj;
		if (terminadas == null) {
			if (other.terminadas != null) {
				return false;
			}
		} else if (!terminadas.equals(other.terminadas)) {
			return false;
		}
		if (timestamp == null) {
			if (other.timestamp != null) {
				return false;
			}
		} else if (!timestamp.equals(other.timestamp)) {
			return false;
		}
		if (totales == null) {
			if (other.totales != null) {
				return false;
			}
		} else if (!totales.equals(other.totales)) {
			return false;
		}
		return true;
	}

	public double getAvance() {
		if (totales != null && totales != 0) {
			return ((double)(terminadas*100))/totales;
		} else if (totales == 0 && terminadas == 0) {
			return 100.0;
		}
		
		return 0.0;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ProgresoCalculo [terminadas=");
		builder.append(terminadas);
		builder.append(", totales=");
		builder.append(totales);
		builder.append(", timestamp=");
		builder.append(timestamp);
		builder.append(", progreso=");
		builder.append(getAvance());
		builder.append("%]");
		return builder.toString();
	}
	

}
