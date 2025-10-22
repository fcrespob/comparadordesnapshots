package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TablaHibridaKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class TablaHibrida implements EntidadBase<TablaHibridaKey> {
	
	public static final int IND_FVALOR = 0;
	public static final int IND_KCOMPANIA = 1;
	public static final int IND_CNEGOCIO = 2;
	public static final int IND_KBASETEC = 3;
	public static final int IND_KRAMO = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KGARANTIA = 6;
	public static final int IND_RIESGOACTUARIAL = 7;
	public static final int IND_SEXO = 8;
	public static final int IND_TMBTI = 9;
	public static final int IND_EXCEPCION = 10;
	public static final int IND_TMBTCALC = 11;
	public static final int IND_PFACTOR1 = 12;
	
	@PortableProperty(IND_FVALOR) private Timestamp fvalor;
	@PortableProperty(IND_KCOMPANIA) private Integer kcompania;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_RIESGOACTUARIAL) private String riesgoactuarial;
	@PortableProperty(IND_SEXO) private String sexo;
	@PortableProperty(IND_TMBTI) private Integer tmbti;
	@PortableProperty(IND_EXCEPCION) private String excepcion;
	@PortableProperty(IND_TMBTCALC) private Integer tmbtcalc;
	@PortableProperty(IND_PFACTOR1) private java.math.BigDecimal pfactor1;

	public Timestamp getFvalor() {
		return fvalor;
	}

	public void setFvalor(Timestamp fvalor) {
		this.fvalor = fvalor;
	}

	public Integer getKcompania() {
		return kcompania;
	}

	public void setKcompania(Integer kcompania) {
		this.kcompania = kcompania;
	}

	public String getcnegocio() {
		return cnegocio;
	}

	public void setcnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
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

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getRiesgoactuarial() {
		return riesgoactuarial;
	}

	public void setRiesgoactuarial(String riesgoactuarial) {
		this.riesgoactuarial = riesgoactuarial;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public Integer getTmbti() {
		return tmbti;
	}

	public void setTmbti(Integer tmbti) {
		this.tmbti = tmbti;
	}

	public String getExcepcion() {
		return excepcion;
	}

	public void setExcepcion(String excepcion) {
		this.excepcion = excepcion;
	}

	public Integer getTmbtcalc() {
		return tmbtcalc;
	}

	public void setTmbtcalc(Integer tmbtcalc) {
		this.tmbtcalc = tmbtcalc;
	}

	public java.math.BigDecimal getPfactor1() {
		return pfactor1;
	}

	public void setPfactor1(java.math.BigDecimal pfactor1) {
		this.pfactor1 = pfactor1;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((excepcion == null) ? 0 : excepcion.hashCode());
		result = prime * result + ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((riesgoactuarial == null) ? 0 : riesgoactuarial.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((fvalor == null) ? 0 : fvalor.hashCode());
		result = prime * result + ((pfactor1 == null) ? 0 : pfactor1.hashCode());
		result = prime * result + ((tmbtcalc == null) ? 0 : tmbtcalc.hashCode());
		result = prime * result + ((tmbti == null) ? 0 : tmbti.hashCode());
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
		TablaHibrida other = (TablaHibrida) obj;
		if (excepcion == null) {
			if (other.excepcion != null)
				return false;
		} else if (!excepcion.equals(other.excepcion))
			return false;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcompania == null) {
			if (other.kcompania != null)
				return false;
		} else if (!kcompania.equals(other.kcompania))
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
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (riesgoactuarial == null) {
			if (other.riesgoactuarial != null)
				return false;
		} else if (!riesgoactuarial.equals(other.riesgoactuarial))
			return false;
		if (sexo == null) {
			if (other.sexo != null)
				return false;
		} else if (!sexo.equals(other.sexo))
			return false;
		if (fvalor == null) {
			if (other.fvalor != null)
				return false;
		} else if (!fvalor.equals(other.fvalor))
			return false;
		if (pfactor1 == null) {
			if (other.pfactor1 != null)
				return false;
		} else if (!pfactor1.equals(other.pfactor1))
			return false;
		if (tmbtcalc == null) {
			if (other.tmbtcalc != null)
				return false;
		} else if (!tmbtcalc.equals(other.tmbtcalc))
			return false;
		if (tmbti == null) {
			if (other.tmbti != null)
				return false;
		} else if (!tmbti.equals(other.tmbti))
			return false;
		return true;
	}

	@Override
	public TablaHibridaKey getKey() {
		return new TablaHibridaKey(fvalor, kcompania, cnegocio, kbasetec, kramo, kmodalidad, kgarantia, riesgoactuarial, sexo, tmbti);
	}

	@Override
	public String toString() {
		return "TablaHibrida [fvalor=" + fvalor + ", kcompania=" + kcompania + ", cnegocio=" + cnegocio + ", kbasetec="
				+ kbasetec + ", kramo=" + kramo + ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia
				+ ", riesgoactuarial=" + riesgoactuarial + ", sexo=" + sexo + ", tmbti=" + tmbti + ", excepcion=" + excepcion
				+ ", tmbtcalc=" + tmbtcalc + ", pfactor1=" + pfactor1 + "]";
	};
	
	
	
}