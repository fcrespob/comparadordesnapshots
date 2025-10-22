/* MODIFICACION:TAR00400971-NECESIDADES NUEVO SISTEMA DE PROCESOS TÉCNICOS 
   FECHA: 14/12/2018 Se incluye campo KBENCON,SPCOM, y KMODEXT
   AUTOR: INDRA
*/

package es.mapfre.solvencia.dominio.entregables;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;

import es.mapfre.solvencia.coherence.keys.entregables.DetalleCorrienteEntregablesKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dominio.EntidadBase;
import es.mapfre.solvencia.dominio.EntidadConBaseTec;

@Portable
public class DetalleCorrienteEntregables implements EntidadBase<DetalleCorrienteEntregablesKey>, EntidadConBaseTec {

	public static final int IND_FCIERRE = 0;
	public static final int IND_BT = 1;
	public static final int IND_UMICKEY = 2;
	public static final int IND_FECHADESDE = 3;
	public static final int IND_CNEGOCIO = 4;
	public static final int IND_CCANAL = 5;
	public static final int IND_KCARTERAINV = 6;
	public static final int IND_KMODALIDAD = 7;
	public static final int IND_KGARANTIA = 8;
	public static final int IND_GAPACT = 9;
	public static final int IND_KPOLIZA = 10;
	public static final int IND_KSUBPOLIZA = 11;
	public static final int IND_NSUSCRI = 12;
	public static final int IND_KRAMO = 13;
	public static final int IND_PRESTCAL = 14;
	public static final int IND_NUEVAPRODUC = 15;
	public static final int IND_SEGMENTO1 = 16;
	public static final int IND_TIPOSUBRIESGO = 17;
	public static final int IND_CURVATI = 18;
	public static final int IND_TOTALFLUJOPROYECCION_SUMFPROB = 19;
	public static final int IND_TOTALFLUJOPROYECCION_SUMFPROBTANUL = 20;
	public static final int IND_TOTALFLUJOPROYECCION_SUMPROVISION = 21;
	public static final int IND_TOTALFLUJOPROYECCION_SUMCOLA = 22;
	public static final int IND_BLOQUEGTO_IMPFLUJOPROBABLE = 23;
	public static final int IND_BLOQUEGTO_IMPFLUJONOMINAL = 24;
	public static final int IND_BLOQUEVIDA_IMPFLUJONOMINAL = 25;
	public static final int IND_BLOQUEFALL_IMPFLUJONOMINAL = 26;
	public static final int IND_BLOQUECOMPL_IMPFLUJONOMINAL = 27;
	public static final int IND_BLOQUECOMI_IMPFLUJONOMINAL = 28;
	public static final int IND_BLOQUERTE_IMPFLUJONOMINAL = 29;
	public static final int IND_BLOQUEPRIM_IMPFLUJONOMINAL = 30;
	public static final int IND_DIA = 31;
	public static final int IND_KPRESTACION = 32;
	public static final int IND_KCOASEORI = 33;
	public static final int IND_SWCASADO = 34;
	public static final int IND_GESTIONIT = 35;
	public static final int IND_BLOQUEVIDA_IMPFLUJOPROBABLE = 36;
	public static final int IND_BLOQUEFALL_IMPFLUJOPROBABLE = 37;
	public static final int IND_BLOQUECOMPL_IMPFLUJOPROBABLE = 38;
	public static final int IND_BLOQUECOMI_IMPFLUJOPROBABLE = 39;
	public static final int IND_BLOQUERTE_IMPFLUJOPROBABLE = 40;
	public static final int IND_BLOQUEPRIM_IMPFLUJOPROBABLE = 41;
	public static final int IND_BLOQUEGTO_IMPFLUJOACTUALIZADO = 42;
	public static final int IND_BLOQUEVIDA_IMPFLUJOACTUALIZADO = 43;
	public static final int IND_BLOQUEFALL_IMPFLUJOACTUALIZADO = 44;
	public static final int IND_BLOQUECOMPL_IMPFLUJOACTUALIZADO = 45;
	public static final int IND_BLOQUECOMI_IMPFLUJOACTUALIZADO = 46;
	public static final int IND_BLOQUERTE_IMPFLUJOACTUALIZADO = 47;
	public static final int IND_BLOQUEPRIM_IMPFLUJOACTUALIZADO = 48;
	public static final int IND_SPCOM = 49;
	public static final int IND_KMODEXT = 50;
	public static final int IND_KBENCON = 51;

	// FaseVIII
	public static final int IND_FECINISUS = 52;
	public static final int IND_PGASTGESIN1I = 53;
	public static final int IND_PGASTGESIN2I = 54;
	public static final int IND_GTO_UNI = 55;
	public static final int IND_GTO_PROV = 56;
	public static final int IND_FACTOR1 = 57;
	public static final int IND_FECINITRAMO = 58;
	public static final int IND_FECFINTRAMO = 59;
	public static final int IND_FECFINTRAMO1 = 60;
	public static final int IND_PINTERTECNI1 = 61;
	public static final int IND_PINTERTECNI2 = 62;
	public static final int IND_TABLA1ASEG1 = 63;
	public static final int IND_KCOASE1 = 64;
	public static final int IND_KCOASE2 = 65;
	public static final int IND_KCOASE3 = 66;
	public static final int IND_KCOASE4 = 67;
	public static final int IND_KCOASE5 = 68;
	public static final int IND_KCOASE6 = 69;
	public static final int IND_KCOASE7 = 70;
	public static final int IND_KCOASE8 = 71;
	public static final int IND_KCOASE9 = 72;
	public static final int IND_KCOASE10 = 73;
	public static final int IND_KCOASE11 = 74;
	public static final int IND_KCOASE12 = 75;
	public static final int IND_KCOASE13 = 76;
	public static final int IND_KCOASE14 = 77;
	public static final int IND_KCOASE15 = 78;
	public static final int IND_KCOASE16 = 79;
	public static final int IND_KCOASE17 = 80;
	public static final int IND_KCOASE18 = 81;
	public static final int IND_KCOASE19 = 82;
	public static final int IND_KCOASE20 = 83;
	public static final int IND_KCUADRO = 84;
	public static final int IND_SWCASADOLST = 85;

	// FLUJINF3
	public static final int IND_NEGOCIO = 86;
	public static final int IND_UOA = 87;
	public static final int IND_KCARTERA_CONTRATO = 88;
	public static final int IND_KCARTERA_COHORT = 89;
	public static final int IND_KCARTERA_ONER = 90;
	public static final int IND_FDESDE = 91;
	public static final int IND_FHASTA = 92;
	public static final int IND_TOT_FP_VIDA = 93;
	public static final int IND_TOT_FP_FALL = 94;
	public static final int IND_TOT_FP_GASTOS = 95;
	public static final int IND_TOT_FP_COMISIONES = 96;
	public static final int IND_TOT_FP_RESCATES = 97;
	public static final int IND_TOT_FP_COMPL = 98;
	public static final int IND_TOT_FP_PRIMAS = 99;

	// FlujosTN17
	public static final int IND_CCARTERA = 100;
	public static final int IND_KCONTRATO = 101;
	public static final int IND_KCERTIFICADO = 102;
	public static final int IND_NORDEN = 103;
	public static final int IND_KAJUSTE = 104;
	public static final int IND_CTIPOAPORT = 105;
	public static final int IND_INTFECCAL = 106;
	public static final int IND_FSUSCRI = 107;
	public static final int IND_TOTFPVIDA = 109;
	public static final int IND_TOTFPNAVIDA = 110;
	public static final int IND_TOTFACTVIDA = 111;
	public static final int IND_TOTCOLAVIDA = 112;
	public static final int IND_TOTFPFALL = 113;
	public static final int IND_TOTFPNAFALL = 114;
	public static final int IND_TOTFACTFALL = 115;
	public static final int IND_TOTCOLAFALL = 116;
	public static final int IND_TOTFPCOMPL = 117;
	public static final int IND_TOTFPNACOMPL = 118;
	public static final int IND_TOTFACTCOMPL = 119;
	public static final int IND_TOTCOLACOMPL = 120;
	public static final int IND_TOTFPGTO = 121;
	public static final int IND_TOTFPNAGTO = 122;
	public static final int IND_TOTFACTGTO = 123;
	public static final int IND_TOTCOLAGTO = 124;
	public static final int IND_TOTFPCOM = 125;
	public static final int IND_TOTFPNACOM = 126;
	public static final int IND_TOTFACTCOM = 127;
	public static final int IND_TOTCOLACOM = 128;
	public static final int IND_TOTFPRTE = 129;
	public static final int IND_TOTFPNARTE = 130;
	public static final int IND_TOTFACTRTE = 131;
	public static final int IND_TOTCOLARTE = 132;
	public static final int IND_TOTFPPRIM = 133;
	public static final int IND_TOTFPNAPRIM = 134;
	public static final int IND_TOTFACTPRIM = 135;
	public static final int IND_TOTCOLAPRIM = 136;
	public static final int IND_TOTFPROB = 137;
	public static final int IND_TOTFPROBTANUL = 138;
	public static final int IND_TOTPROVISION = 139;
	public static final int IND_TOTCOLA = 140;
	public static final int IND_PROVFCAL = 141;
	public static final int IND_TOTRA = 142;
	public static final int IND_TOTCSM = 143;
	public static final int IND_TOTCSMPATRON = 144;
	
	//FPSL
	public static final int IND_KUOA = 145;
	public static final int IND_TEXTRACCION = 146;
	public static final int IND_TPASIVO = 147;
	public static final int IND_TNEGOCIO = 148;
	public static final int IND_TNEGOCIOLRC = 149;
	public static final int IND_PVENTA = 150;
	public static final int IND_CODREASEG = 151;
	public static final int IND_FPROYFLUJEST = 152;
	public static final int IND_MON_PRIM = 153;
	public static final int IND_MON_RTE = 154;
	public static final int IND_MON_FALL = 155;
	public static final int IND_MON_VIDA = 156;
	public static final int IND_MON_GTOADQ = 157;
	public static final int IND_MON_GTO = 158;
	public static final int IND_MON_PB = 159;
	public static final int IND_PB = 160;

	public static final int IND_BLOQUEGTO_IMPFLUJONOANULADO = 161;
	public static final int IND_BLOQUEVIDA_IMPFLUJONOANULADO = 162;
	public static final int IND_BLOQUEFALL_IMPFLUJONOANULADO = 163;
	public static final int IND_BLOQUECOMPL_IMPFLUJONOAULADO = 164;
	public static final int IND_BLOQUECOMI_IMPFLUJONOANULADO = 165;
	public static final int IND_BLOQUERTE_IMPFLUJONOANULADO = 166;
	public static final int IND_BLOQUEPRIM_IMPFLUJONOANULADO = 167;
	public static final int IND_SW_PB = 168;
	public static final int IND_PRIMA_UNICA = 169;
	public static final int IND_TIPO_PRIMA = 170;
	
	public static final int IND_BLOQUEGTOAD_IMPFLUJOPROBABLE = 171;
	public static final int IND_BLOQUEGTOAD_IMPFLUJONOMINAL = 172;
	public static final int IND_BLOQUEGTOAD_IMPFLUJOACTUALIZADO = 173;
	public static final int IND_BLOQUEGTOAD_IMPFLUJONOANULADO = 174;
	public static final int IND_TOTFPGTOAD = 175;
	public static final int IND_TOTFPNAGTOAD = 176;
	public static final int IND_TOTFACTGTOAD = 177;
	public static final int IND_TOTCOLAGTOAD = 178;
	public static final int IND_TOT_FP_GASTOSAD = 179;
	public static final int IND_RAUMIC = 180;
	public static final int IND_ROSSP_CSM = 181;
	public static final int PROV_ROSSP_CSM = 182;
	public static final int PROV_BEL_NIIF17 = 183;
	public static final int IND_PRIMAPERIODICA = 184;
	public static final int IND_OGA = 185;
	public static final int IND_PRIMAUNICA = 186;
	public static final int IND_SIPRIMA = 187;
	public static final int IND_COMI = 188;
	public static final int IND_BLOQUEVIDA_IMPPROVI = 189;
	public static final int IND_BLOQUEFALL_IMPPROVI = 190;
	public static final int IND_BLOQUEPRIM_IMPPROVI = 191;
	public static final int IND_BLOQUEGTO_IMPPROVI = 192;
	public static final int IND_BLOQUECOMI_IMPPROVI = 193;
	public static final int IND_BLOQUECOMPL_IMPPROVI = 194;
	public static final int IND_BLOQUEGTOAD_IMPPROVI = 195;
	public static final int IND_BLOQUERTE_IMPPROVI = 196;
	public static final int IND_SIOGA = 197;
	
	
	@PortableProperty(IND_DIA)
	private Integer dia;
	@PortableProperty(IND_FCIERRE)
	private Timestamp fcierre;
	@PortableProperty(IND_BT)
	private String bt;
	@PortableProperty(IND_UMICKEY)
	private UmicKey umicKey;
	@PortableProperty(IND_FECHADESDE)
	private Timestamp fechadesde;
	@PortableProperty(IND_CNEGOCIO)
	private String cnegocio;
	@PortableProperty(IND_CCANAL)
	private Integer ccanal;
	@PortableProperty(IND_KCARTERAINV)
	private String kcarterainv;
	@PortableProperty(IND_KMODALIDAD)
	private Integer kmodalidad;
	@PortableProperty(IND_KGARANTIA)
	private Integer kgarantia;
	@PortableProperty(IND_GAPACT)
	private String gapact;
	@PortableProperty(IND_KPOLIZA)
	private Long kpoliza;
	@PortableProperty(IND_KSUBPOLIZA)
	private Integer ksubpoliza;
	@PortableProperty(IND_NSUSCRI)
	private Integer nsuscri;
	@PortableProperty(IND_KRAMO)
	private String kramo;
	@PortableProperty(IND_PRESTCAL)
	private String prestcal;
	@PortableProperty(IND_NUEVAPRODUC)
	private Boolean nuevaproduc;
	@PortableProperty(IND_SEGMENTO1)
	private String segmento1;
	@PortableProperty(IND_TIPOSUBRIESGO)
	private String tiposubriesgo;
	@PortableProperty(IND_CURVATI)
	private String curvati;
	@PortableProperty(value = IND_TOTALFLUJOPROYECCION_SUMFPROB, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal sumfprob;
	@PortableProperty(value = IND_TOTALFLUJOPROYECCION_SUMFPROBTANUL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal sumfprobtanul;
	@PortableProperty(value = IND_TOTALFLUJOPROYECCION_SUMPROVISION, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal sumprovision;
	@PortableProperty(value = IND_TOTALFLUJOPROYECCION_SUMCOLA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal sumcola;
	@PortableProperty(value = IND_BLOQUEGTO_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoImpflujoprobable;
	@PortableProperty(value = IND_BLOQUEGTO_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoImpflujonominal;
	@PortableProperty(value = IND_BLOQUEVIDA_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquevidaImpflujonominal;
	@PortableProperty(value = IND_BLOQUEFALL_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquefallImpflujonominal;
	@PortableProperty(value = IND_BLOQUECOMPL_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomplImpflujonominal;
	@PortableProperty(value = IND_BLOQUECOMI_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomiImpflujonominal;
	@PortableProperty(value = IND_BLOQUERTE_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquerteImpflujonominal;
	@PortableProperty(value = IND_BLOQUEPRIM_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloqueprimImpflujonominal;
	@PortableProperty(IND_KPRESTACION)
	private String kprestacion;
	@PortableProperty(IND_KCOASEORI)
	private String kcoaseOri;
	@PortableProperty(IND_SWCASADO)
	private String swcasado;
	@PortableProperty(IND_GESTIONIT)
	private String gestionit;
	@PortableProperty(value = IND_BLOQUEVIDA_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquevidaImpflujoprobable;
	@PortableProperty(value = IND_BLOQUEFALL_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquefallImpflujoprobable;
	@PortableProperty(value = IND_BLOQUECOMPL_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomplImpflujoprobable;
	@PortableProperty(value = IND_BLOQUECOMI_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomiImpflujoprobable;
	@PortableProperty(value = IND_BLOQUERTE_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquerteImpflujoprobable;
	@PortableProperty(value = IND_BLOQUEPRIM_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloqueprimImpflujoprobable;
	@PortableProperty(value = IND_BLOQUEVIDA_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquevidaImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUEFALL_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquefallImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUECOMPL_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomplImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUECOMI_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomiImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUERTE_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquerteImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUEPRIM_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloqueprimImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUEGTO_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoImpflujoactualizado;
	@PortableProperty(IND_SPCOM)
	private String spcom;
	@PortableProperty(IND_KMODEXT)
	private Integer kmodext;
	@PortableProperty(IND_KBENCON)
	private String kbencon;

	// ini Fase VIII
	// Fechas
	@PortableProperty(IND_FECINISUS)
	private Timestamp fecinisus;
	// BaseTecnicaInicial
	@PortableProperty(IND_PGASTGESIN1I)
	private java.math.BigDecimal pgastgesin1I;
	@PortableProperty(IND_PGASTGESIN2I)
	private java.math.BigDecimal pgastgesin2I;
	@PortableProperty(IND_FECFINTRAMO1)
	private Timestamp fecFinTramo1;
	@PortableProperty(IND_PINTERTECNI1)
	private java.math.BigDecimal pintertecnI1;
	@PortableProperty(IND_PINTERTECNI2)
	private java.math.BigDecimal pintertecnI2;
	@PortableProperty(IND_TABLA1ASEG1)
	private String tabla1Aseg1;
	// DetalleBaseTecnica
	@PortableProperty(IND_GTO_UNI)
	private BigDecimal gtoUni;
	@PortableProperty(IND_GTO_PROV)
	private BigDecimal gtoprov;
	@PortableProperty(IND_FACTOR1)
	private BigDecimal factor1;
	@PortableProperty(IND_FECINITRAMO)
	private List<Timestamp> fecinitramo;
	@PortableProperty(IND_FECFINTRAMO)
	private List<Timestamp> fecfintramo;
	// DatosAdicionalesCoaseguro
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
	@PortableProperty(IND_SWCASADOLST)
	private List<String> swcasadolst;
	// Fin fase VIII
	// FLUJINF3
	@PortableProperty(IND_NEGOCIO)
	private String negocio;
	@PortableProperty(IND_KCARTERA_CONTRATO)
	private String kcarteraContrato;
	@PortableProperty(IND_KCARTERA_COHORT)
	private String kcarteraCohort;
	@PortableProperty(IND_KCARTERA_ONER)
	private String kcarteraOner;
	@PortableProperty(IND_FDESDE)
	private Timestamp fdesde;
	@PortableProperty(IND_FHASTA)
	private Timestamp fhasta;
	@PortableProperty(value = IND_TOT_FP_VIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpVida;
	@PortableProperty(value = IND_TOT_FP_FALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpFall;
	@PortableProperty(value = IND_TOT_FP_COMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpCompl;
	@PortableProperty(value = IND_TOT_FP_GASTOS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGastos;
	@PortableProperty(value = IND_TOT_FP_COMISIONES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpComisiones;
	@PortableProperty(value = IND_TOT_FP_RESCATES, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpRescates;
	@PortableProperty(value = IND_TOT_FP_PRIMAS, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpPrimas;

	// FLUJOSTN17
	@PortableProperty(IND_CCARTERA)
	private Integer ccartera;
	@PortableProperty(IND_UOA)
	private String uoa;
	@PortableProperty(IND_KCONTRATO)
	private String kcontrato;
	@PortableProperty(IND_KCERTIFICADO)
	private Integer kcertificado;
	@PortableProperty(IND_NORDEN)
	private Integer norden;
	@PortableProperty(IND_KAJUSTE)
	private Integer kajuste;
	@PortableProperty(IND_CTIPOAPORT)
	private String ctipoaport;
	@PortableProperty(value = IND_INTFECCAL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal intfeccal;
	@PortableProperty(IND_FSUSCRI)
	private Timestamp fsuscri;
	@PortableProperty(value = IND_TOTFPVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpvida;
	@PortableProperty(value = IND_TOTFPNAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnavida;
	@PortableProperty(value = IND_TOTFACTVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactvida;
	@PortableProperty(value = IND_TOTCOLAVIDA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolavida;
	@PortableProperty(value = IND_TOTFPFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpfall;
	@PortableProperty(value = IND_TOTFPNAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnafall;
	@PortableProperty(value = IND_TOTFACTFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactfall;
	@PortableProperty(value = IND_TOTCOLAFALL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolafall;
	@PortableProperty(value = IND_TOTFPCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcompl;
	@PortableProperty(value = IND_TOTFPNACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacompl;
	@PortableProperty(value = IND_TOTFACTCOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcompl;
	@PortableProperty(value = IND_TOTCOLACOMPL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacompl;
	@PortableProperty(value = IND_TOTFPGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgto;
	@PortableProperty(value = IND_TOTFPNAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagto;
	@PortableProperty(value = IND_TOTFACTGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgto;
	@PortableProperty(value = IND_TOTCOLAGTO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagto;
	@PortableProperty(value = IND_TOTFPCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpcom;
	@PortableProperty(value = IND_TOTFPNACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnacom;
	@PortableProperty(value = IND_TOTFACTCOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactcom;
	@PortableProperty(value = IND_TOTCOLACOM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolacom;
	@PortableProperty(value = IND_TOTFPRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprte;
	@PortableProperty(value = IND_TOTFPNARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnarte;
	@PortableProperty(value = IND_TOTFACTRTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactrte;
	@PortableProperty(value = IND_TOTCOLARTE, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolarte;
	@PortableProperty(value = IND_TOTFPPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpprim;
	@PortableProperty(value = IND_TOTFPNAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnaprim;
	@PortableProperty(value = IND_TOTFACTPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactprim;
	@PortableProperty(value = IND_TOTCOLAPRIM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolaprim;
	@PortableProperty(value = IND_TOTFPROB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprob;
	@PortableProperty(value = IND_TOTFPROBTANUL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfprobtanul;
	@PortableProperty(value = IND_TOTPROVISION, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totprovision;
	@PortableProperty(value = IND_TOTCOLA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcola;
	@PortableProperty(value = IND_PROVFCAL, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provfcal;
	@PortableProperty(value = IND_TOTRA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totra;
	@PortableProperty(value = IND_TOTCSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcsm;
	@PortableProperty(value = IND_TOTCSMPATRON, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcsmpatron;

	@PortableProperty(IND_KUOA)
	private String kuoa;
	@PortableProperty(IND_TEXTRACCION)
	private String textraccion;
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
	@PortableProperty(value = IND_PB, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal pb;
	
	@PortableProperty(value = IND_BLOQUEGTO_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueGtoImpflujoNoAnulado;
	

	@PortableProperty(value = IND_BLOQUEVIDA_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueVidaImpflujoNoAnulado;
	@PortableProperty(value = IND_BLOQUEFALL_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueFallImpflujoNoAnulado;
	@PortableProperty(value = IND_BLOQUECOMPL_IMPFLUJONOAULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueComplImpflujoNoAnulado;
	@PortableProperty(value = IND_BLOQUECOMI_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueComiImpflujoNoAnulado;
	@PortableProperty(value = IND_BLOQUERTE_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueRteImpflujoaNoAnulado;
	@PortableProperty(value = IND_BLOQUEPRIM_IMPFLUJONOANULADO, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloquePrimImpflujoNoAnulado;
	
	@PortableProperty(IND_SW_PB)
	private String sw_pb;
	
	@PortableProperty(value = IND_PRIMA_UNICA, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal primaUnica;
	
	@PortableProperty(IND_TIPO_PRIMA)
	private String tipoPrima;
	
	@PortableProperty(value = IND_BLOQUEGTOAD_IMPFLUJONOMINAL, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoadImpflujonominal;
	@PortableProperty(value = IND_BLOQUEGTOAD_IMPFLUJOPROBABLE, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoadImpflujoprobable;
	@PortableProperty(value = IND_BLOQUEGTOAD_IMPFLUJOACTUALIZADO, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoadImpflujoactualizado;
	@PortableProperty(value = IND_BLOQUEGTOAD_IMPFLUJONOANULADO , codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal bloqueGtoadImpflujoNoAnulado;
	@PortableProperty(value = IND_TOT_FP_GASTOSAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totFpGastosad;
	@PortableProperty(value = IND_TOTFPGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpgtoad;
	@PortableProperty(value = IND_TOTFPNAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfpnagtoad;
	@PortableProperty(value = IND_TOTFACTGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totfactgtoad;
	@PortableProperty(value = IND_TOTCOLAGTOAD, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal totcolagtoad;
	
	@PortableProperty(value = IND_RAUMIC, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal raUmic;
	
	@PortableProperty(value = IND_ROSSP_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal rosspCsm;
	
	@PortableProperty(value = PROV_ROSSP_CSM, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provRosspCsm;
	
	@PortableProperty(value = PROV_BEL_NIIF17, codec = BigDecimalSolvenciaCodec.class)
	private java.math.BigDecimal provBelNiif17;
	
	@PortableProperty(value = IND_PRIMAPERIODICA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal primaperiodica;
	
	@PortableProperty(value = IND_PRIMAUNICA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal primaunica;
	
	@PortableProperty(value = IND_OGA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal oga;
	
	@PortableProperty(value = IND_SIPRIMA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal siprima;
	
	@PortableProperty(value = IND_COMI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal comi;
	
	@PortableProperty(value = IND_SIOGA, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal sioga;
	
	@PortableProperty(value = IND_BLOQUEVIDA_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquevidaImpprovi;
	
	@PortableProperty(value = IND_BLOQUEFALL_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquefallImpprovi;
	
	@PortableProperty(value = IND_BLOQUEGTO_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoImpprovi;
	
	@PortableProperty(value = IND_BLOQUECOMI_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomiImpprovi;
	
	@PortableProperty(value = IND_BLOQUECOMPL_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquecomplImpprovi;
	
	@PortableProperty(value = IND_BLOQUEPRIM_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloqueprimImpprovi;
	
	@PortableProperty(value = IND_BLOQUERTE_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquerteImpprovi;
	
	@PortableProperty(value = IND_BLOQUEGTOAD_IMPPROVI, codec = BigDecimalSolvenciaCodec.class)
	private BigDecimal bloquegtoadImpprovi;
	
	public java.math.BigDecimal getRaUmic() {
		return raUmic;
	}

	public void setRaUmic(java.math.BigDecimal raUmic) {
		this.raUmic = raUmic;
	}

	public java.math.BigDecimal getRosspCsm() {
		return rosspCsm;
	}

	public void setRosspCsm(java.math.BigDecimal rosspCsm) {
		this.rosspCsm = rosspCsm;
	}

	public java.math.BigDecimal getProvRosspCsm() {
		return provRosspCsm;
	}

	public void setProvRosspCsm(java.math.BigDecimal provRosspCsm) {
		this.provRosspCsm = provRosspCsm;
	}

	public java.math.BigDecimal getProvBelNiif17() {
		return provBelNiif17;
	}

	public void setProvBelNiif17(java.math.BigDecimal provBelNiif17) {
		this.provBelNiif17 = provBelNiif17;
	}
	
	public String getSw_pb() {
		return sw_pb;
	}

	public void setSw_pb(String sw_pb) {
		this.sw_pb = sw_pb;
	}

	public java.math.BigDecimal getBloqueGtoImpflujoNoAnulado() {
		return bloqueGtoImpflujoNoAnulado;
	}

	public void setBloqueGtoImpflujoNoAnulado(java.math.BigDecimal bloqueGtoImpflujoNoAnulado) {
		this.bloqueGtoImpflujoNoAnulado = bloqueGtoImpflujoNoAnulado;
	}

	public java.math.BigDecimal getBloqueVidaImpflujoNoAnulado() {
		return bloqueVidaImpflujoNoAnulado;
	}

	public void setBloqueVidaImpflujoNoAnulado(java.math.BigDecimal bloqueVidaImpflujoNoAnulado) {
		this.bloqueVidaImpflujoNoAnulado = bloqueVidaImpflujoNoAnulado;
	}

	public java.math.BigDecimal getBloqueFallImpflujoNoAnulado() {
		return bloqueFallImpflujoNoAnulado;
	}

	public void setBloqueFallImpflujoNoAnulado(java.math.BigDecimal bloqueFallImpflujoNoAnulado) {
		this.bloqueFallImpflujoNoAnulado = bloqueFallImpflujoNoAnulado;
	}

	public java.math.BigDecimal getBloqueComplImpflujoNoAnulado() {
		return bloqueComplImpflujoNoAnulado;
	}

	public void setBloqueComplImpflujoNoAnulado(java.math.BigDecimal bloqueComplImpflujoNoAnulado) {
		this.bloqueComplImpflujoNoAnulado = bloqueComplImpflujoNoAnulado;
	}

	public java.math.BigDecimal getBloqueComiImpflujoNoAnulado() {
		return bloqueComiImpflujoNoAnulado;
	}

	public void setBloqueComiImpflujoNoAnulado(java.math.BigDecimal bloqueComiImpflujoNoAnulado) {
		this.bloqueComiImpflujoNoAnulado = bloqueComiImpflujoNoAnulado;
	}

	public java.math.BigDecimal getBloqueRteImpflujoaNoAnulado() {
		return bloqueRteImpflujoaNoAnulado;
	}

	public void setBloqueRteImpflujoaNoAnulado(java.math.BigDecimal bloqueRteImpflujoaNoAnulado) {
		this.bloqueRteImpflujoaNoAnulado = bloqueRteImpflujoaNoAnulado;
	}

	public java.math.BigDecimal getBloquePrimImpflujoNoAnulado() {
		return bloquePrimImpflujoNoAnulado;
	}

	public void setBloquePrimImpflujoNoAnulado(java.math.BigDecimal bloquePrimImpflujoNoAnulado) {
		this.bloquePrimImpflujoNoAnulado = bloquePrimImpflujoNoAnulado;
	}
	
	public java.math.BigDecimal getPb() {
		return pb;
	}

	public void setPb(java.math.BigDecimal pb) {
		this.pb = pb;
	}

	public String getKuoa() {
		return kuoa;
	}

	public void setKuoa(String kuoa) {
		this.kuoa = kuoa;
	}

	public String getTextraccion() {
		return textraccion;
	}

	public void setTextraccion(String textraccion) {
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

	
	public Integer getDia() {
		return dia;
	}

	public void setDia(Integer dia) {
		this.dia = dia;
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

	public UmicKey getUmicKey() {
		return umicKey;
	}

	public void setUmicKey(UmicKey umicKey) {
		this.umicKey = umicKey;
	}

	public Timestamp getFechadesde() {
		return fechadesde;
	}

	public void setFechadesde(Timestamp fechadesde) {
		this.fechadesde = fechadesde;
	}

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

	public String getKcarterainv() {
		return kcarterainv;
	}

	public void setKcarterainv(String kcarterainv) {
		this.kcarterainv = kcarterainv;
	}

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

	public String getGapact() {
		return gapact;
	}

	public void setGapact(String gapact) {
		this.gapact = gapact;
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

	public String getKramo() {
		return kramo;
	}

	public void setKramo(String kramo) {
		this.kramo = kramo;
	}

	public String getPrestcal() {
		return prestcal;
	}

	public void setPrestcal(String prestcal) {
		this.prestcal = prestcal;
	}

	public Boolean getNuevaproduc() {
		return nuevaproduc;
	}

	public void setNuevaproduc(Boolean nuevaproduc) {
		this.nuevaproduc = nuevaproduc;
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

	public String getCurvati() {
		return curvati;
	}

	public void setCurvati(String curvati) {
		this.curvati = curvati;
	}

	public BigDecimal getSumfprob() {
		return sumfprob;
	}

	public void setSumfprob(BigDecimal sumfprob) {
		this.sumfprob = sumfprob;
	}

	public BigDecimal getSumfprobtanul() {
		return sumfprobtanul;
	}

	public void setSumfprobtanul(BigDecimal sumfprobtanul) {
		this.sumfprobtanul = sumfprobtanul;
	}

	public BigDecimal getSumprovision() {
		return sumprovision;
	}

	public void setSumprovision(BigDecimal sumprovision) {
		this.sumprovision = sumprovision;
	}

	public BigDecimal getSumcola() {
		return sumcola;
	}

	public void setSumcola(BigDecimal sumcola) {
		this.sumcola = sumcola;
	}

	public BigDecimal getBloquegtoImpflujoprobable() {
		return bloquegtoImpflujoprobable;
	}

	public void setBloquegtoImpflujoprobable(BigDecimal bloquegtoImpflujoprobable) {
		this.bloquegtoImpflujoprobable = bloquegtoImpflujoprobable;
	}

	public BigDecimal getBloquegtoImpflujonominal() {
		return bloquegtoImpflujonominal;
	}

	public void setBloquegtoImpflujonominal(BigDecimal bloquegtoImpflujonominal) {
		this.bloquegtoImpflujonominal = bloquegtoImpflujonominal;
	}

	public BigDecimal getBloquevidaImpflujonominal() {
		return bloquevidaImpflujonominal;
	}

	public void setBloquevidaImpflujonominal(BigDecimal bloquevidaImpflujonominal) {
		this.bloquevidaImpflujonominal = bloquevidaImpflujonominal;
	}

	public BigDecimal getBloquefallImpflujonominal() {
		return bloquefallImpflujonominal;
	}

	public void setBloquefallImpflujonominal(BigDecimal bloquefallImpflujonominal) {
		this.bloquefallImpflujonominal = bloquefallImpflujonominal;
	}

	public BigDecimal getBloquecomplImpflujonominal() {
		return bloquecomplImpflujonominal;
	}

	public void setBloquecomplImpflujonominal(BigDecimal bloquecomplImpflujonominal) {
		this.bloquecomplImpflujonominal = bloquecomplImpflujonominal;
	}

	public BigDecimal getBloquecomiImpflujonominal() {
		return bloquecomiImpflujonominal;
	}

	public void setBloquecomiImpflujonominal(BigDecimal bloquecomiImpflujonominal) {
		this.bloquecomiImpflujonominal = bloquecomiImpflujonominal;
	}

	public BigDecimal getBloquerteImpflujonominal() {
		return bloquerteImpflujonominal;
	}

	public void setBloquerteImpflujonominal(BigDecimal bloquerteImpflujonominal) {
		this.bloquerteImpflujonominal = bloquerteImpflujonominal;
	}

	public BigDecimal getBloqueprimImpflujonominal() {
		return bloqueprimImpflujonominal;
	}

	public void setBloqueprimImpflujonominal(BigDecimal bloqueprimImpflujonominal) {
		this.bloqueprimImpflujonominal = bloqueprimImpflujonominal;
	}

	public String getKprestacion() {
		return kprestacion;
	}

	public void setKprestacion(String kprestacion) {
		this.kprestacion = kprestacion;
	}

	public String getKcoaseOri() {
		return kcoaseOri;
	}

	public void setKcoaseOri(String kcoaseOri) {
		this.kcoaseOri = kcoaseOri;
	}

	public String getSwcasado() {
		return swcasado;
	}

	public void setSwcasado(String swcasado) {
		this.swcasado = swcasado;
	}

	public String getGestionit() {
		return gestionit;
	}

	public void setGestionit(String gestionit) {
		this.gestionit = gestionit;
	}

	public BigDecimal getBloquevidaImpflujoprobable() {
		return bloquevidaImpflujoprobable;
	}

	public void setBloquevidaImpflujoprobable(BigDecimal bloquevidaImpflujoprobable) {
		this.bloquevidaImpflujoprobable = bloquevidaImpflujoprobable;
	}

	public BigDecimal getBloquefallImpflujoprobable() {
		return bloquefallImpflujoprobable;
	}

	public void setBloquefallImpflujoprobable(BigDecimal bloquefallImpflujoprobable) {
		this.bloquefallImpflujoprobable = bloquefallImpflujoprobable;
	}

	public BigDecimal getBloquecomplImpflujoprobable() {
		return bloquecomplImpflujoprobable;
	}

	public void setBloquecomplImpflujoprobable(BigDecimal bloquecomplImpflujoprobable) {
		this.bloquecomplImpflujoprobable = bloquecomplImpflujoprobable;
	}

	public BigDecimal getBloquecomiImpflujoprobable() {
		return bloquecomiImpflujoprobable;
	}

	public void setBloquecomiImpflujoprobable(BigDecimal bloquecomiImpflujoprobable) {
		this.bloquecomiImpflujoprobable = bloquecomiImpflujoprobable;
	}

	public BigDecimal getBloquerteImpflujoprobable() {
		return bloquerteImpflujoprobable;
	}

	public void setBloquerteImpflujoprobable(BigDecimal bloquerteImpflujoprobable) {
		this.bloquerteImpflujoprobable = bloquerteImpflujoprobable;
	}

	public BigDecimal getBloqueprimImpflujoprobable() {
		return bloqueprimImpflujoprobable;
	}

	public void setBloqueprimImpflujoprobable(BigDecimal bloqueprimImpflujoprobable) {
		this.bloqueprimImpflujoprobable = bloqueprimImpflujoprobable;
	}

	public BigDecimal getBloquevidaImpflujoactualizado() {
		return bloquevidaImpflujoactualizado;
	}

	public void setBloquevidaImpflujoactualizado(BigDecimal bloquevidaImpflujoactualizado) {
		this.bloquevidaImpflujoactualizado = bloquevidaImpflujoactualizado;
	}

	public BigDecimal getBloquefallImpflujoactualizado() {
		return bloquefallImpflujoactualizado;
	}

	public void setBloquefallImpflujoactualizado(BigDecimal bloquefallImpflujoactualizado) {
		this.bloquefallImpflujoactualizado = bloquefallImpflujoactualizado;
	}

	public BigDecimal getBloquecomplImpflujoactualizado() {
		return bloquecomplImpflujoactualizado;
	}

	public void setBloquecomplImpflujoactualizado(BigDecimal bloquecomplImpflujoactualizado) {
		this.bloquecomplImpflujoactualizado = bloquecomplImpflujoactualizado;
	}

	public BigDecimal getBloquecomiImpflujoactualizado() {
		return bloquecomiImpflujoactualizado;
	}

	public void setBloquecomiImpflujoactualizado(BigDecimal bloquecomiImpflujoactualizado) {
		this.bloquecomiImpflujoactualizado = bloquecomiImpflujoactualizado;
	}

	public BigDecimal getBloquerteImpflujoactualizado() {
		return bloquerteImpflujoactualizado;
	}

	public void setBloquerteImpflujoactualizado(BigDecimal bloquerteImpflujoactualizado) {
		this.bloquerteImpflujoactualizado = bloquerteImpflujoactualizado;
	}

	public BigDecimal getBloqueprimImpflujoactualizado() {
		return bloqueprimImpflujoactualizado;
	}

	public void setBloqueprimImpflujoactualizado(BigDecimal bloqueprimImpflujoactualizado) {
		this.bloqueprimImpflujoactualizado = bloqueprimImpflujoactualizado;
	}

	public BigDecimal getBloquegtoImpflujoactualizado() {
		return bloquegtoImpflujoactualizado;
	}

	public void setBloquegtoImpflujoactualizado(BigDecimal bloquegtoImpflujoactualizado) {
		this.bloquegtoImpflujoactualizado = bloquegtoImpflujoactualizado;
	}

	public String getSpcom() {
		return spcom;
	}

	public void setSpcom(String spcom) {
		this.spcom = spcom;
	}

	public Integer getKmodext() {
		return kmodext;
	}

	public void setKmodext(Integer kmodext) {
		this.kmodext = kmodext;
	}

	public String getKbencon() {
		return kbencon;
	}

	public void setKbencon(String kbencon) {
		this.kbencon = kbencon;
	}

	public Timestamp getFecinisus() {
		return fecinisus;
	}

	public void setFecinisus(Timestamp fecinisus) {
		this.fecinisus = fecinisus;
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

	public BigDecimal getFactor1() {
		return factor1;
	}

	public void setFactor1(BigDecimal factor1) {
		this.factor1 = factor1;
	}

	public List<Timestamp> getFecinitramo() {
		return fecinitramo;
	}

	public void setFecinitramo(List<Timestamp> fecinitramo) {
		this.fecinitramo = fecinitramo;
	}

	public List<Timestamp> getFecfintramo() {
		return fecfintramo;
	}

	public void setFecfintramo(List<Timestamp> fecfintramo) {
		this.fecfintramo = fecfintramo;
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

	public List<String> getSwcasadolst() {
		return swcasadolst;
	}

	public void setSwcasadolst(List<String> swcasadolst) {
		this.swcasadolst = swcasadolst;
	}

	public String getNegocio() {
		return negocio;
	}

	public void setNegocio(String negocio) {
		this.negocio = negocio;
	}

	public String getKcarteraContrato() {
		return kcarteraContrato;
	}

	public void setKcarteraContrato(String kcarteraContrato) {
		this.kcarteraContrato = kcarteraContrato;
	}

	public String getKcarteraCohort() {
		return kcarteraCohort;
	}

	public void setKcarteraCohort(String kcarteraCohort) {
		this.kcarteraCohort = kcarteraCohort;
	}

	public String getKcarteraOner() {
		return kcarteraOner;
	}

	public void setKcarteraOner(String kcarteraOner) {
		this.kcarteraOner = kcarteraOner;
	}

	public Timestamp getFdesde() {
		return fdesde;
	}

	public void setFdesde(Timestamp fdesde) {
		this.fdesde = fdesde;
	}

	public Timestamp getFhasta() {
		return fhasta;
	}

	public void setFhasta(Timestamp fhasta) {
		this.fhasta = fhasta;
	}
	
	public java.math.BigDecimal getTotFpVida() {
		return totFpVida;
	}
	
	public void setTotFpVida(java.math.BigDecimal totFpVida) {
		this.totFpVida = totFpVida;
	}
	
	public java.math.BigDecimal getTotFpFall() {
		return totFpFall;
	}
	
	public void setTotFpFall(java.math.BigDecimal totFpFall) {
		this.totFpFall = totFpFall;
	}
	
	public java.math.BigDecimal getTotFpCompl() {
		return totFpCompl;
	}
	
	public void setTotFpCompl(java.math.BigDecimal totFpCompl) {
		this.totFpCompl = totFpCompl;
	}
	
	public java.math.BigDecimal getTotFpGastos() {
		return totFpGastos;
	}
	
	public void setTotFpGastos(java.math.BigDecimal totFpGastos) {
		this.totFpGastos = totFpGastos;
	}

	public java.math.BigDecimal getTotFpComisiones() {
		return totFpComisiones;
	}

	public void setTotFpComisiones(java.math.BigDecimal totFpComisiones) {
		this.totFpComisiones = totFpComisiones;
	}

	public java.math.BigDecimal getTotFpRescates() {
		return totFpRescates;
	}

	public void setTotFpRescates(java.math.BigDecimal totFpRescates) {
		this.totFpRescates = totFpRescates;
	}

	public java.math.BigDecimal getTotFpPrimas() {
		return totFpPrimas;
	}

	public void setTotFpPrimas(java.math.BigDecimal totFpPrimas) {
		this.totFpPrimas = totFpPrimas;
	}

	public Integer getCcartera() {
		return ccartera;
	}

	public void setCcartera(Integer ccartera) {
		this.ccartera = ccartera;
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

	public Integer getKcertificado() {
		return kcertificado;
	}

	public void setKcertificado(Integer kcertificado) {
		this.kcertificado = kcertificado;
	}

	public Integer getNorden() {
		return norden;
	}

	public void setNorden(Integer norden) {
		this.norden = norden;
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

	public java.math.BigDecimal getTotfpvida() {
		return totfpvida;
	}

	public void setTotfpvida(java.math.BigDecimal totfpvida) {
		this.totfpvida = totfpvida;
	}

	public java.math.BigDecimal getTotfpnavida() {
		return totfpnavida;
	}

	public void setTotfpnavida(java.math.BigDecimal totfpnavida) {
		this.totfpnavida = totfpnavida;
	}

	public java.math.BigDecimal getTotfactvida() {
		return totfactvida;
	}

	public void setTotfactvida(java.math.BigDecimal totfactvida) {
		this.totfactvida = totfactvida;
	}

	public java.math.BigDecimal getTotcolavida() {
		return totcolavida;
	}

	public void setTotcolavida(java.math.BigDecimal totcolavida) {
		this.totcolavida = totcolavida;
	}

	public java.math.BigDecimal getTotfpfall() {
		return totfpfall;
	}

	public void setTotfpfall(java.math.BigDecimal totfpfall) {
		this.totfpfall = totfpfall;
	}

	public java.math.BigDecimal getTotfpnafall() {
		return totfpnafall;
	}

	public void setTotfpnafall(java.math.BigDecimal totfpnafall) {
		this.totfpnafall = totfpnafall;
	}

	public java.math.BigDecimal getTotfactfall() {
		return totfactfall;
	}

	public void setTotfactfall(java.math.BigDecimal totfactfall) {
		this.totfactfall = totfactfall;
	}

	public java.math.BigDecimal getTotcolafall() {
		return totcolafall;
	}

	public void setTotcolafall(java.math.BigDecimal totcolafall) {
		this.totcolafall = totcolafall;
	}

	public java.math.BigDecimal getTotfpcompl() {
		return totfpcompl;
	}

	public void setTotfpcompl(java.math.BigDecimal totfpcompl) {
		this.totfpcompl = totfpcompl;
	}

	public java.math.BigDecimal getTotfpnacompl() {
		return totfpnacompl;
	}

	public void setTotfpnacompl(java.math.BigDecimal totfpnacompl) {
		this.totfpnacompl = totfpnacompl;
	}

	public java.math.BigDecimal getTotfactcompl() {
		return totfactcompl;
	}

	public void setTotfactcompl(java.math.BigDecimal totfactcompl) {
		this.totfactcompl = totfactcompl;
	}

	public java.math.BigDecimal getTotcolacompl() {
		return totcolacompl;
	}

	public void setTotcolacompl(java.math.BigDecimal totcolacompl) {
		this.totcolacompl = totcolacompl;
	}

	public java.math.BigDecimal getTotfpgto() {
		return totfpgto;
	}

	public void setTotfpgto(java.math.BigDecimal totfpgto) {
		this.totfpgto = totfpgto;
	}

	public java.math.BigDecimal getTotfpnagto() {
		return totfpnagto;
	}

	public void setTotfpnagto(java.math.BigDecimal totfpnagto) {
		this.totfpnagto = totfpnagto;
	}

	public java.math.BigDecimal getTotfactgto() {
		return totfactgto;
	}

	public void setTotfactgto(java.math.BigDecimal totfactgto) {
		this.totfactgto = totfactgto;
	}

	public java.math.BigDecimal getTotcolagto() {
		return totcolagto;
	}

	public void setTotcolagto(java.math.BigDecimal totcolagto) {
		this.totcolagto = totcolagto;
	}

	public java.math.BigDecimal getTotfpcom() {
		return totfpcom;
	}

	public void setTotfpcom(java.math.BigDecimal totfpcom) {
		this.totfpcom = totfpcom;
	}

	public java.math.BigDecimal getTotfpnacom() {
		return totfpnacom;
	}

	public void setTotfpnacom(java.math.BigDecimal totfpnacom) {
		this.totfpnacom = totfpnacom;
	}

	public java.math.BigDecimal getTotfactcom() {
		return totfactcom;
	}

	public void setTotfactcom(java.math.BigDecimal totfactcom) {
		this.totfactcom = totfactcom;
	}

	public java.math.BigDecimal getTotcolacom() {
		return totcolacom;
	}

	public void setTotcolacom(java.math.BigDecimal totcolacom) {
		this.totcolacom = totcolacom;
	}

	public java.math.BigDecimal getTotfprte() {
		return totfprte;
	}

	public void setTotfprte(java.math.BigDecimal totfprte) {
		this.totfprte = totfprte;
	}

	public java.math.BigDecimal getTotfpnarte() {
		return totfpnarte;
	}

	public void setTotfpnarte(java.math.BigDecimal totfpnarte) {
		this.totfpnarte = totfpnarte;
	}

	public java.math.BigDecimal getTotfactrte() {
		return totfactrte;
	}

	public void setTotfactrte(java.math.BigDecimal totfactrte) {
		this.totfactrte = totfactrte;
	}

	public java.math.BigDecimal getTotcolarte() {
		return totcolarte;
	}

	public void setTotcolarte(java.math.BigDecimal totcolarte) {
		this.totcolarte = totcolarte;
	}

	public java.math.BigDecimal getTotfpprim() {
		return totfpprim;
	}

	public void setTotfpprim(java.math.BigDecimal totfpprim) {
		this.totfpprim = totfpprim;
	}

	public java.math.BigDecimal getTotfpnaprim() {
		return totfpnaprim;
	}

	public void setTotfpnaprim(java.math.BigDecimal totfpnaprim) {
		this.totfpnaprim = totfpnaprim;
	}

	public java.math.BigDecimal getTotfactprim() {
		return totfactprim;
	}

	public void setTotfactprim(java.math.BigDecimal totfactprim) {
		this.totfactprim = totfactprim;
	}

	public java.math.BigDecimal getTotcolaprim() {
		return totcolaprim;
	}

	public void setTotcolaprim(java.math.BigDecimal totcolaprim) {
		this.totcolaprim = totcolaprim;
	}

	public java.math.BigDecimal getTotfprob() {
		return totfprob;
	}

	public void setTotfprob(java.math.BigDecimal totfprob) {
		this.totfprob = totfprob;
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

	public java.math.BigDecimal getTotcola() {
		return totcola;
	}

	public void setTotcola(java.math.BigDecimal totcola) {
		this.totcola = totcola;
	}

	public java.math.BigDecimal getProvfcal() {
		return provfcal;
	}

	public void setProvfcal(java.math.BigDecimal provfcal) {
		this.provfcal = provfcal;
	}

	public java.math.BigDecimal getTotra() {
		return totra;
	}

	public void setTotra(java.math.BigDecimal totra) {
		this.totra = totra;
	}

	public java.math.BigDecimal getTotcsm() {
		return totcsm;
	}

	public void setTotcsm(java.math.BigDecimal totcsm) {
		this.totcsm = totcsm;
	}

	public java.math.BigDecimal getTotcsmpatron() {
		return totcsmpatron;
	}

	public void setTotcsmpatron(java.math.BigDecimal totcsmpatron) {
		this.totcsmpatron = totcsmpatron;
	}

	public java.math.BigDecimal getPrimaUnica() {
		return primaUnica;
	}

	public void setPrimaUnica(java.math.BigDecimal primaUnica) {
		this.primaUnica = primaUnica;
	}

	public String getTipoPrima() {
		return tipoPrima;
	}

	public void setTipoPrima(String tipoPrima) {
		this.tipoPrima = tipoPrima;
	}

	public BigDecimal getBloquegtoadImpflujonominal() {
		return bloquegtoadImpflujonominal;
	}

	public void setBloquegtoadImpflujonominal(BigDecimal bloquegtoadImpflujonominal) {
		this.bloquegtoadImpflujonominal = bloquegtoadImpflujonominal;
	}

	public BigDecimal getBloquegtoadImpflujoprobable() {
		return bloquegtoadImpflujoprobable;
	}

	public void setBloquegtoadImpflujoprobable(BigDecimal bloquegtoadImpflujoprobable) {
		this.bloquegtoadImpflujoprobable = bloquegtoadImpflujoprobable;
	}

	public BigDecimal getBloquegtoadImpflujoactualizado() {
		return bloquegtoadImpflujoactualizado;
	}

	public void setBloquegtoadImpflujoactualizado(BigDecimal bloquegtoadImpflujoactualizado) {
		this.bloquegtoadImpflujoactualizado = bloquegtoadImpflujoactualizado;
	}

	public java.math.BigDecimal getBloqueGtoadImpflujoNoAnulado() {
		return bloqueGtoadImpflujoNoAnulado;
	}

	public void setBloqueGtoadImpflujoNoAnulado(java.math.BigDecimal bloqueGtoadImpflujoNoAnulado) {
		this.bloqueGtoadImpflujoNoAnulado = bloqueGtoadImpflujoNoAnulado;
	}

	public java.math.BigDecimal getTotFpGastosad() {
		return totFpGastosad;
	}

	public void setTotFpGastosad(java.math.BigDecimal totFpGastosad) {
		this.totFpGastosad = totFpGastosad;
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

	public java.math.BigDecimal getTotfactgtoad() {
		return totfactgtoad;
	}

	public void setTotfactgtoad(java.math.BigDecimal totfactgtoad) {
		this.totfactgtoad = totfactgtoad;
	}

	public java.math.BigDecimal getTotcolagtoad() {
		return totcolagtoad;
	}

	public void setTotcolagtoad(java.math.BigDecimal totcolagtoad) {
		this.totcolagtoad = totcolagtoad;
	}
	
	public BigDecimal getPrimaperiodica() {
		return primaperiodica;
	}

	public void setPrimaperiodica(BigDecimal primaperiodica) {
		this.primaperiodica = primaperiodica;
	}

	public BigDecimal getPrimaunica() {
		return primaunica;
	}

	public void setPrimaunica(BigDecimal primaunica) {
		this.primaunica = primaunica;
	}

	public BigDecimal getOga() {
		return oga;
	}

	public void setOga(BigDecimal oga) {
		this.oga = oga;
	}

	public BigDecimal getSiprima() {
		return siprima;
	}

	public void setSiprima(BigDecimal siprima) {
		this.siprima = siprima;
	}

	public BigDecimal getBloquevidaImpprovi() {
		return bloquevidaImpprovi;
	}

	public void setBloquevidaImpprovi(BigDecimal bloquevidaImpprovi) {
		this.bloquevidaImpprovi = bloquevidaImpprovi;
	}

	public BigDecimal getBloquefallImpprovi() {
		return bloquefallImpprovi;
	}

	public void setBloquefallImpprovi(BigDecimal bloquefallImpprovi) {
		this.bloquefallImpprovi = bloquefallImpprovi;
	}

	public BigDecimal getBloquegtoImpprovi() {
		return bloquegtoImpprovi;
	}

	public void setBloquegtoImpprovi(BigDecimal bloquegtoImpprovi) {
		this.bloquegtoImpprovi = bloquegtoImpprovi;
	}

	public BigDecimal getBloquecomiImpprovi() {
		return bloquecomiImpprovi;
	}

	public void setBloquecomiImpprovi(BigDecimal bloquecomiImpprovi) {
		this.bloquecomiImpprovi = bloquecomiImpprovi;
	}

	public BigDecimal getBloquecomplImpprovi() {
		return bloquecomplImpprovi;
	}

	public void setBloquecomplImpprovi(BigDecimal bloquecomplImpprovi) {
		this.bloquecomplImpprovi = bloquecomplImpprovi;
	}

	public BigDecimal getBloqueprimImpprovi() {
		return bloqueprimImpprovi;
	}

	public void setBloqueprimImpprovi(BigDecimal bloqueprimImpprovi) {
		this.bloqueprimImpprovi = bloqueprimImpprovi;
	}

	public BigDecimal getBloquerteImpprovi() {
		return bloquerteImpprovi;
	}

	public void setBloquerteImpprovi(BigDecimal bloquerteImpprovi) {
		this.bloquerteImpprovi = bloquerteImpprovi;
	}

	public BigDecimal getBloquegtoadImpprovi() {
		return bloquegtoadImpprovi;
	}

	public void setBloquegtoadImpprovi(BigDecimal bloquegtoadImpprovi) {
		this.bloquegtoadImpprovi = bloquegtoadImpprovi;
	}

	public BigDecimal getComi() {
		return comi;
	}

	public void setComi(BigDecimal comi) {
		this.comi = comi;
	}
	
	public BigDecimal getSioga() {
		return sioga;
	}

	public void setSioga(BigDecimal sioga) {
		this.sioga = sioga;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bloqueComiImpflujoNoAnulado == null) ? 0 : bloqueComiImpflujoNoAnulado.hashCode());
		result = prime * result
				+ ((bloqueComplImpflujoNoAnulado == null) ? 0 : bloqueComplImpflujoNoAnulado.hashCode());
		result = prime * result + ((bloqueFallImpflujoNoAnulado == null) ? 0 : bloqueFallImpflujoNoAnulado.hashCode());
		result = prime * result + ((bloqueGtoImpflujoNoAnulado == null) ? 0 : bloqueGtoImpflujoNoAnulado.hashCode());
		result = prime * result
				+ ((bloqueGtoadImpflujoNoAnulado == null) ? 0 : bloqueGtoadImpflujoNoAnulado.hashCode());
		result = prime * result + ((bloquePrimImpflujoNoAnulado == null) ? 0 : bloquePrimImpflujoNoAnulado.hashCode());
		result = prime * result + ((bloqueRteImpflujoaNoAnulado == null) ? 0 : bloqueRteImpflujoaNoAnulado.hashCode());
		result = prime * result + ((bloqueVidaImpflujoNoAnulado == null) ? 0 : bloqueVidaImpflujoNoAnulado.hashCode());
		result = prime * result
				+ ((bloquecomiImpflujoactualizado == null) ? 0 : bloquecomiImpflujoactualizado.hashCode());
		result = prime * result + ((bloquecomiImpflujonominal == null) ? 0 : bloquecomiImpflujonominal.hashCode());
		result = prime * result + ((bloquecomiImpflujoprobable == null) ? 0 : bloquecomiImpflujoprobable.hashCode());
		result = prime * result + ((bloquecomiImpprovi == null) ? 0 : bloquecomiImpprovi.hashCode());
		result = prime * result
				+ ((bloquecomplImpflujoactualizado == null) ? 0 : bloquecomplImpflujoactualizado.hashCode());
		result = prime * result + ((bloquecomplImpflujonominal == null) ? 0 : bloquecomplImpflujonominal.hashCode());
		result = prime * result + ((bloquecomplImpflujoprobable == null) ? 0 : bloquecomplImpflujoprobable.hashCode());
		result = prime * result + ((bloquecomplImpprovi == null) ? 0 : bloquecomplImpprovi.hashCode());
		result = prime * result
				+ ((bloquefallImpflujoactualizado == null) ? 0 : bloquefallImpflujoactualizado.hashCode());
		result = prime * result + ((bloquefallImpflujonominal == null) ? 0 : bloquefallImpflujonominal.hashCode());
		result = prime * result + ((bloquefallImpflujoprobable == null) ? 0 : bloquefallImpflujoprobable.hashCode());
		result = prime * result + ((bloquefallImpprovi == null) ? 0 : bloquefallImpprovi.hashCode());
		result = prime * result
				+ ((bloquegtoImpflujoactualizado == null) ? 0 : bloquegtoImpflujoactualizado.hashCode());
		result = prime * result + ((bloquegtoImpflujonominal == null) ? 0 : bloquegtoImpflujonominal.hashCode());
		result = prime * result + ((bloquegtoImpflujoprobable == null) ? 0 : bloquegtoImpflujoprobable.hashCode());
		result = prime * result + ((bloquegtoImpprovi == null) ? 0 : bloquegtoImpprovi.hashCode());
		result = prime * result
				+ ((bloquegtoadImpflujoactualizado == null) ? 0 : bloquegtoadImpflujoactualizado.hashCode());
		result = prime * result + ((bloquegtoadImpflujonominal == null) ? 0 : bloquegtoadImpflujonominal.hashCode());
		result = prime * result + ((bloquegtoadImpflujoprobable == null) ? 0 : bloquegtoadImpflujoprobable.hashCode());
		result = prime * result + ((bloquegtoadImpprovi == null) ? 0 : bloquegtoadImpprovi.hashCode());
		result = prime * result
				+ ((bloqueprimImpflujoactualizado == null) ? 0 : bloqueprimImpflujoactualizado.hashCode());
		result = prime * result + ((bloqueprimImpflujonominal == null) ? 0 : bloqueprimImpflujonominal.hashCode());
		result = prime * result + ((bloqueprimImpflujoprobable == null) ? 0 : bloqueprimImpflujoprobable.hashCode());
		result = prime * result + ((bloqueprimImpprovi == null) ? 0 : bloqueprimImpprovi.hashCode());
		result = prime * result
				+ ((bloquerteImpflujoactualizado == null) ? 0 : bloquerteImpflujoactualizado.hashCode());
		result = prime * result + ((bloquerteImpflujonominal == null) ? 0 : bloquerteImpflujonominal.hashCode());
		result = prime * result + ((bloquerteImpflujoprobable == null) ? 0 : bloquerteImpflujoprobable.hashCode());
		result = prime * result + ((bloquerteImpprovi == null) ? 0 : bloquerteImpprovi.hashCode());
		result = prime * result
				+ ((bloquevidaImpflujoactualizado == null) ? 0 : bloquevidaImpflujoactualizado.hashCode());
		result = prime * result + ((bloquevidaImpflujonominal == null) ? 0 : bloquevidaImpflujonominal.hashCode());
		result = prime * result + ((bloquevidaImpflujoprobable == null) ? 0 : bloquevidaImpflujoprobable.hashCode());
		result = prime * result + ((bloquevidaImpprovi == null) ? 0 : bloquevidaImpprovi.hashCode());
		result = prime * result + ((bt == null) ? 0 : bt.hashCode());
		result = prime * result + ((ccanal == null) ? 0 : ccanal.hashCode());
		result = prime * result + ((ccartera == null) ? 0 : ccartera.hashCode());
		result = prime * result + ((cnegocio == null) ? 0 : cnegocio.hashCode());
		result = prime * result + ((codreaseg == null) ? 0 : codreaseg.hashCode());
		result = prime * result + ((comi == null) ? 0 : comi.hashCode());
		result = prime * result + ((ctipoaport == null) ? 0 : ctipoaport.hashCode());
		result = prime * result + ((curvati == null) ? 0 : curvati.hashCode());
		result = prime * result + ((dia == null) ? 0 : dia.hashCode());
		result = prime * result + ((factor1 == null) ? 0 : factor1.hashCode());
		result = prime * result + ((fcierre == null) ? 0 : fcierre.hashCode());
		result = prime * result + ((fdesde == null) ? 0 : fdesde.hashCode());
		result = prime * result + ((fecFinTramo1 == null) ? 0 : fecFinTramo1.hashCode());
		result = prime * result + ((fecfintramo == null) ? 0 : fecfintramo.hashCode());
		result = prime * result + ((fechadesde == null) ? 0 : fechadesde.hashCode());
		result = prime * result + ((fecinisus == null) ? 0 : fecinisus.hashCode());
		result = prime * result + ((fecinitramo == null) ? 0 : fecinitramo.hashCode());
		result = prime * result + ((fhasta == null) ? 0 : fhasta.hashCode());
		result = prime * result + ((fproyflujest == null) ? 0 : fproyflujest.hashCode());
		result = prime * result + ((fsuscri == null) ? 0 : fsuscri.hashCode());
		result = prime * result + ((gapact == null) ? 0 : gapact.hashCode());
		result = prime * result + ((gestionit == null) ? 0 : gestionit.hashCode());
		result = prime * result + ((gtoUni == null) ? 0 : gtoUni.hashCode());
		result = prime * result + ((gtoprov == null) ? 0 : gtoprov.hashCode());
		result = prime * result + ((intfeccal == null) ? 0 : intfeccal.hashCode());
		result = prime * result + ((kajuste == null) ? 0 : kajuste.hashCode());
		result = prime * result + ((kbencon == null) ? 0 : kbencon.hashCode());
		result = prime * result + ((kcarteraCohort == null) ? 0 : kcarteraCohort.hashCode());
		result = prime * result + ((kcarteraContrato == null) ? 0 : kcarteraContrato.hashCode());
		result = prime * result + ((kcarteraOner == null) ? 0 : kcarteraOner.hashCode());
		result = prime * result + ((kcarterainv == null) ? 0 : kcarterainv.hashCode());
		result = prime * result + ((kcertificado == null) ? 0 : kcertificado.hashCode());
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
		result = prime * result + ((kmodext == null) ? 0 : kmodext.hashCode());
		result = prime * result + ((kpoliza == null) ? 0 : kpoliza.hashCode());
		result = prime * result + ((kprestacion == null) ? 0 : kprestacion.hashCode());
		result = prime * result + ((kramo == null) ? 0 : kramo.hashCode());
		result = prime * result + ((ksubpoliza == null) ? 0 : ksubpoliza.hashCode());
		result = prime * result + ((kuoa == null) ? 0 : kuoa.hashCode());
		result = prime * result + ((mon_fall == null) ? 0 : mon_fall.hashCode());
		result = prime * result + ((mon_gto == null) ? 0 : mon_gto.hashCode());
		result = prime * result + ((mon_gtoadq == null) ? 0 : mon_gtoadq.hashCode());
		result = prime * result + ((mon_pb == null) ? 0 : mon_pb.hashCode());
		result = prime * result + ((mon_prim == null) ? 0 : mon_prim.hashCode());
		result = prime * result + ((mon_rte == null) ? 0 : mon_rte.hashCode());
		result = prime * result + ((mon_vida == null) ? 0 : mon_vida.hashCode());
		result = prime * result + ((negocio == null) ? 0 : negocio.hashCode());
		result = prime * result + ((norden == null) ? 0 : norden.hashCode());
		result = prime * result + ((nsuscri == null) ? 0 : nsuscri.hashCode());
		result = prime * result + ((nuevaproduc == null) ? 0 : nuevaproduc.hashCode());
		result = prime * result + ((oga == null) ? 0 : oga.hashCode());
		result = prime * result + ((pb == null) ? 0 : pb.hashCode());
		result = prime * result + ((pgastgesin1I == null) ? 0 : pgastgesin1I.hashCode());
		result = prime * result + ((pgastgesin2I == null) ? 0 : pgastgesin2I.hashCode());
		result = prime * result + ((pintertecnI1 == null) ? 0 : pintertecnI1.hashCode());
		result = prime * result + ((pintertecnI2 == null) ? 0 : pintertecnI2.hashCode());
		result = prime * result + ((prestcal == null) ? 0 : prestcal.hashCode());
		result = prime * result + ((primaUnica == null) ? 0 : primaUnica.hashCode());
		result = prime * result + ((primaperiodica == null) ? 0 : primaperiodica.hashCode());
		result = prime * result + ((primaunica == null) ? 0 : primaunica.hashCode());
		result = prime * result + ((provBelNiif17 == null) ? 0 : provBelNiif17.hashCode());
		result = prime * result + ((provRosspCsm == null) ? 0 : provRosspCsm.hashCode());
		result = prime * result + ((provfcal == null) ? 0 : provfcal.hashCode());
		result = prime * result + ((pventa == null) ? 0 : pventa.hashCode());
		result = prime * result + ((raUmic == null) ? 0 : raUmic.hashCode());
		result = prime * result + ((rosspCsm == null) ? 0 : rosspCsm.hashCode());
		result = prime * result + ((segmento1 == null) ? 0 : segmento1.hashCode());
		result = prime * result + ((sioga == null) ? 0 : sioga.hashCode());
		result = prime * result + ((siprima == null) ? 0 : siprima.hashCode());
		result = prime * result + ((spcom == null) ? 0 : spcom.hashCode());
		result = prime * result + ((sumcola == null) ? 0 : sumcola.hashCode());
		result = prime * result + ((sumfprob == null) ? 0 : sumfprob.hashCode());
		result = prime * result + ((sumfprobtanul == null) ? 0 : sumfprobtanul.hashCode());
		result = prime * result + ((sumprovision == null) ? 0 : sumprovision.hashCode());
		result = prime * result + ((sw_pb == null) ? 0 : sw_pb.hashCode());
		result = prime * result + ((swcasado == null) ? 0 : swcasado.hashCode());
		result = prime * result + ((swcasadolst == null) ? 0 : swcasadolst.hashCode());
		result = prime * result + ((tabla1Aseg1 == null) ? 0 : tabla1Aseg1.hashCode());
		result = prime * result + ((textraccion == null) ? 0 : textraccion.hashCode());
		result = prime * result + ((tipoPrima == null) ? 0 : tipoPrima.hashCode());
		result = prime * result + ((tiposubriesgo == null) ? 0 : tiposubriesgo.hashCode());
		result = prime * result + ((tnegocio == null) ? 0 : tnegocio.hashCode());
		result = prime * result + ((tnegociolrc == null) ? 0 : tnegociolrc.hashCode());
		result = prime * result + ((totFpComisiones == null) ? 0 : totFpComisiones.hashCode());
		result = prime * result + ((totFpCompl == null) ? 0 : totFpCompl.hashCode());
		result = prime * result + ((totFpFall == null) ? 0 : totFpFall.hashCode());
		result = prime * result + ((totFpGastos == null) ? 0 : totFpGastos.hashCode());
		result = prime * result + ((totFpGastosad == null) ? 0 : totFpGastosad.hashCode());
		result = prime * result + ((totFpPrimas == null) ? 0 : totFpPrimas.hashCode());
		result = prime * result + ((totFpRescates == null) ? 0 : totFpRescates.hashCode());
		result = prime * result + ((totFpVida == null) ? 0 : totFpVida.hashCode());
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
		DetalleCorrienteEntregables other = (DetalleCorrienteEntregables) obj;
		if (bloqueComiImpflujoNoAnulado == null) {
			if (other.bloqueComiImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueComiImpflujoNoAnulado.equals(other.bloqueComiImpflujoNoAnulado))
			return false;
		if (bloqueComplImpflujoNoAnulado == null) {
			if (other.bloqueComplImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueComplImpflujoNoAnulado.equals(other.bloqueComplImpflujoNoAnulado))
			return false;
		if (bloqueFallImpflujoNoAnulado == null) {
			if (other.bloqueFallImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueFallImpflujoNoAnulado.equals(other.bloqueFallImpflujoNoAnulado))
			return false;
		if (bloqueGtoImpflujoNoAnulado == null) {
			if (other.bloqueGtoImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueGtoImpflujoNoAnulado.equals(other.bloqueGtoImpflujoNoAnulado))
			return false;
		if (bloqueGtoadImpflujoNoAnulado == null) {
			if (other.bloqueGtoadImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueGtoadImpflujoNoAnulado.equals(other.bloqueGtoadImpflujoNoAnulado))
			return false;
		if (bloquePrimImpflujoNoAnulado == null) {
			if (other.bloquePrimImpflujoNoAnulado != null)
				return false;
		} else if (!bloquePrimImpflujoNoAnulado.equals(other.bloquePrimImpflujoNoAnulado))
			return false;
		if (bloqueRteImpflujoaNoAnulado == null) {
			if (other.bloqueRteImpflujoaNoAnulado != null)
				return false;
		} else if (!bloqueRteImpflujoaNoAnulado.equals(other.bloqueRteImpflujoaNoAnulado))
			return false;
		if (bloqueVidaImpflujoNoAnulado == null) {
			if (other.bloqueVidaImpflujoNoAnulado != null)
				return false;
		} else if (!bloqueVidaImpflujoNoAnulado.equals(other.bloqueVidaImpflujoNoAnulado))
			return false;
		if (bloquecomiImpflujoactualizado == null) {
			if (other.bloquecomiImpflujoactualizado != null)
				return false;
		} else if (!bloquecomiImpflujoactualizado.equals(other.bloquecomiImpflujoactualizado))
			return false;
		if (bloquecomiImpflujonominal == null) {
			if (other.bloquecomiImpflujonominal != null)
				return false;
		} else if (!bloquecomiImpflujonominal.equals(other.bloquecomiImpflujonominal))
			return false;
		if (bloquecomiImpflujoprobable == null) {
			if (other.bloquecomiImpflujoprobable != null)
				return false;
		} else if (!bloquecomiImpflujoprobable.equals(other.bloquecomiImpflujoprobable))
			return false;
		if (bloquecomiImpprovi == null) {
			if (other.bloquecomiImpprovi != null)
				return false;
		} else if (!bloquecomiImpprovi.equals(other.bloquecomiImpprovi))
			return false;
		if (bloquecomplImpflujoactualizado == null) {
			if (other.bloquecomplImpflujoactualizado != null)
				return false;
		} else if (!bloquecomplImpflujoactualizado.equals(other.bloquecomplImpflujoactualizado))
			return false;
		if (bloquecomplImpflujonominal == null) {
			if (other.bloquecomplImpflujonominal != null)
				return false;
		} else if (!bloquecomplImpflujonominal.equals(other.bloquecomplImpflujonominal))
			return false;
		if (bloquecomplImpflujoprobable == null) {
			if (other.bloquecomplImpflujoprobable != null)
				return false;
		} else if (!bloquecomplImpflujoprobable.equals(other.bloquecomplImpflujoprobable))
			return false;
		if (bloquecomplImpprovi == null) {
			if (other.bloquecomplImpprovi != null)
				return false;
		} else if (!bloquecomplImpprovi.equals(other.bloquecomplImpprovi))
			return false;
		if (bloquefallImpflujoactualizado == null) {
			if (other.bloquefallImpflujoactualizado != null)
				return false;
		} else if (!bloquefallImpflujoactualizado.equals(other.bloquefallImpflujoactualizado))
			return false;
		if (bloquefallImpflujonominal == null) {
			if (other.bloquefallImpflujonominal != null)
				return false;
		} else if (!bloquefallImpflujonominal.equals(other.bloquefallImpflujonominal))
			return false;
		if (bloquefallImpflujoprobable == null) {
			if (other.bloquefallImpflujoprobable != null)
				return false;
		} else if (!bloquefallImpflujoprobable.equals(other.bloquefallImpflujoprobable))
			return false;
		if (bloquefallImpprovi == null) {
			if (other.bloquefallImpprovi != null)
				return false;
		} else if (!bloquefallImpprovi.equals(other.bloquefallImpprovi))
			return false;
		if (bloquegtoImpflujoactualizado == null) {
			if (other.bloquegtoImpflujoactualizado != null)
				return false;
		} else if (!bloquegtoImpflujoactualizado.equals(other.bloquegtoImpflujoactualizado))
			return false;
		if (bloquegtoImpflujonominal == null) {
			if (other.bloquegtoImpflujonominal != null)
				return false;
		} else if (!bloquegtoImpflujonominal.equals(other.bloquegtoImpflujonominal))
			return false;
		if (bloquegtoImpflujoprobable == null) {
			if (other.bloquegtoImpflujoprobable != null)
				return false;
		} else if (!bloquegtoImpflujoprobable.equals(other.bloquegtoImpflujoprobable))
			return false;
		if (bloquegtoImpprovi == null) {
			if (other.bloquegtoImpprovi != null)
				return false;
		} else if (!bloquegtoImpprovi.equals(other.bloquegtoImpprovi))
			return false;
		if (bloquegtoadImpflujoactualizado == null) {
			if (other.bloquegtoadImpflujoactualizado != null)
				return false;
		} else if (!bloquegtoadImpflujoactualizado.equals(other.bloquegtoadImpflujoactualizado))
			return false;
		if (bloquegtoadImpflujonominal == null) {
			if (other.bloquegtoadImpflujonominal != null)
				return false;
		} else if (!bloquegtoadImpflujonominal.equals(other.bloquegtoadImpflujonominal))
			return false;
		if (bloquegtoadImpflujoprobable == null) {
			if (other.bloquegtoadImpflujoprobable != null)
				return false;
		} else if (!bloquegtoadImpflujoprobable.equals(other.bloquegtoadImpflujoprobable))
			return false;
		if (bloquegtoadImpprovi == null) {
			if (other.bloquegtoadImpprovi != null)
				return false;
		} else if (!bloquegtoadImpprovi.equals(other.bloquegtoadImpprovi))
			return false;
		if (bloqueprimImpflujoactualizado == null) {
			if (other.bloqueprimImpflujoactualizado != null)
				return false;
		} else if (!bloqueprimImpflujoactualizado.equals(other.bloqueprimImpflujoactualizado))
			return false;
		if (bloqueprimImpflujonominal == null) {
			if (other.bloqueprimImpflujonominal != null)
				return false;
		} else if (!bloqueprimImpflujonominal.equals(other.bloqueprimImpflujonominal))
			return false;
		if (bloqueprimImpflujoprobable == null) {
			if (other.bloqueprimImpflujoprobable != null)
				return false;
		} else if (!bloqueprimImpflujoprobable.equals(other.bloqueprimImpflujoprobable))
			return false;
		if (bloqueprimImpprovi == null) {
			if (other.bloqueprimImpprovi != null)
				return false;
		} else if (!bloqueprimImpprovi.equals(other.bloqueprimImpprovi))
			return false;
		if (bloquerteImpflujoactualizado == null) {
			if (other.bloquerteImpflujoactualizado != null)
				return false;
		} else if (!bloquerteImpflujoactualizado.equals(other.bloquerteImpflujoactualizado))
			return false;
		if (bloquerteImpflujonominal == null) {
			if (other.bloquerteImpflujonominal != null)
				return false;
		} else if (!bloquerteImpflujonominal.equals(other.bloquerteImpflujonominal))
			return false;
		if (bloquerteImpflujoprobable == null) {
			if (other.bloquerteImpflujoprobable != null)
				return false;
		} else if (!bloquerteImpflujoprobable.equals(other.bloquerteImpflujoprobable))
			return false;
		if (bloquerteImpprovi == null) {
			if (other.bloquerteImpprovi != null)
				return false;
		} else if (!bloquerteImpprovi.equals(other.bloquerteImpprovi))
			return false;
		if (bloquevidaImpflujoactualizado == null) {
			if (other.bloquevidaImpflujoactualizado != null)
				return false;
		} else if (!bloquevidaImpflujoactualizado.equals(other.bloquevidaImpflujoactualizado))
			return false;
		if (bloquevidaImpflujonominal == null) {
			if (other.bloquevidaImpflujonominal != null)
				return false;
		} else if (!bloquevidaImpflujonominal.equals(other.bloquevidaImpflujonominal))
			return false;
		if (bloquevidaImpflujoprobable == null) {
			if (other.bloquevidaImpflujoprobable != null)
				return false;
		} else if (!bloquevidaImpflujoprobable.equals(other.bloquevidaImpflujoprobable))
			return false;
		if (bloquevidaImpprovi == null) {
			if (other.bloquevidaImpprovi != null)
				return false;
		} else if (!bloquevidaImpprovi.equals(other.bloquevidaImpprovi))
			return false;
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
		if (comi == null) {
			if (other.comi != null)
				return false;
		} else if (!comi.equals(other.comi))
			return false;
		if (ctipoaport == null) {
			if (other.ctipoaport != null)
				return false;
		} else if (!ctipoaport.equals(other.ctipoaport))
			return false;
		if (curvati == null) {
			if (other.curvati != null)
				return false;
		} else if (!curvati.equals(other.curvati))
			return false;
		if (dia == null) {
			if (other.dia != null)
				return false;
		} else if (!dia.equals(other.dia))
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
		if (fecfintramo == null) {
			if (other.fecfintramo != null)
				return false;
		} else if (!fecfintramo.equals(other.fecfintramo))
			return false;
		if (fechadesde == null) {
			if (other.fechadesde != null)
				return false;
		} else if (!fechadesde.equals(other.fechadesde))
			return false;
		if (fecinisus == null) {
			if (other.fecinisus != null)
				return false;
		} else if (!fecinisus.equals(other.fecinisus))
			return false;
		if (fecinitramo == null) {
			if (other.fecinitramo != null)
				return false;
		} else if (!fecinitramo.equals(other.fecinitramo))
			return false;
		if (fhasta == null) {
			if (other.fhasta != null)
				return false;
		} else if (!fhasta.equals(other.fhasta))
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
		if (gapact == null) {
			if (other.gapact != null)
				return false;
		} else if (!gapact.equals(other.gapact))
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
		if (kcarteraCohort == null) {
			if (other.kcarteraCohort != null)
				return false;
		} else if (!kcarteraCohort.equals(other.kcarteraCohort))
			return false;
		if (kcarteraContrato == null) {
			if (other.kcarteraContrato != null)
				return false;
		} else if (!kcarteraContrato.equals(other.kcarteraContrato))
			return false;
		if (kcarteraOner == null) {
			if (other.kcarteraOner != null)
				return false;
		} else if (!kcarteraOner.equals(other.kcarteraOner))
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
		if (negocio == null) {
			if (other.negocio != null)
				return false;
		} else if (!negocio.equals(other.negocio))
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
		if (nuevaproduc == null) {
			if (other.nuevaproduc != null)
				return false;
		} else if (!nuevaproduc.equals(other.nuevaproduc))
			return false;
		if (oga == null) {
			if (other.oga != null)
				return false;
		} else if (!oga.equals(other.oga))
			return false;
		if (pb == null) {
			if (other.pb != null)
				return false;
		} else if (!pb.equals(other.pb))
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
		if (prestcal == null) {
			if (other.prestcal != null)
				return false;
		} else if (!prestcal.equals(other.prestcal))
			return false;
		if (primaUnica == null) {
			if (other.primaUnica != null)
				return false;
		} else if (!primaUnica.equals(other.primaUnica))
			return false;
		if (primaperiodica == null) {
			if (other.primaperiodica != null)
				return false;
		} else if (!primaperiodica.equals(other.primaperiodica))
			return false;
		if (primaunica == null) {
			if (other.primaunica != null)
				return false;
		} else if (!primaunica.equals(other.primaunica))
			return false;
		if (provBelNiif17 == null) {
			if (other.provBelNiif17 != null)
				return false;
		} else if (!provBelNiif17.equals(other.provBelNiif17))
			return false;
		if (provRosspCsm == null) {
			if (other.provRosspCsm != null)
				return false;
		} else if (!provRosspCsm.equals(other.provRosspCsm))
			return false;
		if (provfcal == null) {
			if (other.provfcal != null)
				return false;
		} else if (!provfcal.equals(other.provfcal))
			return false;
		if (pventa == null) {
			if (other.pventa != null)
				return false;
		} else if (!pventa.equals(other.pventa))
			return false;
		if (raUmic == null) {
			if (other.raUmic != null)
				return false;
		} else if (!raUmic.equals(other.raUmic))
			return false;
		if (rosspCsm == null) {
			if (other.rosspCsm != null)
				return false;
		} else if (!rosspCsm.equals(other.rosspCsm))
			return false;
		if (segmento1 == null) {
			if (other.segmento1 != null)
				return false;
		} else if (!segmento1.equals(other.segmento1))
			return false;
		if (sioga == null) {
			if (other.sioga != null)
				return false;
		} else if (!sioga.equals(other.sioga))
			return false;
		if (siprima == null) {
			if (other.siprima != null)
				return false;
		} else if (!siprima.equals(other.siprima))
			return false;
		if (spcom == null) {
			if (other.spcom != null)
				return false;
		} else if (!spcom.equals(other.spcom))
			return false;
		if (sumcola == null) {
			if (other.sumcola != null)
				return false;
		} else if (!sumcola.equals(other.sumcola))
			return false;
		if (sumfprob == null) {
			if (other.sumfprob != null)
				return false;
		} else if (!sumfprob.equals(other.sumfprob))
			return false;
		if (sumfprobtanul == null) {
			if (other.sumfprobtanul != null)
				return false;
		} else if (!sumfprobtanul.equals(other.sumfprobtanul))
			return false;
		if (sumprovision == null) {
			if (other.sumprovision != null)
				return false;
		} else if (!sumprovision.equals(other.sumprovision))
			return false;
		if (sw_pb == null) {
			if (other.sw_pb != null)
				return false;
		} else if (!sw_pb.equals(other.sw_pb))
			return false;
		if (swcasado == null) {
			if (other.swcasado != null)
				return false;
		} else if (!swcasado.equals(other.swcasado))
			return false;
		if (swcasadolst == null) {
			if (other.swcasadolst != null)
				return false;
		} else if (!swcasadolst.equals(other.swcasadolst))
			return false;
		if (tabla1Aseg1 == null) {
			if (other.tabla1Aseg1 != null)
				return false;
		} else if (!tabla1Aseg1.equals(other.tabla1Aseg1))
			return false;
		if (textraccion == null) {
			if (other.textraccion != null)
				return false;
		} else if (!textraccion.equals(other.textraccion))
			return false;
		if (tipoPrima == null) {
			if (other.tipoPrima != null)
				return false;
		} else if (!tipoPrima.equals(other.tipoPrima))
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
		if (totFpComisiones == null) {
			if (other.totFpComisiones != null)
				return false;
		} else if (!totFpComisiones.equals(other.totFpComisiones))
			return false;
		if (totFpCompl == null) {
			if (other.totFpCompl != null)
				return false;
		} else if (!totFpCompl.equals(other.totFpCompl))
			return false;
		if (totFpFall == null) {
			if (other.totFpFall != null)
				return false;
		} else if (!totFpFall.equals(other.totFpFall))
			return false;
		if (totFpGastos == null) {
			if (other.totFpGastos != null)
				return false;
		} else if (!totFpGastos.equals(other.totFpGastos))
			return false;
		if (totFpGastosad == null) {
			if (other.totFpGastosad != null)
				return false;
		} else if (!totFpGastosad.equals(other.totFpGastosad))
			return false;
		if (totFpPrimas == null) {
			if (other.totFpPrimas != null)
				return false;
		} else if (!totFpPrimas.equals(other.totFpPrimas))
			return false;
		if (totFpRescates == null) {
			if (other.totFpRescates != null)
				return false;
		} else if (!totFpRescates.equals(other.totFpRescates))
			return false;
		if (totFpVida == null) {
			if (other.totFpVida != null)
				return false;
		} else if (!totFpVida.equals(other.totFpVida))
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
	public String toString() {
		return "DetalleCorrienteEntregables [dia=" + dia + ", fcierre=" + fcierre + ", bt=" + bt + ", umicKey="
				+ umicKey + ", fechadesde=" + fechadesde + ", cnegocio=" + cnegocio + ", ccanal=" + ccanal
				+ ", kcarterainv=" + kcarterainv + ", kmodalidad=" + kmodalidad + ", kgarantia=" + kgarantia
				+ ", gapact=" + gapact + ", kpoliza=" + kpoliza + ", ksubpoliza=" + ksubpoliza + ", nsuscri=" + nsuscri
				+ ", kramo=" + kramo + ", prestcal=" + prestcal + ", nuevaproduc=" + nuevaproduc + ", segmento1="
				+ segmento1 + ", tiposubriesgo=" + tiposubriesgo + ", curvati=" + curvati + ", sumfprob=" + sumfprob
				+ ", sumfprobtanul=" + sumfprobtanul + ", sumprovision=" + sumprovision + ", sumcola=" + sumcola
				+ ", bloquegtoImpflujoprobable=" + bloquegtoImpflujoprobable + ", bloquegtoImpflujonominal="
				+ bloquegtoImpflujonominal + ", bloquevidaImpflujonominal=" + bloquevidaImpflujonominal
				+ ", bloquefallImpflujonominal=" + bloquefallImpflujonominal + ", bloquecomplImpflujonominal="
				+ bloquecomplImpflujonominal + ", bloquecomiImpflujonominal=" + bloquecomiImpflujonominal
				+ ", bloquerteImpflujonominal=" + bloquerteImpflujonominal + ", bloqueprimImpflujonominal="
				+ bloqueprimImpflujonominal + ", kprestacion=" + kprestacion + ", kcoaseOri=" + kcoaseOri
				+ ", swcasado=" + swcasado + ", gestionit=" + gestionit + ", bloquevidaImpflujoprobable="
				+ bloquevidaImpflujoprobable + ", bloquefallImpflujoprobable=" + bloquefallImpflujoprobable
				+ ", bloquecomplImpflujoprobable=" + bloquecomplImpflujoprobable + ", bloquecomiImpflujoprobable="
				+ bloquecomiImpflujoprobable + ", bloquerteImpflujoprobable=" + bloquerteImpflujoprobable
				+ ", bloqueprimImpflujoprobable=" + bloqueprimImpflujoprobable + ", bloquevidaImpflujoactualizado="
				+ bloquevidaImpflujoactualizado + ", bloquefallImpflujoactualizado=" + bloquefallImpflujoactualizado
				+ ", bloquecomplImpflujoactualizado=" + bloquecomplImpflujoactualizado
				+ ", bloquecomiImpflujoactualizado=" + bloquecomiImpflujoactualizado + ", bloquerteImpflujoactualizado="
				+ bloquerteImpflujoactualizado + ", bloqueprimImpflujoactualizado=" + bloqueprimImpflujoactualizado
				+ ", bloquegtoImpflujoactualizado=" + bloquegtoImpflujoactualizado + ", spcom=" + spcom + ", kmodext="
				+ kmodext + ", kbencon=" + kbencon + ", fecinisus=" + fecinisus + ", pgastgesin1I=" + pgastgesin1I
				+ ", pgastgesin2I=" + pgastgesin2I + ", fecFinTramo1=" + fecFinTramo1 + ", pintertecnI1=" + pintertecnI1
				+ ", pintertecnI2=" + pintertecnI2 + ", tabla1Aseg1=" + tabla1Aseg1 + ", gtoUni=" + gtoUni
				+ ", gtoprov=" + gtoprov + ", factor1=" + factor1 + ", fecinitramo=" + fecinitramo + ", fecfintramo="
				+ fecfintramo + ", kcoase1=" + kcoase1 + ", kcoase2=" + kcoase2 + ", kcoase3=" + kcoase3 + ", kcoase4="
				+ kcoase4 + ", kcoase5=" + kcoase5 + ", kcoase6=" + kcoase6 + ", kcoase7=" + kcoase7 + ", kcoase8="
				+ kcoase8 + ", kcoase9=" + kcoase9 + ", kcoase10=" + kcoase10 + ", kcoase11=" + kcoase11 + ", kcoase12="
				+ kcoase12 + ", kcoase13=" + kcoase13 + ", kcoase14=" + kcoase14 + ", kcoase15=" + kcoase15
				+ ", kcoase16=" + kcoase16 + ", kcoase17=" + kcoase17 + ", kcoase18=" + kcoase18 + ", kcoase19="
				+ kcoase19 + ", kcoase20=" + kcoase20 + ", kcuadro=" + kcuadro + ", swcasadolst=" + swcasadolst
				+ ", negocio=" + negocio + ", kcarteraContrato=" + kcarteraContrato + ", kcarteraCohort="
				+ kcarteraCohort + ", kcarteraOner=" + kcarteraOner + ", fdesde=" + fdesde + ", fhasta=" + fhasta
				+ ", totFpVida=" + totFpVida + ", totFpFall=" + totFpFall + ", totFpCompl=" + totFpCompl
				+ ", totFpGastos=" + totFpGastos + ", totFpComisiones=" + totFpComisiones + ", totFpRescates="
				+ totFpRescates + ", totFpPrimas=" + totFpPrimas + ", ccartera=" + ccartera + ", uoa=" + uoa
				+ ", kcontrato=" + kcontrato + ", kcertificado=" + kcertificado + ", norden=" + norden + ", kajuste="
				+ kajuste + ", ctipoaport=" + ctipoaport + ", intfeccal=" + intfeccal + ", fsuscri=" + fsuscri
				+ ", totfpvida=" + totfpvida + ", totfpnavida=" + totfpnavida + ", totfactvida=" + totfactvida
				+ ", totcolavida=" + totcolavida + ", totfpfall=" + totfpfall + ", totfpnafall=" + totfpnafall
				+ ", totfactfall=" + totfactfall + ", totcolafall=" + totcolafall + ", totfpcompl=" + totfpcompl
				+ ", totfpnacompl=" + totfpnacompl + ", totfactcompl=" + totfactcompl + ", totcolacompl=" + totcolacompl
				+ ", totfpgto=" + totfpgto + ", totfpnagto=" + totfpnagto + ", totfactgto=" + totfactgto
				+ ", totcolagto=" + totcolagto + ", totfpcom=" + totfpcom + ", totfpnacom=" + totfpnacom
				+ ", totfactcom=" + totfactcom + ", totcolacom=" + totcolacom + ", totfprte=" + totfprte
				+ ", totfpnarte=" + totfpnarte + ", totfactrte=" + totfactrte + ", totcolarte=" + totcolarte
				+ ", totfpprim=" + totfpprim + ", totfpnaprim=" + totfpnaprim + ", totfactprim=" + totfactprim
				+ ", totcolaprim=" + totcolaprim + ", totfprob=" + totfprob + ", totfprobtanul=" + totfprobtanul
				+ ", totprovision=" + totprovision + ", totcola=" + totcola + ", provfcal=" + provfcal + ", totra="
				+ totra + ", totcsm=" + totcsm + ", totcsmpatron=" + totcsmpatron + ", kuoa=" + kuoa + ", textraccion="
				+ textraccion + ", tpasivo=" + tpasivo + ", tnegocio=" + tnegocio + ", tnegociolrc=" + tnegociolrc
				+ ", pventa=" + pventa + ", codreaseg=" + codreaseg + ", fproyflujest=" + fproyflujest + ", mon_prim="
				+ mon_prim + ", mon_rte=" + mon_rte + ", mon_fall=" + mon_fall + ", mon_vida=" + mon_vida
				+ ", mon_gtoadq=" + mon_gtoadq + ", mon_gto=" + mon_gto + ", mon_pb=" + mon_pb + ", pb=" + pb
				+ ", bloqueGtoImpflujoNoAnulado=" + bloqueGtoImpflujoNoAnulado + ", bloqueVidaImpflujoNoAnulado="
				+ bloqueVidaImpflujoNoAnulado + ", bloqueFallImpflujoNoAnulado=" + bloqueFallImpflujoNoAnulado
				+ ", bloqueComplImpflujoNoAnulado=" + bloqueComplImpflujoNoAnulado + ", bloqueComiImpflujoNoAnulado="
				+ bloqueComiImpflujoNoAnulado + ", bloqueRteImpflujoaNoAnulado=" + bloqueRteImpflujoaNoAnulado
				+ ", bloquePrimImpflujoNoAnulado=" + bloquePrimImpflujoNoAnulado + ", sw_pb=" + sw_pb + ", primaUnica="
				+ primaUnica + ", tipoPrima=" + tipoPrima + ", bloquegtoadImpflujonominal=" + bloquegtoadImpflujonominal
				+ ", bloquegtoadImpflujoprobable=" + bloquegtoadImpflujoprobable + ", bloquegtoadImpflujoactualizado="
				+ bloquegtoadImpflujoactualizado + ", bloqueGtoadImpflujoNoAnulado=" + bloqueGtoadImpflujoNoAnulado
				+ ", totFpGastosad=" + totFpGastosad + ", totfpgtoad=" + totfpgtoad + ", totfpnagtoad=" + totfpnagtoad
				+ ", totfactgtoad=" + totfactgtoad + ", totcolagtoad=" + totcolagtoad + ", raUmic=" + raUmic
				+ ", rosspCsm=" + rosspCsm + ", provRosspCsm=" + provRosspCsm + ", provBelNiif17=" + provBelNiif17
				+ ", primaperiodica=" + primaperiodica + ", primaunica=" + primaunica + ", oga=" + oga + ", siprima="
				+ siprima + ", comi=" + comi + ", sioga=" + sioga + ", bloquevidaImpprovi=" + bloquevidaImpprovi
				+ ", bloquefallImpprovi=" + bloquefallImpprovi + ", bloquegtoImpprovi=" + bloquegtoImpprovi
				+ ", bloquecomiImpprovi=" + bloquecomiImpprovi + ", bloquecomplImpprovi=" + bloquecomplImpprovi
				+ ", bloqueprimImpprovi=" + bloqueprimImpprovi + ", bloquerteImpprovi=" + bloquerteImpprovi
				+ ", bloquegtoadImpprovi=" + bloquegtoadImpprovi + "]";
	}

	@Override
	public DetalleCorrienteEntregablesKey getKey() {
		return new DetalleCorrienteEntregablesKey(bt, fcierre, umicKey, fechadesde, dia);
	}

}