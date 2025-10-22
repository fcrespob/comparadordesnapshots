package es.mapfre.proxy.prestaciones.dominio.entidades;

public class FichaResultado{
	
	public static final int IND_KSISTEMA = 0;
	public static final int IND_KFECHA = 1;
	public static final int IND_KTIPOINFO = 2;
	public static final int IND_GC1RESUL = 3;
	public static final int IND_GC2RESUL = 4;
	public static final int IND_GC3RESUL = 5;
	public static final int IND_GC4RESUL = 6;
	public static final int IND_GC5RESUL = 7;
	public static final int IND_GC6RESUL = 8;
	public static final int IND_GC7RESUL = 9;
	public static final int IND_GC8RESUL = 10;
	public static final int IND_GC9RESUL = 11;
	public static final int IND_GC10RESUL = 12;
	
	private String ksistema;
	private String kfecha;
	private String ktipoinfo;
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
	
	public FichaResultado(String ksistema, String kfecha, String ktipoinfo, String gc1Resul, String gc2Resul,
			String gc3Resul, String gc4Resul, String gc5Resul, String gc6Resul,
			String gc7Resul, String gc8Resul, String gc9Resul, String gc10Resul) {
		super();
		this.ksistema = ksistema;
		this.kfecha = kfecha;
		this.ktipoinfo = ktipoinfo;
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
	public String getKsistema() {
		return ksistema;
	}
	public void setKsistema(String ksistema) {
		this.ksistema = ksistema;
	}
	public String getKfecha() {
		return kfecha;
	}
	public void setKfecha(String kfecha) {
		this.kfecha = kfecha;
	}
	public String getKtipoinfo() {
		return ktipoinfo;
	}
	public void setKtipoinfo(String ktipoinfo) {
		this.ktipoinfo = ktipoinfo;
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
				+ ((kfecha == null) ? 0 : kfecha.hashCode());
		result = prime * result
				+ ((ktipoinfo == null) ? 0 : ktipoinfo.hashCode());
		result = prime * result
				+ ((ksistema == null) ? 0 : ksistema.hashCode());
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
		if (ktipoinfo == null) {
			if (other.ktipoinfo != null) {
				return false;
			}
		} else if (!ktipoinfo.equals(other.ktipoinfo)) {
			return false;
		}
		if (kfecha == null) {
			if (other.kfecha != null) {
				return false;
			}
		} else if (!kfecha.equals(other.kfecha)) {
			return false;
		}
		if (ksistema == null) {
			if (other.ksistema != null) {
				return false;
			}
		} else if (!ksistema.equals(other.ksistema)) {
			return false;
		}
		return true;
	}
}