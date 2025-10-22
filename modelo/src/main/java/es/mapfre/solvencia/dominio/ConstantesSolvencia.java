/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
 */

package es.mapfre.solvencia.dominio;

import java.util.HashSet;
import java.util.Set;

public final class ConstantesSolvencia {
	
	private ConstantesSolvencia() {
		
	}
	
	/**
	 * Códigos proyecciones
	 */
	public static final String CTE_PREFIJO_CORRIENTE = "PROY_";
	public static final String CTE_PERIODOS = "PERIODOS";
	public static final String CTE_BASE_TEC = "ORQUEST_BT";
	public static final String CTE_PROY_VIDA = "PROY_VIDA";
	public static final String CTE_PROY_COMI = "PROY_COMI";
	public static final String CTE_PROY_FALL = "PROY_FALL";
	public static final String CTE_PROY_GTOS = "PROY_GTOS";
	public static final String CTE_PROY_PRIMA = "PROY_PRIMA";
	public static final String CTE_PROY_PRV = "PROY_PRV";
	public static final String CTE_PROY_RESC = "PROY_RESC";
	public static final String CTE_PROY_INVA = "PROY_INVA";
	public static final String CTE_PROY_COMP = "PROY_COMP";
	public static final String CTE_PROY_PMRR = "PROY_PMRR";
	public static final String CTE_PROYECCION = "PROYECCION";
	public static final String CTE_PROC_NIIF17 = "NIIF17";
	public static final String CTE_PROC_PESOSBT = "PESOSBT";
	public static final String CTE_PROC_PATRONCSM = "PATRONCSM";


	public static final String CTE_CALC_PRV = "CALC_PRV";

	public static final String CTE_PROY_GTOAD = "PROY_GTOAD";
	
	/**
	 * Códigos elementos
	 */
	public static final String CTE_ELEMENTO_NOMINAL = "01";
	public static final String CTE_ELEMENTO_PROBABLE = "02";
	public static final String CTE_ELEMENTO_NOANULADO = "03";
	public static final String CTE_ELEMENTO_ACTUALIZADO = "04";
	public static final String CTE_ELEMENTO_PROVI = "05";
	public static final String CTE_ELEMENTO_TERMINAL = "06";
	
	
	/**
	 * Códigos de Error
	 */
	public static final String CTE_ERROR_02 = "02";
	public static final String CTE_ERROR_03 = "03";
	public static final String CTE_ERROR_06 = "06";
	public static final String FICHERO_SIN_UMICS = "ZY";
	public static final String NO_FP_COD_ERROR = "ZZ";
	public static final String NO_BTI_COD_ERROR = "ZX";
	
	/**
	 * Tipos de Error
	 */
	public static final String CTE_ERROR = "AA";
	public static final String CTE_ERROR_BLOQUEANTE = "XX";
	public static final String CTE_AVISO = "II";
	
	/**
	 * Descripciones de error
	 */
	public static final String MENSAJE_ERROR_TECNICO = "Error técnico no controlado";
	
	
	/**
	 * Otros valores
	 */
	public static final int EXPORT_CHUNK_SIZE = 10000;
	public static final int UMICS_CHUNK_SIZE = 10000;
	public static final String MENSAJE_FIN_PROCESAMIENTO_UMIC = "FINALIZADO PROCESAMIENTO DE TODAS LAS UMIC";
	public static final String MENSAJE_INICIO_PROCESO = "INICIADO PROCESO AMAZON";
	public static final String MENSAJE_FIN_PROCESO = "FINALIZADO PROCESO AMAZON";
	public static final String MENSAJE_FIN_CARGA_FICHEROS = "CARGADA INFORMACIÓN EN COHERENCE";
	public static final String CRITERIO_FECHA_PROY_PRV = "INIP";
	public static final String IPC_GENERAL_FUTURO = "IPC General Futuro";
	public static final String GASTOS_REALES = "Gastos Reales";
	public static final String TABLA_EXPERIENCIA = "Tabla Experiencia";
	public static final String CABECERA_TABLA_EXPERIENCIA = "Cabecera Tabla Experiencia";
	public static final String LIMITES_CAPITAL = "LimitesCapital";
	public static final String UMIC_PRINCIPAL = "UMIC Principal";
	public static final String UMIC_TITULAR = "UMIC Titular";
	public static final String TIPO_TABLA_COMPLEMENTARIA = "2";
	public static final String TIPO_TABLA_GENERACIONAL = "3";
	public static final String TIPO_TABLA_TRADICIONAL = "1";
	public static final String NTABLA_LIMITES_CAPITAL = "646";
	public static final String COD_FICHERO_UMICS = "X880JI01";
	
	/**
	 * Ficha resultado
	 */
	public static final String TEXTO_FICHA_RESULTADO_PROCESADAS = "UMIC tratadas: ";
	public static final String TEXTO_FICHA_RESULTADO_PROCESADAS_SEGUNDO = "UMIC tratadas por segundo: ";
	public static final String TEXTO_FICHA_RESULTADO_ERROR = "Total UMIC con error: ";
	public static final String TEXTO_FICHA_RESULTADO_AVISO = "Total UMIC con aviso: ";
	public static final String TEXTO_FICHA_RESULTADO_SUBERROR = "Número de incidencias tipo ";
	public static final String TEXTO_FICHA_FINAL_REGISTRO_SUBERROR = ": ";
	public static final String TEXTO_FICHA_RESULTADO_NODOS_EMPLEADOS = "Número de Nodos de Cálculo empleados: ";
	
	/**
	 * Fichas de Proceso
	 */
	public static final String CTIPOEJEC_CIERRE="COMP";
	public static final String CTIPOEJEC_REPROCESO="REPR";
	public static final String CTIPOEJEC_BOTE="BOTE";
	public static final String NEGOCIO_INDIVIDUAL = "I";
	public static final String NEGOCIO_COLECTIVO = "C";

	/**
	 * Indicadores tabla experiencia
	 */
	public static final char CTE_TABLA_REALISTA = 'R';
	public static final char CTE_TABLA_TRADICIONAL = 'T';
	

	/** Constantes para definir el tipo de los valores de la Tabla de Experiencia - L, Q o I (Invalidez)*/
	public static final String CTE_TABMORT_L = "L";
	public static final String CTE_TABMORT_Q = "Q";
	public static final String CTE_TABMORT_I = "I";
	
	/**
	 * Bases técnicas
	 */
	public static final String BASE_BTI = "BTI";
	public static final String BASE_ROSSP = "ROSSP";
	public static final String BASE_BEL = "BEL";
	public static final String BASE_SCR = "SCR";
	public static final String BASE_BELCOA = "BELCOA";
	public static final String BASE_BELCLR = "BELCLR";
	public static final String BASE_SCRTIU = "SCRTIU";
	public static final String BASE_SCRTID = "SCRTID";
	public static final String BASE_SCRGTO = "SCRGTO";
	public static final String BASE_SCRMFE = "SCRMFE";
	public static final String BASE_SCRMMI = "SCRMMI";
	public static final String BASE_SCRMCF = "SCRMCF";
	public static final String BASE_SCRMCI = "SCRMCI";
	public static final String BASE_SCRLFE = "SCRLFE";
	public static final String BASE_SCRLMI = "SCRLMI";
	public static final String BASE_SCRINC = "SCRINC";
	public static final String BASE_SCRVM  = "SCRVM" ;
	public static final String BASE_SCRAEN = "SCRAEN";
	public static final String BASE_SCRAEP = "SCRAEP";
	public static final String BASE_SCRAIN = "SCRAIN";
	public static final String BASE_SCRAIP = "SCRAIP";
	public static final String BASE_SCRANM = "SCRANM";
	public static final String MULTI3 = "MULTI3";
	public static final String MULTI2 = "MULTI2";
	public static final String BASE_NIIF17 = "NIIF17";
	public static final String BASE_NIF17LIR = "NIIF17LIR";
	public static final String BASE_N17LIRIN = "N17LIRIN";
	public static final String BASE_NIFF17OCI = "NIIF17OCI";
	public static final String BASE_NIIF17IF = "NIIF17IF";
	public static final String BASE_N17CLIR = "N17CLIR";
	public static final String MULTI7 = "MULTI7";
	public static final String BASE_BTIPROY = "BTIPROY";
	public static final String BASE_ROSSPCSM = "ROSSPCSM";
	public static final String MULTI6 = "MULTI6";
	public static final String BASE_ROSSPTE = "ROSSPTE";
	public static final String BASE_ROSSPTI = "ROSSPTI";
	public static final String BASE_ROSSPGA = "ROSSPGA";
	public static final String BTCOA = "BTCOA";
	public static final String BTCOATF = "BTCOATF";

	/**
	 * Apoyo SCR
	 */
	public static final String VAR_VM1 = "VM1";
	public static final String VAR_VM2 = "VM2";
	
	/**
	 * Tipo de caché replicada
	 */
	public static final String REPLICATED_CACHE = "ReplicatedCache";

	/**
	 * Volumen de datos a cargar en cada bloque
	 */
	public static final int BATCH_SIZE = 1000;
	public static final String CATALOGOS = "catalogos";
	public static final String CATALOGOS_ALL = "catalogos-all";
	public static final String CATALOGOS_MULTI2 = "catalogos-multi2";
	public static final String CATALOGOS_MULTI3 = "catalogos-multi3";
	public static final String AUXILIARES = "auxiliares";
	public static final String FICHEROS_SALIDA = "salida";
	public static final String FICHA_SALIDA = "fichaSalida";
	public static final String MENSAJES_ERRORES = "mensajes.errores";
	public static final String UMICS_BLOQUE = "umics.bloque";
	public static final String REGEX_FILES = "[.]*[0-9].*";
	public static final String RUTA_BOTES = "ruta.botes";

	/**
	 * Constante de inclusión en los filtros
	 */
	public static final String INCLUSIVO = "I";
	
	// JBMARTA - PYAM0025 - INI
	/**
	 * Constante de inclusión en los filtros
	 */
	public static final String INCLUSIVO_ADICIONAL = "IN";
	// JBMARTA - PYAM0025 - FIN
	
	/**
	 * Buffer de escritura para BeanIO en gzip
	 */
	public static final int GZIP_BUFFER_SIZE = 8 * 1024;
		
	/**
	 * Charset de los ficheros de entrada-salida
	 */
	public static final String FILE_CHARSET = "ISO-8859-1";


	/**
	 * Tipos de ejecución: Cierre = C
	 */
	public static final String TIPO_EJECUCION_CIERRE_NO_SIMULADO = "C";
	/**
	 * Tipos de ejecución: Simulación = S
	 */
	public static final String TIPO_EJECUCION_SIMULACION = "S";
	public static final String PREFIJO_SIMULACION = "SIMU_";
	/**
	 * Códigos entregables
	 */
	public static final String CTE_ENTREGABLES = "ENTREGABLES";
	public static final String CTE_INFOBT = "INFOBT"; 
	public static final String CTE_PRVAGREGA = "PRVAGREGA";
	public static final String CTE_FLUJOTOT = "FLUJOTOT";
	public static final String CTE_PROVICOA = "PROVICOA";
	/*INI-TAR00302248*/
	public static final String CTE_SSAA = "SSAA";
	/*FIN-TAR00302248*/
	
	/**
	 * Tipos de Origen PTIPO
	 */
	public static final String ORIGEN_MANUAL = "MA";
	public static final String ORIGEN_AUTOMATICO = "AU";
	
	
	/**
	 * Nombres cachés
	 */
	public static final String CACHE_PTIPO = "PTIPO";
	public static final String CACHE_DATOS_GENERALES ="datos-generales";
	public static final String CACHE_INFOPTIPO = "infoPtipo";
	
	// JBMARTA - PYAM0025 - INI
	/**
	 * Fecha String Reglamento ROSSP
	 */
	public static final String CTE_FECHA_REGLAMENTO_ROSSP = "31/12/1998";

	/**
	 * Constantes de errores para los filtros
	 */
	// FILTROS
	@Deprecated
	public static final String CTE_FILTRO_AMBITO_NO_HOMOGENEO_FILTROS_AMBITO = "F0";
	@Deprecated
	public static final String CTE_FILTRO_AMBITO_NO_HOMOGENEO_FILTROS_ADIC = "F1";
	public static final String CTE_FILTRO_ATRIBUTOS_CNEGOCIO_O_CNAL_NULO = "F2";
	public static final String CTE_FILTRO_ERROR_VALOR_FILTRO_MODALIDAD = "F3";
	public static final String CTE_FILTRO_ERROR_VALOR_FILTRO_ACTIVO_PASIVO = "F4";
	public static final String CTE_FILTRO_OPERADOR_DESDE_NULO = "F5";
	public static final String CTE_FILTRO_OPERADOR_DESDE_NO_PERMITIDO_EN_TIPO_STRING = "F6";
	public static final String CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO = "F7";
	public static final String CTE_FILTRO_VALOR_INC_EXC_NO_HOMOGENEO = "F8";
	public static final String CTE_FILTRO_ERROR_VALOR_OPERADORES = "F9";
	public static final String CTE_FILTRO_OPERADOR_HASTA_NO_PERMITIDO_EN_TIPO_STRING = "FA";
	public static final String CTE_FILTRO_SEGM_RIESGO_NINGUN_PARAMETRO_INFORMADO = "FB";
	public static final String CTE_FILTRO_MOD_GARANTIA_PARAM_NO_INFORMADO = "FC";
	public static final String CTE_FILTRO_POL_SUBPOL_CERT_ALGUN_PARAMETRO_NO_INFORMADO = "FD";
	public static final String CTE_FILTRO_POL_SUBPOL_SUSC_ALGUN_PARAMETRO_NO_INFORMADO = "FE";
	public static final String CTE_FILTRO_MOD_GARANTIA_MODALIDAD_NO_HOMOGENEA = "FF";
	public static final String CTE_FILTRO_GAP_CARTERA_GAP_NO_HOMOGENEO = "FG";

	// JBMARTA - PYAM0025 - FIN
	
	public static final Integer CTE_ORDEN_PARAMETRO_NMES = 1;

public static final Set<Integer> MODALIDADES_RENTAS_2C = new HashSet<Integer>();
	static{
		MODALIDADES_RENTAS_2C.add(164);
		MODALIDADES_RENTAS_2C.add(165);
		MODALIDADES_RENTAS_2C.add(462);
	}
	
	// Constantes para el nuevo entregable FlujosTotP
	public static final String CTE_SISTEMA = "SOLV";
	public static final String CTE_PROYECTO_TECNICO = "SO11";
	public static final String CTE_PROCESO_TECNICO_NIIF17 = "SO12";
	public static final String CTE_PROCESO_TECNICO_SWCOBROCOMISIONES = "SO13";
	public static final String CTE_UMIC_EJECUCION = "GP03";
	public static final String CTE_UNID_EJEC_NIIF17 = "GP05";
	public static final String CTE_UNID_EJEC_SWCOBROCOMISIONES = "GP06";
	public static final String CTE_UNID_EJEC_FLUJSUSCRI = "GP10";
	public static final String CTE_CHECKPRIMA = "CHECKPRIMA";
	public static final String CTE_NIIF17 = "NIIF17";
	public static final String CTE_SWCOBROCOMISIONES = "SWCOBROCOM";
	
	// Constantes para el proxy
	public static final String STREAM_PESOSBT = "pesosbt";
	public static final String STREAM_PESOSBTPROXY = "pesosbtproxy";
	public static final String FICH_PESOSBTPROXY = "PESOSBTPROXY";
	public static final String STREAM_FLUJCOASEG = "flujcoaseg";
	public static final String STREAM_PROVCOASEG = "provcoaseg";
	public static final String STREAM_TOTPMACOA = "totpmacoa";
	public static final String STREAM_FLUJTCAS = "flujtcas";
	public static final String STREAM_FLUJPMACOA = "flujpmacoa";
	public static final String STREAM_FLUJPMDCOA = "flujpmdcoa";
	
	public static final String MULTI2A = "MULTI2A";

	public static final String MULTI2M = "MULTI2M";

	public static final String MULTI2G = "MULTI2G";
 
	
}