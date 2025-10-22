package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Primas implements EntidadBase<UmicKey> {

	public static final int IND_CFORMAREVPRIM = 0;
	public static final int IND_CFORMPAGO = 1;
	public static final int IND_FDIADEPAGO = 2;
	public static final int IND_IPRIMANETAACT = 3;
	public static final int IND_IPRIMANETAINI = 4;
	public static final int IND_IPRIMATARADA = 5;
	public static final int IND_PDTOASEG = 6;
	public static final int IND_PDTOEMP = 7;
	public static final int IND_PFPINV = 8;
	public static final int IND_PPC = 9;

	public static final int IND_PPCAP = 10;
	public static final int IND_PPR = 11;
	public static final int IND_PRECARGFRAC = 12;
	public static final int IND_PREVPRIMA = 13;

	
	private String cformarevprim;
	
	private String cformpago;
	
	private Integer fdiadepago;
	
	private java.math.BigDecimal iprimanetaact;
	
	private java.math.BigDecimal iprimanetaini;
	
	private java.math.BigDecimal iprimatarada;
	
	private java.math.BigDecimal pdtoaseg;
	
	private java.math.BigDecimal pdtoemp;
	
	private java.math.BigDecimal pfpinv;
	
	private java.math.BigDecimal ppc;

	
	private java.math.BigDecimal ppcap;
	
	private java.math.BigDecimal ppr;
	
	private java.math.BigDecimal precargfrac;
	
	private java.math.BigDecimal prevprima;

	public String getCformarevprim() {
		return cformarevprim;
	}

	public void setCformarevprim(String cformarevprim) {
		this.cformarevprim = cformarevprim;
	}

	public String getCformpago() {
		return cformpago;
	}

	public void setCformpago(String cformpago) {
		this.cformpago = cformpago;
	}

	public Integer getFdiadepago() {
		return fdiadepago;
	}

	public void setFdiadepago(Integer fdiadepago) {
		this.fdiadepago = fdiadepago;
	}

	public java.math.BigDecimal getIprimanetaact() {
		return iprimanetaact;
	}

	public void setIprimanetaact(java.math.BigDecimal iprimanetaact) {
		this.iprimanetaact = iprimanetaact;
	}

	public java.math.BigDecimal getIprimanetaini() {
		return iprimanetaini;
	}

	public void setIprimanetaini(java.math.BigDecimal iprimanetaini) {
		this.iprimanetaini = iprimanetaini;
	}

	public java.math.BigDecimal getIprimatarada() {
		return iprimatarada;
	}

	public void setIprimatarada(java.math.BigDecimal iprimatarada) {
		this.iprimatarada = iprimatarada;
	}

	public java.math.BigDecimal getPdtoaseg() {
		return pdtoaseg;
	}

	public void setPdtoaseg(java.math.BigDecimal pdtoaseg) {
		this.pdtoaseg = pdtoaseg;
	}

	public java.math.BigDecimal getPdtoemp() {
		return pdtoemp;
	}

	public void setPdtoemp(java.math.BigDecimal pdtoemp) {
		this.pdtoemp = pdtoemp;
	}

	public java.math.BigDecimal getPfpinv() {
		return pfpinv;
	}

	public void setPfpinv(java.math.BigDecimal pfpinv) {
		this.pfpinv = pfpinv;
	}

	public java.math.BigDecimal getPpc() {
		return ppc;
	}

	public void setPpc(java.math.BigDecimal ppc) {
		this.ppc = ppc;
	}

	public java.math.BigDecimal getPpcap() {
		return ppcap;
	}

	public void setPpcap(java.math.BigDecimal ppcap) {
		this.ppcap = ppcap;
	}

	public java.math.BigDecimal getPpr() {
		return ppr;
	}

	public void setPpr(java.math.BigDecimal ppr) {
		this.ppr = ppr;
	}

	public java.math.BigDecimal getPrecargfrac() {
		return precargfrac;
	}

	public void setPrecargfrac(java.math.BigDecimal precargfrac) {
		this.precargfrac = precargfrac;
	}

	public java.math.BigDecimal getPrevprima() {
		return prevprima;
	}

	public void setPrevprima(java.math.BigDecimal prevprima) {
		this.prevprima = prevprima;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
