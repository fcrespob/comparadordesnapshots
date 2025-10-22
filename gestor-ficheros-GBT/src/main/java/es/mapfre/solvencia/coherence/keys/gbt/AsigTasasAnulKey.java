package es.mapfre.solvencia.coherence.keys.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.AsigTasasAnul;

@Portable
public class AsigTasasAnulKey   {
	@PortableProperty(AsigTasasAnul.IND_KBASETEC) private String kbasetec;
	@PortableProperty(AsigTasasAnul.IND_KCOMPANIA) private Integer kcompania;
	@PortableProperty(AsigTasasAnul.IND_KNEGOCIO) private String knegocio;
	@PortableProperty(AsigTasasAnul.IND_KRAMO) private String kramo;
	@PortableProperty(AsigTasasAnul.IND_KPINTERESDESDE) private BigDecimal kpinteresdesde;
	@PortableProperty(AsigTasasAnul.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(AsigTasasAnul.IND_KFINICONVERSION) private Timestamp kfiniconversion;
	public String getKbasetec() {
		return kbasetec;
	}
	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}
	public Integer getKcompania() {
		return kcompania;
	}
	public void setKcompania(Integer kcompania) {
		this.kcompania = kcompania;
	}
	public String getKnegocio() {
		return knegocio;
	}
	public void setKnegocio(String knegocio) {
		this.knegocio = knegocio;
	}
	public String getKramo() {
		return kramo;
	}
	public void setKramo(String kramo) {
		this.kramo = kramo;
	}
	public BigDecimal getKpinteresdesde() {
		return kpinteresdesde;
	}
	public void setKpinteresdesde(BigDecimal kpinteresdesde) {
		this.kpinteresdesde = kpinteresdesde;
	}
	public Integer getKmodalidad() {
		return kmodalidad;
	}
	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}
	public Timestamp getKfiniconversion() {
		return kfiniconversion;
	}
	public void setKfiniconversion(Timestamp kfiniconversion) {
		this.kfiniconversion = kfiniconversion;
	}
	public AsigTasasAnulKey() {
		super();
	}
	public AsigTasasAnulKey(String kbasetec, Integer kcompania, String knegocio,
			String kramo, BigDecimal kpinteresdesde, Integer kmodalidad,
			Timestamp kfiniconversion) {
		super();
		this.kbasetec = kbasetec;
		this.kcompania = kcompania;
		this.knegocio = knegocio;
		this.kramo = kramo;
		this.kpinteresdesde = kpinteresdesde;
		this.kmodalidad = kmodalidad;
		this.kfiniconversion = kfiniconversion;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result
				+ ((kfiniconversion == null) ? 0 : kfiniconversion.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((knegocio == null) ? 0 : knegocio.hashCode());
		result = prime * result
				+ ((kpinteresdesde == null) ? 0 : kpinteresdesde.hashCode());
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
		AsigTasasAnulKey other = (AsigTasasAnulKey) obj;
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
		if (kfiniconversion == null) {
			if (other.kfiniconversion != null)
				return false;
		} else if (!kfiniconversion.equals(other.kfiniconversion))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (knegocio == null) {
			if (other.knegocio != null)
				return false;
		} else if (!knegocio.equals(other.knegocio))
			return false;
		if (kpinteresdesde == null) {
			if (other.kpinteresdesde != null)
				return false;
		} else if (!kpinteresdesde.equals(other.kpinteresdesde))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		return true;
	}
	
	
	
}
