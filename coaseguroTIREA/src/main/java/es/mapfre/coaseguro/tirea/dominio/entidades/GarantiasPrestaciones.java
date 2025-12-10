package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "garantiaPrestacion"
})
@XmlRootElement(name = "DatosPMD")
public class GarantiasPrestaciones {

	private GarantiaPrestacion GarantiaPrestacion;

	public GarantiaPrestacion getGarantiaPrestacion() {
		return GarantiaPrestacion;
	}

	@XmlElement(name = "GarantiaPrestacion")
	public void setGarantiaPrestacion(GarantiaPrestacion garantiaPrestacion) {
		GarantiaPrestacion = garantiaPrestacion;
	}

	public GarantiasPrestaciones() {
		super();
	}
	
}
