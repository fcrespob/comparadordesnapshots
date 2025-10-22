package es.mapfre.solvencia.dominio.parametrizacionGeneral;


import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.TabOGAKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class TabOGA implements EntidadBase<TabOGAKey> {
	
	public static final int IND_FECHA = 0;
	public static final int IND_CARTERA = 1;
	public static final int IND_OGA = 2;
	
	@PortableProperty(IND_FECHA)
	private Timestamp fecha;
	@PortableProperty(IND_CARTERA)
	private String cartera;
	@PortableProperty(IND_OGA)
	private java.math.BigDecimal oga;

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getCartera() {
		return cartera;
	}

	public void setCartera(String cartera) {
		this.cartera = cartera;
	}

	public java.math.BigDecimal getOga() {
		return oga;
	}

	public void setOga(java.math.BigDecimal oga) {
		this.oga = oga;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cartera == null) ? 0 : cartera.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((oga == null) ? 0 : oga.hashCode());
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
		TabOGA other = (TabOGA) obj;
		if (cartera == null) {
			if (other.cartera != null)
				return false;
		} else if (!cartera.equals(other.cartera))
			return false;
		if (fecha == null) {
			if (other.fecha != null)
				return false;
		} else if (!fecha.equals(other.fecha))
			return false;
		if (oga == null) {
			if (other.oga != null)
				return false;
		} else if (!oga.equals(other.oga))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TabOGA [fecha=" + fecha + ", cartera=" + cartera + ", oga=" + oga + "]";
	}

	@Override
	public TabOGAKey getKey() {
		// TODO Auto-generated method stub
		return new TabOGAKey(cartera);
	}
	
}