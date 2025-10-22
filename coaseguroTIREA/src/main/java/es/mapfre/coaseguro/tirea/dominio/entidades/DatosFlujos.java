package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosFlujo"
})
@XmlRootElement(name = "DatosFlujos")
public class DatosFlujos {
		
	private List<DatosFlujo> DatosFlujo;
	
	public DatosFlujos() {
		super();
	}

	public List<DatosFlujo> getDatosFlujo() {
		return DatosFlujo;
	}

	@XmlElement(name = "DatosFlujo")
	public void setDatosFlujo(List<DatosFlujo> datosFlujo) {
		DatosFlujo = datosFlujo;
	}

}
