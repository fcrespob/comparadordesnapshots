package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "reversion"
})
@XmlRootElement(name = "DatosReversion")
public class DatosReversion {

	private Reversion Reversion;

	public DatosReversion() {
		super();
	}

	public Reversion getReversion() {
		return Reversion;
	}

	@XmlElement(name = "Reversion")
	public void setReversion(Reversion reversion) {
		Reversion = reversion;
	}

	
}
