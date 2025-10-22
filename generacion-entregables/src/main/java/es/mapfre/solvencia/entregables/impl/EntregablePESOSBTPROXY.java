package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
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

import es.mapfre.solvencia.dominio.entregables.PesosBtProxy;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregablePESOSBTPROXY extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePESOSBTPROXY.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PESOSBTPROXY;
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
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADOORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDADORIG)};

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
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADOORIG),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZAORIG),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDADORIG)};
	
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

		List<PesosBtProxy> pesosBtProxy = transformResults(pivotResults, pivotResultsTot);

		// Almacenar en cache
		servicio.almacenarEntregablePesosBtProxy(pesosBtProxy);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}
	
	/**
	 * Extraer los resultados de los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<PesosBtProxy> transformResults(Map<Object, Object> pivotResults, Map<Object, Object> pivotResultsTot) {
		List<PesosBtProxy> pesosBtProxy = new ArrayList<PesosBtProxy>();
		PesosBtProxy pesosProxy = null;
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			pesosProxy = transformEntry(entry, pivotResultsTot);
			if (null != pesosProxy){
				pesosBtProxy.add(pesosProxy);
			}
		}
		return pesosBtProxy;
	}
	
	/**
	 * Mapea cada entrada de resultados a la cache PESOSBT
	 * 
	 * @param factores
	 * @param entry
	 * @param totalPesos 
	 * @return
	 */
	private PesosBtProxy transformEntry(Entry<Object, Object> entry, Map<Object, Object> totalPesos) {
		
		List<Object> key = (List<Object>) entry.getKey();
		List<Object> values = (List<Object>) entry.getValue();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		PesosBtProxy pesosProxy = new PesosBtProxy();
		pesosProxy.setCnegocio((String) key.get(ConstantsFunciones.CTE_1));
		pesosProxy.setCcanal(StringUtils.leftPad(((Integer) key.get(ConstantsFunciones.CTE_2)).toString(),5,"0"));
		pesosProxy.setCcartera(StringUtils.leftPad(((Integer) key.get(ConstantsFunciones.CTE_3)).toString(),5,"0"));
		pesosProxy.setFcierre(sdf.format((Timestamp) key.get(ConstantsFunciones.CTE_4)));
		pesosProxy.setCohorte(StringUtils.leftPad((String) key.get(ConstantsFunciones.CTE_5),4," "));
		pesosProxy.setSwcasado((String) key.get(ConstantsFunciones.CTE_6));
		Integer intModalidad = (Integer) key.get(ConstantsFunciones.CTE_10);
		String strModalidad = intModalidad.toString().length() == 5 ? intModalidad.toString().substring(1) : intModalidad.toString();
		pesosProxy.setKmodalidad(StringUtils.leftPad(strModalidad,5,"0"));
		pesosProxy.setKpoliza(StringUtils.leftPad(((Long) key.get(ConstantsFunciones.CTE_7)).toString(),10,"0"));
		pesosProxy.setKsubpoliza(StringUtils.leftPad(((Integer) key.get(ConstantsFunciones.CTE_8)).toString(),4,"0"));
		pesosProxy.setKcertificado(StringUtils.leftPad(((Integer) key.get(ConstantsFunciones.CTE_9)).toString(),6,"0"));
		BigDecimal bdTotFactActT = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
		String totFactActT = bdTotFactActT.toString().replace(".", "").replace("-", "");
		String signoTotFactActT = bdTotFactActT.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		pesosProxy.setTotFactActT(signoTotFactActT + StringUtils.leftPad(totFactActT,15,"0"));
		
		
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
		
		BigDecimal bdPesosUoA = ConstantsModulos.CTE_OPER_100.setScale(2);
		if (null != totProv && totProv.compareTo(BigDecimal.ZERO) != 0) {
			bdPesosUoA = bdTotFactActT.multiply(ConstantsModulos.CTE_OPER_100).divide(totProv, 2, RoundingMode.HALF_UP);
		}
		
		String pesosUoA = bdPesosUoA.toString().replace(".", "").replace("-", "");
		String signoPesosUoA = bdPesosUoA.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
		pesosProxy.setPesosUoA(signoPesosUoA + StringUtils.leftPad(pesosUoA,7,"0"));
		return pesosProxy;
	}
	
}
