package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "entidadAbridora",
    "participacionAbridora",
    "numeroPoliza",
    "numeroSuplemento",
    "cuadroCoaseguro",
    "fechaEfecto",
    "metodoSuplemento"
})
@XmlRootElement(name = "DatosPoliza")
public class DatosPoliza {
	
	private String EntidadAbridora;
	private String ParticipacionAbridora;
	private String NumeroPoliza;
	private String NumeroSuplemento;
	private CuadroCoaseguro CuadroCoaseguro;
	private String FechaEfecto;
	private String MetodoSuplemento;
	
	public DatosPoliza() {
		super();
	}
	
	public String getEntidadAbridora() {
		return EntidadAbridora;
	}

	@XmlElement(name = "EntidadAbridora")
	public void setEntidadAbridora(String entidadAbridora) {
		EntidadAbridora = entidadAbridora;
	}


	public String getParticipacionAbridora() {
		return ParticipacionAbridora;
	}

	@XmlElement(name = "ParticipacionAbridora")
	public void setParticipacionAbridora(String participacionAbridora) {
		ParticipacionAbridora = participacionAbridora;
	}


	public String getNumeroPoliza() {
		return NumeroPoliza;
	}

	@XmlElement(name = "NumeroPoliza")
	public void setNumeroPoliza(String numeroPoliza) {
		NumeroPoliza = numeroPoliza;
	}


	public String getNumeroSuplemento() {
		return NumeroSuplemento;
	}

	@XmlElement(name = "NumeroSuplemento")
	public void setNumeroSuplemento(String numeroSuplemento) {
		NumeroSuplemento = numeroSuplemento;
	}


	public CuadroCoaseguro getCuadroCoaseguro() {
		return CuadroCoaseguro;
	}

	@XmlElement(name = "CuadroCoaseguro")
	public void setCuadroCoaseguro(CuadroCoaseguro cuadroCoaseguro) {
		CuadroCoaseguro = cuadroCoaseguro;
	}


	public String getFechaEfecto() {
		return FechaEfecto;
	}

	@XmlElement(name = "FechaEfecto")
	public void setFechaEfecto(String fechaEfecto) {
		FechaEfecto = fechaEfecto;
	}


	public String getMetodoSuplemento() {
		return MetodoSuplemento;
	}

	@XmlElement(name = "MetodoSuplemento")
	public void setMetodoSuplemento(String metodoSuplemento) {
		MetodoSuplemento = metodoSuplemento;
	}

	
}
