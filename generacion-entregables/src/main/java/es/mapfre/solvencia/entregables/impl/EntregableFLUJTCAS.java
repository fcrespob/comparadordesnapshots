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
import es.mapfre.solvencia.dominio.entregables.FlujTcas;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable FLUJTCAS.
 * 
 */
public class EntregableFLUJTCAS extends EntregableGenerico {
	
	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);

	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;
	
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJTCAS;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

//		LOG.info("Trabajando sobre {} detalles de entregables", detalleEntregables.size());

//		intDgs = getIntDGS();
//
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
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERAINV),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_GAPACT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KRAMO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODALIDAD),
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_NSUSCRI),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE)};

		ValueExtractor multiExtractor = new MultiExtractor(detalleCorrienteExtractor);
//
//		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMFPROBTANUL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class,
						DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMCOLA)),			
		};

		// Construcción del agregador compuesto
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);
		
		Map<Object, Object> aggregationResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
				pivotAggregator);
	
			// Con los resultados de la agregación, se llama a la función encargada
			// de transformar los resultados. Dado que en dicha función
			// se va a incluir el procesamiento por cada entrada de los resultados
			// para obtener los campos guardados en otras cachés, es
			// necesario añadir también como argumento el extractor de
			// DetalleCorrienteEntregables
			// construido previamente
		List<FlujTcas> flujtcass = transformResults(aggregationResults, kbasetec);
	
			// Almacenar en cache
		servicioAlmacenar.almacenarEntregableFlujTcas(flujtcass);
			
		this.setProgress(aggregationResults.size(), aggregationResults.size());


	}
	
	private List<FlujTcas> transformResults(Map<Object, Object> pivotResults, String kbasetec) {
		List<FlujTcas> flujTcas = new ArrayList<FlujTcas>();
		//FlujTcas f = new FlujTcas();
		//flujTcas.add(f);
		FlujTcas flujos = new FlujTcas();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();
			flujos = servicioConfiguracion.recuperarFlujTcas( (String) keys.get(ConstantsFunciones.CTE_0), (Timestamp) keys.get(ConstantsFunciones.CTE_1), 
					(String) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_3), (String) keys.get(ConstantsFunciones.CTE_6), 
					(Integer) keys.get(ConstantsFunciones.CTE_7), (Long)  keys.get(ConstantsFunciones.CTE_8), 
					(Integer) keys.get(ConstantsFunciones.CTE_9), (Integer) keys.get(ConstantsFunciones.CTE_10), (String) keys.get(ConstantsFunciones.CTE_4), 
					(String) keys.get(ConstantsFunciones.CTE_5), (Timestamp) keys.get(ConstantsFunciones.CTE_11));
			if (flujos == null) {
				flujos = new FlujTcas();
			}
			flujTcas.add(transformEntry(flujos,kbasetec, entry));
		}

		return flujTcas;
	}

	/**
	 * Extrae los resultados calculados por el agregador. Realiza la extracción
	 * de los campos restantes.
	 * 
	 * @param aggregationResults
	 * @return
	 */
	private FlujTcas transformEntry(FlujTcas flujtcas, String kbasetec, Entry<Object, Object> entry) {
		
		List values = (List) entry.getValue();

        if (flujtcas.getImpflujprob() != null) {
        	flujtcas.setImpflujprob((BigDecimal) values.get(ConstantsFunciones.CTE_0));
        }
        
        flujtcas.setImpflujact((BigDecimal) values.get(ConstantsFunciones.CTE_1));
        
		return flujtcas;
	}
}
