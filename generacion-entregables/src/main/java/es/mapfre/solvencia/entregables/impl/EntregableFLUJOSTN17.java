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
import es.mapfre.solvencia.dominio.entregables.FlujosTN17;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregableFLUJOSTN17 extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;
	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJOSTN17;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {

		this.initProgress(oEnvironment);

		//NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);
		
		ValueExtractor[] rows = new ValueExtractor[] {
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCARTERA),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(String.class, TotalesFlujos.IND_UOA),
				new PofExtractor(String.class, TotalesFlujos.IND_KCONTRATO),
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_NSUSCRI),
				new PofExtractor(Integer.class, TotalesFlujos.IND_NORDEN),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KGARANTIA),
				new PofExtractor(String.class, TotalesFlujos.IND_KPRESTACION),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KAJUSTE),
				new PofExtractor(String.class, TotalesFlujos.IND_CTIPOAPORT),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FSUSCRI),
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODEXT),
				new PofExtractor(String.class, TotalesFlujos.IND_SPCOM),
				new PofExtractor(String.class, TotalesFlujos.IND_KBENCON)};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_INTFECCALC)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPVIDA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNAVIDA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTVIDA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTOCOLAVIDA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPFALL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNAFALL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTFALL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLAFALL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPCOMPL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNACOMPL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTCOMPL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLACOMPL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPGTO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNAGTO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTGTO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLAGTO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPCOM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNACOM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTCOM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLACOM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPRTE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNARTE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTRTE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLARTE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPPRIM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNAPRIM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTPRIM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLAPRIM)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPROB)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPROBTANUL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLA)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PROVBTIFCAL)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_RAUMIC)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_CSMUMIC)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_CSMAJUSTADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_CSM003)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPGTOAD)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFPNAGTOAD)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTFACTGTOAD)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTCOLAGTOAD)),
				};

		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec,
				pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<FlujosTN17> flujosTotNiif17 = transformResults(pivotResults);

		// Almacenar en cache
		//servicio.almacenarEntregableFlujosTN17(flujosTotNiif17);

		//this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujosTN17> transformResults(Map<Object, Object> pivotResults) {
		List<FlujosTN17> flujosTotNiif17 = new ArrayList<FlujosTN17>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujosTotNiif17.add(transformEntry(new FlujosTN17(), entry));
		}

		return flujosTotNiif17;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache FlujosTotNiif17Key
	 * 
	 * @param flujosTotNiif17Key
	 * @param entry
	 * @return
	 */
	private FlujosTN17 transformEntry(FlujosTN17 flujosTotNiif17, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		flujosTotNiif17.setCnegocio((String) key.get(ConstantsFunciones.CTE_0));
		flujosTotNiif17.setCcanal((Integer) key.get(ConstantsFunciones.CTE_1));
		flujosTotNiif17.setCcartera((Integer) key.get(ConstantsFunciones.CTE_2));
		flujosTotNiif17.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_3));
		flujosTotNiif17.setBt((String) key.get(ConstantsFunciones.CTE_4));
		flujosTotNiif17.setUoa((String) key.get(ConstantsFunciones.CTE_5));
		flujosTotNiif17.setKcontrato((String) key.get(ConstantsFunciones.CTE_6));
		flujosTotNiif17.setKramo((String) key.get(ConstantsFunciones.CTE_7));
		flujosTotNiif17.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_8));
		flujosTotNiif17.setKpoliza((Long) key.get(ConstantsFunciones.CTE_9));
		flujosTotNiif17.setKsubpoliza((Integer) key.get(ConstantsFunciones.CTE_10));
		flujosTotNiif17.setKcertificado((Integer) key.get(ConstantsFunciones.CTE_11));
		flujosTotNiif17.setNsuscri((Integer) key.get(ConstantsFunciones.CTE_12));
		flujosTotNiif17.setNorden((Integer) key.get(ConstantsFunciones.CTE_13));
		flujosTotNiif17.setKgarantia((Integer) key.get(ConstantsFunciones.CTE_14));
		flujosTotNiif17.setKprestacion((String) key.get(ConstantsFunciones.CTE_15));
		flujosTotNiif17.setKajuste((Integer) key.get(ConstantsFunciones.CTE_16));
		flujosTotNiif17.setCtipoaport((String) key.get(ConstantsFunciones.CTE_17));
		flujosTotNiif17.setFsuscri((Timestamp) key.get(ConstantsFunciones.CTE_18));
		flujosTotNiif17.setKcarterainv((String) key.get(ConstantsFunciones.CTE_19));
		flujosTotNiif17.setGapact((String) key.get(ConstantsFunciones.CTE_20));
		flujosTotNiif17.setKmodext((Integer) key.get(ConstantsFunciones.CTE_21));
		flujosTotNiif17.setSpcom((String) key.get(ConstantsFunciones.CTE_22));
		
//		cnegocio, ccanal, ccartera, fcierre, bt, uoa, kcontrato, kramo, kmodalidad, kpoliza,
//		ksubpoliza, kcertificado, nsuscri, norden, kgarantia, kprestacion, kajuste, ctipoaport, kcarterainv,gapact,kmodext,spcom
		

		List values = (List) entry.getValue();
		flujosTotNiif17.setIntfeccal((BigDecimal) values.get(ConstantsFunciones.CTE_0));
		flujosTotNiif17.setTotfpvida((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		flujosTotNiif17.setTotfpnavida((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		flujosTotNiif17.setTotfactvida((BigDecimal) values.get(ConstantsFunciones.CTE_3));
		flujosTotNiif17.setTotcolavida((BigDecimal) values.get(ConstantsFunciones.CTE_4));
		flujosTotNiif17.setTotfpfall((BigDecimal) values.get(ConstantsFunciones.CTE_5));
		flujosTotNiif17.setTotfpnafall((BigDecimal) values.get(ConstantsFunciones.CTE_6));
		flujosTotNiif17.setTotfactfall((BigDecimal) values.get(ConstantsFunciones.CTE_7));
		flujosTotNiif17.setTotcolafall((BigDecimal) values.get(ConstantsFunciones.CTE_8));
		flujosTotNiif17.setTotfpcompl((BigDecimal) values.get(ConstantsFunciones.CTE_9));
		flujosTotNiif17.setTotfpnacompl((BigDecimal) values.get(ConstantsFunciones.CTE_10));
		flujosTotNiif17.setTotfactcompl((BigDecimal) values.get(ConstantsFunciones.CTE_11));
		flujosTotNiif17.setTotcolacompl((BigDecimal) values.get(ConstantsFunciones.CTE_12));
		flujosTotNiif17.setTotfpgto((BigDecimal) values.get(ConstantsFunciones.CTE_13));
		flujosTotNiif17.setTotfpnagto((BigDecimal) values.get(ConstantsFunciones.CTE_14));
		flujosTotNiif17.setTotfactgto((BigDecimal) values.get(ConstantsFunciones.CTE_15));
		flujosTotNiif17.setTotcolagto((BigDecimal) values.get(ConstantsFunciones.CTE_16));
		flujosTotNiif17.setTotfpcom((BigDecimal) values.get(ConstantsFunciones.CTE_17));
		flujosTotNiif17.setTotfpnacom((BigDecimal) values.get(ConstantsFunciones.CTE_18));
		flujosTotNiif17.setTotfactcom((BigDecimal) values.get(ConstantsFunciones.CTE_19));
		flujosTotNiif17.setTotcolacom((BigDecimal) values.get(ConstantsFunciones.CTE_20));
		flujosTotNiif17.setTotfprte((BigDecimal) values.get(ConstantsFunciones.CTE_21));
		flujosTotNiif17.setTotfpnarte((BigDecimal) values.get(ConstantsFunciones.CTE_22));
		flujosTotNiif17.setTotfactrte((BigDecimal) values.get(ConstantsFunciones.CTE_23));
		flujosTotNiif17.setTotcolarte((BigDecimal) values.get(ConstantsFunciones.CTE_24));
		flujosTotNiif17.setTotfpprim((BigDecimal) values.get(ConstantsFunciones.CTE_25));
		flujosTotNiif17.setTotfpnaprim((BigDecimal) values.get(ConstantsFunciones.CTE_26));
		flujosTotNiif17.setTotfactprim((BigDecimal) values.get(ConstantsFunciones.CTE_27));
		flujosTotNiif17.setTotcolaprim((BigDecimal) values.get(ConstantsFunciones.CTE_28));
		flujosTotNiif17.setTotfprob((BigDecimal) values.get(ConstantsFunciones.CTE_29));
		flujosTotNiif17.setTotfprobtanul((BigDecimal) values.get(ConstantsFunciones.CTE_30));
		flujosTotNiif17.setTotprovision((BigDecimal) values.get(ConstantsFunciones.CTE_31));
		flujosTotNiif17.setTotcola((BigDecimal) values.get(ConstantsFunciones.CTE_32));
		flujosTotNiif17.setProvfcal((BigDecimal) values.get(ConstantsFunciones.CTE_33));
		flujosTotNiif17.setTotra((BigDecimal) values.get(ConstantsFunciones.CTE_34));
		flujosTotNiif17.setTotcsm((BigDecimal) values.get(ConstantsFunciones.CTE_35));
		flujosTotNiif17.setTotcsmpatron((BigDecimal) values.get(ConstantsFunciones.CTE_36));
		flujosTotNiif17.setTotcsmrossp((BigDecimal) values.get(ConstantsFunciones.CTE_37));
		flujosTotNiif17.setTotfpgtoad((BigDecimal) values.get(ConstantsFunciones.CTE_38));
		flujosTotNiif17.setTotfpnagtoad((BigDecimal) values.get(ConstantsFunciones.CTE_39));
		flujosTotNiif17.setTotfactgtoad((BigDecimal) values.get(ConstantsFunciones.CTE_40));
		flujosTotNiif17.setTotcolagtoad((BigDecimal) values.get(ConstantsFunciones.CTE_41));
		return flujosTotNiif17;
	}
}