package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "cabecera",
    "movimientopmd"
})
@XmlRootElement(name = "MovimientosCoaseguro")
public class MovimientosCoaseguroPMD {

	private Cabecera cabecera;
	
	private List<MovimientoPMD> movimientopmd;
	
	public MovimientosCoaseguroPMD() {
		super();
	}

	 public Cabecera getCabecera() {
		return cabecera;
	}

	@XmlElement(name = "Cabecera")
	public void setCabecera(Cabecera cabecera) {
		this.cabecera = cabecera;
	}
	
	public List<MovimientoPMD> getMovimientopmd() {
		return movimientopmd;
	}
	
	@XmlElement(name = "Movimiento")
	public void setMovimientopmd(List<MovimientoPMD> movimientopmd) {
		this.movimientopmd = movimientopmd;
	}

	

}
