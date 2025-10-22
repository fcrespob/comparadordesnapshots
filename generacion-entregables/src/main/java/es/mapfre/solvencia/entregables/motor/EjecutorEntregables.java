package es.mapfre.solvencia.entregables.motor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.UUIDBasedIdentifier;
import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.SubmissionState;
import com.oracle.coherence.patterns.processing.internal.DefaultProcessingSession;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.PofExtractor;

import es.mapfre.solvencia.coherence.agent.FreeMemAgent;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.DisenoProcesos;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.ElementoSubproceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.planificador.SubmissionCallback;
import es.mapfre.solvencia.servicios.IGestionarProceso;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;
import es.mapfre.solvencia.utils.BtUtils;

public class EjecutorEntregables {
	
	private static Logger LOG = LoggerFactory.getLogger(EjecutorEntregables.class);
	
	private static final String CACHE_DETALLE_CORRIENTE = "detalle-corriente";
	private static final String CACHE_TOTALES_FLUJOS = "totales-flujos";
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = "detalle-corriente-entregables";

	private static final int TASK_TIMEOUT = 30*60*1000;
	
	private final ValueExtractor btDetalleCorrienteExtractor = new PofExtractor(String.class, DetalleCorriente.IND_BT);
	private final ValueExtractor btTotalesFlujosExtractor = new PofExtractor(String.class, TotalesFlujos.IND_BT);
	private final ValueExtractor btDetalleCorrienteEntregablesExtractor = new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT);
	private final ValueExtractor swcasadoDetalleCorrienteEntregablesExtractor = new PofExtractor(String.class, DetalleCorrienteEntregables.IND_SWCASADO);
	
	private FichaProceso fichaProceso;
	private Map<String, SubmissionOutcome> mapSubmissionOutcomes;
	private ProcessingSession session;
	
	private BtUtils btUtils = new BtUtils();
	private IGestionarProceso gestionarProceso = FachadaServicios.getGestionarProceso();
	
	private List<CalcularEntregable> calcularEntregable;
	
	private List<String> entregablesBT;
	
	public EjecutorEntregables(FichaProceso fichaProceso) {
		this.fichaProceso = fichaProceso;
		this.mapSubmissionOutcomes = new HashMap<String, SubmissionOutcome>();
		this.session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());
		this.calcularEntregable = new ArrayList<CalcularEntregable>();
	}
	
	private static String rutaFichaResultados = null;
	
	
	/*
	 * Método encargado de lanzar las tareas de entregables para una ficha dada
	 */
	public void lanzarEntregables() {

		String fecCierre = System.getProperty("solvencia.fecha.cierre");
		String rutaBase = "";

		if (System.getProperty("solvencia.ruta.base") != null) {
			rutaBase = System.getProperty("solvencia.ruta.base");
		}
		
		String streamNameFichaSalida = btUtils.getCargaFicherosProperty("fichaSalida");
		String nombreFicheroFichaSalida = btUtils.getCargaFicherosProperty(streamNameFichaSalida);
		rutaFichaResultados = rutaBase.concat(File.separator).concat(fecCierre).concat(File.separator)
				.concat(nombreFicheroFichaSalida);

		// Tenemos la ruta al fichero de resultados. Tenemos que actualizarlo
		// periódicamente por lo que levantamos un hilo
		Thread threadLogUpdater = new Thread(new LogUpdater(rutaFichaResultados));
		;
		threadLogUpdater.start();
		
		//Crear indices
		getCache(CACHE_DETALLE_CORRIENTE).addIndex(btDetalleCorrienteExtractor, false, null);
		getCache(CACHE_TOTALES_FLUJOS).addIndex(btTotalesFlujosExtractor, false, null);
		getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES).addIndex(btDetalleCorrienteEntregablesExtractor, false, null);
		getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES).addIndex(swcasadoDetalleCorrienteEntregablesExtractor, false, null);
		CalcularEntregable aux = new CalcularEntregable("FLUJOSTOTP", "BTI");
		
		
		List<String> BTs = btUtils.getBts(fichaProceso.getCtipobt());

		for (String BT : BTs) {
			buscarProceso(ConstantesSolvencia.CTE_ENTREGABLES, fichaProceso, BT);
		}
		if (fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA) && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO) && fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION)) {
			calcularEntregable.add(aux);
		}
		
		LOG.info("Encontrados {} entregables. Preparando para lanzar tareas...", calcularEntregable.size());
		
		int finished = 0;
		// Limpiamos el heap
		FreeMemAgent.freeMem();
		synchronized (mapSubmissionOutcomes) {
			for (CalcularEntregable ent : calcularEntregable) {
				
				try {
					ejecutarEntregable(ent);
				} catch (Throwable e) {
					LOG.error("Error lanzando entregable {}-{}", ent.getNombreEntregable(), ent.getKbasetec());
				}

			}
		}
		
		int jobsSize = mapSubmissionOutcomes.size();

			List<String> jobsFinished = new ArrayList<String>();

			Collection<String> submissionOutcomesKeys = mapSubmissionOutcomes.keySet();


			while (finished < jobsSize) {
				synchronized (mapSubmissionOutcomes) {


					for (String submissionOutcomesKey : submissionOutcomesKeys) {
						SubmissionOutcome sOutcome = mapSubmissionOutcomes.get(submissionOutcomesKey);

						if (!SubmissionState.DONE.equals(sOutcome.getSubmissionState())) {
							Object progress = null;
							try {
								progress = sOutcome.getProgress();
							} catch (Exception e) {
								LOG.error("Error recuperando progreso de tarea de {}", submissionOutcomesKey, e);
								finished++;
								continue;
							}
							String progreso = "";
							if (progress != null) {
								progreso = progress.toString();
								if (progress instanceof ProgresoEntregables) {
									if (((ProgresoEntregables) progress).getTimestamp() != null && (System.currentTimeMillis() - ((ProgresoEntregables) progress).getTimestamp() > TASK_TIMEOUT)) {
										LOG.warn("La tarea {} está tardando demasiado...", submissionOutcomesKey);
										if (System.currentTimeMillis() - ((ProgresoEntregables) progress).getTimestamp() > 3*TASK_TIMEOUT) {
											LOG.error("Cancelando tarea {} por estar atascada", submissionOutcomesKey);
											session.cancelSubmission(sOutcome.getIdentifier());
										}
									}
								}
							}

							//LOG.info("Progreso de {}: {}", submissionOutcomesKey, progreso);
						}

						if (sOutcome.isFinalState() && !jobsFinished.contains(submissionOutcomesKey)) {
							jobsFinished.add(submissionOutcomesKey);
							finished++;						
							LOG.info("Calculados {}/{} entregables: {}", finished, calcularEntregable.size(), submissionOutcomesKey);
						}			
					}	
				}		
			}

		
		// Eliminar indices
		getCache(CACHE_DETALLE_CORRIENTE).removeIndex(btDetalleCorrienteExtractor);
		getCache(CACHE_TOTALES_FLUJOS).removeIndex(btTotalesFlujosExtractor);
		getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES).removeIndex(btDetalleCorrienteEntregablesExtractor);
		getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES).removeIndex(swcasadoDetalleCorrienteEntregablesExtractor);
		
		threadLogUpdater.interrupt();
		
	}
	
	/*
	 * Recorre un diseño de procesos buscando los procesos y subprocesos correspondientes a entregables
	 */
	private CalcularEntregable buscarProceso(String proceso, FichaProceso fp, String kbasetec) {
		
		DisenoProcesos disenoProceso;
		
		if (kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMFE) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMMI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRLFE) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRLMI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMCF) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRMCI) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRINC) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRGTO) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRTIU) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRTID) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAEP) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAEN) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAIP) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRAIN) ||
				kbasetec.equals(ConstantsModulos.CTE_VAL_SCRANM)) {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fp.getCcanal(), null, null, null, ConstantsModulos.CTE_BT_BEL, null);
		} else {
			disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fp.getCcanal(), null, null, null, kbasetec, null);
			
			if(null == disenoProceso){
				disenoProceso = gestionarProceso.obtenerDisenoProceso(proceso, fp.getCcanal(), null, null, null, kbasetec, null);
			}
		}
			
		if(disenoProceso.getCgestespeci()!= null && !disenoProceso.getCgestespeci().isEmpty()) {				
			calcularEntregable.add(new CalcularEntregable(disenoProceso.getCgestespeci(), kbasetec));
		}else {	
			for (ElementoSubproceso elementoSubproceso : disenoProceso.getElementosSubprocesos()) {
				if(elementoSubproceso.getSsubproc()) {
					buscarProceso(elementoSubproceso.getCelement(), fp, kbasetec);										
				} else {
					if ((!elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_PROC_PESOSBT) 
							&& !elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FPSL) 
							&& !elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINF3) 
							&& !elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINF4)
							&& !elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINFSCR)) || 
							(elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_PROC_PESOSBT) && 
									fp.getCtipobt().equals(ConstantesSolvencia.MULTI7))  || (elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_PROC_PATRONCSM) && 
											fp.getCtipobt().equals(ConstantesSolvencia.MULTI6) && kbasetec.equals(ConstantesSolvencia.BASE_NIIF17 )) ||
							((elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FPSL) || 
									elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINF3) || 
									elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINF4) ||
									elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINFSCR)) && 
							 	(kbasetec.equals(ConstantesSolvencia.BASE_NIIF17) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_N17LIRIN) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_NIIF17IF)))
							||((elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINF4)
								|| elementoSubproceso.getCelement().equals(ConstantsFactorias.ENTREGABLE_FLUJINFSCR)) && 
							 	(kbasetec.equals(ConstantesSolvencia.BASE_BEL) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRTIU) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRTID) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRGTO) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRMFE) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRMMI) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRMCF) ||
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRMCI) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRLFE) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRLMI) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRINC) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRVM) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRAEN) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRAEP) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRAIN) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRAIP) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCRANM) || 
							 			kbasetec.equals(ConstantesSolvencia.BASE_SCR)))) {
						
						if (elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_SWCOBROCOMISIONES) 
								&& fp.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_SWCOBROCOMISIONES)
								&& fp.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)) {
							calcularEntregable.add(new CalcularEntregable(elementoSubproceso.getCelement(), kbasetec));
						}
						
						if((elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMACOA)
								|| elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_TOTPMACOA) || elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMDCOA)) 
								&& (fp.getCtipobt().equals(ConstantesSolvencia.BTCOA))){
									continue;
						}
						
						if((elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJCOASEG) || elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJTCAS)
								|| elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_PROVCOASEG) || elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMACOA)
								|| elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_TOTPMACOA) || elementoSubproceso.getCelement().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMDCOA)) 
								&& !(fp.getCtipobt().equals(ConstantesSolvencia.BTCOA)) && (!fp.getCtipobt().equals(ConstantesSolvencia.BTCOATF))){
									continue;
						}
						
						if (!elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_SWCOBROCOMISIONES)){
							calcularEntregable.add(new CalcularEntregable(elementoSubproceso.getCelement(), kbasetec));
						}
						if (elementoSubproceso.getCelement().equals(ConstantesSolvencia.CTE_PROC_PESOSBT) && 
								fp.getCtipobt().equals(ConstantesSolvencia.MULTI7)) {
							calcularEntregable.add(new CalcularEntregable(ConstantsFactorias.ENTREGABLE_PESOSBTPROXY, kbasetec));
						}
					}
				}
			}
		}

		return null;
	}
	
	
	/*
	 * Programa una tarea de entregables
	 */
	private void ejecutarEntregable(CalcularEntregable task) throws Throwable {
		
		// Creamos una tarea para el entregable
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		attrMap.put("task","entregables");
		attrMap.put("entregable", task.getNombreEntregable());
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);
			
		String taskName = task.getKbasetec() + " - " + task.getNombreEntregable();
				
		mapSubmissionOutcomes.put(taskName,
				session.submit(task, submissionConfiguration, new SubmissionCallback(taskName)));
						
	}
	

	/*
	 * Controlar el estado de las tareas y espera a que hayan terminado de ejecutarse todos
	 * los entregables
	 */
	private void controlarTareas() {
		
		int finished = 0;
		int jobsSize = mapSubmissionOutcomes.size();
		List<String> jobsFinished = new ArrayList<String>();
		
		Collection<String> submissionOutcomesKeys = mapSubmissionOutcomes.keySet();

			/*try {
				Thread.sleep(15000);
			} catch (InterruptedException e) {
			}*/
				
			for (String submissionOutcomesKey : submissionOutcomesKeys) {
				SubmissionOutcome sOutcome = mapSubmissionOutcomes.get(submissionOutcomesKey);
				
				if (!SubmissionState.DONE.equals(sOutcome.getSubmissionState())) {
					Object progress = null;
					try {
						progress = sOutcome.getProgress();
					} catch (Exception e) {
						LOG.error("Error recuperando progreso de tarea de {}", submissionOutcomesKey, e);
					}
					String progreso = "";
					if (progress != null) {
						progreso = progress.toString();
						if (progress instanceof ProgresoEntregables) {
							if (((ProgresoEntregables) progress).getTimestamp() != null && (System.currentTimeMillis() - ((ProgresoEntregables) progress).getTimestamp() > TASK_TIMEOUT)) {
								LOG.warn("La tarea {} está tardando demasiado...", submissionOutcomesKey);
								if (System.currentTimeMillis() - ((ProgresoEntregables) progress).getTimestamp() > 3*TASK_TIMEOUT) {
									LOG.error("Cancelando tarea {} por estar atascada", submissionOutcomesKey);
									session.cancelSubmission(sOutcome.getIdentifier());
								}
							}
						}
					}
					
					LOG.info("Progreso de {}: {}", submissionOutcomesKey, progreso);
				}
				
				if (sOutcome.isFinalState() && !jobsFinished.contains(submissionOutcomesKey)) {
					jobsFinished.add(submissionOutcomesKey);
					finished++;						
				}			
			}	
			
			
	}	

	//Filtrado entregables DIP0 por BT
	public void getEntregables (String bt) {
		entregablesBT = new ArrayList<String>();
		CalcularEntregable aux = new CalcularEntregable("FLUJOSTOTP", "BTI");
		
		buscarProceso(ConstantesSolvencia.CTE_ENTREGABLES, fichaProceso, bt);
		if (fichaProceso.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA) && fichaProceso.getKprotecnico().equals(ConstantesSolvencia.CTE_PROYECTO_TECNICO) && fichaProceso.getKuejecucion().equals(ConstantesSolvencia.CTE_UMIC_EJECUCION)) {
			calcularEntregable.add(aux);
		}
		for (CalcularEntregable ent : calcularEntregable) {
			entregablesBT.add(ent.getNombreEntregable());
		}
		entregablesBT.add("FLUJOSTOT");
		entregablesBT.add("MAESBTC");
		entregablesBT.add("FLUJOSDET");
		entregablesBT.add("INCIDENCIAS");
		entregablesBT.add("PTIPO_AUT");
		entregablesBT.add("INCIDENCIASMAESTRO");
	}
	
	public List<String> getEntregablesBT() {
		return entregablesBT;
	}


	public NamedCache getCache(String cacheName) {		
		return CacheFactory.getCache(cacheName);		
	}
	
}
