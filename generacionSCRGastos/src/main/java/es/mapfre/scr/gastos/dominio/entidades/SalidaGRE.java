package es.mapfre.scr.gastos.dominio.entidades;

import es.mapfre.scr.gastos.dominio.EntidadBase;
import es.mapfre.scr.gastos.dominio.keys.SalidaGREKey;

public class SalidaGRE implements EntidadBase<SalidaGREKey> {

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
	private String gastoPorUmic;
	private Integer kmodalidad;
	private String kramo;
	private String ktipobt;
	private String pctGastoProv;
	private String matching;
	
	public SalidaGRE() {
		super();
	}
	
	@Override
	public SalidaGREKey getKey() {
		return new SalidaGREKey(ccanal, cnegocio, fecDesde, fecHasta, kmodalidad, kramo, ktipobt, matching);
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

	public String getGastoPorUmic() {
		return gastoPorUmic;
	}

	public void setGastoPorUmic(String gastoPorUmic) {
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

	public String getPctGastoProv() {
		return pctGastoProv;
	}

	public void setPctGastoProv(String pctGastoProv) {
		this.pctGastoProv = pctGastoProv;
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
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ktipobt == null) ? 0 : ktipobt.hashCode());
		result = prime * result + ((matching == null) ? 0 : matching.hashCode());
		result = prime * result + ((pctGastoProv == null) ? 0 : pctGastoProv.hashCode());
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
		SalidaGRE other = (SalidaGRE) obj;
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
		if (pctGastoProv == null) {
			if (other.pctGastoProv != null)
				return false;
		} else if (!pctGastoProv.equals(other.pctGastoProv))
			return false;
		return true;
	}
}
