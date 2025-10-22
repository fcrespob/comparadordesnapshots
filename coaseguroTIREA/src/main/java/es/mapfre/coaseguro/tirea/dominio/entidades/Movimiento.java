package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoMovimiento",
    "fecha",
    "PMA"
})
@XmlRootElement(name = "Movimiento")
public class Movimiento {

	
	private Integer TipoMovimiento;
	private String Fecha;
	private PMA PMA;
	
	public Movimiento() {
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

	public PMA getPMA() {
		return PMA;
	}

	@XmlElement(name = "PMA")
	public void setPMA(PMA pMA) {
		PMA = pMA;
	}
	
}
