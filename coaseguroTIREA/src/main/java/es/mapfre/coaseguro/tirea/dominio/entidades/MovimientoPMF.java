package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoMovimiento",
    "fecha",
    "PMF"
})
@XmlRootElement(name = "Movimiento")
public class MovimientoPMF {

	private Integer TipoMovimiento;
	private String Fecha;
	private PMF PMF;
	
	public MovimientoPMF() {
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

	public PMF getPMF() {
		return PMF;
	}

	@XmlElement(name = "PMF")
	public void setPMF(PMF pmf) {
		PMF = pmf;
	}
	
	
}
