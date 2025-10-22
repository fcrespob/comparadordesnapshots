package es.mapfre.solvencia.coherence.keys.entregables;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.ConteoCertificado;
import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
@Portable
public class ConteoCertificadoKey implements Comparable<ConteoCertificadoKey> {

	@PortableProperty(ConteoCertificado.IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(ConteoCertificado.IND_BT)
	private String bt;


	public ConteoCertificadoKey(Integer kmodalidad,String bt) {
		this.kmodalidad = kmodalidad;
		this.bt=bt;
	}

	public ConteoCertificadoKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
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
		ConteoCertificadoKey other = (ConteoCertificadoKey) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ConteoCertificadoKey [kmodalidad=" + kmodalidad + ", bt=" + bt + "]";
	}

	@Override
	public int compareTo(ConteoCertificadoKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kmodalidad, o.kmodalidad);
		compareToBuilder.append(this.bt, o.bt);
		return compareToBuilder.toComparison();
	}
}