package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.Tab923;

@Portable
public class Tab923Key {

	@PortableProperty(Tab923.IND_MODALIDAD)
	private Integer modalidad;
	@PortableProperty(Tab923.IND_FINIP)
	private Timestamp finip;
	@PortableProperty(Tab923.IND_FFINP)
	private Timestamp ffinp;

	public Tab923Key(Integer modalidad, Timestamp finip, Timestamp ffinp) {
		super();
		this.modalidad = modalidad;
		this.finip = finip;
		this.ffinp = ffinp;
	}

	public Tab923Key() {
		super();
	}

	public Integer getModalidad() {
		return modalidad;
	}

	public void setModalidad(Integer modalidad) {
		this.modalidad = modalidad;
	}

	public Timestamp getFinip() {
		return finip;
	}

	public void setFinip(Timestamp finip) {
		this.finip = finip;
	}

	public Timestamp getFfinp() {
		return ffinp;
	}

	public void setFfinp(Timestamp ffinp) {
		this.ffinp = ffinp;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ffinp == null) ? 0 : ffinp.hashCode());
		result = prime * result + ((finip == null) ? 0 : finip.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
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
		Tab923Key other = (Tab923Key) obj;
		if (ffinp == null) {
			if (other.ffinp != null)
				return false;
		} else if (!ffinp.equals(other.ffinp))
			return false;
		if (finip == null) {
			if (other.finip != null)
				return false;
		} else if (!finip.equals(other.finip))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		return true;
	}

}