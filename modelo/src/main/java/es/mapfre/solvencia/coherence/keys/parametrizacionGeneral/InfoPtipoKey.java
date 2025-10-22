package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.InfoPtipo;

@Portable
public class InfoPtipoKey {
	
	@PortableProperty(InfoPtipo.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(InfoPtipo.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(InfoPtipo.IND_KPRESTACION)
	private String kprestacion;

	public InfoPtipoKey(){
		super();
	}
	
	public InfoPtipoKey(Integer kmodalidad, Integer kgarantia, String kprestacion) {
		super();
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((kprestacion == null) ? 0 : kprestacion.hashCode());
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
		InfoPtipoKey other = (InfoPtipoKey) obj;
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kprestacion == null) {
			if (other.kprestacion != null) {
				return false;
			}
		} else if (!kprestacion.equals(other.kprestacion)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("InfoPtipoKey [kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kgarantia=");
		builder.append(kgarantia);
		builder.append(", kprestacion=");
		builder.append(kprestacion);
		builder.append("]");
		return builder.toString();
	}	
}