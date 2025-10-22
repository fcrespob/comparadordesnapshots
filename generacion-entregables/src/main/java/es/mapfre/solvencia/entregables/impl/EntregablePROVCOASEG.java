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

import es.mapfre.solvencia.dominio.entregables.ProvCoaSeg;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PROVCOASEG.
 * 
 */
public class EntregablePROVCOASEG extends EntregableGenerico {
	
	private NamedCache totales;

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PROVCOASEG;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {

		this.initProgress(oEnvironment);
		
		totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);
//
//		// Este entregable requiere tanto extracciones de más de una caché, como
//		// agregaciones. Esto significa que es necesario hacerlo en dos fases
//		// bien diferenciadas.
//
//		// En un primer lugar se va a realizar la agregación. Para ello creamos
//		// el extractor con todos los campos a obtener de la caché en la que
//		// están los campos en base a los que se agrega.
		ValueExtractor[] totalesExtractors = new ValueExtractor[] {
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(String.class, TotalesFlujos.IND_SEGMENTO1),
				new PofExtractor(String.class, TotalesFlujos.IND_TIPOSUBRIESGO),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_NSUSCRI),
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT),
				new PofExtractor(String.class, TotalesFlujos.IND_TABLA1ASEG1)
		};

		ValueExtractor multiExtractor = new MultiExtractor(totalesExtractors);

			InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTGTO)),
		};
		
			// Construcción del agregador compuesto
			CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Query
		Map<Object, Object> aggregationResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(aggregationResults.size(), 0);

		// Con los resultados de la agregación, se llama a la función encargada
		// de transformar los resultados. Dado que en dicha función
		// se va a incluir el procesamiento por cada entrada de los resultados
		// para obtener los campos guardados en otras cachés, es
		// necesario añadir también como argumento el extractor de
		// TotalesFLujos construido previamente
		//List<ProvCoaSeg> provcoaseguros = 
		List<ProvCoaSeg> provcoaseguros = this.transformResults(aggregationResults, kbasetec);

		// Almacenar en cache
		servicioAlmacenar.almacenarEntregableProvCoaSeg(provcoaseguros);

		this.setProgress(aggregationResults.size(), aggregationResults.size());
	}
	
	
	
	private List<ProvCoaSeg> transformResults(Map<Object, Object> pivotResults, String kbasetec) {
		List<ProvCoaSeg> provcoaseg = new ArrayList<ProvCoaSeg>();
//		ProvCoaSeg f = new ProvCoaSeg();
//		provcoaseg.add(f);
		ProvCoaSeg flujos = new ProvCoaSeg();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();

			flujos = servicioConfiguracion.recuperarProvCoaSeg( (String) keys.get(ConstantsFunciones.CTE_0), (Timestamp) keys.get(ConstantsFunciones.CTE_1), 
					(String) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_3), (String) keys.get(ConstantsFunciones.CTE_4), 
					(Integer) keys.get(ConstantsFunciones.CTE_5), (String)  keys.get(ConstantsFunciones.CTE_6), 
					(String) keys.get(ConstantsFunciones.CTE_7), (Long) keys.get(ConstantsFunciones.CTE_8), (Integer) keys.get(ConstantsFunciones.CTE_9), 
					(Integer) keys.get(ConstantsFunciones.CTE_10), (String) keys.get(ConstantsFunciones.CTE_11),
					(String) keys.get(ConstantsFunciones.CTE_12), (String) keys.get(ConstantsFunciones.CTE_13),
					(String) keys.get(ConstantsFunciones.CTE_14));
			if (flujos == null) {
				flujos = new ProvCoaSeg();
			}
			provcoaseg.add(transformEntry(flujos,kbasetec, entry));
		}

		return provcoaseg;
	}

	

	/**
	 * Extrae los resultados calculados por los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private ProvCoaSeg transformEntry(ProvCoaSeg provcoaseg, String kbasetec, Entry<Object, Object> entry) {
		
		List values = (List) entry.getValue();
		
		BigDecimal totfprob = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
		BigDecimal totfactgto = (BigDecimal) values.get(ConstantsFunciones.CTE_1);
		
		if (totfprob != null && totfactgto != null) {
			provcoaseg.setTotflujoactsingastos(totfprob.subtract(totfactgto));
			provcoaseg.setTotflujoactcongastos(totfactgto);
		}
		
		return provcoaseg;
	}
}