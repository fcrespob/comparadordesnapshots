package es.mapfre.coaseguro.tirea.utils;

import java.io.File;
import java.math.BigDecimal;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config-tirea.xml";
	public static final String STREAM_ENTRADA = "REA0";
	public static final String STREAM_SALIDA = "REASAL0";
	public static final String STREAM_INCIDENCIAS = "INC0";
	public static final String STREAM_FICHA = "FIC0";
	
	public static final String STREAM_TOTPMACOA = "TOTPMACOA";
	public static final String STREAM_FLUJPMACOA = "FLUJPMACOA";
	public static final String STREAM_FLUJPMDCOA = "FLUJPMDCOA";
	
	public static final String STREAM_TOTPMACOABTCOA = "TOTPMACOABTCOA";
	public static final String STREAM_TOTPMACOABTCOATF = "TOTPMACOABTCOATF";
	
	public static final String STREAM_FLUJPMACOABTCOA = "FLUJPMACOABTCOA";
	public static final String STREAM_FLUJPMACOABTCOATF = "FLUJPMACOABTCOATF";
	
	public static final String STREAM_DATOSCOA = "DATOSCOA";
	public static final String STREAM_TAB35012 = "TAB35012";
	
	public static final String STREAM_FLUJPMDCOABTCOA = "FLUJPMDCOABTCOA";
	public static final String STREAM_FLUJPMDCOABTCOATF = "FLUJPMDCOABTCOATF";
	public static final String STREAM_TAB35013 = "TAB35013";
	public static final String STREAM_TAB35014 = "TAB35014";
	public static final String STREAM_TAB35015 = "TAB35015";
	public static final String STREAM_DATOSESPECIFIC = "DATOSESPECIFIC";
	public static final String STREAM_PAGOSPLAN = "PAGOSPLAN";

	public static final String CATALOGO_FICHA = "FIC0";
	public static final String TIREA = "TIREA";
	public static final String CATALOGO_TOTPMACOA = "TOTPMACOA";
	public static final String CATALOGO_TOTPMACOABTI = "TOTPMACOABTI";
	public static final String CATALOGO_TOTPMACOABTCOA = "TOTPMACOABTCOA";
	public static final String CATALOGO_TOTPMACOABTCOATF = "TOTPMACOABTCOATF";
	
	public static final String CATALOGO_FLUJPMACOABTI = "FLUJPMACOABTI";
	public static final String CATALOGO_FLUJPMACOABTCOA = "FLUJPMACOABTCOA";
	public static final String CATALOGO_FLUJPMACOABTCOATF = "FLUJPMACOABTCOATF";
	
	public static final String CATALOGO_FLUJPMDCOABTI = "FLUJPMDCOABTI";
	public static final String CATALOGO_FLUJPMDCOABTCOA = "FLUJPMDCOABTCOA";
	public static final String CATALOGO_FLUJPMDCOABTCOATF = "FLUJPMDCOABTCOATF";
	
	public static final String CATALOGO_FLUJPMACOA = "FLUJPMACOA";
	public static final String CATALOGO_FLUJPMDCOA = "FLUJPMDCOA";
	public static final String CATALOGO_DATOSCOA = "DATOSCOA";
	public static final String CATALOGO_TAB35012 = "TAB35012";
	public static final String CATALOGO_TAB35013 = "TAB35013";
	public static final String CATALOGO_TAB35014 = "TAB35014";
	public static final String CATALOGO_TAB35015 = "TAB35015";
	public static final String CATALOGO_DATOSESPECIFIC = "DATOSESPECIFIC";
	public static final String CATALOGO_PAGOSPLAN = "PAGOSPLAN";
	
	public static final String CATALOGO_INC = "TIREA_INC";
	public static final String CATALOGO_LOG = "LOG0";
	public static final String RUTA_BASE = "TIREA" + File.separator + "CIERRES";
	public static final String LOG_FICHAS = "FICHAS" + File.separator + "FICHASPEND" + File.separator + "LOGFICHATIREA.TXT";
	public static final String LOG_FICHAS_RESU = "FICHAS" + File.separator + "FICHASPEND" + File.separator + "RESUMEN_LOGFICHATIREA.TXT";
	public static final String RUTA_FICHAS = "FICHAS" + File.separator + "FICHASPEND"  + File.separator;

	public static final String LOG_REG_GEN = "Numero de registros generados: ";
	public static final String LOG_INCI_DET = "Numero de incidencias detectadas: ";
	public static final String LOG_INFO = "INFO";
	public static final String LOG_ERROR = "ERROR";
	
	/**
	 * Constantes auxiliares
	 */
	public static final String CTE_0_PUNTO_01 = "0.01";
	public static final BigDecimal CTE_OPER_0_PUNTO_01 = new BigDecimal(CTE_0_PUNTO_01);
	public static final BigDecimal BIG_CIEN = new BigDecimal(100);
	
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
	
    // Tipo de negocio
	public static final String INDIVIDUAL = "I";
	public static final String COLECTIVOS = "C";
	
	public static final String RUTA_EXPORTA_XML = "exportXml";
	
	// TEMPVIT
	public static final String T = "RENTA-T";
	public static final String RENTA_T = "Seguros de rentas: Rentas Temporales";
	public static final String V = "RENTA-V";
	public static final String RENTA_V = "Seguros de rentas: Rentas Vitalicias";
	public static final String L = "RENTA-L";
	public static final String RENTA_L = "Seguros de rentas: Rentas Locas";
}
