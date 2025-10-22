package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.AsigCurvasTipoUOA;

@Portable
public class AsigCurvasTipoUOAKey {

	@PortableProperty(AsigCurvasTipoUOA.IND_KUOA)
	private String kUOA;
	@PortableProperty(AsigCurvasTipoUOA.IND_KFCIERRECURV)
	private Timestamp kFecCierreCurv;
	@PortableProperty(AsigCurvasTipoUOA.IND_KCARINVN17)
	private String KCarInv17;

	public AsigCurvasTipoUOAKey(String kUOA, Timestamp kFecCierreCurv, String KCarInv17) {
		super();
		this.kUOA = kUOA;
		this.kFecCierreCurv = kFecCierreCurv;
		this.KCarInv17 = KCarInv17;
	}

	public AsigCurvasTipoUOAKey() {
		super();
	}

	public String getkUOA() {
		return kUOA;
	}

	public void setkUOA(String kUOA) {
		this.kUOA = kUOA;
	}

	public Timestamp getkFecCierreCurv() {
		return kFecCierreCurv;
	}

	public void setkFecCierreCurv(Timestamp kFecCierreCurv) {
		this.kFecCierreCurv = kFecCierreCurv;
	}

	public String getKCarInv17() {
		return KCarInv17;
	}

	public void setKCarInv17(String kCarInv17) {
		KCarInv17 = kCarInv17;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((KCarInv17 == null) ? 0 : KCarInv17.hashCode());
		result = prime * result + ((kFecCierreCurv == null) ? 0 : kFecCierreCurv.hashCode());
		result = prime * result + ((kUOA == null) ? 0 : kUOA.hashCode());
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
		AsigCurvasTipoUOAKey other = (AsigCurvasTipoUOAKey) obj;
		if (KCarInv17 == null) {
			if (other.KCarInv17 != null)
				return false;
		} else if (!KCarInv17.equals(other.KCarInv17))
			return false;
		if (kFecCierreCurv == null) {
			if (other.kFecCierreCurv != null)
				return false;
		} else if (!kFecCierreCurv.equals(other.kFecCierreCurv))
			return false;
		if (kUOA == null) {
			if (other.kUOA != null)
				return false;
		} else if (!kUOA.equals(other.kUOA))
			return false;
		return true;
	}

}