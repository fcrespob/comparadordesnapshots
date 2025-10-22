package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ConfiguracionProyeccion   implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PortableProperty(0) private String tipoCorriente;
	@PortableProperty(1) private String criterioFechaPago;
	@PortableProperty(2) private String criterioFecDEvengo;
	@PortableProperty(3) private String moduloNominal;
	@PortableProperty(4) private String moduloProbable;
	@PortableProperty(5) private String moduloNoAnulado;
	@PortableProperty(6) private String moduloActualizacion;
	@PortableProperty(7) private String indProvi;

	public ConfiguracionProyeccion() {
		super();
	}

	public String getModuloNominal() {
		return this.moduloNominal;
	}

	public void setModuloNominal(final String moduloNominal) {
		this.moduloNominal = moduloNominal;
	}

	public String getModuloProbable() {
		return this.moduloProbable;
	}

	public void setModuloProbable(final String moduloProbable) {
		this.moduloProbable = moduloProbable;
	}

	public String getModuloNoAnulado() {
		return this.moduloNoAnulado;
	}

	public void setModuloNoAnulado(final String moduloNoAnulado) {
		this.moduloNoAnulado = moduloNoAnulado;
	}

	public String getModuloActualizacion() {
		return this.moduloActualizacion;
	}

	public void setModuloActualizacion(final String moduloActualizacion) {
		this.moduloActualizacion = moduloActualizacion;
	}

	public String getIndProvi() {
		return this.indProvi;
	}

	public void setIndProvi(final String indProvi) {
		this.indProvi = indProvi;
	}

	public String getTipoCorriente() {
		return tipoCorriente;
	}

	public void setTipoCorriente(String tipoCorriente) {
		this.tipoCorriente = tipoCorriente;
	}

	public String getCriterioFechaPago() {
		return criterioFechaPago;
	}

	public void setCriterioFechaPago(String criterioFechaPago) {
		this.criterioFechaPago = criterioFechaPago;
	}

	public String getCriterioFecDEvengo() {
		return criterioFecDEvengo;
	}

	public void setCriterioFecDEvengo(String criterioFecDEvengo) {
		this.criterioFecDEvengo = criterioFecDEvengo;
	}

}
