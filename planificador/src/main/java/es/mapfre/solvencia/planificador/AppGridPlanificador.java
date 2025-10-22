
package es.mapfre.solvencia.planificador;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.UUIDBasedIdentifier;
import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.SubmissionState;
import com.oracle.coherence.patterns.processing.internal.DefaultProcessingSession;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;
import com.oracle.coherence.patterns.processing.task.ResumableTask;
import com.tangosol.coherence.component.net.MemberSet;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.CacheService;
import com.tangosol.net.Cluster;
import com.tangosol.net.Member;
import com.tangosol.net.NamedCache;
import com.tangosol.net.PartitionedService;

import es.mapfre.solvencia.coherence.agent.FreeMemAgent;
import es.mapfre.solvencia.coherence.jmx.MonitorizacionConstants;
import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.FichaResultadoDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.entregables.motor.EjecutorEntregables;
import es.mapfre.solvencia.entregables.motor.ResumableEntregables;
import es.mapfre.solvencia.entregables.processors.CalculaPtipo;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.ficheros.CargaDatosDividios;
import es.mapfre.solvencia.ficheros.ExportaDatos;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.motor.ProgresoCalculo;
import es.mapfre.solvencia.motor.ResumableMotor;
import es.mapfre.solvencia.planificador.coherence.MemberEventListener;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOWriter;

public class AppGridPlanificador {

	private static final int TASK_TIMEOUT = 30*60*1000;

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config-out.xml";

	private static Logger log = LoggerFactory.getLogger(AppGridPlanificador.class);

	// -Dsolvencia.fecha.cierre
	// -Dsolvencia.ruta.base

	private static String rutaFichaResultados = null;

	public static void main(String[] args) throws Throwable {
		log.warn("START MAIN APP - Arrancando Planificador del Gestor de Proyecciones...");

		try {
			String[] jars = new File(
					AppGridPlanificador.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath())
							.getParentFile().list(new FilenameFilter() {
								@Override
								public boolean accept(File dir, String name) {
									return name != null && name.endsWith(".jar");
								}
							});

			log.info("Librerías empleadas por la aplicación: {}", Arrays.asList(jars));
		} catch (Exception e) {
			log.error("Error obteniendo librerías empleadas por la aplicación", e);
		}

		Calendar comienzoMotorCalculo = GregorianCalendar.getInstance();
		NamedCache cacheMonitorizacion = CacheFactory.getCache("monitorizacionCache");
		cacheMonitorizacion.clear();
		cacheMonitorizacion.put(MonitorizacionConstants.HORA_COMIENZO, comienzoMotorCalculo.getTime());
		cacheMonitorizacion.put(MonitorizacionConstants.PROCESO_ACTUAL, "Arrancando motor...");

		IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		String fecCierre = System.getProperty("solvencia.fecha.cierre");

		String rutaBase = "";

		if (System.getProperty("solvencia.ruta.base") != null) {
			rutaBase = System.getProperty("solvencia.ruta.base");
		}

		BtUtils btUtils = new BtUtils();
		String streamNameFichaSalida = btUtils.getCargaFicherosProperty("fichaSalida");
		String nombreFicheroFichaSalida = btUtils.getCargaFicherosProperty(streamNameFichaSalida);
		rutaFichaResultados = rutaBase.concat(File.separator).concat(fecCierre).concat(File.separator)
				.concat(nombreFicheroFichaSalida);

		// Tenemos la ruta al fichero de resultados. Tenemos que actualizarlo
		// periódicamente por lo que levantamos un hilo
		Thread threadLogUpdater = new Thread(new LogUpdater(rutaFichaResultados));
		;
		threadLogUpdater.start();

		// TODO: Escribir la fecha de comienzo en el log

		List<FichaProceso> fichas = null;
		Map<Date, FichaProceso> fichasTerminadas = new HashMap<Date, FichaProceso>();

		DateFormat dfHH24mmss = new SimpleDateFormat(" HH:mm:ss");

		try {
			if (log.isInfoEnabled()) {
				log.info("___________________________Inicio carga ficheros  ________________________________");
			}
			cacheMonitorizacion.put(MonitorizacionConstants.PROCESO_ACTUAL, "Cargando Ficheros");

			fichas = new CargaDatosDividios(almacenarDatos).cargaDatos(fecCierre, rutaBase);
			if (log.isInfoEnabled()) {
				log.info("___________________________Fin carga ficheros  ________________________________");
			}
			Date horaFinCargaFicheros = new Date();

			UmicDao umicDao = (UmicDao) FactoriaDao.getDao("X880JI01");
			cacheMonitorizacion.put(MonitorizacionConstants.NUM_FICHAS, fichas.size());
			cacheMonitorizacion.put(MonitorizacionConstants.NUM_FICHAS_PROCESADAS, 0);

			if (log.isInfoEnabled()) {
				log.info("___________________________Inicio cálculo PTIPO auto  ________________________________");
			}
			// Constante de Extracción de Ptipo automático
			Integer cteExtr = UtilModulos.getCteExtr();
			if (cteExtr != 0) {
				CalculaPtipo.calcular(cteExtr);
			}
			if (log.isInfoEnabled()) {
				log.info("___________________________Fin cálculo PTIPO auto  ________________________________");
			}

			for (FichaProceso fichaProceso : fichas) {
				try {
					Date fechaInicioFicha = new Date();
					cacheMonitorizacion.put(MonitorizacionConstants.PROCESO_ACTUAL, "Efectuando Cálculos");
					cacheMonitorizacion.put(MonitorizacionConstants.HORA_COMIENZO_FICHA_EJECUCION, fechaInicioFicha);
					cacheMonitorizacion.put(MonitorizacionConstants.FICHA_EJECUCION, fichaProceso.toString());
					cacheMonitorizacion.put(MonitorizacionConstants.UMIC_PROCESADAS_FICHA, Integer.valueOf(0));

					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.MENSAJE_FIN_CARGA_FICHEROS + dfHH24mmss.format(horaFinCargaFicheros));
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.MENSAJE_INICIO_PROCESO + dfHH24mmss.format(fechaInicioFicha));

					// Se saca el mensaje de inicio en cualquier caso para que
					// quede constancia
					escribirFichaResultados(almacenarDatos, streamNameFichaSalida, null);

					// Desmarcamos todas las UMIC procesadas en anteriores
					// fichas
					umicDao.desmarcarProcesadas();

					if (log.isDebugEnabled()) {
						log.debug("NEGOCIO = {}  CANAL = {}", fichaProceso.getCnegocio(), fichaProceso.getCcanal());
					}

					if (log.isInfoEnabled()) {
						log.info(
								"____________________________Inicio Ejecución de programas  ________________________________");
					}
					AppGridPlanificador.execute(fichaProceso);
					if (log.isInfoEnabled()) {
						log.info(
								"____________________________Fin Ejecución de programas  ________________________________");
					}

					Date fechaFinFicha = new Date();
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.MENSAJE_FIN_PROCESAMIENTO_UMIC + dfHH24mmss.format(fechaFinFicha));
					// Nodos empleados
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_NODOS_EMPLEADOS
									+ ((PartitionedService) cacheMonitorizacion.getCacheService())
											.getOwnershipEnabledMembers().size());
					// Procesados
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_PROCESADAS);
					// Procesados por segundo
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_PROCESADAS_SEGUNDO
									+ new DecimalFormat("#.##").format((1000.0
											* ((Integer) cacheMonitorizacion
													.get(MonitorizacionConstants.UMIC_PROCESADAS_FICHA))
											/ (fechaFinFicha.getTime() - fechaInicioFicha.getTime()))));
					// Errores
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_ERROR);
					// Avisos
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_AVISO);
					// Tipos de Errores
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							ConstantesSolvencia.TEXTO_FICHA_RESULTADO_SUBERROR);

					if (log.isInfoEnabled()) {
						log.info(
								"____________________________Inicio cálculo de entregables  ________________________________");
					}
//					AppGridPlanificador.calculoEntregables(fichaProceso);
					
					EjecutorEntregables ejecutor = new EjecutorEntregables(fichaProceso);
					ejecutor.lanzarEntregables();
					
					if (log.isInfoEnabled()) {
						log.info(
								"____________________________Fin cálculo de entregables  ________________________________");
					}

					if (log.isInfoEnabled()) {
						log.info(
								"___________________________Inicio exporta ficheros  ________________________________");
					}
					cacheMonitorizacion.put(MonitorizacionConstants.PROCESO_ACTUAL, "Exportando Resultados a Fichero");
					new ExportaDatos().escribirResultados(fichaProceso, rutaBase + File.separator + fecCierre,
							Boolean.TRUE);
					if (log.isInfoEnabled()) {
						log.info("___________________________Fin exporta ficheros  ________________________________");
					}

				} catch (Exception e) {
					log.error("Error ejecutando ficha {}", fichaProceso, e);

					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							"Error ejecutando ficha " + e.getLocalizedMessage());
				}

				Date fechaFinFicha = new java.util.Date();
				fichasTerminadas.put(fechaFinFicha, fichaProceso);

				// Calculamos el tiempo que se ha tardado y lo que nos queda por
				// ejecutar (estimado)
				Double avanceFichas = ((double) fichasTerminadas.size()) / ((double) fichas.size());
				long elapsedTimeMilis = fechaFinFicha.getTime() - comienzoMotorCalculo.getTimeInMillis();

				long finalMilis = Double.valueOf(elapsedTimeMilis / avanceFichas).longValue();
				Calendar finalEstimado = GregorianCalendar.getInstance();
				finalEstimado.setTimeInMillis(comienzoMotorCalculo.getTimeInMillis() + finalMilis);
				log.info("Procesadas {} fichas de {}. Hora estimada de fin: {}", fichasTerminadas.size(), fichas.size(),
						dfHH24mmss.format(finalEstimado.getTime()));
				cacheMonitorizacion.put(MonitorizacionConstants.NUM_FICHAS_PROCESADAS, fichasTerminadas.size());
				cacheMonitorizacion.put(MonitorizacionConstants.HORA_FIN_ESTIMADA, finalEstimado.getTime());
				cacheMonitorizacion.put(MonitorizacionConstants.FICHA_EJECUCION, null);

			}

		} catch (Throwable e) {
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion("00", null, e);

			s2e.getIncidencia().setFecCierre(new Timestamp(new SimpleDateFormat("yyyyMM").parse(fecCierre).getTime()));
			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());
			if (fichas != null) {
				for (FichaProceso fichaProceso : fichas) {
					almacenarDatos.agregarRegistroFichaResultado(fichaProceso,
							"Error ejecutando ficha " + e.getLocalizedMessage());
				}
			}

			log.error("Error no controlado ", e);
		}

		// Se saca el mensaje de fin en cualquier caso, haya ido bien o mal la
		// ejecución.
		try {
			escribirFichaResultados(almacenarDatos, streamNameFichaSalida, fichasTerminadas);
		} catch (Exception e) {
			log.error("Error escribiendo fichas de resultado", e.getMessage());
		}

		threadLogUpdater.interrupt();

		cacheMonitorizacion.put(MonitorizacionConstants.PROCESO_ACTUAL, ConstantesSolvencia.MENSAJE_FIN_PROCESO);
		log.warn("END - " + ConstantesSolvencia.MENSAJE_FIN_PROCESO);

		System.exit(0);
	}

	/**
	 * @param almacenarDatos
	 * @param streamNameFichaSalida
	 * @param fichasTerminadas
	 * @throws IOException
	 */
	public static void escribirFichaResultados(IAlmacenarDatos almacenarDatos, String streamNameFichaSalida,
			Map<Date, FichaProceso> fichasTerminadas) throws IOException {
		if (streamNameFichaSalida != null && rutaFichaResultados != null) {
			FichaResultadoDao fichaResultadoDao = (FichaResultadoDao) FactoriaDao.getDao(streamNameFichaSalida);

			if (fichasTerminadas != null) {
				almacenarDatos.agregarLineaFinFichasResultado(fichasTerminadas);
			}

			BeanIOWriter writer = new BeanIOWriter(BEANIO_CONFIG_XML, rutaFichaResultados, streamNameFichaSalida);

			fichaResultadoDao.exportCache(writer);

			writer.flush();
			writer.close();

		}
	}

	private static void execute(FichaProceso ficha) throws Throwable {
		// Limpiamos el heap
		FreeMemAgent.freeMem();
		
		Dao umicDao = FactoriaDao.getDao("X880JI01");
		CacheService umicCacheService = umicDao.getCache().getCacheService();
		Member localMember = umicCacheService.getCluster().getLocalMember();
		
		ProcessingSession session = null;
		session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());

		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);

		Map<String, SubmissionOutcome> mapSubmissionOutcomes = new HashMap<String, SubmissionOutcome>();

		// Registramos un listener para atender eventos de nuevos miembros
		MemberEventListener memberEventListener = new MemberEventListener();
		memberEventListener.setSubmissionOutcomes(mapSubmissionOutcomes);
		memberEventListener.setFichaProceso(ficha);
		memberEventListener.setSession(session);
		memberEventListener.setSubmissionConfiguration(submissionConfiguration);
		umicCacheService.addMemberListener(memberEventListener);

		// Obtenemos los miembros que tienen particiones de UMIC
		Set<Member> members = (Set<Member>) ((PartitionedService) umicCacheService).getOwnershipEnabledMembers();
		synchronized (mapSubmissionOutcomes) {
			for (Member member : members) {
				// Si el miembro no es el local...
				if (!member.equals(localMember)) {
					// Creamos una tarea para él
					ResumableTask tarea = new ResumableMotor(ficha);
					String nombreTarea = member.getMachineName() + StringUtils.SPACE + member.getMemberName();
					mapSubmissionOutcomes.put(nombreTarea,
							session.submit(tarea, submissionConfiguration, new SubmissionCallback(nombreTarea)));
				} else {
					if (log.isDebugEnabled()) {
						log.debug("****  este nodo no ejecuta" + member.getId() + "  *****");
					}
				}
			}
		}

		AppGridPlanificador.controlaTareas(session, mapSubmissionOutcomes, memberEventListener);
	}


	private static void controlaTareas(ProcessingSession session, Map<String, SubmissionOutcome> mapSubmissionOutcomes,
			MemberEventListener memberEventListener) {
		File ficheroResultados = new File(rutaFichaResultados);

		Calendar comienzo = GregorianCalendar.getInstance();

		Boolean primeraVuelta = Boolean.TRUE;

		int finished = 0;
		int jobsSize = mapSubmissionOutcomes.size();
		while (finished < jobsSize) {
			try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
			}

			// Actualizamos la fecha de la ficha de resultados para indicar que
			// hay avance
			if (ficheroResultados != null && ficheroResultados.exists()) {
				ficheroResultados.setLastModified(System.currentTimeMillis());
			}

			// Cuando llegue al estado final, llamar al submissionOutcome.get()
			// para obtener el resultado

			synchronized (mapSubmissionOutcomes) {

				Collection<String> submissionOutcomeKeys = mapSubmissionOutcomes.keySet();
				finished = 0;
				Integer terminadas = 0;
				Integer totales = 0;
				for (String submissionOutcomeKey : submissionOutcomeKeys) {
					SubmissionOutcome submissionOutcome = mapSubmissionOutcomes.get(submissionOutcomeKey);
					if (log.isInfoEnabled() && !SubmissionState.DONE.equals(submissionOutcome.getSubmissionState())) {
						Object progress = null;
						try {
							progress = submissionOutcome.getProgress();
						} catch (Exception e) {
							log.error("Error recuperando progreso de tarea de {}", submissionOutcomeKey, e);
						}
						String progreso = "";
						if (progress != null) {
							progreso = progress.toString();
							if (progress instanceof ProgresoCalculo) {
								terminadas += ((ProgresoCalculo) progress).getTerminadas();
								totales += ((ProgresoCalculo) progress).getTotales();

								if (primeraVuelta) {
									log.info("{}: {} UMIC a tratar.", submissionOutcomeKey,
											((ProgresoCalculo) progress).getTotales());
								}
								
								if (((ProgresoCalculo) progress).getTimestamp() != null && (System.currentTimeMillis() - ((ProgresoCalculo) progress).getTimestamp() > TASK_TIMEOUT)) {
									log.warn("La tarea {} está tardando demasiado...", submissionOutcomeKey);
									if (System.currentTimeMillis() - ((ProgresoCalculo) progress).getTimestamp() > 3*TASK_TIMEOUT) {
										log.error("Cancelando tarea {} por estar atascada", submissionOutcomeKey);
										session.cancelSubmission(submissionOutcome.getIdentifier());
									}
								}
							}
						}
						log.debug("****  progreso de {} = {} *****", submissionOutcomeKey, progreso);
					} else {
						log.debug("****  progreso de {} = {} *****", submissionOutcomeKey,
								submissionOutcome.getSubmissionState());
						if (SubmissionState.DONE.equals(submissionOutcome.getSubmissionState())) {
							try {
								ProgresoCalculo progress = (ProgresoCalculo) submissionOutcome.get();
								terminadas += progress.getTerminadas() != null ? progress.getTerminadas() : 0;
								totales += progress.getTotales() != null ? progress.getTotales() : 0;

								if (primeraVuelta) {
									log.info("{}: {} UMIC a tratar.", submissionOutcomeKey,
											((ProgresoCalculo) progress).getTotales());
								}
							} catch (Exception e) {
								log.error("Error recuperando progreso de tarea de {}", submissionOutcome, e);
							}
						}
					}

					if (submissionOutcome.isFinalState()) {
						finished++;
					}
				}

				// Estimamos la hora final de la ficha
				Integer pendientes = totales - terminadas;
				Double avance = 0.0;
				NamedCache cacheMonitorizacion = CacheFactory.getCache("monitorizacionCache");
				if (pendientes > 0) {
					if (terminadas > 0) {
						avance = ((double) (terminadas * 100)) / totales;
						long ahora = System.currentTimeMillis();
						long elapsed = ahora - comienzo.getTimeInMillis();
						long finalMilis = Double.valueOf(100 * elapsed / avance).longValue();
						Calendar finalEstimadoFicha = GregorianCalendar.getInstance();
						Long milisFinal = comienzo.getTimeInMillis() + finalMilis;
						finalEstimadoFicha.setTimeInMillis(milisFinal);
						log.info("Pendientes {} UMIC. Avance {}%. {}/{} motores. Hora estimada fin ficha: {}.",
								pendientes, String.format("%.2f", avance), finished, jobsSize,
								finalEstimadoFicha.getTime());
						cacheMonitorizacion.put(MonitorizacionConstants.HORA_FIN_ESTIMADA_FICHA_EJECUCION,
								finalEstimadoFicha.getTime());

						// Estimamos la hora final definitiva
						Integer numFichas = (Integer) cacheMonitorizacion.get(MonitorizacionConstants.NUM_FICHAS);
						Integer numFichasProcesadas = (Integer) cacheMonitorizacion
								.get(MonitorizacionConstants.NUM_FICHAS_PROCESADAS);
						Date comienzoMotor = (Date) cacheMonitorizacion.get(MonitorizacionConstants.HORA_COMIENZO);
						Long tiempoMedioFichaEstimado = (milisFinal - comienzoMotor.getTime()) / (numFichasProcesadas + 1);
						Calendar finalEstimado = GregorianCalendar.getInstance();

						if (comienzoMotor == null) {
							log.warn("Variable comienzoMotor es null");
							comienzoMotor = new Date();
						}

						if (numFichas == null) {
							log.warn("Variable numFichas es null");
							numFichas = 1;
						}

						if (tiempoMedioFichaEstimado == null) {
							log.warn("Variable tiempoMedioFichaEstimado es null");
							tiempoMedioFichaEstimado = 1L;
						}

						if (finalEstimado == null) {
							log.warn("Variable finalEstimado es null");
							finalEstimado = GregorianCalendar.getInstance();
						}

						finalEstimado.setTimeInMillis(comienzoMotor.getTime() + (numFichas * tiempoMedioFichaEstimado));
						cacheMonitorizacion.put(MonitorizacionConstants.HORA_FIN_ESTIMADA, finalEstimado.getTime());
					}
				}
				cacheMonitorizacion.put(MonitorizacionConstants.AVANCE_FICHA, avance);
				cacheMonitorizacion.put(MonitorizacionConstants.UMIC_PENDIENTE_FICHA, pendientes);
				cacheMonitorizacion.put(MonitorizacionConstants.UMIC_PROCESADAS_FICHA, terminadas);

				// TODO Actualizamos jobsSize por si hay nuevos nodos en el
				// cluster
				// jobsSize = mapSubmissionOutcomes.size();
				primeraVuelta = Boolean.FALSE;
			}
		}

		// Quitamos el listener ya que hemos terminado
		Dao umicDao = FactoriaDao.getDao("X880JI01");
		CacheService umicCacheService = umicDao.getCache().getCacheService();
		memberEventListener.setActive(Boolean.FALSE);
		umicCacheService.removeMemberListener(memberEventListener);
	}

	@Deprecated
	private static void calculoEntregables(FichaProceso fichaProceso) throws Throwable {

		// Obtener miembro (que no sea el local) donde va a ejecutarse la tarea
		// de generación de entregables
		Cluster cluster = CacheFactory.getCluster();
		MemberSet members = (MemberSet) cluster.getMemberSet();

		Member localMember = cluster.getLocalMember();
		Member member = members.getOtherMembers((com.tangosol.coherence.component.net.Member) localMember).getMember(1);

		ProcessingSession session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());

		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		attrMap.put("task", "entregables");
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);

		ResumableTask tarea = new ResumableEntregables(fichaProceso);
		String nombreTarea = member.getMachineName() + StringUtils.SPACE + member.getMemberName();

		SubmissionOutcome outcome = session.submit(tarea, submissionConfiguration, new SubmissionCallback(nombreTarea));

		// Esperar a que termine la tarea de entregables para seguir con el
		// proceso del planificador
		boolean finished = Boolean.FALSE;
		while (!finished) {
			try {

				Thread.sleep(15000);
				
			} catch (InterruptedException e) {
			}
			if (outcome.isFinalState()) {
				finished = Boolean.TRUE;
			}
		}
	}

}
