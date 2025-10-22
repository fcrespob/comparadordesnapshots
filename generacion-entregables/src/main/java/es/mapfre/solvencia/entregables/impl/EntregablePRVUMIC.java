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
import com.tangosol.util.InvocableMap.EntryProcessor;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.processor.CompositeProcessor;
import com.tangosol.util.processor.ExtractorProcessor;

import es.mapfre.solvencia.coherence.keys.salidaCalculo.TotalesFlujosKey;
import es.mapfre.solvencia.dao.impl.salidaCalculo.TotalesFlujosDao;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.PrvUmic;
import es.mapfre.solvencia.dominio.maestro.Fechas;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.processors.PrvUmicProcessor;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVUMIC.
 * 
 */
public class EntregablePRVUMIC extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	final TotalesFlujosDao totalesFlujosdao = new TotalesFlujosDao();

	private static final Logger LOG = LoggerFactory.getLogger(EntregablePRVUMIC.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVUMIC;
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
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				new PofExtractor(String.class, TotalesFlujos.IND_CTIPOPROVI),
				new PofExtractor(String.class, TotalesFlujos.IND_INDICRESCATE),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFNRTE),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFNFALL),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_INTFECCALC),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_INTBTI),
				new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PFPINV),
		//INI-TAR00400971	
//				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT)};
				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT),
				new PofExtractor(String.class, TotalesFlujos.IND_SPCOM),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODEXT),
				new PofExtractor(String.class, TotalesFlujos.IND_KBENCON)};
//FIN-TAR00400971	
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Create Values Processor
		EntryProcessor[] valuesProcessor = new EntryProcessor[] {
				// TotalesFlujos Processor
				new ExtractorProcessor(multiExtractor),
				// Umic Processors
				new PrvUmicProcessor(new PofExtractor(Timestamp.class, Fechas.IND_FECINISUS)) };

		// Create CompositeProcessor
		CompositeProcessor compositeProcessor = new CompositeProcessor(valuesProcessor);

		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Do the Query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.invokeAll(isKbasetec, compositeProcessor);

		this.setProgress(pivotResults.size(), 0);

		List<PrvUmic> PrvUmics = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregablePrvUmic(PrvUmics);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extraer los resultados de los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<PrvUmic> transformResults(Map<Object, Object> pivotResults) {
		List<PrvUmic> PrvUmics = new ArrayList<PrvUmic>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			PrvUmics.add(transformEntry(new PrvUmic(), entry));
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
	private PrvUmic transformEntry(PrvUmic prvUmic, Entry<Object, Object> entry) {

		Object[] values = ((Object[]) entry.getValue());

		List totalesValues = (List) values[ConstantsFunciones.CTE_0];

		prvUmic.setBt((String) totalesValues.get(ConstantsFunciones.CTE_0));
		prvUmic.setFeccierre((Timestamp) totalesValues.get(ConstantsFunciones.CTE_1));
		prvUmic.setCnegocio((String) totalesValues.get(ConstantsFunciones.CTE_2));
		prvUmic.setCcanal((Integer) totalesValues.get(ConstantsFunciones.CTE_3));
		prvUmic.setKramo((String) totalesValues.get(ConstantsFunciones.CTE_4));
		prvUmic.setKcarterainv((String) totalesValues.get(ConstantsFunciones.CTE_5));
		prvUmic.setGapAct((String) totalesValues.get(ConstantsFunciones.CTE_6));
		prvUmic.setCtipoprovi((String) totalesValues.get(ConstantsFunciones.CTE_7));
		prvUmic.setIndicrescate((String) totalesValues.get(ConstantsFunciones.CTE_8));
		prvUmic.setFnrte((BigDecimal) totalesValues.get(ConstantsFunciones.CTE_9));
		prvUmic.setPrv((BigDecimal) totalesValues.get(ConstantsFunciones.CTE_10));
		prvUmic.setFnfall((BigDecimal) totalesValues.get(ConstantsFunciones.CTE_11));
		if ((!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM))  && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
			(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))){
			prvUmic.setIntfeccalc((BigDecimal) totalesValues.get(ConstantsFunciones.CTE_12));
		}
		prvUmic.setIntbti((BigDecimal) totalesValues.get(ConstantsFunciones.CTE_13));
		
		// Para las bases técnicas de SCR mostraremos la provisión de BEL en el campo prvpb
		if ((!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM))  && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN)) && 
				(!prvUmic.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM))){
			prvUmic.setPrvpb((BigDecimal) prvUmic.getPrv().subtract((BigDecimal)totalesValues.get(ConstantsFunciones.CTE_14)));
		} else {
			TotalesFlujosKey key = (TotalesFlujosKey) entry.getKey();
			TotalesFlujosKey totalesFlujoKey = new TotalesFlujosKey(key.getUmicKey().getKajuste(), key.getUmicKey().getKcertificado(), key.getUmicKey().getKgarantia(), key.getUmicKey().getKmodalidad(), key.getUmicKey().getKpoliza(), key.getUmicKey().getKprestacion(), key.getUmicKey().getKsubpoliza(), ConstantsEntregables.CTE_BT_BEL, key.getUmicKey().getNsuscri(), key.getUmicKey().getNorden(), key.getUmicKey().getCtipoaport());
			prvUmic.setPrvpb(totalesFlujosdao.get(totalesFlujoKey).getTotprovision());
		}
		prvUmic.setGestionit((String) totalesValues.get(ConstantsFunciones.CTE_15)); 
		
		if (prvUmic.getFnfall() != null && prvUmic.getPrv() != null) {
			if (prvUmic.getFnfall().compareTo(prvUmic.getPrv()) == 1) {
				prvUmic.setCapriesgo(prvUmic.getFnfall().subtract(prvUmic.getPrv()));
			} else {
				prvUmic.setCapriesgo(BigDecimal.ZERO);
			}
		}

		prvUmic.setFecinisus((Timestamp) values[ConstantsFunciones.CTE_1]);

		prvUmic.setUmicKey(((TotalesFlujosKey) entry.getKey()).getUmicKey());
//INI-TAR00400971	

		prvUmic.setSpcom((String) totalesValues.get(ConstantsFunciones.CTE_16));
		prvUmic.setKmodext((Integer) totalesValues.get(ConstantsFunciones.CTE_17));
		prvUmic.setKbencon((String) totalesValues.get(ConstantsFunciones.CTE_18));
//FIN-TAR00400971	
		return prvUmic;
	}

}
