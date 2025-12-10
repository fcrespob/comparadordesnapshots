package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
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

import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoaM;
import es.mapfre.solvencia.entregables.EntregableGenerico;
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
public class EntregableFLUJPMACOAM extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableFLUJPMACOAM.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJPMACOAM;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
//		
//		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);
//
		ValueExtractor[] detalleCorrienteExtractor = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_NSUSCRI),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FECHADESDE)};

//		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(detalleCorrienteExtractor);

//		// Values : prvinf1.prv = ∑ total.flujos.totprovision
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOPROBABLE)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMFPROB)),
		};

//		// Create Values Aggregator
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

//		// Create Entry Aggregator
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

//		// Filtrar entradas por kbasetec
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT), kbasetec);

//		// Do the query
		Map<Object, Object> pivotResults = (Map<Object, Object>) detalleEntregables.aggregate(isKbasetec, pivotAggregator);
//
		this.setProgress(pivotResults.size(), 0);
//		
		List<FlujPMaCoa> FlujPMaCoas = transformResults(pivotResults);

//		// Guardar resultados en cache
//		servicio.almacenarEntregableFlujPMaCoa(FlujPMaCoas);
		
		// Mapeamos para los entregables
		List<FlujPMaCoaM> FlujPMaCoaM = transformMap(FlujPMaCoas);
						
		// Guardar resultados en cache
		servicio.almacenarEntregableFlujPMaCoaM(FlujPMaCoaM);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FlujPMaCoa> transformResults(Map<Object, Object> pivotResults) {
		List<FlujPMaCoa> flujPMaCoa = new ArrayList<FlujPMaCoa>();
		
		FlujPMaCoa flujos = new FlujPMaCoa();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();
			flujos = servicioConfiguracion.recuperarFlujPMaCoa( (Long) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_3), 
					(Integer) keys.get(ConstantsFunciones.CTE_4), (String) keys.get(ConstantsFunciones.CTE_0), 
					(Timestamp) keys.get(ConstantsFunciones.CTE_1), (Timestamp) keys.get(ConstantsFunciones.CTE_5));
			if (flujos == null) {
				flujos = new FlujPMaCoa();
			}
			flujPMaCoa.add(transformEntry(flujos, entry));
		}

		return flujPMaCoa;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujosTotP
	 * 
	 * @param flujosTotP
	 * @param entry
	 * @return
	 */
	private static FlujPMaCoa transformEntry(FlujPMaCoa FlujPMaCoa, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		List values = (List) entry.getValue();

		BigDecimal sumfprov = (BigDecimal) values.get(ConstantsFunciones.CTE_1);
		BigDecimal gtoimpflujprov = (BigDecimal) values.get(ConstantsFunciones.CTE_0);

		if (sumfprov != null && gtoimpflujprov != null) {
			FlujPMaCoa.setTotflujoprobsingastos(sumfprov.subtract(gtoimpflujprov));
			FlujPMaCoa.setTotflujoprobdegastos(gtoimpflujprov);
		}
		
		return FlujPMaCoa;
	}
	
	/**
	 * Mapea cada entrada de los resultados a la cache flujPMaCoaM
	 * 
	 * @param flujPMaCoaM
	 * @param flujPMaCoa
	 * @return
	 */
	private static List<FlujPMaCoaM> transformMap(List<FlujPMaCoa> FlujPMaCoas) {
		
		List<FlujPMaCoaM> flujPMaCoaMs = new ArrayList<FlujPMaCoaM>();
		
		for (int i=0;i<FlujPMaCoas.size();i++) {
			FlujPMaCoa flujPMaCoa = FlujPMaCoas.get(i);
			FlujPMaCoaM flujPMaCoaM = new FlujPMaCoaM();
			flujPMaCoaM.setBt(flujPMaCoa.getBt());
			flujPMaCoaM.setKpoliza(flujPMaCoa.getKpoliza());
			flujPMaCoaM.setKsubpoliza(flujPMaCoa.getKsubpoliza());
			flujPMaCoaM.setNsuscri(flujPMaCoa.getNsuscri());
			flujPMaCoaM.setFdesde(flujPMaCoa.getFdesde());
			flujPMaCoaM.setFcierre(flujPMaCoa.getFcierre());
			flujPMaCoaM.setKcoase1(flujPMaCoa.getKcoase1());		
			flujPMaCoaM.setKcoase2(flujPMaCoa.getKcoase2());
			flujPMaCoaM.setKcoase3(flujPMaCoa.getKcoase3());
			flujPMaCoaM.setKcoase4(flujPMaCoa.getKcoase4());
			flujPMaCoaM.setKcoase5(flujPMaCoa.getKcoase5());
			flujPMaCoaM.setKcoase6(flujPMaCoa.getKcoase6());
			flujPMaCoaM.setFecefecini(flujPMaCoa.getFecefecini());
			BigDecimal Totflujoprobdegastos = flujPMaCoa.getTotflujoprobdegastos();
			String strTotflujoprobdegastos = Totflujoprobdegastos.toString().replace(".", "").replace("-", "").replace("+", "");
			String signoTotflujoprobdegastos = Totflujoprobdegastos.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMaCoaM.setTotflujoprobdegastos(signoTotflujoprobdegastos + StringUtils.leftPad(strTotflujoprobdegastos,12,"0"));
			BigDecimal Totflujoprobsingastos = flujPMaCoa.getTotflujoprobsingastos();
			String strTotflujoprobsingastos = Totflujoprobsingastos.toString().replace(".", "").replace("-", "").replace("+", "");
			String signoTotflujoprobsingastos = Totflujoprobsingastos.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMaCoaM.setTotflujoprobsingastos(signoTotflujoprobsingastos + StringUtils.leftPad(strTotflujoprobsingastos,12,"0"));
			flujPMaCoaM.setFecIniTramo1(flujPMaCoa.getFecIniTramo1());
			flujPMaCoaM.setFecFinTramo1(flujPMaCoa.getFecFinTramo1());
			flujPMaCoaM.setPintertecnI1(flujPMaCoa.getPintertecnI1());
			flujPMaCoaM.setFecIniTramo2(flujPMaCoa.getFecIniTramo2());
			flujPMaCoaM.setFecFinTramo2(flujPMaCoa.getFecFinTramo2());
			flujPMaCoaM.setPintertecnI2(flujPMaCoa.getPintertecnI2());
			flujPMaCoaM.setFecIniTramo3(flujPMaCoa.getFecIniTramo3());
			flujPMaCoaM.setFecFinTramo3(flujPMaCoa.getFecFinTramo3());
			flujPMaCoaM.setPintertecnI3(flujPMaCoa.getPintertecnI3());
			flujPMaCoaM.setFecIniTramo4(flujPMaCoa.getFecIniTramo4());
			flujPMaCoaM.setFecFinTramo4(flujPMaCoa.getFecFinTramo4());
			flujPMaCoaM.setPintertecnI4(flujPMaCoa.getPintertecnI4());
			flujPMaCoaM.setFecIniTramo5(flujPMaCoa.getFecIniTramo5());
			flujPMaCoaM.setFecFinTramo5(flujPMaCoa.getFecFinTramo5());
			flujPMaCoaM.setPintertecnI5(flujPMaCoa.getPintertecnI5());
			flujPMaCoaMs.add(i, flujPMaCoaM);
		}
		
		return flujPMaCoaMs;
	}


}
