package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.maestro.DatosEspecificos;

@Portable
public class DatosEspecificosKey {

	@PortableProperty(DatosEspecificos.IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(DatosEspecificos.IND_KSUBPOL)
	private Integer ksubpol;
	@PortableProperty(DatosEspecificos.IND_KCERTI)
	private Integer kcerti;
	@PortableProperty(DatosEspecificos.IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(DatosEspecificos.IND_CODIGO)
	private String codigo;
	@PortableProperty(DatosEspecificos.IND_DATO)
	private String dato;

	public DatosEspecificosKey(Long kpoliza, Integer ksubpoliza,
			Integer kcerti, Integer nsuscri, String codigo, String dato) {
		this.kpoliza = kpoliza;
		this.ksubpol = ksubpoliza;
		this.kcerti = kcerti;
		this.nsuscri = nsuscri;
		this.codigo = codigo;
		this.dato = dato;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((dato == null) ? 0 : dato.hashCode());
		result = prime * result + ((kcerti == null) ? 0 : kcerti.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		DatosEspecificosKey other = (DatosEspecificosKey) obj;
		if (codigo == null) {
			if (other.codigo != null) {
				return false;
			}
		} else if (!codigo.equals(other.codigo)) {
			return false;
		}
		if (dato == null) {
			if (other.dato != null) {
				return false;
			}
		} else if (!dato.equals(other.dato)) {
			return false;
		}
		if (kcerti == null) {
			if (other.kcerti != null) {
				return false;
			}
		} else if (!kcerti.equals(other.kcerti)) {
			return false;
		}
		if (nsuscri == null) {
			if (other.nsuscri != null) {
				return false;
			}
		} else if (!nsuscri.equals(other.nsuscri)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (ksubpol == null) {
			if (other.ksubpol != null) {
				return false;
			}
		} else if (!ksubpol.equals(other.ksubpol)) {
			return false;
		}
		return true;
	}

}