package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosCoaseguro implements EntidadBase<UmicKey> {
	
	public static final int IND_KCOASEORI = 0;
	public static final int IND_CTIPOCOASEG = 1;
	public static final int IND_PCOASEG = 2;
	public static final int IND_DISTINT = 3;
	public static final int IND_DISTEXT = 4;

	@PortableProperty(IND_KCOASEORI)
	private String kcoaseOri;
	
	@PortableProperty(IND_CTIPOCOASEG)
	private String ctipocoaseg;
	
	@PortableProperty(IND_PCOASEG)
	private java.math.BigDecimal pcoaseg;
	
	@PortableProperty(IND_DISTINT)
	private java.math.BigDecimal distint;
	
	@PortableProperty(IND_DISTEXT)
	private java.math.BigDecimal distext;
	
	public String getKcoaseOri() {
		return kcoaseOri;
	}
	public void setKcoaseOri(String kcoaseOri) {
		this.kcoaseOri = kcoaseOri;
	}
	public String getCtipocoaseg() {
		return ctipocoaseg;
	}
	public void setCtipocoaseg(String ctipocoaseg) {
		this.ctipocoaseg = ctipocoaseg;
	}
	public java.math.BigDecimal getPcoaseg() {
		return pcoaseg;
	}
	public void setPcoaseg(java.math.BigDecimal pcoaseg) {
		this.pcoaseg = pcoaseg;
	}
	public java.math.BigDecimal getDistint() {
		return distint;
	}
	public void setDistint(java.math.BigDecimal distint) {
		this.distint = distint;
	}
	public java.math.BigDecimal getDistext() {
		return distext;
	}
	public void setDistext(java.math.BigDecimal distext) {
		this.distext = distext;
	}
	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
