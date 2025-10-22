package es.mapfre.solvencia.dominio.salidaCalculo;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Arrays;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TerminosPMCUmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class TerminosPMCUmic implements EntidadConBaseTec, EntidadBase<TerminosPMCUmicKey>{

	public static final int IND_CLAVEUMIC 	= 0;
	public static final int IND_BT 			= 1;
	public static final int IND_BX 			= 2;
	public static final int IND_GAST		= 3;
	public static final int IND_PRIMA 		= 4;
	public static final int IND_GASTP 		= 5;
	public static final int IND_CFALL 		= 6;
	public static final int IND_INTG 		= 7;
	public static final int IND_INTP 		= 8;
	public static final int IND_ITR 		= 9;
	
	@PortableProperty(IND_CLAVEUMIC)	private UmicKey claveUmic;
	@PortableProperty(IND_BT)			private String bt;
	@PortableProperty(IND_BX)			private BigDecimal Bx;
	@PortableProperty(IND_GAST)			private BigDecimal Gast;
	@PortableProperty(IND_PRIMA)		private BigDecimal Prima;
	@PortableProperty(IND_GASTP)		private BigDecimal Gastp;
	@PortableProperty(IND_CFALL)		private BigDecimal Cfall;
	@PortableProperty(IND_INTG)			private BigDecimal IntG;
	@PortableProperty(IND_INTP)			private BigDecimal IntP;
	@PortableProperty(IND_ITR)			private Integer iteracion;
	
	private Object[] args;
		
	public UmicKey getClaveUmic() {
		return claveUmic;
	}
	public void setClaveUmic(UmicKey claveUmic) {
		this.claveUmic = claveUmic;
	}
	public String getBt() {
		return bt;
	}
	public void setBt(String bt) {
		this.bt = bt;
	}
	public BigDecimal getBx() {
		return Bx;
	}
	public void setBx(BigDecimal bx) {
		Bx = bx;
	}
	public BigDecimal getGast() {
		return Gast;
	}
	public void setGast(BigDecimal gast) {
		Gast = gast;
	}
	public BigDecimal getPrima() {
		return Prima;
	}
	public void setPrima(BigDecimal prima) {
		Prima = prima;
	}
	public BigDecimal getGastp() {
		return Gastp;
	}
	public void setGastp(BigDecimal gastp) {
		Gastp = gastp;
	}
	public BigDecimal getCfall() {
		return Cfall;
	}
	public void setCfall(BigDecimal cfall) {
		Cfall = cfall;
	}
	public BigDecimal getIntG() {
		return IntG;
	}
	public void setIntG(BigDecimal intG) {
		IntG = intG;
	}
	public BigDecimal getIntP() {
		return IntP;
	}
	public void setIntP(BigDecimal intP) {
		IntP = intP;
	}
	public Integer getIteracion() {
		return iteracion;
	}
	public void setIteracion(Integer iteracion) {
		this.iteracion = iteracion;
	}
	@Transient
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Bx == null) ? 0 : Bx.hashCode());
		result = prime * result + ((Cfall == null) ? 0 : Cfall.hashCode());
		result = prime * result + ((Gast == null) ? 0 : Gast.hashCode());
		result = prime * result + ((Gastp == null) ? 0 : Gastp.hashCode());
		result = prime * result + ((IntG == null) ? 0 : IntG.hashCode());
		result = prime * result + ((IntP == null) ? 0 : IntP.hashCode());
		result = prime * result + ((Prima == null) ? 0 : Prima.hashCode());
		result = prime * result + Arrays.hashCode(args);
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result
				+ ((claveUmic == null) ? 0 : claveUmic.hashCode());
		result = prime * result
				+ ((iteracion == null) ? 0 : iteracion.hashCode());
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
		TerminosPMCUmic other = (TerminosPMCUmic) obj;
		if (Bx == null) {
			if (other.Bx != null)
				return false;
		} else if (!Bx.equals(other.Bx))
			return false;
		if (Cfall == null) {
			if (other.Cfall != null)
				return false;
		} else if (!Cfall.equals(other.Cfall))
			return false;
		if (Gast == null) {
			if (other.Gast != null)
				return false;
		} else if (!Gast.equals(other.Gast))
			return false;
		if (Gastp == null) {
			if (other.Gastp != null)
				return false;
		} else if (!Gastp.equals(other.Gastp))
			return false;
		if (IntG == null) {
			if (other.IntG != null)
				return false;
		} else if (!IntG.equals(other.IntG))
			return false;
		if (IntP == null) {
			if (other.IntP != null)
				return false;
		} else if (!IntP.equals(other.IntP))
			return false;
		if (Prima == null) {
			if (other.Prima != null)
				return false;
		} else if (!Prima.equals(other.Prima))
			return false;
		if (!Arrays.equals(args, other.args))
			return false;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (claveUmic == null) {
			if (other.claveUmic != null)
				return false;
		} else if (!claveUmic.equals(other.claveUmic))
			return false;
		if (iteracion == null) {
			if (other.iteracion != null)
				return false;
		} else if (!iteracion.equals(other.iteracion))
			return false;
		return true;
	}
	@Override
	public TerminosPMCUmicKey getKey() {
		return new TerminosPMCUmicKey(claveUmic, bt, iteracion);
	}
	
}