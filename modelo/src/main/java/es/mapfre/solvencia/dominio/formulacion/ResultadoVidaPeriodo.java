package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ResultadoVidaPeriodo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private Timestamp fechaInicio;
	@PortableProperty(1) private Timestamp fechaFin;
	@PortableProperty(2) private java.math.BigDecimal nominal = new java.math.BigDecimal("0.0");
	@PortableProperty(3) private java.math.BigDecimal probable = new java.math.BigDecimal("0.0");
	public Timestamp getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Timestamp getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}
	public java.math.BigDecimal getNominal() {
		return nominal;
	}
	public void setNominal(java.math.BigDecimal nominal) {
		this.nominal = nominal;
	}
	public java.math.BigDecimal getProbable() {
		return probable;
	}
	public void setProbable(java.math.BigDecimal probable) {
		this.probable = probable;
	}
	public ResultadoVidaPeriodo(Timestamp fechaInicio, Timestamp fechaFin,
			java.math.BigDecimal nominal, java.math.BigDecimal probable) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.nominal = nominal;
		this.probable = probable;
	}
	
	
	
}
