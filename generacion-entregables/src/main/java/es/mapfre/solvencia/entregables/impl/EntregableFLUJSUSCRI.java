package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujSuscri;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;


/**
 * Clase que implementa el cálculo del Entregable FLUJSUSCRI.
 * 
 */
public class EntregableFLUJSUSCRI extends EntregableGenerico {

	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
		
	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJSUSCRI;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

//		LOG.info("Trabajando sobre {} detalles de entregables", detalleEntregables.size());
		
//		// Este entregable requiere tanto extracciones de más de una caché, como
//		// agregaciones. Esto significa que es necesario hacerlo en dos fases
//		// bien diferenciadas.
//
//		// En un primer lugar se va a realizar la agregación. Para ello creamos
//		// el extractor con todos los campos a obtener de la caché en la que
//		// están los campos en base a los que se agrega.
		ValueExtractor[] detalleCorrienteExtractor = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CNEGOCIO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KRAMO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODALIDAD),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_SEGMENTO1),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_TIPOSUBRIESGO),
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_NSUSCRI),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERAINV),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_GAPACT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KGARANTIA)};

		ValueExtractor multiExtractor = new MultiExtractor(detalleCorrienteExtractor);

//		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {

				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPPROVI)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMFPROB)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMCOLA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMPROVISION)),
		};

		// Construcción del agregador compuesto
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

			Map<Object, Object> aggregationResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
					pivotAggregator);
			
			this.setProgress(aggregationResults.size(), 0);		

			List<FlujSuscri> flujSuscri = transformResults(aggregationResults);

			// Almacenar en cache
			servicioAlmacenar.almacenarEntregableFlujSuscri(flujSuscri);
			
			this.setProgress(aggregationResults.size(), aggregationResults.size());			

	}
	
	private List<FlujSuscri> transformResults(Map<Object, Object> pivotResults) {
		List<FlujSuscri> flujSuscri = new ArrayList<FlujSuscri>();
		FlujSuscri flujos = new FlujSuscri();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();

			flujos = servicioConfiguracion.recuperarFlujSuscri( (String) keys.get(ConstantsFunciones.CTE_0), (Timestamp) keys.get(ConstantsFunciones.CTE_1), 
					(String) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_3), (String) keys.get(ConstantsFunciones.CTE_4), 
					(Integer) keys.get(ConstantsFunciones.CTE_5), (String)  keys.get(ConstantsFunciones.CTE_6), 
					(String) keys.get(ConstantsFunciones.CTE_7), (Long) keys.get(ConstantsFunciones.CTE_8), (Integer) keys.get(ConstantsFunciones.CTE_9), 
					(Integer) keys.get(ConstantsFunciones.CTE_10), (String) keys.get(ConstantsFunciones.CTE_11), (String) keys.get(ConstantsFunciones.CTE_12), 
					(Timestamp) keys.get(ConstantsFunciones.CTE_13), (Integer) keys.get(ConstantsFunciones.CTE_14));
			if (flujos == null) {
				flujos = new FlujSuscri();
				flujos.setBt((String) keys.get(ConstantsFunciones.CTE_0));
			    flujos.setFeccierre((Timestamp) keys.get(ConstantsFunciones.CTE_1));
			    flujos.setCnegocio((String) keys.get(ConstantsFunciones.CTE_2));
			    flujos.setCcanal((Integer) keys.get(ConstantsFunciones.CTE_3));
			    flujos.setKramo((String) keys.get(ConstantsFunciones.CTE_4));
			    flujos.setKmodalidad((Integer) keys.get(ConstantsFunciones.CTE_5));
			    flujos.setSegmento1((String) keys.get(ConstantsFunciones.CTE_6));
				flujos.setTiposubriesgo((String) keys.get(ConstantsFunciones.CTE_7));
				flujos.setKpoliza((Long) keys.get(ConstantsFunciones.CTE_8));
				flujos.setKsubpoliza((Integer) keys.get(ConstantsFunciones.CTE_9));
				flujos.setNsuscri((Integer) keys.get(ConstantsFunciones.CTE_10));
				flujos.setKcarterainv((String) keys.get(ConstantsFunciones.CTE_11));
				flujos.setGapact((String) keys.get(ConstantsFunciones.CTE_12));
				flujos.setFecdesde((Timestamp) keys.get(ConstantsFunciones.CTE_13));
			}
			flujSuscri.add(transformEntry(flujos, entry));
			
		}

		return flujSuscri;
	}
	
	
	
	/**
	 * Extrae los resultados calculados por el agregador. Realiza la extracción
	 * de los campos restantes.
	 * 
	 * @param aggregationResults
	 * @param oEnvironment 
	 * @return
	 */
	private FlujSuscri transformEntry(FlujSuscri flujsuscri, Entry<Object, Object> entry) {

			List values = (List) entry.getValue();
			
			flujsuscri.setTotfpvida((BigDecimal) values.get(ConstantsFunciones.CTE_0));
			flujsuscri.setTotfactvida((BigDecimal) values.get(ConstantsFunciones.CTE_1));
			flujsuscri.setTotcolavida((BigDecimal) values.get(ConstantsFunciones.CTE_2));
			flujsuscri.setTotfpfall((BigDecimal) values.get(ConstantsFunciones.CTE_3));
			flujsuscri.setTotfactfall((BigDecimal) values.get(ConstantsFunciones.CTE_4));
			flujsuscri.setTotcolafall((BigDecimal) values.get(ConstantsFunciones.CTE_5));
			flujsuscri.setTotfpcompl((BigDecimal) values.get(ConstantsFunciones.CTE_6));
			flujsuscri.setTotfactcompl((BigDecimal) values.get(ConstantsFunciones.CTE_7));
			flujsuscri.setTotcolacompl((BigDecimal) values.get(ConstantsFunciones.CTE_8));
			flujsuscri.setTotfprte((BigDecimal) values.get(ConstantsFunciones.CTE_9));
			flujsuscri.setTotfactrte((BigDecimal) values.get(ConstantsFunciones.CTE_10));
			flujsuscri.setTotcolarte((BigDecimal) values.get(ConstantsFunciones.CTE_11));
			flujsuscri.setTotfpgto((BigDecimal) values.get(ConstantsFunciones.CTE_12));
			flujsuscri.setTotfactgto((BigDecimal) values.get(ConstantsFunciones.CTE_13));
			flujsuscri.setTotcolagto((BigDecimal) values.get(ConstantsFunciones.CTE_14));
			flujsuscri.setTotfpcom((BigDecimal) values.get(ConstantsFunciones.CTE_15));
			flujsuscri.setTotfactcom((BigDecimal) values.get(ConstantsFunciones.CTE_16));
			flujsuscri.setTotcolacom((BigDecimal) values.get(ConstantsFunciones.CTE_17));
			flujsuscri.setTotfpgtoad((BigDecimal) values.get(ConstantsFunciones.CTE_18));
			flujsuscri.setTotfactgtoad((BigDecimal) values.get(ConstantsFunciones.CTE_19));
			flujsuscri.setTotcolagtoad((BigDecimal) values.get(ConstantsFunciones.CTE_20));
			flujsuscri.setTotfpprim((BigDecimal) values.get(ConstantsFunciones.CTE_21));
			flujsuscri.setTotfactprim((BigDecimal) values.get(ConstantsFunciones.CTE_22));
			flujsuscri.setTotcolaprim((BigDecimal) values.get(ConstantsFunciones.CTE_23));
			
			
			BigDecimal sumfpvida = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
			BigDecimal sumfpfall = (BigDecimal) values.get(ConstantsFunciones.CTE_3);
			BigDecimal sumfpcompl = (BigDecimal) values.get(ConstantsFunciones.CTE_6);
			BigDecimal sumfpgto = (BigDecimal) values.get(ConstantsFunciones.CTE_12);
			
			if (sumfpvida != null && sumfpfall != null && sumfpcompl != null && sumfpgto != null) {
				flujsuscri.setTotfpprestaciones(sumfpvida.add(sumfpfall).add(sumfpcompl).add(sumfpgto));
			}
			
			BigDecimal sumfactvida = (BigDecimal) values.get(ConstantsFunciones.CTE_1);
			BigDecimal sumfactfall = (BigDecimal) values.get(ConstantsFunciones.CTE_4);
			BigDecimal sumfactcompl = (BigDecimal) values.get(ConstantsFunciones.CTE_7);
			BigDecimal sumfactgto = (BigDecimal) values.get(ConstantsFunciones.CTE_13);
			
			if (sumfactvida != null && sumfactfall != null && sumfactcompl != null && sumfactgto != null) {
				flujsuscri.setTotfactprestaciones(sumfactvida.add(sumfactfall).add(sumfactcompl).add(sumfactgto));
			}
			
			BigDecimal sumcolavida = (BigDecimal) values.get(ConstantsFunciones.CTE_2);
			BigDecimal sumcolafall = (BigDecimal) values.get(ConstantsFunciones.CTE_5);
			BigDecimal sumcolacompl = (BigDecimal) values.get(ConstantsFunciones.CTE_8);
			BigDecimal sumcolagto = (BigDecimal) values.get(ConstantsFunciones.CTE_14);
			
			if (sumcolavida != null && sumcolafall != null && sumcolacompl != null && sumcolagto != null) {
				flujsuscri.setTotcolaprestaciones(sumcolavida.add(sumcolafall).add(sumcolacompl).add(sumcolagto));
			}
			
			
			flujsuscri.setTotfprob((BigDecimal) values.get(ConstantsFunciones.CTE_24));
			flujsuscri.setTotcola((BigDecimal) values.get(ConstantsFunciones.CTE_25));
			flujsuscri.setTotprovision((BigDecimal) values.get(ConstantsFunciones.CTE_26));

			return flujsuscri;

	}

}