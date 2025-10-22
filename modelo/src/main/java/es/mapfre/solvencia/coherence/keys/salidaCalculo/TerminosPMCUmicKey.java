package es.mapfre.solvencia.coherence.keys.salidaCalculo;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.salidaCalculo.TerminosPMCUmic;

@Portable
public class TerminosPMCUmicKey {
	
	@PortableProperty(TerminosPMCUmic.IND_CLAVEUMIC)	private UmicKey claveUmic;
	@PortableProperty(TerminosPMCUmic.IND_BT)			private String bt;
	@PortableProperty(TerminosPMCUmic.IND_INTP)			private Integer iteracion;
	
	public TerminosPMCUmicKey() {
		super();
	}
	
	public TerminosPMCUmicKey (UmicKey claveUmic, String bt, Integer iteracion) {
		super();
		this.claveUmic = claveUmic;
		this.bt = bt;
		this.iteracion = iteracion;
	}
	
	public UmicKey getUmicKey() {
		return claveUmic;
	}
	
	public String getBaseTec() {
		return bt;
	}
	
	public Integer getIteracion(){
		return iteracion;
	}

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result
				+ ((claveUmic == null) ? 0 : claveUmic.hashCode());
		result = prime * result
				+ ((iteracion == null) ? 0 : iteracion.hashCode());
		return result;
	}

	@Override
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
		TerminosPMCUmicKey other = (TerminosPMCUmicKey) obj;
		if (bt == null) {
			if (other.bt != null) {
				return false;
			}
		} else if (!bt.equals(other.bt)) {
			return false;
		}
		if (claveUmic == null) {
			if (other.claveUmic != null) {
				return false;
			}
		} else if (!claveUmic.equals(other.claveUmic)) {
			return false;
		}
		if (iteracion == null) {
			if (other.iteracion != null) {
				return false;
			}
		} else if (!iteracion.equals(other.iteracion)) {
			return false;
		}
		return true;
	}

	public int compareTo(TerminosPMCUmicKey o) {
		if (o == null) {
			return -1;
		}

		CompareToBuilder compareToBuilder = new CompareToBuilder();

		compareToBuilder.append(this.claveUmic, o.claveUmic);
		compareToBuilder.append(this.bt, o.bt);
		compareToBuilder.append(this.iteracion, o.iteracion);
		
		return compareToBuilder.toComparison();
	}
	
	
	}