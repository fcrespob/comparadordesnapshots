package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "revalorizacion"
})
@XmlRootElement(name = "DatosRevalorizaciones")
public class DatosRevalorizaciones {

	private List<Revalorizacion> Revalorizacion;

	public DatosRevalorizaciones() {
		super();
	}

	public List<Revalorizacion> getRevalorizacion() {
		return Revalorizacion;
	}

	@XmlElement(name = "Revalorizacion")
	public void setRevalorizacion(List<Revalorizacion> revalorizacion) {
		Revalorizacion = revalorizacion;
	}

	
}
