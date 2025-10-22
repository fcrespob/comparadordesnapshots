package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tabla"
})
@XmlRootElement(name = "Tablas")
public class Tablas {

	List<Tabla> Tabla;

	public Tablas() {
		super();
	}
	
	public List<Tabla> getTabla() {
		return Tabla;
	}

	@XmlElement(name = "Tabla")
	public void setTabla(List<Tabla> tabla) {
		Tabla = tabla;
	}
	
}
