package es.mapfre.coaseguro.tirea.dominio.entidades;

import java.math.BigDecimal;
import java.sql.Timestamp;

import es.mapfre.coaseguro.tirea.dominio.EntidadBase;
import es.mapfre.coaseguro.tirea.dominio.keys.TotPmaCoaBtcoaKey;

public class TotPmaCoaBtcoa implements EntidadBase<TotPmaCoaBtcoaKey>{


	public static final int IND_BT = 0;
	public static final int IND_FCIERRE = 1;
	public static final int IND_KPOLIZA = 2;
	public static final int IND_KSUBPOLIZA = 3;
	public static final int IND_NSUSCRI = 4;
	public static final int IND_FECEFECINI = 5;
	public static final int IND_FECEFECFIN = 6;
	public static final int IND_KCOASE1 = 7;
	public static final int IND_KCOASE2 = 8;
	public static final int IND_KCOASE3 = 9;
	public static final int IND_KCOASE4 = 10;
	public static final int IND_KCOASE5 = 11;
	public static final int IND_KCOASE6 = 12;
	public static final int IND_TOTPROVISION = 13;
	public static final int IND_TABLACALC1ASEG1 = 14;
	public static final int IND_FACTOR1 = 15;
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
	public static final int IND_PGASTGESIN1I = 31;
	public static final int IND_PGASTGESIN2I = 32;
	public static final int IND_PGASTGESEX1I = 33;
	public static final int IND_PGASTGESEX2I = 34;
	public static final int IND_TOTFACTGTO = 35;
	
	
	private Timestamp fcierre;
	private String bt;
	private Long kpoliza;
	private Integer ksubpoliza;
	private Integer nsuscri;
	private java.math.BigDecimal pgastgesin1I;
	private java.math.BigDecimal pgastgesin2I;
	private java.math.BigDecimal pgastgesex1I;
	private java.math.BigDecimal pgastgesex2I;
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
	private BigDecimal totfactgto;
	private Timestamp fecefecini;
	private String tablacalc1aseg1;
	private BigDecimal factor1;
	private java.math.BigDecimal totprovision;
	private Timestamp fecefecfin;
	
	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
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

	public java.math.BigDecimal getPgastgesin1I() {
		return pgastgesin1I;
	}

	public void setPgastgesin1I(java.math.BigDecimal pgastgesin1i) {
		pgastgesin1I = pgastgesin1i;
	}

	public java.math.BigDecimal getPgastgesin2I() {
		return pgastgesin2I;
	}

	public void setPgastgesin2I(java.math.BigDecimal pgastgesin2i) {
		pgastgesin2I = pgastgesin2i;
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

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public java.math.BigDecimal getTotprovision() {
		return totprovision;
	}

	public void setTotprovision(java.math.BigDecimal totprovision) {
		this.totprovision = totprovision;
	}
	
	public java.math.BigDecimal getPgastgesex1I() {
		return pgastgesex1I;
	}

	public void setPgastgesex1I(java.math.BigDecimal pgastgesex1i) {
		pgastgesex1I = pgastgesex1i;
	}

	public java.math.BigDecimal getPgastgesex2I() {
		return pgastgesex2I;
	}

	public void setPgastgesex2I(java.math.BigDecimal pgastgesex2i) {
		pgastgesex2I = pgastgesex2i;
	}

	public BigDecimal getTotfactgto() {
		return totfactgto;
	}

	public void setTotfactgto(BigDecimal totfactgto) {
		this.totfactgto = totfactgto;
	}

	public Timestamp getFecefecfin() {
		return fecefecfin;
	}

	public void setFecefecfin(Timestamp fecefecfin) {
		this.fecefecfin = fecefecfin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
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
		result = prime * result + ((fecefecfin == null) ? 0 : fecefecfin.hashCode());
		result = prime * result + ((fecefecini == null) ? 0 : fecefecini.hashCode());
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pgastgesex1I == null) ? 0 : pgastgesex1I.hashCode());
		result = prime * result + ((pgastgesex2I == null) ? 0 : pgastgesex2I.hashCode());
		result = prime * result + ((pgastgesin1I == null) ? 0 : pgastgesin1I.hashCode());
		result = prime * result + ((pgastgesin2I == null) ? 0 : pgastgesin2I.hashCode());
		result = prime * result + ((pintertecnI1 == null) ? 0 : pintertecnI1.hashCode());
		result = prime * result + ((pintertecnI2 == null) ? 0 : pintertecnI2.hashCode());
		result = prime * result + ((pintertecnI3 == null) ? 0 : pintertecnI3.hashCode());
		result = prime * result + ((pintertecnI4 == null) ? 0 : pintertecnI4.hashCode());
		result = prime * result + ((pintertecnI5 == null) ? 0 : pintertecnI5.hashCode());
		result = prime * result + ((tablacalc1aseg1 == null) ? 0 : tablacalc1aseg1.hashCode());
		result = prime * result + ((totfactgto == null) ? 0 : totfactgto.hashCode());
		result = prime * result + ((totprovision == null) ? 0 : totprovision.hashCode());
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
		TotPmaCoaBtcoa other = (TotPmaCoaBtcoa) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
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
		if (fecefecfin == null) {
			if (other.fecefecfin != null)
				return false;
		} else if (!fecefecfin.equals(other.fecefecfin))
			return false;
		if (fecefecini == null) {
			if (other.fecefecini != null)
				return false;
		} else if (!fecefecini.equals(other.fecefecini))
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
		if (pgastgesex1I == null) {
			if (other.pgastgesex1I != null)
				return false;
		} else if (!pgastgesex1I.equals(other.pgastgesex1I))
			return false;
		if (pgastgesex2I == null) {
			if (other.pgastgesex2I != null)
				return false;
		} else if (!pgastgesex2I.equals(other.pgastgesex2I))
			return false;
		if (pgastgesin1I == null) {
			if (other.pgastgesin1I != null)
				return false;
		} else if (!pgastgesin1I.equals(other.pgastgesin1I))
			return false;
		if (pgastgesin2I == null) {
			if (other.pgastgesin2I != null)
				return false;
		} else if (!pgastgesin2I.equals(other.pgastgesin2I))
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
		if (totfactgto == null) {
			if (other.totfactgto != null)
				return false;
		} else if (!totfactgto.equals(other.totfactgto))
			return false;
		if (totprovision == null) {
			if (other.totprovision != null)
				return false;
		} else if (!totprovision.equals(other.totprovision))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TotPMaCoa [fcierre=" + fcierre + ", bt=" + bt + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", nsuscri=" + nsuscri + ", pgastgesin1I=" + pgastgesin1I + ", pgastgesin2I=" + pgastgesin2I
				+ ", pgastgesex1I=" + pgastgesex1I + ", pgastgesex2I=" + pgastgesex2I + ", pintertecnI1=" + pintertecnI1
				+ ", pintertecnI2=" + pintertecnI2 + ", pintertecnI3=" + pintertecnI3 + ", pintertecnI4=" + pintertecnI4
				+ ", pintertecnI5=" + pintertecnI5 + ", fecFinTramo1=" + fecFinTramo1 + ", fecFinTramo2=" + fecFinTramo2
				+ ", fecFinTramo3=" + fecFinTramo3 + ", fecFinTramo4=" + fecFinTramo4 + ", fecFinTramo5=" + fecFinTramo5
				+ ", fecIniTramo1=" + fecIniTramo1 + ", fecIniTramo2=" + fecIniTramo2 + ", fecIniTramo3=" + fecIniTramo3
				+ ", fecIniTramo4=" + fecIniTramo4 + ", fecIniTramo5=" + fecIniTramo5 + ", kcoase1=" + kcoase1
				+ ", kcoase2=" + kcoase2 + ", kcoase3=" + kcoase3 + ", kcoase4=" + kcoase4 + ", kcoase5=" + kcoase5
				+ ", kcoase6=" + kcoase6 + ", totfactgto=" + totfactgto + ", fecefecini=" + fecefecini
				+ ", tablacalc1aseg1=" + tablacalc1aseg1 + ", factor1=" + factor1 + ", totprovision=" + totprovision
				+ ", fecefecfin=" + fecefecfin + "]";
	}

	@Override
	public TotPmaCoaBtcoaKey getKey() {
		return new TotPmaCoaBtcoaKey(bt, fcierre,kpoliza, ksubpoliza, nsuscri);
	}

}