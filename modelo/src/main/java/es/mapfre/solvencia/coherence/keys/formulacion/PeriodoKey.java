package es.mapfre.solvencia.coherence.keys.formulacion;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.formulacion.Periodo;

@Portable
public class PeriodoKey {
	
	@PortableProperty(Periodo.IND_FECHAINICIO) private Timestamp fechaInicio;
	@PortableProperty(Periodo.IND_FECHAFIN) private Timestamp fechaFin;	
	@PortableProperty(Periodo.IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(Periodo.IND_CCANAL) private Integer ccanal;
	@PortableProperty(Periodo.IND_CCARTERA) private Integer ccartera;
	@PortableProperty(Periodo.IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(Periodo.IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(Periodo.IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(Periodo.IND_SUBKPOLIZA) private Integer ksubpoliza;
	@PortableProperty(Periodo.IND_KCERTIFICADO) private Integer kcertificado;
	@PortableProperty(Periodo.IND_NSUSCRI) private Integer nsuscri;
	@PortableProperty(Periodo.IND_NORDEN) private Integer norden;
	@PortableProperty(Periodo.IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(Periodo.IND_KPRESTACION) private String kprestacion;
	@PortableProperty(Periodo.IND_KAJUSTE) private Integer kajuste;
	@PortableProperty(Periodo.IND_CTIPOAPORT) private String ctipoaport;
	
	public PeriodoKey() {
		super();
	}
	
	public PeriodoKey(Timestamp fechaInicio, Timestamp fechaFin, String cnegocio, Integer ccanal, Integer ccartera, Timestamp fcierre, Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri, Integer norden, Integer kgarantia, String kprestacion, Integer kajuste, String ctipoaport) {
		
		super();
		this.fechaInicio =fechaInicio;
		this.fechaFin =fechaFin;
		this.cnegocio =cnegocio;
		this.ccanal =ccanal;
		this.ccartera =ccartera;
		this.fcierre =fcierre;
		this.kmodalidad =kmodalidad;
		this.kpoliza =kpoliza;
		this.ksubpoliza =ksubpoliza;
		this.kcertificado =kcertificado;
		this.nsuscri =nsuscri;
		this.norden =norden;
		this.kgarantia =kgarantia;
		this.kprestacion =kprestacion;
		this.kajuste =kajuste;
		this.ctipoaport =ctipoaport;

	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result
				+ ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result
				+ ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result
				+ ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result
				+ ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result
				+ ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
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
		PeriodoKey other = (PeriodoKey) obj;
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
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (ctipoaport == null) {
			if (other.ctipoaport != null) {
				return false;
			}
		} else if (!ctipoaport.equals(other.ctipoaport)) {
			return false;
		}
		if (fcierre == null) {
			if (other.fcierre != null) {
				return false;
			}
		} else if (!fcierre.equals(other.fcierre)) {
			return false;
		}
		if (fechaFin == null) {
			if (other.fechaFin != null) {
				return false;
			}
		} else if (!fechaFin.equals(other.fechaFin)) {
			return false;
		}
		if (fechaInicio == null) {
			if (other.fechaInicio != null) {
				return false;
			}
		} else if (!fechaInicio.equals(other.fechaInicio)) {
			return false;
		}
		if (kajuste == null) {
			if (other.kajuste != null) {
				return false;
			}
		} else if (!kajuste.equals(other.kajuste)) {
			return false;
		}
		if (kcertificado == null) {
			if (other.kcertificado != null) {
				return false;
			}
		} else if (!kcertificado.equals(other.kcertificado)) {
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
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (kprestacion == null) {
			if (other.kprestacion != null) {
				return false;
			}
		} else if (!kprestacion.equals(other.kprestacion)) {
			return false;
		}
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null) {
				return false;
			}
		} else if (!ksubpoliza.equals(other.ksubpoliza)) {
			return false;
		}
		if (norden == null) {
			if (other.norden != null) {
				return false;
			}
		} else if (!norden.equals(other.norden)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		return true;
	}
}