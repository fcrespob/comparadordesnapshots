package es.mapfre.solvencia.dao.impl.gbt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.TasasAnulacionKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.conversionesBel.TablasExperienciaReales;
import es.mapfre.solvencia.dominio.gbt.TasasAnulacion;

public class TasasAnulacionDao extends DaoBase implements Map<TasasAnulacionKey, TasasAnulacion> {

	private static final String CACHE_NAME = "VTA0";
	
	private final ValueExtractor ktablaanuExtractor;
	private final ValueExtractor kfcierreExtractor;
	
	public TasasAnulacionDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ktablaanuExtractor = createExtractor("getKtablaanu", String.class, TasasAnulacion.IND_KTABLAANU);
		kfcierreExtractor = createExtractor("getKfcierre", Timestamp.class, TasasAnulacion.IND_KFCIERRE);
	}

	@Override
	public Set<java.util.Map.Entry<TasasAnulacionKey, TasasAnulacion>> entrySet() {
		return (Set<Entry<TasasAnulacionKey, TasasAnulacion>>) this.getCache().entrySet();
	}

	@Override
	public TasasAnulacion get(Object key) {
		if (!(key instanceof TasasAnulacionKey)) {
			return null;
		} else {
			return (TasasAnulacion) this.getCache().get(key);
		}
	}

	@Override
	public Set<TasasAnulacionKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public TasasAnulacion put(TasasAnulacionKey key, TasasAnulacion value) {
		this.getCache().put(key, value);
		return value;
	}

	@Override
	public TasasAnulacion remove(Object key) {
		return (TasasAnulacion) this.getCache().remove(key);
	}

	@Override
	public Collection<TasasAnulacion> values() {
		return (Collection<TasasAnulacion>) this.getCache().values();
	}
	
	/*
	public List<TasasAnulacion> obtenerTasasAnul(String ktablaanu, String kfcierre, Integer kmesesdesde)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(ktablaanuExtractor, ktablaanu));
		filtros.add(new EqualsFilter(kfcierreExtractor, kfcierre));
		filtros.add(new EqualsFilter(kmesesdesdeExtractor, kmesesdesde));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<TasasAnulacion> tasaAnul = getTasasAnul(allFilter);
		
		return tasaAnul;
	}
	*/
	
	public List<TasasAnulacion> obtenerTasasAnul(String ktablaanu, Timestamp kfcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(ktablaanuExtractor, ktablaanu));
		filtros.add(new LessEqualsFilter(kfcierreExtractor,	kfcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<TasasAnulacion> tasaAnul = getTasasAnul(allFilter);
		
		return tasaAnul;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<TasasAnulacion> getTasasAnul(Filter allFilter) {
		
		List<TasasAnulacion> tasAnul=new ArrayList<TasasAnulacion>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tasAnul.add((TasasAnulacion) entry.getValue());
			}
			
			
		//Ordenamos por fecha
		Collections.sort(tasAnul, Collections.reverseOrder(new Comparator<TasasAnulacion>(){
			@Override
			public int compare(TasasAnulacion o1, TasasAnulacion o2) {
				return o1.getKfcierre().compareTo(o2.getKfcierre());
			}
		}));
		
		return tasAnul;
	}
}
