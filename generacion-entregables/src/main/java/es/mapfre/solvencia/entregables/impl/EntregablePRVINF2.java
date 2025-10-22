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

import es.mapfre.solvencia.dominio.entregables.PrvInf2;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVINF2.
 * 
 */
public class EntregablePRVINF2 extends EntregableGenerico {
	
	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	
	private static final Logger LOG = LoggerFactory.getLogger(EntregablePRVINF2.class);
	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVINF2;
	}

	
	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);
		
		ValueExtractor[] rows = new ValueExtractor[] {
				// BT
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				//Fcierre
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				// Negocio
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				// Canal
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				//ctipramo
				new PofExtractor(String.class, TotalesFlujos.IND_CTIPRAMO),
				//kcarterainv;
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				//gapAct;
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				//kramo;
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				//kmodalidad;
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				//kgarantia;
				new PofExtractor(Integer.class, TotalesFlujos.IND_KGARANTIA),
				//segmento1;
				new PofExtractor(String.class, TotalesFlujos.IND_SEGMENTO1),
				//tiposubriesgo;
				new PofExtractor(String.class, TotalesFlujos.IND_TIPOSUBRIESGO),
				// NuevaProdu
				new PofExtractor(Boolean.class, TotalesFlujos.IND_NUEVAPRODUC),
				// indicrescate
				new PofExtractor(String.class, TotalesFlujos.IND_INDICRESCATE),
				//curvati
				new PofExtractor(String.class, TotalesFlujos.IND_CURVATI)
		};		
		
		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(rows);
		
		// Values :	∑ total.flujos.totprovision,
		//			∑ total.flujos.totfnrte
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				// Totprovision
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				// Totfnrte
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFNRTE))
		};
		
		// Create Values Aggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);
		
		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
		
		// Filtrar entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);
				
		// Do the query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);		
		
		this.setProgress(pivotResults.size(), 0);

		List<PrvInf2> prvInf2s = transformResults(pivotResults);
		
		// Guardar resultados en cache
		servicio.almacenarEntregablePrvInf2(prvInf2s);
		
		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * @param pivotResults
	 * @return
	 */
	private List<PrvInf2> transformResults(Map<Object, Object> pivotResults) {
		List<PrvInf2> prvInf2s= new ArrayList<PrvInf2>();
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			prvInf2s.add(transformEntry(new PrvInf2(), entry));			
		}
		
		return prvInf2s;
	}


	/**
	 * Mapea cada entrada de los resultados a la cache prvInf2
	 * @param prvInf2
	 * @param entry
	 * @return
	 */
	private PrvInf2 transformEntry(PrvInf2 prvInf2, Entry<Object, Object> entry) {
		List prvInf2Key = (List) entry.getKey();
		prvInf2.setBt((String) prvInf2Key.get(ConstantsFunciones.CTE_0));
		prvInf2.setFeccierre((Timestamp) prvInf2Key.get(ConstantsFunciones.CTE_1));
		prvInf2.setCnegocio((String) prvInf2Key.get(ConstantsFunciones.CTE_2));
		prvInf2.setCcanal((Integer) prvInf2Key.get(ConstantsFunciones.CTE_3));
		prvInf2.setCtipramo((String) prvInf2Key.get(ConstantsFunciones.CTE_4));
		prvInf2.setKcarterainv((String) prvInf2Key.get(ConstantsFunciones.CTE_5));
		prvInf2.setGapAct((String) prvInf2Key.get(ConstantsFunciones.CTE_6));
		prvInf2.setKramo((String) prvInf2Key.get(ConstantsFunciones.CTE_7));
		prvInf2.setKmodalidad((Integer) prvInf2Key.get(ConstantsFunciones.CTE_8));
		prvInf2.setKgarantia((Integer) prvInf2Key.get(ConstantsFunciones.CTE_9));
		prvInf2.setSegmento1((String) prvInf2Key.get(ConstantsFunciones.CTE_10));
		prvInf2.setTiposubriesgo((String) prvInf2Key.get(ConstantsFunciones.CTE_11));
		
		if (prvInf2Key.get(ConstantsFunciones.CTE_12) != null && (boolean) prvInf2Key.get(ConstantsFunciones.CTE_12)) {
			prvInf2.setNuevaproduc(ConstantsEntregables.CTE_NUEVAPRODUC_NP);
		} else {
			prvInf2.setNuevaproduc(ConstantsEntregables.CTE_NUEVAPRODUC_CA);			
		}
				
		prvInf2.setIndicrescate((String) prvInf2Key.get(ConstantsFunciones.CTE_13));
		prvInf2.setCurvati((String) prvInf2Key.get(ConstantsFunciones.CTE_14));
			
		List value = (List) entry.getValue();
		prvInf2.setPrv((BigDecimal) value.get(ConstantsFunciones.CTE_0));
		prvInf2.setFnrte((BigDecimal) value.get(ConstantsFunciones.CTE_1));
		
		return prvInf2;
	}
}


