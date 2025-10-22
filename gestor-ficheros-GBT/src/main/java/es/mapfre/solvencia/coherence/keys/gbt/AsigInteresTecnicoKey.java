package es.mapfre.solvencia.coherence.keys.gbt;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.gbt.AsigInteresTecnico;

@Portable
public class AsigInteresTecnicoKey   {
	@PortableProperty(AsigInteresTecnico.IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(AsigInteresTecnico.IND_KTIPOBT) private String ktipobt;
	@PortableProperty(AsigInteresTecnico.IND_KGAP) private String kgap;
	@PortableProperty(AsigInteresTecnico.IND_KAPROSSP) private String kaprossp;
	@PortableProperty(AsigInteresTecnico.IND_KCASADO) private String kcasado;
	
	public AsigInteresTecnicoKey(Timestamp fcierre, String ktipobt, String kgap,
			String kaprossp, String kcasado) {
		super();
		this.fcierre = fcierre;
		this.ktipobt = ktipobt;
		this.kgap = kgap;
		this.kaprossp = kaprossp;
		this.kcasado = kcasado;
	}
	public AsigInteresTecnicoKey() {
		super();
	}
	public Timestamp getFcierre() {
		return fcierre;
	}
	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}
	public String getKtipobt() {
		return ktipobt;
	}
	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}
	public String getKgap() {
		return kgap;
	}
	public void setKgap(String kgap) {
		this.kgap = kgap;
	}
	public String getKaprossp() {
		return kaprossp;
	}
	public void setKaprossp(String kaprossp) {
		this.kaprossp = kaprossp;
	}
	public String getKcasado() {
		return kcasado;
	}
	public void setKcasado(String kcasado) {
		this.kcasado = kcasado;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result
				+ ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result
				+ ((kgap == null) ? 0 : kgap.hashCode());
		result = prime * result
				+ ((kaprossp == null) ? 0 : kaprossp.hashCode());
		result = prime * result
				+ ((kcasado == null) ? 0 : kcasado.hashCode());
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
		AsigInteresTecnicoKey other = (AsigInteresTecnicoKey) obj;
		if (fcierre == null) {
			if (other.fcierre != null) {
				return false;
			}
		} else if (!fcierre.equals(other.fcierre)) {
			return false;
		}
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		if (kgap == null) {
			if (other.kgap != null) {
				return false;
			}
		} else if (!kgap.equals(other.kgap)) {
			return false;
		}
		if (kaprossp == null) {
			if (other.kaprossp != null) {
				return false;
			}
		} else if (!kaprossp.equals(other.kaprossp)) {
			return false;
		}
		if (kcasado == null) {
			if (other.kcasado != null) {
				return false;
			}
		} else if (!kcasado.equals(other.kcasado)) {
			return false;
		}
		return true;
	}
	
}
