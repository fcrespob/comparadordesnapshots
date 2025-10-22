package es.mapfre.solvencia.dominio.salidaCalculo;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;

@Portable
public class BloqueCorriente {
	public static final int IND_FECHAPAGO = 0;
	public static final int IND_FECHADEVENGO = 1;
	public static final int IND_IMPFLUJONOMINAL = 2;
	public static final int IND_IMPFLUJOPROBABLE = 3;
	public static final int IND_IMPFLUJONOANULADO = 4;
	public static final int IND_IMPFLUJOACTUALIZADO = 5;
	public static final int IND_IMPPROVI = 6;
	public static final int IND_FPBPROBABLE = 7;
	public static final int IND_FPBATC = 8;
	public static final int IND_FPBATCFIN = 9;

	
	@PortableProperty(value=IND_FECHAPAGO) private Timestamp fechaPago;
	@PortableProperty(value=IND_FECHADEVENGO) private Timestamp fechaDevengo;
	@PortableProperty(value=IND_IMPFLUJONOMINAL, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impFlujoNominal;
	@PortableProperty(value=IND_IMPFLUJOPROBABLE, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impFlujoProbable;
	@PortableProperty(value=IND_IMPFLUJONOANULADO, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impFlujoNoAnulado;
	@PortableProperty(value=IND_IMPFLUJOACTUALIZADO, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impFlujoActualizado;
	@PortableProperty(value=IND_IMPPROVI, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal impProvi;
	@PortableProperty(value=IND_FPBPROBABLE, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal fpbProbable;
	@PortableProperty(value=IND_FPBATC, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal fpbAtc;
	@PortableProperty(value=IND_FPBATCFIN, codec=BigDecimalSolvenciaCodec.class) private java.math.BigDecimal fpbAtcfin;

	public Timestamp getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Timestamp fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Timestamp getFechaDevengo() {
		return fechaDevengo;
	}
	public void setFechaDevengo(Timestamp fechaDevengo) {
		this.fechaDevengo = fechaDevengo;
	}
	public java.math.BigDecimal getImpFlujoNominal() {
		return impFlujoNominal;
	}
	public void setImpFlujoNominal(java.math.BigDecimal impFlujoNominal) {
		this.impFlujoNominal = impFlujoNominal;
	}
	public java.math.BigDecimal getImpFlujoProbable() {
		return impFlujoProbable;
	}
	public void setImpFlujoProbable(java.math.BigDecimal impFlujoProbable) {
		this.impFlujoProbable = impFlujoProbable;
	}
	public java.math.BigDecimal getImpFlujoNoAnulado() {
		return impFlujoNoAnulado;
	}
	public void setImpFlujoNoAnulado(java.math.BigDecimal impFlujoNoAnulado) {
		this.impFlujoNoAnulado = impFlujoNoAnulado;
	}
	public java.math.BigDecimal getImpFlujoActualizado() {
		return impFlujoActualizado;
	}
	public void setImpFlujoActualizado(java.math.BigDecimal impFlujoActualizado) {
		this.impFlujoActualizado = impFlujoActualizado;
	}
	public java.math.BigDecimal getImpProvi() {
		return impProvi;
	}
	public void setImpProvi(java.math.BigDecimal impProvi) {
		this.impProvi = impProvi;
	}
	public java.math.BigDecimal getFpbProbable() {
		return fpbProbable;
	}
	public void setFpbProbable(java.math.BigDecimal fpbProbable) {
		this.fpbProbable = fpbProbable;
	}
	public java.math.BigDecimal getFpbAtc() {
		return fpbAtc;
	}
	public void setFpbAtc(java.math.BigDecimal fpbAtc) {
		this.fpbAtc = fpbAtc;
	}
	public java.math.BigDecimal getFpbAtcfin() {
		return fpbAtcfin;
	}
	public void setFpbAtcfin(java.math.BigDecimal fpbAtcfin) {
		this.fpbAtcfin = fpbAtcfin;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fechaDevengo == null) ? 0 : fechaDevengo.hashCode());
		result = prime * result
				+ ((fechaPago == null) ? 0 : fechaPago.hashCode());
		result = prime * result + ((fpbAtc == null) ? 0 : fpbAtc.hashCode());
		result = prime * result
				+ ((fpbAtcfin == null) ? 0 : fpbAtcfin.hashCode());
		result = prime * result
				+ ((fpbProbable == null) ? 0 : fpbProbable.hashCode());
		result = prime
				* result
				+ ((impFlujoActualizado == null) ? 0 : impFlujoActualizado
						.hashCode());
		result = prime
				* result
				+ ((impFlujoNoAnulado == null) ? 0 : impFlujoNoAnulado
						.hashCode());
		result = prime * result
				+ ((impFlujoNominal == null) ? 0 : impFlujoNominal.hashCode());
		result = prime
				* result
				+ ((impFlujoProbable == null) ? 0 : impFlujoProbable.hashCode());
		result = prime * result
				+ ((impProvi == null) ? 0 : impProvi.hashCode());
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
		BloqueCorriente other = (BloqueCorriente) obj;
		if (fechaDevengo == null) {
			if (other.fechaDevengo != null) {
				return false;
			}
		} else if (!fechaDevengo.equals(other.fechaDevengo)) {
			return false;
		}
		if (fechaPago == null) {
			if (other.fechaPago != null) {
				return false;
			}
		} else if (!fechaPago.equals(other.fechaPago)) {
			return false;
		}
		if (fpbAtc == null) {
			if (other.fpbAtc != null) {
				return false;
			}
		} else if (!fpbAtc.equals(other.fpbAtc)) {
			return false;
		}
		if (fpbAtcfin == null) {
			if (other.fpbAtcfin != null) {
				return false;
			}
		} else if (!fpbAtcfin.equals(other.fpbAtcfin)) {
			return false;
		}
		if (fpbProbable == null) {
			if (other.fpbProbable != null) {
				return false;
			}
		} else if (!fpbProbable.equals(other.fpbProbable)) {
			return false;
		}
		if (impFlujoActualizado == null) {
			if (other.impFlujoActualizado != null) {
				return false;
			}
		} else if (!impFlujoActualizado.equals(other.impFlujoActualizado)) {
			return false;
		}
		if (impFlujoNoAnulado == null) {
			if (other.impFlujoNoAnulado != null) {
				return false;
			}
		} else if (!impFlujoNoAnulado.equals(other.impFlujoNoAnulado)) {
			return false;
		}
		if (impFlujoNominal == null) {
			if (other.impFlujoNominal != null) {
				return false;
			}
		} else if (!impFlujoNominal.equals(other.impFlujoNominal)) {
			return false;
		}
		if (impFlujoProbable == null) {
			if (other.impFlujoProbable != null) {
				return false;
			}
		} else if (!impFlujoProbable.equals(other.impFlujoProbable)) {
			return false;
		}
		if (impProvi == null) {
			if (other.impProvi != null) {
				return false;
			}
		} else if (!impProvi.equals(other.impProvi)) {
			return false;
		}
		return true;
	}


}
