package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DefinicionesAuxiliares;

@Portable
public class DefinicionesAuxiliaresKey {
	
	@PortableProperty(DefinicionesAuxiliares.IND_KCARTEORIG) private Integer kcarteorig;
	@PortableProperty(DefinicionesAuxiliares.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(DefinicionesAuxiliares.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(DefinicionesAuxiliares.IND_KBASETEC) private String kbasetec;
	@PortableProperty(DefinicionesAuxiliares.IND_CIDENTIVARIAB) private String cidentivariab;
	
	public DefinicionesAuxiliaresKey(Integer carterainv, Integer kmodalidad, Integer kgarantia, String kbasetec, String cidentivariab) {
		super();
		this.kcarteorig = carterainv;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kbasetec = kbasetec;
		this.cidentivariab = cidentivariab;
	}
	
	public Integer getKcarteorig() {
		return kcarteorig;
	}

	public void setKcarteorig(Integer kcarteorig) {
		this.kcarteorig = kcarteorig;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public Integer getKgarantia() {
		return kgarantia;
	}
	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}
	public String getCidentivariab() {
		return cidentivariab;
	}
	public void setCidentivariab(String cidentivariab) {
		this.cidentivariab = cidentivariab;
	}
	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cidentivariab == null) ? 0 : cidentivariab.hashCode());
		result = prime * result + ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcarteorig == null) ? 0 : kcarteorig.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		DefinicionesAuxiliaresKey other = (DefinicionesAuxiliaresKey) obj;
		if (cidentivariab == null) {
			if (other.cidentivariab != null)
				return false;
		} else if (!cidentivariab.equals(other.cidentivariab))
			return false;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcarteorig == null) {
			if (other.kcarteorig != null)
				return false;
		} else if (!kcarteorig.equals(other.kcarteorig))
			return false;
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		return true;
	}

	public DefinicionesAuxiliaresKey() {
		super();
	}
	
}
