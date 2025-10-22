package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;

@Portable
public class DisenoProcesosKey{
	
	@PortableProperty(DisenoProcesos.IND_KCOMPANIA) private Integer kcompania;
	@PortableProperty(DisenoProcesos.IND_KRAMO) private String kramo;
	@PortableProperty(DisenoProcesos.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(DisenoProcesos.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(DisenoProcesos.IND_KBASETEC) private String kbasetec;
	@PortableProperty(DisenoProcesos.IND_KCLAVEADIC) private String kclaveadic;
	@PortableProperty(DisenoProcesos.IND_GPROCESO) private String gproceso;
	
	public Integer getKcompania() {
		return kcompania;
	}
	public void setKcompania(Integer kcompania) {
		this.kcompania = kcompania;
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
	public String getKbasetec() {
		return kbasetec;
	}
	public void setKbasetec(String kbasetec) {
		this.kbasetec = kbasetec;
	}
	public String getKclaveadic() {
		return kclaveadic;
	}
	public void setKclaveadic(String kclaveadic) {
		this.kclaveadic = kclaveadic;
	}
	public String getGproceso() {
		return gproceso;
	}
	public void setGproceso(String gproceso) {
		this.gproceso = gproceso;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gproceso == null) ? 0 : gproceso.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kclaveadic == null) ? 0 : kclaveadic.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
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
		DisenoProcesosKey other = (DisenoProcesosKey) obj;
		if (gproceso == null) {
			if (other.gproceso != null) {
				return false;
			}
		} else if (!gproceso.equals(other.gproceso)) {
			return false;
		}
		if (kbasetec == null) {
			if (other.kbasetec != null) {
				return false;
			}
		} else if (!kbasetec.equals(other.kbasetec)) {
			return false;
		}
		if (kclaveadic == null) {
			if (other.kclaveadic != null) {
				return false;
			}
		} else if (!kclaveadic.equals(other.kclaveadic)) {
			return false;
		}
		if (kcompania == null) {
			if (other.kcompania != null) {
				return false;
			}
		} else if (!kcompania.equals(other.kcompania)) {
			return false;
		}
		if (kgarantia == null) {
			if (other.kgarantia != null) {
				return false;
			}
		} else if (!kgarantia.equals(other.kgarantia)) {
			return false;
		}
		if (kmodalidad == null) {
			if (other.kmodalidad != null) {
				return false;
			}
		} else if (!kmodalidad.equals(other.kmodalidad)) {
			return false;
		}
		if (kramo == null) {
			if (other.kramo != null) {
				return false;
			}
		} else if (!kramo.equals(other.kramo)) {
			return false;
		}
		return true;
	}
	public DisenoProcesosKey(Integer kcompania, String kramo,
			Integer kmodalidad, Integer kgarantia, String kbasetec,
			String kclaveadic, String gproceso) {
		super();
		this.kcompania = kcompania;
		this.kramo = kramo;
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.kbasetec = kbasetec;
		this.kclaveadic = kclaveadic;
		this.gproceso = gproceso;
	}
	
	public DisenoProcesosKey() {
		super();
	}
}