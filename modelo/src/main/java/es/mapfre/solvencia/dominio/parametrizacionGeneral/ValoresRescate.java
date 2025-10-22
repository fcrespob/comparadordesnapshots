package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.ValoresRescateKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresRescate implements EntidadBase<ValoresRescateKey> {
	
	public static final int IND_KCARTERAINVERSION = 1;
	public static final int IND_VM = 2;
	public static final int IND_VCA = 3;
	public static final int IND_KDURACIONES = 4;

	@PortableProperty(IND_KCARTERAINVERSION)
	private String kcarterainv;
	@PortableProperty(IND_KDURACIONES)
	private java.math.BigDecimal kduraciones;
	@PortableProperty(IND_VM)
	private java.math.BigDecimal vm;
	@PortableProperty(IND_VCA)
	private java.math.BigDecimal vca;
	
	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public java.math.BigDecimal getKduraciones() {
		return kduraciones;
	}

	public void setkduraciones(java.math.BigDecimal kduraciones) {
		this.kduraciones = kduraciones;
	}

	public java.math.BigDecimal getVM() {
		return vm;
	}

	public void setVM(java.math.BigDecimal vm) {
		this.vm = vm;
	}

	public java.math.BigDecimal getVCA() {
		return vca;
	}

	public void setVCA(java.math.BigDecimal vca) {
		this.vca = vca;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kduraciones == null) ? 0 : kduraciones.hashCode());
		result = prime * result + ((vca == null) ? 0 : vca.hashCode());
		result = prime * result + ((vm == null) ? 0 : vm.hashCode());
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
		ValoresRescate other = (ValoresRescate) obj;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kduraciones == null) {
			if (other.kduraciones != null)
				return false;
		} else if (!kduraciones.equals(other.kduraciones))
			return false;
		if (vca == null) {
			if (other.vca != null)
				return false;
		} else if (!vca.equals(other.vca))
			return false;
		if (vm == null) {
			if (other.vm != null)
				return false;
		} else if (!vm.equals(other.vm))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ValoresRescate [kcarterainv=" + kcarterainv + ", kduraciones=" + kduraciones + ", vm=" + vm
				+ ", vca=" + vca + "]";
	}

	@Override
	public ValoresRescateKey getKey() {
		// TODO Auto-generated method stub
		return new ValoresRescateKey(kcarterainv);
	}
	
}