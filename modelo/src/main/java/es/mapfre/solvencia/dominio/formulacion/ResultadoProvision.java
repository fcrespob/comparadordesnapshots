package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ResultadoProvision implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private java.math.BigDecimal importe;
		
	public ResultadoProvision(java.math.BigDecimal importe) {
		super();
		this.importe = importe;
	}

	public java.math.BigDecimal getImporte() {
		return importe;
	}

	public void setImporte(java.math.BigDecimal importe) {
		this.importe = importe;
	}
	
}
