package es.mapfre.solvencia.dao.impl.salidaCalculo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;










import com.tangosol.net.CacheFactory;
import com.tangosol.net.DistributedCacheService;
import com.tangosol.net.partition.PartitionSet;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.MultiExtractor;
import com.tangosol.util.extractor.PofExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.AlwaysFilter;
import com.tangosol.util.filter.AnyFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.PartitionedFilter;
import com.tangosol.util.processor.ConditionalRemove;

import es.mapfre.solvencia.coherence.keys.maestro.UmicKey;
import es.mapfre.solvencia.coherence.keys.salidaCalculo.DetalleCorrienteKey;
import es.mapfre.solvencia.dao.DaoBaseSalidaCalculo;
import es.mapfre.solvencia.dao.impl.parametrizacionGeneral.PolizasTipoDao;
import es.mapfre.solvencia.dominio.parametrizacionGeneral.PolizasTipo;
import es.mapfre.solvencia.dominio.salidaCalculo.DetalleCorriente;
import es.mapfre.solvencia.utils.BtUtils;

public class DetalleCorrienteDao extends DaoBaseSalidaCalculo implements Map<DetalleCorrienteKey, DetalleCorriente> {
	
	
	private static final String CACHE_NAME = "detalle-corriente";

	private final ValueExtractor fcierreExtractor = new ChainedExtractor(new PofExtractor(Timestamp.class, DetalleCorriente.IND_FCIERRE), new ReflectionExtractor("getTime"));
	private final ValueExtractor btExtractor = new PofExtractor(String.class, DetalleCorriente.IND_BT);
	private final ValueExtractor kmodalidadExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_KMODALIDAD);
	private final ValueExtractor kpolizaExtractor = new PofExtractor(Long.class, DetalleCorriente.IND_KPOLIZA);
	private final ValueExtractor ksubpolizaExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_KSUBPOLIZA);
	private final ValueExtractor kcertificadoExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_KCERTIFICADO);
	private final ValueExtractor nsuscriExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_NSUSCRI);
	private final ValueExtractor nordenExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_NORDEN);
	private final ValueExtractor kgarantiaExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_KGARANTIA);
	private final ValueExtractor kprestacionExtractor = new PofExtractor(String.class, DetalleCorriente.IND_KPRESTACION);
	private final ValueExtractor kajusteExtractor = new PofExtractor(Integer.class, DetalleCorriente.IND_KAJUSTE);
	private final ValueExtractor ctipoaportExtractor = new PofExtractor(String.class, DetalleCorriente.IND_CTIPOAPORT);
	
	public DetalleCorrienteDao() {
		super();
		super.setCacheName(CACHE_NAME);	

		// Se añade un índice sobre el campo kpoliza para mejorar las prestaciones en la extracción
		getCache().addIndex(kpolizaExtractor, false, null);
		// El resto de índices afectan a la inserción y, por lo tanto, es mejor no activarlos
		//getCache().addIndex(fcierreExtractor, false, null);
		//getCache().addIndex(btExtractor, false, null);
		//getCache().addIndex(kmodalidadExtractor, false, null);
		//getCache().addIndex(ksubpolizaExtractor, false, null);
		//getCache().addIndex(kcertificadoExtractor, false, null);
		//getCache().addIndex(nsuscriExtractor, false, null);
		//getCache().addIndex(nordenExtractor, false, null);
		//getCache().addIndex(kgarantiaExtractor, false, null);
		//getCache().addIndex(kprestacionExtractor, false, null);
		//getCache().addIndex(kajusteExtractor, false, null);
		//getCache().addIndex(ctipoaportExtractor, false, null);
		
		// Se añade un índice compuesto
		getCache().addIndex(this.getMultiExtractor(), false, null);
		getCache().addIndex(this.getMultiExtractorObtenerFiltroDetalles(), false, null);
	}

	@Override
	public DetalleCorriente get(Object key) {
		if (!(key instanceof DetalleCorrienteKey)) {
			return null;
		} else {
			return (DetalleCorriente) this.getCache().get(key);
		}
	}


	@Override
	public DetalleCorriente put(DetalleCorrienteKey key,
			DetalleCorriente value) {
		this.getCache().putAll(Collections.singletonMap(key, value));
		return value;

	}

	@Override
	public DetalleCorriente remove(Object key) {
		return (DetalleCorriente) this.getCache().remove(key);
	}

	@Override
	public Set<DetalleCorrienteKey> keySet() {
		return this.getCache().keySet();
	}
	
	@Override
	public Collection<DetalleCorriente> values() {
		return (Collection<DetalleCorriente>) this.getCache().values();
	}

	@Override
	public Set<java.util.Map.Entry<DetalleCorrienteKey, DetalleCorriente>> entrySet() {
		return (Set<Entry<DetalleCorrienteKey, DetalleCorriente>>) this.getCache().entrySet();
	}
	
	public List<DetalleCorriente> getValues(String BT, Timestamp fcierre, UmicKey umicKey ){
		
		Filter allFilter = getFilter(BT, fcierre, umicKey);
		
		return getValuesByFilter(allFilter, Boolean.TRUE);
	}
	
	public List<DetalleCorriente> getMemberValues(String BT, Timestamp fcierre, UmicKey umicKey ) {
		Filter allFilter = getFilter(BT, fcierre, umicKey);
		
		Filter memberFilter = getPartitionMemberFilter(allFilter);
		
		return getValuesByFilter(memberFilter, Boolean.TRUE);
	}
	

	/**
	 * @param allFilter
	 * @return
	 */
	public List<DetalleCorriente> getValuesByFilter(Filter allFilter, Boolean ordered) {
		Set values = this.getCache().entrySet(allFilter, ordered ? fechaDesdeOrdered() : null);
		List<DetalleCorriente> lista = new ArrayList<DetalleCorriente>();
		
		Iterator iter = values.iterator();
		while(iter.hasNext()){
		    Map.Entry entry = (Map.Entry) iter.next();
		    lista.add((DetalleCorriente) entry.getValue());
		}
		
		return lista;
	}

	
	private ValueExtractor getMultiExtractor() {
		ValueExtractor[] ve = {fcierreExtractor, btExtractor, kmodalidadExtractor, kpolizaExtractor, ksubpolizaExtractor, kcertificadoExtractor,
				nordenExtractor, nsuscriExtractor, kprestacionExtractor, kgarantiaExtractor, kajusteExtractor, ctipoaportExtractor};
		
		return new MultiExtractor(ve);
	}

	/**
	 * @param BT
	 * @param fcierre
	 * @param umicKey
	 * @return
	 */
	public Filter getFilter(String BT, Timestamp fcierre, UmicKey umicKey) {
		/*Filter fcierreFilter = new EqualsFilter(fcierreExtractor, fcierre.getTime());
		Filter btFilter = new EqualsFilter(btExtractor, BT);
		Filter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor, umicKey.getKmodalidad());
		Filter kpolizaFilter = new EqualsFilter(kpolizaExtractor, umicKey.getKpoliza());
		Filter ksubpolizaFilter = new EqualsFilter(ksubpolizaExtractor, umicKey.getKsubpoliza());
		Filter kcertificadoFilter = new EqualsFilter(kcertificadoExtractor, umicKey.getKcertificado());
		Filter nordenFilter = new EqualsFilter(nordenExtractor, umicKey.getNorden());
		Filter nsuscriFilter = new EqualsFilter(nsuscriExtractor, umicKey.getNsuscri());		
		Filter kprestacionFilter = new EqualsFilter(kprestacionExtractor, umicKey.getKprestacion());
		Filter kgarantiaFilter = new EqualsFilter(kgarantiaExtractor, umicKey.getKgarantia());
		Filter kajusteFilter = new EqualsFilter(kajusteExtractor, umicKey.getKajuste());
		Filter ctipoaportFilter = new EqualsFilter(ctipoaportExtractor, umicKey.getCtipoaport());
		
		
		Filter[] filtrosArray = new Filter[]{fcierreFilter,btFilter,kmodalidadFilter,kpolizaFilter
				                             ,ksubpolizaFilter,kcertificadoFilter,nordenFilter,nsuscriFilter
				                             ,kprestacionFilter,kgarantiaFilter,kajusteFilter
				                             ,ctipoaportFilter};
		
		Filter allFilter = new AllFilter(filtrosArray);
		*/
		
		Filter allFilter = new EqualsFilter(getMultiExtractor(), Arrays.asList(fcierre.getTime(), BT, umicKey.getKmodalidad(), umicKey.getKpoliza(),
				umicKey.getKsubpoliza(), umicKey.getKcertificado(), umicKey.getNorden(), umicKey.getNsuscri(), umicKey.getKprestacion(), 
				umicKey.getKgarantia(), umicKey.getKajuste(), umicKey.getCtipoaport()));
		
		return allFilter;
	}
	
	private Comparator fechaDesdeOrdered() {
		return new Comparator<DetalleCorriente>() {

			@Override
			public int compare(DetalleCorriente dc1, DetalleCorriente dc2) {
				if (dc1.getFechaDesde() != null && dc2.getFechaDesde() != null) {
					return dc1.getFechaDesde().compareTo(dc2.getFechaDesde());
				}
				return 0;
			}
			
		};
	}
	
	
	@Override
	protected Comparator exportOrdered() {

		return new Comparator<DetalleCorrienteKey>() {

			@Override
			public int compare(DetalleCorrienteKey dc1, DetalleCorrienteKey dc2) {
				if (dc1 == null && dc2 != null) {
					return 1;
				} else if (dc2 == null) {
					return -1;
				} else if (dc1.getUmicKey() != null && dc2.getUmicKey() != null) {
					int comp = dc1.getUmicKey().compareTo(dc2.getUmicKey());
					
					if (comp == 0 && dc1.getFechaDesde() != null && dc2.getFechaDesde() != null) {
						comp =  dc1.getFechaDesde().compareTo(dc2.getFechaDesde());
						
						if (comp == 0 && dc1.getBt() != null && dc2.getBt() != null) {
							if(dc1.getBt().equals(dc2.getBt())){
								return 0;
							}else{
								return 1;
							}
							
						} else {
							return comp;
						}
					} else {
						return comp;
					}
				}
				
				return 0;
			}
			
		};
	}
	
	
	private ValueExtractor getMultiExtractorObtenerFiltroDetalles() {
		ValueExtractor[] ve = {kmodalidadExtractor, kpolizaExtractor, ksubpolizaExtractor, kcertificadoExtractor,
				nordenExtractor, nsuscriExtractor, kprestacionExtractor, kgarantiaExtractor,  kajusteExtractor, ctipoaportExtractor};
		
		return new MultiExtractor(ve);
	}
	
	
	public Filter obtenerFiltroDetalles(UmicKey umicKey){
				
//		Filter filtroTipo = null;
//
//		Filter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor, key.getKmodalidad());
//		Filter kpolizaFilter = new EqualsFilter(kpolizaExtractor, key.getKpoliza());
//		Filter ksubpolizaFilter = new EqualsFilter(ksubpolizaExtractor, key.getKsubpoliza());
//		Filter kcertificadoFilter = new EqualsFilter(kcertificadoExtractor, key.getKcertificado());
//		Filter nsuscriFilter = new EqualsFilter(nsuscriExtractor, key.getNsuscri());
//		Filter nordenFilter = new EqualsFilter(nordenExtractor, key.getNorden());
//		Filter kgarantiaFilter = new EqualsFilter(kgarantiaExtractor, key.getKgarantia());
//		Filter kprestacionFilter = new EqualsFilter(kprestacionExtractor, key.getKprestacion());
//		Filter kajusteFilter = new EqualsFilter(kajusteExtractor, key.getKajuste());
//		Filter ctipoaportFilter = new EqualsFilter(ctipoaportExtractor, key.getCtipoaport());
//		
//		Filter[] filtrosArray = new Filter[]{kmodalidadFilter,kpolizaFilter
//                ,ksubpolizaFilter,kcertificadoFilter,nordenFilter,nsuscriFilter
//                ,kprestacionFilter,kgarantiaFilter,kajusteFilter
//                ,ctipoaportFilter};
//		
//		filtroTipo = new AllFilter(filtrosArray);
//		
//		return filtroTipo;
		
		Filter allFilter = new EqualsFilter(getMultiExtractorObtenerFiltroDetalles(), Arrays.asList(umicKey.getKmodalidad(), umicKey.getKpoliza(),
				umicKey.getKsubpoliza(), umicKey.getKcertificado(), umicKey.getNorden(), umicKey.getNsuscri(), umicKey.getKprestacion(), umicKey.getKgarantia(),  
				umicKey.getKajuste(), umicKey.getCtipoaport()));
		
		return allFilter;
		
	}
	
	public void filtrarDetalles(Filter filter) {
		this.getCache().invokeAll(filter, new ConditionalRemove(AlwaysFilter.INSTANCE));
	}
		
	private Filter crearFiltroPolizasTipo() {
		
		PolizasTipoDao polizasTipoDao = new PolizasTipoDao();
		
		Collection<PolizasTipo> polizasTipo = polizasTipoDao.values();
		
		Filter anyFilter = null;
		
		List<Filter> filters = new ArrayList<Filter>();
		
		for (PolizasTipo polizaTipo : polizasTipo) {
			filters.add(crearFiltro(polizaTipo));
		}
		if (filters.size() > 0) {
			anyFilter = new AnyFilter((Filter []) filters.toArray(new Filter[0]));
		}
		
		return anyFilter;
	}
	
	
	@Override
	protected Filter exportFiltered() {
		Filter filtroPolizasTipo = null;
		// Comprobamos si hemos de exportar el detalle de forma distribuida o con un unico nodo
		Boolean exportarDetalleDistribuido = new BtUtils().getExportarDetalleDistribuido();
		
		// Descomentar el código posterior si hubiera detalles corriente no incluidos en el ptipo.
		// filtroPolizasTipo = this.crearFiltroPolizasTipo();
		
		if (exportarDetalleDistribuido) {
			// Además del filtro por negocio, se debe filtrar por partición
			return getPartitionMemberFilter(filtroPolizasTipo);
		} else {
			return filtroPolizasTipo;
		}
	}

	/**
	 * @param filtro
	 * @return
	 */
	public Filter getPartitionMemberFilter(Filter filtro) {
		PartitionSet partsMember = ((DistributedCacheService) getCache().getCacheService()).getOwnedPartitions(CacheFactory.getCluster().getLocalMember());
		return new PartitionedFilter(filtro != null ? filtro : AlwaysFilter.INSTANCE, partsMember);
	}
	
	private Filter crearFiltro(PolizasTipo pTipo) {
		
		Filter allFilter = null;
		
		if (pTipo != null) {
			/*						
			Filter cnegocioFilter = new EqualsFilter(cnegocioExtractor, umicKey.getKmodalidad());
			Filter ccanalFilter = new EqualsFilter(ccanalExtractor, umicKey.getKpoliza());
			Filter ccarteraFilter = new EqualsFilter(ccarteraExtractor, umicKey.getKsubpoliza());
			*/
			if (pTipo.getClaveUmic() != null) {
				Filter kmodalidadFilter = new EqualsFilter(kmodalidadExtractor, pTipo.getClaveUmic().getKmodalidad());
				Filter kpolizaFilter = new EqualsFilter(kpolizaExtractor, pTipo.getClaveUmic().getKpoliza());
				Filter ksubpolizaFilter = new EqualsFilter(ksubpolizaExtractor, pTipo.getClaveUmic().getKsubpoliza());
				Filter kcertificadoFilter = new EqualsFilter(kcertificadoExtractor, pTipo.getClaveUmic().getKcertificado());
				Filter nsuscriFilter = new EqualsFilter(nsuscriExtractor, pTipo.getClaveUmic().getNsuscri());
				Filter nordenFilter = new EqualsFilter(nordenExtractor, pTipo.getClaveUmic().getNorden());
				Filter kgarantiaFilter = new EqualsFilter(kgarantiaExtractor, pTipo.getClaveUmic().getKgarantia());
				Filter kprestacionFilter = new EqualsFilter(kprestacionExtractor, pTipo.getClaveUmic().getKprestacion());
				Filter kajusteFilter = new EqualsFilter(kajusteExtractor, pTipo.getClaveUmic().getKajuste());
				Filter ctipoaportFilter = new EqualsFilter(ctipoaportExtractor, pTipo.getClaveUmic().getCtipoaport());
				
				Filter[] filtrosArray = new Filter[]{kmodalidadFilter,kpolizaFilter,ksubpolizaFilter
						,kcertificadoFilter,nsuscriFilter,nordenFilter,kgarantiaFilter,kprestacionFilter
						,kajusteFilter,ctipoaportFilter};
				
				allFilter = new AllFilter(filtrosArray);
			}
		}
		return allFilter;
	}	
	
	
}