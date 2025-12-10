package es.mapfre.solvencia.ficheros;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.UUIDBasedIdentifier;
import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.internal.DefaultProcessingSession;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.Dao;
import es.mapfre.solvencia.dao.impl.maestro.UmicDao;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.FichaProcesoDao;
import es.mapfre.solvencia.dao.impl.salidaCalculo.IncidenciaDao;
import es.mapfre.solvencia.dao.services.FactoriaDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.maestro.Umic;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.dominio.salidaCalculo.Incidencia;
import es.mapfre.solvencia.excepcion.Solvencia2Excepcion;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.planificador.SubmissionCallback;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.utils.BtUtils;
import es.mapfre.solvencia.utils.beanio.BeanIOReader;

public class CargaDatos {

	private static Logger log = LoggerFactory.getLogger(CargaDatos.class);

	private static final String FICHERO_SIN_UMICS = "ZY";
	private static final String NO_FP_COD_ERROR = "ZZ";
	private static final String NO_BTI_COD_ERROR = "ZX";

	private static final String RUTA_BOTES = "ruta.botes";

	private static final String BEANIO_CONFIG_XML = "beanio/beanio-config.xml";

	private Integer batchSize = 10;
	private IAlmacenarDatos almacenarDatos;

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

		boolean multi2 = false;
		boolean multi3 = false;
		boolean multisn = false;
		String catalogos = ConstantesSolvencia.CATALOGOS;
		// carga de fichas de proceso
		// carga botes is hay fichas de proceso
		// carga de umics, si está la umic en la ficha de proceso y en el bote
		// carga de auxiliares de los umics cargados
		// carga de catologo

		String umicsBloque = btUtils.getCargaFicherosProperty("umics.bloque");

		if (umicsBloque != null) {
			batchSize = Integer.valueOf(umicsBloque);
		}

		Collection<UmicKey> umicsBote = new HashSet<UmicKey>();

		cargaDatosGeneral("mensajes.errores", "", rutaBase);

		List<FichaProceso> fichas = cargaFichas(fecCierre, rutaBase);

		cargaDatosGeneral("fichaSalida", fecCierre, rutaBase);

		for (FichaProceso fichaProceso : fichas) {
			if (ConstantesSolvencia.CTIPOEJEC_BOTE.equals(fichaProceso.getCtipoejec())) {
				umicsBote.addAll(getBote(fecCierre, rutaBase, fichas));
			}
			if (fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI3)){
				multi3 = true;
			} else if (fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2M)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2A)
					|| fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTI2G)){
				multi2 = true;
			} else if (fichaProceso.getCtipobt().equals(ConstantesSolvencia.MULTISN)){
				multisn = true;
			}
		}

		cargaUmics(fichas, umicsBote, fecCierre, rutaBase);

		List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();

		submissionOutcomes.addAll(cargaDatosGeneralDistribuido("auxiliares", fecCierre, rutaBase));
		
		if (multi2 && multi3){
			catalogos = ConstantesSolvencia.CATALOGOS_ALL;
		} else if (multi2){
			catalogos = ConstantesSolvencia.CATALOGOS_MULTI2;
		} else if (multi3){
			catalogos = ConstantesSolvencia.CATALOGOS_MULTI3;
		} else if(multisn) {
			catalogos = ConstantesSolvencia.CATALOGOS_MULTISN;
		}
		
		submissionOutcomes.addAll(cargaDatosGeneralDistribuido(catalogos, "", rutaBase));

		for (SubmissionOutcome submissionOutcome : submissionOutcomes) {
			submissionOutcome.get();
		}

		return fichas;
	}

	private List<FichaProceso> cargaFichas(String fecCierre, String rutaBase) throws Solvencia2Excepcion,
			FileNotFoundException, IOException, ParseException {

		String filePath;
		String streamName;
		BeanIOReader inFichaProceso = null;
		List<FichaProceso> fichas = new ArrayList<FichaProceso>();

		FichaProcesoDao fichaProcesoDao = new FichaProcesoDao();

		FichaProceso fichaProceso = null;

		streamName = fichaProcesoDao.getCacheName();
		filePath = rutaBase + File.separator + fecCierre + btUtils.getCargaFicherosProperty(streamName);

		fichaProcesoDao.clear();
		inFichaProceso = new BeanIOReader(BEANIO_CONFIG_XML, filePath, streamName);

		while ((fichaProceso = (FichaProceso) inFichaProceso.read()) != null) {

			if (btUtils.getBts(fichaProceso.getCtipobt()) == null) {
				// TODO ¿qué base técnica lleva este error?
				Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(NO_BTI_COD_ERROR,
						new String[] { fichaProceso.getCtipobt() }, null, null, null, null, null, new Timestamp(
								new SimpleDateFormat("yyyyMM").parse(fecCierre).getTime()), null, null);

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

			Solvencia2Excepcion s2e = Solvencia2ExcepcionHelper.crearExcepcion(NO_FP_COD_ERROR,
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

	private Collection<UmicKey> getBote(String fecCierre, String rutaBase, List<FichaProceso> fichas)
			throws FileNotFoundException, IOException {

		String filePath;
		String streamName;
		Collection<UmicKey> umicsBote = new HashSet<UmicKey>();
		BeanIOReader inBote = new BeanIOReader(BEANIO_CONFIG_XML, null, null);

		UmicKey umicKey = null;
		IncidenciaDao incidenciaDao = new IncidenciaDao();

		streamName = incidenciaDao.getCacheName();

		for (FichaProceso ficha : fichas) {

			filePath = rutaBase + File.separator + fecCierre + File.separator
					+ btUtils.getCargaFicherosProperty(RUTA_BOTES) + File.separator + ficha.getCnegocio()
					+ ficha.getCcanal() + File.separator + btUtils.getCargaFicherosProperty(streamName);

			inBote.createReader(filePath, streamName);
			while ((umicKey = (UmicKey) inBote.read()) != null) {
				umicsBote.add(umicKey);
			}

			if (inBote != null) {
				inBote.close();
			}
		}
		return umicsBote;
	}

	private void cargaUmics(Collection<FichaProceso> fichas, Collection<UmicKey> umicsBote, String fecCierre,
			String rutaBase) throws FileNotFoundException, IOException, ParseException {

		long initMill = System.currentTimeMillis();

		String filePath;
		String streamName;
		BeanIOReader inUmic = null;

		Umic umic = null;
		UmicDao umicDao = new UmicDao();
		streamName = umicDao.getCacheName();
		filePath = rutaBase + File.separator + fecCierre + btUtils.getCargaFicherosProperty(streamName);

		Map<UmicKey, Umic> valores = new HashMap<UmicKey, Umic>();
		int contaUmics = 0;
		inUmic = new BeanIOReader(BEANIO_CONFIG_XML, filePath, streamName);
		while ((umic = (Umic) inUmic.read()) != null) {

			for (FichaProceso ficha : fichas) {
				if (ficha.getCtipoejec().equals(ConstantesSolvencia.CTIPOEJEC_BOTE)
						&& umicsBote.contains(umic.getKey())) {
					umicDao.put(umic.getKey(), umic);
					contaUmics++;
					break;// es suficiente si se cumple para una ficha de
							// proceso
				} else if (ficha.getCcanal().equals(umic.getDatosGenerales().getCcanal())
						&& ficha.getCnegocio().equals(umic.getDatosGenerales().getCnegocio())) {
					valores.put(umic.getKey(), umic);
					contaUmics++;
					break;// es suficiente si se cumple para una ficha de
							// proceso
				}
			}

			if (contaUmics % batchSize == 0) {
				umicDao.putAll(valores);
				valores.clear();
			}

			if (log.isDebugEnabled() && contaUmics % batchSize == 0) {
				log.debug(contaUmics + "");
			}

		}

		if (log.isInfoEnabled()) {
			log.info("CARGADAS " + contaUmics + " UMICS en " + (System.currentTimeMillis() - initMill) + "ms.");
		}

		if (contaUmics > 0) {
			umicDao.putAll(valores);
			valores.clear();
		} else {
			Incidencia incidencia = Solvencia2ExcepcionHelper.crearAviso(null, null, null, null, new Timestamp(
					new SimpleDateFormat("yyyyMM").parse(fecCierre).getTime()), null, null, FICHERO_SIN_UMICS,
					new String[] { filePath });

			log.warn(incidencia.getTextoError());

			almacenarDatos.almacenarIncidencias(incidencia);
		}

		if (inUmic != null) {
			inUmic.close();
		}
	}

	private void cargaDatosGeneral(String clave, String fecCierre, String rutaBase) throws FileNotFoundException,
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
			dao.loadCache(reader);

			reader.close();
		}
	}

	private List<SubmissionOutcome> cargaDatosGeneralDistribuido(final String clave, final String fecCierre,
			final String rutaBase) throws Throwable {

		List<TareaDistribuida> resumableTasks = new ArrayList<TareaDistribuida>();

		String[] daos = btUtils.getCargaFicherosProperty(clave).split(",");

		for (int i = 0; i < daos.length; i++) {
			resumableTasks.add(new CargaDatosDistribuido(fecCierre, rutaBase,
					btUtils.getCargaFicherosProperty(daos[i]), daos[i], BEANIO_CONFIG_XML));
		}

		ProcessingSession session = new DefaultProcessingSession(UUIDBasedIdentifier.newInstance());

		Map<String, String> attrMap = new HashMap<String, String>();
		attrMap.put("type", "grid");
		DefaultSubmissionConfiguration submissionConfiguration = new DefaultSubmissionConfiguration(attrMap);
		List<SubmissionOutcome> submissionOutcomes = new ArrayList<SubmissionOutcome>();

		for (TareaDistribuida resumableTask : resumableTasks) {
			submissionOutcomes.add(session.submit(resumableTask, submissionConfiguration, new SubmissionCallback(
					resumableTask.getNombre())));
		}

		return submissionOutcomes;
	}

	public CargaDatos(IAlmacenarDatos almacenarDatos) {
		super();
		this.almacenarDatos = almacenarDatos;
	}
}
