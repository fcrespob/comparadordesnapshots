package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPoliza",
    "datosFlujos"
})
@XmlRootElement(name = "PMF")
public class PMF {

	private DatosPoliza DatosPoliza;
	private DatosFlujos DatosFlujos;

	public PMF() {
		super();
	}
	
	public DatosPoliza getDatosPoliza() {
		return DatosPoliza;
	}

	@XmlElement(name = "DatosPoliza")
	public void setDatosPoliza(DatosPoliza datosPoliza) {
		DatosPoliza = datosPoliza;
	}

	public DatosFlujos getDatosFlujos() {
		return DatosFlujos;
	}
	
	@XmlElement(name = "DatosFlujos")
	public void setDatosFlujos(DatosFlujos datosFlujos) {
		DatosFlujos = datosFlujos;
	}
}
