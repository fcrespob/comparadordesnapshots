package es.mapfre.solvencia.dominio.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.MetodosAdaptacionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class MetodosAdaptacion implements EntidadBase<MetodosAdaptacionKey> {

	public static final int IND_KMADAPTAC = 0;
	public static final int IND_KFINICIO = 1;
	public static final int IND_FFIN = 2;
	public static final int IND_CPERADAPTAC = 3;
	public static final int IND_CPERPEND = 4;
	public static final int IND_CPERUTIL = 5;
	
	@PortableProperty(IND_KMADAPTAC) private String kmadaptac;
	@PortableProperty(IND_KFINICIO) private Timestamp kfinicio;
	@PortableProperty(IND_KMADAPTAC) private Timestamp ffin;
	@PortableProperty(IND_KFINICIO) private Integer cperadaptac;
	@PortableProperty(IND_KMADAPTAC) private Integer cperpend;
	@PortableProperty(IND_KFINICIO) private Integer cperutil;
		
	public String getKmadaptac() {
		return kmadaptac;
	}

	public void setKmadaptac(String kmadaptac) {
		this.kmadaptac = kmadaptac;
	}

	public Timestamp getKfinicio() {
		return kfinicio;
	}

	public void setKfinicio(Timestamp kfinicio) {
		this.kfinicio = kfinicio;
	}

	public Timestamp getFfin() {
		return ffin;
	}

	public void setFfin(Timestamp ffin) {
		this.ffin = ffin;
	}

	public Integer getCperadaptac() {
		return cperadaptac;
	}

	public void setCperadaptac(Integer cperadaptac) {
		this.cperadaptac = cperadaptac;
	}

	public Integer getCperpend() {
		return cperpend;
	}

	public void setCperpend(Integer cperpend) {
		this.cperpend = cperpend;
	}

	public Integer getCperutil() {
		return cperutil;
	}

	public void setCperutil(Integer cperutil) {
		this.cperutil = cperutil;
	}

	@Override
	public MetodosAdaptacionKey getKey() {
		return new MetodosAdaptacionKey(kmadaptac, kfinicio);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cperadaptac == null) ? 0 : cperadaptac.hashCode());
		result = prime * result
				+ ((cperpend == null) ? 0 : cperpend.hashCode());
		result = prime * result
				+ ((cperutil == null) ? 0 : cperutil.hashCode());
		result = prime * result + ((ffin == null) ? 0 : ffin.hashCode());
		result = prime * result
				+ ((kfinicio == null) ? 0 : kfinicio.hashCode());
		result = prime * result
				+ ((kmadaptac == null) ? 0 : kmadaptac.hashCode());
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
		MetodosAdaptacion other = (MetodosAdaptacion) obj;
		if (cperadaptac == null) {
			if (other.cperadaptac != null)
				return false;
		} else if (!cperadaptac.equals(other.cperadaptac))
			return false;
		if (cperpend == null) {
			if (other.cperpend != null)
				return false;
		} else if (!cperpend.equals(other.cperpend))
			return false;
		if (cperutil == null) {
			if (other.cperutil != null)
				return false;
		} else if (!cperutil.equals(other.cperutil))
			return false;
		if (ffin == null) {
			if (other.ffin != null)
				return false;
		} else if (!ffin.equals(other.ffin))
			return false;
		if (kfinicio == null) {
			if (other.kfinicio != null)
				return false;
		} else if (!kfinicio.equals(other.kfinicio))
			return false;
		if (kmadaptac == null) {
			if (other.kmadaptac != null)
				return false;
		} else if (!kmadaptac.equals(other.kmadaptac))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetodosAdaptacion [kmadaptac=");
		builder.append(kmadaptac);
		builder.append(", kfinicio=");
		builder.append(kfinicio);
		builder.append(", ffin=");
		builder.append(ffin);
		builder.append(", cperadaptac=");
		builder.append(cperadaptac);
		builder.append(", cperpend=");
		builder.append(cperpend);
		builder.append(", cperutil=");
		builder.append(cperutil);
		builder.append("]");
		return builder.toString();
	}
	
}
