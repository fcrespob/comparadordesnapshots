/**MODIFICACION: TAR00433819
  FECHA: 24/09/2018
  DESCRIP: Se incluyen constantes para los nuevos estados de prorrogas U,F
 */
package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;							
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que contiene las constantes necesarias para usar en el modulo de proceso y Modulos.
 * @author rschacon
 *
 */
public final class ConstantsModulos {
	
	private ConstantsModulos() {
	}

	/**
	 * rutas de configuracion.
	 */
	public static final String RUTA_MAP_MODULOS = "conf/mapeosModulos.properties";
	
	/**
	 * Cte primera iteración
	 */
	public static final Integer CTE_FIRST_ITER = 1;
	
	/**
	 * Constantes parametros modulo invocados desde Programas
	 */
	public static final Integer PARAM_P_PROYUMIC = 0;
	public static final Integer PARAM_P_PROY_BLK = 1;
	public static final Integer PARAM_P_ITERACI = 2;
	public static final Integer PARAM_P_FCALC = 3;
	public static final Integer PARAM_P_UMIC = 4;
	public static final Integer PARAM_P_BTC_UMIC = 5;
	public static final Integer PARAM_P_MAP_VARIA = 6;
	public static final Integer PARAM_P_SUBPROC = 7;
	public static final Integer PARAM_P_TERMINAL = 8;
	public static final Integer PARAM_P_FPROCES = 8;											 
	
	/** Cte parametros modulo base tecnica */
	public static final Integer PARAM_UMI_BASE_TEC = 1;
	
	/**
	 * Cte parametros modulo periodos
	 */
	public static final Integer PARAM_BTC = 0;
	public static final Integer PARAM_UMI_PERIODO = 1;
	public static final Integer PARAM_FCA_PERIODO = 2;
	public static final Integer PARAM_FCA_CIERRE = 3;
	public static final Integer PARAM_LST_CORRIEN = 4;
	public static final Integer PARAM_SUBRPROCESO_PERIODOS = 5;
	public static final Integer PARAM_MAP_VARIABLES_PERI = 6;
	
	/**
	 * Cte parametros proyecciones
	 */
	public static final Integer PARAM_PRO_LST_PER = 0;
	public static final Integer PARAM_PRO_CONFIGU = 1;
	public static final Integer PARAM_PRO_FIC_PRO = 2;
	public static final Integer PARAM_PRO_UMIC = 3;
	public static final Integer PARAM_PRO_DET_COR = 4;
	
	/**
	 * Constantes parametros modulo
	 */
	public static final Integer PARAM_DET_CORR_PROV_MAT = 0;
	public static final Integer PARAM_P_FCALC_PROV_MAT = 1;
	public static final Integer PARAM_UMIC_PROV_MAT = 2;
	public static final Integer PARAM_BTC_UMIC_PROV_MAT = 3;
	public static final Integer PARAM_MAP_VARIA_PROV_MAT = 4;
	
	/**
	 * Constantes para módulo de calculo de fechas pago y devengo
	 */
	public static final Integer PARAM_UMIC_FECHAS = 0;
	public static final Integer PARAM_FIC_PROC_FECHAS = 1;
	public static final Integer PARAM_BTC_FECHAS = 2;
	public static final Integer PARAM_DETALLE_FECHAS = 3;
	public static final Integer PARAM_SUBRPROCESO_FECHAS = 4;
	public static final Integer PARAM_MAPA_VAR_FECHAS = 5;
	

	/**
	 * Valores tramoX
	 */
	public static final int CTE_INT_1 = 1;
	public static final int CTE_INT_2 = 2;
	public static final int CTE_INT_3 = 3;
	public static final int CTE_INT_4 = 4;
	public static final int CTE_INT_5 = 5;
	public static final int CTE_INT_28 = 28;
	public static final int CTE_INT_29 = 29;
	public static final int CTE_INT_30 = 30;	
	public static final int CTE_INT_128 = 128;
	/**
	 * CTE Calculo Operaciones
	 */
	public static final String CTE_30 = "30";
	public static final String CTE_0 = "0";
	public static final String CTE_2 = "2";
	public static final String CTE_3 = "3";
	public static final String CTE_300_PUNTO_51 = "300.51";
	public static final Integer CTE_25 = 25;
	public static final Integer CTE_100 = 100;
	public static final Integer CTE_14 = 14;
	public static final Integer CTE_80 = 80;
	public static final Integer CTE_195 = 195;
	public static final BigDecimal CTE_OPER_0_PUNTO_0001 = new BigDecimal(CTE_300_PUNTO_51);
	public static final BigDecimal CTE_OPER_25 = new BigDecimal(CTE_25);
	public static final BigDecimal CTE_OPER_100 = new BigDecimal(CTE_100);
	public static final BigDecimal CTE_OPER_14 = new BigDecimal(CTE_14);
	public static final BigDecimal CTE_OPER_80 = new BigDecimal(CTE_80);
	public static final BigDecimal CTE_OPER_195 = new BigDecimal(CTE_195);


	/**
	 * cte SI.
	 */
	public static final String CTE_S = "S";

	/**
	 * cte KBENCON
	 */
	public static final String CTE_KBENCON_301  = "301";
	public static final String CTE_KBENCON_302  = "302";
	public static final String CTE_KBENCON_BNC  = "BNC";
	public static final String CTE_KBENCON_UMIC_PPAL = "TIT";
	/**
	 * cte NO.
	 */
	public static final String CTE_N = "N";

	/**
	 * cte Estado Aseg
	 */
	public static final String CTE_CESTADO_ASEG_A = "A";
	public static final String CTE_CESTADO_ASEG_F = "F";
	public static final String CTE_CESTADO_ASEG_V = "V";
	public static final String CTE_CESTADO_ASEG_M = "M";
	public static final String CTE_CESTADO_ASEG_S = "S";
	public static final String CTE_CESTADOSEG_1 = "1";

	/**
	 * Cte validacion base tenica BTI.
	 */
	public static final String CTE_VAL_BTI = "BTI";
	
	/**
	 * Cte validacion base tenica MULTI5 NIIF17.
	 */
	public static final String CTE_VAL_MULTI5 = "MULTI5";
	
	/** 
	 * Cte validacion base tencica MULTI4 NIIF17
	 * */
	
	public static final String CTE_VAL_MULTI4 = "MULTI4";
	public static final String CTE_VAL_MULTI4NB = "MULTI4NB";
	public static final String CTE_VAL_MULTI4C = "MULTI4C";
	public static final String CTE_VAL_MULTI8 = "MULTI8";
	public static final String CTE_VAL_MULTI8NB = "MULTI8NB";
	public static final String CTE_VAL_MULTISN = "MULTISN";
	
	/**
	 * Cte validacion base tenica NIIF17IF.
	 */
	public static final String CTE_VAL_NIIF17IF = "NIIF17IF";

	/**
	 * Cte validacion base tenica BEL.
	 */
	public static final String CTE_VAL_BASE_BEL = "BEL";

	/**
	 * Cte validacion base tenica ROSSP.
	 */
	public static final String CTE_VAL_ROSSP = "ROSSP";
	
	/**
	 * Ctevalidacion base tecnica BELCOA.
	 */
	public static final String CTE_VAL_BELCOA = "BELCOA";
	
	/**
	 * Ctevalidacion base tecnica BTCOA.
	 */
	public static final String CTE_VAL_BTCOA = "BTCOA";

	/**
	 * Ctevalidacion base tecnica MULTICOA.
	 */
	public static final String CTE_VAL_MULTICOA = "MULTICOA";
	/**
	 * Cte validacion base tenica SRC.
	 */
	public static final String CTE_VAL_BASE_SCR = "SCR";
	
	/**
	 * Ctevalidacion base tecnica BELCLR.
	 */
	public static final String CTE_VAL_BELCLR = "BELCLR";
	
	/**
	 * Ctevalidacion base tecnica ROSSEARC.
	 */
	public static final String CTE_VAL_ROSSEARC = "ROSSEARC";
/**
	 * Cte validacion base tenica BTI.
	 */
	public static final String CTE_VAL_BTI_PROY = "BTIPROY";
	
	/**
	 * Ctevalidacion base tecnica ROSSPCSM.
	 */
	public static final String CTE_VAL_ROSSPCSM = "ROSSPCSM";
	
	/**
	 * Ctevalidacion base tecnica BTCOATF.
	 */
	public static final String CTE_VAL_BTCOATF = "BTCOATF";
	
	/**
	 * Ctevalidacion base tecnica ROSSPTE.
	 */
	public static final String CTE_VAL_ROSSPTE = "ROSSPTE";
	
	/**
	 * Ctevalidacion base tecnica ROSSPTI.
	 */
	public static final String CTE_VAL_ROSSPTI = "ROSSPTI";
	
	/**
	 * Ctevalidacion base tecnica ROSSPGA.
	 */
	public static final String CTE_VAL_ROSSPGA = "ROSSPGA";
	
	/**
	 * Cte base tenica BTI.
	 */
	public static final String CTE_BTI = "BTI";

	/**
	 * Cte base tenica BEL.
	 */
	public static final String CTE_BT_BEL = "BEL";

	/**
	 * Cte base tenica ROSSP.
	 */
	public static final String CTE_BT_ROSSP = "ROSSP";

	/**
	 * Cte base tenica SRC.
	 */
	public static final String CTE_BT_SCR = "SCR";
	
	/**
	 * Cte base tenica NIIF17.
	 */
	public static final String CTE_BT_NIIF17 = "NIIF17";
	public static final String CTE_BT_NIF17LIR = "NIIF17LIR";
	public static final String CTE_BT_N17LIRIN = "N17LIRIN";
	public static final String CTE_BT_NIFF17OCI = "NIIF17OCI";
	public static final String CTE_BT_NIIF17IF = "NIIF17IF";
	public static final String CTE_BT_N17CLIR = "N17CLIR";
	
	public static final String CTE_BTI_PROY = "BTIPROY";
	public static final String CTE_BT_MULTI6 = "MULTI6";
	
	/**
	 * Cte base tenica ROSSPCSM.
	 */
	public static final String CTE_BT_ROSSPCSM = "ROSSPCSM";											 
	 
	/*
	 * Ctevalidacion bases tecnica SCR.
	 */
	public static final String CTE_VAL_SCRTIU = "SCRTIU";
	public static final String CTE_VAL_SCRTID = "SCRTID";
	public static final String CTE_VAL_SCRMFE = "SCRMFE";
	public static final String CTE_VAL_SCRMMI = "SCRMMI";
	public static final String CTE_VAL_SCRMCF = "SCRMCF";
	public static final String CTE_VAL_SCRMCI = "SCRMCI";
	public static final String CTE_VAL_SCRLFE = "SCRLFE";
	public static final String CTE_VAL_SCRLMI = "SCRLMI";
	public static final String CTE_VAL_SCRINC = "SCRINC";
	public static final String CTE_VAL_SCRGTO = "SCRGTO";
	public static final String CTE_VAL_SCRANM = "SCRANM";
	public static final String CTE_VAL_SCRAEP = "SCRAEP";
	public static final String CTE_VAL_SCRAEN = "SCRAEN";
	public static final String CTE_VAL_SCRAIP = "SCRAIP";
	public static final String CTE_VAL_SCRAIN = "SCRAIN";
	public static final String CTE_VAL_SCRVM  = "SCRVM" ;
	public static final String CTE_VAL_NF17MFE = "NF17MFE";
	public static final String CTE_VAL_NF17AEN = "NF17AEN";
	public static final String CTE_VAL_NF17GTO = "NF17GTO";
	/**
	 * Cte validacion criterios periodos BTI.
	 */
	public static final String CTE_VAL_PER_BTI = "BTI";

	/**
	 * Cte validacion criterios periodos LEIDOS.
	 */
	public static final String CTE_VAL_PER_LEI = "LEIDOS";

	/**
	 * Cte validacion tipo prestacion Temporal.
	 */
	public static final String CTE_VAL_TP_TEM = "T";

	/**
	 * Cte validacion tipo prestacion Vitalicia.
	 */
	public static final String CTE_VAL_TP_VIT = "V";
	
	/**
	 * Cte validacion tipo prestacion Local.
	 */
	public static final String CTE_VAL_TP_LOC = "L";

	/**
	 * Cte revalorizacion de renta Aritmetica.
	 */
	public static final String CTE_REV_RENTA_ARI = "A";

	/**
	 * Cte revalorizacion de renta Geometrica.
	 */
	public static final String CTE_REV_RENTA_GEO = "G";
	
	/**
	 * Cte revalorizacion de renta Negativa.
	 */
	public static final String CTE_REV_RENTA_NEG = "N";

	/**
	 * Cte revalorizacion de renta constante.
	 */
	public static final String CTE_REV_RENTA_CON = "C";

	/**
	 * Cte validacion codigo revalorizacion de renta Aritmetica.
	 */
	public static final String CTE_VAL_REV_ARI = "A";

	/**
	 * Cte validacion codigo revalorizacion de renta Geometrica.
	 */
	public static final String CTE_VAL_REV_GEO = "G";
	
	/**
	 * Cte validacion codigo revalorizacion de renta Negativa.
	 */
	public static final String CTE_VAL_REV_NEG = "N";

	/**
	 * Cte validacion codigo revalorizacion de renta Constante.
	 */
	public static final String CTE_VAL_REV_CON = "C";

	/**
	 * Cte validacion periodicidad pago de la renta Anual.
	 */
	public static final String CTE_VAL_PER_ANUAL = "1";

	/**
	 * Cte validacion periodicidad pago de la renta Semestral.
	 */
	public static final String CTE_VAL_PER_SEMES = "2";

	/**
	 * Cte validacion periodicidad pago de la renta Trimestral.
	 */
	public static final String CTE_VAL_PER_TRIME = "3";

	/**
	 * Cte validacion periodicidad pago de la renta Mensual.
	 */
	public static final String CTE_VAL_PER_MEN = "4";

	/**
	 * Cte  forma de revalorizacion de la renta Natural.
	 */
	public static final String CTE_VAL_FR_AN_NAT = "1";
	
	/**
	 * Cte  forma de revalorizacion de la renta Aniversario.
	 */
	public static final String CTE_VAL_FR_ANIVER = "2";

	/**
	 * Cte validacion forma de revalorizacion de la renta Natural.
	 */
	public static final String CTE_FR_ANIO_NATU = "1";
	public static final String CTE_FR_ANIO_NATU_2 = "2";
	public static final String CTE_FR_ANIO_NATU_3 = "3";
	public static final String CTE_FR_ANIO_NATU_4 = "4";
	public static final String CTE_FR_ANIO_NATU_5 = "5";
	public static final String CTE_FR_ANIO_NATU_6 = "6";
	public static final String CTE_FR_ANIO_NATU_7 = "7";
	public static final String CTE_FR_ANIO_NATU_8 = "8";
	public static final String CTE_FR_ANIO_NATU_9 = "9";
	public static final String CTE_FR_ANIO_NATU_O = "O";
	public static final String CTE_FR_ANIO_NATU_N = "N";
	public static final String CTE_FR_ANIO_NATU_D = "D";
												 
	/**
	 * Cte validacion forma de revalorizacion de la renta Aniversario.
	 */
	public static final String CTE_FR_ANIVER = "2";
	
	/** Umic Titular*/
	public static final String CTE_UMIC_PPAL = "101";
	
	/**
	 * Ctes revalorizacion de la prima geométrica
	 */
	public static final Set<String> CTE_REV_GEO = new HashSet<String>();
	static{
		CTE_REV_GEO.add("V");
		CTE_REV_GEO.add("G");
	}
	
	/**
	 * Ctes revalorizacion de la prima aritmética
	 */
	public static final Set<String> CTE_REV_ARI = new HashSet<String>();
	static{
		CTE_REV_ARI.add("F");
		CTE_REV_ARI.add("A");
	}
	
	/**
	 * Cte para forma de pago, pago unico.
	 */
	public static final String CTE_CFORMPAG_UNICA = "9";
	
	/**
	 * Cte validacion vencimiento Periodos
	 */
	public static final String CTE_VAL_VENCI = "VENCI";
	public static final String CTE_VAL_VITAL = "VITAL";
	public static final String CTE_VAL_TAR = "TAR";
	
	/**
	 * Cte validacion periodicidad periodos
	 */
	public static final String CTE_VAL_MENSU = "MENSU";
	public static final String CTE_VAL_RENO = "RENO";
	public static final String CTE_VAL_MNAT = "MNAT";
	public static final String CTE_VAL_REVEN = "REVEN";
	
	/**
	 * Cte validacion vencimiento periodos vitalicia.
	 */
	public static final String CTE_VAL_VEN_VIT = "01";

	public static final String CTE_TABLACONV_ASEG1 = "tablaconv_aseg1";
	public static final String CTE_TABLACONV_ASEG2 = "tablaconv_aseg2";
	public static final String CTE_TABLACONV_ASEG3 = "tablaconv_aseg3";
	public static final String CTE_TABLACONV_ASEG4 = "tablaconv_aseg4";
	public static final String CTE_TABLACONV_ASEG5 = "tablaconv_aseg5";
	
	/**
	 * Cte validacion vencimiento periodos edad exclusion.
	 */
	public static final String CTE_VAL_VEN_ED_EX = "02";

	/**
	 * Cte validacion vencimiento periodos num fijo periodos.
	 */
	public static final String CTE_VAL_VEN_NUM = "03";

	/**
	 * Cte validacion vencimiento periodos tratamiento TAR.
	 */
	public static final String CTE_VAL_VEN_TAR = "04";

	/**
	 * Cte validacion vencimiento periodos planificados.
	 */
	public static final String CTE_VAL_VEN_PLANI = "05";

	/**
	 * Cte validacion vencimiento periodos hasta vencimiento.
	 */
	public static final String CTE_VAL_VEN_HASTA = "06";

	/**
	 * Cte validacion periodicidad planificada.
	 */
	public static final String CTE_VAL_PER_PLANI = "03";

	/**
	 * Cte validacion periodicidad mensual.
	 */
	public static final String CTE_VAL_PER_MENSU = "01";

	/**
	 * Cte validacion periodicidad anual.
	 */
	public static final String CTE_VAL_PER_ANU = "02";

	/**
	 * Cte periodicidad planificada.
	 */
	public static final String CTE_PER_PLANI = "03";

	/**
	 * Cte periodicidad mensual.
	 */
	public static final String CTE_PER_MENSUAL = "01";

	/**
	 * Cte periodicidad anual.
	 */
	public static final String CTE_PER_ANUAL = "02";

	/**
	 * Cte Periodos vencimiento.
	 */
	public static final String CTE_PERIODO_VEN = "06";

	/**
	 * Cte Periodos planificada.
	 */
	public static final String CTE_PERIODO_PLANI = "05";

	/**
	 * Cte Periodos vitalicia.
	 */
	public static final String CTE_PERIODO_VIT = "01";
	
	/**
	 * Cte Tipo prestacion temporal.
	 */
	public static final String CTE_TIPO_PRES_TEM = "T";

	/**
	 * Cte Tipo prestacion vitalicia.
	 */
	public static final String CTE_TIPO_PRES_VIT = "V";
	
	/**
	 * Constante Tipo prestacion local
	 */
	public static final String CTE_TIPO_PRES_LOCA = "L";

	/**
	 * Cte Datos generales csitupol anulada.
	 */
	public static final String CTE_DG_CSITU_ANU = "AN";

	/**
	 * Cte Datos generales csitupol no reducida.
	 */
	public static final String CTE_DG_CSI_NO_RED = "VI";

	/**
	 * Cte Datos generales csitupol reducida.
	 */
	public static final String CTE_DG_CSITU_RED = "RE";
	
//INI-TAR00433819
	/**
	 * Cte Datos generales csitupol Prorrogas de PU Y PP.
	 */
		public static final String CTE_DG_CSITU_PRORROGA_PU = "U";
		public static final String CTE_DG_CSITU_PRORROGA_PP = "F";
		
/**
 * Cte Datos generales csitupol Prorrogas de PU Y PP.
 */
		public static final String CTE_LEIDOBTI = "LEIDOBTI";
//FIN-TAR00433819
	public static final String CTE_LEIDOBTIPR = "LEIDOBTIPR";	
	
	/**
	* Cte Datos generales ROSSP.
	*/
	public static final String CTE_LEIDOROSSP = "LEIDOROSSP";
	/** Mes pago comisiones para el dia 31/12/yyyy */
	public static final String CTE_DIAMES_3112 = "3112";
	
	public static final String CTE_RENTA_TEMPORAL = "T";
	public static final String CTE_RENTA_VITALICIA = "V";
	
	public static final String CTE_UMIC_PRINCIPAL = "P";
	public static final String CTE_UMIC_SECUNDARIA = "S";
	
	/**
	 * Cte variables modulos
	 */
	public static final String CTE_VAR_EDAD = "varEdad";
	public static final String CTE_FEC_EFEC = "varfechaEfecto";
	public static final String CTE_POC_45 = "varPoc45";
	public static final String CTE_POC_60 = "varPoc60";
	public static final String CTE_VAR_PORREVAL = "varPorreval";
	public static final String CTE_VAR_CSP015 = "varcsp015";																																																		 
	public static final String CTE_VAR_CSPVIU = "varcspVIU";
	public static final String CTE_VAR_CSPVIUBis = "varcspVIUBis";
	public static final String CTE_FEC_INI_AMORT = "fecIniAmort";
	public static final String CTE_CUADRO_AMORT_UMIC = "cuadroAmortUmic";
	public static final String CTE_VAR_NC_MESES = "varncmeses";
	public static final String CTE_VAR_CSPORFA = "varcsporfa";
	public static final String CTE_VAR_CSPORFP = "varcsporfp";
															   
	public static final String CTE_VA_POR_INI = "POR_INI";
	public static final String CTE_VA_POR_DEC = "POR_DEC";
	public static final String CTE_VA_PER_DEC = "PER_DEC";
	public static final String CTE_NUM_ANUA = "varNumAnualidades";
	public static final String CTE_VAR_TCMINI = "varTCmini";
	public static final String CTE_VAR_TCM = "varTCm";
	public static final String CTE_VAR_CJP = "varCJP";												
	public static final String CTE_VAR_TC = "varTC";
	public static final String CTE_VAR_TTM = "varTtm";
	public static final String CTE_VAR_TM = "varTm";										 
	public static final String CTE_VAR_PROB_VIDA = "varProbVida";
	public static final String CTE_VAR_PROB_FALL = "varProbFall";
	public static final String CTE_VAR_PROB_GAST = "varProbGast";
	public static final String CTE_VAR_PROB_COM = "varProbCom";
	public static final String CTE_VAR_PROB_INV = "varProbInv";
	public static final String CTE_VAR_PROB_RESC = "varProbResc";
	public static final String CTE_VAR_PROB_PRIM = "varProbPrim";
	public static final String CTE_VAR_ANU_VIDA = "varAnuVida"; 
	public static final String CTE_VAR_ANU_FALL = "varAnuFall"; 
	public static final String CTE_VAR_ANU_GAST = "varAnuGast"; 
	public static final String CTE_VAR_ANU_COM = "varAnuCom";  
	public static final String CTE_VAR_ANU_INV = "varAnuInv";  
	public static final String CTE_VAR_ANU_RESC = "varAnuResc"; 
	public static final String CTE_VAR_ANU_PRIM = "varAnuPrim";
	public static final String CTE_VAR_ACT_VIDA = "varActVida"; 
	public static final String CTE_VAR_ACT_FALL = "varActFall"; 
	public static final String CTE_VAR_ACT_GAST = "varActGast"; 
	public static final String CTE_VAR_ACT_COM = "varActCom";  
	public static final String CTE_VAR_ACT_INV = "varActInv";  
	public static final String CTE_VAR_ACT_RESC = "varActResc"; 
	public static final String CTE_VAR_ACT_PRIM = "varActPrim"; 
	public static final String CTE_VAR_FEC502G = "FEC502G";

														  
	public static final String CTE_VAR_EDAD_NJ = "varEdadNJ";
	public static final String CTE_VAR_FRACC0 = "varFracc0";
	public static final String CTE_VAR_FRACC2 = "varFracc2";
	public static final String CTE_VAR_FD_ANT = "varFecDevAnt";
	public static final String CTE_VAR_LA_TCM = "varLaTCm";
	public static final String CTE_VAR_TI_INT = "varTipoInteres";
	public static final String CTE_VAR_BETA = "varBeta";
	public static final String CTE_VAR_PP = "varPP";
	public static final String CTE_VARL1 = "varl1";
	public static final String CTE_VARL2 = "varl2";
	public static final String CTE_VAR_TC0 = "varTc0";
	public static final String CTE_VARL = "varL";
	public static final String CTE_VAR_NA = "varna";												
	public static final String CTE_EDAD_CAL = "varEdadCalc";
	public static final String CTE_TAB_MORT = "varTabMort";
	public static final String CTE_VAR_LZC = "varLzc";
	public static final String CTE_VAR_LZC_ENT = "varLzcEntero";
	public static final String CTE_VAR_FECDIFERIMIENTO = "varFecDiferimiento";
	public static final String CTE_EDAD_CAL1 = "varEdadCalc1";
	public static final String CTE_VAR_LJ1 = "varLj1";
	public static final String CTE_VAR_LJ2 = "varLj2";
	public static final String CTE_TAB_MORT1 = "varTabMort1";
	public static final String CTE_VAR_LZC1 = "varLzc1";
	public static final String CTE_VAR_ZC = "varZc";
	public static final String CTE_VAR_ZC1 = "varZc1";
	public static final String CTE_VAR_ZC2 = "varZc2";
	public static final String CTE_EDAD_CAL2 = "varEdadCalc2";
	public static final String CTE_TAB_MORT2 = "varTabMort2";
	public static final String CTE_VAR_LZC2 = "varLzc2";
	public static final String CTE_VAR_REVER = "varRever";
	public static final String CTE_VAR_DIFERCOL = "varVzc1Difercol";
	public static final String CTE_IND_PROV_UMI = "varIndProviUmic";
	public static final String CTE_VAR_J_CASADO = "varJcasado";
	public static final String CTE_PROY_VIDA_UMI = "varProyVidaUmic";
	public static final String CTE_IMP_ACUMULADO = "varImpAcumulado";
	public static final String CTE_W_FEC_RENOV = "wFechaRenov";
	public static final String CTE_VAR_PN0 = "varPN0";
	public static final String CTE_CABCORRIENTE = "cabeceraCorriente";
	public static final String CTE_BLOQUE_CORRIENTE = "bloqueCorriente";
	public static final String CTE_VAR_PNA0 = "varPNA0";
	public static final String CTE_VAR_PNAtc = "varPNAtc";												   
	public static final String CTE_VAR_PRP = "varPRP";
	public static final String CTE_VAR_OP_PRP = "opVarPRP";
	public static final String CTE_VAR_DURACION = "varDuracion";
	public static final String CTE_VAR_FALL_RED = "varFallRed";
	public static final String CTE_VAR_CAP_TC = "varCapTc";
	public static final String CTE_VAR_GIC = "varGic";
	public static final String CTE_VAR_GIPC_PRIMA = "vargipcPrima";
	public static final String CTE_VARI1 = "varI1";
	public static final String CTE_VARI2 = "varI2";
	public static final String CTE_VARN = "varN";
	public static final String CTE_VAR_DIFER = "varDifer";
	public static final String CTE_VAR_FDIFER = "varFdifer";
	public static final String CTE_VAR_DIFER_COL = "varDiferCol";
	public static final String CTE_VAR_FECNAC = "varfecnac";
	public static final String CTE_VAR_P = "varP";
	public static final String CTE_VAR_P0 = "varP0";
	public static final String CTE_VAR_X = "varX";
	public static final String CTE_VAR_XC = "varXC";
	public static final String CTE_VAR_K = "varK";
	public static final String CTE_VAR_LIMITE = "varLimite";
	public static final String CTE_LISTA_LIMITE = "listaLimite";
	public static final String CTE_VAR_LIMITE_CAP = "varLimiteCap";
	public static final String CTE_VAR_GAMMA = "varGamma";
	public static final String CTE_VAR_FEC_DEV = "varFechaDevengo";
	public static final String CTE_VAR_FEC_FPROY = "varFecFinProy";
	public static final String CTE_VA_ANNOS = "vaAnnos";
	public static final String CTE_VAR_GE = "varGE";
	public static final String CTE_VAR_GF = "varGF";											 
	public static final String CTE_VAR_OP_GE =  "opVarGE";
	public static final String CTE_VAR_DELTA = "delta";
	public static final String CTE_VAR_INT = "varInt";
	public static final String CTE_VAR_PRC = "varPRC";											   
	public static final String CTE_VAR_RECARGO = "varRecargo";
	public static final String CTE_VAR_RPF = "varRPF";
	public static final String CTE_VAR_OP_RPF = "opeVarRPF";
	public static final String CTE_VAR_NP =  "varNP";
	public static final String CTE_VAR_OP_NP =  "opVarNP";
	public static final String CTE_VAR_NPP =  "varNPP";
	public static final String CTE_VAR_INCRIPC =  "varIncrIpc";
	public static final String CTE_VAR_GTOANUAL =  "varGtoAnual";
	public static final String CTE_VAR_CLAVEPCTGTOPROVNPP =  "varPctGtoProvNpp";
	public static final String CTE_VAR_NR = "varNR";
	public static final String CTE_VAR_Z = "varZ";										   
	public static final String CTE_VAR_Y = "varY";
	public static final String CTE_VAR_NRM = "varNRM";
	public static final String CTE_VAR_PROXRENOVA = "varProxRenova";
	public static final String CTE_VAR_PUCCAPDIFER = "VarPUCCAPdifer";
	public static final String CTE_VAR_M = "varM";
	public static final String CTE_VAR_PPRI005 = "varPri005";
	public static final String CTE_VAR_W = "varW";
	public static final String CTE_VAR_WX = "varWX";
	public static final String CTE_VAR_WY = "varWY";
	public static final String CTE_VAR_REN = "varRen";
	public static final String CTE_VAR_REVALG = "varRevalg";													 
	public static final String CTE_VAR_TCY = "varTCy";
	public static final String CTE_VAR_TCY1 = "varTCy1";
	public static final String CTE_VAR_DC = "varDc";
	public static final String CTE_VAR_DR = "varDr";
	public static final String CTE_VAR_D = "varD";
	public static final String CTE_VAR_UMIC_PROYEC_BTI = "varUmicProyecBTI";
	public static final String CTE_LST_PROY = "varLstProy";
	public static final String CTE_LST_PRI = "varLstPri";
	public static final String CTE_VAR_RY = "varRy";
	public static final String CTE_VAR_ANO_NAC = "varAnoNac";
	public static final String CTE_VAR_PRIESGO = "varPriesgo";													  													   
	public static final String CTE_PART_ANO_NR = "varPartAnoNR";
	public static final String CTE_PART_ANO_NR_HASTA_RENOVA = "varPartAnoNRHastaRenova";
	public static final String CTE_PART_ANO_NR_RENTAS_INI = "varPartAnoNRRentasIni";
	public static final String CTE_PROY_BTI_FCAL = "varProvBtiFcal";
	public static final String CTE_VAR_TERMINAL = "varTerminal";
	public static final String CTE_PROV_NOMINAL = "varProvNominal";
	public static final String CTE_PROY_NOMINAL = "varProyNominal";
	public static final String CTE_PROY_204 = "varProy204";																												
	public static final String CTE_VAR_ALFA = "varAlfa";
	public static final String CTE_VAR_ALFAM = "varAlfam";
	public static final String CTE_VAR_VAL_TAB_MORT = "varValoresTabMort";
	public static final String CTE_VAR_RENT_GEO_2IT = "varRentgeo2it";
	public static final String CTE_VAR_VAL_TAB_MORT_Q = "varValoresTabMortQ";
	public static final String CTE_VAR_VAL_TAB_MORT_X = "varValoresTabMortX";
	public static final String CTE_VAR_VAL_TAB_MORT_Y = "varValoresTabMortY";
	public static final String CTE_VAR_TAB_INV = "varTabInv";													  
	public static final String CTE_VAR_VAL_TAB_INV = "varValoresTabInv";
	public static final String CTE_VAR_VAL_TAB_MORT1 = "varValoresTabMort1";
	public static final String CTE_VAR_VAL_TAB_MORT2 = "varValoresTabMort2";
	public static final String CTE_VAR_ANULACION = "varValoresAnulacion";
	public static final String CTE_VAR_ANULACION_ANM = "varValoresAnulacionANM";
	public static final String CTE_VAR_GTO_REAL = "varGtoRealUmic";
	public static final String CTE_VAR_FACTOR_REV = "varFactorRev";										
	public static final String CTE_IPC_FUTURO = "varIpcFuturo";
	public static final String CTE_LST_CORR_UMIC = "listaCorrienteUmic";
	public static final String CTE_VAR_ASUBX = "aSubX";
	public static final String CTE_VAR_ASUBXD = "aSubXD";
	public static final String CTE_VAR_GGVTN = "gastgivitini";
	public static final String CTE_LST_CURVA_TIPO = "varValoresCurva";
	public static final String CTE_VAR_CSP = "varCsp";
	public static final String CTE_VAR_CSP011 = "varCsp011";
	public static final String CTE_VAR_CSP071 = "varCsp071";
	public static final String CTE_VAR_CSP307 = "varCsp307";
	public static final String CTE_VAR_GIC_DIV_100 = "varGicDiv100";
	public static final String CTE_VAR_FACTOR_VRTA = "varFactorVrta";
	public static final String CTE_VAR_FACTOR_VRTAM = "varFactorVrtaM";
	public static final String CTE_VAR_FACTOR_VRTAN = "varFactorVrtaN";
	public static final String CTE_UNO_MAS_VAR_IFAL = "varUnoMasVarIfal";
	public static final String CTE_UNO_MAS_VAR_PRP = "varUnoMasVarPrp";
	public static final String CTE_PRP_ENTRE100 = "varPrpEntre100";
	public static final String CTE_PRP_DIV_IFAL = "prpDivIfal";
	public static final String CTE_UNO_MENOS_VAR_GEPC = "varUnoMenosVarGepc";
	public static final String CTE_VAR_BETA1 = "varBeta1";
	public static final String CTE_VAR_BETA2 = "varBeta2";
	public static final String CTE_VAR_EDAD_MAXIMA_2 = "varEdadMaxima2";
	public static final String CTE_VAR_EDAD_J1_ENTERO =  "varEdadJ1Entero";
	public static final String CTE_VAR_EDAD_J2_ENTERO = "varEdadJ2Entero";
	public static final String CTE_VAR_FECHA_VCTO = "varFecVcto";
	public static final String CTE_VAR_ANT_RENOVA = "varAntRenova";
	public static final String CTE_VAR_PROX_RENOVA = "varProxRenova";
    public static final String CTE_VAR_DESDERENOV = "varFecDesdeRenova";
    public static final String CTE_VAR_HASTARENOV = "varFecHastaRenova";
    public static final String CTE_VAR_P1 = "varP1";
    public static final String CTE_VAR_VX = "varVx";
    public static final String CTE_VAR_VX1 = "varvx1";
    public static final String CTE_VAR_VZC1 = "varVzc1";
    public static final String CTE_VAR_PINV = "varPinv";
    public static final String CTE_VAR_PNA = "varPna";
    public static final String CTE_VAR_PNAJ = "varPnaj";
    public static final String CTE_MAPA_ANNOS = "mapanAnnos";
    public static final String CTE_VAR_FCIERTA = "varFcierta";
    public static final String CTE_VAR_VRTA = "varVrta";
    public static final String CTE_VAR_FONDOSUMIC = "varFondosUmic";
    public static final String CTE_VAR_VLPK = "varVLPK";
    public static final String CTE_VAR_FACTOR_IPC = "varFactorIpc";
	public static final String CTE_VAR_FACTOR_I1 = "varFactorI1";
	public static final String CTE_VAR_FACTOR_I2 = "varFactorI2";
    public static final String CTE_VAR_FACTOR_RIESGO = "varFactorRiesgo";															 																	 
    public static final String CTE_VAR_FFIN = "varFfin";
    public static final String CTE_VAR_FP = "varFP";
    public static final String CLAVE_VAR_GASTGINI = "varGastgini";
    public static final String CLAVE_VAR_CLAVEPRINCIPAL = "varClavePrincipal";
    public static final String CTE_VAR_IZCJ = "varIzcj";
    public static final String CTE_VAR_AJUSTE = "varAjuste";    
    public static final String CTE_VAR_T = "vart";  
    public static final String CTE_VAR_AFRXT = "varAfrxt";
    public static final String CTE_VAR_VBX162 = "varVBX162";
    public static final String CTE_VAR_VBX164 = "varVBX164";
    public static final String CTE_VAR_VBX165 = "varVBX165";
    public static final String CTE_VAR_PIMSI = "varPimSi";
    public static final String CTE_VAR_CFANT = "varCFAnt";
    public static final String CTE_VAR_FPR = "varFPR";
    public static final String CTE_VFECFINTRAMO = "vfecfinTramo";
    public static final String CTE_VAR_GAST310 = "varGast310";
    public static final String CTE_VINTCALC = "vintCalc";
	public static final String CTE_VAR_DENOMINADOR = "varDenominador";
    public static final String CTE_VAR_FACTORI1 = "varFactorI1";
    public static final String CTE_VAR_TCM_ZC = "varTcmZc";
    public static final String CTE_VAR_TCM_RENO_HASTA = "varTcmRenoHasta";
    public static final String CTE_VAR_X_RENO_HASTA = "varXRenoHasta";
	public static final String CTE_VAR_GIPC = "varGipc";
	public static final String CTE_VAR_M_FORPAGRENT = "varMForpagrent";
	public static final String CTE_VAR_N_PER_GARAN = "varNperGaran";
	public static final String CTE_VAR_M_PER_GARAN = "varMperGaran";
	public static final String CTE_VAR_FEC_INI = "varFecIni";
	public static final String CTE_VAR_FACTOR_GF = "varFactorGF";
	public static final String CTE_VAR_FACTOR_PRGF = "varFactorPRGF";
	public static final String CTE_VAR_ANO_MAX = "varAnoMax";
	public static final String CTE_VAR_CMAX = "varCMax";
	public static final String CTE_VAR_PORCENTAJE = "varPorcentaje";
    public static final String CTE_VAR_CSP016 = "varCSP016";
	public static final String CTE_VAR_PPR = "varPPR";
    public static final String CTE_VAR_PPC_PPAL = "varPPCppal";
    public static final String CTE_VAR_PPT_PPAL = "varPPTppal";
    public static final String CTE_VAR_SUMA_F = "varSumaF";
	public static final String CTE_VAR_PNEA = "varPNEA";
	public static final String CTE_VXJ_PRIM = "VxjPrim";
	public static final String CTE_VXJ_GTO = "VxjGto";
	public static final String CTE_VXJ_INV = "VxjInv";
	public static final String CTE_VXJ_FALL = "VxjFall";
	public static final String CTE_VXJ_VIDA = "VxjVida";
	public static final String CTE_VXJ_RTE = "VxjRte";
	public static final String CTE_VXJ_COM = "VxjCom";
	public static final String CTE_VAR_INV_PROB = "varInvProb";
	public static final String CTE_VAR_ACT_FIN = "varActFin";
	public static final String CTE_IMP_FLUJ_ACT_FALL = "impFlujActFall";
	public static final String CTE_IMP_FLUJ_ACT_GTO = "impFlujActGto";
	public static final String CTE_IMP_FLUJ_ACT_RESC = "impFlujActResc";
	public static final String CTE_IMP_FLUJ_ACT_COMPL = "impFlujActCompl";
	public static final String CTE_IMP_FLUJ_ACT_PRIM = "impFlujActPrim";
	public static final String CTE_IMP_FLUJ_ACT_COM = "impFlujActCom";
	public static final String CTE_IMP_FLUJ_ACT_VIDA = "impFlujActVida";
	public static final String CTE_VAR_VAL_ESTRES = "varValEstres";
	public static final String CTE_VAR_VAL_POLIZA_ANO1 = "varPolizasAno1";
	public static final String CTE_VAR_VAL_TAB_INV_SCR = "varValoresTabInvSCR";
	public static final String CTE_VAR_CSP238 = "varCsp238";
	public static final String CTE_VAR_CSP238N1 = "varCsp238N1";
	public static final String VAR_TAB_ASEG_X = "varTabAsegX";
	public static final String VAR_TAB_ASEG_X_2 = "varTabAsegX2";
	public static final String VAR_FEC_J_1 = "varFecJ1";
	public static final String CTE_VAR_UMIC_COPIA = "varUmicCopia";
	public static final String CTE_VAR_BTC_UMIC_COPIA = "varbtcUmicCopia";
	public static final String CTE_VAR_UMIC_COPIA2 = "varUmicCopia2";
	public static final String CTE_VAR_BTC_UMIC_COPIA2 = "varbtcUmicCopia2";
	public static final String CTE_VAR_IND_TABLA_ORIGEN = "varindTablaOrigen";
	public static final String CTE_VAR_IND_TABLA_DESTINO = "varindTablaDestino";
	/** Cte para comprobar si una cadena de texto corresponde a Vida / No vida */
	public static final String CTE_CNEGOCIO = "cnegocio";
	public static final String CTE_VIDA = "vida";
	public static final String CTE_VIDA_V = "v";
	public static final String CTE_VIDA_VI = "vi";
	public static final String CTE_NO_VIDA = "no vida";
	public static final String CTE_NO_VIDA_N = "n";
	public static final String CTE_NO_VIDA_NV = "nv";
	public static final String CTE_VAR_MUS ="MUS";
	public static final String CTE_VAR_TAB923 ="varTab923";
	public static final String CTE_VAR_LIZC = "varLizc";
	public static final String CTE_VAR_KCURVA =  "varKCurva";
	public static final String CTE_VAR_ESTRESES = "varValoresEstreses";
										   
	
	/**
	 * Cte para recuperar variables de apoyo
	 */
	public static final String CTE_VA_FECTRANSICION = "FECTRANSICION";
	public static final String CTE_VA_PROVCSM = "PROVCSM";
	public static final String CTE_VA_MES_CIER = "MESES_CIERRE_NIIF17";
	public static final String CTE_VA_CRIT_FEC = "ID-TEMPORAL";
	public static final String CTE_VA_CRIT_EDA = "ID-CRITERIO";
	public static final String CTE_VA_CRIT_INT = "CRITER_INTER";
	public static final String CTE_VA_MESCAREN = "MESCAREN";
	public static final String CTE_VA_INTER = "CRITER_INTER";
	public static final String CTE_VA_IFAL = "IFAL";
	public static final String CTE_VAR_BLOQUE = "VARBLOQUE";
	public static final String CTE_VA_IPC = "IPC";
	public static final String CTE_VA_PAS = "PAS";
	public static final String CTE_VA_FUT = "FUT";
	public static final String CTE_E1PSJ = "E1PSJ";											
	public static final String CTE_VZC2 = "VZC2";	
	public static final String CTE_TABLA2000 = "TABLA2000";	
	public static final String CTE_E3SAL = "E3SAL";
	public static final String CTE_C2S = "C2S";		
	public static final String CTE_ANOESP = "ANOESP";	
	public static final String CTE_PORVIUSS = "PORVIUSS";	
	public static final String CTE_PRPSS = "PRPSS";		
	public static final String CTE_PRPSS_2001 = "PRPSS_2001";	
	public static final String CTE_PRPSS_2002 = "PRPSS_2002";	
	public static final String CTE_PRPSS_REST = "PRPSS_REST";	
	public static final String CTE_E1PSV = "E1PSV";	
	public static final String CTE_EGARB = "EGARB";
	public static final String CTE_E2PST = "E2PST";
	public static final String CTE_LIMITEVIU = "LIMITEVIU";
	public static final String CTE_PORORFSS = "PORORFSS";
	public static final String CTE_VZC2H = "VZC2H";
	public static final String CTE_VZC2M = "VZC2M";
	public static final String CTE_PORREDUC = "PORREDUC";
	public static final String CTE_MARCA_SW = "MARCASW";
	public static final String CTE_PCTREVRENT = "PCTREVRENT";
	
	public static final String CTE_VA_IANT = "IANT";
	public static final String CTE_VA_ALFA = "IANT";
	public static final String CTE_VA_NPP = "NPP";
	public static final String CTE_VA_GZC002 = "GZC002";
	public static final String CTE_VA_MODBETA = "MODBETA";
	public static final String CTE_VA_MOD_BETA = "MOD_BETA";
	public static final String CTE_VA_TIPO_ALFA = "TIPO_ALFA";
	public static final String CTE_VA_FIN_RVC = "FIN_RVC";
	public static final String CTE_VA_UMIC_COPIA = "UMIC_COPIA";
	public static final String CTE_VA_UMIC_COPIA2 = "UMIC_COPIA2";
	public static final String CTE_VA_BTCUMIC_COPIA = "BTCUMIC_COPIA";
	public static final String CTE_VA_BTCUMIC_COPIA2 = "BTCUMIC_COPIA2";
	public static final String CTE_VA_VARINDTABLA_ORIGEN = "VARINDTABLA_ORIGEN";
	public static final String CTE_VA_VARINDTABLA_DESTINO = "VARINDTABLA_DESTINO";
	public static final String CTE_NRTA = "NRTA";
	public static final String CTE_BETARENTAS = "BETARENTAS";
	public static final String CTE_BETAIDMS = "BETAIDMS";
	public static final String CTE_ICAPACT = "ICAPACT";
	public static final String CTE_VACF = "VACF";
	public static final String CTE_LIM_TAR = "LIM-TAR";
	public static final String CTE_VAR_GEDADMAX = "GEDEADMAX";  
	public static final String CTE_VAR_P3TOP = "P3TOP";
	public static final String CTE_VAR_P2VIV = "P2VIV";
	public static final String CTE_VAR_P1JUB = "P1JUB";
	public static final String CTE_VAR_C1FDI = "C1FDI";
	public static final String CTE_VAR_C1BBC = "C1BCC";
	public static final String CTE_VAR_G1CAP = "G1CAP";
	public static final String CTE_VAR_F1EXP = "F1EXP";
	public static final String CTE_VAR_SWC1S = "SWC1S";
	public static final String CTE_VAR_E1PSJ = "E1PSJ";
	public static final String CTE_VAR_I1PRI = "I1PRI";
	public static final String CTE_MESES_CIERRE_NIIF17 = "MESCIERN17";
	public static final String CTE_VA_BETA_IDMS = "BETAIDMS";												
	public static final String CTE_PROVCSM = "PROVCSM";
	public static final String CTE_FEC_TRANSICION = "FEC_TRANSICION";
	public static final String CTE_SPCOMP = "SPCOMP";
	public static final String CTE_SEX_ASEG1 = "sex_aseg1";
	public static final String CTE_EDAD_ASEG1 = "edad_aseg1";
	public static final String CTE_FNAC_ASEG1 = "fnac_aseg1";
	public static final String CTE_TABLACALC_ASEG1 = "tablacalc_aseg1";
	public static final String CTE_TABLACONV_ASEG1_POS0_TABLAINI = "tablaconv_aseg1_pos0_tablaini";
	public static final String CTE_TABLACONV_ASEG1_POS1_TABLAINI = "tablaconv_aseg1_pos1_tablaini";
	public static final String CTE_TABLACONV_ASEG1_POS2_TABLAINI = "tablaconv_aseg1_pos2_tablaini";
	public static final String CTE_TABLACONV_ASEG2_POS0_TABLAINI = "tablaconv_aseg2_pos0_tablaini";
	public static final String CTE_TABLACONV_ASEG2_POS1_TABLAINI = "tablaconv_aseg2_pos1_tablaini";
	public static final String CTE_TABLACONV_ASEG2_POS2_TABLAINI = "tablaconv_aseg2_pos2_tablaini";
	public static final String CTE_TABLACONV_ASEG3_POS0_TABLAINI = "tablaconv_aseg3_pos0_tablaini";
	public static final String CTE_TABLACONV_ASEG3_POS1_TABLAINI = "tablaconv_aseg3_pos1_tablaini";
	public static final String CTE_TABLACONV_ASEG3_POS2_TABLAINI = "tablaconv_aseg3_pos2_tablaini";
	public static final String CTE_TABLACONV_ASEG4_POS0_TABLAINI = "tablaconv_aseg4_pos0_tablaini";
	public static final String CTE_TABLACONV_ASEG4_POS1_TABLAINI = "tablaconv_aseg4_pos1_tablaini";
	public static final String CTE_TABLACONV_ASEG4_POS2_TABLAINI = "tablaconv_aseg4_pos2_tablaini";
	public static final String CTE_TABLACONV_ASEG5_POS0_TABLAINI = "tablaconv_aseg5_pos0_tablaini";
	public static final String CTE_TABLACONV_ASEG5_POS1_TABLAINI = "tablaconv_aseg5_pos1_tablaini";
	public static final String CTE_TABLACONV_ASEG5_POS2_TABLAINI = "tablaconv_aseg5_pos2_tablaini";
	//Nueva Corriente
	public static final String CTE_VAR_PORC_P = "PORC_P";
	public static final String CTE_VAR_PORC_G = "PORC_G";
	
	public static final String CTE_VAR_NPA = "NPA";
	
	public static final String CTE_VAR_IPC = "IPC";
	
	public static final String CTE_VAR_OGA = "OGA";
	
	/**
	 * Código validacion criterios pago devengo
	 */
	public static final String CTE_VAL_INIP = "INIP";
	public static final String CTE_VAL_MITAD = "MITAD";
	public static final String CTE_VAL_FINP = "FINP";
	public static final String CTE_VAL_FPAGO = "FPAGO";
	public static final String CTE_VAL_PLANI = "PLANI";
	public static final String CTE_VAL_RENOV = "RENOV";
	public static final String CTE_VAL_FINIR = "FINIR";
	public static final String CTE_VAL_MITIR = "MITIR";
	public static final String CTE_VAL_EFTEC = "EFTEC";
	public static final String CTE_VAL_IIDIR = "IIDIR";
	public static final String CTE_VAL_FIDIR = "FIDIR";
	public static final String CTE_VAL_INITA = "INITA";
	public static final String CTE_VAL_MPRI = "MPRI";
	public static final String CTE_VAL_PLAIN = "PLAIN";
	public static final String CTE_VAL_EFTIN = "EFTIN";
	public static final String CTE_VAL_BENEF = "BENEF";
	public static final String CTE_VAL_VIBMV = "VIBMV";
	public static final String CTE_VAL_PLNTP = "PLNTP";
	public static final String CTE_VAL_PLANC = "PLANC";
	public static final String CTE_VAL_RENO2 = "RENO2";
	public static final String CTE_VAL_FIJUB = "FIJUB";
	public static final String CTE_VAL_MIJUB = "MIJUB";
	public static final String CTE_VAL_INIPG = "INIPG";
	
	/**
	 * Cte tipo de tablas de experiencia: realista (R), tradicional (T).
	 */
	public static final char CTE_TABLA_REALISTA = 'R';
	public static final char CTE_TABLA_TRADICIONAL = 'T';
		
	public static final String FLUJO_PROVI = "FLUJO_PROVI";
	public static final String FECHA_PAGO_FEV = "FECPAG_FECDEV";
	
	/** Constantes nombre atributo de entrada */
	public static final String CTE_COD_BT = "codigoBaseTecnica";
	public static final String CTE_CRI_PER = "criterioPeriodos";
	public static final String CTE_TIPO_PRES = "tipoPrestacion";
	public static final String CTE_CUADRO_AMORT = "cuadroAmort";												 
	public static final String CTE_VENCI_PER = "vencimientoPeriodo";
	public static final String CTE_PERIODICIDAD = "periodicidad";
	public static final String CTE_FEC_FIN_PROY = "fecFinProy";
	public static final String CTE_FEC_EFEC_FIN = "fecEfecFin";
	public static final String CTE_UMIC_RENTA = "umicRenta";
	public static final String CTE_CFOR_REV_REN = "cFormaRevRenta";
	public static final String CTE_CPAG_RENTA = "cpagRenta";
	public static final String CTE_CTIPO_REV_REN = "cTipoRevRenta";
	public static final String CTE_BTC = "detalleBaseTecnica";
	public static final String CTE_FICHA_PERIODO = "fichaPeriodo";
	public static final String CTE_CLAVE_UMIC = "claveUmic";
	public static final String CTE_FCALC = "fcalc";
	public static final String CTE_PERIODO = "periodo";
	public static final String CTE_PERIODO_PROYECCION = "periodoProyeccion";
	public static final String CTE_DET_CORRIENTE = "detalleCorriente";
	public static final String CTE_UMIC = "umic";
	public static final String CTE_UMIC_2 = "umic_2";
	public static final String CTE_UMIC_3 = "umic_3";
	public static final String CTE_UMIC2_FALL = "umic_2_fall";
	public static final String CTE_PROY_UMIC = "proyUmic";
	public static final String CTE_FICHA_PROCESO = "fichaProceso";
	public static final String CTE_PERIODOS = "periodos";
	public static final String CTE_CONFIG_VIDA = "configProyVida";
	public static final String CTE_CRIT_FEC_PAGO = "criterioFechaPago";
	public static final String CTE_CRIT_FEC_DEVE = "criterioFechaDevengo";
	public static final String CTE_PER_ACTUAL = "periodoActual";
	public static final String CTE_CONF_MOD_NOM = "confProyModNom";
	public static final String CTE_SUBPROCESO_ACT = "subProcesoActual";
	public static final String CTE_ITERACION = "iteracion";
	public static final String CTE_COD_SUBPROCESO = "codSubproceso";
	public static final String CTE_BTC_UMIC = "btcUmic";
	public static final String CTE_BTC_UMIC_2 = "btcUmic2";
	public static final String CTE_BTC_UMIC_3 = "btcUmic3";
	public static final String CTE_BTC_UMIC_2_FALL = "btcUmic2_fall";
	public static final String CTE_VAR_TC_ANT = "varTcAnt";
	public static final String CTE_VAR_PNATC_ANT = "varPnaTcAnt";
	public static final String CTE_VAR_PPT = "varPPT";
	public static final String CTE_VAL_PLANIB = "PLANIB";
	public static final String CTE_VAL_PLANB = "PLANB";
	public static final String CTE_VAL_PVIDA = "PVIDA";
	public static final String CTE_VAR_PCTREVRENT = "PCTREVRENT";
	public static final String CTE_FEC_DIFERIMIENTO = "fecDiferimiento";


	//Detalle proyVida
	public static final String CTE_DET_COR_ACT = "detalleActual"; 
	public static final String CTE_FEC_CIERRE = "fecCierre";
	
	public static final String CTE_VAL_DEV_INI = "01";
	
	public static final String CTE_VAL_DEV_MIT = "02";
	
	public static final String CTE_VAL_DEV_FIN = "03";
	
	public static final String CTE_VAL_DEV_FEC = "04";
	
	public static final String CTE_VAL_DEV_PAP = "05";
	
	public static final String CTE_VAL_DEV_EFR = "06";
	
	public static final String CTE_VAL_PER_CUATRI = "4";
	
	//ACTNIIF17
		public static final String CTE_COD_ERROR_NO_BT_NIIIF17 = "10";
		public static final String CTE_COD_ERROR_BT_VALORES = "11";
		public static final String CTE_COD_ERROR_NO_BT_VALORES = "12";
		public static final String CTE_DESC_ERROR_NO_BT_NIIF17 = "Base Técnica incorrecta, la bt no es NIIF17";
		public static final String CTE_DESC_ERROR_BT_VALORES = "Mas de un valor encontrado para la tabla CTU0";
		public static final String CTE_DESC_ERROR_NO_BT_VALORES = "No se han encontrado valores para la tabla CTU0";
		public static final String CTE_DESL_ERROR_BT_NO_NIIF17 = "La base tecnica en ejecucion no pertence a las bases tecnicas NIIF17";
		public static final String CTE_DESL_ERROR_BT_VALORES = "Se ha encoentrado mas de un valor en la tabla CTU0 para la busqueda realizada";
		public static final String CTE_DESL_ERROR_NO_BT_VALORES = "No se han encontrado valores en la tabla CTU0 para la busqueda realizada";

	
	/**
	 * Validaciones base técnica
	 */
	public static final Map<String, String> TIPO_BASE_TECNICA = new HashMap<String, String>();
	static {
		TIPO_BASE_TECNICA.put(CTE_VAL_BTI, CTE_VAL_BTI);
		TIPO_BASE_TECNICA.put(CTE_VAL_BASE_BEL, CTE_VAL_BASE_BEL);
		TIPO_BASE_TECNICA.put(CTE_VAL_BASE_BEL, CTE_VAL_BASE_BEL);
	}
	
	/**
	 * Validaciones atributo cFormaRevRenta
	 */
	public static final Map<String, String> TIPO_CFORMAREVREN = new HashMap<String, String>();
	static {
		TIPO_CFORMAREVREN.put(CTE_VAL_REV_ARI, CTE_VAL_REV_ARI);
		TIPO_CFORMAREVREN.put(CTE_VAL_REV_GEO, CTE_VAL_REV_GEO);
		TIPO_CFORMAREVREN.put(CTE_VAL_REV_NEG, CTE_VAL_REV_NEG);
		TIPO_CFORMAREVREN.put(CTE_VAL_REV_CON, CTE_VAL_REV_CON);
	}
	
	/**
	 * Validaciones atributo cPagRenta
	 */
	public static final Map<String, String> TIPO_CPAG_RENTA = new HashMap<String, String>();
	static {
		TIPO_CPAG_RENTA.put(CTE_VAL_PER_ANUAL, CTE_VAL_PER_ANUAL);
		TIPO_CPAG_RENTA.put(CTE_VAL_PER_SEMES, CTE_VAL_PER_SEMES);
		TIPO_CPAG_RENTA.put(CTE_VAL_PER_TRIME, CTE_VAL_PER_TRIME);
		TIPO_CPAG_RENTA.put(CTE_VAL_PER_MEN, CTE_VAL_PER_MEN);
		TIPO_CPAG_RENTA.put(CTE_VAL_PER_CUATRI, CTE_VAL_PER_CUATRI);
		TIPO_CPAG_RENTA.put(CTE_CFORMPAG_UNICA, CTE_CFORMPAG_UNICA);
	}
	
	/**
	 * Validaciones periodicidad
	 */
	public static final Map<String, String> TIPO_PERIODICIAD = new HashMap<String, String>();
	static {
		TIPO_PERIODICIAD.put(CTE_VAL_MENSU, CTE_VAL_MENSU);
		TIPO_PERIODICIAD.put(CTE_VAL_RENO, CTE_VAL_RENO);
		TIPO_PERIODICIAD.put(CTE_VAL_MNAT, CTE_VAL_MNAT);
		TIPO_PERIODICIAD.put(CTE_VAL_REVEN, CTE_VAL_REVEN);
	}
	
	/**
	 * Validaciones vencimiento periodos
	 */
	public static final Map<String, String> TIPO_VENC_PERIOD = new HashMap<String, String>();
	static {
		TIPO_VENC_PERIOD.put(CTE_VAL_VENCI, CTE_VAL_VENCI);
		TIPO_VENC_PERIOD.put(CTE_VAL_VITAL, CTE_VAL_VITAL);
		TIPO_VENC_PERIOD.put(CTE_VAL_TAR, CTE_VAL_TAR);
		TIPO_VENC_PERIOD.put(CTE_VAL_BENEF, CTE_VAL_BENEF);
		TIPO_VENC_PERIOD.put(CTE_VAL_VIBMV, CTE_VAL_VIBMV);
	}
	
	/**
	 * Validaciones flujo nominal fallecimiento
	 */
	public static final Map<String, String> TIPO_FN_FALLEC = new HashMap<String, String>();
	static {
		TIPO_FN_FALLEC.put(ConstantsFactorias.MODULO_CSP064, ConstantsFactorias.MODULO_CSP064);
		TIPO_FN_FALLEC.put(ConstantsFactorias.MODULO_CSP051, ConstantsFactorias.MODULO_CSP051);
		TIPO_FN_FALLEC.put(ConstantsFactorias.MODULO_CSP037, ConstantsFactorias.MODULO_CSP037);
		TIPO_FN_FALLEC.put(ConstantsFactorias.MODULO_CSP362, ConstantsFactorias.MODULO_CSP362);
		TIPO_FN_FALLEC.put(ConstantsFactorias.MODULO_HAN_BTI, ConstantsFactorias.MODULO_HAN_BTI);
	}
	
	/**
	 * Validaciones flujo nominal gastos
	 */
	public static final Map<String, String> TIPO_FN_GASTOS = new HashMap<String, String>();
	static {
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC002, ConstantsFactorias.MODULO_GZC002);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC001, ConstantsFactorias.MODULO_GZC001);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZCRTA, ConstantsFactorias.MODULO_GZCRTA);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC004, ConstantsFactorias.MODULO_GZC004);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC008, ConstantsFactorias.MODULO_GZC008);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC005, ConstantsFactorias.MODULO_GZC005);
		TIPO_FN_GASTOS.put(ConstantsFactorias.MODULO_GZC010, ConstantsFactorias.MODULO_GZC010);
	}
	
	/**
	 * validaciones flujo nominal vida
	 */
	public static final Map<String, String> TIPO_FN_VIDA = new HashMap<String, String>();
	static {
		TIPO_FN_VIDA.put(ConstantsFactorias.MODULO_CSP003, ConstantsFactorias.MODULO_CSP003);
		TIPO_FN_VIDA.put(ConstantsFactorias.MODULO_CSP001, ConstantsFactorias.MODULO_CSP001);
		TIPO_FN_VIDA.put(ConstantsFactorias.MODULO_CSP071, ConstantsFactorias.MODULO_CSP071);
		TIPO_FN_VIDA.put(ConstantsFactorias.MODULO_HAN_BTI, ConstantsFactorias.MODULO_HAN_BTI);
		TIPO_FN_VIDA.put(ConstantsFactorias.MODULO_CSP238, ConstantsFactorias.MODULO_CSP238);
	}
	
	/**
	 * Validaciones Fecha de pago y Fecha devengo
	 */
	public static final Map<String, String> TIPO_FEC_PAG_DEV = new HashMap<String, String>();
	static {
		TIPO_FEC_PAG_DEV.put(CTE_VAL_INIP, CTE_VAL_INIP);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_MITAD, CTE_VAL_MITAD);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_FINP, CTE_VAL_FINP);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_FPAGO, CTE_VAL_FPAGO);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PLANI, CTE_VAL_PLANI);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_RENOV, CTE_VAL_RENOV);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_MITIR, CTE_VAL_MITIR);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_FINIR, CTE_VAL_FINIR); 
		TIPO_FEC_PAG_DEV.put(CTE_VAL_EFTEC, CTE_VAL_EFTEC);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_IIDIR, CTE_VAL_IIDIR);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_FIDIR, CTE_VAL_FIDIR);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_INITA, CTE_VAL_INITA);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_MPRI, CTE_VAL_MPRI);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PLAIN, CTE_VAL_PLAIN);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_EFTIN, CTE_VAL_EFTIN);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PVIDA, CTE_VAL_PVIDA);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PLANB, CTE_VAL_PLANB);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PLNTP, CTE_VAL_PLNTP);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_PLANC, CTE_VAL_PLANC);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_RENO2, CTE_VAL_RENO2);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_FIJUB, CTE_VAL_FIJUB);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_MIJUB, CTE_VAL_MIJUB);
		TIPO_FEC_PAG_DEV.put(CTE_VAL_INIPG, CTE_VAL_INIPG);
	}
	
	/**
	 * Cte para la base de calculo de los módulos de comisiones
	 */
	public static final String CTE_COMI_PR = "PR";
	public static final String CTE_COMI_PV = "PV";
	
	/**
	 * Cte para rescates
	 */
	public static final String CTE_CODK_KT = "KT";
	public static final String CTE_CODK_KC = "KC";
	public static final String CTE_CODK_KF = "KF";
	public static final String CTE_CODK_KM = "KM";
	public static final String CTE_CODK_KN = "KN";
	public static final String CTE_CODK_KS = "KS";
	
	/**
	 * Cte de rescaste
	 */
	public static final String CTE_RESCATE_VARK1 = "codk1";
	public static final String CTE_RESCATE_VARK2 = "codk2";
	public static final String CTE_RESCATE_VARK3 = "codk3";
	
	public static final String CTE_RESCATE = "CTE_RTE";
	public static final String CTES_RESCATE = "CTES_RTE";
	
	/**
	 * Codigos tipo corriente
	 */
	public static final String CTE_CORRIE_VIDA = "01";
	public static final String CTE_CORRIE_FALL = "02";
	public static final String CTE_CORRIE_PRIM = "03";
	public static final String CTE_CORRIE_GAST = "04";
	public static final String CTE_CORRIE_RESC = "05";
	public static final String CTE_CORRIE_COMI = "06";
	public static final String CTE_CORRIE_PRMA = "07";
	public static final String CTE_CORRIE_FORC = "08";
	
	/**
	 * Cte para modulo csp362
	 */
	public static final String CTE_CRP = "CRP";
	public static final String CTE_CRPI = "CRPI";
	public static final String CTE_SR = "SR";
	
	/** Códigos de funciones auxiliares a utilizar en los módulos */
	public static final String CTE_VTX001 = "VTX001";
	public static final String CTE_VTX002 = "VTX002";
	public static final String CTE_VTX003 = "VTX003";
	public static final String CTE_VTX004 = "VTX004";
	public static final String CTE_VTX005 = "VTX005";
	public static final String CTE_VTX009 = "VTX009";
	public static final String CTE_VTXVEPU = "VTXVEPU";
	public static final String CTE_VTXTPFPU = "VTXTFPU";
	
	
	/** Constantes para definir el sexo del asegurado */
	public static final String CTE_SX_H = "H";
	public static final String CTE_SX_M = "M";
	
	/** Constantes para definir el  asegurado */
	public static final String CTE_ASEG_1 = "ASEG1";
	public static final String CTE_ASEG_2 = "ASEG2";
	public static final String CTE_ASEG_3 = "ASEG3";
	public static final String CTE_ASEG_4 = "ASEG4";
	public static final String CTE_ASEG_5 = "ASEG5";
	
	/** Constante para definir el año minimo registrado en las tablas de mortalidad */
	public static final int CTE_ANNO_MIN = 1880;

	
	/**
	 * Códigos proyecciones
	 */
	public static final String CTE_PROY_VIDA = "PROY_VIDA";
	public static final String CTE_PROY_FALL = "PROY_FALL";
	public static final String CTE_PROY_COMP = "PROY_COMP";
	public static final String CTE_PROY_PRIMA = "PROY_PRIMA";
	public static final String CTE_PROY_GTOS = "PROY_GTOS";
	public static final String CTE_PROY_COMI = "PROY_COMI";
	public static final String CTE_PROY_RESC = "PROY_RESC";
	public static final String CTE_PROY_PRV = "PROY_PRV";
	public static final String CTE_PROY_GTOAD = "PROY_GTOAD";
	
	/** Códigos de modalidad */
	public static final String CTE_MODA_01_NOMI = "NOMI";
	public static final String CTE_MODA_02_PROB = "PROB";
	public static final String CTE_MODA_03_PRNA = "PRNA";
	public static final String CTE_MODA_04_ACTU = "ACTU";
	public static final String CTE_MODA_05_PRVI = "PRVI";
	
	/** Códigos de modalidad */
	public static final Integer MOD_186 = 186;
	public static final Integer MOD_209 = 209;
	public static final Integer MOD_228 = 228;
	public static final Integer MOD_238 = 238;
	public static final Integer MOD_362 = 362;
	public static final Integer MOD_546 = 546;
	public static final Integer MOD_821 = 821;
	public static final Integer MOD_852 = 852;
	public static final Integer MOD_860 = 860;
	public static final Integer MOD_868 = 868;
	// Fase II
	public static final Integer MOD_141 = 141;
	public static final Integer MOD_147 = 147;
	public static final Integer MOD_155 = 155;
	public static final Integer MOD_158 = 158;
	public static final Integer MOD_350 = 350;
	public static final Integer MOD_372 = 372;
	public static final Integer MOD_373 = 373;
	public static final Integer MOD_374 = 374;
	public static final Integer MOD_375 = 375;
	public static final Integer MOD_376 = 376;
	public static final Integer MOD_377 = 377;
	public static final Integer MOD_378 = 378;
	public static final Integer MOD_379 = 379;
	public static final Integer MOD_380 = 380;
	public static final Integer MOD_221 = 221;
	public static final Integer MOD_222 = 222;
	public static final Integer MOD_224 = 224;
	public static final Integer MOD_330 = 330;
	public static final Integer MOD_342 = 342;
	public static final Integer MOD_400 = 400;
	public static final Integer MOD_841 = 841;
	public static final Integer MOD_855 = 855;
	public static final Integer MOD_858 = 858;
	public static final Integer MOD_861 = 861;
	public static final Integer MOD_863 = 863;
	public static final Integer MOD_363 = 363;
	public static final Integer MOD_302 = 302;
	public static final Integer MOD_333 = 333;
	
	/** Modalidades gemelas */
	public static final Integer FAMILIA_209 [] = { ConstantsModulos.MOD_209, ConstantsModulos.MOD_186,  ConstantsModulos.MOD_141 , ConstantsModulos.MOD_147 , ConstantsModulos.MOD_155 , 
		ConstantsModulos.MOD_158, ConstantsModulos.MOD_350 , ConstantsModulos.MOD_372 , ConstantsModulos.MOD_373, ConstantsModulos.MOD_374, ConstantsModulos.MOD_375,  ConstantsModulos.MOD_376,
		ConstantsModulos.MOD_376 , ConstantsModulos.MOD_377 , ConstantsModulos.MOD_378 , ConstantsModulos.MOD_379 , ConstantsModulos.MOD_380};
	public static final Integer FAMILIA_362 [] = { ConstantsModulos.MOD_362, ConstantsModulos.MOD_228, ConstantsModulos.MOD_238, ConstantsModulos.MOD_546, ConstantsModulos.MOD_868, 
		ConstantsModulos.MOD_221 , ConstantsModulos.MOD_222 , ConstantsModulos.MOD_224 , ConstantsModulos.MOD_330 , ConstantsModulos.MOD_342 , ConstantsModulos.MOD_400 , ConstantsModulos.MOD_841, ConstantsModulos.MOD_363};	
	public static final Integer FAMILIA_852 [] = { ConstantsModulos.MOD_852, ConstantsModulos.MOD_821, ConstantsModulos.MOD_860 ,
		ConstantsModulos.MOD_855, ConstantsModulos.MOD_858, ConstantsModulos.MOD_861, ConstantsModulos.MOD_863};
	
	
	/** Códigos para establecimiento de tablaXasegY en la conversión de base tecnica */
	public static final String TABLA_ASEG_00721 = "00721";
	public static final String TABLA_ASEG_00740 = "00740";
	public static final String TABLA_ASEG_00741 = "00741";
	public static final String TABLA_ASEG_00039 = "00039";
		public static final String TABLA_ASEG_VZC2_740 = "740";
	public static final String TABLA_ASEG_VZC2_741 = "741";
	
	/** Códigos tipos de elementos */
	public static final String CTE_TIPO_ELEM_01 = "01";
	public static final String CTE_TIPO_ELEM_02 = "02";
	public static final String CTE_TIPO_ELEM_03 = "03";
	public static final String CTE_TIPO_ELEM_04 = "04";
	public static final String CTE_TIPO_ELEM_05 = "05";
	
	/** Códigos de clave de negocio */
	public static final String CTE_NEGOCIO_COLECTIVO = "C";
	public static final String CTE_NEGOCIO_INDIVIDUAL = "I";
	
	/**
	 * Constantes con los meses 4, 6, 9 y 11
	 */
	public static final Set<Integer> MESES_CAL_PER_RENOVA = new HashSet<Integer>();
	static {
		MESES_CAL_PER_RENOVA.add(ConstantsFunciones.CTE_4);
		MESES_CAL_PER_RENOVA.add(ConstantsFunciones.CTE_6);
		MESES_CAL_PER_RENOVA.add(ConstantsFunciones.CTE_9);
		MESES_CAL_PER_RENOVA.add(ConstantsFunciones.CTE_11);
	}
	
	/**
	 * Constantes nombre cache
	 */
	public static final String CACHE_DATOS_CALCULADOS_UMIC = "datos-calculados-umic";
	
	/**
	 * Constantes getters cache
	 */
	
	public static final String GET_BT = "getBt";
	public static final String GET_ITERACION = "getIteracion";
	public static final String GET_BX = "getBx";
	public static final String GET_CLAVEUMIC = "getClaveUmic";
	public static final String GET_KAJUSTE = "getKajuste";
	public static final String GET_KCERTIFICADO = "getKcertificado";
	public static final String GET_KMODALIDAD = "getKmodalidad";
	public static final String GET_KPOLIZA = "getKpoliza";
	public static final String GET_KPRESTACION = "getKprestacion";
	public static final String GET_KSUBPOLIZA = "getKsubpoliza";
	public static final String GET_NSUSCRI = "getNsuscri";

	/**
	 * Tipos de subriesgo
	 */
	public static final Object CTE_RIES_AHOR = "AHOR";
	public static final Object CTE_RIES_FALL = "FALL";
	public static final Object CTE_RIES_LONG = "LONG";
	public static final Object CTE_RIES_INCA = "INCA";
	public static final Object CTE_RIES_FACC = "FACC";
	public static final Object CTE_RIES_OTRO = "OTRO";
	
	/**
	 * Constantes SCR
	 */
	public static final String CTE_SCR_ANPCI = "ANPCI";
	public static final String CTE_SCR_ANPCC = "ANPCC";
	public static final String CTE_SCR_INCA1 = "INCA1";
	
	/**
	 * Curvas tipo
	 */
	public static final String CURVA_CLR000D = "CLR000D";
	public static final String CURVA_CLR000U = "CLR000U";
	public static final String CURVA_CLR0000 = "CLR0000";
	public static final String CURVA_CLR_MA0 = "CLR_MA0";
	public static final String CURVA_CLRMA0U = "CLRMA0U";
	public static final String CURVA_CLRMA0D = "CLRMA0D";
	public static final String CURVA_CLR_VOL = "CLR_VOL";
	public static final String CURVA_CLRVOLU = "CLRVOLU";
	public static final String CURVA_CLRVOLD = "CLRVOLD";
	
}
