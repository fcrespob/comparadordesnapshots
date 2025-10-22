package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.FlujosProbablesKey;
import es.mapfre.solvencia.dominio.EntidadBase;

/**
 * @author jpardoc
 * 
 */
@Portable
public class FlujosProbables implements EntidadBase<FlujosProbablesKey>, Cloneable {

	public static final int IND_MODALIDAD = 0;
	public static final int IND_GARANTIA = 1;
	public static final int IND_PRESTACION = 2;
	public static final int IND_BASETECNICA = 3;
	public static final int IND_VIDA = 4;
	public static final int IND_FALL = 5;
	public static final int IND_PRIM = 6;
	public static final int IND_INVA = 7;
	public static final int IND_GAST = 8;
	public static final int IND_COMI = 9;
	public static final int IND_ANUL = 10;
	public static final int IND_PROV_NOMINAL = 11;
	public static final int IND_PROV_TERMINAL = 12;
	public static final int IND_GTOAD = 13;

	@PortableProperty(IND_MODALIDAD)
	private Integer modalidad;
	@PortableProperty(IND_GARANTIA)
	private Integer garantia;
	@PortableProperty(IND_PRESTACION)
	private String prestacion;
	@PortableProperty(IND_BASETECNICA)
	private String basetecnica;

	@PortableProperty(IND_VIDA)
	private BloqueFlujosProbables vida;
	@PortableProperty(IND_FALL)
	private BloqueFlujosProbables fall;
	@PortableProperty(IND_PRIM)
	private BloqueFlujosProbables prim;
	@PortableProperty(IND_INVA)
	private BloqueFlujosProbables inva;
	@PortableProperty(IND_GAST)
	private BloqueFlujosProbables gast;
	@PortableProperty(IND_COMI)
	private BloqueFlujosProbables comi;
	@PortableProperty(IND_ANUL)
	private BloqueFlujosProbables anul;

	@PortableProperty(IND_PROV_NOMINAL)
	private String provNominal;
	@PortableProperty(IND_PROV_TERMINAL)
	private String provTerminal;
	
	@PortableProperty(IND_GTOAD)
	private BloqueFlujosProbables gtoad;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anul == null) ? 0 : anul.hashCode());
		result = prime * result + ((basetecnica == null) ? 0 : basetecnica.hashCode());
		result = prime * result + ((comi == null) ? 0 : comi.hashCode());
		result = prime * result + ((fall == null) ? 0 : fall.hashCode());
		result = prime * result + ((garantia == null) ? 0 : garantia.hashCode());
		result = prime * result + ((gast == null) ? 0 : gast.hashCode());
		result = prime * result + ((gtoad == null) ? 0 : gtoad.hashCode());
		result = prime * result + ((inva == null) ? 0 : inva.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result + ((prestacion == null) ? 0 : prestacion.hashCode());
		result = prime * result + ((prim == null) ? 0 : prim.hashCode());
		result = prime * result + ((provNominal == null) ? 0 : provNominal.hashCode());
		result = prime * result + ((provTerminal == null) ? 0 : provTerminal.hashCode());
		result = prime * result + ((vida == null) ? 0 : vida.hashCode());
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
		FlujosProbables other = (FlujosProbables) obj;
		if (anul == null) {
			if (other.anul != null)
				return false;
		} else if (!anul.equals(other.anul))
			return false;
		if (basetecnica == null) {
			if (other.basetecnica != null)
				return false;
		} else if (!basetecnica.equals(other.basetecnica))
			return false;
		if (comi == null) {
			if (other.comi != null)
				return false;
		} else if (!comi.equals(other.comi))
			return false;
		if (fall == null) {
			if (other.fall != null)
				return false;
		} else if (!fall.equals(other.fall))
			return false;
		if (garantia == null) {
			if (other.garantia != null)
				return false;
		} else if (!garantia.equals(other.garantia))
			return false;
		if (gast == null) {
			if (other.gast != null)
				return false;
		} else if (!gast.equals(other.gast))
			return false;
		if (gtoad == null) {
			if (other.gtoad != null)
				return false;
		} else if (!gtoad.equals(other.gtoad))
			return false;
		if (inva == null) {
			if (other.inva != null)
				return false;
		} else if (!inva.equals(other.inva))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		if (prestacion == null) {
			if (other.prestacion != null)
				return false;
		} else if (!prestacion.equals(other.prestacion))
			return false;
		if (prim == null) {
			if (other.prim != null)
				return false;
		} else if (!prim.equals(other.prim))
			return false;
		if (provNominal == null) {
			if (other.provNominal != null)
				return false;
		} else if (!provNominal.equals(other.provNominal))
			return false;
		if (provTerminal == null) {
			if (other.provTerminal != null)
				return false;
		} else if (!provTerminal.equals(other.provTerminal))
			return false;
		if (vida == null) {
			if (other.vida != null)
				return false;
		} else if (!vida.equals(other.vida))
			return false;
		return true;
	}

	public Integer getModalidad() {
		return modalidad;
	}

	public void setModalidad(Integer modalidad) {
		this.modalidad = modalidad;
	}

	public Integer getGarantia() {
		return garantia;
	}

	public void setGarantia(Integer garantia) {
		this.garantia = garantia;
	}

	public String getPrestacion() {
		return prestacion;
	}

	public void setPrestacion(String prestacion) {
		this.prestacion = prestacion;
	}

	public String getBasetecnica() {
		return basetecnica;
	}

	public void setBasetecnica(String basetecnica) {
		this.basetecnica = basetecnica;
	}

	public BloqueFlujosProbables getVida() {
		return vida;
	}

	public void setVida(BloqueFlujosProbables vida) {
		this.vida = vida;
	}

	public BloqueFlujosProbables getFall() {
		return fall;
	}

	public void setFall(BloqueFlujosProbables fall) {
		this.fall = fall;
	}

	public BloqueFlujosProbables getPrim() {
		return prim;
	}

	public void setPrim(BloqueFlujosProbables prim) {
		this.prim = prim;
	}

	public BloqueFlujosProbables getInva() {
		return inva;
	}

	public void setInva(BloqueFlujosProbables inva) {
		this.inva = inva;
	}

	public BloqueFlujosProbables getGast() {
		return gast;
	}

	public void setGast(BloqueFlujosProbables gast) {
		this.gast = gast;
	}

	public BloqueFlujosProbables getComi() {
		return comi;
	}

	public void setComi(BloqueFlujosProbables comi) {
		this.comi = comi;
	}

	public BloqueFlujosProbables getAnul() {
		return anul;
	}

	public void setAnul(BloqueFlujosProbables anul) {
		this.anul = anul;
	}

	public String getProvNominal() {
		return provNominal;
	}

	public void setProvNominal(String provNominal) {
		this.provNominal = provNominal;
	}

	public String getProvTerminal() {
		return provTerminal;
	}

	public void setProvTerminal(String provTerminal) {
		this.provTerminal = provTerminal;
	}

	public BloqueFlujosProbables getGtoad() {
		return gtoad;
	}

	public void setGtoad(BloqueFlujosProbables gtoad) {
		this.gtoad = gtoad;
	}

	@Override
	public FlujosProbablesKey getKey() {
		return new FlujosProbablesKey(modalidad, garantia, prestacion, basetecnica);
	}
	@Override
	protected FlujosProbables clone() throws CloneNotSupportedException {
//		return new FlujosProbables(this.modalidad, this.garantia, this.prestacion, this.basetecnica, this.vida,
//				this.fall, this.prim, this.inva, this.gast, this.comi, this.anul, this.provNominal, this.provTerminal);
		FlujosProbables obj = null;
		try {
			obj = (FlujosProbables) super.clone();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new FlujosProbables(this.modalidad, this.garantia, this.prestacion, this.basetecnica,
				obj.vida = (BloqueFlujosProbables) obj.vida.clone(), 
				obj.fall = (BloqueFlujosProbables) obj.fall.clone(),
				obj.prim = (BloqueFlujosProbables) obj.prim.clone(),
				obj.inva = (BloqueFlujosProbables) obj.inva.clone(),
				obj.gast = (BloqueFlujosProbables) obj.gast.clone(),
				obj.comi = (BloqueFlujosProbables) obj.comi.clone(),
				obj.anul = (BloqueFlujosProbables) obj.anul.clone(), 
				obj.gtoad = (BloqueFlujosProbables) obj.anul.clone(), this.provNominal, this.provTerminal);
	}

	public FlujosProbables(Integer modalidad, Integer garantia, String prestacion, String basetecnica,
			BloqueFlujosProbables vida, BloqueFlujosProbables fall, BloqueFlujosProbables prim,
			BloqueFlujosProbables inva, BloqueFlujosProbables gast, BloqueFlujosProbables comi,
			BloqueFlujosProbables anul, BloqueFlujosProbables gtoad, String provNominal, String provTerminal) {
		super();
		this.modalidad = modalidad;
		this.garantia = garantia;
		this.prestacion = prestacion;
		this.basetecnica = basetecnica;
		this.vida = vida;
		this.fall = fall;
		this.prim = prim;
		this.inva = inva;
		this.gast = gast;
		this.comi = comi;
		this.anul = anul;
		this.gtoad = gtoad;
		this.provNominal = provNominal;
		this.provTerminal = provTerminal;
	}

	public FlujosProbables clonar() throws CloneNotSupportedException {
		return (FlujosProbables) this.clone();
	}

	public FlujosProbables() {
		super();
	}

}
