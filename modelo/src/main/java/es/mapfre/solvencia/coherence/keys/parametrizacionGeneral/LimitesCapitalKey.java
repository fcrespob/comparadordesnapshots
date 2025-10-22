package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.LimitesCapital;

@Portable
public class LimitesCapitalKey {

	@PortableProperty(LimitesCapital.IND_KMODALIDAD)
	private String kmodalidad;
	@PortableProperty(LimitesCapital.IND_KGARANTIA)
	private String kgarantia;
	@PortableProperty(LimitesCapital.IND_FEFECFIN)
	private Timestamp fefecfin;
	@PortableProperty(LimitesCapital.IND_KEDAD1)
	private Integer kedad1;
	@PortableProperty(LimitesCapital.IND_KEDAD2)
	private Integer kedad2;
	@PortableProperty(LimitesCapital.IND_NMESHASTA)
	private Integer nmeshasta;
	@PortableProperty(LimitesCapital.IND_GSPACES1)
	private String gspaces1;
	@PortableProperty(LimitesCapital.IND_NREGISTRO)
	private String nregistro;

	public LimitesCapitalKey(String kmodalidad, String kgarantia,
			Timestamp fefecfin, Integer kedad1, Integer kedad2,
			Integer nmeshasta, String gspaces1, String nregistro) {
		super();
		this.kmodalidad = kmodalidad;
		this.kgarantia = kgarantia;
		this.fefecfin = fefecfin;
		this.kedad1 = kedad1;
		this.kedad2 = kedad2;
		this.nmeshasta = nmeshasta;
		this.gspaces1 = gspaces1;
		this.nregistro = nregistro;
	}
	
	public LimitesCapitalKey() {
		super();
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
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
		LimitesCapitalKey other = (LimitesCapitalKey) obj;
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
		return true;
	}
}