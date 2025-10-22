package es.mapfre.solvencia.coherence.keys.entregables;

import org.apache.commons.lang3.builder.CompareToBuilder;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.entregables.SwCobroComCsv;

@Portable
public class SwCobroComCsvKey implements Comparable<SwCobroComCsvKey> {

	@PortableProperty(SwCobroComCsv.IND_KPOL)
	private Long kpoliza;
	@PortableProperty(SwCobroComCsv.IND_KSUBPOL)
	private Integer ksubpoliza;
	
	public SwCobroComCsvKey() {
		super();
	}

	public SwCobroComCsvKey(Long kpoliza, Integer ksubpoliza) {
		super();
		this.kpoliza = kpoliza;
		this.ksubpoliza = ksubpoliza;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}


	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	@Override
	public String toString() {
		return "SWCOBROCOMCSVKey [kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
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
		SwCobroComCsvKey other = (SwCobroComCsvKey) obj;
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
		return true;
	}

	@Override
	public int compareTo(SwCobroComCsvKey o) {
		CompareToBuilder compareToBuilder = new CompareToBuilder();
		compareToBuilder.append(this.kpoliza, o.kpoliza);
		compareToBuilder.append(this.ksubpoliza, o.ksubpoliza);
		return compareToBuilder.toComparison();
	}

}
