package es.mapfre.solvencia.entregables.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.PrvBt;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.aggregators.FirstItemAggregator;
import es.mapfre.solvencia.entregables.processors.PrvBtdurTrCasadoProcessor;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable PRVBT.
 * 
 */
public class EntregablePRVBT extends EntregableGenerico {

	
	private NamedCache totales;

	private static final Logger LOG = LoggerFactory.getLogger(EntregableFLUJCOASEG.class);

	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_PRVBT;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);

		// Este entregable requiere tanto extracciones de más de una caché, como
		// agregaciones. Esto significa que es necesario hacerlo en dos fases
		// bien diferenciadas.

		// En un primer lugar se va a realizar la agregación. Para ello creamos
		// el extractor con todos los campos a obtener de la caché en la que
		// están los campos en base a los que se agrega.

		// totales-flujos cache extractors
		ValueExtractor[] totalesExtractors = new ValueExtractor[] {
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KGARANTIA),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_NSUSCRI),
				new PofExtractor(String.class, TotalesFlujos.IND_KCARTERAINV),
				// new PofExtractor(BigDecimal.class,
				// TotalesFlujos.IND_TOTFNFALL),
				new PofExtractor(String.class, TotalesFlujos.IND_GAPACT),
				new PofExtractor(String.class, TotalesFlujos.IND_SEGMENTO1),
				new PofExtractor(String.class, TotalesFlujos.IND_TIPOSUBRIESGO),
//INI-TAR00400971	
//				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT),
				new PofExtractor(String.class, TotalesFlujos.IND_GESTIONIT),
				new PofExtractor(String.class, TotalesFlujos.IND_SPCOM),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODEXT),
				new PofExtractor(String.class, TotalesFlujos.IND_KBENCON),
//FIN-TAR00400971	
				// new PofExtractor(BigDecimal.class,
				// TotalesFlujos.IND_TOTPROVISION)
				
				
		};
		

		ValueExtractor multiExtractor = new MultiExtractor(totalesExtractors);
		//Fase VIII
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {

			//	new Count(),
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new FirstItemAggregator(new PofExtractor(UmicKey.class, TotalesFlujos.IND_UMICKEY)),
				new FirstItemAggregator(new PofExtractor(Timestamp.class, TotalesFlujos.IND_FECINISUS)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PCOASEG)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_DISTINT)),
				new FirstItemAggregator(new PofExtractor(String.class, TotalesFlujos.IND_PREST_CAL)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PGASTGESIN1I)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PGASTGESIN2I)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_PGASTGESEX1I)),
				new FirstItemAggregator(new PofExtractor(List.class, TotalesFlujos.IND_ITCALC)),
				new FirstItemAggregator(new PofExtractor(String.class, TotalesFlujos.IND_TABLACALC1ASEG1)),
				new FirstItemAggregator(new PofExtractor(String.class, TotalesFlujos.IND_TABLA_TANUL)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_GTO_UNI)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_GTO_PROV)),
				new FirstItemAggregator(new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCURVA_TI)),
				new FirstItemAggregator(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_FACTOR1)),
				new FirstItemAggregator(new PofExtractor(List.class, TotalesFlujos.IND_FACTOR2))
		};
		//Fase VIII

		// Construcción del agregador compuesto
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);
		
		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);
		// kbasetec filter
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Query
		Map<Object, Object> aggregationResults = (Map<Object, Object>) totales.aggregate(isKbasetec, pivotAggregator);

		this.setProgress(aggregationResults.size(), 0);

		// Con los resultados de la agregación, se llama a la función encargada
		// de transformar los resultados. Dado que en dicha función
		// se va a incluir el procesamiento por cada entrada de los resultados
		// para obtener los campos guardados en otras cachés, es
		// necesario añadir también como argumento el extractor de
		// DetalleCorrienteEntregables
		// construido previamente
		this.transformAndStoreResults(kbasetec, aggregationResults, multiExtractor);

		// Almacenar en cache
		//servicioAlmacenar.almacenarEntregablePrvBt(prvbts);

		this.setProgress(aggregationResults.size(), aggregationResults.size());
	}

	/**
	 * Extrae los resultados calculados por el agregador. Realiza la extracción
	 * de los campos restantes.
	 * 
	 * @param aggregationResults
	 * @return
	 */
	private void transformAndStoreResults(String kbasetec, Map<Object, Object> aggregationResults,
			ValueExtractor multiExtractor) {
		// Se crea la lista que va a almacenar los resultados
		List<PrvBt> prvbts = new ArrayList<PrvBt>();


		// Empezamos a recorrer la lista de resultados de la agregación
		for (Entry<Object, Object> entry : aggregationResults.entrySet()) {

			PrvBt prvBt = new PrvBt();

			// En esta primera parte vamos a realizar la asignación de los
			// campos asociados a DetalleCorrienteEntregables, ya que van a ser
			// necesarios para construir el filtro que le vamos a pasar al
			// procesamiento

			List keys = (List) entry.getKey();

			prvBt.setBt((String) keys.get(ConstantsFunciones.CTE_0));
			prvBt.setFeccierre((Timestamp) keys.get(ConstantsFunciones.CTE_1));
			prvBt.setCnegocio((String) keys.get(ConstantsFunciones.CTE_2));
			prvBt.setCcanal((Integer) keys.get(ConstantsFunciones.CTE_3));
			prvBt.setKramo((String) keys.get(ConstantsFunciones.CTE_4));
			prvBt.setKmodalidad((Integer) keys.get(ConstantsFunciones.CTE_5));
			prvBt.setKgarantia((Integer) keys.get(ConstantsFunciones.CTE_6));
			prvBt.setKpoliza((Long) keys.get(ConstantsFunciones.CTE_7));
			prvBt.setKsubpoliza((Integer) keys.get(ConstantsFunciones.CTE_8));
			prvBt.setNsuscri((Integer) keys.get(ConstantsFunciones.CTE_9));
			prvBt.setKcarterainv((String) keys.get(ConstantsFunciones.CTE_10));
			// prvBt.setFnfall((BigDecimal)
			// keys.get(ConstantsFunciones.CTE_11));
			prvBt.setGapact((String) keys.get(ConstantsFunciones.CTE_11));
			prvBt.setSegmento1((String) keys.get(ConstantsFunciones.CTE_12));
			prvBt.setTiposubriesgo((String) keys.get(ConstantsFunciones.CTE_13));
			prvBt.setGestionit((String) keys.get(ConstantsFunciones.CTE_14));
			//INI-TAR00400971	
			prvBt.setSpcom((String) keys.get(ConstantsFunciones.CTE_15));
			prvBt.setKmodext((Integer) keys.get(ConstantsFunciones.CTE_16));
			prvBt.setKbencon((String) keys.get(ConstantsFunciones.CTE_17));
	//FIN-TAR00400971	

			if (prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BEL) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRTIU) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRTID) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRGTO) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRMFE) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRMMI) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCF) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRMCI) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRLFE) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRLMI) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRINC) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRVM)  || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEP) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRAEN) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIP) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRAIN) || 
					prvBt.getBt().equals(ConstantsEntregables.CTE_BT_SCRANM)) {
				prvBt.setIpc(servicioConfiguracion.recuperarIpcFuturo((Timestamp) keys.get(ConstantsFunciones.CTE_1),(String) keys.get(ConstantsFunciones.CTE_0)));
			}
			List values = (List) entry.getValue();
			prvBt.setPrv((BigDecimal) values.get(ConstantsFunciones.CTE_0));
			Set<UmicKey> umicKeys = (Set<UmicKey>) values.get(ConstantsFunciones.CTE_1);
			prvBt.setUmickey(umicKeys.iterator().next());
			
			
			
			// datosCoasegurado
			Set<BigDecimal> pCoaseg = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_3);
			if (pCoaseg.iterator().next().equals(null)) {
				prvBt.setPcoase(BigDecimal.ZERO);
			}else {
				prvBt.setPcoase(pCoaseg.iterator().next());
			}
			//prvBt.setPcoase(pCoaseg.iterator().next());
			Set<BigDecimal> distint = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_4);
			if (distint.iterator().next().equals(null)) {
				prvBt.setDistint(BigDecimal.ZERO);
			}else {
				prvBt.setDistint(distint.iterator().next());
			}
			//prvBt.setDistint(distint.iterator().next());

			// datosAdicionales
			
			Set<String> lPrestCal = (Set<String>) values.get(ConstantsFunciones.CTE_5);
			prvBt.setKprestcal(lPrestCal.iterator().hasNext()?lPrestCal.iterator().next():"");

			// fechas
			Set<Timestamp> fIniSusc = (Set<Timestamp>) values.get(ConstantsFunciones.CTE_2);
			prvBt.setFinisusc(fIniSusc.iterator().next());

			if ((!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR))) {

				// basetecnicaInicial
				Set<BigDecimal> pGastGesIn1 = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_6);
				if (pGastGesIn1.iterator().next().equals(null)) {
					prvBt.setPgastgesin1(BigDecimal.ZERO);
				}else {
					prvBt.setPgastgesin1(pGastGesIn1.iterator().next());
				}
				//prvBt.setPgastgesin1(pGastGesIn1.iterator().next());
				Set<BigDecimal> pGastGesIn2 = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_7);
				if (pGastGesIn2.iterator().next().equals(null)) {
					prvBt.setPgastgesin2(BigDecimal.ZERO);
				}else {
					prvBt.setPgastgesin2(pGastGesIn2.iterator().next());
				}
				//prvBt.setPgastgesin2(pGastGesIn2.iterator().next());
				Set<BigDecimal> pGastExt = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_8);
				if (pGastExt.iterator().next().equals(null)) {
					prvBt.setPgastext(BigDecimal.ZERO);
				}else {
					prvBt.setPgastext(pGastExt.iterator().next());
				}
				//prvBt.setPgastext(pGastExt.iterator().next());
			}

			// detalleBaseTecnica


			Set<List<BigDecimal>> itCalc = (Set<List<BigDecimal>>) values.get(ConstantsFunciones.CTE_9);
			List<BigDecimal> itcalc = (List<BigDecimal>) itCalc.iterator().next();

			if ((!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR))) {

				if (itcalc.size() >= 1 && itcalc.get(0) != null) {
					prvBt.setPintertecn1(itcalc.get(ConstantsFunciones.CTE_0));
				}
				if (itcalc.size() >= 2 && itcalc.get(1) != null) {
					prvBt.setPintertecn2(itcalc.get(ConstantsFunciones.CTE_1));
				}

				Set<String> tabla1 = (Set<String>) values.get(ConstantsFunciones.CTE_10);
				prvBt.setTabla1(tabla1.iterator().hasNext()?tabla1.iterator().next():"");
			}

			Set<String> tablaTAnul = (Set<String>) values.get(ConstantsFunciones.CTE_11);
			prvBt.setTablatanul(tablaTAnul.iterator().hasNext()?tablaTAnul.iterator().next():"");
			Set<BigDecimal> gastRealUnitario = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_12);
			if (gastRealUnitario.iterator().next().equals(null)) {
				prvBt.setGastrealunitario(BigDecimal.ZERO);
			}else {
				prvBt.setGastrealunitario(gastRealUnitario.iterator().next());
			}
			//prvBt.setGastrealunitario(gastRealUnitario.iterator().next());
			Set<BigDecimal> gastReal = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_13);
			if (gastReal.iterator().next().equals(null)) {
				prvBt.setGastreal(BigDecimal.ZERO);
			}else {
				prvBt.setGastreal(gastReal.iterator().next());
			}
			//prvBt.setGastreal(gastReal.iterator().next());
			Set<String> curvaTI = (Set<String>) values.get(ConstantsFunciones.CTE_14);
			prvBt.setCurvati(curvaTI.iterator().hasNext()?curvaTI.iterator().next():"");

			if (prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BEL) || prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA) || prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR)) {
				Set<BigDecimal> factor1 = (Set<BigDecimal>) values.get(ConstantsFunciones.CTE_15);
				if (factor1.iterator().next().equals(null)) {
					prvBt.setFactor1(BigDecimal.ZERO);
				}else {
					prvBt.setFactor1(factor1.iterator().next());
				}
				//prvBt.setFactor1(factor1.iterator().next());
			}

			Set<List<BigDecimal>> indfactor2 = (Set<List<BigDecimal>>) values.get(ConstantsFunciones.CTE_16);
			
			prvBt.setIndfactor2(factor2Value(indfactor2.iterator().next()));

			//Duración tramo casado
			if ((!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BEL)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCOA)) && (!prvBt.getBt().equals(ConstantsEntregables.CTE_BT_BELCLR))) {
				PrvBtdurTrCasadoProcessor prvBtDurTrCasado = new PrvBtdurTrCasadoProcessor(kbasetec);
				prvBt.setDurtrcasado(prvBtDurTrCasado.getDurTramoCasado(prvBt.getUmickey()));
			}
			prvbts.add(prvBt);
			
		}
			if (prvbts.size() > 0) {
				servicioAlmacenar.almacenarEntregablePrvBt(prvbts);
				prvbts.clear();
			}
		
	}

	/**
	 * Calcula el valor del campo IndFactor2
	 * 
	 * @param factor2
	 * @return
	 */
	private String factor2Value(List<BigDecimal> factor2) {
		for (BigDecimal factor2Entry : factor2) {
			if (factor2Entry != null && factor2Entry != BigDecimal.ZERO
					&& !factor2Entry.equals(BigDecimal.valueOf(100))) {
				return ConstantsEntregables.CTE_S;
			}
		}
		return ConstantsEntregables.CTE_N;
	}

}