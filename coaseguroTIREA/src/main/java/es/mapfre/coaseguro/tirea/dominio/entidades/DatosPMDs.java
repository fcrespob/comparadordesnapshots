package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPMD"
})
@XmlRootElement(name = "DatosPMDs")
public class DatosPMDs {
		
	private List<DatosPMD> DatosPMD;
	
	public DatosPMDs() {
		super();
	}

	public List<DatosPMD> getDatosPMD() {
		return DatosPMD;
	}

	@XmlElement(name = "DatosPMD")
	public void setDatosPMD(List<DatosPMD> datosPMD) {
		DatosPMD = datosPMD;
	}

}
