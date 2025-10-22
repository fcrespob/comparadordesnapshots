package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.CurvasTipo656;

@Portable
public class CurvasTipo656Key {
	
	@PortableProperty(CurvasTipo656.IND_CARTERAINV) private String carterainv;
	@PortableProperty(CurvasTipo656.IND_FECHA) private Long fecha;
	
	public CurvasTipo656Key(String carterainv, Timestamp fecha) {

		this.fecha = fecha.getTime();
		this.carterainv = carterainv;
	}

	public CurvasTipo656Key() {
		super();
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carterainv == null) ? 0 : carterainv.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
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
		CurvasTipo656Key other = (CurvasTipo656Key) obj;
		if (carterainv == null) {
			if (other.carterainv != null) {
				return false;
			}
		} else if (!carterainv.equals(other.carterainv)) {
			return false;
		}
		if (fecha == null) {
			if (other.fecha != null) {
				return false;
			}
		} else if (!fecha.equals(other.fecha)) {
			return false;
		}
		return true;
	}
}