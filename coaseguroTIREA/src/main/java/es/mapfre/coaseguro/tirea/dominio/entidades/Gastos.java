package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "gasto"
})
@XmlRootElement(name = "Gastos")
public class Gastos {

	

	private List<Gasto> Gasto;
	
	public Gastos() {
		super();
	}
	
	public List<Gasto> getGasto() {
		return Gasto;
	}

	@XmlElement(name = "Gasto")
	public void setGasto(List<Gasto> gasto) {
		Gasto = gasto;
	}

	
}
