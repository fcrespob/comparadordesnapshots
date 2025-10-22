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

import es.mapfre.solvencia.dominio.entregables.FlujInf4;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregableFLUJINF4 extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJINF4;
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
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FPROYFLUJEST),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KCARTERAINV),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KMODALIDAD)
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

		List<FlujInf4> flujinf4s = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableFlujInf4(flujinf4s);

		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujInf4> transformResults(Map<Object, Object> pivotResults) {
		List<FlujInf4> flujinf4s = new ArrayList<FlujInf4>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			flujinf4s.add(transformEntry(new FlujInf4(), entry));
		}

		return flujinf4s;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujinf4
	 * 
	 * @param flujinf4
	 * @param entry
	 * @return
	 */
	private FlujInf4 transformEntry(FlujInf4 flujinf4, Entry<Object, Object> entry) {

		List key = (List) entry.getKey();
		if(null == (String) key.get(ConstantsFunciones.CTE_0)){
			flujinf4.setBt((String) key.get(ConstantsFunciones.CTE_0));
		}else{
			flujinf4.setBt((String) key.get(ConstantsFunciones.CTE_0));
		}
		
		flujinf4.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_1));
		flujinf4.setCcanal((Integer) key.get(ConstantsFunciones.CTE_2));
		if(null == (String) key.get(ConstantsFunciones.CTE_3)){
			flujinf4.setNegocio("");
		}else{
			flujinf4.setNegocio((String) key.get(ConstantsFunciones.CTE_3));
		}
		
		flujinf4.setUoa((String) key.get(ConstantsFunciones.CTE_4));
		if(null == (String) key.get(ConstantsFunciones.CTE_5)){
			flujinf4.setKcarteraContrato("");
		}else{
			flujinf4.setKcarteraContrato((String) key.get(ConstantsFunciones.CTE_5));
		}
		
		if(null == (String) key.get(ConstantsFunciones.CTE_6)){
			flujinf4.setKcarteraCohort("");
		}else{
			flujinf4.setKcarteraCohort((String) key.get(ConstantsFunciones.CTE_6));
		}
		
		if(null == (String) key.get(ConstantsFunciones.CTE_7)){
			flujinf4.setKcarteraOner("");
		}else{
			flujinf4.setKcarteraOner((String) key.get(ConstantsFunciones.CTE_7));
		}
		
		flujinf4.setFproyflujest((Timestamp) key.get(ConstantsFunciones.CTE_8));
		if(null == (String) key.get(ConstantsFunciones.CTE_9)){
			flujinf4.setKcarterainv("");
		}else{
			flujinf4.setKcarterainv((String) key.get(ConstantsFunciones.CTE_9));
		}
		
		flujinf4.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_10));

		List values = (List) entry.getValue();
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_0)){
			flujinf4.setTotFpVida(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpVida((BigDecimal) values.get(ConstantsFunciones.CTE_0));

		}
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_1)){
			flujinf4.setTotFpFall(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpFall((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_2)){
			flujinf4.setTotFpGastos(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpGastos((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_3)){
			flujinf4.setTotFpComisiones(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpComisiones((BigDecimal) values.get(ConstantsFunciones.CTE_3));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_4)){
			flujinf4.setTotFpRescates(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpRescates((BigDecimal) values.get(ConstantsFunciones.CTE_4));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_5)){
			flujinf4.setTotFpCompl(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpCompl((BigDecimal) values.get(ConstantsFunciones.CTE_5));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_6)){
			flujinf4.setTotFpPrimas(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpPrimas((BigDecimal) values.get(ConstantsFunciones.CTE_6));
		}
		
		if(null == (BigDecimal) values.get(ConstantsFunciones.CTE_7)){
			flujinf4.setTotFpGtoAd(BigDecimal.ZERO);
		}else{
			flujinf4.setTotFpGtoAd((BigDecimal) values.get(ConstantsFunciones.CTE_7));
		}
		
		return flujinf4;
	}
}
