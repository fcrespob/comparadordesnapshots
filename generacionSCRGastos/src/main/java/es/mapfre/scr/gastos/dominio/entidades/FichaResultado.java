package es.mapfre.scr.gastos.dominio.entidades;

public class FichaResultado{
	
	public static final int IND_KEJECUCION = 0;
	public static final int IND_KSISTEMA = 1;
	public static final int IND_KPROTECNICO = 2;
	public static final int IND_KUEJECUCION = 3;
	public static final int IND_KSECUENCIA = 4;
	public static final int IND_KTIPORES = 5;
	public static final int IND_KSECRES = 6;
	public static final int IND_GC1RESUL = 7;
	public static final int IND_GC2RESUL = 8;
	public static final int IND_GC3RESUL = 9;
	public static final int IND_GC4RESUL = 10;
	public static final int IND_GC5RESUL = 11;
	public static final int IND_GC6RESUL = 12;
	public static final int IND_GC7RESUL = 13;
	public static final int IND_GC8RESUL = 14;
	public static final int IND_GC9RESUL = 15;
	public static final int IND_GC10RESUL = 16;
	
	private Integer kejecucion;
	private String ksistema;
	private String kprotecnico;
	private String kuejecucion;
	private Integer ksecuencia;
	private String ktipores;
	private Integer ksecres;
	private String gc1Resul;
	private String gc2Resul;
	private String gc3Resul;
	private String gc4Resul;
	private String gc5Resul;
	private String gc6Resul;
	private String gc7Resul;
	private String gc8Resul;
	private String gc9Resul;
	private String gc10Resul;
	
	public FichaResultado(){
		super();
	}
	
	public FichaResultado(Integer kejecucion, String ksistema,
			String kprotecnico, String kuejecucion, Integer ksecuencia,
			String ktipores, Integer ksecres, String gc1Resul, String gc2Resul,
			String gc3Resul, String gc4Resul, String gc5Resul, String gc6Resul,
			String gc7Resul, String gc8Resul, String gc9Resul, String gc10Resul) {
		super();
		this.kejecucion = kejecucion;
		this.ksistema = ksistema;
		this.kprotecnico = kprotecnico;
		this.kuejecucion = kuejecucion;
		this.ksecuencia = ksecuencia;
		this.ktipores = ktipores;
		this.ksecres = ksecres;
		this.gc1Resul = gc1Resul;
		this.gc2Resul = gc2Resul;
		this.gc3Resul = gc3Resul;
		this.gc4Resul = gc4Resul;
		this.gc5Resul = gc5Resul;
		this.gc6Resul = gc6Resul;
		this.gc7Resul = gc7Resul;
		this.gc8Resul = gc8Resul;
		this.gc9Resul = gc9Resul;
		this.gc10Resul = gc10Resul;
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
	public String getKtipores() {
		return ktipores;
	}
	public void setKtipores(String ktipores) {
		this.ktipores = ktipores;
	}
	public Integer getKsecres() {
		return ksecres;
	}
	public void setKsecres(Integer ksecres) {
		this.ksecres = ksecres;
	}
	public String getGc1Resul() {
		return gc1Resul;
	}
	public void setGc1Resul(String gc1Resul) {
		this.gc1Resul = gc1Resul;
	}
	public String getGc2Resul() {
		return gc2Resul;
	}
	public void setGc2Resul(String gc2Resul) {
		this.gc2Resul = gc2Resul;
	}
	public String getGc3Resul() {
		return gc3Resul;
	}
	public void setGc3Resul(String gc3Resul) {
		this.gc3Resul = gc3Resul;
	}
	public String getGc4Resul() {
		return gc4Resul;
	}
	public void setGc4Resul(String gc4Resul) {
		this.gc4Resul = gc4Resul;
	}
	public String getGc5Resul() {
		return gc5Resul;
	}
	public void setGc5Resul(String gc5Resul) {
		this.gc5Resul = gc5Resul;
	}
	public String getGc6Resul() {
		return gc6Resul;
	}
	public void setGc6Resul(String gc6Resul) {
		this.gc6Resul = gc6Resul;
	}
	public String getGc7Resul() {
		return gc7Resul;
	}
	public void setGc7Resul(String gc7Resul) {
		this.gc7Resul = gc7Resul;
	}
	public String getGc8Resul() {
		return gc8Resul;
	}
	public void setGc8Resul(String gc8Resul) {
		this.gc8Resul = gc8Resul;
	}
	public String getGc9Resul() {
		return gc9Resul;
	}
	public void setGc9Resul(String gc9Resul) {
		this.gc9Resul = gc9Resul;
	}
	public String getGc10Resul() {
		return gc10Resul;
	}
	public void setGc10Resul(String gc10Resul) {
		this.gc10Resul = gc10Resul;
	}
	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gc10Resul == null) ? 0 : gc10Resul.hashCode());
		result = prime * result
				+ ((gc1Resul == null) ? 0 : gc1Resul.hashCode());
		result = prime * result
				+ ((gc2Resul == null) ? 0 : gc2Resul.hashCode());
		result = prime * result
				+ ((gc3Resul == null) ? 0 : gc3Resul.hashCode());
		result = prime * result
				+ ((gc4Resul == null) ? 0 : gc4Resul.hashCode());
		result = prime * result
				+ ((gc5Resul == null) ? 0 : gc5Resul.hashCode());
		result = prime * result
				+ ((gc6Resul == null) ? 0 : gc6Resul.hashCode());
		result = prime * result
				+ ((gc7Resul == null) ? 0 : gc7Resul.hashCode());
		result = prime * result
				+ ((gc8Resul == null) ? 0 : gc8Resul.hashCode());
		result = prime * result
				+ ((gc9Resul == null) ? 0 : gc9Resul.hashCode());
		result = prime * result
				+ ((kejecucion == null) ? 0 : kejecucion.hashCode());
		result = prime * result
				+ ((kprotecnico == null) ? 0 : kprotecnico.hashCode());
		result = prime * result + ((ksecres == null) ? 0 : ksecres.hashCode());
		result = prime * result
				+ ((ksecuencia == null) ? 0 : ksecuencia.hashCode());
		result = prime * result
				+ ((ksistema == null) ? 0 : ksistema.hashCode());
		result = prime * result
				+ ((ktipores == null) ? 0 : ktipores.hashCode());
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
		FichaResultado other = (FichaResultado) obj;
		if (gc10Resul == null) {
			if (other.gc10Resul != null) {
				return false;
			}
		} else if (!gc10Resul.equals(other.gc10Resul)) {
			return false;
		}
		if (gc1Resul == null) {
			if (other.gc1Resul != null) {
				return false;
			}
		} else if (!gc1Resul.equals(other.gc1Resul)) {
			return false;
		}
		if (gc2Resul == null) {
			if (other.gc2Resul != null) {
				return false;
			}
		} else if (!gc2Resul.equals(other.gc2Resul)) {
			return false;
		}
		if (gc3Resul == null) {
			if (other.gc3Resul != null) {
				return false;
			}
		} else if (!gc3Resul.equals(other.gc3Resul)) {
			return false;
		}
		if (gc4Resul == null) {
			if (other.gc4Resul != null) {
				return false;
			}
		} else if (!gc4Resul.equals(other.gc4Resul)) {
			return false;
		}
		if (gc5Resul == null) {
			if (other.gc5Resul != null) {
				return false;
			}
		} else if (!gc5Resul.equals(other.gc5Resul)) {
			return false;
		}
		if (gc6Resul == null) {
			if (other.gc6Resul != null) {
				return false;
			}
		} else if (!gc6Resul.equals(other.gc6Resul)) {
			return false;
		}
		if (gc7Resul == null) {
			if (other.gc7Resul != null) {
				return false;
			}
		} else if (!gc7Resul.equals(other.gc7Resul)) {
			return false;
		}
		if (gc8Resul == null) {
			if (other.gc8Resul != null) {
				return false;
			}
		} else if (!gc8Resul.equals(other.gc8Resul)) {
			return false;
		}
		if (gc9Resul == null) {
			if (other.gc9Resul != null) {
				return false;
			}
		} else if (!gc9Resul.equals(other.gc9Resul)) {
			return false;
		}
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
		if (ksecres == null) {
			if (other.ksecres != null) {
				return false;
			}
		} else if (!ksecres.equals(other.ksecres)) {
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
		if (ktipores == null) {
			if (other.ktipores != null) {
				return false;
			}
		} else if (!ktipores.equals(other.ktipores)) {
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
}