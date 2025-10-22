package es.mapfre.solvencia.dominio.conversionesBel;

import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.conversionesBel.TablasExperienciaRealesKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class TablasExperienciaReales implements EntidadBase<TablasExperienciaRealesKey>{
	
	public static final int IND_FECCIERRE = 1;
	public static final int IND_KBASETEC = 2;
	public static final int IND_COMPANIA = 3;
	public static final int IND_CNEGOCIO = 4;
	public static final int IND_RIESGOACTUARIAL = 5;
	public static final int IND_SEXO = 6;
	public static final int IND_CATEGORIA = 7;
//	public static final int IND_EDadFija = 8;
//	public static final int IND_EDADHASTA = 9;
	public static final int IND_EDADFIJA = 8;

	public static final int IND_KMODALIDAD = 10;
	public static final int IND_TABLABASE = 11;
	public static final int IND_GENERACION = 12;
//	public static final int IND_ANOPOLIZA = 13;
//	public static final int IND_SOBRERRIESGO = 14;
//	public static final int IND_SOBREMORTALIDAD = 15;
	public static final int IND_Q = 16;
	public static final int IND_L = 17;
	
	@PortableProperty(IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_COMPANIA) private Integer compania;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_RIESGOACTUARIAL) private String riesgoActuarial;
	@PortableProperty(IND_SEXO) private String sexo;
	@PortableProperty(IND_CATEGORIA) private String categoria;
	@PortableProperty(IND_EDADFIJA) private Integer edadFija;
//	@PortableProperty(IND_EDadFija) private Integer edadFija;
//	@PortableProperty(IND_EDADHASTA) private Integer edadHasta;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_TABLABASE) private Integer tablaBase;
	@PortableProperty(IND_GENERACION) private Integer generacion;
//	@PortableProperty(IND_ANOPOLIZA) private Integer anoPoliza;
//	@PortableProperty(IND_SOBRERRIESGO) private BigDecimal sobrerriesgo;
//	@PortableProperty(IND_SOBREMORTALIDAD) private BigDecimal sobremortalidad;
	@PortableProperty(IND_Q) private List<java.math.BigDecimal> q;
	@PortableProperty(IND_L) private List<java.math.BigDecimal> l;
	public Timestamp getFecCierre() {
		return fecCierre;
	}
	public void setFecCierre(Timestamp fecCierre) {
		this.fecCierre = fecCierre;
	}
	public String getKbasetec() {
		return kbasetec;
	}
	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}
	public Integer getCompania() {
		return compania;
	}
	public void setCompania(Integer compania) {
		this.compania = compania;
	}
	public String getCnegocio() {
		return cnegocio;
	}
	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}
	public String getRiesgoActuarial() {
		return riesgoActuarial;
	}
	public void setRiesgoActuarial(String riesgoActuarial) {
		this.riesgoActuarial = riesgoActuarial;
	}
	public String getSexo() {
		return sexo;
	}
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	public String getCategoria() {
		return categoria;
	}
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
	public Integer getEdadFija() {
		return edadFija;
	}
	public void setEdadFija(Integer edadFija) {
		this.edadFija = edadFija;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public Integer getTablaBase() {
		return tablaBase;
	}
	public void setTablaBase(Integer tablaBase) {
		this.tablaBase = tablaBase;
	}
	public Integer getGeneracion() {
		return generacion;
	}
	public void setGeneracion(Integer generacion) {
		this.generacion = generacion;
	}
	public List<java.math.BigDecimal> getQ() {
		return q;
	}
	public void setQ(List<java.math.BigDecimal> q) {
		this.q = q;
	}
	public List<java.math.BigDecimal> getL() {
		return l;
	}
	public void setL(List<java.math.BigDecimal> l) {
		this.l = l;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((compania == null) ? 0 : compania.hashCode());
		result = prime * result
				+ ((edadFija == null) ? 0 : edadFija.hashCode());
		result = prime * result
				+ ((fecCierre == null) ? 0 : fecCierre.hashCode());
		result = prime * result
				+ ((generacion == null) ? 0 : generacion.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((l == null) ? 0 : l.hashCode());
		result = prime * result + ((q == null) ? 0 : q.hashCode());
		result = prime * result
				+ ((riesgoActuarial == null) ? 0 : riesgoActuarial.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result
				+ ((tablaBase == null) ? 0 : tablaBase.hashCode());
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
		TablasExperienciaReales other = (TablasExperienciaReales) obj;
		if (categoria == null) {
			if (other.categoria != null) {
				return false;
			}
		} else if (!categoria.equals(other.categoria)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (compania == null) {
			if (other.compania != null) {
				return false;
			}
		} else if (!compania.equals(other.compania)) {
			return false;
		}
		if (edadFija == null) {
			if (other.edadFija != null) {
				return false;
			}
		} else if (!edadFija.equals(other.edadFija)) {
			return false;
		}
		if (fecCierre == null) {
			if (other.fecCierre != null) {
				return false;
			}
		} else if (!fecCierre.equals(other.fecCierre)) {
			return false;
		}
		if (generacion == null) {
			if (other.generacion != null) {
				return false;
			}
		} else if (!generacion.equals(other.generacion)) {
			return false;
		}
		if (kbasetec == null) {
			if (other.kbasetec != null) {
				return false;
			}
		} else if (!kbasetec.equals(other.kbasetec)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (l == null) {
			if (other.l != null) {
				return false;
			}
		} else if (!l.equals(other.l)) {
			return false;
		}
		if (q == null) {
			if (other.q != null) {
				return false;
			}
		} else if (!q.equals(other.q)) {
			return false;
		}
		if (riesgoActuarial == null) {
			if (other.riesgoActuarial != null) {
				return false;
			}
		} else if (!riesgoActuarial.equals(other.riesgoActuarial)) {
			return false;
		}
		if (sexo == null) {
			if (other.sexo != null) {
				return false;
			}
		} else if (!sexo.equals(other.sexo)) {
			return false;
		}
		if (tablaBase == null) {
			if (other.tablaBase != null) {
				return false;
			}
		} else if (!tablaBase.equals(other.tablaBase)) {
			return false;
		}
		return true;
	}
	@Override
	public TablasExperienciaRealesKey getKey() {
		return new TablasExperienciaRealesKey(fecCierre, kbasetec, compania,
				cnegocio, riesgoActuarial, sexo, categoria, edadFija,
				kmodalidad, tablaBase, generacion);
	}
	@Override
	public String toString() {
		return "TablasExperienciaReales [fecCierre=" + fecCierre
				+ ", kbasetec=" + kbasetec + ", compania=" + compania
				+ ", cnegocio=" + cnegocio + ", riesgoActuarial="
				+ riesgoActuarial + ", sexo=" + sexo + ", categoria="
				+ categoria + ", edadFija=" + edadFija + ", kmodalidad="
				+ kmodalidad + ", tablaBase=" + tablaBase + ", generacion=" + generacion + "]";
	}
	
	
}