package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.OpcionesGeneracionKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class OpcionesGeneracion implements EntidadBase<OpcionesGeneracionKey> {

	public static final int IND_MODALIDAD = 0;
	public static final int IND_GARANTIA = 1;
	public static final int IND_PRESTACION = 2;
	public static final int IND_PERIODICIDAD = 3;
	public static final int IND_VENCIMIENTO = 4;
	public static final int IND_LIMITE = 5;
	public static final int IND_DEVENGOVIDA = 6;
	public static final int IND_PAGOVIDA = 7;
	public static final int IND_DEVENGOFALLECIMIENT = 8;
	public static final int IND_PAGOFALLECIMIENTO = 9;
	public static final int IND_DEVENGOINVALIDEZ = 10;
	public static final int IND_PAGOINVALIDEZ = 11;
	public static final int IND_DEVENGOPRIMAS = 12;
	public static final int IND_PAGOPRIMAS = 13;
	public static final int IND_DEVENGOGASTOS = 14;
	public static final int IND_PAGOGASTOS = 15;
	public static final int IND_DEVENGOANULACIONES = 16;
	public static final int IND_PAGOANULACIONES = 17;
	public static final int IND_DEVENGOCOMISIONES = 18;
	public static final int IND_PAGOCOMISIONES = 19;
	public static final int IND_DEVENGOGTOAD = 20;
	public static final int IND_PAGOGTOAD = 21;

	@PortableProperty(IND_MODALIDAD) private Integer modalidad;
	@PortableProperty(IND_GARANTIA) private Integer garantia;
	@PortableProperty(IND_PRESTACION) private String prestacion;
	@PortableProperty(IND_PERIODICIDAD) private String periodicidad;
	@PortableProperty(IND_VENCIMIENTO) private String vencimiento;
	@PortableProperty(IND_LIMITE) private String limite;
	@PortableProperty(IND_DEVENGOVIDA) private String devengovida;
	@PortableProperty(IND_PAGOVIDA) private String pagovida;
	@PortableProperty(IND_DEVENGOFALLECIMIENT) private String devengofallecimient;
	@PortableProperty(IND_PAGOFALLECIMIENTO) private String pagofallecimiento;
	@PortableProperty(IND_DEVENGOINVALIDEZ) private String devengoinvalidez;
	@PortableProperty(IND_PAGOINVALIDEZ) private String pagoinvalidez;
	@PortableProperty(IND_DEVENGOPRIMAS) private String devengoprimas;
	@PortableProperty(IND_PAGOPRIMAS) private String pagoprimas;
	@PortableProperty(IND_DEVENGOGASTOS) private String devengogastos;
	@PortableProperty(IND_PAGOGASTOS) private String pagogastos;
	@PortableProperty(IND_DEVENGOANULACIONES) private String devengoanulaciones;
	@PortableProperty(IND_PAGOANULACIONES) private String pagoanulaciones;
	@PortableProperty(IND_DEVENGOCOMISIONES) private String devengocomisiones;
	@PortableProperty(IND_PAGOCOMISIONES) private String pagocomisiones;
	@PortableProperty(IND_DEVENGOGTOAD) private String devengogtoad;
	@PortableProperty(IND_PAGOGTOAD) private String pagogtoad;

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
	public String getPeriodicidad() {
		return periodicidad;
	}
	public void setPeriodicidad(String periodicidad) {
		this.periodicidad = periodicidad;
	}
	public String getVencimiento() {
		return vencimiento;
	}
	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}
	public String getLimite() {
		return limite;
	}
	public void setLimite(String limite) {
		this.limite = limite;
	}
	public String getDevengovida() {
		return devengovida;
	}
	public void setDevengovida(String devengovida) {
		this.devengovida = devengovida;
	}
	public String getPagovida() {
		return pagovida;
	}
	public void setPagovida(String pagovida) {
		this.pagovida = pagovida;
	}
	public String getDevengofallecimient() {
		return devengofallecimient;
	}
	public void setDevengofallecimient(String devengofallecimient) {
		this.devengofallecimient = devengofallecimient;
	}
	public String getPagofallecimiento() {
		return pagofallecimiento;
	}
	public void setPagofallecimiento(String pagofallecimiento) {
		this.pagofallecimiento = pagofallecimiento;
	}
	public String getDevengoinvalidez() {
		return devengoinvalidez;
	}
	public void setDevengoinvalidez(String devengoinvalidez) {
		this.devengoinvalidez = devengoinvalidez;
	}
	public String getPagoinvalidez() {
		return pagoinvalidez;
	}
	public void setPagoinvalidez(String pagoinvalidez) {
		this.pagoinvalidez = pagoinvalidez;
	}
	public String getDevengoprimas() {
		return devengoprimas;
	}
	public void setDevengoprimas(String devengoprimas) {
		this.devengoprimas = devengoprimas;
	}
	public String getPagoprimas() {
		return pagoprimas;
	}
	public void setPagoprimas(String pagoprimas) {
		this.pagoprimas = pagoprimas;
	}
	public String getDevengogastos() {
		return devengogastos;
	}
	public void setDevengogastos(String devengogastos) {
		this.devengogastos = devengogastos;
	}
	public String getPagogastos() {
		return pagogastos;
	}
	public void setPagogastos(String pagogastos) {
		this.pagogastos = pagogastos;
	}
	public String getDevengoanulaciones() {
		return devengoanulaciones;
	}
	public void setDevengoanulaciones(String devengoanulaciones) {
		this.devengoanulaciones = devengoanulaciones;
	}
	public String getPagoanulaciones() {
		return pagoanulaciones;
	}
	public void setPagoanulaciones(String pagoanulaciones) {
		this.pagoanulaciones = pagoanulaciones;
	}
	public String getDevengocomisiones() {
		return devengocomisiones;
	}
	public void setDevengocomisiones(String devengocomisiones) {
		this.devengocomisiones = devengocomisiones;
	}
	public String getPagocomisiones() {
		return pagocomisiones;
	}
	public void setPagocomisiones(String pagocomisiones) {
		this.pagocomisiones = pagocomisiones;
	}	
	public String getDevengogtoad() {
		return devengogtoad;
	}
	public void setDevengogtoad(String devengogtoad) {
		this.devengogtoad = devengogtoad;
	}
	public String getPagogtoad() {
		return pagogtoad;
	}
	public void setPagogtoad(String pagogtoad) {
		this.pagogtoad = pagogtoad;
	}
	
	public String getPrestacion() {
		return prestacion;
	}
	public void setPrestacion(String prestacion) {
		this.prestacion = prestacion;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((devengoanulaciones == null) ? 0 : devengoanulaciones.hashCode());
		result = prime * result + ((devengocomisiones == null) ? 0 : devengocomisiones.hashCode());
		result = prime * result + ((devengofallecimient == null) ? 0 : devengofallecimient.hashCode());
		result = prime * result + ((devengogastos == null) ? 0 : devengogastos.hashCode());
		result = prime * result + ((devengogtoad == null) ? 0 : devengogtoad.hashCode());
		result = prime * result + ((devengoinvalidez == null) ? 0 : devengoinvalidez.hashCode());
		result = prime * result + ((devengoprimas == null) ? 0 : devengoprimas.hashCode());
		result = prime * result + ((devengovida == null) ? 0 : devengovida.hashCode());
		result = prime * result + ((garantia == null) ? 0 : garantia.hashCode());
		result = prime * result + ((limite == null) ? 0 : limite.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result + ((pagoanulaciones == null) ? 0 : pagoanulaciones.hashCode());
		result = prime * result + ((pagocomisiones == null) ? 0 : pagocomisiones.hashCode());
		result = prime * result + ((pagofallecimiento == null) ? 0 : pagofallecimiento.hashCode());
		result = prime * result + ((pagogastos == null) ? 0 : pagogastos.hashCode());
		result = prime * result + ((pagogtoad == null) ? 0 : pagogtoad.hashCode());
		result = prime * result + ((pagoinvalidez == null) ? 0 : pagoinvalidez.hashCode());
		result = prime * result + ((pagoprimas == null) ? 0 : pagoprimas.hashCode());
		result = prime * result + ((pagovida == null) ? 0 : pagovida.hashCode());
		result = prime * result + ((periodicidad == null) ? 0 : periodicidad.hashCode());
		result = prime * result + ((prestacion == null) ? 0 : prestacion.hashCode());
		result = prime * result + ((vencimiento == null) ? 0 : vencimiento.hashCode());
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
		OpcionesGeneracion other = (OpcionesGeneracion) obj;
		if (devengoanulaciones == null) {
			if (other.devengoanulaciones != null)
				return false;
		} else if (!devengoanulaciones.equals(other.devengoanulaciones))
			return false;
		if (devengocomisiones == null) {
			if (other.devengocomisiones != null)
				return false;
		} else if (!devengocomisiones.equals(other.devengocomisiones))
			return false;
		if (devengofallecimient == null) {
			if (other.devengofallecimient != null)
				return false;
		} else if (!devengofallecimient.equals(other.devengofallecimient))
			return false;
		if (devengogastos == null) {
			if (other.devengogastos != null)
				return false;
		} else if (!devengogastos.equals(other.devengogastos))
			return false;
		if (devengogtoad == null) {
			if (other.devengogtoad != null)
				return false;
		} else if (!devengogtoad.equals(other.devengogtoad))
			return false;
		if (devengoinvalidez == null) {
			if (other.devengoinvalidez != null)
				return false;
		} else if (!devengoinvalidez.equals(other.devengoinvalidez))
			return false;
		if (devengoprimas == null) {
			if (other.devengoprimas != null)
				return false;
		} else if (!devengoprimas.equals(other.devengoprimas))
			return false;
		if (devengovida == null) {
			if (other.devengovida != null)
				return false;
		} else if (!devengovida.equals(other.devengovida))
			return false;
		if (garantia == null) {
			if (other.garantia != null)
				return false;
		} else if (!garantia.equals(other.garantia))
			return false;
		if (limite == null) {
			if (other.limite != null)
				return false;
		} else if (!limite.equals(other.limite))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		if (pagoanulaciones == null) {
			if (other.pagoanulaciones != null)
				return false;
		} else if (!pagoanulaciones.equals(other.pagoanulaciones))
			return false;
		if (pagocomisiones == null) {
			if (other.pagocomisiones != null)
				return false;
		} else if (!pagocomisiones.equals(other.pagocomisiones))
			return false;
		if (pagofallecimiento == null) {
			if (other.pagofallecimiento != null)
				return false;
		} else if (!pagofallecimiento.equals(other.pagofallecimiento))
			return false;
		if (pagogastos == null) {
			if (other.pagogastos != null)
				return false;
		} else if (!pagogastos.equals(other.pagogastos))
			return false;
		if (pagogtoad == null) {
			if (other.pagogtoad != null)
				return false;
		} else if (!pagogtoad.equals(other.pagogtoad))
			return false;
		if (pagoinvalidez == null) {
			if (other.pagoinvalidez != null)
				return false;
		} else if (!pagoinvalidez.equals(other.pagoinvalidez))
			return false;
		if (pagoprimas == null) {
			if (other.pagoprimas != null)
				return false;
		} else if (!pagoprimas.equals(other.pagoprimas))
			return false;
		if (pagovida == null) {
			if (other.pagovida != null)
				return false;
		} else if (!pagovida.equals(other.pagovida))
			return false;
		if (periodicidad == null) {
			if (other.periodicidad != null)
				return false;
		} else if (!periodicidad.equals(other.periodicidad))
			return false;
		if (prestacion == null) {
			if (other.prestacion != null)
				return false;
		} else if (!prestacion.equals(other.prestacion))
			return false;
		if (vencimiento == null) {
			if (other.vencimiento != null)
				return false;
		} else if (!vencimiento.equals(other.vencimiento))
			return false;
		return true;
	}
	@Override
	public OpcionesGeneracionKey getKey() {
		return new OpcionesGeneracionKey(modalidad,garantia,prestacion);
	}
}