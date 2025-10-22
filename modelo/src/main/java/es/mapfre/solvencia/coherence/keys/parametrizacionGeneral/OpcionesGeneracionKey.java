package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.OpcionesGeneracion;

@Portable
public class OpcionesGeneracionKey {
	
	@PortableProperty(OpcionesGeneracion.IND_MODALIDAD) private Integer modalidad;
	@PortableProperty(OpcionesGeneracion.IND_GARANTIA) private Integer garantia;
	@PortableProperty(OpcionesGeneracion.IND_PRESTACION) private String prestacion;
	
	public OpcionesGeneracionKey () {
		super();
	}
	
	public OpcionesGeneracionKey (Integer modalidad, Integer garantia, String prestacion) {
		super();
		this.modalidad = modalidad;
		this.garantia = garantia;
		this.prestacion = prestacion;
	
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((garantia == null) ? 0 : garantia.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result + ((prestacion == null) ? 0 : prestacion.hashCode());
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
		OpcionesGeneracionKey other = (OpcionesGeneracionKey) obj;
		if (garantia == null) {
			if (other.garantia != null)
				return false;
		} else if (!garantia.equals(other.garantia))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		if (prestacion == null) {
			if (other.prestacion != null)
				return false;
		} else if (!prestacion.equals(other.prestacion))
			return false;
		return true;
	}
}