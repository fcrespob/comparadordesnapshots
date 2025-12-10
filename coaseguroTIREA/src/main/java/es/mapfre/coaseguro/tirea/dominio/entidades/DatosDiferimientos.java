package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "diferimiento"
})
@XmlRootElement(name = "DatosDiferimiento")
public class DatosDiferimientos {

	private List<Diferimiento> Diferimiento;

	public DatosDiferimientos() {
		super();
	}

	public List<Diferimiento> getDiferimiento() {
		return Diferimiento;
	}

	@XmlElement(name = "Diferimiento")
	public void setDiferimiento(List<Diferimiento> diferimiento) {
		Diferimiento = diferimiento;
	}

	
}
