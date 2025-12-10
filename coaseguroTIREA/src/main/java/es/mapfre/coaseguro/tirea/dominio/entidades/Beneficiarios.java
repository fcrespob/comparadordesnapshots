package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "beneficiario"
})
@XmlRootElement(name = "Beneficiarios")
public class Beneficiarios {

	private List<Beneficiario> Beneficiario;

	public Beneficiarios() {
		super();
	}

	public List<Beneficiario> getBeneficiario() {
		return Beneficiario;
	}

	@XmlElement(name = "Beneficiario")
	public void setBeneficiario(List<Beneficiario> beneficiario) {
		Beneficiario = beneficiario;
	}

	
}
