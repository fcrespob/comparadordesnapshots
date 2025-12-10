package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "crecimiento"
})
@XmlRootElement(name = "DatosCrecimientos")
public class DatosCrecimientos {

	private List<Crecimiento> Crecimiento;

	public DatosCrecimientos() {
		super();
	}

	public List<Crecimiento> getCrecimiento() {
		return Crecimiento;
	}

	@XmlElement(name = "Crecimiento")
	public void setCrecimiento(List<Crecimiento> crecimiento) {
		Crecimiento = crecimiento;
	}

	
}
