package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.PolizaInstrumentalKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class PolizaInstrumental implements EntidadBase<PolizaInstrumentalKey> {

	public static final int IND_KMODALIDADINSTR = 0;
	public static final int IND_KPOLIZAINSTR = 1;
	public static final int IND_KSUBPOLIZAINSTR = 2;
	public static final int IND_KCERTIFICADOINSTR = 3;
	public static final int IND_KMODALIDADORIG = 4;
	public static final int IND_KPOLIZAORIG = 5;
	public static final int IND_KSUBPOLIZAORIG = 6;
	public static final int IND_KCERTIFICADOORIG = 7;
	
	@PortableProperty(IND_KMODALIDADINSTR)
	private Integer kmodalidadInstr;
	@PortableProperty(IND_KPOLIZAINSTR)
	private Long kpolizaInstr;
	@PortableProperty(IND_KSUBPOLIZAINSTR)
	private Integer ksubpolizaInstr;
	@PortableProperty(IND_KCERTIFICADOINSTR)
	private Integer kcertificadoInstr;
	@PortableProperty(IND_KMODALIDADORIG)
	private Integer kmodalidadOrig;
	@PortableProperty(IND_KPOLIZAORIG)
	private Long kpolizaOrig;
	@PortableProperty(IND_KSUBPOLIZAORIG)
	private Integer ksubpolizaOrig;
	@PortableProperty(IND_KCERTIFICADOORIG)
	private Integer kcertificadoOrig;
	
	public Integer getKmodalidadInstr() {
		return kmodalidadInstr;
	}
	public void setKmodalidadInstr(Integer kmodalidadInstr) {
		this.kmodalidadInstr = kmodalidadInstr;
	}
	public Long getKpolizaInstr() {
		return kpolizaInstr;
	}
	public void setKpolizaInstr(Long kpolizaInstr) {
		this.kpolizaInstr = kpolizaInstr;
	}
	public Integer getKsubpolizaInstr() {
		return ksubpolizaInstr;
	}
	public void setKsubpolizaInstr(Integer ksubpolizaInstr) {
		this.ksubpolizaInstr = ksubpolizaInstr;
	}
	public Integer getKcertificadoInstr() {
		return kcertificadoInstr;
	}
	public void setKcertificadoInstr(Integer kcertificadoInstr) {
		this.kcertificadoInstr = kcertificadoInstr;
	}
	public Integer getKmodalidadOrig() {
		return kmodalidadOrig;
	}
	public void setKmodalidadOrig(Integer kmodalidadOrig) {
		this.kmodalidadOrig = kmodalidadOrig;
	}
	public Long getKpolizaOrig() {
		return kpolizaOrig;
	}
	public void setKpolizaOrig(Long kpolizaOrig) {
		this.kpolizaOrig = kpolizaOrig;
	}
	public Integer getKsubpolizaOrig() {
		return ksubpolizaOrig;
	}
	public void setKsubpolizaOrig(Integer ksubpolizaOrig) {
		this.ksubpolizaOrig = ksubpolizaOrig;
	}
	public Integer getKcertificadoOrig() {
		return kcertificadoOrig;
	}
	public void setKcertificadoOrig(Integer kcertificadoOrig) {
		this.kcertificadoOrig = kcertificadoOrig;
	}
	
	@Override
	public PolizaInstrumentalKey getKey() {
		return new PolizaInstrumentalKey(kmodalidadInstr, kpolizaInstr, ksubpolizaInstr, kcertificadoInstr);	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kcertificadoInstr == null) ? 0 : kcertificadoInstr.hashCode());
		result = prime * result + ((kcertificadoOrig == null) ? 0 : kcertificadoOrig.hashCode());
		result = prime * result + ((kmodalidadInstr == null) ? 0 : kmodalidadInstr.hashCode());
		result = prime * result + ((kmodalidadOrig == null) ? 0 : kmodalidadOrig.hashCode());
		result = prime * result + ((kpolizaInstr == null) ? 0 : kpolizaInstr.hashCode());
		result = prime * result + ((kpolizaOrig == null) ? 0 : kpolizaOrig.hashCode());
		result = prime * result + ((ksubpolizaInstr == null) ? 0 : ksubpolizaInstr.hashCode());
		result = prime * result + ((ksubpolizaOrig == null) ? 0 : ksubpolizaOrig.hashCode());
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
		PolizaInstrumental other = (PolizaInstrumental) obj;
		if (kcertificadoInstr == null) {
			if (other.kcertificadoInstr != null)
				return false;
		} else if (!kcertificadoInstr.equals(other.kcertificadoInstr))
			return false;
		if (kcertificadoOrig == null) {
			if (other.kcertificadoOrig != null)
				return false;
		} else if (!kcertificadoOrig.equals(other.kcertificadoOrig))
			return false;
		if (kmodalidadInstr == null) {
			if (other.kmodalidadInstr != null)
				return false;
		} else if (!kmodalidadInstr.equals(other.kmodalidadInstr))
			return false;
		if (kmodalidadOrig == null) {
			if (other.kmodalidadOrig != null)
				return false;
		} else if (!kmodalidadOrig.equals(other.kmodalidadOrig))
			return false;
		if (kpolizaInstr == null) {
			if (other.kpolizaInstr != null)
				return false;
		} else if (!kpolizaInstr.equals(other.kpolizaInstr))
			return false;
		if (kpolizaOrig == null) {
			if (other.kpolizaOrig != null)
				return false;
		} else if (!kpolizaOrig.equals(other.kpolizaOrig))
			return false;
		if (ksubpolizaInstr == null) {
			if (other.ksubpolizaInstr != null)
				return false;
		} else if (!ksubpolizaInstr.equals(other.ksubpolizaInstr))
			return false;
		if (ksubpolizaOrig == null) {
			if (other.ksubpolizaOrig != null)
				return false;
		} else if (!ksubpolizaOrig.equals(other.ksubpolizaOrig))
			return false;
		return true;
	}
	
	
	
}
