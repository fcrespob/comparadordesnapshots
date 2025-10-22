package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FlujosProbables;

@Portable
public class FlujosProbablesKey {
	@PortableProperty(FlujosProbables.IND_MODALIDAD) private Integer modalidad;
	@PortableProperty(FlujosProbables.IND_GARANTIA) private Integer garantia;
	@PortableProperty(FlujosProbables.IND_PRESTACION) private String prestacion;
	@PortableProperty(FlujosProbables.IND_BASETECNICA) private String basetecnica;
	
	
	
	
	public FlujosProbablesKey(Integer modalidad, Integer garantia,
			String prestacion, String basetecnica) {
		super();
		this.modalidad = modalidad;
		this.garantia = garantia;
		this.prestacion = prestacion;
		this.basetecnica = basetecnica;
	}



	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((basetecnica == null) ? 0 : basetecnica.hashCode());
		result = prime * result
				+ ((garantia == null) ? 0 : garantia.hashCode());
		result = prime * result
				+ ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result
				+ ((prestacion == null) ? 0 : prestacion.hashCode());
		return result;
	}
	
	
	
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			{{ return true; }}
		if (obj == null)
			{{ return false; }}
		if (getClass() != obj.getClass())
			{{ return false; }}
		FlujosProbablesKey other = (FlujosProbablesKey) obj;
		if (basetecnica == null) {
			if (other.basetecnica != null)
				{{ return false; }}
		} else if (!basetecnica.equals(other.basetecnica))
			{{ return false; }}
		if (garantia == null) {
			if (other.garantia != null)
				{{ return false; }}
		} else if (!garantia.equals(other.garantia))
			{{ return false; }}
		if (modalidad == null) {
			if (other.modalidad != null)
				{{ return false; }}
		} else if (!modalidad.equals(other.modalidad))
			{{ return false; }}
		if (prestacion == null) {
			if (other.prestacion != null)
				{{ return false; }}
		} else if (!prestacion.equals(other.prestacion))
			{{ return false; }}
		{ return true; }
	}
	
	public FlujosProbablesKey(){
		super();
	}
	
}
