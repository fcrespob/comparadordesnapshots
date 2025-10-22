package es.mapfre.solvencia.coherence.keys.maestro;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.dataaffinity.maestro.AssociatedKeyMaestro;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;

@Portable
public class UmicKey implements Comparable<UmicKey> {
	@PortableProperty(DatosGenerales.IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(DatosGenerales.IND_EKAJUSTE)
	private Integer kajuste;
	@PortableProperty(DatosGenerales.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(DatosGenerales.IND_EKGARANTIA)
	private Integer kgarantia;
	@PortableProperty(DatosGenerales.IND_EKMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(DatosGenerales.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(DatosGenerales.IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(DatosGenerales.IND_EKSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(DatosGenerales.IND_ENORDEN)
	private Integer norden;
	@PortableProperty(DatosGenerales.IND_ENSUSCRI)
	private Integer nsuscri;

	@Override // NOSONAR
	public int hashCode() { // NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		return result;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	@Override // NOSONAR
	public boolean equals(Object obj) { // NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		UmicKey other = (UmicKey) obj;
		if (ctipoaport == null) {
			if (other.ctipoaport != null) {
				return false;
			}
		} else if (!ctipoaport.equals(other.ctipoaport)) {
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

	public UmicKey(String ctipoaport, Integer kajuste, Integer kcertificado, Integer kgarantia, Integer kmodalidad,
			Long kpoliza, String kprestacion, Integer ksubpoliza, Integer norden, Integer nsuscri) {
		super();
		this.ctipoaport = ctipoaport;
		this.kajuste = kajuste;
		this.kcertificado = kcertificado;
		this.kgarantia = kgarantia;
		this.kmodalidad = kmodalidad;
		this.kpoliza = kpoliza;
		this.kprestacion = kprestacion;
		this.ksubpoliza = ksubpoliza;
		this.norden = norden;
		this.nsuscri = nsuscri;
	}

	public UmicKey() {
		super();
	}

	@Override
	public String toString() {
		return "UmicKey [ctipoaport=" + ctipoaport + ", kajuste=" + kajuste + ", kcertificado=" + kcertificado
				+ ", kgarantia=" + kgarantia + ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", kprestacion="
				+ kprestacion + ", ksubpoliza=" + ksubpoliza + ", norden=" + norden + ", nsuscri=" + nsuscri + "]";
	}

	public AssociatedKeyMaestro getAssociatedKeyMaestro() {
		return new AssociatedKeyMaestro(kpoliza, ksubpoliza, /*kgarantia,*/ kajuste, nsuscri, kcertificado, kprestacion);
	}

	/**
	 * El orden es:
	 * 
	 * <pre>
	 * -	POLIZA
	 * -	SUBPOLIZA
	 * -	CERTIFICADO
	 * -	SUSCRIPCION
	 * -	ORDEN
	 * -	GARANTIA
	 * -	PRESTACION
	 * -	AJUSTE
	 * -	TIPO APORTACION
	 * </pre>
	 * 
	 * @param o
	 * @return
	 */
	public int compareTo(UmicKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		// El orden se debe mantener
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.kcertificado, o.kcertificado);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.norden, o.norden);
		compareToBuilder.append(this.kgarantia, o.kgarantia);
		compareToBuilder.append(this.kprestacion, o.kprestacion);
		compareToBuilder.append(this.kajuste, o.kajuste);
		compareToBuilder.append(this.ctipoaport, o.ctipoaport);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);

		return compareToBuilder.toComparison();
	}
}
