package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "codigoTabla",
    "pjeTabla",
    "desplazamiento",
    "pjeUnisexM",
    "pjeUnisexF"
})
@XmlRootElement(name = "Tabla")
public class Tabla {

	private String CodigoTabla;
	private String PjeTabla;
	private String Desplazamiento;
	private String PjeUnisexM;
	private String PjeUnisexF;
	
	public Tabla() {
		super();
	}
	
	public String getCodigoTabla() {
		return CodigoTabla;
	}
	
	@XmlElement(name = "CodigoTabla")
	public void setCodigoTabla(String codigoTabla) {
		CodigoTabla = codigoTabla;
	}
	public String getPjeTabla() {
		return PjeTabla;
	}
	
	@XmlElement(name = "PjeTabla")
	public void setPjeTabla(String pjeTabla) {
		PjeTabla = pjeTabla;
	}
	public String getDesplazamiento() {
		return Desplazamiento;
	}
	
	@XmlElement(name = "Desplazamiento")
	public void setDesplazamiento(String desplazamiento) {
		Desplazamiento = desplazamiento;
	}
	
	public String getPjeUnisexM() {
		return PjeUnisexM;
	}
	
	@XmlElement(name = "PjeUnisexM")
	public void setPjeUnisexM(String pjeUnisexM) {
		PjeUnisexM = pjeUnisexM;
	}

	public String getPjeUnisexF() {
		return PjeUnisexF;
	}
	@XmlElement(name = "PjeUnisexF")
	public void setPjeUnisexF(String pjeUnisexF) {
		PjeUnisexF = pjeUnisexF;
	}
	
}
