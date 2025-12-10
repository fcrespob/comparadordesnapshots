package es.mapfre.coaseguro.tirea.dominio.keys;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class DatosEspecificKey implements Comparable<DatosEspecificKey> {

	private Long kpoliza;
	private Integer ksubpol;
	private Integer kcerti;
	private Integer nsuscri;
	private String codigo;
	private String dato;
	
	public DatosEspecificKey() {
		super();
	}

	public DatosEspecificKey(Long kpoliza, Integer ksubpoliza,
			Integer kcerti, Integer nsuscri, String codigo, String dato) {
		this.kpoliza = kpoliza;
		this.ksubpol = ksubpoliza;
		this.kcerti = kcerti;
		this.nsuscri = nsuscri;
		this.codigo = codigo;
		this.dato = dato;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((dato == null) ? 0 : dato.hashCode());
		result = prime * result + ((kcerti == null) ? 0 : kcerti.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
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
		DatosEspecificKey other = (DatosEspecificKey) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (dato == null) {
			if (other.dato != null)
				return false;
		} else if (!dato.equals(other.dato))
			return false;
		if (kcerti == null) {
			if (other.kcerti != null)
				return false;
		} else if (!kcerti.equals(other.kcerti))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpol == null) {
			if (other.ksubpol != null)
				return false;
		} else if (!ksubpol.equals(other.ksubpol))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "DatosEspecificKey [kpoliza=" + kpoliza + ", ksubpol=" + ksubpol + ", kcerti=" + kcerti + ", nsuscri="
				+ nsuscri + ", codigo=" + codigo + ", dato=" + dato + "]";
	}
	
	public int compareTo(DatosEspecificKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpol, o.ksubpol);
		compareToBuilder.append(this.kcerti, o.kcerti);
		compareToBuilder.append(this.nsuscri, o.nsuscri);
		compareToBuilder.append(this.codigo, o.codigo);
		compareToBuilder.append(this.dato, o.dato);
		return compareToBuilder.toComparison();
	}


}