package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.MetodosAdaptacion;

@Portable
public class MetodosAdaptacionKey   {
	@PortableProperty(MetodosAdaptacion.IND_KMADAPTAC) private String kmadaptac;
	@PortableProperty(MetodosAdaptacion.IND_KFINICIO) private Timestamp kfinicio;
	public String getKmadaptac() {
		return kmadaptac;
	}
	public void setKmadaptac(String kmadaptac) {
		this.kmadaptac = kmadaptac;
	}
	public Timestamp getKfinicio() {
		return kfinicio;
	}
	public void setKfinicio(Timestamp kfinicio) {
		this.kfinicio = kfinicio;
	}
	public MetodosAdaptacionKey(String kmadaptac, Timestamp kfinicio) {
		super();
		this.kmadaptac = kmadaptac;
		this.kfinicio = kfinicio;
	}
	public MetodosAdaptacionKey() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kmadaptac == null) ? 0 : kmadaptac.hashCode());
		result = prime * result
				+ ((kfinicio == null) ? 0 : kfinicio.hashCode());
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
		MetodosAdaptacionKey other = (MetodosAdaptacionKey) obj;
		if (kfinicio == null) {
			if (other.kfinicio != null)
				return false;
		} else if (!kfinicio.equals(other.kfinicio))
			return false;
		if (kmadaptac == null) {
			if (other.kmadaptac != null)
				return false;
		} else if (!kmadaptac.equals(other.kmadaptac))
			return false;
		return true;
	}
	
		
	
}
