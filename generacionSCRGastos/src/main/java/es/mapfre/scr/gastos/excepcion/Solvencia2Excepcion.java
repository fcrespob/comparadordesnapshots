package es.mapfre.scr.gastos.excepcion;

import es.mapfre.scr.gastos.dominio.entidades.Incidencia;

public class Solvencia2Excepcion extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private Incidencia incidencia;

	public Incidencia getIncidencia() {
		return incidencia;
	}

	public void setIncidencia(Incidencia incidencia) {
		this.incidencia = incidencia;
	}
	
	public Solvencia2Excepcion(Incidencia incidencia) {
		super();
		this.incidencia = incidencia;
	}
	
	public Solvencia2Excepcion(String mensaje,Incidencia incidencia) {
		super(mensaje);
		this.incidencia = incidencia;
	}

	public Solvencia2Excepcion(Throwable cause,Incidencia incidencia) {
		super(cause);
		this.incidencia = incidencia;
	}

	public Solvencia2Excepcion(String mensaje,Incidencia incidencia, Throwable cause) {
		super(mensaje,cause);
		this.incidencia = incidencia;
	}

}
