package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;

@Portable
public class TablasExperienciaRealesKey {
	
	@PortableProperty(TablasExperienciaReales.IND_FECCIERRE) private Timestamp fecCierre;
	@PortableProperty(TablasExperienciaReales.IND_KBASETEC) private String kbasetec;
	@PortableProperty(TablasExperienciaReales.IND_COMPANIA) private Integer compania;
	@PortableProperty(TablasExperienciaReales.IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(TablasExperienciaReales.IND_RIESGOACTUARIAL) private String riesgoActuarial;
	@PortableProperty(TablasExperienciaReales.IND_SEXO) private String sexo;
	@PortableProperty(TablasExperienciaReales.IND_CATEGORIA) private String categoria;
	@PortableProperty(TablasExperienciaReales.IND_EDADFIJA) private Integer edadFija;
	@PortableProperty(TablasExperienciaReales.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(TablasExperienciaReales.IND_TABLABASE) private Integer tablaBase;
	@PortableProperty(TablasExperienciaReales.IND_GENERACION) private Integer generacion;
	
	
	
	
	public TablasExperienciaRealesKey(Timestamp fecCierre, String kbasetec,
			Integer compania, String cnegocio, String riesgoActuarial,
			String sexo, String categoria, Integer edadFija,
			Integer kmodalidad, Integer tablaBase,
			Integer generacion) {
		super();
		this.fecCierre = fecCierre;
		this.kbasetec = kbasetec;
		this.compania = compania;
		this.cnegocio = cnegocio;
		this.riesgoActuarial = riesgoActuarial;
		this.sexo = sexo;
		this.categoria = categoria;
		this.edadFija = edadFija;
		this.kmodalidad = kmodalidad;
		this.tablaBase = tablaBase;
		this.generacion = generacion;
	}




	public TablasExperienciaRealesKey() {
		super();
	}




	@Override
	public int hashCode() {
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
		result = prime * result
				+ ((riesgoActuarial == null) ? 0 : riesgoActuarial.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result
				+ ((tablaBase == null) ? 0 : tablaBase.hashCode());
		return result;
	}




	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		TablasExperienciaRealesKey other = (TablasExperienciaRealesKey) obj;
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
	
	

}