package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoTablaExperiencia",
    "importeProvision",
    "tablas",
    "tiposInteres",
    "gastos"
})
@XmlRootElement(name = "BaseTecnica")
public class BaseTecnica {

	private String TipoTablaExperiencia;
	private String ImporteProvision;
	private Tablas Tablas;
	private TiposInteres TiposInteres;
	private Gastos Gastos;
	
	public BaseTecnica() {
		super();
	}
	
	public String getTipoTablaExperiencia() {
		return TipoTablaExperiencia;
	}
	
	@XmlElement(name = "TipoTablaExperiencia")
	public void setTipoTablaExperiencia(String tipoTablaExperiencia) {
		TipoTablaExperiencia = tipoTablaExperiencia;
	}
	public String getImporteProvision() {
		return ImporteProvision;
	}
	
	@XmlElement(name = "ImporteProvision")
	public void setImporteProvision(String importeProvision) {
		ImporteProvision = importeProvision;
	}
	public Tablas getTablas() {
		return Tablas;
	}
	
	@XmlElement(name = "Tablas")
	public void setTablas(Tablas tablas) {
		Tablas = tablas;
	}
	public TiposInteres getTiposInteres() {
		return TiposInteres;
	}
	
	@XmlElement(name = "TiposInteres")
	public void setTiposInteres(TiposInteres tiposInteres) {
		TiposInteres = tiposInteres;
	}
	
	public Gastos getGastos() {
		return Gastos;
	}

	@XmlElement(name = "Gastos")
	public void setGastos(Gastos gastos) {
		Gastos = gastos;
	}
}
