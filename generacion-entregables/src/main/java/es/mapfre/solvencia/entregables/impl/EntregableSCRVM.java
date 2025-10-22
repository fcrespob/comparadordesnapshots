package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

import es.mapfre.solvencia.dao.impl.scr.ValoresEstresDao;
import es.mapfre.solvencia.dominio.ConstantesSolvencia;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.dominio.scr.ValoresEstres;
import es.mapfre.solvencia.dominio.scr.entregables.FactoresVolatilidad;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.Util;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

public class EntregableSCRVM extends EntregableGenerico{

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableSCRVM.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private ValoresEstresDao valoresEstresDao = new ValoresEstresDao();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_SCRVM;
	}
	
	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);

		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		// totales-flujos cache extractors
		ValueExtractor[] rows = new ValueExtractor[] { new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD)};

		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Create Values Processor
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_QIXI2)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_QIXI3)) };

		// Create CompositeAggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
		
		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Do the Query
		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(pivotResults.size(), 0);

		List<FactoresVolatilidad> factoresVolatilidad = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableSCRVM(factoresVolatilidad);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}
	
	/**
	 * Extraer los resultados de los procesadores
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FactoresVolatilidad> transformResults(Map<Object, Object> pivotResults) {
		List<FactoresVolatilidad> factoresVolatilidad = new ArrayList<FactoresVolatilidad>();
		FactoresVolatilidad factores = null;
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			factores = transformEntry(new FactoresVolatilidad(), entry);
			if (null != factores){
				factoresVolatilidad.add(factores);
			}
		}

		return factoresVolatilidad;
	}

	/**
	 * Mapea cada entrada de resultados a la cache PRVUMIC
	 * 
	 * @param factores
	 * @param entry
	 * @return
	 */
	private FactoresVolatilidad transformEntry(FactoresVolatilidad factores, Entry<Object, Object> entry) {

		BigDecimal varVm1;
		BigDecimal varVm2;
		
		List key = (List) entry.getKey();
		
		factores.setBt((String) key.get(ConstantsFunciones.CTE_0));
		factores.setCcanal((Integer) key.get(ConstantsFunciones.CTE_1));
		factores.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_3));
		
		List<ValoresEstres> valEstres = valoresEstresDao.obtenerValoresEstres((Timestamp) key.get(ConstantsFunciones.CTE_2), (String) key.get(ConstantsFunciones.CTE_0));
		
		if (valEstres.get(0).getVariable().equals(ConstantesSolvencia.VAR_VM1)){
			varVm1 = valEstres.get(0).getValor();
			varVm2 = valEstres.get(1).getValor();
		} else {
			varVm1 = valEstres.get(1).getValor();
			varVm2 = valEstres.get(0).getValor();
		}
		
		List values = (List) entry.getValue();
		BigDecimal varqiXi2 = (BigDecimal) values.get(ConstantsFunciones.CTE_0);
		BigDecimal varqiXi3 = (BigDecimal) values.get(ConstantsFunciones.CTE_1);
		
		if (null == varqiXi2 || null == varqiXi3){
			return null;
		}
		if (varqiXi2.equals(BigDecimal.ZERO)){
			factores.setFactor1(BigDecimal.ZERO);
			factores.setFactor2(BigDecimal.ZERO);
			factores.setC(BigDecimal.ZERO);
		} else {
			factores.setFactor1(Util.pow(varqiXi2, ConstantsFunciones.CTE_OPER_0_PUNTO_5));
			factores.setFactor2(varqiXi3.divide(Util.pow(factores.getFactor1(), ConstantsFunciones.CTE_OPER_3),ConstantsFunciones.MATH_CONTEXT));
			factores.setC(factores.getFactor1().multiply(varVm1.add(varVm2.multiply(factores.getFactor2()))).setScale(2,RoundingMode.HALF_UP));
		}
		
		return factores;
	}
}
