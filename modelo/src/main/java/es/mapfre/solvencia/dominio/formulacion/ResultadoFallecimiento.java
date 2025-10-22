package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ResultadoFallecimiento implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private String clave;
	@PortableProperty(1) private java.math.BigDecimal nominal = new java.math.BigDecimal("0.0");
	public String getClave() {
		return clave;
	}
	public void setClave(String clave) {
		this.clave = clave;
	}
	public java.math.BigDecimal getNominal() {
		return nominal;
	}
	public void setNominal(java.math.BigDecimal nominal) {
		this.nominal = nominal;
	}	
}
