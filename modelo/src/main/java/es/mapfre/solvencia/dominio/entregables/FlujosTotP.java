package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTotPKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;


@Portable
public class FlujosTotP implements EntidadBase<FlujosTotPKey>, EntidadConBaseTec {

	public static final int IND_BT = 0;
	public static final int IND_CCANAL = 1;
	public static final int IND_CCARTERA = 2;
	public static final int IND_CNEGOCIO = 3;
	public static final int IND_CTIPOAPORT = 4;
	public static final int IND_FCIERRE = 5;
	public static final int IND_KAJUSTE = 6;
	public static final int IND_KCERTIFICADO = 7;
	public static final int IND_KGARANTIA = 8;
	public static final int IND_KMODALIDAD = 9;
	public static final int IND_KPOLIZA = 10;
	public static final int IND_KPRESTACION = 11;
	public static final int IND_KSUBPOLIZA = 12;
	public static final int IND_NORDEN = 13;
	public static final int IND_NSUSCRI = 14;
	public static final int IND_PROVBTIFCAL = 15;
	public static final int IND_TOTCOLA = 16;
	public static final int IND_TOTCOLACOM = 17;
	public static final int IND_TOTCOLACOMPL = 18;
	public static final int IND_TOTCOLAFALL = 19;
	public static final int IND_TOTCOLAGTO = 20;
	public static final int IND_TOTCOLAPRIM = 21;
	public static final int IND_TOTCOLARTE = 22;
	public static final int IND_TOTOCOLAVIDA = 23;
	public static final int IND_TOTFACTCOM = 24;
	public static final int IND_TOTFACTCOMPL = 25;
	public static final int IND_TOTFACTFALL = 26;
	public static final int IND_TOTFACTGTO = 27;
	public static final int IND_TOTFACTPRIM = 28;
	public static final int IND_TOTFACTRTE = 29;
	public static final int IND_TOTFACTVIDA = 30;
	public static final int IND_TOTFPNACOMPL = 31;
	public static final int IND_TOTFPCOM = 32;
	public static final int IND_TOTFPCOMPL = 33;
	public static final int IND_TOTFPFALL = 34;
	public static final int IND_TOTFPGTO = 35;
	public static final int IND_TOTFPNACOM = 36;
	public static final int IND_TOTFPNAFALL = 37;
	public static final int IND_TOTFPNAGTO = 38;
	public static final int IND_TOTFPNAPRIM = 39;
	public static final int IND_TOTFPNARTE = 40;
	public static final int IND_TOTFPNAVIDA = 41;
	public static final int IND_TOTFPPRIM = 42;
	public static final int IND_TOTFPROB = 43;
	public static final int IND_TOTFPRTE = 44;
	public static final int IND_TOTFPVIDA = 45;
	public static final int IND_TOTFPROBTANUL = 46;
	public static final int IND_TOTPROVISION = 47;
	
	public static final int IND_KRAMO = 48;
	public static final int IND_FSUSCRI = 49;
	public static final int IND_KCARTERAINV = 50;
	public static final int IND_GAPACT = 51;
	public static final int IND_INTFECCALC = 52; 
	
	public static final int IND_CTIPOPROVI = 53; 
	public static final int IND_SPCOM =54; 
	public static final int IND_KOFICONT = 55;
	public static final int IND_PFPINV = 56;
	public static final int IND_CTIPRAMO = 57; 
	public static final int IND_SEGMENTO1 = 58;  
	public static final int IND_TIPOSUBRIESGO = 59; 
	public static final int IND_NUEVAPRODUC = 60; 
	public static final int IND_INDICRESCATE = 61; 
	public static final int IND_TOTFNRTE = 62; 
	public static final int IND_CURVATI = 63;
	public static final int IND_TOTFNFALL = 64;
	
	public static final int IND_KMODEXT = 65;
	public static final int IND_INTBTI = 66;
	public static final int IND_GESTIONIT = 67;
	public static final int IND_QIXI2 = 68;
	public static final int IND_QIXI3 = 69;
	public static final int IND_KBENCON = 70; 
	
	public static final int IND_IPRIMANETAINI = 71;
	public static final int IND_DESVIACION = 72;
	public static final int IND_PCOASEG = 73;
	public static final int IND_PGASTGESEX1I = 74;
	public static final int IND_CSITUPOL = 75;
	
	public static final int IND_TOTCOLAGTOAD = 76;
	public static final int IND_TOTFACTGTOAD = 77;
	public static final int IND_TOTFPGTOAD = 78;
	public static final int IND_TOTFPNAGTOAD = 79;
	
	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NORDEN)
	private Integer norden;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(value = IND_PROVBTIFCAL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provbtifcal;
	@PortableProperty(value = IND_TOTCOLA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcola;
	@PortableProperty(value = IND_TOTCOLACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacom;
	@PortableProperty(value = IND_TOTCOLACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacompl;
	@PortableProperty(value = IND_TOTCOLAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolafall;
	@PortableProperty(value = IND_TOTCOLAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagto;
	@PortableProperty(value = IND_TOTCOLAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolaprim;
	@PortableProperty(value = IND_TOTCOLARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolarte;
	@PortableProperty(value = IND_TOTOCOLAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolavida;
	@PortableProperty(value = IND_TOTFACTCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcom;
	@PortableProperty(value = IND_TOTFACTCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcompl;
	@PortableProperty(value = IND_TOTFACTFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactfall;
	@PortableProperty(value = IND_TOTFACTGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgto;
	@PortableProperty(value = IND_TOTFACTPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactprim;
	@PortableProperty(value = IND_TOTFACTRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactrte;
	@PortableProperty(value = IND_TOTFACTVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactvida;
	@PortableProperty(value = IND_TOTFPNACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacompl;
	@PortableProperty(value = IND_TOTFPCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcom;
	@PortableProperty(value = IND_TOTFPCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcompl;
	@PortableProperty(value = IND_TOTFPFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpfall;
	@PortableProperty(value = IND_TOTFPGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgto;
	@PortableProperty(value = IND_TOTFPNACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacom;
	@PortableProperty(value = IND_TOTFPNAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnafall;
	@PortableProperty(value = IND_TOTFPNAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagto;
	@PortableProperty(value = IND_TOTFPNAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnaprim;
	@PortableProperty(value = IND_TOTFPNARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnarte;
	@PortableProperty(value = IND_TOTFPNAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnavida;
	@PortableProperty(value = IND_TOTFPPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpprim;
	@PortableProperty(value = IND_TOTFPROB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprob;
	@PortableProperty(value = IND_TOTFPRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprte;
	@PortableProperty(value = IND_TOTFPVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpvida;
	@PortableProperty(value = IND_TOTFPROBTANUL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprobtanul;
	@PortableProperty(value = IND_TOTPROVISION, codec = BigDecimalSolvenciaCodec.class)
	
	private java.math.BigDecimal totprovision;
	@PortableProperty(IND_KRAMO) 
	private String kramo;
	@PortableProperty(IND_FSUSCRI)
	private Timestamp fsuscri;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_GAPACT)
	private String gapAct;
	@PortableProperty(value = IND_INTFECCALC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal intfeccal;
	@PortableProperty(value = IND_INTBTI, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal intBTI;
	
	@PortableProperty(IND_CTIPOPROVI) 
	private String ctipoprovi;
	@PortableProperty(IND_SPCOM)
	private String spcom;
	@PortableProperty(IND_KOFICONT)
	private String koficont;
	@PortableProperty(value = IND_PFPINV, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pfpinv;
	@PortableProperty(IND_CTIPRAMO)
	private String ctipramo;
	@PortableProperty(IND_SEGMENTO1)
	private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(IND_NUEVAPRODUC)
	private boolean nuevaprodu;
	@PortableProperty(IND_INDICRESCATE)
	private String indicrescate;
	@PortableProperty(value = IND_TOTFNRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfnrte;
	@PortableProperty(IND_CURVATI)
	private String curvati;
	@PortableProperty(value = IND_TOTFNFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfnfall;
	
	@PortableProperty(IND_KMODEXT)
	private Integer kmodext;
	
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	
	@PortableProperty(value = IND_QIXI2, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal qiXi2;
	@PortableProperty(value = IND_QIXI3, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal qiXi3;
	@PortableProperty(IND_KBENCON) 
	private String kbencon;
	
	@PortableProperty(value = IND_IPRIMANETAINI, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal iprimanetaini;
	@PortableProperty(value = IND_DESVIACION, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal desviacion;
	@PortableProperty(value = IND_PCOASEG, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pcoaseg;
	@PortableProperty(value = IND_PGASTGESEX1I, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pgastgesex1i;
	@PortableProperty(IND_CSITUPOL)
	private String csitupol;

	@PortableProperty(value = IND_TOTCOLAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagtoad;
	@PortableProperty(value = IND_TOTFACTGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgtoad;
	@PortableProperty(value = IND_TOTFPGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgtoad;
	@PortableProperty(value = IND_TOTFPNAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagtoad;
	
	public FlujosTotP() {
		super();
	}

	public String getBt() {
		return bt;
	}

	public void setBt(String bt) {
		this.bt = bt;
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

	public String getCnegocio() {
		return cnegocio;
	}

	public void setCnegocio(String cnegocio) {
		this.cnegocio = cnegocio;
	}

	public String getCtipoaport() {
		return ctipoaport;
	}

	public void setCtipoaport(String ctipoaport) {
		this.ctipoaport = ctipoaport;
	}

	public Timestamp getFcierre() {
		return fcierre;
	}

	public void setFcierre(Timestamp fcierre) {
		this.fcierre = fcierre;
	}

	public Integer getKajuste() {
		return kajuste;
	}

	public void setKajuste(Integer kajuste) {
		this.kajuste = kajuste;
	}

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getKgarantia() {
		return kgarantia;
	}

	public void setKgarantia(Integer kgarantia) {
		this.kgarantia = kgarantia;
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

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public Integer getKsubpoliza() {
		return ksubpoliza;
	}

	public void setKsubpoliza(Integer ksubpoliza) {
		this.ksubpoliza = ksubpoliza;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
	}

	public Integer getNsuscri() {
		return nsuscri;
	}

	public void setNsuscri(Integer nsuscri) {
		this.nsuscri = nsuscri;
	}

	public java.math.BigDecimal getProvbtifcal() {
		return provbtifcal;
	}

	public void setProvbtifcal(java.math.BigDecimal provbtifcal) {
		this.provbtifcal = provbtifcal;
	}

	public java.math.BigDecimal getTotcola() {
		return totcola;
	}

	public void setTotcola(java.math.BigDecimal totcola) {
		this.totcola = totcola;
	}

	public java.math.BigDecimal getTotcolacom() {
		return totcolacom;
	}

	public void setTotcolacom(java.math.BigDecimal totcolacom) {
		this.totcolacom = totcolacom;
	}

	public java.math.BigDecimal getTotcolacompl() {
		return totcolacompl;
	}

	public void setTotcolacompl(java.math.BigDecimal totcolacompl) {
		this.totcolacompl = totcolacompl;
	}

	public java.math.BigDecimal getTotcolafall() {
		return totcolafall;
	}

	public void setTotcolafall(java.math.BigDecimal totcolafall) {
		this.totcolafall = totcolafall;
	}

	public java.math.BigDecimal getTotcolagto() {
		return totcolagto;
	}

	public void setTotcolagto(java.math.BigDecimal totcolagto) {
		this.totcolagto = totcolagto;
	}

	public java.math.BigDecimal getTotcolaprim() {
		return totcolaprim;
	}

	public void setTotcolaprim(java.math.BigDecimal totcolaprim) {
		this.totcolaprim = totcolaprim;
	}

	public java.math.BigDecimal getTotcolarte() {
		return totcolarte;
	}

	public void setTotcolarte(java.math.BigDecimal totcolarte) {
		this.totcolarte = totcolarte;
	}

	public java.math.BigDecimal getTotcolavida() {
		return totcolavida;
	}

	public void setTotcolavida(java.math.BigDecimal totcolavida) {
		this.totcolavida = totcolavida;
	}

	public java.math.BigDecimal getTotfactcom() {
		return totfactcom;
	}

	public void setTotfactcom(java.math.BigDecimal totfactcom) {
		this.totfactcom = totfactcom;
	}

	public java.math.BigDecimal getTotfactcompl() {
		return totfactcompl;
	}

	public void setTotfactcompl(java.math.BigDecimal totfactcompl) {
		this.totfactcompl = totfactcompl;
	}

	public java.math.BigDecimal getTotfactfall() {
		return totfactfall;
	}

	public void setTotfactfall(java.math.BigDecimal totfactfall) {
		this.totfactfall = totfactfall;
	}

	public java.math.BigDecimal getTotfactgto() {
		return totfactgto;
	}

	public void setTotfactgto(java.math.BigDecimal totfactgto) {
		this.totfactgto = totfactgto;
	}

	public java.math.BigDecimal getTotfactprim() {
		return totfactprim;
	}

	public void setTotfactprim(java.math.BigDecimal totfactprim) {
		this.totfactprim = totfactprim;
	}

	public java.math.BigDecimal getTotfactrte() {
		return totfactrte;
	}

	public void setTotfactrte(java.math.BigDecimal totfactrte) {
		this.totfactrte = totfactrte;
	}

	public java.math.BigDecimal getTotfactvida() {
		return totfactvida;
	}

	public void setTotfactvida(java.math.BigDecimal totfactvida) {
		this.totfactvida = totfactvida;
	}

	public java.math.BigDecimal getTotfpcom() {
		return totfpcom;
	}

	public void setTotfpcom(java.math.BigDecimal totfpcom) {
		this.totfpcom = totfpcom;
	}

	public java.math.BigDecimal getTotfpcompl() {
		return totfpcompl;
	}

	public void setTotfpcompl(java.math.BigDecimal totfpcompl) {
		this.totfpcompl = totfpcompl;
	}

	public java.math.BigDecimal getTotfpfall() {
		return totfpfall;
	}

	public void setTotfpfall(java.math.BigDecimal totfpfall) {
		this.totfpfall = totfpfall;
	}

	public java.math.BigDecimal getTotfpgto() {
		return totfpgto;
	}

	public void setTotfpgto(java.math.BigDecimal totfpgto) {
		this.totfpgto = totfpgto;
	}

	public java.math.BigDecimal getTotfpnacom() {
		return totfpnacom;
	}

	public void setTotfpnacom(java.math.BigDecimal totfpnacom) {
		this.totfpnacom = totfpnacom;
	}

	public java.math.BigDecimal getTotfpnacompl() {
		return totfpnacompl;
	}

	public void setTotfpnacompl(java.math.BigDecimal totfpnacompl) {
		this.totfpnacompl = totfpnacompl;
	}

	public java.math.BigDecimal getTotfpnafall() {
		return totfpnafall;
	}

	public void setTotfpnafall(java.math.BigDecimal totfpnafall) {
		this.totfpnafall = totfpnafall;
	}

	public java.math.BigDecimal getTotfpnagto() {
		return totfpnagto;
	}

	public void setTotfpnagto(java.math.BigDecimal totfpnagto) {
		this.totfpnagto = totfpnagto;
	}

	public java.math.BigDecimal getTotfpnaprim() {
		return totfpnaprim;
	}

	public void setTotfpnaprim(java.math.BigDecimal totfpnaprim) {
		this.totfpnaprim = totfpnaprim;
	}

	public java.math.BigDecimal getTotfpnarte() {
		return totfpnarte;
	}

	public void setTotfpnarte(java.math.BigDecimal totfpnarte) {
		this.totfpnarte = totfpnarte;
	}

	public java.math.BigDecimal getTotfpnavida() {
		return totfpnavida;
	}

	public void setTotfpnavida(java.math.BigDecimal totfpnavida) {
		this.totfpnavida = totfpnavida;
	}

	public java.math.BigDecimal getTotfpprim() {
		return totfpprim;
	}

	public void setTotfpprim(java.math.BigDecimal totfpprim) {
		this.totfpprim = totfpprim;
	}

	public java.math.BigDecimal getTotfprob() {
		return totfprob;
	}

	public void setTotfprob(java.math.BigDecimal totfprob) {
		this.totfprob = totfprob;
	}

	public java.math.BigDecimal getTotfprte() {
		return totfprte;
	}

	public void setTotfprte(java.math.BigDecimal totfprte) {
		this.totfprte = totfprte;
	}

	public java.math.BigDecimal getTotfpvida() {
		return totfpvida;
	}

	public void setTotfpvida(java.math.BigDecimal totfpvida) {
		this.totfpvida = totfpvida;
	}

	public java.math.BigDecimal getTotfprobtanul() {
		return totfprobtanul;
	}

	public void setTotfprobtanul(java.math.BigDecimal totfprobtanul) {
		this.totfprobtanul = totfprobtanul;
	}

	public java.math.BigDecimal getTotprovision() {
		return totprovision;
	}

	public void setTotprovision(java.math.BigDecimal totprovision) {
		this.totprovision = totprovision;
	}

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
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

	public String getGapAct() {
		return gapAct;
	}

	public void setGapAct(String gapAct) {
		this.gapAct = gapAct;
	}

	public java.math.BigDecimal getIntfeccal() {
		return intfeccal;
	}

	public void setIntfeccal(java.math.BigDecimal intfeccal) {
		this.intfeccal = intfeccal;
	}
	
	public java.math.BigDecimal getIntBTI() {
		return intBTI;
	}

	public void setIntBTI(java.math.BigDecimal intBTI) {
		this.intBTI = intBTI;
	}
	
	public String getCtipoprovi() {
		return ctipoprovi;
	}

	public void setCtipoprovi(String ctipoprovi) {
		this.ctipoprovi = ctipoprovi;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}

	public String getKoficont() {
		return koficont;
	}

	public void setKoficont(String koficont) {
		this.koficont = koficont;
	}

	public java.math.BigDecimal getPfpinv() {
		return pfpinv;
	}

	public void setPfpinv(java.math.BigDecimal pfpinv) {
		this.pfpinv = pfpinv;
	}

	public String getCtipramo() {
		return ctipramo;
	}

	public void setCtipramo(String ctipramo) {
		this.ctipramo = ctipramo;
	}

	public String getSegmento1() {
		return segmento1;
	}

	public void setSegmento1(String segmento1) {
		this.segmento1 = segmento1;
	}

	public String getTiposubriesgo() {
		return tiposubriesgo;
	}

	public void setTiposubriesgo(String tiposubriesgo) {
		this.tiposubriesgo = tiposubriesgo;
	}

	public boolean isNuevaprodu() {
		return nuevaprodu;
	}

	public void setNuevaprodu(boolean nuevaprodu) {
		this.nuevaprodu = nuevaprodu;
	}

	public String getIndicrescate() {
		return indicrescate;
	}

	public void setIndicrescate(String indicrescate) {
		this.indicrescate = indicrescate;
	}

	public java.math.BigDecimal getTotfnrte() {
		return totfnrte;
	}

	public void setTotfnrte(java.math.BigDecimal totfnrte) {
		this.totfnrte = totfnrte;
	}

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
	}
	
	
	
	public java.math.BigDecimal getTotfnfall() {
		return totfnfall;
	}

	public void setTotfnfall(java.math.BigDecimal totfnfall) {
		this.totfnfall = totfnfall;
	}
	
	
	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}

	public String getGestionit() {
		return gestionit;
	}

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}

	public java.math.BigDecimal getQiXi2() {
		return qiXi2;
	}

	public void setQiXi2(java.math.BigDecimal qiXi2) {
		this.qiXi2 = qiXi2;
	}

	public java.math.BigDecimal getQiXi3() {
		return qiXi3;
	}

	public void setQiXi3(java.math.BigDecimal qiXi3) {
		this.qiXi3 = qiXi3;
	}
	
	public String getKbencon() {
		return kbencon;
	}
	
	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}

	public java.math.BigDecimal getIprimanetaini() {
		return iprimanetaini;
	}

	public void setIprimanetaini(java.math.BigDecimal iprimanetaini) {
		this.iprimanetaini = iprimanetaini;
	}

	public java.math.BigDecimal getDesviacion() {
		return desviacion;
	}

	public void setDesviacion(java.math.BigDecimal desviacion) {
		this.desviacion = desviacion;
	}

	public java.math.BigDecimal getPcoaseg() {
		return pcoaseg;
	}

	public void setPcoaseg(java.math.BigDecimal pcoaseg) {
		this.pcoaseg = pcoaseg;
	}

	public java.math.BigDecimal getPgastgesex1i() {
		return pgastgesex1i;
	}

	public void setPgastgesex1i(java.math.BigDecimal pgastgesex1i) {
		this.pgastgesex1i = pgastgesex1i;
	}
	
	public String getCsitupol() {
		return csitupol;
	}

	public void setCsitupol(String csitupol) {
		this.csitupol = csitupol;
	}

	public java.math.BigDecimal getTotcolagtoad() {
		return totcolagtoad;
	}

	public void setTotcolagtoad(java.math.BigDecimal totcolagtoad) {
		this.totcolagtoad = totcolagtoad;
	}

	public java.math.BigDecimal getTotfactgtoad() {
		return totfactgtoad;
	}

	public void setTotfactgtoad(java.math.BigDecimal totfactgtoad) {
		this.totfactgtoad = totfactgtoad;
	}

	public java.math.BigDecimal getTotfpgtoad() {
		return totfpgtoad;
	}

	public void setTotfpgtoad(java.math.BigDecimal totfpgtoad) {
		this.totfpgtoad = totfpgtoad;
	}

	public java.math.BigDecimal getTotfpnagtoad() {
		return totfpnagtoad;
	}

	public void setTotfpnagtoad(java.math.BigDecimal totfpnagtoad) {
		this.totfpnagtoad = totfpnagtoad;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((csitupol == null) ? 0 : csitupol.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result + ((ctipramo == null) ? 0 : ctipramo.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((desviacion == null) ? 0 : desviacion.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((indicrescate == null) ? 0 : indicrescate.hashCode());
		result = prime * result + ((intBTI == null) ? 0 : intBTI.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((iprimanetaini == null) ? 0 : iprimanetaini.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((koficont == null) ? 0 : koficont.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + (nuevaprodu ? 1231 : 1237);
		result = prime * result + ((pcoaseg == null) ? 0 : pcoaseg.hashCode());
		result = prime * result + ((pfpinv == null) ? 0 : pfpinv.hashCode());
		result = prime * result + ((pgastgesex1i == null) ? 0 : pgastgesex1i.hashCode());
		result = prime * result + ((provbtifcal == null) ? 0 : provbtifcal.hashCode());
		result = prime * result + ((qiXi2 == null) ? 0 : qiXi2.hashCode());
		result = prime * result + ((qiXi3 == null) ? 0 : qiXi3.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((totcola == null) ? 0 : totcola.hashCode());
		result = prime * result + ((totcolacom == null) ? 0 : totcolacom.hashCode());
		result = prime * result + ((totcolacompl == null) ? 0 : totcolacompl.hashCode());
		result = prime * result + ((totcolafall == null) ? 0 : totcolafall.hashCode());
		result = prime * result + ((totcolagto == null) ? 0 : totcolagto.hashCode());
		result = prime * result + ((totcolagtoad == null) ? 0 : totcolagtoad.hashCode());
		result = prime * result + ((totcolaprim == null) ? 0 : totcolaprim.hashCode());
		result = prime * result + ((totcolarte == null) ? 0 : totcolarte.hashCode());
		result = prime * result + ((totcolavida == null) ? 0 : totcolavida.hashCode());
		result = prime * result + ((totfactcom == null) ? 0 : totfactcom.hashCode());
		result = prime * result + ((totfactcompl == null) ? 0 : totfactcompl.hashCode());
		result = prime * result + ((totfactfall == null) ? 0 : totfactfall.hashCode());
		result = prime * result + ((totfactgto == null) ? 0 : totfactgto.hashCode());
		result = prime * result + ((totfactgtoad == null) ? 0 : totfactgtoad.hashCode());
		result = prime * result + ((totfactprim == null) ? 0 : totfactprim.hashCode());
		result = prime * result + ((totfactrte == null) ? 0 : totfactrte.hashCode());
		result = prime * result + ((totfactvida == null) ? 0 : totfactvida.hashCode());
		result = prime * result + ((totfnfall == null) ? 0 : totfnfall.hashCode());
		result = prime * result + ((totfnrte == null) ? 0 : totfnrte.hashCode());
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
		FlujosTotP other = (FlujosTotP) obj;
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
		if (csitupol == null) {
			if (other.csitupol != null)
				return false;
		} else if (!csitupol.equals(other.csitupol))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (ctipoprovi == null) {
			if (other.ctipoprovi != null)
				return false;
		} else if (!ctipoprovi.equals(other.ctipoprovi))
			return false;
		if (ctipramo == null) {
			if (other.ctipramo != null)
				return false;
		} else if (!ctipramo.equals(other.ctipramo))
			return false;
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (desviacion == null) {
			if (other.desviacion != null)
				return false;
		} else if (!desviacion.equals(other.desviacion))
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
		if (gapAct == null) {
			if (other.gapAct != null)
				return false;
		} else if (!gapAct.equals(other.gapAct))
			return false;
		if (gestionit == null) {
			if (other.gestionit != null)
				return false;
		} else if (!gestionit.equals(other.gestionit))
			return false;
		if (indicrescate == null) {
			if (other.indicrescate != null)
				return false;
		} else if (!indicrescate.equals(other.indicrescate))
			return false;
		if (intBTI == null) {
			if (other.intBTI != null)
				return false;
		} else if (!intBTI.equals(other.intBTI))
			return false;
		if (intfeccal == null) {
			if (other.intfeccal != null)
				return false;
		} else if (!intfeccal.equals(other.intfeccal))
			return false;
		if (iprimanetaini == null) {
			if (other.iprimanetaini != null)
				return false;
		} else if (!iprimanetaini.equals(other.iprimanetaini))
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
		if (koficont == null) {
			if (other.koficont != null)
				return false;
		} else if (!koficont.equals(other.koficont))
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
		if (nuevaprodu != other.nuevaprodu)
			return false;
		if (pcoaseg == null) {
			if (other.pcoaseg != null)
				return false;
		} else if (!pcoaseg.equals(other.pcoaseg))
			return false;
		if (pfpinv == null) {
			if (other.pfpinv != null)
				return false;
		} else if (!pfpinv.equals(other.pfpinv))
			return false;
		if (pgastgesex1i == null) {
			if (other.pgastgesex1i != null)
				return false;
		} else if (!pgastgesex1i.equals(other.pgastgesex1i))
			return false;
		if (provbtifcal == null) {
			if (other.provbtifcal != null)
				return false;
		} else if (!provbtifcal.equals(other.provbtifcal))
			return false;
		if (qiXi2 == null) {
			if (other.qiXi2 != null)
				return false;
		} else if (!qiXi2.equals(other.qiXi2))
			return false;
		if (qiXi3 == null) {
			if (other.qiXi3 != null)
				return false;
		} else if (!qiXi3.equals(other.qiXi3))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
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
		if (totfnfall == null) {
			if (other.totfnfall != null)
				return false;
		} else if (!totfnfall.equals(other.totfnfall))
			return false;
		if (totfnrte == null) {
			if (other.totfnrte != null)
				return false;
		} else if (!totfnrte.equals(other.totfnrte))
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
		return true;
	}
	
	@Override
	public String toString() {
		return "FlujosTotP [bt=" + bt + ", cnegocio=" + cnegocio + "]";
	}

	@Override
	public FlujosTotPKey getKey() {

		return new FlujosTotPKey(kajuste, kcertificado, kgarantia, kmodalidad, kpoliza, kprestacion, ksubpoliza, bt,
				nsuscri, norden, ctipoaport);
	}

}
