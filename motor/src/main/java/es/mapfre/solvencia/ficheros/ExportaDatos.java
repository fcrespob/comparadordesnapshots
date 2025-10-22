package es.mapfre.solvencia.ficheros;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.UUIDBasedIdentifier;
import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.internal.DefaultProcessingSession;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;
import com.tangosol.net.CacheService;
import com.tangosol.net.Member;
import com.tangosol.net.PartitionedService;

import es.mapfre.solvencia.coherence.agent.FreeMemAgent;
import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.DaoSalidaCalculo;
import es.mapfre.solvencia.dao.impl.entregables.DetalleCorrienteEntregablesDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.data.services.FactoriaExporters;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.entregables.motor.EjecutorEntregables;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.planificador.SubmissionCallback;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;

public class ExportaDatos {
	
	private static Logger log = LoggerFactory.getLogger(ExportaDatos.class);
	private BtUtils btUtils = new BtUtils();
	private static final String BEANIO_OUT_CONFIG_XML = "beanio/beanio-config-out.xml";
	private SimpleDateFormat dateFormatterFeEfecto = new SimpleDateFormat("yyyyMMdd");
	private DetalleCorrienteEntregablesDao detalleCorrienteEntregablesDao = new DetalleCorrienteEntregablesDao();
	
	
	public void escribirResultados(FichaProceso ficha, String rutaBase, Boolean limpiarCaches) throws Throwable{
		// Limpiamos el heap
		FreeMemAgent.freeMem();

		IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
		
		ProcessingSession session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		attrMap.put("task","entregables");
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);
		List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();
		
		String nombref;
		
		List<TareaDistribuida> resumableTasks = new ArrayList<TareaDistribuida>();
		
		// TODO Sustituir por llamada a SolvenciaExporter
//		String[] daos = btUtils.getCargaFicherosProperty(ConstantesSolvencia.FICHEROS_SALIDA).split(",");
//		for (int i = 0; i < daos.length; i++) {
//			nombref = btUtils.getCargaFicherosProperty(daos[i]);
//			resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, nombref, daos[i], BEANIO_OUT_CONFIG_XML, btUtils.getBts(ficha.getCtipobt()), btUtils.getFormatoSalidaFichero()));
//		}
		
		
		// Comprobamos si hemos de exportar el detalle de forma distribuida o con un unico nodo
		Boolean exportarDetalleDistribuido = btUtils.getExportarDetalleDistribuido();
		
		// Buscamos los FileDescriptors de Exportación de Ficheros
		List<FileDescriptor> fileDescriptors = FactoriaExporters.getExporter("SOLVXPRT").getFileDescriptors(rutaBase, dateFormatterFeEfecto.format(ficha.getFefecto()), ficha);
	    	
		// Añadimos los FileDescriptors de exportación de los Entregables
		fileDescriptors.addAll(FactoriaExporters.getExporter("ENTREGABLES").getFileDescriptors(rutaBase, dateFormatterFeEfecto.format(ficha.getFefecto()), ficha));
	
		// Añadimos los FileDescriptors de exportación de los PTIPO automáticos
		fileDescriptors.addAll(FactoriaExporters.getExporter("PTIPOXPRT").getFileDescriptors(rutaBase, dateFormatterFeEfecto.format(ficha.getFefecto()), ficha));
	
		//Redimensionamineto entregables DIP0
		Map<String, List<String>> mapEntregables = new HashMap<String, List<String>>();
		for (String bt : btUtils.getBts(ficha.getCtipobt())) {
			EjecutorEntregables ejecEnt = new EjecutorEntregables(ficha);
			List<String> entregablesBt;
			if (!mapEntregables.containsKey(bt)) {
				ejecEnt.getEntregables(bt);
				entregablesBt = ejecEnt.getEntregablesBT();
				mapEntregables.put(bt, entregablesBt);
			}
		}
		
		FileDescriptor descriptorDetalleCorriente = null;
		for (FileDescriptor fileDescriptor : fileDescriptors) {
			// Comprobamos si exportamos de forma distribuida
			if (!"detalle-corriente".equals(fileDescriptor.getStreamName()) || !exportarDetalleDistribuido) {
				
				if((fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMACOA)
						|| fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_TOTPMACOA) || fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMDCOA)) 
						&& (ficha.getCtipobt().equals(ConstantesSolvencia.BTCOA))){
							continue;
				}
				
				if((fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJCOASEG) || fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJTCAS)
					|| fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_PROVCOASEG) || fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMACOA)
					|| fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_TOTPMACOA) || fileDescriptor.getStreamName().equalsIgnoreCase(ConstantesSolvencia.STREAM_FLUJPMDCOA)) 
					&& !(ficha.getCtipobt().equals(ConstantesSolvencia.BTCOA)) && (!ficha.getCtipobt().equals(ConstantesSolvencia.BTCOATF))){
						continue;
				}
				
				if (!fileDescriptor.getStreamName().equals(ConstantesSolvencia.STREAM_PESOSBT) || 
						(fileDescriptor.getStreamName().equals(ConstantesSolvencia.STREAM_PESOSBT) && 
								ficha.getCtipobt().equals(ConstantesSolvencia.MULTI7))) {
					if (!fileDescriptor.getStreamName().equals("swcobrocom")
							&& !fileDescriptor.getStreamName().equals("incidenciasmaestro")) {
						resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, fileDescriptor.getFilePath(), fileDescriptor.getStreamName(), fileDescriptor.getBeanioConfigXml(), btUtils.getBts(ficha.getCtipobt()), btUtils.getFormatoSalidaFichero(), fileDescriptor.getType(), mapEntregables));
					}	
					if (fileDescriptor.getStreamName().equals(ConstantesSolvencia.STREAM_PESOSBT)) {
						List<String> entregables = mapEntregables.get(ConstantesSolvencia.BASE_ROSSP);
						entregables.add(ConstantesSolvencia.FICH_PESOSBTPROXY);
						resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, ConstantesSolvencia.FICH_PESOSBTPROXY, ConstantesSolvencia.STREAM_PESOSBTPROXY, fileDescriptor.getBeanioConfigXml(), btUtils.getBts(ficha.getCtipobt()), btUtils.getFormatoSalidaFichero(), fileDescriptor.getType(), mapEntregables));
					}
					if (fileDescriptor.getStreamName().equals("incidenciasmaestro")) {
						resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, fileDescriptor.getFilePath(), fileDescriptor.getStreamName(), fileDescriptor.getBeanioConfigXml(), btUtils.getBts(ficha.getCtipobt()), btUtils.getFormatoSalidaFichero(), ConstantsFunciones.CTE_5, mapEntregables));
					}
				}
			} else {
				// No extraemos los detalles de corriente aquí, sino de forma distribuida
				descriptorDetalleCorriente = fileDescriptor;
			}
		}
		
		if (ficha.getKsistema().equals(ConstantesSolvencia.CTE_SISTEMA)
				&& ficha.getKprotecnico().equals(ConstantesSolvencia.CTE_PROCESO_TECNICO_SWCOBROCOMISIONES)
				&& ficha.getKuejecucion().equals(ConstantesSolvencia.CTE_UNID_EJEC_SWCOBROCOMISIONES)) {
			List<String> entregables = mapEntregables.get(ConstantesSolvencia.BASE_BEL);
			entregables.add(ConstantesSolvencia.CTE_SWCOBROCOMISIONES);
			resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, ConstantesSolvencia.CTE_SWCOBROCOMISIONES, "swcobrocom", BEANIO_OUT_CONFIG_XML, btUtils.getBts(ficha.getCtipobt()), "TXT", ConstantsFunciones.CTE_4, mapEntregables));
			resumableTasks.add(new ExportaDatosDistribuido(ficha, rutaBase, ConstantesSolvencia.CTE_SWCOBROCOMISIONES, "swcobrocomcsv", BEANIO_OUT_CONFIG_XML, btUtils.getBts(ficha.getCtipobt()), "CSV", ConstantsFunciones.CTE_4, mapEntregables));
		}
		
		// Exportamos la ficha de salida (LOGFICHACALC)
		String streamName = btUtils.getCargaFicherosProperty(ConstantesSolvencia.FICHA_SALIDA);
		nombref = btUtils.getCargaFicherosProperty(streamName);
		resumableTasks.add(new ExportaFichaResultado(ficha, rutaBase, nombref, streamName, BEANIO_OUT_CONFIG_XML));
		
		log.info("Preparado para lanzar la extracción de {}", resumableTasks);
		
		for (TareaDistribuida resumableTask : resumableTasks) {
			submissionOutcomes.add(session.submit(resumableTask, submissionConfiguration, new SubmissionCallback(resumableTask.getNombre())));
		}
		
		for (SubmissionOutcome submissionOutcome : submissionOutcomes) {
			try {
				submissionOutcome.get();
			} catch (Exception e) {
				log.error("Error escribiendo los resultados", e);
				almacenarDatos.agregarRegistroFichaResultado(ficha, "Se han producido errores escribiendo los resultados de salida");
			}
		}
		
		// Limpiamos el heap
		FreeMemAgent.freeMem();

		// TODO Exportamos los Detalles de Corriente en forma de tarea distribuida
		if (exportarDetalleDistribuido && descriptorDetalleCorriente != null) {
			log.info("Exportamos los Detalles de Corriente...");
			resumableTasks.clear();
			submissionOutcomes.clear();
			Set<Member> members = obtenerMiembrosCluster();
			for (Member member : members) {
				// El DAO del detalle de corriente debe tener filtrado por partición
				TareaDistribuida resumableTask = new ExportaDatosDistribuido(ficha, rutaBase, descriptorDetalleCorriente.getFilePath(), descriptorDetalleCorriente.getStreamName(), descriptorDetalleCorriente.getBeanioConfigXml(), btUtils.getBts(ficha.getCtipobt()), btUtils.getFormatoSalidaFichero(), Boolean.TRUE, descriptorDetalleCorriente.getType(), mapEntregables);
				submissionOutcomes.add(session.submit(resumableTask, submissionConfiguration, new SubmissionCallback(resumableTask.getNombre() + "_" + member.getMachineName() + "_" + member.getMemberName())));
			}
			
			for (SubmissionOutcome submissionOutcome : submissionOutcomes) {
				try {
					submissionOutcome.get();
				} catch (Exception e) {
					log.error("Error escribiendo los Detalles de Corriente de {}", submissionOutcome.getIdentifier(), e);
					almacenarDatos.agregarRegistroFichaResultado(ficha, "Se han producido errores escribiendo los Detalles de Corriente");
				}
			}
			log.info("Fin de exportar Detalles de Corriente.");
		}


		if (limpiarCaches) {
//			for (String dao : daos) {
			for (FileDescriptor fileDescriptor : fileDescriptors) {
				//Dao daoSalida = FactoriaDao.getDao(dao);
				Dao daoSalida = FactoriaDao.getDao(fileDescriptor.getStreamName());
				if (daoSalida != null && ((DaoSalidaCalculo) daoSalida).clearAfterExport()) {
					daoSalida.clear();
				}
			}
			
			// Limpiar cache intermedia de detalles que se utiliza en el cálculo de entregables
			detalleCorrienteEntregablesDao.clear();
		}
		
		
		
	}

	private Set<Member> obtenerMiembrosCluster() {
		Dao detalleCorrienteDao = FactoriaDao.getDao("detalle-corriente");
		CacheService detalleCorrienteCacheService = detalleCorrienteDao.getCache().getCacheService();
		Member localMember = detalleCorrienteCacheService.getCluster().getLocalMember();
		Set<Member> members = (Set<Member>) ((PartitionedService) detalleCorrienteCacheService).getOwnershipEnabledMembers();

		members.remove(localMember);
		
		return members;
	}
}
