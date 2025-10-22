package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoInteres",
    "fechaInicio",
    "fechaFin"
})
@XmlRootElement(name = "Interes")
public class Interes {

	private String TipoInteres;
	private String FechaInicio;
	private String FechaFin;
	
	public Interes() {
		super();
	}

	public String getTipoInteres() {
		return TipoInteres;
	}

	@XmlElement(name = "TipoInteres")
	public void setTipoInteres(String tipoInteres) {
		TipoInteres = tipoInteres;
	}

	public String getFechaInicio() {
		return FechaInicio;
	}

	@XmlElement(name = "FechaInicio")
	public void setFechaInicio(String fechaInicio) {
		FechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return FechaFin;
	}

	@XmlElement(name = "FechaFin")
	public void setFechaFin(String fechaFin) {
		FechaFin = fechaFin;
	}

	
	
}


