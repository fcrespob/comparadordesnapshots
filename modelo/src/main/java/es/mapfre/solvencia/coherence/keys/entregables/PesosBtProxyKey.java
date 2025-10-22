package es.mapfre.solvencia.coherence.keys.entregables;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.PesosBt;

@Portable
public class PesosBtProxyKey implements Comparable<PesosBtProxyKey>{

	@PortableProperty(PesosBt.IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(PesosBt.IND_CCANAL)
	private String ccanal;
	@PortableProperty(PesosBt.IND_CCARTERA)
	private String ccartera;
	@PortableProperty(PesosBt.IND_FCIERRE)
	private String fcierre;
	@PortableProperty(PesosBt.IND_KMODALIDAD)
	private String kmodalidad;
	@PortableProperty(PesosBt.IND_COHORTE)
	private String cohorte;
	@PortableProperty(PesosBt.IND_SWCASADO)
	private String swcasado;
	@PortableProperty(PesosBt.IND_KPOLIZA)
	private String kpoliza;
	@PortableProperty(PesosBt.IND_KSUBPOLIZA)
	private String ksubpoliza;
	@PortableProperty(PesosBt.IND_KCERTIFICADO)
	private String kcertificado;
	
	public PesosBtProxyKey() {
		super();
	}

	public PesosBtProxyKey(String cnegocio, String ccanal, String ccartera, String fcierre, String cohorte,
			String swcasado, String kpoliza, String ksubpoliza, String kcertificado, String kmodalidad) {
		super();
		this.cnegocio = cnegocio;
		this.ccanal = ccanal;
		this.ccartera = ccartera;
		this.fcierre = fcierre;
		this.cohorte = cohorte;
		this.swcasado = swcasado;
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
		this.kcertificado = kcertificado;
		this.kmodalidad = kmodalidad;
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
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
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
		PesosBtProxyKey other = (PesosBtProxyKey) obj;
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
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
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
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		return true;
	}

	@Override
	public int compareTo(PesosBtProxyKey o) {
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
