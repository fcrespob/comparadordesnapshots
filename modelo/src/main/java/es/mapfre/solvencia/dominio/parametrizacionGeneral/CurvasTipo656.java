package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.CurvasTipo656Key;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class CurvasTipo656 implements EntidadBase<CurvasTipo656Key>{

	public static final int IND_CARTERAINV = 0;
	public static final int IND_DIASPLAZO = 1;
	public static final int IND_FECHA = 2;
	public static final int IND_KPLAZO = 3;
	public static final int IND_PINTERES = 4;
	
	@PortableProperty(IND_CARTERAINV) private String carterainv;
	@PortableProperty(IND_DIASPLAZO) private Integer diasplazo;
	@PortableProperty(IND_FECHA) private Timestamp fecha;
	@PortableProperty(IND_KPLAZO) private String kplazo;
	@PortableProperty(IND_PINTERES) private java.math.BigDecimal pinteres;

	public String getCarterainv() {
		return carterainv;
	}

	public void setCarterainv(String carterainv) {
		this.carterainv = carterainv;
	}

	public Integer getDiasplazo() {
		return diasplazo;
	}

	public void setDiasplazo(Integer diasplazo) {
		this.diasplazo = diasplazo;
	}

	public Timestamp getFecha() {
		return fecha;
	}

	public void setFecha(Timestamp fecha) {
		this.fecha = fecha;
	}

	public String getKplazo() {
		return kplazo;
	}

	public void setKplazo(String kplazo) {
		this.kplazo = kplazo;
	}

	public java.math.BigDecimal getPinteres() {
		return pinteres;
	}

	public void setPinteres(java.math.BigDecimal pinteres) {
		this.pinteres = pinteres;
	}

	@Override
	public CurvasTipo656Key getKey() {
		return new CurvasTipo656Key(carterainv, fecha);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((carterainv == null) ? 0 : carterainv.hashCode());
		result = prime * result
				+ ((diasplazo == null) ? 0 : diasplazo.hashCode());
		result = prime * result + ((fecha == null) ? 0 : fecha.hashCode());
		result = prime * result + ((kplazo == null) ? 0 : kplazo.hashCode());
		result = prime * result
				+ ((pinteres == null) ? 0 : pinteres.hashCode());
		return result;
	}

	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj)
			{ return true; }
		if (obj == null)
			{ return false; }
		if (getClass() != obj.getClass())
			{ return false; }
		CurvasTipo656 other = (CurvasTipo656) obj;
		if (carterainv == null) {
			if (other.carterainv != null)
				{ return false; }
		} else if (!carterainv.equals(other.carterainv))
			{ return false; }
		if (diasplazo == null) {
			if (other.diasplazo != null)
				{ return false; }
		} else if (!diasplazo.equals(other.diasplazo))
			{ return false; }
		if (fecha == null) {
			if (other.fecha != null)
				{ return false; }
		} else if (!fecha.equals(other.fecha))
			{ return false; }
		if (kplazo == null) {
			if (other.kplazo != null)
				{ return false; }
		} else if (!kplazo.equals(other.kplazo))
			{ return false; }
		if (pinteres == null) {
			if (other.pinteres != null)
				{ return false; }
		} else if (!pinteres.equals(other.pinteres))
			{ return false; }
		{ return true; }
	}
	
	
	
}