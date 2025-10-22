package es.mapfre.gbt.tablasExperiencia.utils;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-plantillas-tblExp.xml";
	public static final String CATALOGO_SALIDA = "SAL0";
	public static final String CATALOGO_SALIDA_AUX = "SAL_AUX";
	public static final String CATALOGO_INCIDENCIAS = "INC0";
	public static final String CATALOGO_LOG = "R340T003";
	public static final String RUTA_BASE = "CATALOGOS";
	public static final String LOG_FICHAS = "FICHAS/FICHASPEND/LOGFICHAGENTM.TXT";

	public static final String LOG_REG_ELIM = "Número de registros eliminados de la tabla TB340VTR0: ";
	public static final String LOG_REG_CONS = "Número de registros conservados de la tabla TB340VTR0: ";
	public static final String LOG_REG_GEN = "Número de registros generados: ";
	public static final String LOG_REG_TRA = "Número de registros tratados de la TB340ATR0: ";
	public static final String LOG_INCI_DET = "Número de incidencias detectadas: ";
	public static final String LOG_INI = "Hora Inicio: ";
	public static final String LOG_FIN = "Hora Fin: ";
	
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
