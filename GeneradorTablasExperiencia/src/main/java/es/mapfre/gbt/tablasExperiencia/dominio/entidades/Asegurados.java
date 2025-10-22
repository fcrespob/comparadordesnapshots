package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import java.sql.Timestamp;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Asegurados implements EntidadBase<UmicKey> {

	public static final int IND_PORSEXH = 0;
	public static final int IND_CSEXASEG1 = 1;
	public static final int IND_CSEXASEG2 = 2;
	public static final int IND_CSEXASEG3 = 3;
	public static final int IND_CSEXASEG4 = 4;
	public static final int IND_CSEXASEG5 = 5;
	public static final int IND_EDADASEG1 = 6;
	public static final int IND_EDADASEG2 = 7;
	public static final int IND_EDADASEG3 = 8;
	public static final int IND_EDADASEG4 = 9;

	public static final int IND_EDADASEG5 = 10;
	public static final int IND_FNACASEG1 = 11;
	public static final int IND_FNACASEG2 = 12;
	public static final int IND_FNACASEG3 = 13;
	public static final int IND_FNACASEG4 = 14;
	public static final int IND_FNACASEG5 = 15;
	public static final int IND_GEDADMAX = 16;
	public static final int IND_GEDADMIN = 17;

	
	private java.math.BigDecimal porsexh;
	
	private String csexAseg1;
	
	private String csexAseg2;
	
	private String csexAseg3;
	
	private String csexAseg4;
	
	private String csexAseg5;
	
	private Integer edadAseg1;
	
	private Integer edadAseg2;
	
	private Integer edadAseg3;
	
	private Integer edadAseg4;

	
	private Integer edadAseg5;
	
	private Timestamp fnacAseg1;
	
	private Timestamp fnacAseg2;
	
	private Timestamp fnacAseg3;
	
	private Timestamp fnacAseg4;
	
	private Timestamp fnacAseg5;
	
	private Integer gedadMax;
	
	private Integer gedadMin;

	public String getCsexAseg1() {
		return csexAseg1;
	}

	public void setCsexAseg1(String csexAseg1) {
		this.csexAseg1 = csexAseg1;
	}

	public String getCsexAseg2() {
		return csexAseg2;
	}

	public void setCsexAseg2(String csexAseg2) {
		this.csexAseg2 = csexAseg2;
	}

	public String getCsexAseg3() {
		return csexAseg3;
	}

	public void setCsexAseg3(String csexAseg3) {
		this.csexAseg3 = csexAseg3;
	}

	public String getCsexAseg4() {
		return csexAseg4;
	}

	public void setCsexAseg4(String csexAseg4) {
		this.csexAseg4 = csexAseg4;
	}

	public String getCsexAseg5() {
		return csexAseg5;
	}

	public void setCsexAseg5(String csexAseg5) {
		this.csexAseg5 = csexAseg5;
	}

	public Integer getEdadAseg1() {
		return edadAseg1;
	}

	public void setEdadAseg1(Integer edadAseg1) {
		this.edadAseg1 = edadAseg1;
	}

	public Integer getEdadAseg2() {
		return edadAseg2;
	}

	public void setEdadAseg2(Integer edadAseg2) {
		this.edadAseg2 = edadAseg2;
	}

	public Integer getEdadAseg3() {
		return edadAseg3;
	}

	public void setEdadAseg3(Integer edadAseg3) {
		this.edadAseg3 = edadAseg3;
	}

	public Integer getEdadAseg4() {
		return edadAseg4;
	}

	public void setEdadAseg4(Integer edadAseg4) {
		this.edadAseg4 = edadAseg4;
	}

	public Integer getEdadAseg5() {
		return edadAseg5;
	}

	public void setEdadAseg5(Integer edadAseg5) {
		this.edadAseg5 = edadAseg5;
	}

	public Timestamp getFnacAseg1() {
		return fnacAseg1;
	}

	public void setFnacAseg1(Timestamp fnacAseg1) {
		this.fnacAseg1 = fnacAseg1;
	}

	public Timestamp getFnacAseg2() {
		return fnacAseg2;
	}

	public void setFnacAseg2(Timestamp fnacAseg2) {
		this.fnacAseg2 = fnacAseg2;
	}

	public Timestamp getFnacAseg3() {
		return fnacAseg3;
	}

	public void setFnacAseg3(Timestamp fnacAseg3) {
		this.fnacAseg3 = fnacAseg3;
	}

	public Timestamp getFnacAseg4() {
		return fnacAseg4;
	}

	public void setFnacAseg4(Timestamp fnacAseg4) {
		this.fnacAseg4 = fnacAseg4;
	}

	public Timestamp getFnacAseg5() {
		return fnacAseg5;
	}

	public void setFnacAseg5(Timestamp fnacAseg5) {
		this.fnacAseg5 = fnacAseg5;
	}

	public Integer getGedadMax() {
		return gedadMax;
	}

	public void setGedadMax(Integer gedadMax) {
		this.gedadMax = gedadMax;
	}

	public Integer getGedadMin() {
		return gedadMin;
	}

	public void setGedadMin(Integer gedadMin) {
		this.gedadMin = gedadMin;
	}

	public java.math.BigDecimal getPorsexh() {
		return porsexh;
	}

	public void setPorsexh(java.math.BigDecimal porsexh) {
		this.porsexh = porsexh;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
