package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "cabecera",
    "movimientopmf"
})
@XmlRootElement(name = "MovimientosCoaseguro")
public class MovimientosCoaseguroPMF {

	private Cabecera cabecera;
	
	private List<MovimientoPMF> movimientopmf;
	
	public MovimientosCoaseguroPMF() {
		super();
	}

	 public Cabecera getCabecera() {
		return cabecera;
	}

	@XmlElement(name = "Cabecera")
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}
	
	public List<MovimientoPMF> getMovimientopmf() {
		return movimientopmf;
	}
	
	@XmlElement(name = "Movimiento")
	public void setMovimientopmf(List<MovimientoPMF> movimientopmf) {
		this.movimientopmf = movimientopmf;
	}

	

}
