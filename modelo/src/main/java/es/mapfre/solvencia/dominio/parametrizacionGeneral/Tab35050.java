package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.Tab35050Key;
import es.mapfre.solvencia.dominio.EntidadBase;
@Portable
public class Tab35050  implements EntidadBase<Tab35050Key> {

	public static final int IND_KRAMO = 0; 
	public static final int IND_KMODALIDAD = 1;

	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	
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
		Tab35050 other = (Tab35050) obj;
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
	@Override
	public String toString() {
		return "Tab35050 [kmodalidad=" + kmodalidad + ", kramo=" + kramo + "]";
	}
	@Override
	public Tab35050Key getKey() {
		return new Tab35050Key(kramo, kmodalidad);
	}
}
