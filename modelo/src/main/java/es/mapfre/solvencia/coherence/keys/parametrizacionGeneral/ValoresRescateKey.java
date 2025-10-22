package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ValoresRescate;

@Portable
public class ValoresRescateKey {

	@PortableProperty(ValoresRescate.IND_KCARTERAINVERSION)
	private String kCarteraInv;

	public ValoresRescateKey(String kCarteraInv) {
		super();
		this.kCarteraInv = kCarteraInv;
	}

	public ValoresRescateKey() {
		super();
	}

	public String getkCarteraInv() {
		return kCarteraInv;
	}

	public void setkCarteraInv(String kCarteraInv) {
		this.kCarteraInv = kCarteraInv;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kCarteraInv == null) ? 0 : kCarteraInv.hashCode());
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
		ValoresRescateKey other = (ValoresRescateKey) obj;
		if (kCarteraInv == null) {
			if (other.kCarteraInv != null)
				return false;
		} else if (!kCarteraInv.equals(other.kCarteraInv))
			return false;
		return true;
	}

}