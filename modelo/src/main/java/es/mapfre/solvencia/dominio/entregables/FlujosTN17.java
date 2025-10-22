package es.mapfre.solvencia.dominio.entregables;

import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTN17Key;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class FlujosTN17 implements EntidadBase<FlujosTN17Key>, EntidadConBaseTec {

	public static final int IND_CNEGOCIO = 0;
	public static final int IND_CCANAL = 1;
	public static final int IND_CCARTERA = 2;
	public static final int IND_FCIERRE = 3;
	public static final int IND_BT = 4;
	public static final int IND_UOA = 5;
	public static final int IND_KCONTRATO = 6;
	public static final int IND_KRAMO = 7;
	public static final int IND_KMODALIDAD = 8;
	public static final int IND_KPOLIZA = 9;
	public static final int IND_KSUBPOLIZA = 10;
	public static final int IND_KCERTIFICADO = 11;
	public static final int IND_NSUSCRI = 12;
	public static final int IND_NORDEN = 13;
	public static final int IND_KGARANTIA = 14;
	public static final int IND_KPRESTACION = 15;
	public static final int IND_KAJUSTE = 16;
	public static final int IND_CTIPOAPORT = 17;
	public static final int IND_INTFECCAL = 18;
	public static final int IND_FSUSCRI = 19;
	public static final int IND_KCARTERAINV = 20;
	public static final int IND_GAPACT = 21;
	public static final int IND_KMODEXT = 22;
	public static final int IND_SPCOM = 23;
	public static final int IND_KBENCON = 24;
	public static final int IND_SIG_TOTFPVIDA = 25;
	public static final int IND_TOTFPVIDA = 26;
	public static final int IND_SIG_TOTFPNAVIDA = 27;
	public static final int IND_TOTFPNAVIDA = 28;
	public static final int IND_SIG_TOTFACTVIDA = 29;
	public static final int IND_TOTFACTVIDA = 30;
	public static final int IND_SIG_TOTCOLAVIDA = 31;
	public static final int IND_TOTCOLAVIDA = 32;
	public static final int IND_SIG_TOTFPFALL = 33;
	public static final int IND_TOTFPFALL = 34;
	public static final int IND_SIG_TOTFPNAFALL = 35;
	public static final int IND_TOTFPNAFALL = 36;
	public static final int IND_SIG_TOTFACTFALL = 37;
	public static final int IND_TOTFACTFALL = 38;
	public static final int IND_SIG_TOTCOLAFALL = 39;
	public static final int IND_TOTCOLAFALL = 40;
	public static final int IND_SIG_TOTFPCOMPL = 41;
	public static final int IND_TOTFPCOMPL = 42;
	public static final int IND_SIG_TOTFPNACOMPL = 43;
	public static final int IND_TOTFPNACOMPL = 44;
	public static final int IND_SIG_TOTFACTCOMPL = 45;
	public static final int IND_TOTFACTCOMPL = 46;
	public static final int IND_SIG_TOTCOLACOMPL = 47;
	public static final int IND_TOTCOLACOMPL = 48;
	public static final int IND_SIG_TOTFPGTO = 49;
	public static final int IND_TOTFPGTO = 50;
	public static final int IND_SIG_TOTFPNAGTO = 51;
	public static final int IND_TOTFPNAGTO = 52;
	public static final int IND_SIG_TOTFACTGTO = 53;
	public static final int IND_TOTFACTGTO = 54;
	public static final int IND_SIG_TOTCOLAGTO = 55;
	public static final int IND_TOTCOLAGTO = 56;
	public static final int IND_SIG_TOTFPCOM = 57;
	public static final int IND_TOTFPCOM = 58;
	public static final int IND_SIG_TOTFPNACOM = 59;
	public static final int IND_TOTFPNACOM = 60;
	public static final int IND_SIG_TOTFACTCOM = 61;
	public static final int IND_TOTFACTCOM = 62;
	public static final int IND_SIG_TOTCOLACOM = 63;
	public static final int IND_TOTCOLACOM = 64;
	public static final int IND_SIG_TOTFPRTE = 65;
	public static final int IND_TOTFPRTE = 66;
	public static final int IND_SIG_TOTFPNARTE = 67;
	public static final int IND_TOTFPNARTE = 68;
	public static final int IND_SIG_TOTFACTRTE = 69;
	public static final int IND_TOTFACTRTE = 70;
	public static final int IND_SIG_TOTCOLARTE = 71;
	public static final int IND_TOTCOLARTE = 72;
	public static final int IND_SIG_TOTFPPRIM = 73;
	public static final int IND_TOTFPPRIM = 74;
	public static final int IND_SIG_TOTFPNAPRIM = 75;
	public static final int IND_TOTFPNAPRIM = 76;
	public static final int IND_SIG_TOTFACTPRIM = 77;
	public static final int IND_TOTFACTPRIM = 78;
	public static final int IND_SIG_TOTCOLAPRIM = 79;
	public static final int IND_TOTCOLAPRIM = 80;
	public static final int IND_SIG_TOTFPROB = 81;
	public static final int IND_TOTFPROB = 82;
	public static final int IND_SIG_TOTFPROBTANUL = 83;
	public static final int IND_TOTFPROBTANUL = 84;
	public static final int IND_SIG_TOTPROVISION = 85;
	public static final int IND_TOTPROVISION = 86;
	public static final int IND_SIG_TOTCOLA = 87;
	public static final int IND_TOTCOLA = 88;
	public static final int IND_SIG_PROVFCAL = 89;
	public static final int IND_PROVFCAL = 90;
	public static final int IND_SIG_TOTRA = 91;
	public static final int IND_TOTRA = 92;
	public static final int IND_SIG_TOTCSM = 93;
	public static final int IND_TOTCSM = 94;
	public static final int IND_SIG_TOTCSMPATRON = 95;
	public static final int IND_TOTCSMPATRON = 96;
	public static final int IND_SIG_TOTCSMROSSP = 97;
	public static final int IND_TOTCSMROSSP = 98;
	
	public static final int IND_SIG_TOTCOLAGTOAD = 99;
	public static final int IND_TOTCOLAGTOAD = 100;
	public static final int IND_SIG_TOTFACTGTOAD = 101;
	public static final int IND_TOTFACTGTOAD = 102;
	public static final int IND_SIG_TOTFPGTOAD = 103;
	public static final int IND_TOTFPGTOAD = 104;
	public static final int IND_SIG_TOTFPNAGTOAD = 105;
	public static final int IND_TOTFPNAGTOAD = 106;

	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_KCONTRATO)
	private String kcontrato;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_NORDEN)
	private Integer norden;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(value = IND_INTFECCAL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal intfeccal;
	@PortableProperty(IND_FSUSCRI)
	private Timestamp fsuscri;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(IND_SPCOM)
	private String spcom;
	@PortableProperty(IND_KBENCON)
	private String kbencon;
	@PortableProperty(IND_SIG_TOTFPVIDA)
	private String sigTotfpvida;
	@PortableProperty(value = IND_TOTFPVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpvida;
	@PortableProperty(IND_SIG_TOTFPNAVIDA)
	private String sigTotfpnavida;
	@PortableProperty(value = IND_TOTFPNAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnavida;
	@PortableProperty(IND_SIG_TOTFACTVIDA)
	private String sigTotfactvida;
	@PortableProperty(value = IND_TOTFACTVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactvida;
	@PortableProperty(IND_SIG_TOTCOLAVIDA)
	private String sigTotcolavida;
	@PortableProperty(value = IND_TOTCOLAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolavida;
	@PortableProperty(IND_SIG_TOTFPFALL)
	private String sigTotfpfall;
	@PortableProperty(value = IND_TOTFPFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpfall;
	@PortableProperty(IND_SIG_TOTFPNAFALL)
	private String sigTotfpnafall;
	@PortableProperty(value = IND_TOTFPNAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnafall;
	@PortableProperty(IND_SIG_TOTFACTFALL)
	private String sigTotfactfall;
	@PortableProperty(value = IND_TOTFACTFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactfall;
	@PortableProperty(IND_SIG_TOTCOLAFALL)
	private String sigTotcolafall;
	@PortableProperty(value = IND_TOTCOLAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolafall;
	@PortableProperty(IND_SIG_TOTFPCOMPL)
	private String sigTotfpcompl;
	@PortableProperty(value = IND_TOTFPCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcompl;
	@PortableProperty(IND_SIG_TOTFPNACOMPL)
	private String sigTotfpnacompl;
	@PortableProperty(value = IND_TOTFPNACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacompl;
	@PortableProperty(IND_SIG_TOTFACTCOMPL)
	private String sigTotfactcompl;
	@PortableProperty(value = IND_TOTFACTCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcompl;
	@PortableProperty(IND_SIG_TOTCOLACOMPL)
	private String sigTotcolacompl;
	@PortableProperty(value = IND_TOTCOLACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacompl;
	@PortableProperty(IND_SIG_TOTFPGTO)
	private String sigTotfpgto;
	@PortableProperty(value = IND_TOTFPGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgto;
	@PortableProperty(IND_SIG_TOTFPNAGTO)
	private String sigTotfpnagto;
	@PortableProperty(value = IND_TOTFPNAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagto;
	@PortableProperty(IND_SIG_TOTFACTGTO)
	private String sigTotfactgto;
	@PortableProperty(value = IND_TOTFACTGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgto;
	@PortableProperty(IND_SIG_TOTCOLAGTO)
	private String sigTotcolagto;
	@PortableProperty(value = IND_TOTCOLAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagto;
	@PortableProperty(IND_SIG_TOTFPCOM)
	private String sigTotfpcom;
	@PortableProperty(value = IND_TOTFPCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcom;
	@PortableProperty(IND_SIG_TOTFPNACOM)
	private String sigTotfpnacom;
	@PortableProperty(value = IND_TOTFPNACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacom;
	@PortableProperty(IND_SIG_TOTFACTCOM)
	private String sigTotfactcom;
	@PortableProperty(value = IND_TOTFACTCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcom;
	@PortableProperty(IND_SIG_TOTCOLACOM)
	private String sigTotcolacom;
	@PortableProperty(value = IND_TOTCOLACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacom;
	@PortableProperty(IND_SIG_TOTFPRTE)
	private String sigTotfprte;
	@PortableProperty(value = IND_TOTFPRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprte;
	@PortableProperty(IND_SIG_TOTFPNARTE)
	private String sigTotfpnarte;
	@PortableProperty(value = IND_TOTFPNARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnarte;
	@PortableProperty(IND_SIG_TOTFACTRTE)
	private String sigTotfactrte;
	@PortableProperty(value = IND_TOTFACTRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactrte;
	@PortableProperty(IND_SIG_TOTCOLARTE)
	private String sigTotcolarte;
	@PortableProperty(value = IND_TOTCOLARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolarte;
	@PortableProperty(IND_SIG_TOTFPPRIM)
	private String sigTotfpprim;
	@PortableProperty(value = IND_TOTFPPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpprim;
	@PortableProperty(IND_SIG_TOTFPNAPRIM)
	private String sigTotfpnaprim;
	@PortableProperty(value = IND_TOTFPNAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnaprim;
	@PortableProperty(IND_SIG_TOTFACTPRIM)
	private String sigTotfactprim;
	@PortableProperty(value = IND_TOTFACTPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactprim;
	@PortableProperty(IND_SIG_TOTCOLAPRIM)
	private String sigTotcolaprim;
	@PortableProperty(value = IND_TOTCOLAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolaprim;
	@PortableProperty(IND_SIG_TOTFPROB)
	private String sigTotfprob;
	@PortableProperty(value = IND_TOTFPROB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprob;
	@PortableProperty(IND_SIG_TOTFPROBTANUL)
	private String sigTotfprobtanul;
	@PortableProperty(value = IND_TOTFPROBTANUL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprobtanul;
	@PortableProperty(IND_SIG_TOTPROVISION)
	private String sigTotprovision;
	@PortableProperty(value = IND_TOTPROVISION, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totprovision;
	@PortableProperty(IND_SIG_TOTCOLA)
	private String sigTotcola;
	@PortableProperty(value = IND_TOTCOLA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcola;
	@PortableProperty(IND_SIG_PROVFCAL)
	private String sigProvfcal;
	@PortableProperty(value = IND_PROVFCAL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provfcal;
	@PortableProperty(IND_SIG_TOTRA)
	private String sigTotra;
	@PortableProperty(value = IND_TOTRA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totra;
	@PortableProperty(IND_SIG_TOTCSM)
	private String sigTotcsm;
	@PortableProperty(value = IND_TOTCSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcsm;
	@PortableProperty(IND_SIG_TOTCSMPATRON)
	private String sigTotcsmpatron;
	@PortableProperty(value = IND_TOTCSMPATRON, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcsmpatron;
	@PortableProperty(IND_SIG_TOTCSMROSSP)
	private String sigTotcsmrossp;
	@PortableProperty(value = IND_TOTCSMROSSP, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcsmrossp;
	
	@PortableProperty(IND_SIG_TOTFPGTOAD)
	private String sigTotfpgtoad;
	@PortableProperty(value = IND_TOTFPGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgtoad;
	@PortableProperty(IND_SIG_TOTFPNAGTOAD)
	private String sigTotfpnagtoad;
	@PortableProperty(value = IND_TOTFPNAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagtoad;
	@PortableProperty(IND_SIG_TOTFACTGTOAD)
	private String sigTotfactgtoad;
	@PortableProperty(value = IND_TOTFACTGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgtoad;
	@PortableProperty(IND_SIG_TOTCOLAGTOAD)
	private String sigTotcolagtoad;
	@PortableProperty(value = IND_TOTCOLAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagtoad;
	
	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public Integer getCcanal() {
		return ccanal;
	}

	public void setCcanal(Integer ccanal) {
		this.ccanal = ccanal;
	}

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
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

	public String getUoa() {
		return uoa;
	}

	public void setUoa(String uoa) {
		this.uoa = uoa;
	}

	public String getKcontrato() {
		return kcontrato;
	}

	public void setKcontrato(String kcontrato) {
		this.kcontrato = kcontrato;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public Integer getKmodalidad() {
		return kmodalidad;
	}

	public void setKmodalidad(Integer kmodalidad) {
		this.kmodalidad = kmodalidad;
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

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
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

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	public java.math.BigDecimal getIntfeccal() {
		return intfeccal;
	}

	public void setIntfeccal(java.math.BigDecimal intfeccal) {
		this.intfeccal = intfeccal;
	}

	public Timestamp getFsuscri() {
		return fsuscri;
	}

	public void setFsuscri(Timestamp fsuscri) {
		this.fsuscri = fsuscri;
	}

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

	public String getGapact() {
		return gapact;
	}

	public void setGapact(String gapact) {
		this.gapact = gapact;
	}

	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}

	public String getKbencon() {
		return kbencon;
	}

	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}

	public String getSigTotfpvida() {
		return sigTotfpvida;
	}

	public void setSigTotfpvida(String sigTotfpvida) {
		this.sigTotfpvida = sigTotfpvida;
	}

	public java.math.BigDecimal getTotfpvida() {
		return totfpvida;
	}

	public void setTotfpvida(java.math.BigDecimal totfpvida) {
		this.totfpvida = totfpvida;
	}

	public String getSigTotfpnavida() {
		return sigTotfpnavida;
	}

	public void setSigTotfpnavida(String sigTotfpnavida) {
		this.sigTotfpnavida = sigTotfpnavida;
	}

	public java.math.BigDecimal getTotfpnavida() {
		return totfpnavida;
	}

	public void setTotfpnavida(java.math.BigDecimal totfpnavida) {
		this.totfpnavida = totfpnavida;
	}

	public String getSigTotfactvida() {
		return sigTotfactvida;
	}

	public void setSigTotfactvida(String sigTotfactvida) {
		this.sigTotfactvida = sigTotfactvida;
	}

	public java.math.BigDecimal getTotfactvida() {
		return totfactvida;
	}

	public void setTotfactvida(java.math.BigDecimal totfactvida) {
		this.totfactvida = totfactvida;
	}

	public String getSigTotcolavida() {
		return sigTotcolavida;
	}

	public void setSigTotcolavida(String sigTotcolavida) {
		this.sigTotcolavida = sigTotcolavida;
	}

	public java.math.BigDecimal getTotcolavida() {
		return totcolavida;
	}

	public void setTotcolavida(java.math.BigDecimal totcolavida) {
		this.totcolavida = totcolavida;
	}

	public String getSigTotfpfall() {
		return sigTotfpfall;
	}

	public void setSigTotfpfall(String sigTotfpfall) {
		this.sigTotfpfall = sigTotfpfall;
	}

	public java.math.BigDecimal getTotfpfall() {
		return totfpfall;
	}

	public void setTotfpfall(java.math.BigDecimal totfpfall) {
		this.totfpfall = totfpfall;
	}

	public String getSigTotfpnafall() {
		return sigTotfpnafall;
	}

	public void setSigTotfpnafall(String sigTotfpnafall) {
		this.sigTotfpnafall = sigTotfpnafall;
	}

	public java.math.BigDecimal getTotfpnafall() {
		return totfpnafall;
	}

	public void setTotfpnafall(java.math.BigDecimal totfpnafall) {
		this.totfpnafall = totfpnafall;
	}

	public String getSigTotfactfall() {
		return sigTotfactfall;
	}

	public void setSigTotfactfall(String sigTotfactfall) {
		this.sigTotfactfall = sigTotfactfall;
	}

	public java.math.BigDecimal getTotfactfall() {
		return totfactfall;
	}

	public void setTotfactfall(java.math.BigDecimal totfactfall) {
		this.totfactfall = totfactfall;
	}

	public String getSigTotcolafall() {
		return sigTotcolafall;
	}

	public void setSigTotcolafall(String sigTotcolafall) {
		this.sigTotcolafall = sigTotcolafall;
	}

	public java.math.BigDecimal getTotcolafall() {
		return totcolafall;
	}

	public void setTotcolafall(java.math.BigDecimal totcolafall) {
		this.totcolafall = totcolafall;
	}

	public String getSigTotfpcompl() {
		return sigTotfpcompl;
	}

	public void setSigTotfpcompl(String sigTotfpcompl) {
		this.sigTotfpcompl = sigTotfpcompl;
	}

	public java.math.BigDecimal getTotfpcompl() {
		return totfpcompl;
	}

	public void setTotfpcompl(java.math.BigDecimal totfpcompl) {
		this.totfpcompl = totfpcompl;
	}

	public String getSigTotfpnacompl() {
		return sigTotfpnacompl;
	}

	public void setSigTotfpnacompl(String sigTotfpnacompl) {
		this.sigTotfpnacompl = sigTotfpnacompl;
	}

	public java.math.BigDecimal getTotfpnacompl() {
		return totfpnacompl;
	}

	public void setTotfpnacompl(java.math.BigDecimal totfpnacompl) {
		this.totfpnacompl = totfpnacompl;
	}

	public String getSigTotfactcompl() {
		return sigTotfactcompl;
	}

	public void setSigTotfactcompl(String sigTotfactcompl) {
		this.sigTotfactcompl = sigTotfactcompl;
	}

	public java.math.BigDecimal getTotfactcompl() {
		return totfactcompl;
	}

	public void setTotfactcompl(java.math.BigDecimal totfactcompl) {
		this.totfactcompl = totfactcompl;
	}

	public String getSigTotcolacompl() {
		return sigTotcolacompl;
	}

	public void setSigTotcolacompl(String sigTotcolacompl) {
		this.sigTotcolacompl = sigTotcolacompl;
	}

	public java.math.BigDecimal getTotcolacompl() {
		return totcolacompl;
	}

	public void setTotcolacompl(java.math.BigDecimal totcolacompl) {
		this.totcolacompl = totcolacompl;
	}

	public String getSigTotfpgto() {
		return sigTotfpgto;
	}

	public void setSigTotfpgto(String sigTotfpgto) {
		this.sigTotfpgto = sigTotfpgto;
	}

	public java.math.BigDecimal getTotfpgto() {
		return totfpgto;
	}

	public void setTotfpgto(java.math.BigDecimal totfpgto) {
		this.totfpgto = totfpgto;
	}

	public String getSigTotfpnagto() {
		return sigTotfpnagto;
	}

	public void setSigTotfpnagto(String sigTotfpnagto) {
		this.sigTotfpnagto = sigTotfpnagto;
	}

	public java.math.BigDecimal getTotfpnagto() {
		return totfpnagto;
	}

	public void setTotfpnagto(java.math.BigDecimal totfpnagto) {
		this.totfpnagto = totfpnagto;
	}

	public String getSigTotfactgto() {
		return sigTotfactgto;
	}

	public void setSigTotfactgto(String sigTotfactgto) {
		this.sigTotfactgto = sigTotfactgto;
	}

	public java.math.BigDecimal getTotfactgto() {
		return totfactgto;
	}

	public void setTotfactgto(java.math.BigDecimal totfactgto) {
		this.totfactgto = totfactgto;
	}

	public String getSigTotcolagto() {
		return sigTotcolagto;
	}

	public void setSigTotcolagto(String sigTotcolagto) {
		this.sigTotcolagto = sigTotcolagto;
	}

	public java.math.BigDecimal getTotcolagto() {
		return totcolagto;
	}

	public void setTotcolagto(java.math.BigDecimal totcolagto) {
		this.totcolagto = totcolagto;
	}

	public String getSigTotfpcom() {
		return sigTotfpcom;
	}

	public void setSigTotfpcom(String sigTotfpcom) {
		this.sigTotfpcom = sigTotfpcom;
	}

	public java.math.BigDecimal getTotfpcom() {
		return totfpcom;
	}

	public void setTotfpcom(java.math.BigDecimal totfpcom) {
		this.totfpcom = totfpcom;
	}

	public String getSigTotfpnacom() {
		return sigTotfpnacom;
	}

	public void setSigTotfpnacom(String sigTotfpnacom) {
		this.sigTotfpnacom = sigTotfpnacom;
	}

	public java.math.BigDecimal getTotfpnacom() {
		return totfpnacom;
	}

	public void setTotfpnacom(java.math.BigDecimal totfpnacom) {
		this.totfpnacom = totfpnacom;
	}

	public String getSigTotfactcom() {
		return sigTotfactcom;
	}

	public void setSigTotfactcom(String sigTotfactcom) {
		this.sigTotfactcom = sigTotfactcom;
	}

	public java.math.BigDecimal getTotfactcom() {
		return totfactcom;
	}

	public void setTotfactcom(java.math.BigDecimal totfactcom) {
		this.totfactcom = totfactcom;
	}

	public String getSigTotcolacom() {
		return sigTotcolacom;
	}

	public void setSigTotcolacom(String sigTotcolacom) {
		this.sigTotcolacom = sigTotcolacom;
	}

	public java.math.BigDecimal getTotcolacom() {
		return totcolacom;
	}

	public void setTotcolacom(java.math.BigDecimal totcolacom) {
		this.totcolacom = totcolacom;
	}

	public String getSigTotfprte() {
		return sigTotfprte;
	}

	public void setSigTotfprte(String sigTotfprte) {
		this.sigTotfprte = sigTotfprte;
	}

	public java.math.BigDecimal getTotfprte() {
		return totfprte;
	}

	public void setTotfprte(java.math.BigDecimal totfprte) {
		this.totfprte = totfprte;
	}

	public String getSigTotfpnarte() {
		return sigTotfpnarte;
	}

	public void setSigTotfpnarte(String sigTotfpnarte) {
		this.sigTotfpnarte = sigTotfpnarte;
	}

	public java.math.BigDecimal getTotfpnarte() {
		return totfpnarte;
	}

	public void setTotfpnarte(java.math.BigDecimal totfpnarte) {
		this.totfpnarte = totfpnarte;
	}

	public String getSigTotfactrte() {
		return sigTotfactrte;
	}

	public void setSigTotfactrte(String sigTotfactrte) {
		this.sigTotfactrte = sigTotfactrte;
	}

	public java.math.BigDecimal getTotfactrte() {
		return totfactrte;
	}

	public void setTotfactrte(java.math.BigDecimal totfactrte) {
		this.totfactrte = totfactrte;
	}

	public String getSigTotcolarte() {
		return sigTotcolarte;
	}

	public void setSigTotcolarte(String sigTotcolarte) {
		this.sigTotcolarte = sigTotcolarte;
	}

	public java.math.BigDecimal getTotcolarte() {
		return totcolarte;
	}

	public void setTotcolarte(java.math.BigDecimal totcolarte) {
		this.totcolarte = totcolarte;
	}

	public String getSigTotfpprim() {
		return sigTotfpprim;
	}

	public void setSigTotfpprim(String sigTotfpprim) {
		this.sigTotfpprim = sigTotfpprim;
	}

	public java.math.BigDecimal getTotfpprim() {
		return totfpprim;
	}

	public void setTotfpprim(java.math.BigDecimal totfpprim) {
		this.totfpprim = totfpprim;
	}

	public String getSigTotfpnaprim() {
		return sigTotfpnaprim;
	}

	public void setSigTotfpnaprim(String sigTotfpnaprim) {
		this.sigTotfpnaprim = sigTotfpnaprim;
	}

	public java.math.BigDecimal getTotfpnaprim() {
		return totfpnaprim;
	}

	public void setTotfpnaprim(java.math.BigDecimal totfpnaprim) {
		this.totfpnaprim = totfpnaprim;
	}

	public String getSigTotfactprim() {
		return sigTotfactprim;
	}

	public void setSigTotfactprim(String sigTotfactprim) {
		this.sigTotfactprim = sigTotfactprim;
	}

	public java.math.BigDecimal getTotfactprim() {
		return totfactprim;
	}

	public void setTotfactprim(java.math.BigDecimal totfactprim) {
		this.totfactprim = totfactprim;
	}

	public String getSigTotcolaprim() {
		return sigTotcolaprim;
	}

	public void setSigTotcolaprim(String sigTotcolaprim) {
		this.sigTotcolaprim = sigTotcolaprim;
	}

	public java.math.BigDecimal getTotcolaprim() {
		return totcolaprim;
	}

	public void setTotcolaprim(java.math.BigDecimal totcolaprim) {
		this.totcolaprim = totcolaprim;
	}

	public String getSigTotfprob() {
		return sigTotfprob;
	}

	public void setSigTotfprob(String sigTotfprob) {
		this.sigTotfprob = sigTotfprob;
	}

	public java.math.BigDecimal getTotfprob() {
		return totfprob;
	}

	public void setTotfprob(java.math.BigDecimal totfprob) {
		this.totfprob = totfprob;
	}

	public String getSigTotfprobtanul() {
		return sigTotfprobtanul;
	}

	public void setSigTotfprobtanul(String sigTotfprobtanul) {
		this.sigTotfprobtanul = sigTotfprobtanul;
	}

	public java.math.BigDecimal getTotfprobtanul() {
		return totfprobtanul;
	}

	public void setTotfprobtanul(java.math.BigDecimal totfprobtanul) {
		this.totfprobtanul = totfprobtanul;
	}

	public String getSigTotprovision() {
		return sigTotprovision;
	}

	public void setSigTotprovision(String sigTotprovision) {
		this.sigTotprovision = sigTotprovision;
	}

	public java.math.BigDecimal getTotprovision() {
		return totprovision;
	}

	public void setTotprovision(java.math.BigDecimal totprovision) {
		this.totprovision = totprovision;
	}

	public String getSigTotcola() {
		return sigTotcola;
	}

	public void setSigTotcola(String sigTotcola) {
		this.sigTotcola = sigTotcola;
	}

	public java.math.BigDecimal getTotcola() {
		return totcola;
	}

	public void setTotcola(java.math.BigDecimal totcola) {
		this.totcola = totcola;
	}

	public String getSigProvfcal() {
		return sigProvfcal;
	}

	public void setSigProvfcal(String sigProvfcal) {
		this.sigProvfcal = sigProvfcal;
	}

	public java.math.BigDecimal getProvfcal() {
		return provfcal;
	}

	public void setProvfcal(java.math.BigDecimal provfcal) {
		this.provfcal = provfcal;
	}

	public String getSigTotra() {
		return sigTotra;
	}

	public void setSigTotra(String sigTotra) {
		this.sigTotra = sigTotra;
	}

	public java.math.BigDecimal getTotra() {
		return totra;
	}

	public void setTotra(java.math.BigDecimal totra) {
		this.totra = totra;
	}

	public String getSigTotcsm() {
		return sigTotcsm;
	}

	public void setSigTotcsm(String sigTotcsm) {
		this.sigTotcsm = sigTotcsm;
	}

	public java.math.BigDecimal getTotcsm() {
		return totcsm;
	}

	public void setTotcsm(java.math.BigDecimal totcsm) {
		this.totcsm = totcsm;
	}

	public String getSigTotcsmpatron() {
		return sigTotcsmpatron;
	}

	public void setSigTotcsmpatron(String sigTotcsmpatron) {
		this.sigTotcsmpatron = sigTotcsmpatron;
	}

	public java.math.BigDecimal getTotcsmpatron() {
		return totcsmpatron;
	}

	public void setTotcsmpatron(java.math.BigDecimal totcsmpatron) {
		this.totcsmpatron = totcsmpatron;
	}
	public String getSigTotcsmrossp() {
		return sigTotcsmrossp;
	}

	public void setSigTotcsmrossp(String sigTotcsmrossp) {
		this.sigTotcsmrossp = sigTotcsmrossp;
	}

	public java.math.BigDecimal getTotcsmrossp() {
		return totcsmrossp;
	}

	public void setTotcsmrossp(java.math.BigDecimal totcsmrossp) {
		this.totcsmrossp = totcsmrossp;
	}
	public static int getIndCnegocio() {
		return IND_CNEGOCIO;
	}

	public static int getIndCcanal() {
		return IND_CCANAL;
	}

	public static int getIndCcartera() {
		return IND_CCARTERA;
	}

	public static int getIndFcierre() {
		return IND_FCIERRE;
	}

	public static int getIndBt() {
		return IND_BT;
	}

	public static int getIndKcontrato() {
		return IND_KCONTRATO;
	}

	public static int getIndKramo() {
		return IND_KRAMO;
	}

	public static int getIndKmodalidad() {
		return IND_KMODALIDAD;
	}

	public static int getIndKpoliza() {
		return IND_KPOLIZA;
	}

	public static int getIndKsubpoliza() {
		return IND_KSUBPOLIZA;
	}

	public static int getIndKcertificado() {
		return IND_KCERTIFICADO;
	}

	public static int getIndNsuscri() {
		return IND_NSUSCRI;
	}

	public static int getIndNorden() {
		return IND_NORDEN;
	}

	public static int getIndKgarantia() {
		return IND_KGARANTIA;
	}

	public static int getIndKprestacion() {
		return IND_KPRESTACION;
	}

	public static int getIndKajuste() {
		return IND_KAJUSTE;
	}

	public static int getIndCtipoaport() {
		return IND_CTIPOAPORT;
	}

	public static int getIndIntfeccal() {
		return IND_INTFECCAL;
	}

	public static int getIndFsuscri() {
		return IND_FSUSCRI;
	}

	public static int getIndKcarterainv() {
		return IND_KCARTERAINV;
	}

	public static int getIndGapact() {
		return IND_GAPACT;
	}

	public static int getIndKmodext() {
		return IND_KMODEXT;
	}

	public static int getIndSpcom() {
		return IND_SPCOM;
	}

	public static int getIndKbencon() {
		return IND_KBENCON;
	}

	public static int getIndSigTotfpvida() {
		return IND_SIG_TOTFPVIDA;
	}

	public static int getIndSigTotfpnavida() {
		return IND_SIG_TOTFPNAVIDA;
	}

	public static int getIndSigTotfactvida() {
		return IND_SIG_TOTFACTVIDA;
	}

	public static int getIndSigTotcolavida() {
		return IND_SIG_TOTCOLAVIDA;
	}

	public static int getIndSigTotfpfall() {
		return IND_SIG_TOTFPFALL;
	}

	public static int getIndSigTotfpnafall() {
		return IND_SIG_TOTFPNAFALL;
	}

	public static int getIndSigTotfactfall() {
		return IND_SIG_TOTFACTFALL;
	}

	public static int getIndSigTotcolafall() {
		return IND_SIG_TOTCOLAFALL;
	}

	public static int getIndTotcolafall() {
		return IND_TOTCOLAFALL;
	}

	public static int getIndSigTotfpcompl() {
		return IND_SIG_TOTFPCOMPL;
	}

	public static int getIndSigTotfpnacompl() {
		return IND_SIG_TOTFPNACOMPL;
	}

	public static int getIndSigTotfactcompl() {
		return IND_SIG_TOTFACTCOMPL;
	}

	public static int getIndSigTotcolacompl() {
		return IND_SIG_TOTCOLACOMPL;
	}

	public static int getIndTotcolacompl() {
		return IND_TOTCOLACOMPL;
	}

	public static int getIndSigTotfpgto() {
		return IND_SIG_TOTFPGTO;
	}

	public static int getIndSigTotfpnagto() {
		return IND_SIG_TOTFPNAGTO;
	}

	public static int getIndSigTotfactgto() {
		return IND_SIG_TOTFACTGTO;
	}

	public static int getIndSigTotcolagto() {
		return IND_SIG_TOTCOLAGTO;
	}

	public static int getIndTotcolagto() {
		return IND_TOTCOLAGTO;
	}

	public static int getIndSigTotfpcom() {
		return IND_SIG_TOTFPCOM;
	}

	public static int getIndSigTotfpnacom() {
		return IND_SIG_TOTFPNACOM;
	}

	public static int getIndSigTotfactcom() {
		return IND_SIG_TOTFACTCOM;
	}

	public static int getIndSigTotcolacom() {
		return IND_SIG_TOTCOLACOM;
	}

	public static int getIndTotcolacom() {
		return IND_TOTCOLACOM;
	}

	public static int getIndSigTotfprte() {
		return IND_SIG_TOTFPRTE;
	}

	public static int getIndSigTotfpnarte() {
		return IND_SIG_TOTFPNARTE;
	}

	public static int getIndSigTotfactrte() {
		return IND_SIG_TOTFACTRTE;
	}

	public static int getIndSigTotcolarte() {
		return IND_SIG_TOTCOLARTE;
	}

	public static int getIndSigTotfpprim() {
		return IND_SIG_TOTFPPRIM;
	}

	public static int getIndSigTotfpnaprim() {
		return IND_SIG_TOTFPNAPRIM;
	}

	public static int getIndSigTotfactprim() {
		return IND_SIG_TOTFACTPRIM;
	}

	public static int getIndSigTotcolaprim() {
		return IND_SIG_TOTCOLAPRIM;
	}

	public static int getIndTotcolaprim() {
		return IND_TOTCOLAPRIM;
	}

	public static int getIndSigTotfprob() {
		return IND_SIG_TOTFPROB;
	}

	public static int getIndSigTotfprobtanul() {
		return IND_SIG_TOTFPROBTANUL;
	}

	public static int getIndSigTotprovision() {
		return IND_SIG_TOTPROVISION;
	}

	public static int getIndSigTotcola() {
		return IND_SIG_TOTCOLA;
	}

	public static int getIndTotcola() {
		return IND_TOTCOLA;
	}

	public static int getIndSigProvfcal() {
		return IND_SIG_PROVFCAL;
	}

	public static int getIndProvfcal() {
		return IND_PROVFCAL;
	}

	public static int getIndSigTotra() {
		return IND_SIG_TOTRA;
	}

	public static int getIndSigTotcsm() {
		return IND_SIG_TOTCSM;
	}

	public static int getIndSigTotcsmpatron() {
		return IND_SIG_TOTCSMPATRON;
	}

	public static int getIndSigTotcsmrossp() {
		return IND_SIG_TOTCSMROSSP;
	}
	
	public String getSigTotfpgtoad() {
		return sigTotfpgtoad;
	}

	public void setSigTotfpgtoad(String sigTotfpgtoad) {
		this.sigTotfpgtoad = sigTotfpgtoad;
	}

	public java.math.BigDecimal getTotfpgtoad() {
		return totfpgtoad;
	}

	public void setTotfpgtoad(java.math.BigDecimal totfpgtoad) {
		this.totfpgtoad = totfpgtoad;
	}

	public String getSigTotfpnagtoad() {
		return sigTotfpnagtoad;
	}

	public void setSigTotfpnagtoad(String sigTotfpnagtoad) {
		this.sigTotfpnagtoad = sigTotfpnagtoad;
	}

	public java.math.BigDecimal getTotfpnagtoad() {
		return totfpnagtoad;
	}

	public void setTotfpnagtoad(java.math.BigDecimal totfpnagtoad) {
		this.totfpnagtoad = totfpnagtoad;
	}

	public String getSigTotfactgtoad() {
		return sigTotfactgtoad;
	}

	public void setSigTotfactgtoad(String sigTotfactgtoad) {
		this.sigTotfactgtoad = sigTotfactgtoad;
	}

	public java.math.BigDecimal getTotfactgtoad() {
		return totfactgtoad;
	}

	public void setTotfactgtoad(java.math.BigDecimal totfactgtoad) {
		this.totfactgtoad = totfactgtoad;
	}

	public String getSigTotcolagtoad() {
		return sigTotcolagtoad;
	}

	public void setSigTotcolagtoad(String sigTotcolagtoad) {
		this.sigTotcolagtoad = sigTotcolagtoad;
	}

	public java.math.BigDecimal getTotcolagtoad() {
		return totcolagtoad;
	}

	public void setTotcolagtoad(java.math.BigDecimal totcolagtoad) {
		this.totcolagtoad = totcolagtoad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcontrato == null) ? 0 : kcontrato.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((provfcal == null) ? 0 : provfcal.hashCode());
		result = prime * result + ((sigProvfcal == null) ? 0 : sigProvfcal.hashCode());
		result = prime * result + ((sigTotcola == null) ? 0 : sigTotcola.hashCode());
		result = prime * result + ((sigTotcolacom == null) ? 0 : sigTotcolacom.hashCode());
		result = prime * result + ((sigTotcolacompl == null) ? 0 : sigTotcolacompl.hashCode());
		result = prime * result + ((sigTotcolafall == null) ? 0 : sigTotcolafall.hashCode());
		result = prime * result + ((sigTotcolagto == null) ? 0 : sigTotcolagto.hashCode());
		result = prime * result + ((sigTotcolagtoad == null) ? 0 : sigTotcolagtoad.hashCode());
		result = prime * result + ((sigTotcolaprim == null) ? 0 : sigTotcolaprim.hashCode());
		result = prime * result + ((sigTotcolarte == null) ? 0 : sigTotcolarte.hashCode());
		result = prime * result + ((sigTotcolavida == null) ? 0 : sigTotcolavida.hashCode());
		result = prime * result + ((sigTotcsm == null) ? 0 : sigTotcsm.hashCode());
		result = prime * result + ((sigTotcsmpatron == null) ? 0 : sigTotcsmpatron.hashCode());
		result = prime * result + ((sigTotcsmrossp == null) ? 0 : sigTotcsmrossp.hashCode());
		result = prime * result + ((sigTotfactcom == null) ? 0 : sigTotfactcom.hashCode());
		result = prime * result + ((sigTotfactcompl == null) ? 0 : sigTotfactcompl.hashCode());
		result = prime * result + ((sigTotfactfall == null) ? 0 : sigTotfactfall.hashCode());
		result = prime * result + ((sigTotfactgto == null) ? 0 : sigTotfactgto.hashCode());
		result = prime * result + ((sigTotfactgtoad == null) ? 0 : sigTotfactgtoad.hashCode());
		result = prime * result + ((sigTotfactprim == null) ? 0 : sigTotfactprim.hashCode());
		result = prime * result + ((sigTotfactrte == null) ? 0 : sigTotfactrte.hashCode());
		result = prime * result + ((sigTotfactvida == null) ? 0 : sigTotfactvida.hashCode());
		result = prime * result + ((sigTotfpcom == null) ? 0 : sigTotfpcom.hashCode());
		result = prime * result + ((sigTotfpcompl == null) ? 0 : sigTotfpcompl.hashCode());
		result = prime * result + ((sigTotfpfall == null) ? 0 : sigTotfpfall.hashCode());
		result = prime * result + ((sigTotfpgto == null) ? 0 : sigTotfpgto.hashCode());
		result = prime * result + ((sigTotfpgtoad == null) ? 0 : sigTotfpgtoad.hashCode());
		result = prime * result + ((sigTotfpnacom == null) ? 0 : sigTotfpnacom.hashCode());
		result = prime * result + ((sigTotfpnacompl == null) ? 0 : sigTotfpnacompl.hashCode());
		result = prime * result + ((sigTotfpnafall == null) ? 0 : sigTotfpnafall.hashCode());
		result = prime * result + ((sigTotfpnagto == null) ? 0 : sigTotfpnagto.hashCode());
		result = prime * result + ((sigTotfpnagtoad == null) ? 0 : sigTotfpnagtoad.hashCode());
		result = prime * result + ((sigTotfpnaprim == null) ? 0 : sigTotfpnaprim.hashCode());
		result = prime * result + ((sigTotfpnarte == null) ? 0 : sigTotfpnarte.hashCode());
		result = prime * result + ((sigTotfpnavida == null) ? 0 : sigTotfpnavida.hashCode());
		result = prime * result + ((sigTotfpprim == null) ? 0 : sigTotfpprim.hashCode());
		result = prime * result + ((sigTotfprob == null) ? 0 : sigTotfprob.hashCode());
		result = prime * result + ((sigTotfprobtanul == null) ? 0 : sigTotfprobtanul.hashCode());
		result = prime * result + ((sigTotfprte == null) ? 0 : sigTotfprte.hashCode());
		result = prime * result + ((sigTotfpvida == null) ? 0 : sigTotfpvida.hashCode());
		result = prime * result + ((sigTotprovision == null) ? 0 : sigTotprovision.hashCode());
		result = prime * result + ((sigTotra == null) ? 0 : sigTotra.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((totcola == null) ? 0 : totcola.hashCode());
		result = prime * result + ((totcolacom == null) ? 0 : totcolacom.hashCode());
		result = prime * result + ((totcolacompl == null) ? 0 : totcolacompl.hashCode());
		result = prime * result + ((totcolafall == null) ? 0 : totcolafall.hashCode());
		result = prime * result + ((totcolagto == null) ? 0 : totcolagto.hashCode());
		result = prime * result + ((totcolagtoad == null) ? 0 : totcolagtoad.hashCode());
		result = prime * result + ((totcolaprim == null) ? 0 : totcolaprim.hashCode());
		result = prime * result + ((totcolarte == null) ? 0 : totcolarte.hashCode());
		result = prime * result + ((totcolavida == null) ? 0 : totcolavida.hashCode());
		result = prime * result + ((totcsm == null) ? 0 : totcsm.hashCode());
		result = prime * result + ((totcsmpatron == null) ? 0 : totcsmpatron.hashCode());
		result = prime * result + ((totcsmrossp == null) ? 0 : totcsmrossp.hashCode());
		result = prime * result + ((totfactcom == null) ? 0 : totfactcom.hashCode());
		result = prime * result + ((totfactcompl == null) ? 0 : totfactcompl.hashCode());
		result = prime * result + ((totfactfall == null) ? 0 : totfactfall.hashCode());
		result = prime * result + ((totfactgto == null) ? 0 : totfactgto.hashCode());
		result = prime * result + ((totfactgtoad == null) ? 0 : totfactgtoad.hashCode());
		result = prime * result + ((totfactprim == null) ? 0 : totfactprim.hashCode());
		result = prime * result + ((totfactrte == null) ? 0 : totfactrte.hashCode());
		result = prime * result + ((totfactvida == null) ? 0 : totfactvida.hashCode());
		result = prime * result + ((totfpcom == null) ? 0 : totfpcom.hashCode());
		result = prime * result + ((totfpcompl == null) ? 0 : totfpcompl.hashCode());
		result = prime * result + ((totfpfall == null) ? 0 : totfpfall.hashCode());
		result = prime * result + ((totfpgto == null) ? 0 : totfpgto.hashCode());
		result = prime * result + ((totfpgtoad == null) ? 0 : totfpgtoad.hashCode());
		result = prime * result + ((totfpnacom == null) ? 0 : totfpnacom.hashCode());
		result = prime * result + ((totfpnacompl == null) ? 0 : totfpnacompl.hashCode());
		result = prime * result + ((totfpnafall == null) ? 0 : totfpnafall.hashCode());
		result = prime * result + ((totfpnagto == null) ? 0 : totfpnagto.hashCode());
		result = prime * result + ((totfpnagtoad == null) ? 0 : totfpnagtoad.hashCode());
		result = prime * result + ((totfpnaprim == null) ? 0 : totfpnaprim.hashCode());
		result = prime * result + ((totfpnarte == null) ? 0 : totfpnarte.hashCode());
		result = prime * result + ((totfpnavida == null) ? 0 : totfpnavida.hashCode());
		result = prime * result + ((totfpprim == null) ? 0 : totfpprim.hashCode());
		result = prime * result + ((totfprob == null) ? 0 : totfprob.hashCode());
		result = prime * result + ((totfprobtanul == null) ? 0 : totfprobtanul.hashCode());
		result = prime * result + ((totfprte == null) ? 0 : totfprte.hashCode());
		result = prime * result + ((totfpvida == null) ? 0 : totfpvida.hashCode());
		result = prime * result + ((totprovision == null) ? 0 : totprovision.hashCode());
		result = prime * result + ((totra == null) ? 0 : totra.hashCode());
		result = prime * result + ((uoa == null) ? 0 : uoa.hashCode());
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
		FlujosTN17 other = (FlujosTN17) obj;
		if (bt == null) {
			if (other.bt != null)
				return false;
		} else if (!bt.equals(other.bt))
			return false;
		if (ccanal == null) {
			if (other.ccanal != null)
				return false;
		} else if (!ccanal.equals(other.ccanal))
			return false;
		if (ccartera == null) {
			if (other.ccartera != null)
				return false;
		} else if (!ccartera.equals(other.ccartera))
			return false;
		if (cnegocio == null) {
			if (other.cnegocio != null)
				return false;
		} else if (!cnegocio.equals(other.cnegocio))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fsuscri == null) {
			if (other.fsuscri != null)
				return false;
		} else if (!fsuscri.equals(other.fsuscri))
			return false;
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
			return false;
		if (intfeccal == null) {
			if (other.intfeccal != null)
				return false;
		} else if (!intfeccal.equals(other.intfeccal))
			return false;
		if (kajuste == null) {
			if (other.kajuste != null)
				return false;
		} else if (!kajuste.equals(other.kajuste))
			return false;
		if (kbencon == null) {
			if (other.kbencon != null)
				return false;
		} else if (!kbencon.equals(other.kbencon))
			return false;
		if (kcarterainv == null) {
			if (other.kcarterainv != null)
				return false;
		} else if (!kcarterainv.equals(other.kcarterainv))
			return false;
		if (kcertificado == null) {
			if (other.kcertificado != null)
				return false;
		} else if (!kcertificado.equals(other.kcertificado))
			return false;
		if (kcontrato == null) {
			if (other.kcontrato != null)
				return false;
		} else if (!kcontrato.equals(other.kcontrato))
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
		if (kmodext == null) {
			if (other.kmodext != null)
				return false;
		} else if (!kmodext.equals(other.kmodext))
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
		if (kramo == null) {
			if (other.kramo != null)
				return false;
		} else if (!kramo.equals(other.kramo))
			return false;
		if (ksubpoliza == null) {
			if (other.ksubpoliza != null)
				return false;
		} else if (!ksubpoliza.equals(other.ksubpoliza))
			return false;
		if (norden == null) {
			if (other.norden != null)
				return false;
		} else if (!norden.equals(other.norden))
			return false;
		if (nsuscri == null) {
			if (other.nsuscri != null)
				return false;
		} else if (!nsuscri.equals(other.nsuscri))
			return false;
		if (provfcal == null) {
			if (other.provfcal != null)
				return false;
		} else if (!provfcal.equals(other.provfcal))
			return false;
		if (sigProvfcal == null) {
			if (other.sigProvfcal != null)
				return false;
		} else if (!sigProvfcal.equals(other.sigProvfcal))
			return false;
		if (sigTotcola == null) {
			if (other.sigTotcola != null)
				return false;
		} else if (!sigTotcola.equals(other.sigTotcola))
			return false;
		if (sigTotcolacom == null) {
			if (other.sigTotcolacom != null)
				return false;
		} else if (!sigTotcolacom.equals(other.sigTotcolacom))
			return false;
		if (sigTotcolacompl == null) {
			if (other.sigTotcolacompl != null)
				return false;
		} else if (!sigTotcolacompl.equals(other.sigTotcolacompl))
			return false;
		if (sigTotcolafall == null) {
			if (other.sigTotcolafall != null)
				return false;
		} else if (!sigTotcolafall.equals(other.sigTotcolafall))
			return false;
		if (sigTotcolagto == null) {
			if (other.sigTotcolagto != null)
				return false;
		} else if (!sigTotcolagto.equals(other.sigTotcolagto))
			return false;
		if (sigTotcolagtoad == null) {
			if (other.sigTotcolagtoad != null)
				return false;
		} else if (!sigTotcolagtoad.equals(other.sigTotcolagtoad))
			return false;
		if (sigTotcolaprim == null) {
			if (other.sigTotcolaprim != null)
				return false;
		} else if (!sigTotcolaprim.equals(other.sigTotcolaprim))
			return false;
		if (sigTotcolarte == null) {
			if (other.sigTotcolarte != null)
				return false;
		} else if (!sigTotcolarte.equals(other.sigTotcolarte))
			return false;
		if (sigTotcolavida == null) {
			if (other.sigTotcolavida != null)
				return false;
		} else if (!sigTotcolavida.equals(other.sigTotcolavida))
			return false;
		if (sigTotcsm == null) {
			if (other.sigTotcsm != null)
				return false;
		} else if (!sigTotcsm.equals(other.sigTotcsm))
			return false;
		if (sigTotcsmpatron == null) {
			if (other.sigTotcsmpatron != null)
				return false;
		} else if (!sigTotcsmpatron.equals(other.sigTotcsmpatron))
			return false;
		if (sigTotcsmrossp == null) {
			if (other.sigTotcsmrossp != null)
				return false;
		} else if (!sigTotcsmrossp.equals(other.sigTotcsmrossp))
			return false;
		if (sigTotfactcom == null) {
			if (other.sigTotfactcom != null)
				return false;
		} else if (!sigTotfactcom.equals(other.sigTotfactcom))
			return false;
		if (sigTotfactcompl == null) {
			if (other.sigTotfactcompl != null)
				return false;
		} else if (!sigTotfactcompl.equals(other.sigTotfactcompl))
			return false;
		if (sigTotfactfall == null) {
			if (other.sigTotfactfall != null)
				return false;
		} else if (!sigTotfactfall.equals(other.sigTotfactfall))
			return false;
		if (sigTotfactgto == null) {
			if (other.sigTotfactgto != null)
				return false;
		} else if (!sigTotfactgto.equals(other.sigTotfactgto))
			return false;
		if (sigTotfactgtoad == null) {
			if (other.sigTotfactgtoad != null)
				return false;
		} else if (!sigTotfactgtoad.equals(other.sigTotfactgtoad))
			return false;
		if (sigTotfactprim == null) {
			if (other.sigTotfactprim != null)
				return false;
		} else if (!sigTotfactprim.equals(other.sigTotfactprim))
			return false;
		if (sigTotfactrte == null) {
			if (other.sigTotfactrte != null)
				return false;
		} else if (!sigTotfactrte.equals(other.sigTotfactrte))
			return false;
		if (sigTotfactvida == null) {
			if (other.sigTotfactvida != null)
				return false;
		} else if (!sigTotfactvida.equals(other.sigTotfactvida))
			return false;
		if (sigTotfpcom == null) {
			if (other.sigTotfpcom != null)
				return false;
		} else if (!sigTotfpcom.equals(other.sigTotfpcom))
			return false;
		if (sigTotfpcompl == null) {
			if (other.sigTotfpcompl != null)
				return false;
		} else if (!sigTotfpcompl.equals(other.sigTotfpcompl))
			return false;
		if (sigTotfpfall == null) {
			if (other.sigTotfpfall != null)
				return false;
		} else if (!sigTotfpfall.equals(other.sigTotfpfall))
			return false;
		if (sigTotfpgto == null) {
			if (other.sigTotfpgto != null)
				return false;
		} else if (!sigTotfpgto.equals(other.sigTotfpgto))
			return false;
		if (sigTotfpgtoad == null) {
			if (other.sigTotfpgtoad != null)
				return false;
		} else if (!sigTotfpgtoad.equals(other.sigTotfpgtoad))
			return false;
		if (sigTotfpnacom == null) {
			if (other.sigTotfpnacom != null)
				return false;
		} else if (!sigTotfpnacom.equals(other.sigTotfpnacom))
			return false;
		if (sigTotfpnacompl == null) {
			if (other.sigTotfpnacompl != null)
				return false;
		} else if (!sigTotfpnacompl.equals(other.sigTotfpnacompl))
			return false;
		if (sigTotfpnafall == null) {
			if (other.sigTotfpnafall != null)
				return false;
		} else if (!sigTotfpnafall.equals(other.sigTotfpnafall))
			return false;
		if (sigTotfpnagto == null) {
			if (other.sigTotfpnagto != null)
				return false;
		} else if (!sigTotfpnagto.equals(other.sigTotfpnagto))
			return false;
		if (sigTotfpnagtoad == null) {
			if (other.sigTotfpnagtoad != null)
				return false;
		} else if (!sigTotfpnagtoad.equals(other.sigTotfpnagtoad))
			return false;
		if (sigTotfpnaprim == null) {
			if (other.sigTotfpnaprim != null)
				return false;
		} else if (!sigTotfpnaprim.equals(other.sigTotfpnaprim))
			return false;
		if (sigTotfpnarte == null) {
			if (other.sigTotfpnarte != null)
				return false;
		} else if (!sigTotfpnarte.equals(other.sigTotfpnarte))
			return false;
		if (sigTotfpnavida == null) {
			if (other.sigTotfpnavida != null)
				return false;
		} else if (!sigTotfpnavida.equals(other.sigTotfpnavida))
			return false;
		if (sigTotfpprim == null) {
			if (other.sigTotfpprim != null)
				return false;
		} else if (!sigTotfpprim.equals(other.sigTotfpprim))
			return false;
		if (sigTotfprob == null) {
			if (other.sigTotfprob != null)
				return false;
		} else if (!sigTotfprob.equals(other.sigTotfprob))
			return false;
		if (sigTotfprobtanul == null) {
			if (other.sigTotfprobtanul != null)
				return false;
		} else if (!sigTotfprobtanul.equals(other.sigTotfprobtanul))
			return false;
		if (sigTotfprte == null) {
			if (other.sigTotfprte != null)
				return false;
		} else if (!sigTotfprte.equals(other.sigTotfprte))
			return false;
		if (sigTotfpvida == null) {
			if (other.sigTotfpvida != null)
				return false;
		} else if (!sigTotfpvida.equals(other.sigTotfpvida))
			return false;
		if (sigTotprovision == null) {
			if (other.sigTotprovision != null)
				return false;
		} else if (!sigTotprovision.equals(other.sigTotprovision))
			return false;
		if (sigTotra == null) {
			if (other.sigTotra != null)
				return false;
		} else if (!sigTotra.equals(other.sigTotra))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (totcola == null) {
			if (other.totcola != null)
				return false;
		} else if (!totcola.equals(other.totcola))
			return false;
		if (totcolacom == null) {
			if (other.totcolacom != null)
				return false;
		} else if (!totcolacom.equals(other.totcolacom))
			return false;
		if (totcolacompl == null) {
			if (other.totcolacompl != null)
				return false;
		} else if (!totcolacompl.equals(other.totcolacompl))
			return false;
		if (totcolafall == null) {
			if (other.totcolafall != null)
				return false;
		} else if (!totcolafall.equals(other.totcolafall))
			return false;
		if (totcolagto == null) {
			if (other.totcolagto != null)
				return false;
		} else if (!totcolagto.equals(other.totcolagto))
			return false;
		if (totcolagtoad == null) {
			if (other.totcolagtoad != null)
				return false;
		} else if (!totcolagtoad.equals(other.totcolagtoad))
			return false;
		if (totcolaprim == null) {
			if (other.totcolaprim != null)
				return false;
		} else if (!totcolaprim.equals(other.totcolaprim))
			return false;
		if (totcolarte == null) {
			if (other.totcolarte != null)
				return false;
		} else if (!totcolarte.equals(other.totcolarte))
			return false;
		if (totcolavida == null) {
			if (other.totcolavida != null)
				return false;
		} else if (!totcolavida.equals(other.totcolavida))
			return false;
		if (totcsm == null) {
			if (other.totcsm != null)
				return false;
		} else if (!totcsm.equals(other.totcsm))
			return false;
		if (totcsmpatron == null) {
			if (other.totcsmpatron != null)
				return false;
		} else if (!totcsmpatron.equals(other.totcsmpatron))
			return false;
		if (totcsmrossp == null) {
			if (other.totcsmrossp != null)
				return false;
		} else if (!totcsmrossp.equals(other.totcsmrossp))
			return false;
		if (totfactcom == null) {
			if (other.totfactcom != null)
				return false;
		} else if (!totfactcom.equals(other.totfactcom))
			return false;
		if (totfactcompl == null) {
			if (other.totfactcompl != null)
				return false;
		} else if (!totfactcompl.equals(other.totfactcompl))
			return false;
		if (totfactfall == null) {
			if (other.totfactfall != null)
				return false;
		} else if (!totfactfall.equals(other.totfactfall))
			return false;
		if (totfactgto == null) {
			if (other.totfactgto != null)
				return false;
		} else if (!totfactgto.equals(other.totfactgto))
			return false;
		if (totfactgtoad == null) {
			if (other.totfactgtoad != null)
				return false;
		} else if (!totfactgtoad.equals(other.totfactgtoad))
			return false;
		if (totfactprim == null) {
			if (other.totfactprim != null)
				return false;
		} else if (!totfactprim.equals(other.totfactprim))
			return false;
		if (totfactrte == null) {
			if (other.totfactrte != null)
				return false;
		} else if (!totfactrte.equals(other.totfactrte))
			return false;
		if (totfactvida == null) {
			if (other.totfactvida != null)
				return false;
		} else if (!totfactvida.equals(other.totfactvida))
			return false;
		if (totfpcom == null) {
			if (other.totfpcom != null)
				return false;
		} else if (!totfpcom.equals(other.totfpcom))
			return false;
		if (totfpcompl == null) {
			if (other.totfpcompl != null)
				return false;
		} else if (!totfpcompl.equals(other.totfpcompl))
			return false;
		if (totfpfall == null) {
			if (other.totfpfall != null)
				return false;
		} else if (!totfpfall.equals(other.totfpfall))
			return false;
		if (totfpgto == null) {
			if (other.totfpgto != null)
				return false;
		} else if (!totfpgto.equals(other.totfpgto))
			return false;
		if (totfpgtoad == null) {
			if (other.totfpgtoad != null)
				return false;
		} else if (!totfpgtoad.equals(other.totfpgtoad))
			return false;
		if (totfpnacom == null) {
			if (other.totfpnacom != null)
				return false;
		} else if (!totfpnacom.equals(other.totfpnacom))
			return false;
		if (totfpnacompl == null) {
			if (other.totfpnacompl != null)
				return false;
		} else if (!totfpnacompl.equals(other.totfpnacompl))
			return false;
		if (totfpnafall == null) {
			if (other.totfpnafall != null)
				return false;
		} else if (!totfpnafall.equals(other.totfpnafall))
			return false;
		if (totfpnagto == null) {
			if (other.totfpnagto != null)
				return false;
		} else if (!totfpnagto.equals(other.totfpnagto))
			return false;
		if (totfpnagtoad == null) {
			if (other.totfpnagtoad != null)
				return false;
		} else if (!totfpnagtoad.equals(other.totfpnagtoad))
			return false;
		if (totfpnaprim == null) {
			if (other.totfpnaprim != null)
				return false;
		} else if (!totfpnaprim.equals(other.totfpnaprim))
			return false;
		if (totfpnarte == null) {
			if (other.totfpnarte != null)
				return false;
		} else if (!totfpnarte.equals(other.totfpnarte))
			return false;
		if (totfpnavida == null) {
			if (other.totfpnavida != null)
				return false;
		} else if (!totfpnavida.equals(other.totfpnavida))
			return false;
		if (totfpprim == null) {
			if (other.totfpprim != null)
				return false;
		} else if (!totfpprim.equals(other.totfpprim))
			return false;
		if (totfprob == null) {
			if (other.totfprob != null)
				return false;
		} else if (!totfprob.equals(other.totfprob))
			return false;
		if (totfprobtanul == null) {
			if (other.totfprobtanul != null)
				return false;
		} else if (!totfprobtanul.equals(other.totfprobtanul))
			return false;
		if (totfprte == null) {
			if (other.totfprte != null)
				return false;
		} else if (!totfprte.equals(other.totfprte))
			return false;
		if (totfpvida == null) {
			if (other.totfpvida != null)
				return false;
		} else if (!totfpvida.equals(other.totfpvida))
			return false;
		if (totprovision == null) {
			if (other.totprovision != null)
				return false;
		} else if (!totprovision.equals(other.totprovision))
			return false;
		if (totra == null) {
			if (other.totra != null)
				return false;
		} else if (!totra.equals(other.totra))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FlujosTN17 [cnegocio=" + cnegocio + ", ccanal=" + ccanal + ", ccartera=" + ccartera + ", fcierre="
				+ fcierre + ", bt=" + bt + ", uoa=" + uoa + ", kcontrato=" + kcontrato + ", kramo=" + kramo
				+ ", kmodalidad=" + kmodalidad + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza
				+ ", kcertificado=" + kcertificado + ", nsuscri=" + nsuscri + ", norden=" + norden + ", kgarantia="
				+ kgarantia + ", kprestacion=" + kprestacion + ", kajuste=" + kajuste + ", ctipoaport=" + ctipoaport
				+ ", intfeccal=" + intfeccal + ", fsuscri=" + fsuscri + ", kcarterainv=" + kcarterainv + ", gapact="
				+ gapact + ", kmodext=" + kmodext + ", spcom=" + spcom + ", kbencon=" + kbencon + ", sigTotfpvida="
				+ sigTotfpvida + ", totfpvida=" + totfpvida + ", sigTotfpnavida=" + sigTotfpnavida + ", totfpnavida="
				+ totfpnavida + ", sigTotfactvida=" + sigTotfactvida + ", totfactvida=" + totfactvida
				+ ", sigTotcolavida=" + sigTotcolavida + ", totcolavida=" + totcolavida + ", sigTotfpfall="
				+ sigTotfpfall + ", totfpfall=" + totfpfall + ", sigTotfpnafall=" + sigTotfpnafall + ", totfpnafall="
				+ totfpnafall + ", sigTotfactfall=" + sigTotfactfall + ", totfactfall=" + totfactfall
				+ ", sigTotcolafall=" + sigTotcolafall + ", totcolafall=" + totcolafall + ", sigTotfpcompl="
				+ sigTotfpcompl + ", totfpcompl=" + totfpcompl + ", sigTotfpnacompl=" + sigTotfpnacompl
				+ ", totfpnacompl=" + totfpnacompl + ", sigTotfactcompl=" + sigTotfactcompl + ", totfactcompl="
				+ totfactcompl + ", sigTotcolacompl=" + sigTotcolacompl + ", totcolacompl=" + totcolacompl
				+ ", sigTotfpgto=" + sigTotfpgto + ", totfpgto=" + totfpgto + ", sigTotfpnagto=" + sigTotfpnagto
				+ ", totfpnagto=" + totfpnagto + ", sigTotfactgto=" + sigTotfactgto + ", totfactgto=" + totfactgto
				+ ", sigTotcolagto=" + sigTotcolagto + ", totcolagto=" + totcolagto + ", sigTotfpcom=" + sigTotfpcom
				+ ", totfpcom=" + totfpcom + ", sigTotfpnacom=" + sigTotfpnacom + ", totfpnacom=" + totfpnacom
				+ ", sigTotfactcom=" + sigTotfactcom + ", totfactcom=" + totfactcom + ", sigTotcolacom=" + sigTotcolacom
				+ ", totcolacom=" + totcolacom + ", sigTotfprte=" + sigTotfprte + ", totfprte=" + totfprte
				+ ", sigTotfpnarte=" + sigTotfpnarte + ", totfpnarte=" + totfpnarte + ", sigTotfactrte=" + sigTotfactrte
				+ ", totfactrte=" + totfactrte + ", sigTotcolarte=" + sigTotcolarte + ", totcolarte=" + totcolarte
				+ ", sigTotfpprim=" + sigTotfpprim + ", totfpprim=" + totfpprim + ", sigTotfpnaprim=" + sigTotfpnaprim
				+ ", totfpnaprim=" + totfpnaprim + ", sigTotfactprim=" + sigTotfactprim + ", totfactprim=" + totfactprim
				+ ", sigTotcolaprim=" + sigTotcolaprim + ", totcolaprim=" + totcolaprim + ", sigTotfprob=" + sigTotfprob
				+ ", totfprob=" + totfprob + ", sigTotfprobtanul=" + sigTotfprobtanul + ", totfprobtanul="
				+ totfprobtanul + ", sigTotprovision=" + sigTotprovision + ", totprovision=" + totprovision
				+ ", sigTotcola=" + sigTotcola + ", totcola=" + totcola + ", sigProvfcal=" + sigProvfcal + ", provfcal="
				+ provfcal + ", sigTotra=" + sigTotra + ", totra=" + totra + ", sigTotcsm=" + sigTotcsm + ", totcsm="
				+ totcsm + ", sigTotcsmpatron=" + sigTotcsmpatron + ", totcsmpatron=" + totcsmpatron
				+ ", sigTotcsmrossp=" + sigTotcsmrossp + ", totcsmrossp=" + totcsmrossp + ", sigTotfpgtoad="
				+ sigTotfpgtoad + ", totfpgtoad=" + totfpgtoad + ", sigTotfpnagtoad=" + sigTotfpnagtoad
				+ ", totfpnagtoad=" + totfpnagtoad + ", sigTotfactgtoad=" + sigTotfactgtoad + ", totfactgtoad="
				+ totfactgtoad + ", sigTotcolagtoad=" + sigTotcolagtoad + ", totcolagtoad=" + totcolagtoad + "]";
	}

	@Override
	public FlujosTN17Key getKey() {
		return new FlujosTN17Key(cnegocio, ccanal, ccartera, fcierre, bt, uoa, kcontrato, kramo, kmodalidad, kpoliza,
				ksubpoliza, kcertificado, nsuscri, norden, kgarantia, kprestacion, kajuste, ctipoaport, kcarterainv,gapact,kmodext,spcom );
	}

}
