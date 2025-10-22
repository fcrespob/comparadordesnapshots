package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.ErrorSolvencia2Key;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class ErrorSolvencia2 implements EntidadBase<ErrorSolvencia2Key> {

	public static final int IND_KAPLICACION = 0;
	public static final int IND_KIDPROGRAMA = 1;
	public static final int IND_KRETORNO = 2;
	public static final int IND_CNIVEL = 3;
	public static final int IND_GDESC = 4;
	public static final int IND_GDESCCORTA = 5;
	public static final int IND_KLITERAL = 6;

	@PortableProperty(IND_KAPLICACION)
	private String kaplicacion;
	@PortableProperty(IND_KIDPROGRAMA)
	private String kidprograma;
	@PortableProperty(IND_KRETORNO)
	private String kretorno;
	@PortableProperty(IND_CNIVEL)
	private String cnivel;
	@PortableProperty(IND_GDESC)
	private String gdesc;
	@PortableProperty(IND_GDESCCORTA)
	private String gdesccorta;
	@PortableProperty(IND_KLITERAL)
	private Integer kliteral;


	@Override
	public ErrorSolvencia2Key getKey() {
		return new ErrorSolvencia2Key(kaplicacion, kidprograma, kretorno);
	}


	public String getKaplicacion() {
		return kaplicacion;
	}


	public void setKaplicacion(String kaplicacion) {
		this.kaplicacion = kaplicacion;
	}


	public String getKidprograma() {
		return kidprograma;
	}


	public void setKidprograma(String kidprograma) {
		this.kidprograma = kidprograma;
	}


	public String getKretorno() {
		return kretorno;
	}


	public void setKretorno(String kretorno) {
		this.kretorno = kretorno;
	}


	public String getCnivel() {
		return cnivel;
	}


	public void setCnivel(String cnivel) {
		this.cnivel = cnivel;
	}


	public String getGdesc() {
		return gdesc;
	}


	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}


	public String getGdesccorta() {
		return gdesccorta;
	}


	public void setGdesccorta(String gdesccorta) {
		this.gdesccorta = gdesccorta;
	}


	public Integer getKliteral() {
		return kliteral;
	}


	public void setKliteral(Integer kliteral) {
		this.kliteral = kliteral;
	}


	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kliteral == null) ? 0 : kliteral.hashCode());
		result = prime * result + ((cnivel == null) ? 0 : cnivel.hashCode());
		result = prime * result + ((gdesc == null) ? 0 : gdesc.hashCode());
		result = prime * result
				+ ((gdesccorta == null) ? 0 : gdesccorta.hashCode());
		result = prime * result
				+ ((kaplicacion == null) ? 0 : kaplicacion.hashCode());
		result = prime * result
				+ ((kidprograma == null) ? 0 : kidprograma.hashCode());
		result = prime * result
				+ ((kretorno == null) ? 0 : kretorno.hashCode());
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
		ErrorSolvencia2 other = (ErrorSolvencia2) obj;
		if (kliteral == null) {
			if (other.kliteral != null) {
				return false;
			}
		} else if (!kliteral.equals(other.kliteral)) {
			return false;
		}
		if (cnivel == null) {
			if (other.cnivel != null) {
				return false;
			}
		} else if (!cnivel.equals(other.cnivel)) {
			return false;
		}
		if (gdesc == null) {
			if (other.gdesc != null) {
				return false;
			}
		} else if (!gdesc.equals(other.gdesc)) {
			return false;
		}
		if (gdesccorta == null) {
			if (other.gdesccorta != null) {
				return false;
			}
		} else if (!gdesccorta.equals(other.gdesccorta)) {
			return false;
		}
		if (kaplicacion == null) {
			if (other.kaplicacion != null) {
				return false;
			}
		} else if (!kaplicacion.equals(other.kaplicacion)) {
			return false;
		}
		if (kidprograma == null) {
			if (other.kidprograma != null) {
				return false;
			}
		} else if (!kidprograma.equals(other.kidprograma)) {
			return false;
		}
		if (kretorno == null) {
			if (other.kretorno != null) {
				return false;
			}
		} else if (!kretorno.equals(other.kretorno)) {
			return false;
		}
		return true;
	}
	
}
