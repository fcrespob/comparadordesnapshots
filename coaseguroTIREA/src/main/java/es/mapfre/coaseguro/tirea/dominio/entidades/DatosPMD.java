package es.mapfre.coaseguro.tirea.dominio.entidades;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "idAplicacion",
    "idAplicacionOrigen",
    "fechaNacimiento",
    "sexo",
    "estado",
    "tablas",
    "tiposInteres",
    "gastos",
    "garantiasPrestaciones"
})
@XmlRootElement(name = "DatosPMD")
public class DatosPMD {

	private String IdAplicacion;
	private String IdAplicacionOrigen;
	private String FechaNacimiento;
	private String Sexo;
	private String Estado;
	private Tablas Tablas;
	private Gastos Gastos;
	private TiposInteres TiposInteres;
	private GarantiasPrestaciones GarantiasPrestaciones;
	
	
	public String getIdAplicacion() {
		return IdAplicacion;
	}

	@XmlElement(name = "IdAplicacion")
	public void setIdAplicacion(String idAplicacion) {
		IdAplicacion = idAplicacion;
	}

	public String getIdAplicacionOrigen() {
		return IdAplicacionOrigen;
	}

	@XmlElement(name = "IdAplicacionOrigen")
	public void setIdAplicacionOrigen(String idAplicacionOrigen) {
		IdAplicacionOrigen = idAplicacionOrigen;
	}

	public String getFechaNacimiento() {
		return FechaNacimiento;
	}

	@XmlElement(name = "FechaNacimiento")
	public void setFechaNacimiento(String fechaNacimiento) {
		FechaNacimiento = fechaNacimiento;
	}

	public String getEstado() {
		return Estado;
	}

	@XmlElement(name = "Estado")
	public void setEstado(String estado) {
		Estado = estado;
	}
	
	public String getSexo() {
		return Sexo;
	}

	@XmlElement(name = "Sexo")
	public void setSexo(String sexo) {
		Sexo = sexo;
	}

	public Tablas getTablas() {
		return Tablas;
	}

	@XmlElement(name = "Tablas")
	public void setTablas(Tablas tablas) {
		Tablas = tablas;
	}

	public Gastos getGastos() {
		return Gastos;
	}

	@XmlElement(name = "Gastos")
	public void setGastos(Gastos gastos) {
		Gastos = gastos;
	}

	public TiposInteres getTiposInteres() {
		return TiposInteres;
	}

	@XmlElement(name = "TiposInteres")
	public void setTiposInteres(TiposInteres tiposInteres) {
		TiposInteres = tiposInteres;
	}

	public GarantiasPrestaciones getGarantiasPrestaciones() {
		return GarantiasPrestaciones;
	}

	@XmlElement(name = "GarantiasPrestaciones")
	public void setGarantiasPrestaciones(GarantiasPrestaciones garantiasPrestaciones) {
		GarantiasPrestaciones = garantiasPrestaciones;
	}

	public DatosPMD() {
		super();
	}
	
}
