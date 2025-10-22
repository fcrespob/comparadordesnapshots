package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TablaExperienciaKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class TablaExperiencia implements EntidadBase<TablaExperienciaKey> {
	
	public static final int IND_KTABLA = 0;
	public static final int IND_K2TIPOVALOR = 1;
	public static final int IND_KANACIMIENTO = 2;
	public static final int IND_KINTERES = 3;
	public static final int IND_KSOBREMORT = 4;
	public static final int IND_KSOBRERIES = 5;
	public static final int IND_GVALORESLEN = 6;
	public static final int IND_GVALOR = 7;
	public static final int IND_FANULACION = 8;
	public static final int IND_CUSUARIO = 9;
	public static final int IND_FMODIFICACION = 10;
	
	@PortableProperty(IND_KTABLA) private Integer ktabla;
	@PortableProperty(IND_K2TIPOVALOR) private String k2tipovalor;
	@PortableProperty(IND_KANACIMIENTO) private String kanacimiento;
	@PortableProperty(IND_KINTERES) private java.math.BigDecimal kinteres;
	@PortableProperty(IND_KSOBREMORT) private java.math.BigDecimal ksobremort;
	@PortableProperty(IND_KSOBRERIES) private java.math.BigDecimal ksobreries;
	@PortableProperty(IND_GVALORESLEN) private Integer gvaloreslen;
	@PortableProperty(IND_GVALOR) private List<java.math.BigDecimal> gvalor;
	@PortableProperty(IND_FANULACION) private Timestamp fanulacion;
	@PortableProperty(IND_CUSUARIO) private String cusuario;
	@PortableProperty(IND_FMODIFICACION) private Timestamp fmodificacion;
	

	public Integer getKtabla() {
		return ktabla;
	}

	public void setKtabla(Integer ktabla) {
		this.ktabla = ktabla;
	}

	public String getK2tipovalor() {
		return k2tipovalor;
	}

	public void setK2tipovalor(String k2tipovalor) {
		this.k2tipovalor = k2tipovalor;
	}

	public String getKanacimiento() {
		return kanacimiento;
	}

	public void setKanacimiento(String kanacimiento) {
		this.kanacimiento = kanacimiento;
	}

	public java.math.BigDecimal getKinteres() {
		return kinteres;
	}

	public void setKinteres(java.math.BigDecimal kinteres) {
		this.kinteres = kinteres;
	}

	public java.math.BigDecimal getKsobremort() {
		return ksobremort;
	}

	public void setKsobremort(java.math.BigDecimal ksobremort) {
		this.ksobremort = ksobremort;
	}

	public java.math.BigDecimal getKsobreries() {
		return ksobreries;
	}

	public void setKsobreries(java.math.BigDecimal ksobreries) {
		this.ksobreries = ksobreries;
	}

	public Integer getGvaloreslen() {
		return gvaloreslen;
	}

	public void setGvaloreslen(Integer gvaloreslen) {
		this.gvaloreslen = gvaloreslen;
	}

	public List<java.math.BigDecimal> getGvalor() {
		return gvalor;
	}

	public void setGvalor(List<java.math.BigDecimal> gvalor) {
		this.gvalor = gvalor;
	}

	public Timestamp getFanulacion() {
		return fanulacion;
	}

	public void setFanulacion(Timestamp fanulacion) {
		this.fanulacion = fanulacion;
	}

	public String getCusuario() {
		return cusuario;
	}

	public void setCusuario(String cusuario) {
		this.cusuario = cusuario;
	}

	public Timestamp getFmodificacion() {
		return fmodificacion;
	}

	public void setFmodificacion(Timestamp fmodificacion) {
		this.fmodificacion = fmodificacion;
	}


	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cusuario == null) ? 0 : cusuario.hashCode());
		result = prime * result
				+ ((fanulacion == null) ? 0 : fanulacion.hashCode());
		result = prime * result
				+ ((fmodificacion == null) ? 0 : fmodificacion.hashCode());
		result = prime * result + ((gvalor == null) ? 0 : gvalor.hashCode());
		result = prime * result
				+ ((gvaloreslen == null) ? 0 : gvaloreslen.hashCode());
		result = prime * result
				+ ((k2tipovalor == null) ? 0 : k2tipovalor.hashCode());
		result = prime * result
				+ ((kanacimiento == null) ? 0 : kanacimiento.hashCode());
		result = prime * result
				+ ((kinteres == null) ? 0 : kinteres.hashCode());
		result = prime * result
				+ ((ksobremort == null) ? 0 : ksobremort.hashCode());
		result = prime * result
				+ ((ksobreries == null) ? 0 : ksobreries.hashCode());
		result = prime * result + ((ktabla == null) ? 0 : ktabla.hashCode());
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
		TablaExperiencia other = (TablaExperiencia) obj;
		if (cusuario == null) {
			if (other.cusuario != null) {
				return false;
			}
		} else if (!cusuario.equals(other.cusuario)) {
			return false;
		}
		if (fanulacion == null) {
			if (other.fanulacion != null) {
				return false;
			}
		} else if (!fanulacion.equals(other.fanulacion)) {
			return false;
		}
		if (fmodificacion == null) {
			if (other.fmodificacion != null) {
				return false;
			}
		} else if (!fmodificacion.equals(other.fmodificacion)) {
			return false;
		}
		if (gvalor == null) {
			if (other.gvalor != null) {
				return false;
			}
		} else if (!gvalor.equals(other.gvalor)) {
			return false;
		}
		if (gvaloreslen == null) {
			if (other.gvaloreslen != null) {
				return false;
			}
		} else if (!gvaloreslen.equals(other.gvaloreslen)) {
			return false;
		}
		if (k2tipovalor == null) {
			if (other.k2tipovalor != null) {
				return false;
			}
		} else if (!k2tipovalor.equals(other.k2tipovalor)) {
			return false;
		}
		if (kanacimiento == null) {
			if (other.kanacimiento != null) {
				return false;
			}
		} else if (!kanacimiento.equals(other.kanacimiento)) {
			return false;
		}
		if (kinteres == null) {
			if (other.kinteres != null) {
				return false;
			}
		} else if (!kinteres.equals(other.kinteres)) {
			return false;
		}
		if (ksobremort == null) {
			if (other.ksobremort != null) {
				return false;
			}
		} else if (!ksobremort.equals(other.ksobremort)) {
			return false;
		}
		if (ksobreries == null) {
			if (other.ksobreries != null) {
				return false;
			}
		} else if (!ksobreries.equals(other.ksobreries)) {
			return false;
		}
		if (ktabla == null) {
			if (other.ktabla != null) {
				return false;
			}
		} else if (!ktabla.equals(other.ktabla)) {
			return false;
		}
		return true;
	}

	@Override
	public TablaExperienciaKey getKey() {
		return new TablaExperienciaKey(ktabla, k2tipovalor, kanacimiento, kinteres, ksobremort, ksobreries);
	}

	@Override
	public String toString() {
		return "TablaExperiencia [ktabla=" + ktabla + ", k2tipovalor="
				+ k2tipovalor + ", kanacimiento=" + kanacimiento
				+ ", kinteres=" + kinteres + ", ksobremort=" + ksobremort
				+ ", ksobreries=" + ksobreries + ", gvaloreslen=" + gvaloreslen
				+ "]";
	};
	
	
	
}