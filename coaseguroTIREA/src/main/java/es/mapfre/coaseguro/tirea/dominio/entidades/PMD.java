package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPoliza",
    "datosPMDs"
})
@XmlRootElement(name = "PMD")
public class PMD {

	private DatosPoliza DatosPoliza;
	private DatosPMDs DatosPMDs;

	public PMD() {
		super();
	}
	
	public DatosPoliza getDatosPoliza() {
		return DatosPoliza;
	}

	@XmlElement(name = "DatosPoliza")
	public void setDatosPoliza(DatosPoliza datosPoliza) {
		DatosPoliza = datosPoliza;
	}

	public DatosPMDs getDatosPMDs() {
		return DatosPMDs;
	}
	
	@XmlElement(name = "DatosPMDs")
	public void setDatosPMDs(DatosPMDs datosPMDs) {
		DatosPMDs = datosPMDs;
	}
}
