package es.mapfre.solvencia.formulacion.util;

import java.math.BigDecimal;


/**
 * Clase que contiene las constantes necesarias para las funciones auxiliares.
 * 
 * @author rschacon
 * 
 */
public final class ConstantsBT {

	private ConstantsBT() {
		
	}
	
	public static final BigDecimal CTE_OPER_2_PUNTO_37 = new BigDecimal(ConstantsBT.CTE_2_PUNTO_37);
	public static final String CTE_2_PUNTO_37 = "2.37";
	public static final String CTE_0_STRING = "0";
	public static final String CTE_CADENA_VACIA = "";
	/** Cte parametros modulo base tecnica */
	public static final Integer PARAM_SIMUL = 2;
	public static final Integer PARAM_UMI_BASE_TEC = 1;
	public static final Integer PARAM_BTC = 0;
	/**
	 * Cte validacion base tenica BTI.
	 */
	public static final String CTE_VAL_BTI = "BTI";
	public static final String CTE_VAL_BTIPROY = "BTIPROY";

	/**
	 * Cte validacion base tenica BEL.
	 */
	public static final String CTE_VAL_BASE_BEL = "BEL";

	/**
	 * Cte validacion base tenica ROSSP.
	 */
	public static final String CTE_VAL_ROSSP = "ROSSP";
	/**
	 * Cte validacion base tenica BTCOA.
	 */
	public static final String CTE_VAL_BTCOA = "BTCOA";
	
	/**
	 * Cte validacion base tenica BELCOA.
	 */
	public static final String CTE_VAL_BELCOA = "BELCOA";
	
	/**
	 * Cte validacion base tenica BELCLR.
	 */
	public static final String CTE_VAL_BELCLR = "BELCLR";
	
	/**
	 * Cte validacion base tenica ROSSEAR.
	 */
	public static final String CTE_VAL_ROSSEAR = "ROSSEAR";
	
	/**
	 * Cte validacion base tenica ROSSEARC.
	 */
	public static final String CTE_VAL_ROSSEARC = "ROSSEARC";
	
	/**
	 * Cte validacion base tenica SCR.
	 */
	public static final String CTE_VAL_SCRTIU = "SCRTIU";
	public static final String CTE_VAL_SCRTID = "SCRTID";
	public static final String CTE_VAL_SCRGTO = "SCRGTO";
	public static final String CTE_VAL_SCRMFE = "SCRMFE";
	public static final String CTE_VAL_SCRMMI = "SCRMMI";
	public static final String CTE_VAL_SCRMCF = "SCRMCF";
	public static final String CTE_VAL_SCRMCI = "SCRMCI";
	public static final String CTE_VAL_SCRLFE = "SCRLFE";
	public static final String CTE_VAL_SCRLMI = "SCRLMI";
	public static final String CTE_VAL_SCRINC = "SCRINC";
	public static final String CTE_VAL_SCRVM  = "SCRVM" ;
	public static final String CTE_VAL_SCRAEP = "SCRAEP";
	public static final String CTE_VAL_SCRAEN = "SCRAEN";
	public static final String CTE_VAL_SCRAIP = "SCRAIP";
	public static final String CTE_VAL_SCRAIN = "SCRAIN";
	public static final String CTE_VAL_SCRANM = "SCRANM";
	
	/** Códigos de modalidad */
	public static final Integer MOD_186 = 186;
	public static final Integer MOD_209 = 209;
	public static final Integer MOD_228 = 228;
	public static final Integer MOD_238 = 238;
	public static final Integer MOD_362 = 362;
	public static final Integer MOD_475 = 475;
	public static final Integer MOD_476 = 476;
	public static final Integer MOD_546 = 546;
	public static final Integer MOD_600 = 600;
	public static final Integer MOD_618 = 618;
	public static final Integer MOD_619 = 619;
	public static final Integer MOD_821 = 821;
	public static final Integer MOD_852 = 852;
	public static final Integer MOD_860 = 860;
	public static final Integer MOD_868 = 868;
	//Fase II
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
	
	/** Modalidades gemelas */
	//public static final Integer FAMILIA_362 [] = { ConstantsBT.MOD_362, ConstantsBT.MOD_228, ConstantsBT.MOD_238, ConstantsBT.MOD_546, ConstantsBT.MOD_868 };
	//public static final Integer FAMILIA_852 [] = { ConstantsBT.MOD_852, ConstantsBT.MOD_821, ConstantsBT.MOD_860, ConstantsBT.MOD_475, ConstantsBT.MOD_476, ConstantsBT.MOD_600, ConstantsBT.MOD_618, ConstantsBT.MOD_619 };
	public static final Integer FAMILIA_209 [] = { ConstantsBT.MOD_209, ConstantsBT.MOD_186,  ConstantsBT.MOD_141, ConstantsBT.MOD_147, ConstantsBT.MOD_155, ConstantsBT.MOD_158, ConstantsBT.MOD_350, ConstantsBT.MOD_372,
		ConstantsBT.MOD_373, ConstantsBT.MOD_374, ConstantsBT.MOD_375, ConstantsBT.MOD_376, ConstantsBT.MOD_377, ConstantsBT.MOD_378, ConstantsBT.MOD_379, ConstantsBT.MOD_380};
	public static final Integer FAMILIA_362 [] = { ConstantsBT.MOD_362, ConstantsBT.MOD_228, ConstantsBT.MOD_238, ConstantsBT.MOD_546, ConstantsBT.MOD_868, 
		ConstantsBT.MOD_221 , ConstantsBT.MOD_222 , ConstantsBT.MOD_224 , ConstantsBT.MOD_330 , ConstantsBT.MOD_342 , ConstantsBT.MOD_400 , ConstantsBT.MOD_841, ConstantsBT.MOD_363};	
	public static final Integer FAMILIA_852 [] = { ConstantsBT.MOD_852, ConstantsBT.MOD_821, ConstantsBT.MOD_860, ConstantsBT.MOD_475, ConstantsBT.MOD_476, ConstantsBT.MOD_600, ConstantsBT.MOD_618, ConstantsBT.MOD_619,
		ConstantsBT.MOD_855, ConstantsBT.MOD_858, ConstantsBT.MOD_861, ConstantsBT.MOD_863};
	
	/** Códigos para establecimiento de tablaXasegY en la conversión de base tecnica */
	public static final String TABLA_ASEG_00721 = "00721";
	public static final String TABLA_ASEG_00740 = "00740";
	public static final String TABLA_ASEG_00741 = "00741";
	public static final String TABLA_ASEG_00039 = "00039";
	
	/**
	 * cte SI.
	 */
	public static final String CTE_S = "S";

	/**
	 * cte NO.
	 */
	public static final String CTE_N = "N";
	
	/** Constantes para definir el sexo del asegurado */
	public static final String CTE_SX_H = "H";
	public static final String CTE_SX_M = "M";
	
	/**
	 * Cte tipo de tablas de experiencia: realista (R), tradicional (T).
	 */
	public static final char CTE_TABLA_REALISTA = 'R';
	public static final char CTE_TABLA_TRADICIONAL = 'T';
}
