package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesAuxiliaresKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DefinicionesAuxiliares implements EntidadBase<DefinicionesAuxiliaresKey>{
	
	public static final int IND_KCARTEORIG = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_KGARANTIA = 2;
	public static final int IND_KBASETEC = 3;
	public static final int IND_CIDENTIVARIAB = 4;
	public static final int IND_CNEGOCIO = 5;
	public static final int IND_GVARIABLE = 6;
	public static final int IND_GVALOR = 7;
	
	
	@PortableProperty(IND_KCARTEORIG) private Integer kcarteorig;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_CIDENTIVARIAB) private String cidentivariab;
	@PortableProperty(IND_GVARIABLE) private String gvariable;
	@PortableProperty(IND_GVALOR) private String gvalor;
	
	public Integer getKcarteorig() {
		return kcarteorig;
	}
	public void setKcarteorig(Integer kcarteorig) {
		this.kcarteorig = kcarteorig;
	}
	public String getCnegocio() {
		return cnegocio;
	}
	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
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
	public String getGvariable() {
		return gvariable;
	}
	public void setGvariable(String gvariable) {
		this.gvariable = gvariable;
	}
	public String getGvalor() {
		return gvalor;
	}
	public void setGvalor(String gvalor) {
		this.gvalor = gvalor;
	}
	public String getKbasetec() {
		return kbasetec;
	}
	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}
	
	@Override
	public DefinicionesAuxiliaresKey getKey() {
		return new DefinicionesAuxiliaresKey(kcarteorig, kmodalidad, kgarantia, kbasetec, cidentivariab);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cidentivariab == null) ? 0 : cidentivariab.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((gvalor == null) ? 0 : gvalor.hashCode());
		result = prime * result + ((gvariable == null) ? 0 : gvariable.hashCode());
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
		DefinicionesAuxiliares other = (DefinicionesAuxiliares) obj;
		if (cidentivariab == null) {
			if (other.cidentivariab != null)
				return false;
		} else if (!cidentivariab.equals(other.cidentivariab))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (gvalor == null) {
			if (other.gvalor != null)
				return false;
		} else if (!gvalor.equals(other.gvalor))
			return false;
		if (gvariable == null) {
			if (other.gvariable != null)
				return false;
		} else if (!gvariable.equals(other.gvariable))
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
	@Override
	public String toString() {
		return "DefinicionesAuxiliares [kcarteorig=" + kcarteorig + ", cnegocio=" + cnegocio + ", kmodalidad="
				+ kmodalidad + ", kgarantia=" + kgarantia + ", kbasetec=" + kbasetec + ", cidentivariab="
				+ cidentivariab + ", gvariable=" + gvariable + ", gvalor=" + gvalor + "]";
	}
	
	
	
}