package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.util.ArrayList;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.DisenoProcesosKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DisenoProcesos implements EntidadBase<DisenoProcesosKey>{
	
	public static final int IND_GPROCESO = 0;
	public static final int IND_KCOMPANIA = 1;
	public static final int IND_KRAMO = 2;
	public static final int IND_KMODALIDAD = 3;
	public static final int IND_KGARANTIA = 4;
	public static final int IND_KBASETEC = 5;
	public static final int IND_KCLAVEADIC = 6;
	
	public static final int IND_CGESTESPECI = 7;
	public static final int IND_CELEMENT1 = 8;
	public static final int IND_SSUBPROC1 = 9;
	public static final int IND_CELEMENT2 = 10;
	public static final int IND_SSUBPROC2 = 11;
	public static final int IND_CELEMENT3 = 12;
	public static final int IND_SSUBPROC3 = 13;
	public static final int IND_CELEMENT4 = 14;
	public static final int IND_SSUBPROC4 = 15;
	public static final int IND_CELEMENT5 = 16;
	public static final int IND_SSUBPROC5 = 17;
	public static final int IND_CELEMENT6 = 18;
	public static final int IND_SSUBPROC6 = 19;
	public static final int IND_CELEMENT7 = 20;
	public static final int IND_SSUBPROC7 = 21;
	public static final int IND_CELEMENT8 = 22;
	public static final int IND_SSUBPROC8 = 23;
	public static final int IND_CELEMENT9 = 24;
	public static final int IND_SSUBPROC9 = 25;
	public static final int IND_CELEMENT10 = 26;
	public static final int IND_SSUBPROC10 = 27;
	public static final int IND_CELEMENT11 = 28;
	public static final int IND_SSUBPROC11 = 29;
	public static final int IND_CELEMENT12 = 30;
	public static final int IND_SSUBPROC12 = 31;
	public static final int IND_CELEMENT13 = 32;
	public static final int IND_SSUBPROC13 = 33;
	public static final int IND_CELEMENT14 = 34;
	public static final int IND_SSUBPROC14 = 35;
	public static final int IND_CELEMENT15 = 36;
	public static final int IND_SSUBPROC15 = 37;
	public static final int IND_CELEMENT16 = 38;
	public static final int IND_SSUBPROC16 = 39;
	public static final int IND_CELEMENT17 = 40;
	public static final int IND_SSUBPROC17 = 41;
	public static final int IND_CELEMENT18 = 42;
	public static final int IND_SSUBPROC18 = 43;
	public static final int IND_CELEMENT19 = 44;
	public static final int IND_SSUBPROC19 = 45;
	public static final int IND_CELEMENT20 = 46;
	public static final int IND_SSUBPROC20 = 47;
	public static final int IND_KESTADO = 48;

	
	@PortableProperty(IND_KCOMPANIA) private Integer kcompania;
	@PortableProperty(IND_KRAMO) private String kramo;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KBASETEC) private String kbasetec;
	@PortableProperty(IND_KCLAVEADIC) private String kclaveadic;
	@PortableProperty(IND_GPROCESO) private String gproceso;
	@PortableProperty(IND_CGESTESPECI) private String cgestespeci;
	
	@PortableProperty(IND_CELEMENT1) private List<ElementoSubproceso> elementosSubprocesos = new ArrayList<ElementoSubproceso>();
	
	
	@PortableProperty(IND_KESTADO) private Boolean kestado;

	
	
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
	public String getCgestespeci() {
		return cgestespeci;
	}
	public void setCgestespeci(String cgestespeci) {
		this.cgestespeci = cgestespeci;
	}
	
	public Boolean getKestado() {
		return kestado;
	}
	public void setKestado(Boolean kestado) {
		this.kestado = kestado;
	}
	@Override
	public DisenoProcesosKey getKey() {
		return new DisenoProcesosKey(kcompania, kramo, kmodalidad, kgarantia, kbasetec, kclaveadic,gproceso);
	}
	public List<ElementoSubproceso> getElementosSubprocesos() {
		return elementosSubprocesos;
	}
	public void setElementosSubprocesos(
			List<ElementoSubproceso> elementosSubprocesos) {
		this.elementosSubprocesos = elementosSubprocesos;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cgestespeci == null) ? 0 : cgestespeci.hashCode());
		result = prime
				* result
				+ ((elementosSubprocesos == null) ? 0 : elementosSubprocesos
						.hashCode());
		result = prime * result
				+ ((gproceso == null) ? 0 : gproceso.hashCode());
		result = prime * result
				+ ((kbasetec == null) ? 0 : kbasetec.hashCode());
		result = prime * result
				+ ((kclaveadic == null) ? 0 : kclaveadic.hashCode());
		result = prime * result
				+ ((kcompania == null) ? 0 : kcompania.hashCode());
		result = prime * result + ((kestado == null) ? 0 : kestado.hashCode());
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
		DisenoProcesos other = (DisenoProcesos) obj;
		if (cgestespeci == null) {
			if (other.cgestespeci != null) {
				return false;
			}
		} else if (!cgestespeci.equals(other.cgestespeci)) {
			return false;
		}
		if (elementosSubprocesos == null) {
			if (other.elementosSubprocesos != null) {
				return false;
			}
		} else if (!elementosSubprocesos.equals(other.elementosSubprocesos)) {
			return false;
		}
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
		if (kestado == null) {
			if (other.kestado != null) {
				return false;
			}
		} else if (!kestado.equals(other.kestado)) {
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
	
	
	

}
