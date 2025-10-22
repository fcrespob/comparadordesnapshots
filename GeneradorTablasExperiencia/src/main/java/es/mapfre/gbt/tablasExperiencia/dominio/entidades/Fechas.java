package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import java.sql.Timestamp;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Fechas implements EntidadBase<UmicKey> {

	public static final int IND_CLAVEUMIC = 0;
	public static final int IND_FECAFINFINANCIA = 1;
	public static final int IND_FECDESDERENOVA = 2;
	public static final int IND_FECEFECFIN = 3;
	public static final int IND_FECEFECINI = 4;
	public static final int IND_FECEFECRED = 5;
	public static final int IND_FECHASTARENOVA = 6;
	public static final int IND_FECINIPAGPRIM = 7;
	public static final int IND_FECINISUS = 8;
	public static final int IND_PARTANO = 9;
	public static final int IND_TC = 10;

	
	private String claveUmic;
	
	private Timestamp fecafinfinancia;
	
	private Timestamp fecdesderenova;
	
	private Timestamp fecefecfin;
	
	private Timestamp fecefecini;
	
	private Timestamp fecefecred;
	
	private Timestamp fechastarenova;
	
	private Timestamp fecinipagprim;
	
	private Timestamp fecinisus;
	
	private java.math.BigDecimal partano;

	
	private Integer tc;

	public String getClaveUmic() {
		return claveUmic;
	}

	public void setClaveUmic(String claveUmic) {
		this.claveUmic = claveUmic;
	}

	public Timestamp getFecafinfinancia() {
		return fecafinfinancia;
	}

	public void setFecafinfinancia(Timestamp fecafinfinancia) {
		this.fecafinfinancia = fecafinfinancia;
	}

	public Timestamp getFecdesderenova() {
		return fecdesderenova;
	}

	public void setFecdesderenova(Timestamp fecdesderenova) {
		this.fecdesderenova = fecdesderenova;
	}

	public Timestamp getFecefecfin() {
		return fecefecfin;
	}

	public void setFecefecfin(Timestamp fecefecfin) {
		this.fecefecfin = fecefecfin;
	}

	public Timestamp getFecefecini() {
		return fecefecini;
	}

	public void setFecefecini(Timestamp fecefecini) {
		this.fecefecini = fecefecini;
	}

	public Timestamp getFecefecred() {
		return fecefecred;
	}

	public void setFecefecred(Timestamp fecefecred) {
		this.fecefecred = fecefecred;
	}

	public Timestamp getFechastarenova() {
		return fechastarenova;
	}

	public void setFechastarenova(Timestamp fechastarenova) {
		this.fechastarenova = fechastarenova;
	}

	public Timestamp getFecinipagprim() {
		return fecinipagprim;
	}

	public void setFecinipagprim(Timestamp fecinipagprim) {
		this.fecinipagprim = fecinipagprim;
	}

	public Timestamp getFecinisus() {
		return fecinisus;
	}

	public void setFecinisus(Timestamp fecinisus) {
		this.fecinisus = fecinisus;
	}

	public java.math.BigDecimal getPartano() {
		return partano;
	}

	public void setPartano(java.math.BigDecimal partano) {
		this.partano = partano;
	}

	public Integer getTc() {
		return tc;
	}

	public void setTc(Integer tc) {
		this.tc = tc;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
