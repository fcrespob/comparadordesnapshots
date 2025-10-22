package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPoliza",
    "datosPMA"
})
@XmlRootElement(name = "PMA")
public class PMA {

	private DatosPoliza DatosPoliza;
	private DatosPMA DatosPMA;

	public PMA() {
		super();
	}
	
	public DatosPoliza getDatosPoliza() {
		return DatosPoliza;
	}

	@XmlElement(name = "DatosPoliza")
	public void setDatosPoliza(DatosPoliza datosPoliza) {
		DatosPoliza = datosPoliza;
	}

	public DatosPMA getDatosPMA() {
		return DatosPMA;
	}

	@XmlElement(name = "DatosPMA")
	public void setDatosPMA(DatosPMA datosPMA) {
		DatosPMA = datosPMA;
	}

	
}
