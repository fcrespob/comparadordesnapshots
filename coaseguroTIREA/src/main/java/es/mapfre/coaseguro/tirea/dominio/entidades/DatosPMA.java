package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "basesTecnicas",
    "gastos"
})
@XmlRootElement(name = "DatosPMA")
public class DatosPMA {
	
	private BasesTecnicas BasesTecnicas;

	private Gastos Gastos;
	
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

	public Gastos getGastos() {
		return Gastos;
	}

	@XmlElement(name = "Gastos")
	public void setGastos(Gastos gastos) {
		Gastos = gastos;
	}


}
