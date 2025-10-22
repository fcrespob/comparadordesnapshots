package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import java.sql.Timestamp;

public class PlanPagos{
	
	private Timestamp fecPago;	
	private java.math.BigDecimal impPago;
	
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
