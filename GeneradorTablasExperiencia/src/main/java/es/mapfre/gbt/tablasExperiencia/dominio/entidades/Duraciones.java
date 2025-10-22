package es.mapfre.gbt.tablasExperiencia.dominio.entidades;

import es.mapfre.gbt.tablasExperiencia.dominio.EntidadBase;
import es.mapfre.gbt.tablasExperiencia.dominio.keys.UmicKey;

public class Duraciones implements EntidadBase<UmicKey> {

	public static final int IND_NRENOVACIONES = 0;
	public static final int IND_NDURSEGANO = 1;
	public static final int IND_NDURSEGMES = 2;
	public static final int IND_NDURSEGDIA = 3;
	public static final int IND_NDURPRIMA = 4;

	
	private Integer nrenovaciones;
	
	private Integer ndursegano;
	
	private Integer ndursegmes;
	
	private Integer ndursegdia;
	
	private Integer ndurprima;

	public Integer getNrenovaciones() {
		return nrenovaciones;
	}

	public void setNrenovaciones(Integer nrenovaciones) {
		this.nrenovaciones = nrenovaciones;
	}

	public Integer getNdursegano() {
		return ndursegano;
	}

	public void setNdursegano(Integer ndursegano) {
		this.ndursegano = ndursegano;
	}

	public Integer getNdursegmes() {
		return ndursegmes;
	}

	public void setNdursegmes(Integer ndursegmes) {
		this.ndursegmes = ndursegmes;
	}

	public Integer getNdursegdia() {
		return ndursegdia;
	}

	public void setNdursegdia(Integer ndursegdia) {
		this.ndursegdia = ndursegdia;
	}

	public Integer getNdurprima() {
		return ndurprima;
	}

	public void setNdurprima(Integer ndurprima) {
		this.ndurprima = ndurprima;
	}

	@Override
	public UmicKey getKey() {
		// No existe la posibilidad de crear una UmicKey a partir de los datos
		return null;
	}

}
