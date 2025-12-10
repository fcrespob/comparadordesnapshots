package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipo",
    "fechaInicio",
    "fechaFin"
})
@XmlRootElement(name = "Crecimiento")
public class Diferimiento {

	private String Tipo;
	private String FechaInicio;
	private String FechaFin;

	public Diferimiento() {
		super();
	}
	
	public String getTipo() {
		return Tipo;
	}

	@XmlElement(name = "Tipo")
	public void setTipo(String tipo) {
		Tipo = tipo;
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
