package es.mapfre.solvencia.dominio.gbt;

import java.io.Serializable;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.gbt.AsigCurvaTipoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class AsigCurvaTipo implements
Serializable,EntidadBase<AsigCurvaTipoKey> {

	public static final int IND_KBASETEC = 0;
	public static final int IND_KRIESGO = 1;
	public static final int IND_KRIESGORESCATE = 2;
	public static final int IND_KPAGOUNICO = 3;
	public static final int IND_KCARTERAINV = 4;
	public static final int IND_KPB = 5;
	public static final int IND_KAPBEL = 6;
	public static final int IND_KFDESDECONVER = 7;
	public static final int IND_FHASTACONVER = 8;
	public static final int IND_KCURVA = 9;
	public static final int IND_SPERIODOTRANS = 10;
	public static final int IND_KMADAPTAC = 11;
	
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_KRIESGO) private String kriesgo;
	@PortableProperty(IND_KRIESGORESCATE) private String kriesgorescate;
	@PortableProperty(IND_KPAGOUNICO) private String kpagounico;
	@PortableProperty(IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(IND_KPB) private String kpb;
	@PortableProperty(IND_KAPBEL) private String kapbel;
	@PortableProperty(IND_KFDESDECONVER) private Timestamp kfdesdeconver;
	@PortableProperty(IND_FHASTACONVER) private Timestamp fhastaconver;
	@PortableProperty(IND_KCURVA) private String kcurva;
	@PortableProperty(IND_SPERIODOTRANS) private String speriodotrans;
	@PortableProperty(IND_KMADAPTAC) private String kmadaptac;
	
	public String getKbasetec() {
		return kbasetec;
	}

	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}

	public String getKriesgo() {
		return kriesgo;
	}

	public void setKriesgo(String kriesgo) {
		this.kriesgo = kriesgo;
	}

	public String getKriesgorescate() {
		return kriesgorescate;
	}

	public void setKriesgorescate(String kriesgorescate) {
		this.kriesgorescate = kriesgorescate;
	}

	public String getKpagounico() {
		return kpagounico;
	}

	public void setKpagounico(String kpagounico) {
		this.kpagounico = kpagounico;
	}

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public String getKpb() {
		return kpb;
	}

	public void setKpb(String kpb) {
		this.kpb = kpb;
	}

	public String getKapbel() {
		return kapbel;
	}

	public void setKapbel(String kapbel) {
		this.kapbel = kapbel;
	}

	public Timestamp getKfdesdeconver() {
		return kfdesdeconver;
	}

	public void setKfdesdeconver(Timestamp kfdesdeconver) {
		this.kfdesdeconver = kfdesdeconver;
	}

	public Timestamp getFhastaconver() {
		return fhastaconver;
	}

	public void setFhastaconver(Timestamp fhastaconver) {
		this.fhastaconver = fhastaconver;
	}

	public String getKcurva() {
		return kcurva;
	}

	public void setKcurva(String kcurva) {
		this.kcurva = kcurva;
	}

	public String getSperiodotrans() {
		return speriodotrans;
	}

	public void setSperiodotrans(String speriodotrans) {
		this.speriodotrans = speriodotrans;
	}

	public String getKmadaptac() {
		return kmadaptac;
	}

	public void setKmadaptac(String kmadaptac) {
		this.kmadaptac = kmadaptac;
	}

	@Override
	public AsigCurvaTipoKey getKey() {
		return new AsigCurvaTipoKey(kbasetec, kriesgo, kriesgorescate, kpagounico, kcarterainv, kpb, kapbel, kfdesdeconver);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fhastaconver == null) ? 0 : fhastaconver.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcurva == null) ? 0 : kcurva.hashCode());
		result = prime * result
				+ ((kfdesdeconver == null) ? 0 : kfdesdeconver.hashCode());
		result = prime * result
				+ ((kmadaptac == null) ? 0 : kmadaptac.hashCode());
		result = prime * result
				+ ((kpagounico == null) ? 0 : kpagounico.hashCode());
		result = prime * result + ((kpb == null) ? 0 : kpb.hashCode());
		result = prime * result + ((kapbel == null) ? 0 : kapbel.hashCode());
		result = prime * result + ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result
				+ ((kriesgorescate == null) ? 0 : kriesgorescate.hashCode());
		result = prime * result
				+ ((speriodotrans == null) ? 0 : speriodotrans.hashCode());
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
		AsigCurvaTipo other = (AsigCurvaTipo) obj;
		if (fhastaconver == null) {
			if (other.fhastaconver != null)
				return false;
		} else if (!fhastaconver.equals(other.fhastaconver))
			return false;
		if (kbasetec == null) {
			if (other.kbasetec != null)
				return false;
		} else if (!kbasetec.equals(other.kbasetec))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kcurva == null) {
			if (other.kcurva != null)
				return false;
		} else if (!kcurva.equals(other.kcurva))
			return false;
		if (kfdesdeconver == null) {
			if (other.kfdesdeconver != null)
				return false;
		} else if (!kfdesdeconver.equals(other.kfdesdeconver))
			return false;
		if (kmadaptac == null) {
			if (other.kmadaptac != null)
				return false;
		} else if (!kmadaptac.equals(other.kmadaptac))
			return false;
		if (kpagounico == null) {
			if (other.kpagounico != null)
				return false;
		} else if (!kpagounico.equals(other.kpagounico))
			return false;
		if (kpb == null) {
			if (other.kpb != null)
				return false;
		} else if (!kpb.equals(other.kpb))
			return false;
		if (kapbel == null) {
			if (other.kapbel != null)
				return false;
		} else if (!kapbel.equals(other.kapbel))
			return false;
		if (kriesgo == null) {
			if (other.kriesgo != null)
				return false;
		} else if (!kriesgo.equals(other.kriesgo))
			return false;
		if (kriesgorescate == null) {
			if (other.kriesgorescate != null)
				return false;
		} else if (!kriesgorescate.equals(other.kriesgorescate))
			return false;
		if (speriodotrans == null) {
			if (other.speriodotrans != null)
				return false;
		} else if (!speriodotrans.equals(other.speriodotrans))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AsigCurvaTipo [kbasetec=");
		builder.append(kbasetec);
		builder.append(", kriesgo=");
		builder.append(kriesgo);
		builder.append(", kriesgorescate=");
		builder.append(kriesgorescate);
		builder.append(", kpagounico=");
		builder.append(kpagounico);
		builder.append(", kcarterainv=");
		builder.append(kcarterainv);
		builder.append(", kpb=");
		builder.append(kpb);
		builder.append(", kapbel=");
		builder.append(kapbel);
		builder.append(", kfdesdeconver=");
		builder.append(kfdesdeconver);
		builder.append(", fhastaconver=");
		builder.append(fhastaconver);
		builder.append(", kcurva=");
		builder.append(kcurva);
		builder.append(", speriodotrans=");
		builder.append(speriodotrans);
		builder.append(", kmadaptac=");
		builder.append(kmadaptac);
		builder.append("]");
		return builder.toString();
	}

}
