package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.GastosReales;

@Portable
public class GastosRealesKey {

	@PortableProperty(GastosReales.IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(GastosReales.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(GastosReales.IND_FECDESDE)
	private Timestamp fecDesde;
	@PortableProperty(GastosReales.IND_FECHASTA)
	private Timestamp fecHasta;
	// TODO: Check modalidad en la clave?
	@PortableProperty(GastosReales.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(GastosReales.IND_KRAMO)
	private String kramo;
	@PortableProperty(GastosReales.IND_KTIPOBT)
	private String ktipobt;
	@PortableProperty(GastosReales.IND_MATCHING)
	private String matching;

	public GastosRealesKey() {
		super();
	}

	public GastosRealesKey(Integer ccanal, String cnegocio, Timestamp fecDesde,
			Timestamp fecHasta, Integer kmodalidad, String kramo, String ktipobt, String matching) {

		super();
		this.ccanal = ccanal;
		this.cnegocio = cnegocio;
		this.fecDesde = fecDesde;
		this.fecHasta = fecHasta;
		this.kmodalidad = kmodalidad;
		this.kramo = kramo;
		this.ktipobt = ktipobt;
		this.matching = matching;
	}
	
	

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result
				+ ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result
				+ ((fecDesde == null) ? 0 : fecDesde.hashCode());
		result = prime * result
				+ ((fecHasta == null) ? 0 : fecHasta.hashCode());
		result = prime * result
				+ ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ktipobt == null) ? 0 : ktipobt.hashCode());
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
		GastosRealesKey other = (GastosRealesKey) obj;
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
		if (fecDesde == null) {
			if (other.fecDesde != null) {
				return false;
			}
		} else if (!fecDesde.equals(other.fecDesde)) {
			return false;
		}
		if (fecHasta == null) {
			if (other.fecHasta != null) {
				return false;
			}
		} else if (!fecHasta.equals(other.fecHasta)) {
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
		if (ktipobt == null) {
			if (other.ktipobt != null) {
				return false;
			}
		} else if (!ktipobt.equals(other.ktipobt)) {
			return false;
		}
		if (matching == null) {
			if (other.matching != null) {
				return false;
			}
		} else if (!matching.equals(other.matching)) {
			return false;
		}
		return true;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public Timestamp getFecDesde() {
		return fecDesde;
	}

	public void setFecDesde(Timestamp fecDesde) {
		this.fecDesde = fecDesde;
	}

	public Timestamp getFecHasta() {
		return fecHasta;
	}

	public void setFecHasta(Timestamp fecHasta) {
		this.fecHasta = fecHasta;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public String getKtipobt() {
		return ktipobt;
	}

	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}
	
	public String getMatching() {
		return matching;
	}

	public void setMatching(String matching) {
		this.matching = matching;
	}

}