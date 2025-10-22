package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab35050;

@Portable
public class Tab35050Key {

	@PortableProperty(Tab35050.IND_KRAMO)
	private String kramo;
	@PortableProperty(Tab35050.IND_KMODALIDAD)
	private Integer kmodalidad;

	public Tab35050Key(String kramo, Integer kmodalidad) {
		super();
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
	}
	
	public Tab35050Key() {
		super();
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
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
		Tab35050Key other = (Tab35050Key) obj;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		return true;
	}

	

}
