package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "participacionEntidad"
})
@XmlRootElement(name = "CuadroCoaseguro")
public class CuadroCoaseguro {

	private List<ParticipacionEntidad> ParticipacionEntidad;

	public CuadroCoaseguro() {
		super();
	}

	public List<ParticipacionEntidad> getParticipacionEntidad() {
		return ParticipacionEntidad;
	}

	@XmlElement(name = "ParticipacionEntidad")
	public void setParticipacionEntidad(List<ParticipacionEntidad> participacionEntidad) {
		ParticipacionEntidad = participacionEntidad;
	}

	
}
