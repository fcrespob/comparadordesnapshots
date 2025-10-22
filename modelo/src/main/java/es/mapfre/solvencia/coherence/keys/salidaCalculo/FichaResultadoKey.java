package es.mapfre.solvencia.coherence.keys.salidaCalculo;


import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;

@Portable
public class FichaResultadoKey {

	@PortableProperty(FichaResultado.IND_KEJECUCION) private Integer kejecucion;
	@PortableProperty(FichaResultado.IND_KSISTEMA) private String ksistema;
	@PortableProperty(FichaResultado.IND_KPROTECNICO) private String kprotecnico;
	@PortableProperty(FichaResultado.IND_KUEJECUCION) private String kuejecucion;
	@PortableProperty(FichaResultado.IND_KSECUENCIA) private Integer ksecuencia;
	
	public FichaResultadoKey() {
		super();
	}
	
	public FichaResultadoKey(Integer kejecucion, String ksistema,
			String kprotecnico, String kuejecucion, Integer ksecuencia) {
		super();
		
		this.kejecucion = kejecucion;
		this.ksistema = ksistema;
		this.kprotecnico = kprotecnico;
		this.kuejecucion = kuejecucion;
		this.ksecuencia = ksecuencia;
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((kejecucion == null) ? 0 : kejecucion.hashCode());
		result = prime * result
				+ ((kprotecnico == null) ? 0 : kprotecnico.hashCode());
		result = prime * result
				+ ((ksecuencia == null) ? 0 : ksecuencia.hashCode());
		result = prime * result
				+ ((ksistema == null) ? 0 : ksistema.hashCode());
		result = prime * result
				+ ((kuejecucion == null) ? 0 : kuejecucion.hashCode());
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
		FichaResultadoKey other = (FichaResultadoKey) obj;
		if (kejecucion == null) {
			if (other.kejecucion != null) {
				return false;
			}
		} else if (!kejecucion.equals(other.kejecucion)) {
			return false;
		}
		if (kprotecnico == null) {
			if (other.kprotecnico != null) {
				return false;
			}
		} else if (!kprotecnico.equals(other.kprotecnico)) {
			return false;
		}
		if (ksecuencia == null) {
			if (other.ksecuencia != null) {
				return false;
			}
		} else if (!ksecuencia.equals(other.ksecuencia)) {
			return false;
		}
		if (ksistema == null) {
			if (other.ksistema != null) {
				return false;
			}
		} else if (!ksistema.equals(other.ksistema)) {
			return false;
		}
		if (kuejecucion == null) {
			if (other.kuejecucion != null) {
				return false;
			}
		} else if (!kuejecucion.equals(other.kuejecucion)) {
			return false;
		}
		return true;
	}

	public Integer getKejecucion() {
		return kejecucion;
	}

	public void setKejecucion(Integer kejecucion) {
		this.kejecucion = kejecucion;
	}

	public String getKsistema() {
		return ksistema;
	}

	public void setKsistema(String ksistema) {
		this.ksistema = ksistema;
	}

	public String getKprotecnico() {
		return kprotecnico;
	}

	public void setKprotecnico(String kprotecnico) {
		this.kprotecnico = kprotecnico;
	}

	public String getKuejecucion() {
		return kuejecucion;
	}

	public void setKuejecucion(String kuejecucion) {
		this.kuejecucion = kuejecucion;
	}

	public Integer getKsecuencia() {
		return ksecuencia;
	}

	public void setKsecuencia(Integer ksecuencia) {
		this.ksecuencia = ksecuencia;
	}
}