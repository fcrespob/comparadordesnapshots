package es.mapfre.solvencia.entregables.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import com.tangosol.util.aggregator.CompositeAggregator;
import com.tangosol.util.aggregator.Count;
import com.tangosol.util.aggregator.GroupAggregator;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.filter.EqualsFilter;

import es.mapfre.solvencia.dominio.entregables.ConteoCertificado;
import es.mapfre.solvencia.dominio.maestro.DatosGenerales;
import es.mapfre.solvencia.dominio.salidaCalculo.TotalesFlujos;
import es.mapfre.solvencia.entregables.EntregableGenerico;
import es.mapfre.solvencia.entregables.util.ConstantsEntregables;
import es.mapfre.solvencia.formulacion.util.ConstantsFunciones;
import es.mapfre.solvencia.servicios.IAlmacenarDatos;
import es.mapfre.solvencia.servicios.IObtenerConfiguracion;
import es.mapfre.solvencia.servicios.fachada.impl.FachadaServicios;
import es.mapfre.solvencia.util.ConstantsFactorias;


public class EntregableCONTEOCERTIFICADO extends EntregableGenerico {
	
	private NamedCache totales;

	private static final String CACHE_TOTALES_FLUJOS =  ConstantsEntregables.CACHE_TOTALES_FLUJOS;

	private final IAlmacenarDatos servicioAlmacenar = FachadaServicios.getAlmacenarDatos();
	private final IObtenerConfiguracion servicioConfiguracion = FachadaServicios.getObtenerConfiguracion();
	
	private static final Logger LOG = LoggerFactory.getLogger(EntregableCONTEOCERTIFICADO.class);
	
	Map<Integer, Integer> mapaTotales = new HashMap<Integer, Integer>();
	Map<Integer, Integer> mapaMaestro = new HashMap<Integer, Integer>();
	Map<String, Integer> mapaCertificado = new HashMap<String, Integer>();
	private String gNegocio; 

	@Override
	public String getNombreEntregable() {
		return ConstantsFactorias.ENTREGABLE_CONTEOCERTIF;
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
				new PofExtractor(String.class, TotalesFlujos.IND_CNEGOCIO),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KMODALIDAD),
				new PofExtractor(Long.class, TotalesFlujos.IND_KPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KSUBPOLIZA),
				new PofExtractor(Integer.class, TotalesFlujos.IND_KCERTIFICADO)
		};
		
		ValueExtractor multiExtractor = new MultiExtractor(totalesExtractors);

		// Construcción de los agregadores
		InvocableMap.EntryAggregator[] values = new InvocableMap.EntryAggregator[] {
				new Count()
				//new BigDecimalSum(new PofExtractor(Integer.class,DetalleCorrienteEntregables.IND_KCERTIFICADO))
		};
		
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
		// de transformar los resultados. 
		List<ConteoCertificado> conteo= transformResults(aggregationResults);
		
		
		
		Collection<DatosGenerales> maestro = servicioConfiguracion.recuperarMaestro();
		List<DatosGenerales> listMaestro = new ArrayList(maestro);
		List<ConteoCertificado> conteoTotal= transformResults2(conteo, listMaestro, kbasetec);
		// Almacenar en cache
		servicioAlmacenar.almacenarEntregableCONTEOCERTIFICADO(conteoTotal);

		this.setProgress(aggregationResults.size(), aggregationResults.size());
	}

	/**
	 * Extraer los resultados de la agregación
	 * 
	 * @param pivotResults
	 * @return
	 */
	private List<ConteoCertificado> transformResults(Map<Object, Object> pivotResults) {
		
		List<ConteoCertificado> conteoCert = new ArrayList<ConteoCertificado>();

		for (Entry<Object, Object> entry : pivotResults.entrySet()) {
			List key = (List) entry.getKey();
			Integer mod = (Integer) key.get(ConstantsFunciones.CTE_2);
			Integer numCertificados = (Integer) mapaTotales.get((Integer) key.get(ConstantsFunciones.CTE_2));
			String negocio = (String) key.get(ConstantsFunciones.CTE_1);
			gNegocio = negocio;
			
			if (mapaTotales.containsKey(mod)) {
				mapaTotales.put(mod, numCertificados + 1);
				int num = 0;
				ConteoCertificado conteAux = new ConteoCertificado();
				conteAux.setBt((String) key.get(ConstantsFunciones.CTE_0));
				conteAux.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_2));
				conteAux.setKcertificado_totales(numCertificados);
				conteAux.setKcertificado_umic(0);
				num = conteoCert.indexOf(conteAux);
				conteoCert.set(num, transformEntry(new ConteoCertificado(), entry, numCertificados + 1));
			} else {
				mapaTotales.put(mod, 1);
				conteoCert.add(transformEntry(new ConteoCertificado(), entry, 1));
			}
		}
		

		return conteoCert;
	}
	
	private List<ConteoCertificado> transformResults2(List<ConteoCertificado> conteoCert, List<DatosGenerales> maestro, String basetec) {
		
		for (int i=0; i<maestro.size(); i++){
			Integer mod = maestro.get(i).getKmodalidad();
			Long poliza = maestro.get(i).getKpoliza();
			Integer subpoliza = maestro.get(i).getKsubpoliza();
			Integer certifiado = maestro.get(i).getKcertificado();
			Integer numCertificados = 0;
			String negocio = maestro.get(i).getCnegocio();
			if (mapaTotales.containsKey(mod) && gNegocio.equals(negocio)) {
				String aux = mod.toString().concat(poliza.toString()).concat(subpoliza.toString()).concat(certifiado.toString());
				if (!mapaCertificado.containsKey(aux)) {
					mapaCertificado.put(aux, 1);
					numCertificados = (Integer) mapaMaestro.get(mod);
					if (numCertificados == null) {
						numCertificados = 0;
					}
					mapaMaestro.put(mod, numCertificados + 1);
					int num = 0;
					ConteoCertificado conteAux = new ConteoCertificado();
					conteAux.setBt(basetec);
					conteAux.setKmodalidad(mod);
					conteAux.setKcertificado_totales((Integer) mapaTotales.get(mod));
					conteAux.setKcertificado_umic(numCertificados);
					num = conteoCert.indexOf(conteAux);
					conteAux.setKcertificado_umic(numCertificados + 1);
					conteoCert.set(num, conteAux);
				}
			} else if (gNegocio.equals(negocio)) {
				Integer numCerAux = 0;
				if (mapaTotales.containsKey(mod)) {
					numCerAux = (Integer) mapaTotales.get(mod);
					mapaMaestro.put(mod, numCerAux + 1);
				} else {
					mapaMaestro.put(mod, 1);
				}
				numCerAux = (Integer) mapaTotales.get(mod);
				conteoCert.add(transformEntry2(new ConteoCertificado(), maestro.get(i).getKmodalidad(), basetec, 1));
			}
		}

		return conteoCert;
	}

	/**
	 * Mapea cada entrada de los resultados a la cache CONTCERTI
	 * 
	 * @param CONTCERTI
	 * @param entry
	 * @return
	 */
	private ConteoCertificado transformEntry(ConteoCertificado conteo, Entry<Object, Object> entry, Integer numCertificados) {

		List key = (List) entry.getKey();
		List values = (List) entry.getValue();
	
		conteo.setKmodalidad((Integer) key.get(ConstantsFunciones.CTE_2));
		conteo.setBt((String) key.get(ConstantsFunciones.CTE_0));
		conteo.setKcertificado_totales(numCertificados);
		conteo.setKcertificado_umic(0);

		return conteo;
	}
	
	private ConteoCertificado transformEntry2(ConteoCertificado conteo, Integer modalidad, String bt, Integer numCertificado) {
		
		conteo.setKmodalidad(modalidad);
		conteo.setBt(bt);
		conteo.setKcertificado_totales(0);
		conteo.setKcertificado_umic(numCertificado);
		

		return conteo;
	}
}