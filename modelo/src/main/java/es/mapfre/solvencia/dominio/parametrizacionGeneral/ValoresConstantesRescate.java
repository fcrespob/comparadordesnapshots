package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresConstantesRescateKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresConstantesRescate implements EntidadBase<ValoresConstantesRescateKey> {

	public static final int IND_KK1 = 0;
	
	public static final int IND_KDURACION = 1;
	
	public static final int IND_PORCKONST = 2;

	@PortableProperty(IND_KK1) private String kk1;
	
	@PortableProperty(IND_KDURACION) private Integer kduracion;
	
	@PortableProperty(IND_PORCKONST) private java.math.BigDecimal porckonst;

	@Override
	public ValoresConstantesRescateKey getKey() {
		return new ValoresConstantesRescateKey(kk1, kduracion);
	}

	public String getKk1() {
		return kk1;
	}

	public void setKk1(String kk1) {
		this.kk1 = kk1;
	}

	public Integer getKduracion() {
		return kduracion;
	}

	public void setKduracion(Integer kduracion) {
		this.kduracion = kduracion;
	}

	public java.math.BigDecimal getPorckonst() {
		return porckonst;
	}

	public void setPorckonst(java.math.BigDecimal porckonst) {
		this.porckonst = porckonst;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kduracion == null) ? 0 : kduracion.hashCode());
		result = prime * result + ((kk1 == null) ? 0 : kk1.hashCode());
		result = prime * result
				+ ((porckonst == null) ? 0 : porckonst.hashCode());
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
		ValoresConstantesRescate other = (ValoresConstantesRescate) obj;
		if (kduracion == null) {
			if (other.kduracion != null) {
				return false;
			}
		} else if (!kduracion.equals(other.kduracion)) {
			return false;
		}
		if (kk1 == null) {
			if (other.kk1 != null) {
				return false;
			}
		} else if (!kk1.equals(other.kk1)) {
			return false;
		}
		if (porckonst == null) {
			if (other.porckonst != null) {
				return false;
			}
		} else if (!porckonst.equals(other.porckonst)) {
			return false;
		}
		return true;
	}
}