package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.AsigCurvasTipoUOAKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class AsigCurvasTipoUOA implements EntidadBase<AsigCurvasTipoUOAKey> {
	
	public static final int IND_KUOA = 1;
	public static final int IND_KFCIERRECURV = 2;
	public static final int IND_KCARINVN17 = 3;
	public static final int IND_CCI0_KCURVA = 4;
	public static final int IND_SWACTIVO = 5;
	public static final int IND_USUARIOALTA = 6;
	public static final int IND_FECHAALTA = 7;
	public static final int IND_USUARIOMOD = 8;
	public static final int IND_FECHAMOD = 9;

	@PortableProperty(IND_KUOA)
	private String kUOA;
	@PortableProperty(IND_KFCIERRECURV)
	private Timestamp kFecCierreCurv;
	@PortableProperty(IND_KCARINVN17)
	private String kCarInv17;
	@PortableProperty(IND_CCI0_KCURVA)
	private String kCurva;
	@PortableProperty(IND_SWACTIVO)
	private String swActivo;
	@PortableProperty(IND_USUARIOALTA)
	private String usuarioAlta;
	@PortableProperty(IND_FECHAALTA)
	private Timestamp fechaAlta;
	@PortableProperty(IND_USUARIOMOD)
	private String usuarioMod;
	@PortableProperty(IND_FECHAMOD)
	private Timestamp fechaMod;

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

	public String getkCarInv17() {
		return kCarInv17;
	}

	public void setkCarInv17(String kCarInv17) {
		this.kCarInv17 = kCarInv17;
	}

	public String getkCurva() {
		return kCurva;
	}

	public void setkCurva(String kCurva) {
		this.kCurva = kCurva;
	}

	public String getSwActivo() {
		return swActivo;
	}

	public void setSwActivo(String swActivo) {
		this.swActivo = swActivo;
	}

	public String getUsuarioAlta() {
		return usuarioAlta;
	}

	public void setUsuarioAlta(String usuarioAlta) {
		this.usuarioAlta = usuarioAlta;
	}

	public Timestamp getFechaAlta() {
		return fechaAlta;
	}

	public void setFechaAlta(Timestamp fechaAlta) {
		this.fechaAlta = fechaAlta;
	}

	public String getUsuarioMod() {
		return usuarioMod;
	}

	public void setUsuarioMod(String usuarioMod) {
		this.usuarioMod = usuarioMod;
	}

	public Timestamp getFechaMod() {
		return fechaMod;
	}

	public void setFechaMod(Timestamp fechaMod) {
		this.fechaMod = fechaMod;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fechaAlta == null) ? 0 : fechaAlta.hashCode());
		result = prime * result + ((fechaMod == null) ? 0 : fechaMod.hashCode());
		result = prime * result + ((kCarInv17 == null) ? 0 : kCarInv17.hashCode());
		result = prime * result + ((kCurva == null) ? 0 : kCurva.hashCode());
		result = prime * result + ((kFecCierreCurv == null) ? 0 : kFecCierreCurv.hashCode());
		result = prime * result + ((kUOA == null) ? 0 : kUOA.hashCode());
		result = prime * result + ((swActivo == null) ? 0 : swActivo.hashCode());
		result = prime * result + ((usuarioAlta == null) ? 0 : usuarioAlta.hashCode());
		result = prime * result + ((usuarioMod == null) ? 0 : usuarioMod.hashCode());
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
		AsigCurvasTipoUOA other = (AsigCurvasTipoUOA) obj;
		if (fechaAlta == null) {
			if (other.fechaAlta != null)
				return false;
		} else if (!fechaAlta.equals(other.fechaAlta))
			return false;
		if (fechaMod == null) {
			if (other.fechaMod != null)
				return false;
		} else if (!fechaMod.equals(other.fechaMod))
			return false;
		if (kCarInv17 == null) {
			if (other.kCarInv17 != null)
				return false;
		} else if (!kCarInv17.equals(other.kCarInv17))
			return false;
		if (kCurva == null) {
			if (other.kCurva != null)
				return false;
		} else if (!kCurva.equals(other.kCurva))
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
		if (swActivo == null) {
			if (other.swActivo != null)
				return false;
		} else if (!swActivo.equals(other.swActivo))
			return false;
		if (usuarioAlta == null) {
			if (other.usuarioAlta != null)
				return false;
		} else if (!usuarioAlta.equals(other.usuarioAlta))
			return false;
		if (usuarioMod == null) {
			if (other.usuarioMod != null)
				return false;
		} else if (!usuarioMod.equals(other.usuarioMod))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AsigCurvasTipoUOA [kUOA=" + kUOA + ", kFecCierreCurv=" + kFecCierreCurv + ", kCarInv17=" + kCarInv17
				+ ", kCurva=" + kCurva + ", swActivo=" + swActivo + ", usuarioAlta=" + usuarioAlta + ", fechaAlta="
				+ fechaAlta + ", usuarioMod=" + usuarioMod + ", fechaMod=" + fechaMod + "]";
	}

	@Override
	public AsigCurvasTipoUOAKey getKey() {
		// TODO Auto-generated method stub
		return new AsigCurvasTipoUOAKey(kUOA, kFecCierreCurv, kCarInv17);
	}
	
}