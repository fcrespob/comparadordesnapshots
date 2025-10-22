package es.mapfre.solvencia.coherence.keys.salidaCalculo;

import java.beans.Transient;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;

@Portable
public class DetalleCorrienteKey {

	@PortableProperty(DetalleCorriente.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(DetalleCorriente.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(DetalleCorriente.IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(DetalleCorriente.IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(DetalleCorriente.IND_BT)
	private String bt;
	@PortableProperty(DetalleCorriente.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(DetalleCorriente.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(DetalleCorriente.IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(DetalleCorriente.IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(DetalleCorriente.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(DetalleCorriente.IND_NORDEN)
	private Integer norden;
	@PortableProperty(DetalleCorriente.IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(DetalleCorriente.IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(DetalleCorriente.IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(DetalleCorriente.IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(DetalleCorriente.IND_FECHADESDE)
	private Timestamp fechaDesde;
	@PortableProperty(DetalleCorriente.IND_FECHAHASTA)
	private Timestamp fechaHasta;

	private UmicKey umicKey = null;
	
	@Transient
	public UmicKey getUmicKey() {
		if (umicKey == null) {
			umicKey = new UmicKey(ctipoaport, kajuste, kcertificado, kgarantia, kmodalidad, kpoliza, kprestacion, ksubpoliza, norden, nsuscri);
		}
		return umicKey;
	}
	
	public Timestamp getFechaDesde() {
		return fechaDesde;
	}

	public String getBt() {
		return bt;
	}

	public DetalleCorrienteKey() {
		super();
	}

	public DetalleCorrienteKey(String cnegocio, Integer ccanal,
			Timestamp fcierre, String bt, Integer kmodalidad, Long kpoliza,
			Integer ksubpoliza, Integer kcertificado, Integer nsuscri,
			Integer norden, Integer kgarantia, String kprestacion,
			Integer kajuste, String ctipoaport, Timestamp fechaDesde,
			Timestamp fechaHasta) {

		super();
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.fcierre = fcierre;
		this.bt = bt;
		this.kmodalidad = kmodalidad;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.kcertificado = kcertificado;
		this.nsuscri = nsuscri;
		this.norden = norden;
		this.kgarantia = kgarantia;
		this.kprestacion = kprestacion;
		this.kajuste = kajuste;
		this.ctipoaport = ctipoaport;
		this.fechaDesde = fechaDesde;
		this.fechaHasta = fechaHasta;

	}

	@Override//NOSONAR
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result
				+ ((fechaDesde == null) ? 0 : fechaDesde.hashCode());
		result = prime * result
				+ ((fechaHasta == null) ? 0 : fechaHasta.hashCode());
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

	@Override//NOSONAR
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
		DetalleCorrienteKey other = (DetalleCorrienteKey) obj;
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
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
		if (fechaDesde == null) {
			if (other.fechaDesde != null) {
				return false;
			}
		} else if (!fechaDesde.equals(other.fechaDesde)) {
			return false;
		}
		if (fechaHasta == null) {
			if (other.fechaHasta != null) {
				return false;
			}
		} else if (!fechaHasta.equals(other.fechaHasta)) {
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

	@Override
	public String toString() {
		return "DetalleCorrienteKey [cnegocio=" + cnegocio + ", ccanal="
				+ ccanal + ", ccartera=" + ccartera + ", fcierre=" + fcierre
				+ ", bt=" + bt + ", kmodalidad=" + kmodalidad + ", kpoliza="
				+ kpoliza + ", ksubpoliza=" + ksubpoliza + ", kcertificado="
				+ kcertificado + ", nsuscri=" + nsuscri + ", norden=" + norden
				+ ", kgarantia=" + kgarantia + ", kprestacion=" + kprestacion
				+ ", kajuste=" + kajuste + ", ctipoaport=" + ctipoaport
				+ ", fechaDesde=" + fechaDesde + ", fechaHasta=" + fechaHasta
				+ "]";
	}
	
//	public TotalesFlujosKey toTotalesFlujosKey() {
//		return new TotalesFlujosKey(kajuste, kcertificado, kgarantia, kmodalidad, kpoliza, kprestacion, ksubpoliza, bt, nsuscri, norden, ctipoaport)
//	}

	
	
}