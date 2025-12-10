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

import es.mapfre.solvencia.coherence.keys.entregables.FlujosTotPKey;
import es.mapfre.solvencia.coherence.keys.maestro.DatosAdicionalesCoaseguroKey;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTotPDao;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FlujPMaCoa;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoa;
import es.mapfre.solvencia.dominio.entregables.TotPMaCoaM;
import es.mapfre.solvencia.dominio.maestro.DatosAdicionalesCoaseguro;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.UtilFechas;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable TOTPMACOAM.
 * 
 */
public class EntregableTOTPMACOAM extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableTOTPMACOAM.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;
	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_TOTPMACOAM;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
//		NamedCache totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		ValueExtractor[] detalleCorrienteExtractor = new ValueExtractor[] {
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(Long.class, DetalleCorrienteEntregables.IND_KPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_NSUSCRI)};

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(detalleCorrienteExtractor);

		// Values : prvinf1.prv = ∑ total.flujos.totprovision
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJOACTUALIZADO)),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_TOTALFLUJOPROYECCION_SUMPROVISION)),
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
		
		List<TotPMaCoa> TotPMaCoas = transformResults(pivotResults);

		// Guardar resultados en cache
//		servicio.almacenarEntregableTotPMaCoa(TotPMaCoas);
		
		// Mapeamos para los entregables
		List<TotPMaCoaM> TotPMaCoaM = transformMap(TotPMaCoas);
				
		// Guardar resultados en cache
		servicio.almacenarEntregableTotPMaCoaM(TotPMaCoaM);

		this.setProgress(pivotResults.size(), pivotResults.size());
	}

	/**
	 * Extrae los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<TotPMaCoa> transformResults(Map<Object, Object> pivotResults) {
		List<TotPMaCoa> totPMaCoas = new ArrayList<TotPMaCoa>();
		
		TotPMaCoa flujos = new TotPMaCoa();
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List keys = (List) entry.getKey();
			flujos = servicioConfiguracion.recuperarTotPMaCoa( (Long) keys.get(ConstantsFunciones.CTE_2), (Integer) keys.get(ConstantsFunciones.CTE_3), 
					(Integer) keys.get(ConstantsFunciones.CTE_4), (String) keys.get(ConstantsFunciones.CTE_0), 
					(Timestamp) keys.get(ConstantsFunciones.CTE_1));
			if (flujos == null) {
				flujos = new TotPMaCoa();
			}
			totPMaCoas.add(transformEntry(flujos, entry));
		}

		return totPMaCoas;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache flujosTotP
	 * 
	 * @param flujosTotP
	 * @param entry
	 * @return
	 */
	private static TotPMaCoa transformEntry(TotPMaCoa TotPMaCoa, Entry<Object, Object> entry) {
		List key = (List) entry.getKey();
		List values = (List) entry.getValue();
		
		BigDecimal sumfprov = (BigDecimal) values.get(ConstantsFunciones.CTE_1);
		BigDecimal gtoimpflujact = (BigDecimal) values.get(ConstantsFunciones.CTE_0);

		if (sumfprov != null && gtoimpflujact != null) {
			TotPMaCoa.setTotprovision(sumfprov);
			TotPMaCoa.setTotfactgto(gtoimpflujact);
		}
		
		return TotPMaCoa;
	}
	
	/**
	 * Mapea cada entrada de los resultados a la cache totpmacoam
	 * 
	 * @param TotPMaCoaM
	 * @param TotPMaCoa
	 * @return
	 */
	private static List<TotPMaCoaM> transformMap(List<TotPMaCoa> TotPMaCoas) {
		
		List<TotPMaCoaM> totPMaCoaMs = new ArrayList<TotPMaCoaM>();
		
		for (int i=0;i<TotPMaCoas.size();i++) {
			TotPMaCoa totPMaCoa = TotPMaCoas.get(i);
			TotPMaCoaM totPMaCoaM = new TotPMaCoaM();
			totPMaCoaM.setBt(totPMaCoa.getBt());
			totPMaCoaM.setKpoliza(totPMaCoa.getKpoliza());
			totPMaCoaM.setKsubpoliza(totPMaCoa.getKsubpoliza());
			totPMaCoaM.setNsuscri(totPMaCoa.getNsuscri());
			totPMaCoaM.setFcierre(totPMaCoa.getFcierre());
			totPMaCoaM.setKcoase1(totPMaCoa.getKcoase1());		
			totPMaCoaM.setKcoase2(totPMaCoa.getKcoase2());
			totPMaCoaM.setKcoase3(totPMaCoa.getKcoase3());
			totPMaCoaM.setKcoase4(totPMaCoa.getKcoase4());
			totPMaCoaM.setKcoase5(totPMaCoa.getKcoase5());
			totPMaCoaM.setKcoase6(totPMaCoa.getKcoase6());
			totPMaCoaM.setFecefecini(totPMaCoa.getFecefecini());
			totPMaCoaM.setFecefecfin(totPMaCoa.getFecefecfin());
			BigDecimal Totprovision = totPMaCoa.getTotprovision();
			String strTotprovision = Totprovision.toString().replace(".", "").replace("-", "").replace("+", "");
			String signoTotprovision = Totprovision.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			totPMaCoaM.setTotprovision(signoTotprovision + StringUtils.leftPad(strTotprovision,14,"0"));
			totPMaCoaM.setFecIniTramo1(totPMaCoa.getFecIniTramo1());
			totPMaCoaM.setFecFinTramo1(totPMaCoa.getFecFinTramo1());
			totPMaCoaM.setPintertecnI1(totPMaCoa.getPintertecnI1());
			totPMaCoaM.setFecIniTramo2(totPMaCoa.getFecIniTramo2());
			totPMaCoaM.setFecFinTramo2(totPMaCoa.getFecFinTramo2());
			totPMaCoaM.setPintertecnI2(totPMaCoa.getPintertecnI2());
			totPMaCoaM.setFecIniTramo3(totPMaCoa.getFecIniTramo3());
			totPMaCoaM.setFecFinTramo3(totPMaCoa.getFecFinTramo3());
			totPMaCoaM.setPintertecnI3(totPMaCoa.getPintertecnI3());
			totPMaCoaM.setFecIniTramo4(totPMaCoa.getFecIniTramo4());
			totPMaCoaM.setFecFinTramo4(totPMaCoa.getFecFinTramo4());
			totPMaCoaM.setPintertecnI4(totPMaCoa.getPintertecnI4());
			totPMaCoaM.setFecIniTramo5(totPMaCoa.getFecIniTramo5());
			totPMaCoaM.setFecFinTramo5(totPMaCoa.getFecFinTramo5());
			totPMaCoaM.setPintertecnI5(totPMaCoa.getPintertecnI5());
			totPMaCoaM.setPgastgesin1I(totPMaCoa.getPgastgesin1I());
			totPMaCoaM.setPgastgesin2I(totPMaCoa.getPgastgesin2I());
			BigDecimal Pgastgesex1I = totPMaCoa.getPgastgesex1I();
			String strPgastgesex1I = Pgastgesex1I.toString().replace(".", "");
			totPMaCoaM.setPgastgesex1I(StringUtils.leftPad(strPgastgesex1I,5,"0"));
			BigDecimal Pgastgesex2I = totPMaCoa.getPgastgesex2I();
			String strPgastgesex2I = Pgastgesex2I.toString().replace(".", "");
			totPMaCoaM.setPgastgesex2I(StringUtils.leftPad(strPgastgesex2I,5,"0"));
			BigDecimal Factor1 = totPMaCoa.getFactor1();
			String strFactor1 = Factor1.toString().replace(".", "");
			totPMaCoaM.setFactor1(new BigDecimal(StringUtils.leftPad(strFactor1,6,"0")));
			totPMaCoaM.setTablacalc1aseg1(StringUtils.leftPad(totPMaCoa.getTablacalc1aseg1(),5,"0"));
			BigDecimal Totfactgto = totPMaCoa.getTotfactgto();
			String strTotfactgto = Totfactgto.toString().replace(".", "").replace("-", "").replace("+", "");
			String signoTotfactgto = Totfactgto.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			totPMaCoaM.setTotfactgto(signoTotfactgto + StringUtils.leftPad(strTotfactgto,14,"0"));
			totPMaCoaMs.add(i, totPMaCoaM);
		}
		
		return totPMaCoaMs;
	}
}
