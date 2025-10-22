package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.io.pof.annotation.PortableProperty;
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

import es.mapfre.solvencia.coherence.keys.entregables.FPSLKey;
import es.mapfre.solvencia.coherence.serialization.codec.BigDecimalSolvenciaCodec;
import es.mapfre.solvencia.dao.impl.entregables.FlujosTotPDao;
import es.mapfre.solvencia.dominio.entregables.DetalleCorrienteEntregables;
import es.mapfre.solvencia.dominio.entregables.FPSL;
import es.mapfre.solvencia.dominio.entregables.FlujosTotP;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.excepcion.helper.Solvencia2ExcepcionHelper;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.formulacion.util.ConstantsModulos;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVINF1.
 * 
 */
public class EntregableFPSL extends EntregableGenerico {

	final IAlmacenarDatos servicio = FachadaServicios.getAlmacenarDatos();

	private static final Logger LOG = LoggerFactory.getLogger(EntregableFPSL.class);
	private static final String CACHE_DETALLE_CORRIENTE_ENTREGABLES = ConstantsEntregables.CACHE_DETALLE_CORRIENTE_ENTREGABLES;

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_FPSL;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {

		this.initProgress(oEnvironment);

		NamedCache totales = CacheFactory.getCache(CACHE_DETALLE_CORRIENTE_ENTREGABLES);
		
		
		ValueExtractor[] rows = new ValueExtractor[] {
				// BT
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FCIERRE),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_KUOA),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_TEXTRACCION),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_TPASIVO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_TNEGOCIO),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_TNEGOCIOLRC),
				//new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_PVENTA),
				new PofExtractor(Integer.class, DetalleCorrienteEntregables.IND_CODREASEG),
				new PofExtractor(Timestamp.class, DetalleCorrienteEntregables.IND_FPROYFLUJEST),
				new PofExtractor(String.class, DetalleCorrienteEntregables.IND_CNEGOCIO),
				};

		// Rows
		ValueExtractor multiExtractor = new MultiExtractor(rows);

		// Values : prvinf1.prv = ∑ total.flujos.totprovision
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
			new Count(),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEPRIM_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUERTE_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEFALL_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEVIDA_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTO_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMPL_IMPFLUJONOAULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUECOMI_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_PB)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_BLOQUEGTOAD_IMPFLUJONOANULADO)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_PRIMAPERIODICA)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_OGA)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_SIPRIMA)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_COMI)),
			new BigDecimalSum(new PofExtractor(BigDecimal.class, DetalleCorrienteEntregables.IND_SIOGA)),
 
		};
		
		//gasto adquisicion
		//pb
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
 
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, DetalleCorrienteEntregables.IND_BT),
				kbasetec);

		Map<Object, Object> pivotResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);
		
		this.setProgress(pivotResults.size(), 0);

		List<FPSL> fpsl = transformResults(pivotResults);

		// Almacenar en cache
		servicio.almacenarEntregableFPSL(fpsl);

		this.setProgress(pivotResults.size(), pivotResults.size());

	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<FPSL> transformResults(Map<Object, Object> pivotResults) {
		
		List<FPSL> fpsl = new ArrayList<FPSL>();
		FPSL f = new FPSL();
		fpsl.add(f);
		boolean prim = false;
		BigDecimal varSiPrima;
		BigDecimal varSiOga;
		Timestamp fproy = null;
		int j = 0;
		BigDecimal primPeriodica = null;
		BigDecimal oga = null;
		BigDecimal comi = null;
		BigDecimal primPeriodicaUlt = null;
		BigDecimal ogaUlt = null;
		BigDecimal comiUlt = null;
		String fUOAUlt = null;
		String fUOA = null;
		Timestamp fecc = null;
		//UmicKey umicKey = null;
		Integer nVeces = 0;
		List<String> uoaResult = new ArrayList<String>();
		List<String> uoaResultAux = new ArrayList<String>();
		Map<String, BigDecimal> primResult = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> comiResult = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> ogaResult = new HashMap<String, BigDecimal>();
		Map<String, Timestamp> fproyResult = new HashMap<String, Timestamp>();
		String negocio = null;
		String keyResult = null;
		Boolean multi8nb = false;
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List key = (List) entry.getKey();
			List values = (List) entry.getValue();
			
			fproy = (Timestamp) key.get(ConstantsFunciones.CTE_8);
			primPeriodica = (BigDecimal) values.get(ConstantsFunciones.CTE_10);
			oga = (BigDecimal) values.get(ConstantsFunciones.CTE_11);
			comi = (BigDecimal) values.get(ConstantsFunciones.CTE_13);
			fUOA = (String) key.get(ConstantsFunciones.CTE_2);
			negocio = (String) key.get(ConstantsFunciones.CTE_9);
			nVeces = (Integer) values.get(ConstantsFunciones.CTE_0);
			keyResult = negocio + fUOA;

			if (uoaResultAux.isEmpty() || !uoaResultAux.contains(keyResult)) {
				
				primResult.put(keyResult, primPeriodica);
				comiResult.put(keyResult, comi);
				ogaResult.put(keyResult, oga);
				fproyResult.put(keyResult, fproy);
				uoaResultAux.add(keyResult);
				ogaUlt = oga;
				primPeriodicaUlt = primPeriodica;
				comiUlt = comi;
				fUOAUlt = fUOA;
			} else {
				if (fproy.before(fproyResult.get(keyResult))) {
					primResult.put(keyResult, primPeriodica);
					comiResult.put(keyResult, comi);
					ogaResult.put(keyResult, oga);
					fproyResult.put(keyResult, fproy);
					ogaUlt = oga;
					primPeriodicaUlt = primPeriodica;
					comiUlt = comi;
					fUOAUlt = fUOA;
				}
			}
			fecc = (Timestamp) key.get(ConstantsFunciones.CTE_8);
		}
		
		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List key = (List) entry.getKey();
			List values = (List) entry.getValue();
			negocio = (String) key.get(ConstantsFunciones.CTE_9);
			fUOA = (String) key.get(ConstantsFunciones.CTE_2);
			keyResult = negocio + fUOA;
			varSiOga = (BigDecimal) values.get(ConstantsFunciones.CTE_14);
			if (varSiOga == null) {
				varSiOga = BigDecimal.ZERO;
			}
			varSiPrima = (BigDecimal) values.get(ConstantsFunciones.CTE_12);
			if (varSiPrima == null) {
				varSiPrima = BigDecimal.ZERO;
			}
			
			if ((uoaResult.isEmpty() || !uoaResult.contains(keyResult))
					&&
					(varSiOga.compareTo(BigDecimal.ZERO) > 0
						|| varSiPrima.compareTo(BigDecimal.ZERO) > 0 )) {
				primPeriodica = primResult.get(keyResult);
				if (primPeriodica == null) {
					primPeriodica = BigDecimal.ZERO;
				}
				
				comi = comiResult.get(keyResult);
				if (comi == null) {
					comi = BigDecimal.ZERO;
				}
				
				oga = ogaResult.get(keyResult);
				if (oga == null) {
					oga = BigDecimal.ZERO;
				}
				
				fpsl.add(transformEntry2(new FPSL(), entry, primPeriodica, comi, oga));				
				uoaResult.add(keyResult);
				multi8nb = true;
			}
			
			fpsl.add(transformEntry(new FPSL(), entry, multi8nb));

		}

		return fpsl;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache FlujosTotNiif17Key
	 * 
	 * @param flujosTotNiif17Key
	 * @param entry
	 * @return
	 */
	private FPSL transformEntry(FPSL fpsl, Entry<Object, Object> entry, Boolean multi8nb) {
		
		List key = (List) entry.getKey();
		
		fpsl.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_0));
		fpsl.setkuoa((String) key.get(ConstantsFunciones.CTE_2));
		fpsl.setTextraccion((String) key.get(ConstantsFunciones.CTE_3));
		fpsl.setTpasivo((String) key.get(ConstantsFunciones.CTE_4));
		fpsl.setTnegocio((Integer) key.get(ConstantsFunciones.CTE_5));
		fpsl.setTnegociolrc((Integer) key.get(ConstantsFunciones.CTE_6));
		//fpsl.setPventa((Timestamp) key.get(ConstantsFunciones.CTE_7));
		fpsl.setCodreaseg((Integer) key.get(ConstantsFunciones.CTE_7));
		fpsl.setFproyflujest((Timestamp) key.get(ConstantsFunciones.CTE_8));

		String btc = (String) key.get(ConstantsFunciones.CTE_1);
			if(btc.equals("N17LIRIN")
					|| multi8nb){
			int decreDias = 1;
			Timestamp f = null;
			final LocalDateTime dtPventa = new LocalDateTime(fpsl.getFcierre().getTime());
			Timestamp fVenta = new Timestamp(dtPventa.minusDays(decreDias).toDateTime().getMillis());
			LocalDateTime dateVenta = new LocalDateTime(fVenta.getTime());
			int mesVenta = dateVenta.getMonthOfYear();
			int	diaVenta=15;
			int anioVenta = dateVenta.getYear();
			
			String anyoPV = String.valueOf(anioVenta);
			String mesPV = String.valueOf(mesVenta);
			String diaPV = String.valueOf(diaVenta);
	
			String fechaPV = anyoPV + "/" + mesPV + "/" + diaPV;
			
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			try {
				 Date fechaParseada = formatoFecha.parse(fechaPV);
				 f = new Timestamp(fechaParseada.getTime());
				 fpsl.setPventa(f);
				//Fdiferimiento = new Timestamp(fechaParseada.getTime());
			} catch (ParseException e) {
				//Si el formato no es el esperado se lanza excepción.
				throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fechaPV, "yyyy/MM/dd"});
			}
		}else{
			
			fpsl.setPventa(null);
		}
		
		List values = (List) entry.getValue();
		BigDecimal totFall = (BigDecimal) values.get(ConstantsFunciones.CTE_3);
		BigDecimal totCompl = (BigDecimal) values.get(ConstantsFunciones.CTE_6);
		fpsl.setTotfpnaprim((BigDecimal) values.get(ConstantsFunciones.CTE_1));
		
		fpsl.setTotfpnarte((BigDecimal) values.get(ConstantsFunciones.CTE_2));
		//Corriente de siniestros
		fpsl.setTotfpnafall(totFall.subtract(totCompl));
		
		//Rentas
		fpsl.setTotfpnavida((BigDecimal) values.get(ConstantsFunciones.CTE_4));
		fpsl.setTotfpnagtoad(BigDecimal.ZERO);
//		if (((BigDecimal) values.get(ConstantsFunciones.CTE_14)).compareTo(BigDecimal.ZERO) > 0) {
//			fpsl.setTotfpnagtoad(BigDecimal.ZERO);
//		} else {
//			fpsl.setTotfpnagtoad((BigDecimal) values.get(ConstantsFunciones.CTE_9));
//		}
		
		fpsl.setTotfpnagto((BigDecimal) values.get(ConstantsFunciones.CTE_5));
		fpsl.setTotfpnacomi((BigDecimal) values.get(ConstantsFunciones.CTE_7));
		
		fpsl.setPb((BigDecimal) values.get(ConstantsFunciones.CTE_8));
		
		return fpsl;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache FlujosTotNiif17Key
	 * 
	 * @param flujosTotNiif17Key
	 * @param entry
	 * @return
	 */
	private FPSL transformEntry2(FPSL fpsl, Entry<Object, Object> entry, BigDecimal primPeriodica, BigDecimal comi, BigDecimal oga) {
		
		List key = (List) entry.getKey();
		
		fpsl.setFcierre((Timestamp) key.get(ConstantsFunciones.CTE_0));
		fpsl.setkuoa((String) key.get(ConstantsFunciones.CTE_2));
		fpsl.setTextraccion((String) key.get(ConstantsFunciones.CTE_3));
		fpsl.setTpasivo((String) key.get(ConstantsFunciones.CTE_4));
		fpsl.setTnegocio((Integer) key.get(ConstantsFunciones.CTE_5));
		fpsl.setTnegociolrc((Integer) key.get(ConstantsFunciones.CTE_6));
		fpsl.setCodreaseg((Integer) key.get(ConstantsFunciones.CTE_7));
		fpsl.setFproyflujest((Timestamp) key.get(ConstantsFunciones.CTE_0));

		String btc = (String) key.get(ConstantsFunciones.CTE_1);
			if(btc.equals("N17LIRIN")
					|| btc.equals("NIIF17")){
			int decreDias = 1;
			Timestamp f = null;
			final LocalDateTime dtPventa = new LocalDateTime(fpsl.getFcierre().getTime());
			Timestamp fVenta = new Timestamp(dtPventa.minusDays(decreDias).toDateTime().getMillis());
			LocalDateTime dateVenta = new LocalDateTime(fVenta.getTime());
			int mesVenta = dateVenta.getMonthOfYear();
			int	diaVenta=15;
			int anioVenta = dateVenta.getYear();
			
			String anyoPV = String.valueOf(anioVenta);
			String mesPV = String.valueOf(mesVenta);
			String diaPV = String.valueOf(diaVenta);
	
			String fechaPV = anyoPV + "/" + mesPV + "/" + diaPV;
			
			SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
			try {
				 Date fechaParseada = formatoFecha.parse(fechaPV);
				 f = new Timestamp(fechaParseada.getTime());
				 fpsl.setPventa(f);
				//Fdiferimiento = new Timestamp(fechaParseada.getTime());
			} catch (ParseException e) {
				//Si el formato no es el esperado se lanza excepción.
				throw Solvencia2ExcepcionHelper.crearExcepcion("AP", new String[]{fechaPV, "yyyy/MM/dd"});
			}
		}else{
			
			fpsl.setPventa(null);
		}
		
		List values = (List) entry.getValue();
		fpsl.setTotfpnaprim(primPeriodica);
		fpsl.setTotfpnarte(BigDecimal.ZERO);
		//Corriente de siniestros
		fpsl.setTotfpnafall(BigDecimal.ZERO);
		
		//Rentas
		fpsl.setTotfpnavida(BigDecimal.ZERO);
		fpsl.setTotfpnagtoad(oga);
		fpsl.setTotfpnagto(BigDecimal.ZERO);
		fpsl.setTotfpnacomi(comi);
		
		return fpsl;
	}

}
