package es.mapfre.solvencia.dominio.conversionesBel;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.conversionesBel.ValoresCurvaTipoKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ValoresCurvaTipo implements EntidadBase<ValoresCurvaTipoKey> {
	
	public static final int IND_CODCURVATIPOS = 0;
	public static final int IND_FECEFECCURVA = 1;
	public static final int IND_DIASPLAZO = 2;
	public static final int IND_PORCENTAJEINTERES = 3;
	
	@PortableProperty(IND_CODCURVATIPOS) private String codCurvaTipos;
	@PortableProperty(IND_FECEFECCURVA) private Timestamp fecEfecCurva;
	@PortableProperty(IND_DIASPLAZO) private Integer diasPlazo;
	@PortableProperty(IND_PORCENTAJEINTERES) private java.math.BigDecimal porcentajeInteres;
	
	public ValoresCurvaTipo() {
		super();
	}

	public String getCodCurvaTipos() {
		return codCurvaTipos;
	}

	public void setCodCurvaTipos(String codCurvaTipos) {
		this.codCurvaTipos = codCurvaTipos;
	}

	public Integer getDiasPlazo() {
		return diasPlazo;
	}

	public void setDiasPlazo(Integer diasPlazo) {
		this.diasPlazo = diasPlazo;
	}

	public Timestamp getFecEfecCurva() {
		return fecEfecCurva;
	}

	public void setFecEfecCurva(Timestamp fecEfecCurva) {
		this.fecEfecCurva = fecEfecCurva;
	}

	public java.math.BigDecimal getPorcentajeInteres() {
		return porcentajeInteres;
	}

	public void setPorcentajeInteres(java.math.BigDecimal porcentajeInteres) {
		this.porcentajeInteres = porcentajeInteres;
	}

	@Override
	public ValoresCurvaTipoKey getKey() {
		return new ValoresCurvaTipoKey(codCurvaTipos, fecEfecCurva, diasPlazo);
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
		result = prime
				* result
				+ ((porcentajeInteres == null) ? 0 : porcentajeInteres
						.hashCode());
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
		ValoresCurvaTipo other = (ValoresCurvaTipo) obj;
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
		if (porcentajeInteres == null) {
			if (other.porcentajeInteres != null) {
				return false;
			}
		} else if (!porcentajeInteres.equals(other.porcentajeInteres)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "ValoresCurvaTipo [codCurvaTipos=" + codCurvaTipos
				+ ", fecEfecCurva=" + fecEfecCurva + ", diasPlazo=" + diasPlazo
				+ ", porcentajeInteres=" + porcentajeInteres + "]";
	}
	
	
}