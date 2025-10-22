package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "entidadAceptante",
    "participacionAceptante"
})
@XmlRootElement(name = "ParticipacionEntidad")
public class ParticipacionEntidad {

	
	private String EntidadAceptante;
	private String ParticipacionAceptante;
	
	public ParticipacionEntidad() {
		super();
	}
	
	public String getEntidadAceptante() {
		return EntidadAceptante;
	}

	@XmlElement(name = "EntidadAceptante")
	public void setEntidadAceptante(String entidadAceptante) {
		EntidadAceptante = entidadAceptante;
	}

	public String getParticipacionAceptante() {
		return ParticipacionAceptante;
	}

	@XmlElement(name = "ParticipacionAceptante")
	public void setParticipacionAceptante(String participacionAceptante) {
		ParticipacionAceptante = participacionAceptante;
	}
	
}

