package es.mapfre.solvencia.dominio.scr.entregables;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.scr.entregables.FactoresVolatilidadKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FactoresVolatilidad implements EntidadBase<FactoresVolatilidadKey>, EntidadConBaseTec{

	public static final int IND_BT = 0;
	public static final int IND_CCANAL = 1;
	public static final int IND_KMODALIDAD = 2;
	public static final int IND_QIXI2 = 3;
	public static final int IND_QIXI3 = 4;
	public static final int IND_FACTOR1 = 5;
	public static final int IND_FACTOR2 = 6;
	public static final int IND_C = 7;
	
	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(value = IND_QIXI2, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal qiXi2;
	@PortableProperty(value = IND_QIXI3, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal qiXi3;
	@PortableProperty(value = IND_FACTOR1, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal factor1;
	@PortableProperty(value = IND_FACTOR2, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal factor2;
	@PortableProperty(value = IND_C, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal c;
	
	public String getBt() {
		return bt;
	}

	public void setBt(String baseTec) {
		this.bt = baseTec;
	}
	
	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public java.math.BigDecimal getQiXi2() {
		return qiXi2;
	}

	public void setQiXi2(java.math.BigDecimal qiXi2) {
		this.qiXi2 = qiXi2;
	}

	public java.math.BigDecimal getQiXi3() {
		return qiXi3;
	}

	public void setQiXi3(java.math.BigDecimal qiXi3) {
		this.qiXi3 = qiXi3;
	}

	public java.math.BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(java.math.BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public java.math.BigDecimal getFactor2() {
		return factor2;
	}

	public void setFactor2(java.math.BigDecimal factor2) {
		this.factor2 = factor2;
	}

	public java.math.BigDecimal getC() {
		return c;
	}

	public void setC(java.math.BigDecimal c) {
		this.c = c;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FactoresVolatilidad other = (FactoresVolatilidad) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (qiXi2 == null) {
			if (other.qiXi2 != null)
				return false;
		} else if (!qiXi2.equals(other.qiXi2))
			return false;
		if (qiXi3 == null) {
			if (other.qiXi3 != null)
				return false;
		} else if (!qiXi3.equals(other.qiXi3))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (factor2 == null) {
			if (other.factor2 != null)
				return false;
		} else if (!factor2.equals(other.factor2))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FactoresVolatilidad [bt=" + bt + ", ccanal=" + ccanal + 
				", kmodalidad=" + kmodalidad + ", qiXi2=" + qiXi2 + 
				", qiXi3=" + qiXi3 + ", factor1=" + factor1 + 
				", factor1=" + factor1 + ", c=" + c + "]";
	}
	
	@Override
	public FactoresVolatilidadKey getKey() {
		return new FactoresVolatilidadKey(bt, ccanal, kmodalidad);
	}
	
	
	
	
	
}
