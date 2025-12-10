package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "cabecera",
    "movimientodatosespecificos"
})
@XmlRootElement(name = "MovimientosCoaseguro")
public class MovimientosCoaseguroDatosEspecificos {

	private Cabecera cabecera;
	
	private List<MovimientoDatosEspecificos> movimientodatosespecificos;
	
	public MovimientosCoaseguroDatosEspecificos() {
		super();
	}

	 public Cabecera getCabecera() {
		return cabecera;
	}

	@XmlElement(name = "Cabecera")
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}
	
	public List<MovimientoDatosEspecificos> getMovimientodatosespecificos() {
		return movimientodatosespecificos;
	}
	
	@XmlElement(name = "Movimiento")
	public void setMovimientodatosespecificos(List<MovimientoDatosEspecificos> movimientodatosespecificos) {
		this.movimientodatosespecificos = movimientodatosespecificos;
	}

	

}
