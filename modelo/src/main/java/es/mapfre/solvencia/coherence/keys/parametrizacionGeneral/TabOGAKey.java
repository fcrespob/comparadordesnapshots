package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.TabOGA;

@Portable
public class TabOGAKey {

	@PortableProperty(TabOGA.IND_CARTERA)
	private String cartera;

	public TabOGAKey(String cartera) {
		super();
		this.cartera = cartera;
	}

	public TabOGAKey() {
		super();
	}

	public String getCartera() {
		return cartera;
	}

	public void setCartera(String cartera) {
		this.cartera = cartera;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartera == null) ? 0 : cartera.hashCode());
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
		TabOGAKey other = (TabOGAKey) obj;
		if (cartera == null) {
			if (other.cartera != null)
				return false;
		} else if (!cartera.equals(other.cartera))
			return false;
		return true;
	}

}