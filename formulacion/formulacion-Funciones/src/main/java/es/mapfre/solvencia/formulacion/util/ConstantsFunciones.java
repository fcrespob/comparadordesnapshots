 /* MODIFICACION:TAR00393113-CONTABILIZACION EN SISTEMAS ABIERTOS POR MODALIDAD ORIGEN PARA LA MODALIDAD INSTRUMENTAL 400
   FECHA: 26/04/2018 
   AUTOR: INDRA
*/
/**MODIFICACION: TAR00433819
  FECHA: 24/09/2018
  DESCRIP: Se incluyen constantes para los nuevos estados de prorrogas U,F
 */
/**MODIFICACION: MU-2019-066508: 
  FECHA: 20/11/2019
  DESCRIP: Se incluyen constantes para controlar error de asegurado cuya edad a la fecha de calculo supera la edad de las tablas de mortalidada
 */
package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase que contiene las constantes necesarias para las funciones auxiliares.
 * 
 * @author rschacon
 * 
 */
public final class ConstantsFunciones {

	private ConstantsFunciones() {
		
	}
	
	/**
	 * Cte primera iteración
	 */
	public static final Integer CTE_FIRST_ITER = 1;
	
	/*
	 * Cte de utilidadº 
	 */
	public static final String CTE_CADENA_VACIA = "";
	public static final Integer CTE_MENOS_1 = -1;
	
	/**
	 * Cte númericas
	 */
	public static final Integer CTE_0 = 0;
	public static final Integer CTE_1 = 1;
	public static final Integer CTE_2 = 2;
	public static final Integer CTE_3 = 3;
	public static final Integer CTE_4 = 4;
	public static final Integer CTE_5 = 5;
	public static final Integer CTE_6 = 6;
	public static final Integer CTE_7 = 7;
	public static final Integer CTE_8 = 8;
	public static final Integer CTE_9 = 9;
	public static final Integer CTE_10 = 10;
	public static final Integer CTE_11 = 11;
	public static final Integer CTE_12 = 12;
	public static final Integer CTE_13 = 13;
	public static final Integer CTE_14 = 14;
	public static final Integer CTE_15 = 15;
	public static final Integer CTE_16 = 16;
	public static final Integer CTE_17 = 17;
	public static final Integer CTE_18 = 18;
	public static final Integer CTE_19 = 19;
	public static final Integer CTE_20 = 20;
	public static final Integer CTE_21 = 21;
	public static final Integer CTE_22 = 22;
	public static final Integer CTE_23 = 23;
	public static final Integer CTE_24 = 24;
	public static final Integer CTE_25 = 25;
	public static final Integer CTE_26 = 26;
	public static final Integer CTE_27 = 27;
	public static final Integer CTE_28 = 28;
	public static final Integer CTE_29 = 29;
	public static final Integer CTE_30 = 30;
	public static final Integer CTE_31 = 31;
	public static final Integer CTE_32 = 32;
	public static final Integer CTE_33 = 33;
	public static final Integer CTE_34 = 34;
	public static final Integer CTE_35 = 35;
	public static final Integer CTE_36 = 36;
	public static final Integer CTE_37 = 37;
	public static final Integer CTE_38 = 38;
	public static final Integer CTE_39 = 39;
	public static final Integer CTE_40 = 40;
	public static final Integer CTE_41 = 41;
	public static final Integer CTE_42 = 42;
	public static final Integer CTE_43 = 43;
	public static final Integer CTE_44 = 44;
	public static final Integer CTE_45 = 45;
	public static final Integer CTE_46 = 46;
	public static final Integer CTE_47 = 47;
	public static final Integer CTE_48 = 48;
	public static final Integer CTE_49 = 49;
	public static final Integer CTE_50 = 50;
	public static final Integer CTE_51 = 51;
	public static final Integer CTE_52 = 52;
	public static final Integer CTE_53 = 53;
	public static final Integer CTE_54= 54;
	public static final Integer CTE_55 = 55;
	public static final Integer CTE_56 = 56;
	public static final Integer CTE_57 = 57;
	public static final Integer CTE_58 = 58;
	public static final Integer CTE_59 = 59;
	public static final Integer CTE_60 = 60;
	public static final Integer CTE_65 = 65;
	public static final Integer CTE_75 = 75;
	public static final Integer CTE_80 = 80;
	public static final Integer CTE_90 = 90;
	public static final Integer CTE_99 = 99;
	public static final Integer CTE_100 = 100;
	public static final Integer CTE_119 = 119;
	public static final Integer CTE_120 = 120;
	public static final Integer CTE_128 = 128;
	public static final Integer CTE_130 = 130;
	public static final Integer CTE_150 = 150;
	public static final Integer CTE_151 = 151;
	public static final Integer CTE_180 = 180;
	public static final Integer CTE_181 = 181;
	public static final Integer CTE_210 = 210;
	public static final Integer CTE_212 = 212;
	public static final Integer CTE_240 = 240;
	public static final Integer CTE_243 = 243;
	public static final Integer CTE_270 = 270;
	public static final Integer CTE_273 = 273;
	public static final Integer CTE_300 = 300;
	public static final Integer CTE_304 = 304;
	public static final Integer CTE_310 = 310;
	public static final Integer CTE_330 = 330;
	public static final Integer CTE_334 = 334;
	public static final Integer CTE_360 = 360;
	public static final Integer CTE_365 = 365;
//INI-TAR00393113
	public static final Integer CTE_400 = 400;
//FIN-TAR00393113
	public static final Integer CTE_700 = 700;
	public static final Integer CTE_999 = 999;
	public static final Integer CTE_1000 = 1000;
	public static final Integer CTE_1024 = 1024;
	public static final Integer CTE_2001 = 2001;
	public static final Integer CTE_9999 = 9999;
	public static final Integer CTE_10000 = 10000;
	
	/** Cte numeros decimales como String para crear los BigDecimal */
	public static final String CTE_0_PUNTO_0001 = "0.0001";
	public static final String CTE_0_PUNTO_001 = "0.001";
	public static final String CTE_0_PUNTO_01 = "0.01";
	public static final String CTE_0_PUNTO_1 = "0.1";
	public static final String CTE_0_PUNTO_5 = "0.5";
	public static final String CTE_0_PUNTO_5_NEG = "-0.5";
	public static final String CTE_0_STRING = "0";
	public static final String CTE_1_STRING = "1";
	public static final String CTE_2_STRING = "2";
	public static final String CTE_2_PUNTO_73 = "2.73";
	public static final String CTE_3_STRING = "3";
	public static final String CTE_4_STRING = "4";
	public static final String CTE_5_STRING = "5";
	public static final String CTE_10_PUNTO_5 = "10.5";
	public static final String CTE_11_PUNTO_5 = "11.5";
	public static final String CTE_30_PUNTO_5 = "30.5";
	public static final String CTE_0_PUNTO_0365 = "0.0365";
	public static final String CTE_1_PUNTO_035 = "1.035"; 
	public static final String CTE_2013_PUNTO_54237 = "2103.54237";
	public static final String CTE_0_PUNTO_20133905 = "0.20133905";
	public static final String CTE_1_PUNTO_32823675 = "1.32823675";
	public static final String CTE_300_PUNTO_506052 = "300.506052";
	public static final String CTE_0_PUNTO_003 = "0.003";
	public static final String CTE_0_PUNTO_03 = "0.03";
	public static final String CTE_3_PUNTO_8 = "3.8";
	public static final String CTE_1_PUNTO_5 = "1.5";
	public static final String CTE_101 = "101";
	public static final String CTE_1_PUNTO_038 = "1.038";
	public static final String CTE_72_PUNTO_1274626 = "72.1274626";
	public static final String CTE_12_PUNTO_0202421 = "12.0202421";
		
	/** Cte contexto matemático para las operaciones con BigDecimal */
	public static final MathContext MATH_CONTEXT = new MathContext(34, RoundingMode.HALF_DOWN);
	
	public static final String [] meses = {"01","02","03","04","05","06","07","08","09","10","11","12"};

	/**
	 * Cte BigDecimal
	 */
	public static final BigDecimal CTE_OPER_0_PUNTO_0001 = new BigDecimal(CTE_0_PUNTO_0001);
	public static final BigDecimal CTE_OPER_0_PUNTO_001 = new BigDecimal(CTE_0_PUNTO_001);
	public static final BigDecimal CTE_OPER_0_PUNTO_01 = new BigDecimal(CTE_0_PUNTO_01);
	public static final BigDecimal CTE_OPER_0_PUNTO_1 = new BigDecimal(CTE_0_PUNTO_1);
	public static final BigDecimal CTE_OPER_0_PUNTO_5 = new BigDecimal(CTE_0_PUNTO_5);
	public static final BigDecimal CTE_OPER_0_PUNTO_5_NEG = new BigDecimal(CTE_0_PUNTO_5_NEG);
	public static final BigDecimal CTE_OPER_1 = new BigDecimal(CTE_1);
	public static final BigDecimal CTE_OPER_2 = new BigDecimal(CTE_2);
	public static final BigDecimal CTE_OPER_3 = new BigDecimal(CTE_3);
	public static final BigDecimal CTE_OPER_4 = new BigDecimal(CTE_4);
	public static final BigDecimal CTE_OPER_5_PUNTO_0 = new BigDecimal(CTE_5);
	public static final BigDecimal CTE_OPER_7_PUNTO_0 = new BigDecimal(CTE_7);
	public static final BigDecimal CTE_OPER_10_PUNTO_5 = new BigDecimal(CTE_10_PUNTO_5);
	public static final BigDecimal CTE_OPER_11_PUNTO_5 = new BigDecimal(CTE_11_PUNTO_5);
	public static final BigDecimal CTE_OPER_12 = new BigDecimal(CTE_12);
	public static final BigDecimal CTE_OPER_14 = new BigDecimal(CTE_14);
	public static final BigDecimal CTE_OPER_1ENTRE12 = BigDecimal.ONE.divide(CTE_OPER_12, MATH_CONTEXT);
	public static final BigDecimal CTE_OPER_20_PUNTO_0 = new BigDecimal(CTE_20);
	public static final BigDecimal CTE_OPER_24 = new BigDecimal(CTE_24);
	public static final BigDecimal CTE_OPER_1ENTRE24 = BigDecimal.ONE.divide(CTE_OPER_24, MATH_CONTEXT);
	public static final BigDecimal CTE_OPER_20 = new BigDecimal(CTE_20);
	public static final BigDecimal CTE_OPER_30 = new BigDecimal(CTE_30);
	public static final BigDecimal CTE_OPER_60 = new BigDecimal(CTE_60);
	public static final BigDecimal CTE_OPER_75 = new BigDecimal(CTE_75);
	public static final BigDecimal CTE_OPER_30_PUNTO_5 = new BigDecimal(CTE_30_PUNTO_5);
	public static final BigDecimal CTE_OPER_100 = new BigDecimal(CTE_100);
	public static final BigDecimal CTE_OPER_1000 = new BigDecimal(CTE_1000);
	public static final BigDecimal CTE_OPER_365 = new BigDecimal(CTE_365);
	public static final BigDecimal CTE_OPER_1_PARTIDO_365 = BigDecimal.ONE.divide(CTE_OPER_365, MATH_CONTEXT);
	public static final BigDecimal CTE_OPER_360 = new BigDecimal(CTE_360);
	public static final BigDecimal CTE_OPER_2_PUNTO_73 = new BigDecimal(CTE_2_PUNTO_73);
	public static final Double CTE_0_PUNTO_01_D = 0.01;
	public static final BigDecimal CTE_OPER_1_PUNTO_035 = new BigDecimal(CTE_1_PUNTO_035);
	public static final BigDecimal CTE_OPER_50 = new BigDecimal(CTE_50);
	public static final BigDecimal CTE_OPER_80 = new BigDecimal(CTE_80);
	public static final BigDecimal CTE_OPER_2103_PUNTO_54237 = new BigDecimal(CTE_2013_PUNTO_54237);
	public static final BigDecimal CTE_OPER_0_PUNTO_20133905 = new BigDecimal(CTE_0_PUNTO_20133905);
	public static final BigDecimal CTE_OPER_1_PUNTO_32823675 = new BigDecimal(CTE_1_PUNTO_32823675);
	public static final BigDecimal CTE_OPER_CTE_300_PUNTO_506052 = new BigDecimal(CTE_300_PUNTO_506052);
	public static final BigDecimal CTE_OPER_CTE_0_PUNTO_003 = new BigDecimal(CTE_0_PUNTO_003);
	public static final BigDecimal CTE_OPER_CTE_0_PUNTO_03 = new BigDecimal(CTE_0_PUNTO_03);
	public static final BigDecimal CTE_OPER_TE_3_PUNTO_8 = new BigDecimal(CTE_3_PUNTO_8);
	public static final BigDecimal CTE_OPER_1_PUNTO_5 = new BigDecimal(CTE_1_PUNTO_5);
	public static final BigDecimal CTE_OPER_CTE_101 = new BigDecimal(CTE_101);
	public static final BigDecimal CTE_OPER_1_PUNTO_038 = new BigDecimal(CTE_1_PUNTO_038);
	public static final BigDecimal CTE_OPER_72_PUNTO_1274626 = new BigDecimal(CTE_72_PUNTO_1274626);
	public static final BigDecimal CTE_OPER_12_PUNTO_0202421 = new BigDecimal(CTE_12_PUNTO_0202421);
	public static final BigDecimal CTE_OPER_10000 = new BigDecimal(CTE_10000);
	public static final BigDecimal CTE_OPER_1ENTRE4 = BigDecimal.ONE.divide(CTE_OPER_4, MATH_CONTEXT);
	public static final BigDecimal CTE_OPER_3ENTRE4 = CTE_OPER_3.divide(CTE_OPER_4, MATH_CONTEXT);
	
	
	/**
	 * Cte contexto matemático para las operaciones con BigDecimal de la generación de plan de pagos
	 */
	public static final MathContext MATH_CONTEXT_PLAN_PAGOS = new MathContext(4, RoundingMode.CEILING);
	
	/** Milisegundos en un día: 1000 * 60 * 60 * 24 */
	public static final Integer CTE_MILIS_DIA = 86400000;
	
	/** Formatos de fecha para la clase de utilidades de fecha */
	public static final String FMT_DIA = "dd";
	public static final String FMT_MES = "MM";
	public static final String FMT_ANYO = "yyyy";
	public static final String GMT = "GMT";
	
	/*
	 * Cte nombre parametros entrada funciones axiliares
	 */
	public static final String CTE_FECHA_ENTRADA = "fechaEntrada";
	public static final String CTE_CRITERIO_FEC = "criterioFecha";
	public static final String CTE_FECHA_1 = "fecha1";
	public static final String CTE_FECHA_2 = "fecha2";
	public static final String CTE_FECHA_CALCULO = "fcalc";
	public static final String CTE_FEC_NACI = "fnac";
	public static final String CTE_CRITERIO_EDAD = "criterioEdad";
	public static final String CTE_FEC_EFE_POL = "fefecto";
	public static final String CTE_FEC_INI_SUS = "finisusc";
	public static final String CTE_EDAD_MAXIMA = "edadMax";
	public static final String CTE_FEC_NAC = "fecnac";
	public static final String CTE_CRI_INTERES = "criterioInteres";
	public static final String CTE_DUR_ANTERIOR = "durAnt";
	public static final String CTE_INT_ANTERIOR = "intAnt";
	public static final String CTE_DUR_POSTERIOR = "durPost";
	public static final String CTE_INT_POSTERIOR = "intPost";
	public static final String CTE_DUR_REQUERIDA = "durJ";
	public static final String CTE_MESES_COMPLET = "mesesCompletos";
	public static final String CTE_TI_PRIMER_TRA = "tipoIPrimerTramo";
	public static final String CTE_DUR_PRIMER_TR = "duracionPrimerTra";
	public static final String CTE_TI_SEGUNDO_TR = "tipoISegundoTramo";
	public static final String CTE_EDAD_ACTUARIA = "edadActuarial";
	public static final String CTE_TABLA_EXP = "tablaExperiencia";
	public static final String CTE_TABLA_2000 = "tablaSalarialInicial";
	public static final String CTE_PRIMA = "prima";
	public static final String CTE_PRIMA_INI = "primaIni";
	public static final String CTE_PRIMA_TOTAL = "primaTotal";
	public static final String CTE_CAP_RIESGO = "cRm";
	public static final String CTE_GAMMA = "gamma";
	public static final String CTE_MES_CAREN = "mescare";
	public static final String CTE_DIFER_RENTA = "diferRenta";
	public static final String CTE_DUR_RENTA = "duracionRenta";
	public static final String CTE_NUM_PAGOS = "numPagos";
	public static final String CTE_NVA = "nivelSalarialCertificado";
	public static final String CTE_KBENCON = "nivelSalarialCertificado";
	public static final String CTE_ANIOINICIO = "AnioInicioPrestacion";
	public static final String CTE_REVAL_RENTA = "porcenRevalRen";
	public static final String CTE_LIMITE_CAPITAL = "limiteCapital";
	public static final String CTE_LISTA_LIMITES_CAPITAL = "listaLimitesCapital";
	public static final String CTE_PREF_COD_T2000 = "NVA_";
	public static final String CTE_FORMAT_COD_T2000 = "%03d";
	public static final String CTE_TCM = "tcm";
	public static final String CTE_TTM = "ttm";
	public static final String CTE_BETA = "beta";

	public static final String CTE_FEC_ANT_RENOV = "fantRenovacion";
	public static final String CTE_FEC_PROX_RENO = "fproxRenovacion";
	public static final String CTE_FEC_CIERRE = "fcierre";
	public static final String CTE_TIPO = "tipo";
	public static final String CTE_TEMP_VIT = "TempVit";
	public static final String CTE_FEC_EFECTO = "fecEfecto";
	public static final String CTE_FEC_FIN_TRAMO1 = "fFinTramo1";
	public static final String CTE_FEC_INI_RENTA = "fecIniRenta";

	public static final String CTE_EDAD_ASEG = "edadAsegurado";
	public static final String CTE_DES_RENTA = "desRenta";
	public static final String CTE_DUR_SEGURO = "durSeguro";
	public static final String CTE_IN_TRAMO1 = "inTramo1";
	public static final String CTE_DIFER = "difer";
	public static final String CTE_CAP_GAR = "varx";
	public static final String CTE_INTERES1 = "interes1";
	
	public static final String CTE_ALFAT = "alfat";
	public static final String CTE_ALFAT30 = "alfat30";
	public static final String CTE_ALFA2 = "alfa2";
	public static final String CTE_ALFA230 = "alfa230";
	public static final String CTE_ALFA = "alfa";
	public static final String CTE_FACTOR_ALFAT = "factorAlfat";
	public static final String CTE_FACTOR_ALFA2 = "factorAlfa2";
	public static final String CTE_VRTA = "vrta";
    public static final String CTE_NRTA="nrta";
	public static final String CTE_FPR="fpr";
	public static final String CTE_PRR="prr";
	public static final String CTE_PNATC="pnaTc";
	public static final String CTE_TC="tc";
	public static final String CTE_T="t";
	public static final String CTE_PPRUMIC="pprumic";
	public static final String CTE_PRIMA_TARADA="primaTarada";
	public static final String CTE_VAR_PU = "varPu";
	public static final String CTE_CRM = "crm";
	public static final String CTE_VAR_TM = "varTm";
	public static final String CTE_VAR_TCM = "varTcm";
	public static final String CTE_VAR_TTM = "varTtm";
	public static final String CTE_VAR_L = "varl";
	public static final String CTE_MESCAREN = "mescaren";
	public static final String CTE_I1_BTI = "i1BTI";
	public static final String CTE_I2_BTI = "i2BTI";
	public static final String CTE_I1_BTI_MINUS = "i1Bti";
	public static final String CTE_I2_BTI_MINUS = "i2Bti";
	public static final String CTE_VAR_NR = "varNr";
	public static final String CTE_VAR_NRM = "varNrm";
	public static final String CTE_BETA1 = "beta1";
	public static final String CTE_BETA2 = "beta2";
	public static final String CTE_DIFERCOL = "difercol";
	public static final String CTE_FRAC_ANIO_IN = "fracAnioInc";
	public static final String CTE_VAR_TC_MAYUS = "varTc";
	public static final String CTE_VAR_TCY = "varTCY";
	public static final String CTE_VAR_RY = "varRy";
	public static final String CTE_NIRP = "nirp";
	public static final String CTE_NIRMV = "nirmv";
	public static final String CTE_FEC_INI = "fecIni";
	public static final String CTE_FEC_VTO = "fecvto";
	public static final String CTE_FEC_ANT_REV = "fecAntRenova";
	public static final String CTE_FEC_PROX_REV = "fecProxRenova";
	public static final String CTE_FEC_INI_SUSC = "fecIniSusc";
	public static final String CTE_FEC_REN_PSGACT = "fecRenPsgact";
	public static final String CTE_FCAL = "fcal";
	public static final String CTE_NDAP = "ndap";
	public static final String CTE_NDMV = "ndmv";
	public static final String CTE_VVIDA = "vvida";
	public static final String CTE_CRIT_FEC = "criterFec";

	public static final String CTE_VARX = "varx";
	public static final String CTE_VARW = "varw";
	public static final String CTE_VAR_Y = "vary";
	public static final String CTE_VARP = "varp";
	public static final String CTE_VARM = "varm";
	public static final String CTE_VARN = "varn";
	public static final String CTE_VARI1 = "vari1";
	public static final String CTE_VARI2 = "vari2";
	public static final String CTE_VARF = "varf";
	public static final String CTE_VALORES_TM = "valoresTabMort";
	public static final String CTE_VAR_DIFER = "difer";
	public static final String CTE_VAR_PRP = "prp";
	public static final String CTE_VAR_PPR = "ppr";
	public static final String CTE_VAR_PRIMA_TARADA = "primaTarada";
	public static final String CTE_VAR_PPCAP = "ppcap";
	public static final String CTE_VAR_IANT = "iant";
	public static final String CTE_VAR_TC = "vartc";
	public static final String CTE_VAR_TC0 = "varTC0";
	public static final String CTE_VAR_T = "vart";
	public static final String CTE_FUT = "fut";
	public static final String CTE_PAS = "pas";
	public static final String CTE_GI = "gi";
	public static final String CTE_GIC = "gic";
	public static final String CTE_GIPC = "gipc";
	public static final String CTE_GIPC_PRIMA = "gipcPrima";
	public static final String CTE_INTERES = "interes";
	public static final String CTE_REN = "ren";
	public static final String CTE_J = "j";
	public static final String CTE_VAR_J = "varj";
	public static final String CTE_FEC_J = "fecJ";
	public static final String CTE_GASGIVIT = "gasgivit";
	public static final String CTE_GASGIVITINI = "gasgivitini";
	public static final String CTE_X363 = "x363";
	public static final String CTE_LX_INI = "LxIni";
	public static final String CTE_GASTGI = "gastGi";
	public static final String CTE_C1BBC = "c1bbc";
	public static final String CTE_EDADEXP = "edadExp";
	
	public static final String CTE_NC = "nc";
	public static final String CTE_PC = "pc";
	public static final String CTE_IF = "if";
	public static final String CTE_DELTA = "delta";
	public static final String CTE_CP0 = "cp0";
	public static final String CTE_NA = "na";

	public static final String CTE_CAPITAL_GAR = "capitalGar";
	public static final String CTE_CAPITAL_INI = "capitalIni";
	public static final String CTE_PRC = "prc";
	public static final String CTE_PRP = "prp";
	public static final String CTE_DIFFER = "differ";
	public static final String CTE_GEPC = "gepc";
	public static final String CTE_PNA0 = "pna0";
	public static final String CTE_CSITUPOL = "csitupol";
	public static final String CTE_CTIPOAPORT = "ctipoaport";
	public static final String CTE_VAR_IFAL = "ifal";
	public static final String CTE_VAR_FALLRED = "fallRed0";
	public static final String CTE_VARZC = "varzc";
	public static final String CTE_XREN = "xren";
	public static final String CTE_VMORT = "vmort";
	public static final String CTE_MOD_BETA = "modBeta";
	public static final String CTE_CFORMAPAGO = "cformapago";
	
	public static final String CTE_POLIZA = "poliza";
	public static final String CTE_SUBPOLIZA = "subpoliza";
	public static final String CTE_CERTIFICADO = "certificado";
	public static final String CTE_NSUSCRIPCION = "suscripcion";

	public static final String CTE_NPA = "npa";
	public static final String CTE_G = "g";
	public static final String CTE_MES1 = "mes1";
	public static final String CTE_MES2 = "mes2";
	
	public static final String CTE_ACTJ = "actj";
	public static final String CTE_NUMM = "numm";
	public static final String CTE_NUMN = "numn";
	public static final String CTE_NUMI1 = "numi1";
	public static final String CTE_NUMI2 = "numi2";
	public static final String CTE_PART_ANO_NR = "partAnoNR";
	public static final String CTE_RENTA = "renta";
	public static final String CTE_RENTA_1 = "renta1";
	public static final String CTE_RENTA_2 = "renta2";
	public static final String CTE_VARK = "vark";
	public static final String CTE_VARD = "vard";
	
	public static final String CTE_PROY_UMIC = "proyUmic";
	public static final String CTE_BLOQUE_CORR = "bloqueCorriente";
	public static final String CTE_FCALC = "fcalc";
	public static final String CTE_UMIC = "umic";
	public static final String CTE_BTC_UMIC = "btcUmic";
	
	public static final String CTE_VA_CRIT_FEC = "ID-TEMPORAL";
	public static final String CTE_VA_CRIT_EDA = "ID-CRITERIO";
	public static final String CTE_VA_MOD_BETA = "MODBETA";
	
	public static final String CTE_RENTA_TEMPORAL = "T";
	public static final String CTE_RENTA_VITALICIA = "V";
	
	public static final String CTE_EDAD_COB = "edadCob";
	public static final String CTE_VAR_1 = "var1";
	public static final String CTE_VAR_2 = "var2";
	public static final String CTE_PRG = "prg";
	public static final String CTE_PGASTGESIN_1 = "pgastgesin1";
	public static final String CTE_PGASTGESIN_2 = "pgastgesin2";
	public static final String CTE_IPRIMANETA_INI = "iprimanetaini";
	
	public static final String CTE_DIAVTO = "diaVto";
	public static final String CTE_GGIM = "ggim";
	public static final String CTE_DIAPRIMA = "diaprima";
	public static final String CTE_BXANTERIOR = "bxAnterior";
	
	public static final String CTE_GTOSF = "gtosF";
	public static final String CTE_VAR_CRMAX = "varCRMax";
	
	public static final String CTE_PNA = "pna";
	public static final String CTE_NP = "np";
	public static final String CTE_MES_EFECTO = "mesefect";
	
	public static final String CTE_GAST = "gast";
	public static final String CTE_CFALL = "cfall";
	public static final String CTE_CC06 = "CC06";
	
	public static final String CTE_AGP = "AGP";
	public static final String CTE_AG = "AG";
	public static final String CTE_WX = "wX";
	public static final String CTE_WY = "wY";
	
	public static final String CTE_XC = "xc";
	public static final String CTE_XJ = "xj";
	public static final String CTE_DURCIERRE = "durcierre";
	public static final String CTE_DURIT1 = "durit1";
	public static final String CTE_ACTJANT = "actjant";
	public static final String CTE_CFANT = "cfant";
	public static final String CTE_XJANT = "xjant";
	
	public static final String CTE_LIMSUPERIOR = "varLimSuperior";
	
	public static final String CTE_SP_COM = "spCom";
	public static final String CTE_PROB_X_RENOV_HASTA = "probXRenoHasta";
	public static final String CTE_FACTOR_GASTO = "factorGasto";
	public static final String CTE_VARFFINCAS = "varffincas";
	public static final String CTE_FECSUSC = "fecsusc";
	public static final String CTE_FECVTO = "fecvto";
	public static final String CTE_FACTOR_I1 = "factorI1";
	
	
	/*
	 * Cte nombre variables locales funciones axiliares
	 */
	public static final String CTE_OPERANDO1 = "operando1";
	
	/** 
	 * Códigos proyecciones
	 */
	public static final String CTE_PROY_VIDA = "PROY_VIDA";
	public static final String CTE_PROY_FALL = "PROY_FALL";

	/**
	 * Cte para validaciones de criterioInteres en función calcularTipoInteres
	 */
	public static final String CTE_CRI_IN_SUPERI = "00";
	public static final String CTE_CRI_IN_ME_DIA = "01";

	/*
	 * Constantes para variables de remplazo para las descripciones de error de
	 * las funciones auxiliares
	 */
	public static final String CTE_CAL_PRORRATA = "PRORRATA";
	public static final String CTE_CAL_FORFAIT = "FORFAIT";

	/*
	 * Constantes valores permitidos campo criterio fecha de las funciones
	 * auxiliares
	 */
	public static final String CTE_CRI_FECHA_01 = "01";
	public static final String CTE_CRI_FECHA_02 = "02";
	public static final String CTE_CRI_FECHA_03 = "03";
	public static final String CTE_CRI_FECHA_04 = "04";
	public static final String CTE_CRI_FECHA_05 = "05";
	public static final String CTE_CRI_FECHA_06 = "06";
	
	/** Código de error para Excepción no controlada */
	public static final String CTE_COD_ERROR_000 = "E0";
	
	/** Códigos de error para módulos. 100-399 */
	public static final String CTE_COD_ERROR_A1 = "A1";
	public static final String CTE_COD_ERROR_A2 = "A2";
	public static final String CTE_COD_ERROR_A3 = "A3";
	public static final String CTE_COD_ERROR_A4 = "A4";
	public static final String CTE_COD_ERROR_A5 = "A5";
	public static final String CTE_COD_ERROR_A6 = "A6";
	public static final String CTE_COD_ERROR_A7 = "A7";
	public static final String CTE_COD_ERROR_A8 = "A8";
	public static final String CTE_COD_ERROR_A9 = "A9";
	public static final String CTE_COD_ERROR_A10 = "A10"; 
	public static final String CTE_COD_ERROR_AA = "AA";
	public static final String CTE_COD_ERROR_AB = "AB";
	public static final String CTE_COD_ERROR_AC = "AC";
	public static final String CTE_COD_ERROR_AD = "AD";
	public static final String CTE_COD_ERROR_AE = "AE";
	public static final String CTE_COD_ERROR_AF = "AF";
	public static final String CTE_COD_ERROR_AG = "AG";
	public static final String CTE_COD_ERROR_AH = "AH";
	public static final String CTE_COD_ERROR_AI = "AI";
	public static final String CTE_COD_ERROR_AJ = "AJ";
	public static final String CTE_COD_ERROR_AK = "AK";
	public static final String CTE_COD_ERROR_AL = "AL";
	public static final String CTE_COD_ERROR_AM = "AM";
	public static final String CTE_COD_ERROR_AO = "AO";
	public static final String CTE_COD_ERROR_AQ = "AQ";
	public static final String CTE_COD_ERROR_CC = "CC";
	public static final String CTE_COD_ERROR_CZ = "CZ";
	public static final String CTE_COD_ERROR_AR = "AR";
	public static final String CTE_COD_ERROR_AS = "AS";
	public static final String CTE_COD_ERROR_AT = "AT";
	public static final String CTE_COD_ERROR_AU = "AU";
	public static final String CTE_COD_ERROR_AV = "AV";
	public static final String CTE_COD_ERROR_AW = "AW";
	public static final String CTE_COD_ERROR_AX = "AX";
	public static final String CTE_COD_ERROR_AY = "AY";
	public static final String CTE_COD_ERROR_AZ = "AZ";
	public static final String CTE_COD_ERROR_G1 = "G1";
	public static final String CTE_COD_ERROR_G2 = "G2";
	public static final String CTE_COD_ERROR_G3 = "G3";
	public static final String CTE_COD_ERROR_G4 = "G4";
	public static final String CTE_COD_ERROR_G5 = "G5";
	public static final String CTE_COD_ERROR_G6 = "G6";
	public static final String CTE_COD_ERROR_G7 = "G7";
	public static final String CTE_COD_ERROR_G8 = "G8";
	public static final String CTE_COD_ERROR_G9 = "G9";
	public static final String CTE_COD_ERROR_GA = "GA";
	public static final String CTE_COD_ERROR_GB = "GB";
	public static final String CTE_COD_ERROR_GC = "GC";
	public static final String CTE_COD_ERROR_GD = "GD";
	public static final String CTE_COD_ERROR_GE = "GE";
	public static final String CTE_COD_ERROR_GF = "GF";
	public static final String CTE_COD_ERROR_GG = "GG";
	public static final String CTE_COD_ERROR_GH = "GH";
	public static final String CTE_COD_ERROR_GI = "GI";
	public static final String CTE_COD_ERROR_GJ = "GJ";
	public static final String CTE_COD_ERROR_GK = "GK";
	public static final String CTE_COD_ERROR_GL = "GL";
	public static final String CTE_COD_ERROR_GM = "GM";
	public static final String CTE_COD_ERROR_GN = "GN";
	public static final String CTE_COD_ERROR_GO = "GO";
	public static final String CTE_COD_ERROR_GP = "GP";
	public static final String CTE_COD_ERROR_GQ = "GQ";
	public static final String CTE_COD_ERROR_GR = "GR";
	public static final String CTE_COD_ERROR_GS = "GS";
	public static final String CTE_COD_ERROR_GT = "GT";
	public static final String CTE_COD_ERROR_GU = "GU";
	public static final String CTE_COD_ERROR_GV = "GV";
	public static final String CTE_COD_ERROR_GW = "GW";
	public static final String CTE_COD_ERROR_GZ = "GZ";
	public static final String CTE_COD_ERROR_HA = "HA";
	public static final String CTE_COD_ERROR_HB = "HB";
	
	/** Códigos de error para servicios de datos. */
	public static final String CTE_COD_ERROR_D1 = "D1";
	public static final String CTE_COD_ERROR_D2 = "D2";
	public static final String CTE_COD_ERROR_D3 = "D3";
	public static final String CTE_COD_ERROR_D4 = "D4";
	public static final String CTE_COD_ERROR_D5 = "D5";
	public static final String CTE_COD_ERROR_D6 = "D6";
	public static final String CTE_COD_ERROR_D7 = "D7";
	public static final String CTE_COD_ERROR_D8 = "D8";
	public static final String CTE_COD_ERROR_D9 = "D9";
	public static final String CTE_COD_ERROR_DA = "DA";
	public static final String CTE_COD_ERROR_DB = "DB";
	public static final String CTE_COD_ERROR_DC = "DC";
	public static final String CTE_COD_ERROR_DD = "DD";
	public static final String CTE_COD_ERROR_DE = "DE";
	public static final String CTE_COD_ERROR_DF = "DF";
	public static final String CTE_COD_ERROR_DG = "DG";												
	public static final String CTE_COD_ERROR_DJ = "DJ";
	public static final String CTE_COD_ERROR_DK = "DK";										
	public static final String CTE_COD_ERROR_DL = "DL";										
	public static final String CTE_COD_ERROR_DM = "DM";
	public static final String CTE_COD_ERROR_DN = "DN";
	public static final String CTE_COD_ERROR_DO = "DO";
	public static final String CTE_COD_ERROR_DP = "DP";
	public static final String CTE_COD_ERROR_DQ = "DQ";
	public static final String CTE_COD_ERROR_DR = "DR";
	public static final String CTE_COD_ERROR_DS = "DS";


	/** Códigos de aviso para incidencias informativas */
	public static final String CTE_COD_ERROR_I000 = "I0";
	public static final String CTE_COD_ERROR_I001 = "I1";
	public static final String CTE_COD_ERROR_I002 = "I2";
	public static final String CTE_COD_ERROR_I003 = "I3";
	public static final String CTE_COD_ERROR_I004 = "I4";		
	public static final String CTE_COD_ERROR_I005 = "I5";
	public static final String CTE_COD_ERROR_I006 = "I6";
	public static final String CTE_COD_ERROR_I007 = "I7";
	public static final String CTE_COD_ERROR_I008 = "I8";
	public static final String CTE_COD_ERROR_I009 = "I9";
	public static final String CTE_COD_ERROR_IA = "IA";
	public static final String CTE_COD_ERROR_IB = "IB";
	public static final String CTE_COD_ERROR_IC = "IC";
	public static final String CTE_COD_ERROR_ID = "ID";
	public static final String CTE_COD_ERROR_IE = "IE";
	public static final String CTE_COD_ERROR_IF = "IF";
	public static final String CTE_COD_ERROR_IG = "IG";
	public static final String CTE_COD_ERROR_IH = "IH";
	public static final String CTE_COD_ERROR_II = "II";
	public static final String CTE_COD_ERROR_IJ = "IJ";
	public static final String CTE_COD_ERROR_IK = "IK";
	public static final String CTE_COD_ERROR_IL = "IL";
	public static final String CTE_COD_ERROR_IM = "IM";
	public static final String CTE_COD_ERROR_IN = "IN";
	public static final String CTE_COD_ERROR_IO = "IO";
	public static final String CTE_COD_ERROR_IP = "IP";
												  
	
	
	/** Constantes para variables de remplazo para las descripciones de error de las funciones auxiliares */
	public static final String CTE_NOM_ATR_EN = "&NombreAtributoEntrada";
	public static final String CTE_VAL_ATR_EN = "&ValorAtributoEntrada";
	public static final String CTE_CLA_UMIC_REM = "&claveUmic";
	public static final String CTE_MOD_NOM_REM = "&ModuloNominal";
	public static final String CTE_NOM_PARA_EN = "&NombreParámetroEntrada";
	public static final String CTE_TIPO_CORRE = "&tipoCorriente";
	public static final String CTE_NOM_APO_REM = "&NombreVariableApoyo";
	public static final String CTE_RES_CODK_REM = "&codKX";
	public static final String CTE_CRI_EDAD = "&CriterioEdad";
	
	/** Constantes con las descripciones de error de las funciones auxiliares */
	public static final String CTE_DES_ERROR_A1 = " - Parámetro obligatorio no informado &NombreAtributoEntrada.";
	public static final String CTE_DES_ERROR_A2 = " - Valor &ValorAtributoEntrada incorrecto para &NombreAtributoEntrada.";
	public static final String CTE_DES_ERROR_A3 = " - Valor &ValorAtributoEntrada para &NombreAtributoEntrada no contemplado en alcance actual.";
	public static final String CTE_DES_ERROR_A4 = " - No se han encontrado datos de la proyección de vida en la base técnica BTI para la umic &claveUmic";
	public static final String CTE_DES_ERROR_A5 = "No se ha encontrado la Variable de Apoyo &NombreVariableApoyo";
	public static final String CTE_DES_ERROR_A6 = "No se ha encontrado la Variable de Apoyo &NombreVariableApoyo, finalizando el proceso para la UMIC";
	public static final String CTE_DES_ERROR_A7 = " - Umic en estado Anulado &claveUmic";
	public static final String CTE_DES_ERROR_A8 = "No se ha encontrado la Constante de Rescate &codKX, finalizando el proceso para la UMIC.";
	public static final String CTE_DES_ERROR_A9 = "Fecha de nacimiento mayor que fecha actual.";
	public static final String CTE_DES_ERROR_A10 = "Parámetro Fecha de Inicio de la Renta no informado para Criterio de Edad &CriterioEdad";
	public static final String CTE_DES_ERROR_AA = "En situación de reducción no se permiten Primas Periódicas, finalizando el proceso para la UMIC.";
	public static final String CTE_DES_ERROR_AB = "No existe el código de la Constante de Rescate para la UMIC, finalizando el proceso para la UMIC.";
	public static final String CTE_DES_ERROR_AF = " - Criterio de generación de fecha &NombreAtributoEntrada incorrecto (&ValorAtributoEntrada) para &tipoCorriente.";
	public static final String CTE_DES_ERROR_G1 = "Valor &ModuloNominal para &NombreParámetroEntrada no contemplado en alcance actual para la corriente &tipoCorriente";
	
	public static final int [] ARRAY_BASE_365 = { CTE_0, CTE_31, CTE_59, CTE_90, CTE_120, CTE_151, CTE_181, CTE_212, CTE_243, CTE_273, CTE_304, CTE_334 };

	public static final int [] ARRAY_BASE_360 = { CTE_0, CTE_30, CTE_60, CTE_90, CTE_120, CTE_150, CTE_180, CTE_210, CTE_240, CTE_270, CTE_300, CTE_330 };
	
	public static final BigDecimal [] GAMMA = new BigDecimal[12]; 
	static {
		GAMMA[0] = BigDecimal.ZERO;
		for(int i=1; i<12; i++) {
			GAMMA[i] = BigDecimal.valueOf(i).divide(CTE_OPER_12, MATH_CONTEXT);
		}
	}
	/**
	 * Validaciones criterio Fechas
	 */
	public static final Map<String, String> TIPO_CRITER_FECHA = new HashMap<String, String>();
	static {
		TIPO_CRITER_FECHA.put(CTE_CRI_FECHA_01, CTE_CRI_FECHA_01);
		TIPO_CRITER_FECHA.put(CTE_CRI_FECHA_02, CTE_CRI_FECHA_02);
		TIPO_CRITER_FECHA.put(CTE_CRI_FECHA_03, CTE_CRI_FECHA_03);
		TIPO_CRITER_FECHA.put(CTE_CRI_FECHA_04, CTE_CRI_FECHA_04);
	}

	public static final Map<String, String> TIPO_CRITER_EDAD = new HashMap<String, String>();
	static {
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_01, CTE_CRI_FECHA_01);
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_02, CTE_CRI_FECHA_02);
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_03, CTE_CRI_FECHA_03);
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_04, CTE_CRI_FECHA_04);
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_05, CTE_CRI_FECHA_05);
		TIPO_CRITER_EDAD.put(CTE_CRI_FECHA_06, CTE_CRI_FECHA_06);
	}

	public static final Map<Integer, Integer> FORPAGRENT = new HashMap<Integer, Integer>();
	static {
		FORPAGRENT.put(ConstantsFunciones.CTE_1, ConstantsFunciones.CTE_1);
		FORPAGRENT.put(ConstantsFunciones.CTE_2, ConstantsFunciones.CTE_2);
		FORPAGRENT.put(ConstantsFunciones.CTE_3, ConstantsFunciones.CTE_4);
		FORPAGRENT.put(ConstantsFunciones.CTE_4, ConstantsFunciones.CTE_12);
		FORPAGRENT.put(ConstantsFunciones.CTE_5, ConstantsFunciones.CTE_3);
	}
		
	/**
	 * Cte usadas en función vtx005
	 */
	public static final String CTE_POL_NO_RED = "VI";
//INI-TAR00433819
	public static final String CTE_POL_PRORROGA_PU = "U";
	public static final String CTE_POL_PRORROGA_PP = "F";
//FIN-TAR00433819
	public static final String CTE_APOR_UNICA = "U";
	public static final String CTE_APOR_PERIO = "P";
	public static final String CTE_APOR_REDUCIDA = "RE";

	/**
	 * Cte función cmorpend
	 */
	public static final String CTE_SR = "SR";
	
	/**
	 * CTE FECHAS PAGO y DEVENGO
	 */
	public static final String VAR_FECHA_PAGO = "varFechaPago";
	public static final String VAR_FECHA_DEVENGO = "varFechaDevengo";
	
	/**
	 * CTE FORMAS DE PAGO
	 */
	public static final String CTE_FORMPAGO_1 = "1";
	public static final String CTE_FORMPAGO_2 = "2";
	public static final String CTE_FORMPAGO_3 = "3";
	public static final String CTE_FORMPAGO_4 = "4";
	public static final String CTE_FORMPAGO_9 = "9";
    /**
	 * Cte utilizadas en función tarifaXRenoHasta
	 */
	public static final String CTE_GARANTIA_PRINCIPAL = "P";

	


	
}
