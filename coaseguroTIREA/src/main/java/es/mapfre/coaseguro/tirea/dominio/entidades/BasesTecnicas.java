package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "baseTecnica"
})
@XmlRootElement(name = "BasesTecnicas")
public class BasesTecnicas {

	List<BaseTecnica> BaseTecnica;

	public BasesTecnicas() {
		super();
	}
	
	public List<BaseTecnica> getBaseTecnica() {
		return BaseTecnica;
	}

	@XmlElement(name = "BaseTecnica")
	public void setBaseTecnica(List<BaseTecnica> baseTecnica) {
		BaseTecnica = baseTecnica;
	}
}
