package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.FlujPMaCoaKey;


public class FlujPMaCoa implements EntidadBase<FlujPMaCoaKey> {

	public static final int IND_KPOLIZA = 0;
	public static final int IND_KSUBPOLIZA = 1;
	public static final int IND_NSUSCRI = 2;
	public static final int IND_FDESDE = 3;
	public static final int IND_FCIERRE = 4;
	public static final int IND_KCOASE1 = 4;
	public static final int IND_KCOASE2 = 5;
	public static final int IND_KCOASE3 = 6;
	public static final int IND_KCOASE4 = 7;
	public static final int IND_KCOASE5 = 8;
	public static final int IND_KCOASE6 = 9;
	public static final int IND_FECEFECINI = 10;
	public static final int IND_BT = 11;
	public static final int IND_TOTFLUJOPROBSINGASTOS = 12;
	public static final int IND_TOTFLUJOPROBDEGASTOS = 13;
	public static final int IND_TABLACALC1ASEG1 = 14;
	public static final int IND_ITCALC = 15;
	public static final int IND_FECINITRAMO1 = 16;
	public static final int IND_FECFINTRAMO1 = 17;
	public static final int IND_PINTERTECNI1 = 18;
	public static final int IND_FECINITRAMO2 = 19;
	public static final int IND_FECFINTRAMO2 = 20;
	public static final int IND_PINTERTECNI2 = 21;
	public static final int IND_FECINITRAMO3 = 22;
	public static final int IND_FECFINTRAMO3 = 23;
	public static final int IND_PINTERTECNI3 = 24;
	public static final int IND_FECINITRAMO4 = 25;
	public static final int IND_FECFINTRAMO4 = 26;
	public static final int IND_PINTERTECNI4 = 27;
	public static final int IND_FECINITRAMO5 = 28;
	public static final int IND_FECFINTRAMO5 = 29;
	public static final int IND_PINTERTECNI5 = 30;
	

	private Timestamp fcierre;
	private Timestamp fdesde;
	private String bt;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer nsuscri;
	private java.math.BigDecimal pintertecnI1;
	private java.math.BigDecimal pintertecnI2;
	private java.math.BigDecimal pintertecnI3;
	private java.math.BigDecimal pintertecnI4;
	private java.math.BigDecimal pintertecnI5;
	private Timestamp fecFinTramo1;
	private Timestamp fecFinTramo2;
	private Timestamp fecFinTramo3;
	private Timestamp fecFinTramo4;
	private Timestamp fecFinTramo5;
	private Timestamp fecIniTramo1;
	private Timestamp fecIniTramo2;
	private Timestamp fecIniTramo3;
	private Timestamp fecIniTramo4;
	private Timestamp fecIniTramo5;
	private String kcoase1;
	private String kcoase2;
	private String kcoase3;
	private String kcoase4;
	private String kcoase5;
	private String kcoase6;
	private Timestamp fecefecini;
	private String tablacalc1aseg1;
	private BigDecimal itcalc;
	private BigDecimal totflujoprobsingastos;
	private BigDecimal totflujoprobdegastos;
	
	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public Timestamp getFdesde() {
		return fdesde;
	}

	public void setFdesde(Timestamp fdesde) {
		this.fdesde = fdesde;
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
	}

	public Long getKpoliza() {
		return kpoliza;
	}

	public void setKpoliza(Long kpoliza) {
		this.kpoliza = kpoliza;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public java.math.BigDecimal getPintertecnI1() {
		return pintertecnI1;
	}

	public void setPintertecnI1(java.math.BigDecimal pintertecnI1) {
		this.pintertecnI1 = pintertecnI1;
	}

	public java.math.BigDecimal getPintertecnI2() {
		return pintertecnI2;
	}

	public void setPintertecnI2(java.math.BigDecimal pintertecnI2) {
		this.pintertecnI2 = pintertecnI2;
	}

	public java.math.BigDecimal getPintertecnI3() {
		return pintertecnI3;
	}

	public void setPintertecnI3(java.math.BigDecimal pintertecnI3) {
		this.pintertecnI3 = pintertecnI3;
	}

	public java.math.BigDecimal getPintertecnI4() {
		return pintertecnI4;
	}

	public void setPintertecnI4(java.math.BigDecimal pintertecnI4) {
		this.pintertecnI4 = pintertecnI4;
	}

	public java.math.BigDecimal getPintertecnI5() {
		return pintertecnI5;
	}

	public void setPintertecnI5(java.math.BigDecimal pintertecnI5) {
		this.pintertecnI5 = pintertecnI5;
	}

	public Timestamp getFecFinTramo1() {
		return fecFinTramo1;
	}

	public void setFecFinTramo1(Timestamp fecFinTramo1) {
		this.fecFinTramo1 = fecFinTramo1;
	}

	public Timestamp getFecFinTramo2() {
		return fecFinTramo2;
	}

	public void setFecFinTramo2(Timestamp fecFinTramo2) {
		this.fecFinTramo2 = fecFinTramo2;
	}

	public Timestamp getFecFinTramo3() {
		return fecFinTramo3;
	}

	public void setFecFinTramo3(Timestamp fecFinTramo3) {
		this.fecFinTramo3 = fecFinTramo3;
	}

	public Timestamp getFecFinTramo4() {
		return fecFinTramo4;
	}

	public void setFecFinTramo4(Timestamp fecFinTramo4) {
		this.fecFinTramo4 = fecFinTramo4;
	}

	public Timestamp getFecFinTramo5() {
		return fecFinTramo5;
	}

	public void setFecFinTramo5(Timestamp fecFinTramo5) {
		this.fecFinTramo5 = fecFinTramo5;
	}

	public Timestamp getFecIniTramo1() {
		return fecIniTramo1;
	}

	public void setFecIniTramo1(Timestamp fecIniTramo1) {
		this.fecIniTramo1 = fecIniTramo1;
	}

	public Timestamp getFecIniTramo2() {
		return fecIniTramo2;
	}

	public void setFecIniTramo2(Timestamp fecIniTramo2) {
		this.fecIniTramo2 = fecIniTramo2;
	}

	public Timestamp getFecIniTramo3() {
		return fecIniTramo3;
	}

	public void setFecIniTramo3(Timestamp fecIniTramo3) {
		this.fecIniTramo3 = fecIniTramo3;
	}

	public Timestamp getFecIniTramo4() {
		return fecIniTramo4;
	}

	public void setFecIniTramo4(Timestamp fecIniTramo4) {
		this.fecIniTramo4 = fecIniTramo4;
	}

	public Timestamp getFecIniTramo5() {
		return fecIniTramo5;
	}

	public void setFecIniTramo5(Timestamp fecIniTramo5) {
		this.fecIniTramo5 = fecIniTramo5;
	}

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

	public Timestamp getFecefecini() {
		return fecefecini;
	}

	public void setFecefecini(Timestamp fecefecini) {
		this.fecefecini = fecefecini;
	}

	public String getTablacalc1aseg1() {
		return tablacalc1aseg1;
	}

	public void setTablacalc1aseg1(String tablacalc1aseg1) {
		this.tablacalc1aseg1 = tablacalc1aseg1;
	}

	public BigDecimal getItcalc() {
		return itcalc;
	}

	public void setItcalc(BigDecimal itcalc) {
		this.itcalc = itcalc;
	}

	public BigDecimal getTotflujoprobsingastos() {
		return totflujoprobsingastos;
	}

	public void setTotflujoprobsingastos(BigDecimal totflujoprobsingastos) {
		this.totflujoprobsingastos = totflujoprobsingastos;
	}

	public BigDecimal getTotflujoprobdegastos() {
		return totflujoprobdegastos;
	}

	public void setTotflujoprobdegastos(BigDecimal totflujoprobdegastos) {
		this.totflujoprobdegastos = totflujoprobdegastos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fdesde == null) ? 0 : fdesde.hashCode());
		result = prime * result + ((fecFinTramo1 == null) ? 0 : fecFinTramo1.hashCode());
		result = prime * result + ((fecFinTramo2 == null) ? 0 : fecFinTramo2.hashCode());
		result = prime * result + ((fecFinTramo3 == null) ? 0 : fecFinTramo3.hashCode());
		result = prime * result + ((fecFinTramo4 == null) ? 0 : fecFinTramo4.hashCode());
		result = prime * result + ((fecFinTramo5 == null) ? 0 : fecFinTramo5.hashCode());
		result = prime * result + ((fecIniTramo1 == null) ? 0 : fecIniTramo1.hashCode());
		result = prime * result + ((fecIniTramo2 == null) ? 0 : fecIniTramo2.hashCode());
		result = prime * result + ((fecIniTramo3 == null) ? 0 : fecIniTramo3.hashCode());
		result = prime * result + ((fecIniTramo4 == null) ? 0 : fecIniTramo4.hashCode());
		result = prime * result + ((fecIniTramo5 == null) ? 0 : fecIniTramo5.hashCode());
		result = prime * result + ((fecefecini == null) ? 0 : fecefecini.hashCode());
		result = prime * result + ((itcalc == null) ? 0 : itcalc.hashCode());
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pintertecnI1 == null) ? 0 : pintertecnI1.hashCode());
		result = prime * result + ((pintertecnI2 == null) ? 0 : pintertecnI2.hashCode());
		result = prime * result + ((pintertecnI3 == null) ? 0 : pintertecnI3.hashCode());
		result = prime * result + ((pintertecnI4 == null) ? 0 : pintertecnI4.hashCode());
		result = prime * result + ((pintertecnI5 == null) ? 0 : pintertecnI5.hashCode());
		result = prime * result + ((tablacalc1aseg1 == null) ? 0 : tablacalc1aseg1.hashCode());
		result = prime * result + ((totflujoprobdegastos == null) ? 0 : totflujoprobdegastos.hashCode());
		result = prime * result + ((totflujoprobsingastos == null) ? 0 : totflujoprobsingastos.hashCode());
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
		FlujPMaCoa other = (FlujPMaCoa) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fdesde == null) {
			if (other.fdesde != null)
				return false;
		} else if (!fdesde.equals(other.fdesde))
			return false;
		if (fecFinTramo1 == null) {
			if (other.fecFinTramo1 != null)
				return false;
		} else if (!fecFinTramo1.equals(other.fecFinTramo1))
			return false;
		if (fecFinTramo2 == null) {
			if (other.fecFinTramo2 != null)
				return false;
		} else if (!fecFinTramo2.equals(other.fecFinTramo2))
			return false;
		if (fecFinTramo3 == null) {
			if (other.fecFinTramo3 != null)
				return false;
		} else if (!fecFinTramo3.equals(other.fecFinTramo3))
			return false;
		if (fecFinTramo4 == null) {
			if (other.fecFinTramo4 != null)
				return false;
		} else if (!fecFinTramo4.equals(other.fecFinTramo4))
			return false;
		if (fecFinTramo5 == null) {
			if (other.fecFinTramo5 != null)
				return false;
		} else if (!fecFinTramo5.equals(other.fecFinTramo5))
			return false;
		if (fecIniTramo1 == null) {
			if (other.fecIniTramo1 != null)
				return false;
		} else if (!fecIniTramo1.equals(other.fecIniTramo1))
			return false;
		if (fecIniTramo2 == null) {
			if (other.fecIniTramo2 != null)
				return false;
		} else if (!fecIniTramo2.equals(other.fecIniTramo2))
			return false;
		if (fecIniTramo3 == null) {
			if (other.fecIniTramo3 != null)
				return false;
		} else if (!fecIniTramo3.equals(other.fecIniTramo3))
			return false;
		if (fecIniTramo4 == null) {
			if (other.fecIniTramo4 != null)
				return false;
		} else if (!fecIniTramo4.equals(other.fecIniTramo4))
			return false;
		if (fecIniTramo5 == null) {
			if (other.fecIniTramo5 != null)
				return false;
		} else if (!fecIniTramo5.equals(other.fecIniTramo5))
			return false;
		if (fecefecini == null) {
			if (other.fecefecini != null)
				return false;
		} else if (!fecefecini.equals(other.fecefecini))
			return false;
		if (itcalc == null) {
			if (other.itcalc != null)
				return false;
		} else if (!itcalc.equals(other.itcalc))
			return false;
		if (kcoase1 == null) {
			if (other.kcoase1 != null)
				return false;
		} else if (!kcoase1.equals(other.kcoase1))
			return false;
		if (kcoase2 == null) {
			if (other.kcoase2 != null)
				return false;
		} else if (!kcoase2.equals(other.kcoase2))
			return false;
		if (kcoase3 == null) {
			if (other.kcoase3 != null)
				return false;
		} else if (!kcoase3.equals(other.kcoase3))
			return false;
		if (kcoase4 == null) {
			if (other.kcoase4 != null)
				return false;
		} else if (!kcoase4.equals(other.kcoase4))
			return false;
		if (kcoase5 == null) {
			if (other.kcoase5 != null)
				return false;
		} else if (!kcoase5.equals(other.kcoase5))
			return false;
		if (kcoase6 == null) {
			if (other.kcoase6 != null)
				return false;
		} else if (!kcoase6.equals(other.kcoase6))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (pintertecnI1 == null) {
			if (other.pintertecnI1 != null)
				return false;
		} else if (!pintertecnI1.equals(other.pintertecnI1))
			return false;
		if (pintertecnI2 == null) {
			if (other.pintertecnI2 != null)
				return false;
		} else if (!pintertecnI2.equals(other.pintertecnI2))
			return false;
		if (pintertecnI3 == null) {
			if (other.pintertecnI3 != null)
				return false;
		} else if (!pintertecnI3.equals(other.pintertecnI3))
			return false;
		if (pintertecnI4 == null) {
			if (other.pintertecnI4 != null)
				return false;
		} else if (!pintertecnI4.equals(other.pintertecnI4))
			return false;
		if (pintertecnI5 == null) {
			if (other.pintertecnI5 != null)
				return false;
		} else if (!pintertecnI5.equals(other.pintertecnI5))
			return false;
		if (tablacalc1aseg1 == null) {
			if (other.tablacalc1aseg1 != null)
				return false;
		} else if (!tablacalc1aseg1.equals(other.tablacalc1aseg1))
			return false;
		if (totflujoprobdegastos == null) {
			if (other.totflujoprobdegastos != null)
				return false;
		} else if (!totflujoprobdegastos.equals(other.totflujoprobdegastos))
			return false;
		if (totflujoprobsingastos == null) {
			if (other.totflujoprobsingastos != null)
				return false;
		} else if (!totflujoprobsingastos.equals(other.totflujoprobsingastos))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlujPMaCoa [fcierre=" + fcierre + ", fdesde=" + fdesde + ", bt=" + bt + ", kpoliza=" + kpoliza
				+ ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri + ", pintertecnI1=" + pintertecnI1
				+ ", pintertecnI2=" + pintertecnI2 + ", pintertecnI3=" + pintertecnI3 + ", pintertecnI4=" + pintertecnI4
				+ ", pintertecnI5=" + pintertecnI5 + ", fecFinTramo1=" + fecFinTramo1 + ", fecFinTramo2=" + fecFinTramo2
				+ ", fecFinTramo3=" + fecFinTramo3 + ", fecFinTramo4=" + fecFinTramo4 + ", fecFinTramo5=" + fecFinTramo5
				+ ", fecIniTramo1=" + fecIniTramo1 + ", fecIniTramo2=" + fecIniTramo2 + ", fecIniTramo3=" + fecIniTramo3
				+ ", fecIniTramo4=" + fecIniTramo4 + ", fecIniTramo5=" + fecIniTramo5 + ", kcoase1=" + kcoase1
				+ ", kcoase2=" + kcoase2 + ", kcoase3=" + kcoase3 + ", kcoase4=" + kcoase4 + ", kcoase5=" + kcoase5
				+ ", kcoase6=" + kcoase6 + ", fecefecini=" + fecefecini + ", tablacalc1aseg1=" + tablacalc1aseg1
				+ ", itcalc=" + itcalc + ", totflujoprobsingastos=" + totflujoprobsingastos + ", totflujoprobdegastos="
				+ totflujoprobdegastos + "]";
	}

	@Override
	public FlujPMaCoaKey getKey() {
		return new FlujPMaCoaKey(kpoliza, ksubpoliza, nsuscri, bt, fcierre, fdesde);
	}

}