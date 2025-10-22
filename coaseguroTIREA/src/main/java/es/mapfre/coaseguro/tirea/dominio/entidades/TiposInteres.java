package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "intereses"
})
@XmlRootElement(name = "TiposInteres")
public class TiposInteres {

	
	private List<Interes> Intereses;

	public TiposInteres() {
		super();
	}
	
	public List<Interes> getIntereses() {
		return Intereses;
	}

	@XmlElement(name = "Intereses")
	public void setIntereses(List<Interes> intereses) {
		Intereses = intereses;
	}

	
}
