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

import es.mapfre.solvencia.dominio.entregables.PrvFpb;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVFPB.
 * 
 */
public class EntregablePRVFPB extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePRVFPB.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVFPB;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		// totales-flujos cache extractors
		ValueExtractor[] rows = new ValueExtractor[] { new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_INTBTI), 
		};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PFPINV)) };

		// Construcción del agregador compuesto
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Do the Query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<PrvFpb> prvFpbs = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregablePrvFpb(prvFpbs);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extraer los resultados de los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<PrvFpb> transformResults(Map<Object, Object> pivotResults) {
		List<PrvFpb> PrvUmics = new ArrayList<PrvFpb>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			PrvUmics.add(transformEntry(new PrvFpb(), entry));
		}

		return PrvUmics;
	}

	/**
	 * Mapea cada entrada de resultados a la cache PRVUMIC
	 * 
	 * @param prvUmic
	 * @param entry
	 * @return
	 */
	private PrvFpb transformEntry(PrvFpb prvFpb, Entry<Object, Object> entry) {

		List keys = (List) entry.getKey();

		prvFpb.setBt((String) keys.get(ConstantsFunciones.CTE_0));
		prvFpb.setFeccierre((Timestamp) keys.get(ConstantsFunciones.CTE_1));
		prvFpb.setCnegocio((String) keys.get(ConstantsFunciones.CTE_2));
		prvFpb.setCcanal((Integer) keys.get(ConstantsFunciones.CTE_3));
		prvFpb.setKcartera((String) keys.get(ConstantsFunciones.CTE_4));
		prvFpb.setGap((String) keys.get(ConstantsFunciones.CTE_5));
		prvFpb.setKmodalidad((Integer) keys.get(ConstantsFunciones.CTE_6));
		prvFpb.setIntbti((BigDecimal) keys.get(ConstantsFunciones.CTE_7));

		List values = (List) entry.getValue();

		prvFpb.setPrv((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		prvFpb.setPrvpb((BigDecimal) prvFpb.getPrv().subtract((BigDecimal) values.get(ConstantsFunciones.CTE_1)));

		return prvFpb;
	}

}
