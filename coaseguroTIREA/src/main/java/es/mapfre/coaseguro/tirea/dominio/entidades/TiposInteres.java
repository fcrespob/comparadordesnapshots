package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "interes"
})
@XmlRootElement(name = "TiposInteres")
public class TiposInteres {

	
	private List<Interes> Interes;

	public TiposInteres() {
		super();
	}
	
	public List<Interes> getInteres() {
		return Interes;
	}

	@XmlElement(name = "Interes")
	public void setInteres(List<Interes> interes) {
		Interes = interes;
	}

	
}
