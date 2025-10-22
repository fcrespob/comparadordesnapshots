package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;

@Portable
public class ValoresCurvaTipoKey{
	
	@PortableProperty(ValoresCurvaTipo.IND_CODCURVATIPOS) private String codCurvaTipos;
	@PortableProperty(ValoresCurvaTipo.IND_FECEFECCURVA) private Timestamp fecEfecCurva;
	@PortableProperty(ValoresCurvaTipo.IND_DIASPLAZO) private Integer diasPlazo;

	public String getCodCurvaTipos() {
		return codCurvaTipos;
	}
	
	public void setCodCurvaTipos(String codCurvaTipos) {
		this.codCurvaTipos = codCurvaTipos;
	}
	
	public Timestamp getFecEfecCurva() {
		return fecEfecCurva;
	}
	
	public void setFecEfecCurva(Timestamp fecEfecCurva) {
		this.fecEfecCurva = fecEfecCurva;
	}


	public Integer getDiasPlazo() {
		return diasPlazo;
	}


	public void setDiasPlazo(Integer diasPlazo) {
		this.diasPlazo = diasPlazo;
	}


	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codCurvaTipos == null) ? 0 : codCurvaTipos.hashCode());
		result = prime * result
				+ ((diasPlazo == null) ? 0 : diasPlazo.hashCode());
		result = prime * result
				+ ((fecEfecCurva == null) ? 0 : fecEfecCurva.hashCode());
		return result;
	}


	@Override //NOSONAR
	public boolean equals(Object obj) { //NOSONAR
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ValoresCurvaTipoKey other = (ValoresCurvaTipoKey) obj;
		if (codCurvaTipos == null) {
			if (other.codCurvaTipos != null) {
				return false;
			}
		} else if (!codCurvaTipos.equals(other.codCurvaTipos)) {
			return false;
		}
		if (diasPlazo == null) {
			if (other.diasPlazo != null) {
				return false;
			}
		} else if (!diasPlazo.equals(other.diasPlazo)) {
			return false;
		}
		if (fecEfecCurva == null) {
			if (other.fecEfecCurva != null) {
				return false;
			}
		} else if (!fecEfecCurva.equals(other.fecEfecCurva)) {
			return false;
		}
		return true;
	}


	public ValoresCurvaTipoKey(String codCurvaTipos, Timestamp fecEfecCurva,
			Integer diasPlazo) {
		super();
		this.codCurvaTipos = codCurvaTipos;
		this.fecEfecCurva = fecEfecCurva;
		this.diasPlazo = diasPlazo;
	}


	public ValoresCurvaTipoKey() {
		super();
	}


	@Override
	public String toString() {
		return "ValoresCurvaTipoKey [codCurvaTipos=" + codCurvaTipos
				+ ", fecEfecCurva=" + fecEfecCurva + ", diasPlazo=" + diasPlazo
				+ "]";
	}
	
	

}