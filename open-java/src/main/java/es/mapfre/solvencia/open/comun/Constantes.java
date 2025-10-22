

/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */
package es.mapfre.solvencia.open.comun;


/**
 * @author amdepedro
 *
 */
public final class Constantes {
	
	public static final int LONGITUD_PARAMETRO_FECHA_CIERRE = 6;

	public static final String RUTA_CIERRES = "CIERRES";

	public static final String STREAM_FICHAS = "R340T000";
	public static final String STREAM_LOGS = "R340T003";
	public static final String STREAM_FICHAS_PURGADO = "R340T000_PURGADO";
	public static final String STREAM_LOGS_PURGADO = "R340T003_PURGADO";
	public static final String LISTA_FICHEROS_ERRORES = "mensajes.errores";
	public static final String LISTA_FICHEROS_AUXILIARES = "auxiliares";
	public static final String LISTA_FICHEROS_CATALOGOS = "catalogos";
	public static final String STREAM_UMIC = "X880JI01";
	
	public static final String PARAM_RUTA_BASE = "solvencia.ruta.base";
	public static final String PARAM_FECHA_CIERRE = "solvencia.fecha.cierre";
	public static final String PARAM_CODIGO_ERROR = "solvencia.error.codigo";

	
	public static final String RUTA_BASE_DEFECTO = "/mnt/solv2vida";
	public static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	public static final String BEANIO_CONFIG_OUT_XML = "beanio/beanio-config-out.xml";

	public static final String URL_JMX_DEFECTO = "service:jmx:rmi://localhost:3000/jndi/rmi://localhost:9000/server";

	
	public static final String ENTORNO_EJECUCION_DEFECTO = "dev";
	public static final String EXT_PROPERTIES = "properties";
	public static final String SOLVENCIA_OPEN_PROPERTIES = "solvenciaOpen";
	public static final String CARGA_FICHEROS_PROPERTIES = "cargaFicheros.properties";
	public static final String INSTANCIAS_PROPERTIES = "configuracionInstancias.properties";
		
	public static final String NOMBRES_FICHEROS_A_CARGAR = ".*MAESBTC.*|.*FLUJOSTOT.*|.*INCIDENCIAS.*";
	public static final String NOMBRES_FICHEROS_A_COPIAR = ".*FLUJOSDET.*";
	public static final String NOMBRES_FICHEROS_A_CARGAR_Y_COPIAR = ".*MAESBTC.*|.*FLUJOSTOT.*|.*INCIDENCIAS.*|.*FLUJOSDET.*";
	
	/*INI-TAR00302248*/
  /*public static final String NOMBRES_FICHEROS_ENTREGABLES_A_COPIAR = ".*BASETEC.*|.*PRVUMIC.*|.*PRVBT.*|.*PRVINF1.*|.*PRVINF2.*|.*PRVCR.*|.*FLUJINF1.*|.*FLUJINF2.*|.*PROVCOASEG.*|.*FLUJCOASEG.*|.*PTIPO_AUT.*|.*PRVFPB.*|.*FLUJTCAS.*"; */
	public static final String NOMBRES_FICHEROS_ENTREGABLES_A_COPIAR = ".*BASETEC.*|.*PRVUMIC.*|.*PRVBT.*|.*PRVINF1.*|.*PRVINF2.*|.*PRVCR.*|.*FLUJINF1.*|.*FLUJINF2.*|"
		+ ".*PROVCOASEG.*|.*FLUJCOASEG.*|.*PTIPO_AUT.*|.*PRVFPB.*|.*FLUJTCAS.*|.*CONTAB.*|.*SCRVM.*|.*FLUJOSTOTP.*|.*FLUJOSTN17.*|.*FLUJINF3.*|.*FPSL.*|.*FLUJINF4.*|"
		+ ".*FLUJOTOTPV.*|.*PESOSBT.*|.*PESOSBTPROXY.*|.*PATRONCSM.*|.*SWCOBROCOM.*|.*SWCOBROCOMCSV.*|.*CONTABC.*|.*FLUJINFSCR.*|";
/*FIN-TAR00302248*/
	public static final String EXPRE_FICHEROS_MAESTC = "MAESBTC|FLUJOSTOT|INCIDENCIAS";
	
	public static final String NOMBRES_CARPETAS_NO_CARGAR = ".*FICHAS.*|.*CTEC.*";
	
	public static final String BRUTOSCOL = "BRUTOSCOL";
	public static final String BRUTOSIND = "BRUTOSIND";	
	
	public static final String RESULT_COL = "RESULT_COL";
	public static final String RESULT_IND = "RESULT_IND";	
	
	public static final String PROCEDIMIENTO_CARGAR_TOTALES_FLUJOS = "TOTALES.CARGAR_TOTALES";	
	public static final String PROCEDIMIENTO_CARGAR_DETALLE_BASE_TECNICAS = "TOTALES.CARGAR_DETALLES_BTC";	
	public static final String PROCEDIMIENTO_CARGAR_INCIDENCIAS = "TOTALES.CARGAR_INCIDENCIAS";
	
	public static final String PROCEDIMIENTO_ACTUALIZAR_TOTALES_FLUJOS = "TOTALES.ACTUALIZAR_TOTALES";
	public static final String PROCEDIMIENTO_ACTUALIZAR_DETALLE_BASE_TECNICAS = "TOTALES.ACTUALIZAR_DETALLES_BTC";
	public static final String PROCEDIMIENTO_ACTUALIZAR_INCIDENCIAS = "TOTALES.ACTUALIZAR_INCIDENCIAS";
	
	
	public static final String PROCEDIMIENTO_EXTRAER_TOTALES_FLUJOS = "TOTALES.EXPORTAR_TOTALES";	
	public static final String PROCEDIMIENTO_EXTRAER_DETALLE_BASE_TECNICAS = "TOTALES.EXPORTAR_DETALLES_BTC";
	public static final String PROCEDIMIENTO_EXTRAER_INCIDENCIAS = "TOTALES.EXPORTAR_INCIDENCIAS";
	
	public static final String PROCEDIMIENTO_PURGAR_TABLAS= "TOTALES.PURGAR_CIERRE";	

	public static final String HABILITAR_PARALELISMO = "bbdd.carga.paralelismo.habilitar";
	
	public static final String SI = "S";
	
	public static final String GUION_BAJO = "_";
	
	public static final String FORMATO_SALIDA_FICHERO = "txt";
	
	public static final String PROPERTY_MAXIMO_NUMERO_INSTANCIAS = "escenario.MAXIMO";
	public static final String PROPERTY_NODOS_POR_INSTANCIA = "nodos.coherence.instancia";
	

	public static final String FILE_CHARSET = "ISO-8859-1";
	public static final int LONGITUD_NEW_LINE = 2;
	
	public static final int GZIP_BUFFER_SIZE = 8 * 1024;
	
	public static final long MIN_SIZE_FICHERO_COMPRIMIR = 1024L * 1024L;

	public static final String FORMATO_FICHERO_TXT = ".txt";
	public static final String FORMATO_FICHERO_GZIP = ".gz";
	public static final String FORMATO_FICHERO_ZIP = ".zip";

	/**
	 * Número de hilos en paralelo para compresión y descompresión
	 */
	public static final int PARALLEL_THREADS = 4;

	// Número de meses a purgar
	public static final int MESES_PURGADO = 3;

	private Constantes() {
		
	}
	
}
