package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.UUIDBasedIdentifier;
import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.internal.DefaultProcessingSession;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;
import com.tangosol.net.CacheFactory;

import es.mapfre.solvencia.coherence.jmx.MonitorizacionConstants;
import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.maestro.UmicsBoteDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FichaProcesoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.IncidenciaDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.data.Loader;
import es.mapfre.solvencia.data.services.FactoriaLoaders;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.RegistroParametros;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.files.FileDescriptor;
import es.mapfre.solvencia.planificador.SubmissionCallback;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class CargaDatosDividios {

	private static Logger log = LoggerFactory.getLogger(CargaDatosDividios.class);

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";
	private static final String BEANIO_CONFIG_OUT_XML = "beanio/beanio-config-out.xml";

	private Integer batchSize = 10;
	private IAlmacenarDatos almacenarDatos = FachadaServicios.getAlmacenarDatos();

	private List<FichaProceso> fichas;
	private Integer numUmics;

	private BtUtils btUtils = new BtUtils();

	// ERRORES
	// X excepción al mapear cualquier fichero

	// X excepción si no hay ficheros de fichasProceso
	// X excepción si no hay fichasProceso
	// X excepción si el literal de bt de fichasProceso no está registrado

	// X excepción si no hay umics en los ficheros
	// X excepción si no hay ficheros de umics
	// X no hay umics para una ficha de proceso, aviso

	public List<FichaProceso> cargaDatos(String fecCierre, String rutaBase) throws Throwable {
		Boolean realizarCargaFicheros = Boolean.valueOf(System.getProperty("solvencia.motor.cargar.ficheros", "true"));
		boolean multi2 = false;
		boolean multi3 = false;
		String catalogos = ConstantesSolvencia.CATALOGOS;
		
		// carga de fichas de proceso
		// carga botes is hay fichas de proceso
		// carga de umics, si está la umic en la ficha de proceso y en el bote
		// carga de auxiliares de los umics cargados
		// carga de catálogo

		String umicsBloque = btUtils.getCargaFicherosProperty(ConstantesSolvencia.UMICS_BLOQUE);

		if (umicsBloque != null) {
			batchSize = Integer.valueOf(umicsBloque);
		}

		//Collection<UmicKey> umicsBote = new HashSet<UmicKey>();

		cargaDatosErrores(rutaBase);

		cargaFichas(fecCierre, rutaBase);

		// Cargamos la ficha de log de salida. Limpiamos algún registro que pudiera quedar de otra ejecución.
		cargaDatosGeneral(ConstantesSolvencia.FICHA_SALIDA, fecCierre, rutaBase, true);

		for (FichaProceso fichaProceso : fichas) {
			String tipoEjecucion = null;
			if (fichaProceso.getRegistroParametros() != null) {
				tipoEjecucion = fichaProceso.getRegistroParametros().getIndicadorTipoProceso();
			} else {
				log.warn("La ficha de proceso {} no contiene registro de tipo de ejecución (COMP, REPR, BOTE)",
						fichaProceso.getKejecucion());
				// Suponemos CIERRE (COMP)
				tipoEjecucion = ConstantesSolvencia.CTIPOEJEC_CIERRE;
				RegistroParametros registroParametros = new RegistroParametros();
				registroParametros.setIndicadorTipoProceso(tipoEjecucion);
				fichaProceso.setRegistroParametros(registroParametros);
			}
			if (ConstantesSolvencia.CTIPOEJEC_BOTE.equals(tipoEjecucion)) {
				//umicsBote.addAll(getBote(fecCierre, rutaBase, fichas));
				getBote(fecCierre, rutaBase, fichas);
			}
			if (fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI3)){
				multi3 = true;
			} else if (fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2M)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2A)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2G)){
				multi2 = true;
			}
		}

		// Sólo realizamos la carga si no están ya cargados los ficheros
		if (realizarCargaFicheros) {
			log.info("Cargando Ficheros de Maestro");
			CacheFactory.getCache("monitorizacionCache").put(MonitorizacionConstants.PROCESO_ACTUAL, "Cargando Ficheros de Maestro");
			numUmics = cargaUmics(fichas, fecCierre, rutaBase);
	
			List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();
	
			log.info("Buscando Ficheros Auxiliares...");
			submissionOutcomes.addAll(cargaDatosGeneralDistribuido(Boolean.TRUE, ConstantesSolvencia.AUXILIARES, fecCierre, rutaBase));
			log.info("Buscando Ficheros de Catálogo...");
			CacheFactory.getCache("monitorizacionCache").put(MonitorizacionConstants.PROCESO_ACTUAL, "Cargando Ficheros de Catálogo");
			
			if (multi2 && multi3){
				catalogos = ConstantesSolvencia.CATALOGOS_ALL;
			} else if (multi2){
				catalogos = ConstantesSolvencia.CATALOGOS_MULTI2;
			} else if (multi3){
				catalogos = ConstantesSolvencia.CATALOGOS_MULTI3;
			}
			
			submissionOutcomes.addAll(cargaDatosGeneralDistribuido(Boolean.TRUE, catalogos, "", rutaBase));
	
			log.info("Buscando Ficheros de Extensiones...");
			CacheFactory.getCache("monitorizacionCache").put(MonitorizacionConstants.PROCESO_ACTUAL, "Cargando Ficheros de Extensiones");
			submissionOutcomes.addAll(cargaDatosGeneralDistribuido(Boolean.FALSE, null, "", rutaBase));
	
			for (SubmissionOutcome submissionOutcome : submissionOutcomes) {
				try {
					submissionOutcome.get();
				} catch (ExecutionException ex) {
					if (fichas != null) {
						for (FichaProceso fichaProceso : fichas) {
							almacenarDatos.agregarRegistroFichaResultado(fichaProceso, "Error cargando: " + ex.getCause().getLocalizedMessage());
						}
					}
					throw ex;
				}
			}
		}

		return fichas;
	}

	private List<FichaProceso> cargaFichas(String fecCierre, String rutaBase) throws Solvencia2Excepcion,
			FileNotFoundException, IOException, ParseException {

		String filePath;
		String streamName;
		BeanIOReader inFichaProceso = null;
		fichas = new ArrayList<FichaProceso>();

		FichaProcesoDao fichaProcesoDao = new FichaProcesoDao();

		FichaProceso fichaProceso = null;

		streamName = fichaProcesoDao.getCacheName();
		filePath = rutaBase + File.separator + fecCierre + btUtils.getCargaFicherosProperty(streamName);

		fichaProcesoDao.clear();
		inFichaProceso = new BeanIOReader(BEANIO_CONFIG_XML, filePath, streamName);

		while ((fichaProceso = (FichaProceso) inFichaProceso.read()) != null) {

			if (btUtils.getBts(fichaProceso.getCtipobt()) == null) {
				// TODO ¿qué base técnica lleva este error?
				Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(
						ConstantesSolvencia.NO_BTI_COD_ERROR, new String[] { fichaProceso.getCtipobt() }, null, null,
						null, null, null, new Timestamp(new SimpleDateFormat("yyyyMM").parse(fecCierre).getTime()),
						null, null);

				almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

				throw s2e;
			}

			fichas.add(fichaProceso);
			fichaProcesoDao.put(fichaProceso.getKey(), fichaProceso);
		}

		if (inFichaProceso != null) {
			inFichaProceso.close();
		}

		if (fichas.size() == 0) {

			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.NO_FP_COD_ERROR,
					new String[] { filePath }, null, null, null, null, null, new Timestamp(new SimpleDateFormat(
							"yyyyMM").parse(fecCierre).getTime()), null, null);

			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

			throw s2e;
		}

		Collections.sort(fichas, new Comparator<FichaProceso>() {
			public int compare(FichaProceso o1, FichaProceso o2) {
				return o1.getKejecucion().compareTo(o2.getKejecucion());
			}
		});

		return fichas;
	}

	private void getBote(String fecCierre, String rutaBase, List<FichaProceso> fichas)
			throws FileNotFoundException, IOException {

		String filePath;
		String streamName;
		//Collection<UmicKey> umicsBote = new HashSet<UmicKey>();
		BeanIOReader inBote = new BeanIOReader(BEANIO_CONFIG_OUT_XML, null, null);

		//Incidencia incidencia = null;
		IncidenciaDao incidenciaDao = new IncidenciaDao();
		UmicsBoteDao umicsBoteDao = new UmicsBoteDao();

		streamName = incidenciaDao.getCacheName();

		for (FichaProceso ficha : fichas) {

			filePath = rutaBase + File.separator + fecCierre + File.separator
					+ btUtils.getCargaFicherosProperty(ConstantesSolvencia.RUTA_BOTES) + File.separator
					+ ficha.getCnegocio() + ficha.getCcanal() + File.separator
					+ btUtils.getCargaFicherosProperty(streamName);

			inBote.createReader(filePath, streamName);
//			while ((incidencia = (Incidencia) inBote.read()) != null) {
//				umicsBote.add(incidencia.getClaveUmic());
//			}
			
			umicsBoteDao.loadCache(inBote);

			if (inBote != null) {
				inBote.close();
			}
		}
	}

	private Integer cargaUmics(Collection<FichaProceso> fichas, String fecCierre,
			String rutaBase) throws Throwable {

		long initMill = System.currentTimeMillis();
		Integer contaUmics = 0;
		
		Dao umicsDao = new UmicDao();
		umicsDao.clear();

		ProcessingSession session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());
		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);
		List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();

		String fileName = btUtils.getCargaFicherosProperty(ConstantesSolvencia.COD_FICHERO_UMICS);

		int lastSeparatorIndex = 0;
		lastSeparatorIndex = fileName.lastIndexOf('/');
		if (lastSeparatorIndex < 0) {
			lastSeparatorIndex = fileName.lastIndexOf('\\');
		}

		String fileNamePatternPrev = fileName.substring(0, lastSeparatorIndex + 1);
		String fileNamePattern = fileName.substring(lastSeparatorIndex + 1);
		fileNamePattern = fileNamePattern.concat(ConstantesSolvencia.REGEX_FILES);

		final Pattern p = Pattern.compile(fileNamePattern);
		String[] ficheros = new File(rutaBase + File.separator + fecCierre + fileNamePatternPrev)
				.list(new FilenameFilter() {

					@Override
					public boolean accept(File dir, String name) {
						return (p.matcher(name).matches());
					}

				});

		if (ficheros == null || ficheros.length == 0) {
			ficheros = new String[] { fileName.substring(lastSeparatorIndex + 1) };
		}

		// lanzar tareas
		for (int j = 0; j < ficheros.length; j++) {
			TareaDistribuida resumableTask = new CargaUmicsDistribuido(fecCierre, rutaBase, fileNamePatternPrev
					+ ficheros[j], ConstantesSolvencia.COD_FICHERO_UMICS, BEANIO_CONFIG_XML, fichas, 
					batchSize);
			submissionOutcomes.add(session.submit(resumableTask, submissionConfiguration, new SubmissionCallback(
					resumableTask.getNombre())));
		}

		// esperar tareas
		for (SubmissionOutcome submissionOutcome : submissionOutcomes) {
			contaUmics = contaUmics + (Integer) submissionOutcome.get();
			if (log.isDebugEnabled()) {
				log.debug("CARGADAS " + contaUmics + " UMICS en " + (System.currentTimeMillis() - initMill) + "ms.");
			}
		}

		if (log.isInfoEnabled()) {
			log.info("TOTAL CARGADAS " + contaUmics + " UMICS en " + (System.currentTimeMillis() - initMill) + "ms.");
		}

		if (contaUmics == 0) {
//			Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(null, null, null, null, new Timestamp(
//					new SimpleDateFormat("yyyyMM").parse(fecCierre).getTime()), null, null,
//					ConstantesSolvencia.FICHERO_SIN_UMICS, new String[] { fileNamePattern });
//
//			log.warn(incidencia.getTextoError());
//
//			almacenarDatos.almacenarIncidencias(incidencia);
			
			// Creamos excepción en lugar de incidencia
			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(ConstantesSolvencia.FICHERO_SIN_UMICS,
					null, null, null, null, null, null, new Timestamp(new SimpleDateFormat(
							"yyyyMM").parse(fecCierre).getTime()), null, null);

			almacenarDatos.almacenarIncidencias(s2e.getIncidencia());

			throw s2e;

		}

		return contaUmics;
	}

	private void cargaDatosGeneral(String clave, String fecCierre, String rutaBase) throws FileNotFoundException,
	IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
	InvocationTargetException {
		cargaDatosGeneral(clave, fecCierre, rutaBase, false);
	}
	
	private void cargaDatosGeneral(String clave, String fecCierre, String rutaBase, boolean clearCache) throws FileNotFoundException,
			IOException, ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String filePath;
		String streamName;
		String[] daos = btUtils.getCargaFicherosProperty(clave).split(",");

		BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML, null, null);
		for (int i = 0; i < daos.length; i++) {

			streamName = daos[i];
			filePath = rutaBase + File.separator + fecCierre + btUtils.getCargaFicherosProperty(streamName);

			reader.createReader(filePath, streamName);

			Dao dao = FactoriaDao.getDao(streamName);
			if (clearCache) {
				dao.clear();
			}
			dao.loadCache(reader);

			reader.close();
		}
	}

	private List<SubmissionOutcome> cargaDatosGeneralDistribuido(final Boolean loadSolvenciaFiles, final String clave, final String fecCierre,
			final String rutaBase) throws Throwable {

		List<TareaDistribuida> resumableTasks = new ArrayList<TareaDistribuida>();
		List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();

		List<Loader> loaders = new ArrayList<Loader>();
		if (loadSolvenciaFiles) {
			loaders.add(FactoriaLoaders.getLoader("SOLVLODR"));
		} else {
			//List<String> filtroLoaders = new ArrayList<String>().add("SOLVLODR");
			loaders.addAll(FactoriaLoaders.getLoadersFiltrado(Arrays.asList(new String[] {"SOLVLODR"} )));
		}
		for (Loader loader : loaders) {
			if (loader != null) {
				List<FileDescriptor> descriptores;
				if (loadSolvenciaFiles) {
					SolvenciaLoader solvLoader = (SolvenciaLoader) loader;
					descriptores = solvLoader.getFileDescriptors(rutaBase, fecCierre, clave);
				} else {
					descriptores = loader.getFileDescriptors(rutaBase, fecCierre);
				}
				if (descriptores != null) {
					for (FileDescriptor descriptor : descriptores) { 
						resumableTasks.add(new CargaDatosDistribuido(fecCierre, rutaBase, descriptor));
					}
			
					ProcessingSession session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());
			
					Map<String, String> attrMap = new HashMap<String, String>();
					attrMap.put("type", "grid");
					DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);
			
					for (TareaDistribuida resumableTask : resumableTasks) {
						submissionOutcomes.add(session.submit(resumableTask, submissionConfiguration, new SubmissionCallback(
								resumableTask.getNombre())));
					}
				}
			}
		}
		
		return submissionOutcomes;
	}

	private void cargaDatosErrores(String rutaBase) throws IOException {
		String filePath;
		String streamName;
		String[] daos = btUtils.getCargaFicherosProperty(ConstantesSolvencia.MENSAJES_ERRORES).split(",");
		Dao dao = null;

		BeanIOReader reader = new BeanIOReader(BEANIO_CONFIG_XML, null, null);

		if (daos != null && daos.length > 0) {
			// Cargamos el fichero genérico
			URL recursoFicheroErrores = ClassLoader.getSystemResource("CTLG_TB880ERR0.TXT");
			if (recursoFicheroErrores != null) {
				filePath = "CTLG_TB880ERR0.TXT";// recursoFicheroErrores.getFile();
				streamName = daos[0];
				reader.createReader(filePath, streamName, StandardCharsets.UTF_8.name());

				dao = FactoriaDao.getDao(streamName);
				if (dao != null) {
					dao.loadCache(reader);
				}
			}

			// Cargamos el resto de ficheros de descripción de errores
			for (int i = 0; i < daos.length; i++) {
				streamName = daos[i];

				dao = FactoriaDao.getDao(streamName);
				filePath = rutaBase + File.separator + btUtils.getCargaFicherosProperty(streamName);

				reader.createReader(filePath, streamName);
				dao.loadCache(reader);
			}

			reader.close();
		}
	}

	public CargaDatosDividios(IAlmacenarDatos almacenarDatos) {
		super();
		this.almacenarDatos = almacenarDatos;
	}

	public List<FichaProceso> getFichas() {
		return fichas;
	}

	public Integer getNumUmics() {
		return numUmics;
	}

}
