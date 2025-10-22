package es.mapfre.gbt.mensualizadorTasas.exception;

import es.mapfre.gbt.mensualizadorTasas.key.IncidenciaMensKey;

public class IncidenciaMens {
	
	public static final int IND_TABLA = 0;
	public static final int IND_FECHA = 1;
	public static final int IND_DESC = 2;
	
	 private String tabla;
	 private String fecha;	
	 private String descripcion;
	
	
	
	public IncidenciaMens(String tabla, String fecha, String descripcion) {
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
		IncidenciaMens other = (IncidenciaMens) obj;
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



	public String getTabla() {
		return tabla;
	}



	public void setTabla(String tabla) {
		this.tabla = tabla;
	}



	public String getFecha() {
		return fecha;
	}



	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	public String getDescripcion() {
		return descripcion;
	}



	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}



	public IncidenciaMensKey getKey() {
		return new IncidenciaMensKey(tabla, fecha, descripcion);
	}

}