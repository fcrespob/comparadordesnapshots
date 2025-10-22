package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;


import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ComisionesParticipadasCOM;

@Portable
public class ComisionesParticipadasCOMKey{
	
	@PortableProperty(ComisionesParticipadasCOM.IND_KCARTEORIG)
	private Integer kCarteOrig;
	@PortableProperty(ComisionesParticipadasCOM.IND_KMODALIDAD)
	private Integer kModalidad;
	@PortableProperty(ComisionesParticipadasCOM.IND_KGARANTIA)
	private Integer kGarantia;
	@PortableProperty(ComisionesParticipadasCOM.IND_KFVIGDES)
	private Timestamp kFvigDes;
	@PortableProperty(ComisionesParticipadasCOM.IND_KFVIGHAS)
	private Timestamp kFvigHas;
	@PortableProperty(ComisionesParticipadasCOM.IND_KFVIGDES)
	private Timestamp faltaDes;
	@PortableProperty(ComisionesParticipadasCOM.IND_KFVIGHAS)
	private Timestamp faltaHas;

	public ComisionesParticipadasCOMKey(Integer kCarteOrig, Integer kModalidad, Integer kGarantia,
			Timestamp kFvigDes, Timestamp kFvigHas, Timestamp faltaDes, Timestamp faltaHas) {
		super();
		this.kCarteOrig = kCarteOrig;
		this.kModalidad = kModalidad;
		this.kGarantia = kGarantia;
		this.kFvigDes = kFvigDes;
		this.kFvigHas = kFvigHas;
		this.faltaDes = faltaDes;
		this.faltaHas = faltaHas;
	}

	public ComisionesParticipadasCOMKey() {
		super();
	}

	public Integer getkCarteOrig() {
		return kCarteOrig;
	}

	public void setkCarteOrig(Integer kCarteOrig) {
		this.kCarteOrig = kCarteOrig;
	}

	public Integer getkModalidad() {
		return kModalidad;
	}

	public void setkModalidad(Integer kModalidad) {
		this.kModalidad = kModalidad;
	}
	
	public Integer getkGarantia() {
		return kGarantia;
	}

	public void setkGarantia(Integer kGarantia) {
		this.kGarantia = kGarantia;
	}

	public Timestamp getkFvigDes() {
		return kFvigDes;
	}

	public void setkFvigDes(Timestamp kFvigDes) {
		this.kFvigDes = kFvigDes;
	}
	
	public Timestamp getkFvigHas() {
		return kFvigHas;
	}

	public void setkFvigHas(Timestamp kFvigHas) {
		this.kFvigHas = kFvigHas;
	}
	
	public Timestamp getFaltaDes() {
		return faltaDes;
	}

	public void setFaltaDes(Timestamp faltaDes) {
		this.faltaDes = faltaDes;
	}
	
	public Timestamp getFaltaHas() {
		return faltaHas;
	}

	public void setFaltaHas(Timestamp faltaHas) {
		this.faltaHas = faltaHas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kCarteOrig == null) ? 0 : kCarteOrig.hashCode());
		result = prime * result + ((kGarantia == null) ? 0 : kGarantia.hashCode());
		result = prime * result + ((kModalidad == null) ? 0 : kModalidad.hashCode());
		result = prime * result + ((kFvigDes == null) ? 0 : kFvigDes.hashCode());
		result = prime * result + ((kFvigHas == null) ? 0 : kFvigHas.hashCode());
		result = prime * result + ((faltaDes == null) ? 0 : faltaDes.hashCode());
		result = prime * result + ((faltaHas == null) ? 0 : faltaHas.hashCode());
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
		ComisionesParticipadasCOMKey other = (ComisionesParticipadasCOMKey) obj;
		if (kCarteOrig == null) {
			if (other.kCarteOrig != null)
				return false;
		} else if (!kCarteOrig.equals(other.kCarteOrig))
			return false;
		if (kGarantia == null) {
			if (other.kGarantia != null)
				return false;
		} else if (!kGarantia.equals(other.kGarantia))
			return false;
		if (kModalidad == null) {
			if (other.kModalidad != null)
				return false;
		} else if (!kModalidad.equals(other.kModalidad))
			return false;
		if (kFvigDes == null) {
			if (other.kFvigDes != null)
				return false;
		} else if (!kFvigDes.equals(other.kFvigDes))
			return false;
		if (kFvigHas == null) {
			if (other.kFvigHas != null)
				return false;
		} else if (!kFvigHas.equals(other.kFvigHas))
			return false;
		if (faltaDes == null) {
			if (other.faltaDes != null)
				return false;
		} else if (!faltaDes.equals(other.faltaDes))
			return false;
		if (faltaHas == null) {
			if (other.faltaHas != null)
				return false;
		} else if (!faltaHas.equals(other.faltaHas))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ComisionesParticipadasCOMKey [kCarteOrig=" + kCarteOrig + ", kModalidad=" + kModalidad
				+ ", kGarantia=" + kGarantia + ", kFvigDes=" + kFvigDes + ", kFigHas=" + kFvigHas + ", faltaDes=" + faltaDes +  ", faltaHas=" + faltaHas + "]";
	}

}
