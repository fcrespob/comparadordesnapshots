package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipoTablaExperiencia",
    "tipoInteres",
    "importeFlujo",
    "importeFlujoGastos"
})
@XmlRootElement(name = "BaseTecnica")
public class BaseTecnicaPMF {

	private String TipoTablaExperiencia;
	private String TipoInteres;
	private String ImporteFlujo;
	private String ImporteFlujoGastos;
	
	public BaseTecnicaPMF() {
		super();
	}
	
	public String getTipoTablaExperiencia() {
		return TipoTablaExperiencia;
	}
	
	@XmlElement(name = "TipoTablaExperiencia")
	public void setTipoTablaExperiencia(String tipoTablaExperiencia) {
		TipoTablaExperiencia = tipoTablaExperiencia;
	}
	
	public String getTipoInteres() {
		return TipoInteres;
	}

	@XmlElement(name = "TipoInteres")
	public void setTipoInteres(String tipoInteres) {
		TipoInteres = tipoInteres;
	}

	public String getImporteFlujo() {
		return ImporteFlujo;
	}
	
	@XmlElement(name = "ImporteFlujo")
	public void setImporteFlujo(String importeFlujo) {
		ImporteFlujo = importeFlujo;
	}

	public String getImporteFlujoGastos() {
		return ImporteFlujoGastos;
	}

	@XmlElement(name = "ImporteFlujoGastos")
	public void setImporteFlujoGastos(String importeFlujoGastos) {
		ImporteFlujoGastos = importeFlujoGastos;
	}
	
}
