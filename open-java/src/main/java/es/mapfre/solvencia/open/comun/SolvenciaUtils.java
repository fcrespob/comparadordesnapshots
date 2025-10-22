/**
 * 
 */
package es.mapfre.solvencia.open.comun;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.config.ConfigCargaFichero;
import es.mapfre.solvencia.open.config.ConfigSolvenciaOpen;
import es.mapfre.solvencia.open.enums.TipoFichero;
import es.mapfre.solvencia.open.exception.SolvenciaRuntimeException;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

/**
 * @author amdepedro
 * 
 */
public final class SolvenciaUtils {

	public static final long SLEEP_THREAD = 5000L;

	private static Logger logger = LoggerFactory.getLogger(SolvenciaUtils.class);
	
	private static ExecutorService threadPool = Executors.newFixedThreadPool(Constantes.PARALLEL_THREADS);
	
	private static BtUtils btUtils = new BtUtils();
	
	public static void shutdown() {
		if (null != threadPool) {
			threadPool.shutdownNow();
		}
	}

	private SolvenciaUtils() {

	}

	/**
	 * Obtiene la fecha de cierre configurada como parámetro de la aplicación
	 * 
	 * @return fecha de cierre
	 */
	public static String getFechaCierre() {
		return getFechaCierre(null);
	}

	/**
	 * Obtiene la fecha de cierre configurada como parámetro de la aplicación
	 * 
	 * @param param
	 *            nombre del parámetro que contiene la fecha de cierre
	 * @return fecha de cierre
	 */
	public static String getFechaCierre(String param) {
		String propertyFecCierre = null;
		if (param == null || param.isEmpty()) {
			propertyFecCierre = Constantes.PARAM_FECHA_CIERRE;
		} else {
			propertyFecCierre = param;
		}
		String fecCierre = System.getProperty(propertyFecCierre);

		if (fecCierre == null || fecCierre.length() != Constantes.LONGITUD_PARAMETRO_FECHA_CIERRE) {
			logger.error("La fecha de cierre recibida ({}) es errónea");
			throw new SolvenciaRuntimeException("Error: La fecha de cierre recibida ({}) es errónea");
		}
		return fecCierre;
	}

	/**
	 * Obtiene la ruta base configurada.
	 * 
	 * @return ruta
	 */
	public static String getRutaBase() {
		return ConfigSolvenciaOpen.getInstance().getProperty(Constantes.PARAM_RUTA_BASE, Constantes.RUTA_BASE_DEFECTO);
	}

	/**
	 * Obtener la ruta absoluta de cierre de la aplicación
	 * 
	 * @param concatFechaCierre
	 *            <code>true</code> para concatenar la fecha de cierre a la ruta
	 *            de cierre <code>false</code> para no concatenar la fecha de
	 *            cierre a la ruta de cierre
	 * @return ruta de cierre
	 */
	public static String getRutaCierre(boolean concatFechaCierre) {
		String rutaBase = getRutaBase();
		String fecCierre = getFechaCierre();
		StringBuffer rutaCierre = new StringBuffer();
		rutaCierre.append(rutaBase).append(File.separator).append(Constantes.RUTA_CIERRES);
		// concatFechaCierre = true en caso de querer la ruta de cierre con la
		// fecha de cierre
		if (concatFechaCierre) {
			rutaCierre.append(File.separator).append(fecCierre);
		}
		return rutaCierre.toString();
	}

	/**
	 * Obtener la ruta relativa de cierre de la aplicación
	 * 
	 * @param concatFechaCierre
	 *            <code>true</code> indica que se quiere concatenar la fecha de
	 *            cierre a la ruta de cierre y <code>false</code> indica que no
	 *            se quiere concatenar la fecha de cierre a la ruta de cierre
	 * @return ruta de cierre
	 */
	public static String getRutaCierreRelativa(boolean concatFechaCierre) {
		String fecCierre = getFechaCierre();
		StringBuffer rutaCierre = new StringBuffer();
		rutaCierre.append(File.separator).append(Constantes.RUTA_CIERRES);
		// concatFechaCierre = true en caso de querer la ruta de cierre con la
		// fecha de cierre
		if (concatFechaCierre) {
			rutaCierre.append(File.separator).append(fecCierre);
		}
		return rutaCierre.toString();
	}

	public static String getRutaRelativaFichero(String tipoFichero) {
		String ruta = ConfigCargaFichero.getInstance().getProperty(tipoFichero);
		if (ruta != null) {
			ruta = ruta.replace('/', File.separatorChar);
		}

		return ruta;
	}

	public static Map<FichaResultadoKey, List<FichaResultado>> cargarFichasResultadoLog() throws IOException {
		return cargarFichasResultadoLog(Constantes.STREAM_LOGS, Constantes.STREAM_LOGS);
	}
	
	public static Map<FichaResultadoKey, List<FichaResultado>> cargarFichasResultadoLogPurgado() throws IOException {
		return cargarFichasResultadoLog(Constantes.STREAM_LOGS_PURGADO, Constantes.STREAM_LOGS);
	}
	
	public static Map<FichaResultadoKey, List<FichaResultado>> cargarFichasResultadoLog(String codigoFicheroLogs, String streamName) throws IOException {
		logger.info("Cargando fichas resultado....");
		Map<FichaResultadoKey, List<FichaResultado>> fichasResultado = new HashMap<FichaResultadoKey, List<FichaResultado>>();
		String filePath = getRutaCierre(true) + SolvenciaUtils.getRutaRelativaFichero(codigoFicheroLogs);
		BeanIOReader in = new BeanIOReader(Constantes.BEANIO_CONFIG_XML, null, null);
		String error = "Error cargando fichas de resultados";
		try {
			in.createReader(filePath, streamName);
			FichaResultado ficha = null;
			while ((ficha = (FichaResultado) in.read()) != null) {
				escribeLog(ficha, fichasResultado);
			}
		} catch (IOException e) {
			logger.error(error, e);
			throw new SolvenciaRuntimeException(e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.error(error, e);
				throw new SolvenciaRuntimeException(e);
			}
		}
		logger.info("Se han cargado " + fichasResultado.size() + " fichas resultado.");
		return fichasResultado;
	}

	public static void generarFichasResultadoLog(BeanIOWriter out, String rutaCierre,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		generarFichasResultadoLog(out, rutaCierre, fichasResultado, Constantes.STREAM_LOGS);
	}
	
	public static void generarFichasResultadoLogPurgado(BeanIOWriter out, String rutaCierre,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		generarFichasResultadoLog(out, rutaCierre, fichasResultado, Constantes.STREAM_LOGS_PURGADO);
	}
	
	public static void generarFichasResultadoLog(BeanIOWriter out, String rutaCierre,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado, String streamName) {
		String filePath = rutaCierre + SolvenciaUtils.getRutaRelativaFichero(streamName);

		// Guardar las fichas de resultado
		try {
			out.createWriter(filePath, Constantes.STREAM_LOGS);

			for (List<FichaResultado> fichas : fichasResultado.values()) {
				for (FichaResultado ficha : fichas) {
					out.write(ficha);
				}
			}

		} catch (IOException e) {
			logger.error("Error generando las fichas de resultado", e);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				logger.error("IOException: ", e);
			}
		}
	}

	//CAV
	public static void validarExistenciaFicheros(List<FichaProceso> listaFichasProceso) {
		List<String> ficherosNoEncontrados = new ArrayList<String>();

		String[] ficherosErrores = ConfigCargaFichero.getInstance().getProperty(Constantes.LISTA_FICHEROS_ERRORES)
				.split(",");

		for (String fichero : ficherosErrores) {
			String filePath = getRutaCierre(false) + SolvenciaUtils.getRutaRelativaFichero(fichero);
			if (!new File(filePath).exists()) {
				ficherosNoEncontrados.add(filePath);
			} else {
				SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
			}
		}

		String[] ficherosAuxiliares = ConfigCargaFichero.getInstance()
				.getProperty(Constantes.LISTA_FICHEROS_AUXILIARES).split(",");

		for (String fichero : ficherosAuxiliares) {
			String filePath = getRutaCierre(true) + SolvenciaUtils.getRutaRelativaFichero(fichero);
			if (!new File(filePath).exists()) {
				ficherosNoEncontrados.add(filePath);
			} else {
				SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
			}
		}

		String[] ficherosCatalogo = ConfigCargaFichero.getInstance().getProperty(Constantes.LISTA_FICHEROS_CATALOGOS)
				.split(",");

		for (String fichero : ficherosCatalogo) {
			String filePath = getRutaCierre(false) + SolvenciaUtils.getRutaRelativaFichero(fichero);
			if (!new File(filePath).exists()) {
				ficherosNoEncontrados.add(filePath);
			} else {
				SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
			}
		}

		String filePath = getRutaCierre(true) + SolvenciaUtils.getRutaRelativaFichero(Constantes.STREAM_UMIC);
		if (!new File(filePath).exists()) {
			ficherosNoEncontrados.add(filePath);
		} else {
			SolvenciaUtils.borrarFicherosComprimidosAntiguos(filePath);
		}

		// Buscar los ficheros de los botes
		for (FichaProceso fichaProceso : listaFichasProceso) {
			String tipoEjecucion = null;
			if (fichaProceso.getRegistroParametros() != null) {
				tipoEjecucion = fichaProceso.getRegistroParametros().getIndicadorTipoProceso();
			}
			
			if (ConstantesSolvencia.CTIPOEJEC_BOTE.equals(tipoEjecucion)) {
				filePath = SolvenciaUtils.construirRutaBotes(fichaProceso.getCcanal(), fichaProceso.getCnegocio(), Boolean.FALSE);
				if (!new File(filePath).exists()) {
					ficherosNoEncontrados.add(filePath);
				}
			}
		}
		
		
		if (ficherosNoEncontrados.size() > 0) {
			logger.error("No se encuentran los ficheros necesarios: {}", ficherosNoEncontrados);
			throw new SolvenciaRuntimeException("No se encuentran los ficheros necesarios");
		}

	}

	public static void escribeLog(FichaResultado fichaResultado,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		FichaResultadoKey fichaKey = fichaResultado.getKey();
		List<FichaResultado> fichas = fichasResultado.get(fichaKey);
		if (fichas == null) {
			fichas = new ArrayList<FichaResultado>();
		}
		fichas.add(fichaResultado);
		fichasResultado.put(fichaKey, fichas);
	}

	public static FichaResultado creaRegistroFichaResultado(FichaProceso fichaProceso, String traza) {
		FichaResultado fichaResultado = new FichaResultado();

		fichaResultado.setKejecucion(fichaProceso.getKejecucion());
		fichaResultado.setKsistema(fichaProceso.getKsistema());
		fichaResultado.setKprotecnico(fichaProceso.getKprotecnico());
		fichaResultado.setKuejecucion(fichaProceso.getKuejecucion());
		fichaResultado.setKsecuencia(fichaProceso.getKsecuencia());
		fichaResultado.setKtipores("HIST");
		fichaResultado.setKsecres(000);
		fichaResultado.setGc1Resul(traza);

		return fichaResultado;
	}

	/**
	 * Método para obtener el tipo de fichero para diferenciar el procedimiento
	 * almacenado a ejecutar.
	 * 
	 * @param nombre
	 *            nombre del fichero
	 * @return Tipo de fichero
	 */
	public static TipoFichero getTipoFichero(String nombre) {
		Pattern pat = Pattern.compile(Constantes.EXPRE_FICHEROS_MAESTC);
		Matcher mat = pat.matcher(nombre);
		if (mat.find()) {
			return TipoFichero.getTipoFichero(mat.group());
		}
		return null;
	}

	/**
	 * Método que indica si está habilitado o no el paralelismo.
	 * 
	 * @return <code>true</code> o <code>false</code>
	 */
	public static Boolean isParalelismo() {
		String habilitarParalelismo = ConfigSolvenciaOpen.getInstance().getProperty(Constantes.HABILITAR_PARALELISMO);
		if (habilitarParalelismo == null) {
			return Boolean.FALSE;
		}
		if (Constantes.SI.equals(habilitarParalelismo.trim().toUpperCase())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public static String getDBUrl() {
		StringBuilder sbUrl = new StringBuilder("jdbc:oracle:thin:@");
		
		String hosts = ConfigSolvenciaOpen.getInstance().getProperty("bbdd.host");
		String ports = ConfigSolvenciaOpen.getInstance().getProperty("bbdd.port");
		
		sbUrl.append("(DESCRIPTION=(ADDRESS_LIST=");
		
		if(hosts != null && ports != null) {
			String[] arHosts = hosts.split(",");
			String[] arPorts = ports.split(",");
			
			if (arHosts.length > 1) {
				sbUrl.append("(LOAD_BALANCE=ON)(FAILOVER=ON)");
			}

			for (int i=0; i<arHosts.length; i++) {
				sbUrl.append("(ADDRESS=(PROTOCOL=TCP)(HOST=").append(arHosts[i]).append(")(PORT=").append(arPorts[i]).append("))");
			}
			
		}
		
		String serviceName = ConfigSolvenciaOpen.getInstance().getProperty("bbdd.service_name");
		
		sbUrl.append(")(CONNECT_DATA=(SERVER=DEDICATED)(SERVICE_NAME=").append(serviceName).append(")))");
		
		return sbUrl.toString();
	}

	public static String getDBUser() {
		return ConfigSolvenciaOpen.getInstance().getProperty("bbdd.user", "solvencia");
	}

	public static String getDBPass() {
		return ConfigSolvenciaOpen.getInstance().getProperty("bbdd.password", "indra2014");
	}

	public static String getDBScheme() {
		return ConfigSolvenciaOpen.getInstance().getProperty("bbdd.scheme", "SOLVENCIA_LD");
	}

	public static String getProcPackage() {
		return ConfigSolvenciaOpen.getInstance().getProperty("bbdd.package", "TOTALES");
	}

	public static String construirRutaResultados(Integer canal, String negocio, Boolean isRelativa) {
		if (canal == null || negocio == null) {
			// TODO: Gestionar esta excepción
			logger.error("Canal y negocio son necesarios para construir la ruta de resultados.");
			throw new SolvenciaRuntimeException("Canal y negocio son necesarios para construir la ruta de resultados.");
		}
		StringBuffer ruta = new StringBuffer();
		if (isRelativa) {
			ruta.append(SolvenciaUtils.getRutaCierreRelativa(true));
		} else {
			ruta.append(SolvenciaUtils.getRutaCierre(true));
		}
		ruta.append(File.separator).append(canal);
		// dependiendo del canal construiremos una ruta u otra.
		if (ConstantesSolvencia.NEGOCIO_COLECTIVO.equals(negocio)) {
			// canal colectivos --> ruta de RESULT_COL
			ruta.append(File.separator).append(Constantes.RESULT_COL);
		} else if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(negocio)) {
			// canal individuales --> ruta de RESULT_IND
			ruta.append(File.separator).append(Constantes.RESULT_IND);
		} else {
			// TODO: Gestionar esta excepción
			logger.error("Negocio no reconocido.");
			throw new SolvenciaRuntimeException("Negocio no reconocido.");
		}
		return ruta.toString();
	}

	public static String construirRutaBotes(Integer canal, String negocio, Boolean isRelativa) {
		if (canal == null || negocio == null) {
			logger.error("Canal y negocio son necesarios para construir la ruta de resultados.");
			throw new SolvenciaRuntimeException("Canal y negocio son necesarios para construir la ruta de resultados.");
		}
		StringBuffer ruta = new StringBuffer();
		if (isRelativa) {
			ruta.append(SolvenciaUtils.getRutaCierreRelativa(true));
		} else {
			ruta.append(SolvenciaUtils.getRutaCierre(true));
		}

		ruta.append(File.separator);
		ruta.append(btUtils.getCargaFicherosProperty(ConstantesSolvencia.RUTA_BOTES));
		ruta.append(File.separator);
		ruta.append(negocio).append(canal).append(File.separator);
		ruta.append(btUtils.getCargaFicherosProperty("incidencias"));
		
		return ruta.toString();
	}
	
	/**
	 * @param ficheroOriginal
	 * @param numTrozos
	 * @param minSize
	 */
	public static Future partirFichero(String ficheroOriginal, String formatoFinal, int numTrozos, long minSize) {
		EjecutorPartirFicheros ejecutor = new EjecutorPartirFicheros(ficheroOriginal, formatoFinal, numTrozos, minSize);
		return threadPool.submit(ejecutor);
	}

	public static void borrarFicherosComprimidosAntiguos(String filePath) {
		String dir = filePath.substring(0, filePath.lastIndexOf(File.separatorChar));
		String fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1);

		File directorio = new File(dir);
		File[] ficherosPartidos = directorio.listFiles(new FiltroFicherosPartidos(fileName));

		for (File ficheroPartido : ficherosPartidos) {
			ficheroPartido.delete();
		}
	}

	public static void descomprimirFichero(String ficheroOriginal, String ficheroDestino) {
		FileInputStream fis = null;
		InflaterInputStream zis = null;
		FileOutputStream fos = null;

		try {
			fis = new FileInputStream(ficheroOriginal);
			if (ficheroOriginal.endsWith(Constantes.FORMATO_FICHERO_GZIP)) {
				zis = new GZIPInputStream(fis);
			} else if (ficheroOriginal.endsWith(Constantes.FORMATO_FICHERO_ZIP)) {
				zis = new ZipInputStream(fis);
			} else {
				fis.close();
				return;
			}
			fos = new FileOutputStream(ficheroDestino);
			byte[] buffer = new byte[Constantes.GZIP_BUFFER_SIZE];
			int len;
			while ((len = zis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}

		} catch (IOException e) {
			logger.error("Error descomprimiendo fichero {}", ficheroOriginal, e);
		} finally {
			if (zis != null) {
				try {
					zis.close();
				} catch (IOException e) {
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
				}
			}

			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
				}
			}
		}

	}
	
	/**
	 * Obtiene la URL del servicio JMX.
	 * 
	 * @return ruta
	 */
	public static String getJmxURL() {
		return ConfigSolvenciaOpen.getInstance().getProperty("jmx.url", Constantes.URL_JMX_DEFECTO);
	}


}
