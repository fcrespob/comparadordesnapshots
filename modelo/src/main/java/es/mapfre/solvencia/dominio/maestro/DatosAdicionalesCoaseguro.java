package es.mapfre.solvencia.dominio.maestro;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class DatosAdicionalesCoaseguro implements
		EntidadBase<DatosAdicionalesCoaseguroKey> {

	public static final int IND_KCOASE1 = 0;
	public static final int IND_KCOASE2 = 1;
	public static final int IND_KCOASE3 = 2;
	public static final int IND_KCOASE4 = 3;
	public static final int IND_KCOASE5 = 4;
	public static final int IND_KCOASE6 = 5;
	public static final int IND_KCOASE7 = 6;
	public static final int IND_KCOASE8 = 7;
	public static final int IND_KCOASE9 = 8;
	public static final int IND_KCOASE10 = 9;

	public static final int IND_KCOASE11 = 10;
	public static final int IND_KCOASE12 = 11;
	public static final int IND_KCOASE13 = 12;
	public static final int IND_KCOASE14 = 13;
	public static final int IND_KCOASE15 = 14;
	public static final int IND_KCOASE16 = 15;
	public static final int IND_KCOASE17 = 16;
	public static final int IND_KCOASE18 = 17;
	public static final int IND_KCOASE19 = 18;
	public static final int IND_KCOASE20 = 19;
	public static final int IND_KCUADRO = 20;
	public static final int IND_KPOLIZA = 21;
	public static final int IND_KRAZON = 22;
	public static final int IND_KSUBPOL = 23;

	@PortableProperty(IND_KCOASE1)
	private String kcoase1;
	@PortableProperty(IND_KCOASE2)
	private String kcoase2;
	@PortableProperty(IND_KCOASE3)
	private String kcoase3;
	@PortableProperty(IND_KCOASE4)
	private String kcoase4;
	@PortableProperty(IND_KCOASE5)
	private String kcoase5;
	@PortableProperty(IND_KCOASE6)
	private String kcoase6;
	@PortableProperty(IND_KCOASE7)
	private String kcoase7;
	@PortableProperty(IND_KCOASE8)
	private String kcoase8;
	@PortableProperty(IND_KCOASE9)
	private String kcoase9;
	@PortableProperty(IND_KCOASE10)
	private String kcoase10;

	@PortableProperty(IND_KCOASE11)
	private String kcoase11;
	@PortableProperty(IND_KCOASE12)
	private String kcoase12;
	@PortableProperty(IND_KCOASE13)
	private String kcoase13;
	@PortableProperty(IND_KCOASE14)
	private String kcoase14;
	@PortableProperty(IND_KCOASE15)
	private String kcoase15;
	@PortableProperty(IND_KCOASE16)
	private String kcoase16;
	@PortableProperty(IND_KCOASE17)
	private String kcoase17;
	@PortableProperty(IND_KCOASE18)
	private String kcoase18;
	@PortableProperty(IND_KCOASE19)
	private String kcoase19;
	@PortableProperty(IND_KCOASE20)
	private String kcoase20;
	@PortableProperty(IND_KCUADRO)
	private Integer kcuadro;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KRAZON)
	private String krazon;
	@PortableProperty(IND_KSUBPOL)
	private Integer ksubpol;

	public String getKcoase1() {
		return kcoase1;
	}

	public void setKcoase1(String kcoase1) {
		this.kcoase1 = kcoase1;
	}

	public String getKcoase2() {
		return kcoase2;
	}

	public void setKcoase2(String kcoase2) {
		this.kcoase2 = kcoase2;
	}

	public String getKcoase3() {
		return kcoase3;
	}

	public void setKcoase3(String kcoase3) {
		this.kcoase3 = kcoase3;
	}

	public String getKcoase4() {
		return kcoase4;
	}

	public void setKcoase4(String kcoase4) {
		this.kcoase4 = kcoase4;
	}

	public String getKcoase5() {
		return kcoase5;
	}

	public void setKcoase5(String kcoase5) {
		this.kcoase5 = kcoase5;
	}

	public String getKcoase6() {
		return kcoase6;
	}

	public void setKcoase6(String kcoase6) {
		this.kcoase6 = kcoase6;
	}

	public String getKcoase7() {
		return kcoase7;
	}

	public void setKcoase7(String kcoase7) {
		this.kcoase7 = kcoase7;
	}

	public String getKcoase8() {
		return kcoase8;
	}

	public void setKcoase8(String kcoase8) {
		this.kcoase8 = kcoase8;
	}

	public String getKcoase9() {
		return kcoase9;
	}

	public void setKcoase9(String kcoase9) {
		this.kcoase9 = kcoase9;
	}

	public String getKcoase10() {
		return kcoase10;
	}

	public void setKcoase10(String kcoase10) {
		this.kcoase10 = kcoase10;
	}

	public String getKcoase11() {
		return kcoase11;
	}

	public void setKcoase11(String kcoase11) {
		this.kcoase11 = kcoase11;
	}

	public String getKcoase12() {
		return kcoase12;
	}

	public void setKcoase12(String kcoase12) {
		this.kcoase12 = kcoase12;
	}

	public String getKcoase13() {
		return kcoase13;
	}

	public void setKcoase13(String kcoase13) {
		this.kcoase13 = kcoase13;
	}

	public String getKcoase14() {
		return kcoase14;
	}

	public void setKcoase14(String kcoase14) {
		this.kcoase14 = kcoase14;
	}

	public String getKcoase15() {
		return kcoase15;
	}

	public void setKcoase15(String kcoase15) {
		this.kcoase15 = kcoase15;
	}

	public String getKcoase16() {
		return kcoase16;
	}

	public void setKcoase16(String kcoase16) {
		this.kcoase16 = kcoase16;
	}

	public String getKcoase17() {
		return kcoase17;
	}

	public void setKcoase17(String kcoase17) {
		this.kcoase17 = kcoase17;
	}

	public String getKcoase18() {
		return kcoase18;
	}

	public void setKcoase18(String kcoase18) {
		this.kcoase18 = kcoase18;
	}

	public String getKcoase19() {
		return kcoase19;
	}

	public void setKcoase19(String kcoase19) {
		this.kcoase19 = kcoase19;
	}

	public String getKcoase20() {
		return kcoase20;
	}

	public void setKcoase20(String kcoase20) {
		this.kcoase20 = kcoase20;
	}

	public Integer getKcuadro() {
		return kcuadro;
	}

	public void setKcuadro(Integer kcuadro) {
		this.kcuadro = kcuadro;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public String getKrazon() {
		return krazon;
	}

	public void setKrazon(String krazon) {
		this.krazon = krazon;
	}

	public Integer getKsubpol() {
		return ksubpol;
	}

	public void setKsubpol(Integer ksubpol) {
		this.ksubpol = ksubpol;
	}

	@Override
	public DatosAdicionalesCoaseguroKey getKey() {
		return new DatosAdicionalesCoaseguroKey(kpoliza, ksubpol);
	}

	@Override //NOSONAR
	public int hashCode() { //NOSONAR
		final int prime = 31;
		int result = 1;
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result
				+ ((kcoase10 == null) ? 0 : kcoase10.hashCode());
		result = prime * result
				+ ((kcoase11 == null) ? 0 : kcoase11.hashCode());
		result = prime * result
				+ ((kcoase12 == null) ? 0 : kcoase12.hashCode());
		result = prime * result
				+ ((kcoase13 == null) ? 0 : kcoase13.hashCode());
		result = prime * result
				+ ((kcoase14 == null) ? 0 : kcoase14.hashCode());
		result = prime * result
				+ ((kcoase15 == null) ? 0 : kcoase15.hashCode());
		result = prime * result
				+ ((kcoase16 == null) ? 0 : kcoase16.hashCode());
		result = prime * result
				+ ((kcoase17 == null) ? 0 : kcoase17.hashCode());
		result = prime * result
				+ ((kcoase18 == null) ? 0 : kcoase18.hashCode());
		result = prime * result
				+ ((kcoase19 == null) ? 0 : kcoase19.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result
				+ ((kcoase20 == null) ? 0 : kcoase20.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kcoase7 == null) ? 0 : kcoase7.hashCode());
		result = prime * result + ((kcoase8 == null) ? 0 : kcoase8.hashCode());
		result = prime * result + ((kcoase9 == null) ? 0 : kcoase9.hashCode());
		result = prime * result + ((kcuadro == null) ? 0 : kcuadro.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((krazon == null) ? 0 : krazon.hashCode());
		result = prime * result + ((ksubpol == null) ? 0 : ksubpol.hashCode());
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
		DatosAdicionalesCoaseguro other = (DatosAdicionalesCoaseguro) obj;
		if (kcoase1 == null) {
			if (other.kcoase1 != null) {
				return false;
			}
		} else if (!kcoase1.equals(other.kcoase1)) {
			return false;
		}
		if (kcoase10 == null) {
			if (other.kcoase10 != null) {
				return false;
			}
		} else if (!kcoase10.equals(other.kcoase10)) {
			return false;
		}
		if (kcoase11 == null) {
			if (other.kcoase11 != null) {
				return false;
			}
		} else if (!kcoase11.equals(other.kcoase11)) {
			return false;
		}
		if (kcoase12 == null) {
			if (other.kcoase12 != null) {
				return false;
			}
		} else if (!kcoase12.equals(other.kcoase12)) {
			return false;
		}
		if (kcoase13 == null) {
			if (other.kcoase13 != null) {
				return false;
			}
		} else if (!kcoase13.equals(other.kcoase13)) {
			return false;
		}
		if (kcoase14 == null) {
			if (other.kcoase14 != null) {
				return false;
			}
		} else if (!kcoase14.equals(other.kcoase14)) {
			return false;
		}
		if (kcoase15 == null) {
			if (other.kcoase15 != null) {
				return false;
			}
		} else if (!kcoase15.equals(other.kcoase15)) {
			return false;
		}
		if (kcoase16 == null) {
			if (other.kcoase16 != null) {
				return false;
			}
		} else if (!kcoase16.equals(other.kcoase16)) {
			return false;
		}
		if (kcoase17 == null) {
			if (other.kcoase17 != null) {
				return false;
			}
		} else if (!kcoase17.equals(other.kcoase17)) {
			return false;
		}
		if (kcoase18 == null) {
			if (other.kcoase18 != null) {
				return false;
			}
		} else if (!kcoase18.equals(other.kcoase18)) {
			return false;
		}
		if (kcoase19 == null) {
			if (other.kcoase19 != null) {
				return false;
			}
		} else if (!kcoase19.equals(other.kcoase19)) {
			return false;
		}
		if (kcoase2 == null) {
			if (other.kcoase2 != null) {
				return false;
			}
		} else if (!kcoase2.equals(other.kcoase2)) {
			return false;
		}
		if (kcoase20 == null) {
			if (other.kcoase20 != null) {
				return false;
			}
		} else if (!kcoase20.equals(other.kcoase20)) {
			return false;
		}
		if (kcoase3 == null) {
			if (other.kcoase3 != null) {
				return false;
			}
		} else if (!kcoase3.equals(other.kcoase3)) {
			return false;
		}
		if (kcoase4 == null) {
			if (other.kcoase4 != null) {
				return false;
			}
		} else if (!kcoase4.equals(other.kcoase4)) {
			return false;
		}
		if (kcoase5 == null) {
			if (other.kcoase5 != null) {
				return false;
			}
		} else if (!kcoase5.equals(other.kcoase5)) {
			return false;
		}
		if (kcoase6 == null) {
			if (other.kcoase6 != null) {
				return false;
			}
		} else if (!kcoase6.equals(other.kcoase6)) {
			return false;
		}
		if (kcoase7 == null) {
			if (other.kcoase7 != null) {
				return false;
			}
		} else if (!kcoase7.equals(other.kcoase7)) {
			return false;
		}
		if (kcoase8 == null) {
			if (other.kcoase8 != null) {
				return false;
			}
		} else if (!kcoase8.equals(other.kcoase8)) {
			return false;
		}
		if (kcoase9 == null) {
			if (other.kcoase9 != null) {
				return false;
			}
		} else if (!kcoase9.equals(other.kcoase9)) {
			return false;
		}
		if (kcuadro == null) {
			if (other.kcuadro != null) {
				return false;
			}
		} else if (!kcuadro.equals(other.kcuadro)) {
			return false;
		}
		if (kpoliza == null) {
			if (other.kpoliza != null) {
				return false;
			}
		} else if (!kpoliza.equals(other.kpoliza)) {
			return false;
		}
		if (krazon == null) {
			if (other.krazon != null) {
				return false;
			}
		} else if (!krazon.equals(other.krazon)) {
			return false;
		}
		if (ksubpol == null) {
			if (other.ksubpol != null) {
				return false;
			}
		} else if (!ksubpol.equals(other.ksubpol)) {
			return false;
		}
		return true;
	}	
}