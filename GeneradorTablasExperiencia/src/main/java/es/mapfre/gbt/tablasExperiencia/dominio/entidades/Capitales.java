package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

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

	
	private String cformarevcap;
	
	private String claveUmic;
	
	private java.math.BigDecimal crmax;
	
	private java.math.BigDecimal icapact;
	
	private java.math.BigDecimal icapfall;
	
	private java.math.BigDecimal icapini;
	
	private java.math.BigDecimal isaldo;
	
	private java.math.BigDecimal pcapriesgo;
	
	private java.math.BigDecimal porevalcap;
	
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
