package es.mapfre.scr.gastos.utils;

import java.math.BigDecimal;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config-scrgto.xml";
	public static final String CATALOGO_SALIDA_GRE = "SALGRE0";
	public static final String CATALOGO_SALIDA_IPC = "SALIPC0";
	public static final String CATALOGO_SALIDA_AUX = "SAL0_AUX";
	public static final String CATALOGO_SALIDA = "SAL0";
	public static final String CATALOGO_INCIDENCIAS = "INC0";
	public static final String CATALOGO_LOG = "R340T003";
	public static final String RUTA_BASE = "CATALOGOS";
	public static final String LOG_FICHASGRE = "FICHAS/FICHASPEND/LOGFICHAGENSCRGRE.TXT";
	public static final String LOG_FICHASIPC = "FICHAS/FICHASPEND/LOGFICHAGENSCRIPC.TXT";
	public static final String LOG_FICHASGRE_NIIF17 = "FICHAS/FICHASPEND/LOGFICHAGENNF17GRE.TXT";

	public static final String LOG_REG_GEN = "N�mero de registros generados: ";
	public static final String LOG_INCI_DET = "N�mero de incidencias detectadas: ";
	public static final String LOG_INI = "Hora Inicio: ";
	public static final String LOG_FIN = "Hora Fin: ";
	
	/**
	 * Variables de estrés
	 */
	public static final String CTE_IPC = "IPC";
	public static final String CTE_GTO_UMIC = "GTO_UMIC";
	public static final String BASETEC_GTO = "SCRGTO";
	public static final String BASETEC_BEL = "BEL";
	public static final String BASETEC_GTO_NIIF17 = "NF17GTO";
	public static final String CTE_GTO_UMIC_NIIF17 = "GTO_UMIC_NIIF17";
	
	/**
	 * Constantes auxiliares
	 */
	public static final String CTE_0_PUNTO_01 = "0.01";
	public static final BigDecimal CTE_OPER_0_PUNTO_01 = new BigDecimal(CTE_0_PUNTO_01);
	
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
}
