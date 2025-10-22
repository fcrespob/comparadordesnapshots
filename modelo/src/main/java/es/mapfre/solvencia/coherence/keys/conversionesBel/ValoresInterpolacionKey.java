package es.mapfre.solvencia.coherence.keys.conversionesBel;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.conversionesBel.ValoresCurvaTipo;

@Portable
@Deprecated //A eliminar en 10 dias a partir del 22/04/2014
public class ValoresInterpolacionKey{

	
	@PortableProperty(ValoresCurvaTipo.IND_CODCURVATIPOS) private Integer codCurvaTipos;
	@PortableProperty(ValoresCurvaTipo.IND_FECEFECCURVA) private Timestamp fecEfecCurva;
	
	public ValoresInterpolacionKey(Integer codCurvaTipos, Timestamp fecEfecCurva) {
		super();
		this.codCurvaTipos = codCurvaTipos;
		this.fecEfecCurva = fecEfecCurva;
	}

	public Integer getCodCurvaTipos() {
		return codCurvaTipos;
	}
	
	public void setCodCurvaTipos(Integer codCurvaTipos) {
		this.codCurvaTipos = codCurvaTipos;
	}
	
	public Timestamp getFecEfecCurva() {
		return fecEfecCurva;
	}
	
	public void setFecEfecCurva(Timestamp fecEfecCurva) {
		this.fecEfecCurva = fecEfecCurva;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((codCurvaTipos == null) ? 0 : codCurvaTipos.hashCode());
		result = prime * result
				+ ((fecEfecCurva == null) ? 0 : fecEfecCurva.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			{ return true; }
		if (obj == null)
			{ return false; }
		if (getClass() != obj.getClass())
			{ return false; }
		ValoresInterpolacionKey other = (ValoresInterpolacionKey) obj;
		if (codCurvaTipos == null) {
			if (other.codCurvaTipos != null)
				{ return false; }
		} else if (!codCurvaTipos.equals(other.codCurvaTipos))
			{ return false; }
		if (fecEfecCurva == null) {
			if (other.fecEfecCurva != null)
				{ return false; }
		} else if (!fecEfecCurva.equals(other.fecEfecCurva))
			{ return false; }
		{ return true; }
	}
	
	public ValoresInterpolacionKey(){
		super();
	}
	
}
