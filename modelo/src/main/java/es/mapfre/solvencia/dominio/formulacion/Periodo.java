package es.mapfre.solvencia.dominio.formulacion;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.formulacion.PeriodoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class Periodo implements EntidadBase<PeriodoKey> {
	
	public static final int IND_FECHAINICIO = 0;
	public static final int IND_FECHAFIN = 1;
	public static final int IND_IMPORTE = 2;
	public static final int IND_FECHAPAGO = 3;
	public static final int IND_CNEGOCIO = 4;
	public static final int IND_CCANAL = 5;
	public static final int IND_CCARTERA = 6;
	public static final int IND_FCIERRE = 7;
	public static final int IND_BT = 8;
	public static final int IND_KMODALIDAD = 9;
	public static final int IND_KPOLIZA = 10;
	public static final int IND_SUBKPOLIZA = 11;
	public static final int IND_KCERTIFICADO = 12;
	public static final int IND_NSUSCRI = 13;
	public static final int IND_NORDEN = 14;
	public static final int IND_KGARANTIA = 15;
	public static final int IND_KPRESTACION = 16;
	public static final int IND_KAJUSTE = 17;
	public static final int IND_CTIPOAPORT = 18;
	
	@PortableProperty(IND_FECHAINICIO) private Timestamp fechaInicio;
	@PortableProperty(IND_FECHAFIN) private Timestamp fechaFin;	
	@PortableProperty(IND_IMPORTE) private java.math.BigDecimal importe;
	@PortableProperty(IND_FECHAPAGO) private Timestamp fechaPago;
	@PortableProperty(IND_CNEGOCIO) private String cnegocio;
	@PortableProperty(IND_CCANAL) private Integer ccanal;
	@PortableProperty(IND_CCARTERA) private Integer ccartera;
	@PortableProperty(IND_FCIERRE) private Timestamp fcierre;
	@PortableProperty(IND_BT) private String bt;
	@PortableProperty(IND_KMODALIDAD) private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA) private Long kpoliza;
	@PortableProperty(IND_SUBKPOLIZA) private Integer ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO) private Integer kcertificado;
	@PortableProperty(IND_NSUSCRI) private Integer nsuscri;
	@PortableProperty(IND_NORDEN) private Integer norden;
	@PortableProperty(IND_KGARANTIA) private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION) private String kprestacion;
	@PortableProperty(IND_KAJUSTE) private Integer kajuste;
	@PortableProperty(IND_CTIPOAPORT) private String ctipoaport;

	@Override
	public PeriodoKey getKey() {
		return new PeriodoKey(fechaInicio, fechaFin , cnegocio, ccanal, ccartera, fcierre, kmodalidad, kpoliza, ksubpoliza, kcertificado, nsuscri, norden, kgarantia, kprestacion, kajuste, ctipoaport);
	}
	
	public Periodo() {
		super();
	}
	
	public Periodo(final Timestamp fechaInicio, final Timestamp fechaFin, final java.math.BigDecimal importe, final Timestamp fechaPago) {
		super();
		this.fechaInicio = fechaInicio;
		this.fechaFin = fechaFin;
		this.importe = importe;
		this.fechaPago = fechaPago;
	}

	public Timestamp getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(final Timestamp fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Timestamp getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(final Timestamp fechaFin) {
		this.fechaFin = fechaFin;
	}
	public java.math.BigDecimal getImporte() {
		return importe;
	}
	public void setImporte(final java.math.BigDecimal importe) {
		this.importe = importe;
	}

	public Timestamp getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(final Timestamp fechaPago) {
		this.fechaPago = fechaPago;
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

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
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

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
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
				+ ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result
				+ ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
		result = prime * result
				+ ((fechaPago == null) ? 0 : fechaPago.hashCode());
		result = prime * result + ((importe == null) ? 0 : importe.hashCode());
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
		Periodo other = (Periodo) obj;
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
		if (fechaPago == null) {
			if (other.fechaPago != null) {
				return false;
			}
		} else if (!fechaPago.equals(other.fechaPago)) {
			return false;
		}
		if (importe == null) {
			if (other.importe != null) {
				return false;
			}
		} else if (!importe.equals(other.importe)) {
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