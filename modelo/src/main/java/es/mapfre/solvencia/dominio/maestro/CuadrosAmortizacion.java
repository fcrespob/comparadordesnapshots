package es.mapfre.solvencia.dominio.maestro;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.CuadrosAmortizacionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class CuadrosAmortizacion implements EntidadBase<CuadrosAmortizacionKey> {

	public static final int IND_CAPITALACT = 0;
	public static final int IND_CAPITALINI = 1;
	public static final int IND_CCANAL = 2;
	public static final int IND_CFORMCUOTA = 3;
	public static final int IND_CNEGOCIO = 4;
	public static final int IND_CPERAMONT = 5;
	public static final int IND_CPERASEG = 6;
	public static final int IND_CSISAMORT = 7;
	public static final int IND_FDESEMBOLSO = 9;

	public static final int IND_KAJUSTE = 10;
	public static final int IND_KCERTIFICADO = 11;
	public static final int IND_KGARANTIA = 12;
	public static final int IND_KMODALIDAD = 13;
	public static final int IND_KPOLIZA = 14;
	public static final int IND_KPRESTACION = 15;
	public static final int IND_KSUBPOLIZA = 16;
	public static final int IND_NORDEN = 17;
	public static final int IND_NPERCAR = 18;
	public static final int IND_NSUSCRI = 19;
	public static final int IND_PINTERMOR = 20;
	public static final int IND_PORASEG = 21;
	public static final int IND_PORCREC = 22;

	@PortableProperty(IND_CAPITALACT)
	private java.math.BigDecimal capitalAct;
	@PortableProperty(IND_CAPITALINI)
	private java.math.BigDecimal capitalIni;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_CFORMCUOTA)
	private String cformcuota;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CPERAMONT)
	private String cperamont;
	@PortableProperty(IND_CPERASEG)
	private Integer cperaseg;
	@PortableProperty(IND_CSISAMORT)
	private String csisamort;
	
	@PortableProperty(IND_FDESEMBOLSO)
	private Timestamp fdesembolso;

	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NORDEN)
	private Integer norden;
	@PortableProperty(IND_NPERCAR)
	private Integer npercar;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_PINTERMOR)
	private java.math.BigDecimal pintermor;
	@PortableProperty(IND_PORASEG)
	private java.math.BigDecimal poraseg;
	@PortableProperty(IND_PORCREC)
	private java.math.BigDecimal porcrec;

	public java.math.BigDecimal getCapitalAct() {
		return capitalAct;
	}

	public void setCapitalAct(java.math.BigDecimal capitalAct) {
		this.capitalAct = capitalAct;
	}

	public java.math.BigDecimal getCapitalIni() {
		return capitalIni;
	}

	public void setCapitalIni(java.math.BigDecimal capitalIni) {
		this.capitalIni = capitalIni;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getCformcuota() {
		return cformcuota;
	}

	public void setCformcuota(String cformcuota) {
		this.cformcuota = cformcuota;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getCperamont() {
		return cperamont;
	}

	public void setCperamont(String cperamont) {
		this.cperamont = cperamont;
	}

	public Integer getCperaseg() {
		return cperaseg;
	}

	public void setCperaseg(Integer cperaseg) {
		this.cperaseg = cperaseg;
	}

	public String getCsisamort() {
		return csisamort;
	}

	public void setCsisamort(String csisamort) {
		this.csisamort = csisamort;
	}

	public Timestamp getFdesembolso() {
		return fdesembolso;
	}

	public void setFdesembolso(Timestamp fdesembolso) {
		this.fdesembolso = fdesembolso;
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

	public Integer getNpercar() {
		return npercar;
	}

	public void setNpercar(Integer npercar) {
		this.npercar = npercar;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public java.math.BigDecimal getPintermor() {
		return pintermor;
	}

	public void setPintermor(java.math.BigDecimal pintermor) {
		this.pintermor = pintermor;
	}

	public java.math.BigDecimal getPoraseg() {
		return poraseg;
	}

	public void setPoraseg(java.math.BigDecimal poraseg) {
		this.poraseg = poraseg;
	}

	public java.math.BigDecimal getPorcrec() {
		return porcrec;
	}

	public void setPorcrec(java.math.BigDecimal porcrec) {
		this.porcrec = porcrec;
	}

	@Override
	public CuadrosAmortizacionKey getKey() {
		return new CuadrosAmortizacionKey(ksubpoliza, kajuste, kgarantia, kmodalidad, norden, nsuscri, kpoliza, ccanal, cnegocio,/* ctipoaport,*/ kcertificado, kprestacion);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((capitalAct == null) ? 0 : capitalAct.hashCode());
		result = prime * result
				+ ((capitalIni == null) ? 0 : capitalIni.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((cformcuota == null) ? 0 : cformcuota.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((cperamont == null) ? 0 : cperamont.hashCode());
		result = prime * result
				+ ((cperaseg == null) ? 0 : cperaseg.hashCode());
		result = prime * result
				+ ((csisamort == null) ? 0 : csisamort.hashCode());
		result = prime * result
				+ ((fdesembolso == null) ? 0 : fdesembolso.hashCode());
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
		result = prime * result + ((npercar == null) ? 0 : npercar.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result
				+ ((pintermor == null) ? 0 : pintermor.hashCode());
		result = prime * result + ((poraseg == null) ? 0 : poraseg.hashCode());
		result = prime * result + ((porcrec == null) ? 0 : porcrec.hashCode());
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
		CuadrosAmortizacion other = (CuadrosAmortizacion) obj;
		if (capitalAct == null) {
			if (other.capitalAct != null) {
				return false;
			}
		} else if (!capitalAct.equals(other.capitalAct)) {
			return false;
		}
		if (capitalIni == null) {
			if (other.capitalIni != null) {
				return false;
			}
		} else if (!capitalIni.equals(other.capitalIni)) {
			return false;
		}
		if (ccanal == null) {
			if (other.ccanal != null) {
				return false;
			}
		} else if (!ccanal.equals(other.ccanal)) {
			return false;
		}
		if (cformcuota == null) {
			if (other.cformcuota != null) {
				return false;
			}
		} else if (!cformcuota.equals(other.cformcuota)) {
			return false;
		}
		if (cnegocio == null) {
			if (other.cnegocio != null) {
				return false;
			}
		} else if (!cnegocio.equals(other.cnegocio)) {
			return false;
		}
		if (cperamont == null) {
			if (other.cperamont != null) {
				return false;
			}
		} else if (!cperamont.equals(other.cperamont)) {
			return false;
		}
		if (cperaseg == null) {
			if (other.cperaseg != null) {
				return false;
			}
		} else if (!cperaseg.equals(other.cperaseg)) {
			return false;
		}
		if (csisamort == null) {
			if (other.csisamort != null) {
				return false;
			}
		} else if (!csisamort.equals(other.csisamort)) {
			return false;
		}
		if (fdesembolso == null) {
			if (other.fdesembolso != null) {
				return false;
			}
		} else if (!fdesembolso.equals(other.fdesembolso)) {
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
		if (npercar == null) {
			if (other.npercar != null) {
				return false;
			}
		} else if (!npercar.equals(other.npercar)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		if (pintermor == null) {
			if (other.pintermor != null) {
				return false;
			}
		} else if (!pintermor.equals(other.pintermor)) {
			return false;
		}
		if (poraseg == null) {
			if (other.poraseg != null) {
				return false;
			}
		} else if (!poraseg.equals(other.poraseg)) {
			return false;
		}
		if (porcrec == null) {
			if (other.porcrec != null) {
				return false;
			}
		} else if (!porcrec.equals(other.porcrec)) {
			return false;
		}
		return true;
	}
}