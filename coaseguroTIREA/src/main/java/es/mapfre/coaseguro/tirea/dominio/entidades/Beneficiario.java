package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "fechaNacimiento",
    "sexo",
    "estado",
    "porcentaje",
    "importe",
    "parentesco",
    "orfandad",
    "limiteOrfandad"
})
@XmlRootElement(name = "Beneficiario")
public class Beneficiario {

	private String FechaNacimiento;
	private String Sexo;
	private String Estado;
	private String Porcentaje;
	private String Importe;
	private String Parentesco;
	private String Orfandad;
	private String LimiteOrfandad;

	public Beneficiario() {
		super();
	}

	public String getFechaNacimiento() {
		return FechaNacimiento;
	}

	@XmlElement(name = "FechaNacimiento")
	public void setFechaNacimiento(String fechaNacimiento) {
		FechaNacimiento = fechaNacimiento;
	}

	public String getSexo() {
		return Sexo;
	}

	@XmlElement(name = "Sexo")
	public void setSexo(String sexo) {
		Sexo = sexo;
	}

	public String getEstado() {
		return Estado;
	}

	@XmlElement(name = "Estado")
	public void setEstado(String estado) {
		Estado = estado;
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

	public String getParentesco() {
		return Parentesco;
	}

	@XmlElement(name = "Parentesco")
	public void setParentesco(String parentesco) {
		Parentesco = parentesco;
	}

	public String getLimiteOrfandad() {
		return LimiteOrfandad;
	}

	@XmlElement(name = "LimiteOrfandad")
	public void setLimiteOrfandad(String limiteOrfandad) {
		LimiteOrfandad = limiteOrfandad;
	}
	
	public String getOrfandad() {
		return Orfandad;
	}

	@XmlElement(name = "Orfandad")
	public void setOrfandad(String orfandad) {
		Orfandad = orfandad;
	}

}
