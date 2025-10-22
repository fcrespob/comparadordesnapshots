package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.maestro.PolizaInstrumental;

@Portable
public class PolizaInstrumentalKey {

	@PortableProperty(PolizaInstrumental.IND_KMODALIDADINSTR)
	private Integer kmodalidadInstr;
	@PortableProperty(PolizaInstrumental.IND_KPOLIZAINSTR)
	private Long kpolizaInstr;
	@PortableProperty(PolizaInstrumental.IND_KSUBPOLIZAINSTR)
	private Integer ksubpolizaInstr;
	@PortableProperty(PolizaInstrumental.IND_KCERTIFICADOINSTR)
	private Integer kcertificadoInstr;
	
	public PolizaInstrumentalKey(Integer kmodalidadInstr, Long kpolizaInstr, Integer ksubpolizaInstr,
			Integer kcertificadoInstr) {
		super();
		this.kmodalidadInstr = kmodalidadInstr;
		this.kpolizaInstr = kpolizaInstr;
		this.ksubpolizaInstr = ksubpolizaInstr;
		this.kcertificadoInstr = kcertificadoInstr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kcertificadoInstr == null) ? 0 : kcertificadoInstr.hashCode());
		result = prime * result + ((kmodalidadInstr == null) ? 0 : kmodalidadInstr.hashCode());
		result = prime * result + ((kpolizaInstr == null) ? 0 : kpolizaInstr.hashCode());
		result = prime * result + ((ksubpolizaInstr == null) ? 0 : ksubpolizaInstr.hashCode());
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
		PolizaInstrumentalKey other = (PolizaInstrumentalKey) obj;
		if (kcertificadoInstr == null) {
			if (other.kcertificadoInstr != null)
				return false;
		} else if (!kcertificadoInstr.equals(other.kcertificadoInstr))
			return false;
		if (kmodalidadInstr == null) {
			if (other.kmodalidadInstr != null)
				return false;
		} else if (!kmodalidadInstr.equals(other.kmodalidadInstr))
			return false;
		if (kpolizaInstr == null) {
			if (other.kpolizaInstr != null)
				return false;
		} else if (!kpolizaInstr.equals(other.kpolizaInstr))
			return false;
		if (ksubpolizaInstr == null) {
			if (other.ksubpolizaInstr != null)
				return false;
		} else if (!ksubpolizaInstr.equals(other.ksubpolizaInstr))
			return false;
		return true;
	}
}
