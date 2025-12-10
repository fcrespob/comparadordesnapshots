package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
	"idAplicacion",
    "codigo",
    "valor"
})
@XmlRootElement(name = "Variable")
public class Variable {

	private String IdAplicacion;
	private String Codigo;
	private String Valor;
	
	
	public String getCodigo() {
		return Codigo;
	}

	@XmlElement(name = "Codigo")
	public void setCodigo(String codigo) {
		Codigo = codigo;
	}

	public String getValor() {
		return Valor;
	}

	@XmlElement(name = "Valor")
	public void setValor(String valor) {
		Valor = valor;
	}
	
	public String getIdAplicacion() {
		return IdAplicacion;
	}

	@XmlElement(name = "IdAplicacion")
	public void setIdAplicacion(String idAplicacion) {
		IdAplicacion = idAplicacion;
	}

	public Variable() {
		super();
	}
	
}
