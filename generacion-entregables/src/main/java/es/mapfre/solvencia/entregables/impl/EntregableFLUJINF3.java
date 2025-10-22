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

import es.mapfre.solvencia.dominio.entregables.FlujInf3;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregableFLUJINF3 extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJINF3;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);

		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CCANAL),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CNEGOCIO),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_UOA),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_CONTRATO),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_COHORT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERA_ONER),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FHASTA),
		};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPFLUJONOANULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJONOAULADO)),
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJONOANULADO)), 
				new BigDecimalSum(
						new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJONOANULADO)),
		};

		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec,
				pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<FlujInf3> flujinf3s = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableFlujInf3(flujinf3s);

		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujInf3> transformResults(Map<Object, Object> pivotResults) {
		List<FlujInf3> flujinf3s = new ArrayList<FlujInf3>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujinf3s.add(transformEntry(new FlujInf3(), entry));
		}

		return flujinf3s;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujinf3
	 * 
	 * @param flujinf3
	 * @param entry
	 * @return
	 */
	private FlujInf3 transformEntry(FlujInf3 flujinf3, Entry<Object, Object> entry) {

		List key = (List) entry.getKey();
		flujinf3.setBt((String) key.get(ConstantsFunciones.CTE_0));
		flujinf3.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujinf3.setCcanal((Integer) key.get(ConstantsFunciones.CTE_2));
		flujinf3.setNegocio((String) key.get(ConstantsFunciones.CTE_3));
		flujinf3.setUoa((String) key.get(ConstantsFunciones.CTE_4));
		flujinf3.setKcarteraContrato((String) key.get(ConstantsFunciones.CTE_5));
		flujinf3.setKcarteraCohort((String) key.get(ConstantsFunciones.CTE_6));
		flujinf3.setKcarteraOner((String) key.get(ConstantsFunciones.CTE_7));
		flujinf3.setFdesde((Timestamp) key.get(ConstantsFunciones.CTE_8));
		flujinf3.setFhasta((Timestamp) key.get(ConstantsFunciones.CTE_9));

		List values = (List) entry.getValue();
		flujinf3.setTotFpVida((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		flujinf3.setTotFpFall((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		flujinf3.setTotFpGastos((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		flujinf3.setTotFpComisiones((BigDecimal) values.get(ConstantsFunciones.CTE_3));
		flujinf3.setTotFpRescates((BigDecimal) values.get(ConstantsFunciones.CTE_4));
		flujinf3.setTotFpCompl((BigDecimal) values.get(ConstantsFunciones.CTE_5));
		flujinf3.setTotFpPrimas((BigDecimal) values.get(ConstantsFunciones.CTE_6));
		
		flujinf3.setTotFpGtoAd((BigDecimal) values.get(ConstantsFunciones.CTE_7));

		return flujinf3;
	}
}
