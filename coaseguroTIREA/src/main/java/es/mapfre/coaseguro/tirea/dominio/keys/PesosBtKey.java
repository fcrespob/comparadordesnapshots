package es.mapfre.coaseguro.tirea.dominio.keys;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

public class PesosBtKey implements Comparable<PesosBtKey>{

	private String cnegocio;
	private Integer ccanal;
	private Integer ccartera;
	private Timestamp fcierre;
	private Integer kmodalidad;
	private String cohorte;
	private String swcasado;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer kcertificado;
	
	public PesosBtKey() {
		super();
	}

	public PesosBtKey(String cnegocio, Integer ccanal, Integer ccartera, Timestamp fcierre, String cohorte,
			String swcasado, Long kpoliza, Integer ksubpoliza, Integer kcertificado, Integer kmodalidad) {
		super();
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.ccartera = ccartera;
		this.fcierre = fcierre;
		this.kmodalidad = kmodalidad;
		this.cohorte = cohorte;
		this.swcasado = swcasado;
		this.kpoliza = kpoliza;
		this.kcertificado = kcertificado;
		this.ksubpoliza = ksubpoliza;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((cohorte == null) ? 0 : cohorte.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
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
		PesosBtKey other = (PesosBtKey) obj;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (ccartera == null) {
			if (other.ccartera != null)
				return false;
		} else if (!ccartera.equals(other.ccartera))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (cohorte == null) {
			if (other.cohorte != null)
				return false;
		} else if (!cohorte.equals(other.cohorte))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
			return false;
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		return true;
	}

	@Override
	public int compareTo(PesosBtKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.cnegocio, o.cnegocio);
		compareToBuilder.append(this.ccanal, o.ccanal);
		compareToBuilder.append(this.ccartera, o.ccartera);
		compareToBuilder.append(this.fcierre, o.fcierre);
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		compareToBuilder.append(this.kcertificado, o.kcertificado);
		compareToBuilder.append(this.cohorte, o.cohorte);
		compareToBuilder.append(this.swcasado, o.swcasado);
		return compareToBuilder.toComparison();
	}
	
}
