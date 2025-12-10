package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoMovimiento",
    "fecha",
    "PMD"
})
@XmlRootElement(name = "Movimiento")
public class MovimientoPMD {

	private Integer TipoMovimiento;
	private String Fecha;
	private PMD PMD;
	
	public MovimientoPMD() {
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

	public PMD getPMD() {
		return PMD;
	}

	@XmlElement(name = "PMD")
	public void setPMD(PMD pmd) {
		PMD = pmd;
	}
	
	
}
