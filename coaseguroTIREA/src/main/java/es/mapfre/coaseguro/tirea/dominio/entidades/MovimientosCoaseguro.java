package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "MovimientosCoaseguro")
@XmlType(propOrder = {    
	    "cabecera",
	    "movimiento"
	})
public class MovimientosCoaseguro {

	
	private Cabecera cabecera;
	
	private List<Movimiento> movimiento;
	
	public MovimientosCoaseguro() {
		super();
	}
	@XmlElement(name = "Cabecera")
	public Cabecera getCabecera() {
			return cabecera;
		}

	public void setCabecera(Cabecera cabecera1) {
		this.cabecera = cabecera1;
	}
	
	@XmlElement(name = "Movimiento")
	public List<Movimiento> getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(List<Movimiento> movimiento1) {
		this.movimiento = movimiento1;
	}

}
