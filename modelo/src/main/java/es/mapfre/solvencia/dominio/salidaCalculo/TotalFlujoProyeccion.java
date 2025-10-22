package es.mapfre.solvencia.dominio.salidaCalculo;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;

@Portable
public class TotalFlujoProyeccion {
	private static final int IND_SUMFPROB = 0;
	private static final int IND_SUMFPROBTANUL = 1;
	private static final int IND_SUMPROVISION = 2;
	private static final int IND_SUMCOLA = 3;
	private static final int IND_PROVBTIPROY = 4;
	private static final int IND_TERMINALANTERIOR = 5;
	private static final int IND_TERMINALPOSTERIOR = 6;
	private static final int IND_FECHAPAGO = 7;
	private static final int IND_FECHADEVENGO = 8;
	private static final int IND_PROV_NIIF17LIR = 9;
	private static final int IND_PROV_N17LIRIN = 10;
	private static final int IND_PROVNIIF17 = 11;
	
	@PortableProperty(value=IND_SUMFPROB, codec=BigDecimalSolvenciaCodec.class) private BigDecimal sumfprob;
	@PortableProperty(value=IND_SUMFPROBTANUL, codec=BigDecimalSolvenciaCodec.class) private BigDecimal sumfprobtanul;
	@PortableProperty(value=IND_SUMPROVISION, codec=BigDecimalSolvenciaCodec.class) private BigDecimal sumprovision;
	@PortableProperty(value=IND_SUMCOLA, codec=BigDecimalSolvenciaCodec.class) private BigDecimal sumcola;
	@PortableProperty(value=IND_PROVBTIPROY, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provbtiproy;
	@PortableProperty(value=IND_TERMINALANTERIOR, codec=BigDecimalSolvenciaCodec.class) private BigDecimal terminalAnterior;
	@PortableProperty(value=IND_TERMINALPOSTERIOR, codec=BigDecimalSolvenciaCodec.class) private BigDecimal terminalPosterior;

	@PortableProperty(value=IND_FECHAPAGO) private Timestamp fechaPago;
	@PortableProperty(value=IND_FECHADEVENGO) private Timestamp fechaDevengo;
	
	@PortableProperty(value=IND_PROV_NIIF17LIR, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provNIIF17LIR;
	@PortableProperty(value=IND_PROV_N17LIRIN, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provN17LIRIN;
	@PortableProperty(value=IND_PROVNIIF17, codec=BigDecimalSolvenciaCodec.class) private BigDecimal provNIIF17;
	
	
	public BigDecimal getSumfprob() {
		return sumfprob;
	}
	public void setSumfprob(BigDecimal sumfprob) {
		this.sumfprob = sumfprob;
	}
	public BigDecimal getSumfprobtanul() {
		return sumfprobtanul;
	}
	public void setSumfprobtanul(BigDecimal sumfprobtanul) {
		this.sumfprobtanul = sumfprobtanul;
	}
	public BigDecimal getSumprovision() {
		return sumprovision;
	}
	public void setSumprovision(BigDecimal sumprovision) {
		this.sumprovision = sumprovision;
	}
	public BigDecimal getSumcola() {
		return sumcola;
	}
	public void setSumcola(BigDecimal sumcola) {
		this.sumcola = sumcola;
	}
	public BigDecimal getProvbtiproy() {
		return provbtiproy;
	}
	public void setProvbtiproy(BigDecimal provbtiproy) {
		this.provbtiproy = provbtiproy;
	}
	public BigDecimal getTerminalAnterior() {
		return terminalAnterior;
	}
	public void setTerminalAnterior(BigDecimal terminalAnterior) {
		this.terminalAnterior = terminalAnterior;
	}
	public BigDecimal getTerminalPosterior() {
		return terminalPosterior;
	}
	public void setTerminalPosterior(BigDecimal terminalPosterior) {
		this.terminalPosterior = terminalPosterior;
	}
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
	public BigDecimal getProvNIIF17LIR() {
		return provNIIF17LIR;
	}
	public void setProvNIIF17LIR(BigDecimal provNIIF17LIR) {
		this.provNIIF17LIR = provNIIF17LIR;
	}
	public BigDecimal getProvN17LIRIN() {
		return provN17LIRIN;
	}
	public void setProvN17LIRIN(BigDecimal provN17LIRIN) {
		this.provN17LIRIN = provN17LIRIN;
	}
	public BigDecimal getProvNIIF17() {
		return provNIIF17;
	}
	public void setProvNIIF17(BigDecimal provNIIF17) {
		this.provNIIF17 = provNIIF17;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaDevengo == null) ? 0 : fechaDevengo.hashCode());
		result = prime * result + ((fechaPago == null) ? 0 : fechaPago.hashCode());
		result = prime * result + ((provN17LIRIN == null) ? 0 : provN17LIRIN.hashCode());
		result = prime * result + ((provNIIF17 == null) ? 0 : provNIIF17.hashCode());
		result = prime * result + ((provNIIF17LIR == null) ? 0 : provNIIF17LIR.hashCode());
		result = prime * result + ((provbtiproy == null) ? 0 : provbtiproy.hashCode());
		result = prime * result + ((sumcola == null) ? 0 : sumcola.hashCode());
		result = prime * result + ((sumfprob == null) ? 0 : sumfprob.hashCode());
		result = prime * result + ((sumfprobtanul == null) ? 0 : sumfprobtanul.hashCode());
		result = prime * result + ((sumprovision == null) ? 0 : sumprovision.hashCode());
		result = prime * result + ((terminalAnterior == null) ? 0 : terminalAnterior.hashCode());
		result = prime * result + ((terminalPosterior == null) ? 0 : terminalPosterior.hashCode());
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
		TotalFlujoProyeccion other = (TotalFlujoProyeccion) obj;
		if (fechaDevengo == null) {
			if (other.fechaDevengo != null)
				return false;
		} else if (!fechaDevengo.equals(other.fechaDevengo))
			return false;
		if (fechaPago == null) {
			if (other.fechaPago != null)
				return false;
		} else if (!fechaPago.equals(other.fechaPago))
			return false;
		if (provN17LIRIN == null) {
			if (other.provN17LIRIN != null)
				return false;
		} else if (!provN17LIRIN.equals(other.provN17LIRIN))
			return false;
		if (provNIIF17 == null) {
			if (other.provNIIF17 != null)
				return false;
		} else if (!provNIIF17.equals(other.provNIIF17))
			return false;
		if (provNIIF17LIR == null) {
			if (other.provNIIF17LIR != null)
				return false;
		} else if (!provNIIF17LIR.equals(other.provNIIF17LIR))
			return false;
		if (provbtiproy == null) {
			if (other.provbtiproy != null)
				return false;
		} else if (!provbtiproy.equals(other.provbtiproy))
			return false;
		if (sumcola == null) {
			if (other.sumcola != null)
				return false;
		} else if (!sumcola.equals(other.sumcola))
			return false;
		if (sumfprob == null) {
			if (other.sumfprob != null)
				return false;
		} else if (!sumfprob.equals(other.sumfprob))
			return false;
		if (sumfprobtanul == null) {
			if (other.sumfprobtanul != null)
				return false;
		} else if (!sumfprobtanul.equals(other.sumfprobtanul))
			return false;
		if (sumprovision == null) {
			if (other.sumprovision != null)
				return false;
		} else if (!sumprovision.equals(other.sumprovision))
			return false;
		if (terminalAnterior == null) {
			if (other.terminalAnterior != null)
				return false;
		} else if (!terminalAnterior.equals(other.terminalAnterior))
			return false;
		if (terminalPosterior == null) {
			if (other.terminalPosterior != null)
				return false;
		} else if (!terminalPosterior.equals(other.terminalPosterior))
			return false;
		return true;
	}

	
	
	
}
