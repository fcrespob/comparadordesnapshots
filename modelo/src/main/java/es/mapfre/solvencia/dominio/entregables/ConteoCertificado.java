package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.ConteoCertificadoKey;
import es.mapfre.solvencia.coherence.keys.entregables.PatronCsmKey;
import es.mapfre.solvencia.coherence.keys.entregables.ProvCoaSegKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;
@Portable
public class ConteoCertificado implements EntidadBase<ConteoCertificadoKey>, EntidadConBaseTec {
	
	public static final int IND_BT = 0;
	public static final int IND_KMODALIDAD = 1;
	public static final int IND_KCERTIFICADO = 2;
	public static final int IND_KCERTIFICADO_UMIC = 3;

	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado_totales;
	@PortableProperty(IND_KCERTIFICADO_UMIC)
	private Integer kcertificado_umic;

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getKcertificado_totales() {
		return kcertificado_totales;
	}

	public void setKcertificado_totales(Integer kcertificado_totales) {
		this.kcertificado_totales = kcertificado_totales;
	}

	public Integer getKcertificado_umic() {
		return kcertificado_umic;
	}

	public void setKcertificado_umic(Integer kcertificado_umic) {
		this.kcertificado_umic = kcertificado_umic;
	}

	@Override
	public String getBt() {
		return bt;
	}

	@Override
	public void setBt(String bt) {
		this.bt = bt;
	}

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((kcertificado_totales == null) ? 0 : kcertificado_totales.hashCode());
		result = prime * result + ((kcertificado_umic == null) ? 0 : kcertificado_umic.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		ConteoCertificado other = (ConteoCertificado) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (kcertificado_totales == null) {
			if (other.kcertificado_totales != null)
				return false;
		} else if (!kcertificado_totales.equals(other.kcertificado_totales))
			return false;
		if (kcertificado_umic == null) {
			if (other.kcertificado_umic != null)
				return false;
		} else if (!kcertificado_umic.equals(other.kcertificado_umic))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "ConteoCertificado [bt=" + bt + ", kmodalidad=" + kmodalidad + ", kcertificado_totales="
				+ kcertificado_totales + ", kcertificado_umic=" + kcertificado_umic + "]";
	}

	@Override
	public ConteoCertificadoKey getKey() {
		return new ConteoCertificadoKey(kmodalidad,bt);
	}
}

