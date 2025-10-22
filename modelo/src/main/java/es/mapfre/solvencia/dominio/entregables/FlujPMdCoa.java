/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 14/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaKey;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujPMdCoa implements EntidadBase<FlujPMdCoaKey>, EntidadConBaseTec {

	public static final int IND_KMODALIDAD = 0;
	public static final int IND_KGARANTIA = 1;
	public static final int IND_KPRESTACION = 2;
	public static final int IND_KPOLIZA = 3;
	public static final int IND_KSUBPOLIZA = 4;
	public static final int IND_KCERTIFICADO = 5;
	public static final int IND_NSUSCRI = 6;
	public static final int IND_FCIERRE = 7;
	public static final int IND_KCOASE1 = 8;
	public static final int IND_KCOASE2 = 9;
	public static final int IND_KCOASE3 = 10;
	public static final int IND_KCOASE4 = 11;
	public static final int IND_KCOASE5 = 12;
	public static final int IND_KCOASE6 = 13;
	public static final int IND_FECEFECINI = 14;
	public static final int IND_FECEFECFIN = 15;
	public static final int IND_BT = 16;
	public static final int IND_TOTPROVISION = 17;
	public static final int IND_TABLACALC1ASEG1 = 18;
	public static final int IND_ITCALC = 19;
	public static final int IND_FECINITRAMO1 = 20;
	public static final int IND_FECFINTRAMO1 = 21;
	public static final int IND_PINTERTECNI1 = 22;
	public static final int IND_FECINITRAMO2 = 23;
	public static final int IND_FECFINTRAMO2 = 24;
	public static final int IND_PINTERTECNI2 = 25;
	public static final int IND_FECINITRAMO3 = 26;
	public static final int IND_FECFINTRAMO3 = 27;
	public static final int IND_PINTERTECNI3 = 28;
	public static final int IND_FECINITRAMO4 = 29;
	public static final int IND_FECFINTRAMO4 = 30;
	public static final int IND_PINTERTECNI4 = 31;
	public static final int IND_FECINITRAMO5 = 32;
	public static final int IND_FECFINTRAMO5 = 33;
	public static final int IND_PINTERTECNI5 = 34;
	public static final int IND_PGASTGESIN1I = 35;
	public static final int IND_PGASTGESIN2I = 36;
	public static final int IND_PGASTGESIN3I = 37;
	public static final int IND_GTOROSSP_CAP = 38;
	public static final int IND_GTOROSSP_PRIMA = 39;
	public static final int IND_GTOROSSP_PROV = 40;
	public static final int IND_FNACASEG1 = 41;
	public static final int IND_FNACASEG2 = 42;
	public static final int IND_FNACASEG3 = 43;
	public static final int IND_FNACASEG4 = 44;
	public static final int IND_FNACASEG5 = 45;
	public static final int IND_CSEXASEG1 = 46;
	public static final int IND_CSEXASEG2 = 47;
	public static final int IND_CSEXASEG3 = 48;
	public static final int IND_CSEXASEG4 = 49;
	public static final int IND_CSEXASEG5 = 50;
	public static final int IND_CESTADOASEG1 = 51;
	public static final int IND_CESTADOASEG2 = 52;
	public static final int IND_CESTADOASEG3 = 53;
	public static final int IND_CESTADOASEG4 = 54;
	public static final int IND_CESTADOASEG5 = 55;
	public static final int IND_IPRIMANETAINI = 56;
	public static final int IND_IPRIMANETAACT = 57;
	public static final int IND_IPRIMATARADA = 58;
	public static final int IND_ICAPINI = 59;
	public static final int IND_ICAPACT = 60;
	public static final int IND_ISALDO = 61;
	public static final int IND_FECINI = 62;
	public static final int IND_FECFIN = 63;
	public static final int IND_TEMPVIT = 64;
	public static final int IND_PREVRENTA = 65;
	public static final int IND_PREVERSION = 66;
	public static final int IND_NPERGARAN = 67;
	public static final int IND_NADIFER = 68;
	public static final int IND_FORPAGRENT = 69;
	public static final int IND_CPAGRENTA = 70;
	public static final int IND_CTIPOREVRENTA = 71;
	public static final int IND_CFORMAREVRENTA = 72;
	public static final int IND_NDURRENTA = 73;
	public static final int IND_KAJUSTE = 74;
	public static final int IND_PSOBREMORT = 75;
	public static final int IND_PRIESGO = 76;
	public static final int IND_GVALOR = 77;
	public static final int IND_FDIADEPAGO = 78;
	

	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_PINTERTECNI1)
	private java.math.BigDecimal pintertecnI1;
	@PortableProperty(IND_PINTERTECNI2)
	private java.math.BigDecimal pintertecnI2;
	@PortableProperty(IND_PINTERTECNI3)
	private java.math.BigDecimal pintertecnI3;
	@PortableProperty(IND_PINTERTECNI4)
	private java.math.BigDecimal pintertecnI4;
	@PortableProperty(IND_PINTERTECNI5)
	private java.math.BigDecimal pintertecnI5;
	@PortableProperty(IND_FECFINTRAMO1)
	private Timestamp fecFinTramo1;
	@PortableProperty(IND_FECFINTRAMO2)
	private Timestamp fecFinTramo2;
	@PortableProperty(IND_FECFINTRAMO3)
	private Timestamp fecFinTramo3;
	@PortableProperty(IND_FECFINTRAMO4)
	private Timestamp fecFinTramo4;
	@PortableProperty(IND_FECFINTRAMO5)
	private Timestamp fecFinTramo5;
	@PortableProperty(IND_FECINITRAMO1)
	private Timestamp fecIniTramo1;
	@PortableProperty(IND_FECINITRAMO2)
	private Timestamp fecIniTramo2;
	@PortableProperty(IND_FECINITRAMO3)
	private Timestamp fecIniTramo3;
	@PortableProperty(IND_FECINITRAMO4)
	private Timestamp fecIniTramo4;
	@PortableProperty(IND_FECINITRAMO5)
	private Timestamp fecIniTramo5;
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
	@PortableProperty(IND_FECEFECINI)
	private Timestamp fecefecini;
	@PortableProperty(IND_FECEFECFIN)
	private Timestamp fecefecfin;
	@PortableProperty(IND_TABLACALC1ASEG1)
	private String tablacalc1aseg1;
	@PortableProperty(IND_ITCALC)
	private BigDecimal itcalc;
	@PortableProperty(IND_TOTPROVISION)
	private java.math.BigDecimal totprovision;
	@PortableProperty(IND_GTOROSSP_CAP)
	private BigDecimal gtorosspCap;
	@PortableProperty(IND_GTOROSSP_PRIMA)
	private BigDecimal gtorosspPrima;
	@PortableProperty(IND_GTOROSSP_PROV)
	private BigDecimal gtorosspProv;
	@PortableProperty(IND_PGASTGESIN1I)
	private java.math.BigDecimal pgastgesin1I;
	@PortableProperty(IND_PGASTGESIN2I)
	private java.math.BigDecimal pgastgesin2I;
	@PortableProperty(IND_PGASTGESIN3I)
	private java.math.BigDecimal pgastgesin3I;
	@PortableProperty(IND_CSEXASEG1)
	private String csexAseg1;
	@PortableProperty(IND_CSEXASEG2)
	private String csexAseg2;
	@PortableProperty(IND_CSEXASEG3)
	private String csexAseg3;
	@PortableProperty(IND_CSEXASEG4)
	private String csexAseg4;
	@PortableProperty(IND_CSEXASEG5)
	private String csexAseg5;
	@PortableProperty(IND_CESTADOASEG1)
	private String cestadoAseg1;
	@PortableProperty(IND_CESTADOASEG2)
	private String cestadoAseg2;
	@PortableProperty(IND_CESTADOASEG3)
	private String cestadoAseg3;
	@PortableProperty(IND_CESTADOASEG4)
	private String cestadoAseg4;
	@PortableProperty(IND_CESTADOASEG5)
	private String cestadoAseg5;
	@PortableProperty(IND_FNACASEG1)
	private Timestamp fnacAseg1;
	@PortableProperty(IND_FNACASEG2)
	private Timestamp fnacAseg2;
	@PortableProperty(IND_FNACASEG3)
	private Timestamp fnacAseg3;
	@PortableProperty(IND_FNACASEG4)
	private Timestamp fnacAseg4;
	@PortableProperty(IND_FNACASEG5)
	private Timestamp fnacAseg5;
	@PortableProperty(IND_IPRIMANETAACT)
	private java.math.BigDecimal iprimanetaact;
	@PortableProperty(IND_IPRIMANETAINI)
	private java.math.BigDecimal iprimanetaini;
	@PortableProperty(IND_IPRIMATARADA)
	private java.math.BigDecimal iprimatarada;
	@PortableProperty(IND_ICAPACT)
	private java.math.BigDecimal icapact;
	@PortableProperty(IND_ICAPINI)
	private java.math.BigDecimal icapini;
	@PortableProperty(IND_ISALDO)
	private java.math.BigDecimal isaldo;
	@PortableProperty(IND_FECFIN)
	private Timestamp fecFin;
	@PortableProperty(IND_FECINI)
	private Timestamp fecIni;
	@PortableProperty(IND_TEMPVIT)
	private String tempVit;
	@PortableProperty(IND_PREVRENTA)
	private java.math.BigDecimal prevrenta;
	@PortableProperty(IND_PREVERSION)
	private java.math.BigDecimal preversion;
	@PortableProperty(IND_NPERGARAN)
	private Integer npergaran;
	@PortableProperty(IND_NADIFER)
	private Integer nadifer;
	@PortableProperty(IND_FORPAGRENT)
	private Integer forpagrent;
	@PortableProperty(IND_CPAGRENTA)
	private String cpagrenta;
	@PortableProperty(IND_CTIPOREVRENTA)
	private String ctipoRevrenta;
	@PortableProperty(IND_CFORMAREVRENTA)
	private String cformaRevrenta;
	@PortableProperty(IND_NDURRENTA)
	private Integer ndurrenta;
	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_PSOBREMORT)
	private java.math.BigDecimal psobremort;
	@PortableProperty(IND_PRIESGO)
	private java.math.BigDecimal priesgo;
	@PortableProperty(IND_GVALOR) 
	private String gvalor;
	@PortableProperty(IND_FDIADEPAGO)
	private Integer fdiadepago;

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

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

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
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

	public Timestamp getFecefecfin() {
		return fecefecfin;
	}

	public void setFecefecfin(Timestamp fecefecfin) {
		this.fecefecfin = fecefecfin;
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

	public java.math.BigDecimal getTotprovision() {
		return totprovision;
	}

	public void setTotprovision(java.math.BigDecimal totprovision) {
		this.totprovision = totprovision;
	}

	public BigDecimal getGtorosspCap() {
		return gtorosspCap;
	}

	public void setGtorosspCap(BigDecimal gtorosspCap) {
		this.gtorosspCap = gtorosspCap;
	}

	public BigDecimal getGtorosspPrima() {
		return gtorosspPrima;
	}

	public void setGtorosspPrima(BigDecimal gtorosspPrima) {
		this.gtorosspPrima = gtorosspPrima;
	}

	public BigDecimal getGtorosspProv() {
		return gtorosspProv;
	}

	public void setGtorosspProv(BigDecimal gtorosspProv) {
		this.gtorosspProv = gtorosspProv;
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

	public java.math.BigDecimal getPgastgesin3I() {
		return pgastgesin3I;
	}

	public void setPgastgesin3I(java.math.BigDecimal pgastgesin3i) {
		pgastgesin3I = pgastgesin3i;
	}

	public String getCsexAseg1() {
		return csexAseg1;
	}

	public void setCsexAseg1(String csexAseg1) {
		this.csexAseg1 = csexAseg1;
	}

	public String getCsexAseg2() {
		return csexAseg2;
	}

	public void setCsexAseg2(String csexAseg2) {
		this.csexAseg2 = csexAseg2;
	}

	public String getCsexAseg3() {
		return csexAseg3;
	}

	public void setCsexAseg3(String csexAseg3) {
		this.csexAseg3 = csexAseg3;
	}

	public String getCsexAseg4() {
		return csexAseg4;
	}

	public void setCsexAseg4(String csexAseg4) {
		this.csexAseg4 = csexAseg4;
	}

	public String getCsexAseg5() {
		return csexAseg5;
	}

	public void setCsexAseg5(String csexAseg5) {
		this.csexAseg5 = csexAseg5;
	}

	public String getCestadoAseg1() {
		return cestadoAseg1;
	}

	public void setCestadoAseg1(String cestadoAseg1) {
		this.cestadoAseg1 = cestadoAseg1;
	}

	public String getCestadoAseg2() {
		return cestadoAseg2;
	}

	public void setCestadoAseg2(String cestadoAseg2) {
		this.cestadoAseg2 = cestadoAseg2;
	}

	public String getCestadoAseg3() {
		return cestadoAseg3;
	}

	public void setCestadoAseg3(String cestadoAseg3) {
		this.cestadoAseg3 = cestadoAseg3;
	}

	public String getCestadoAseg4() {
		return cestadoAseg4;
	}

	public void setCestadoAseg4(String cestadoAseg4) {
		this.cestadoAseg4 = cestadoAseg4;
	}

	public String getCestadoAseg5() {
		return cestadoAseg5;
	}

	public void setCestadoAseg5(String cestadoAseg5) {
		this.cestadoAseg5 = cestadoAseg5;
	}

	public Timestamp getFnacAseg1() {
		return fnacAseg1;
	}

	public void setFnacAseg1(Timestamp fnacAseg1) {
		this.fnacAseg1 = fnacAseg1;
	}

	public Timestamp getFnacAseg2() {
		return fnacAseg2;
	}

	public void setFnacAseg2(Timestamp fnacAseg2) {
		this.fnacAseg2 = fnacAseg2;
	}

	public Timestamp getFnacAseg3() {
		return fnacAseg3;
	}

	public void setFnacAseg3(Timestamp fnacAseg3) {
		this.fnacAseg3 = fnacAseg3;
	}

	public Timestamp getFnacAseg4() {
		return fnacAseg4;
	}

	public void setFnacAseg4(Timestamp fnacAseg4) {
		this.fnacAseg4 = fnacAseg4;
	}

	public Timestamp getFnacAseg5() {
		return fnacAseg5;
	}

	public void setFnacAseg5(Timestamp fnacAseg5) {
		this.fnacAseg5 = fnacAseg5;
	}

	public java.math.BigDecimal getIprimanetaact() {
		return iprimanetaact;
	}

	public void setIprimanetaact(java.math.BigDecimal iprimanetaact) {
		this.iprimanetaact = iprimanetaact;
	}

	public java.math.BigDecimal getIprimanetaini() {
		return iprimanetaini;
	}

	public void setIprimanetaini(java.math.BigDecimal iprimanetaini) {
		this.iprimanetaini = iprimanetaini;
	}

	public java.math.BigDecimal getIprimatarada() {
		return iprimatarada;
	}

	public void setIprimatarada(java.math.BigDecimal iprimatarada) {
		this.iprimatarada = iprimatarada;
	}

	public java.math.BigDecimal getIcapact() {
		return icapact;
	}

	public void setIcapact(java.math.BigDecimal icapact) {
		this.icapact = icapact;
	}

	public java.math.BigDecimal getIcapini() {
		return icapini;
	}

	public void setIcapini(java.math.BigDecimal icapini) {
		this.icapini = icapini;
	}

	public java.math.BigDecimal getIsaldo() {
		return isaldo;
	}

	public void setIsaldo(java.math.BigDecimal isaldo) {
		this.isaldo = isaldo;
	}

	public Timestamp getFecFin() {
		return fecFin;
	}

	public void setFecFin(Timestamp fecFin) {
		this.fecFin = fecFin;
	}

	public Timestamp getFecIni() {
		return fecIni;
	}

	public void setFecIni(Timestamp fecIni) {
		this.fecIni = fecIni;
	}

	public String getTempVit() {
		return tempVit;
	}

	public void setTempVit(String tempVit) {
		this.tempVit = tempVit;
	}

	public java.math.BigDecimal getPrevrenta() {
		return prevrenta;
	}

	public void setPrevrenta(java.math.BigDecimal prevrenta) {
		this.prevrenta = prevrenta;
	}

	public java.math.BigDecimal getPreversion() {
		return preversion;
	}

	public void setPreversion(java.math.BigDecimal preversion) {
		this.preversion = preversion;
	}

	public Integer getNpergaran() {
		return npergaran;
	}

	public void setNpergaran(Integer npergaran) {
		this.npergaran = npergaran;
	}

	public Integer getNadifer() {
		return nadifer;
	}

	public void setNadifer(Integer nadifer) {
		this.nadifer = nadifer;
	}

	public Integer getForpagrent() {
		return forpagrent;
	}

	public void setForpagrent(Integer forpagrent) {
		this.forpagrent = forpagrent;
	}

	public String getCpagrenta() {
		return cpagrenta;
	}

	public void setCpagrenta(String cpagrenta) {
		this.cpagrenta = cpagrenta;
	}

	public String getCtipoRevrenta() {
		return ctipoRevrenta;
	}

	public void setCtipoRevrenta(String ctipoRevrenta) {
		this.ctipoRevrenta = ctipoRevrenta;
	}

	public String getCformaRevrenta() {
		return cformaRevrenta;
	}

	public void setCformaRevrenta(String cformaRevrenta) {
		this.cformaRevrenta = cformaRevrenta;
	}

	public Integer getNdurrenta() {
		return ndurrenta;
	}

	public void setNdurrenta(Integer ndurrenta) {
		this.ndurrenta = ndurrenta;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public java.math.BigDecimal getPsobremort() {
		return psobremort;
	}

	public void setPsobremort(java.math.BigDecimal psobremort) {
		this.psobremort = psobremort;
	}

	public java.math.BigDecimal getPriesgo() {
		return priesgo;
	}

	public void setPriesgo(java.math.BigDecimal priesgo) {
		this.priesgo = priesgo;
	}

	public String getGvalor() {
		return gvalor;
	}

	public void setGvalor(String gvalor) {
		this.gvalor = gvalor;
	}

	public Integer getFdiadepago() {
		return fdiadepago;
	}

	public void setFdiadepago(Integer fdiadepago) {
		this.fdiadepago = fdiadepago;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((cestadoAseg1 == null) ? 0 : cestadoAseg1.hashCode());
		result = prime * result + ((cestadoAseg2 == null) ? 0 : cestadoAseg2.hashCode());
		result = prime * result + ((cestadoAseg3 == null) ? 0 : cestadoAseg3.hashCode());
		result = prime * result + ((cestadoAseg4 == null) ? 0 : cestadoAseg4.hashCode());
		result = prime * result + ((cestadoAseg5 == null) ? 0 : cestadoAseg5.hashCode());
		result = prime * result + ((cformaRevrenta == null) ? 0 : cformaRevrenta.hashCode());
		result = prime * result + ((cpagrenta == null) ? 0 : cpagrenta.hashCode());
		result = prime * result + ((csexAseg1 == null) ? 0 : csexAseg1.hashCode());
		result = prime * result + ((csexAseg2 == null) ? 0 : csexAseg2.hashCode());
		result = prime * result + ((csexAseg3 == null) ? 0 : csexAseg3.hashCode());
		result = prime * result + ((csexAseg4 == null) ? 0 : csexAseg4.hashCode());
		result = prime * result + ((csexAseg5 == null) ? 0 : csexAseg5.hashCode());
		result = prime * result + ((ctipoRevrenta == null) ? 0 : ctipoRevrenta.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fdiadepago == null) ? 0 : fdiadepago.hashCode());
		result = prime * result + ((fecFin == null) ? 0 : fecFin.hashCode());
		result = prime * result + ((fecFinTramo1 == null) ? 0 : fecFinTramo1.hashCode());
		result = prime * result + ((fecFinTramo2 == null) ? 0 : fecFinTramo2.hashCode());
		result = prime * result + ((fecFinTramo3 == null) ? 0 : fecFinTramo3.hashCode());
		result = prime * result + ((fecFinTramo4 == null) ? 0 : fecFinTramo4.hashCode());
		result = prime * result + ((fecFinTramo5 == null) ? 0 : fecFinTramo5.hashCode());
		result = prime * result + ((fecIni == null) ? 0 : fecIni.hashCode());
		result = prime * result + ((fecIniTramo1 == null) ? 0 : fecIniTramo1.hashCode());
		result = prime * result + ((fecIniTramo2 == null) ? 0 : fecIniTramo2.hashCode());
		result = prime * result + ((fecIniTramo3 == null) ? 0 : fecIniTramo3.hashCode());
		result = prime * result + ((fecIniTramo4 == null) ? 0 : fecIniTramo4.hashCode());
		result = prime * result + ((fecIniTramo5 == null) ? 0 : fecIniTramo5.hashCode());
		result = prime * result + ((fecefecfin == null) ? 0 : fecefecfin.hashCode());
		result = prime * result + ((fecefecini == null) ? 0 : fecefecini.hashCode());
		result = prime * result + ((fnacAseg1 == null) ? 0 : fnacAseg1.hashCode());
		result = prime * result + ((fnacAseg2 == null) ? 0 : fnacAseg2.hashCode());
		result = prime * result + ((fnacAseg3 == null) ? 0 : fnacAseg3.hashCode());
		result = prime * result + ((fnacAseg4 == null) ? 0 : fnacAseg4.hashCode());
		result = prime * result + ((fnacAseg5 == null) ? 0 : fnacAseg5.hashCode());
		result = prime * result + ((forpagrent == null) ? 0 : forpagrent.hashCode());
		result = prime * result + ((gtorosspCap == null) ? 0 : gtorosspCap.hashCode());
		result = prime * result + ((gtorosspPrima == null) ? 0 : gtorosspPrima.hashCode());
		result = prime * result + ((gtorosspProv == null) ? 0 : gtorosspProv.hashCode());
		result = prime * result + ((gvalor == null) ? 0 : gvalor.hashCode());
		result = prime * result + ((icapact == null) ? 0 : icapact.hashCode());
		result = prime * result + ((icapini == null) ? 0 : icapini.hashCode());
		result = prime * result + ((iprimanetaact == null) ? 0 : iprimanetaact.hashCode());
		result = prime * result + ((iprimanetaini == null) ? 0 : iprimanetaini.hashCode());
		result = prime * result + ((iprimatarada == null) ? 0 : iprimatarada.hashCode());
		result = prime * result + ((isaldo == null) ? 0 : isaldo.hashCode());
		result = prime * result + ((itcalc == null) ? 0 : itcalc.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((nadifer == null) ? 0 : nadifer.hashCode());
		result = prime * result + ((ndurrenta == null) ? 0 : ndurrenta.hashCode());
		result = prime * result + ((npergaran == null) ? 0 : npergaran.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((pgastgesin1I == null) ? 0 : pgastgesin1I.hashCode());
		result = prime * result + ((pgastgesin2I == null) ? 0 : pgastgesin2I.hashCode());
		result = prime * result + ((pgastgesin3I == null) ? 0 : pgastgesin3I.hashCode());
		result = prime * result + ((pintertecnI1 == null) ? 0 : pintertecnI1.hashCode());
		result = prime * result + ((pintertecnI2 == null) ? 0 : pintertecnI2.hashCode());
		result = prime * result + ((pintertecnI3 == null) ? 0 : pintertecnI3.hashCode());
		result = prime * result + ((pintertecnI4 == null) ? 0 : pintertecnI4.hashCode());
		result = prime * result + ((pintertecnI5 == null) ? 0 : pintertecnI5.hashCode());
		result = prime * result + ((preversion == null) ? 0 : preversion.hashCode());
		result = prime * result + ((prevrenta == null) ? 0 : prevrenta.hashCode());
		result = prime * result + ((priesgo == null) ? 0 : priesgo.hashCode());
		result = prime * result + ((psobremort == null) ? 0 : psobremort.hashCode());
		result = prime * result + ((tablacalc1aseg1 == null) ? 0 : tablacalc1aseg1.hashCode());
		result = prime * result + ((tempVit == null) ? 0 : tempVit.hashCode());
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
		FlujPMdCoa other = (FlujPMdCoa) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (cestadoAseg1 == null) {
			if (other.cestadoAseg1 != null)
				return false;
		} else if (!cestadoAseg1.equals(other.cestadoAseg1))
			return false;
		if (cestadoAseg2 == null) {
			if (other.cestadoAseg2 != null)
				return false;
		} else if (!cestadoAseg2.equals(other.cestadoAseg2))
			return false;
		if (cestadoAseg3 == null) {
			if (other.cestadoAseg3 != null)
				return false;
		} else if (!cestadoAseg3.equals(other.cestadoAseg3))
			return false;
		if (cestadoAseg4 == null) {
			if (other.cestadoAseg4 != null)
				return false;
		} else if (!cestadoAseg4.equals(other.cestadoAseg4))
			return false;
		if (cestadoAseg5 == null) {
			if (other.cestadoAseg5 != null)
				return false;
		} else if (!cestadoAseg5.equals(other.cestadoAseg5))
			return false;
		if (cformaRevrenta == null) {
			if (other.cformaRevrenta != null)
				return false;
		} else if (!cformaRevrenta.equals(other.cformaRevrenta))
			return false;
		if (cpagrenta == null) {
			if (other.cpagrenta != null)
				return false;
		} else if (!cpagrenta.equals(other.cpagrenta))
			return false;
		if (csexAseg1 == null) {
			if (other.csexAseg1 != null)
				return false;
		} else if (!csexAseg1.equals(other.csexAseg1))
			return false;
		if (csexAseg2 == null) {
			if (other.csexAseg2 != null)
				return false;
		} else if (!csexAseg2.equals(other.csexAseg2))
			return false;
		if (csexAseg3 == null) {
			if (other.csexAseg3 != null)
				return false;
		} else if (!csexAseg3.equals(other.csexAseg3))
			return false;
		if (csexAseg4 == null) {
			if (other.csexAseg4 != null)
				return false;
		} else if (!csexAseg4.equals(other.csexAseg4))
			return false;
		if (csexAseg5 == null) {
			if (other.csexAseg5 != null)
				return false;
		} else if (!csexAseg5.equals(other.csexAseg5))
			return false;
		if (ctipoRevrenta == null) {
			if (other.ctipoRevrenta != null)
				return false;
		} else if (!ctipoRevrenta.equals(other.ctipoRevrenta))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fdiadepago == null) {
			if (other.fdiadepago != null)
				return false;
		} else if (!fdiadepago.equals(other.fdiadepago))
			return false;
		if (fecFin == null) {
			if (other.fecFin != null)
				return false;
		} else if (!fecFin.equals(other.fecFin))
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
		if (fecIni == null) {
			if (other.fecIni != null)
				return false;
		} else if (!fecIni.equals(other.fecIni))
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
		if (fnacAseg1 == null) {
			if (other.fnacAseg1 != null)
				return false;
		} else if (!fnacAseg1.equals(other.fnacAseg1))
			return false;
		if (fnacAseg2 == null) {
			if (other.fnacAseg2 != null)
				return false;
		} else if (!fnacAseg2.equals(other.fnacAseg2))
			return false;
		if (fnacAseg3 == null) {
			if (other.fnacAseg3 != null)
				return false;
		} else if (!fnacAseg3.equals(other.fnacAseg3))
			return false;
		if (fnacAseg4 == null) {
			if (other.fnacAseg4 != null)
				return false;
		} else if (!fnacAseg4.equals(other.fnacAseg4))
			return false;
		if (fnacAseg5 == null) {
			if (other.fnacAseg5 != null)
				return false;
		} else if (!fnacAseg5.equals(other.fnacAseg5))
			return false;
		if (forpagrent == null) {
			if (other.forpagrent != null)
				return false;
		} else if (!forpagrent.equals(other.forpagrent))
			return false;
		if (gtorosspCap == null) {
			if (other.gtorosspCap != null)
				return false;
		} else if (!gtorosspCap.equals(other.gtorosspCap))
			return false;
		if (gtorosspPrima == null) {
			if (other.gtorosspPrima != null)
				return false;
		} else if (!gtorosspPrima.equals(other.gtorosspPrima))
			return false;
		if (gtorosspProv == null) {
			if (other.gtorosspProv != null)
				return false;
		} else if (!gtorosspProv.equals(other.gtorosspProv))
			return false;
		if (gvalor == null) {
			if (other.gvalor != null)
				return false;
		} else if (!gvalor.equals(other.gvalor))
			return false;
		if (icapact == null) {
			if (other.icapact != null)
				return false;
		} else if (!icapact.equals(other.icapact))
			return false;
		if (icapini == null) {
			if (other.icapini != null)
				return false;
		} else if (!icapini.equals(other.icapini))
			return false;
		if (iprimanetaact == null) {
			if (other.iprimanetaact != null)
				return false;
		} else if (!iprimanetaact.equals(other.iprimanetaact))
			return false;
		if (iprimanetaini == null) {
			if (other.iprimanetaini != null)
				return false;
		} else if (!iprimanetaini.equals(other.iprimanetaini))
			return false;
		if (iprimatarada == null) {
			if (other.iprimatarada != null)
				return false;
		} else if (!iprimatarada.equals(other.iprimatarada))
			return false;
		if (isaldo == null) {
			if (other.isaldo != null)
				return false;
		} else if (!isaldo.equals(other.isaldo))
			return false;
		if (itcalc == null) {
			if (other.itcalc != null)
				return false;
		} else if (!itcalc.equals(other.itcalc))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
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
		if (kgarantia == null) {
			if (other.kgarantia != null)
				return false;
		} else if (!kgarantia.equals(other.kgarantia))
			return false;
		if (kmodalidad == null) {
			if (other.kmodalidad != null)
				return false;
		} else if (!kmodalidad.equals(other.kmodalidad))
			return false;
		if (kpoliza == null) {
			if (other.kpoliza != null)
				return false;
		} else if (!kpoliza.equals(other.kpoliza))
			return false;
		if (kprestacion == null) {
			if (other.kprestacion != null)
				return false;
		} else if (!kprestacion.equals(other.kprestacion))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (nadifer == null) {
			if (other.nadifer != null)
				return false;
		} else if (!nadifer.equals(other.nadifer))
			return false;
		if (ndurrenta == null) {
			if (other.ndurrenta != null)
				return false;
		} else if (!ndurrenta.equals(other.ndurrenta))
			return false;
		if (npergaran == null) {
			if (other.npergaran != null)
				return false;
		} else if (!npergaran.equals(other.npergaran))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
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
		if (pgastgesin3I == null) {
			if (other.pgastgesin3I != null)
				return false;
		} else if (!pgastgesin3I.equals(other.pgastgesin3I))
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
		if (preversion == null) {
			if (other.preversion != null)
				return false;
		} else if (!preversion.equals(other.preversion))
			return false;
		if (prevrenta == null) {
			if (other.prevrenta != null)
				return false;
		} else if (!prevrenta.equals(other.prevrenta))
			return false;
		if (priesgo == null) {
			if (other.priesgo != null)
				return false;
		} else if (!priesgo.equals(other.priesgo))
			return false;
		if (psobremort == null) {
			if (other.psobremort != null)
				return false;
		} else if (!psobremort.equals(other.psobremort))
			return false;
		if (tablacalc1aseg1 == null) {
			if (other.tablacalc1aseg1 != null)
				return false;
		} else if (!tablacalc1aseg1.equals(other.tablacalc1aseg1))
			return false;
		if (tempVit == null) {
			if (other.tempVit != null)
				return false;
		} else if (!tempVit.equals(other.tempVit))
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
		return "FlujPMdCoa [kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia + ", kprestacion=" + kprestacion
				+ ", fcierre=" + fcierre + ", bt=" + bt + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", nsuscri=" + nsuscri + ", kcertificado=" + kcertificado + ", pintertecnI1=" + pintertecnI1
				+ ", pintertecnI2=" + pintertecnI2 + ", pintertecnI3=" + pintertecnI3 + ", pintertecnI4=" + pintertecnI4
				+ ", pintertecnI5=" + pintertecnI5 + ", fecFinTramo1=" + fecFinTramo1 + ", fecFinTramo2=" + fecFinTramo2
				+ ", fecFinTramo3=" + fecFinTramo3 + ", fecFinTramo4=" + fecFinTramo4 + ", fecFinTramo5=" + fecFinTramo5
				+ ", fecIniTramo1=" + fecIniTramo1 + ", fecIniTramo2=" + fecIniTramo2 + ", fecIniTramo3=" + fecIniTramo3
				+ ", fecIniTramo4=" + fecIniTramo4 + ", fecIniTramo5=" + fecIniTramo5 + ", kcoase1=" + kcoase1
				+ ", kcoase2=" + kcoase2 + ", kcoase3=" + kcoase3 + ", kcoase4=" + kcoase4 + ", kcoase5=" + kcoase5
				+ ", kcoase6=" + kcoase6 + ", fecefecini=" + fecefecini + ", fecefecfin=" + fecefecfin
				+ ", tablacalc1aseg1=" + tablacalc1aseg1 + ", itcalc=" + itcalc + ", totprovision=" + totprovision
				+ ", gtorosspCap=" + gtorosspCap + ", gtorosspPrima=" + gtorosspPrima + ", gtorosspProv=" + gtorosspProv
				+ ", pgastgesin1I=" + pgastgesin1I + ", pgastgesin2I=" + pgastgesin2I + ", pgastgesin3I=" + pgastgesin3I
				+ ", csexAseg1=" + csexAseg1 + ", csexAseg2=" + csexAseg2 + ", csexAseg3=" + csexAseg3 + ", csexAseg4="
				+ csexAseg4 + ", csexAseg5=" + csexAseg5 + ", cestadoAseg1=" + cestadoAseg1 + ", cestadoAseg2="
				+ cestadoAseg2 + ", cestadoAseg3=" + cestadoAseg3 + ", cestadoAseg4=" + cestadoAseg4 + ", cestadoAseg5="
				+ cestadoAseg5 + ", fnacAseg1=" + fnacAseg1 + ", fnacAseg2=" + fnacAseg2 + ", fnacAseg3=" + fnacAseg3
				+ ", fnacAseg4=" + fnacAseg4 + ", fnacAseg5=" + fnacAseg5 + ", iprimanetaact=" + iprimanetaact
				+ ", iprimanetaini=" + iprimanetaini + ", iprimatarada=" + iprimatarada + ", icapact=" + icapact
				+ ", icapini=" + icapini + ", isaldo=" + isaldo + ", fecFin=" + fecFin + ", fecIni=" + fecIni
				+ ", tempVit=" + tempVit + ", prevrenta=" + prevrenta + ", preversion=" + preversion + ", npergaran="
				+ npergaran + ", nadifer=" + nadifer + ", forpagrent=" + forpagrent + ", cpagrenta=" + cpagrenta
				+ ", ctipoRevrenta=" + ctipoRevrenta + ", cformaRevrenta=" + cformaRevrenta + ", ndurrenta=" + ndurrenta
				+ ", kajuste=" + kajuste + ", psobremort=" + psobremort + ", priesgo=" + priesgo + ", gvalor=" + gvalor
				+ ", fdiadepago=" + fdiadepago + "]";
	}

	@Override
	public FlujPMdCoaKey getKey() {
		return new FlujPMdCoaKey(kmodalidad, kgarantia, kprestacion, kpoliza, ksubpoliza, kcertificado, nsuscri, bt, fcierre);
	}

}