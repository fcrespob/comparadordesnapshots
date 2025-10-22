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
import es.mapfre.solvencia.dominio.entregables.PatronCsm;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregablePATRONCSM extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PATRONCSM;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);

		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_UOA),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE),
				
		};

		ValueExtractor multiExtractor = new MultiExtractor(rows);
		
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				 new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_RAUMIC)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_ROSSP_CSM)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.PROV_ROSSP_CSM)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.PROV_BEL_NIIF17))
				};

		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
				pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<PatronCsm> patronCsm = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregablePatronCsm(patronCsm);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<PatronCsm> transformResults(Map<Object, Object> pivotResults) {
		List<PatronCsm> patronCsm = new ArrayList<PatronCsm>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			patronCsm.add(transformEntry(new PatronCsm(), entry));
		}

		return patronCsm;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache patroncsm
	 * 
	 * @param patronCsm
	 * @param entry
	 * @return
	 */
	private PatronCsm transformEntry(PatronCsm patronCsm, Entry<Object, Object> entry) {

		List key = (List) entry.getKey();
		patronCsm.setBt((String) key.get(ConstantsFunciones.CTE_0));
		patronCsm.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		patronCsm.setCcanal((Integer) key.get(ConstantsFunciones.CTE_2));
		patronCsm.setUoa((String) key.get(ConstantsFunciones.CTE_3));
		patronCsm.setFecdesde((Timestamp) key.get(ConstantsFunciones.CTE_4));

		List values = (List) entry.getValue();
		patronCsm.setRaumic((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		patronCsm.setRosspCSM((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		patronCsm.setProvbtiproy((BigDecimal) values.get(ConstantsFunciones.CTE_2)); //Provision rosspcsm
		patronCsm.setProvbelproy((BigDecimal) values.get(ConstantsFunciones.CTE_3));

		return patronCsm;
	}
}
