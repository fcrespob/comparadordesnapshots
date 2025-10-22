package es.mapfre.gbt.tablasExperiencia.dominio.entidades;


import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class OtrosDatos implements EntidadBase<UmicKey> {

	public static final int IND_GESTIONIT = 0;
	public static final int IND_SIGPINTERTEC1 = 1;
	public static final int IND_SIGPINTERTEC2 = 2;
	public static final int IND_SIGPINTERTEC3 = 3;
	public static final int IND_SIGPINTERTEC4 = 4;
	public static final int IND_SIGPINTERTEC5 = 5;
	public static final int IND_INDINVAL = 6;
	public static final int IND_PREGRUPO = 7;
	
	public static final int IND_CESTADOASEG1 = 8;
	public static final int IND_CESTADOASEG2 = 9;
	public static final int IND_CESTADOASEG3 = 10;
	public static final int IND_CESTADOASEG4 = 11;
	public static final int IND_CESTADOASEG5 = 12;
	
	private String gestionit;
	private String sigpitertecn1;
	private String sigpitertecn2;
	private String sigpitertecn3;
	private String sigpitertecn4;
	private String sigpitertecn5;
	private String indinval;
	private String pregrupo;
	
	private String cestadoAseg1;
	private String cestadoAseg2;
	private String cestadoAseg3;
	private String cestadoAseg4;
	private String cestadoAseg5;
	
	public String getGestionit() {
		return gestionit;
	}
	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}
	public String getSigpitertecn1() {
		return sigpitertecn1;
	}
	public void setSigpitertecn1(String sigpitertecn1) {
		this.sigpitertecn1 = sigpitertecn1;
	}
	public String getSigpitertecn2() {
		return sigpitertecn2;
	}
	public void setSigpitertecn2(String sigpitertecn2) {
		this.sigpitertecn2 = sigpitertecn2;
	}
	public String getSigpitertecn3() {
		return sigpitertecn3;
	}
	public void setSigpitertecn3(String sigpitertecn3) {
		this.sigpitertecn3 = sigpitertecn3;
	}
	public String getSigpitertecn4() {
		return sigpitertecn4;
	}
	public void setSigpitertecn4(String sigpitertecn4) {
		this.sigpitertecn4 = sigpitertecn4;
	}
	public String getSigpitertecn5() {
		return sigpitertecn5;
	}
	public void setSigpitertecn5(String sigpitertecn5) {
		this.sigpitertecn5 = sigpitertecn5;
	}
	public String getIndinval() {
		return indinval;
	}
	public void setIndinval(String indinval) {
		this.indinval = indinval;
	}
	public String getPregrupo() {
		return pregrupo;
	}
	public void setPregrupo(String pregrupo) {
		this.pregrupo = pregrupo;
	}
	
	public String getCestadoAseg1() {
		return cestadoAseg1;
	}

	public void setCestadoAseg1(String cestadoAseg1) {
		this.cestadoAseg1 = cestadoAseg1;
	}

	public String getCestadoAseg2() {
		return cestadoAseg2;
	}

	public void setCestadoAseg2(String cestadoAseg2) {
		this.cestadoAseg2 = cestadoAseg2;
	}

	public String getCestadoAseg3() {
		return cestadoAseg3;
	}

	public void setCestadoAseg3(String cestadoAseg3) {
		this.cestadoAseg3 = cestadoAseg3;
	}

	public String getCestadoAseg4() {
		return cestadoAseg4;
	}

	public void setCestadoAseg4(String cestadoAseg4) {
		this.cestadoAseg4 = cestadoAseg4;
	}

	public String getCestadoAseg5() {
		return cestadoAseg5;
	}

	public void setCestadoAseg5(String cestadoAseg5) {
		this.cestadoAseg5 = cestadoAseg5;
	}
	@Override
	public UmicKey getKey() {
		
		return null;
	}
	
	
	
}
