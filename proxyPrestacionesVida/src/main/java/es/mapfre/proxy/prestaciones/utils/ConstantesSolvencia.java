package es.mapfre.proxy.prestaciones.utils;

import java.io.File;
import java.math.BigDecimal;

public class ConstantesSolvencia {
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config-proxy.xml";
	public static final String STREAM_ENTRADA = "REA0";
	public static final String STREAM_SALIDA = "REASAL0";
	public static final String STREAM_INCIDENCIAS = "INC0";
	public static final String STREAM_PESOS_COL = "PESCOL0";
	public static final String STREAM_PESOS_COL_CERT = "PESCOLCERT0";
	public static final String STREAM_PESOS_IND = "PESIND0";
	public static final String STREAM_FICHA = "FIC0";

	public static final String PROXY = "PROXY";
	public static final String CATALOGO_PESOS = "PES0";
	public static final String CATALOGO_PESOS_CERT = "PESCERT0";
	public static final String CATALOGO_FICHA = "FIC0";
	
	public static final String CATALOGO_GESINTRO_BT = "GESINTRO_BT";
	public static final String CATALOGO_GESINTRO = "GESINTRO";
	public static final String CATALOGO_INC_GESINTRO = "INC_GESINTRO";
	public static final String CATALOGO_RTEVTOMAN_BT = "RTEVTOMAN_BT";
	public static final String CATALOGO_RTEVTOMAN = "RTEVTOMAN";
	public static final String CATALOGO_INC_RTEVTOMAN = "INC_RTEVTOMAN";
	public static final String CATALOGO_RTEIND_BT = "RTEIND_BT";
	public static final String CATALOGO_RTEIND = "RTEIND";
	public static final String CATALOGO_INC_RTEIND = "INC_RTEIND";
	public static final String CATALOGO_VTOIND_BT = "VTOIND_BT";
	public static final String CATALOGO_VTOIND = "VTOIND";
	public static final String CATALOGO_INC_VTOIND = "INC_VTOIND";
	public static final String CATALOGO_RTEVTOCOL_BT = "RTEVTOCOL_BT";
	public static final String CATALOGO_RTEVTOCOL = "RTEVTOCOL";
	public static final String CATALOGO_INC_RTEVTOCOL = "INC_RTEVTOCOL";
	public static final String CATALOGO_ANTIND_BT = "ANTIND_BT";
	public static final String CATALOGO_ANTIND = "ANTIND";
	public static final String CATALOGO_INC_ANTIND = "INC_ANTIND";
	public static final String CATALOGO_ASEVAL_BT = "ASEVAL_BT";
	public static final String CATALOGO_ASEVAL = "ASEVAL";
	public static final String CATALOGO_INC_ASEVAL = "INC_ASEVAL";
	public static final String CATALOGO_AS400_BT = "AS400_BT";
	public static final String CATALOGO_AS400 = "AS400";
	public static final String CATALOGO_INC_AS400 = "INC_AS400";
	public static final String CATALOGO_RTANEO_BT = "RTANEO_BT";
	public static final String CATALOGO_RTANEO = "RTANEO";
	public static final String CATALOGO_INC_RTANEO = "INC_RTANEO";
	public static final String CATALOGO_MOVNEO_BT = "MOVNEO_BT";
	public static final String CATALOGO_MOVNEO = "MOVNEO";
	public static final String CATALOGO_INC_MOVNEO = "INC_MOVNEO";
	public static final String CATALOGO_PREONS_BT = "PREONS_BT";
	public static final String CATALOGO_PREONS = "PREONS";
	public static final String CATALOGO_INC_PREONS = "INC_PREONS";
	public static final String CATALOGO_INC = "PROXY_INC";
	public static final String CATALOGO_RTAIND_TRAD_BT = "RTAINDTRAD_BT";
	public static final String CATALOGO_RTAIND_TRAD = "RTAINDTRAD";
	public static final String CATALOGO_INC_RTAIND_TRAD = "INC_RTAINDTRAD";
	public static final String CATALOGO_RTACOL_BT = "RTACOL_BT";
	public static final String CATALOGO_RTACOL = "RTACOL";
	public static final String CATALOGO_INC_RTACOL = "INC_RTACOL";
	
	public static final String CATALOGO_LOG = "LOG0";
	public static final String RUTA_BASE = "PROXYVIDA" + File.separator + "CIERRES" + File.separator;
	public static final String LOG_FICHAS = "FICHAS" + File.separator + "FICHASPEND" + File.separator + "LOGFICHAPROXY.TXT";
	public static final String LOG_FICHAS_RESU = "FICHAS" + File.separator + "FICHASPEND" + File.separator + "RESUMEN_LOGFICHAPROXY.TXT";
	public static final String RUTA_FICHAS = "FICHAS" + File.separator + "FICHASPEND"  + File.separator;

	public static final String LOG_REG_GEN = "Número de registros generados: ";
	public static final String LOG_INCI_DET = "Número de incidencias detectadas: ";
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
	
	// Sistema
	public static final String GESINTRO = "GESINTRO";
	public static final String RTE_VTO_MAN = "RTEVTOMAN";
	public static final String RTE_IND = "RTEIND";
	public static final String VTO_IND = "VTOIND";
	public static final String RTE_VTO_COL = "RTEVTOCOL";
	public static final String ANT_IND = "ANTIND";
	public static final String ASEVAL = "ASEVAL";
	public static final String AS400 = "AS400";
	public static final String RTA_NEO = "RTANEO";
	public static final String MOV_NEO = "MOVNEO";
	public static final String PRE_ONS = "PREONS";
	public static final String RTA_IND_TRAD = "RTAINDTRAD";
	public static final String RTA_COL = "RTACOL";
	
	// Carpetas
	public static final String RUTA_GESINTRO = "SINIESTROS";
	public static final String RUTA_RTE_VTO_MAN = "RTE_VTO_MANUAL";
	public static final String RUTA_RTE_IND = "RTE_IND";
	public static final String RUTA_VTO_IND = "VTO_IND";
	public static final String RUTA_RTE_VTO_COL = "RTE_VTO_COL";
	public static final String RUTA_ANT_IND = "ANT_IND";
	public static final String RUTA_ASEVAL = "ASEVAL";
	public static final String RUTA_AS400 = "AS400";
	public static final String RUTA_RTA_NEO = "RTA_NEO";
	public static final String RUTA_MOV_NEO = "MOV_NEO";
	public static final String RUTA_PRE_ONS = "PRE_ONS";
	public static final String RUTA_RTA_IND_TRAD = "RTA_IND_TRAD";
	public static final String RUTA_RTA_COL = "RTA_COL";
	public static final String RUTA_RAIZ_SSAA = "raizSSAA";
	
	public static final String STR_MOV_MES = "_MOV_MES_"; 
}
