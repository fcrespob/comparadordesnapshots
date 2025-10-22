/**
 * 
 */
package es.mapfre.solvencia.open.extraer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.dao.ProceduresDAO;
import es.mapfre.solvencia.open.dao.extraer.ProceduresExtraerDAO;
import es.mapfre.solvencia.open.dto.ExtraerFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;
import es.mapfre.solvencia.open.exception.SolvenciaRuntimeException;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

/**
 * @author indra
 * 
 */
public final class SolvenciaUtilsExtraerBBDD {

	private static Logger logger = LoggerFactory.getLogger(SolvenciaUtilsExtraerBBDD.class);
	private static BtUtils btUtils = new BtUtils();
	private static Boolean hayError = Boolean.FALSE;

	private SolvenciaUtilsExtraerBBDD() {

	}

	/**
	 * Método encargado de la invocacion a los procedimientos almacenados de
	 * extracción para una lista de ficheros pasados como parámetro.
	 * 
	 * @param listaFicherosExtraer
	 */
	public static void ejecutarProcedimientoExtraer(Map<TipoFichero, List<ExtraerFicheroDTO>> mapaFicherosExtraer) {
		List<ExtraerFicheroDTO> listaFicherosExtraer = new ArrayList<ExtraerFicheroDTO>();
		if (mapaFicherosExtraer == null || mapaFicherosExtraer.isEmpty()) {
			logger.debug("No existen ficheros a extraer de BBDD.");
		} else {
			Collection<List<ExtraerFicheroDTO>> listaFicheros = mapaFicherosExtraer.values();
			for (List<ExtraerFicheroDTO> list : listaFicheros) {
				listaFicherosExtraer.addAll(list);
			}
			logger.debug("Existen en total {} tipos de ficheros a extraer.", listaFicherosExtraer.size());
		}
		boolean isParalelismo = SolvenciaUtils.isParalelismo();
		if (isParalelismo) {
			logger.debug("Paralelismo habilitado.");
			// usar paralelismo
			ExecutorService executor = null;
			try {
				executor = Executors.newFixedThreadPool(listaFicherosExtraer.size() * 2);
				for (Map.Entry<TipoFichero, List<ExtraerFicheroDTO>> entry : mapaFicherosExtraer.entrySet()) {
					executor.execute(new EjecutorExtraer(entry.getKey(), entry.getValue()));
				}

				executor.shutdown();
				
				while (!executor.isTerminated()) {
					try {
						Thread.sleep(SolvenciaUtils.SLEEP_THREAD);
					} catch (InterruptedException e) {
						logger.error(e.getMessage());
					}
				}
			} finally {
				ProceduresDAO.closeConnections();
				if (null != executor) {
					executor.shutdownNow();
				}
			}
		} else {
			logger.debug("Paralelismo deshabilitado. Extraemos {} tipos de fichero.", mapaFicherosExtraer.size());

			for (Map.Entry<TipoFichero, List<ExtraerFicheroDTO>> entry : mapaFicherosExtraer.entrySet()) {
				ejecutarProcedimiento(entry.getKey(), entry.getValue());
			}
		}

	}

	/**
	 * Método encargado de la invocacion a los procedimientos almacenados de
	 * extracción para una lista de ficheros pasados como parámetro.
	 * 
	 * @param params
	 *            fichero a extraer
	 */
	public static void ejecutarProcedimiento(TipoFichero tipoFichero, List<ExtraerFicheroDTO> params) {
		if (params == null) {
			logger.debug("No existe fichero a cargar.");
		} else {
			logger.debug("Ficheros a extraer: {}" + params);
		}
		try {
			ProceduresExtraerDAO.callOracleStoredProceduresExtraer(tipoFichero, params);
		} catch (Exception e) {
			logger.error("Error ejecutarProcedimiento {}", e.getMessage());
			setHayError(Boolean.TRUE);
		}
	}

	/**
	 * Método encargado de realizar la invocació a los procemdiento almacenados
	 * correspondientes a partir de la lista de fichas de proceso pasada como
	 * parámetro.
	 * 
	 * @param listaFichasProceso
	 *            lista de fichas de proceso a extraer
	 */
	public static void procesarFicherosExtraer(List<FichaProceso> listaFichasProceso,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		logger.info("Procesando ficheros extraer...");
		int numeroFicherosExtraer = 0;
		for (FichaProceso fichaExtraer : listaFichasProceso) {
			if (ConstantesSolvencia.TIPO_EJECUCION_CIERRE_NO_SIMULADO.equals(fichaExtraer.getCtipoejec())) {
				List<ExtraerFicheroDTO> listaFicherosExtraccion = new ArrayList<ExtraerFicheroDTO>();
				String fechaCierre = SolvenciaUtils.getFechaCierre();
				DateFormat dfAAAAMM = new SimpleDateFormat("yyyyMM");
				DateFormat dfAAAAMMDD = new SimpleDateFormat("yyyyMMdd");
				// recorremos la fichas de proceso
				String fecEfecto = dfAAAAMM.format(fichaExtraer.getFefecto());
				// comprobamos que la fecha de efecto de la ficha corresponde
				// con la fecha de cierre configuradas
				if (fecEfecto.equals(fechaCierre)) {
					String tipoCierre = fichaExtraer.getRegistroParametros().getIndicadorTipoProceso();
					// se realiza la extracción cuando el tipo de ejecución sea
					// REPR o BOTE
					if (ConstantesSolvencia.CTIPOEJEC_BOTE.equals(tipoCierre)
							|| ConstantesSolvencia.CTIPOEJEC_REPROCESO.equals(tipoCierre)) {
						Integer canal = fichaExtraer.getCcanal();
						String negocio = fichaExtraer.getCnegocio();
						String ctipobt = fichaExtraer.getCtipobt();
						// obtenemos la ruta donde se almacenarán los ficheros
						// de resultados
						String ruta = SolvenciaUtils.construirRutaResultados(canal, negocio, Boolean.TRUE);
						String rutaAbsoluta = SolvenciaUtils.construirRutaResultados(canal, negocio, Boolean.FALSE);
						// Asignamos permisos de escritura al directorio
						File dirSalida = new File(rutaAbsoluta);
						dirSalida.mkdirs();
						dirSalida.setWritable(true, false);
						// Se da el permiso de ejecutable para prevenir problemas de salida de Oracle
						dirSalida.setExecutable(true, false);
						
						// obtenemos las bases técnicas del tipo de base tecnica
						// de la ficha de proceso
						List<String> listaBts = getBts(ctipobt);
						for (String bt : listaBts) {
							// obtenemos los nombres de los ficheros de salida a
							// generar
							String[] daos = getFicherosSalida();
							for (int i = 0; i < daos.length; i++) {
								String nombref = getNombreF(daos[i]);
								ExtraerFicheroDTO extraccion = new ExtraerFicheroDTO();
								// ruta donde se generá el fichero
								extraccion.setDir(ruta);
								// canal
								extraccion.setCanal(canal);
								// negocio
								extraccion.setNegocio(negocio);
								// tipo cierre --> ¿¿¿Necesario????
								extraccion.setTipoEjec(tipoCierre);
								// fecha de cierre
								extraccion.setFechaCierre(new java.sql.Date(fichaExtraer.getFefecto().getTime()));
								extraccion.setBt(bt);
								// formamos el nombre del fichero a generar
								String fefecto = dfAAAAMMDD.format(fichaExtraer.getFefecto());
								String nombreFichero = getNombreFichero(fichaExtraer, fefecto, bt, nombref);
								extraccion.setNombreFichero(nombreFichero);
								// Asignamos permisos de escritura al fichero
								File ficheroSalida = new File(rutaAbsoluta + File.separator + nombreFichero);
								ficheroSalida.setWritable(true, false);
								
								// indica si la carga es de MAESBTC o FLUJOSTOT
								// o INCIDENCIAS para saber posteriormente a que
								// procedimiento almacenado invocar
								extraccion.setTipoFichero(SolvenciaUtils.getTipoFichero(nombref));
								listaFicherosExtraccion.add(extraccion);
							}

						}

					}
				}
				// escribir un log con el número de ficheros extraidos en BBDD
				numeroFicherosExtraer += listaFicherosExtraccion.size();
				Map<TipoFichero, List<ExtraerFicheroDTO>> mapaFicheros = getMapaTiposFicheros(listaFicherosExtraccion);
				SolvenciaUtilsExtraerBBDD.ejecutarProcedimientoExtraer(mapaFicheros);
				SolvenciaUtils.escribeLog(SolvenciaUtils.creaRegistroFichaResultado(fichaExtraer,
						"Número de ficheros extraidos de BBDD : " + listaFicherosExtraccion.size()), fichasResultado);
			} else {
				logger.info("No se extraen ficheros ya que es una simulación");
				SolvenciaUtils.escribeLog(SolvenciaUtils.creaRegistroFichaResultado(fichaExtraer,
						"No se extraen ficheros ya que es una simulación"), fichasResultado);
			}

		}
		// por cada fichero extracción generado --> invocar a su procedimiento
		// almacenado
		logger.info("Número de ficheros extraidos de BBDD : {}", numeroFicherosExtraer);
		logger.info("Fin procesamiento extracción.");
	}

	private static Map<TipoFichero, List<ExtraerFicheroDTO>> getMapaTiposFicheros(
			List<ExtraerFicheroDTO> listaFicherosExtraccion) {
		Map<TipoFichero, List<ExtraerFicheroDTO>> mapaFicheros = new HashMap<TipoFichero, List<ExtraerFicheroDTO>>();
		for (ExtraerFicheroDTO extraerFicheroDTO : listaFicherosExtraccion) {
			List<ExtraerFicheroDTO> list = mapaFicheros.get(extraerFicheroDTO.getTipoFichero());
			if (list == null) {
				list = new ArrayList<ExtraerFicheroDTO>();
			}
			list.add(extraerFicheroDTO);
			mapaFicheros.put(extraerFicheroDTO.getTipoFichero(), list);
		}

		return mapaFicheros;
	}

	/**
	 * Método encargada de obtener las fichas de proceso que se van a enviar al
	 * proceso de extracción de ficheros
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<FichaProceso> cargarFichasProcesoLog() throws IOException {
		return cargarFichasProcesoLog(Constantes.STREAM_FICHAS, Constantes.STREAM_FICHAS);
	}

	/**
	 * Método encargada de obtener las fichas de proceso que se van a enviar al
	 * proceso de purgado de BBDD
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<FichaProceso> cargarFichasProcesoLogPurgado() throws IOException {
		return cargarFichasProcesoLog(Constantes.STREAM_FICHAS_PURGADO, Constantes.STREAM_FICHAS);
	}

	public static List<FichaProceso> cargarFichasProcesoLog(String codigoFichasProceso, String streamName)
			throws IOException {
		logger.info("Cargando fichas proceso...");
		List<FichaProceso> fichasProceso = new ArrayList<FichaProceso>();
		String filePath = SolvenciaUtils.getRutaCierre(true)
				+ SolvenciaUtils.getRutaRelativaFichero(codigoFichasProceso);
		BeanIOReader in = new BeanIOReader(Constantes.BEANIO_CONFIG_XML, null, null);
		String error = "Error cargando fichas de procesos";
		try {
			in.createReader(filePath, streamName);
			FichaProceso ficha = null;
			while ((ficha = (FichaProceso) in.read()) != null) {
				fichasProceso.add(ficha);
			}
			// cada canal y negocio, acceder a su carpeta correspondiente y
			// buscar los ficheros generados

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
		logger.info("Se han cargado {} fichas proceso.", fichasProceso.size());
		return fichasProceso;
	}

	/***
	 * 
	 * Métodos privados
	 * 
	 * 
	 ***/

	private static String getNombreFichero(FichaProceso ficha, String fechaCierre, String bt, String nombref) {
		String nombreFichero = new StringBuilder().append(ficha.getKejecucion()).append(Constantes.GUION_BAJO)
				.append(ficha.getKsistema()).append(Constantes.GUION_BAJO).append(ficha.getKprotecnico())
				.append(Constantes.GUION_BAJO).append(ficha.getKuejecucion()).append(Constantes.GUION_BAJO)
				.append(ficha.getKsecuencia()).append(Constantes.GUION_BAJO).append(fechaCierre)
				.append(Constantes.GUION_BAJO).append(ficha.getCnegocio()).append(Constantes.GUION_BAJO)
				.append(ficha.getCcanal()).append(Constantes.GUION_BAJO).append(bt).append(Constantes.GUION_BAJO)
				.append(nombref).append(Constantes.GUION_BAJO).append(".").append(Constantes.FORMATO_SALIDA_FICHERO)
				.toString();
		return nombreFichero;

	}

	private static List<String> getBts(String ctipobt) {
		List<String> listaBts = btUtils.getBts(ctipobt);

		if (listaBts == null || listaBts.isEmpty()) {
			// TODO: Gestionar esta excepción
			logger.error("No existen bases técnicas para el ctipobt " + ctipobt);
			throw new SolvenciaRuntimeException("No existen bases técnicas para el ctipobt " + ctipobt);
		}
		return listaBts;
	}

	private static String[] getFicherosSalida() {
		String[] daos = btUtils.getCargaFicherosProperty("salida").split(",");
		if (daos == null || daos.length == 0) {
			// TODO: Gestionar esta excepción
			logger.error("No están configurados los ficheros de salida.");
			throw new SolvenciaRuntimeException("No están configurados los ficheros de salida.");
		}
		return daos;
	}

	private static String getNombreF(String nombre) {
		String nombref = btUtils.getCargaFicherosProperty(nombre);
		if (nombref == null || StringUtils.EMPTY.equals(nombref)) {
			// TODO: Gestionar esta excepción
			logger.error("No está configurado el nombre del fichero con clave: {}", nombre);
			throw new SolvenciaRuntimeException("No está configurado el nombre del fichero con clave: " + nombre);
		}
		return nombref;
	}

	private static String getRutaBotes() {
		String ruta = btUtils.getCargaFicherosProperty(ConstantesSolvencia.RUTA_BOTES);
		if (ruta == null || StringUtils.EMPTY.equals(ruta)) {
			logger.error("No está configurado el nombre del fichero con clave: {}", ConstantesSolvencia.RUTA_BOTES);
			throw new SolvenciaRuntimeException("No está configurado el nombre del fichero con clave: "
					+ ConstantesSolvencia.RUTA_BOTES);
		}
		return ruta;
	}

	public static void recuperarBoteDeIncidencias(List<FichaProceso> listaFichasProceso,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		// Por cada ficha, buscamos los ficheros de incidencias y los
		// movemos a la cartera de bote.
		for (FichaProceso fichaExtraer : listaFichasProceso) {
			if (ConstantesSolvencia.TIPO_EJECUCION_CIERRE_NO_SIMULADO.equals(fichaExtraer.getCtipoejec())) {
				Integer canal = fichaExtraer.getCcanal();
				String negocio = fichaExtraer.getCnegocio();
				String ctipobt = fichaExtraer.getCtipobt();
				// obtenemos la ruta donde se almacenarán los ficheros
				// de resultados
				String ruta = SolvenciaUtils.construirRutaResultados(canal, negocio, Boolean.FALSE);
				File rutaDirectorioResultados = new File(ruta);
				File[] ficherosIncidencias = rutaDirectorioResultados.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return name.contains("INCIDENCIAS") && name.endsWith(Constantes.FORMATO_FICHERO_TXT);
					}
				});

				String rutaBote = SolvenciaUtils.construirRutaBotes(canal, negocio, Boolean.FALSE);
				File ficheroBote = new File(rutaBote);
				ficheroBote.mkdirs();
				if (ficheroBote.exists()) {
					ficheroBote.delete();
				}
				if (ficherosIncidencias != null && ficherosIncidencias.length > 0) {
					mergeFiles(ficherosIncidencias, ficheroBote);
				} else {
					logger.info("No hay ficheros de incidencias que mezclar");
				}
			}
		}

	}

	public static void mergeFiles(File[] files, File mergedFile) {

		if (files != null && files.length > 0) {
			FileWriterWithEncoding fstream = null;
			BufferedWriter out = null;
			try {
				fstream = new FileWriterWithEncoding(mergedFile, Constantes.FILE_CHARSET, true);
				out = new BufferedWriter(fstream);
	
				for (File f : files) {
					FileInputStream fis = null;
					BufferedReader in = null;
					try {
						fis = new FileInputStream(f);
						in = new BufferedReader(new InputStreamReader(fis, Constantes.FILE_CHARSET));
	
						String aLine;
						while ((aLine = in.readLine()) != null) {
							out.write(aLine);
							out.newLine();
						}
	
					} catch (IOException e) {
						logger.error("Error escribiendo fichero", e);
					} finally {
						if (in != null) {
							try {
								in.close();
							} catch (IOException e) {
							}
						}
						if (fis != null) {
							try {
								fis.close();
							} catch (IOException e) {
							}
						}
					}
				}
	
			} catch (IOException e1) {
				logger.error("Error abriendo fichero", e1);
			} finally {
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						logger.error("Error cerrando fichero", e);
					}
				}
			}
		} else {
			logger.warn("No hay ficheros que mergear en {}", mergedFile.getAbsolutePath());
		}
	}

	public static Boolean getHayError() {
		return hayError;
	}

	public static void setHayError(Boolean hayError) {
		SolvenciaUtilsExtraerBBDD.hayError = hayError;
	}
}
