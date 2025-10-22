package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ConfiguracionSubproceso   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private String criterioPeriodos;
	@PortableProperty(1) private String periodicidad;
	@PortableProperty(2) private String vencimiento;
	
	public ConfiguracionSubproceso() {
		super();
	}

	public String getCriterioPeriodos() {
		return criterioPeriodos;
	}

	public void setCriterioPeriodos(String criterioPeriodos) {
		this.criterioPeriodos = criterioPeriodos;
	}

	public String getPeriodicidad() {
		return periodicidad;
	}

	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}

	public String getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}
}
