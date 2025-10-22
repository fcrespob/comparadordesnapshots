package es.mapfre.solvencia.dominio.salidaCalculo;


import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;


@Portable
public class TablaConversion {
	public static final int IND_TABLA_INICIO = 0;
	public static final int IND_TABLA_FIN = 1;
	
	
	@PortableProperty(value=IND_TABLA_INICIO) private String tablaInicio;
	@PortableProperty(value=IND_TABLA_FIN) private String tablaFin;
	
	public String getTablaInicio() {
		return tablaInicio;
	}
	public void setTablaInicio(String tablaInicio) {
		this.tablaInicio = tablaInicio;
	}
	public String getTablaFin() {
		return tablaFin;
	}
	public void setTablaFin(String tablaFin) {
		this.tablaFin = tablaFin;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((tablaFin == null) ? 0 : tablaFin.hashCode());
		result = prime * result
				+ ((tablaInicio == null) ? 0 : tablaInicio.hashCode());
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
		TablaConversion other = (TablaConversion) obj;
		if (tablaFin == null) {
			if (other.tablaFin != null) {
				return false;
			}
		} else if (!tablaFin.equals(other.tablaFin)) {
			return false;
		}
		if (tablaInicio == null) {
			if (other.tablaInicio != null) {
				return false;
			}
		} else if (!tablaInicio.equals(other.tablaInicio)) {
			return false;
		}
		return true;
	}

	


}
