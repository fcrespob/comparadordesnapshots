package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "cabecera",
    "movimientorentasirregulares"
})
@XmlRootElement(name = "MovimientosCoaseguro")
public class MovimientosCoaseguroRentasIrregulares {

	private Cabecera cabecera;
	
	private List<MovimientoRentasIrregulares> movimientorentasirregulares;
	
	public MovimientosCoaseguroRentasIrregulares() {
		super();
	}

	 public Cabecera getCabecera() {
		return cabecera;
	}

	@XmlElement(name = "Cabecera")
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}
	
	public List<MovimientoRentasIrregulares> getMovimientorentasirregulares() {
		return movimientorentasirregulares;
	}
	
	@XmlElement(name = "Movimiento")
	public void setMovimientorentasirregulares(List<MovimientoRentasIrregulares> movimientorentasirregulares) {
		this.movimientorentasirregulares = movimientorentasirregulares;
	}

	

}
