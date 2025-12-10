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
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoa;
import es.mapfre.solvencia.dominio.entregables.FlujPMdCoaM;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable flujPMdCoa.
 * 
 */
public class EntregableFLUJPMDCOAM extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableTOTPMACOA.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;
	private NamedCache detalleEntregables = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
	
	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FLUJPMDCOAM;
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
//		servicio.almacenarEntregableFlujPMdCoa(FlujPMdCoas);
		
		// Mapeamos para los entregables
		List<FlujPMdCoaM> FlujPMdCoaM = transformMap(FlujPMdCoas);
								
		// Guardar resultados en cache
		servicio.almacenarEntregableFlujPMdCoaM(FlujPMdCoaM);

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
			if (flujos == null) {
				flujos = new FlujPMdCoa();
			}
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
	
	/**
	 * Mapea cada entrada de los resultados a la cache flujPMdCoaM
	 * 
	 * @param flujPMdCoaM
	 * @param flujPMdCoa
	 * @return
	 */
	private static List<FlujPMdCoaM> transformMap(List<FlujPMdCoa> FlujPMdCoas) {
		
		List<FlujPMdCoaM> flujPMdCoaMs = new ArrayList<FlujPMdCoaM>();
		
		for (int i=0;i<FlujPMdCoas.size();i++) {
			FlujPMdCoa flujPMdCoa = FlujPMdCoas.get(i);
			FlujPMdCoaM flujPMdCoaM = new FlujPMdCoaM();
			flujPMdCoaM.setBt(flujPMdCoa.getBt());
			flujPMdCoaM.setKpoliza(flujPMdCoa.getKpoliza());
			flujPMdCoaM.setKsubpoliza(flujPMdCoa.getKsubpoliza());
			flujPMdCoaM.setNsuscri(flujPMdCoa.getNsuscri());
			flujPMdCoaM.setKprestacion(flujPMdCoa.getKprestacion());
			flujPMdCoaM.setKcertificado(flujPMdCoa.getKcertificado());
			flujPMdCoaM.setKmodalidad(flujPMdCoa.getKmodalidad());
			flujPMdCoaM.setKgarantia(flujPMdCoa.getKgarantia());
			flujPMdCoaM.setFcierre(flujPMdCoa.getFcierre());
			flujPMdCoaM.setKcoase1(flujPMdCoa.getKcoase1());		
			flujPMdCoaM.setKcoase2(flujPMdCoa.getKcoase2());
			flujPMdCoaM.setKcoase3(flujPMdCoa.getKcoase3());
			flujPMdCoaM.setKcoase4(flujPMdCoa.getKcoase4());
			flujPMdCoaM.setKcoase5(flujPMdCoa.getKcoase5());
			flujPMdCoaM.setKcoase6(flujPMdCoa.getKcoase6());
			flujPMdCoaM.setFecefecini(flujPMdCoa.getFecefecini());
			flujPMdCoaM.setFecefecfin(flujPMdCoa.getFecefecfin());
			BigDecimal Totprovision = flujPMdCoa.getTotprovision();
			String strTotprovision = Totprovision.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Totprovision.toString().contains(".")) {
				strTotprovision = strTotprovision + "00";
			} else if (Totprovision.scale() == 1){
				strTotprovision = strTotprovision + "0";
			}
			String signoTotprovision = Totprovision.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setTotprovision(signoTotprovision + StringUtils.leftPad(strTotprovision,14,"0"));
			flujPMdCoaM.setTablacalc1aseg1(flujPMdCoa.getTablacalc1aseg1());
			flujPMdCoaM.setFecIniTramo1(flujPMdCoa.getFecIniTramo1());
			flujPMdCoaM.setFecFinTramo1(flujPMdCoa.getFecFinTramo1());
			flujPMdCoaM.setPintertecnI1(flujPMdCoa.getPintertecnI1());
			flujPMdCoaM.setFecIniTramo2(flujPMdCoa.getFecIniTramo2());
			flujPMdCoaM.setFecFinTramo2(flujPMdCoa.getFecFinTramo2());
			flujPMdCoaM.setPintertecnI2(flujPMdCoa.getPintertecnI2());
			flujPMdCoaM.setFecIniTramo3(flujPMdCoa.getFecIniTramo3());
			flujPMdCoaM.setFecFinTramo3(flujPMdCoa.getFecFinTramo3());
			flujPMdCoaM.setPintertecnI3(flujPMdCoa.getPintertecnI3());
			flujPMdCoaM.setFecIniTramo4(flujPMdCoa.getFecIniTramo4());
			flujPMdCoaM.setFecFinTramo4(flujPMdCoa.getFecFinTramo4());
			flujPMdCoaM.setPintertecnI4(flujPMdCoa.getPintertecnI4());
			flujPMdCoaM.setFecIniTramo5(flujPMdCoa.getFecIniTramo5());
			flujPMdCoaM.setFecFinTramo5(flujPMdCoa.getFecFinTramo5());
			flujPMdCoaM.setPintertecnI5(flujPMdCoa.getPintertecnI5());
			flujPMdCoaM.setPgastgesin1I(flujPMdCoa.getPgastgesin1I());
			flujPMdCoaM.setPgastgesin2I(flujPMdCoa.getPgastgesin2I());
			flujPMdCoaM.setPgastgesin3I(flujPMdCoa.getPgastgesin3I());
			BigDecimal GtorosspCap = flujPMdCoa.getGtorosspCap();
			String strGtorosspCap = GtorosspCap.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!GtorosspCap.toString().contains(".")) {
				strGtorosspCap = strGtorosspCap + "0000";
			}
			flujPMdCoaM.setGtorosspCap(StringUtils.leftPad(strGtorosspCap,8,"0"));
			BigDecimal GtorosspPrima = flujPMdCoa.getGtorosspPrima();
			String strGtorosspPrima = GtorosspPrima.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!GtorosspPrima.toString().contains(".")) {
				strGtorosspPrima = strGtorosspPrima + "0000";
			}
			flujPMdCoaM.setGtorosspPrima(StringUtils.leftPad(strGtorosspPrima,8,"0"));
			BigDecimal GtorosspProv = flujPMdCoa.getGtorosspProv();
			String strGtorosspProv = GtorosspProv.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!GtorosspProv.toString().contains(".")) {
				strGtorosspProv = strGtorosspProv + "00000";
			}
			flujPMdCoaM.setGtorosspProv(StringUtils.leftPad(strGtorosspProv,8,"0"));
			flujPMdCoaM.setFnacAseg1(flujPMdCoa.getFnacAseg1());
			flujPMdCoaM.setFnacAseg2(flujPMdCoa.getFnacAseg2());
			flujPMdCoaM.setFnacAseg3(flujPMdCoa.getFnacAseg3());
			flujPMdCoaM.setFnacAseg4(flujPMdCoa.getFnacAseg4());
			flujPMdCoaM.setFnacAseg5(flujPMdCoa.getFnacAseg5());
			flujPMdCoaM.setCsexAseg1(flujPMdCoa.getCsexAseg1());
			flujPMdCoaM.setCsexAseg2(flujPMdCoa.getCsexAseg2());
			flujPMdCoaM.setCsexAseg3(flujPMdCoa.getCsexAseg3());
			flujPMdCoaM.setCsexAseg4(flujPMdCoa.getCsexAseg4());
			flujPMdCoaM.setCsexAseg5(flujPMdCoa.getCsexAseg5());
			flujPMdCoaM.setCestadoAseg1(flujPMdCoa.getCestadoAseg1());
			flujPMdCoaM.setCestadoAseg2(flujPMdCoa.getCestadoAseg2());
			flujPMdCoaM.setCestadoAseg3(flujPMdCoa.getCestadoAseg3());
			flujPMdCoaM.setCestadoAseg4(flujPMdCoa.getCestadoAseg4());
			flujPMdCoaM.setCestadoAseg5(flujPMdCoa.getCestadoAseg5());
			BigDecimal Iprimanetaini = flujPMdCoa.getIprimanetaini();
			String strIprimanetaini = Iprimanetaini.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Iprimanetaini.toString().contains(".")) {
				strIprimanetaini = strIprimanetaini + "00";
			} else if (Iprimanetaini.scale() == 1){
				strIprimanetaini = strIprimanetaini + "0";
			}
			String signoIprimanetaini = Iprimanetaini.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIprimanetaini(signoIprimanetaini + StringUtils.leftPad(strIprimanetaini,12,"0"));
			BigDecimal Iprimanetaact = flujPMdCoa.getIprimanetaact();
			String strIprimanetaact = Iprimanetaact.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Iprimanetaact.toString().contains(".")) {
				strIprimanetaact = strIprimanetaact + "00";
			} else if (Iprimanetaact.scale() == 1){
				strIprimanetaact = strIprimanetaact + "0";
			}
			String signoIprimanetaact = Iprimanetaact.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIprimanetaact(signoIprimanetaact + StringUtils.leftPad(strIprimanetaact,12,"0"));
			BigDecimal Iprimatarada = flujPMdCoa.getIprimatarada();
			String strIprimatarada = Iprimatarada.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Iprimatarada.toString().contains(".")) {
				strIprimatarada = strIprimatarada + "00";
			} else if (Iprimatarada.scale() == 1){
				strIprimatarada = strIprimatarada + "0";
			}
			String signoIprimatarada = Iprimatarada.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIprimatarada(signoIprimatarada + StringUtils.leftPad(strIprimatarada,12,"0"));
			BigDecimal Icapini = flujPMdCoa.getIcapini();
			String strIcapini = Icapini.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Icapini.toString().contains(".")) {
				strIcapini = strIcapini + "00";
			} else if (Icapini.scale() == 1){
				strIcapini = strIcapini + "0";
			}
			String signoIcapini = Icapini.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIcapini(signoIcapini + StringUtils.leftPad(strIcapini,12,"0"));
			BigDecimal Icapact = flujPMdCoa.getIcapact();
			String strIcapact = Icapact.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Icapact.toString().contains(".")) {
				strIcapact = strIcapact + "00";
			} else if (Icapact.scale() == 1){
				strIcapact = strIcapact + "0";
			}
			String signoIcapact = Icapact.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIcapact(signoIcapact + StringUtils.leftPad(strIcapact,12,"0"));
			BigDecimal Isaldo = flujPMdCoa.getIsaldo();
			String strIsaldo = Isaldo.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Isaldo.toString().contains(".")) {
				strIsaldo = strIsaldo + "00";
			} else if (Isaldo.scale() == 1){
				strIsaldo = strIsaldo + "0";
			}
			String signoIsaldo = Isaldo.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setIsaldo(signoIsaldo + StringUtils.leftPad(strIsaldo,13,"0"));
			flujPMdCoaM.setFecIni(flujPMdCoa.getFecIni());
			flujPMdCoaM.setFecFin(flujPMdCoa.getFecFin());
			flujPMdCoaM.setTempVit(flujPMdCoa.getTempVit());
			flujPMdCoaM.setPrevrenta(flujPMdCoa.getPrevrenta());
			flujPMdCoaM.setPreversion(flujPMdCoa.getPreversion());
			flujPMdCoaM.setNpergaran(flujPMdCoa.getNpergaran());
			flujPMdCoaM.setNadifer(flujPMdCoa.getNadifer());
			flujPMdCoaM.setForpagrent(flujPMdCoa.getForpagrent());
			flujPMdCoaM.setCpagrenta(flujPMdCoa.getCpagrenta());
			flujPMdCoaM.setCtipoRevrenta(flujPMdCoa.getCtipoRevrenta());
			flujPMdCoaM.setCformaRevrenta(flujPMdCoa.getCformaRevrenta());
			flujPMdCoaM.setNdurrenta(flujPMdCoa.getNdurrenta());
			flujPMdCoaM.setKajuste(flujPMdCoa.getKajuste());
			flujPMdCoaM.setPsobremort(flujPMdCoa.getPsobremort());
			flujPMdCoaM.setPriesgo(flujPMdCoa.getPriesgo());
			flujPMdCoaM.setGvalor(flujPMdCoa.getGvalor());
			flujPMdCoaM.setFdiadepago(flujPMdCoa.getFdiadepago());
			flujPMdCoaM.setFecinisus(flujPMdCoa.getFecinisus());
			BigDecimal Rentini = flujPMdCoa.getRentini();
			String strRentini = Rentini.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Rentini.toString().contains(".")) {
				strRentini = strRentini + "00";
			} else if (Rentini.scale() == 1){
				strRentini = strRentini + "0";
			}
			
			String signoRentini = Rentini.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setRentini(signoRentini + StringUtils.leftPad(strRentini,13,"0"));
			BigDecimal Pgastgesex1I = flujPMdCoa.getPgastgesex1I();
			String strPgastgesex1I = Pgastgesex1I.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Pgastgesex1I.toString().contains(".")) {
				strPgastgesex1I = strPgastgesex1I + "00";
			} else if (Pgastgesex1I.scale() == 1){
				strPgastgesex1I = strPgastgesex1I + "0";
			}
			flujPMdCoaM.setPgastgesex1I(StringUtils.leftPad(strPgastgesex1I,5,"0"));
			BigDecimal Pgastgesex2I = flujPMdCoa.getPgastgesex2I();
			String strPgastgesex2I = Pgastgesex2I.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Pgastgesex2I.toString().contains(".")) {
				strPgastgesex2I = strPgastgesex2I + "00";
			} else if (Pgastgesex2I.scale() == 1){
				strPgastgesex2I = strPgastgesex2I + "0";
			}
			flujPMdCoaM.setPgastgesex2I(StringUtils.leftPad(strPgastgesex2I,5,"0"));
			BigDecimal Totfactgto = flujPMdCoa.getTotfactgto();
			String strTotfactgto = Totfactgto.toString().replace(".", "").replace("-", "").replace("+", "");
			if (!Totfactgto.toString().contains(".")) {
				strTotfactgto = strTotfactgto + "00";
			} else if (Totfactgto.scale() == 1){
				strTotfactgto = strTotfactgto + "0";
			}
			String signoTotfactgto = Totfactgto.compareTo(BigDecimal.ZERO) < 0 ? "-" : "+";
			flujPMdCoaM.setTotfactgto(signoTotfactgto + StringUtils.leftPad(strTotfactgto,14,"0"));
			flujPMdCoaM.setCformarevprim(flujPMdCoa.getCformarevprim());
			flujPMdCoaM.setPrevprima(flujPMdCoa.getPrevprima());
			flujPMdCoaM.setCformpago(flujPMdCoa.getCformpago());
			flujPMdCoaM.setFecinipagprim(flujPMdCoa.getFecinipagprim());
			flujPMdCoaM.setFecfinpagprim(flujPMdCoa.getFecfinpagprim());
			flujPMdCoaM.setGedadmax(flujPMdCoa.getGedadmax());
			flujPMdCoaM.setPb(flujPMdCoa.getPb());
			flujPMdCoaM.setPb(flujPMdCoa.getTipopb());
			flujPMdCoaMs.add(i, flujPMdCoaM);
		}
		
		return flujPMdCoaMs;
	}
}
