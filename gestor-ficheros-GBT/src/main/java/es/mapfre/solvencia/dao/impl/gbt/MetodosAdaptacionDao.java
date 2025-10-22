package es.mapfre.solvencia.dao.impl.gbt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.MetodosAdaptacionKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.MetodosAdaptacion;

public class MetodosAdaptacionDao extends DaoBase implements Map<MetodosAdaptacionKey, MetodosAdaptacion> {
	private static final String CACHE_NAME = "MVI0";
	
	private final ValueExtractor kmadaptacExtractor;
	private final ValueExtractor kfinicioExtractor;
	private final ValueExtractor ffinExtractor;
	
	public MetodosAdaptacionDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		kmadaptacExtractor = createExtractor("getKmadaptac", String.class, MetodosAdaptacion.IND_KMADAPTAC);
		kfinicioExtractor = createExtractor("getKfinicio", Timestamp.class, MetodosAdaptacion.IND_KFINICIO);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, MetodosAdaptacion.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<MetodosAdaptacionKey, MetodosAdaptacion>> entrySet() {
		return (Set<Entry<MetodosAdaptacionKey, MetodosAdaptacion>>) this.getCache().entrySet();
	}

	@Override
	public MetodosAdaptacion get(Object key) {
		if (!(key instanceof MetodosAdaptacionKey)) {
			return null;
		} else {
			return (MetodosAdaptacion) this.getCache().get(key);
		}
	}

	@Override
	public Set<MetodosAdaptacionKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public MetodosAdaptacion put(MetodosAdaptacionKey arg0,
			MetodosAdaptacion arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public MetodosAdaptacion remove(Object arg0) {
		return (MetodosAdaptacion) this.getCache().remove(arg0);
	}

	@Override
	public Collection<MetodosAdaptacion> values() {
		return (Collection<MetodosAdaptacion>) this.getCache().values();
	}
	
	public List<MetodosAdaptacion> obtenerMetAdaptacion(String kmadaptac, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(kmadaptacExtractor, kmadaptac));
		filtros.add(new LessEqualsFilter(kfinicioExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<MetodosAdaptacion> metAd = getMetAdap(allFilter);
		
		return metAd;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<MetodosAdaptacion> getMetAdap(Filter allFilter) {
		
		List<MetodosAdaptacion> metAd=new ArrayList<MetodosAdaptacion>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    metAd.add((MetodosAdaptacion) entry.getValue());
			}
		
		return metAd;
	}
}
