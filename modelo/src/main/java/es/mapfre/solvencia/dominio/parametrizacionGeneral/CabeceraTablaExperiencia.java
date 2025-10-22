package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CabeceraTablaExperienciaKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class CabeceraTablaExperiencia implements EntidadBase<CabeceraTablaExperienciaKey>{
	
	public static final int IND_KTABLA = 0;
	public static final int IND_K2TIPOTABLA = 1;
	public static final int IND_GCORTA = 2;
	public static final int IND_GDESLARGA = 3;
	public static final int IND_FECALTA = 4;
	public static final int IND_FECANULA = 5;
	public static final int IND_CUSUARIO = 6;
	public static final int IND_FECMODIF = 7;

	@PortableProperty(IND_KTABLA) private Integer ktabla;
	@PortableProperty(IND_K2TIPOTABLA) private String k2tipotabla;
	@PortableProperty(IND_GCORTA) private String gcorta;
	@PortableProperty(IND_GDESLARGA) private String gdeslarga;
	@PortableProperty(IND_FECALTA) private Timestamp fecAlta;
	@PortableProperty(IND_FECANULA) private Timestamp fecAnula;
	@PortableProperty(IND_CUSUARIO) private String cUsuario;
	@PortableProperty(IND_FECMODIF) private Timestamp fecModif;
	
	@Override
	public CabeceraTablaExperienciaKey getKey() {
		return new CabeceraTablaExperienciaKey(ktabla, fecAnula,fecAlta);
	}

	public Integer getKtabla() {
		return ktabla;
	}

	public void setKtabla(Integer ktabla) {
		this.ktabla = ktabla;
	}

	public String getK2tipotabla() {
		return k2tipotabla;
	}

	public void setK2tipotabla(String k2tipotabla) {
		this.k2tipotabla = k2tipotabla;
	}

	public String getGcorta() {
		return gcorta;
	}

	public void setGcorta(String gcorta) {
		this.gcorta = gcorta;
	}

	public String getGdeslarga() {
		return gdeslarga;
	}

	public void setGdeslarga(String gdeslarga) {
		this.gdeslarga = gdeslarga;
	}

	public Timestamp getFecAlta() {
		return fecAlta;
	}

	public void setFecAlta(Timestamp fecAlta) {
		this.fecAlta = fecAlta;
	}

	public Timestamp getFecAnula() {
		return fecAnula;
	}

	public void setFecAnula(Timestamp fecAnula) {
		this.fecAnula = fecAnula;
	}

	public String getcUsuario() {
		return cUsuario;
	}

	public void setcUsuario(String cUsuario) {
		this.cUsuario = cUsuario;
	}

	public Timestamp getFecModif() {
		return fecModif;
	}

	public void setFecModif(Timestamp fecModif) {
		this.fecModif = fecModif;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cUsuario == null) ? 0 : cUsuario.hashCode());
		result = prime * result + ((fecAlta == null) ? 0 : fecAlta.hashCode());
		result = prime * result
				+ ((fecAnula == null) ? 0 : fecAnula.hashCode());
		result = prime * result
				+ ((fecModif == null) ? 0 : fecModif.hashCode());
		result = prime * result + ((gcorta == null) ? 0 : gcorta.hashCode());
		result = prime * result
				+ ((gdeslarga == null) ? 0 : gdeslarga.hashCode());
		result = prime * result
				+ ((k2tipotabla == null) ? 0 : k2tipotabla.hashCode());
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
		CabeceraTablaExperiencia other = (CabeceraTablaExperiencia) obj;
		if (cUsuario == null) {
			if (other.cUsuario != null) {
				return false;
			}
		} else if (!cUsuario.equals(other.cUsuario)) {
			return false;
		}
		if (fecAlta == null) {
			if (other.fecAlta != null) {
				return false;
			}
		} else if (!fecAlta.equals(other.fecAlta)) {
			return false;
		}
		if (fecAnula == null) {
			if (other.fecAnula != null) {
				return false;
			}
		} else if (!fecAnula.equals(other.fecAnula)) {
			return false;
		}
		if (fecModif == null) {
			if (other.fecModif != null) {
				return false;
			}
		} else if (!fecModif.equals(other.fecModif)) {
			return false;
		}
		if (gcorta == null) {
			if (other.gcorta != null) {
				return false;
			}
		} else if (!gcorta.equals(other.gcorta)) {
			return false;
		}
		if (gdeslarga == null) {
			if (other.gdeslarga != null) {
				return false;
			}
		} else if (!gdeslarga.equals(other.gdeslarga)) {
			return false;
		}
		if (k2tipotabla == null) {
			if (other.k2tipotabla != null) {
				return false;
			}
		} else if (!k2tipotabla.equals(other.k2tipotabla)) {
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
	public String toString() {
		return "CabeceraTablaExperiencia [ktabla=" + ktabla + ", k2tipotabla="
				+ k2tipotabla + ", gcorta=" + gcorta + ", gdeslarga="
				+ gdeslarga + "]";
	}
	
	
	
}