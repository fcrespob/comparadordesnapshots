package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.DatosEspecificosKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosEspecificos implements EntidadBase<DatosEspecificosKey> {

	public static final int IND_CODIGO = 0;
	public static final int IND_DATO = 1;
	public static final int IND_KCERTI = 2;
	public static final int IND_KPOLIZA = 3;
	public static final int IND_KSUBPOL = 4;
	public static final int IND_NSUSCRI = 5;

	@PortableProperty(IND_CODIGO)
	private String codigo;
	@PortableProperty(IND_DATO)
	private String dato;
	@PortableProperty(IND_KCERTI)
	private Integer kcerti;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOL)
	private Integer ksubpol;

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDato() {
		return dato;
	}

	public void setDato(String dato) {
		this.dato = dato;
	}

	public Integer getKcerti() {
		return kcerti;
	}

	public void setKcerti(Integer kcerti) {
		this.kcerti = kcerti;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpol() {
		return ksubpol;
	}

	public void setKsubpol(Integer ksubpol) {
		this.ksubpol = ksubpol;
	}

	public Integer getnsuscri() {
		return nsuscri;
	}
	
	public void setnsuscri(Integer nsuscri){
		this.nsuscri = nsuscri;
	}

	@Override
	public DatosEspecificosKey getKey() {
		return new DatosEspecificosKey(kpoliza, ksubpol, kcerti, nsuscri, codigo, dato);
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
		DatosEspecificos other = (DatosEspecificos) obj;
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