
/* MODIFICACION:TAR00302248-Contabilidad SolvenciaII en SSAA 
   FECHA: 25/09/2017
   AUTOR: INDRA
*/
 /* MODIFICACION:MU-2018-023544-No se debe hacer la agrupacion por garantia, ya que no esta acumulando a nivel de poliza.
   FECHA: 05/04/2018 
   AUTOR: INDRA
*/
 /* MODIFICACION:TAR00393113-CONTABILIZACION EN SISTEMAS ABIERTOS POR MODALIDAD ORIGEN PARA LA MODALIDAD INSTRUMENTAL 400
   FECHA: 26/04/2018 
   AUTOR: INDRA
*/
/* MODIFICACION:SOLVENCIA-FASE VIII optimización cálculo Entregable CONTAB
   FECHA: 22/01/2019 
   AUTOR: NFQ
*/
 /* MODIFICACION:MU-2019-053597:Código SD01707312: Incidencia en FICHERO CONTAB PARA SEPARAR PTRI DE PROV MATEMATICA (CLOUD)
   FECHA: 26/08/2019 
   AUTOR: INDRA
   DESCRIPCION: Se incluye en campo ptipoprovi en el fichero CONTAB
*/
package es.mapfre.solvencia.entregables.impl;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.oracle.coherence.patterns.processing.task.TaskExecutionEnvironment;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.GuardSupport;
import com.tangosol.net.NamedCache;
import com.tangosol.util.Filter;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.aggregator.BigDecimalSum;
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AnyFilter;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.dominio.entregables.Contabilidad;
import es.mapfre.solvencia.dominio.entregables.ContabilidadCertificado;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.aggregators.FirstItemAggregator;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;

/**
 * Clase que implementa el cálculo del Entregable CONTABILIDAD .
 * 
 */
public class EntregableCONTABC extends EntregableGenerico {
	
	private NamedCache totales;


	private static final String CACHE_TOTALES_FLUJOS = ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_CONTABC;
	}

	@Override
	public void execute(String kbasetec, TaskExecutionEnvironment oEnvironment) {
		this.initProgress(oEnvironment);
		
		totales = CacheFactory.getCache(CACHE_TOTALES_FLUJOS);
		
		// En un primer lugar se va a realizar la agregación. Para ello creamos
		// el extractor con todos los campos a obtener de la caché en la que
		// están los campos en base a los que se agrega.

		// totales-flujos cache extractors
		ValueExtractor[] totalesExtractors = new ValueExtractor[] {
				new PofExtractor(String.class, TotalesFlujos.IND_BT),
				new PofExtractor(Timestamp.class, TotalesFlujos.IND_FCIERRE),
				new PofExtractor(Integer.class, TotalesFlujos.IND_CCANAL),
				new PofExtractor(String.class, TotalesFlujos.IND_KRAMO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
//INI-823544				
//          	new PofExtractor(Integer.class, TotalesFlujos.IND_KGARANTIA),
//FIN-823544
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(String.class, TotalesFlujos.IND_KOFICONT),
//INI-TAR00393113	
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODEXT),
				new PofExtractor(String.class, TotalesFlujos.IND_CTIPOPROVI),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADO)

//FIN-TAR00393113
				// new PofExtractor(BigDecimal.class,
				// TotalesFlujos.IND_TOTPROVISION)
		};

		ValueExtractor multiExtractor = new MultiExtractor(totalesExtractors);

		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new BigDecimalSum(new PofExtractor(BigDecimal.class, TotalesFlujos.IND_TOTPROVISION)),
				new FirstItemAggregator(new PofExtractor(Timestamp.class, TotalesFlujos.IND_FECINI)),
				new FirstItemAggregator(new PofExtractor(Timestamp.class, TotalesFlujos.IND_FECFIN)),
				new FirstItemAggregator(new PofExtractor(UmicKey.class, TotalesFlujos.IND_UMICKEY))
		};
		
		CompositeAggregator valuesAggregator = CompositeAggregator.createInstance(values);

		// Construcción del agregador que realiza la agregación sobre los
		// valores a extraer
		InvocableMap.EntryAggregator pivotAggregator = GroupAggregator.createInstance(multiExtractor, valuesAggregator);

		// kbasetec filter
		List<Filter> filtros = new ArrayList<Filter>();
		Filter[] a = new Filter[2];
		EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);
		EqualsFilter isMod413 = new EqualsFilter(new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD), new Integer(413));
		EqualsFilter isMod412 = new EqualsFilter(new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD), new Integer(412));
		a[0] = isMod413;
		a[1] = isMod412;
		AnyFilter isMod = new AnyFilter(a);
		
		filtros.add(isKbasetec);
		filtros.add(isMod);
		
		Filter[] arrayFiltros = new Filter[filtros.size()];
		for (int i = 0; i < arrayFiltros.length; i++) {
			arrayFiltros[i] = filtros.get(i);
		}
		Filter allFilter = new AllFilter(arrayFiltros);
		//EqualsFilter isKbasetec = new EqualsFilter(new PofExtractor(String.class, TotalesFlujos.IND_BT), kbasetec);

		// Query
		Map<Object, Object> aggregationResults = (Map<Object, Object>) totales.aggregate(allFilter, pivotAggregator);
		
		this.setProgress(aggregationResults.size(), 0);

		// Con los resultados de la agregación, se llama a la función encargada
		// de transformar los resultados. 
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
		List<ContabilidadCertificado> contabilidades = new ArrayList<ContabilidadCertificado>();

		// Empezamos a recorrer la lista de resultados de la agregación
		int numProcesadas = 0;
		int total = aggregationResults.size();
		for (Entry<Object, Object> entry : aggregationResults.entrySet()) {

			ContabilidadCertificado contabilidad = new ContabilidadCertificado();

			List keys = (List) entry.getKey();
			List values = (List) entry.getValue();

			contabilidad.setBt((String) keys.get(ConstantsFunciones.CTE_0));
			contabilidad.setFeccierre((Timestamp) keys.get(ConstantsFunciones.CTE_1));
			contabilidad.setCcanal((Integer) keys.get(ConstantsFunciones.CTE_2));
			contabilidad.setKramo((String) keys.get(ConstantsFunciones.CTE_3));
			contabilidad.setKmodalidad((Integer) keys.get(ConstantsFunciones.CTE_4));
			contabilidad.setKcertificado((Integer) keys.get(ConstantsFunciones.CTE_11));
//INI-TAR00393113
			if (((Integer) keys.get(ConstantsFunciones.CTE_4)).equals(ConstantsFunciones.CTE_400)) {
				contabilidad.setKmodalidad((Integer) keys.get(ConstantsFunciones.CTE_9));
			}
//FIN-TAR00393113
//INI-823544
			contabilidad.setKpoliza((Long) keys.get(ConstantsFunciones.CTE_5));
			contabilidad.setKsubpoliza((Integer) keys.get(ConstantsFunciones.CTE_6));
			contabilidad.setCnegocio((String) keys.get(ConstantsFunciones.CTE_7));
			contabilidad.setKoficont((String) keys.get(ConstantsFunciones.CTE_8));
			contabilidad.setPrv((BigDecimal) values.get(ConstantsFunciones.CTE_0));
//javivil
			contabilidad.setKmodext((Integer) keys.get(ConstantsFunciones.CTE_9));
//javivil
			contabilidad.setctipoprovi((String) keys.get(ConstantsFunciones.CTE_10));
//			contabilidad.setKpoliza((Long) keys.get(ConstantsFunciones.CTE_6));		
//			contabilidad.setKsubpoliza((Integer) keys.get(ConstantsFunciones.CTE_7));
//			contabilidad.setCnegocio((String) keys.get(ConstantsFunciones.CTE_8));
//			contabilidad.setKoficont((String) keys.get(ConstantsFunciones.CTE_9)); 
//FIN-823544			
			//INICIO FASE VIII
			Set<Timestamp> feFecIni = (Set<Timestamp>) values.get(ConstantsFunciones.CTE_1);
			Set<Timestamp> feFecfin = (Set<Timestamp>) values.get(ConstantsFunciones.CTE_2);
			contabilidad.setFefecini(feFecIni.iterator().next());
			contabilidad.setFefecfin(feFecfin.iterator().next());
			
			Set<UmicKey> umicKeys = (Set<UmicKey>) values.get(ConstantsFunciones.CTE_3);
			contabilidad.setUmickey(umicKeys.iterator().next());
			//FIN FASE VIII

			contabilidades.add(contabilidad);
			numProcesadas++;
			// Flush
			if (contabilidades.size() > 0 && contabilidades.size()% 1000 == 0) {
				servicioAlmacenar.almacenarEntregableContabilidadCertificado(contabilidades);
				contabilidades.clear();
				
				// Informar de progreso
				this.setProgress(total, numProcesadas);
			}

			// Es necesario avisar al guardián de que el hilo sigue vivo a lo
			// largo del procesamiento de los resultados de la agregación para
			// que no lo mate
			GuardSupport.heartbeat();
		}

		if (contabilidades.size() > 0) {
			servicioAlmacenar.almacenarEntregableContabilidadCertificado(contabilidades);
			contabilidades.clear();
		}
	}
}