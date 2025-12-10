package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPoliza",
    "variables"
})
@XmlRootElement(name = "DatosEspecificos")
public class DatosEspecificos {

	private DatosPoliza DatosPoliza;
	private Variables Variables;

	public DatosEspecificos() {
		super();
	}
	
	public DatosPoliza getDatosPoliza() {
		return DatosPoliza;
	}

	@XmlElement(name = "DatosPoliza")
	public void setDatosPoliza(DatosPoliza datosPoliza) {
		DatosPoliza = datosPoliza;
	}

	public Variables getVariables() {
		return Variables;
	}
	
	@XmlElement(name = "Variables")
	public void setVariables(Variables variables) {
		Variables = variables;
	}
}
