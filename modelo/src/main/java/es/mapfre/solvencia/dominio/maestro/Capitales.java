package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class Capitales implements EntidadBase<UmicKey> {

	public static final int IND_CFORMAREVCAP = 0;
	public static final int IND_CLAVEUMIC = 1;
	public static final int IND_CRMAX = 2;
	public static final int IND_ICAPACT = 3;
	public static final int IND_ICAPFALL = 4;
	public static final int IND_ICAPINI = 5;
	public static final int IND_ISALDO = 6;
	public static final int IND_PCAPRIESGO = 7;
	public static final int IND_POREVALCAP = 8;
	public static final int IND_PORGAMMA = 9;

	@PortableProperty(IND_CFORMAREVCAP)
	private String cformarevcap;
	@PortableProperty(IND_CLAVEUMIC)
	private String claveUmic;
	@PortableProperty(IND_CRMAX)
	private java.math.BigDecimal crmax;
	@PortableProperty(IND_ICAPACT)
	private java.math.BigDecimal icapact;
	@PortableProperty(IND_ICAPFALL)
	private java.math.BigDecimal icapfall;
	@PortableProperty(value=IND_ICAPINI, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal icapini;
	@PortableProperty(IND_ISALDO)
	private java.math.BigDecimal isaldo;
	@PortableProperty(IND_PCAPRIESGO)
	private java.math.BigDecimal pcapriesgo;
	@PortableProperty(IND_POREVALCAP)
	private java.math.BigDecimal porevalcap;
	@PortableProperty(IND_PORGAMMA)
	private java.math.BigDecimal porgamma;

	public String getCformarevcap() {
		return cformarevcap;
	}

	public void setCformarevcap(String cformarevcap) {
		this.cformarevcap = cformarevcap;
	}

	public String getClaveUmic() {
		return claveUmic;
	}

	public void setClaveUmic(String claveUmic) {
		this.claveUmic = claveUmic;
	}

	public java.math.BigDecimal getCrmax() {
		return crmax;
	}

	public void setCrmax(java.math.BigDecimal crmax) {
		this.crmax = crmax;
	}

	public java.math.BigDecimal getIcapact() {
		return icapact;
	}

	public void setIcapact(java.math.BigDecimal icapact) {
		this.icapact = icapact;
	}

	public java.math.BigDecimal getIcapfall() {
		return icapfall;
	}

	public void setIcapfall(java.math.BigDecimal icapfall) {
		this.icapfall = icapfall;
	}

	public java.math.BigDecimal getIcapini() {
		return icapini;
	}

	public void setIcapini(java.math.BigDecimal icapini) {
		this.icapini = icapini;
	}

	public java.math.BigDecimal getIsaldo() {
		return isaldo;
	}

	public void setIsaldo(java.math.BigDecimal isaldo) {
		this.isaldo = isaldo;
	}

	public java.math.BigDecimal getPcapriesgo() {
		return pcapriesgo;
	}

	public void setPcapriesgo(java.math.BigDecimal pcapriesgo) {
		this.pcapriesgo = pcapriesgo;
	}

	public java.math.BigDecimal getPorevalcap() {
		return porevalcap;
	}

	public void setPorevalcap(java.math.BigDecimal porevalcap) {
		this.porevalcap = porevalcap;
	}

	public java.math.BigDecimal getPorgamma() {
		return porgamma;
	}

	public void setPorgamma(java.math.BigDecimal porgamma) {
		this.porgamma = porgamma;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}
}
