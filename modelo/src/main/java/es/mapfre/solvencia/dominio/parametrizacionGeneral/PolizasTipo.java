package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.PolizasTipoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class PolizasTipo implements EntidadBase<PolizasTipoKey> {
	
	public static final int IND_ORIGEN      = 0;
	public static final int IND_CNEGOCIO      = 1;
	public static final int IND_CCANAL	      = 2;
	public static final int IND_CCARTERA      = 3;
	public static final int IND_CLAVEUMIC     = 4;
	public static final int IND_KPOLIZA       = 5;
	public static final int IND_KSUBPOLIZA    = 6;
	public static final int IND_NSUSCRI       = 7;

	
	@PortableProperty(IND_ORIGEN) private String origen;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;	
	@PortableProperty(IND_CCARTERA) private Integer ccartera;
	
	@PortableProperty(IND_CLAVEUMIC) private UmicKey claveUmic;
	
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI) private Integer nsuscri;

	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
	}

	public UmicKey getClaveUmic() {
		return claveUmic;
	}

	public void setClaveUmic(UmicKey claveUmic) {
		this.claveUmic = claveUmic;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result
				+ ((claveUmic == null) ? 0 : claveUmic.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result
				+ ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((origen == null) ? 0 : origen.hashCode());
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
		PolizasTipo other = (PolizasTipo) obj;
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (ccartera == null) {
			if (other.ccartera != null) {
				return false;
			}
		} else if (!ccartera.equals(other.ccartera)) {
			return false;
		}
		if (claveUmic == null) {
			if (other.claveUmic != null) {
				return false;
			}
		} else if (!claveUmic.equals(other.claveUmic)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null) {
				return false;
			}
		} else if (!ksubpoliza.equals(other.ksubpoliza)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		if (origen == null) {
			if (other.origen != null) {
				return false;
			}
		} else if (!origen.equals(other.origen)) {
			return false;
		}
		return true;
	}

	@Override
	public PolizasTipoKey getKey() {
		return new PolizasTipoKey(claveUmic);
	}
}