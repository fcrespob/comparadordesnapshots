package es.mapfre.solvencia.dominio.scr;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.scr.ValoresEstresKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresEstres implements EntidadBase<ValoresEstresKey>{

	public static final int IND_FECCIERRE = 0;
	public static final int IND_BT = 1;
	public static final int IND_VARIABLE = 2;
	public static final int IND_VALOR = 3;
	
	@PortableProperty(ValoresEstres.IND_FECCIERRE) private Timestamp feccierre;
	@PortableProperty(ValoresEstres.IND_BT)        private String bt;
	@PortableProperty(ValoresEstres.IND_VARIABLE)  private String variable;
	@PortableProperty(ValoresEstres.IND_VALOR)     private BigDecimal valor;
	
	public ValoresEstres() {
		super();
	}

	public Timestamp getFeccierre() {
		return feccierre;
	}

	public void setFeccierre(Timestamp feccierre) {
		this.feccierre = feccierre;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	@Override
	public ValoresEstresKey getKey() {
		return new ValoresEstresKey(feccierre, bt, variable);
	}

	@Override 
	public int hashCode() { 
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((feccierre == null) ? 0 : feccierre.hashCode());
		result = prime * result
				+ ((bt == null) ? 0 : bt.hashCode());
		result = prime * result
				+ ((variable == null) ? 0 : variable.hashCode());
		result = prime * result
				+ ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override 
	public boolean equals(Object obj) { 
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValoresEstres other = (ValoresEstres) obj;
		if (feccierre == null) {
			if (other.feccierre != null) {
				return false;
			}
		} else if (!feccierre.equals(other.feccierre)) {
			return false;
		}
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (variable == null) {
			if (other.variable != null) {
				return false;
			}
		} else if (!variable.equals(other.variable)) {
			return false;
		}
		if (valor == null) {
			if (other.valor != null) {
				return false;
			}
		} else if (!valor.equals(other.valor)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValoresEstres [feccierre=" + feccierre
				+ ", bt=" + bt + ", variable=" + variable
				+ ", valor=" + valor + "]";
	}
	
	
}
