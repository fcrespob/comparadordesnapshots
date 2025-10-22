package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DefinicionesSolvencia2Key;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DefinicionesSolvencia2 implements EntidadBase<DefinicionesSolvencia2Key>{

	public static final int IND_KCARTEORIG = 0;
	public static final int IND_KRAMO = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_KGARANTIA = 3;
	public static final int IND_KESTADO = 4;
	public static final int IND_CINDCOL = 5;
	public static final int IND_SRESCATE = 6;
	public static final int IND_CNEGOCIO = 7;
	public static final int IND_CINVERSION = 8;
	public static final int IND_CRIESGO = 9;
	public static final int IND_CTIPOPROVI = 10;
	
	@PortableProperty(IND_KCARTEORIG) private Integer kcarteorig;
	@PortableProperty(IND_KRAMO) private Integer kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KESTADO) private Boolean kestado;
	@PortableProperty(IND_CINDCOL) private Boolean cindcol;
	@PortableProperty(IND_SRESCATE) private Boolean srescate;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CINVERSION) private String cinversion;
	@PortableProperty(IND_CRIESGO) private String criesgo;
	@PortableProperty(IND_CTIPOPROVI) private String ctipoprovi;
	
	public Integer getKcarteorig() {
		return kcarteorig;
	}
	public void setKcarteorig(Integer kcarteorig) {
		this.kcarteorig = kcarteorig;
	}
	public Integer getKramo() {
		return kcarteorig;
	}
	public void setKramo(Integer kramo) {
		this.kramo = kramo;
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
	public Boolean getKestado() {
		return kestado;
	}
	public void setKestado(Boolean kestado) {
		this.kestado = kestado;
	}
	public Boolean getCindcol() {
		return cindcol;
	}
	public void setCindcol(Boolean cindcol) {
		this.cindcol = cindcol;
	}
	public Boolean getSrescate() {
		return srescate;
	}
	public void setSrescate(Boolean srescate) {
		this.srescate = srescate;
	}
	public String getCnegocio() {
		return cnegocio;
	}
	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}
	public String getCinversion() {
		return cinversion;
	}
	public void setCinversion(String cinversion) {
		this.cinversion = cinversion;
	}
	public String getCriesgo() {
		return criesgo;
	}
	public void setCriesgo(String criesgo) {
		this.criesgo = criesgo;
	}
	public String getCtipoprovi() {
		return ctipoprovi;
	}
	public void setCtipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}
	@Override
	public DefinicionesSolvencia2Key getKey() {
		return new DefinicionesSolvencia2Key(kcarteorig, kmodalidad, kgarantia);
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cindcol == null) ? 0 : cindcol.hashCode());
		result = prime * result
				+ ((cinversion == null) ? 0 : cinversion.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((criesgo == null) ? 0 : criesgo.hashCode());
		result = prime * result
				+ ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result
				+ ((kcarteorig == null) ? 0 : kcarteorig.hashCode());
		result = prime * result
				+ ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((kestado == null) ? 0 : kestado.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((srescate == null) ? 0 : srescate.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		DefinicionesSolvencia2 other = (DefinicionesSolvencia2) obj;
		if (cindcol == null) {
			if (other.cindcol != null) {
				return false;
			}
		} else if (!cindcol.equals(other.cindcol)) {
			return false;
		}
		if (cinversion == null) {
			if (other.cinversion != null) {
				return false;
			}
		} else if (!cinversion.equals(other.cinversion)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (criesgo == null) {
			if (other.criesgo != null) {
				return false;
			}
		} else if (!criesgo.equals(other.criesgo)) {
			return false;
		}
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null) {
				return false;
			}
		} else if (!ctipoprovi.equals(other.ctipoprovi)) {
			return false;
		}
		if (kcarteorig == null) {
			if (other.kcarteorig != null) {
				return false;
			}
		} else if (!kcarteorig.equals(other.kcarteorig)) {
			return false;
		}
		if (kramo == null) {
			if (other.kramo != null) {
				return false;
			}
		} else if (!kramo.equals(other.kramo)) {
			return false;
		}
		if (kestado == null) {
			if (other.kestado != null) {
				return false;
			}
		} else if (!kestado.equals(other.kestado)) {
			return false;
		}
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (srescate == null) {
			if (other.srescate != null) {
				return false;
			}
		} else if (!srescate.equals(other.srescate)) {
			return false;
		}
		return true;
	}
}