package es.mapfre.solvencia.dao.impl.gbt;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tangosol.io.pof.annotation.Portable;
import com.tangosol.util.Filter;
import com.tangosol.util.ValueExtractor;
import com.tangosol.util.filter.AllFilter;
import com.tangosol.util.filter.EqualsFilter;
import com.tangosol.util.filter.GreaterEqualsFilter;
import com.tangosol.util.filter.LessEqualsFilter;

import es.mapfre.solvencia.coherence.keys.gbt.AdapTablaExpKey;
import es.mapfre.solvencia.dao.DaoBase;
import es.mapfre.solvencia.dominio.gbt.AdapTablaExp;

@Portable
public class AdapTablaExpDao extends DaoBase implements Map<AdapTablaExpKey, AdapTablaExp> {
	private static final String CACHE_NAME = "ATE0"; 

	private final ValueExtractor ktipobtExtractor;
	private final ValueExtractor kmodalidadExtractor;
	private final ValueExtractor finiExtractor;
	private final ValueExtractor ffinExtractor;
	
	public AdapTablaExpDao() {
		super();
		super.setCacheName(CACHE_NAME);
		
		ktipobtExtractor = createExtractor("getKtipobt", String.class, AdapTablaExp.IND_KTIPOBT);
		kmodalidadExtractor = createExtractor("getKmodalidad", Integer.class, AdapTablaExp.IND_KMODALIDAD);
		finiExtractor = createExtractor("getFini", Timestamp.class, AdapTablaExp.IND_FINI);
		ffinExtractor = createExtractor("getFfin", Timestamp.class, AdapTablaExp.IND_FFIN);
	}

	@Override
	public Set<java.util.Map.Entry<AdapTablaExpKey, AdapTablaExp>> entrySet() {
		return (Set<Entry<AdapTablaExpKey, AdapTablaExp>>) this.getCache().entrySet();
	}

	@Override
	public AdapTablaExp get(Object key) {
		if (!(key instanceof AdapTablaExpKey)) {
			return null;
		} else {
			return (AdapTablaExp) this.getCache().get(key);
		}
	}

	@Override
	public Set<AdapTablaExpKey> keySet() {
		return this.getCache().keySet();
	}

	@Override
	public AdapTablaExp put(AdapTablaExpKey arg0, AdapTablaExp arg1) {
		this.getCache().put(arg0, arg1);
		return arg1;
	}

	@Override
	public AdapTablaExp remove(Object arg0) {
		return (AdapTablaExp) this.getCache().remove(arg0);
	}

	@Override
	public Collection<AdapTablaExp> values() {
		return (Collection<AdapTablaExp>) this.getCache().values();
	}
	public List<AdapTablaExp> obtenerAdapTablasExp(String ktipobt, Integer kmodalidad, Timestamp fcierre)  {
		
		Filter allFilter = null;
		
		List<Filter> filtros = new ArrayList<Filter>();
		
		filtros.add(new EqualsFilter(ktipobtExtractor, ktipobt));
		filtros.add(new EqualsFilter(kmodalidadExtractor, kmodalidad));
		filtros.add(new LessEqualsFilter(finiExtractor, fcierre.getTime()));
		filtros.add(new GreaterEqualsFilter(ffinExtractor, fcierre.getTime()));
		
		Filter[] arrayFiltros = filtros.toArray(new Filter[filtros.size()]);
		
		allFilter = new AllFilter(arrayFiltros);
		
		List<AdapTablaExp> tablasEx = getTablasExp(allFilter);
		
		return tablasEx;
	}
	
	private Filter reducirFiltro (Filter[] filter, int contador) {
		
		Filter filtroReducido = null;
		
		Filter[] arrayReducido = new Filter[filter.length-contador];
		
		System.arraycopy(filter, 0, arrayReducido, 0, filter.length-contador);
		
		filtroReducido = new AllFilter(arrayReducido);
		
		return filtroReducido;
	}
	
	public List<AdapTablaExp> getTablasExp(Filter allFilter) {
		
		List<AdapTablaExp> tablasExp=new ArrayList<AdapTablaExp>();
		
		Set values = this.getCache().entrySet(allFilter);
		
			Iterator iter = values.iterator();
			while(iter.hasNext()){
			    Map.Entry entry = (Map.Entry) iter.next();
			    tablasExp.add((AdapTablaExp) entry.getValue());
			}
		
		return tablasExp;
	}

}
