package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.LimitesCapitalKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class LimitesCapital implements EntidadBase<LimitesCapitalKey> {
	
	public static final int IND_NTABLA = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_KGARANTIA = 2;
	public static final int IND_FEFECFIN = 3;
	public static final int IND_KEDAD1 = 4;
	public static final int IND_KEDAD2 = 5;
	public static final int IND_NMESHASTA = 6;
	public static final int IND_GSPACES1 = 7;
	public static final int IND_NREGISTRO = 8;
	public static final int IND_PORGAMMA = 9;

	public static final int IND_ECAPHASTA = 10;
	public static final int IND_ECAPHASTAAGRA = 11;
	public static final int IND_ECAPDESDENOR = 12;
	public static final int IND_ECAPDESDEAGRA = 13;
	public static final int IND_CMONEDA = 14;

	@PortableProperty(IND_NTABLA)
	private String ntabla;
	@PortableProperty(IND_KMODALIDAD)
	private String kmodalidad;
	@PortableProperty(IND_KGARANTIA)
	private String kgarantia;
	@PortableProperty(IND_FEFECFIN)
	private Timestamp fefecfin;
	@PortableProperty(IND_KEDAD1)
	private Integer kedad1;
	@PortableProperty(IND_KEDAD2)
	private Integer kedad2;
	@PortableProperty(IND_NMESHASTA)
	private Integer nmeshasta;
	@PortableProperty(IND_GSPACES1)
	private String gspaces1;
	@PortableProperty(IND_NREGISTRO)
	private String nregistro;
	@PortableProperty(IND_PORGAMMA)
	private java.math.BigDecimal porgamma;

	@PortableProperty(IND_ECAPHASTA)
	private java.math.BigDecimal ecaphasta;
	@PortableProperty(IND_ECAPHASTAAGRA)
	private java.math.BigDecimal ecaphastaAgra;
	@PortableProperty(IND_ECAPDESDENOR)
	private java.math.BigDecimal ecapdesdeNor;
	@PortableProperty(IND_ECAPDESDEAGRA)
	private java.math.BigDecimal ecapdesdeAgra;
	@PortableProperty(IND_CMONEDA)
	private String cmoneda;

	public String getNtabla() {
		return ntabla;
	}

	public void setNtabla(String ntabla) {
		this.ntabla = ntabla;
	}

	public String getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(String kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(String kgarantia) {
		this.kgarantia = kgarantia;
	}

	public Timestamp getFefecfin() {
		return fefecfin;
	}

	public void setFefecfin(Timestamp fefecfin) {
		this.fefecfin = fefecfin;
	}

	public Integer getKedad1() {
		return kedad1;
	}

	public void setKedad1(Integer kedad1) {
		this.kedad1 = kedad1;
	}

	public Integer getKedad2() {
		return kedad2;
	}

	public void setKedad2(Integer kedad2) {
		this.kedad2 = kedad2;
	}

	public Integer getNmeshasta() {
		return nmeshasta;
	}

	public void setNmeshasta(Integer nmeshasta) {
		this.nmeshasta = nmeshasta;
	}

	public String getGspaces1() {
		return gspaces1;
	}

	public void setGspaces1(String gspaces1) {
		this.gspaces1 = gspaces1;
	}

	public String getNregistro() {
		return nregistro;
	}

	public void setNregistro(String nregistro) {
		this.nregistro = nregistro;
	}

	public java.math.BigDecimal getPorgamma() {
		return porgamma;
	}

	public void setPorgamma(java.math.BigDecimal porgamma) {
		this.porgamma = porgamma;
	}

	public java.math.BigDecimal getEcaphasta() {
		return ecaphasta;
	}

	public void setEcaphasta(java.math.BigDecimal ecaphasta) {
		this.ecaphasta = ecaphasta;
	}

	public java.math.BigDecimal getEcaphastaAgra() {
		return ecaphastaAgra;
	}

	public void setEcaphastaAgra(java.math.BigDecimal ecaphastaAgra) {
		this.ecaphastaAgra = ecaphastaAgra;
	}

	public java.math.BigDecimal getEcapdesdeNor() {
		return ecapdesdeNor;
	}

	public void setEcapdesdeNor(java.math.BigDecimal ecapdesdeNor) {
		this.ecapdesdeNor = ecapdesdeNor;
	}

	public java.math.BigDecimal getEcapdesdeAgra() {
		return ecapdesdeAgra;
	}

	public void setEcapdesdeAgra(java.math.BigDecimal ecapdesdeAgra) {
		this.ecapdesdeAgra = ecapdesdeAgra;
	}

	public String getCmoneda() {
		return cmoneda;
	}

	public void setCmoneda(String cmoneda) {
		this.cmoneda = cmoneda;
	}

	@Override
	public LimitesCapitalKey getKey() {
		return new LimitesCapitalKey(kmodalidad, kgarantia, fefecfin, kedad1,
				kedad2, nmeshasta, gspaces1, nregistro);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cmoneda == null) ? 0 : cmoneda.hashCode());
		result = prime * result
				+ ((ecapdesdeAgra == null) ? 0 : ecapdesdeAgra.hashCode());
		result = prime * result
				+ ((ecapdesdeNor == null) ? 0 : ecapdesdeNor.hashCode());
		result = prime * result
				+ ((ecaphasta == null) ? 0 : ecaphasta.hashCode());
		result = prime * result
				+ ((ecaphastaAgra == null) ? 0 : ecaphastaAgra.hashCode());
		result = prime * result
				+ ((fefecfin == null) ? 0 : fefecfin.hashCode());
		result = prime * result
				+ ((gspaces1 == null) ? 0 : gspaces1.hashCode());
		result = prime * result + ((kedad1 == null) ? 0 : kedad1.hashCode());
		result = prime * result + ((kedad2 == null) ? 0 : kedad2.hashCode());
		result = prime * result
				+ ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result
				+ ((nmeshasta == null) ? 0 : nmeshasta.hashCode());
		result = prime * result
				+ ((nregistro == null) ? 0 : nregistro.hashCode());
		result = prime * result + ((ntabla == null) ? 0 : ntabla.hashCode());
		result = prime * result
				+ ((porgamma == null) ? 0 : porgamma.hashCode());
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
		LimitesCapital other = (LimitesCapital) obj;
		if (cmoneda == null) {
			if (other.cmoneda != null) {
				return false;
			}
		} else if (!cmoneda.equals(other.cmoneda)) {
			return false;
		}
		if (ecapdesdeAgra == null) {
			if (other.ecapdesdeAgra != null) {
				return false;
			}
		} else if (!ecapdesdeAgra.equals(other.ecapdesdeAgra)) {
			return false;
		}
		if (ecapdesdeNor == null) {
			if (other.ecapdesdeNor != null) {
				return false;
			}
		} else if (!ecapdesdeNor.equals(other.ecapdesdeNor)) {
			return false;
		}
		if (ecaphasta == null) {
			if (other.ecaphasta != null) {
				return false;
			}
		} else if (!ecaphasta.equals(other.ecaphasta)) {
			return false;
		}
		if (ecaphastaAgra == null) {
			if (other.ecaphastaAgra != null) {
				return false;
			}
		} else if (!ecaphastaAgra.equals(other.ecaphastaAgra)) {
			return false;
		}
		if (fefecfin == null) {
			if (other.fefecfin != null) {
				return false;
			}
		} else if (!fefecfin.equals(other.fefecfin)) {
			return false;
		}
		if (gspaces1 == null) {
			if (other.gspaces1 != null) {
				return false;
			}
		} else if (!gspaces1.equals(other.gspaces1)) {
			return false;
		}
		if (kedad1 == null) {
			if (other.kedad1 != null) {
				return false;
			}
		} else if (!kedad1.equals(other.kedad1)) {
			return false;
		}
		if (kedad2 == null) {
			if (other.kedad2 != null) {
				return false;
			}
		} else if (!kedad2.equals(other.kedad2)) {
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
		if (nmeshasta == null) {
			if (other.nmeshasta != null) {
				return false;
			}
		} else if (!nmeshasta.equals(other.nmeshasta)) {
			return false;
		}
		if (nregistro == null) {
			if (other.nregistro != null) {
				return false;
			}
		} else if (!nregistro.equals(other.nregistro)) {
			return false;
		}
		if (ntabla == null) {
			if (other.ntabla != null) {
				return false;
			}
		} else if (!ntabla.equals(other.ntabla)) {
			return false;
		}
		if (porgamma == null) {
			if (other.porgamma != null) {
				return false;
			}
		} else if (!porgamma.equals(other.porgamma)) {
			return false;
		}
		return true;
	}
}