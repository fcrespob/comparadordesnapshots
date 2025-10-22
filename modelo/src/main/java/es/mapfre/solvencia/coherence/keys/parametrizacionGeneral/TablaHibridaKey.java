package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaHibrida;

@Portable
public class TablaHibridaKey {

	@PortableProperty(TablaHibrida.IND_FVALOR)
	private Timestamp fvalor;
	@PortableProperty(TablaHibrida.IND_KCOMPANIA) 
	private Integer kcompania;
	@PortableProperty(TablaHibrida.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(TablaHibrida.IND_KBASETEC)
	private String kbasetec;
	@PortableProperty(TablaHibrida.IND_KRAMO)
	private String kramo;
	@PortableProperty(TablaHibrida.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(TablaHibrida.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(TablaHibrida.IND_RIESGOACTUARIAL)
	private String riesgoactuarial;
	@PortableProperty(TablaHibrida.IND_SEXO)
	private String sexo;
	@PortableProperty(TablaHibrida.IND_TMBTI)
	private Integer tmbti;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result + ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result + ((fvalor == null) ? 0 : fvalor.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((riesgoactuarial == null) ? 0 : riesgoactuarial.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
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
		TablaHibridaKey other = (TablaHibridaKey) obj;
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
		if (fvalor == null) {
			if (other.fvalor != null)
				return false;
		} else if (!fvalor.equals(other.fvalor))
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
		if (tmbti == null) {
			if (other.tmbti != null)
				return false;
		} else if (!tmbti.equals(other.tmbti))
			return false;
		return true;
	}
	
	public TablaHibridaKey(Timestamp fvalor, Integer kcompania, String cnegocio, String kbasetec, String kramo,
			Integer kmodalidad, Integer kgarantia, String riesgoactuarial, String sexo, Integer tmbti) {
		super();
		this.fvalor = fvalor;
		this.kcompania = kcompania;
		this.cnegocio = cnegocio;
		this.kbasetec = kbasetec;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.riesgoactuarial = riesgoactuarial;
		this.sexo = sexo;
		this.tmbti = tmbti;
	}
	public TablaHibridaKey() {
		super();
	}
	
}