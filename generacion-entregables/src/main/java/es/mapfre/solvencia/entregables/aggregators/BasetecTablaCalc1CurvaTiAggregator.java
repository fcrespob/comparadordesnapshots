package es.mapfre.solvencia.entregables.aggregators;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.net.BackingMapContext;
import com.tangosol.net.BackingMapManagerContext;
import com.tangosol.util.Binary;
import com.tangosol.util.BinaryEntry;
import com.tangosol.util.Converter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.AbstractAggregator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleBaseTecnica;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;

/**
 * Calcula los tres valores más repetidos de la agrupación para un Set de
 * entradas y el porcentaje sobre la provision para cada uno de ellos calculado
 * como: (∑total.flujos.totprovision (mod-gar-basetec.curvaTi1)〗)/(∑
 * total.flujos.totprovision (mod-gar))
 *
 */
public class BasetecTablaCalc1CurvaTiAggregator extends AbstractAggregator {

	private static final long serialVersionUID = 7081741126876926366L;

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private ValueExtractor extractor;
	private BigDecimal totProvision;
	private Map<String, BigDecimal> extractorTotProvision;
	private Map<String, Integer> repeatedValues;

	/**
	 * 
	 */
	public BasetecTablaCalc1CurvaTiAggregator() {
		super();
	}

	/**
	 * 
	 * @param extractor
	 */
	public BasetecTablaCalc1CurvaTiAggregator(ValueExtractor extractor) {
		this.extractor = extractor;
	}

	@Override
	protected void init(boolean isFinal) {
		this.totProvision = BigDecimal.ZERO;
		this.repeatedValues = new HashMap<String, Integer>();
		this.extractorTotProvision = new HashMap<String, BigDecimal>();
	}

	@Override
	protected void process(Object arg0, boolean fFinal) {
		List<Object> values = (List<Object>) arg0;

		BigDecimal prv = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
		String extratorValue = (String) values.get(ConstantsFunciones.CTE_1);

		if (prv != null) {
			// ∑ total.flujos.totprovision
			totProvision = totProvision.add(prv);

			// ∑ total.flujos.totprovision para cada valor del extractor
			if (extratorValue != null) {
				BigDecimal parcialPrv = extractorTotProvision.containsKey(extratorValue)
						? extractorTotProvision.get(extratorValue) : BigDecimal.ZERO;
				extractorTotProvision.put(extratorValue, parcialPrv.add(prv));
			}
		}

		// Número de veces que se repite cada valor
		if (extratorValue != null) {
			int count = repeatedValues.containsKey(extratorValue) ? repeatedValues.get(extratorValue) : 0;
			repeatedValues.put(extratorValue, count + ConstantsFunciones.CTE_1);
		}

	}

	/**
	 * Procesa la entrada para obtener los valores : - Campo de la entrada
	 * utilizando un extractor - totProvision de la cache de totales
	 * 
	 * @param entry
	 */
	protected void processEntry(BinaryEntry entry) {

		List<Object> processValues = new ArrayList<Object>();

		processValues.add(calculaTotProvision(entry));

		if (entry.extract(extractor) instanceof List<?>) {
			processValues.add(((List<Integer>) entry.extract(extractor)).get(0).toString());
		} else {
			processValues.add((String) entry.extract(extractor));
		}

		process(processValues, false);
	}

	@Override
	public Object aggregate(Set setEntries) {
		boolean fFinal = !m_fParallel;
		init(fFinal);

		for (Iterator iter = setEntries.iterator(); iter.hasNext();) {
			processEntry((BinaryEntry) iter.next());
		}

		List<Object> parallelAggregationResult = new ArrayList<Object>();
		parallelAggregationResult.add(totProvision);
		parallelAggregationResult.add(repeatedValues);
		parallelAggregationResult.add(extractorTotProvision);

		return parallelAggregationResult;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tangosol.util.aggregator.AbstractAggregator#aggregateResults(java.
	 * util.Collection)
	 * 
	 * Aggregate the results of the parallel aggregations.
	 * 
	 * Se añade a la agregación principal los resultados parciales de los
	 * diferente nodos
	 */
	@Override
	public Object aggregateResults(Collection parallelResults) {
		boolean fFinal = !m_fParallel;
		init(fFinal);

		for (Object nodeResult : parallelResults) {
			List nodeValues = (List) nodeResult;

			totProvision = totProvision.add((BigDecimal) nodeValues.get(ConstantsFunciones.CTE_0));

			// Añadir el número de veces que se repite cada valor en cada uno de
			// los nodos y totprovision de cada uno
			Map<String, Integer> repeatedvalues = (Map<String, Integer>) nodeValues.get(ConstantsFunciones.CTE_1);
			for (Map.Entry<String, Integer> entry : repeatedvalues.entrySet()) {
				// Entrada del nodo a añadir en la agregacion principal
				String key = entry.getKey();
				Integer value = entry.getValue();

				int count = repeatedValues.containsKey(key) ? value : ConstantsFunciones.CTE_0;
				repeatedValues.put(key, count + value);

				Map<String, BigDecimal> totProvisionExtractor = (Map<String, BigDecimal>) nodeValues
						.get(ConstantsFunciones.CTE_2);
				BigDecimal parcialPrv = extractorTotProvision.containsKey(key) ? extractorTotProvision.get(key)
						: BigDecimal.ZERO;
				extractorTotProvision.put(key, parcialPrv.add(totProvisionExtractor.get(key)));
			}
		}

		return finalizeResult(fFinal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.tangosol.util.aggregator.AbstractAggregator#finalizeResult(boolean)
	 * 
	 * Genera los resultados de salida: - valores más repetidos - Porcentaje
	 * sobre la provisión
	 */
	@Override
	protected Object finalizeResult(boolean arg0) {

		List<Object> finalResult = new ArrayList<Object>();

		// Ordenamos la lista de valores repetidos
		List<Map.Entry<String, Integer>> sortedList = sortList();

		// Obtener tres valores más repetidos si existen y el porcentaje sobre
		// la provisión
		for (int i = 0; i < sortedList.size() && sortedList.size() > 0; i++) {
			String key = sortedList.get(i).getKey();

			finalResult.add(key);

			if (totProvision.compareTo(BigDecimal.ZERO) == ConstantsFunciones.CTE_0) {
				finalResult.add(BigDecimal.ZERO);
			} else {
				BigDecimal porcprov = extractorTotProvision.get(key).divide(totProvision,
						ConstantsFunciones.MATH_CONTEXT);
				finalResult.add(porcprov);
			}
		}

		return finalResult;
	}

	/**
	 * Devuelve una lista con los valores más repetidos ordenados de forma
	 * descendente
	 * 
	 * @return
	 */
	private List<Entry<String, Integer>> sortList() {

		// Convert Map to List
		List<Map.Entry<String, Integer>> sortedList = new LinkedList<Map.Entry<String, Integer>>(
				repeatedValues.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(sortedList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		return sortedList;
	}

	/**
	 * Devuelve el valor totprovision para una de las umics de la agregación
	 * 
	 * @param entry
	 * @return
	 */
	private BigDecimal calculaTotProvision(BinaryEntry entry) {

		BackingMapContext totalesBackingMapContext = entry.getContext().getBackingMapContext(CACHE_TOTALES_FLUJOS);
		BackingMapManagerContext context = totalesBackingMapContext.getManagerContext();

		Converter converter = context.getValueFromInternalConverter();
		Converter keyConverter = context.getKeyToInternalConverter();

		TotalesFlujosKey totalesKey = new TotalesFlujosKey(
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KAJUSTE))),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KCERTIFICADO))),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KGARANTIA))),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KMODALIDAD))),
				(Long) entry.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
						new ReflectionExtractor(ConstantsEntregables.CTE_GET_KPOLIZA))),
				(String) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KPRESTACION))),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_KSUBPOLIZA))),
				(String) entry.extract(new PofExtractor(String.class, DetalleBaseTecnica.IND_BASETEC)),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_NSUSCRI))),
				(Integer) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_NORDEN))),
				(String) entry
						.extract(new ChainedExtractor(new ReflectionExtractor(ConstantsEntregables.CTE_GET_UMICKEY),
								new ReflectionExtractor(ConstantsEntregables.CTE_GET_CTIPOAPORT))));

		Map totalesMap = totalesBackingMapContext.getBackingMap();

		Binary totalesBinary = (Binary) totalesMap.get(keyConverter.convert(totalesKey));

		TotalesFlujos totales = (TotalesFlujos) converter.convert(totalesBinary);

		if (totales != null) {
			return totales.getTotprovision() == null ? BigDecimal.ZERO : totales.getTotprovision();
		} else {
			return BigDecimal.ZERO;
		}

	}

	@Override
	public void readExternal(PofReader arg0) throws IOException {
		this.extractor = (ValueExtractor) arg0.readObject(ConstantsFunciones.CTE_1);
	}

	@Override
	public void writeExternal(PofWriter arg0) throws IOException {
		arg0.writeObject(ConstantsFunciones.CTE_1, this.extractor);
	}

}
