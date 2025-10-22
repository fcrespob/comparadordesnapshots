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
public class BasesTecnicasPMF {

	List<BaseTecnicaPMF> BaseTecnica;

	public BasesTecnicasPMF() {
		super();
	}
	
	public List<BaseTecnicaPMF> getBaseTecnica() {
		return BaseTecnica;
	}

	@XmlElement(name = "BaseTecnica")
	public void setBaseTecnica(List<BaseTecnicaPMF> baseTecnica) {
		BaseTecnica = baseTecnica;
	}
}
