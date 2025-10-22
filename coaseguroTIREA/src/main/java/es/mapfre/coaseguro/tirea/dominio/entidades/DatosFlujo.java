package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "fechaFlujo",
    "basesTecnicas"
})
@XmlRootElement(name = "DatosFlujo")
public class DatosFlujo {

	private String FechaFlujo;
	private BasesTecnicasPMF BasesTecnicas;
	
	public DatosFlujo() {
		super();
	}

	public BasesTecnicasPMF getBasesTecnicas() {
		return BasesTecnicas;
	}

	@XmlElement(name = "BasesTecnicas")
	public void setBasesTecnicas(BasesTecnicasPMF basesTecnicas) {
		BasesTecnicas = basesTecnicas;
	}

	public String getFechaFlujo() {
		return FechaFlujo;
	}

	@XmlElement(name = "FechaFlujo")
	public void setFechaFlujo(String fechaFlujo) {
		FechaFlujo = fechaFlujo;
	}
	
}
