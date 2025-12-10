package es.mapfre.scr.tasasAnulacion.utils;

import java.math.BigDecimal;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config-TAnu.xml";
	public static final String CATALOGO_SALIDA = "SAL0";
	public static final String CATALOGO_SALIDA_AUX = "SAL0_AUX";
	public static final String CATALOGO_INCIDENCIAS = "INC0";
	public static final String CATALOGO_LOG = "R340T003";
	public static final String RUTA_BASE = "CATALOGOS";
	public static final String LOG_FICHASAEP = "FICHAS/FICHASPEND/LOGFICHAGENSCRAEP.TXT";
	public static final String LOG_FICHASAEN = "FICHAS/FICHASPEND/LOGFICHAGENSCRAEN.TXT";
	public static final String LOG_FICHASAIP = "FICHAS/FICHASPEND/LOGFICHAGENSCRAIP.TXT";
	public static final String LOG_FICHASAIN = "FICHAS/FICHASPEND/LOGFICHAGENSCRAIN.TXT";
	public static final String LOG_FICHASAEN_NIIF17 = "FICHAS/FICHASPEND/LOGFICHAGENNF17AEN.TXT";

	public static final String LOG_REG_ELIM = "Número de registros eliminados de la tabla TB340VMA0: ";
	public static final String LOG_REG_CONS = "Número de registros conservados de la tabla TB340VMA0: ";
	public static final String LOG_REG_GEN = "Número de registros generados: ";
	public static final String LOG_INCI_DET = "Número de incidencias detectadas: ";
	public static final String LOG_INI = "Hora Inicio: ";
	public static final String LOG_FIN = "Hora Fin: ";
	
	/**
	 * Bases t�cnicas para SCR
	 */
	public static final String BT_BEL    = "BEL"   ;
	public static final String BT_SCRAEP = "SCRAEP";
	public static final String BT_SCRAEN = "SCRAEN";
	public static final String BT_SCRAIP = "SCRAIP";
	public static final String BT_SCRAIN = "SCRAIN";
	public static final String BT_NF17AEN = "NF17AEN";
	
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
	public static final BigDecimal CTE_OPER_0_PUNTO_0000000001 = new BigDecimal("0.0000000001");
}
