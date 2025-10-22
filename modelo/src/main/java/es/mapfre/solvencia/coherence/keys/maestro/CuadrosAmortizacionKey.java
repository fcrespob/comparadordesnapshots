package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.dataaffinity.maestro.AssociatedKeyMaestro;
import es.mapfre.solvencia.dominio.maestro.CuadrosAmortizacion;

@Portable
public class CuadrosAmortizacionKey {

	@PortableProperty(CuadrosAmortizacion.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(CuadrosAmortizacion.IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(CuadrosAmortizacion.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(CuadrosAmortizacion.IND_NORDEN)
	private Integer norden;
	@PortableProperty(CuadrosAmortizacion.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(CuadrosAmortizacion.IND_KPOLIZA)
	private Long kpoliza;
	
	@PortableProperty(CuadrosAmortizacion.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(CuadrosAmortizacion.IND_KPRESTACION)
	private String kprestacion;

	@PortableProperty(CuadrosAmortizacion.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(CuadrosAmortizacion.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(CuadrosAmortizacion.IND_CNEGOCIO)
	private String cnegocio;
	
	public CuadrosAmortizacionKey(Integer ksubpoliza, Integer kajuste,
			Integer kgarantia, Integer kmodalidad, Integer norden,
			Integer nsuscri, Long kpoliza, Integer ccanal, String cnegocio,
			 Integer kcertificado, String kprestacion) {

		this.ksubpoliza = ksubpoliza;
		this.kajuste = kajuste;
		this.kgarantia = kgarantia;
		this.kmodalidad = kmodalidad;
		this.norden = norden;
		this.nsuscri = nsuscri;
		this.kpoliza = kpoliza;
		this.ccanal = ccanal;
		this.cnegocio = cnegocio;
	
		this.kcertificado = kcertificado;
		this.kprestacion = kprestacion;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
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
		CuadrosAmortizacionKey other = (CuadrosAmortizacionKey) obj;
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
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

	public AssociatedKeyMaestro getAssociatedKeyMaestro(){
		return new AssociatedKeyMaestro(kpoliza, ksubpoliza, kajuste, nsuscri, kcertificado, kprestacion);
	}
}
