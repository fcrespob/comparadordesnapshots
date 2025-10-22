package es.mapfre.solvencia.dominio.parametrizacionGeneral;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.parametrizacionGeneral.Tab923Key;
import es.mapfre.solvencia.dominio.EntidadBase;

@Portable
public class Tab923 implements EntidadBase<Tab923Key> {
	
	public static final int IND_MODALIDAD = 1;
	public static final int IND_FINIP = 2;
	public static final int IND_FFINP = 3;
	public static final int IND_l1 = 4;
	public static final int IND_PRP1 = 5;
	public static final int IND_l2 = 6;
	public static final int IND_PRP2 = 7;
	public static final int IND_l3 = 8;
	public static final int IND_PRP3 = 9;
	public static final int IND_l4 = 10;
	public static final int IND_PRP4 = 11;
	public static final int IND_l5 = 12;
	public static final int IND_PRP5 = 13;
	public static final int IND_l6 = 14;
	public static final int IND_PRP6 = 15;
	public static final int IND_l7 = 16;
	public static final int IND_PRP7 = 17;
	public static final int IND_l8 = 18;
	public static final int IND_PRP8 = 19;
	public static final int IND_l9 = 20;
	public static final int IND_PRP9 = 21;
	
	@PortableProperty(IND_MODALIDAD)
	private Integer modalidad;
	@PortableProperty(IND_FINIP)
	private Timestamp finip;
	@PortableProperty(IND_FFINP)
	private Timestamp ffinp;
	@PortableProperty(IND_l1)
	private String l1;
	@PortableProperty(IND_PRP1)
	private String prp1;
	@PortableProperty(IND_l2)
	private String l2;
	@PortableProperty(IND_PRP2)
	private String prp2;
	@PortableProperty(IND_l3)
	private String l3;
	@PortableProperty(IND_PRP3)
	private String prp3;
	@PortableProperty(IND_l4)
	private String l4;
	@PortableProperty(IND_PRP4)
	private String prp4;
	@PortableProperty(IND_l5)
	private String l5;
	@PortableProperty(IND_PRP5)
	private String prp5;
	@PortableProperty(IND_l6)
	private String l6;
	@PortableProperty(IND_PRP6)
	private String prp6;
	@PortableProperty(IND_l7)
	private String l7;
	@PortableProperty(IND_PRP7)
	private String prp7;
	@PortableProperty(IND_l8)
	private String l8;
	@PortableProperty(IND_PRP8)
	private String prp8;
	@PortableProperty(IND_l9)
	private String l9;
	@PortableProperty(IND_PRP9)
	private String prp9;
	

	public Integer getModalidad() {
		return modalidad;
	}

	public void setModalidad(Integer modalidad) {
		this.modalidad = modalidad;
	}

	public Timestamp getFinip() {
		return finip;
	}

	public void setFinip(Timestamp finip) {
		this.finip = finip;
	}

	public Timestamp getFfinp() {
		return ffinp;
	}

	public void setFfinp(Timestamp ffinp) {
		this.ffinp = ffinp;
	}

	public String getPRP1() {
		return prp1;
	}

	public void setPRP1(String PRP1) {
		this.prp1 = PRP1;
	}

	public String getl1() {
		return l1;
	}

	public void setl1(String L1) {
		this.l1 = L1;
	}
	
	

	public String getl2() {
		return l2;
	}

	public void setl2(String l2) {
		this.l2 = l2;
	}

	public String getPRP2() {
		return prp2;
	}

	public void setPRP2(String prp2) {
		this.prp2 = prp2;
	}

	public String getl3() {
		return l3;
	}

	public void setl3(String l3) {
		this.l3 = l3;
	}

	public String getPRP3() {
		return prp3;
	}

	public void setPRP3(String prp3) {
		this.prp3 = prp3;
	}

	public String getl4() {
		return l4;
	}

	public void setl4(String l4) {
		this.l4 = l4;
	}

	public String getPRP4() {
		return prp4;
	}

	public void setPRP4(String prp4) {
		this.prp4 = prp4;
	}

	public String getl5() {
		return l5;
	}

	public void setl5(String l5) {
		this.l5 = l5;
	}

	public String getPRP5() {
		return prp5;
	}

	public void setPRP5(String prp5) {
		this.prp5 = prp5;
	}

	public String getl6() {
		return l6;
	}

	public void setl6(String l6) {
		this.l6 = l6;
	}

	public String getPRP6() {
		return prp6;
	}

	public void setPRP6(String prp6) {
		this.prp6 = prp6;
	}

	public String getl7() {
		return l7;
	}

	public void setL7(String l7) {
		this.l7 = l7;
	}

	public String getPRP7() {
		return prp7;
	}

	public void setPRP7(String prp7) {
		this.prp7 = prp7;
	}

	public String getl8() {
		return l8;
	}

	public void setl8(String l8) {
		this.l8 = l8;
	}

	public String getPRP8() {
		return prp8;
	}

	public void setPRP8(String prp8) {
		this.prp8 = prp8;
	}

	public String getl9() {
		return l9;
	}

	public void setl9(String l9) {
		this.l9 = l9;
	}

	public String getPRP9() {
		return prp9;
	}

	public void setPRP9(String prp9) {
		this.prp9 = prp9;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ffinp == null) ? 0 : ffinp.hashCode());
		result = prime * result + ((finip == null) ? 0 : finip.hashCode());
		result = prime * result + ((l1 == null) ? 0 : l1.hashCode());
		result = prime * result + ((l2 == null) ? 0 : l2.hashCode());
		result = prime * result + ((l3 == null) ? 0 : l3.hashCode());
		result = prime * result + ((l4 == null) ? 0 : l4.hashCode());
		result = prime * result + ((l5 == null) ? 0 : l5.hashCode());
		result = prime * result + ((l6 == null) ? 0 : l6.hashCode());
		result = prime * result + ((l7 == null) ? 0 : l7.hashCode());
		result = prime * result + ((l8 == null) ? 0 : l8.hashCode());
		result = prime * result + ((l9 == null) ? 0 : l9.hashCode());
		result = prime * result + ((modalidad == null) ? 0 : modalidad.hashCode());
		result = prime * result + ((prp1 == null) ? 0 : prp1.hashCode());
		result = prime * result + ((prp2 == null) ? 0 : prp2.hashCode());
		result = prime * result + ((prp3 == null) ? 0 : prp3.hashCode());
		result = prime * result + ((prp4 == null) ? 0 : prp4.hashCode());
		result = prime * result + ((prp5 == null) ? 0 : prp5.hashCode());
		result = prime * result + ((prp6 == null) ? 0 : prp6.hashCode());
		result = prime * result + ((prp7 == null) ? 0 : prp7.hashCode());
		result = prime * result + ((prp8 == null) ? 0 : prp8.hashCode());
		result = prime * result + ((prp9 == null) ? 0 : prp9.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tab923 other = (Tab923) obj;
		if (ffinp == null) {
			if (other.ffinp != null)
				return false;
		} else if (!ffinp.equals(other.ffinp))
			return false;
		if (finip == null) {
			if (other.finip != null)
				return false;
		} else if (!finip.equals(other.finip))
			return false;
		if (l1 == null) {
			if (other.l1 != null)
				return false;
		} else if (!l1.equals(other.l1))
			return false;
		if (l2 == null) {
			if (other.l2 != null)
				return false;
		} else if (!l2.equals(other.l2))
			return false;
		if (l3 == null) {
			if (other.l3 != null)
				return false;
		} else if (!l3.equals(other.l3))
			return false;
		if (l4 == null) {
			if (other.l4 != null)
				return false;
		} else if (!l4.equals(other.l4))
			return false;
		if (l5 == null) {
			if (other.l5 != null)
				return false;
		} else if (!l5.equals(other.l5))
			return false;
		if (l6 == null) {
			if (other.l6 != null)
				return false;
		} else if (!l6.equals(other.l6))
			return false;
		if (l7 == null) {
			if (other.l7 != null)
				return false;
		} else if (!l7.equals(other.l7))
			return false;
		if (l8 == null) {
			if (other.l8 != null)
				return false;
		} else if (!l8.equals(other.l8))
			return false;
		if (l9 == null) {
			if (other.l9 != null)
				return false;
		} else if (!l9.equals(other.l9))
			return false;
		if (modalidad == null) {
			if (other.modalidad != null)
				return false;
		} else if (!modalidad.equals(other.modalidad))
			return false;
		if (prp1 == null) {
			if (other.prp1 != null)
				return false;
		} else if (!prp1.equals(other.prp1))
			return false;
		if (prp2 == null) {
			if (other.prp2 != null)
				return false;
		} else if (!prp2.equals(other.prp2))
			return false;
		if (prp3 == null) {
			if (other.prp3 != null)
				return false;
		} else if (!prp3.equals(other.prp3))
			return false;
		if (prp4 == null) {
			if (other.prp4 != null)
				return false;
		} else if (!prp4.equals(other.prp4))
			return false;
		if (prp5 == null) {
			if (other.prp5 != null)
				return false;
		} else if (!prp5.equals(other.prp5))
			return false;
		if (prp6 == null) {
			if (other.prp6 != null)
				return false;
		} else if (!prp6.equals(other.prp6))
			return false;
		if (prp7 == null) {
			if (other.prp7 != null)
				return false;
		} else if (!prp7.equals(other.prp7))
			return false;
		if (prp8 == null) {
			if (other.prp8 != null)
				return false;
		} else if (!prp8.equals(other.prp8))
			return false;
		if (prp9 == null) {
			if (other.prp9 != null)
				return false;
		} else if (!prp9.equals(other.prp9))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Tab923 [modalidad=" + modalidad + ", finip=" + finip + ", ffinp=" + ffinp + ", l1=" + l1 + ", prp1="
				+ prp1 + ", l2=" + l2 + ", prp2=" + prp2 + ", l3=" + l3 + ", prp3=" + prp3 + ", l4=" + l4 + ", prp4="
				+ prp4 + ", l5=" + l5 + ", prp5=" + prp5 + ", l6=" + l6 + ", prp6=" + prp6 + ", l7=" + l7 + ", prp7="
				+ prp7 + ", l8=" + l8 + ", prp8=" + prp8 + ", l9=" + l9 + ", prp9=" + prp9 + "]";
	}

	@Override
	public Tab923Key getKey() {
		// TODO Auto-generated method stub
		return new Tab923Key(modalidad, finip, ffinp);
	}
	
}