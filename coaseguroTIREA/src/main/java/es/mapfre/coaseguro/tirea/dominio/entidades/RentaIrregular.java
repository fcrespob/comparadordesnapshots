package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "datosPoliza",
    "pagosPlanificados"
})
@XmlRootElement(name = "RentaIrregular")
public class RentaIrregular {

	private DatosPoliza DatosPoliza;
	private PagosPlanificados PagosPlanificados;

	public RentaIrregular() {
		super();
	}
	
	public DatosPoliza getDatosPoliza() {
		return DatosPoliza;
	}

	@XmlElement(name = "DatosPoliza")
	public void setDatosPoliza(DatosPoliza datosPoliza) {
		DatosPoliza = datosPoliza;
	}

	public PagosPlanificados getPagosPlanificados() {
		return PagosPlanificados;
	}
	
	@XmlElement(name = "PagosPlanificados")
	public void setPagosPlanificados(PagosPlanificados pagosPlanificados) {
		PagosPlanificados = pagosPlanificados;
	}
}
