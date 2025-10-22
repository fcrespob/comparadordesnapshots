package es.mapfre.solvencia.motor;

import java.util.ArrayList;
// JBMARTA - PYAM0025 - INI
//import java.util.Comparator;
// JBMARTA - PYAM0025 - FIN
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
// JBMARTA - PYAM0025 - INI
//import java.util.TreeSet;
// JBMARTA - PYAM0025 - FIN

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.ResumableTask;
import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.io.pof.annotation.PortableProperty;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.DistributedCacheService;
import com.tangosol.net.Member;
import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.filter.AnyFilter;
import com.tangosol.util.filter.PartitionedFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.maestro.DatosGeneralesDao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.PolizasTipoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.DetalleCorrienteDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TerminosPMCUmicDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.filtros.FiltroHelper;
import es.mapfre.solvencia.formulacion.util.UtilModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.executor.AdjustableThreadPoolExecutor;

@Portable
public class ResumableMotor implements ResumableTask {

	private static final String E00 = "00";

	private BtUtils btUtils = new BtUtils();

	private static Logger log = LoggerFactory.getLogger(ResumableMotor.class);
	@PortableProperty(0)
	private FichaProceso fichaproceso;

	private UmicDao umicDao = new UmicDao();
	private DatosGeneralesDao datosGeneralesDao = new DatosGeneralesDao();
	private IObtenerDatos obtenerDatos = FachadaServicios.getObtenerDatos();
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();
	private DetalleCorrienteDao detalleCorrienteDao = new DetalleCorrienteDao();
	private PolizasTipoDao polizasTipoDao = new PolizasTipoDao();
	private DistributedCacheService service;
	private TerminosPMCUmicDao terminosPMCUmicDao = new TerminosPMCUmicDao();

	private FiltroHelper helperFiltros = new FiltroHelper();
	
// JBMARTA - PYAM0025 - INI
//	private Comparator umicsOrdered() {
//		return new Comparator<UmicKey>() {
//
//			@Override
//			public int compare(UmicKey uk1, UmicKey uk2) {
//				if (uk1.getNorden()!= null && uk2.getNorden() != null) {
//					if (uk1.getNorden().compareTo(uk2.getNorden()) == 0){
//						return 1;
//					} else {
//						return uk1.getNorden().compareTo(uk2.getNorden());
//					}
//				}
//				return 0;
//			}
//			
//		};
//	}
// JBMARTA - PYAM0025 - FIN

	public ResumableMotor() {
		super();
		this.service = (DistributedCacheService) datosGeneralesDao.getCache().getCacheService();
	}

	public ResumableMotor(FichaProceso ficharoceso) {
		super();
		this.service = (DistributedCacheService) datosGeneralesDao.getCache().getCacheService();
		this.fichaproceso = ficharoceso;
	}

	@Override
	public Object run(TaskExecutionEnvironment oEnvironment) {
		ProgresoCalculo progreso = new ProgresoCalculo();
		try {

			Member localMember = CacheFactory.getCluster().getLocalMember();
			PartitionSet partsMember = service.getOwnedPartitions(localMember);
			PartitionSet parts = new PartitionSet(partsMember.getPartitionCount());

			while(!partsMember.isEmpty()){

				parts.add(partsMember);

				// JBMARTA - PYAM0025 - INI
				// Filter filtroProceso =
				// helperFiltros.obtenerFiltroFichaProceso(this.fichaproceso);
				// Filter filterPart = new PartitionedFilter(filtroProceso, partsMember);
				// JBMARTA - PYAM0025 - FIN

				// //Constante de extracción ptipo automático
				Integer cteExtr = UtilModulos.getCteExtr();

				// JBMARTA - PYAM0025 - INI
				// // Se ordena la ejecución para que se calculen primero las UMICs principales.
				// Set<UmicKey> listaClavesUmic = new TreeSet(umicsOrdered());
				// listaClavesUmic.addAll(datosGeneralesDao.keySet(filterPart));
				Set<UmicKey> listaClavesUmic = helperFiltros.filtrarUmics(partsMember, this.fichaproceso);
				// JBMARTA - PYAM0025 - FIN

				progreso.setTerminadas(0);
				progreso.setTotales(listaClavesUmic.size());
				oEnvironment.reportProgress(progreso);
			    
			    if(log.isInfoEnabled()){
			    	log.info("SE HAN RECUPERADO {} UMIC DE LA CACHE", listaClavesUmic.size());
			    }
			    
			    if (listaClavesUmic.size() > 0) {
			    	// Creamos el pool de hilos para ejecutar en paralelo. En principio, con un tamaño fijo de 1 hilo
					AdjustableThreadPoolExecutor taskExecutor = new AdjustableThreadPoolExecutor(listaClavesUmic.size(), 100, localMember.getMachineName() + "-" + localMember.getMemberName());
					
				    List<String> BTs = btUtils.getBts(fichaproceso.getCtipobt());
				    
					Iterator<UmicKey> iter = listaClavesUmic.iterator();
					
					Set<UmicKey> umicsPrincipales = new HashSet<UmicKey>();
	
					int index=1;
					long principioTodasUmics = System.currentTimeMillis();
	
					while(iter.hasNext()) {
						UmicKey umicKey = iter.next();
						UmicExecutor umicExecutor = new UmicExecutor(umicKey, fichaproceso, partsMember, cteExtr, BTs, umicsPrincipales, index);
						taskExecutor.execute(umicExecutor);
					}
					
					// Esperamos hasta que han finalizado todas las ejecuciones
					while (taskExecutor.getCompletedTaskCount() < listaClavesUmic.size()) {
						try {
							Thread.currentThread().sleep(2500l);
						} catch (InterruptedException e) {
							// Do nothing
						}
						progreso.setTerminadas((int) taskExecutor.getCompletedTaskCount());
						progreso.setTotales(listaClavesUmic.size());
						progreso.setTimestamp(System.currentTimeMillis());
						if (taskExecutor.getCompletedTaskCount() % 10 == 0 || taskExecutor.getCompletedTaskCount() == progreso.getTotales()) {
							oEnvironment.reportProgress(progreso);
						}
						
						//TODO Ajustamos poolsize
						if (progreso.getAvance() > 100) {
							int maximumThreads = (int) (progreso.getAvance() - 95);
							taskExecutor.adjustPoolSize(maximumThreads);
						}
						
						taskExecutor.runNextBatch();
					}
					
					// Se han finalizado todas
					progreso.setTerminadas(listaClavesUmic.size());
					progreso.setTimestamp(System.currentTimeMillis());
					oEnvironment.reportProgress(progreso);
					
					//Una vez se han procesado todas las UMICs se elimina el detalle de las UMICs que no estén en el PTIPO
					//(almacenadas en el SET)
					List<Filter> filtrosUmicPrincipales = new ArrayList<Filter>();
					for (UmicKey umicKey: umicsPrincipales) {
						if (!polizasTipoDao.containsUmic(umicKey)) {
	//						Filter filtroPart = new PartitionedFilter(detalleCorrienteDao.obtenerFiltroDetalles(umicKey), partsMember);
	//						detalleCorrienteDao.filtrarDetalles(filtroPart);
							filtrosUmicPrincipales.add(detalleCorrienteDao.obtenerFiltroDetalles(umicKey));
						}
					}
					if(log.isInfoEnabled()){
						log.info("Filtrando UMIC principales que no están en PTIPO ({})...", filtrosUmicPrincipales.size());
					}			
					Filter todosFiltrosUmicPrincipales = new AnyFilter(filtrosUmicPrincipales.toArray(new Filter[0]));
					Filter filtroPart = new PartitionedFilter(todosFiltrosUmicPrincipales, partsMember);
					detalleCorrienteDao.filtrarDetalles(filtroPart);
					
					
					if(log.isInfoEnabled()){
						log.info("Se han procesado {} UMIC en {} ms.", listaClavesUmic.size(), (System.currentTimeMillis()-principioTodasUmics));
					}
	
					taskExecutor.shutdown();
			    }

			    partsMember = service.getOwnedPartitions(localMember);
				partsMember.remove(parts);
				
			}

		} catch (Throwable e) {

			log.error(e.getMessage(),e);
			//TODO ¿qué base técnica lleva este error?
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(E00,
					new Object[]{e}, ConstantesSolvencia.CTE_PROYECCION, fichaproceso.getCtipobt(),
					fichaproceso.getCcanal(), null, null, fichaproceso.getFefecto(),
					fichaproceso.getCnegocio(), e);

			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

			throw s2e;
		}

		progreso.setTerminadas(progreso.getTotales());
		return progreso;
	}

	public FichaProceso getFichaproceso() {
		return fichaproceso;
	}

	public void setFichaproceso(FichaProceso fichaproceso) {
		this.fichaproceso = fichaproceso;
	}

	@Override
	public String toString() {
		return "ResumableMotor";
	}
}