package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.math.BigDecimal;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.TablaExperiencia;

@Portable
public class TablaExperienciaKey {

	@PortableProperty(TablaExperiencia.IND_KTABLA)
	private Integer ktabla;
	@PortableProperty(TablaExperiencia.IND_K2TIPOVALOR) 
	private String k2tipovalor;
	@PortableProperty(TablaExperiencia.IND_KANACIMIENTO)
	private String kanacimiento;
	@PortableProperty(TablaExperiencia.IND_KINTERES)
	private java.math.BigDecimal kinteres;
	@PortableProperty(TablaExperiencia.IND_KSOBREMORT)
	private java.math.BigDecimal ksobremort;
	@PortableProperty(TablaExperiencia.IND_KSOBRERIES)
	private java.math.BigDecimal ksobreries;
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((k2tipovalor == null) ? 0 : k2tipovalor.hashCode());
		result = prime * result
				+ ((kanacimiento == null) ? 0 : kanacimiento.hashCode());
		result = prime * result
				+ ((kinteres == null) ? 0 : kinteres.hashCode());
		result = prime * result
				+ ((ksobremort == null) ? 0 : ksobremort.hashCode());
		result = prime * result
				+ ((ksobreries == null) ? 0 : ksobreries.hashCode());
		result = prime * result + ((ktabla == null) ? 0 : ktabla.hashCode());
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
		TablaExperienciaKey other = (TablaExperienciaKey) obj;
		if (k2tipovalor == null) {
			if (other.k2tipovalor != null) {
				return false;
			}
		} else if (!k2tipovalor.equals(other.k2tipovalor)) {
			return false;
		}
		if (kanacimiento == null) {
			if (other.kanacimiento != null) {
				return false;
			}
		} else if (!kanacimiento.equals(other.kanacimiento)) {
			return false;
		}
		if (kinteres == null) {
			if (other.kinteres != null) {
				return false;
			}
		} else if (!kinteres.equals(other.kinteres)) {
			return false;
		}
		if (ksobremort == null) {
			if (other.ksobremort != null) {
				return false;
			}
		} else if (!ksobremort.equals(other.ksobremort)) {
			return false;
		}
		if (ksobreries == null) {
			if (other.ksobreries != null) {
				return false;
			}
		} else if (!ksobreries.equals(other.ksobreries)) {
			return false;
		}
		if (ktabla == null) {
			if (other.ktabla != null) {
				return false;
			}
		} else if (!ktabla.equals(other.ktabla)) {
			return false;
		}
		return true;
	}
	
	public TablaExperienciaKey(Integer ktabla, String k2tipovalor,
			String kanacimiento, BigDecimal kinteres, BigDecimal ksobremort,
			BigDecimal ksobreries) {
		super();
		this.ktabla = ktabla;
		this.k2tipovalor = k2tipovalor;
		this.kanacimiento = kanacimiento;
		this.kinteres = kinteres;
		this.ksobremort = ksobremort;
		this.ksobreries = ksobreries;
	}
	
	public TablaExperienciaKey() {
		super();
	}

	
	
	
}