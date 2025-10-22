package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.AsigCurvaTipo;

@Portable
public class AsigCurvaTipoKey   {
	@PortableProperty(AsigCurvaTipo.IND_KBASETEC) private String kbasetec;
	@PortableProperty(AsigCurvaTipo.IND_KRIESGO) private String kriesgo;
	@PortableProperty(AsigCurvaTipo.IND_KRIESGORESCATE) private String kriesgorescate;
	@PortableProperty(AsigCurvaTipo.IND_KPAGOUNICO) private String kpagounico;
	@PortableProperty(AsigCurvaTipo.IND_KCARTERAINV) private String kcarterainv;
	@PortableProperty(AsigCurvaTipo.IND_KPB) private String kpb;
	@PortableProperty(AsigCurvaTipo.IND_KAPBEL) private String kapbel;
	@PortableProperty(AsigCurvaTipo.IND_KFDESDECONVER) private Timestamp kfdesdeconver;
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
	public AsigCurvaTipoKey() {
		super();
	}
	public AsigCurvaTipoKey(String kbasetec, String kriesgo,
			String kriesgorescate, String kpagounico, String kcarterainv,
			String kpb, String kapbel, Timestamp kfdesdeconver) {
		super();
		this.kbasetec = kbasetec;
		this.kriesgo = kriesgo;
		this.kriesgorescate = kriesgorescate;
		this.kpagounico = kpagounico;
		this.kcarterainv = kcarterainv;
		this.kpb = kpb;
		this.kapbel = kapbel;
		this.kfdesdeconver = kfdesdeconver;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result
				+ ((kfdesdeconver == null) ? 0 : kfdesdeconver.hashCode());
		result = prime * result
				+ ((kpagounico == null) ? 0 : kpagounico.hashCode());
		result = prime * result + ((kpb == null) ? 0 : kpb.hashCode());
		result = prime * result + ((kapbel == null) ? 0 : kapbel.hashCode());
		result = prime * result + ((kriesgo == null) ? 0 : kriesgo.hashCode());
		result = prime * result
				+ ((kriesgorescate == null) ? 0 : kriesgorescate.hashCode());
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
		AsigCurvaTipoKey other = (AsigCurvaTipoKey) obj;
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
		if (kfdesdeconver == null) {
			if (other.kfdesdeconver != null)
				return false;
		} else if (!kfdesdeconver.equals(other.kfdesdeconver))
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
		return true;
	}
		
}
