package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "MovimientosCoaseguro")
@XmlType(propOrder = {    
	    "fecha",
	    "idEmisor",
	    "idReceptor",
	    "idFichero"
	})
public class Cabecera {
	
	private String Fecha;
	private String IdEmisor;
	private String IdReceptor;
	private String IdFichero;
	
	public Cabecera() {
		super();
	}
	
	@XmlElement(name = "Fecha")
	public String getFecha() {
		return Fecha;
	}
	
	public void setFecha(String fecha) {
		Fecha = fecha;
	}
	
	@XmlElement(name = "IdEmisor")
	public String getIdEmisor() {
		return IdEmisor;
	}
	
	public void setIdEmisor(String idEmisor) {
		IdEmisor = idEmisor;
	}
	
	@XmlElement(name = "IdReceptor")
	public String getIdReceptor() {
		return IdReceptor;
	}
	
	public void setIdReceptor(String idReceptor) {
		IdReceptor = idReceptor;
	}
	
	@XmlElement(name = "IdFichero")
	public String getIdFichero() {
		return IdFichero;
	}
	
	public void setIdFichero(String idFichero) {
		IdFichero = idFichero;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((IdEmisor == null) ? 0 : IdEmisor.hashCode());
		result = prime * result + ((IdFichero == null) ? 0 : IdFichero.hashCode());
		result = prime * result + ((IdReceptor == null) ? 0 : IdReceptor.hashCode());
		result = prime * result + ((Fecha == null) ? 0 : Fecha.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cabecera other = (Cabecera) obj;
		if (IdEmisor == null) {
			if (other.IdEmisor != null)
				return false;
		} else if (!IdEmisor.equals(other.IdEmisor))
			return false;
		if (IdFichero == null) {
			if (other.IdFichero != null)
				return false;
		} else if (!IdFichero.equals(other.IdFichero))
			return false;
		if (IdReceptor == null) {
			if (other.IdReceptor != null)
				return false;
		} else if (!IdReceptor.equals(other.IdReceptor))
			return false;
		if (Fecha == null) {
			if (other.Fecha != null)
				return false;
		} else if (!Fecha.equals(other.Fecha))
			return false;
		return true;
	}
	
}
