package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class BloqueFlujosProbables implements Cloneable{
	public static final int IND_NOMINAL = 1;
	public static final int IND_PROBABLE = 2;
	public static final int IND_NOANULADO = 3;
	public static final int IND_ACTUALIZADO = 4;
	public static final int IND_PROVI = 5;


	@PortableProperty(IND_NOMINAL)
	private String nominal;
	@PortableProperty(IND_PROBABLE)
	private String probable;
	@PortableProperty(IND_NOANULADO)
	private String noanulado;
	@PortableProperty(IND_ACTUALIZADO)
	private String actualizado;
	@PortableProperty(IND_PROVI)
	private String provi;
	
	public String getNominal() {
		return nominal;
	}
	public void setNominal(String nominal) {
		this.nominal = nominal;
	}
	public String getProbable() {
		return probable;
	}
	public void setProbable(String probable) {
		this.probable = probable;
	}
	public String getNoanulado() {
		return noanulado;
	}
	public void setNoanulado(String noanulado) {
		this.noanulado = noanulado;
	}
	public String getActualizado() {
		return actualizado;
	}
	public void setActualizado(String actualizado) {
		this.actualizado = actualizado;
	}
	public String getProvi() {
		return provi;
	}
	public void setProvi(String provi) {
		this.provi = provi;
	}
	@Override //NOSONAR
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actualizado == null) ? 0 : actualizado.hashCode());
		result = prime * result + ((noanulado == null) ? 0 : noanulado.hashCode());
		result = prime * result + ((nominal == null) ? 0 : nominal.hashCode());
		result = prime * result + ((probable == null) ? 0 : probable.hashCode());
		result = prime * result + ((provi == null) ? 0 : provi.hashCode());
		return result;
	}
	@Override //NOSONAR
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		BloqueFlujosProbables other = (BloqueFlujosProbables) obj;
		if (actualizado == null) {
			if (other.actualizado != null) {
				return false;
			}
		} else if (!actualizado.equals(other.actualizado)) {
			return false;
		}
		if (noanulado == null) {
			if (other.noanulado != null) {
				return false;
			}
		} else if (!noanulado.equals(other.noanulado)) {
			return false;
		}
		if (nominal == null) {
			if (other.nominal != null) {
				return false;
			}
		} else if (!nominal.equals(other.nominal)) {
			return false;
		}
		if (probable == null) {
			if (other.probable != null) {
				return false;
			}
		} else if (!probable.equals(other.probable)) {
			return false;
		}
		if (provi == null) {
			if (other.provi != null) {
				return false;
			}
		} else if (!provi.equals(other.provi)) {
			return false;
		}
		return true;
	}
	@Override
	protected BloqueFlujosProbables clone() throws CloneNotSupportedException {
		return new BloqueFlujosProbables(this.nominal, this.probable, this.noanulado, this.actualizado, this.provi);
	}

	public BloqueFlujosProbables(String nominal, String probable, String noanulado, String actualizado, String provi) {
		super();
		this.nominal = nominal;
		this.probable = probable;
		this.noanulado = noanulado;
		this.actualizado = actualizado;
		this.provi = provi;
	}

	public BloqueFlujosProbables clonar() throws CloneNotSupportedException {
		return (BloqueFlujosProbables) this.clone();
	}

	public BloqueFlujosProbables() {
		super();
	}
}
