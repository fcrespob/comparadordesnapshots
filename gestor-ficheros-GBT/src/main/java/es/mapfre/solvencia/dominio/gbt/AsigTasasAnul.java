package es.mapfre.solvencia.dominio.gbt;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.AsigTasasAnulKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class AsigTasasAnul implements EntidadBase<AsigTasasAnulKey> {

	public static final int IND_KBASETEC = 0;
	public static final int IND_KCOMPANIA = 1;
	public static final int IND_KNEGOCIO = 2;
	public static final int IND_KRAMO = 3;
	public static final int IND_KPINTERESDESDE = 4;
	public static final int IND_KMODALIDAD = 5;
	public static final int IND_KFINICONVERSION = 6;
	public static final int IND_PINTERESHASTA = 7;
	public static final int IND_FFINCONVERSION = 8;
	public static final int IND_KTABLAANU = 9;
	
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_KCOMPANIA) private Integer kcompania;
	@PortableProperty(IND_KNEGOCIO) private String knegocio;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KPINTERESDESDE) private BigDecimal kpinteresdesde;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KFINICONVERSION) private Timestamp kfiniconversion;
	@PortableProperty(IND_PINTERESHASTA) private BigDecimal pintereshasta;
	@PortableProperty(IND_FFINCONVERSION) private Timestamp ffinconversion;
	@PortableProperty(IND_KTABLAANU) private String ktablaanu;
	
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

	public BigDecimal getPintereshasta() {
		return pintereshasta;
	}

	public void setPintereshasta(BigDecimal pintereshasta) {
		this.pintereshasta = pintereshasta;
	}

	public Timestamp getFfinconversion() {
		return ffinconversion;
	}

	public void setFfinconversion(Timestamp ffinconversion) {
		this.ffinconversion = ffinconversion;
	}

	public String getKtablaanu() {
		return ktablaanu;
	}

	public void setKtablaanu(String ktablaanu) {
		this.ktablaanu = ktablaanu;
	}

	public AsigTasasAnul(String kbasetec, Integer kcompania, String knegocio,
			String kramo, BigDecimal kpinteresdesde, Integer kmodalidad,
			Timestamp kfiniconversion, BigDecimal pintereshasta,
			Timestamp ffinconversion, String ktablaanu, String cusuaralta,
			String falta, String cusuarmodif, String fmodif) {
		super();
		this.kbasetec = kbasetec;
		this.kcompania = kcompania;
		this.knegocio = knegocio;
		this.kramo = kramo;
		this.kpinteresdesde = kpinteresdesde;
		this.kmodalidad = kmodalidad;
		this.kfiniconversion = kfiniconversion;
		this.pintereshasta = pintereshasta;
		this.ffinconversion = ffinconversion;
		this.ktablaanu = ktablaanu;
	}

	public AsigTasasAnul() {
		super();
	}

	@Override
	public AsigTasasAnulKey getKey() {
		return new AsigTasasAnulKey(kbasetec, kcompania, knegocio, kramo, kpinteresdesde, kmodalidad, kfiniconversion);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ffinconversion == null) ? 0 : ffinconversion.hashCode());
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
		result = prime * result
				+ ((ktablaanu == null) ? 0 : ktablaanu.hashCode());
		result = prime * result
				+ ((pintereshasta == null) ? 0 : pintereshasta.hashCode());
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
		AsigTasasAnul other = (AsigTasasAnul) obj;
		if (ffinconversion == null) {
			if (other.ffinconversion != null)
				return false;
		} else if (!ffinconversion.equals(other.ffinconversion))
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
		if (ktablaanu == null) {
			if (other.ktablaanu != null)
				return false;
		} else if (!ktablaanu.equals(other.ktablaanu))
			return false;
		if (pintereshasta == null) {
			if (other.pintereshasta != null)
				return false;
		} else if (!pintereshasta.equals(other.pintereshasta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AsigTasasAnul [kbasetec=");
		builder.append(kbasetec);
		builder.append(", kcompania=");
		builder.append(kcompania);
		builder.append(", knegocio=");
		builder.append(knegocio);
		builder.append(", kramo=");
		builder.append(kramo);
		builder.append(", kpinteresdesde=");
		builder.append(kpinteresdesde);
		builder.append(", kmodalidad=");
		builder.append(kmodalidad);
		builder.append(", kfiniconversion=");
		builder.append(kfiniconversion);
		builder.append(", pintereshasta=");
		builder.append(pintereshasta);
		builder.append(", ffinconversion=");
		builder.append(ffinconversion);
		builder.append(", ktablaanu=");
		builder.append(ktablaanu);
		builder.append("]");
		return builder.toString();
	}
	
}
