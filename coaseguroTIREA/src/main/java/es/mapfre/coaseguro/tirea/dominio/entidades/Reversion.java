package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "tipo",
    "formula",
    "fechaInicio",
    "fechaFin",
    "porcentaje",
    "importe",
    "beneficiarios"
})
@XmlRootElement(name = "Reversion")
public class Reversion {

	private String Tipo;
	private String Formula;
	private String FechaInicio;
	private String FechaFin;
	private String Porcentaje;
	private String Importe;
	private Beneficiarios Beneficiarios;

	public Reversion() {
		super();
	}
	
	public String getTipo() {
		return Tipo;
	}

	@XmlElement(name = "Tipo")
	public void setTipo(String tipo) {
		Tipo = tipo;
	}

	public String getFechaInicio() {
		return FechaInicio;
	}


	@XmlElement(name = "FechaInicio")
	public void setFechaInicio(String fechaInicio) {
		FechaInicio = fechaInicio;
	}

	public String getFechaFin() {
		return FechaFin;
	}

	@XmlElement(name = "FechaFin")
	public void setFechaFin(String fechaFin) {
		FechaFin = fechaFin;
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
	
	public Beneficiarios getBeneficiarios() {
		return Beneficiarios;
	}

	@XmlElement(name = "Beneficiarios")
	public void setBeneficiarios(Beneficiarios beneficiarios) {
		Beneficiarios = beneficiarios;
	}

	public String getFormula() {
		return Formula;
	}
	
	@XmlElement(name = "Formula")
	public void setFormula(String formula) {
		Formula = formula;
	}
	

}
