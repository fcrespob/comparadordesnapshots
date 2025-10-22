package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

import es.mapfre.solvencia.dominio.entregables.PesosBt;
import es.mapfre.solvencia.dominio.entregables.PesosBtProxy;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregablePESOSBT extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePESOSBT.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PESOSBT;
	}
	
	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		// totales-flujos cache extractors
		ValueExtractor[] rows = new ValueExtractor[] { 
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCARTERA),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(String.class, TotalesFlujos.IND_COHORTE),
				new PofExtractor(String.class, TotalesFlujos.IND_SWCASADO),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDADORIG),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADOORIG)};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Create Values Processor
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION))};

		// Create CompositeAggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
		
		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Do the Query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);
		
		// totales-flujos cache extractors (total a nivel de póliza/subpoliza/certificado)
		ValueExtractor[] rowsTot = new ValueExtractor[] { 
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCARTERA),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADO),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD)};
	
		ValueExtractor multiExtractorTot = new MultiExtractor(rowsTot);
	
		// Create Values Processor
		InvocableMap.EntryAggregator[] valuesTot = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION))};
	
		// Create CompositeAggregator
		CompositeAggregator valuesAggregatorTot = CompositeAggregator.createInstance(valuesTot);
	
		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregatorTot = GroupAggregator.createInstance(multiExtractorTot, valuesAggregatorTot);
		
		// Do the Query
		Map<Object, Object> pivotResultsTot = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregatorTot);

		this.setProgress(pivotResults.size(), 0);

		List<PesosBtProxy> pesosBtProxy = new ArrayList<PesosBtProxy>();
		List<PesosBt> pesosBt = transformResults(pivotResults, pivotResultsTot);

		// Almacenar en cache
		servicio.almacenarEntregablePesosBt(pesosBt);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}
	
	/**
	 * Extraer los resultados de los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<PesosBt> transformResults(Map<Object, Object> pivotResults, Map<Object, Object> pivotResultsTot) {
		List<PesosBt> pesosBt = new ArrayList<PesosBt>();
		PesosBt pesos = null;
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			pesos = transformEntry(entry, pivotResultsTot);
			if (null != pesos){
				pesosBt.add(pesos);
			}
		}
		return pesosBt;
	}

	/**
	 * Mapea cada entrada de resultados a la cache PESOSBT
	 * 
	 * @param factores
	 * @param entry
	 * @param totalPesos 
	 * @return
	 */
	private PesosBt transformEntry(Entry<Object, Object> entry, Map<Object, Object> totalPesos) {
		
		List<Object> key = (List<Object>) entry.getKey();
		List<Object> values = (List<Object>) entry.getValue();
		PesosBt pesosBt = new PesosBt();
		pesosBt.setBt((String) key.get(ConstantsFunciones.CTE_0));
		pesosBt.setCnegocio((String) key.get(ConstantsFunciones.CTE_1));
		pesosBt.setCcanal((Integer) key.get(ConstantsFunciones.CTE_2));
		pesosBt.setCcartera((Integer) key.get(ConstantsFunciones.CTE_3));
		pesosBt.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_4));
		pesosBt.setCohorte((String) key.get(ConstantsFunciones.CTE_5));
		pesosBt.setSwcasado((String) key.get(ConstantsFunciones.CTE_6));
		pesosBt.setKpoliza((Long) key.get(ConstantsFunciones.CTE_7));
		pesosBt.setKsubpoliza((Integer) key.get(ConstantsFunciones.CTE_8));
		pesosBt.setKcertificado((Integer) key.get(ConstantsFunciones.CTE_9));
		pesosBt.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_10));
		pesosBt.setTotFactActT((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		
		List<Object> keyTot = new ArrayList<Object>(); 
		keyTot.add(key.get(ConstantsFunciones.CTE_0));
		keyTot.add(key.get(ConstantsFunciones.CTE_2));
		keyTot.add(key.get(ConstantsFunciones.CTE_3));
		keyTot.add(key.get(ConstantsFunciones.CTE_1));
		keyTot.add(key.get(ConstantsFunciones.CTE_4));
		keyTot.add(key.get(ConstantsFunciones.CTE_9));
		keyTot.add(key.get(ConstantsFunciones.CTE_7));
		keyTot.add(key.get(ConstantsFunciones.CTE_8));
		keyTot.add(key.get(ConstantsFunciones.CTE_10));
		
		List<Object> valuetot = (List<Object>) totalPesos.get(keyTot);
		BigDecimal totProv = (BigDecimal) valuetot.get(ConstantsFunciones.CTE_0);
		
		pesosBt.setPesosUoA(ConstantsModulos.CTE_OPER_100);
		if (null != totProv && totProv.compareTo(BigDecimal.ZERO) != 0) {
			pesosBt.setPesosUoA(pesosBt.getTotFactActT().multiply(ConstantsModulos.CTE_OPER_100).divide(totProv, 2, RoundingMode.HALF_UP));
		}
		
		if (pesosBt.getKmodalidad() == 400) {
			pesosBt.setKmodalidadOrig((Integer) key.get(ConstantsFunciones.CTE_11));
			pesosBt.setKpolizaOrig((Long) key.get(ConstantsFunciones.CTE_12));
			pesosBt.setKsubpolizaOrig((Integer) key.get(ConstantsFunciones.CTE_13));
			pesosBt.setKcertificadoOrig((Integer) key.get(ConstantsFunciones.CTE_14));
		}
		
		return pesosBt;
	}
	
}
