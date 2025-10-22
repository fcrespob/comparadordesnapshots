package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesConstantesRescatesKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DefinicionesConstantesRescates implements EntidadBase<DefinicionesConstantesRescatesKey> {

	public static final int IND_KCONSTANTE = 0;
	
	public static final int IND_DESCRIPCION = 1;

	@PortableProperty(IND_KCONSTANTE) private String kconstante;
	
	@PortableProperty(IND_DESCRIPCION) private String descripcion;

	@Override
	public DefinicionesConstantesRescatesKey getKey() {
		return new DefinicionesConstantesRescatesKey(kconstante);
	}

	public String getKconstante() {
		return kconstante;
	}

	public void setKconstante(String kconstante) {
		this.kconstante = kconstante;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((kconstante == null) ? 0 : kconstante.hashCode());
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
		DefinicionesConstantesRescates other = (DefinicionesConstantesRescates) obj;
		if (descripcion == null) {
			if (other.descripcion != null) {
				return false;
			}
		} else if (!descripcion.equals(other.descripcion)) {
			return false;
		}
		if (kconstante == null) {
			if (other.kconstante != null) {
				return false;
			}
		} else if (!kconstante.equals(other.kconstante)) {
			return false;
		}
		return true;
	}
}