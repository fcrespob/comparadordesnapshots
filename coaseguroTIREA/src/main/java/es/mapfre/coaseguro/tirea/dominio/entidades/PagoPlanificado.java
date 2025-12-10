package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "idAplicacion",
    "idGarantia",
    "idGarantiaPrincipal",
    "codGarantia",
    "fechaEfecto",
    "importeBruto",
    "fechaInicio",
    "fechaFin"
})
@XmlRootElement(name = "PagoPlanificado")
public class PagoPlanificado {

	private String IdAplicacion;
	private String IdGarantia;
	private String IdGarantiaPrincipal;
	private String CodGarantia;
	private String FechaEfecto;
	private String ImporteBruto;
	private String FechaInicio;
	private String FechaFin;
	
	
	public String getIdAplicacion() {
		return IdAplicacion;
	}

	@XmlElement(name = "IdAplicacion")
	public void setIdAplicacion(String idAplicacion) {
		IdAplicacion = idAplicacion;
	}

	public String getIdGarantia() {
		return IdGarantia;
	}

	@XmlElement(name = "IdGarantia")
	public void setIdGarantia(String idGarantia) {
		IdGarantia = idGarantia;
	}

	public String getIdGarantiaPrincipal() {
		return IdGarantiaPrincipal;
	}

	@XmlElement(name = "IdGarantiaPrincipal")
	public void setIdGarantiaPrincipal(String idGarantiaPrincipal) {
		IdGarantiaPrincipal = idGarantiaPrincipal;
	}

	public String getCodGarantia() {
		return CodGarantia;
	}

	@XmlElement(name = "CodGarantia")
	public void setCodGarantia(String codGarantia) {
		CodGarantia = codGarantia;
	}

	public String getFechaEfecto() {
		return FechaEfecto;
	}

	@XmlElement(name = "FechaEfecto")
	public void setFechaEfecto(String fechaEfecto) {
		FechaEfecto = fechaEfecto;
	}

	public String getImporteBruto() {
		return ImporteBruto;
	}

	@XmlElement(name = "ImporteBruto")
	public void setImporteBruto(String importeBruto) {
		ImporteBruto = importeBruto;
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

	public PagoPlanificado() {
		super();
	}
	
}
