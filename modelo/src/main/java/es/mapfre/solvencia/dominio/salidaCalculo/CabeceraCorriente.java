package es.mapfre.solvencia.dominio.salidaCalculo;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class CabeceraCorriente {
	private String tipoCorriente;
	private Timestamp fechaDesde;
	private Timestamp fechaHasta;	
	private String cnegocio;
	private Integer ccanal;
	private Integer ccartera;
	private Timestamp fcierre;
	private String bt;
	private Integer kmodalidad;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer kcertificado;
	private Integer nsuscri;
	private Integer norden;
	private Integer kgarantia;
	private String kprestacion;
	private Integer kajuste;
	private String ctipoaport;
	private BigDecimal impPago;
	
	public BigDecimal getImpPago() {
		return impPago;
	}
	public void setImpPago(BigDecimal impPago) {
		this.impPago = impPago;
	}
	public String getTipoCorriente() {
		return tipoCorriente;
	}
	public void setTipoCorriente(String tipoCorriente) {
		this.tipoCorriente = tipoCorriente;
	}
	public Timestamp getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Timestamp fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public Timestamp getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Timestamp fechaHasta) {
		this.fechaHasta = fechaHasta;
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
		result = prime * result
				+ ((tipoCorriente == null) ? 0 : tipoCorriente.hashCode());
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
		CabeceraCorriente other = (CabeceraCorriente) obj;
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
		if (tipoCorriente == null) {
			if (other.tipoCorriente != null) {
				return false;
			}
		} else if (!tipoCorriente.equals(other.tipoCorriente)) {
			return false;
		}
		return true;
	} 
	
	
}
