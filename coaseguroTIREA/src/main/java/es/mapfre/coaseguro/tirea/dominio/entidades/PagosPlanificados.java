package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlType(propOrder = {    
    "pagoPlanificado"
})
@XmlRootElement(name = "PagosPlanificados")
public class PagosPlanificados {
		
	private List<PagoPlanificado> PagoPlanificado;
	
	public PagosPlanificados() {
		super();
	}

	public List<PagoPlanificado> getPagoPlanificado() {
		return PagoPlanificado;
	}

	@XmlElement(name = "PagoPlanificado")
	public void setPagoPlanificado(List<PagoPlanificado> pagoPlanificado) {
		PagoPlanificado = pagoPlanificado;
	}

}
