/**
 * 
 */
package es.mapfre.solvencia.open.cargar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.FichaResultadoKey;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.FichaResultado;
import es.mapfre.solvencia.open.comun.Constantes;
import es.mapfre.solvencia.open.comun.SolvenciaUtils;
import es.mapfre.solvencia.open.dao.ProceduresDAO;
import es.mapfre.solvencia.open.dao.cargar.ProceduresCargarDAO;
import es.mapfre.solvencia.open.dto.CargarFicheroDTO;
import es.mapfre.solvencia.open.enums.TipoFichero;
import es.mapfre.solvencia.open.exception.SolvenciaRuntimeException;

/**
 * @author amdepedro
 * 
 */
public final class SolvenciaUtilsCargaBBDD {

	public static final int SIZE_FICHEROS_GZ = 3;
	public static final int SIZE_FICHEROS_TXT = 2;

	public static final int BYTES = 1024;

	private static Logger logger = LoggerFactory.getLogger(SolvenciaUtilsCargaBBDD.class);

	private static ExecutorService threadPool = Executors.newFixedThreadPool(Constantes.PARALLEL_THREADS);

	private static Boolean hayError = Boolean.FALSE;

	public static void shutdown() {
		if (threadPool != null) {
			threadPool.shutdown();
		}
	}


	/**
	 * 
	 */
	private SolvenciaUtilsCargaBBDD() {

	}


	private static Map<TipoFichero, List<CargarFicheroDTO>> getMapaTiposFicheros(List<CargarFicheroDTO> listaParams) {
		Map<TipoFichero, List<CargarFicheroDTO>> mapaFicheros = new HashMap<TipoFichero, List<CargarFicheroDTO>>();
		for (CargarFicheroDTO cargarFicheroDTO : listaParams) {
			List<CargarFicheroDTO> list = mapaFicheros.get(cargarFicheroDTO.getTipoFichero());
			if (list == null) {
				list = new ArrayList<CargarFicheroDTO>();
			}
			list.add(cargarFicheroDTO);
			mapaFicheros.put(cargarFicheroDTO.getTipoFichero(), list);
		}

		return mapaFicheros;
	}

	/**
	 * Método encargado de la invocacion a los procedimientos almacenados de
	 * carga para una lista de ficheros pasados como parámetro.
	 * 
	 * @param listaFicherosCargar
	 */
	public static void ejecutarProcedimientoCarga(Map<TipoFichero, List<CargarFicheroDTO>> mapFicherosCargar, Boolean isCierre) {
		List<CargarFicheroDTO> listaFicherosCargar = new ArrayList<CargarFicheroDTO>();
		if (mapFicherosCargar == null || mapFicherosCargar.isEmpty()) {
			logger.debug("No existen ficheros a cargar en BBDD.");
		} else {

			boolean isParalelismo = SolvenciaUtils.isParalelismo();
			if (isParalelismo) {
				logger.debug("Paralelismo habilitado.");
				Collection<List<CargarFicheroDTO>> listaFicheros = mapFicherosCargar.values();
				for (List<CargarFicheroDTO> list : listaFicheros) {
					listaFicherosCargar.addAll(list);
				}
				logger.debug("Existen en total " + listaFicherosCargar.size() + " ficheros a cargar.");
	
	
				// usar paralelismo
				ExecutorService executor = null;
				try {
					executor = Executors.newFixedThreadPool(listaFicherosCargar.size() * 2);
					for (Map.Entry<TipoFichero, List<CargarFicheroDTO>> entry : mapFicherosCargar.entrySet()) {
						// ejecutarProcedimiento(entry.getKey(), entry.getValue());
						executor.execute(new EjecutorCarga(entry.getKey(), entry.getValue(), isCierre));
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
					if (executor != null) {
						executor.shutdown();
					}
				}
			} else {
				logger.debug("Paralelismo deshabilitado. Cargamos {} tipos de fichero.", mapFicherosCargar.size());
				// no usar paralelismo
				/*
				 * for (CargarFicheroDTO solvenciaParams : listaFicherosCargar) {
				 * ejecutarProcedimiento(solvenciaParams); }
				 */
				for (Map.Entry<TipoFichero, List<CargarFicheroDTO>> entry : mapFicherosCargar.entrySet()) {
					ejecutarProcedimiento(entry.getKey(), entry.getValue(), isCierre);
				}
			}
		}
	}

	/**
	 * Método encargado de la invocacion a los procedimientos almacenados de
	 * carga para una lista de ficheros pasados como parámetro.
	 * 
	 * @param params
	 *            fichero a cargar
	 */
	public static void ejecutarProcedimiento(TipoFichero tipoFichero, List<CargarFicheroDTO> params, Boolean isCierre) {
		if (params == null) {
			logger.debug("No existe fichero a cargar.");
		} else {
			logger.debug("Se van a cargar : " + params.size() + " de tipo " + tipoFichero);
		}
		try {
			ProceduresCargarDAO.callOracleStoredProcedures(tipoFichero, params, isCierre);
		} catch (Exception e) {
			logger.error("Error ejecutarProcedimiento " + e.getMessage());
			setHayError(Boolean.TRUE);
		}
	}

	public static void procesarFicherosCargar(List<FichaProceso> listaFichasProceso,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		logger.info("Procesando ficheros cargar...");
		boolean isCierre = true;
		Map<TipoFichero, List<CargarFicheroDTO>> mapaFicheros = new HashMap<TipoFichero, List<CargarFicheroDTO>>();
		for (FichaProceso fichaCargar : listaFichasProceso) {
			Boolean isSimulacion = ConstantesSolvencia.TIPO_EJECUCION_SIMULACION.equals(fichaCargar.getCtipoejec());
		
			String fechaCierre = SolvenciaUtils.getFechaCierre();
			DateFormat dfAAAAMM = new SimpleDateFormat("yyyyMM");
			// recorremos la fichas de proceso
			String fecEfecto = dfAAAAMM.format(fichaCargar.getFefecto());
			// comprobamos que la fecha de efecto de la ficha corresponde
			// con la fecha de cierre configuradas
			if (fecEfecto.equals(fechaCierre)) {
				String tipoEjecucion = null;
				if (fichaCargar.getRegistroParametros() != null) {
					tipoEjecucion = fichaCargar.getRegistroParametros().getIndicadorTipoProceso();
				} else {
					logger.warn("La ficha de proceso {} no contiene registro de tipo de ejecución (COMP, REPR, BOTE)", fichaCargar.getKejecucion());
					// Suponemos CIERRE (COMP)
					tipoEjecucion = ConstantesSolvencia.CTIPOEJEC_CIERRE;
				}
				Integer canal = fichaCargar.getCcanal();
				String negocio = fichaCargar.getCnegocio();
				String rutaAbsoluta = construirRutaBrutos(canal, negocio, Boolean.FALSE);
				// Creamos las rutas para los logs de la base de datos
				File dirLOG = new File(rutaAbsoluta + File.separator + "LOG");
				dirLOG.mkdirs();
				dirLOG.setWritable(true, false);
				File dirBAD = new File(rutaAbsoluta + File.separator + "BAD");
				dirBAD.mkdirs();
				dirBAD.setWritable(true, false);
				
				logger.info("Ficha proceso tipo ejecución: " + tipoEjecucion);

				// En todos los casos se cargarán los ficheros y, si es REPR
				// o BOTE cuando se termine la carga, se invocará al segundo
				// juego de procedimientos almacenados que extraerán los
				// ficheros definitivos.
				// Obtenemos ruta relativa de BRUTOSXXX para el canal dado
				String rutaRelativa = construirRutaBrutos(canal, negocio, Boolean.TRUE);
				// Cargamos los ficheros indicados según el canal y el
				// negocio
				List<CargarFicheroDTO> listaFicherosCargar = extraerFicheros(rutaAbsoluta, rutaRelativa, Constantes.NOMBRES_FICHEROS_A_CARGAR_Y_COPIAR + "|" +  Constantes.NOMBRES_FICHEROS_ENTREGABLES_A_COPIAR);

				if (ConstantesSolvencia.CTIPOEJEC_CIERRE.equals(tipoEjecucion) || isSimulacion) {
					// Si el tipo es COMP, no se extraerán los ficheros
					// recibidos ya que se copiarán de
					// BRUTOSCOL y BRUTOSIND a RESULT_COL y RESULT_IND.
					copiarFicheros(rutaAbsoluta, canal, negocio, Constantes.NOMBRES_FICHEROS_A_CARGAR_Y_COPIAR, isSimulacion);
					
					isCierre &= true;
				} else {
					copiarFicheros(rutaAbsoluta, canal, negocio, Constantes.NOMBRES_FICHEROS_A_COPIAR, isSimulacion);
					isCierre = false;
				}
				
				// En cualquier caso, hay que copiar los entregables
				copiarFicheros(rutaAbsoluta, canal, negocio, Constantes.NOMBRES_FICHEROS_ENTREGABLES_A_COPIAR, isSimulacion);
				

				if (!isSimulacion) {
					// Cargamos los ficheros correspondientes a la ficha de proceso
					Map<TipoFichero, List<CargarFicheroDTO>> mapaFicherosTemporal = getMapaTiposFicheros(listaFicherosCargar);
					for (TipoFichero tipoFichero : mapaFicherosTemporal.keySet()) {
						List<CargarFicheroDTO> lista = mapaFicheros.get(tipoFichero);
						if (lista == null) {
							lista = new ArrayList<CargarFicheroDTO>();
						}
						lista.addAll(mapaFicherosTemporal.get(tipoFichero));
						mapaFicheros.put(tipoFichero, lista);
					}
					// escribir un log con el número de ficheros cargados en BBDD
					logger.info("Número de ficheros a cargar en BBDD: {}", listaFicherosCargar.size());
					SolvenciaUtils.escribeLog(
							SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "Número de ficheros cargados en BBDD: "
									+ listaFicherosCargar.size()), fichasResultado);
				} else {
					logger.info("No se cargan ficheros ya que es una simulación");
					SolvenciaUtils.escribeLog(
							SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "No se cargan ficheros ya que es una simulación"), fichasResultado);
				}
			}
		}
		SolvenciaUtilsCargaBBDD.ejecutarProcedimientoCarga(mapaFicheros, isCierre);
	
		// Borramos los symlinks
		File symlinks = new File(SolvenciaUtils.getRutaCierre(true)+File.separator+"carga");
		try {
			FileUtils.deleteDirectory(symlinks);
		} catch (IOException e) {
			logger.warn("Problema borrando directorio temporal: {}", e.getMessage());
		}
		
		logger.info("Fin procesamiento carga.");
	}

	/***
	 * 
	 * Métodos privados
	 * 
	 * 
	 ***/


	private static List<CargarFicheroDTO> extraerFicheros(String rutaBrutos, String dir, String tiposFicherosExtraer) {
		List<CargarFicheroDTO> listaParams = new ArrayList<CargarFicheroDTO>();
		File fileBrutos = new File(rutaBrutos);
		Boolean existsDir = Boolean.FALSE;
		if (fileBrutos.exists()) {
			existsDir = Boolean.TRUE;
			logger.debug("Buscando ficheros en " + rutaBrutos + " ...");
			// Primero descomprimimos
			FiltroFicherosCarga filtroFicherosComprimidos = new FiltroFicherosCarga(tiposFicherosExtraer, Boolean.TRUE);
			File[] ficherosBrutosComprimidos = fileBrutos.listFiles(filtroFicherosComprimidos);
			List<Future> tareasDescompresion = new ArrayList<Future>();
			
			for (File ficheroBruto : ficherosBrutosComprimidos) {
				String nombre = ficheroBruto.getName();
				// quedarnos con los fichero que se llamen: MAESBTC, FLUJOSTOT e
				// INCIDENCIAS
				if (nombre.matches(tiposFicherosExtraer)) {
					tareasDescompresion.add(descomprimirFicheroSiNecesario(nombre, rutaBrutos));
				}
			}
			
			// Esperamos que termine de descomprimir
			Boolean terminado = Boolean.FALSE;
			while(!terminado) {
				terminado = Boolean.TRUE;
				for (Future tarea : tareasDescompresion) {
					terminado &= tarea.isDone();
				}
			}
			// Y luego cargamos
			FiltroFicherosCarga filtroFicherosCarga = new FiltroFicherosCarga(Constantes.NOMBRES_FICHEROS_A_CARGAR,
					Boolean.FALSE);
			File[] ficherosBrutosCarga = fileBrutos.listFiles(filtroFicherosCarga);
			for (File ficheroBrutoCarga : ficherosBrutosCarga) {
				if (ficheroBrutoCarga.exists() && ficheroBrutoCarga.length() > 0) {
					String nombre = ficheroBrutoCarga.getName();
					CargarFicheroDTO param = new CargarFicheroDTO();
					
					// indica si la carga es de MAESBTC o FLUJOSTOT o
					// INCIDENCIAS para saber posteriormente a que
					// procedimiento almacenado invocar
					TipoFichero tipoFichero = SolvenciaUtils.getTipoFichero(nombre);
					param.setTipoFichero(tipoFichero);
					// nombre del fichero
					param.setFich(nombre);

					// ruta donde se encuentra el fichero
					Path pathFicheroBrutoCarga = ficheroBrutoCarga.toPath();
					String rutaSimbolica = SolvenciaUtils.getRutaCierre(true)+File.separator+"carga"+File.separator+tipoFichero.getTipo();
					File rutaSimbolicaFile = new File(rutaSimbolica);
					File fileSimbolico = new File(rutaSimbolica+File.separator+nombre);
					Path pathFicheroBrutoCargaSymLink = fileSimbolico.toPath();
					
					// Creamos las rutas para los logs de la base de datos
					File dirLOG = new File(rutaSimbolica + File.separator + "LOG");
					dirLOG.mkdirs();
					dirLOG.setWritable(true, false);
					File dirBAD = new File(rutaSimbolica + File.separator + "BAD");
					dirBAD.mkdirs();
					dirBAD.setWritable(true, false);
					
					try {
						if (fileSimbolico.exists()) {
							fileSimbolico.delete();
						}
						rutaSimbolicaFile.mkdirs();
						Files.copy(pathFicheroBrutoCarga, pathFicheroBrutoCargaSymLink);
						param.setDir(SolvenciaUtils.getRutaCierreRelativa(true)+File.separator+"carga"+File.separator+tipoFichero);
						
					} catch (IOException e) {
						logger.warn("No se pudo crear el link simbólico a {}", rutaSimbolica, e);
						param.setDir(dir);
					}
					
					listaParams.add(param);
				}
			}
		} else {
			logger.debug("No existe el directorio  " + rutaBrutos);
		}
		if (listaParams != null && !listaParams.isEmpty()) {
			logger.debug("Se han encontrado " + listaParams.size() + " ficheros para cargar.");
		} else {
			if (existsDir) {
				logger.debug("No se han encontrado ficheros para cargar en: " + rutaBrutos);
			}
		}
		return listaParams;
	}

	private static void copiarFicheros(String rutaOrigen, Integer canal, String negocio, String tiposFicherosCargar, Boolean isSimulacion) {
		List<String> listaFicherosCopiar = new ArrayList<String>();
		File fileBrutos = new File(rutaOrigen);
		String rutaDestino = SolvenciaUtils.construirRutaResultados(canal, negocio, Boolean.FALSE);
		Boolean existsDir = Boolean.FALSE;
		if (fileBrutos.exists()) {
			existsDir = Boolean.TRUE;
			logger.debug("Buscando ficheros en {}...", rutaOrigen);
			FiltroFicherosCarga filtroFicherosCarga = new FiltroFicherosCarga(tiposFicherosCargar,
					null);
			File[] ficherosBrutos = fileBrutos.listFiles(filtroFicherosCarga);
			for (File ficheroBruto : ficherosBrutos) {
				String nombre = ficheroBruto.getName();
				// quedarnos con los fichero que se llamen: MAESBTC, FLUJOSTOT e
				// INCIDENCIAS
				if (ficheroBruto.isFile()) {
					listaFicherosCopiar.add(nombre);
				}
			}
		} else {
			logger.warn("No existe el directorio {}.", rutaOrigen);
		}
		if (listaFicherosCopiar != null && !listaFicherosCopiar.isEmpty()) {
			logger.info("Se han encontrado {} ficheros para copiar.", listaFicherosCopiar.size());
			for (String nombre : listaFicherosCopiar) {
				String nombreDestino = (isSimulacion ? ConstantesSolvencia.PREFIJO_SIMULACION : "") + nombre;
				copiarFichero(rutaOrigen, rutaDestino, nombre, nombreDestino);
			}
		} else {
			if (existsDir) {
				logger.warn("No se han encontrado ficheros para copiar en {}.", rutaOrigen);
			}
		}
	}

	private static Future descomprimirFicheroSiNecesario(String nombre, String rutaBrutos) {
		EjecutorDescomprimirFicheros tareaDescomprimir = new EjecutorDescomprimirFicheros(nombre, rutaBrutos);
		
		return threadPool.submit(tareaDescomprimir);
	}

	public static String getExtension(String nombre) {
		String[] arrayTemp = nombre.split("\\.");
		String extension = arrayTemp[arrayTemp.length - 1];

		return extension;
	}

	// CAV
	private static String construirRutaBrutos(Integer canal, String negocio, Boolean isRelativa) {
		if (canal == null || negocio == null) {
			// TODO: Gestionar esta excepción
			logger.error("Canal y negocio son necesarios para construir la ruta de brutos.");
			throw new SolvenciaRuntimeException("Canal y negocio son necesarios para construir la ruta de brutos.");
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
			ruta.append(File.separator).append(Constantes.BRUTOSCOL);
		} else if (ConstantesSolvencia.NEGOCIO_INDIVIDUAL.equals(negocio)) {
			// canal individuales --> ruta de RESULT_IND
			ruta.append(File.separator).append(Constantes.BRUTOSIND);
		} else {
			// TODO: Gestionar esta excepción
			logger.error("Negocio no reconocido.");
			throw new SolvenciaRuntimeException("Negocio no reconocido.");
		}
		return ruta.toString();
	}

	private static void copiarFichero(String rutaOrigen, String rutaDestino, String nombreFicheroOrigen, String nombreFicheroDestino) {
		String ficheroOrigen = rutaOrigen + File.separator + nombreFicheroOrigen;
		File origen = new File(ficheroOrigen);
		File destino = new File(rutaDestino);

		InputStream in = null;
		OutputStream out = null;
		try {
			if (origen.exists() && origen.isFile()) {
				in = new FileInputStream(origen);
			} else {
				throw new SolvenciaRuntimeException("No existe el fichero origen: " + ficheroOrigen);
			}
			// si el directorio no existe lo creamos
			if (!destino.exists()) {
				// crear la carpeta, si no existe crea toda la ruta necesaria
				if (destino.mkdirs()) {
					out = new FileOutputStream(destino + File.separator + nombreFicheroDestino);
				} else {
					throw new SolvenciaRuntimeException("No se puede crear el destino: " + destino);
				}
			} else {
				// si el directorio existe, copiar ficheros
				out = new FileOutputStream(destino + File.separator + nombreFicheroDestino);
			}

			byte[] buf = new byte[BYTES];
			int len;

			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}

			in.close();
			out.close();
		} catch (IOException ioe) {
			logger.info("IOException: {}", ioe.getMessage());
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			} catch (IOException ex) {
				logger.error("Error cerrando los descriptores de fichero: ", ex);
			}
		}

	}


	public static void procesarPurgadoFicheros(List<FichaProceso> listaFichasProceso,
			Map<FichaResultadoKey, List<FichaResultado>> fichasResultado) {
		logger.info("Procesando ficha proceso purgado...");
		for (FichaProceso fichaCargar : listaFichasProceso) {
			if (ConstantesSolvencia.TIPO_EJECUCION_CIERRE_NO_SIMULADO.equals(fichaCargar.getCtipoejec())) {
				String fechaCierre = SolvenciaUtils.getFechaCierre();
				DateFormat dfAAAAMM = new SimpleDateFormat("yyyyMM");
				// recorremos la fichas de proceso
				String fecEfecto = dfAAAAMM.format(fichaCargar.getFefecto());
				// comprobamos que la fecha de efecto de la ficha corresponde
				// con la fecha de cierre configuradas
				if (fecEfecto.equals(fechaCierre)) {
					// La fecha de cierre a borrar es la N meses anterior a la fechaCierre
					Calendar calFechaCierrePurgar = GregorianCalendar.getInstance();
					calFechaCierrePurgar.setTime(fichaCargar.getFefecto());
					calFechaCierrePurgar.add(Calendar.MONTH, -1 * Constantes.MESES_PURGADO);
					String fechaCierrePurgar = dfAAAAMM.format(calFechaCierrePurgar.getTime());
					String rutaCierrePurgar = SolvenciaUtils.getRutaCierre(false).concat(File.separator).concat(fechaCierrePurgar);
					
					try {
						logger.info("Borrando directorios de cierre {}...", rutaCierrePurgar);
						FileUtils.deleteDirectory(new File(rutaCierrePurgar));
						logger.info("Borrado de rutas de cierre realizado");
						SolvenciaUtils.escribeLog(
								SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "Borrado de rutas de cierre realizado"), fichasResultado);
					} catch (IOException e1) {
						logger.info("Error ejecutando procedimiento de borrado", e1);
						SolvenciaUtils.escribeLog(
								SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "Error ejecutando procedimiento de borrado"), fichasResultado);
					}

					
					try {
						logger.info("Purgado de BBDD para fecha {}...", fechaCierrePurgar);
						ProceduresDAO.purgarTablas(fechaCierrePurgar);
						// escribir un log con las tareas realizadas
						logger.info("Purgado de BBDD realizado");
						SolvenciaUtils.escribeLog(
								SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "Purgado de BBDD realizado"), fichasResultado);
					} catch (SQLException e) {
						logger.error("Error ejecutando procedimiento de purgado", e);
						SolvenciaUtils.escribeLog(
								SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "Error ejecutando procedimiento de purgado"), fichasResultado);
					}
				} else {
					logger.error("La fecha efecto {} no corresponde con la fecha de cierre {}", fecEfecto, fechaCierre);
				}
			} else {
				logger.info("No se purga ya que es una simulación");
				SolvenciaUtils.escribeLog(
						SolvenciaUtils.creaRegistroFichaResultado(fichaCargar, "No se purga ya que es una simulación"), fichasResultado);
			}
		}
		logger.info("Fin procesamiento carga.");
	}


	public static Boolean getHayError() {
		return hayError;
	}


	public static void setHayError(Boolean hayError) {
		SolvenciaUtilsCargaBBDD.hayError = hayError;
	}
	
	public void main(String[] args) {
		
	}
}
