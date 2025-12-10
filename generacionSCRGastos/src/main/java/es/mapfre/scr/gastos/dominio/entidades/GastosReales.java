package es.mapfre.scr.gastos.dominio.entidades;

import es.mapfre.scr.gastos.dominio.EntidadBase;
import es.mapfre.scr.gastos.dominio.keys.GastosRealesKey;

public class GastosReales implements EntidadBase<GastosRealesKey> {
	
	public static final int IND_CNEGOCIO = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_KRAMO = 2;
	public static final int IND_KTIPOBT = 3;
	public static final int IND_CCANAL = 4;
	public static final int IND_FECHASTA = 5;
	public static final int IND_FECDESDE = 6;
	public static final int IND_GASTOSADMINCONTAB = 7;
	public static final int IND_GASTOSADMINSINPROV = 8;
	public static final int IND_GASTOPORUMIC = 9;
	public static final int IND_NUMUMICCOMPRAR = 10;
	public static final int IND_NUMUMICREF = 11;
	public static final int IND_PCTGASTOPROV = 12;
	public static final int IND_PROVMATCOMPARAR = 13;
	public static final int IND_PROVMATREF = 14;
	public static final int IND_MATCHING = 15;

	private Integer ccanal;
	private String cnegocio;
	private String fecDesde;
	private String fecHasta;
	private java.math.BigDecimal gastosAdminContab;
	private java.math.BigDecimal gastosAdminSinProv;
	private java.math.BigDecimal gastoPorUmic;
	private Integer kmodalidad;
	private String kramo;
	private String ktipobt;
	private Integer numUmicComprar;
	private Integer numUmicRef;
	private java.math.BigDecimal pctGastoProv;
	private java.math.BigDecimal provMatComparar;
	private java.math.BigDecimal provMatRef;
	private String matching;

	@Override
	public GastosRealesKey getKey() {
		return new GastosRealesKey(ccanal, cnegocio, fecDesde, fecHasta, kmodalidad, kramo, ktipobt, matching);
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

	public String getFecDesde() {
		return fecDesde;
	}

	public void setFecDesde(String fecDesde) {
		this.fecDesde = fecDesde;
	}

	public String getFecHasta() {
		return fecHasta;
	}

	public void setFecHasta(String fecHasta) {
		this.fecHasta = fecHasta;
	}

	public java.math.BigDecimal getGastosAdminContab() {
		return gastosAdminContab;
	}

	public void setGastosAdminContab(java.math.BigDecimal gastosAdminContab) {
		this.gastosAdminContab = gastosAdminContab;
	}

	public java.math.BigDecimal getGastosAdminSinProv() {
		return gastosAdminSinProv;
	}

	public void setGastosAdminSinProv(java.math.BigDecimal gastosAdminSinProv) {
		this.gastosAdminSinProv = gastosAdminSinProv;
	}

	public java.math.BigDecimal getGastoPorUmic() {
		return gastoPorUmic;
	}

	public void setGastoPorUmic(java.math.BigDecimal gastoPorUmic) {
		this.gastoPorUmic = gastoPorUmic;
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

	public Integer getNumUmicComprar() {
		return numUmicComprar;
	}

	public void setNumUmicComprar(Integer numUmicComprar) {
		this.numUmicComprar = numUmicComprar;
	}

	public Integer getNumUmicRef() {
		return numUmicRef;
	}

	public void setNumUmicRef(Integer numUmicRef) {
		this.numUmicRef = numUmicRef;
	}

	public java.math.BigDecimal getPctGastoProv() {
		return pctGastoProv;
	}

	public void setPctGastoProv(java.math.BigDecimal pctGastoProv) {
		this.pctGastoProv = pctGastoProv;
	}

	public java.math.BigDecimal getProvMatComparar() {
		return provMatComparar;
	}

	public void setProvMatComparar(java.math.BigDecimal provMatComparar) {
		this.provMatComparar = provMatComparar;
	}

	public java.math.BigDecimal getProvMatRef() {
		return provMatRef;
	}

	public void setProvMatRef(java.math.BigDecimal provMatRef) {
		this.provMatRef = provMatRef;
	}
	
	public String getMatching() {
		return matching;
	}

	public void setMatching(String matching) {
		this.matching = matching;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((fecDesde == null) ? 0 : fecDesde.hashCode());
		result = prime * result + ((fecHasta == null) ? 0 : fecHasta.hashCode());
		result = prime * result + ((gastoPorUmic == null) ? 0 : gastoPorUmic.hashCode());
		result = prime * result + ((gastosAdminContab == null) ? 0 : gastosAdminContab.hashCode());
		result = prime * result + ((gastosAdminSinProv == null) ? 0 : gastosAdminSinProv.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result + ((matching == null) ? 0 : matching.hashCode());
		result = prime * result + ((numUmicComprar == null) ? 0 : numUmicComprar.hashCode());
		result = prime * result + ((numUmicRef == null) ? 0 : numUmicRef.hashCode());
		result = prime * result + ((pctGastoProv == null) ? 0 : pctGastoProv.hashCode());
		result = prime * result + ((provMatComparar == null) ? 0 : provMatComparar.hashCode());
		result = prime * result + ((provMatRef == null) ? 0 : provMatRef.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GastosReales other = (GastosReales) obj;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (fecDesde == null) {
			if (other.fecDesde != null)
				return false;
		} else if (!fecDesde.equals(other.fecDesde))
			return false;
		if (fecHasta == null) {
			if (other.fecHasta != null)
				return false;
		} else if (!fecHasta.equals(other.fecHasta))
			return false;
		if (gastoPorUmic == null) {
			if (other.gastoPorUmic != null)
				return false;
		} else if (!gastoPorUmic.equals(other.gastoPorUmic))
			return false;
		if (gastosAdminContab == null) {
			if (other.gastosAdminContab != null)
				return false;
		} else if (!gastosAdminContab.equals(other.gastosAdminContab))
			return false;
		if (gastosAdminSinProv == null) {
			if (other.gastosAdminSinProv != null)
				return false;
		} else if (!gastosAdminSinProv.equals(other.gastosAdminSinProv))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (ktipobt == null) {
			if (other.ktipobt != null)
				return false;
		} else if (!ktipobt.equals(other.ktipobt))
			return false;
		if (matching == null) {
			if (other.matching != null)
				return false;
		} else if (!matching.equals(other.matching))
			return false;
		if (numUmicComprar == null) {
			if (other.numUmicComprar != null)
				return false;
		} else if (!numUmicComprar.equals(other.numUmicComprar))
			return false;
		if (numUmicRef == null) {
			if (other.numUmicRef != null)
				return false;
		} else if (!numUmicRef.equals(other.numUmicRef))
			return false;
		if (pctGastoProv == null) {
			if (other.pctGastoProv != null)
				return false;
		} else if (!pctGastoProv.equals(other.pctGastoProv))
			return false;
		if (provMatComparar == null) {
			if (other.provMatComparar != null)
				return false;
		} else if (!provMatComparar.equals(other.provMatComparar))
			return false;
		if (provMatRef == null) {
			if (other.provMatRef != null)
				return false;
		} else if (!provMatRef.equals(other.provMatRef))
			return false;
		return true;
	}
}