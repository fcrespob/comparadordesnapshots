package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class FiltroFichaProceso {
	
	public static final int IND_KSECAMBITO = 0;
	public static final int IND_CCLASEAMB = 1;
	public static final int IND_COPERADORD = 2;
	public static final int IND_GAMBITOD = 3;
	public static final int IND_COPERADORH = 4;
	public static final int IND_GAMBITOH = 5;
	public static final int IND_SMANUAL = 6;
	public static final int IND_FGRABACION = 7;
	public static final int IND_CUSUARIO = 8;
	public static final int IND_KTIPOAMB = 9;

	@PortableProperty(IND_KSECAMBITO) private Integer ksecambito;
	@PortableProperty(IND_CCLASEAMB) private String cclaseamb;
	@PortableProperty(IND_COPERADORD) private String coperadord;
	@PortableProperty(IND_GAMBITOD) private String gambitod;
	@PortableProperty(IND_COPERADORH) private String coperadorh;
	@PortableProperty(IND_GAMBITOH) private String gambitoh;
	@PortableProperty(IND_SMANUAL) private String smanual;
	@PortableProperty(IND_FGRABACION) private Timestamp fgrabacion;
	@PortableProperty(IND_CUSUARIO) private String cusuario;
	@PortableProperty(IND_KTIPOAMB) private String ktipoamb;
	
	public Integer getKsecambito() {
		return ksecambito;
	}
	public void setKsecambito(Integer ksecambito) {
		this.ksecambito = ksecambito;
	}
	public String getCclaseamb() {
		return cclaseamb;
	}
	public void setCclaseamb(String cclaseamb) {
		this.cclaseamb = cclaseamb;
	}
	public String getCoperadord() {
		return coperadord;
	}
	public void setCoperadord(String coperadord) {
		this.coperadord = coperadord;
	}
	public String getGambitod() {
		return gambitod;
	}
	public void setGambitod(String gambitod) {
		this.gambitod = gambitod;
	}
	public String getCoperadorh() {
		return coperadorh;
	}
	public void setCoperadorh(String coperadorh) {
		this.coperadorh = coperadorh;
	}
	public String getGambitoh() {
		return gambitoh;
	}
	public void setGambitoh(String gambitoh) {
		this.gambitoh = gambitoh;
	}
	public String getSmanual() {
		return smanual;
	}
	public void setSmanual(String smanual) {
		this.smanual = smanual;
	}
	public Timestamp getFgrabacion() {
		return fgrabacion;
	}
	public void setFgrabacion(Timestamp fgrabacion) {
		this.fgrabacion = fgrabacion;
	}
	public String getCusuario() {
		return cusuario;
	}
	public void setCusuario(String cusuario) {
		this.cusuario = cusuario;
	}
	public String getKtipoamb() {
		return ktipoamb;
	}
	public void setKtipoamb(String ktipoamb) {
		this.ktipoamb = ktipoamb;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cclaseamb == null) ? 0 : cclaseamb.hashCode());
		result = prime * result
				+ ((coperadord == null) ? 0 : coperadord.hashCode());
		result = prime * result
				+ ((coperadorh == null) ? 0 : coperadorh.hashCode());
		result = prime * result
				+ ((cusuario == null) ? 0 : cusuario.hashCode());
		result = prime * result
				+ ((fgrabacion == null) ? 0 : fgrabacion.hashCode());
		result = prime * result
				+ ((gambitod == null) ? 0 : gambitod.hashCode());
		result = prime * result
				+ ((gambitoh == null) ? 0 : gambitoh.hashCode());
		result = prime * result
				+ ((ksecambito == null) ? 0 : ksecambito.hashCode());
		result = prime * result
				+ ((ktipoamb == null) ? 0 : ktipoamb.hashCode());
		result = prime * result + ((smanual == null) ? 0 : smanual.hashCode());
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
		FiltroFichaProceso other = (FiltroFichaProceso) obj;
		if (cclaseamb == null) {
			if (other.cclaseamb != null) {
				return false;
			}
		} else if (!cclaseamb.equals(other.cclaseamb)) {
			return false;
		}
		if (coperadord == null) {
			if (other.coperadord != null) {
				return false;
			}
		} else if (!coperadord.equals(other.coperadord)) {
			return false;
		}
		if (coperadorh == null) {
			if (other.coperadorh != null) {
				return false;
			}
		} else if (!coperadorh.equals(other.coperadorh)) {
			return false;
		}
		if (cusuario == null) {
			if (other.cusuario != null) {
				return false;
			}
		} else if (!cusuario.equals(other.cusuario)) {
			return false;
		}
		if (fgrabacion == null) {
			if (other.fgrabacion != null) {
				return false;
			}
		} else if (!fgrabacion.equals(other.fgrabacion)) {
			return false;
		}
		if (gambitod == null) {
			if (other.gambitod != null) {
				return false;
			}
		} else if (!gambitod.equals(other.gambitod)) {
			return false;
		}
		if (gambitoh == null) {
			if (other.gambitoh != null) {
				return false;
			}
		} else if (!gambitoh.equals(other.gambitoh)) {
			return false;
		}
		if (ksecambito == null) {
			if (other.ksecambito != null) {
				return false;
			}
		} else if (!ksecambito.equals(other.ksecambito)) {
			return false;
		}
		if (ktipoamb == null) {
			if (other.ktipoamb != null) {
				return false;
			}
		} else if (!ktipoamb.equals(other.ktipoamb)) {
			return false;
		}
		if (smanual == null) {
			if (other.smanual != null) {
				return false;
			}
		} else if (!smanual.equals(other.smanual)) {
			return false;
		}
		return true;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FiltroFichaProceso [ksecambito=");
		builder.append(ksecambito);
		builder.append(", cclaseamb=");
		builder.append(cclaseamb);
		builder.append(", coperadord=");
		builder.append(coperadord);
		builder.append(", gambitod=");
		builder.append(gambitod);
		builder.append(", coperadorh=");
		builder.append(coperadorh);
		builder.append(", gambitoh=");
		builder.append(gambitoh);
		builder.append(", smanual=");
		builder.append(smanual);
		builder.append(", fgrabacion=");
		builder.append(fgrabacion);
		builder.append(", cusuario=");
		builder.append(cusuario);
		builder.append(", ktipoamb=");
		builder.append(ktipoamb);
		builder.append("]");
		return builder.toString();
	}
	
	
}