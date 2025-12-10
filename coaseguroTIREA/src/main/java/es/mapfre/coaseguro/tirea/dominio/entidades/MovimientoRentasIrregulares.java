package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(propOrder = {    
    "tipoMovimiento",
    "fecha",
    "rentaIrregular"
})
@XmlRootElement(name = "MovimientoRentasIrregulares")
public class MovimientoRentasIrregulares {

	private Integer TipoMovimiento;
	private String Fecha;
	private RentaIrregular RentaIrregular;
	
	public MovimientoRentasIrregulares() {
		super();
	}
	
	public Integer getTipoMovimiento() {
		return TipoMovimiento;
	}

	@XmlElement(name = "TipoMovimiento")
	public void setTipoMovimiento(Integer tipoMovimiento) {
		TipoMovimiento = tipoMovimiento;
	}

	public String getFecha() {
		return Fecha;
	}

	@XmlElement(name = "Fecha")
	public void setFecha(String fecha) {
		Fecha = fecha;
	}

	public RentaIrregular getRentaIrregular() {
		return RentaIrregular;
	}

	@XmlElement(name = "RentaIrregular")
	public void setRentaIrregular(RentaIrregular rentaIrregular) {
		RentaIrregular = rentaIrregular;
	}
	
}
