package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
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

import es.mapfre.solvencia.dominio.entregables.PrvCr;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVCR.
 * 
 */
public class EntregablePRVCR extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePRVINF1.class);
	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVCR;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		// Construcción de los extractores individuales de los valores sobre los
		// que se va a agrupar
		ValueExtractor[] rows = new ValueExtractor[] {
				// BT
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				// Fcierre
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				// Negocio
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				// Canal
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				// Ramo
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				// Modalidad
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				// Spcom
				new PofExtractor(String.class, TotalesFlujos.IND_SPCOM), };

		// Construcción del extractor compuesto
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Construcción de los agregadores individuales que se necesitan para
		// los campos del entregable que requieren un sumatorio
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {

				// prvcv.impnomfall = ∑ total.flujos.totfnfall
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFNFALL)),
				// prvcr.prv = ∑ total.flujos.totcola
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)) };

		// Construcción del agregador compuesto
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// Filtrado entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		// Construcción de la estructura de resultados
		List<PrvCr> prvCr = transformResults(pivotResults);

		// Guardado de resultados en caché
		servicio.almacenarEntregablePrvCr(prvCr);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private static List<PrvCr> transformResults(Map<Object, Object> pivotResults) {
		List<PrvCr> prvCrs = new ArrayList<PrvCr>();

		// Transformación individual de cada entrada de la lista de resultados
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			prvCrs.add(transformEntry(new PrvCr(), entry));
		}

		return prvCrs;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache prvcr
	 * 
	 * @param prvInf1
	 * @param entry
	 * @return
	 */
	private static PrvCr transformEntry(PrvCr prvCr, Entry<Object, Object> entry) {

		// Obtención de los valores sobre los que se realizó la agregación
		List key = (List) entry.getKey();
		prvCr.setBt((String) key.get(ConstantsFunciones.CTE_0));
		prvCr.setFeccierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		prvCr.setCnegocio((String) key.get(ConstantsFunciones.CTE_2));
		prvCr.setCcanal((Integer) key.get(ConstantsFunciones.CTE_3));
		prvCr.setKramo((String) key.get(ConstantsFunciones.CTE_4));
		prvCr.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_5));
		prvCr.setSpcom((String) key.get(ConstantsFunciones.CTE_6));

		// Obtención de los resultados de la agregación
		List value = (List) entry.getValue();
		prvCr.setImpnomfall((BigDecimal) value.get(ConstantsFunciones.CTE_0));
		prvCr.setPrv((BigDecimal) value.get(ConstantsFunciones.CTE_1));

		// prvcv.capriesgo = prvcv.impnomfall - prvcv.prv
		if (prvCr.getImpnomfall() != null && prvCr.getPrv() != null) {
			if (prvCr.getImpnomfall().compareTo(prvCr.getPrv()) == 1) {
				prvCr.setCapriesgo(prvCr.getImpnomfall().subtract(prvCr.getPrv()));
			} else {
				prvCr.setCapriesgo(BigDecimal.ZERO);
			}
		}

		return prvCr;
	}

}
