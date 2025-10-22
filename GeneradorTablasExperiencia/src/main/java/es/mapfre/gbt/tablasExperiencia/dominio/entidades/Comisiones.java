package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Comisiones implements EntidadBase<UmicKey> {

	public static final int IND_BASECALCUCOMI1 = 0;
	public static final int IND_BASECALCUCOMI2 = 1;
	public static final int IND_BASECALCUCOMI3 = 2;
	public static final int IND_MESPAGOCOMI = 3;
	public static final int IND_NPERICOMI1 = 4;
	public static final int IND_PCOMISIONA1 = 5;
	public static final int IND_NPERICOMI2 = 6;
	public static final int IND_PCOMISIONA2 = 7;
	public static final int IND_NPERICOMI3 = 8;
	public static final int IND_PCOMISIONA3 = 9;

	
	private String basecalcucomi1;
	
	private String basecalcucomi2;
	
	private String basecalcucomi3;
	
	private String mespagocomi;
	
	private Integer npericomi1;
	
	private java.math.BigDecimal pcomisiona1;
	
	private Integer npericomi2;
	
	private java.math.BigDecimal pcomisiona2;
	
	private Integer npericomi3;
	
	private java.math.BigDecimal pcomisiona3;

	public String getBasecalcucomi1() {
		return basecalcucomi1;
	}

	public void setBasecalcucomi1(String basecalcucomi1) {
		this.basecalcucomi1 = basecalcucomi1;
	}

	public String getBasecalcucomi2() {
		return basecalcucomi2;
	}

	public void setBasecalcucomi2(String basecalcucomi2) {
		this.basecalcucomi2 = basecalcucomi2;
	}

	public String getBasecalcucomi3() {
		return basecalcucomi3;
	}

	public void setBasecalcucomi3(String basecalcucomi3) {
		this.basecalcucomi3 = basecalcucomi3;
	}

	public String getMespagocomi() {
		return mespagocomi;
	}

	public void setMespagocomi(String mespagocomi) {
		this.mespagocomi = mespagocomi;
	}

	public Integer getNpericomi1() {
		return npericomi1;
	}

	public void setNpericomi1(Integer npericomi1) {
		this.npericomi1 = npericomi1;
	}

	public java.math.BigDecimal getPcomisiona1() {
		return pcomisiona1;
	}

	public void setPcomisiona1(java.math.BigDecimal pcomisiona1) {
		this.pcomisiona1 = pcomisiona1;
	}

	public Integer getNpericomi2() {
		return npericomi2;
	}

	public void setNpericomi2(Integer npericomi2) {
		this.npericomi2 = npericomi2;
	}

	public java.math.BigDecimal getPcomisiona2() {
		return pcomisiona2;
	}

	public void setPcomisiona2(java.math.BigDecimal pcomisiona2) {
		this.pcomisiona2 = pcomisiona2;
	}

	public Integer getNpericomi3() {
		return npericomi3;
	}

	public void setNpericomi3(Integer npericomi3) {
		this.npericomi3 = npericomi3;
	}

	public java.math.BigDecimal getPcomisiona3() {
		return pcomisiona3;
	}

	public void setPcomisiona3(java.math.BigDecimal pcomisiona3) {
		this.pcomisiona3 = pcomisiona3;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
