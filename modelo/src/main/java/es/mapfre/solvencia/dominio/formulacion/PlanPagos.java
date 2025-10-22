package es.mapfre.solvencia.dominio.formulacion;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;

@Portable
public class PlanPagos{
	
	private static final int FEC_PAGO = 1;
	private static final int IMP_PAGO = 2;
	
	@PortableProperty(FEC_PAGO) private Timestamp fecPago;	
	@PortableProperty(value=IMP_PAGO, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impPago;
	
	public Timestamp getFecPago() {
		return fecPago;
	}
	public void setFecPago(Timestamp fecPago) {
		this.fecPago = fecPago;
	}
	public java.math.BigDecimal getImpPago() {
		return impPago;
	}
	public void setImpPago(java.math.BigDecimal impPago) {
		this.impPago = impPago;
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecPago == null) ? 0 : fecPago.hashCode());
		result = prime * result + ((impPago == null) ? 0 : impPago.hashCode());
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
		PlanPagos other = (PlanPagos) obj;
		if (fecPago == null) {
			if (other.fecPago != null) {
				return false;
			}
		} else if (!fecPago.equals(other.fecPago)) {
			return false;
		}
		if (impPago == null) {
			if (other.impPago != null) {
				return false;
			}
		} else if (!impPago.equals(other.impPago)) {
			return false;
		}
		return true;
	}
	

		
}
