package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Rescates implements EntidadBase<UmicKey> {

	public static final int IND_CODTIR = 0;
	public static final int IND_TIRINI = 1;
	public static final int IND_TIRCIE = 2;
	public static final int IND_KRESCATE1 = 3;
	public static final int IND_KRESCATE2 = 4;
	public static final int IND_KRESCATE3 = 5;
	public static final int IND_INDICRESCATE = 6;
	public static final int IND_RIESGRESCI = 7;

	
	private String codTir;
	
	private java.math.BigDecimal tirIni;
	
	private java.math.BigDecimal tirCie;
	
	private String krescate1;
	
	private String krescate2;
	
	private String krescate3;
	
	private String indicrescate;
	
	private String riesgrescI;

	public String getCodTir() {
		return codTir;
	}

	public void setCodTir(String codTir) {
		this.codTir = codTir;
	}

	public java.math.BigDecimal getTirIni() {
		return tirIni;
	}

	public void setTirIni(java.math.BigDecimal tirIni) {
		this.tirIni = tirIni;
	}

	public java.math.BigDecimal getTirCie() {
		return tirCie;
	}

	public void setTirCie(java.math.BigDecimal tirCie) {
		this.tirCie = tirCie;
	}

	public String getKrescate1() {
		return krescate1;
	}

	public void setKrescate1(String krescate1) {
		this.krescate1 = krescate1;
	}

	public String getKrescate2() {
		return krescate2;
	}

	public void setKrescate2(String krescate2) {
		this.krescate2 = krescate2;
	}

	public String getKrescate3() {
		return krescate3;
	}

	public void setKrescate3(String krescate3) {
		this.krescate3 = krescate3;
	}

	public String getIndicrescate() {
		return indicrescate;
	}

	public void setIndicrescate(String indicrescate) {
		this.indicrescate = indicrescate;
	}

	public String getRiesgrescI() {
		return riesgrescI;
	}

	public void setRiesgrescI(String riesgrescI) {
		this.riesgrescI = riesgrescI;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
