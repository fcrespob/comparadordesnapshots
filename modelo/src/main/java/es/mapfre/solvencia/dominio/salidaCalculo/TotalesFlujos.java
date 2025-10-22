/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 14/12/2018 Se incluye campo KBENCON
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.salidaCalculo;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class TotalesFlujos implements EntidadBase<TotalesFlujosKey>, EntidadConBaseTec {

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
	public static final int IND_FECINI = 71;
	public static final int IND_FECFIN = 72;
	public static final int IND_PCOASEG = 73;
	public static final int IND_DISTINT = 74;
	public static final int IND_PREST_CAL = 75;
	public static final int IND_PGASTGESIN1I = 76;
	public static final int IND_PGASTGESIN2I = 77;
	public static final int IND_PGASTGESEX1I = 78;
	public static final int IND_ITCALC = 79;
	public static final int IND_TABLACALC1ASEG1 = 80;
	public static final int IND_TABLA_TANUL = 81;
	public static final int IND_GTO_UNI = 82;
	public static final int IND_GTO_PROV = 83;
	public static final int IND_FCURVA_TI = 84;
	public static final int IND_FACTOR1 = 85;
	public static final int IND_FACTOR2 = 86;
	public static final int IND_FECINISUS = 87;
	public static final int IND_FECFINTRAMO1 = 88;
	public static final int IND_PINTERTECNI1 = 89;
	public static final int IND_PINTERTECNI2 = 90;
	public static final int IND_TABLA1ASEG1 = 91;
	public static final int IND_KCOASEORI = 92;
	public static final int IND_KCOASE1 = 93;
	public static final int IND_KCOASE2 = 94;
	public static final int IND_KCOASE3 = 95;
	public static final int IND_KCOASE4 = 96;
	public static final int IND_KCOASE5 = 97;
	public static final int IND_KCOASE6 = 98;
	public static final int IND_KCOASE7 = 99;
	public static final int IND_KCOASE8 = 100;
	public static final int IND_KCOASE9 = 101;
	public static final int IND_KCOASE10 = 102;
	public static final int IND_KCOASE11 = 103;
	public static final int IND_KCOASE12 = 104;
	public static final int IND_KCOASE13 = 105;
	public static final int IND_KCOASE14 = 106;
	public static final int IND_KCOASE15 = 107;
	public static final int IND_KCOASE16 = 108;
	public static final int IND_KCOASE17 = 109;
	public static final int IND_KCOASE18 = 110;
	public static final int IND_KCOASE19 = 111;
	public static final int IND_KCOASE20 = 112;
	public static final int IND_KCUADRO = 113;
	public static final int IND_UMICKEY = 114;
	public static final int IND_TOTNOMRTE = 115;
    public static final int IND_RAUMIC = 116;
	public static final int IND_CSMUMIC = 117;
	public static final int IND_CSMAJUSTADO = 118;	
	public static final int IND_UOA = 119;
	public static final int IND_KCONTRATO = 120;
	
	//Entregable FPSL
	public static final int IND_KUOA = 121;
	public static final int IND_TEXTRACCION = 122;
	public static final int IND_TPASIVO = 123;
	public static final int IND_TNEGOCIO = 124;
	public static final int IND_TNEGOCIOLRC = 125;
	public static final int IND_PVENTA = 126;
	public static final int IND_CODREASEG = 127;
	public static final int IND_FPROYFLUJEST = 128;
	public static final int IND_MON_PRIM = 129;
	public static final int IND_MON_RTE = 130;
	public static final int IND_MON_FALL = 131;
	public static final int IND_MON_VIDA = 132;
	public static final int IND_MON_GTOADQ = 133;
	public static final int IND_MON_GTO = 134;
	public static final int IND_MON_PB = 135;
	public static final int IND_PB = 136;
	public static final int IND_CSM003 = 137;
	public static final int IND_COHORTE = 138;
	public static final int IND_SWCASADO = 139;
	
	public static final int IND_TOTCOLAGTOAD = 140;
	public static final int IND_TOTFACTGTOAD = 141;
	public static final int IND_TOTFPGTOAD = 142;
	public static final int IND_TOTFPNAGTOAD = 143;
	public static final int IND_KMODALIDADORIG = 144;
	public static final int IND_KPOLIZAORIG = 145;
	public static final int IND_KSUBPOLIZAORIG = 146;
	public static final int IND_KCERTIFICADOORIG = 147;

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
	@PortableProperty(IND_KBENCON) private String kbencon;
	
	//ini Fase VIII
	//Fechas
	@PortableProperty(IND_FECINI)
	private Timestamp fecini;
	@PortableProperty(IND_FECFIN)
	private Timestamp fecfin;
	@PortableProperty(IND_FECINISUS)
	private Timestamp fecinisus;
	//Coaseguro
	@PortableProperty(IND_PCOASEG)
	private java.math.BigDecimal pcoaseg;
	@PortableProperty(IND_DISTINT)
	private java.math.BigDecimal distint;
	@PortableProperty(IND_KCOASEORI)
	private String kcoaseOri;
	//Datos Adicionales
	@PortableProperty(IND_PREST_CAL) 
	private String prestCal;
	//BaseTecnicaInicial
	@PortableProperty(IND_PGASTGESIN1I)
	private java.math.BigDecimal pgastgesin1I;
	@PortableProperty(IND_PGASTGESIN2I)
	private java.math.BigDecimal pgastgesin2I;
	@PortableProperty(IND_PGASTGESEX1I)
	private java.math.BigDecimal pgastgesex1I;
	@PortableProperty(IND_FECFINTRAMO1)
	private Timestamp fecFinTramo1;
	@PortableProperty(IND_PINTERTECNI1)
	private java.math.BigDecimal pintertecnI1;
	@PortableProperty(IND_PINTERTECNI2)
	private java.math.BigDecimal pintertecnI2;
	@PortableProperty(IND_TABLA1ASEG1)
	private String tabla1Aseg1;
	//DetalleBaseTecnica
	@PortableProperty(IND_ITCALC)
	private List<BigDecimal> itcalc;
	@PortableProperty(IND_TABLACALC1ASEG1)
	private String tablacalc1aseg1;
	@PortableProperty(IND_TABLA_TANUL)
	private String tablaTanul;
	@PortableProperty(IND_GTO_UNI)
	private BigDecimal gtoUni;
	@PortableProperty(IND_GTO_PROV)
	private BigDecimal gtoprov;
	@PortableProperty(IND_FCURVA_TI)
	private Timestamp fcurvaTi;
	@PortableProperty(IND_FACTOR1)
	private BigDecimal factor1;
	@PortableProperty(IND_FACTOR2)
	private List<BigDecimal> factor2;
	//DatosAdicionalesCoaseguro
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
	@PortableProperty(IND_UMICKEY)
	private UmicKey umicKey;
	@PortableProperty(value = IND_TOTNOMRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totNomRte;
	@PortableProperty(value = IND_RAUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal raumic;
	@PortableProperty(value = IND_CSMUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal csmumic;
	@PortableProperty(value = IND_CSMAJUSTADO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal csmajustado;				
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_KCONTRATO)
	private String kcontrato;
	// INI - PROXY Prestaciones
	@PortableProperty(IND_COHORTE)
	private String cohorte;
	@PortableProperty(IND_SWCASADO)
	private String swcasado;
	@PortableProperty(IND_KMODALIDADORIG)
	private Integer kmodalidadOrig;
	@PortableProperty(IND_KPOLIZAORIG)
	private Long kpolizaOrig;
	@PortableProperty(IND_KSUBPOLIZAORIG)
	private Integer ksubpolizaOrig;
	@PortableProperty(IND_KCERTIFICADOORIG)
	private Integer kcertificadoOrig;
	
	// FIN - PROXY Prestaciones
	
	//Fin fase VIII
	
	//Entregable FPSL
	@PortableProperty(IND_KUOA)
	private String kuoa;
	@PortableProperty(IND_TEXTRACCION)
	private Integer textraccion;
	@PortableProperty(IND_TPASIVO)
	private String tpasivo;
	@PortableProperty(IND_TNEGOCIO)
	private Integer tnegocio;
	@PortableProperty(IND_TNEGOCIOLRC)
	private Integer tnegociolrc;
	@PortableProperty(IND_PVENTA)
	private Timestamp pventa;
	@PortableProperty(IND_CODREASEG)
	private Integer codreaseg;
	@PortableProperty(IND_FPROYFLUJEST)
	private Timestamp fproyflujest;
	@PortableProperty(IND_MON_PRIM)
	private String mon_prim;
	@PortableProperty(IND_MON_RTE)
	private String mon_rte;
	@PortableProperty(IND_MON_FALL)
	private String mon_fall;
	@PortableProperty(IND_MON_VIDA)
	private String mon_vida;
	@PortableProperty(IND_MON_GTOADQ)
	private String mon_gtoadq;
	@PortableProperty(IND_MON_GTO)
	private String mon_gto;
	@PortableProperty(IND_MON_PB)
	private String mon_pb;
	
	@PortableProperty(value = IND_CSM003, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal csm003;	
	@PortableProperty(value = IND_PB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pb;
	
	@PortableProperty(value = IND_TOTCOLAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagtoad;
	@PortableProperty(value = IND_TOTFACTGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgtoad;
	@PortableProperty(value = IND_TOTFPGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgtoad;
	@PortableProperty(value = IND_TOTFPNAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagtoad;	

	public TotalesFlujos() {
		super();
	}

	public java.math.BigDecimal getPb() {
		return pb;
	}

	public void setPb(java.math.BigDecimal pb) {
		this.pb = pb;
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
	public Timestamp getFecini() {
		return fecini;
	}

	public void setFecini(Timestamp fecini) {
		this.fecini = fecini;
	}
	
	public Timestamp getFecinisus() {
		return fecinisus;
	}

	public void setFecinisus(Timestamp fecinisus) {
		this.fecinisus = fecinisus;
	}
	public Timestamp getFecfin() {
		return fecfin;
	}

	public void setFecfin(Timestamp fecfin) {
		this.fecfin = fecfin;
	}
	
	public java.math.BigDecimal getPcoaseg() {
		return pcoaseg;
	}

	public void setPcoaseg(java.math.BigDecimal pcoaseg) {
		this.pcoaseg = pcoaseg;
	}

	public java.math.BigDecimal getDistint() {
		return distint;
	}

	public void setDistint(java.math.BigDecimal distint) {
		this.distint = distint;
	}

	public String getPrestCal() {
		return prestCal;
	}

	public void setPrestCal(String prestCal) {
		this.prestCal = prestCal;
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

	public java.math.BigDecimal getPgastgesex1I() {
		return pgastgesex1I;
	}

	public void setPgastgesex1I(java.math.BigDecimal pgastgesex1i) {
		pgastgesex1I = pgastgesex1i;
	}

	public List<BigDecimal> getItcalc() {
		return itcalc;
	}

	public void setItcalc(List<BigDecimal> itcalc) {
		this.itcalc = itcalc;
	}

	public String getTablacalc1aseg1() {
		return tablacalc1aseg1;
	}

	public void setTablacalc1aseg1(String tablacalc1aseg1) {
		this.tablacalc1aseg1 = tablacalc1aseg1;
	}

	public BigDecimal getGtoUni() {
		return gtoUni;
	}

	public void setGtoUni(BigDecimal gtoUni) {
		this.gtoUni = gtoUni;
	}

	public BigDecimal getGtoprov() {
		return gtoprov;
	}

	public void setGtoprov(BigDecimal gtoprov) {
		this.gtoprov = gtoprov;
	}

	public Timestamp getFcurvaTi() {
		return fcurvaTi;
	}

	public void setFcurvaTi(Timestamp fcurvaTi) {
		this.fcurvaTi = fcurvaTi;
	}

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public List<BigDecimal> getFactor2() {
		return factor2;
	}

	public void setFactor2(List<BigDecimal> factor2) {
		this.factor2 = factor2;
	}
	
	public String getTablaTanul() {
		return tablaTanul;
	}

	public void setTablaTanul(String tablaTanul) {
		this.tablaTanul = tablaTanul;
	}

	public String getKcoaseOri() {
		return kcoaseOri;
	}

	public void setKcoaseOri(String kcoaseOri) {
		this.kcoaseOri = kcoaseOri;
	}

	public Timestamp getFecFinTramo1() {
		return fecFinTramo1;
	}

	public void setFecFinTramo1(Timestamp fecFinTramo1) {
		this.fecFinTramo1 = fecFinTramo1;
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

	public String getTabla1Aseg1() {
		return tabla1Aseg1;
	}

	public void setTabla1Aseg1(String tabla1Aseg1) {
		this.tabla1Aseg1 = tabla1Aseg1;
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
	
	public UmicKey getUmicKey() {
		return umicKey;
	}

	public void setUmicKey(UmicKey umicKey) {
		this.umicKey = umicKey;
	}

	public java.math.BigDecimal getTotNomRte() {
		return totfpvida;
	}

	public void setTotNomRte(java.math.BigDecimal totNomRte) {
		this.totNomRte = totNomRte;
	}
	
	public java.math.BigDecimal getRaumic() {
		return raumic;
	}

	public void setRaumic(java.math.BigDecimal raumic) {
		this.raumic = raumic;
	}
	
	public java.math.BigDecimal getCsmumic() {
		return csmumic;
	}

	public void setCsmumic(java.math.BigDecimal csmumic) {
		this.csmumic = csmumic;
	}
	
	public java.math.BigDecimal getCsmajustado() {
		return csmajustado;
	}

	public void setCsmajustado(java.math.BigDecimal csmajustado) {
		this.csmajustado = csmajustado;
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
	
	public String getCohorte() {
		return cohorte;
	}
	
	public void setCohorte(String cohorte) {
		this.cohorte = cohorte;
	}

	public String getSwcasado() {
		return swcasado;
	}

	public void setSwcasado(String swcasado) {
		this.swcasado = swcasado;
	}

	public Integer getKmodalidadOrig() {
		return kmodalidadOrig;
	}

	public void setKmodalidadOrig(Integer kmodalidadOrig) {
		this.kmodalidadOrig = kmodalidadOrig;
	}

	public Long getKpolizaOrig() {
		return kpolizaOrig;
	}

	public void setKpolizaOrig(Long kpolizaOrig) {
		this.kpolizaOrig = kpolizaOrig;
	}

	public Integer getKsubpolizaOrig() {
		return ksubpolizaOrig;
	}

	public void setKsubpolizaOrig(Integer ksubpolizaOrig) {
		this.ksubpolizaOrig = ksubpolizaOrig;
	}

	public Integer getKcertificadoOrig() {
		return kcertificadoOrig;
	}

	public void setKcertificadoOrig(Integer kcertificadoOrig) {
		this.kcertificadoOrig = kcertificadoOrig;
	}

	public String getKuoa() {
		return kuoa;
	}

	public void setKuoa(String kuoa) {
		this.kuoa = kuoa;
	}

	public Integer getTextraccion() {
		return textraccion;
	}

	public void setTextraccion(Integer textraccion) {
		this.textraccion = textraccion;
	}

	public String getTpasivo() {
		return tpasivo;
	}

	public void setTpasivo(String tpasivo) {
		this.tpasivo = tpasivo;
	}

	public Integer getTnegocio() {
		return tnegocio;
	}

	public void setTnegocio(Integer tnegocio) {
		this.tnegocio = tnegocio;
	}

	public Integer getTnegociolrc() {
		return tnegociolrc;
	}

	public void setTnegociolrc(Integer tnegociolrc) {
		this.tnegociolrc = tnegociolrc;
	}

	public Timestamp getPventa() {
		return pventa;
	}

	public void setPventa(Timestamp pventa) {
		this.pventa = pventa;
	}

	public Integer getCodreaseg() {
		return codreaseg;
	}

	public void setCodreaseg(Integer codreaseg) {
		this.codreaseg = codreaseg;
	}

	public Timestamp getFproyflujest() {
		return fproyflujest;
	}

	public void setFproyflujest(Timestamp fproyflujest) {
		this.fproyflujest = fproyflujest;
	}

	public String getMon_prim() {
		return mon_prim;
	}

	public void setMon_prim(String mon_prim) {
		this.mon_prim = mon_prim;
	}

	public String getMon_rte() {
		return mon_rte;
	}

	public void setMon_rte(String mon_rte) {
		this.mon_rte = mon_rte;
	}

	public String getMon_fall() {
		return mon_fall;
	}

	public void setMon_fall(String mon_fall) {
		this.mon_fall = mon_fall;
	}

	public String getMon_vida() {
		return mon_vida;
	}

	public void setMon_vida(String mon_vida) {
		this.mon_vida = mon_vida;
	}

	public String getMon_gtoadq() {
		return mon_gtoadq;
	}

	public void setMon_gtoadq(String mon_gtoadq) {
		this.mon_gtoadq = mon_gtoadq;
	}

	public String getMon_gto() {
		return mon_gto;
	}

	public void setMon_gto(String mon_gto) {
		this.mon_gto = mon_gto;
	}

	public String getMon_pb() {
		return mon_pb;
	}

	public void setMon_pb(String mon_pb) {
		this.mon_pb = mon_pb;
	}
	public java.math.BigDecimal getCsm003() {
		return csm003;
	}

	public void setCsm003(java.math.BigDecimal csm003) {
		this.csm003 = csm003;
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
		result = prime * result + ((codreaseg == null) ? 0 : codreaseg.hashCode());
		result = prime * result + ((cohorte == null) ? 0 : cohorte.hashCode());
		result = prime * result + ((csm003 == null) ? 0 : csm003.hashCode());
		result = prime * result + ((csmajustado == null) ? 0 : csmajustado.hashCode());
		result = prime * result + ((csmumic == null) ? 0 : csmumic.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((ctipoprovi == null) ? 0 : ctipoprovi.hashCode());
		result = prime * result + ((ctipramo == null) ? 0 : ctipramo.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((distint == null) ? 0 : distint.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((factor2 == null) ? 0 : factor2.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fcurvaTi == null) ? 0 : fcurvaTi.hashCode());
		result = prime * result + ((fecFinTramo1 == null) ? 0 : fecFinTramo1.hashCode());
		result = prime * result + ((fecfin == null) ? 0 : fecfin.hashCode());
		result = prime * result + ((fecini == null) ? 0 : fecini.hashCode());
		result = prime * result + ((fecinisus == null) ? 0 : fecinisus.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapAct == null) ? 0 : gapAct.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((gtoUni == null) ? 0 : gtoUni.hashCode());
		result = prime * result + ((gtoprov == null) ? 0 : gtoprov.hashCode());
		result = prime * result + ((indicrescate == null) ? 0 : indicrescate.hashCode());
		result = prime * result + ((intBTI == null) ? 0 : intBTI.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((itcalc == null) ? 0 : itcalc.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
		result = prime * result + ((kcertificadoOrig == null) ? 0 : kcertificadoOrig.hashCode());
		result = prime * result + ((kcoase1 == null) ? 0 : kcoase1.hashCode());
		result = prime * result + ((kcoase10 == null) ? 0 : kcoase10.hashCode());
		result = prime * result + ((kcoase11 == null) ? 0 : kcoase11.hashCode());
		result = prime * result + ((kcoase12 == null) ? 0 : kcoase12.hashCode());
		result = prime * result + ((kcoase13 == null) ? 0 : kcoase13.hashCode());
		result = prime * result + ((kcoase14 == null) ? 0 : kcoase14.hashCode());
		result = prime * result + ((kcoase15 == null) ? 0 : kcoase15.hashCode());
		result = prime * result + ((kcoase16 == null) ? 0 : kcoase16.hashCode());
		result = prime * result + ((kcoase17 == null) ? 0 : kcoase17.hashCode());
		result = prime * result + ((kcoase18 == null) ? 0 : kcoase18.hashCode());
		result = prime * result + ((kcoase19 == null) ? 0 : kcoase19.hashCode());
		result = prime * result + ((kcoase2 == null) ? 0 : kcoase2.hashCode());
		result = prime * result + ((kcoase20 == null) ? 0 : kcoase20.hashCode());
		result = prime * result + ((kcoase3 == null) ? 0 : kcoase3.hashCode());
		result = prime * result + ((kcoase4 == null) ? 0 : kcoase4.hashCode());
		result = prime * result + ((kcoase5 == null) ? 0 : kcoase5.hashCode());
		result = prime * result + ((kcoase6 == null) ? 0 : kcoase6.hashCode());
		result = prime * result + ((kcoase7 == null) ? 0 : kcoase7.hashCode());
		result = prime * result + ((kcoase8 == null) ? 0 : kcoase8.hashCode());
		result = prime * result + ((kcoase9 == null) ? 0 : kcoase9.hashCode());
		result = prime * result + ((kcoaseOri == null) ? 0 : kcoaseOri.hashCode());
		result = prime * result + ((kcontrato == null) ? 0 : kcontrato.hashCode());
		result = prime * result + ((kcuadro == null) ? 0 : kcuadro.hashCode());
		result = prime * result + ((kgarantia == null) ? 0 : kgarantia.hashCode());
		result = prime * result + ((kmodalidad == null) ? 0 : kmodalidad.hashCode());
		result = prime * result + ((kmodalidadOrig == null) ? 0 : kmodalidadOrig.hashCode());
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((koficont == null) ? 0 : koficont.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kpolizaOrig == null) ? 0 : kpolizaOrig.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((ksubpolizaOrig == null) ? 0 : ksubpolizaOrig.hashCode());
		result = prime * result + ((kuoa == null) ? 0 : kuoa.hashCode());
		result = prime * result + ((mon_fall == null) ? 0 : mon_fall.hashCode());
		result = prime * result + ((mon_gto == null) ? 0 : mon_gto.hashCode());
		result = prime * result + ((mon_gtoadq == null) ? 0 : mon_gtoadq.hashCode());
		result = prime * result + ((mon_pb == null) ? 0 : mon_pb.hashCode());
		result = prime * result + ((mon_prim == null) ? 0 : mon_prim.hashCode());
		result = prime * result + ((mon_rte == null) ? 0 : mon_rte.hashCode());
		result = prime * result + ((mon_vida == null) ? 0 : mon_vida.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + (nuevaprodu ? 1231 : 1237);
		result = prime * result + ((pb == null) ? 0 : pb.hashCode());
		result = prime * result + ((pcoaseg == null) ? 0 : pcoaseg.hashCode());
		result = prime * result + ((pfpinv == null) ? 0 : pfpinv.hashCode());
		result = prime * result + ((pgastgesex1I == null) ? 0 : pgastgesex1I.hashCode());
		result = prime * result + ((pgastgesin1I == null) ? 0 : pgastgesin1I.hashCode());
		result = prime * result + ((pgastgesin2I == null) ? 0 : pgastgesin2I.hashCode());
		result = prime * result + ((pintertecnI1 == null) ? 0 : pintertecnI1.hashCode());
		result = prime * result + ((pintertecnI2 == null) ? 0 : pintertecnI2.hashCode());
		result = prime * result + ((prestCal == null) ? 0 : prestCal.hashCode());
		result = prime * result + ((provbtifcal == null) ? 0 : provbtifcal.hashCode());
		result = prime * result + ((pventa == null) ? 0 : pventa.hashCode());
		result = prime * result + ((qiXi2 == null) ? 0 : qiXi2.hashCode());
		result = prime * result + ((qiXi3 == null) ? 0 : qiXi3.hashCode());
		result = prime * result + ((raumic == null) ? 0 : raumic.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
		result = prime * result + ((tabla1Aseg1 == null) ? 0 : tabla1Aseg1.hashCode());
		result = prime * result + ((tablaTanul == null) ? 0 : tablaTanul.hashCode());
		result = prime * result + ((tablacalc1aseg1 == null) ? 0 : tablacalc1aseg1.hashCode());
		result = prime * result + ((textraccion == null) ? 0 : textraccion.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((tnegocio == null) ? 0 : tnegocio.hashCode());
		result = prime * result + ((tnegociolrc == null) ? 0 : tnegociolrc.hashCode());
		result = prime * result + ((totNomRte == null) ? 0 : totNomRte.hashCode());
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
		result = prime * result + ((tpasivo == null) ? 0 : tpasivo.hashCode());
		result = prime * result + ((umicKey == null) ? 0 : umicKey.hashCode());
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
		TotalesFlujos other = (TotalesFlujos) obj;
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
		if (codreaseg == null) {
			if (other.codreaseg != null)
				return false;
		} else if (!codreaseg.equals(other.codreaseg))
			return false;
		if (cohorte == null) {
			if (other.cohorte != null)
				return false;
		} else if (!cohorte.equals(other.cohorte))
			return false;
		if (csm003 == null) {
			if (other.csm003 != null)
				return false;
		} else if (!csm003.equals(other.csm003))
			return false;
		if (csmajustado == null) {
			if (other.csmajustado != null)
				return false;
		} else if (!csmajustado.equals(other.csmajustado))
			return false;
		if (csmumic == null) {
			if (other.csmumic != null)
				return false;
		} else if (!csmumic.equals(other.csmumic))
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
		if (distint == null) {
			if (other.distint != null)
				return false;
		} else if (!distint.equals(other.distint))
			return false;
		if (factor1 == null) {
			if (other.factor1 != null)
				return false;
		} else if (!factor1.equals(other.factor1))
			return false;
		if (factor2 == null) {
			if (other.factor2 != null)
				return false;
		} else if (!factor2.equals(other.factor2))
			return false;
		if (fcierre == null) {
			if (other.fcierre != null)
				return false;
		} else if (!fcierre.equals(other.fcierre))
			return false;
		if (fcurvaTi == null) {
			if (other.fcurvaTi != null)
				return false;
		} else if (!fcurvaTi.equals(other.fcurvaTi))
			return false;
		if (fecFinTramo1 == null) {
			if (other.fecFinTramo1 != null)
				return false;
		} else if (!fecFinTramo1.equals(other.fecFinTramo1))
			return false;
		if (fecfin == null) {
			if (other.fecfin != null)
				return false;
		} else if (!fecfin.equals(other.fecfin))
			return false;
		if (fecini == null) {
			if (other.fecini != null)
				return false;
		} else if (!fecini.equals(other.fecini))
			return false;
		if (fecinisus == null) {
			if (other.fecinisus != null)
				return false;
		} else if (!fecinisus.equals(other.fecinisus))
			return false;
		if (fproyflujest == null) {
			if (other.fproyflujest != null)
				return false;
		} else if (!fproyflujest.equals(other.fproyflujest))
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
		if (gtoUni == null) {
			if (other.gtoUni != null)
				return false;
		} else if (!gtoUni.equals(other.gtoUni))
			return false;
		if (gtoprov == null) {
			if (other.gtoprov != null)
				return false;
		} else if (!gtoprov.equals(other.gtoprov))
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
		if (kcertificadoOrig == null) {
			if (other.kcertificadoOrig != null)
				return false;
		} else if (!kcertificadoOrig.equals(other.kcertificadoOrig))
			return false;
		if (kcoase1 == null) {
			if (other.kcoase1 != null)
				return false;
		} else if (!kcoase1.equals(other.kcoase1))
			return false;
		if (kcoase10 == null) {
			if (other.kcoase10 != null)
				return false;
		} else if (!kcoase10.equals(other.kcoase10))
			return false;
		if (kcoase11 == null) {
			if (other.kcoase11 != null)
				return false;
		} else if (!kcoase11.equals(other.kcoase11))
			return false;
		if (kcoase12 == null) {
			if (other.kcoase12 != null)
				return false;
		} else if (!kcoase12.equals(other.kcoase12))
			return false;
		if (kcoase13 == null) {
			if (other.kcoase13 != null)
				return false;
		} else if (!kcoase13.equals(other.kcoase13))
			return false;
		if (kcoase14 == null) {
			if (other.kcoase14 != null)
				return false;
		} else if (!kcoase14.equals(other.kcoase14))
			return false;
		if (kcoase15 == null) {
			if (other.kcoase15 != null)
				return false;
		} else if (!kcoase15.equals(other.kcoase15))
			return false;
		if (kcoase16 == null) {
			if (other.kcoase16 != null)
				return false;
		} else if (!kcoase16.equals(other.kcoase16))
			return false;
		if (kcoase17 == null) {
			if (other.kcoase17 != null)
				return false;
		} else if (!kcoase17.equals(other.kcoase17))
			return false;
		if (kcoase18 == null) {
			if (other.kcoase18 != null)
				return false;
		} else if (!kcoase18.equals(other.kcoase18))
			return false;
		if (kcoase19 == null) {
			if (other.kcoase19 != null)
				return false;
		} else if (!kcoase19.equals(other.kcoase19))
			return false;
		if (kcoase2 == null) {
			if (other.kcoase2 != null)
				return false;
		} else if (!kcoase2.equals(other.kcoase2))
			return false;
		if (kcoase20 == null) {
			if (other.kcoase20 != null)
				return false;
		} else if (!kcoase20.equals(other.kcoase20))
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
		if (kcoase7 == null) {
			if (other.kcoase7 != null)
				return false;
		} else if (!kcoase7.equals(other.kcoase7))
			return false;
		if (kcoase8 == null) {
			if (other.kcoase8 != null)
				return false;
		} else if (!kcoase8.equals(other.kcoase8))
			return false;
		if (kcoase9 == null) {
			if (other.kcoase9 != null)
				return false;
		} else if (!kcoase9.equals(other.kcoase9))
			return false;
		if (kcoaseOri == null) {
			if (other.kcoaseOri != null)
				return false;
		} else if (!kcoaseOri.equals(other.kcoaseOri))
			return false;
		if (kcontrato == null) {
			if (other.kcontrato != null)
				return false;
		} else if (!kcontrato.equals(other.kcontrato))
			return false;
		if (kcuadro == null) {
			if (other.kcuadro != null)
				return false;
		} else if (!kcuadro.equals(other.kcuadro))
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
		if (kmodalidadOrig == null) {
			if (other.kmodalidadOrig != null)
				return false;
		} else if (!kmodalidadOrig.equals(other.kmodalidadOrig))
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
		if (kpolizaOrig == null) {
			if (other.kpolizaOrig != null)
				return false;
		} else if (!kpolizaOrig.equals(other.kpolizaOrig))
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
		if (ksubpolizaOrig == null) {
			if (other.ksubpolizaOrig != null)
				return false;
		} else if (!ksubpolizaOrig.equals(other.ksubpolizaOrig))
			return false;
		if (kuoa == null) {
			if (other.kuoa != null)
				return false;
		} else if (!kuoa.equals(other.kuoa))
			return false;
		if (mon_fall == null) {
			if (other.mon_fall != null)
				return false;
		} else if (!mon_fall.equals(other.mon_fall))
			return false;
		if (mon_gto == null) {
			if (other.mon_gto != null)
				return false;
		} else if (!mon_gto.equals(other.mon_gto))
			return false;
		if (mon_gtoadq == null) {
			if (other.mon_gtoadq != null)
				return false;
		} else if (!mon_gtoadq.equals(other.mon_gtoadq))
			return false;
		if (mon_pb == null) {
			if (other.mon_pb != null)
				return false;
		} else if (!mon_pb.equals(other.mon_pb))
			return false;
		if (mon_prim == null) {
			if (other.mon_prim != null)
				return false;
		} else if (!mon_prim.equals(other.mon_prim))
			return false;
		if (mon_rte == null) {
			if (other.mon_rte != null)
				return false;
		} else if (!mon_rte.equals(other.mon_rte))
			return false;
		if (mon_vida == null) {
			if (other.mon_vida != null)
				return false;
		} else if (!mon_vida.equals(other.mon_vida))
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
		if (pb == null) {
			if (other.pb != null)
				return false;
		} else if (!pb.equals(other.pb))
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
		if (pgastgesex1I == null) {
			if (other.pgastgesex1I != null)
				return false;
		} else if (!pgastgesex1I.equals(other.pgastgesex1I))
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
		if (prestCal == null) {
			if (other.prestCal != null)
				return false;
		} else if (!prestCal.equals(other.prestCal))
			return false;
		if (provbtifcal == null) {
			if (other.provbtifcal != null)
				return false;
		} else if (!provbtifcal.equals(other.provbtifcal))
			return false;
		if (pventa == null) {
			if (other.pventa != null)
				return false;
		} else if (!pventa.equals(other.pventa))
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
		if (raumic == null) {
			if (other.raumic != null)
				return false;
		} else if (!raumic.equals(other.raumic))
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
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		if (tabla1Aseg1 == null) {
			if (other.tabla1Aseg1 != null)
				return false;
		} else if (!tabla1Aseg1.equals(other.tabla1Aseg1))
			return false;
		if (tablaTanul == null) {
			if (other.tablaTanul != null)
				return false;
		} else if (!tablaTanul.equals(other.tablaTanul))
			return false;
		if (tablacalc1aseg1 == null) {
			if (other.tablacalc1aseg1 != null)
				return false;
		} else if (!tablacalc1aseg1.equals(other.tablacalc1aseg1))
			return false;
		if (textraccion == null) {
			if (other.textraccion != null)
				return false;
		} else if (!textraccion.equals(other.textraccion))
			return false;
		if (tiposubriesgo == null) {
			if (other.tiposubriesgo != null)
				return false;
		} else if (!tiposubriesgo.equals(other.tiposubriesgo))
			return false;
		if (tnegocio == null) {
			if (other.tnegocio != null)
				return false;
		} else if (!tnegocio.equals(other.tnegocio))
			return false;
		if (tnegociolrc == null) {
			if (other.tnegociolrc != null)
				return false;
		} else if (!tnegociolrc.equals(other.tnegociolrc))
			return false;
		if (totNomRte == null) {
			if (other.totNomRte != null)
				return false;
		} else if (!totNomRte.equals(other.totNomRte))
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
		if (tpasivo == null) {
			if (other.tpasivo != null)
				return false;
		} else if (!tpasivo.equals(other.tpasivo))
			return false;
		if (umicKey == null) {
			if (other.umicKey != null)
				return false;
		} else if (!umicKey.equals(other.umicKey))
			return false;
		if (uoa == null) {
			if (other.uoa != null)
				return false;
		} else if (!uoa.equals(other.uoa))
			return false;
		return true;
	}

	@Override
	public TotalesFlujosKey getKey() {

		return new TotalesFlujosKey(kajuste, kcertificado, kgarantia, kmodalidad, kpoliza, kprestacion, ksubpoliza, bt,
				nsuscri, norden, ctipoaport);
	}
}
