package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "concepto",
    "base",
    "porcentaje",
    "importe"
})
@XmlRootElement(name = "Gasto")
public class Gasto {

	
	private String Concepto;
	private String Base;
	private String Porcentaje;
	private String Importe;
	
	public Gasto() {
		super();
	}
	
	public String getConcepto() {
		return Concepto;
	}

	@XmlElement(name = "Concepto")
	public void setConcepto(String concepto) {
		Concepto = concepto;
	}

	public String getBase() {
		return Base;
	}

	@XmlElement(name = "Base")
	public void setBase(String base) {
		Base = base;
	}

	public String getPorcentaje() {
		return Porcentaje;
	}

	@XmlElement(name = "Porcentaje")
	public void setPorcentaje(String porcentaje) {
		Porcentaje = porcentaje;
	}

	public String getImporte() {
		return Importe;
	}

	@XmlElement(name = "Importe")
	public void setImporte(String importe) {
		Importe = importe;
	}

	
}
