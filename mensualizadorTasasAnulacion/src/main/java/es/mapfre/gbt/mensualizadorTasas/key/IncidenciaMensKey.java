package es.mapfre.gbt.mensualizadorTasas.key;

import java.io.Serializable;

public class IncidenciaMensKey implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private String tabla;
	 private String fecha;
	 private String descripcion;
	
	public IncidenciaMensKey() {
		super();
	}
	
	public IncidenciaMensKey(String tabla, String fecha, String descripcion) {
		super();
		this.tabla = tabla;
		this.fecha = fecha;
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((tabla == null) ? 0 : tabla.hashCode());
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
		IncidenciaMensKey other = (IncidenciaMensKey) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (tabla == null) {
			if (other.tabla != null)
				return false;
		} else if (!tabla.equals(other.tabla))
			return false;
		return true;
	}


}