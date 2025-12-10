package es.mapfre.scr.tablasExperiencia.utils;

import java.math.BigDecimal;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config-scrTExp.xml";
	public static final String CATALOGO_SALIDA = "SAL0";
	public static final String CATALOGO_SALIDA_AUX = "SAL0_AUX";
	public static final String CATALOGO_INCIDENCIAS = "INC0";
	public static final String CATALOGO_LOG = "R340T003";
	public static final String RUTA_BASE = "CATALOGOS";
	public static final String LOG_FICHASMFE = "FICHAS/FICHASPEND/LOGFICHAGENSCRMFE.TXT";
	public static final String LOG_FICHASMMI = "FICHAS/FICHASPEND/LOGFICHAGENSCRMMI.TXT";
	public static final String LOG_FICHASMCF = "FICHAS/FICHASPEND/LOGFICHAGENSCRMCF.TXT";
	public static final String LOG_FICHASMCI = "FICHAS/FICHASPEND/LOGFICHAGENSCRMCI.TXT";
	public static final String LOG_FICHASLFE = "FICHAS/FICHASPEND/LOGFICHAGENSCRLFE.TXT";
	public static final String LOG_FICHASLMI = "FICHAS/FICHASPEND/LOGFICHAGENSCRLMI.TXT";
	public static final String LOG_FICHASMFE_NIIF17 = "FICHAS/FICHASPEND/LOGFICHAGENNF17MFE.TXT";

	public static final String LOG_REG_ELIM = "N�mero de registros eliminados de la tabla TB340VTR0: ";
	public static final String LOG_REG_CONS = "N�mero de registros conservados de la tabla TB340VTR0: ";
	public static final String LOG_REG_GEN = "N�mero de registros generados: ";
	public static final String LOG_REG_TRA = "N�mero de registros tratados de la TB340ATR0: ";
	public static final String LOG_INCI_DET = "N�mero de incidencias detectadas: ";
	public static final String LOG_INI = "Hora Inicio: ";
	public static final String LOG_FIN = "Hora Fin: ";
	
	/**
	 * Bases t�cnicas para SCR
	 */
	public static final String BT_BEL    = "BEL"   ;
	public static final String BT_SCRMFE = "SCRMFE";
	public static final String BT_SCRMMI = "SCRMMI";
	public static final String BT_SCRMCF = "SCRMCF";
	public static final String BT_SCRMCI = "SCRMCI";
	public static final String BT_SCRLFE = "SCRLFE";
	public static final String BT_SCRLMI = "SCRLMI";
	public static final String BT_NF17MFE = "NF17MFE";
	
	/**
	 * Tipos subriesgo
	 */
	public static final String CTE_LONG = "LONG";
	public static final String CTE_AHOR = "AHOR";
	public static final String CTE_FALL = "FALL";
	
	/**
	 * Volumen de datos a cargar en cada bloque
	 */
	public static final int BATCH_SIZE = 1000;
	
	/**
	 * Charset de los ficheros de entrada-salida
	 */
	public static final String FILE_CHARSET = "ISO-8859-1";
	
	/**
	 * Buffer de escritura para BeanIO en gzip
	 */
	public static final int GZIP_BUFFER_SIZE = 8 * 1024;
	
	/**
	 * Constantes para c�lculos
	 */
	public static final BigDecimal CTE_OPER_0_PUNTO_01 = new BigDecimal("0.01");
}
