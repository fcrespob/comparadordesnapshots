package es.mapfre.solvencia.coherence.keys.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

@Portable
public class ErrorSolvencia2Key {

	public static final int IND_KAPLICACION = 0;
	public static final int IND_KIDPROGRAMA = 1;
	public static final int IND_KRETORNO = 2;

	@PortableProperty(IND_KAPLICACION)
	private String kaplicacion;
	@PortableProperty(IND_KIDPROGRAMA)
	private String kidprograma;
	@PortableProperty(IND_KRETORNO)
	private String kretorno;
	
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
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
		ErrorSolvencia2Key other = (ErrorSolvencia2Key) obj;
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
	
	public ErrorSolvencia2Key() {
		super();
	}
	
	public ErrorSolvencia2Key(String kaplicacion, String kidprograma,
			String kretorno) {
		super();
		this.kaplicacion = kaplicacion;
		this.kidprograma = kidprograma;
		this.kretorno = kretorno;
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
	
	
}
