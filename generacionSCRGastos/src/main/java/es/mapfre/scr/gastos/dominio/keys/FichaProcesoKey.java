package es.mapfre.scr.gastos.dominio.keys;


public class FichaProcesoKey{

	private Integer kejecucion;
	private String ksistema;
	private String kprotecnico;
	private String kuejecucion;
	private Integer ksecuencia;

	public FichaProcesoKey(Integer kejecucion, String ksistema,
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
		if (this == obj)
		{{ return true; }}
		if (obj == null)
		{{ return false; }}
		if (getClass() != obj.getClass())
		{{ return false; }}
		FichaProcesoKey other = (FichaProcesoKey) obj;
		if (kejecucion == null) {
			if (other.kejecucion != null)
			{{ return false; }}
		} else if (!kejecucion.equals(other.kejecucion))
		{{ return false; }}
		if (kprotecnico == null) {
			if (other.kprotecnico != null)
			{{ return false; }}
		} else if (!kprotecnico.equals(other.kprotecnico))
		{{ return false; }}
		if (ksecuencia == null) {
			if (other.ksecuencia != null)
			{{ return false; }}
		} else if (!ksecuencia.equals(other.ksecuencia))
		{{ return false; }}
		if (ksistema == null) {
			if (other.ksistema != null)
			{{ return false; }}
		} else if (!ksistema.equals(other.ksistema))
		{{ return false; }}
		if (kuejecucion == null) {
			if (other.kuejecucion != null)
			{{ return false; }}
		} else if (!kuejecucion.equals(other.kuejecucion))
		{{ return false; }}
		{ return true; }
	}

	public FichaProcesoKey(){
		super();
	}
}

