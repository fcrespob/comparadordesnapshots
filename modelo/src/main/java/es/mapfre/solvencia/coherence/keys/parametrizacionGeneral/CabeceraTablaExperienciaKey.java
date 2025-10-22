package es.mapfre.solvencia.coherence.keys.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.CabeceraTablaExperiencia;

@Portable
public class CabeceraTablaExperienciaKey {
	
	@PortableProperty(CabeceraTablaExperiencia.IND_KTABLA)
	private Integer ktabla;
	@PortableProperty(CabeceraTablaExperiencia.IND_FECANULA)
	private Timestamp fecAnula;

	@PortableProperty(CabeceraTablaExperiencia.IND_FECALTA)
	private Timestamp fecAlta;

	public CabeceraTablaExperienciaKey(Integer ktabla, Timestamp fecAnula,
			Timestamp fecAlta) {
		super();
		this.ktabla = ktabla;
		this.fecAnula = fecAnula;
		this.fecAlta = fecAlta;
	}

	public CabeceraTablaExperienciaKey(){
		super();
	}
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fecAlta == null) ? 0 : fecAlta.hashCode());
		result = prime * result
				+ ((fecAnula == null) ? 0 : fecAnula.hashCode());
		result = prime * result + ((ktabla == null) ? 0 : ktabla.hashCode());
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
		CabeceraTablaExperienciaKey other = (CabeceraTablaExperienciaKey) obj;
		if (fecAlta == null) {
			if (other.fecAlta != null) {
				return false;
			}
		} else if (!fecAlta.equals(other.fecAlta)) {
			return false;
		}
		if (fecAnula == null) {
			if (other.fecAnula != null) {
				return false;
			}
		} else if (!fecAnula.equals(other.fecAnula)) {
			return false;
		}
		if (ktabla == null) {
			if (other.ktabla != null) {
				return false;
			}
		} else if (!ktabla.equals(other.ktabla)) {
			return false;
		}
		return true;
	}

	public Integer getKtabla() {
		return ktabla;
	}

	public void setKtabla(Integer ktabla) {
		this.ktabla = ktabla;
	}

	public Timestamp getFecAnula() {
		return fecAnula;
	}

	public void setFecAnula(Timestamp fecAnula) {
		this.fecAnula = fecAnula;
	}

	public Timestamp getFecAlta() {
		return fecAlta;
	}

	public void setFecAlta(Timestamp fecAlta) {
		this.fecAlta = fecAlta;
	}
	
	
	
}
