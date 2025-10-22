package es.mapfre.solvencia.dominio.formulacion;

import java.io.Serializable;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class BaseTecnica implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@PortableProperty(0) private String btsimul;
	@PortableProperty(1) private String gtipobt;	
	@PortableProperty(2) private java.math.BigDecimal kliteral;
	@PortableProperty(3) private String ktipobt;
	@PortableProperty(4) private String scierre;
	@PortableProperty(5) private String sdetalletbt;
	
	public String getBtsimul() {
		return btsimul;
	}
	public void setBtsimul(String btsimul) {
		this.btsimul = btsimul;
	}
	public String getGtipobt() {
		return gtipobt;
	}
	public void setGtipobt(String gtipobt) {
		this.gtipobt = gtipobt;
	}
	public java.math.BigDecimal getKliteral() {
		return kliteral;
	}
	public void setKliteral(java.math.BigDecimal kliteral) {
		this.kliteral = kliteral;
	}
	public String getKtipobt() {
		return ktipobt;
	}
	public void setKtipobt(String ktipobt) {
		this.ktipobt = ktipobt;
	}
	public String getScierre() {
		return scierre;
	}
	public void setScierre(String scierre) {
		this.scierre = scierre;
	}
	public String getSdetalletbt() {
		return sdetalletbt;
	}
	public void setSdetalletbt(String sdetalletbt) {
		this.sdetalletbt = sdetalletbt;
	}
}
