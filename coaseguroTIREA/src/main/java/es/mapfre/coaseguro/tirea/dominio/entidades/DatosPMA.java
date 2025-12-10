package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "basesTecnicas"
})
@XmlRootElement(name = "DatosPMA")
public class DatosPMA {
	
	private BasesTecnicas BasesTecnicas;
	
	public DatosPMA() {
		super();
	}

	public BasesTecnicas getBasesTecnicas() {
		return BasesTecnicas;
	}

	@XmlElement(name = "BasesTecnicas")
	public void setBasesTecnicas(BasesTecnicas basesTecnicas) {
		BasesTecnicas = basesTecnicas;
	}
}
