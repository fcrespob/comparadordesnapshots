package es.mapfre.gbt.mensualizadorTasas.utils;

public class ConstantesMensualizador {
	
	public static final String BEANIO_CONFIG_OUT = "beanio/beanio-gbt-config-out.xml";
	public static final String BEANIO_CONFIG = "beanio/beanio-gbt-config.xml";
	public static final String EHCACHE_CONFIG = "ehcacheConfig/ehcache2.xml";
	public static final String CACHE_TASAS = "tasas-mensualizadas";
	public static final String CACHE_R340T000 = "R340T000";
	public static final String LOG_FICHAS = "LOG_FICHAS";
	public static final String CACHE_VTA0 = "VTA0";
	public static final String CACHE_INC0 = "INC0";
	public static final String CACHE_R340T003 = "R340T003";
	
	public static final String LOG_REG_GEN = "Número de registros generados: ";
	public static final String LOG_REG_TRA = "Número de registros tratados de la TB340VTA0: ";
	public static final String LOG_INCI_DET = "Número de incidencias detectadas: ";
	public static final String LOG_INI = "Hora Inicio: ";
	public static final String LOG_FIN = "Hora Fin: ";
	
	public static final int BATCH_SIZE = 1000;
	public static final String FILE_CHARSET = "ISO-8859-1";
	public static final int GZIP_BUFFER_SIZE = 8 * 1024;
	public static final int MESES_DEFECTO =13;
	public static final int ANIOS_DEFECTO =1;

}
