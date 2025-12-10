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
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.entregables.FlujPMdCoaKey;
import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTotPDao;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.aggregators.FirstItemAggregator;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable FLUJPMACOA.
 * 
 */
public class EntregableFLUJPMDCOA extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableTOTPMACOA.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
	
	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJPMDCOA;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		


		ValueExtractor[] detalleCorrienteExtractor = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODALIDAD),
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KCERTIFICADO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_NSUSCRI),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KGARANTIA),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KPRESTACION)
		};

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(detalleCorrienteExtractor);

		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOACTUALIZADO)),
		};

		// Create Values Aggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// Filtrar entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT), kbasetec);

		// Do the query
		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);
		
		List<FlujPMdCoa> FlujPMdCoas = transformResults(pivotResults);

		// Guardar resultados en cache
		servicio.almacenarEntregableFlujPMdCoa(FlujPMdCoas);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujPMdCoa> transformResults(Map<Object, Object> pivotResults) {
		List<FlujPMdCoa> flujPMdCoa = new ArrayList<FlujPMdCoa>();
		
		FlujPMdCoa flujos = new FlujPMdCoa();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();
			flujos = servicioConfiguracion.recuperarFlujPMdCoa( (Integer) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_7), 
					(String) keys.get(ConstantsFunciones.CTE_8), (Long) keys.get(ConstantsFunciones.CTE_3), 
					(Integer) keys.get(ConstantsFunciones.CTE_4), (Integer) keys.get(ConstantsFunciones.CTE_5),
					(Integer) keys.get(ConstantsFunciones.CTE_6), (String) keys.get(ConstantsFunciones.CTE_0),
					(Timestamp) keys.get(ConstantsFunciones.CTE_1));
			flujPMdCoa.add(transformEntry(flujos, entry));
		}

		return flujPMdCoa;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujosTotP
	 * 
	 * @param flujosTotP
	 * @param entry
	 * @return
	 */
	private static FlujPMdCoa transformEntry(FlujPMdCoa FlujPMdCoa, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		List values = (List) entry.getValue();
		FlujPMdCoa.setTotprovision((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		FlujPMdCoa.setTotfactgto((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		
		return FlujPMdCoa;
	}


}
