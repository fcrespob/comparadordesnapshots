package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class FichaPeriodos implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private String ktipobt;
	@PortableProperty(1) private String vctoPeriodos;
	@PortableProperty(2) private String periodicidad;
	@PortableProperty(3) private String limite;
	
	public FichaPeriodos() {
		super();
	}

	public String getKtipobt() {
		return ktipobt;
	}

	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}

	public String getVctoPeriodos() {
		return vctoPeriodos;
	}

	public void setVctoPeriodos(String vctoPeriodos) {
		this.vctoPeriodos = vctoPeriodos;
	}

	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getLimite() {
		return limite;
	}

	public void setLimite(String limite) {
		this.limite = limite;
	}
	
}
